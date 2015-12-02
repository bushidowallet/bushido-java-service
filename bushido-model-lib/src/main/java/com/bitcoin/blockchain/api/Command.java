package com.bitcoin.blockchain.api;

/**
 * Created by Jesion on 2014-12-29.
 */
public class Command {

    public static String BALANCE_CHANGE_RECEIVED = "BALANCE_CHANGE_RECEIVED";
    public static String BALANCE_CHANGE_SPENT = "BALANCE_CHANGE_SPENT";
    public static String BALANCE_CHANGE_STATUS = "BALANCE_CHANGE_STATUS";
    public static String FC_BALANCE_CHANGE_RECEIVED = "FC_BALANCE_CHANGE_RECEIVED";
    public static String TRANSACTION_STATUS_CHANGE = "TRANSACTION_STATUS_CHANGE";
    public static String GET_ADDRESS = "GET_ADDRESS";
    public static String GET_UNSPENT_OUTPUTS = "GET_UNSPENT_OUTPUTS";
    public static String GET_INSTRUMENT_DATA = "GET_INSTRUMENT_DATA";
    public static String SPEND_ALL_UTXO = "SPEND_ALL_UTXO";
    public static String SPEND = "SPEND";
    public static String HEARTBEAT = "HEARTBEAT";
    public static String INSTRUMENT_CHANGE = "INSTRUMENT_CHANGE";
}
