package com.bitcoin.blockchain.api.application;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;

/**
 * Created by Jesion on 2014-11-27.
 */
public class ApplicationConfig {

    private Environment environment;

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public NetworkParameters getNetworkParams() {
        if (getEnvironment().equals(Environment.REGTEST)) {
            return RegTestParams.get();
        } else if (getEnvironment().equals(Environment.TESTNET)) {
            return TestNet3Params.get();
        } else if (getEnvironment().equals(Environment.MAINNET)) {
            return MainNetParams.get();
        }
        return null;
    }
}
