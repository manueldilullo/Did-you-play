package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.FragmentChangeListener;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements FragmentChangeListener {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor prefEditor;

    public final static String TAG_LOGIN_FRAGMENT = "LOGIN";
    public final static String TAG_SIGNUP_FRAGMENT = "SIGNUP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPreferences = getSharedPreferences("login", MODE_PRIVATE);
        prefEditor = mPreferences.edit();

        boolean logged_in = mPreferences.getBoolean("loggedin", false);
        if(logged_in){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        switchFragment(new LoginFragment(), null);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void switchFragment(Fragment fragment, String title) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flLoginFragment, fragment);
        fragmentTransaction.commit();

        if(title != null)
            getSupportActionBar().setTitle(title);
    }
}