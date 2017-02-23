package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.DiagnosisWrapper;
import com.mymdsmanager.wrapper.MedicalProfessionalWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class MedicalHistoryActivity extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.showHideDiagnosisImgBtn)
    ImageButton showHideDiagnosisImgBtn;
    @Bind(R.id.diagnosis_ll)
    LinearLayout diagnosisLl;
    @Bind(R.id.diagnosisListView)
    ListView diagnosisListView;
    @Bind(R.id.addDiagnosisBtn)
    Button addDiagnosisBtn;
    @Bind(R.id.showHideSurgeryImgBtn)
    ImageButton showHideSurgeryImgBtn;
    @Bind(R.id.surgery_ll)
    LinearLayout surgeryLl;
    @Bind(R.id.surgeryListView)
    ListView surgeryListView;
    @Bind(R.id.addSurgeryBtn)
    Button addSurgeryBtn;
    private DBAdapter dbAdapter;
    private Toolbar toolbar;
    HomeWatcher mHomeWatcher;
    boolean surgeryListviewShow=false;
    boolean diagnosiListviewShow=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);
        ButterKnife.bind(this);
        dbAdapter = new DBAdapter(MedicalHistoryActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Medical History");
         mHomeWatcher = new HomeWatcher(MedicalHistoryActivity.this);
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
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onBackPressed() {
        mHomeWatcher.stopWatch();
       finish();
    }

    @OnClick(R.id.showHideSurgeryImgBtn)
    public void showHideSurgeryImgBtn() {


        if (surgeryLl.getVisibility() == View.VISIBLE) {
            showHideSurgeryImgBtn.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            surgeryLl.setVisibility(View.GONE);
        } else {
            showHideSurgeryImgBtn.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            surgeryLl.setVisibility(View.VISIBLE);
            setListViewHeightBasedOnChildren(surgeryListView);

        }


    }

    @OnClick(R.id.showHideDiagnosisImgBtn)
    public void showHideDiagnosisImgBtn() {


        if (diagnosisLl.getVisibility() == View.VISIBLE) {
            showHideDiagnosisImgBtn.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            diagnosisLl.setVisibility(View.GONE);
        } else {
            showHideDiagnosisImgBtn.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            diagnosisLl.setVisibility(View.VISIBLE);
            setListViewHeightBasedOnChildren(diagnosisListView);
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

    @Override
    protected void onResume() {
        super.onResume();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(MedicalHistoryActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dbAdapter.openMdsDB();

        ArrayList<DiagnosisWrapper> diagnosisWrappers = dbAdapter.getDiagnosisList("d");
        Collections.reverse(diagnosisWrappers);
        TreatMentAdapter diagnosisAdapter = new TreatMentAdapter(MedicalHistoryActivity.this, diagnosisWrappers);
        diagnosisListView.setAdapter(diagnosisAdapter);
//        setListViewHeightBasedOnChildren(diagnosisListView);

        ArrayList<DiagnosisWrapper> surgeryWrappers = dbAdapter.getDiagnosisList("s");
        Collections.reverse(surgeryWrappers);
        TreatMentAdapter surgeryAdapter = new TreatMentAdapter(MedicalHistoryActivity.this,surgeryWrappers);
        surgeryListView.setAdapter(surgeryAdapter);

        if (dbAdapter.getDiagnosisList("Diagnosis").size() > 0) {
            showHideDiagnosisImgBtn.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            diagnosisLl.setVisibility(View.VISIBLE);
        }
        if (dbAdapter.getDiagnosisList("Surgery").size() > 0) {
            showHideSurgeryImgBtn.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            surgeryLl.setVisibility(View.VISIBLE);
        }
        dbAdapter.closeMdsDB();


    }

    @OnClick(R.id.addDiagnosisBtn)
    public void addDiagnosisBtn() {

        startActivity(new Intent(MedicalHistoryActivity.this, AddDiagnosisActivity.class)
                .putExtra("title", "d"));

    }

    @OnClick(R.id.addSurgeryBtn)
    public void addSurgeryBtn() {

        startActivity(new Intent(MedicalHistoryActivity.this, AddDiagnosisActivity.class)
                .putExtra("title", "s"));

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

        dialog.show();

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();


                button.setText(professionalList.get(position));

            }
        });


    }

    @Override
    public void onFinish() {

    }

    private class TreatMentAdapter extends BaseAdapter {
        Context context;
        ArrayList<DiagnosisWrapper> rowItems;

        public TreatMentAdapter(Context context,
                                ArrayList<DiagnosisWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
        }

        /* private view holder class */
        private class ViewHolder {
            TextView dateTxt, treatmentTxt;
            ImageView delete_btn;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_treatment_item,
                        null);
                holder = new ViewHolder();
                holder.dateTxt = (TextView) convertView
                        .findViewById(R.id.dateTxt);
                holder.treatmentTxt = (TextView) convertView
                        .findViewById(R.id.treatmentTxt);
                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.treatmentTxt.setVisibility(View.VISIBLE);
            holder.treatmentTxt.setText(rowItems.get(position).getDiagnosis());
            holder.dateTxt.setText(rowItems.get(position).getDate());

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    showCarGiverDialog(rowItems.get(position), "Update Caregiver");


                    startActivity(new Intent(MedicalHistoryActivity.this, AddDiagnosisActivity.class)
                            .putExtra("id", rowItems.get(position).getDrowid()).putExtra("title", rowItems.get(position).getHistorytype()));

                }
            });
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteDiagnosis(String.valueOf(rowItems.get(position).getDrowid()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),MedicalHistoryActivity.this);
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
