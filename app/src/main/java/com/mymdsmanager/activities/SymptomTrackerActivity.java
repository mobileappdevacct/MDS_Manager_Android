package com.mymdsmanager.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.SymptomDetailWrapper;
import com.mymdsmanager.wrapper.SymptomWrapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class SymptomTrackerActivity extends AppCompatActivity implements OnFinishActivity {
    @Bind(R.id.symptomSpinner)
    Spinner symptomSpinner;
    @Bind(R.id.addSymptomBtn)
    ImageButton addSymptomBtn;
    @Bind(R.id.severitySeekBar)
    SeekBar severitySeekBar;
    @Bind(R.id.selectDateTxt)
    TextView selectDateTxt;
    @Bind(R.id.selectTimeTxt)
    TextView selectTimeTxt;
    @Bind(R.id.saveSymtomBtn)
    Button saveSymtomBtn;
    @Bind(R.id.viewHistoryText)
    TextView viewHistoryText;
    @Bind(R.id.enterDurationEdt)
    Button enterDurationEdt;
    @Bind(R.id.enterFrequencyEdt)
    Button enterFrequencyEdt;
    @Bind(R.id.subsymptomSpinner)
    Spinner subsymptomSpinner;
    @Bind(R.id.symptomSpinnertxt)
    TextView symptomSpinnertxt;
    @Bind(R.id.subsymptomSpinnertxt)
    TextView subsymptomSpinnertxt;
    private DatePicker datePicker;
    private Toolbar toolbar;
    private int year;
    private int month;
    private int day;
    private final String SELECT_DURATION = "SELECT DURATION";
    private final String SELECT_FREQUENCY = "SELECT FREQUENCY";
    private final int DATE_DIALOG_ID = 1;
    private final int TIME_DIALOG_ID = 2;
    private DBAdapter dbAdapter;
    @Bind(R.id.notesEdt)
    EditText notesEdt;
    private String durationString = "", frequencyString = "";
    private boolean isChartSelected = false;
    private int id = -1;
    ArrayList<String> symtomArrayList = new ArrayList<>();
    ArrayList<String> durationList = new ArrayList<>();
    ArrayList<String> frequencyList = new ArrayList<>();
    String subsymptom_str = "";
    ProgressDialog dialog;
    ArrayList<String> symptom_Arraylist = new ArrayList<>();
    ArrayList<String> practicalProblems_Arraylist = new ArrayList<>();
    SymptomDetailWrapper wrapper;
    HomeWatcher mHomeWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  MyApplication.getApplication().hideSoftKeyBoard(SymptomTrackerActivity.this)
        setContentView(R.layout.activity_symptom_tracker);
        ButterKnife.bind(this);
        if (wrapper == null) {
//            selectDateTxt.setText(formattedDate);
//            selectTimeTxt.setText(formattedtime);
            enterDurationEdt.setText("ENTER DURATION");
            enterFrequencyEdt.setText("ENTER FREQUENCY");
        }
        getUiComponents();
        mHomeWatcher = new HomeWatcher(SymptomTrackerActivity.this);
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
    boolean fristSeek = true;

    private void getUiComponents() {
        //Syspto items
        id = getIntent().getIntExtra("id", -1);
        symptom_Arraylist.add("Anxiety");
        symptom_Arraylist.add("Bleeding or Bruising");
        symptom_Arraylist.add("Changes in urination");
        symptom_Arraylist.add("Constipation");
        symptom_Arraylist.add("Depression");
        symptom_Arraylist.add("Diarrhea");
        symptom_Arraylist.add("Difficulty getting around");
        symptom_Arraylist.add("Difficulty sleeping");
        symptom_Arraylist.add("Fatigue");
        symptom_Arraylist.add("Fear");
        symptom_Arraylist.add("Fevers");
        symptom_Arraylist.add("Indigestion");
        symptom_Arraylist.add("Lack of Appetite");
        symptom_Arraylist.add("Loss of interest in usual activities");
        symptom_Arraylist.add("Memory  or concentration problems");
        symptom_Arraylist.add("Mouth sores");
        symptom_Arraylist.add("Nausea");
        symptom_Arraylist.add("Pain");
        symptom_Arraylist.add("Sadness");
        symptom_Arraylist.add("Sexual dysfunction");
        symptom_Arraylist.add("Shortness of breath");
        symptom_Arraylist.add("Skin changes (dry, itching, rash)");
        symptom_Arraylist.add("Vomiting");
        symptom_Arraylist.add("Worry");
        //pratical problem
        practicalProblems_Arraylist.add("Child Care");
        practicalProblems_Arraylist.add("Finances");
        practicalProblems_Arraylist.add("Housing");
        practicalProblems_Arraylist.add("Insurance");
        practicalProblems_Arraylist.add("Spiritual/religious concerns");
        practicalProblems_Arraylist.add("Transportation");
        practicalProblems_Arraylist.add("Work-related problems");
        if (DataManager.getInstance().getSymtomsubArrayList().size() == 0) {
            DataManager.getInstance().setSymtomsubArrayList(symptom_Arraylist);
        }
        if (DataManager.getInstance().getPraticalsubArraylist().size() == 0) {
            DataManager.getInstance().setPraticalsubArraylist(practicalProblems_Arraylist);
        }
        dbAdapter = new DBAdapter(SymptomTrackerActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Symptom Tracker");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setSymptomSpinner();
            }
        });

        durationList.add("Minutes");
        durationList.add("Hours");
        durationList.add("Days");
        durationList.add("Weeks");
        durationList.add("Months");
        durationList.add("Years");
        frequencyList.add("per minute");
        frequencyList.add("per Hour");
        frequencyList.add("per day");
        frequencyList.add("per week");
        frequencyList.add("per month");
        frequencyList.add("per year");
        severitySeekBar.incrementProgressBy(10);
        severitySeekBar.setMax(90);
        severitySeekBar.setProgress(40);

        severitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                seekBar.setProgress(progress);
                if (!fristSeek) {
                    dataChanged = true;
                }
                fristSeek = false;
                System.out.println("progress" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        if (id > 0) {
            viewHistoryText.setVisibility(View.GONE);
            setData();

        }
        new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                notesEdt.addTextChangedListener(textWatcher);
                enterDurationEdt.addTextChangedListener(textWatcher);
                enterFrequencyEdt.addTextChangedListener(textWatcher);
                selectDateTxt.addTextChangedListener(textWatcher);
                selectTimeTxt.addTextChangedListener(textWatcher);
            }
        }.start();

    }

    private void showPopUp(final String title, final ArrayList<String> arr) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

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


                if (title.equalsIgnoreCase(SELECT_DURATION)) {

                    if (TextUtils.isEmpty(durationString)) {
                        durationString = arr.get(position).replaceAll("feet", "");
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 1; i <= 60; i++) {
                            list.add(i + "");
                        }
                        showPopUp(SELECT_DURATION, list);
                    } else {
                        durationString = arr.get(position) + " " + durationString;
                        enterDurationEdt.setText(durationString);

                    }

                } else if (title.equalsIgnoreCase(SELECT_FREQUENCY)) {

                    if (TextUtils.isEmpty(frequencyString)) {
                        frequencyString = arr.get(position).replaceAll("feet", "");
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 1; i <= 49; i++) {
                            list.add(i + "");
                        }

                        showPopUp(SELECT_FREQUENCY, list);
                    } else {
                        frequencyString = arr.get(position) + " " + frequencyString;
                        enterFrequencyEdt.setText(frequencyString);

                    }
                }


            }
        });


    }

    private void setData() {
        try {
            if (id > 0) {
                dbAdapter.openMdsDB();
                wrapper = dbAdapter.getSymtomWrapper(id);
                viewHistoryText.setVisibility(View.GONE);
                saveSymtomBtn.setText("Save Symptom");
                notesEdt.setText(wrapper.getNotes());
                symptomSpinner.setSelection(symtomArrayList.indexOf(wrapper.getSymptomname()));
//                subsymptomSpinner.setSelection();
                enterFrequencyEdt.setText(wrapper.getFrequency());
                enterDurationEdt.setText(wrapper.getDuration());
                selectTimeTxt.setText(wrapper.getSymptomtime());
                selectDateTxt.setText(wrapper.getSymptomdate());
                if (!TextUtils.isEmpty(wrapper.getSeverity())) {
                    severitySeekBar.setProgress((Integer.parseInt(wrapper.getSeverity()) * 10 - 10));
                }
//            symptomSpinnertxt.setVisibility(View.VISIBLE);
//            subsymptomSpinnertxt.setVisibility(View.VISIBLE);
//            symptomSpinnertxt.setText(wrapper.getSymptomname());
//                subsymptomSpinnertxt.setText(wrapper.getSubsymptom_str());
                dbAdapter.closeMdsDB();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.addSymptomBtn)
    public void addSymptomBtn() {
        showAddSymptomDialog();
    }

    @OnClick(R.id.enterDurationEdt)
    public void enterDurationEdt() {
        durationString = "";

        showPopUp(SELECT_DURATION, durationList);

    }

    @OnClick(R.id.enterFrequencyEdt)
    public void enterFrequencyEdt() {
        frequencyString = "";


        showPopUp(SELECT_FREQUENCY, frequencyList);
    }

    boolean showToast = true;

    @Override
    protected void onResume() {
        super.onResume();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(SymptomTrackerActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
        Calendar c = Calendar.getInstance();


        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat time_df = new SimpleDateFormat("KK:mm");
        String formattedDate = df.format(c.getTime());
        String formattedtime = time_df.format(c.getTime());




    }

    ArrayList<String> sub_praticalStrings;

    private void setSymptomSpinner() {


        symtomArrayList.add("Select Symptom Category *");
        symtomArrayList.add("Symptoms");
        symtomArrayList.add("Practical Problems");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, symtomArrayList);
        symptomSpinner.setAdapter(adapter);

        String select_symptom_pratical = "";

        symptomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == symtomArrayList.size() - 1) {
//                    showAddSymptomDialog();
//                }

                sub_praticalStrings = new ArrayList<String>();
                sub_praticalStrings.add("Select Symptom Sub Category *");
                if (position == 1) {
                    sub_praticalStrings.addAll(DataManager.getInstance().getSymtomsubArrayList());
                    sub_praticalStrings.add("Other");
//                    Collections.sort(sub_praticalStrings, new Comparator<String>() {
//                        @Override
//                        public int compare(String s1, String s2) {
//                            return s1.compareToIgnoreCase(s2);
//                        }
//                    });
                    ArrayAdapter<String> sub_symptom_adapter = new ArrayAdapter<String>(SymptomTrackerActivity.this, android.R.layout.simple_spinner_item, sub_praticalStrings);
                    subsymptomSpinner.setAdapter(sub_symptom_adapter);
                    if (wrapper != null) {

                        subsymptomSpinner.setSelection(sub_praticalStrings.indexOf(wrapper.getSubsymptom_str()));
                    }
                } else if (position == 2) {

                    sub_praticalStrings.addAll(DataManager.getInstance().getPraticalsubArraylist());
                    sub_praticalStrings.add("Other");
                    ArrayAdapter<String> sub_pratical_adapter = new ArrayAdapter<String>(SymptomTrackerActivity.this, android.R.layout.simple_spinner_item, sub_praticalStrings);
                    subsymptomSpinner.setAdapter(sub_pratical_adapter);
                    if (wrapper != null) {

                        subsymptomSpinner.setSelection(sub_praticalStrings.indexOf(wrapper.getSubsymptom_str()));
                    }
                } else if (position == 0) {
                    ArrayAdapter<String> sub_symptom_adapter = new ArrayAdapter<String>(SymptomTrackerActivity.this, android.R.layout.simple_spinner_item, sub_praticalStrings);
                    subsymptomSpinner.setAdapter(sub_symptom_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subsymptomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
                if (position == sub_praticalStrings.size() - 1 && subsymptomSpinner.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                    showAddOthersDialog(subsymptomSpinner, "type");
                } else {
                    subsymptom_str = subsymptomSpinner.getSelectedItem().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subsymptomSpinner.setOnTouchListener(spinnerOnTouch);
//        hideKeyboard();

    }

    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //Your code
                if (symptomSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Symptom Category *")) {
                    Toast.makeText(SymptomTrackerActivity.this, "Please select symptom category before the selection of sub category", Toast.LENGTH_SHORT).show();

                }

            }
            return false;
        }
    };

    @OnClick(R.id.saveSymtomBtn)


    public void saveSymtomBtn() {
        if (TextUtils.isEmpty(selectDateTxt.getText().toString()) || symptomSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Symptom Category *") || subsymptom_str.equalsIgnoreCase("Select Symptom Sub Category *")) {
            Toast.makeText(SymptomTrackerActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
        } else {
            dbAdapter.openMdsDB();

            SymptomDetailWrapper wrapper = new SymptomDetailWrapper();
            wrapper.setSymptomname(symptomSpinner.getSelectedItem().toString());
            wrapper.setSymptomtime(selectTimeTxt.getText().toString());
            wrapper.setSymptomdate(selectDateTxt.getText().toString());
            int severity = severitySeekBar.getProgress();
            severity = severity + 10;
            severity = severity / 10;
            wrapper.setSeverity("" + severity);
            wrapper.setNotes(notesEdt.getText().toString());
            wrapper.setFrequency(enterFrequencyEdt.getText().toString());
            wrapper.setDuration(enterDurationEdt.getText().toString());
            wrapper.setSubsymptom_str(subsymptom_str);
            if (id == -1) {
                dbAdapter.saveSymptomDetail(wrapper);
                Toast.makeText(SymptomTrackerActivity.this, "Symptom Added", Toast.LENGTH_SHORT).show();
            } else {
                dbAdapter.updateSymtomData(wrapper, id);
                Toast.makeText(SymptomTrackerActivity.this, "Symptom Updated", Toast.LENGTH_SHORT).show();
            }

            dbAdapter.closeMdsDB();
            MyApplication.saveLocalData(true);
            new UpdateOnClass(MyApplication.getApplication(), this);
            startActivity(new Intent(SymptomTrackerActivity.this, SymptomListGraphActivity.class));
            notesEdt.setText("");
            enterDurationEdt.setText("");
            enterFrequencyEdt.setText("");
            selectTimeTxt.setText("");
            selectDateTxt.setText("");
            symptomSpinner.setSelection(0);


        }
    }


    private void showAddSymptomDialog() {
        final Dialog dialog = new Dialog(SymptomTrackerActivity.this, R.style.AppCompatTheme);
        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.pop_up_add_symptom);
// Check if no view has focus:

        final EditText symptomEdt = (EditText) dialog.findViewById(R.id.symptomEdt);


        symptomEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (TextUtils.isEmpty(symptomEdt.getText().toString())) {
                            Toast.makeText(SymptomTrackerActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
                        } else {
                            dbAdapter.openMdsDB();

                            SymptomWrapper wrapper = new SymptomWrapper();
                            String text = symptomEdt.getText().toString();
                            wrapper.setSymptom(text);

                            dbAdapter.saveSymptom(wrapper);

                            dbAdapter.closeMdsDB();
                            new UpdateOnClass(MyApplication.getApplication(), SymptomTrackerActivity.this);
                            dialog.dismiss();
                            Toast.makeText(SymptomTrackerActivity.this, "New Symptom Added", Toast.LENGTH_LONG).show();

                            setSymptomSpinner();
                            hideKeyboard();
                        }
                }
                return true;

            }
        });

        Button addSymptomBtn, closeBtn;
        addSymptomBtn = (Button) dialog.findViewById(R.id.addSymptomBtn);
        closeBtn = (Button) dialog.findViewById(R.id.closeBtn);

        addSymptomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(symptomEdt.getText().toString())) {
                    Toast.makeText(SymptomTrackerActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
                } else {
                    dbAdapter.openMdsDB();

                    SymptomWrapper wrapper = new SymptomWrapper();
                    String text = symptomEdt.getText().toString();
                    wrapper.setSymptom(text);
                    dbAdapter.saveSymptom(wrapper);

                    dbAdapter.closeMdsDB();
                    new UpdateOnClass(MyApplication.getApplication(), SymptomTrackerActivity.this);
                    dialog.dismiss();
                    Toast.makeText(SymptomTrackerActivity.this, "New Symptom Added", Toast.LENGTH_LONG).show();

                    setSymptomSpinner();
// Check if no view has focus:
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

    // Check if no view has focus:
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

    @OnClick(R.id.selectDateTxt)
    public void setSelectDateTxt() {
        showDialog(DATE_DIALOG_ID);

    }

    @OnClick(R.id.viewHistoryText)
    public void viewHistoryText() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(SymptomTrackerActivity.this, SymptomListGraphActivity.class));
    }

    @OnClick(R.id.selectTimeTxt)
    public void selectTimeTxt() {
        showDialog(TIME_DIALOG_ID);

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date

                return new DatePickerDialog(this, datePickerListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            case TIME_DIALOG_ID:

                return new TimePickerDialog(this,
                        timePickerListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
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
            selectDateTxt.setText(formateDate(dateString));
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener
            = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            selectTimeTxt.setText(new StringBuilder().append(pad(hourOfDay))
                    .append(":").append(pad(minute)));
        }
    };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private String formateDate(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
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

//        if (wrapper != null) {
//            if (!wrapper.getSymptomname().equalsIgnoreCase(symptomSpinner.getSelectedItem().toString()) || !wrapper.getSubsymptom_str().equalsIgnoreCase(subsymptomSpinner.getSelectedItem().toString())) {
//                dataChanged = true;
//            }
//        }

        if (dataChanged || (!symptomSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Symptom Category *")
                || !subsymptom_str.equalsIgnoreCase("Select Symptom Sub Category *"))
                ) {
            saveAlert();
        } else {

            finish();
        }


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

    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SymptomTrackerActivity.this);
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

    @Override
    public void onFinish() {


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

    private void showAddOthersDialog(final Spinner typeSpinner, final String title) {


        final Dialog dialog = new Dialog(SymptomTrackerActivity.this, R.style.AppCompatTheme);
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
                            Toast.makeText(SymptomTrackerActivity.this, "Please fill required field", Toast.LENGTH_LONG).show();
                        } else {

                            if (title.equalsIgnoreCase("type")) {
                                sub_praticalStrings.add(sub_praticalStrings.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SymptomTrackerActivity.this, android.R.layout.simple_spinner_item, sub_praticalStrings);
                                typeSpinner.setAdapter(adapter);
                                typeSpinner.setSelection(sub_praticalStrings.size() - 2);

                                dialog.dismiss();

                                hideKeyboard();
                            }
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
                    Toast.makeText(SymptomTrackerActivity.this, "Please fill required field", Toast.LENGTH_LONG).show();
                } else {

                    if (title.equalsIgnoreCase("type")) {
                        sub_praticalStrings.add(sub_praticalStrings.size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SymptomTrackerActivity.this, android.R.layout.simple_spinner_item, sub_praticalStrings);
                        typeSpinner.setAdapter(adapter);
                        typeSpinner.setSelection(sub_praticalStrings.size() - 2);

                        dialog.dismiss();

                        hideKeyboard();
                    }
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
}
