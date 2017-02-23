package com.mymdsmanager.activities;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Locale;
import butterknife.OnClick;
import eu.janmuller.android.simplecropimage.CropImage;
public class MedicationActivity extends AppCompatActivity implements View.OnClickListener,OnFinishActivity {
    private final int DATE_DIALOG_ID = 1;
    private TextView addReminderTxt;
    private int year;
    private int month;
    private int day;

    private Button supplementsBtn, overTheCounterBtn, prescriptionBtn, addMedicationBtn;
    private Toolbar toolbar;
    private File mFileTemp;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x53;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x52;
    public static final int REQUEST_CODE_GALLERY = 0x51;
    private LinearLayout takePhoto_ll;
    private String imagePath = "";
    private ListView medicineListView;
    private ArrayList<MedicineInfoWrapper> medicineList = new ArrayList<>();
    public static boolean show_dialog_flag = true;
    String start_date1 = "";
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
    HomeWatcher mHomeWatcher;
    ArrayList<String> reminderIDS;
    private int image_store_count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new DBAdapter(MedicationActivity.this);
        getUiComponents();
        setOnClickListener();

//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.SECOND, 5);
//
//        //Create a new PendingIntent and add it to the AlarmManager
//        Intent intent = new Intent(this, SplashActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager am =
//                (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//                pendingIntent);

         mHomeWatcher = new HomeWatcher(MedicationActivity.this);
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


    private void getUiComponents() {
        setContentView(R.layout.activity_medication);
        typeArr.add("Over-the-Counter");
        typeArr.add("Prescription");
        typeArr.add("Supplements/Other");
//        frequencyArr.add("Every Day");
//        frequencyArr.add("Weekly");
//        frequencyArr.add("Monthly");
        frequencyArr.add("");
        frequencyArr.addAll(DataManager.getInstance().getFrequencyArraylist());
        Collections.sort(frequencyArr, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        frequencyArr.add("Other");
        refillfrequencyArr.add("");
        refillfrequencyArr.addAll(DataManager.getInstance().getRefill_frequencyArraylist());

        Collections.sort(refillfrequencyArr, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        refillfrequencyArr.add("Other");
        prescriptionBtn = (Button) findViewById(R.id.prescriptionBtn);
        medicineListView = (ListView) findViewById(R.id.medicineListView);
        supplementsBtn = (Button) findViewById(R.id.supplementsBtn);
        overTheCounterBtn = (Button) findViewById(R.id.overTheCounterBtn);
        prescriptionBtn.bringToFront();
        addMedicationBtn = (Button) findViewById(R.id.addMedicationBtn);
        dbAdapter = new DBAdapter(MedicationActivity.this);
        dbAdapter.openMdsDB();
        medicineList = dbAdapter.getMedicineInfoMap(typeArr.get(1));
        dbAdapter.closeMdsDB();
        Collections.reverse(medicineList);
        MedicineAdapter adapter = new MedicineAdapter(MedicationActivity.this, medicineList);
        medicineListView.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });
        for (int i = 0; i <medicineList.size() ; i++) {
            if (!TextUtils.isEmpty(medicineList.get(i).getImagename())) {
                image_store_count++;
            }
        }
        getSupportActionBar().setTitle("Medication");



    }

    @Override
    public void onBackPressed() {

        finish();
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
        if(prescribedBtn!=null)
        {
            prescribedBtn.setText(DataManager.getInstance().getProvider_name());
        }
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(MedicationActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
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

    private void setOnClickListener() {
        supplementsBtn.setOnClickListener(this);
        overTheCounterBtn.setOnClickListener(this);
        prescriptionBtn.setOnClickListener(this);
        addMedicationBtn.setOnClickListener(this);
    }
int selectType=1;
    private void showMedicationDialog() {
        dialog = new Dialog(MedicationActivity.this, R.style.AppCompatTheme);
        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.pop_up_add_medicine);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        LinearLayout takePhotoLl = (LinearLayout) dialog.findViewById(R.id.takePhoto_ll);
        LinearLayout addProviderLl = (LinearLayout) dialog.findViewById(R.id.addProviderLl);
        final EditText drugNameEdt = (EditText) dialog.findViewById(R.id.drugNameEdt);
        final EditText genericEdt = (EditText) dialog.findViewById(R.id.genericEdt);
        final EditText dosageEdt = (EditText) dialog.findViewById(R.id.dosageEdt);
        dosageEdt.setCursorVisible(false);
        final Spinner frequencySpinner = (Spinner) dialog.findViewById(R.id.frequencySpinner);
        dosageEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dosageEdt.setCursorVisible(true);
            }
        });
        final Spinner typeSpinner = (Spinner) dialog.findViewById(R.id.typeSpinner);

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
                mHomeWatcher.stopWatch();
                Intent reminderIntent = new Intent(MedicationActivity.this, ReminderAcivity.class);
                if (show_dialog_flag==false) {
                    reminderIntent.putExtra("wrapper", rowItem);
                }
                startActivity(reminderIntent);

            }
        });
        addProviderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeWatcher.stopWatch();
                startActivity(new Intent(MedicationActivity.this, AddMedicalProfessionalActivity.class));
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
                if (dataChanged || !typeSpinner.getSelectedItem().toString().equalsIgnoreCase(""))
                         {
                    saveAlert();
                } else {
                    dialog.dismiss();
                    rowItem = null;
                }
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
                if (TextUtils.isEmpty(drugNameEdt.getText().toString()))
                {
                    drugNameEdt.setError("Please Enter Drug Name");
                    drugNameEdt.requestFocus();
                }
//                else if (TextUtils.isEmpty(genericEdt.getText().toString()))
//                {
//                    genericEdt.setError("Please Enter Generic Name");
//                    genericEdt.requestFocus();
//                }

//                else if (TextUtils.isEmpty(dosageEdt.getText().toString()))
//                {
//                    dosageEdt.setError("Please Enter Dosage");
//                    dosageEdt.requestFocus();
//                }
                else if (typeSpinner.getSelectedItem().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(MedicationActivity.this,"Please select type",Toast.LENGTH_SHORT).show();
                }
                else {
                    dbAdapter.openMdsDB();
                    wrapper.setRefilfrequency(refillfrequencySpinner.getSelectedItem().toString());
                    wrapper.setPrescirbedby(prescribedBtn.getText().toString());
                    wrapper.setNotes(notesEdt.getText().toString());
                    wrapper.setRefildate(refillDateBtn.getText().toString());
                    wrapper.setType(typeSpinner.getSelectedItem().toString());
                    wrapper.setReminderstring(DataManager.getInstance().getReminderTimeList().toString().replace("[", "").replace("]", "").replace(", ", ","));
                    wrapper.setGenericname(genericEdt.getText().toString());
                    wrapper.setDosage(dosageEdt.getText().toString());
                    wrapper.setDrugname(drugNameEdt.getText().toString());
                    wrapper.setStartdate(startDateBtn.getText().toString());
                    wrapper.setEnddate(endDateBtn.getText().toString());
                    wrapper.setImagename(imagePath);
                    if (!TextUtils.isEmpty(imagePath))
                    {
                        image_store_count++;
                    }
                    wrapper.setFrequency(frequencySpinner.getSelectedItem().toString());
                    if (reminderIDS!=null) {
                        if (reminderIDS.size() != 0) {
                            String reminder_id = TextUtils.join(",", reminderIDS);
                            wrapper.setREMINDERIDS(reminder_id);
                        }
                    }
//                if (TextUtils.isEmpty(wrapper.getRefildate()) || TextUtils.isEmpty(wrapper.getStartdate()) || TextUtils.isEmpty(wrapper.getEnddate()) || TextUtils.isEmpty(wrapper.getFrequency()) || TextUtils.isEmpty(wrapper.getDosage()) || TextUtils.isEmpty(wrapper.getDrugname()) || TextUtils.isEmpty(wrapper.getGenericname()) || TextUtils.isEmpty(wrapper.getRefildate()) || TextUtils.isEmpty(wrapper.getStartdate()) || TextUtils.isEmpty(wrapper.getEnddate()) || TextUtils.isEmpty(wrapper.getFrequency()) || TextUtils.isEmpty(wrapper.getDosage()) || TextUtils.isEmpty(wrapper.getRefilfrequency()) || TextUtils.isEmpty(wrapper.getPrescirbedby())) {
//
//                    Toast.makeText(MedicationActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
//
//                } else {
                    dbAdapter.saveMedicineInfo(wrapper);
                    dbAdapter.closeMdsDB();
                    MyApplication.saveLocalData(true);
//                    Intent service_intent = new Intent(MedicationActivity.this,DataUploadService.class);
//                    startService(service_intent);
                    MyApplication.saveLocalData(true);
                    new UpdateOnClass(MedicationActivity.this,MedicationActivity.this);
                    dialog.dismiss();
//                }
                    Constants.add_reminder =true;
                    if (typeSpinner.getSelectedItem().toString().equalsIgnoreCase(typeArr.get(1))) {
                        prescriptionBtn.performClick();
                    }
                    else if (typeSpinner.getSelectedItem().toString().equalsIgnoreCase(typeArr.get(0))) {
                        overTheCounterBtn.performClick();
                    }
                    else if (typeSpinner.getSelectedItem().toString().equalsIgnoreCase(typeArr.get(2))) {
                        supplementsBtn.performClick();
                    }
                    if (MyApplication.reminderBln) {
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
                                    createScheduledNotification(drugNameEdt.getText().toString(), calendar, id, "");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
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
        typeSpinner.setSelection(selectType);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == typeArr.size() - 1) {
//                    showAddOthersDialog(typeSpinner, "type");
//                }
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

                if (saveTxt.getText().toString().equalsIgnoreCase("Edit"))
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            MedicationActivity.this);


                    alertDialogBuilder
                            .setMessage("Do you want to edit medicine?")
                            .setCancelable(false)
                            .setPositiveButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, closeMdsDB
                                            // current activity
                                            dialog.cancel();


                                        }

                                    })
                            .setNegativeButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, just closeMdsDB
                                            // the dialog box and do nothing

                                            dialog.cancel();
                                            typeSpinner.setVisibility(View.VISIBLE);
                                            frequencySpinner.setVisibility(View.VISIBLE);
                                            refillfrequencySpinner.setVisibility(View.VISIBLE);
                                            type_txt.setVisibility(View.GONE);
                                            frequencyTxt.setVisibility(View.GONE);
                                            refillfrequencyTxt.setVisibility(View.GONE);
                                            addProviderBtn.setEnabled(true);
                                            refillDateBtn.setEnabled(true);
                                            prescribedBtn.setEnabled(true);
                                            drugNameEdt.setEnabled(true);
                                            genericEdt.setEnabled(true);
                                            dosageEdt.setEnabled(true);
                                            notesEdt.setEnabled(true);
                                            startDateBtn.setClickable(true);
                                            endDateBtn.setClickable(true);
                                            takePhoto_ll.setEnabled(true);
                                            addReminderTxt.setEnabled(true);
                                            saveTxt.setText("Update");

                                        }

                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.setCancelable(true);
                    // show it
                    alertDialog.show();
                }else {
                    attachImage();
                }
            }
        });
        if (rowItem != null&&!this.id.equalsIgnoreCase("-1") && show_dialog_flag == false) {
            saveTxt.setText("Edit");
            typeSpinner.setVisibility(View.GONE);
            frequencySpinner.setVisibility(View.GONE);
            refillfrequencySpinner.setVisibility(View.GONE);
            type_txt.setVisibility(View.VISIBLE);
            frequencyTxt.setVisibility(View.VISIBLE);
            refillfrequencyTxt.setVisibility(View.VISIBLE);
            drugNameEdt.setEnabled(false);
            genericEdt.setEnabled(false);
            dosageEdt.setEnabled(false);
            notesEdt.setEnabled(false);
            addProviderBtn.setEnabled(false);
            prescribedBtn.setEnabled(false);
            startDateBtn.setClickable(false);
            endDateBtn.setClickable(false);

            addReminderTxt.setEnabled(false);
            refillDateBtn.setEnabled(false);
            drugNameEdt.setText(rowItem.getDrugname());
            genericEdt.setText(rowItem.getGenericname());
            dosageEdt.setText(rowItem.getDosage());
            type_txt.setText(rowItem.getType());
            frequencyTxt.setText(rowItem.getFrequency());
            startDateBtn.setText(rowItem.getStartdate());
            endDateBtn.setText(rowItem.getEnddate());
            refillfrequencyTxt.setText(rowItem.getRefilfrequency());
            addReminderTxt.setText(rowItem.getReminderstring());
            refillDateBtn.setText(rowItem.getRefildate());
            prescribedBtn.setText(rowItem.getPrescirbedby());
            notesEdt.setText(rowItem.getNotes());
            typeSpinner.setSelection(typeArr.indexOf(rowItem.getType()));
            frequencySpinner.setSelection(frequencyArr.indexOf(rowItem.getFrequency()));
            refillfrequencySpinner.setSelection(refillfrequencyArr.indexOf(rowItem.getRefilfrequency()));
            File file = new File(rowItem.getImagename());
            if (file.exists()) {
                Drawable d = Drawable.createFromPath(rowItem.getImagename());
                takePhoto_ll.setBackground(d);
            }

            saveTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeSpinner.setVisibility(View.VISIBLE);
                    frequencySpinner.setVisibility(View.VISIBLE);
                    refillfrequencySpinner.setVisibility(View.VISIBLE);
                    type_txt.setVisibility(View.GONE);
                    frequencyTxt.setVisibility(View.GONE);
                    refillfrequencyTxt.setVisibility(View.GONE);
                    addProviderBtn.setEnabled(true);
                    refillDateBtn.setEnabled(true);
                    prescribedBtn.setEnabled(true);
                    drugNameEdt.setEnabled(true);
                    genericEdt.setEnabled(true);
                    dosageEdt.setEnabled(true);
                    notesEdt.setEnabled(true);
                    startDateBtn.setClickable(true);
                    endDateBtn.setClickable(true);
                    takePhoto_ll.setEnabled(true);
                    addReminderTxt.setEnabled(true);
                    if (saveTxt.getText().toString().equalsIgnoreCase("Update")) {
                        ReminderWrapper reminderWrapper;
                        MedicineInfoWrapper wrapper = new MedicineInfoWrapper();
                        if (TextUtils.isEmpty(drugNameEdt.getText().toString()))
                        {
                            drugNameEdt.setError("Please Enter Drug Name");
                            drugNameEdt.requestFocus();
                        }
//                        else if (TextUtils.isEmpty(dosageEdt.getText().toString()))
//                        {
//                            dosageEdt.setError("Please Enter Dosage");
//                            dosageEdt.requestFocus();
//                        }
                        else {
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
                            if (!imagePath.equalsIgnoreCase("")) {
                                wrapper.setImagename(imagePath);
                            } else {
                                wrapper.setImagename(rowItem.getImagename()
                                );
                            }
                            if (!TextUtils.isEmpty(imagePath)&&!TextUtils.isEmpty(rowItem.getImagename()))
                            {
                                    image_store_count++;
                            }
//                            if (MyApplication.reminderBln) {
                                if (Constants.add_reminder == false) {
                                    reminderWrapper = DataManager.getInstance().getReminderWrapper();
                                    wrapper.setReminderstarttime(reminderWrapper.getReminderstarttime());
                                    wrapper.setRemindersoundindex(reminderWrapper.getRemindersoundindex());
                                    wrapper.setScheduledays(reminderWrapper.getScheduledays());
                                    wrapper.setRemindersoundfile(reminderWrapper.getRemindersoundfile());
                                    wrapper.setRemindercounts(reminderWrapper.getRemindercounts());
                                } else {
                                    wrapper.setReminderstarttime(rowItem.getReminderstarttime());
                                    wrapper.setRemindersoundindex(rowItem.getRemindersoundindex());
                                    wrapper.setScheduledays(rowItem.getScheduledays());
                                    wrapper.setRemindersoundfile(rowItem.getRemindersoundfile());
                                    wrapper.setRemindercounts(rowItem.getRemindercounts());
                                }

                                wrapper.setFrequency(frequencySpinner.getSelectedItem().toString());
                                if (reminderIDS != null) {
                                    if (reminderIDS.size() != 0) {
                                        String reminder_id = TextUtils.join(",", reminderIDS);
                                        wrapper.setREMINDERIDS(reminder_id);
                                    }
                                }

                            dbAdapter.updateMedicineInfo(wrapper, Integer.parseInt(rowItem.getId()));
                            dbAdapter.closeMdsDB();
                            dialog.dismiss();
//                }
                            MyApplication.saveLocalData(true);
                            new UpdateOnClass(MedicationActivity.this,MedicationActivity.this);

                            if (typeSpinner.getSelectedItem().toString().equalsIgnoreCase(typeArr.get(1)))
                                prescriptionBtn.performClick();

                            else if (typeSpinner.getSelectedItem().toString().equalsIgnoreCase(typeArr.get(0)))
                                overTheCounterBtn.performClick();
                            else if (typeSpinner.getSelectedItem().toString().equalsIgnoreCase(typeArr.get(2)))
                                supplementsBtn.performClick();
                            if (!DataManager.getInstance().getReminderTimeList().isEmpty()) {
                                String csv = DataManager.getInstance().getReminderTimeList().toString().replace("[", "").replace("]", "")
                                        .replace(", ", ",");
                                if (!TextUtils.isEmpty(csv)) {
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
//                                        if (!TextUtils.isEmpty(rowItem.getREMINDERIDS())&&rowItem!=null) {
//
//                                            for (int i = 0; i <reminder_id.length ; i++) {
//                                                id =Integer.parseInt(reminder_id[i]);
//                                            }
//                                        }

                                            createScheduledNotification(drugNameEdt.getText().toString(), calendar, id, rowItem.getRemindersoundfile());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                        Constants.add_reminder =true;
                    }
                    saveTxt.setText("Update");
                }
            });

        }
        dialog.show();
        drugNameEdt.addTextChangedListener(textWatcher);
        genericEdt.addTextChangedListener(textWatcher);
        dosageEdt.addTextChangedListener(textWatcher);
        type_txt.addTextChangedListener(textWatcher);
        frequencyTxt.addTextChangedListener(textWatcher);
        startDateBtn.addTextChangedListener(textWatcher);
        endDateBtn.addTextChangedListener(textWatcher);
        refillfrequencyTxt.addTextChangedListener(textWatcher);
        addReminderTxt.addTextChangedListener(textWatcher);
        refillDateBtn.addTextChangedListener(textWatcher);
        prescribedBtn.addTextChangedListener(textWatcher);
        notesEdt.addTextChangedListener(textWatcher);



    }
    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MedicationActivity.this);
        builder.setTitle("" + getResources().getString(R.string.app_name3));
        builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (MedicationActivity.this.dialog!=null) {
                                    dataChanged=false;
                                    MedicationActivity.this.dialog.dismiss();
                                }


                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void showAddOthersDialog(final Spinner typeSpinner, final String title) {


        final Dialog dialog = new Dialog(MedicationActivity.this, R.style.AppCompatTheme);
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
                            Toast.makeText(MedicationActivity.this, "Please fill required field", Toast.LENGTH_LONG).show();
                        } else {

                            if (title.equalsIgnoreCase("type")) {
                                MedicationActivity.this.typeArr.add(MedicationActivity.this.typeArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MedicationActivity.this, android.R.layout.simple_spinner_item, MedicationActivity.this.typeArr);
                                typeSpinner.setAdapter(adapter);
                                typeSpinner.setSelection(MedicationActivity.this.typeArr.size() - 2);
                            } else if (title.equalsIgnoreCase("frequency")) {
                                MedicationActivity.this.frequencyArr.add(MedicationActivity.this.frequencyArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MedicationActivity.this, android.R.layout.simple_spinner_item, MedicationActivity.this.frequencyArr);
                                typeSpinner.setAdapter(adapter);
                                typeSpinner.setSelection(MedicationActivity.this.frequencyArr.size() - 2);
                            } else if (title.equalsIgnoreCase("refill")) {
                                MedicationActivity.this.frequencyArr.add(MedicationActivity.this.frequencyArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MedicationActivity.this, android.R.layout.simple_spinner_item, MedicationActivity.this.frequencyArr);
                                typeSpinner.setAdapter(adapter);
                                typeSpinner.setSelection(MedicationActivity.this.frequencyArr.size() - 2);
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
                    Toast.makeText(MedicationActivity.this, "Please fill required field", Toast.LENGTH_LONG).show();
                } else {

                    if (title.equalsIgnoreCase("type")) {
                        MedicationActivity.this.typeArr.add(MedicationActivity.this.typeArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MedicationActivity.this, android.R.layout.simple_spinner_item, MedicationActivity.this.typeArr);
                        typeSpinner.setAdapter(adapter);
                        typeSpinner.setSelection(MedicationActivity.this.typeArr.size() - 2);
                    } else if (title.equalsIgnoreCase("frequency")) {
                        MedicationActivity.this.frequencyArr.add(MedicationActivity.this.frequencyArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MedicationActivity.this, android.R.layout.simple_spinner_item, MedicationActivity.this.frequencyArr);
                        typeSpinner.setAdapter(adapter);
                        typeSpinner.setSelection(MedicationActivity.this.frequencyArr.size() - 2);
                    } else if (title.equalsIgnoreCase("refill")) {
                        MedicationActivity.this.frequencyArr.add(MedicationActivity.this.frequencyArr.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MedicationActivity.this, android.R.layout.simple_spinner_item, MedicationActivity.this.frequencyArr);
                        typeSpinner.setAdapter(adapter);
                        typeSpinner.setSelection(MedicationActivity.this.frequencyArr.size() - 2);
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

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.addProviderBtn)
    public void addProviderBtn() {

        startActivity(new Intent(MedicationActivity.this, AddMedicalProfessionalActivity.class));
    }

    @Override
    public void onFinish() {
    }
  String id="-1";
    public class MedicineAdapter extends BaseAdapter {
        Context context;
        List<MedicineInfoWrapper> rowItems;

        public MedicineAdapter(Context context, List<MedicineInfoWrapper> items) {
            this.context = context;
            this.rowItems = items;
        }

        /*private view holder class*/
        private class ViewHolder {

            TextView dosageTxt, genericNameTxt, drugNameTxt;
            ImageView delete_btn;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_medicine_item, null);
                holder = new ViewHolder();
                holder.dosageTxt = (TextView) convertView.findViewById(R.id.dosageTxt);
                holder.genericNameTxt = (TextView) convertView.findViewById(R.id.genericNameTxt);
                holder.drugNameTxt = (TextView) convertView.findViewById(R.id.drugNameTxt);
                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            rowItem = (MedicineInfoWrapper) getItem(position);

            holder.dosageTxt.setText(rowItems.get(position).getDosage());
            holder.genericNameTxt.setText(rowItems.get(position).getGenericname());
            holder.drugNameTxt.setText(rowItems.get(position).getDrugname());
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteMedicineInfoMap(String.valueOf(rowItems.get(position).getId()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),MedicationActivity.this);
                            notifyDataSetChanged();
                            dbAdapter.closeMdsDB();

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show_dialog_flag = false;
                    rowItem = rowItems.get(position);
                    id=rowItems.get(position).getId();
                    showMedicationDialog();
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
            return rowItems.indexOf(getItem(position));
        }
    }

    private void attachImage() {
        setimagepath();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MedicationActivity.this);

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

        String fileName = image_store_count+"_MedicineInfo.png";
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
            Intent intent = new Intent(MedicationActivity.this, CropImage.class);
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
        if (professionalList.isEmpty())
            startActivity(new Intent(MedicationActivity.this, AddMedicalProfessionalActivity.class));
        else
            dialog.show();

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();


                button.setText(professionalList.get(position));

            }
        });


    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
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


            if (SELECTED_DATE == START_DATE) {
                String dayString = day + "", monthString = (month + 1) + "";

                if ((month + 1) < 10) {
                    monthString = "0" + (month + 1);
                }
                if (day < 10) {
                    dayString = "0" + day;
                }

                String start_date = monthString + "-" + dayString + "-" + year;
                start_date1 =start_date;
//                start_date = (new StringBuilder().append(month + 1)
//                        .append("-").append(day).append("-").append(year)
//                        .append(" ")).toString();
                startDateBtn.setText(formateDate(start_date));
            }

            if (SELECTED_DATE == END_DATE) {
                String dayString = day + "", monthString = (month + 1) + "";
                Date startdate1 = null;
                try {


                    startdate1 = sdf.parse(start_date1);
                } catch (Exception ex) {
                    //exception
                    ex.printStackTrace();
                }
                if ((month + 1) < 10) {
                    monthString = "0" + (month + 1);
                }
                if (day < 10) {
                    dayString = "0" + day;
                }

                String end_date = monthString + "-" + dayString + "-" + year;
//                String end_date = (new StringBuilder().append(month + 1)
//                        .append("-").append(day).append("-").append(year)
//                        .append(" ")).toString();

                if (startdate1!=null) {

                    Date enddate2 = null;
                    try {
                        enddate2 = sdf.parse(end_date);

                        if (enddate2.after(startdate1)) {
                            endDateBtn.setText(formateDate(end_date));
                        } else {
                            Toast.makeText(MedicationActivity.this, "Please select end date is after start date", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        //exception
                        ex.printStackTrace();
                        Toast.makeText(MedicationActivity.this, "Please select end date is after start date", Toast.LENGTH_SHORT).show();
                    }
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

                String end_date = monthString + "-" + dayString + "-" + year;
                refillDateBtn.setText(formateDate(end_date));

            }


            // set selected date into datepicker also

        }
    };


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
                takePhoto_ll.setBackground(d);//
                //              MyApplication.getApplication().loader.displayImage("file://"
//                        + imagePath, takePhoto_ll, MyApplication.options);
                System.out.println("image path:" + mFileTemp.getPath());
                dataChanged =true;

                // newFragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        if (v == supplementsBtn) {
            selectType=2;
            supplementsBtn.setBackgroundResource(R.mipmap.btn_prescription_touch);
            overTheCounterBtn.setBackgroundResource(R.mipmap.btn_prescription_normal);
            prescriptionBtn.setBackgroundResource(R.mipmap.btn_prescription_normal);
            supplementsBtn.bringToFront();
            dbAdapter = new DBAdapter(MedicationActivity.this);
            dbAdapter.openMdsDB();
            medicineList = dbAdapter.getMedicineInfoMap(typeArr.get(2));
            dbAdapter.closeMdsDB();
            Collections.reverse(medicineList);
            MedicineAdapter adapter = new MedicineAdapter(MedicationActivity.this, medicineList);
            medicineListView.setAdapter(adapter);
            show_dialog_flag = true;
        } else if (v == overTheCounterBtn) {
            selectType=0;
            supplementsBtn.setBackgroundResource(R.mipmap.btn_prescription_normal);
            overTheCounterBtn.setBackgroundResource(R.mipmap.btn_prescription_touch);
            prescriptionBtn.setBackgroundResource(R.mipmap.btn_prescription_normal);
            overTheCounterBtn.bringToFront();
            dbAdapter = new DBAdapter(MedicationActivity.this);
            dbAdapter.openMdsDB();
            medicineList = dbAdapter.getMedicineInfoMap(typeArr.get(0));
            dbAdapter.closeMdsDB();
            Collections.reverse(medicineList);
            MedicineAdapter adapter = new MedicineAdapter(MedicationActivity.this, medicineList);
            medicineListView.setAdapter(adapter);
            show_dialog_flag = true;
        } else if (v == prescriptionBtn) {
            selectType=1;
            supplementsBtn.setBackgroundResource(R.mipmap.btn_prescription_normal);
            overTheCounterBtn.setBackgroundResource(R.mipmap.btn_prescription_normal);
            prescriptionBtn.setBackgroundResource(R.mipmap.btn_prescription_touch);
            prescriptionBtn.bringToFront();
            dbAdapter = new DBAdapter(MedicationActivity.this);
            dbAdapter.openMdsDB();
            medicineList = dbAdapter.getMedicineInfoMap(typeArr.get(1));
            dbAdapter.closeMdsDB();
            Collections.reverse(medicineList);
            MedicineAdapter adapter = new MedicineAdapter(MedicationActivity.this, medicineList);
            medicineListView.setAdapter(adapter);
            show_dialog_flag = true;
        } else if (v == addMedicationBtn) {
            id="-1";
            imagePath="";
            showMedicationDialog();
            show_dialog_flag = true;
        }
    }
    private void createScheduledNotification(String title, Calendar calendar,int id,String soundName) {
        reminderIDS.add(""+id);
        long repeatingTime = 1 * 24 * 60 * 60 * 1000;
        // Retrieve alarm manager from the system
        AlarmManager alarmManager = (AlarmManager) getApplicationContext()
                .getSystemService(MedicationActivity.ALARM_SERVICE);
        // Prepare the intent which should be launched at the date
        Intent intent = new Intent(this, ReminderReceiver.class).putExtra("title", title);
        if (soundName.equalsIgnoreCase("Attention"))
        {
            intent.putExtra("sound", "attention");
        }else if (soundName.equalsIgnoreCase("Frenzy"))
        {
            intent.putExtra("sound", "frenzy");
        }else if (soundName.equalsIgnoreCase("Gentlealarm"))
        {
            intent.putExtra("sound", "gentlealarm");
        }else if (soundName.equalsIgnoreCase("Jingle bell"))
        {
            intent.putExtra("sound", "jingle_bell");
        }else if (soundName.equalsIgnoreCase("Msg posted"))
        {
            intent.putExtra("sound", "msg_posted");
        }else if (soundName.equalsIgnoreCase("Soft play once"))
        {
            intent.putExtra("sound", "soft_alert_play_once");
        }else
        {
            intent.putExtra("sound", "attention");
        }
        // Prepare the pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String alarmType = "N";
//        if (ApplicationUtils.isRepeatAlarm()) {
        alarmType = "Y";
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                timeInMills(mHour, mMinute), repeatingTime, pendingIntent);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), repeatingTime,
                pendingIntent);



    }
    private String formateDate(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd,yyyy");
            Date date = originalFormat.parse(dateString);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
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
    private File loadImageFormSdCard(String fileName) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            mFileTemp = new File(Environment.getExternalStorageDirectory()
                    + "/" + getResources().getString(R.string.imagepath) + "/",
                    fileName);
        } else {
            mFileTemp = new File(getFilesDir(), fileName);
        }
        if (mFileTemp.exists()) {
            return mFileTemp;
        }
        return null;
    }
    boolean dataChanged = false;
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

}
