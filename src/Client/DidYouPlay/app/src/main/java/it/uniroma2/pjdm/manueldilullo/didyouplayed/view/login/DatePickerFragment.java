package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    private Context context;
    private DatePickerDialog.OnDateSetListener listener;

    public DatePickerFragment(Context context, DatePickerDialog.OnDateSetListener listener){
        super();
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar mCalender = Calendar.getInstance();
        int year = mCalender.get(Calendar.YEAR);
        int month = mCalender.get(Calendar.MONTH);
        int dayOfMonth = mCalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new android.app.DatePickerDialog(context, listener, year, month, dayOfMonth);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        return dialog;
    }
}
