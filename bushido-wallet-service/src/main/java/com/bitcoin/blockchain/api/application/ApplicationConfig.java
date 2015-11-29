package com.bitcoin.blockchain.api.application;

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

    private boolean mockMode;

    public boolean getMockMode() {
        return mockMode;
    }

    public void setMockMode(boolean mockMode) {
        this.mockMode = mockMode;
    }

    private String blockDir;

    public void setBlockDir(String blockDir) {
        this.blockDir = blockDir;
    }

    public String getBlockDir() {
        return this.blockDir;
    }

    private String walletDir;

    public void setWalletDir(String walletDir) {
        this.walletDir = walletDir;
    }

    public String getWalletDir() {
        return this.walletDir;
    }
}
