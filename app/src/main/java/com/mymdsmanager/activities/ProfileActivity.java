package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.Constants;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.AllergyWrapper;
import com.mymdsmanager.wrapper.CarGiversWrapper;
import com.mymdsmanager.wrapper.InsuranceWrapper;
import com.mymdsmanager.wrapper.UserDetailsWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.janmuller.android.simplecropimage.CropImage;

public class ProfileActivity extends AppCompatActivity implements OnFinishActivity {
    public static final int REQUEST_CODE_CROP_IMAGE = 0x53;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x52;
    public static final int REQUEST_CODE_GALLERY = 0x51;
    @Bind(R.id.userNameTxt)
    TextView userNameTxt;
    @Bind(R.id.userNameEdt)
    EditText userNameEdt;
    @Bind(R.id.editNameImgBtn)
    ImageButton editNameImgBtn;
    @Bind(R.id.userDetailEditImgBtn)
    ImageButton userDetailEditImgBtn;
    @Bind(R.id.userDetailShowHideImgBtn)
    ImageButton userDetailShowHideImgBtn;
    @Bind(R.id.dobTxt)
    TextView dobTxt;
    @Bind(R.id.sexText)
    TextView sexText;
    @Bind(R.id.radioMale)
    RadioButton radioMale;
    @Bind(R.id.radioFemale)
    RadioButton radioFemale;
    @Bind(R.id.radioSex)
    RadioGroup radioSex;
    @Bind(R.id.heightTxt)
    TextView heightTxt;
    @Bind(R.id.heightBtn)
    Button heightBtn;
    @Bind(R.id.weightTxt)
    TextView weightTxt;
    @Bind(R.id.weightBtn)
    Button weightBtn;
    @Bind(R.id.bloodGroupTxt)
    TextView bloodGroupTxt;
    @Bind(R.id.bloodTypeBtn)
    Button bloodTypeBtn;
    @Bind(R.id.maritalStatusTxt)
    TextView maritalStatusTxt;
    @Bind(R.id.maritalStatusBtn)
    Button maritalStatusBtn;
    @Bind(R.id.livingWithTxt)
    TextView livingWithTxt;
    @Bind(R.id.livingWithBtn)
    Button livingWithBtn;
    @Bind(R.id.ssnTxt)
    TextView ssnTxt;
    @Bind(R.id.ssnEdt)
    EditText ssnEdt;
    @Bind(R.id.about_ll)
    LinearLayout aboutLl;
    @Bind(R.id.contactEditImgBtn)
    ImageButton contactEditImgBtn;
    @Bind(R.id.contactShowHideImgBtn)
    ImageButton contactShowHideImgBtn;
    @Bind(R.id.addressLine1Txt)
    TextView addressLine1Txt;
    @Bind(R.id.addressLine1Edt)
    EditText addressLine1Edt;
    @Bind(R.id.addressLine2Txt)
    TextView addressLine2Txt;
    @Bind(R.id.addressLine2Edt)
    EditText addressLine2Edt;
    @Bind(R.id.addressLine3Txt)
    TextView addressLine3Txt;
    @Bind(R.id.addressLine3Edt)
    EditText addressLine3Edt;
    @Bind(R.id.cityTxt)
    TextView cityTxt;
    @Bind(R.id.cityEdt)
    EditText cityEdt;
    @Bind(R.id.stateTxt)
    TextView stateTxt;
    @Bind(R.id.stateEdt)
    EditText stateEdt;
    @Bind(R.id.zipcodeTxt)
    TextView zipcodeTxt;
    @Bind(R.id.zipcodeEdt)
    EditText zipcodeEdt;
    @Bind(R.id.emailIdTxt)
    TextView emailIdTxt;
    @Bind(R.id.emailIdEdt)
    EditText emailIdEdt;
    @Bind(R.id.contact_ll)
    LinearLayout contactLl;
    @Bind(R.id.dobPickerButton)
    Button dobPickerButton;
    @Bind(R.id.homePhoneTxt)
    TextView homePhoneTxt;
    @Bind(R.id.homePhoneEdt)
    EditText homePhoneEdt;
    @Bind(R.id.homeWorkTxt)
    TextView homeWorkTxt;
    @Bind(R.id.homeWorkEdt)
    EditText homeWorkEdt;
    @Bind(R.id.homeCellTxt)
    TextView homeCellTxt;
    @Bind(R.id.homeCellEdt)
    EditText homeCellEdt;
    @Bind(R.id.addCargiversImgBtn)
    ImageButton addCargiversImgBtn;
    @Bind(R.id.showHideCargiversImgBtn)
    ImageButton showHideCargiversImgBtn;
    @Bind(R.id.cargiversListView)
    ListView cargiversListView;
    @Bind(R.id.addAllergiesImgBtn)
    ImageButton addAllergiesImgBtn;
    @Bind(R.id.showHideAllergiesImgBtn)
    ImageButton showHideAllergiesImgBtn;
    @Bind(R.id.allergyListView)
    ListView allergyListView;
    private final int DATE_DIALOG_ID = 1;
    @Bind(R.id.medicalHistoryTxt)
    TextView medicalHistoryTxt;
    @Bind(R.id.initialLabResultTxt)
    TextView initialLabResultTxt;
    @Bind(R.id.userImgView)
    ImageView userImgView;
    @Bind(R.id.homephoneTxt)
    TextView homephoneTxt;
    @Bind(R.id.workphoneTxt)
    TextView workphoneTxt;
    @Bind(R.id.mobilephoneTxt)
    TextView mobilephoneTxt;
    @Bind(R.id.editImgBtn_P)
    ImageButton editImgBtnP;
    @Bind(R.id.showHideImgBtn_P)
    ImageButton showHideImgBtnP;
    @Bind(R.id.insuranceImgView)
    ImageView insuranceImgView;
    @Bind(R.id.optionTxt)
    TextView optionTxt;
    @Bind(R.id.optionEdt)
    EditText optionEdt;
    @Bind(R.id.companyNameTxt)
    TextView companyNameTxt;
    @Bind(R.id.companyNameEdt)
    EditText companyNameEdt;
    @Bind(R.id.phoneNoTxt)
    TextView phoneNoTxt;
    @Bind(R.id.phoneNoEdt)
    EditText phoneNoEdt;
    @Bind(R.id.employerTxt)
    TextView employerTxt;
    @Bind(R.id.employerEdt)
    EditText employerEdt;
    @Bind(R.id.groupTxt)
    TextView groupTxt;
    @Bind(R.id.groupEdt)
    EditText groupEdt;
    @Bind(R.id.prescriptionTxt)
    TextView prescriptionTxt;
    @Bind(R.id.prescriptionEdt)
    EditText prescriptionEdt;
    @Bind(R.id.addressTxt)
    TextView addressTxt;
    @Bind(R.id.addressEdt)
    EditText addressEdt;
    @Bind(R.id.cityTxtP)
    TextView cityTxtP;
    @Bind(R.id.cityEdtP)
    EditText cityEdtP;
    @Bind(R.id.stateTxtP)
    TextView stateTxtP;
    @Bind(R.id.stateEdtP)
    EditText stateEdtP;
    @Bind(R.id.zipCodeTxt)
    TextView zipCodeTxt;
    @Bind(R.id.zipCodeEdt)
    EditText zipCodeEdt;
    @Bind(R.id.primaryInsurance_ll)
    LinearLayout primaryInsuranceLl;
    @Bind(R.id.editImgBtn_S)
    ImageButton editImgBtnS;
    @Bind(R.id.showHideImgBtn_S)
    ImageButton showHideImgBtnS;
    @Bind(R.id.insuranceImgView_S)
    ImageView insuranceImgViewS;
    @Bind(R.id.optionTxt_S)
    TextView optionTxtS;
    @Bind(R.id.optionEdt_S)
    EditText optionEdtS;
    @Bind(R.id.companyNameTxt_S)
    TextView companyNameTxtS;
    @Bind(R.id.companyNameEdt_S)
    EditText companyNameEdtS;
    @Bind(R.id.phoneNoTxt_S)
    TextView phoneNoTxtS;
    @Bind(R.id.phoneNoEdt_S)
    EditText phoneNoEdtS;
    @Bind(R.id.employerTxt_S)
    TextView employerTxtS;
    @Bind(R.id.employerEdt_S)
    EditText employerEdtS;
    @Bind(R.id.groupTxt_S)
    TextView groupTxtS;
    @Bind(R.id.groupEdt_S)
    EditText groupEdtS;
    @Bind(R.id.prescriptionTxt_S)
    TextView prescriptionTxtS;
    @Bind(R.id.prescriptionEdt_S)
    EditText prescriptionEdtS;
    @Bind(R.id.addressTxt_S)
    TextView addressTxtS;
    @Bind(R.id.addressEdt_S)
    EditText addressEdtS;
    @Bind(R.id.cityTxt_S)
    TextView cityTxtS;
    @Bind(R.id.cityEdt_S)
    EditText cityEdtS;
    @Bind(R.id.stateTxt_S)
    TextView stateTxtS;
    @Bind(R.id.stateEdt_S)
    EditText stateEdtS;
    @Bind(R.id.zipCodeTxt_S)
    TextView zipCodeTxtS;
    @Bind(R.id.zipCodeEdt_S)
    EditText zipCodeEdtS;
    @Bind(R.id.secondryInsurance_ll)
    LinearLayout secondryInsuranceLl;
    @Bind(R.id.backImag)
    ImageView backImag;
    @Bind(R.id.usernameTxt)
    TextView usernameTxt;
    @Bind(R.id.radioDonotdisclose)
    RadioButton radioDonotdisclose;
    private boolean isEditMode_P = false, isEditMode_S = false;
    private boolean isPrimaryImageSelected = false;
    private boolean isSecondaryImageSelected = false;

    private String imagePathPrimary = "";
    private String imagePathSecondry = "";
    //primary content
    private int year;
    private int month;
    private int day;
    private File mFileTemp;
    private String imagePath = "";
    private boolean isNameEditMode = false;
    private boolean isDetailEditMode = false;
    private boolean isContactEditMode = false;
    private final String SELECT_HEIGHT = "Select Height";
    private final String SELECT_WEIGHT = "Select Weight";
    private final String SELECT_MARITAL_STATUS = "Select Marital Status";
    private final String SELECT_LIVING_WITH = "Select Living With";
    private final String SELECT_SSN = "Select Ssn";
    private final String SELECT_BLOOD_GORUP = "Select Blood Group";

    private ArrayList<String> feetArr = new ArrayList<String>();
    private ArrayList<String> weightArr = new ArrayList<String>();

    private ArrayList<String> inchArr = new ArrayList<String>();
    private ArrayList<String> bloodGroupArr = new ArrayList<String>();
    private ArrayList<String> maritalStatusArr = new ArrayList<String>();
    private ArrayList<String> livingStatusArr = new ArrayList<String>();


//    private String[] weightArr = new String[]{"1 lbs", "2 lbs", "3 lbs", "4 lbs", "5 lbs", "6 lbs", "7 lbs", "8 lbs", "9 lbs", "10 lbs"};

    private DBAdapter dbAdapter;

    private boolean saveprofileData = false;
    private String sexString = "",
            heightString = "", weightString = "", ssnString = "", bloodTypeString = "", maritalStatusString = "", livingWithString = "";
    private Toolbar toolbar;
    boolean dataChanged;

    private void setArray() {
        feetArr.add("1 feet");
        feetArr.add("2 feet");
        feetArr.add("3 feet");
        feetArr.add("4 feet");
        feetArr.add("5 feet");
        feetArr.add("6 feet");
        feetArr.add("7 feet");
        feetArr.add("8 feet");
        feetArr.add("9 feet");
        feetArr.add("10 feet");
        inchArr.add("1 inch");
        inchArr.add("2 inch");
        inchArr.add("3 inch");
        inchArr.add("4 inch");
        inchArr.add("5 inch");
        inchArr.add("6 inch");
        inchArr.add("7 inch");
        inchArr.add("8 inch");
        inchArr.add("9 inch");
        inchArr.add("10 inch");
        inchArr.add("11 inch");
        bloodGroupArr.add("A+");
        bloodGroupArr.add("A-");
        bloodGroupArr.add("B+");
        bloodGroupArr.add("B-");
        bloodGroupArr.add("AB+");
        bloodGroupArr.add("AB-");
        bloodGroupArr.add("O+");
        bloodGroupArr.add("O-");
        bloodGroupArr.add("Other");
//        maritalStatusArr.add("Single");
//        maritalStatusArr.add("Married");
//        maritalStatusArr.add("Seperated");
//        maritalStatusArr.add("Divorced");
//        maritalStatusArr.add("Widowed");
        maritalStatusArr.addAll(DataManager.getInstance().getMarital_statusArraylist());
        Collections.sort(maritalStatusArr, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        maritalStatusArr.add("Other");

//        livingStatusArr.add("Alone");
//        livingStatusArr.add("With Faimly");
//        livingStatusArr.add("Hostel");
        livingStatusArr.addAll(DataManager.getInstance().getLiving_statusArraylist());
        Collections.sort(livingStatusArr, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        livingStatusArr.add("Other");


        for (int i = 1; i <= 300; i++) {

            weightArr.add(i + " lbs");
        }

        weightArr.add("+");


    }

    UserDetailsWrapper userDetailsWrapper;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new DBAdapter(ProfileActivity.this);

        setContentView(R.layout.activity_profile);
        imagePath = MyApplication.getProfileImage();
        setArray();
//        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//
//        toolbar.setNavigationIcon(R.mipmap.icn_back);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        getSupportActionBar().setTitle("Personal Profile");

        ButterKnife.bind(this);
        userImgView.setEnabled(false);
        showUserDetailsSaveMode();
        showContactDetailsSaveMode();

        getUserData();

//        sexText.setText(sexString);


        radioMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sexString = "Male";
                    sexText.setText(sexString);
                }
            }
        });
        radioFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sexString = "Female";
                    sexText.setText(sexString);

                }
            }
        });
        radioDonotdisclose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sexString = "Don't Disclose";
                    sexText.setText(sexString);
                }
            }
        });

        System.out.println("countrycode" + Locale.getDefault().getCountry());

        homeWorkEdt.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = homeWorkEdt.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = homeWorkEdt.getText().toString();

                if ((str.length() == 3 && len < str.length()) || (str.length() == 7 && len < str.length())) {
                    //checking length  for backs-pace.
                    homeWorkEdt.append("-");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        homeCellEdt.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = homeCellEdt.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = homeCellEdt.getText().toString();

                if ((str.length() == 3 && len < str.length()) || (str.length() == 7 && len < str.length())) {
                    //checking length  for backs-pace.
                    homeCellEdt.append("-");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        homePhoneEdt.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = homePhoneEdt.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = homePhoneEdt.getText().toString();

                if ((str.length() == 3 && len < str.length()) || (str.length() == 7 && len < str.length())) {
                    //checking length  for backs-pace.
                    homePhoneEdt.append("-");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        getPrimaryValues();
        getSecondryValues();
        phoneNoEdt.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = phoneNoEdt.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dataChanged = true;
                String str = phoneNoEdt.getText().toString();

                if ((str.length() == 3 && len < str.length()) || (str.length() == 7 && len < str.length())) {
                    //checking length  for backs-pace.
                    phoneNoEdt.append("-");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phoneNoEdtS.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = phoneNoEdtS.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dataChanged = true;
                String str = phoneNoEdtS.getText().toString();

                if ((str.length() == 3 && len < str.length()) || (str.length() == 7 && len < str.length())) {
                    //checking length  for backs-pace.
                    phoneNoEdtS.append("-");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mHomeWatcher = new HomeWatcher(ProfileActivity.this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                // do something here...
                isActivityFound = true;
            }

            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();
        addressEdt.addTextChangedListener(textWatcher);
        addressLine1Edt.addTextChangedListener(textWatcher);
        addressLine2Edt.addTextChangedListener(textWatcher);
        addressLine3Edt.addTextChangedListener(textWatcher);
        cityEdt.addTextChangedListener(textWatcher);
        stateEdt.addTextChangedListener(textWatcher);
        zipcodeEdt.addTextChangedListener(textWatcher);
        emailIdEdt.addTextChangedListener(textWatcher);
        homeCellEdt.addTextChangedListener(textWatcher);
        homeWorkEdt.addTextChangedListener(textWatcher);
        userNameEdt.addTextChangedListener(textWatcher);
    }

    boolean isActivityFound = false;

    @Override
    protected void onResume() {
        super.onResume();
//        new UserProfileActivity.this, R.style.Dialog).dismiss();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(ProfileActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @OnClick(R.id.medicalHistoryTxt)
    public void medicalHistoryTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(ProfileActivity.this, MedicalHistoryActivity.class));
    }

    @OnClick(R.id.homephoneTxt)
    public void homephoneTxt() {
        showPopUp(1);
    }

    @OnClick(R.id.workphoneTxt)
    public void workphoneTxt() {
        showPopUp(2);
    }

    @OnClick(R.id.mobilephoneTxt)
    public void mobilephoneTxt() {
        showPopUp(3);
    }

    @OnClick(R.id.initialLabResultTxt)
    public void initialLabResultTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(ProfileActivity.this, LabResultActivity.class).putExtra("title", "Initial Lab Results"));
    }

    @OnClick(R.id.backImag)
    public void backImag() {
        onBackPressed();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @OnClick(R.id.addCargiversImgBtn)
    public void addCargiversImgBtn() {
        showCarGiverDialog(new CarGiversWrapper(), "Add Caregiver");
    }

    @OnClick(R.id.addAllergiesImgBtn)
    public void addAllergiesImgBtn() {
        showAllergyDialog(new AllergyWrapper(), "Add Allergy");
    }

    @OnClick(R.id.dobPickerButton)
    public void dobPickerButton() {
        MyApplication.getApplication().hideSoftKeyBoard(ProfileActivity.this);
        showDialog(DATE_DIALOG_ID);

    }


    @OnClick(R.id.heightBtn)
    public void heightBtn() {
        heightString = "";
        showPopUp(SELECT_HEIGHT, feetArr);
    }


    @OnClick(R.id.bloodTypeBtn)
    public void bloodTypeBtn() {

        showPopUp(SELECT_BLOOD_GORUP, bloodGroupArr);
    }

    @OnClick(R.id.maritalStatusBtn)
    public void maritalStatusBtn() {

        showPopUp(SELECT_MARITAL_STATUS, maritalStatusArr);
    }

    @OnClick(R.id.livingWithBtn)
    public void livingWithBtn() {

        showPopUp(SELECT_LIVING_WITH, livingStatusArr);
    }

    @OnClick(R.id.weightBtn)
    public void weightBtn() {

        showPopUp(SELECT_WEIGHT, weightArr);
    }

    @OnClick(R.id.contactEditImgBtn)
    public void contactEditImgBtn() {

        isContactEditMode = !isContactEditMode;
        if (isContactEditMode) {
            contactEditImgBtn.setImageResource(R.mipmap.icn_save);
            showContactDetailsEditMode();
            contactLl.setVisibility(View.VISIBLE);
        } else {
            contactEditImgBtn.setImageResource(R.mipmap.icn_edit_aboutlabel);

            showContactDetailsSaveMode();
            saveUserData();
            getUserData();

        }


    }

    private void showAddOthersDialog(final String title) {


        final Dialog dialog = new Dialog(ProfileActivity.this, R.style.AppCompatTheme);
        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.pop_up_add_others);
// Check if no view has focus:

        final EditText enterValueEdt = (EditText) dialog.findViewById(R.id.enterValueEdt);


        enterValueEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (TextUtils.isEmpty(enterValueEdt.getText().toString())) {
                            Toast.makeText(ProfileActivity.this, "Please fill required field", Toast.LENGTH_LONG).show();
                        } else {


                            if (title.equalsIgnoreCase(SELECT_LIVING_WITH)) {
                                livingStatusArr.add(livingStatusArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");

                                livingWithString = enterValueEdt.getText().toString();
                                livingWithBtn.setText(livingWithString);
                                livingWithTxt.setText(livingWithString);

                            } else if (title.equalsIgnoreCase(SELECT_BLOOD_GORUP)) {
                                bloodGroupArr.add(bloodGroupArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");

                                bloodTypeString = enterValueEdt.getText().toString();
                                bloodGroupTxt.setText(bloodTypeString);
                                bloodTypeBtn.setText(bloodTypeString);

                            } else if (title.equalsIgnoreCase(SELECT_MARITAL_STATUS)) {
                                maritalStatusArr.add(maritalStatusArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");

                                maritalStatusString = enterValueEdt.getText().toString();
                                maritalStatusTxt.setText(maritalStatusString);
                                maritalStatusBtn.setText(maritalStatusString);
                            } else if (title.equalsIgnoreCase(SELECT_WEIGHT)) {
                                weightArr.add(weightArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");

                                weightString = enterValueEdt.getText().toString();
                                weightBtn.setText(weightString);
                                weightTxt.setText(weightString);
                            }
                            dialog.dismiss();

                            hideKeyboard();
                        }
                }
                return true;

            }
        });

        Button addBtn, closeBtn;
        addBtn = (Button) dialog.findViewById(R.id.addBtn);
        closeBtn = (Button) dialog.findViewById(R.id.closeBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(enterValueEdt.getText().toString())) {
                    Toast.makeText(ProfileActivity.this, "Please fill required field", Toast.LENGTH_LONG).show();
                } else {


                    if (title.equalsIgnoreCase(SELECT_LIVING_WITH)) {
                        livingStatusArr.add(livingStatusArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");

                        livingWithString = enterValueEdt.getText().toString();
                        livingWithBtn.setText(livingWithString);
                        livingWithTxt.setText(livingWithString);

                    } else if (title.equalsIgnoreCase(SELECT_BLOOD_GORUP)) {
                        bloodGroupArr.add(bloodGroupArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");

                        bloodTypeString = enterValueEdt.getText().toString();
                        bloodGroupTxt.setText(bloodTypeString);
                        bloodTypeBtn.setText(bloodTypeString);

                    } else if (title.equalsIgnoreCase(SELECT_MARITAL_STATUS)) {
                        maritalStatusArr.add(maritalStatusArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");
                        maritalStatusString = enterValueEdt.getText().toString();
                        maritalStatusTxt.setText(maritalStatusString);
                        maritalStatusBtn.setText(maritalStatusString);
                    } else if (title.equalsIgnoreCase(SELECT_WEIGHT)) {
                        weightArr.add(weightArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");
                        weightString = enterValueEdt.getText().toString();
                        weightBtn.setText(weightString);
                        weightTxt.setText(weightString);
                    }
                    dialog.dismiss();


                    hideKeyboard();
                }
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void hideKeyboard() {
        // Check if no view has focus:

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopUp(final String title, final ArrayList<String> arr) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
//        Collections.sort(arr, new Comparator<String>() {
//            @Override
//            public int compare(String s1, String s2) {
//                return s1.compareToIgnoreCase(s2);
//            }
//        });
        ListView modeList = new ListView(this);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, arr);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);
        final Dialog dialog = builder.create();

        dialog.show();

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();


                if (title.equalsIgnoreCase(SELECT_HEIGHT)) {

                    if (TextUtils.isEmpty(heightString)) {
                        heightString = arr.get(position).replaceAll("feet", "") + "'";
                        showPopUp(SELECT_HEIGHT, inchArr);
                    } else {
                        dataChanged = true;
                        heightString = heightString + arr.get(position).replaceAll("inch", "\"") + "";
                        heightTxt.setText(heightString);

                        heightBtn.setText(heightString);
                    }

                } else if (title.equalsIgnoreCase(SELECT_WEIGHT)) {

                    weightString = arr.get(position);
                    dataChanged = true;
                    weightBtn.setText(weightString);
                    weightTxt.setText(weightString);
                    if (weightString.equalsIgnoreCase("+")) {
                        showAddOthersDialog(SELECT_WEIGHT);
                    }
                } else if (title.equalsIgnoreCase(SELECT_LIVING_WITH)) {

                    livingWithString = arr.get(position);
                    dataChanged = true;
                    livingWithBtn.setText(livingWithString);

                    livingWithTxt.setText(livingWithString);
                    if (livingWithString.equalsIgnoreCase("Other")) {
                        showAddOthersDialog(SELECT_LIVING_WITH);
                    }
                } else if (title.equalsIgnoreCase(SELECT_MARITAL_STATUS)) {

                    maritalStatusString = arr.get(position);
                    dataChanged = true;
                    maritalStatusBtn.setText(maritalStatusString);

                    maritalStatusTxt.setText(maritalStatusString);
                    if (maritalStatusString.equalsIgnoreCase("Other")) {
                        showAddOthersDialog(SELECT_MARITAL_STATUS);
                    }
                } else if (title.equalsIgnoreCase(SELECT_BLOOD_GORUP)) {

                    bloodTypeString = arr.get(position);
                    dataChanged = true;
                    bloodTypeBtn.setText(bloodTypeString);

                    bloodGroupTxt.setText(bloodTypeString);
                    if (bloodTypeString.equalsIgnoreCase("Other")) {
                        showAddOthersDialog(SELECT_BLOOD_GORUP);
                    }
                }

            }
        });

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title);

//
//        if (title.equalsIgnoreCase(SELECT_WEIGHT)) {
//            for (int i = 0; i < typeArr.length; i++) {
//                typeArr[i] = (i + 1) + " lbs";
//            }
//        }

//        final ListView modeList = new ListView(this);
//        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, typeArr);
//        modeList.setAdapter(modeAdapter);

//        final Dialog dialog = builder.create();
//
//        builder.setView(modeList);
//
//        dialog.show();

    }

    @OnClick(R.id.contactShowHideImgBtn)
    public void contactShowHideImgBtn() {

        if (contactLl.getVisibility() == View.VISIBLE) {
            contactShowHideImgBtn.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            contactLl.setVisibility(View.GONE);
        } else {
            contactShowHideImgBtn.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            contactLl.setVisibility(View.VISIBLE);
        }

    }

    boolean save_dataBln = false;

    private void showAllergyDialog(final AllergyWrapper wrapper, final String title) {
        save_dataBln = false;

        final Dialog dialog1 = new Dialog(ProfileActivity.this, R.style.AppCompatTheme);
        // Include dialog.xml file
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.pop_up_add_alergies);
// Check if no view has focus:
        final EditText substanceEdt, alergyEdt, contactNoEdt;
        TextView saveTxt, closeTxt, titleTxt;
        saveTxt = (TextView) dialog1.findViewById(R.id.saveTxt);
        closeTxt = (TextView) dialog1.findViewById(R.id.closeTxt);
        final String name = wrapper.getAllergytype();
        titleTxt = (TextView) dialog1.findViewById(R.id.titleTxt);
        substanceEdt = (EditText) dialog1.findViewById(R.id.substanceEdt);
        alergyEdt = (EditText) dialog1.findViewById(R.id.alergyEdt);
        titleTxt.setText(title);
        try {
            AESCrypt aesCrypt = new AESCrypt("SquareBits");
            substanceEdt.setText(aesCrypt.decrypt(wrapper.getAllergysubstance()));
            alergyEdt.setText(aesCrypt.decrypt(wrapper.getAllergytype()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        saveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AllergyWrapper wrapper = new AllergyWrapper();
                try {
                    allergydataChanged = false;
                    dataChanged = false;

                    AESCrypt aesCrypt = new AESCrypt("SquareBits");
                    wrapper.setAllergysubstance(aesCrypt.encrypt(substanceEdt.getText().toString()));
                    wrapper.setAllergytype(aesCrypt.encrypt(alergyEdt.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dbAdapter.openUSERDataBase();
                if (title.equalsIgnoreCase("Update Allergy")) {
                    dbAdapter.updateAllergyData(wrapper, name);
                    new UserUpdateOnClass(ProfileActivity.this, ProfileActivity.this);
                    dbAdapter.closeUserDB();
                } else {
                    dbAdapter.saveAllergy(wrapper);
                    new UserUpdateOnClass(ProfileActivity.this, ProfileActivity.this);
                    dbAdapter.closeUserDB();
                }
                save_dataBln = true;
                showAllergyList();
                dialog1.dismiss();
            }
        });


        closeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allergydataChanged) {
                    save_dataBln = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("" + getResources().getString(R.string.app_name3));
                    builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            allergydataChanged = false;
                                            dialog.cancel();
                                            dialog1.dismiss();


                                        }
                                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                } else {
                    dialog1.dismiss();
                }
            }
        });


        dialog1.show();


//        dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                if (allergydataChanged) {
//                    save_dataBln = true;
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
//                    builder.setTitle("" + getResources().getString(R.string.app_name3));
//                    builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
//                            .setCancelable(false)
//                            .setPositiveButton("Yes",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                            dialog1.dismiss();
//                                        }
//                                    })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.show();
//                }
//            }
//        });
        substanceEdt.addTextChangedListener(allergytextWatcher);
        alergyEdt.addTextChangedListener(allergytextWatcher);

    }

    private void showCarGiverDialog(final CarGiversWrapper wrapper, final String title) {

        save_dataBln = false;
        final Dialog dialog1 = new Dialog(ProfileActivity.this, R.style.AppCompatTheme);
        // Include dialog.xml file
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.pop_up_add_cargivers);
// Check if no view has focus:
        final EditText cargiversNameEdt, relationEdt, contactNoEdt,emailEdt;
        TextView saveTxt, closeTxt, titleTxt;
        saveTxt = (TextView) dialog1.findViewById(R.id.saveTxt);
        closeTxt = (TextView) dialog1.findViewById(R.id.closeTxt);
        final String name = wrapper.getCaregivername();
        titleTxt = (TextView) dialog1.findViewById(R.id.titleTxt);
        cargiversNameEdt = (EditText) dialog1.findViewById(R.id.cargiversNameEdt);
        relationEdt = (EditText) dialog1.findViewById(R.id.relationEdt);
        contactNoEdt = (EditText) dialog1.findViewById(R.id.contactNoEdt);
        emailEdt=(EditText)dialog1.findViewById(R.id.emailEdt);
        titleTxt.setText(title);
        try {
            AESCrypt aesCrypt = new AESCrypt("SquareBits");
            cargiversNameEdt.setText(aesCrypt.decrypt(wrapper.getCaregivername()));
            relationEdt.setText(aesCrypt.decrypt(wrapper.getCaregiverrelation()));
            contactNoEdt.setText(aesCrypt.decrypt(wrapper.getCaregivercontact()));
            contactNoEdt.setText(aesCrypt.decrypt(wrapper.getCaregiveremail()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        contactNoEdt.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = contactNoEdt.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = contactNoEdt.getText().toString();

                if ((str.length() == 3 && len < str.length()) || (str.length() == 7 && len < str.length())) {
                    //checking length  for backs-pace.
                    contactNoEdt.append("-");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        saveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_dataBln = true;


                CarGiversWrapper wrapper = new CarGiversWrapper();
                try {
                    cargiverdataChanged = false;
                    dataChanged = false;
                    AESCrypt aesCrypt = new AESCrypt("SquareBits");
                    wrapper.setCaregivername(aesCrypt.encrypt(cargiversNameEdt.getText().toString()));
                    wrapper.setCaregiverrelation(aesCrypt.encrypt(relationEdt.getText().toString()));
                    wrapper.setCaregivercontact(aesCrypt.encrypt(contactNoEdt.getText().toString()));
                    wrapper.setCaregiveremail(aesCrypt.encrypt(emailEdt.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                dbAdapter.openUSERDataBase();
                if (title.equalsIgnoreCase("Update Caregiver")) {
                    dbAdapter.updateCarGiverData(wrapper, name);
                    new UserUpdateOnClass(ProfileActivity.this, ProfileActivity.this);

                } else {
                    dbAdapter.saveCarGivers(wrapper);
                    new UserUpdateOnClass(ProfileActivity.this, ProfileActivity.this);

                }
                showcarGiverList();
                dialog1.dismiss();
            }
        });


        closeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cargiverdataChanged) {
                    save_dataBln = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("" + getResources().getString(R.string.app_name3));
                    builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            cargiverdataChanged = false;
                                            dialog.cancel();
                                            dialog1.dismiss();
                                        }
                                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                } else {
                    dialog1.dismiss();
                }
            }
        });

//        dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                if (cargiverdataChanged) {
//                    save_dataBln = true;
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
//                    builder.setTitle("" + getResources().getString(R.string.app_name3));
//                    builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
//                            .setCancelable(false)
//                            .setPositiveButton("Yes",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                            dialog1.dismiss();
//                                        }
//                                    })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.show();
//                }
//            }
//        });
        dialog1.show();
        cargiversNameEdt.addTextChangedListener(CarGivertextWatcher);
        relationEdt.addTextChangedListener(CarGivertextWatcher);
        contactNoEdt.addTextChangedListener(CarGivertextWatcher);

    }

    @OnClick(R.id.showHideAllergiesImgBtn)
    public void showHideAllergiesImgBtn() {

        if (allergyListView.getVisibility() == View.VISIBLE) {
            showHideAllergiesImgBtn.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            allergyListView.setVisibility(View.GONE);
        } else {
            showHideAllergiesImgBtn.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            allergyListView.setVisibility(View.VISIBLE);
            showAllergyList();
        }

    }

    @OnClick(R.id.showHideCargiversImgBtn)
    public void showHideCargiversImgBtn() {

        if (cargiversListView.getVisibility() == View.VISIBLE) {
            showHideCargiversImgBtn.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            cargiversListView.setVisibility(View.GONE);
        } else {
            showHideCargiversImgBtn.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            cargiversListView.setVisibility(View.VISIBLE);
            showcarGiverList();


        }

    }


    private void showcarGiverList() {
        dbAdapter.openUSERDataBase();

        ArrayList<CarGiversWrapper> wrapperList = dbAdapter.getCargivers();
        Collections.reverse(wrapperList);
        CarGiversAdapter adapter = new CarGiversAdapter(ProfileActivity.this, wrapperList);
        cargiversListView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(cargiversListView);
        dbAdapter.closeUserDB();

    }

    private void showAllergyList() {
        dbAdapter.openUSERDataBase();

        ArrayList<AllergyWrapper> wrapperList = dbAdapter.getAllergiess();
        Collections.reverse(wrapperList);
        AllergyAdapter adapter = new AllergyAdapter(ProfileActivity.this, wrapperList);
        allergyListView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(allergyListView);
        dbAdapter.closeUserDB();

    }

    @OnClick(R.id.editNameImgBtn)
    public void editNameImgBtn() {

        isNameEditMode = !isNameEditMode;
        if (isNameEditMode) {
            editNameImgBtn.setImageResource(R.mipmap.icn_save);
            userNameEdt.setVisibility(View.VISIBLE);
            userImgView.setEnabled(true);
            userNameTxt.setVisibility(View.GONE);
        } else {
            editNameImgBtn.setImageResource(R.mipmap.icn_edit_aboutlabel);
            userNameEdt.setVisibility(View.GONE);
            userNameTxt.setVisibility(View.VISIBLE);
            saveUserData();
            getUserData();
        }


    }

    @Override
    public void onFinish() {

    }

    private class AllergyAdapter extends BaseAdapter {
        Context context;
        ArrayList<AllergyWrapper> rowItems;


        AESCrypt aesCrypt;

        public AllergyAdapter(Context context,
                              ArrayList<AllergyWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
            try {
                aesCrypt = new AESCrypt("SquareBits");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* private view holder class */
        private class ViewHolder {
            TextView nameTxt, detailTxt;

        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_cargiver_item,
                        null);
                holder = new ViewHolder();
                holder.nameTxt = (TextView) convertView
                        .findViewById(R.id.nameTxt);
                holder.detailTxt = (TextView) convertView
                        .findViewById(R.id.detailTxt);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try {
                holder.nameTxt.setText(aesCrypt.decrypt(rowItems.get(position).getAllergytype()));
                holder.detailTxt.setText(aesCrypt.decrypt(rowItems.get(position).getAllergysubstance()));
            } catch (Exception e) {
                e.printStackTrace();
            }


            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    showAllergyDialog(rowItems.get(position), "Update Allergy");

                }
            });

            return convertView;
        }

        @Override
        public int getCount() {
            return rowItems.size();
        }

        @Override
        public Object getItem(int position) {
            return rowItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return rowItems.indexOf(position);

        }
    }

    private class CarGiversAdapter extends BaseAdapter {
        Context context;
        ArrayList<CarGiversWrapper> rowItems;
        AESCrypt aesCrypt;

        public CarGiversAdapter(Context context,
                                ArrayList<CarGiversWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
            try {
                aesCrypt = new AESCrypt("SquareBits");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* private view holder class */
        private class ViewHolder {
            TextView nameTxt, detailTxt;

        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_cargiver_item,
                        null);
                holder = new ViewHolder();
                holder.nameTxt = (TextView) convertView
                        .findViewById(R.id.nameTxt);
                holder.detailTxt = (TextView) convertView
                        .findViewById(R.id.detailTxt);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try {
                holder.nameTxt.setText(aesCrypt.decrypt(rowItems.get(position).getCaregivername()));
                holder.detailTxt.setText(aesCrypt.decrypt(rowItems.get(position).getCaregiverrelation()));
            } catch (Exception e) {
                e.printStackTrace();
            }


            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showCarGiverDialog(rowItems.get(position), "Update Caregiver");

                }
            });

            return convertView;
        }

        @Override
        public int getCount() {
            return rowItems.size();
        }

        @Override
        public Object getItem(int position) {
            return rowItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return rowItems.indexOf(position);

        }

    }

    @OnClick(R.id.userDetailShowHideImgBtn)
    public void userDetailShowHideImgBtn() {

        if (aboutLl.getVisibility() == View.VISIBLE) {
            userDetailShowHideImgBtn.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            aboutLl.setVisibility(View.GONE);
        } else {
            userDetailShowHideImgBtn.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            aboutLl.setVisibility(View.VISIBLE);
            showUserDetailsSaveMode();

        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String dayString = day + "", monthString = (month + 1) + "";

            if ((month + 1) < 10) {
                monthString = "0" + (month + 1);
            }
            if (day < 10) {
                dayString = "0" + day;
            }

            String dateString = monthString + "-" + dayString + "-" + year;
            // set selected date into textview
            dobTxt.setText(formateDate(dateString));
            dobPickerButton.setText(formateDate(dateString));
            // set selected date into datepicker also

        }
    };

    @OnClick(R.id.userDetailEditImgBtn)
    public void userDetailEditImgBtn() {

        isDetailEditMode = !isDetailEditMode;
        if (isDetailEditMode) {
            allergydataChanged = false;
            cargiverdataChanged = false;
            dataChanged = false;
            userDetailEditImgBtn.setImageResource(R.mipmap.icn_save);
            showUserDetailsEditMode();
            aboutLl.setVisibility(View.VISIBLE);
            editNameImgBtn.setImageResource(R.mipmap.icn_save);
            userNameEdt.setVisibility(View.VISIBLE);
            userImgView.setEnabled(true);
            userNameTxt.setVisibility(View.GONE);
            contactEditImgBtn.setImageResource(R.mipmap.icn_save);
            showContactDetailsEditMode();
            contactLl.setVisibility(View.VISIBLE);
            optionEdt.setVisibility(View.VISIBLE);
            companyNameEdt.setVisibility(View.VISIBLE);
            phoneNoEdt.setVisibility(View.VISIBLE);
            employerEdt.setVisibility(View.VISIBLE);
            groupEdt.setVisibility(View.VISIBLE);
            prescriptionEdt.setVisibility(View.VISIBLE);
            addressEdt.setVisibility(View.VISIBLE);
            cityEdtP.setVisibility(View.VISIBLE);
            stateEdtP.setVisibility(View.VISIBLE);
            zipCodeEdt.setVisibility(View.VISIBLE);
            primaryInsuranceLl.setVisibility(View.VISIBLE);
            optionTxt.setVisibility(View.GONE);
            companyNameTxt.setVisibility(View.GONE);
            phoneNoTxt.setVisibility(View.GONE);
            employerTxt.setVisibility(View.GONE);
            groupTxt.setVisibility(View.GONE);
            prescriptionTxt.setVisibility(View.GONE);
            addressTxt.setVisibility(View.GONE);
            cityTxtP.setVisibility(View.GONE);
            stateTxtP.setVisibility(View.GONE);
            zipCodeTxt.setVisibility(View.GONE);
            optionEdtS.setVisibility(View.VISIBLE);
            companyNameEdtS.setVisibility(View.VISIBLE);
            phoneNoEdtS.setVisibility(View.VISIBLE);
            employerEdtS.setVisibility(View.VISIBLE);
            groupEdtS.setVisibility(View.VISIBLE);
            prescriptionEdtS.setVisibility(View.VISIBLE);
            addressEdtS.setVisibility(View.VISIBLE);
            cityEdtS.setVisibility(View.VISIBLE);
            stateEdtS.setVisibility(View.VISIBLE);
            zipCodeEdtS.setVisibility(View.VISIBLE);
            secondryInsuranceLl.setVisibility(View.VISIBLE);
            optionTxtS.setVisibility(View.GONE);
            companyNameTxtS.setVisibility(View.GONE);
            phoneNoTxtS.setVisibility(View.GONE);
            employerTxtS.setVisibility(View.GONE);
            groupTxtS.setVisibility(View.GONE);
            prescriptionTxtS.setVisibility(View.GONE);
            addressTxtS.setVisibility(View.GONE);
            cityTxtS.setVisibility(View.GONE);
            stateTxtS.setVisibility(View.GONE);
            zipCodeTxtS.setVisibility(View.GONE);
        } else {

            userDetailEditImgBtn.setImageResource(R.mipmap.icn_edit_aboutlabel);
            optionEdt.setVisibility(View.GONE);
            companyNameEdt.setVisibility(View.GONE);
            phoneNoEdt.setVisibility(View.GONE);
            employerEdt.setVisibility(View.GONE);
            groupEdt.setVisibility(View.GONE);
            prescriptionEdt.setVisibility(View.GONE);
            addressEdt.setVisibility(View.GONE);
            cityEdtP.setVisibility(View.GONE);
            stateEdtP.setVisibility(View.GONE);
            zipCodeEdt.setVisibility(View.GONE);

            optionTxt.setVisibility(View.VISIBLE);
            companyNameTxt.setVisibility(View.VISIBLE);
            phoneNoTxt.setVisibility(View.VISIBLE);
            employerTxt.setVisibility(View.VISIBLE);
            groupTxt.setVisibility(View.VISIBLE);
            prescriptionTxt.setVisibility(View.VISIBLE);
            addressTxt.setVisibility(View.VISIBLE);
            cityTxtP.setVisibility(View.VISIBLE);
            stateTxtP.setVisibility(View.VISIBLE);
            zipCodeTxt.setVisibility(View.VISIBLE);
            optionEdtS.setVisibility(View.GONE);
            companyNameEdtS.setVisibility(View.GONE);
            phoneNoEdtS.setVisibility(View.GONE);
            employerEdtS.setVisibility(View.GONE);
            groupEdtS.setVisibility(View.GONE);
            prescriptionEdtS.setVisibility(View.GONE);
            addressEdtS.setVisibility(View.GONE);
            cityEdtS.setVisibility(View.GONE);
            stateEdtS.setVisibility(View.GONE);
            zipCodeEdtS.setVisibility(View.GONE);
            optionTxtS.setVisibility(View.VISIBLE);
            companyNameTxtS.setVisibility(View.VISIBLE);
            phoneNoTxtS.setVisibility(View.VISIBLE);
            employerTxtS.setVisibility(View.VISIBLE);
            groupTxtS.setVisibility(View.VISIBLE);
            prescriptionTxtS.setVisibility(View.VISIBLE);
            addressTxtS.setVisibility(View.VISIBLE);
            cityTxtS.setVisibility(View.VISIBLE);
            stateTxtS.setVisibility(View.VISIBLE);
            zipCodeTxtS.setVisibility(View.VISIBLE);

            setSecondryValues();
            setPrimaryValues();
            getPrimaryValues();
            getSecondryValues();
            userNameEdt.setVisibility(View.GONE);
            userNameTxt.setVisibility(View.VISIBLE);
            showContactDetailsSaveMode();
            showUserDetailsSaveMode();
            saveUserData();
            getUserData();
            allergydataChanged = false;
            cargiverdataChanged = false;
            dataChanged = false;
        }
    }

    private void getUserData() {
        dbAdapter.openUSERDataBase();
        userDetailsWrapper = dbAdapter.getUserDetails();
        try {
            System.out.println("userid" + userDetailsWrapper.getUserId());
            AESCrypt aesCrypt = new AESCrypt("SquareBits");
            addressLine1Edt.setText(aesCrypt.decrypt(userDetailsWrapper.getAddressline1()));
            addressLine2Edt.setText(aesCrypt.decrypt(userDetailsWrapper.getAddressline2()));
            addressLine3Edt.setText(aesCrypt.decrypt(userDetailsWrapper.getAddressline3()));
            cityEdt.setText(aesCrypt.decrypt(userDetailsWrapper.getCity()));
            stateEdt.setText(aesCrypt.decrypt(userDetailsWrapper.getState()));
            zipcodeEdt.setText(aesCrypt.decrypt(userDetailsWrapper.getZipcode()));
            emailIdEdt.setText(aesCrypt.decrypt(userDetailsWrapper.getEmailid()));
            homeCellEdt.setText(aesCrypt.decrypt(userDetailsWrapper.getContactcell()));
            homePhoneEdt.setText(aesCrypt.decrypt(userDetailsWrapper.getContacthome()));
            homeWorkEdt.setText(aesCrypt.decrypt(userDetailsWrapper.getContactwork()));
            addressLine1Txt.setText(aesCrypt.decrypt(userDetailsWrapper.getAddressline1()));
            addressLine2Txt.setText(aesCrypt.decrypt(userDetailsWrapper.getAddressline2()));
            addressLine3Txt.setText(aesCrypt.decrypt(userDetailsWrapper.getAddressline3()));
            cityTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getCity()));
            stateTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getState()));
            zipcodeTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getZipcode()));
            emailIdTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getEmailid()));
            homeCellTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getContactcell()));
            homePhoneTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getContacthome()));
            homeWorkTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getContactwork()));
//            userNameEdt.setText(userDetailsWrapper.getName());
            userNameEdt.setText(aesCrypt.decrypt(userDetailsWrapper.getName()));
            usernameTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getName()));
            userNameTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getName()));
            dobPickerButton.setText(aesCrypt.decrypt(userDetailsWrapper.getDob()));
            dobTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getDob()));
            heightBtn.setText(aesCrypt.decrypt(userDetailsWrapper.getHeight()));
            heightTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getHeight()));
            weightTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getWeight()));
            weightBtn.setText(aesCrypt.decrypt(userDetailsWrapper.getWeight()));
            bloodGroupTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getBloodgroup()));
            bloodTypeBtn.setText(aesCrypt.decrypt(userDetailsWrapper.getBloodgroup()));
            maritalStatusTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getMaritalstatus()));
            maritalStatusBtn.setText(aesCrypt.decrypt(userDetailsWrapper.getMaritalstatus()));
            livingWithTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getLivingstatus()));
            livingWithBtn.setText(aesCrypt.decrypt(userDetailsWrapper.getLivingstatus()));
            ssnEdt.setText(aesCrypt.decrypt(userDetailsWrapper.getSsn()));
            ssnTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getSsn()));
//        wrapper.setCountrycodecell(homephoneTxt.getText().toString());
//        imagePath = wrapper.getProfile_image();
            System.out.println("imagePath" + MyApplication.getApplication().getProfileImage());
            if (!TextUtils.isEmpty(MyApplication.getApplication().getProfileImage())) {
                MyApplication.getApplication().loader.displayImage("file://"
                        + MyApplication.getApplication().getProfileImage(), userImgView, MyApplication.option2);
                setprimaryandsecImg();
            } else {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File sdIconStorageDir = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + getResources().getString(R.string.imagepath) + "/");
                    if (!sdIconStorageDir.exists()) {
                        sdIconStorageDir.mkdirs();
                    }
                    mFileTemp = new File(Environment.getExternalStorageDirectory()
                            + "/" + getResources().getString(R.string.imagepath),
                            "UserImage.png");
                } else {
                    mFileTemp = new File(getFilesDir(), "UserImage.png");
                }
                if (mFileTemp.exists()) {
                    MyApplication.saveProfileImage(mFileTemp.getAbsolutePath());
                    MyApplication.getApplication().loader.displayImage("file://"
                            + mFileTemp.toString(), userImgView, MyApplication.option2);
                }
                setprimaryandsecImg();
            }

            sexText.setText(aesCrypt.decrypt(userDetailsWrapper.getGender()));
            if (userDetailsWrapper.getGender() != null && aesCrypt.decrypt(userDetailsWrapper.getGender()).equalsIgnoreCase("Male")) {
                sexString = "Male";
                radioMale.setChecked(true);

            } else if (userDetailsWrapper.getGender() != null && aesCrypt.decrypt(userDetailsWrapper.getGender()).equalsIgnoreCase("Female")) {
                sexString = "Female";
                radioFemale.setChecked(true);


            } else {
                sexString = "Don't Disclose";
                radioDonotdisclose.setChecked(true);
            }
            homephoneTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getCountrycodehome()));
            workphoneTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getCountrycodework()));
            mobilephoneTxt.setText(aesCrypt.decrypt(userDetailsWrapper.getCountrycodecell()));
            dbAdapter.closeUserDB();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setprimaryandsecImg() {
        File primaryImage, secondaryImg;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdIconStorageDir = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + getResources().getString(R.string.imagepath) + "/");
            // create storage directories, if they don't exist
            if (!sdIconStorageDir.exists()) {
                sdIconStorageDir.mkdirs();
            }
            primaryImage = new File(Environment.getExternalStorageDirectory()
                    + "/" + getResources().getString(R.string.imagepath),
                    "PrimaryImage.png");
            secondaryImg = new File(Environment.getExternalStorageDirectory()
                    + "/" + getResources().getString(R.string.imagepath),
                    "SecondaryImage.png");
        } else {
            primaryImage = new File(getFilesDir(), "PrimaryImage.png");
            secondaryImg = new File(getFilesDir(), "SecondaryImage.png");
        }
        if (primaryImage.exists()) {
            Bitmap bitmap1 = BitmapFactory.decodeFile(primaryImage.getAbsolutePath());
            insuranceImgViewS.setImageBitmap(bitmap1);
//            MyApplication.getApplication().loader.displayImage("file://"
//                    + primaryImage.toString(), insuranceImgView, MyApplication.options);
        }
        if (secondaryImg.exists()) {
            Bitmap bitmap1 = BitmapFactory.decodeFile(secondaryImg.getAbsolutePath());
            insuranceImgViewS.setImageBitmap(bitmap1);
//            MyApplication.getApplication().loader.displayImage("file://"
//                    + secondaryImg.toString(), insuranceImgViewS, MyApplication.options);
        }
    }

    private void saveUserData() {

        dbAdapter.openUSERDataBase();
        UserDetailsWrapper wrapper = new UserDetailsWrapper();

        try {


            AESCrypt aesCrypt = new AESCrypt("SquareBits");
            wrapper.setUserId("1");
            wrapper.setHeight(aesCrypt.encrypt(heightBtn.getText().toString()));
            wrapper.setSsn(aesCrypt.encrypt(ssnEdt.getText().toString()));
            wrapper.setZipcode(aesCrypt.encrypt(zipcodeEdt.getText().toString()));
            wrapper.setMaritalstatus(aesCrypt.encrypt((String) maritalStatusBtn.getText().toString()));
            wrapper.setLivingstatus(aesCrypt.encrypt((String) livingWithBtn.getText().toString()));

            wrapper.setWeight(aesCrypt.encrypt(weightBtn.getText().toString()));
            wrapper.setAddressline1(aesCrypt.encrypt(addressLine1Edt.getText().toString()));
            wrapper.setAddressline2(aesCrypt.encrypt(addressLine2Edt.getText().toString()));
            wrapper.setAddressline3(aesCrypt.encrypt(addressLine3Edt.getText().toString()));
            wrapper.setBloodgroup(aesCrypt.encrypt((String) bloodTypeBtn.getText().toString()));
            wrapper.setCity(aesCrypt.encrypt(cityEdt.getText().toString()));
            wrapper.setState(aesCrypt.encrypt(stateEdt.getText().toString()));
            wrapper.setContactcell(aesCrypt.encrypt(homeCellEdt.getText().toString()));
            wrapper.setContacthome(aesCrypt.encrypt(homePhoneEdt.getText().toString()));
            wrapper.setContactwork(aesCrypt.encrypt(homeWorkEdt.getText().toString()));
            wrapper.setEmailid(aesCrypt.encrypt(emailIdEdt.getText().toString()));
            wrapper.setName(aesCrypt.encrypt(userNameEdt.getText().toString()));
//            wrapper.setName(userNameEdt.getText().toString());
            wrapper.setWeight(aesCrypt.encrypt(weightBtn.getText().toString()));
            wrapper.setDob(aesCrypt.encrypt(dobPickerButton.getText().toString()));
            wrapper.setGender(aesCrypt.encrypt(sexString));
            MyApplication.saveStringPrefs(Constants.genderStr, sexString);
            wrapper.setCountrycodecell(aesCrypt.encrypt(mobilephoneTxt.getText().toString()));
            wrapper.setCountrycodework(aesCrypt.encrypt(workphoneTxt.getText().toString()));
            wrapper.setCountrycodehome(aesCrypt.encrypt(homephoneTxt.getText().toString()));

            if (!TextUtils.isEmpty(imagePath)) {
                MyApplication.getApplication().saveProfileImage(imagePath);
            }
            if (TextUtils.isEmpty(userDetailsWrapper.getUserId())) {
                dbAdapter.saveUserDetailsp(wrapper);
                dbAdapter.closeUserDB();
//                Intent service_intent = new Intent(ProfileActivity.this, UserDataUploadService.class);
//                startService(service_intent);
                MyApplication.saveLocalData(true);
                new UserUpdateOnClass(ProfileActivity.this, this);
            } else {
                dbAdapter.updateUserDetailsp(wrapper, 1);// if no userid found, use 1 as temp userid
                dbAdapter.closeUserDB();
//                Intent service_intent = new Intent(ProfileActivity.this, UserDataUploadService.class);
//                startService(service_intent);
                MyApplication.saveLocalData(true);
                new UserUpdateOnClass(ProfileActivity.this, this);
                if (dataChanged) {
                    new UpdateOnClass(ProfileActivity.this, this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        exportContact("", userNameEdt.getText().toString(), homeCellEdt.getText().toString(), emailIdEdt.getText().toString());
    }

    private void showContactDetailsEditMode() {
        addressLine1Edt.setVisibility(View.VISIBLE);
        addressLine2Edt.setVisibility(View.VISIBLE);
        addressLine3Edt.setVisibility(View.VISIBLE);
        cityEdt.setVisibility(View.VISIBLE);
        stateEdt.setVisibility(View.VISIBLE);
        zipcodeEdt.setVisibility(View.VISIBLE);
        emailIdEdt.setVisibility(View.VISIBLE);
        homeCellEdt.setVisibility(View.VISIBLE);
        homePhoneEdt.setVisibility(View.VISIBLE);
        homeWorkEdt.setVisibility(View.VISIBLE);
        addressLine1Txt.setVisibility(View.GONE);
        addressLine2Txt.setVisibility(View.GONE);
        addressLine3Txt.setVisibility(View.GONE);
        cityTxt.setVisibility(View.GONE);
        stateTxt.setVisibility(View.GONE);
        zipcodeTxt.setVisibility(View.GONE);
        emailIdTxt.setVisibility(View.GONE);
        mobilephoneTxt.setVisibility(View.VISIBLE);
        homephoneTxt.setVisibility(View.VISIBLE);
        workphoneTxt.setVisibility(View.VISIBLE);
        dobTxt.setVisibility(View.GONE);
        mobilephoneTxt.setEnabled(true);
        homephoneTxt.setEnabled(true);
        workphoneTxt.setEnabled(true);
    }
    private void showContactDetailsSaveMode() {
        addressLine1Edt.setVisibility(View.GONE);
        addressLine2Edt.setVisibility(View.GONE);
        addressLine3Edt.setVisibility(View.GONE);
        cityEdt.setVisibility(View.GONE);
        stateEdt.setVisibility(View.GONE);
        zipcodeEdt.setVisibility(View.GONE);
        emailIdEdt.setVisibility(View.GONE);
        homeCellEdt.setVisibility(View.GONE);
        homePhoneEdt.setVisibility(View.GONE);
        homeWorkEdt.setVisibility(View.GONE);
        homephoneTxt.setVisibility(View.VISIBLE);
        workphoneTxt.setVisibility(View.VISIBLE);
        mobilephoneTxt.setVisibility(View.VISIBLE);
        homePhoneTxt.setVisibility(View.VISIBLE);
        homeWorkTxt.setVisibility(View.VISIBLE);
        homeCellTxt.setVisibility(View.VISIBLE);
        addressLine1Txt.setVisibility(View.VISIBLE);
        addressLine2Txt.setVisibility(View.VISIBLE);
        addressLine3Txt.setVisibility(View.VISIBLE);
        cityTxt.setVisibility(View.VISIBLE);
        stateTxt.setVisibility(View.VISIBLE);
        zipcodeTxt.setVisibility(View.VISIBLE);
        emailIdTxt.setVisibility(View.VISIBLE);
        mobilephoneTxt.setEnabled(false);
        homephoneTxt.setEnabled(false);
        workphoneTxt.setEnabled(false);
    }
    private void showUserDetailsEditMode() {
        heightBtn.setVisibility(View.VISIBLE);
        weightBtn.setVisibility(View.VISIBLE);
        bloodTypeBtn.setVisibility(View.VISIBLE);
        maritalStatusBtn.setVisibility(View.VISIBLE);
        livingWithBtn.setVisibility(View.VISIBLE);
        ssnEdt.setVisibility(View.VISIBLE);
        dobPickerButton.setVisibility(View.VISIBLE);
        radioSex.setVisibility(View.VISIBLE);
        sexText.setVisibility(View.GONE);
        heightTxt.setVisibility(View.GONE);
        weightTxt.setVisibility(View.GONE);
        bloodGroupTxt.setVisibility(View.GONE);
        maritalStatusTxt.setVisibility(View.GONE);
        livingWithTxt.setVisibility(View.GONE);
        ssnTxt.setVisibility(View.GONE);
        dobTxt.setVisibility(View.GONE);
    }
    private void showUserDetailsSaveMode() {
        heightBtn.setVisibility(View.GONE);
        weightBtn.setVisibility(View.GONE);
        bloodTypeBtn.setVisibility(View.GONE);
        maritalStatusBtn.setVisibility(View.GONE);
        livingWithBtn.setVisibility(View.GONE);
        ssnEdt.setVisibility(View.GONE);
        dobPickerButton.setVisibility(View.GONE);
        radioSex.setVisibility(View.GONE);
        sexText.setVisibility(View.VISIBLE);
        heightTxt.setVisibility(View.VISIBLE);
        weightTxt.setVisibility(View.VISIBLE);
        bloodGroupTxt.setVisibility(View.VISIBLE);
        maritalStatusTxt.setVisibility(View.VISIBLE);
        livingWithTxt.setVisibility(View.VISIBLE);
        ssnTxt.setVisibility(View.VISIBLE);
        dobTxt.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.userImgView)
    public void userImgView() {
        isPrimaryImageSelected = false;
        isSecondaryImageSelected = false;
        uploadImage(true);
    }
    private void setimagepath(boolean isProfileimage) {
        String fileName = "IMG_"
                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
                .toString() + ".jpg";
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdIconStorageDir = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + getResources().getString(R.string.imagepath) + "/");
            // create storage directories, if they don't exist
            sdIconStorageDir.mkdirs();
            if (isProfileimage) {
                File checkmFileTemp = new File(Environment.getExternalStorageDirectory()
                        + "/" + getResources().getString(R.string.imagepath) + "/",
                        "UserImage.png");
                if (checkmFileTemp.exists()) {
                    checkmFileTemp.delete();
                }

                mFileTemp = new File(Environment.getExternalStorageDirectory()
                        + "/" + getResources().getString(R.string.imagepath) + "/",
                        "UserImage.png");

            } else {
                if (isPrimaryImageSelected) {
                    mFileTemp = new File(Environment.getExternalStorageDirectory()
                            + "/" + getResources().getString(R.string.imagepath) + "/",
                            "PrimaryImage.png");
                } else if (isSecondaryImageSelected) {
                    mFileTemp = new File(Environment.getExternalStorageDirectory()
                            + "/" + getResources().getString(R.string.imagepath) + "/",
                            "SecondaryImage.png");
                }
            }
        } else {
            if (isProfileimage) {
                File checkmFileTemp = new File(getFilesDir(), "UserImage.png");
                if (checkmFileTemp.exists()) {
                    checkmFileTemp.delete();
                }
                mFileTemp = new File(getFilesDir(), "UserImage.png");
            } else {
                if (isPrimaryImageSelected) {
                    mFileTemp = new File(getFilesDir(), "PrimaryImage.png");
                } else if (isSecondaryImageSelected) {
                    mFileTemp = new File(getFilesDir(), "SecondaryImage.png");
                }
            }
        }
    }
    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }
    private void startCropImage() {
        try {
            System.out.println("on activity result startcrop functions");
            Intent intent = new Intent(ProfileActivity.this, CropImage.class);
            intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
            intent.putExtra(CropImage.SCALE, true);

            intent.putExtra(CropImage.ASPECT_X, 1);
            intent.putExtra(CropImage.ASPECT_Y, 1);

            startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            mImageCaptureUri = Uri.fromFile(mFileTemp);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            Log.d("", "cannot take picture", e);
        }
    }
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(mFileTemp);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode != RESULT_OK) {

                return;
            }

            Bitmap bitmap;

            if (requestCode == REQUEST_CODE_GALLERY) {

                try {

                    System.out.println("on activity result gallery");
                    InputStream inputStream = getContentResolver()
                            .openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {

                startCropImage();

            } else if (requestCode == REQUEST_CODE_CROP_IMAGE) {

                System.out.println("on activity result crop");
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {

                    return;
                }

                if (isPrimaryImageSelected) {
                    dataChanged = true;
                    imagePathPrimary = mFileTemp.getPath();

                    Bitmap bitmap1 = BitmapFactory.decodeFile(imagePathPrimary);
                    insuranceImgView.setImageBitmap(bitmap1);

//                    MyApplication.getApplication().loader.displayImage("file://"
//                            + imagePathPrimary, insuranceImgView, MyApplication.options);
                } else if (isSecondaryImageSelected) {
                    imagePathSecondry = mFileTemp.getPath();
                    dataChanged = true;
                    Bitmap bitmap1 = BitmapFactory.decodeFile(imagePathSecondry);
                    insuranceImgViewS.setImageBitmap(bitmap1);
//                    MyApplication.getApplication().loader.displayImage("file://"
//                            + imagePathSecondry, insuranceImgViewS, MyApplication.options);
                } else {

                    imagePath = mFileTemp.getPath();
                    Bitmap bitmap1 = BitmapFactory.decodeFile(imagePath);
                    userImgView.setImageBitmap(bitmap1);
//                    MyApplication.getApplication().loader.displayImage("file://"
//                            + mFileTemp, userImgView, MyApplication.option2);

                    System.out.println("image path:" + mFileTemp.getPath());
                }
            }

            // bundle2.putString("path", mFileTemp.getPath());


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void uploadImage(final boolean isProfile) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ProfileActivity.this);

        // set title
        alertDialogBuilder.setTitle("Choose option");

        // set dialog message
        alertDialogBuilder
                .setMessage("Please select image from ")
                .setCancelable(false)
                .setPositiveButton("Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, closeUserDB
                                // current activity
                                dialog.cancel();
                                setimagepath(isProfile);
                                takePicture();

                            }

                        })
                .setNegativeButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just closeUserDB
                                // the dialog box and do nothing
                                dialog.cancel();
                                setimagepath(isProfile);
                                openGallery();
                            }

                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setCancelable(true);
        // show it
        alertDialog.show();
    }

    private void showPopUp(final int type) {
        DBAdapter dbAdapter = new DBAdapter(ProfileActivity.this);
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
                if (type == 1) {
                    homephoneTxt.setText("+" + countrynameStringStringHashMap.get(countryList1.get(position)));
                } else if (type == 2) {
                    workphoneTxt.setText("+" + countrynameStringStringHashMap.get(countryList1.get(position)));

                } else {
                    mobilephoneTxt.setText("+" + countrynameStringStringHashMap.get(countryList1.get(position)));
                }
            }
        });
    }
    @OnClick(R.id.insuranceImgView)
    public void insuranceImgView() {
        isPrimaryImageSelected = true;
        isSecondaryImageSelected = false;
        uploadImage(false);
    }
    @OnClick(R.id.insuranceImgView_S)
    public void insuranceImgView_S() {
        isPrimaryImageSelected = false;
        isSecondaryImageSelected = true;
        uploadImage(false);
    }
    @OnClick(R.id.editImgBtn_P)
    public void editImgBtn_P() {
        isEditMode_P = !isEditMode_P;
        if (isEditMode_P) {
            editImgBtnP.setImageResource(R.mipmap.icn_save);
            optionEdt.setVisibility(View.VISIBLE);
            companyNameEdt.setVisibility(View.VISIBLE);
            phoneNoEdt.setVisibility(View.VISIBLE);
            employerEdt.setVisibility(View.VISIBLE);
            groupEdt.setVisibility(View.VISIBLE);
            prescriptionEdt.setVisibility(View.VISIBLE);
            addressEdt.setVisibility(View.VISIBLE);
            cityEdtP.setVisibility(View.VISIBLE);
            stateEdtP.setVisibility(View.VISIBLE);
            zipCodeEdt.setVisibility(View.VISIBLE);
            primaryInsuranceLl.setVisibility(View.VISIBLE);
            optionTxt.setVisibility(View.GONE);
            companyNameTxt.setVisibility(View.GONE);
            phoneNoTxt.setVisibility(View.GONE);
            employerTxt.setVisibility(View.GONE);
            groupTxt.setVisibility(View.GONE);
            prescriptionTxt.setVisibility(View.GONE);
            addressTxt.setVisibility(View.GONE);
            cityTxtP.setVisibility(View.GONE);
            stateTxtP.setVisibility(View.GONE);
            zipCodeTxt.setVisibility(View.GONE);
        } else {
            editImgBtnP.setImageResource(R.mipmap.icn_edit_aboutlabel);
            optionEdt.setVisibility(View.GONE);
            companyNameEdt.setVisibility(View.GONE);
            phoneNoEdt.setVisibility(View.GONE);
            employerEdt.setVisibility(View.GONE);
            groupEdt.setVisibility(View.GONE);
            prescriptionEdt.setVisibility(View.GONE);
            addressEdt.setVisibility(View.GONE);
            cityEdtP.setVisibility(View.GONE);
            stateEdtP.setVisibility(View.GONE);
            zipCodeEdt.setVisibility(View.GONE);
            optionTxt.setVisibility(View.VISIBLE);
            companyNameTxt.setVisibility(View.VISIBLE);
            phoneNoTxt.setVisibility(View.VISIBLE);
            employerTxt.setVisibility(View.VISIBLE);
            groupTxt.setVisibility(View.VISIBLE);
            prescriptionTxt.setVisibility(View.VISIBLE);
            addressTxt.setVisibility(View.VISIBLE);
            cityTxtP.setVisibility(View.VISIBLE);
            stateTxtP.setVisibility(View.VISIBLE);
            zipCodeTxt.setVisibility(View.VISIBLE);
            setPrimaryValues();
            getPrimaryValues();
        }
    }
    @OnClick(R.id.editImgBtn_S)
    public void editImgBtn_S() {
        isEditMode_S = !isEditMode_S;
        if (isEditMode_S) {
//                editImgBtn_P.setImageResource(R.mipmap.);
            editImgBtnS.setImageResource(R.mipmap.icn_save);
            optionEdtS.setVisibility(View.VISIBLE);
            companyNameEdtS.setVisibility(View.VISIBLE);
            phoneNoEdtS.setVisibility(View.VISIBLE);
            employerEdtS.setVisibility(View.VISIBLE);
            groupEdtS.setVisibility(View.VISIBLE);
            prescriptionEdtS.setVisibility(View.VISIBLE);
            addressEdtS.setVisibility(View.VISIBLE);
            cityEdtS.setVisibility(View.VISIBLE);
            stateEdtS.setVisibility(View.VISIBLE);
            zipCodeEdtS.setVisibility(View.VISIBLE);
            secondryInsuranceLl.setVisibility(View.VISIBLE);
            optionTxtS.setVisibility(View.GONE);
            companyNameTxtS.setVisibility(View.GONE);
            phoneNoTxtS.setVisibility(View.GONE);
            employerTxtS.setVisibility(View.GONE);
            groupTxtS.setVisibility(View.GONE);
            prescriptionTxtS.setVisibility(View.GONE);
            addressTxtS.setVisibility(View.GONE);
            cityTxtS.setVisibility(View.GONE);
            stateTxtS.setVisibility(View.GONE);
            zipCodeTxtS.setVisibility(View.GONE);
        } else {
//                editImgBtnP.setImageResource(R.mipmap.icneditaboutlabel);
            editImgBtnS.setImageResource(R.mipmap.icn_edit_aboutlabel);
            optionEdtS.setVisibility(View.GONE);
            companyNameEdtS.setVisibility(View.GONE);
            phoneNoEdtS.setVisibility(View.GONE);
            employerEdtS.setVisibility(View.GONE);
            groupEdtS.setVisibility(View.GONE);
            prescriptionEdtS.setVisibility(View.GONE);
            addressEdtS.setVisibility(View.GONE);
            cityEdtS.setVisibility(View.GONE);
            stateEdtS.setVisibility(View.GONE);
            zipCodeEdtS.setVisibility(View.GONE);
            optionTxtS.setVisibility(View.VISIBLE);
            companyNameTxtS.setVisibility(View.VISIBLE);
            phoneNoTxtS.setVisibility(View.VISIBLE);
            employerTxtS.setVisibility(View.VISIBLE);
            groupTxtS.setVisibility(View.VISIBLE);
            prescriptionTxtS.setVisibility(View.VISIBLE);
            addressTxtS.setVisibility(View.VISIBLE);
            cityTxtS.setVisibility(View.VISIBLE);
            stateTxtS.setVisibility(View.VISIBLE);
            zipCodeTxtS.setVisibility(View.VISIBLE);
            getSecondryValues();
            setSecondryValues();
        }
    }
    private void getPrimaryValues() {
        dbAdapter.openMdsDB();
        InsuranceWrapper wrapper = new InsuranceWrapper();
        wrapper = dbAdapter.getInsuranceHashMap().get(Constants.INSURANCE_PRIMARY);
        if (wrapper != null) {
            optionTxt.setText(wrapper.getCompanyoption());
            companyNameTxt.setText(wrapper.getCompanyname());
            phoneNoTxt.setText(wrapper.getPhoneno());
            employerTxt.setText(wrapper.getEmployer());
            prescriptionTxt.setText(wrapper.getPrescription());
            addressTxt.setText(wrapper.getAddress());
            cityTxtP.setText(wrapper.getI_city());
            stateTxtP.setText(wrapper.getI_state());
            zipCodeTxt.setText(wrapper.getI_zipcode());
            groupTxt.setText(wrapper.getCompanygroup());
            optionEdt.setText(wrapper.getCompanyoption());
            companyNameEdt.setText(wrapper.getCompanyname());
            phoneNoEdt.setText(wrapper.getPhoneno());
            employerEdt.setText(wrapper.getEmployer());
            prescriptionEdt.setText(wrapper.getPrescription());
            addressEdt.setText(wrapper.getAddress());
            cityEdtP.setText(wrapper.getI_city());
            stateEdtP.setText(wrapper.getI_state());
            zipCodeEdt.setText(wrapper.getI_zipcode());
            groupEdt.setText(wrapper.getCompanygroup());
            imagePathPrimary = wrapper.getCompanyimagename();
//            MyApplication.getApplication().loader.displayImage("file://"
//                    + wrapper.getCompanyimagename(), insuranceImgView, MyApplication.options);
            optionEdt.addTextChangedListener(textWatcher);
            companyNameEdt.addTextChangedListener(textWatcher);
//            phoneNoEdt.addTextChangedListener(textWatcher);
            employerEdt.addTextChangedListener(textWatcher);
            prescriptionEdt.addTextChangedListener(textWatcher);
            addressEdt.addTextChangedListener(textWatcher);
            cityEdtP.addTextChangedListener(textWatcher);
            stateEdtP.addTextChangedListener(textWatcher);
            zipCodeEdt.addTextChangedListener(textWatcher);
            groupEdt.addTextChangedListener(textWatcher);
            employerTxt.addTextChangedListener(textWatcher);
        }
        dbAdapter.closeMdsDB();
    }
    private void getSecondryValues() {
        dbAdapter.openMdsDB();
        InsuranceWrapper wrapper = new InsuranceWrapper();
        wrapper = dbAdapter.getInsuranceHashMap().get(Constants.INSURANCE_SECONDRY);
        if (wrapper != null) {
            optionTxtS.setText(wrapper.getCompanyoption());
            companyNameTxtS.setText(wrapper.getCompanyname());
            phoneNoTxtS.setText(wrapper.getPhoneno());
            employerTxtS.setText(wrapper.getEmployer());
            prescriptionTxtS.setText(wrapper.getPrescription());
            addressTxtS.setText(wrapper.getAddress());
            cityTxtS.setText(wrapper.getI_city());
            stateTxtS.setText(wrapper.getI_state());
            zipCodeTxtS.setText(wrapper.getI_zipcode());
            groupTxtS.setText(wrapper.getCompanygroup());
            imagePathSecondry = wrapper.getCompanyimagename();
//            MyApplication.getApplication().loader.displayImage("file://"
//                    + wrapper.getCompanyimagename(), insuranceImgViewS, MyApplication.options);

            optionEdtS.setText(wrapper.getCompanyoption());
            companyNameEdtS.setText(wrapper.getCompanyname());
            phoneNoEdtS.setText(wrapper.getPhoneno());
            employerEdtS.setText(wrapper.getEmployer());
            prescriptionEdtS.setText(wrapper.getPrescription());
            addressEdtS.setText(wrapper.getAddress());
            cityEdtS.setText(wrapper.getI_city());
            stateEdtS.setText(wrapper.getI_state());
            zipCodeEdtS.setText(wrapper.getI_zipcode());
            groupEdtS.setText(wrapper.getCompanygroup());
            optionEdtS.addTextChangedListener(textWatcher);
            companyNameEdtS.addTextChangedListener(textWatcher);
//            phoneNoEdtS.addTextChangedListener(textWatcher);
            employerEdtS.addTextChangedListener(textWatcher);
            prescriptionEdtS.addTextChangedListener(textWatcher);
            addressEdtS.addTextChangedListener(textWatcher);
            cityEdtS.addTextChangedListener(textWatcher);
            stateEdtS.addTextChangedListener(textWatcher);
            zipCodeEdtS.addTextChangedListener(textWatcher);
            groupEdtS.addTextChangedListener(textWatcher);
            employerTxtS.addTextChangedListener(textWatcher);


        }
        dbAdapter.closeMdsDB();

    }

    private void setPrimaryValues() {
        dbAdapter.openMdsDB();
        InsuranceWrapper wrapper = new InsuranceWrapper();
        wrapper.setAddress(addressEdt.getText().toString());
        wrapper.setCompanygroup(groupEdt.getText().toString());
        wrapper.setCompanyimagename("");
        wrapper.setCompanyname(companyNameEdt.getText().toString());
        wrapper.setCompanyoption(optionEdt.getText().toString());
        wrapper.setI_city(cityEdtP.getText().toString());
        wrapper.setI_state(stateEdtP.getText().toString());
        wrapper.setI_zipcode(zipCodeEdt.getText().toString());
        wrapper.setPhoneno(phoneNoEdt.getText().toString());
        wrapper.setPrescription(prescriptionEdt.getText().toString());
        wrapper.setEmployer(employerEdt.getText().toString());
        wrapper.setCompanyimagename(imagePathPrimary);
        dbAdapter.saveInsuranceHashMap(Constants.INSURANCE_PRIMARY, wrapper);
        // MyApplication.saveLocalData(true);
        dbAdapter.closeMdsDB();
//        new UpdateOnClass(MyApplication.getApplication(),this);
//        Intent service_intent = new Intent(ProfileActivity.this, DataUploadService.class);
//        startService(service_intent);
//        new UpdateOnClass(ProfileActivity.this,this);

    }

    private void setSecondryValues() {
        dbAdapter.openMdsDB();

        InsuranceWrapper wrapper = new InsuranceWrapper();
        wrapper.setAddress(addressEdtS.getText().toString());
        wrapper.setCompanygroup(groupEdtS.getText().toString());
        wrapper.setCompanyimagename("");
        wrapper.setCompanyname(companyNameEdtS.getText().toString());
        wrapper.setCompanyoption(optionEdtS.getText().toString());
        wrapper.setI_city(cityEdtS.getText().toString());
        wrapper.setI_state(stateEdtS.getText().toString());
        wrapper.setI_zipcode(zipCodeEdtS.getText().toString());
        wrapper.setPhoneno(phoneNoEdtS.getText().toString());
        wrapper.setPrescription(prescriptionEdtS.getText().toString());
        wrapper.setEmployer(employerEdtS.getText().toString());
        wrapper.setCompanyimagename(imagePathSecondry);
        dbAdapter.saveInsuranceHashMap(Constants.INSURANCE_SECONDRY, wrapper);
        //  MyApplication.saveLocalData(true);

        dbAdapter.closeMdsDB();
//        new UpdateOnClass(MyApplication.getApplication(),this);
//        Intent service_intent = new Intent(ProfileActivity.this, DataUploadService.class);
//        startService(service_intent);
//        new UpdateOnClass(ProfileActivity.this,this);

    }

    @OnClick(R.id.showHideImgBtn_P)
    public void showHideImgBtn_P() {
        if (primaryInsuranceLl.getVisibility() == View.VISIBLE) {
            showHideImgBtnP.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            primaryInsuranceLl.setVisibility(View.GONE);
        } else {
            showHideImgBtnP.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            primaryInsuranceLl.setVisibility(View.VISIBLE);
        }


    }


    @OnClick(R.id.showHideImgBtn_S)
    public void showHideImgBtn_S() {

        if (secondryInsuranceLl.getVisibility() == View.VISIBLE) {
            showHideImgBtnS.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            secondryInsuranceLl.setVisibility(View.GONE);
        } else {
            showHideImgBtnS.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            secondryInsuranceLl.setVisibility(View.VISIBLE);
        }


    }

    private String formateDate(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = originalFormat.parse(dateString);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onBackPressed() {

        MyApplication.getApplication().hideSoftKeyBoard(ProfileActivity.this);
        if (dataChanged) {
            saveAlert();
        } else {
            finish();
        }
    }
    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("" + getResources().getString(R.string.app_name3));
        builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            mHomeWatcher.stopWatch();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private TextWatcher textWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            dataChanged = true;
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        public void onTextChanged(CharSequence s, int start, int before,int count) {
            dataChanged = true;
        }
    };
    boolean allergydataChanged = false;
    private TextWatcher allergytextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            allergydataChanged = true;

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            allergydataChanged = true;
        }
    };
    boolean cargiverdataChanged = false;
    private TextWatcher CarGivertextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            cargiverdataChanged = true;

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            cargiverdataChanged = true;
        }
    };
}
