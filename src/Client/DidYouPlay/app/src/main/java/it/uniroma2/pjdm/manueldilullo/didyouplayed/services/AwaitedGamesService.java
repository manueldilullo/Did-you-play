package it.uniroma2.pjdm.manueldilullo.didyouplayed.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.HttpResponseHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.VolleyRequestManager;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame.VideogameActivity;

public class AwaitedGamesService extends Service implements HttpResponseHandler {
    private static final String TAG = AwaitedGamesService.class.getSimpleName();
    private static final String CHANNEL_ID = "DIDYOUPLAYED_CH";
    private final String last_notification_key = "last_notification";

    private String username;
    private String servletUrl;
    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;
    private VolleyRequestManager volleyRequestManager;

    @Override
    public void onCreate() {
        servletUrl = getString(R.string.urlAwaitedServlet);
        volleyRequestManager = new VolleyRequestManager(this);
        preferences = getSharedPreferences("notifications", MODE_PRIVATE);
        prefEditor = preferences.edit();
        createNotificationChannel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: MyAlarmService.onStartCommand()");
        if(username == null)
            username = intent.getExtras().getString("username");
        Log.d(TAG, "onStartCommand: User session: " +username);

        String url = servletUrl + "?user="+username;
        volleyRequestManager.doJsonArrayRequest(Request.Method.GET, url, null, null,
                VolleyRequestManager.getJSONArrayResponseListener(this),
                VolleyRequestManager.getGenericErrorListener(this, this));

        return START_NOT_STICKY;
    }


    public void sendNotification(String content, PendingIntent pendingIntent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle("Did you play?")
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(new Random().nextInt(100000), builder.build());
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onStringRequestSuccess(String response) {}

    @Override
    public void onJsonObjectRequestSuccess(JSONObject response) {}

    @Override
    public void onJsonArrayRequestSuccess(JSONArray response) {
        Log.d(TAG, "onJsonArrayRequestSuccess: " + response.toString());
        try {
            for(int i=0; i<response.length(); i++){
                // Extract videogames from the JSONArray response
                JSONObject jObj = response.getJSONObject(i);
                Videogame v = new Gson().fromJson(jObj.get("videogame").toString(), Videogame.class);

                // If the game was released before the last notification sent to the user, continue
                // otherwise, if the game is released more recently, send a notification to the user
                int missing_days = v.getMissingDays();
                // if game was released
                if(missing_days <= 0){
                    long millis_from_release = (- missing_days) * (24*60*60*1000);
                    long last_notification = preferences.getLong(last_notification_key, 0);
                    if(millis_from_release > last_notification){
                        // Intent that will redirect from notification to the Videogame page
                        Intent intent = new Intent(this, VideogameActivity.class);
                        intent.putExtra("videogame", jObj.get("videogame").toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this,  new Random().nextInt(100000), intent, 0);

                        //Send the notification
                        String textContent = getString(R.string.game_released_notification) + " " + v.getName();
                        sendNotification(textContent, pendingIntent);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        prefEditor.remove(last_notification_key);
        prefEditor.putLong(last_notification_key, System.currentTimeMillis());
        prefEditor.commit();
    }

    @Override
    public void onFail(String response) {
        Log.d(TAG, "onFail: " + response);
    }
}