package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Reccomendation;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Videogame;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    OnClickHandler<ListAdapter> mCallback;
    private ArrayList<Reccomendation> itemList;
    private AppCompatActivity main;
    private Context context;
    private LayoutInflater inflater;

    public ListAdapter(AppCompatActivity main) {
        this.main = main;
        this.context = main;
        itemList = new ArrayList<Reccomendation>();
        inflater = LayoutInflater.from(context);
    }

    public void add(Reccomendation notification){
        itemList.add(notification);
        notifyDataSetChanged();
    }

    public void add(ArrayList<Reccomendation> notifications){
        itemList.addAll(notifications);
        notifyDataSetChanged();
    }

    public Reccomendation getItem(int position){
        return itemList.get(position);
    }

    public void setOnClickHandler(OnClickHandler<ListAdapter> mCallback){ this.mCallback = mCallback; }

    @NonNull
    @NotNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cv_notification_row,parent,false);
        return new ListAdapter.ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListViewHolder holder, int position) {
        holder.setData(itemList.get(position));
    }

    @Override
    public int getItemCount() { return itemList.size(); }



    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Videogame videogame;
        public CardView cvReccomendationRow;
        public TextView tvReccomendationMessage;
        public TextView tvReccomendationDate;
        public ImageView ivGameIcon;

        public ListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            cvReccomendationRow = (CardView) itemView.findViewById(R.id.cvReccomendationRow);
            tvReccomendationMessage = (TextView) itemView.findViewById(R.id.tvReccomendationMessage);
            tvReccomendationDate = (TextView) itemView.findViewById(R.id.tvReccomendationDate);
            ivGameIcon = (ImageView) itemView.findViewById(R.id.ivGameIcon);

            cvReccomendationRow.setOnClickListener(this);
        }

        public void setData(Reccomendation reccomendation){
            videogame = reccomendation.getVideogame();
            Glide.with(main).load(videogame.getCover())
                    .placeholder(R.drawable.ic_baseline_gamepad_filled_24)
                    .fitCenter().into(ivGameIcon);
            String message = reccomendation.getSender() + " " +context.getString(R.string.reccomendation_message);
            tvReccomendationMessage.setText(message);
            tvReccomendationDate.setText(reccomendation.getCreated_at());
        }

        @Override
        public void onClick(View v) {
            Log.d("ListAdapter", "onClick: " + videogame.getName());
            if(mCallback != null){
                int pos = getLayoutPosition();
                mCallback.clickHandle(pos, ListAdapter.this);
            }
        }
    }
}
