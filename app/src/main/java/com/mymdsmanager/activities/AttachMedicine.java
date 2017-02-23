package com.mymdsmanager.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.Constants;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.MedicalProfessionalWrapper;
import com.mymdsmanager.wrapper.MedicineInfoWrapper;
import com.mymdsmanager.wrapper.ReminderWrapper;
import com.mymdsmanager.wrapper.TreatmentMedicineInfoWrapper;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.janmuller.android.simplecropimage.CropImage;


public class AttachMedicine extends AppCompatActivity implements OnFinishActivity{

    @Bind(R.id.addmedicineBtn)
    Button addmedicineBtn;
    @Bind(R.id.dosageEdt)
    EditText dosageEdt;
    @Bind(R.id.daysEdt)
    EditText daysEdt;
    @Bind(R.id.mainaddmedicineBtn)
    Button mainaddmedicineBtn;
    @Bind(R.id.medicineEdt)
    TextView medicineEdt;
    private Toolbar toolbar;

    //add medicine dialog box decleration
    private final int DATE_DIALOG_ID = 1;
    private TextView addReminderTxt;
    private int year;
    private int month;
    private int day;

    private Button supplementsBtn, overTheCounterBtn, prescriptionBtn, addMedicationBtn;

    private File mFileTemp;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x53;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x52;
    public static final int REQUEST_CODE_GALLERY = 0x51;
    private LinearLayout takePhoto_ll;
    private String imagePath = "";
    private ListView medicineListView;
    private ArrayList<MedicineInfoWrapper> medicineList = new ArrayList<>();
    public static boolean show_dialog_flag = true;
    String start_date = "";
    private int START_DATE = 1, END_DATE = 2, REFILL_DATE = 3;
    private int SELECTED_DATE = -1;
    private DBAdapter dbAdapter;
    private Dialog dialog;
    private Button startDateBtn, endDateBtn, refillDateBtn;
    ArrayList<String> typeArr = new ArrayList<>();
    ArrayList<String> frequencyArr = new ArrayList<>();
    ArrayList<String> refillfrequencyArr = new ArrayList<>();
    MedicineInfoWrapper rowItem;
    Button prescribedBtn;
    Button addProviderBtn;
    ArrayList<String> reminderIDS;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attach_madicine_layout);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.mipmap.icn_back);
        getSupportActionBar().setTitle("Attach Medicine");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        typeArr.add("Prescription");
        typeArr.add("Over-The-Counter");
        typeArr.add("Supplements");
        typeArr.add("Other");
        dbAdapter = new DBAdapter(AttachMedicine.this);

//        frequencyArr.add("Every Day");
//        frequencyArr.add("Weekly");
//        frequencyArr.add("Monthly");
        frequencyArr.addAll(DataManager.getInstance().getFrequencyArraylist());
        frequencyArr.add("Other");
        refillfrequencyArr.addAll(DataManager.getInstance().getRefill_frequencyArraylist());
        refillfrequencyArr.add("Other");

        Collections.sort(refillfrequencyArr, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        Collections.sort(frequencyArr, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        mHomeWatcher = new HomeWatcher(AttachMedicine.this);
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
    }

    boolean isActivityFound = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @OnClick(R.id.medicineEdt)
    public void medicineEdt() {
        showPopUpMedicine("Select Medicine", medicineEdt);
    }


    @OnClick(R.id.addmedicineBtn)
    public void addmedicineBtn() {
        showMedicationDialog();
    }

    @OnClick(R.id.mainaddmedicineBtn)
    public void mainaddmedicineBtn() {
        if (TextUtils.isEmpty(medicineEdt.getText().toString())) {
            medicineEdt.setError("Please Enter Medicine");
            medicineEdt.requestFocus();
        } else if (TextUtils.isEmpty(dosageEdt.getText().toString())) {
            dosageEdt.setError("Please Enter Dosage Number");
            dosageEdt.requestFocus();
        } else {
            TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper = new TreatmentMedicineInfoWrapper();

            treatmentMedicineInfoWrapper.setDays(daysEdt.getText().toString());
            treatmentMedicineInfoWrapper.setDosage(dosageEdt.getText().toString());
            treatmentMedicineInfoWrapper.setMedicinename(medicineEdt.getText().toString());
            treatmentMedicineInfoWrapper.setType("M");
            DataManager.getInstance().treatmentMedicineInfoWrappers.add(treatmentMedicineInfoWrapper);
            finish();
        }
    }

    private void showMedicationDialog() {

        dialog = new Dialog(AttachMedicine.this, R.style.AppCompatTheme);

        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.pop_up_add_medicine);


        LinearLayout takePhotoLl = (LinearLayout) dialog.findViewById(R.id.takePhoto_ll);
        LinearLayout addProviderLl = (LinearLayout) dialog.findViewById(R.id.addProviderLl);
        final EditText drugNameEdt = (EditText) dialog.findViewById(R.id.drugNameEdt);
        final EditText genericEdt = (EditText) dialog.findViewById(R.id.genericEdt);
        final EditText dosageEdt = (EditText) dialog.findViewById(R.id.dosageEdt);
        dosageEdt.setCursorVisible(false);
        dosageEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dosageEdt.setCursorVisible(true);
            }
        });
        final Spinner typeSpinner = (Spinner) dialog.findViewById(R.id.typeSpinner);
        final Spinner frequencySpinner = (Spinner) dialog.findViewById(R.id.frequencySpinner);
        startDateBtn = (Button) dialog.findViewById(R.id.startDateBtn);
        endDateBtn = (Button) dialog.findViewById(R.id.endDateBtn);
        CheckBox progressCheckBox = (CheckBox) dialog.findViewById(R.id.progressCheckBox);
        addReminderTxt = (TextView) dialog.findViewById(R.id.addReminderTxt);
        refillDateBtn = (Button) dialog.findViewById(R.id.refillDateBtn);
        final Spinner refillfrequencySpinner = (Spinner) dialog.findViewById(R.id.refillfrequencySpinner);
        prescribedBtn = (Button) dialog.findViewById(R.id.prescribedBtn);
        addProviderBtn = (Button) dialog.findViewById(R.id.addProviderBtn);
        final TextView saveTxt = (TextView) dialog.findViewById(R.id.saveTxt);
        final TextView closeTxt = (TextView) dialog.findViewById(R.id.closeTxt);
        final EditText notesEdt = (EditText) dialog.findViewById(R.id.notesEdt);
        addReminderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reminderIntent = new Intent(AttachMedicine.this, ReminderAcivity.class);
                if (show_dialog_flag == false) {
                    reminderIntent.putExtra("wrapper", rowItem);
                }
                startActivity(reminderIntent);

            }
        });
        addProviderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AttachMedicine.this, AddMedicalProfessionalActivity.class));
            }
        });
// Check if no view has focus:
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED_DATE = START_DATE;

                showDialog(DATE_DIALOG_ID);
            }
        });
        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED_DATE = END_DATE;

                showDialog(DATE_DIALOG_ID);
            }
        });
        refillDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SELECTED_DATE = REFILL_DATE;
                showDialog(DATE_DIALOG_ID);
            }
        });


        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        closeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                rowItem = null;
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                rowItem = null;
            }
        });
        saveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("reminder" + DataManager.getInstance().getReminderTimeList().toString().replace("[", "").replace("]", "")
                        .replace(", ", ","));
                ReminderWrapper reminderWrapper;
                MedicineInfoWrapper wrapper = new MedicineInfoWrapper();
                reminderWrapper = DataManager.getInstance().getReminderWrapper();
                wrapper.setReminderstarttime(reminderWrapper.getReminderstarttime());
                wrapper.setRemindersoundindex(reminderWrapper.getRemindersoundindex());
                wrapper.setScheduledays(reminderWrapper.getScheduledays());
                wrapper.setRemindersoundfile(reminderWrapper.getRemindersoundfile());
                wrapper.setRemindercounts(reminderWrapper.getRemindercounts());
                if (TextUtils.isEmpty(drugNameEdt.getText().toString())) {
                    drugNameEdt.setError("Please Enter Drug Name");
                    drugNameEdt.requestFocus();

                } else if (TextUtils.isEmpty(genericEdt.getText().toString())) {
                    genericEdt.setError("Please Enter Generic Name");
                    genericEdt.requestFocus();
                } else if (TextUtils.isEmpty(dosageEdt.getText().toString())) {
                    dosageEdt.setError("Please Enter Dosage");
                    dosageEdt.requestFocus();
                } else {

                    dbAdapter.openMdsDB();

                    wrapper.setRefilfrequency(refillfrequencySpinner.getSelectedItem().toString());
                    wrapper.setPrescirbedby(prescribedBtn.getText().toString());
                    wrapper.setNotes(notesEdt.getText().toString());
                    wrapper.setRefildate(refillDateBtn.getText().toString());
                    wrapper.setType(typeSpinner.getSelectedItem().toString());
                    wrapper.setReminderstring(DataManager.getInstance().getReminderTimeList().toString().replace("[", "").replace("]", "")
                            .replace(", ", ","));
                    wrapper.setGenericname(genericEdt.getText().toString());
                    wrapper.setDosage(dosageEdt.getText().toString());
                    wrapper.setDrugname(drugNameEdt.getText().toString());
                    wrapper.setStartdate(startDateBtn.getText().toString());
                    wrapper.setEnddate(endDateBtn.getText().toString());
                    wrapper.setImagename(imagePath);
                    wrapper.setFrequency(frequencySpinner.getSelectedItem().toString());
//                    if (reminderIDS.size() != 0) {
//                        String reminder_id = TextUtils.join(",", reminderIDS);
//                        wrapper.setREMINDERIDS(reminder_id);
//                    }


//                if (TextUtils.isEmpty(wrapper.getRefildate()) || TextUtils.isEmpty(wrapper.getStartdate()) || TextUtils.isEmpty(wrapper.getEnddate()) || TextUtils.isEmpty(wrapper.getFrequency()) || TextUtils.isEmpty(wrapper.getDosage()) || TextUtils.isEmpty(wrapper.getDrugname()) || TextUtils.isEmpty(wrapper.getGenericname()) || TextUtils.isEmpty(wrapper.getRefildate()) || TextUtils.isEmpty(wrapper.getStartdate()) || TextUtils.isEmpty(wrapper.getEnddate()) || TextUtils.isEmpty(wrapper.getFrequency()) || TextUtils.isEmpty(wrapper.getDosage()) || TextUtils.isEmpty(wrapper.getRefilfrequency()) || TextUtils.isEmpty(wrapper.getPrescirbedby())) {
//
//                    Toast.makeText(MedicationActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
//
//                } else {

                    dbAdapter.saveMedicineInfo(wrapper);
                    MyApplication.saveLocalData(true);
                    medicineEdt.setText(drugNameEdt.getText().toString());
                    dbAdapter.closeMdsDB();

                    new UpdateOnClass(MyApplication.getApplication(),AttachMedicine.this);
                    dialog.dismiss();
//                }
                    Constants.add_reminder = true;

                    if (!DataManager.getInstance().getReminderTimeList().isEmpty()) {
                        String csv = DataManager.getInstance().getReminderTimeList().toString().replace("[", "").replace("]", "")
                                .replace(", ", ",");

                        String time = csv.replace("", "");
                        String timeStr[] = time.split(",");
                        for (int i = 0; i < timeStr.length; i++) {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
                            try {

                                System.out.println("time" + time);
                                String time_end = timeStr[i].substring(1, 5);
                                Date dateObj = sdf.parse(time);
                                String[] houre_sec = time_end.split(":");
                                Calendar calendar = Calendar.getInstance();

                                calendar.set(Calendar.HOUR, Integer.parseInt(houre_sec[0]));
                                calendar.set(Calendar.MINUTE, Integer.parseInt(houre_sec[1]));
                                calendar.set(Calendar.SECOND, 0);
                                int AM_orPM = calendar.get(Calendar.AM_PM);
                                calendar.set(Calendar.AM_PM, AM_orPM);
                                int id = (int) System.currentTimeMillis();
                                createScheduledNotification(drugNameEdt.getText().toString(), calendar, id);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        prescribedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp("Select Professional", prescribedBtn);

            }
        });

        addProviderLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp("Select Professional", prescribedBtn);

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeArr);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == typeArr.size() - 1) {
                    showAddOthersDialog(typeSpinner, "type");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> frequncyadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, frequencyArr);
        frequencySpinner.setAdapter(frequncyadapter);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == frequencyArr.size() - 1) {
                    showAddOthersDialog(frequencySpinner, "frequency");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> refillfrequncyadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, refillfrequencyArr);
        refillfrequencySpinner.setAdapter(refillfrequncyadapter);

        refillfrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == frequencyArr.size() - 1) {
                    showAddOthersDialog(refillfrequencySpinner, "refill");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        takePhoto_ll = (LinearLayout) dialog.findViewById(R.id.takePhoto_ll);
        final TextView frequencyTxt = (TextView) dialog.findViewById(R.id.frequencyTxt);
        final TextView refillfrequencyTxt = (TextView) dialog.findViewById(R.id.refillfrequencytxt);
        final TextView type_txt = (TextView) dialog.findViewById(R.id.type_txt);
        takePhoto_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachImage();
            }
        });

        dialog.show();

    }

    private void showPopUp(final String title, final Button button) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        dbAdapter.openMdsDB();

        ArrayList<MedicalProfessionalWrapper> wrapperList = dbAdapter.getMedicalProfessionalWrapper();

        final ArrayList<String> professionalList = new ArrayList<>();
        for (int i = 0; i < wrapperList.size(); i++) {
            professionalList.add(wrapperList.get(i).getProvidername());
        }


        dbAdapter.closeMdsDB();
        ListView modeList = new ListView(this);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, professionalList);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);
        final Dialog dialog = builder.create();
        if (professionalList.isEmpty()) {

            startActivity(new Intent(AttachMedicine.this, AddMedicalProfessionalActivity.class));

        } else {
            dialog.show();
        }

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();


                button.setText(professionalList.get(position));

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

    @Override
    protected void onResume() {
        super.onResume();
        if (addReminderTxt != null) {
            if (!DataManager.getInstance().getReminderTimeList().isEmpty()) {
                String csv = DataManager.getInstance().getReminderTimeList().toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");
                addReminderTxt.setText(csv);
                reminderIDS = new ArrayList<>();

            }
        }
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(AttachMedicine.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    private void attachImage() {
        setimagepath();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AttachMedicine.this);

        // set title
        alertDialogBuilder.setTitle("Choose option");

        // set dialog message
        alertDialogBuilder
                .setMessage("Please select image from ")
                .setCancelable(false)
                .setPositiveButton("Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, closeMdsDB
                                // current activity
                                dialog.cancel();
                                takePicture();

                            }

                        })
                .setNegativeButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just closeMdsDB
                                // the dialog box and do nothing
                                dialog.cancel();
                                openGallery();
                            }

                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setCancelable(true);
        // show it
        alertDialog.show();
    }

    private void setimagepath() {

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

            mFileTemp = new File(Environment.getExternalStorageDirectory()
                    + "/" + getResources().getString(R.string.imagepath) + "/",
                    fileName);
        } else {
            mFileTemp = new File(getFilesDir(), fileName);
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
            Intent intent = new Intent(AttachMedicine.this, CropImage.class);
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

    private void showAddOthersDialog(final Spinner typeSpinner, final String title) {


        final Dialog dialog = new Dialog(AttachMedicine.this, R.style.AppCompatTheme);
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
                            Toast.makeText(AttachMedicine.this, "Please fill required field", Toast.LENGTH_LONG).show();
                        } else {

                            if (title.equalsIgnoreCase("type")) {
                                AttachMedicine.this.typeArr.add(AttachMedicine.this.typeArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AttachMedicine.this, android.R.layout.simple_spinner_item, AttachMedicine.this.typeArr);
                                typeSpinner.setAdapter(adapter);
                                typeSpinner.setSelection(AttachMedicine.this.typeArr.size() - 2);
                            } else if (title.equalsIgnoreCase("frequency")) {
                                AttachMedicine.this.frequencyArr.add(AttachMedicine.this.frequencyArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AttachMedicine.this, android.R.layout.simple_spinner_item, AttachMedicine.this.frequencyArr);
                                typeSpinner.setAdapter(adapter);
                                typeSpinner.setSelection(AttachMedicine.this.frequencyArr.size() - 2);
                            } else if (title.equalsIgnoreCase("refill")) {
                                AttachMedicine.this.frequencyArr.add(AttachMedicine.this.frequencyArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AttachMedicine.this, android.R.layout.simple_spinner_item, AttachMedicine.this.frequencyArr);
                                typeSpinner.setAdapter(adapter);
                                typeSpinner.setSelection(AttachMedicine.this.frequencyArr.size() - 2);
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
                    Toast.makeText(AttachMedicine.this, "Please fill required field", Toast.LENGTH_LONG).show();
                } else {

                    if (title.equalsIgnoreCase("type")) {
                        AttachMedicine.this.typeArr.add(AttachMedicine.this.typeArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AttachMedicine.this, android.R.layout.simple_spinner_item, AttachMedicine.this.typeArr);
                        typeSpinner.setAdapter(adapter);
                        typeSpinner.setSelection(AttachMedicine.this.typeArr.size() - 2);
                    } else if (title.equalsIgnoreCase("frequency")) {
                        AttachMedicine.this.frequencyArr.add(AttachMedicine.this.frequencyArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AttachMedicine.this, android.R.layout.simple_spinner_item, AttachMedicine.this.frequencyArr);
                        typeSpinner.setAdapter(adapter);
                        typeSpinner.setSelection(AttachMedicine.this.frequencyArr.size() - 2);
                    } else if (title.equalsIgnoreCase("refill")) {
                        AttachMedicine.this.frequencyArr.add(AttachMedicine.this.frequencyArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AttachMedicine.this, android.R.layout.simple_spinner_item, AttachMedicine.this.frequencyArr);
                        typeSpinner.setAdapter(adapter);
                        typeSpinner.setSelection(AttachMedicine.this.frequencyArr.size() - 2);
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

    private void createScheduledNotification(String title, Calendar calendar, int id) {
        // Every scheduled intent needs a different ID, else it is just executed
        // once


        // Retrieve alarm manager from the system
//        AlarmManager alarmManager = (AlarmManager) getApplicationContext()
//                .getSystemService(MedicationActivity.ALARM_SERVICE);

        // Prepare the intent which should be launched at the date
        Intent intent = new Intent(this, RemiderDialog.class);
        intent.putExtra("title", title);

        // Prepare the pending intent
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                getApplicationContext(), id, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        String alarmType = "N";
//
//            // Register the alert in the system. You have the option to define
//            // if
//            // the device has to wake up on the alert or not
//            alarmManager.set(AlarmManager.RTC_WAKEUP,
//                    calendar.getTimeInMillis(), pendingIntent);

        reminderIDS.add("" + id);
        long repeatingTime = 1 * 24 * 60 * 60 * 1000;
        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), repeatingTime,
                pendingIntent);


    }

    public Uri getOutputMediaFileUri() {

        return Uri.fromFile(mFileTemp);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

                imagePath = mFileTemp.getPath();
                Drawable d = Drawable.createFromPath(imagePath);
                takePhoto_ll.setBackground(d);//                MyApplication.getApplication().loader.displayImage("file://"
//                        + imagePath, takePhoto_ll, MyApplication.options);
                System.out.println("image path:" + mFileTemp.getPath());

                // newFragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {

            e.printStackTrace();
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

//    dbAdapter = new DBAdapter(MedicationActivity.this);
//    dbAdapter.openMdsDB();
//
//    medicineList = dbAdapter.getMedicineInfoMap(typeArr.get(0));
//    dbAdapter.closeMdsDB();

    private void showPopUpMedicine(final String title, final TextView button) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        dbAdapter.openMdsDB();
        medicineList = dbAdapter.getMedicineInfoMap(typeArr.get(0));
//        ArrayList<MedicalProfessionalWrapper> wrapperList = dbAdapter.getMedicalProfessionalWrapper();

        final ArrayList<String> professionalList = new ArrayList<>();
        for (int i = 0; i < medicineList.size(); i++) {
            professionalList.add(medicineList.get(i).getDrugname());
        }


        dbAdapter.closeMdsDB();
        ListView modeList = new ListView(this);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, professionalList);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);
        final Dialog dialog = builder.create();

        dialog.show();

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();


                button.setText(professionalList.get(position));

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

    private String formateTime(String timeString) {
        String oriTime = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH-mm");
            Date dateObj = sdf.parse(timeString);
            System.out.println(dateObj);
            System.out.println(new SimpleDateFormat("K:mm").format(dateObj));

            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
            Date dt = null;
            try {
                dt = sdf.parse(timeString);
                System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            oriTime = sdfs.format(dt);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return oriTime;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                DatePickerDialog dialog = new DatePickerDialog(this, datePickerListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//                if (SELECTED_DATE == END_DATE) {
//                      Date date = new Date();
//                      date.setDate(day);
//                    date.setYear(year);
//                    date.setMonth(month+1);
//                    dialog.getDatePicker().setMinDate(date.getTime());
//                }
                return dialog;


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

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            Date startdate1 = null;
            try {


                startdate1 = sdf.parse(start_date);
            } catch (Exception ex) {
                //exception
                ex.printStackTrace();
            }

            if (SELECTED_DATE == START_DATE) {
                String dayString = day + "", monthString = (month + 1) + "";

                if ((month + 1) < 10) {
                    monthString = "0" + (month + 1);
                }
                if (day < 10) {
                    dayString = "0" + day;
                }

                String start_date = monthString + "-" + dayString + "-" + year;

                startDateBtn.setText(start_date);
            }

            if (SELECTED_DATE == END_DATE) {
                String dayString = day + "", monthString = (month + 1) + "";

                if ((month + 1) < 10) {
                    monthString = "0" + (month + 1);
                }
                if (day < 10) {
                    dayString = "0" + day;
                }

                String end_date = monthString + "-" + dayString + "-" + year;

                Date enddate2 = null;
                try {
                    enddate2 = sdf.parse(end_date);
                } catch (Exception ex) {
                    //exception
                    ex.printStackTrace();
                }
                if (enddate2.after(startdate1)) {
                    endDateBtn.setText(end_date);
                } else {
                    Toast.makeText(AttachMedicine.this, "Please select end date is after start date", Toast.LENGTH_SHORT).show();
                }

            }
            if (SELECTED_DATE == REFILL_DATE) {
                String dayString = day + "", monthString = (month + 1) + "";

                if ((month + 1) < 10) {
                    monthString = "0" + (month + 1);
                }
                if (day < 10) {
                    dayString = "0" + day;
                }

                String start_date = monthString + "-" + dayString + "-" + year;
                refillDateBtn.setText(start_date);

            }


            // set selected date into datepicker also

        }
    };

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
