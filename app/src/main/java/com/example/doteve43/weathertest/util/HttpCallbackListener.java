package com.example.doteve43.weathertest.util;

/**
 * Created by doteve43 on 2016/1/6.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
