package sotos.apps.message13033.MainActivityFragment;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import sotos.apps.message13033.Constants;
import sotos.apps.message13033.R;

import static sotos.apps.message13033.Activities.MainActivity.editor;
import static sotos.apps.message13033.Activities.MainActivity.preferences;

public class FirstUserFragment extends Fragment {

    public static TextInputEditText textName, textAddress;
    private MaterialButton buttonSavePersonalInfo;

    public FirstUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_user, container, false);
        init(view);

        initData();

        buttonSavePersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        return view;
    }

    private void init(View view) {
        textName = view.findViewById(R.id.textInputName);
        textAddress = view.findViewById(R.id.textInputAddress);
        buttonSavePersonalInfo = view.findViewById(R.id.buttonSavePersonalInfo1);
    }

    private void initData() {
        if (preferences.getBoolean(Constants.PREFERENCES_USER_SAVE_STATS, false)) {
            textName.setText(preferences.getString(Constants.PREFERENCES_USER_NAME, ""));
            textAddress.setText(preferences.getString(Constants.PREFERENCES_ADDRESS, ""));
        }
    }

    private boolean validateTextInputs() {
        if (textName.getText() == null || textName.getText().toString().isEmpty()) {
            //empty name string
            textName.setError("Κενό ονοματεπώνυμο!");
            return false;
        }
        if (textAddress.getText() == null || textAddress.getText().toString().isEmpty()) {
            //empty address string
            textAddress.setError("Κενή διεύθυνση κατοικίας!");
            return false;
        }
        return true;
    }

    private void saveData() {
        if (validateTextInputs()) {
            //save user stats if all input layouts are filled
            editor.putString(Constants.PREFERENCES_USER_NAME,
                    Objects.requireNonNull(textName.getText()).toString());
            editor.putString(Constants.PREFERENCES_ADDRESS,
                    Objects.requireNonNull(textAddress.getText()).toString());
            editor.putBoolean(Constants.PREFERENCES_USER_SAVE_STATS, true);
            editor.commit();
            alertSaveStats();
        }
    }

    private void alertSaveStats() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        ExtendedFloatingActionButton close;
        TextView title, message, address;
        ImageView customImageUserNumber;

        close = dialogView.findViewById(R.id.customAlertButtonClose);
        title = dialogView.findViewById(R.id.customAlertTitle);
        message = dialogView.findViewById(R.id.customAlertName);
        address = dialogView.findViewById(R.id.customAlertAddress);
        customImageUserNumber = dialogView.findViewById(R.id.customImageUserNumber);

        Drawable image = getResources().getDrawable(R.drawable.user_1_red);

        title.setText("Αποθήκευση στοιχείων");
        message.setText(preferences.getString(Constants.PREFERENCES_USER_NAME, ""));
        address.setText(preferences.getString(Constants.PREFERENCES_ADDRESS, ""));
        customImageUserNumber.setImageDrawable(image);

        final android.app.AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).
                setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}