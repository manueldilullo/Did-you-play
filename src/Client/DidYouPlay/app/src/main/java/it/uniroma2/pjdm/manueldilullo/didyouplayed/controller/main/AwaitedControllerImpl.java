package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyRequestManager;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.main.AwaitedFragment;

public class AwaitedControllerImpl implements AwaitedController{
    private static final String TAG = AwaitedControllerImpl.class.getSimpleName();

    private VolleyRequestManager volleyRequestManager;
    private SharedPreferences sharedPreferences;
    private AwaitedFragment awaitedFragment;
    private Context context;

    public AwaitedControllerImpl(AwaitedFragment awaitedFragment){
        this.awaitedFragment = awaitedFragment;
        context = awaitedFragment.getActivity();
        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        volleyRequestManager = new VolleyRequestManager(context);
    }

    @Override
    public void getAwaited() {
        String user = sharedPreferences.getString("username", "");
        String url = context.getResources().getString(R.string.urlAwaitedServlet)+"?user="+user;

        volleyRequestManager.doJsonArrayRequest(Request.Method.GET, url, null, null,
                VolleyRequestManager.getJSONArrayResponseListener(awaitedFragment),
                VolleyRequestManager.getGenericErrorListener(awaitedFragment, context));
    }

    @Override
    public void removeAwaited(int videogame_id) {
        HashMap<String, String> params = new HashMap<String, String>();
        String user = sharedPreferences.getString("username", "");
        String url = context.getResources().getString(R.string.urlAwaitedServlet);

        params.put("user", user);
        params.put("videogame_id", String.valueOf(videogame_id));

        volleyRequestManager.doStringRequest(Request.Method.DELETE, url, params, null,
                VolleyRequestManager.getStringResponseListener(awaitedFragment),
                VolleyRequestManager.getGenericErrorListener(awaitedFragment, context));
    }

    /**
     * Creates an HashMap<Integer, ArrayList<Videogame>> where:
     *  the key is the missing days
     *  the values are ArrayLists of Videogames out in a number of days equals to the key
     * @param response
     * @return
     */
    public HashMap<Integer, ArrayList<Videogame>> sortAwaited(JSONArray response){
        HashMap<Integer, ArrayList<Videogame>> awaited = new HashMap<Integer, ArrayList<Videogame>>();

        for(int i=0;i < response.length(); i++){
            try {
                JSONObject jObj = response.getJSONObject(i);
                Videogame v = new Gson().fromJson(jObj.get("videogame").toString(), Videogame.class);
                int out_in_days = v.getMissingDays();

                ArrayList<Videogame> arr;
                if(awaited.containsKey(out_in_days)){
                    arr = awaited.get(out_in_days);
                    arr.add(v);
                }else{
                    arr = new ArrayList<Videogame>();
                    arr.add(v);
                    awaited.put(out_in_days, arr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "sortAwaited: " + awaited.toString());
        return awaited;
    }
}
