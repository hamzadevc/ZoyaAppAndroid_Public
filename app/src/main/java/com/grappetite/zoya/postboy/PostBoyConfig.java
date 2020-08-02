package com.grappetite.zoya.postboy;

public class PostBoyConfig {
    static int CONNECTION_TIMEOUT = 100000;
    static int READ_TIMEOUT = 100000;
    static boolean KEEP_PERSISTENT = true;

    int connectionTimeout;
    int readTimeout;
    boolean keepPersistent;

    public PostBoyConfig setDefaultConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public PostBoyConfig setDefaultReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public PostBoyConfig setDefaultKeepPersistent(boolean keepPersistent) {
        this.keepPersistent = keepPersistent;
        return this;
    }
}
