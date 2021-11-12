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
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.main.PlayedFragment;

public class PlayedControllerImpl implements PlayedController{

    private static final String TAG = PlayedControllerImpl.class.getSimpleName();

    private VolleyRequestManager volleyRequestManager;
    private SharedPreferences sharedPreferences;
    private PlayedFragment playedFragment;
    private Context context;

    public PlayedControllerImpl(PlayedFragment playedFragment){
        this.playedFragment = playedFragment;
        context = playedFragment.getActivity();
        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        volleyRequestManager = new VolleyRequestManager(context);
    }

    /**
     * Creates an HashMap<Integer, ArrayList<Videogame>> where:
     *  the key is the score
     *  the values are ArrayLists of Videogames to which the user has assigned a rating equal to the key
     * @param response
     * @return
     */
    public HashMap<Integer, ArrayList<Videogame>> sortPlayed(JSONArray response){
        HashMap<Integer, ArrayList<Videogame>> played = new HashMap<Integer, ArrayList<Videogame>>();

        for(int i=0;i < response.length(); i++){
            try {
                JSONObject jObj = response.getJSONObject(i);
                Integer score = jObj.getInt("score");
                Videogame v = new Gson().fromJson(jObj.get("videogame").toString(), Videogame.class);

                ArrayList<Videogame> arr;
                if(played.containsKey(score)){
                    arr = played.get(score);
                    arr.add(v);
                }else{
                    arr = new ArrayList<Videogame>();
                    arr.add(v);
                    played.put(score, arr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "sortPlayed: " + played.toString());
        return played;
    }

    @Override
    public void getPlayed() {
        String user = sharedPreferences.getString("username", "");
        String url = context.getResources().getString(R.string.urlPlayedServlet)+"?user="+user;
        volleyRequestManager.doJsonArrayRequest(Request.Method.GET, url, null, null,
                VolleyRequestManager.getJSONArrayResponseListener(playedFragment),
                VolleyRequestManager.getGenericErrorListener(playedFragment, context));
    }

    @Override
    public void removePlayed(int videogame_id) {
        String url = context.getResources().getString(R.string.urlPlayedServlet);
        String user = sharedPreferences.getString("username", "");

        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("videogame_id", String.valueOf(videogame_id));

        volleyRequestManager.doStringRequest(Request.Method.DELETE, url, params, null,
                VolleyRequestManager.getStringResponseListener(playedFragment),
                VolleyRequestManager.getGenericErrorListener(playedFragment, context));
    }
}
