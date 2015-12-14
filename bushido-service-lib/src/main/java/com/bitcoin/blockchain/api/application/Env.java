package com.bitcoin.blockchain.api.application;

/**
 * Created by Jesion on 2015-12-14.
 */
public class Env {

    public static String DEV = "dev";
    public static String PROD = "prod";
    private static String ACTIVE_PROFILE_SYS_FLAG = "spring.profiles.active";

    public static boolean isDev() {
        return DEV.equals(System.getProperty(ACTIVE_PROFILE_SYS_FLAG));
    }

    public static boolean isProd() {
        return PROD.equals(System.getProperty(ACTIVE_PROFILE_SYS_FLAG));
    }

    public static String getEnv() {
        return System.getProperty(ACTIVE_PROFILE_SYS_FLAG);
    }
}
