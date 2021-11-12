package it.uniroma2.pjdm.manueldilullo.didyouplayed.view.login;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.Calendar;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.R;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.HttpResponseHandler;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.login.SignupControllerImpl;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.UserRegister;
import it.uniroma2.pjdm.manueldilullo.didyouplayed.view.FragmentChangeListener;


public class SignupFragment extends Fragment implements View.OnClickListener, HttpResponseHandler {

        private static final String TAG = SignupFragment.class.getSimpleName();
        private View view;

        private EditText etSignupName;
        private EditText etSignupSurname;
        private EditText etSignupUsername;
        private EditText etSignupEmail;
        private EditText etSignupPassword;
        private EditText etSignupConfirmPassword;

        private Button btSignupPickDate;
        private TextView tvBirthdate;

        private RadioGroup rgGender;
        private RadioButton rbSignupMale;
        private RadioButton rbSignupFemale;
        private RadioButton rbSignupOther;

        private TextView tvSignupError;
        private Button btSignup;
        private Button btToLoginFragment;

        private SignupControllerImpl signupController;
        private FragmentChangeListener fc;
        private DatePickerDialog.OnDateSetListener dateSetListener;
        private String dataPickerTAG = "DATE PICK";

        public SignupFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_signup, container, false);

            signupController = new SignupControllerImpl(this);
            fc = (FragmentChangeListener) getActivity();

            etSignupName = (EditText) view.findViewById(R.id.etSignupName);
            etSignupSurname = (EditText) view.findViewById(R.id.etSignupSurname);
            etSignupUsername = (EditText) view.findViewById(R.id.etSignupUsername);
            etSignupEmail = (EditText) view.findViewById(R.id.etSignupEmail);
            etSignupPassword = (EditText) view.findViewById(R.id.etSignupPassword);
            etSignupConfirmPassword = (EditText) view.findViewById(R.id.etSignupConfirmPassword);

            btSignupPickDate = (Button) view.findViewById(R.id.btSignupPickDate);
            tvBirthdate = (TextView) view.findViewById(R.id.tvBirthdate);

            rgGender = (RadioGroup) view.findViewById(R.id.rgGender);
            rbSignupMale = (RadioButton) view.findViewById(R.id.rbSignupMale);
            rbSignupFemale = (RadioButton) view.findViewById(R.id.rbSignupFemale);
            rbSignupOther = (RadioButton) view.findViewById(R.id.rbSignupOther);

            tvSignupError = (TextView) view.findViewById(R.id.tvSignupError);
            btSignup = (Button) view.findViewById(R.id.btSignup);
            btToLoginFragment = (Button) view.findViewById(R.id.btToLoginFragment);

            btSignupPickDate.setOnClickListener(this);
            btSignup.setOnClickListener(this);
            btToLoginFragment.setOnClickListener(this);

            dateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String selectedDate = (new Date(cal.getTimeInMillis())).toString();

                    tvBirthdate.setText(selectedDate);
                }
            };

            return view;
        }

        @Override
        public void onClick(View v){
            // If btSignupPickDate is clicked, prompt a DatePickerDialog
            if(v.getId() == R.id.btSignupPickDate){
                Log.d(TAG, "onClick: Pick birthdate button clicked");
                DatePickerFragment datePickerFragment = new DatePickerFragment(getActivity(), dateSetListener);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), dataPickerTAG);
            }

            // If btSignin is clicked it'll start login procedure
            if(v.getId() == R.id.btSignup) {
                Log.d(TAG, "onClick: Sign in button clicked");
                UserRegister userRegister = getUserRegisterFromForm();
                Log.d(TAG, "onClick: " + userRegister.toString());
                signupController.onRegister(userRegister);
            }

            // If btToSignupFragment is clicked it'll replace current fragment with Fragment SignupFragment
            if(v.getId() == R.id.btToLoginFragment){
                Log.d(TAG, "onClick: To login button clicked");
                fc.switchFragment(new LoginFragment(), null);
            }
        }


        private UserRegister getUserRegisterFromForm(){
            // Extracting UserRegister informations from form
            String name = etSignupName.getText().toString().trim();
            String surname = etSignupSurname.getText().toString().trim();
            String username = etSignupUsername.getText().toString().trim();
            String password = etSignupPassword.getText().toString().trim();
            String confirmPassword = etSignupConfirmPassword.getText().toString().trim();
            String email = etSignupEmail.getText().toString().trim();

            String birthdate = tvBirthdate.getText().toString();
            String gender = "other";
            if(rgGender.getCheckedRadioButtonId() != -1){
                RadioButton checked = (RadioButton) view.findViewById(rgGender.getCheckedRadioButtonId());
                gender = checked.getText().toString();
            }

            return new UserRegister(name, surname, username, email, birthdate, gender, password, confirmPassword);
        }

    @Override
    public void onStringRequestSuccess(String response) {
        Toast.makeText(getActivity(), "Signed up...", Toast.LENGTH_LONG).show();
        fc.switchFragment(new LoginFragment(), null);
    }

    @Override
    public void onJsonObjectRequestSuccess(JSONObject response) {

    }

    @Override
    public void onJsonArrayRequestSuccess(JSONArray response) {

    }

    @Override
    public void onFail(String response){
            Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
            tvSignupError.setText(response);
            tvSignupError.setVisibility(View.VISIBLE);
        }
}