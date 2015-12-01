/**
 * Parts of this code was extracted from the Java cryptography library from
 * www.bouncycastle.org.
 */
package com.bccapi.bitlib.crypto;

import com.bccapi.bitlib.crypto.ec.EcTools;
import com.bccapi.bitlib.crypto.ec.Parameters;
import com.bccapi.bitlib.crypto.ec.Point;
import com.bccapi.bitlib.model.NetworkParameters;
import com.bccapi.bitlib.util.Base58;
import com.bccapi.bitlib.util.HashUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * A Bitcoin private key that is kept in memory.
 */
public class InMemoryPrivateKey extends PrivateKey implements KeyExporter, Serializable {

   private static final long serialVersionUID = 1L;

   private final BigInteger _privateKey;
   private final PublicKey _publicKey;

   /**
    * Construct a random private key using a secure random source. Using this
    * constructor yields uncompressed public keys.
    * @param random pseudo random number generator
    */
   public InMemoryPrivateKey(SecureRandom random) {
      this(random, false);
   }

   /**
    * Construct a random private key using a secure random source with optional
    * compressed public keys.
    * 
    * @param random
    *           The random source from which the private key will be
    *           deterministically generated.
    * @param compressed
    *           Specifies whether the corresponding public key should be
    *           compressed
    */
   public InMemoryPrivateKey(SecureRandom random, boolean compressed) {
      int nBitLength = Parameters.n.bitLength();
      BigInteger d;
      do {
         // Make a BigInteger from bytes to ensure that Andriod and 'classic'
         // java make the same BigIntegers from the same random source with the
         // same seed. Using BigInteger(nBitLength, random)
         // produces different results on Android compared to 'classic' java.
         byte[] bytes = new byte[nBitLength / 8];
         random.nextBytes(bytes);
         bytes[0] = (byte) (bytes[0] & 0x7F); // ensure positive number
         d = new BigInteger(bytes);
      } while (d.equals(BigInteger.ZERO) || (d.compareTo(Parameters.n) >= 0));

      Point Q = EcTools.multiply(Parameters.G, d);
      _privateKey = d;
      if (compressed) {
         // Convert Q to a compressed point on the curve
         Q = new Point(Q.getCurve(), Q.getX(), Q.getY(), true);
      }
      _publicKey = new PublicKey(Q.getEncoded());
   }

   /**
    * Construct from private key bytes. Using this constructor yields
    * uncompressed public keys.
    * 
    * @param bytes
    *           The private key as an array of bytes
    */
   public InMemoryPrivateKey(byte[] bytes) {
      this(bytes, false);
   }

   /**
    * Construct from private key bytes. Using this constructor yields
    * uncompressed public keys.
    * 
    * @param bytes
    *           The private key as an array of bytes
    * @param compressed
    *           Specifies whether the corresponding public key should be
    *           compressed
    */
   public InMemoryPrivateKey(byte[] bytes, boolean compressed) {
      if (bytes.length != 32) {
         throw new IllegalArgumentException("The length must be 32 bytes");
      }
      // Ensure that we treat it as a positive number
      byte[] keyBytes = new byte[33];
      System.arraycopy(bytes, 0, keyBytes, 1, 32);
      _privateKey = new BigInteger(keyBytes);
      Point Q = EcTools.multiply(Parameters.G, _privateKey);
      if (compressed) {
         // Convert Q to a compressed point on the curve
         Q = new Point(Q.getCurve(), Q.getX(), Q.getY(), true);
      }
      _publicKey = new PublicKey(Q.getEncoded());
   }

   /**
    * Construct from private and public key bytes
    * 
    * @param priBytes The private key as an array of bytes
    * @param pubBytes The public key bytes
    */
   public InMemoryPrivateKey(byte[] priBytes, byte[] pubBytes) {
      if (priBytes.length != 32) {
         throw new IllegalArgumentException("The length of the array of bytes must be 32");
      }
      // Ensure that we treat it as a positive number
      byte[] keyBytes = new byte[33];
      System.arraycopy(priBytes, 0, keyBytes, 1, 32);
      _privateKey = new BigInteger(keyBytes);
      _publicKey = new PublicKey(pubBytes);
   }

   /**
    * Construct from a base58 encoded key (SIPA format)
    *
    * @param base58Encoded private key in Base58 format
    * @param network bitcoin network to talk to
    */
   public InMemoryPrivateKey(String base58Encoded, NetworkParameters network) {
      byte[] decoded = Base58.decodeChecked(base58Encoded);

      // Validate format
      if (decoded == null || decoded.length < 33 || decoded.length > 34) {
         throw new IllegalArgumentException("Invalid base58 encoded key");
      }
      if (network.equals(NetworkParameters.productionNetwork) && decoded[0] != (byte) 0x80) {
         throw new IllegalArgumentException("The base58 encoded key is not for the production network");
      }
      if (network.equals(NetworkParameters.testNetwork) && decoded[0] != (byte) 0xEF) {
         throw new IllegalArgumentException("The base58 encoded key is not for the test network");
      }

      // Determine whether compression should be used for the public key
      boolean compressed;
      if (decoded.length == 34) {
         if (decoded[33] != 0x01) {
            throw new IllegalArgumentException("Invalid base58 encoded key");
         }
         // Get rid of the compression indication byte at the end
         byte[] temp = new byte[33];
         System.arraycopy(decoded, 0, temp, 0, temp.length);
         decoded = temp;
         compressed = true;
      } else {
         compressed = false;
      }
      // Make positive and clear network prefix
      decoded[0] = 0;

      _privateKey = new BigInteger(decoded);
      Point Q = EcTools.multiply(Parameters.G, _privateKey);
      if (compressed) {
         // Convert Q to a compressed point on the curve
         Q = new Point(Q.getCurve(), Q.getX(), Q.getY(), true);
      }
      _publicKey = new PublicKey(Q.getEncoded());
   }

   @Override
   public PublicKey getPublicKey() {
      return _publicKey;
   }

   @Override
   protected BigInteger[] generateSignature(byte[] message) {
      BigInteger n = Parameters.n;
      BigInteger e = calculateE(n, message);
      BigInteger r = null;
      BigInteger s = null;
      SecureRandom random = new SecureRandom();
      // 5.3.2
      do // generate s
      {
         BigInteger k = null;
         int nBitLength = n.bitLength();

         do // generate r
         {
            do {
               // make a BigInteger from bytes to ensure that Andriod and
               // 'classic' java make the same BigIntegers
               byte[] bytes = new byte[nBitLength / 8];
               random.nextBytes(bytes);
               bytes[0] = (byte) (bytes[0] & 0x7F); // ensure positive number
               k = new BigInteger(bytes);
            } while (k.equals(BigInteger.ZERO));

            Point p = EcTools.multiply(Parameters.G, k);

            // 5.3.3
            BigInteger x = p.getX().toBigInteger();

            r = x.mod(n);
         } while (r.equals(BigInteger.ZERO));

         BigInteger d = _privateKey;

         s = k.modInverse(n).multiply(e.add(d.multiply(r))).mod(n);
      } while (s.equals(BigInteger.ZERO));

      BigInteger[] res = new BigInteger[2];

      res[0] = r;
      res[1] = s;

      return res;
   }

   private BigInteger calculateE(BigInteger n, byte[] message) {
      if (n.bitLength() > message.length * 8) {
         return new BigInteger(1, message);
      } else {
         int messageBitLength = message.length * 8;
         BigInteger trunc = new BigInteger(1, message);

         if (messageBitLength - n.bitLength() > 0) {
            trunc = trunc.shiftRight(messageBitLength - n.bitLength());
         }

         return trunc;
      }
   }

   @Override
   public byte[] getPrivateKeyBytes() {
      byte[] result = new byte[32];
      byte[] bytes = _privateKey.toByteArray();
      if (bytes.length <= result.length) {
         System.arraycopy(bytes, 0, result, result.length - bytes.length, bytes.length);
      } else {
         // This happens if the most significant bit is set and we have an
         // extra leading zero to avoid a negative BigInteger
         assert bytes.length == 33 && bytes[0] == 0;
         System.arraycopy(bytes, 1, result, 0, bytes.length - 1);
      }
      return result;
   }

   @Override
   public String getBase58EncodedPrivateKey(NetworkParameters network) {
      if (getPublicKey().isCompressed()) {
         return getBase58EncodedPrivateKeyCompressed(network);
      } else {
         return getBase58EncodedPrivateKeyUncompressed(network);
      }
   }

   private String getBase58EncodedPrivateKeyUncompressed(NetworkParameters network) {
      byte[] toEncode = new byte[1 + 32 + 4];
      // Set network
      toEncode[0] = network.isProdnet() ? (byte) 0x80 : (byte) 0xEF;
      // Set key bytes
      byte[] keyBytes = getPrivateKeyBytes();
      System.arraycopy(keyBytes, 0, toEncode, 1, keyBytes.length);
      // Set checksum
      byte[] checkSum = HashUtils.doubleSha256(toEncode, 0, 1 + 32);
      System.arraycopy(checkSum, 0, toEncode, 1 + 32, 4);
      // Encode
      return Base58.encode(toEncode);
   }

   private String getBase58EncodedPrivateKeyCompressed(NetworkParameters network) {
      byte[] toEncode = new byte[1 + 32 + 1 + 4];
      // Set network
      toEncode[0] = network.isProdnet() ? (byte) 0x80 : (byte) 0xEF;
      // Set key bytes
      byte[] keyBytes = getPrivateKeyBytes();
      System.arraycopy(keyBytes, 0, toEncode, 1, keyBytes.length);
      // Set compressed indicator
      toEncode[33] = 0x01;
      // Set checksum
      byte[] checkSum = HashUtils.doubleSha256(toEncode, 0, 1 + 32 + 1);
      System.arraycopy(checkSum, 0, toEncode, 1 + 32 + 1, 4);
      // Encode
      return Base58.encode(toEncode);
   }

}
