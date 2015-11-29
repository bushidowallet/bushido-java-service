package com.bitcoin.blockchain.api.service.v2wallet;

import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.domain.message.ClientMessageBase;

import java.util.List;

/**
 * Created by Jesion on 2015-01-09.
 */
public interface V2WalletService {

    public void messageToWallet(ClientMessageBase incoming);

    public V2WalletRegistry getWallets();

    public UserLoginResponse create(V2WalletDescriptor wallet);

    public Response addAccount(String key, V2WalletAccount accountDTO);

    public V2WalletDescriptor get(String key);

    public V2Wallet getWallet(String key);

    public LoginResponse login(V2WalletDescriptor wallet, String password);

    public Response delete(V2WalletDescriptor wallet);

    public List<V2WalletDescriptor> getAll();

    public Response getKeys(String key, int account);

    public Response getTransactionsKeys(String key, int account);

    public Response getTransactions(String key, int account);

    public Response getSpendingTransactions(String key);

    public Response getUnspentOutputs(String key);

    public Response spendAllUTXO(String key, String address) throws Exception;

    public Response spendAllUTXOPush(String key, String address) throws Exception;

    public String initTopUp(V2WalletDescriptor wallet);

    public void handleTopUpNotification(TopUpNotification notification);
}
