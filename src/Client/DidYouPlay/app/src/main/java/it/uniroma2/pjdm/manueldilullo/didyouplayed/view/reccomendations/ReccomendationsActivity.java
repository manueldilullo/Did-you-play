package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.reccomendations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.HttpResponseHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.reccomendation.ReccomendationsActivityControllerImpl;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Reccomendation;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters.ListAdapter;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters.OnClickHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame.VideogameActivity;

public class ReccomendationsActivity extends AppCompatActivity implements HttpResponseHandler, OnClickHandler<ListAdapter> {

    private static final String TAG = ReccomendationsActivity.class.getSimpleName();

    private RecyclerView rvNotifications;
    private ListAdapter adapter;
    private ReccomendationsActivityControllerImpl notificationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Toolbar main_toolbar = findViewById(R.id.tbMainToolbar);
        main_toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(main_toolbar);

        rvNotifications = findViewById(R.id.rvReccomendations);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter(this);
        adapter.setOnClickHandler(this);
        rvNotifications.setAdapter(adapter);

        notificationController = new ReccomendationsActivityControllerImpl(this);
        notificationController.getReccomendations();

        Log.d(TAG, "onCreate: created fragment");
    }

    @Override
    public void onStringRequestSuccess(String response) {}

    @Override
    public void onJsonObjectRequestSuccess(JSONObject response) {}

    @Override
    public void onJsonArrayRequestSuccess(JSONArray response) {
        try {
            for(int i=0; i<response.length(); i++){
                JSONObject jObj = response.getJSONObject(i);
                String sender = jObj.getString("sender");
                String receiver = jObj.getString("receiver");
                String created_at = jObj.getString("created_at");
                Videogame videogame = new Gson().fromJson(jObj.getJSONObject("videogame").toString(), Videogame.class);
                adapter.add(new Reccomendation(videogame, sender, receiver, created_at));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFail(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clickHandle(int position, ListAdapter adapter) {
        Videogame v = adapter.getItem(position).getVideogame();
        Intent i = new Intent(this, VideogameActivity.class);
        i.putExtra("videogame", (new Gson()).toJson(v));
        startActivity(i);
    }

    @Override
    public void longClickHandle(int position, ListAdapter adapter) {}
}