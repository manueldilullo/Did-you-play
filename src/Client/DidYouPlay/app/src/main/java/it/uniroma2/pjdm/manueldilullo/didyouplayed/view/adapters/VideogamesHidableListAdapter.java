package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame.VideogameActivity;

public class VideogamesHidableListAdapter extends RecyclerView.Adapter<VideogamesHidableListAdapter.VideogamesHidableViewHolder> {

    private static final String TAG = VideogamesHidableListAdapter.class.getSimpleName();

    private ArrayList<Videogame> videogames;
    private AppCompatActivity main;
    private Context context;
    private LayoutInflater inflater;

    public VideogamesHidableListAdapter(AppCompatActivity main) {
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
    public VideogamesHidableListAdapter.VideogamesHidableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cv_vertical_gamecard_view,parent,false);
        return new VideogamesHidableViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideogamesHidableListAdapter.VideogamesHidableViewHolder holder, int position) {
        holder.setData(videogames.get(position));
    }

    @Override
    public int getItemCount() {
        return videogames.size();
    }


    /*
     * View Holder
     * */
    class VideogamesHidableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher{

        public CardView cvGameCard;
        public ImageView imgVideogame;
        public EditText etSearchInput;
        private Videogame game;

        public VideogamesHidableViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imgVideogame = (ImageView) itemView.findViewById(R.id.imgGameCover);
            cvGameCard = (CardView) itemView.findViewById(R.id.cvGameCard);
            etSearchInput = (EditText) main.findViewById(R.id.etSearchInput);

            cvGameCard.setVisibility(View.GONE);
            etSearchInput.addTextChangedListener(this);
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


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = s.toString().toLowerCase();
            String gameName = game.getName().toLowerCase();
            if(gameName.contains(text))
                cvGameCard.setVisibility(View.VISIBLE);
            else
                cvGameCard.setVisibility(View.GONE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
