package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.CardItem;

public class CardListAdapter extends RecyclerView
        .Adapter<CardListAdapter.CardViewHolder> {

    // An object of RecyclerView.RecycledViewPool
    // is created to share the Views
    // between the child and
    // the parent RecyclerViews
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    private OnClickHandler clickHandler;
    private ArrayList<CardItem> itemList;
    private AppCompatActivity main;
    private Context context;
    private LayoutInflater inflater;

    public CardListAdapter(AppCompatActivity main, OnClickHandler clickHandler) {
        this.main = main;
        this.context = main;
        this.clickHandler = clickHandler;

        itemList = new ArrayList<CardItem>();
        // method to load videogames
        inflater = LayoutInflater.from(context);
    }

    public CardListAdapter(AppCompatActivity main) {
        this.main = main;
        this.context = main;
        itemList = new ArrayList<CardItem>();
        // method to load videogames
        inflater = LayoutInflater.from(context);
    }

    public void add(CardItem cardItem){
        itemList.add(cardItem);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Here we inflate the corresponding
        // layout of the parent item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_horizontal_list, viewGroup, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, int position) {
        // Create an instance of the CardItem
        // class for the given position
        CardItem cardItem = itemList.get(position);

        // For the created instance,
        // get the title and set it
        // as the text for the TextView
        cardViewHolder.cardItemTitle.setText(cardItem.getCardItemTitle());

        // Create a layout manager
        // to assign a layout
        // to the RecyclerView.
        // Here we have assigned the layout
        // as LinearLayout with vertical orientation
        GridLayoutManager layoutManager = new GridLayoutManager(cardViewHolder.childRecyclerView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);

        // Since this is a nested layout, so
        // to define how many child items
        // should be prefetched when the
        // child RecyclerView is nested
        // inside the parent RecyclerView,
        // we use the following method
        layoutManager.setInitialPrefetchItemCount(cardItem.getCardItemList().size());

        // Create an instance of the child
        // item view adapter and set its
        // adapter, layout manager and RecyclerViewPool
        VideogameHorizontalListAdapter childItemAdapter = new VideogameHorizontalListAdapter(main);
        cardViewHolder.childRecyclerView.setLayoutManager(layoutManager);
        cardViewHolder.childRecyclerView.setAdapter(childItemAdapter);
        cardViewHolder.childRecyclerView.setRecycledViewPool(viewPool);
        childItemAdapter.setOnClickHandler(clickHandler);

        childItemAdapter.add(cardItem.getCardItemList());
    }

    // This method returns the number
    // of items we have added in the
    // CardItemList i.e. the number
    // of instances we have created
    // of the CardItemList
    @Override
    public int getItemCount(){ return itemList.size(); }

    // This class is to initialize
    // the Views present in
    // the parent RecyclerView
    class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView cardItemTitle;
        private RecyclerView childRecyclerView;

        CardViewHolder(final View itemView) {
            super(itemView);
            cardItemTitle = itemView.findViewById(R.id.tvCardTitle);
            childRecyclerView = itemView.findViewById(R.id.rvChildRecyclerview);
        }
    }
}
