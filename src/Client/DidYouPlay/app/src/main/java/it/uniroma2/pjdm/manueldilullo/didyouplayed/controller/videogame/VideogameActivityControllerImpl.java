package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.videogame;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyRequestManager;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame.VideogameActivity;

public class VideogameActivityControllerImpl implements VideogameActivityController {

    private static final String TAG = VideogameActivityControllerImpl.class.getSimpleName();

    private VolleyRequestManager volleyRequestManager;
    private Videogame videogame;
    private VideogameActivity videogameActivity;
    private String session_username;

    public VideogameActivityControllerImpl(VideogameActivity videogameActivity){
        this.videogameActivity = videogameActivity;
        this.videogame = videogameActivity.getVideogame();
        this.session_username = videogameActivity.getSharedPreferences("login", Context.MODE_PRIVATE).getString("username", "");
        this.volleyRequestManager = new VolleyRequestManager(videogameActivity);

        if (videogameActivity.getSharedPreferences("login", Context.MODE_PRIVATE).getBoolean("loggedin", false))
        Log.d(TAG, "VideogameActivityControllerImpl: " + session_username + " " + videogame.getId() + " " + videogame.getName());
    }

    @Override
    public void addDetailsRows() {
        /*
         * Dynamically extracting and formatting informations from Videogame Object
         * Given this informations it inflates the view with the addition of a custom view
         * based on <ll_gameinfo_row> template
         */
        if(!videogame.getCompanies().isEmpty()){
            String content = "";
            for(Map.Entry<String, String> entry : videogame.getCompanies().entrySet()){
                content += entry.getKey() + " ("+ entry.getValue() +")\n";
            }
            videogameActivity.addGameInfoRow(R.drawable.ic_baseline_home_24, videogameActivity.getResources().getString(R.string.gameinfo_category_companies), content);
        }

        if(!videogame.getGenres().isEmpty()){
            String content = "";
            for(String genre : videogame.getGenres()){
                content += genre + "\n";
            }
            videogameActivity.addGameInfoRow(R.drawable.ic_baseline_home_24, videogameActivity.getResources().getString(R.string.gameinfo_category_genres), content);
        }

        if(!videogame.getPlatforms().isEmpty()){
            String content = "";
            for(Map.Entry<String, String> entry : videogame.getPlatforms().entrySet()){
                content += entry.getValue() + " ("+ entry.getKey() +")\n";
            }
            videogameActivity.addGameInfoRow(R.drawable.ic_baseline_calendar_today_24, videogameActivity.getResources().getString(R.string.gameinfo_category_release), content);
        }

        if(!videogame.getStoryline().isEmpty()){
            videogameActivity.addGameInfoRow(R.drawable.ic_baseline_menu_book_24, videogameActivity.getResources().getString(R.string.gameinfo_category_storyline), videogame.getStoryline());
        }
        if(!videogame.getSummary().isEmpty()){
            videogameActivity.addGameInfoRow(R.drawable.ic_baseline_library_books_24, videogameActivity.getResources().getString(R.string.gameinfo_category_summary), videogame.getSummary());
        }

    }

    @Override
    public void addToAwaited() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("videogame_id", String.valueOf(videogame.getId()));
        params.put("user", session_username);

        String url = videogameActivity.getResources().getString(R.string.urlAwaitedServlet);
        volleyRequestManager.doStringRequest(Request.Method.POST, url, params, null,
                VolleyRequestManager.getStringResponseListener(videogameActivity),
                VolleyRequestManager.getGenericErrorListener(videogameActivity, videogameActivity));
    }

    @Override
    public void addToPlayed(int score) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("videogame_id", String.valueOf(videogame.getId()));
        params.put("user", session_username);
        params.put("score", String.valueOf(score));

        String url = videogameActivity.getResources().getString(R.string.urlPlayedServlet);
        volleyRequestManager.doStringRequest(Request.Method.POST, url, params, null,
                VolleyRequestManager.getStringResponseListener(videogameActivity),
                VolleyRequestManager.getGenericErrorListener(videogameActivity, videogameActivity));
    }

    @Override
    public void share(String receiver_username) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("videogame_id", String.valueOf(videogame.getId()));
        params.put("sender", session_username);
        params.put("receiver", receiver_username);
        Log.d(TAG, "share: " + receiver_username);
        String url = videogameActivity.getResources().getString(R.string.urlReccomendationServlet);
        volleyRequestManager.doStringRequest(Request.Method.POST, url, params, null,
                VolleyRequestManager.getStringResponseListener(videogameActivity),
                VolleyRequestManager.getGenericErrorListener(videogameActivity, videogameActivity));
    }
}
