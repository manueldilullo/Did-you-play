package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.login;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.util.HashMap;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyRequestManager;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.UserRegister;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.login.SignupFragment;

public class SignupControllerImpl implements SignupController{
    private static final String TAG = SignupControllerImpl.class.getSimpleName();

    private VolleyRequestManager volleyRequestManager;
    private SignupFragment signupFragment;
    private Context context;

    public SignupControllerImpl(SignupFragment signupFragment) {
        this.signupFragment = signupFragment;
        context = signupFragment.getActivity();
        volleyRequestManager = new VolleyRequestManager(context);
    }

    @Override
    public void onRegister(UserRegister userRegister) {
        String name = userRegister.getName();
        String surname = userRegister.getSurname();
        String username = userRegister.getUsername();
        String email = userRegister.getEmail();
        String password = userRegister.getPassword();
        String confirmPassword = userRegister.getConfirmPassword();

        if (name.length() == 0 || surname.length() == 0 || email.length() == 0 || username.length() == 0) {
            signupFragment.onFail(signupFragment.getResources().getString(R.string.signup_missing_values));
            return;
        }
        if (password.length() < 8) {
            signupFragment.onFail(signupFragment.getResources().getString(R.string.password_too_short));
            return;
        }
        if (!password.equals(confirmPassword)) {
            signupFragment.onFail(signupFragment.getResources().getString(R.string.signup_password_dont_match));
            return;
        }

        String url = signupFragment.getResources().getString(R.string.urlUserServlet);
        HashMap<String, String> params = new Gson().fromJson(userRegister.toJSON().toString(), HashMap.class);
        params.put("action", context.getResources().getString(R.string.userservlet_action_registration));

        volleyRequestManager.doStringRequest(Request.Method.POST, url, params, null,
                VolleyRequestManager.getStringResponseListener(signupFragment),
                VolleyRequestManager.getGenericErrorListener(signupFragment, context));
    }
}
