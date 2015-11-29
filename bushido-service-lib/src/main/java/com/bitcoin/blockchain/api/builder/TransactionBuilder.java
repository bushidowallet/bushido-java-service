package com.bitcoin.blockchain.api.builder;

import com.bccapi.bitlib.StandardTransactionBuilder;
import com.bccapi.bitlib.crypto.InMemoryPrivateKey;
import com.bccapi.bitlib.crypto.PrivateKeyRing;
import com.bccapi.bitlib.crypto.PublicKey;
import com.bccapi.bitlib.crypto.PublicKeyRing;
import com.bccapi.bitlib.model.*;
import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.domain.TransactionOutput;
import com.bushidowallet.core.bitcoin.bip32.Derivation;
import com.bushidowallet.core.bitcoin.bip32.ExtendedKey;
import com.bushidowallet.core.bitcoin.bip32.Hash;
import com.bushidowallet.core.bitcoin.bip32.Seed;
import com.bushidowallet.core.crypto.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jesion on 2015-03-17.
 */
public class TransactionBuilder {

    private Map<String, TransactionOutputList> utxo;
    private List<SpendDescriptor> spendings;
    private V2Key changeKey;
    private Balance balance;
    private String seed;
    private boolean compressedPubKeys;
    private long fee;

    public TransactionBuilder(Map<String, TransactionOutputList> utxo,
                              Balance balance,
                              List<SpendDescriptor> spendings,
                              V2Key changeKey,
                              String seed,
                              boolean compressedPubKeys,
                              long fee) {
        this.utxo = utxo;
        this.spendings = spendings;
        this.changeKey = changeKey;
        this.balance = balance;
        this.seed = seed;
        this.compressedPubKeys = compressedPubKeys;
        this.fee = fee;
    }

    public String build() throws Exception {
        Address changeAddress = Address.fromString(changeKey.address);
        StandardTransactionBuilder builder = new StandardTransactionBuilder(NetworkParameters.productionNetwork);
        for (int s = 0; s < spendings.size(); s++) {
            builder.addOutput(Address.fromString(spendings.get(s).receivingAddress), spendings.get(s).value);
        }
        PublicKeyRing pubKeys = new PublicKeyRing();
        PrivateKeyRing privKeys = new PrivateKeyRing();
        TransactionOutputList pendingUTXOs = utxo.get(TransactionStatus.PENDING);
        TransactionOutputList confirmedUTXOs = utxo.get(TransactionStatus.CONFIRMED);
        ExtendedKey root = getRootKey(seed);
        List<UnspentTransactionOutput> unspents = new ArrayList<UnspentTransactionOutput>();
        UTXOBuilder utxoBuilder = new UTXOBuilder();
        for (int i = 0; i < pendingUTXOs.outputs.size(); i++) {
            TransactionOutput to = pendingUTXOs.outputs.get(i);
            pubKeys.addPublicKey(new PublicKey(ByteUtil.fromHex(to.getKey().publicKeyBytes)), NetworkParameters.productionNetwork);
            ExtendedKey privKey = getDerivedKey(root, to.getKey().account, to.getKey().sequence);
            privKeys.addPrivateKey(new InMemoryPrivateKey(privKey.getWIF(), NetworkParameters.productionNetwork), NetworkParameters.productionNetwork);
            unspents.add(utxoBuilder.build(to.getTxHash(), 0, to.getScriptBytes(), to.getIndex(), to.getValue()));
        }
        for (int j = 0; j < confirmedUTXOs.outputs.size(); j++) {
            TransactionOutput to = confirmedUTXOs.outputs.get(j);
            pubKeys.addPublicKey(new PublicKey(ByteUtil.fromHex(to.getKey().publicKeyBytes)), NetworkParameters.productionNetwork);
            ExtendedKey privKey = getDerivedKey(root, to.getKey().account, to.getKey().sequence);
            privKeys.addPrivateKey(new InMemoryPrivateKey(privKey.getWIF(), NetworkParameters.productionNetwork), NetworkParameters.productionNetwork);
            unspents.add(utxoBuilder.build(to.getTxHash(), 1, to.getScriptBytes(), to.getIndex(), to.getValue()));
        }
        StandardTransactionBuilder.UnsignedTransaction ut = builder.createUnsignedTransaction(unspents, changeAddress, fee, pubKeys, NetworkParameters.productionNetwork);
        StandardTransactionBuilder.SigningRequest[] s = ut.getSignatureInfo();
        List<byte[]> signatures = StandardTransactionBuilder.generateSignatures(s, privKeys);
        com.bccapi.bitlib.model.Transaction tx = StandardTransactionBuilder.finalizeTransaction(ut, signatures);
        return ByteUtil.toHex(tx.toBytes());
    }

    private ExtendedKey getRootKey(String seed) throws Exception {
        byte[] passphraseHash = ByteUtil.fromHex(seed);
        byte[] keyHash = new Hash(passphraseHash).getHmacSHA512(Seed.BITCOIN_SEED);
        return new ExtendedKey(keyHash, compressedPubKeys);
    }

    private ExtendedKey getDerivedKey(ExtendedKey root,
                                      int account,
                                      int sequence) throws Exception {
        Derivation d = new Derivation(root);
        return d.accountKey(0, account, sequence);
    }
}
