package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.HttpResponseHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.main.AwaitedControllerImpl;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.CardItem;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters.CardListAdapter;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters.OnClickHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters.VideogameHorizontalListAdapter;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame.VideogameActivity;

public class AwaitedFragment extends Fragment implements OnClickHandler<VideogameHorizontalListAdapter>, HttpResponseHandler {


    private final String TAG = AwaitedFragment.class.getSimpleName();

    private String title;

    private AwaitedControllerImpl controller;
    private CardListAdapter cardListAdapter;
    private RecyclerView parentRecyclerViewItem;
    private SwipeRefreshLayout swipeRefreshLayout;

    public AwaitedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: created fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        controller = new AwaitedControllerImpl(this);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_awaited, container, false);

        // Initialize the recycler view
        parentRecyclerViewItem = view.findViewById(R.id.rvAwaitedCards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        cardListAdapter = new CardListAdapter((AppCompatActivity) getActivity(), this);
        parentRecyclerViewItem.setAdapter(cardListAdapter);
        parentRecyclerViewItem.setLayoutManager(layoutManager);

        controller.getAwaited();

        // Setup a swipeRefreshLayout with Swipe-to-Update functionality
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).switchFragment(AwaitedFragment.this, getString(R.string.fragment_awaited_title));
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onStringRequestSuccess(String response) {
        if(response.isEmpty())
            response = "Successfully deleted";
        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onJsonObjectRequestSuccess(JSONObject response) {}

    @Override
    public void onJsonArrayRequestSuccess(JSONArray response) {
        Log.d(TAG, "onJsonArrayRequestSuccess: " + response.toString());

        HashMap<Integer, ArrayList<Videogame>> map = controller.sortAwaited(response);
        // TreeMap to store sorted values of HashMap
        TreeMap<Integer, ArrayList<Videogame>> sorted = new TreeMap<>(map);

        for(Map.Entry<Integer, ArrayList<Videogame>> entry : sorted.descendingMap().entrySet()) {
            String title = "Out in: " + entry.getKey().toString() + " days";
            cardListAdapter.add(new CardItem(title, entry.getValue()));
        }

    }

    @Override
    public void onFail(String response){
        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clickHandle(int position, VideogameHorizontalListAdapter adapter) {
        Log.d(TAG, "clickHandle: ");
        Videogame videogame = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), VideogameActivity.class);
        intent.putExtra("videogame", (new Gson()).toJson(videogame));
        startActivity(intent);
    }

    @Override
    public void longClickHandle(int position, VideogameHorizontalListAdapter adapter) {
        Log.d(TAG, "LongClickHandle: ");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(R.string.dialog_delete_message);

        alertDialogBuilder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.removeAwaited(adapter.getItem(position).getId());
                adapter.delete(position);
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}