package com.example.admin.bean;

import java.io.Serializable;

public class MenuMeta implements Serializable {
    private boolean keepAlive;
    private boolean requireAuth;

    public boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean getRequireAuth() {
        return requireAuth;
    }

    public void setRequireAuth(boolean requireAuth) {
        this.requireAuth = requireAuth;
    }
}
