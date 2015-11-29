package com.bitcoin.blockchain.api.service.v2wallet;

import com.bitcoin.blockchain.api.domain.PersistedV2Key;
import com.bitcoin.blockchain.api.domain.V2Key;
import com.bitcoin.blockchain.api.domain.V2WalletDescriptor;
import com.bitcoin.blockchain.api.domain.V2WalletSetting;
import com.bitcoin.blockchain.api.persistence.V2WalletKeyDAO;
import com.bushidowallet.core.bitcoin.bip32.Derivation;
import com.bushidowallet.core.bitcoin.bip32.ExtendedKey;
import com.bushidowallet.core.bitcoin.bip32.Hash;
import com.bushidowallet.core.bitcoin.bip32.Seed;
import com.bushidowallet.core.crypto.util.ByteUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Security;
import java.util.List;

/**
 * Created by Jesion on 2015-01-21.
 */
public class V2Keygen {

    private V2WalletDescriptor wallet;

    @Autowired
    private V2WalletKeyDAO keyDAO;

    private Derivation derivation;

    public V2Keygen() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public void setWallet(V2WalletDescriptor wallet) {
        this.wallet = wallet;
    }

    public void init() throws Exception {
        V2WalletSetting pass = wallet.getSetting(V2WalletSetting.PASSPHRASE);
        V2WalletSetting compressed = wallet.getSetting(V2WalletSetting.COMPRESSED_KEYS);
        boolean compressedKeys = (Boolean) compressed.value;
        byte[] keyHash = new Hash(ByteUtil.fromHex((String) pass.value)).getHmacSHA512(Seed.BITCOIN_SEED);
        ExtendedKey root = new ExtendedKey(keyHash, compressedKeys);
        derivation = new Derivation(root);
    }

    public synchronized V2Key getKey(int account) throws Exception {
        int sequence = keyDAO.getNextAvailableSequence(wallet.getKey(), account);
        ExtendedKey coreKey = derivation.accountKey(0, account, sequence);
        long creationTimeMillis = System.currentTimeMillis();
        PersistedV2Key key = new PersistedV2Key(wallet.getKey(),
                coreKey.serializePublic(),
                ByteUtil.toHex(coreKey.getPublic()),
                coreKey.getAddress().toString(),
                account,
                sequence,
                creationTimeMillis
        );
        keyDAO.logKey(key);
        return key;
    }

    public List<PersistedV2Key> getKeys(int account) {
        return keyDAO.getKeys(this.wallet.getKey(), account);
    }

    public List<PersistedV2Key> getKeys(List<String> ids) {
        return keyDAO.getKeys(ids);
    }
}
