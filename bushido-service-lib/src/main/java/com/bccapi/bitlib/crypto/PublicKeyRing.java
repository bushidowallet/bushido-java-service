package com.bccapi.bitlib.crypto;

import com.bccapi.bitlib.model.Address;
import com.bccapi.bitlib.model.NetworkParameters;

import java.util.*;

public class PublicKeyRing {
   private List<Address> _addresses;
   private Set<Address> _addressSet;
   private Map<Address, PublicKey> _publicKeys;

   public PublicKeyRing() {
      _addresses = new ArrayList<Address>();
      _addressSet = new HashSet<Address>();
      _publicKeys = new HashMap<Address, PublicKey>();
   }

   /**
    * Add a public key to the key ring.
    */
   public void addPublicKey(PublicKey key, NetworkParameters network) {
      Address address = Address.fromStandardPublicKey(key, network);
      _addresses.add(address);
      _addressSet.add(address);
      _publicKeys.put(address, key);
   }

   public PublicKey findPublicKeyByAddress(Address address) {
      return _publicKeys.get(address);
   }

   public List<Address> getAddresses() {
      return Collections.unmodifiableList(_addresses);
   }

   public Set<Address> getAddressSet() {
      return Collections.unmodifiableSet(_addressSet);
   }

}
