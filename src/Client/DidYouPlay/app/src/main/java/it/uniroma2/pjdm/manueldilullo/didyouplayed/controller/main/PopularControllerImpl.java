package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.main;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.BasicAuth;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.SingletonRequestQueue;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyErrorHelper;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyRequestManager;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.main.PopularFragment;


public class PopularControllerImpl implements PopularController{
    private static final String TAG = PopularControllerImpl.class.getSimpleName();

    private VolleyRequestManager volleyRequestManager;
    private PopularFragment popularFragment;
    private Context context;

    public PopularControllerImpl(PopularFragment popularFragment){
        this.popularFragment = popularFragment;
        context = popularFragment.getActivity();
        volleyRequestManager = new VolleyRequestManager(context);
    }

    @Override
    public void getPopular() {
        Log.d(TAG, "getPopular: Method called");

        volleyRequestManager.doJsonArrayRequest(Request.Method.GET, popularFragment.getResources().getString(R.string.urlPopularServlet), null, null,
                VolleyRequestManager.getJSONArrayResponseListener(popularFragment),
                VolleyRequestManager.getGenericErrorListener(popularFragment, context));
    }
}
