package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.services.AwaitedGamesService;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.FragmentChangeListener;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.reccomendations.ReccomendationsActivity;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.search.SearchVideogameActivity;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements FragmentChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private SharedPreferences mPreferences;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences("login", MODE_PRIVATE);
        username = mPreferences.getString("username", "");

        startAwaitingService();

        Toolbar main_toolbar = findViewById(R.id.tbMainToolbar);
        main_toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(main_toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.popular:
                        switchFragment(new PopularFragment(), getString(R.string.fragment_popular_title));
                        return true;
                    case R.id.played:
                        switchFragment(new PlayedFragment(), getString(R.string.fragment_played_title));
                        return true;
                    case R.id.awaited:
                        switchFragment(new AwaitedFragment(), getString(R.string.fragment_awaited_title));
                        return true;
                }
                return false;
            }
        });
        switchFragment(new PopularFragment(), getResources().getString(R.string.app_name));
    }

    public void startAwaitingService(){
        Intent intent = new Intent(MainActivity.this, AwaitedGamesService.class);
        intent.putExtra("username", username);

        pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        long interval = 24*60*60*1000;
        assert alarmManager != null;
        // Repeat every day at 00:00:00
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
        Log.d(TAG, "startAwaitingService: Starting Service ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_notifications) {
            Intent i = new Intent(this, ReccomendationsActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_search) {
            Intent i = new Intent(this, SearchVideogameActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param fragment Fragment.
     * @param title
     *
     * This implementation switches the current fragment without destroying it
     * and without add a new fragment more than one time
     */
    @Override
    public void switchFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment activeFragment = getVisibleFragment();
        if(activeFragment != null){
            fragmentTransaction.hide(activeFragment);
        }

        if(fragment.isAdded())
            fragmentTransaction.show(fragment);
        else
            fragmentTransaction.add(R.id.flMainFragment, fragment);

        fragmentTransaction.commit();

        if(title != null)
            getSupportActionBar().setTitle(title);
    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
}