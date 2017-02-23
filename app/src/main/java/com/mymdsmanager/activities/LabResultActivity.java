package com.mymdsmanager.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.mymdsmanager.datacontrollers.Constants;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.LabResultInfoWrapper;
import com.mymdsmanager.wrapper.MedicalProfessionalWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class LabResultActivity extends AppCompatActivity implements OnFinishActivity {
    @Bind(R.id.showHideDiagnosisImgBtn)
    ImageButton showHideDiagnosisImgBtn;
    @Bind(R.id.diagnosis_ll)
    LinearLayout diagnosisLl;
    @Bind(R.id.diagnosisListView)
    ListView diagnosisListView;
    @Bind(R.id.addDiagnosisBtn)
    Button addDiagnosisBtn;
    //    @Bind(R.id.showHideSurgeryImgBtn)
//    ImageButton showHideSurgeryImgBtn;
//    @Bind(R.id.surgery_ll)
//    LinearLayout surgeryLl;
//    @Bind(R.id.surgeryListView)
//    ListView surgeryListView;
//    @Bind(R.id.addSurgeryBtn)
//    Button addSurgeryBtn;
    private DBAdapter dbAdapter;
    private Toolbar toolbar;
    String title = "";
    HomeWatcher mHomeWatcher;
    private int image_store_columan_count=0;
    String genStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_result);
        ButterKnife.bind(this);
        dbAdapter = new DBAdapter(LabResultActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        genStr = MyApplication.getStringPrefs(Constants.genderStr);
        title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);

        mHomeWatcher = new HomeWatcher(LabResultActivity.this);
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

    @OnClick(R.id.showHideDiagnosisImgBtn)
    public void showHideDiagnosisImgBtn() {
        if (diagnosisLl.getVisibility() == View.VISIBLE) {
            showHideDiagnosisImgBtn.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            diagnosisLl.setVisibility(View.GONE);
        } else {
            showHideDiagnosisImgBtn.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            diagnosisLl.setVisibility(View.VISIBLE);
//            setListViewHeightBasedOnChildren(diagnosisListView);
        }


    }

    @Override
    public void onBackPressed() {
        finish();
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dbAdapter.openMdsDB();

        if (title.equalsIgnoreCase("Initial Lab Results")) {
            ArrayList<LabResultInfoWrapper> notesList = dbAdapter.getLabResultsList("Ib");
            Collections.reverse(notesList);
            TreatMentAdapter diagnosisAdapter = new TreatMentAdapter(LabResultActivity.this, notesList);
            diagnosisListView.setAdapter(diagnosisAdapter);
            if (notesList.size()>0) {
                for (int i = 0; i < notesList.size(); i++) {
                    if (!TextUtils.isEmpty(notesList.get(i).getLabimages()))
                    {
                        image_store_columan_count++;
                    }
                }
            }
            if (dbAdapter.getLabResultsList("Ib").size() > 0) {
                diagnosisLl.setVisibility(View.VISIBLE);
            }
        } else {
            ArrayList<LabResultInfoWrapper> notesList = dbAdapter.getLabResultsList("Diagnosis");
            Collections.reverse(notesList);
            TreatMentAdapter surgeryAdapter = new TreatMentAdapter(LabResultActivity.this, notesList);
            diagnosisListView.setAdapter(surgeryAdapter);
            if (dbAdapter.getLabResultsList("Diagnosis").size() > 0) {
                diagnosisLl.setVisibility(View.VISIBLE);
            }
        }

        dbAdapter.closeMdsDB();
        if (isActivityFound) {
            new UpdateDataDialog(LabResultActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @OnClick(R.id.addDiagnosisBtn)
    public void addDiagnosisBtn() {
        if (title.equalsIgnoreCase("Initial Lab Results")) {

            startActivity(new Intent(LabResultActivity.this, AddInitialLabResultsActivity.class)
                    .putExtra("type", "Ib").putExtra("imageclm",image_store_columan_count));
        } else {

            startActivity(new Intent(LabResultActivity.this, AddInitialLabResultsActivity.class)
                    .putExtra("type", "Diagnosis").putExtra("imageclm",image_store_columan_count));
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
        ArrayList<LabResultInfoWrapper> rowItems;

        public TreatMentAdapter(Context context,
                                ArrayList<LabResultInfoWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
        }

        /* private view holder class */
        private class ViewHolder {
            TextView dateTxt, treatmentTxt,resultValue;
            ImageView delete_btn,notesImage;

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
                holder.notesImage=(ImageView)convertView.findViewById(R.id.notesImage);
                holder.resultValue=(TextView)convertView.findViewById(R.id.resultValue);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.notesImage.setVisibility(View.GONE);
            String key = rowItems.get(position).getDiagnosistest();
            if (!TextUtils.isEmpty(key))
            {
                if (genStr.equalsIgnoreCase("Male")) {

                    String str=MyApplication.getApplication().getNormal_value_maleMap().get(key);
                         if (str!=null) {
                             if (MyApplication.getApplication().getNormal_value_maleMap().get(key).contains("null")) {
                                 holder.dateTxt.setText("-");
                             } else {
                                 holder.dateTxt.setText(MyApplication.getApplication().getNormal_value_maleMap().get(key));
                             }
                         }else
                         {
                             holder.dateTxt.setText("-");
                         }
                } else if (genStr.equalsIgnoreCase("Female")) {
                    String str=MyApplication.getApplication().getNormal_value_femaleMap().get(key);
                    if (str!=null) {
                        if (MyApplication.getApplication().getNormal_value_femaleMap().get(key).contains("null")) {
                            holder.dateTxt.setText("-");
                        } else {
                            holder.dateTxt.setText(MyApplication.getApplication().getNormal_value_femaleMap().get(key));
                        }
                    }else
                    {
                        holder.dateTxt.setText("-");
                    }
                } else {

                    HashMap<String, String> map = MyApplication.getApplication().getNormal_value_maleMap();
                    String str=MyApplication.getApplication().getNormal_value_maleMap().get(key);
                    if (str!=null) {
                        if (MyApplication.getApplication().getNormal_value_maleMap().get(key).contains("null")) {
                            holder.dateTxt.setText("-");
                        } else {
                            holder.dateTxt.setText(MyApplication.getApplication().getNormal_value_maleMap().get(key) + " " + rowItems.get(position).getUnits());
                        }
                    }else
                    {
                        holder.dateTxt.setText("-");
                    }
                }

            }else
            {
                holder.dateTxt.setText("-");
            }


            holder.treatmentTxt.setVisibility(View.VISIBLE);
            holder.treatmentTxt.setText(rowItems.get(position).getDiagnosistest());
            holder.resultValue.setText(rowItems.get(position).getResults());
            holder.resultValue.setVisibility(View.VISIBLE);



            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    showCarGiverDialog(rowItems.get(position), "Update Caregiver");


                    startActivity(new Intent(LabResultActivity.this, AddInitialLabResultsActivity.class)
                            .putExtra("id", rowItems.get(position).getLrowid()).putExtra("type", rowItems.get(position).getLabresulttype()).putExtra("imageclm",position));

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
                            dbAdapter.deleteLabResults(String.valueOf(rowItems.get(position).getLrowid()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),LabResultActivity.this);
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
