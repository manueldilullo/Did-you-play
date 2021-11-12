package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.HttpResponseHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.login.LoginControllerImpl;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Login;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.FragmentChangeListener;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.main.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment implements View.OnClickListener, HttpResponseHandler {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private CheckBox cbRememberMe;
    private TextView tvLoginError;

    private Button btSignin;
    private Button btToSignupFragment;

    private LoginControllerImpl loginController;
    private FragmentChangeListener fc;
    private Login login;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginController = new LoginControllerImpl(this);
        fc = (FragmentChangeListener) getActivity();

        etLoginUsername = (EditText) view.findViewById(R.id.etLoginUsername);
        etLoginPassword = (EditText) view.findViewById(R.id.etLoginPassword);
        cbRememberMe = (CheckBox) view.findViewById(R.id.cbRememberMe);
        tvLoginError = (TextView) view.findViewById(R.id.tvLoginError);

        btSignin = (Button) view.findViewById(R.id.btSignin);
        btToSignupFragment = (Button) view.findViewById(R.id.btToSignupFragment);

        btSignin.setOnClickListener(this);
        btToSignupFragment.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){
        // If btSignin is clicked it'll start login procedure
        if(v.getId() == R.id.btSignin) {
            Log.d(TAG, "onClick: Sign in button clicked");

            String username = etLoginUsername.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            login = new Login(username, password);
            loginController.onLogin(login);
        }

        // If btToSignupFragment is clicked it'll replace current fragment with Fragment SignupFragment
        if(v.getId() == R.id.btToSignupFragment){
            Log.d(TAG, "onClick: To Signup button clicked");
            fc.switchFragment(new SignupFragment(), null);
        }
    }

    @Override
    public void onStringRequestSuccess(String response) {
        Toast.makeText(getActivity(), "Logging in...", Toast.LENGTH_SHORT).show();

        SharedPreferences mPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = mPreferences.edit();
        prefEditor.putString("username", login.getUsername());
        prefEditor.putString("password", login.getPassword());

        if(cbRememberMe.isChecked()){
            prefEditor.putBoolean("loggedin", true);
        }
        prefEditor.apply();

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onJsonObjectRequestSuccess(JSONObject response) {}

    @Override
    public void onJsonArrayRequestSuccess(JSONArray response) {}

    @Override
    public void onFail(String response){
        if(response == null)
            response = getResources().getString(R.string.login_failed);

        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
        tvLoginError.setVisibility(View.VISIBLE);
        tvLoginError.setText(response);
    }
}