package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.videogame;

import android.content.Context;

import com.android.volley.Request;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyRequestManager;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame.DialogShare;

public class DialogShareControllerImpl {

    private DialogShare fragment;
    private Context context;
    private VolleyRequestManager volleyRequestManager;

    public DialogShareControllerImpl(DialogShare fragment){
        this.fragment = fragment;
        context = fragment.getContext();
        volleyRequestManager = new VolleyRequestManager(context);
    }

    public void getUsers(){
        String url = context.getString(R.string.urlUserServlet);
        volleyRequestManager.doJsonArrayRequest(Request.Method.GET, url, null, null,
                VolleyRequestManager.getJSONArrayResponseListener(fragment),
                VolleyRequestManager.getGenericErrorListener(fragment, context));
    }
}
