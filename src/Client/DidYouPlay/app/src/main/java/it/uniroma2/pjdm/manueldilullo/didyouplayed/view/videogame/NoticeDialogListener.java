package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.videogame;

import androidx.fragment.app.DialogFragment;

/* The activity that creates an instance of this dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it. */
public interface NoticeDialogListener {
    public void onDialogPositiveClick(DialogFragment dialog);
    public void onDialogNegativeClick(DialogFragment dialog);
}