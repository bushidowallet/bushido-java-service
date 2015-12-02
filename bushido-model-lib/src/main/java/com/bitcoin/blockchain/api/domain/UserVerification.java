package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jesion on 2015-11-17.
 */
public class UserVerification implements Serializable {

    public String userId;

    public List<String> verifications;

    public UserVerification() {

    }

    public UserVerification(String userId, List<String> verifications) {
        this.userId = userId;
        this.verifications = verifications;
    }

    public enum UserVerificationType {
        emailVerified,
        phoneVerified
    }

    public boolean hasVerification(String verification) {
        for (int i = 0; i < verifications.size(); i++) {
            if (verifications.get(i).equals(verification) == true) {
                return true;
            }
        }
        return false;
    }
}
