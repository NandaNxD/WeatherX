package com.mathx.weatherx;

import com.android.volley.VolleyError;

public interface VolleyCallback {
    void onSuccess(String result);
    void onError(VolleyError volleyError);
}

