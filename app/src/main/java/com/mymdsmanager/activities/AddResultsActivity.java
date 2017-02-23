package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.BloodCountResultWrapper;
import com.mymdsmanager.wrapper.BoneMarrowResultWrapper;
import com.mymdsmanager.wrapper.Transfusionwrapper;
import com.mymdsmanager.wrapper.TreatmentInfoWrapper;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddResultsActivity extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.addResultsBtn)
    Button addResultsBtn;
    @Bind(R.id.bct_ll)
    LinearLayout bctLl;
    @Bind(R.id.treatment_ll)
    LinearLayout treatmentLl;
    @Bind(R.id.boneMarrow_ll)
    LinearLayout boneMarrowLl;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.topContainer)
    LinearLayout topContainer;
    @Bind(R.id.listImgBtn)
    ImageButton listImgBtn;
    @Bind(R.id.chartImgBtn)
    ImageButton chartImgBtn;
    @Bind(R.id.tabLayout)
    LinearLayout tabLayout;
    @Bind(R.id.transfusion_ll)
    LinearLayout transfusionLl;
    @Bind(R.id.typetransfusionSpinner)
    Spinner typetransfusionSpinner;
    private Toolbar toolbar;
    int[] x;
    double[] y;
    private String[] title1;
    private GraphicalView mChart;
    private LinearLayout graph;
    private String title = "";
    DBAdapter dbAdapter;
    private GraphView graphPlates, graphPBRS;
    Date minDate = null, maxDate = null;
    boolean isChartSelected = false;
    DataPoint arr[];
    ArrayList<BloodCountResultWrapper> arrayList;
    ArrayList<Transfusionwrapper> transfusionwrappers;
    ArrayList<String> add_diagnosis;
    HomeWatcher mHomeWatcher;
    private int image_store_columan_count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_results);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        graph = (LinearLayout) findViewById(R.id.graph);
        graphPlates = (GraphView) findViewById(R.id.trnsplategraph);
        graphPBRS = (GraphView) findViewById(R.id.transpbrsgraph);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(title);
        if (title.equalsIgnoreCase("MDS Treatments")) {
            addResultsBtn.setText("Add Treatment");
        }
        mHomeWatcher = new HomeWatcher(AddResultsActivity.this);
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

    @OnClick(R.id.chartImgBtn)
    public void chartImgBtn() {
        try {
            isChartSelected = true;
            listImgBtn.setImageResource(R.mipmap.btn_list_normal);
            chartImgBtn.setImageResource(R.mipmap.btn_chart_touch);
            listView.setVisibility(View.GONE);
//          symtomListView.setVisibility(View.GONE);
            if (title.equalsIgnoreCase("Blood Counts")) {
                tabLayout.setVisibility(View.VISIBLE);
                bctLl.setVisibility(View.GONE);
                typetransfusionSpinner.setVisibility(View.VISIBLE);
                graph.setVisibility(View.VISIBLE);
                setGraphType();
            } else {
                addResultsBtn.setVisibility(View.GONE);
                transfusionLl.setVisibility(View.GONE);
                typetransfusionSpinner.setVisibility(View.VISIBLE);
//                setTransfusionGraph();
                if (typetransfusionSpinner.getSelectedItem().toString().equalsIgnoreCase("Platelets")) {
                    setTransfusionGraph();
                } else  if (typetransfusionSpinner.getSelectedItem().toString().equalsIgnoreCase("PRBCs")){
                    setTransfusionGraph1();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.listImgBtn)
    public void listImgBtn() {
        isChartSelected = false;
        listImgBtn.setImageResource(R.mipmap.btn_list_touch);
        chartImgBtn.setImageResource(R.mipmap.btn_chart_normal);
        listView.setVisibility(View.VISIBLE);
//      symtomListView.setVisibility(View.VISIBLE);
        if (title.equalsIgnoreCase("Blood Counts")) {
            graph.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            bctLl.setVisibility(View.VISIBLE);
            typetransfusionSpinner.setVisibility(View.GONE);
        } else {
            addResultsBtn.setVisibility(View.VISIBLE);
            graph.setVisibility(View.GONE);
            transfusionLl.setVisibility(View.VISIBLE);
            graphPlates.setVisibility(View.GONE);
            graphPBRS.setVisibility(View.GONE);
            typetransfusionSpinner.setVisibility(View.GONE);
//          setTransfusionGraph();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        fillList();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(AddResultsActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    private void fillList() {
        add_diagnosis = new ArrayList<>();
        dbAdapter = new DBAdapter(AddResultsActivity.this);
        dbAdapter.openMdsDB();
        DataManager.getInstance().setTreatmentInfoWrapper(new TreatmentInfoWrapper());
        DataManager.getInstance().setBloodCountResultWrapper(new BloodCountResultWrapper());
        DataManager.getInstance().setBoneMarrowResultWrapper(new BoneMarrowResultWrapper());
        topContainer.setVisibility(View.VISIBLE);
        if (title.equalsIgnoreCase("Bone Marrow Results")) {
            ArrayList<BoneMarrowResultWrapper> list = dbAdapter.getBoneMarrow();
            Collections.reverse(list);
            BoneMarrowAdapter adapter = new BoneMarrowAdapter(AddResultsActivity.this, list);
            listView.setAdapter(adapter);
            if (dbAdapter.getBoneMarrow().isEmpty()) {
                topContainer.setVisibility(View.GONE);
            }
            if (list.size()>0) {
                for (int i = 0; i < list.size(); i++) {
                    if (!TextUtils.isEmpty(list.get(i).getBoneimages())) {
                        image_store_columan_count++;
                    }
                }
            }
            treatmentLl.setVisibility(View.GONE);
            bctLl.setVisibility(View.GONE);
            boneMarrowLl.setVisibility(View.VISIBLE);
            transfusionLl.setVisibility(View.GONE);
        } else if (title.equalsIgnoreCase("MDS Treatments")) {
            ArrayList<TreatmentInfoWrapper> list = dbAdapter.getTreatMentInfo();
            Collections.reverse(list);
            TreatMentAdapter adapter = new TreatMentAdapter(AddResultsActivity.this, list);
            listView.setAdapter(adapter);
            if (dbAdapter.getTreatMentInfo().isEmpty()) {
                topContainer.setVisibility(View.GONE);
            }
            transfusionLl.setVisibility(View.GONE);
            treatmentLl.setVisibility(View.VISIBLE);
            bctLl.setVisibility(View.GONE);
            boneMarrowLl.setVisibility(View.GONE);
        } else if (title.equalsIgnoreCase("Blood Counts")) {
            arr = new DataPoint[dbAdapter.getBloodCount().size()];
            arrayList = dbAdapter.getBloodCount();
            Collections.reverse(arrayList);
            BCTAdapter adapter = new BCTAdapter(AddResultsActivity.this, arrayList);
            listView.setAdapter(adapter);
//          Collections.reverse(arrayList);
            if (arrayList.isEmpty()) {
                topContainer.setVisibility(View.GONE);
            }
            if (arrayList.size()>0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!TextUtils.isEmpty(arrayList.get(i).getImage_path())) {
                        image_store_columan_count++;
                    }
                }
            }
            if (!isChartSelected) {
                treatmentLl.setVisibility(View.GONE);
                bctLl.setVisibility(View.VISIBLE);
                transfusionLl.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                boneMarrowLl.setVisibility(View.GONE);
            }
            add_diagnosis.add("Select Blood Count");
            add_diagnosis.add("HGB");
            add_diagnosis.add("WBC");
            add_diagnosis.add("ANC");
            add_diagnosis.add("PLATELETS");
            add_diagnosis.add("FERRITIN");
            setGraphType();
        } else if (title.equalsIgnoreCase("Transfusions")) {
            arr = new DataPoint[dbAdapter.getTransfusion().size()];
            ArrayList<Transfusionwrapper> list = dbAdapter.getTransfusion();
            Collections.reverse(list);
            TransfusionAdapter transfusionAdapter = new TransfusionAdapter(AddResultsActivity.this, list);
            listView.setAdapter(transfusionAdapter);
            transfusionwrappers = dbAdapter.getTransfusion();
//            Collections.reverse(transfusionwrappers);
            if (dbAdapter.getTransfusion().isEmpty()) {
                topContainer.setVisibility(View.GONE);
            }
            treatmentLl.setVisibility(View.GONE);
            bctLl.setVisibility(View.GONE);
            transfusionLl.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            boneMarrowLl.setVisibility(View.GONE);
            add_diagnosis.add("Select Transfusion");
            add_diagnosis.add("Platelets");
            add_diagnosis.add("PRBCs");
            setGraphType();
        }
        dbAdapter.closeMdsDB();
    }

    @OnClick(R.id.addResultsBtn)
    public void addResultsBtn() {
        if (title.equalsIgnoreCase("Bone Marrow Results")) {
            startActivity(new Intent(AddResultsActivity.this, AddResultsBoneMarrowActivity.class).putExtra("imageclm",image_store_columan_count));
            treatmentLl.setVisibility(View.GONE);
            bctLl.setVisibility(View.GONE);
            boneMarrowLl.setVisibility(View.VISIBLE);
        } else if (title.equalsIgnoreCase("MDS Treatments")) {

            treatmentLl.setVisibility(View.GONE);
            bctLl.setVisibility(View.GONE);
            boneMarrowLl.setVisibility(View.GONE);
            startActivity(new Intent(AddResultsActivity.this, AddTreatmentsActivity.class));
        } else if (title.equalsIgnoreCase("Blood Counts")) {
            if (!isChartSelected) {
                treatmentLl.setVisibility(View.GONE);
                bctLl.setVisibility(View.VISIBLE);
                boneMarrowLl.setVisibility(View.GONE);
            }
            startActivity(new Intent(AddResultsActivity.this, AddResultsBloodCountTransfutionsActivity.class).putExtra("imageclm",image_store_columan_count));
        } else if (title.equalsIgnoreCase("Transfusions")) {

//       treatmentLl.setVisibility(View.GONE);
//       bctLl.setVisibility(View.VISIBLE);
//       boneMarrowLl.setVisibility(View.GONE);
            startActivity(new Intent(AddResultsActivity.this, AddTranfusionDiagnosis.class).putExtra("pos", -1));
        }
    }

    @Override
    public void onFinish() {

    }

    private class TreatMentAdapter extends BaseAdapter {
        Context context;
        ArrayList<TreatmentInfoWrapper> rowItems;

        public TreatMentAdapter(Context context,
                                ArrayList<TreatmentInfoWrapper> arrayList) {
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
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteTreatMentInfo(String.valueOf(rowItems.get(position).getTrowid()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),AddResultsActivity.this);
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
//            holder.treatmentTxt.setText(rowItems.get(position).getNotes());
            holder.dateTxt.setText(rowItems.get(position).getStartdate());
            if (TextUtils.isEmpty(rowItems.get(position).getTreatement())||rowItems.get(position).getTreatement().equalsIgnoreCase("null"))
            {
                holder.treatmentTxt.setText("-");
            }else {
                holder.treatmentTxt.setText(rowItems.get(position).getTreatement());
            }
            holder.treatmentTxt.setVisibility(View.GONE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showCarGiverDialog(rowItems.get(position), "Update Caregiver");

                    DataManager.getInstance().setTreatmentInfoWrapper(rowItems.get(position));
                    startActivity(new Intent(AddResultsActivity.this, AddTreatmentsActivity.class)
                            .putExtra("id", rowItems.get(position).getTrowid()).putExtra("title",rowItems.get(position).getTreatement()));
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

    private class BoneMarrowAdapter extends BaseAdapter {
        Context context;
        ArrayList<BoneMarrowResultWrapper> rowItems;

        public BoneMarrowAdapter(Context context, ArrayList<BoneMarrowResultWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
        }

        /* private view holder class */
        private class ViewHolder {
            TextView dateTxt, descriptionTxt, boneBlastTxt;
            ImageView delete_btn;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_bone_marrow_item,
                        null);
                holder = new ViewHolder();
                holder.dateTxt = (TextView) convertView
                        .findViewById(R.id.dateTxt);
                holder.descriptionTxt = (TextView) convertView
                        .findViewById(R.id.descriptionTxt);
                holder.boneBlastTxt = (TextView) convertView
                        .findViewById(R.id.boneBlastTxt);
                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);
                holder.delete_btn.setVisibility(View.VISIBLE);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.descriptionTxt.setText(rowItems.get(position).getDescription());
            holder.dateTxt.setText(rowItems.get(position).getDate());
            holder.boneBlastTxt.setText(rowItems.get(position).getMarrowblast());
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteBoneMarrow(String.valueOf(rowItems.get(position).getBrowid()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),AddResultsActivity.this);
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
//                    showCarGiverDialog(rowItems.get(position), "Update Caregiver");

                    DataManager.getInstance().setBoneMarrowResultWrapper(rowItems.get(position));
                    startActivity(new Intent(AddResultsActivity.this, AddResultsBoneMarrowActivity.class)
                            .putExtra("id", rowItems.get(position).getBrowid()).putExtra("imageclm",position));
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

    private class BCTAdapter extends BaseAdapter {
        Context context;
        ArrayList<BloodCountResultWrapper> rowItems;

        public BCTAdapter(Context context,
                          ArrayList<BloodCountResultWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
        }

        /* private view holder class */
        private class ViewHolder {
            TextView dateTxt, hgbTxt, wbcTxt, ancTxt, rbcsTxt;
            ImageView delete_btn;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_bct_item,
                        null);
                holder = new ViewHolder();
                holder.dateTxt = (TextView) convertView
                        .findViewById(R.id.dateTxt);
                holder.hgbTxt = (TextView) convertView
                        .findViewById(R.id.hgbTxt);
                holder.wbcTxt = (TextView) convertView
                        .findViewById(R.id.wbcTxt);
                holder.ancTxt = (TextView) convertView
                        .findViewById(R.id.ancTxt);
                holder.rbcsTxt = (TextView) convertView
                        .findViewById(R.id.rbcsTxt);

                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.hgbTxt.setText(rowItems.get(position).getHgb());
            holder.dateTxt.setText(rowItems.get(position).getDate());
            holder.wbcTxt.setText(rowItems.get(position).getWbc());
            holder.ancTxt.setText(rowItems.get(position).getAnc());
            holder.rbcsTxt.setText(rowItems.get(position).getRbcs());
            holder.rbcsTxt.setText(rowItems.get(position).getPlatelets());

            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteBloodCount(String.valueOf(rowItems.get(position).getBroid()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),AddResultsActivity.this);
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
//                    showCarGiverDialog(rowItems.get(position), "Update Caregiver");
                    mHomeWatcher.stopWatch();
                    DataManager.getInstance().setBloodCountResultWrapper(rowItems.get(position));
                    startActivity(new Intent(AddResultsActivity.this, AddResultsBloodCountTransfutionsActivity.class)
                            .putExtra("id", rowItems.get(position).getBroid()).putExtra("imageclm",position));
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

    private class TransfusionAdapter extends BaseAdapter {
        Context context;
        ArrayList<Transfusionwrapper> rowItems;

        public TransfusionAdapter(Context context,
                                  ArrayList<Transfusionwrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
        }

        /* private view holder class */
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

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_bone_marrow_item,
                        null);
                convertView.setTag(holder);
                holder.dateTxt = (TextView) convertView
                        .findViewById(R.id.dateTxt);
                holder.descriptionTxt = (TextView) convertView
                        .findViewById(R.id.descriptionTxt);
                holder.boneBlastTxt = (TextView) convertView
                        .findViewById(R.id.boneBlastTxt);
                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);
                holder.delete_btn.setVisibility(View.VISIBLE);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.descriptionTxt.setText(rowItems.get(position).getTtype());
            holder.dateTxt.setText(rowItems.get(position).getDate());
            holder.boneBlastTxt.setText(rowItems.get(position).getUnit());
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteTransfusion(String.valueOf(rowItems.get(position).getId()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),AddResultsActivity.this);
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
                    mHomeWatcher.stopWatch();
                    startActivity(new Intent(AddResultsActivity.this, AddTranfusionDiagnosis.class).putExtra("pos", position).putExtra("model",rowItems.get(position)));
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView dateTxt, descriptionTxt, boneBlastTxt;
            ImageView delete_btn;
        }

    }

    public void setBloodGraph(String type) {
        dbAdapter.openMdsDB();
        ArrayList<BloodCountResultWrapper> bloodCountResultWrappers = dbAdapter.getBloodCount();
        dbAdapter.closeMdsDB();
//        bloodCountResultWrappers.addAll(arrayList);
//        Collections.reverse(bloodCountResultWrappers);
        List<Long> longList = new ArrayList<Long>();
//        graph.removeAllSeries();
        graphPBRS.setVisibility(View.GONE);
        graphPlates.setVisibility(View.GONE);
        graph.setVisibility(View.VISIBLE);
        x = null;
        y = null;
        int size = arrayList.size();
        title1 = null;
        mChart = null;
        graph.removeAllViews();
        x = new int[size];
        y = new double[size];
        title1 = new String[size];
        try {
            for (int i = 0; i < bloodCountResultWrappers.size(); i++) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(formateDate1(bloodCountResultWrappers.get(i).getDate()));

                longList.add(calendar.getTimeInMillis());
                Date d1 = calendar.getTime();

                Log.e("Date : ", d1 + "");

//                if (i == 0) {
//                    minDate = d1;
//                }
//                maxDate = d1;

                if (type.equalsIgnoreCase("HGB")) {
//                    arr[i] = new DataPoint(d1, Double.parseDouble(bloodCountResultWrappers.get(i).getHgb()));
                    x[i] = i;
                    y[i] = Double.parseDouble(bloodCountResultWrappers.get(i).getHgb());
                    title1[i] = String.valueOf(formateDate2(bloodCountResultWrappers.get(i).getDate()));
                } else if (type.equalsIgnoreCase("WBC")) {
//                    arr[i] = new DataPoint(d1, Double.parseDouble(bloodCountResultWrappers.get(i).getWbc()));
                    x[i] = i;
                    y[i] = Double.parseDouble(bloodCountResultWrappers.get(i).getWbc());
                    title1[i] = String.valueOf(formateDate2(bloodCountResultWrappers.get(i).getDate()));
                } else if (type.equalsIgnoreCase("ANC")) {
//                    arr[i] = new DataPoint(d1, Double.parseDouble(bloodCountResultWrappers.get(i).getAnc()));
                    x[i] = i;
                    y[i] = Double.parseDouble(bloodCountResultWrappers.get(i).getAnc());
                    title1[i] = String.valueOf(formateDate2(bloodCountResultWrappers.get(i).getDate()));
                } else if (type.equalsIgnoreCase("PLATELETS")) {
//                    arr[i] = new DataPoint(d1, Double.parseDouble(bloodCountResultWrappers.get(i).getPlatelets()));
                    x[i] = i;
                    y[i] = Double.parseDouble(bloodCountResultWrappers.get(i).getPlatelets());
                    title1[i] = String.valueOf(formateDate2(bloodCountResultWrappers.get(i).getDate()));
                } else if (type.equalsIgnoreCase("RBCS")) {
//                    arr[i] = new DataPoint(d1, Double.parseDouble(bloodCountResultWrappers.get(i).getRbcs()));
                    x[i] = i;
                    y[i] = Double.parseDouble(bloodCountResultWrappers.get(i).getRbcs());
                    title1[i] = String.valueOf(formateDate2(bloodCountResultWrappers.get(i).getDate()));
                } else if (type.equalsIgnoreCase("TRANSFUSION")) {
//                    arr[i] = new DataPoint(d1, Double.parseDouble(bloodCountResultWrappers.get(i).getTranfusion()));
                    x[i] = i;
                    y[i] = Double.parseDouble(bloodCountResultWrappers.get(i).getTranfusion());
                    title1[i] = String.valueOf(formateDate2(bloodCountResultWrappers.get(i).getDate()));
                } else if (type.equalsIgnoreCase("FERRITIN")) {
//                    arr[i] = new DataPoint(d1, Double.parseDouble(bloodCountResultWrappers.get(i).getFerritin()));
                    x[i] = i;
                    y[i] = Double.parseDouble(bloodCountResultWrappers.get(i).getFerritin());
                    title1[i] = String.valueOf(formateDate2(bloodCountResultWrappers.get(i).getDate()));
                }
            }
            openChart("");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String formateDate2(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MM-dd-yy");
            Date date = originalFormat.parse(dateString);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private Date formateDate1(String dateString) {
        Date date = null;
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd yyyy");
            date = originalFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setTransfusionGraph() {
//        graph = (GraphView) findViewById(R.id.graph);
//        graph.setVisibility(View.GONE);
        try {
            graph.setVisibility(View.VISIBLE);
            graphPBRS.setVisibility(View.GONE);

            ArrayList<Transfusionwrapper> transfusionwrapperArrayList = new ArrayList<>();
            List<Long> longList = new ArrayList<Long>();
            for (int k = 0; k < transfusionwrappers.size(); k++) {
                if (transfusionwrappers.get(k).getTtype().equalsIgnoreCase("Platelets")) {
                    if (!TextUtils.isEmpty(transfusionwrappers.get(k).getUnit())) {
                        transfusionwrapperArrayList.add(transfusionwrappers.get(k));
                    }
                }
            }
            x = null;
            y = null;
            int size = transfusionwrapperArrayList.size();
            title1 = null;
            mChart = null;
            graph.removeAllViews();
            x = new int[size];
            y = new double[size];
            title1 = new String[size];
            arr = new DataPoint[transfusionwrapperArrayList.size()];
            for (int i = 0; i < transfusionwrapperArrayList.size(); i++) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(formateDate(transfusionwrapperArrayList.get(i).getDate()));
                longList.add(calendar.getTimeInMillis());
                Date d1 = calendar.getTime();

                Log.e("Date : ", d1 + "");

//                if (i == 0) {
//                    minDate = d1;
//                }
//                maxDate = d1;
                x[i] = i;
                y[i] = Double.parseDouble(transfusionwrapperArrayList.get(i).getUnit());
                title1[i] = String.valueOf(formateDate21(transfusionwrapperArrayList.get(i).getDate()));

//                arr[i] = new DataPoint(d1, Double.parseDouble(transfusionwrapperArrayList.get(i).getUnit()));
            }
            openChart("");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String formateDate21(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MM-dd-yy");
            Date date = originalFormat.parse(dateString);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setTransfusionGraph1() {
//        graph = (GraphView) findViewById(R.id.graph);
        try {
            graph.setVisibility(View.VISIBLE);

//            graphPBRS.setVisibility(View.VISIBLE);
            List<Long> longList = new ArrayList<Long>();
            ArrayList<Transfusionwrapper> transfusionwrapperArrayList = new ArrayList<>();
            for (int k = 0; k < transfusionwrappers.size(); k++) {
                if (transfusionwrappers.get(k).getTtype().equalsIgnoreCase("PRBCs")) {
                    if (!TextUtils.isEmpty(transfusionwrappers.get(k).getUnit())) {
                        transfusionwrapperArrayList.add(transfusionwrappers.get(k));
                    }
                }
            }
            x = null;
            y = null;
            int size = transfusionwrapperArrayList.size();
            title1 = null;
            mChart = null;
            graph.removeAllViews();
            x = new int[size];
            y = new double[size];
            title1 = new String[size];
            arr = new DataPoint[transfusionwrapperArrayList.size()];
//            Collections.reverse(transfusionwrapperArrayList);
            for (int i = 0; i < transfusionwrapperArrayList.size(); i++) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(formateDate(transfusionwrapperArrayList.get(i).getDate()));
                longList.add(calendar.getTimeInMillis());
                Date d1 = calendar.getTime();

                Log.e("Date : ", d1 + "");

//                if (i == 0) {
//                    minDate = d1;
//                }
//                maxDate = d1;
                x[i] = i;
                y[i] = Double.parseDouble(transfusionwrapperArrayList.get(i).getUnit());
                title1[i] = String.valueOf(formateDate21(transfusionwrapperArrayList.get(i).getDate()));

//                arr[i] = new DataPoint(d1, Double.parseDouble(transfusionwrapperArrayList.get(i).getUnit()));
            }
            openChart("");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openChart(String chartTitle) {

        // Creating an XYSeries for Expense
        XYSeries expenseSeries = new XYSeries("");
        // Adding data to Income and Expense Series

        System.out.println("size of x and y is " + x.length + " y " + y.length);
        for (int i = 0; i < x.length; i++) {

            expenseSeries.add(x[i], y[i]);

        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        // Adding Expense Series to dataset
        dataset.addSeries(expenseSeries);

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(getResources().getColor(R.color.brown_theme));
        expenseRenderer.setPointStyle(PointStyle.CIRCLE);
        expenseRenderer.setPointStrokeWidth(8);
        expenseRenderer.setChartValuesTextSize(25);
        expenseRenderer.setFillPoints(false);
        expenseRenderer.setLineWidth(4);
        expenseRenderer.setDisplayChartValues(false);
        expenseRenderer.setDisplayChartValuesDistance(5);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setShowGridX(true);
        multiRenderer.isMinYSet(0);
        multiRenderer.isMaxXSet(0);
        multiRenderer.setGridColor(getResources().getColor(R.color.black));
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setAxesColor(Color.BLACK);
        multiRenderer.setYLabelsPadding(10);
        multiRenderer.setYLabelsColor(0, getResources().getColor(R.color.black));
        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        multiRenderer.setXLabelsAlign(Paint.Align.RIGHT);

//        multiRenderer.setXLabelsAngle(-90);
        multiRenderer.setLabelsTextSize(15);
        multiRenderer.setXLabelsColor(getResources().getColor(R.color.black));
        multiRenderer.setBarWidth(20);
        multiRenderer.setChartTitle(chartTitle);
        multiRenderer.setYAxisMax(100);
        multiRenderer.setAxisTitleTextSize(25);
        multiRenderer.setZoomButtonsVisible(false);
        for (int i = 0; i < x.length; i++) {
            multiRenderer.addXTextLabel(i, title1[i]);
        }
        multiRenderer.setXAxisMin(-0.5);
        multiRenderer.setXAxisMax(4);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setPanEnabled(true, true);
        multiRenderer.setZoomEnabled(false, false);

        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);
//        double[] panLimits = {0, expenseSeries.getMaxX(), 0, expenseSeries.getMaxY()};
//        multiRenderer.setPanLimits(panLimits);
        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to
        multiRenderer.addSeriesRenderer(expenseRenderer);
        // Getting a reference to LinearLayout of the MainActivity Layout
        // Creating a Line Chart
        mChart = ChartFactory.getLineChartView(getBaseContext(), dataset,
                multiRenderer);

        mChart.setBackgroundColor(Color.WHITE);
        multiRenderer.setClickEnabled(true);//
        multiRenderer.setSelectableBuffer(10);
        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();

                if (seriesSelection != null) {
                    String month = title1[(int) seriesSelection.getXValue()];
                    // Getting the y value
                    // Getting the y value
                    int amount = (int) seriesSelection.getValue();
                    Toast.makeText(getBaseContext(), month + "," + amount,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Adding the Line Chart to the LinearLayout
        graph.addView(mChart);

    }

    private Date formateDate(String dateString) {
        Date date = null;
        try {
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy");
            date = targetFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Date formateDate12(String dateString) {
        Date date = null;
        try {
            SimpleDateFormat targetFormat = new SimpleDateFormat("MM-dd-yyyy");
            date = targetFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setGraphType() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.transfusionspinner_layout, add_diagnosis) {
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextColor(getResources()
                        .getColorStateList(R.color.white));

                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,
                        parent);


                ((TextView) v).setTextColor(Color.BLACK);


                return v;
            }
        };
        //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.transfusionspinner_layout);
        typetransfusionSpinner.setAdapter(spinnerArrayAdapter);
        typetransfusionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (title.equalsIgnoreCase("Blood Counts")) {
                    if (!typetransfusionSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Blood Count")) {
                        setBloodGraph(typetransfusionSpinner.getSelectedItem().toString());
                    }
                } else if (title.equalsIgnoreCase("Transfusions")) {
                    if (!typetransfusionSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Transfusion"))
                    {
                        dbAdapter.openMdsDB();
                        transfusionwrappers = dbAdapter.getTransfusion();
//                        Collections.reverse(transfusionwrappers);
                        dbAdapter.closeMdsDB();

                        if (typetransfusionSpinner.getSelectedItem().toString().equalsIgnoreCase("Platelets")) {
//                            Collections.reverse(transfusionwrappers);
                            setTransfusionGraph();
                        } else if (typetransfusionSpinner.getSelectedItem().toString().equalsIgnoreCase("PRBCs")){
                            setTransfusionGraph1();
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
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
