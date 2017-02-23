package com.mymdsmanager.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.TreatmentInfoWrapper;
import com.mymdsmanager.wrapper.TreatmentMedicineInfoWrapper;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddTreatmentsActivity extends AppCompatActivity implements OnFinishActivity {
    private final int DATE_DIALOG_ID = 1;
    @Bind(R.id.medicineLinearlayout)
    LinearLayout medicineLinearlayout;
    @Bind(R.id.medicineListView)
    ListView medicineListView;
    @Bind(R.id.savemedicineBtn)
    Button savemedicineBtn;
    @Bind(R.id.treatmentLinearlayout)
    LinearLayout treatmentLinearlayout;
    @Bind(R.id.treatmentListView)
    ListView treatmentListView;
    @Bind(R.id.addtrramentBtn)
    Button addtrramentBtn;
    @Bind(R.id.mdsTreatmentTxt)
    TextView mdsTreatmentTxt;
    @Bind(R.id.othersTxt)
    TextView othersTxt;
    @Bind(R.id.showBottomlayout)
    LinearLayout showBottomlayout;
    @Bind(R.id.clinicalTrialsTxt)
    TextView clinicalTrialsTxt;
    @Bind(R.id.title1)
    TextView title1;
    @Bind(R.id.title2)
    TextView title2;
    @Bind(R.id.title3)
    TextView title3;
    private int year;
    private int month;
    private int day;
    @Bind(R.id.startDateBtn)
    Button startDateBtn;
    @Bind(R.id.endDateBtn)
    Button endDateBtn;
    @Bind(R.id.treatmentEdt)
    EditText treatmentEdt;
    @Bind(R.id.notesEdt)
    EditText notesEdt;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    boolean medicinelistAdapterBoolean = true, treatmentlistAdapterBoolean = true;
    int id = -1;
    boolean addTreatmentBoolean = true;
    boolean isStartDateSelected = false;
    private Toolbar toolbar;
    ArrayList<TreatmentMedicineInfoWrapper> treatmentrowItems;
    ArrayList<TreatmentMedicineInfoWrapper> medicinerowItems;
    AttechTreatmentAdapter attechTreatmentAdapter;
    AttechMedicineAdapter attechMedicineAdapter;
    DBAdapter adapter;
    boolean get_databaseValues = true;
    private String imagePath = "";
    private File mFileTemp;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x53;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x52;
    public static final int REQUEST_CODE_GALLERY = 0x51;
    int treatmentMedicineInfoWrappersSize = 0;

    String checkendDate = "";
    HomeWatcher mHomeWatcher;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_treatments);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("id", -1);
        adapter = new DBAdapter(AddTreatmentsActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (id == -1) {
            getSupportActionBar().setTitle("Add Treatment");
            saveBtn.setText("SAVE");
        } else {
            getSupportActionBar().setTitle("Update Treatment");
            type =getIntent().getStringExtra("title");
            treatmentMedicineInfoWrappersSize = DataManager.getInstance().treatmentMedicineInfoWrappers.size();
            saveBtn.setText("SAVE");
        }
        setData();
        startDateBtn.addTextChangedListener(textWatcher);
        endDateBtn.addTextChangedListener(textWatcher);
        notesEdt.addTextChangedListener(textWatcher);
        treatmentEdt.addTextChangedListener(textWatcher);
        mHomeWatcher = new HomeWatcher(AddTreatmentsActivity.this);
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
    private void setData() {
        TreatmentInfoWrapper wrapper = DataManager.getInstance().getTreatmentInfoWrapper();
        startDateBtn.setText(wrapper.getStartdate());
        endDateBtn.setText(wrapper.getEnddate());
        notesEdt.append(wrapper.getNotes());
        treatmentEdt.append(wrapper.getTreatement());
    }
    @OnClick(R.id.saveBtn)
    public void saveBtn() {
        if (TextUtils.isEmpty(startDateBtn.getText().toString())) {
            Toast.makeText(AddTreatmentsActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
        } else {
            adapter.openMdsDB();
            TreatmentInfoWrapper wrapper = new TreatmentInfoWrapper();
            wrapper.setNotes(notesEdt.getText().toString());
            wrapper.setStartdate(startDateBtn.getText().toString());
            wrapper.setEnddate(endDateBtn.getText().toString());
            wrapper.setTreatement(treatmentEdt.getText().toString());
            if (DataManager.getInstance().treatmentMedicineInfoWrappers.size() > 0) {
                if (DataManager.getInstance().treatmentMedicineInfoWrappers.get(0).getDosage().equalsIgnoreCase("Clinical Trial")) {
                    wrapper.setTreatement("Clinical Trial");
                } else if (DataManager.getInstance().treatmentMedicineInfoWrappers.get(0).getDays().equalsIgnoreCase("Other Treatment")) {
                    wrapper.setTreatement("Other Treatment");
                } else if (DataManager.getInstance().treatmentMedicineInfoWrappers.get(0).getOthertreatmentname().equalsIgnoreCase("MDS Treatment")) {
                    wrapper.setTreatement("MDS Treatment");
                }
            } else {
                wrapper.setTreatement("-");
            }
            if (id == -1) {
                adapter.saveTreatMentInfo(wrapper);
                if (DataManager.getInstance().treatmentMedicineInfoWrappers.size() > 0) {
                    adapter.saveTreatMentMedicineInfo(DataManager.getInstance().treatmentMedicineInfoWrappers, adapter.getTreatMentID(notesEdt.getText().toString()));
                    DataManager.getInstance().treatmentMedicineInfoWrappers.clear();
                    DataManager.getInstance().updatetreatmentMedicineInfoWrappers.clear();
                }
                MyApplication.saveLocalData(true);
            } else {
                int updatetreatmentmedicineSize=DataManager.getInstance().updatetreatmentMedicineInfoWrappers.size();
                if (updatetreatmentmedicineSize > 0) {
                    if (DataManager.getInstance().updatetreatmentMedicineInfoWrappers.get(0).getDosage().equalsIgnoreCase("Clinical Trial")) {
                        wrapper.setTreatement("Clinical Trial");
                    } else if (DataManager.getInstance().updatetreatmentMedicineInfoWrappers.get(0).getDays().equalsIgnoreCase("Other Treatment")) {
                        wrapper.setTreatement("Other Treatment");
                    } else if (DataManager.getInstance().updatetreatmentMedicineInfoWrappers.get(0).getOthertreatmentname().equalsIgnoreCase("MDS Treatment")) {
                        wrapper.setTreatement("MDS Treatment");
//                        MDS Treatment
                    }
                }
                wrapper.setTreatement(type);
                adapter.updateTreatmentData(wrapper, id);
                MyApplication.saveLocalData(true);
                if (updatetreatmentmedicineSize > 0) {
                    adapter.saveTreatMentMedicineInfo(DataManager.getInstance().updatetreatmentMedicineInfoWrappers, id);
                    DataManager.getInstance().updatetreatmentMedicineInfoWrappers.clear();
                    DataManager.getInstance().treatmentMedicineInfoWrappers.clear();
                }
            }
            adapter.closeMdsDB();
            new UpdateOnClass(MyApplication.getApplication(), this);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
//        if (DataManager.getInstance().treatmentMedicineInfoWrappers.size() > 0) {
        if (dataChanged) {
            saveAlert();
        } else {
            DataManager.getInstance().treatmentMedicineInfoWrappers.clear();
            finish();
        }
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
    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTreatmentsActivity.this);
        builder.setTitle("" + getResources().getString(R.string.app_name3));
        builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                DataManager.getInstance().treatmentMedicineInfoWrappers.clear();
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
    @OnClick(R.id.startDateBtn)
    public void startDateBtn() {
        isStartDateSelected = true;
        showDatePickerDialog();
    }
    @OnClick(R.id.endDateBtn)
    public void endDateBtn() {
        isStartDateSelected = false;
        showDatePickerDialog();
    }
    @OnClick(R.id.addtrramentBtn)
    public void addtrramentBtn() {
        if (addTreatmentBoolean) {
            showBottomlayout.setVisibility(View.VISIBLE);
            addTreatmentBoolean = false;
        } else {
            showBottomlayout.setVisibility(View.GONE);
            addTreatmentBoolean = true;
        }
    }

    @OnClick(R.id.mdsTreatmentTxt)
    public void mdsTreatmentTxt() {
        startActivity(new Intent(AddTreatmentsActivity.this, AttachTreatment.class));
        showBottomlayout.setVisibility(View.GONE);
        addTreatmentBoolean = true;
    }
    @OnClick(R.id.clinicalTrialsTxt)
    public void clinicalTrialsTxt() {
        startActivity(new Intent(AddTreatmentsActivity.this, AddClinicalTrail.class));
        showBottomlayout.setVisibility(View.GONE);
        addTreatmentBoolean = true;
    }
    @OnClick(R.id.othersTxt)
    public void othersTxt() {
        startActivity(new Intent(AddTreatmentsActivity.this, AttachOtherTreatment.class));
        showBottomlayout.setVisibility(View.GONE);
        addTreatmentBoolean = true;
    }
    @OnClick(R.id.savemedicineBtn)
    public void savemedicineBtn() {
        startActivity(new Intent(AddTreatmentsActivity.this, AttachMedicine.class));
        showBottomlayout.setVisibility(View.GONE);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dialog = new DatePickerDialog(AddTreatmentsActivity.this,
                        datePickerListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                Date date = new Date();
                date.setMonth(month);
                date.setYear(year);
                date.setDate(day);
                return dialog;
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
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
            if (isStartDateSelected)
                startDateBtn.setText(dateString);
            else
                endDateBtn.setText(dateString);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(c.getTime());
//        startDateBtn.setText(formattedDate);
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(AddTreatmentsActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
        if (id == -1) {
            if (DataManager.getInstance().treatmentMedicineInfoWrappers.size() != 0) {
                medicinerowItems = new ArrayList<>();
                treatmentrowItems = new ArrayList<>();
                for (int k = 0; k < DataManager.getInstance().treatmentMedicineInfoWrappers.size(); k++) {
                    if (DataManager.getInstance().treatmentMedicineInfoWrappers.get(k).getType().equalsIgnoreCase("T")) {
                        TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper=DataManager.getInstance().treatmentMedicineInfoWrappers.get(k);
                        treatmentMedicineInfoWrapper.setRemovePos(k);
                        treatmentrowItems.add(treatmentMedicineInfoWrapper);
                    } else {
                        TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper=DataManager.getInstance().treatmentMedicineInfoWrappers.get(k);
                        treatmentMedicineInfoWrapper.setRemovePos(k);
                        medicinerowItems.add(treatmentMedicineInfoWrapper);
                    }
                }

                attechTreatmentAdapter = new AttechTreatmentAdapter(AddTreatmentsActivity.this, treatmentrowItems);
                treatmentListView.setAdapter(attechTreatmentAdapter);
                setListViewHeightBasedOnChildren(treatmentListView);
                if (treatmentrowItems.size() != 0) {
                    treatmentLinearlayout.setVisibility(View.VISIBLE);
                    treatmentListView.setVisibility(View.VISIBLE);
                }
                if (treatmentrowItems.size()>0)
                {
                    if (treatmentrowItems.get(0).getDosage().equalsIgnoreCase("Clinical Trial"))
                    {
                        title1.setText("Number");
                        title2.setText("Name");
                        title3.setText("Location");
                    }else if (treatmentrowItems.get(0).getDays().equalsIgnoreCase("Other Treatment"))
                    {
                        title1.setText("Name");
                        title2.setText("Notes");
                    }
                }
            }
        } else {
            medicinerowItems = new ArrayList<>();
            treatmentrowItems = new ArrayList<>();
            if (get_databaseValues) {

                adapter.openMdsDB();
                if (adapter.getTreatMentMedicineInfo(id).size() != 0) {

                    for (int k = 0; k < adapter.getTreatMentMedicineInfo(id).size(); k++) {
                        TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper = adapter.getTreatMentMedicineInfo(id).get(k);
                        if (treatmentMedicineInfoWrapper.getType().equalsIgnoreCase("T")) {
                            treatmentrowItems.add(treatmentMedicineInfoWrapper);
                        } else {
                            medicinerowItems.add(treatmentMedicineInfoWrapper);
                        }
//                        DataManager.getInstance().treatmentMedicineInfoWrappers.add(treatmentMedicineInfoWrapper);
                    }

                    Collections.reverse(treatmentrowItems);
                    attechTreatmentAdapter = new AttechTreatmentAdapter(AddTreatmentsActivity.this, treatmentrowItems);
                    treatmentListView.setAdapter(attechTreatmentAdapter);
                    setListViewHeightBasedOnChildren(treatmentListView);
                    if (treatmentrowItems.size() != 0) {
                        treatmentLinearlayout.setVisibility(View.VISIBLE);
                        treatmentListView.setVisibility(View.VISIBLE);
                        treatmentlistAdapterBoolean = false;
                    }
                    if (treatmentrowItems.size()>0)
                    {
                        if (treatmentrowItems.get(0).getDosage().equalsIgnoreCase("Clinical Trial"))
                        {
                            title1.setText("Number");
                            title2.setText("Name");
                            title3.setText("Location");
                        }else if (treatmentrowItems.get(0).getDays().equalsIgnoreCase("Other Treatment"))
                        {
                            title1.setText("Name");
                            title2.setText("Notes");
                        }
                    }

                    adapter.closeMdsDB();



                }
                get_databaseValues = false;
            } else {

                if (attechTreatmentAdapter != null && attechMedicineAdapter != null) {
                    if (DataManager.getInstance().treatmentMedicineInfoWrappers.size() != 0) {

                        for (int k = 0; k < DataManager.getInstance().treatmentMedicineInfoWrappers.size(); k++) {
                            if (DataManager.getInstance().treatmentMedicineInfoWrappers.get(k).getType().equalsIgnoreCase("T")) {
                                treatmentrowItems.add(DataManager.getInstance().treatmentMedicineInfoWrappers.get(k));
                                attechTreatmentAdapter.notifyDataSetChanged();
                            } else {
                                medicinerowItems.add(DataManager.getInstance().treatmentMedicineInfoWrappers.get(k));
                                attechMedicineAdapter.notifyDataSetChanged();
                            }
                        }
                        setListViewHeightBasedOnChildren(medicineListView);
                        setListViewHeightBasedOnChildren(treatmentListView);
                    }
                } else {
                    if (DataManager.getInstance().treatmentMedicineInfoWrappers.size() != 0) {

                        for (int k = 0; k < DataManager.getInstance().treatmentMedicineInfoWrappers.size(); k++) {
                            if (DataManager.getInstance().treatmentMedicineInfoWrappers.get(k).getType().equalsIgnoreCase("T")) {
                                treatmentrowItems.add(DataManager.getInstance().treatmentMedicineInfoWrappers.get(k));

                            } else {
                                medicinerowItems.add(DataManager.getInstance().treatmentMedicineInfoWrappers.get(k));

                            }
                        }
                    }

                    if (treatmentlistAdapterBoolean) {
                        attechTreatmentAdapter = new AttechTreatmentAdapter(AddTreatmentsActivity.this, treatmentrowItems);
                        treatmentListView.setAdapter(attechTreatmentAdapter);
                        setListViewHeightBasedOnChildren(treatmentListView);
                    }

                    if (treatmentrowItems.size() != 0) {
                        treatmentLinearlayout.setVisibility(View.VISIBLE);
                        treatmentListView.setVisibility(View.VISIBLE);
                        treatmentlistAdapterBoolean = false;
                    }
                    if (treatmentrowItems.size()>0)
                    {
                        if (treatmentrowItems.get(0).getDosage().equalsIgnoreCase("Clinical Trial"))
                        {
                            title1.setText("Number");
                            title2.setText("Name");
                            title3.setText("Location");
                        }else if (treatmentrowItems.get(0).getDays().equalsIgnoreCase("Other Treatment"))
                        {
                            title1.setText("Name");
                            title2.setText("Notes");
                        }
                    }
                }

            }
        }
    }

    @Override
    public void onFinish() {

//        finish();
    }


    private class AttechTreatmentAdapter extends BaseAdapter {
        Context context;


        public AttechTreatmentAdapter(Context context,
                                      ArrayList<TreatmentMedicineInfoWrapper> arrayList) {
            this.context = context;
            AddTreatmentsActivity.this.treatmentrowItems = arrayList;
        }

        /* private view holder class */
        private class ViewHolder {
            TextView drugnameTxt, dosagenameTxt, daysTxt;
            ImageView delete_btn;

        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.attechtreatment_list_item,
                        null);
                holder = new ViewHolder();
                holder.drugnameTxt = (TextView) convertView
                        .findViewById(R.id.drugnameTxt);
                holder.dosagenameTxt = (TextView) convertView
                        .findViewById(R.id.dosagenameTxt);
                holder.daysTxt = (TextView) convertView
                        .findViewById(R.id.daysTxt);
                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (TextUtils.isEmpty(treatmentrowItems.get(position).getNotes())) {
                holder.drugnameTxt.setText(treatmentrowItems.get(position).getMedicinename());
            } else {
                holder.drugnameTxt.setText(treatmentrowItems.get(position).getNotes());
            }
            holder.dosagenameTxt.setText(treatmentrowItems.get(position).getDosage());
            holder.daysTxt.setText(treatmentrowItems.get(position).getDays());
            if (treatmentrowItems.get(position).getDays().equalsIgnoreCase("Other Treatment")) {
                holder.drugnameTxt.setText(treatmentrowItems.get(position).getDosage());
                holder.dosagenameTxt.setText(treatmentrowItems.get(position).getMedicinename());

                holder.daysTxt.setText("-");
            } else {
            }

            if (treatmentrowItems.get(position).getDosage().equalsIgnoreCase("Clinical Trial")) {
                holder.daysTxt.setText(treatmentrowItems.get(position).getMedicinename());
                holder.drugnameTxt.setText(treatmentrowItems.get(position).getDays());
                holder.dosagenameTxt.setText(treatmentrowItems.get(position).getCyclenumber());
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (treatmentrowItems.size() > 0) {

                        if (treatmentrowItems.get(position).getDosage().equalsIgnoreCase("Clinical Trial")) {
                            get_databaseValues = true;
                            startActivity(new Intent(AddTreatmentsActivity.this, AddClinicalTrail.class).putExtra("model", treatmentrowItems.get(position)));
                            showBottomlayout.setVisibility(View.GONE);

                        } else if (treatmentrowItems.get(position).getDays().equalsIgnoreCase("Other Treatment")) {
                            get_databaseValues = true;
                            startActivity(new Intent(AddTreatmentsActivity.this, AttachOtherTreatment.class).putExtra("model", treatmentrowItems.get(position)));
                            showBottomlayout.setVisibility(View.GONE);
                        } else {
                            get_databaseValues = true;
                            startActivity(new Intent(AddTreatmentsActivity.this, AttachTreatment.class).putExtra("model", treatmentrowItems.get(position)));
                            showBottomlayout.setVisibility(View.GONE);
                        }
                    }
                }
            });
            holder.delete_btn.setTag(position);
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = (int) v.getTag();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            adapter.openMdsDB();
                            if (!TextUtils.isEmpty(treatmentrowItems.get(pos).getId())) {
                                adapter.deleteTreatMentMedicineInfo(treatmentrowItems.get(pos).getId());
                            }else
                            {
                                TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper=treatmentrowItems.get(pos);
                                DataManager.getInstance().treatmentMedicineInfoWrappers.remove(treatmentMedicineInfoWrapper.getRemovePos());
                            }
                            adapter.closeMdsDB();
                            treatmentrowItems.remove(pos);
                            new UpdateOnClass(MyApplication.getApplication(), AddTreatmentsActivity.this);
                            notifyDataSetChanged();

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
            return convertView;
        }

        @Override
        public int getCount() {
            return treatmentrowItems.size();
        }

        @Override
        public Object getItem(int position) {
            return treatmentrowItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return treatmentrowItems.indexOf(position);

        }
    }

    private class AttechMedicineAdapter extends BaseAdapter {
        Context context;


        public AttechMedicineAdapter(Context context,
                                     ArrayList<TreatmentMedicineInfoWrapper> arrayList) {
            this.context = context;
            AddTreatmentsActivity.this.medicinerowItems = arrayList;
        }

        /* private view holder class */
        private class ViewHolder {
            TextView drugnameTxt, dosagenameTxt, daysTxt;
            ImageView delete_btn;

        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.attechtreatment_list_item,
                        null);
                holder = new ViewHolder();
                holder.drugnameTxt = (TextView) convertView
                        .findViewById(R.id.drugnameTxt);
                holder.dosagenameTxt = (TextView) convertView
                        .findViewById(R.id.dosagenameTxt);
                holder.daysTxt = (TextView) convertView
                        .findViewById(R.id.daysTxt);
                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.drugnameTxt.setText(medicinerowItems.get(position).getMedicinename());
            holder.dosagenameTxt.setText(medicinerowItems.get(position).getDosage());
            holder.daysTxt.setText(medicinerowItems.get(position).getDays());
            holder.delete_btn.setTag(position);
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int pos = (int) v.getTag();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            adapter.openMdsDB();
                            if (!TextUtils.isEmpty(medicinerowItems.get(pos).getId())) {
                                adapter.deleteTreatMentMedicineInfo(medicinerowItems.get(pos).getId());
                            }
                            adapter.closeMdsDB();
                            medicinerowItems.remove(pos);
                            new UpdateOnClass(MyApplication.getApplication(), AddTreatmentsActivity.this);
                            notifyDataSetChanged();

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


            return convertView;
        }

        @Override
        public int getCount() {
            return medicinerowItems.size();
        }

        @Override
        public Object getItem(int position) {
            return medicinerowItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return medicinerowItems.indexOf(position);

        }
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

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    this, year, month, day);
            Date date = new Date();
            date.setMonth(month);
            date.setYear(year);
            date.setDate(day);
//            if (!isStartDateSelected) {
            dialog.getDatePicker().setMinDate(new Date().getTime() - 10000);

//            }else
//            {
//                dialog.getDatePicker().setMaxDate(new Date().getTime()- 10000);
//            }
            // Create a new instance of DatePickerDialog and return it
            return dialog;
        }

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            String dayString = day + "", monthString = (month + 1) + "";

            if ((month + 1) < 10) {
                monthString = "0" + (month + 1);
            }
            if (day < 10) {
                dayString = "0" + day;
            }


            if (isStartDateSelected) {
                String dateString = monthString + "-" + dayString + "-" + year;
                checkendDate = dateString;
                startDateBtn.setText(formateDate(dateString));

            } else {
                String dayString1 = day + "", monthString1 = (month + 1) + "";
                Date startdate1 = null;
                try {


                    startdate1 = sdf.parse(checkendDate);
                } catch (Exception ex) {
                    //exception
                    ex.printStackTrace();
                }
                if ((month + 1) < 10) {
                    monthString1 = "0" + (month + 1);
                }
                if (day < 10) {
                    dayString1 = "0" + day;
                }

                String end_date = monthString1 + "-" + dayString1 + "-" + year;
//                String end_date = (new StringBuilder().append(month + 1)
//                        .append("-").append(day).append("-").append(year)
//                        .append(" ")).toString();
                Date enddate2 = null;
                try {
                    enddate2 = sdf.parse(end_date);
                } catch (Exception ex) {
                    //exception
                    ex.printStackTrace();
                }

                    endDateBtn.setText(formateDate(end_date));


            }
        }
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

    private String localformateDate(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date = targetFormat.parse(dateString);
            String formattedDate = originalFormat.format(date);
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
