package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequestManager implements VolleyRequest{

    private static final String TAG = VolleyRequestManager.class.getSimpleName();

    private RequestQueue requestQueue;
    private Context context;

    public VolleyRequestManager(Context context){
        this.context = context;
        requestQueue = SingletonRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    static public Response.Listener<JSONArray> getJSONArrayResponseListener(HttpResponseHandler handler){
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: " + response.toString());
                handler.onJsonArrayRequestSuccess(response);
            }
        };
    }

    static public Response.Listener<JSONObject> getJSONObjectResponseListener(HttpResponseHandler handler){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response.toString());
                handler.onJsonObjectRequestSuccess(response);
            }
        };
    }

    static public Response.Listener<String> getStringResponseListener(HttpResponseHandler handler){
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                handler.onStringRequestSuccess(response);
            }
        };
    }

    static public Response.ErrorListener getGenericErrorListener(HttpResponseHandler handler, Context context){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + VolleyErrorHelper.getMessage(error, context));
                handler.onFail(VolleyErrorHelper.getMessage(error, context));
            }
        };
    }

    @Override
    public void doJsonArrayRequest(int method, String url, HashMap<String, String> params, HashMap<String, String> headers, Response.Listener listener, Response.ErrorListener errorListener) {
        Log.d(TAG, "doJsonArrayRequest: Method called");

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers == null ? new HashMap<String, String>() : headers;
            }

            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params == null ? new HashMap<String, String>() : params;
            }
        };

        Log.d(TAG, "doJsonArrayRequest: " + jsonArrayRequest);
        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void doJsonObjectRequest(int method, String url, HashMap<String, String> params, HashMap<String, String> headers, Response.Listener listener, Response.ErrorListener errorListener) {
        Log.d(TAG, "doJsonObjectRequest: Method called");

        final JSONObject paramsObj = new JSONObject(params);
        Log.d(TAG, "doJsonObjectRequest: " + paramsObj.toString());

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, paramsObj, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers == null ? new HashMap<>() : headers;
            }
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                return params == null ? new HashMap<>() : params;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void doStringRequest(int method, String url, HashMap<String, String> params, HashMap<String, String> headers, Response.Listener listener, Response.ErrorListener errorListener) {
        Log.d(TAG, "doStringRequest: Method called");

        if(method == Request.Method.DELETE){
            url = url+"?";
            for(Map.Entry<String, String> entry : params.entrySet()){
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
            url = url.substring(0, url.length()-1);
        }

        Log.d(TAG, "doStringRequest: url = " + url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(method, url, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers == null ? new HashMap<>() : headers;
            }
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                return params == null ? new HashMap<>() : params;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }
}
