package it.uniroma2.pjdm.manueldilullo.didyouplayed.view;

import androidx.fragment.app.Fragment;

public interface FragmentChangeListener {

    /**
     * This method opens a new Fragment using FragmentTransaction
     *  and it changes SupportActionBar title
     * @param fragment Fragment.
     */
    public void switchFragment(Fragment fragment, String title);
}
