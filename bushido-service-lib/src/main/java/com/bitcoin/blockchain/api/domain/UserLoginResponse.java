package com.bitcoin.blockchain.api.domain;

import java.util.List;

/**
 * Created by Jesion on 2015-06-26.
 */
public class UserLoginResponse extends ResponseBase {

    public UserLoginResponse() {
        super();
    }

    public UserLoginResponse(List<Error> errors, List<V2WalletDescriptor> wallets, User user) {
        super(errors);
        setWallets(wallets);
        this.user = user;
    }

    public UserLoginResponse(List<V2WalletDescriptor> wallets) {
        super();
        setWallets(wallets);
    }

    private List<V2WalletDescriptor> wallets;

    public void setWallets(List<V2WalletDescriptor> wallets) {
        this.wallets = wallets;
    }

    public List<V2WalletDescriptor> getWallets() {
        return this.wallets;
    }

    public User user;
}
