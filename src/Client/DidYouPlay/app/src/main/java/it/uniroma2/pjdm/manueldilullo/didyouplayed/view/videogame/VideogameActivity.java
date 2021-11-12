package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.HttpResponseHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.videogame.VideogameActivityControllerImpl;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;

public class VideogameActivity extends AppCompatActivity implements View.OnClickListener, NoticeDialogListener, HttpResponseHandler {

    private static final String TAG = VideogameActivity.class.getSimpleName();
    private Videogame videogame;
    private boolean clicked = false;

    private ImageView ivVideogameCover;
    private TextView tvVideogameName;
    private ImageView ivRatingCircle;
    private TextView tvRatingScore;

    private FloatingActionButton fabMaster;
    private ArrayList<FloatingActionButton> fabSlave;

    private VideogameActivityControllerImpl controller;
    private ViewGroup viewGroup;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videogame);

        Intent intent = getIntent();
        videogame = new Gson().fromJson(intent.getStringExtra("videogame"), Videogame.class);
        controller = new VideogameActivityControllerImpl(this);

        Log.d(TAG, "onCreate: " + videogame.toString());

        ivVideogameCover = (ImageView) findViewById(R.id.ivVideogameCover);
        tvVideogameName = (TextView) findViewById(R.id.tvVideogameName);
        ivRatingCircle = (ImageView) findViewById(R.id.ivRatingCircle);
        tvRatingScore = (TextView) findViewById(R.id.tvRatingScore);

        fabMaster = (FloatingActionButton) findViewById(R.id.fabMaster);
        fabSlave = new ArrayList<FloatingActionButton>();
        /*
         * If game is released, can't add it to awaited list
         * otherwhise you can't add it to played list
         */
        if(videogame.isReleased()){
            fabSlave.add((FloatingActionButton) findViewById(R.id.fabPlayed));
        }else{
            fabSlave.add((FloatingActionButton) findViewById(R.id.fabAwait));
        }
        fabSlave.add((FloatingActionButton) findViewById(R.id.fabShare));

        // Setting onClickListeners
        fabMaster.setOnClickListener(this);
        for(FloatingActionButton fab : fabSlave)
            fab.setOnClickListener(this);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewGroup = (ViewGroup) findViewById(R.id.linearMain);

        setupView();
        Log.d(TAG, "onCreate: VideogameActivity created");
    }

    public Videogame getVideogame(){
        return videogame;
    }

    @Override
    protected void onStart() {
        super.onStart();
        toggleFab();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fabMaster){
            Log.d(TAG, "onClick: fabMaster button pressed");
            toggleFab();
        }
        if(v.getId() == R.id.fabAwait){
            Log.d(TAG, "onClick: fabAwait button pressed");
            controller.addToAwaited();
        }
        if(v.getId() == R.id.fabPlayed){
            Log.d(TAG, "onClick: fabPlayed button pressed");
            DialogRating dialog = new DialogRating();
            dialog.show(getSupportFragmentManager(), getResources().getString(R.string.dialog_rating_tag));
        }
        if(v.getId() == R.id.fabShare){
            Log.d(TAG, "onClick: fabShare button pressed");
            DialogShare dialog = new DialogShare();
            dialog.show(getSupportFragmentManager(), getResources().getString(R.string.dialog_rating_tag));
        }
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if(dialog instanceof DialogRating) {
            DialogRating dialogRating = (DialogRating) dialog;
            int score = (int) dialogRating.getRating();
            controller.addToPlayed(score);
        }
        if(dialog instanceof DialogShare) {
            ArrayList<String> checked = ((DialogShare) dialog).getChecked();
            for(String user : checked){
                controller.share(user);
            }
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    private void toggleFab(){
        int visibility;
        Animation masterAnim;
        Animation slaveAnim;
        if(clicked){
            visibility = View.VISIBLE;
            masterAnim =  AnimationUtils.loadAnimation(this, R.anim.rotate_open);
            slaveAnim =  AnimationUtils.loadAnimation(this, R.anim.from_bottom_animate);
        }else{
            visibility = View.INVISIBLE;
            masterAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_close);
            slaveAnim = AnimationUtils.loadAnimation(this, R.anim.to_bottom_animate);
        }

        fabMaster.startAnimation(masterAnim);

        for(FloatingActionButton fab : fabSlave) {
            fab.setVisibility(visibility);
            fab.startAnimation(slaveAnim);
        }

        clicked = !clicked;
    }

    private void setupView(){
        // Getting Videogame Cover
        Glide.with(this).load(videogame.getCover()).placeholder(R.drawable.ic_baseline_gamepad_filled_24).fitCenter().
                into(ivVideogameCover);

        // Setting up videogame name TextView
        tvVideogameName.setText(videogame.getName());

        // Printing Rating score
        int color_code = R.color.yellow;
        if(videogame.getRating() < 50) {
            color_code = R.color.red;
        }
        else {
            if (videogame.getRating() > 80)
                color_code = R.color.green;
        }
        ivRatingCircle.setColorFilter(ContextCompat.getColor(this, color_code));
        tvRatingScore.setText(String.valueOf(videogame.getRating()));

        controller.addDetailsRows();
    }

    public void addGameInfoRow(int drawable, String categoryName, String content){
        View v = layoutInflater.inflate(R.layout.ll_gameinfo_row, null);

        ImageView ivCategoryIcon = (ImageView) v.findViewById(R.id.ivCategoryIcon);
        ivCategoryIcon.setImageResource(drawable);
        ivCategoryIcon.setColorFilter(ContextCompat.getColor(this, R.color.magenta));

        TextView tvCategoryName = (TextView) v.findViewById(R.id.tvCategoryName);
        tvCategoryName.setText(categoryName);

        TextView tvCategoryContent = (TextView) v.findViewById(R.id.tvCategoryContent);
        tvCategoryContent.setText(content);

        viewGroup.addView(v);
    }

    @Override
    public void onStringRequestSuccess(String response) {
        response = response.equals("") ? getResources().getString(R.string.response_successfully_added) : response;
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onJsonObjectRequestSuccess(JSONObject response) {}

    @Override
    public void onJsonArrayRequestSuccess(JSONArray response) {}

    @Override
    public void onFail(String response){
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }
}