package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.reccomendation;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyRequestManager;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.reccomendations.ReccomendationsActivity;

public class ReccomendationsActivityControllerImpl implements ReccomendationsActivityController {

    private static final String TAG = ReccomendationsActivityControllerImpl.class.getSimpleName();

    private VolleyRequestManager volleyRequestManager;
    private SharedPreferences sharedPreferences;
    private ReccomendationsActivity notificationsActivity;
    private Context context;

    public ReccomendationsActivityControllerImpl(ReccomendationsActivity notificationsActivity){
        this.notificationsActivity = notificationsActivity;
        context = notificationsActivity;
        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        volleyRequestManager = new VolleyRequestManager(context);
    }

    @Override
    public void getReccomendations() {
        String receiver = sharedPreferences.getString("username", "");
        String url = context.getResources().getString(R.string.urlReccomendationServlet)+"?receiver="+receiver;

        volleyRequestManager.doJsonArrayRequest(Request.Method.GET, url, null, null,
                VolleyRequestManager.getJSONArrayResponseListener(notificationsActivity),
                VolleyRequestManager.getGenericErrorListener(notificationsActivity, context));
    }
}
