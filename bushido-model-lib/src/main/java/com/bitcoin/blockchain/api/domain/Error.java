package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2014-12-25.
 */
public class Error implements Serializable {

    //1 - Could not create wallet, as the key provided is already used
    //2 - Failed to stop wallet, as it was not running
    //3 - Could not get wallet, its key does not exist
    //4 - Could not get transactions for wallet, which key does not exist for
    //5 - Trying to send money out of wallet, which key does not exist for
    //6 - You don't have the permission to wallet or wallet does not exist
    //7 - Could not create user, as username already exists
    //8 - You don't have permission to this wallet or acting on wallet key that don't exist
    //9 - You have been refused to authenticate, access denied
    //10 - Could not create organization, as key used already exists
    //11 - Could not create a user, as organization provided does not exist. Create organization first
    //12 - Could not add account to wallet, there is no wallet with key provided
    //13 - Could not add account to wallet, as account sequence number is already used by the wallet
    //14 - Could not create wallet, we encountered an unexpected problem while hashing passphrase
    //15 - No UTXOs available for spending
    //16 - Wrong wallet credentials provided
    //17 - Not implemented
    //18 - Invalid reg code
    //19 - User with email provided not found
    //20 - User with email provided is already registered

    public static int USER_BY_EMAIL_NOT_FOUND = 19;
    public static int USER_BY_EMAIL_FOUND = 20;
    public static int TOKEN_NOT_FOUND = 21;
    public static int TOKEN_USER_NOT_FOUND = 22;
    public static int TOKEN_EXPIRED = 23;
    public static int SECOND_FACTOR_FAILED = 24;
    public static int SECOND_FACTOR_NOT_REQUIRED = 25;
    public static int USER_BY_ID_NOT_FOUND = 26;
    public static int SECOND_FACTOR_REQUIRED_NOT_COMPLETE = 27;
    public static int USER_NOT_VERIFIED = 28;
    public static int USER_EMAIL_NOT_VERIFIED = 29;
    public static int EMAILS_DONT_MATCH = 30;
    public static int USER_ALREADY_VERIFIED = 31;
    public static int USER_NOT_FOUND = 32;
    public static int PIN_ALREADY_SET = 33;
    public static int PIN_NOT_SET = 34;

    public int code;

    public Error(int code) {
        this.code = code;
    }

    public Error() {

    }
}
