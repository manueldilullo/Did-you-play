package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame.DialogShare;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UsersListViewHolder> {

    private static final String TAG = UsersListAdapter.class.getSimpleName();

    private Activity main;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> itemList;
    private ArrayList<String> checkedList;

    private EditText searchBar;

    public UsersListAdapter(Activity main) {
        this.main = main;
        this.context = main;
        itemList = new ArrayList<String>();
        checkedList = new ArrayList<String>();
        inflater = LayoutInflater.from(context);
    }

    public void add(String username){
        itemList.add(username);
        notifyDataSetChanged();
    }

    public ArrayList<String> getChecked(){
        return checkedList;
    }

    public void setSearchBar(EditText searchBar){ this.searchBar = searchBar; }

    @NonNull
    @NotNull
    @Override
    public UsersListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        EditText etSearchUsers = parent.findViewById(R.id.etSearchUser);
        View v = inflater.inflate(R.layout.cv_userslist_row, parent,false);
        return new UsersListViewHolder(v, etSearchUsers);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UsersListViewHolder holder, int position) {
        holder.setData(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public ArrayList<String> getItemList(){
        return itemList;
    }


    public class UsersListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher{

        public String username;

        public CardView cvUserRow;
        public CheckBox cbUser;
        public EditText etSearchUser;

        public UsersListViewHolder(View itemView, EditText etSearchUser) {
            super(itemView);
            cvUserRow = (CardView) itemView.findViewById(R.id.cvUserRow);
            cbUser = (CheckBox) itemView.findViewById(R.id.cbUser);

            cbUser.setOnClickListener(this);
            searchBar.addTextChangedListener(this);
        }

        public void setData(String username){
            this.username = username;
            cbUser.setText(username);
            boolean isChecked = checkedList.contains(username);
            cbUser.setChecked(isChecked);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(count == 0)
                cvUserRow.setVisibility(View.VISIBLE);
            Log.d("UsersListAdapter", "onTextChanged: " + s.toString());
            String text = s.toString().toLowerCase();
            String userName = username.toLowerCase();
            if(userName.contains(text))
                cvUserRow.setVisibility(View.VISIBLE);
            else
                cvUserRow.setVisibility(View.GONE);
        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            if(cbUser.isChecked()){
                checkedList.add(itemList.get(pos));
            }else{
                checkedList.remove(itemList.get(pos));
            }
        }
    }
}
