package com.liuqi.token;

public interface TokenManager {

    /**
     * 刷新用户
     * @param token
     * @return
     */
    void refreshUserToken(String token);

}
