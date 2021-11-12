package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;


import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;

public class VolleyErrorHelper {
    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     */

    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.timeout);
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);

        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.nointernet);
        }
        return context.getResources().getString(R.string.generic_error);

    }

    private static String handleServerError(Object error, Context context) {
        VolleyError er = (VolleyError) error;
        NetworkResponse response = er.networkResponse;

        String message = null;
        if (response != null) {
            switch (response.statusCode) {
                case 400:
                case 401:
                case 403:
                case 404:
                case 500:
                    message = responseMessage(response);
                    if(message != null)
                        return message;
                    // invalid request
                    return ((VolleyError) error).getMessage();
                default:
                    return context.getResources().getString(R.string.timeout);
            }
        }
        return context.getResources().getString(R.string.generic_error);
    }

    private static String responseMessage(NetworkResponse response){
        String message = null;
        try {
            // server might return error like this { "error": "Some error occured" }
            message = new String(response.data);
            JSONObject jobj = new JSONObject(message);
            message = jobj.getString("message");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError || error instanceof AuthFailureError);
    }

    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError || error instanceof NoConnectionError);
    }
}