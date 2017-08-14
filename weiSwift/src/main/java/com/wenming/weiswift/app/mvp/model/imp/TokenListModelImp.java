package com.wenming.weiswift.app.mvp.model.imp;

import android.content.Context;

import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wenming.weiswift.app.common.entity.Token;
import com.wenming.weiswift.app.common.entity.list.TokenList;
import com.wenming.weiswift.app.common.oauth.AccessTokenManager;
import com.wenming.weiswift.app.mvp.model.TokenListModel;
import com.wenming.weiswift.utils.SDCardUtils;

/**
 * Created by wenmingvs on 16/5/18.
 */
public class TokenListModelImp implements TokenListModel {


    public void addToken(Context context, String token, String expiresIn, String refresh_token, String uid) {
        Gson gson = new Gson();
        Token element = new Token(token, expiresIn, refresh_token, uid);
        TokenList tokenList = TokenList.parse(SDCardUtils.get(context, SDCardUtils.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt"));
        if (tokenList == null || tokenList.tokenList.size() == 0) {
            tokenList = new TokenList();
        }
        //重复登录的话，不进行重复token的添加
        for (int i = 0; i < tokenList.tokenList.size(); i++) {
            if (tokenList.tokenList.get(i).uid.equals(uid)) {
                updateAccessToken(token, expiresIn, refresh_token, uid);
                return;
            }
        }
        tokenList.tokenList.add(element);
        tokenList.total_number = tokenList.tokenList.size();
        tokenList.current_uid = uid;
        SDCardUtils.put(context, SDCardUtils.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt", gson.toJson(tokenList));
        updateAccessToken(token, expiresIn, refresh_token, uid);
    }

    @Override
    public void deleteToken(Context context, String uid) {
        Gson gson = new Gson();
        TokenList tokenList = TokenList.parse(SDCardUtils.get(context, SDCardUtils.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt"));
        for (int i = 0; i < tokenList.tokenList.size(); i++) {
            if (tokenList.tokenList.get(i).uid.equals(uid)) {
                tokenList.tokenList.remove(i);
            }
        }
        tokenList.total_number = tokenList.tokenList.size();
        if (tokenList.tokenList.size() == 0) {
            SDCardUtils.put(context, SDCardUtils.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt", gson.toJson(tokenList));
            AccessTokenManager.clearAccessToken();
            return;
        }
        tokenList.current_uid = tokenList.tokenList.get(0).uid;
        SDCardUtils.put(context, SDCardUtils.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt", gson.toJson(tokenList));
        if (AccessTokenManager.getInstance().getOAuthToken().equals(uid)) {
            AccessTokenManager.clearAccessToken();
            Token token = tokenList.tokenList.get(0);
            updateAccessToken(token.token, token.expiresIn, token.refresh_token, token.uid);
        }

    }

    @Override
    public void switchToken(Context context, String uid) {
        TokenList tokenList = TokenList.parse(SDCardUtils.get(context, SDCardUtils.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt"));
        for (int i = 0; i < tokenList.tokenList.size(); i++) {
            if (tokenList.tokenList.get(i).uid.equals(uid)) {
                Token token = tokenList.tokenList.get(i);
                updateAccessToken(token.token, token.expiresIn, token.refresh_token, token.uid);
                break;
            }
        }
    }

    @Override
    public void switchToken(Context context, int positonInCache, OnTokenSwitchListener onTokenSwitchListener) {
        TokenList tokenList = TokenList.parse(SDCardUtils.get(context, SDCardUtils.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt"));
        if (tokenList.total_number > 0) {
            Token token = tokenList.tokenList.get(positonInCache);
            updateAccessToken(token.token, token.expiresIn, token.refresh_token, token.uid);
            onTokenSwitchListener.onSuccess();
        } else {
            onTokenSwitchListener.onError("切换失败,本地token缓存为空");
        }

    }


    public void updateAccessToken(String token, String expiresIn, String refresh_token, String uid) {
        Oauth2AccessToken mAccessToken = new Oauth2AccessToken();
        mAccessToken.setToken(token);
        mAccessToken.setExpiresIn(expiresIn);
        mAccessToken.setRefreshToken(refresh_token);
        mAccessToken.setUid(uid);
        AccessTokenManager.writeAccessToken(mAccessToken);
    }
}
