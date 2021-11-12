package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.login;

import android.content.Context;

import com.android.volley.Request;

import java.util.HashMap;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.BasicAuth;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyRequestManager;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Login;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.login.LoginFragment;

public class LoginControllerImpl implements LoginController {
    private static final String TAG = LoginControllerImpl.class.getSimpleName();

    private VolleyRequestManager volleyRequestManager;
    private LoginFragment loginFragment;
    private Context context;

    public LoginControllerImpl(LoginFragment loginFragment){
        this.loginFragment = loginFragment;
        context = loginFragment.getActivity();
        volleyRequestManager = new VolleyRequestManager(context);
    }


    @Override
    public void onLogin(Login login) {
        if (login.getUsername().length() == 0) {
            loginFragment.onFail(loginFragment.getResources().getString(R.string.login_missing_username));
            return;
        }
        if (login.getPassword().length() < 8) {
            loginFragment.onFail(loginFragment.getResources().getString(R.string.password_too_short));
            return;
        }
        String url = loginFragment.getResources().getString(R.string.urlUserServlet);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("action", context.getResources().getString(R.string.userservlet_action_authorization));

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", BasicAuth.basicAuthenticationToken(login));

        volleyRequestManager.doStringRequest(Request.Method.POST, url, params, headers,
                VolleyRequestManager.getStringResponseListener(loginFragment),
                VolleyRequestManager.getGenericErrorListener(loginFragment, context));
    }
}
