package sotos.apps.message13033.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.Objects;

import hari.bounceview.BounceView;
import sotos.apps.message13033.Constants;
import sotos.apps.message13033.MainActivityFragment.FirstUserFragment;
import sotos.apps.message13033.MainActivityFragment.SecondUserFragment;
import sotos.apps.message13033.R;
import sotos.apps.message13033.SMS;
import sotos.apps.message13033.Utils.PageTransformers.ZoomInOutTransformer;
import sotos.apps.message13033.Utils.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    private String message;
    private String reasonNumber;
    private String name, address;
    private CardView cardReason1, cardReason2, cardReason3, cardReason4, cardReason5, cardReason6;
    private MaterialButton help1, help2, help3, help4, help5, help6;
    private ViewPager viewPagerUsers;
    private WormDotsIndicator wormDotsIndicator;
    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView nestedScrollView;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Καλώς ορίσατε");
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        preferences = getPreferences(MODE_PRIVATE);
        editor = preferences.edit();

        init();
        cardClickHandler();
        cardLongClickHandler();
        initUsersFragments();

        zoomInOutAnimation();

        final ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSExternal();
            }
        });

        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollViewMain);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY < oldScrollY) {
                        //get up
                        floatingActionButton.extend();
                    }
                    if (scrollY > oldScrollX) {
                        //get down
                        floatingActionButton.shrink();
                    }
                }
            });
        }
    }

    private void initUsersFragments() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new FirstUserFragment(), "First");
        viewPagerAdapter.addFrag(new SecondUserFragment(), "Second");
        viewPagerUsers.setAdapter(viewPagerAdapter);
        wormDotsIndicator.setViewPager(viewPagerUsers);
    }

    private void cardLongClickHandler() {
        help1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCategoryMoreInfo(0);
            }
        });
        help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCategoryMoreInfo(1);
            }
        });
        help3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCategoryMoreInfo(2);
            }
        });
        help4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCategoryMoreInfo(3);
            }
        });
        help5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCategoryMoreInfo(4);
            }
        });
        help6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCategoryMoreInfo(5);
            }
        });
    }

    private void alertDialogCategoryMoreInfo(int numberOfReason) {

        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> reasons = new ArrayList<>();

        initDataForDialog(data, reasons);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_more_info, null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        MaterialButton close;
        TextView title, message;

        close = dialogView.findViewById(R.id.customAlertButtonClose);
        title = dialogView.findViewById(R.id.customAlertTitle);
        message = dialogView.findViewById(R.id.customAlertName);

        title.setText(String.format("%s. %s", numberOfReason + 1, reasons.get(numberOfReason)));
        message.setText(data.get(numberOfReason));

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

    private void initDataForDialog(ArrayList<String> data, ArrayList<String> reasons) {
        //init first data
        data.add("Μετάβαση σε φαρμακείο ή επίσκεψη στον γιατρό, εφόσον αυτό συνιστάται μετά από σχετική επικοινωνία.");
        data.add("Μετάβαση σε εν λειτουργία κατάστημα προμηθειών αγαθών πρώτης ανάγκης, όπου δεν είναι δυνατή η αποστολή τους.");
        data.add("Μετάβαση στην τράπεζα, στο μέτρο που δεν είναι δυνατή η ηλεκτρονική συναλλαγή.");
        data.add("Κίνηση για παροχή βοήθειας σε ανθρώπους που βρίσκονται σε ανάγκη.");
        data.add("Μετάβαση σε τελετή(πχ. κηδεία, γάμος, βάφτιση) υπό τους όρους που προβλέπει ο νόμος ή μετάβαση διαζευγμένων γονέων ή γονέων που τελούν σε διάσταση που είναι αναγκαία για την διασφάλιση της επικοινωνίας γονέων και τέκνων, σύμφωνα με τις κείμενες διατάξεις.");
        data.add("Σύντομη μετακίνηση, κοντά στην κατοικία μου, για ατομική σωματική άσκηση (εξαιρείται οποιαδήποτε συλλογική αθλητική δραστηριότητα) ή για τις ανάγκες κατοικιδίου ζώου");

        //init reason titles
        reasons.add("Φαρμακείο - Ιατρείο");
        reasons.add("Κατάστημα προμήθειας αγαθών");
        reasons.add("Τράπεζα");
        reasons.add("Παροχή βοήθειας");
        reasons.add("Τελετή - Γονείας σε διάσταση");
        reasons.add("Άθληση - Ανάγκες κατοικιδίου");
    }

    private void init() {
        viewPagerUsers = findViewById(R.id.viewpagerUsers);
        wormDotsIndicator = findViewById(R.id.dotIndicator);
        nestedScrollView = findViewById(R.id.nestedScrollViewMain);

        cardReason1 = findViewById(R.id.cardReason1);
        cardReason2 = findViewById(R.id.cardReason2);
        cardReason3 = findViewById(R.id.cardReason3);
        cardReason4 = findViewById(R.id.cardReason4);
        cardReason5 = findViewById(R.id.cardReason5);
        cardReason6 = findViewById(R.id.cardReason6);

        help1 = findViewById(R.id.buttonHelpReason1);
        help2 = findViewById(R.id.buttonHelpReason2);
        help3 = findViewById(R.id.buttonHelpReason3);
        help4 = findViewById(R.id.buttonHelpReason4);
        help5 = findViewById(R.id.buttonHelpReason5);
        help6 = findViewById(R.id.buttonHelpReason6);

        BounceView.addAnimTo(cardReason1);
        BounceView.addAnimTo(cardReason2);
        BounceView.addAnimTo(cardReason3);
        BounceView.addAnimTo(cardReason4);
        BounceView.addAnimTo(cardReason5);
        BounceView.addAnimTo(cardReason6);
    }

    private void cardClickHandler() {
        cardReason1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonNumber = "1";
                showSnackBar("Ο λόγος εξόδου (1) καταχωρήθηκε!", true);
            }
        });
        cardReason2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonNumber = "2";
                showSnackBar("Ο λόγος εξόδου (2) καταχωρήθηκε!", true);
            }
        });
        cardReason3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonNumber = "3";
                showSnackBar("Ο λόγος εξόδου (3) καταχωρήθηκε!", true);
            }
        });
        cardReason4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonNumber = "4";
                showSnackBar("Ο λόγος εξόδου (4) καταχωρήθηκε!", true);
            }
        });
        cardReason5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonNumber = "5";
                showSnackBar("Ο λόγος εξόδου (5) καταχωρήθηκε!", true);
            }
        });
        cardReason6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonNumber = "6";
                showSnackBar("Ο λόγος εξόδου (6) καταχωρήθηκε!", true);
            }
        });
    }

    private void showSnackBar(String message, boolean isForGoodReason) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        if (isForGoodReason) {
            snackbar.setBackgroundTint(getResources().getColor(android.R.color.holo_green_dark));
        }
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void sendSMSExternal() {
        if (generateMessage()) {

            android.app.AlertDialog.Builder builder =
                    new android.app.AlertDialog.Builder(this, R.style.CustomAlertDialog);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_alert_preview_message_before_send,
                    null);
            builder.setCancelable(true);
            builder.setView(dialogView);

            TextView textMessagePreview = dialogView.findViewById(R.id.alertMessagePreview);
            MaterialButton buttonSend = dialogView.findViewById(R.id.alertButtonSendMessage);
            MaterialButton buttonCancel = dialogView.findViewById(R.id.buttonCancelSendMessage);

            textMessagePreview.setText(message);
            final android.app.AlertDialog dialog = builder.create();

            final Intent intent = new Intent(Intent.ACTION_SENDTO);
            // This ensures only SMS apps respond
            intent.setData(Uri.parse("smsto:" + Constants.NUMBER));
            intent.putExtra("sms_body", message);
            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Objects.requireNonNull(dialog.getWindow()).
                    setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

        }
    }

    private void getAllStatsForUser(int number) {

        name = "";
        address = "";

        switch (number) {
            case 0:
                if (preferences.getBoolean(Constants.PREFERENCES_USER_SAVE_STATS, false)) {
                    //user save for user1
                    name = preferences.getString(Constants.PREFERENCES_USER_NAME, "");
                    address = preferences.getString(Constants.PREFERENCES_ADDRESS, "");
                    break;
                } else {
                    if (Objects.requireNonNull(FirstUserFragment.textName.getText()).toString().equals("") ||
                            FirstUserFragment.textName.getText() == null) {
                        showSnackBar("Δηλώστε έγκυρα προσωπικά στοιχεία", false);
                        break;
                    }
                    if (Objects.requireNonNull(FirstUserFragment.textAddress.getText()).toString().equals("") ||
                            FirstUserFragment.textAddress.getText() == null) {
                        showSnackBar("Δηλώστε έγκυρα προσωπικά στοιχεία", false);
                        break;
                    }
                }

                name = FirstUserFragment.textName.getText().toString();
                address = FirstUserFragment.textAddress.getText().toString();
                break;

            case 1:
                if (preferences.getBoolean(Constants.PREFERENCES_USER_SAVE_STATS2, false)) {
                    //user save for user2
                    name = preferences.getString(Constants.PREFERENCES_USER_NAME2, "");
                    address = preferences.getString(Constants.PREFERENCES_ADDRESS2, "");
                    break;
                } else {
                    if (Objects.requireNonNull(SecondUserFragment.textName.getText()).toString().equals("") ||
                            SecondUserFragment.textName.getText() == null) {
                        showSnackBar("Δηλώστε έγκυρα προσωπικά στοιχεία", false);
                        break;
                    }
                    if (Objects.requireNonNull(SecondUserFragment.textAddress.getText()).toString().equals("") ||
                            SecondUserFragment.textAddress.getText() == null) {
                        showSnackBar("Δηλώστε έγκυρα προσωπικά στοιχεία", false);
                        break;
                    }
                }

                name = SecondUserFragment.textName.getText().toString();
                address = SecondUserFragment.textAddress.getText().toString();

                break;

            default:
                break;

        }
    }

    private boolean generateMessage() {
        getAllStatsForUser(viewPagerUsers.getCurrentItem());
        if (validateInput()) {
            message = new SMS().transform(name.trim(),
                    address.trim(), reasonNumber);
            return true;
        }
        return false;
    }

    private boolean validateInput() {

        if (name.equals("") || address.equals("")) {
            showSnackBar("Εισάγετε έγκυρα στοιχεία", false);
            return false;
        }

        if (reasonNumber == null || reasonNumber.isEmpty()) {
            //no reason number is not selected
            showSnackBar("Επιλέξτε την αιτιολογία εξόδου σας!", false);
            return false;
        }

        //no errors here...
        return true;
    }

    private void zoomInOutAnimation() {
        viewPagerUsers.setPageTransformer(true, new ZoomInOutTransformer());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_preferences) {
            startActivity(new Intent(this, InfoActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}