package sotos.apps.message13033.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;

import sotos.apps.message13033.R;


public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_round_keyboard_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //finishing activity
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.settingsContainer,
                new InfoFragment()).commit();

    }

    public static class InfoFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            Preference preference_app_code = findPreference("preference_open_source");
            Preference preference_app_info = findPreference("preference_app_info");
            Preference preference_privacy = findPreference("preference_privacy_policy");
            Preference preference_licence = findPreference("preference_licence");

            final Intent browserIntentOpenSource =
                    new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/SotirisSapakos/Message13033"));
            assert preference_app_code != null;
            preference_app_code.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(browserIntentOpenSource);
                    return true;
                }
            });

            assert preference_app_info != null;
            preference_app_info.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    alertAppInfo();
                    return true;
                }
            });

            assert preference_licence != null;
            preference_licence.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // When the user selects an option to see the licenses:
                    startActivity(new Intent(getContext(), OssLicensesMenuActivity.class));
                    return true;
                }
            });

            final Intent browserIntent =
                    new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://epharmoge-menumatos-1.flycricket.io/privacy.html"));
            assert preference_privacy != null;
            preference_privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(browserIntent);
                    return true;
                }
            });
        }

        private void alertAppInfo() {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_alert_app_info, null);
            builder.setCancelable(true);
            builder.setView(dialogView);

            ExtendedFloatingActionButton close;

            close = dialogView.findViewById(R.id.customAlertButtonClose);

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
}