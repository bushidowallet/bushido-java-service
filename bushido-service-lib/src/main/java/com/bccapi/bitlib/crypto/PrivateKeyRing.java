package com.bccapi.bitlib.crypto;

import com.bccapi.bitlib.model.NetworkParameters;

import java.util.HashMap;
import java.util.Map;

public class PrivateKeyRing extends PublicKeyRing {

   private Map<PublicKey, PrivateKey> _privateKeys;

   public PrivateKeyRing() {
      _privateKeys = new HashMap<PublicKey, PrivateKey>();
   }

   /**
    * Add a private key to the key ring.
    *
    * @param key private key
    * @param network bitcoin network to talk to
    */
   public void addPrivateKey(PrivateKey key, NetworkParameters network) {
      _privateKeys.put(key.getPublicKey(), key);
      addPublicKey(key.getPublicKey(), network);
   }

   /**
    * Find a Bitcoin signer by public key
    *
    * @param publicKey public key instance
    *
    * @return BitcoinSigner to sign transactions with
    */
   public BitcoinSigner findSignerByPublicKey(PublicKey publicKey) {
      return _privateKeys.get(publicKey);
   }

   /**
    * Find a KeyExporter by public key
    *
    * @return key exporter
    */
   public KeyExporter findKeyExporterByPublicKey(PublicKey publicKey) {
      PrivateKey key = _privateKeys.get(publicKey);
      if (key instanceof KeyExporter) {
         return (KeyExporter) key;
      }
      return null;
   }

}
