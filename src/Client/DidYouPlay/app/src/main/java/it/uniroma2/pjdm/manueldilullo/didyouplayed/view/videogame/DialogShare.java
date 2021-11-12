package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.HttpResponseHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.videogame.DialogShareControllerImpl;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters.OnClickHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters.UsersListAdapter;

public class DialogShare extends DialogFragment implements HttpResponseHandler {
    public static final String TAG = DialogShare.class.getSimpleName();

    private EditText etSearchUser;
    private RecyclerView rvUsersList;
    private UsersListAdapter usersListAdapter;
    private DialogShareControllerImpl dialogShareController;

    // Use this instance of the interface to deliver action events
    NoticeDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_share, null);

        rvUsersList = (RecyclerView) view.findViewById(R.id.rvUsersList);
        usersListAdapter = new UsersListAdapter((AppCompatActivity) getActivity());
        rvUsersList.setAdapter(usersListAdapter);
        rvUsersList.setLayoutManager(new LinearLayoutManager(getActivity()));

        etSearchUser = view.findViewById(R.id.etSearchUser);
        usersListAdapter.setSearchBar(etSearchUser);

        dialogShareController = new DialogShareControllerImpl(this);
        dialogShareController.getUsers();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(R.string.dialog_share_title)
                .setView(view)
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(DialogShare.this);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(DialogShare.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public ArrayList<String> getChecked(){ return usersListAdapter.getChecked(); }

    @Override
    public void onStringRequestSuccess(String response) {}

    @Override
    public void onJsonObjectRequestSuccess(JSONObject response) {}

    @Override
    public void onJsonArrayRequestSuccess(JSONArray response) {
        try {
            for(int i=0; i<response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                usersListAdapter.add(jsonObject.getString("username"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail(String response) {
        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
