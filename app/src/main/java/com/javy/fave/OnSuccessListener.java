package com.javy.fave;

import okhttp3.ResponseBody;

public interface OnSuccessListener {
    void onSuccess(ResponseBody responseBody);
    void onComplete();

}

