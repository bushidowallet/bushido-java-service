package com.bitcoin.blockchain.api.domain;

/**
 * Created by Jesion on 2014-12-06.
 */
public class WalletChange extends BalanceChange {

    private long coinsReceivedOrSpent;

    public long getCoinsReceivedOrSpent() {
        return this.coinsReceivedOrSpent;
    }

    private String currentAddress;

    public String getCurrentAddress() {
        return this.currentAddress;
    }

    private Transaction tx;

    public Transaction getTx() {
        return tx;
    }

    public void setTx(Transaction value) {
        this.tx = value;
    }

    public WalletChange(long coinsReceivedOrSpent,
                        Balance balance,
                        String currentAddress,
                        Transaction tx) {
        super(balance);
        this.coinsReceivedOrSpent = coinsReceivedOrSpent;
        this.currentAddress = currentAddress;
        this.tx = tx;
    }

    public WalletChange() {
        super();
    }
}
