package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;

public class VideogameHorizontalListAdapter extends RecyclerView.Adapter<VideogameHorizontalListAdapter.VideogameHorizontalViewHolder> {

    private static final String TAG = VideogameHorizontalListAdapter.class.getSimpleName();

    OnClickHandler mCallback;
    private ArrayList<Videogame> itemList;
    private AppCompatActivity main;
    private Context context;
    private LayoutInflater inflater;

    public VideogameHorizontalListAdapter(AppCompatActivity main) {
        this.main = main;
        this.context = main;
        itemList = new ArrayList<Videogame>();
        // method to load videogames
        inflater = LayoutInflater.from(context);
    }


    public void setOnClickHandler(OnClickHandler mCallback){ this.mCallback = mCallback; }

    public void add(ArrayList<Videogame> itemList){
        this.itemList.addAll(itemList);
    }

    public void add(Videogame videogame){
        itemList.add(videogame);
        notifyDataSetChanged();
    }

    public void delete(int position){
        itemList.remove(position);
        notifyDataSetChanged();
    }

    public Videogame getItem(int position){
        return itemList.get(position);
    }

    @NonNull
    @Override
    public VideogameHorizontalListAdapter.VideogameHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cv_horizontal_gamecard_view,parent,false);
        return new VideogameHorizontalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideogameHorizontalListAdapter.VideogameHorizontalViewHolder holder, int position) {
        holder.setData(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    /*
     * View Holder
     * */
    class VideogameHorizontalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgVideogame;
        private Videogame game;

        public VideogameHorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imgVideogame = (ImageView) itemView.findViewById(R.id.img_child_item);
        }

        public void setData(Videogame game) {
            this.game = game;
            Log.d(TAG, "setData: " + game.toString());
            Glide.with(main).load(game.getCover()).placeholder(R.drawable.ic_baseline_gamepad_filled_24).fitCenter().into(imgVideogame);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            mCallback.clickHandle(pos, VideogameHorizontalListAdapter.this);
        }

        @Override
        public boolean onLongClick(View v) {
            int pos = getLayoutPosition();
            mCallback.longClickHandle(pos, VideogameHorizontalListAdapter.this);
            return true;
        }
    }
}
