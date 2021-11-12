package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame.VideogameActivity;

public class VideogamesListAdapter extends RecyclerView.Adapter<VideogamesListAdapter.VideogamesViewHolder> {

    private static final String TAG = VideogamesListAdapter.class.getSimpleName();

    private ArrayList<Videogame> videogames;
    private AppCompatActivity main;
    private Context context;
    private LayoutInflater inflater;

    public VideogamesListAdapter(AppCompatActivity main) {
        this.main = main;
        this.context = main;
        videogames = new ArrayList<Videogame>();
        // method to load videogames
        inflater = LayoutInflater.from(context);
    }

    public void add(Videogame videogame){
        videogames.add(videogame);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public VideogamesListAdapter.VideogamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cv_vertical_gamecard_view,parent,false);
        return new VideogamesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideogamesListAdapter.VideogamesViewHolder holder, int position) {
        holder.setData(videogames.get(position));
    }

    @Override
    public int getItemCount() {
        return videogames.size();
    }


    /*
    * View Holder
    * */
    class VideogamesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imgVideogame;
        protected Videogame game;

        public VideogamesViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imgVideogame = (ImageView) itemView.findViewById(R.id.imgGameCover);
        }

        public void setData(Videogame game) {
            this.game = game;
            Log.d(TAG, "setData: " + game.toString());
            Glide.with(main).load(game.getCover()).placeholder(R.drawable.ic_baseline_gamepad_filled_24).fitCenter().into(imgVideogame);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            Videogame videogame = videogames.get(pos);

            Intent intent = new Intent(main, VideogameActivity.class);
            intent.putExtra("videogame", (new Gson()).toJson(videogame));
            main.startActivity(intent);
        }


    }
}
