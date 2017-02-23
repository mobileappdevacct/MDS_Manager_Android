package com.mymdsmanager.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.MedicalProfessionalWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class AddMedicalProfessionalActivity extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.providerNameEdt)
    EditText providerNameEdt;
    @Bind(R.id.providerSpecialityEdt)
    EditText providerSpecialityEdt;
    @Bind(R.id.referredByEdt)
    EditText referredByEdt;
    @Bind(R.id.addressEdt)
    EditText addressEdt;
    @Bind(R.id.phoneNameEdt)
    EditText phoneNameEdt;
    @Bind(R.id.faxNameEdt)
    EditText faxNameEdt;
    @Bind(R.id.emailNameEdt)
    EditText emailNameEdt;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    DBAdapter dbAdapter;
    @Bind(R.id.exportContact)
    Button exportContact;
    @Bind(R.id.addExportLayout)
    LinearLayout addExportLayout;
    @Bind(R.id.honeTxt)
    TextView honeTxt;
    private Toolbar toolbar;
    MedicalProfessionalWrapper medicalProfessionalWrapper;
    HomeWatcher mHomeWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new DBAdapter(AddMedicalProfessionalActivity.this);
        setContentView(R.layout.activity_add_professionals);
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
        getSupportActionBar().setTitle("Add Medical Professional");
        phoneNameEdt.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        emailNameEdt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (TextUtils.isEmpty(providerNameEdt.getText().toString())) {
                        providerNameEdt.setError("Please Enter Provider Name");
                        providerNameEdt.requestFocus();
                    } else {
                        saveData();
                    }
                }
                return false;
            }
        });



        try {
            if (getIntent().getExtras().getString("update").equalsIgnoreCase("update")) {
                medicalProfessionalWrapper = (MedicalProfessionalWrapper) getIntent().getExtras().getSerializable("model");
                providerNameEdt.append(medicalProfessionalWrapper.getProvidername());
                providerSpecialityEdt.append(medicalProfessionalWrapper.getProviderspeciality());
                referredByEdt.append(medicalProfessionalWrapper.getReferredby());
                addressEdt.append(medicalProfessionalWrapper.getAddress());
                phoneNameEdt.append(medicalProfessionalWrapper.getPhone());
                faxNameEdt.append(medicalProfessionalWrapper.getFax());
                emailNameEdt.append(medicalProfessionalWrapper.getEmail());
                honeTxt.setText(medicalProfessionalWrapper.getCountrycode());
                saveBtn.setText("Update");
                getSupportActionBar().setTitle("Update Professional");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        providerNameEdt.addTextChangedListener(textWatcher);
        providerSpecialityEdt.addTextChangedListener(textWatcher);
        referredByEdt.addTextChangedListener(textWatcher);
        addressEdt.addTextChangedListener(textWatcher);
        phoneNameEdt.addTextChangedListener(textWatcher);
        faxNameEdt.addTextChangedListener(textWatcher);
        emailNameEdt.addTextChangedListener(textWatcher);
        honeTxt.addTextChangedListener(textWatcher);
         mHomeWatcher = new HomeWatcher(AddMedicalProfessionalActivity.this);
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
        phoneNameEdt.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = phoneNameEdt.getText().toString();
                len = str.length();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = phoneNameEdt.getText().toString();

                if ((str.length() == 3 && len < str.length()) || (str.length() == 7 && len < str.length())) {
                    //checking length  for backs-pace.
                    phoneNameEdt.append("-");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    boolean isActivityFound = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityFound) {
            new UpdateDataDialog(AddMedicalProfessionalActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @OnClick(R.id.exportContact)
    public void exportContact() {
//        MyApplication.getApplication().
        MyApplication.getApplication().produceAnimation(exportContact);
        if (TextUtils.isEmpty(providerNameEdt.getText().toString())) {
            providerNameEdt.setError("Please enter provider name");
            providerNameEdt.requestFocus();
        } else if (TextUtils.isEmpty(phoneNameEdt.getText().toString())) {
            phoneNameEdt.setError("Please enter phone");
            phoneNameEdt.requestFocus();
        } else {
            exportContact("", providerNameEdt.getText().toString(), phoneNameEdt.getText().toString(), emailNameEdt.getText().toString());
        }
    }
    @Override
    public void onBackPressed() {

        if (dataChanged&&(!TextUtils.isEmpty(providerNameEdt.getText().toString())||!TextUtils.isEmpty(providerSpecialityEdt.getText().toString())||!TextUtils.isEmpty(referredByEdt.getText().toString())||!TextUtils.isEmpty(addressEdt.getText().toString())||!TextUtils.isEmpty(phoneNameEdt.getText().toString())||!TextUtils.isEmpty(faxNameEdt.getText().toString())||!TextUtils.isEmpty(emailNameEdt.getText().toString())))
        {
            saveAlert();
        }else
        {

            finish();
        }
    }
    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMedicalProfessionalActivity.this);
        builder.setTitle("" + getResources().getString(R.string.app_name3));
        builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                finish();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }


    boolean dataChanged;
    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            dataChanged = true;

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            dataChanged = true;
        }
    };

    private void saveData() {
        dbAdapter.openMdsDB();
        MedicalProfessionalWrapper wrapper = new MedicalProfessionalWrapper();
        wrapper.setProvidername(providerNameEdt.getText().toString());
        wrapper.setAddress(addressEdt.getText().toString());
        wrapper.setProviderspeciality(providerSpecialityEdt.getText().toString());
        wrapper.setReferredby(referredByEdt.getText().toString());
        wrapper.setPhone(phoneNameEdt.getText().toString());
        wrapper.setFax(faxNameEdt.getText().toString());
        wrapper.setEmail(emailNameEdt.getText().toString());
        wrapper.setCountrycode(honeTxt.getText().toString());
        if (saveBtn.getText().toString().equalsIgnoreCase("Update")) {
            dbAdapter.updateMedicalProfessional(wrapper, medicalProfessionalWrapper.getId());
            MyApplication.saveLocalData(true);
        } else {
            dbAdapter.saveMedicalProfessional(wrapper);
            MyApplication.saveLocalData(true);
        }
        DataManager.getInstance().setProvider_name(providerNameEdt.getText().toString());
        dbAdapter.closeMdsDB();
//        Intent service_intent = new Intent(AddMedicalProfessionalActivity.this,DataUploadService.class);
//        startService(service_intent);
        new UpdateOnClass(MyApplication.getApplication(),this);
        finish();
    }
    @OnClick(R.id.saveBtn)
    public void saveBtn() {
        if (TextUtils.isEmpty(providerNameEdt.getText().toString())) {
            providerNameEdt.setError("Please Enter Provider Name");
            providerNameEdt.requestFocus();
        } else {
            saveData();
        }
    }
    @OnClick(R.id.honeTxt)
    public void honeTxt() {
        showPopUp(0);
    }
    private void showPopUp(final int type) {
        DBAdapter dbAdapter = new DBAdapter(AddMedicalProfessionalActivity.this);
        dbAdapter.openUSERDataBase();
        final ArrayList<String> countryList = dbAdapter.getCountryList(DBAdapter.COUNTRY);
        final ArrayList<String> countryList1 = new ArrayList<>();
        ArrayList<String> countrycodeList = dbAdapter.getCountryList(DBAdapter.COUNTRYCODE);
        ArrayList<String> countrycodeList1 = dbAdapter.getCountryList(DBAdapter.COUNTRYCODE);
        final HashMap<String, String> countrycode = new HashMap<>();
        final Map<String, String> countrynameStringStringHashMap = new HashMap<>();
        for (int i = 0; i < countryList.size(); i++) {
            if (countrynameStringStringHashMap.get(i) == null) {
                countrynameStringStringHashMap.put(countryList.get(i), countrycodeList.get(i));
            }
        }
        for (Map.Entry<String, String> entry : countrynameStringStringHashMap.entrySet()) {
            countryList1.add(entry.getKey());
            System.out.println(entry.getKey());
        }
        Collections.sort(countryList1);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select country");
        dbAdapter.closeMdsDB();
        Collections.sort(countryList1, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        ListView modeList = new ListView(this);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, countryList1);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);
        final Dialog dialog = builder.create();
        dialog.show();
//        homephoneTxt.setText("+" + countrycode.get(countryList1.get(0)));
//        workphoneTxt.setText("+"+countrycode.get(countryList1.get(0)));
//        mobilephoneTxt.setText("+"+countrycode.get(countryList1.get(0)));
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                honeTxt.setText("+" + countrynameStringStringHashMap.get(countryList1.get(position)));
            }
        });
    }

    private void exportContact(String second_name, String first_name, String mobile_number, String email_user) {
        ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>();
        op_list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)//.withValue(RawContacts.AGGREGATION_MODE, RawContacts.AGGREGATION_MODE_DEFAULT)
                .build());
        // first and last names
        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, second_name)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, first_name)
                .build());

        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile_number)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());
        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email_user)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());
        try {
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, op_list);
            AlertDialog.Builder builder = new AlertDialog.Builder(AddMedicalProfessionalActivity.this);
            builder.setMessage("Export Contact Successfully")
                    .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            exportContact.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish() {



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
