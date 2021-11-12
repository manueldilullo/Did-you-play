package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.HttpResponseHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.main.PopularControllerImpl;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters.VideogamesListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularFragment extends Fragment implements HttpResponseHandler {

    private final String TAG = PopularFragment.class.getSimpleName();

    private PopularControllerImpl popularController;
    private RecyclerView rvPopular;
    private VideogamesListAdapter adapter;

    public PopularFragment() {
        // Required empty public constructor
    }

    public static PopularFragment newInstance() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        rvPopular = view.findViewById(R.id.rvPopular);
        rvPopular.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new VideogamesListAdapter((MainActivity) getActivity());
        rvPopular.setAdapter(adapter);

        popularController = new PopularControllerImpl(this);
        popularController.getPopular();

        Log.d(TAG, "onCreate: created fragment");

        return view;
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
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFail(String response){
        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG);
    }
}