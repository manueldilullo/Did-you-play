package it.uniroma2.pjdm.manueldilullo.didyouplayed.entity;

import java.util.ArrayList;

public class CardItem {

    // Declaration of the variables
    private String title;
    private int icon_resource;
    private ArrayList<Videogame> itemList;

    // Constructor of the class
    // to initialize the variables
    public CardItem(String title, ArrayList<Videogame> itemList) {
        this.title = title;
        this.itemList = itemList;
    }

    public CardItem(String title, int icon_resource, ArrayList<Videogame> itemList){
        this(title, itemList);
        this.icon_resource = icon_resource;
    }

    // Getter and Setter methods
    // for each parameter
    public String getCardItemTitle(){
        return title;
    }

    public void setCardItemTitle(String title) {
        this.title = title;
    }

    public ArrayList<Videogame> getCardItemList() {
        return itemList;
    }

    public void setCardItemList(ArrayList<Videogame> itemList) {
        this.itemList = itemList;
    }

    public int getIcon() {
        return icon_resource;
    }

    public void setIcon(int icon_resource) {
        this.icon_resource = icon_resource;
    }
}
