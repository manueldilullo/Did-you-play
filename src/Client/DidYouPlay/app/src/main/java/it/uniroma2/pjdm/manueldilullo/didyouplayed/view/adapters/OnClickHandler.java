package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.adapters;

import androidx.recyclerview.widget.RecyclerView;

public interface OnClickHandler<T extends RecyclerView.Adapter> {
        public void clickHandle(int position, T adapter);
        public void longClickHandle(int position, T adapter);
}
