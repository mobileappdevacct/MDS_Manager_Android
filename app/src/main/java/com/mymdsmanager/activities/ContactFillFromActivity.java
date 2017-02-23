package com.mymdsmanager.activities;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.task.CompleteListener;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.task.SubmitFOrmTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.Bind;
import butterknife.ButterKnife;
public class ContactFillFromActivity extends AppCompatActivity implements OnCheckedChangeListener {

    @Bind(R.id.first_name)
    EditText firstName;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.email_name)
    EditText emailName;
    @Bind(R.id.commentEdt)
    EditText commentEdt;
    @Bind(R.id.patient)
    CheckBox patient;
    @Bind(R.id.family)
    CheckBox family;
    @Bind(R.id.friend)
    CheckBox friend;
    @Bind(R.id.caregiver)
    CheckBox caregiver;
    @Bind(R.id.submit)
    Button submit;
    @Bind(R.id.layout)
    LinearLayout layout;
    private String caregiver_str = "";
    String family_str = "", friend_str = "", patient_str = "";
    private Toolbar toolbar;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_fill_from);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Send Us A Message");
        phone.addTextChangedListener(new TextWatcher() {
            int len = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = phone.getText().toString();
                len = str.length();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = phone.getText().toString();
                if ((str.length() == 3 && len < str.length()) || (str.length() == 7 && len < str.length())) {
                    //checking length  for backs-pace.
                    phone.append("-");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        family.setOnCheckedChangeListener(this);
        patient.setOnCheckedChangeListener(this);
        friend.setOnCheckedChangeListener(this);
        caregiver.setOnCheckedChangeListener(this);
        submit = (Button)findViewById(R.id.submit);
        final float scale = this.getResources().getDisplayMetrics().density;
        friend.setPadding((int) getResources().getDimension(R.dimen.dim_30),
                friend.getPaddingTop(),
                friend.getPaddingRight(),
                friend.getPaddingBottom());
        caregiver.setPadding((int) getResources().getDimension(R.dimen.dim_30),
                caregiver.getPaddingTop(),
                caregiver.getPaddingRight(),
                caregiver.getPaddingBottom());
        patient.setPadding((int) getResources().getDimension(R.dimen.dim_30),
                patient.getPaddingTop(),
                patient.getPaddingRight(),
                patient.getPaddingBottom());
        family.setPadding((int) getResources().getDimension(R.dimen.dim_30),
                family.getPaddingTop(),
                family.getPaddingRight(),
                family.getPaddingBottom());
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(firstName.getText().toString())) {
                    firstName.setError("Please enter first name");
                    firstName.requestFocus();
                } else if (TextUtils.isEmpty(emailName.getText().toString())) {
                    emailName.setError("Please enter e-mail");
                    emailName.requestFocus();
                }else if (!isEmailValid(emailName.getText().toString())) {
                    emailName.setError("Invalid email address");
                    emailName.requestFocus();
                } else {

//                    Toast.makeText(ContactFillFromActivity.this, "Message Sent", Toast.LENGTH_LONG).show();
                    getTrials();
                }

            }
        });
         mHomeWatcher = new HomeWatcher(ContactFillFromActivity.this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                // do something here...
                isActivityFound=true;
            }
            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();
    }
    boolean isActivityFound = false;
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityFound) {
            new UpdateDataDialog(ContactFillFromActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }
    private ProgressDialog dialog;
    public void getTrials() {
        try {
            if (!MyApplication.isConnectingToInternet(ContactFillFromActivity.this)) {
                MyApplication.ShowMassage(ContactFillFromActivity.this,
                        "Please connect to working Internet connection!");
                return;
            } else {
                String param = "links.php?"+"name=" + firstName.getText().toString() + "&phone="
                        + phone.getText().toString() + "&email=" + emailName.getText().toString()
                        + "&comment=" + commentEdt.getText().toString() + "&i_am="
                        + family_str + "," + friend_str + "," + patient_str
                        + "," + caregiver_str;
                dialog = ProgressDialog.show(ContactFillFromActivity.this, "", "Fetching Data....");
                new SubmitFOrmTask(ContactFillFromActivity.this, param,
                        new CompleteListener() {

                            @Override
                            public void onRemoteErrorOccur(Object error) {
                                dialog.dismiss();
                                // actionBarFragment.mHandler
                                // .removeCallbacks(actionBarFragment.mRunnable);
                            }
                            @Override
                            public void onRemoteCallComplete(Object result) {
                                dialog.dismiss();
                                JSONObject main;
                                try {
                                    main = new JSONObject(result.toString());
                                    dialog.dismiss();
                                    if (main.getString("status").equalsIgnoreCase("1")) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ContactFillFromActivity.this);
                                        builder.setTitle("").setMessage("Form Successfully Submited")
                                                .setPositiveButton("OK", new OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                });

                                        AlertDialog alert = builder.create();
                                        alert.setCanceledOnTouchOutside(false);
                                        alert.show();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ContactFillFromActivity.this);
                                        builder.setTitle("").setMessage("Please try again")
                                                .setPositiveButton("OK", new OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();

                                                    }
                                                });

                                        AlertDialog alert = builder.create();
                                        alert.show();
                                        alert.setCanceledOnTouchOutside(false);
                                    }

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                            }
                        }).execute();

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.family:
                if (isChecked) {
                    family_str = "Family";
                } else {
                    family_str = "";
                }
                break;
            case R.id.friend:
                if (isChecked) {
                    friend_str = "Friend";
                } else {
                    friend_str = "";
                }
                break;
            case R.id.patient:
                if (isChecked) {
                    patient_str = "Patient";
                } else {
                    patient_str = "";
                }
                break;
            case R.id.caregiver:

                if (isChecked) {
                    caregiver_str = "Caregiver";
                } else {
                    caregiver_str = "";
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mHomeWatcher.stopWatch();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
