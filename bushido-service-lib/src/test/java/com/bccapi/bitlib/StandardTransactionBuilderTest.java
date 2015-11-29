package com.bccapi.bitlib;

import com.bccapi.bitlib.crypto.InMemoryPrivateKey;
import com.bccapi.bitlib.crypto.PrivateKeyRing;
import com.bccapi.bitlib.crypto.PublicKey;
import com.bccapi.bitlib.crypto.PublicKeyRing;
import com.bccapi.bitlib.model.Address;
import com.bccapi.bitlib.model.NetworkParameters;
import com.bccapi.bitlib.model.Transaction;
import com.bccapi.bitlib.model.UnspentTransactionOutput;
import com.bitcoin.blockchain.api.builder.UTXOBuilder;
import com.bushidowallet.core.bitcoin.bip32.Derivation;
import com.bushidowallet.core.bitcoin.bip32.ExtendedKey;
import com.bushidowallet.core.bitcoin.bip32.Hash;
import com.bushidowallet.core.bitcoin.bip32.Seed;
import com.bushidowallet.core.crypto.util.ByteUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesion on 2015-03-16.
 */
public class StandardTransactionBuilderTest {

    @BeforeClass
    public static void init()
    {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Work in progress..
     * For now its just demonstrating the BCCAPI use flow to construct and sign transaction...
     */

    /*
        SEED: crazy horse battery staple 7

        "outputs": [
        {
            "key": {
                    "walletKey": "robswallet7",
                    "publicKey": "xpub6D9J3VCrBNwrMy7Ea4k3UKnv3QX6geyWWsyAojq41Yxvkr6LUbveN76KQq9saL55XETzxM9VdjTuwfmrzWEfBLe3KerqdTqA915JLa9LrEm",
                    "publicKeyBytes": "02e60644505994462e28db61c8f2d5fee4ec727956a95fe68fd057ccda2feade7f",
                    "address": "1GjzJfKEJaGFNTXqvPqDicaRt9GRd8U9QC",
                    "account": 0,
                    "sequence": 369,
                    "creationTimeMillis": 1426520228128
            },
            "value": 10000,
            "toAddress": "1GjzJfKEJaGFNTXqvPqDicaRt9GRd8U9QC",
            "script": "DUP HASH160 PUSHDATA(20)[acab3149e11c16e8517206a682d09e706a963597] EQUALVERIFY CHECKSIG",
            "scriptBytes": "76a914acab3149e11c16e8517206a682d09e706a96359788ac",
            "txHash": "ec0c8f8406fe287826590f7f8a47879151a7cb12e419eacada08eacb8ff55e20",
            "index": 0
        },
        {
            "key": {
                    "walletKey": "robswallet7",
                    "publicKey": "xpub6D9J3VCrBNwrrcFwvbd5ZBXieajVsgaEHDnqWS5th9X89A6zosuothgnNc7pJbVvG6Y9944RtHrmX4as4mcseL56u59axufVR9DQsyBoPBL",
                    "publicKeyBytes": "0358b6e085af1ea01ce4730d85be18463e8efe89c0e30d6099058c47df90a38f2e",
                    "address": "16PjrYtcG1nXnHG8EshVPVzKhhcuS3J2yb",
                    "account": 0,
                    "sequence": 380,
                    "creationTimeMillis": 1426533292234
            },
            "value": 500000,
            "toAddress": "16PjrYtcG1nXnHG8EshVPVzKhhcuS3J2yb",
            "script": "DUP HASH160 PUSHDATA(20)[3b258d94d8eba029a9fcb3107e2e17a43beeb1dc] EQUALVERIFY CHECKSIG",
            "scriptBytes": "76a9143b258d94d8eba029a9fcb3107e2e17a43beeb1dc88ac",
            "txHash": "33ea628f6cb44d4cf72e4fe1b8d8946a735120830d52ae29598f8d113264112e",
            "index": 0
        }
    */

    @Test
    public void testBuildUnsignedTx() throws Exception {

        StandardTransactionBuilder builder = new StandardTransactionBuilder(NetworkParameters.productionNetwork);
        builder.addOutput(Address.fromString("1KxRfiqcNi2GbpdN3pzuQHgewShmeNW9g1"), 5000);
        Address changeAddress = Address.fromString("1Azyem1HhD94XrsJkPGNKr6T3uQB3sfjkz");

        PublicKeyRing pubKeys = new PublicKeyRing();
        pubKeys.addPublicKey(new PublicKey(ByteUtil.fromHex("02e60644505994462e28db61c8f2d5fee4ec727956a95fe68fd057ccda2feade7f")), NetworkParameters.productionNetwork);

        List<UnspentTransactionOutput> unspents = new ArrayList<UnspentTransactionOutput>();
        UTXOBuilder utxoBuilder = new UTXOBuilder();
        UnspentTransactionOutput utxo = utxoBuilder.build("ec0c8f8406fe287826590f7f8a47879151a7cb12e419eacada08eacb8ff55e20", 12, "76a914acab3149e11c16e8517206a682d09e706a96359788ac", 0, 100000);
        unspents.add(utxo);

        StandardTransactionBuilder.UnsignedTransaction ut = builder.createUnsignedTransaction(unspents, changeAddress, pubKeys, NetworkParameters.productionNetwork);

        System.out.println("Created an unsigned transaction: " + ut.toString());
        Assert.assertNotNull(ut);

        StandardTransactionBuilder.SigningRequest[] signingRequests = ut.getSignatureInfo();
        boolean isCompressed = signingRequests[0].publicKey.isCompressed();

        System.out.println("Pub key compressed: " + isCompressed);
        //The wallet we are working on in this test case is using compressed public keys, so we shall assert that.
        Assert.assertTrue(isCompressed);
    }

    @Test
    public void testBuildSignedTx() throws Exception {

        StandardTransactionBuilder builder = new StandardTransactionBuilder(NetworkParameters.productionNetwork);
        builder.addOutput(Address.fromString("1KxRfiqcNi2GbpdN3pzuQHgewShmeNW9g1"), 5000);
        Address changeAddress = Address.fromString("1Azyem1HhD94XrsJkPGNKr6T3uQB3sfjkz");

        PublicKeyRing pubKeys = new PublicKeyRing();
        pubKeys.addPublicKey(new PublicKey(ByteUtil.fromHex("02e60644505994462e28db61c8f2d5fee4ec727956a95fe68fd057ccda2feade7f")), NetworkParameters.productionNetwork);
        pubKeys.addPublicKey(new PublicKey(ByteUtil.fromHex("0358b6e085af1ea01ce4730d85be18463e8efe89c0e30d6099058c47df90a38f2e")), NetworkParameters.productionNetwork);
        PrivateKeyRing privateKeys = new PrivateKeyRing();

        ExtendedKey root = getRootKey("crazy horse battery staple 7");
        ExtendedKey key1 = getDerivedKey(root, 0, 369);
        ExtendedKey key2 = getDerivedKey(root, 0, 380);
        privateKeys.addPrivateKey(new InMemoryPrivateKey(key1.getWIF(), NetworkParameters.productionNetwork), NetworkParameters.productionNetwork);
        privateKeys.addPrivateKey(new InMemoryPrivateKey(key2.getWIF(), NetworkParameters.productionNetwork), NetworkParameters.productionNetwork);

        List<UnspentTransactionOutput> unspents = new ArrayList<UnspentTransactionOutput>();
        UTXOBuilder utxoBuilder = new UTXOBuilder();
        UnspentTransactionOutput utxo1 = utxoBuilder.build("ec0c8f8406fe287826590f7f8a47879151a7cb12e419eacada08eacb8ff55e20", 12, "76a914acab3149e11c16e8517206a682d09e706a96359788ac", 0, 10000);
        UnspentTransactionOutput utxo2 = utxoBuilder.build("33ea628f6cb44d4cf72e4fe1b8d8946a735120830d52ae29598f8d113264112e", 5, "76a9143b258d94d8eba029a9fcb3107e2e17a43beeb1dc88ac", 0, 500000);
        unspents.add(utxo1);
        unspents.add(utxo2);

        StandardTransactionBuilder.UnsignedTransaction ut = builder.createUnsignedTransaction(unspents, changeAddress, pubKeys, NetworkParameters.productionNetwork);
        StandardTransactionBuilder.SigningRequest[] s = ut.getSignatureInfo();
        List<byte[]> signatures = StandardTransactionBuilder.generateSignatures(s, privateKeys);
        Transaction tx = StandardTransactionBuilder.finalizeTransaction(ut, signatures);

        System.out.println("Signed transaction: " + tx.toString());

        Assert.assertNotNull(ut);
        Assert.assertNotNull(tx);

        String txHex = ByteUtil.toHex(tx.toBytes());

        System.out.println("TxHex:" + txHex);
    }

    /*

    UTXOs:

    {
      "errors": null,
      "payload": {
        "Confirmed": {
          "status": "Confirmed",
          "outputs": [],
          "total": 0
        },
        "Pending": {
          "status": "Pending",
          "outputs": [
            {
              "key": {
                "walletKey": "robswallet",
                "publicKey": "xpub6D4dUctownVE6J7sS9QzZs3nbFt7ihxVUet2sugqnP1GHX2r7beCJ7L66eCcj25HakcEdXxCLcpJqwdksW34XnWvS9vxgV2z5nixNQJvnsy",
                "publicKeyBytes": "0231a7b9edb4e893117563d9ba74a24a7ba366235b3b71b3620bdae2d2045f5625",
                "address": "1LvBUDJmDGf7z6BNnWvECJnWguxxzP6DBH",
                "account": 0,
                "sequence": 3,
                "creationTimeMillis": 1426598218817
              },
              "value": 40000,
              "toAddress": "1LvBUDJmDGf7z6BNnWvECJnWguxxzP6DBH",
              "script": "DUP HASH160 PUSHDATA(20)[da792cd2f8cc81db3ccee87353b82538ca7b4128] EQUALVERIFY CHECKSIG",
              "scriptBytes": "76a914da792cd2f8cc81db3ccee87353b82538ca7b412888ac",
              "txHash": "7238e843acd0e659358d8f60a491f48c401852ce1ea37c78c4817e121c3787f0",
              "index": 0
            },
            {
              "key": {
                "walletKey": "robswallet",
                "publicKey": "xpub6D4dUctownVE9BVNe4XjhCQVbYLEZCFuJbhmsxBLJ11aNoS2gmzHCT1uBtv28Dk141k3XmdJYu7SFWzCnx7pPxUAJiP38yje15oZXVwwE6X",
                "publicKeyBytes": "0290fd099b044b4fb90c5c583582335e66d5390c3e30adcd553edb9b95fa493a68",
                "address": "1DN3d1vTLybicLT1vS1yEY6Ty9fnuv2cL8",
                "account": 0,
                "sequence": 4,
                "creationTimeMillis": 1426598360810
              },
              "value": 50000,
              "toAddress": "1DN3d1vTLybicLT1vS1yEY6Ty9fnuv2cL8",
              "script": "DUP HASH160 PUSHDATA(20)[879c6eb68f9335047d81c64646e0ebb425e171b9] EQUALVERIFY CHECKSIG",
              "scriptBytes": "76a914879c6eb68f9335047d81c64646e0ebb425e171b988ac",
              "txHash": "f00ca9a9a15dfa21e40c0d94444f44203645d71a6fa514508d19c3387329e0e8",
              "index": 0
            }
          ],
          "total": 90000
        }
      }
    }

    */

    @Test
    public void testSpend() throws Exception {
        long totalUTXO = 90000;
        Address changeAddress = Address.fromString("1L4Cj5LJjKJNadUm67XBXPFP2XgKaKYbF9");
        long sendToPhone = 55000;
        StandardTransactionBuilder builder = new StandardTransactionBuilder(NetworkParameters.productionNetwork);
        builder.addOutput(Address.fromString("1KxRfiqcNi2GbpdN3pzuQHgewShmeNW9g1"), sendToPhone);

        PublicKeyRing pubKeys = new PublicKeyRing();
        pubKeys.addPublicKey(new PublicKey(ByteUtil.fromHex("0231a7b9edb4e893117563d9ba74a24a7ba366235b3b71b3620bdae2d2045f5625")), NetworkParameters.productionNetwork);
        pubKeys.addPublicKey(new PublicKey(ByteUtil.fromHex("0290fd099b044b4fb90c5c583582335e66d5390c3e30adcd553edb9b95fa493a68")), NetworkParameters.productionNetwork);
        PrivateKeyRing privateKeys = new PrivateKeyRing();

        ExtendedKey root = getRootKey("Recent scholarship in both Japan and abroad has focused on differences between the samurai class and the bushido theories that developed in modern Japan");
        ExtendedKey key1 = getDerivedKey(root, 0, 3);
        ExtendedKey key2 = getDerivedKey(root, 0, 4);
        privateKeys.addPrivateKey(new InMemoryPrivateKey(key1.getWIF(), NetworkParameters.productionNetwork), NetworkParameters.productionNetwork);
        privateKeys.addPrivateKey(new InMemoryPrivateKey(key2.getWIF(), NetworkParameters.productionNetwork), NetworkParameters.productionNetwork);

        List<UnspentTransactionOutput> unspents = new ArrayList<UnspentTransactionOutput>();
        UTXOBuilder utxoBuilder = new UTXOBuilder();
        UnspentTransactionOutput utxo1 = utxoBuilder.build("7238e843acd0e659358d8f60a491f48c401852ce1ea37c78c4817e121c3787f0", 12, "76a914da792cd2f8cc81db3ccee87353b82538ca7b412888ac", 0, 40000);
        UnspentTransactionOutput utxo2 = utxoBuilder.build("f00ca9a9a15dfa21e40c0d94444f44203645d71a6fa514508d19c3387329e0e8", 5, "76a914879c6eb68f9335047d81c64646e0ebb425e171b988ac", 0, 50000);
        unspents.add(utxo1);
        unspents.add(utxo2);

        StandardTransactionBuilder.UnsignedTransaction ut = builder.createUnsignedTransaction(unspents, changeAddress, pubKeys, NetworkParameters.productionNetwork);
        StandardTransactionBuilder.SigningRequest[] s = ut.getSignatureInfo();
        List<byte[]> signatures = StandardTransactionBuilder.generateSignatures(s, privateKeys);
        Transaction tx = StandardTransactionBuilder.finalizeTransaction(ut, signatures);

        System.out.println("Unsigned transaction: " + ut.toString());
        System.out.println("Signed transaction: " + tx.toString());

        Assert.assertNotNull(ut);
        Assert.assertNotNull(tx);

        String txHex = ByteUtil.toHex(tx.toBytes());

        System.out.println("TxHex:" + txHex);

        /*

        Unsigned transaction: Fee: 0.0002
        1DN3d1vTLybicLT1vS1yEY6Ty9fnuv2cL8      (0.0005) ->   1KxRfiqcNi2GbpdN3pzuQHgewShmeNW9g1     (0.00055)
        1LvBUDJmDGf7z6BNnWvECJnWguxxzP6DBH      (0.0004) ->   1L4Cj5LJjKJNadUm67XBXPFP2XgKaKYbF9     (0.00015)

        Signed transaction: d392f53487abf56078d1440df9f36fe5a9ac06a12f06e0e677e7e99aba90ab2b in: 2 out: 2
        TxHex:0100000002e8e0297338c3198d5014a56f1ad7453620444f44940d0ce421fa5da1a9a90cf0000000006b48304502207fb4e9c4829c757335db69c729875143c6f498b18de7eb222e7bc6b8793cb774022100dde6460348e6828ce7e0699ba6031b5cd2bd3c63b1b71af6c80610577e2cdce501210290fd099b044b4fb90c5c583582335e66d5390c3e30adcd553edb9b95fa493a68fffffffff087371c127e81c4787ca31ece5218408cf491a4608f8d3559e6d0ac43e83872000000006a473044022002703a5db313270f84d99e3805640f4f3d2f93cf38c2298b0a4d7ba0edab350a022036495caf8860af91b60d31ab578ce8159078ad0a77acaf25dd02f29a5fbc898c01210231a7b9edb4e893117563d9ba74a24a7ba366235b3b71b3620bdae2d2045f5625ffffffff02d8d60000000000001976a914cfedbbc5fc5fd9665b548a66bdade69ca5d9ce2188ac983a0000000000001976a914d1056cf8b2328a939961fb3367c42e6ca21f913a88ac00000000

        */

        long balance = 90000 - 55000 - ut.calculateFee();

        System.out.println("Balance: " + balance);
    }

    /*

    UTXo's;

        {
          "errors": null,
          "payload": {
            "Confirmed": {
              "status": "Confirmed",
              "outputs": [],
              "total": 0
            },
            "Pending": {
              "status": "Pending",
              "outputs": [
                {
                  "key": {
                    "walletKey": "robswallet",
                    "publicKey": "xpub6Cig5xNQWZkDtG5p3BJ95uNAX8v3iAKhDgoBidXLrcLBmTPrvwfZdmKqXpCpNrwbHVFmXG8Qdmzpoy9PC53e5RCJZsrrBPQg8yuWv28ADhE",
                    "publicKeyBytes": "02f1c480a69fb512e302e3aa8f47aef69ec6abfff5485049e828723151e64add36",
                    "address": "1KW7BZnNVadr2BfX3zS7YomWKjcT2wrAW5",
                    "account": 0,
                    "sequence": 1,
                    "creationTimeMillis": 1426610262902
                  },
                  "value": 40000,
                  "toAddress": "1KW7BZnNVadr2BfX3zS7YomWKjcT2wrAW5",
                  "script": "DUP HASH160 PUSHDATA(20)[caf37cd3c22adb4cd8e542c2c21d88378f3c706b] EQUALVERIFY CHECKSIG",
                  "scriptBytes": "76a914caf37cd3c22adb4cd8e542c2c21d88378f3c706b88ac",
                  "txHash": "482f983a2edbc042cc6c8112ab6e453442dbf5a167e7a808bdad83abdf4035a0",
                  "index": 0
                },
                {
                  "key": {
                    "walletKey": "robswallet",
                    "publicKey": "xpub6Cig5xNQWZkDwPb3qArwQkomRRrT6krMFgUG5LGEPgBumQZrZL8BTMuHctpdNG7etoMX8n6bTSi4iwH7RJ33P1S92vXfYcSZmtBfabfqc3o",
                    "publicKeyBytes": "020772dd6a3de0419e32cbf9728bf848c27576774df1277d6988b9285c5b5e2f57",
                    "address": "12NC1txkoWPfi1VfCBpfNsCKNhVetn5u6a",
                    "account": 0,
                    "sequence": 2,
                    "creationTimeMillis": 1426610307040
                  },
                  "value": 50000,
                  "toAddress": "12NC1txkoWPfi1VfCBpfNsCKNhVetn5u6a",
                  "script": "DUP HASH160 PUSHDATA(20)[0efa0e63f6f9215d0baf11271c57f07deb2e91d3] EQUALVERIFY CHECKSIG",
                  "scriptBytes": "76a9140efa0e63f6f9215d0baf11271c57f07deb2e91d388ac",
                  "txHash": "29d31193859eb01e6f33e317f41d9d99ad627541dfd98ce5a47199a67bb798b4",
                  "index": 0
                }
              ],
              "total": 90000
            }
          }
        }

     */

    @Test
    public void testSpendWitchChangeHandling() throws Exception {
        long totalUTXO = 90000;
        Address changeAddress = Address.fromString("1AjiMuTdRTEUprFrpQMnwriinAkfSi4ktY");
        long sendToPhone = 55000;
        StandardTransactionBuilder builder = new StandardTransactionBuilder(NetworkParameters.productionNetwork);
        builder.addOutput(Address.fromString("1KxRfiqcNi2GbpdN3pzuQHgewShmeNW9g1"), sendToPhone);

        PublicKeyRing pubKeys = new PublicKeyRing();
        pubKeys.addPublicKey(new PublicKey(ByteUtil.fromHex("02f1c480a69fb512e302e3aa8f47aef69ec6abfff5485049e828723151e64add36")), NetworkParameters.productionNetwork);
        pubKeys.addPublicKey(new PublicKey(ByteUtil.fromHex("020772dd6a3de0419e32cbf9728bf848c27576774df1277d6988b9285c5b5e2f57")), NetworkParameters.productionNetwork);
        PrivateKeyRing privateKeys = new PrivateKeyRing();

        ExtendedKey root = getRootKey("Scholarship in both Japan and abroad has focused on differences between the samurai class and the bushido theories that developed in modern Japan");
        ExtendedKey key1 = getDerivedKey(root, 0, 1);
        ExtendedKey key2 = getDerivedKey(root, 0, 2);
        privateKeys.addPrivateKey(new InMemoryPrivateKey(key1.getWIF(), NetworkParameters.productionNetwork), NetworkParameters.productionNetwork);
        privateKeys.addPrivateKey(new InMemoryPrivateKey(key2.getWIF(), NetworkParameters.productionNetwork), NetworkParameters.productionNetwork);

        List<UnspentTransactionOutput> unspents = new ArrayList<UnspentTransactionOutput>();
        UTXOBuilder utxoBuilder = new UTXOBuilder();
        UnspentTransactionOutput utxo1 = utxoBuilder.build("482f983a2edbc042cc6c8112ab6e453442dbf5a167e7a808bdad83abdf4035a0", 12, "76a914caf37cd3c22adb4cd8e542c2c21d88378f3c706b88ac", 0, 40000);
        UnspentTransactionOutput utxo2 = utxoBuilder.build("29d31193859eb01e6f33e317f41d9d99ad627541dfd98ce5a47199a67bb798b4", 5, "76a9140efa0e63f6f9215d0baf11271c57f07deb2e91d388ac", 0, 50000);
        unspents.add(utxo1);
        unspents.add(utxo2);

        StandardTransactionBuilder.UnsignedTransaction ut = builder.createUnsignedTransaction(unspents, changeAddress, pubKeys, NetworkParameters.productionNetwork);
        StandardTransactionBuilder.SigningRequest[] s = ut.getSignatureInfo();
        List<byte[]> signatures = StandardTransactionBuilder.generateSignatures(s, privateKeys);
        Transaction tx = StandardTransactionBuilder.finalizeTransaction(ut, signatures);

        System.out.println("Unsigned transaction: " + ut.toString());
        System.out.println("Signed transaction: " + tx.toString());

        Assert.assertNotNull(ut);
        Assert.assertNotNull(tx);

        String txHex = ByteUtil.toHex(tx.toBytes());

        System.out.println("TxHex:" + txHex);

        long balance = 90000 - 55000 - ut.calculateFee();

        System.out.println("Balance: " + balance);
    }

    private ExtendedKey getRootKey(String seed) throws Exception {
        byte[] passphraseHash = new Hash(seed, 50000, "SHA-256").hash();
        byte[] keyHash = new Hash(passphraseHash).getHmacSHA512(Seed.BITCOIN_SEED);
        //we are working with compressed public keys
        return new ExtendedKey(keyHash, true);
    }

    private ExtendedKey getDerivedKey(ExtendedKey root, int account, int sequence) throws Exception {
        Derivation d = new Derivation(root);
        return d.accountKey(0, account, sequence);
    }
}
