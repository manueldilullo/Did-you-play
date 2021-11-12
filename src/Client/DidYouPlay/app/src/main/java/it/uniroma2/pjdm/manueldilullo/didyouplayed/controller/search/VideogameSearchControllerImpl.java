package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.search;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.SingletonRequestQueue;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyErrorHelper;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyRequestManager;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.search.SearchVideogameActivity;

public class VideogameSearchControllerImpl implements VideogameSearchController {

    private static final String TAG = VideogameSearchControllerImpl.class.getSimpleName();

    private VolleyRequestManager volleyRequestManager;
    private SearchVideogameActivity activity;

    public VideogameSearchControllerImpl(SearchVideogameActivity activity){
        this.activity = activity;
        volleyRequestManager = new VolleyRequestManager(activity);
    }

    @Override
    public void getAllGames() {
        Log.d(TAG, "getAllGames: Method called");
        String url = activity.getResources().getString(R.string.urlVideogameServlet) + "?action="
                + activity.getResources().getString(R.string.videgoameservlet_action_list);
        volleyRequestManager.doJsonArrayRequest(Request.Method.GET, url, null, null,
                VolleyRequestManager.getJSONArrayResponseListener(activity),
                VolleyRequestManager.getGenericErrorListener(activity, activity));
    }
}
