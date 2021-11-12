package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.login.LoginActivity;

public class SettingsActivity extends AppCompatActivity {

    private Button btSignout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btSignout = (Button) findViewById(R.id.btSignout);
        btSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPreferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mPreferences.edit();
                mEditor.clear();
                mEditor.putBoolean("loggedin", false);
                mEditor.apply();

                Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}