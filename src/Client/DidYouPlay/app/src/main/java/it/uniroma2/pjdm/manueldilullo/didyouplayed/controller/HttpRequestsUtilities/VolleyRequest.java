package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities;

import android.app.VoiceInteractor;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;

public interface VolleyRequest {
    void doJsonArrayRequest(int method, String url, HashMap<String, String> params, HashMap<String, String> headers, Response.Listener listener, Response.ErrorListener errorListener);
    void doJsonObjectRequest(int method, String url, HashMap<String, String> params, HashMap<String, String> headers, Response.Listener listener, Response.ErrorListener errorListener);
    void doStringRequest(int method, String url, HashMap<String, String> params, HashMap<String, String> headers, Response.Listener listener, Response.ErrorListener errorListener);
}
