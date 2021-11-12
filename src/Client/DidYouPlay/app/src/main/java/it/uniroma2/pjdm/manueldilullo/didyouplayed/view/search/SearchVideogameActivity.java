package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.HttpResponseHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.search.VideogameSearchControllerImpl;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters.VideogamesHidableListAdapter;

public class SearchVideogameActivity extends AppCompatActivity implements HttpResponseHandler {

    private VideogameSearchControllerImpl controller;
    private RecyclerView rvSearchResults;
    private VideogamesHidableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_videogame);

        rvSearchResults = findViewById(R.id.rvSearchResults);
        rvSearchResults.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new VideogamesHidableListAdapter(this);
        rvSearchResults.setAdapter(adapter);

        controller = new VideogameSearchControllerImpl(this);
        controller.getAllGames();
    }

    @Override
    public void onStringRequestSuccess(String response) {}

    @Override
    public void onJsonObjectRequestSuccess(JSONObject response) {}

    @Override
    public void onJsonArrayRequestSuccess(JSONArray response) {
        for(int i=0; i<response.length(); i++){
            try {
                JSONObject jObj = response.getJSONObject(i);
                Videogame v = new Gson().fromJson(jObj.toString(), Videogame.class);
                adapter.add(v);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFail(String response){
        Toast.makeText(this, response, Toast.LENGTH_LONG);
    }
}