package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.jjoe64.graphview.series.DataPoint;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.SymptomDetailWrapper;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class SymptomListGraphActivity extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.listImgBtn)
    ImageButton listImgBtn;
    @Bind(R.id.chartImgBtn)
    ImageButton chartImgBtn;
    @Bind(R.id.symtomListView)
    ListView symtomListView;
    @Bind(R.id.addSymtomBtn)
    Button addSymtomBtn;
    @Bind(R.id.typetransfusionSpinner)
    Spinner typetransfusionSpinner;
    private Toolbar toolbar;
    @Bind(R.id.listContainer)
    LinearLayout listContainer;
    private GraphicalView mChart;
    private LinearLayout graph;
    Date minDate = null, maxDate = null;
    boolean isChartSelected = false;
    DBAdapter dbAdapter;
    DataPoint arr[];
    ArrayList<SymptomDetailWrapper> arrayList;
    ArrayList<SymptomDetailWrapper> arrayListGraph;
    int[] x;
    double[] y;
    private String[] title;
    HomeWatcher mHomeWatcher;
    ArrayList<String> sysptomnameArr = new ArrayList<>();
    ArrayList<SymptomDetailWrapper> symptomDetailWrapperArrayList;
    @OnClick(R.id.chartImgBtn)
    public void chartImgBtn() {
        try {
            addSymtomBtn.setVisibility(View.GONE);
            isChartSelected = true;
            listImgBtn.setImageResource(R.mipmap.btn_list_normal);
            chartImgBtn.setImageResource(R.mipmap.btn_chart_touch);
            listContainer.setVisibility(View.GONE);
            symtomListView.setVisibility(View.GONE);
            graph.setVisibility(View.VISIBLE);
            typetransfusionSpinner.setVisibility(View.VISIBLE);
//            if (minDate == maxDate) {
//                Toast.makeText(SymptomListGraphActivity.this, "No data to show", Toast.LENGTH_LONG).show();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.listImgBtn)
    public void listImgBtn() {
        addSymtomBtn.setVisibility(View.VISIBLE);
        isChartSelected = false;
        listImgBtn.setImageResource(R.mipmap.btn_list_touch);
        chartImgBtn.setImageResource(R.mipmap.btn_chart_normal);
        listContainer.setVisibility(View.VISIBLE);
        symtomListView.setVisibility(View.VISIBLE);
        graph.setVisibility(View.GONE);
        typetransfusionSpinner.setVisibility(View.GONE);
    }

    @OnClick(R.id.addSymtomBtn)
    public void addSymtomBtn() {
        finish();
    }
    HashMap<String,String> nameSubcateHashMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_symptom_list_graph);
            ButterKnife.bind(this);
            toolbar = (Toolbar) findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.icn_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            graph = (LinearLayout) findViewById(R.id.graph);
            getSupportActionBar().setTitle("Symptom Tracker");
            dbAdapter = new DBAdapter(SymptomListGraphActivity.this);
            dbAdapter.openMdsDB();
            arrayList = new ArrayList<>();
            sysptomnameArr.add(" No Symptom");
            arrayList = dbAdapter.getSymtomDetailList();
            arrayListGraph = dbAdapter.getSymtomDetailList();
//            Collections.reverse(arrayListGraph);

            for (int i = 0; i <arrayList.size(); i++) {
                if (nameSubcateHashMap.get(arrayList.get(i).getSubsymptom_str())==null)
                {
                    nameSubcateHashMap.put(arrayList.get(i).getSubsymptom_str(),arrayList.get(i).getSubsymptom_str());
                    sysptomnameArr.add(arrayList.get(i).getSubsymptom_str());
                }
            }
            Collections.sort(sysptomnameArr, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });
            setGraphType();
            Collections.reverse(arrayList);
            SymptomAdapter adapter = new SymptomAdapter(SymptomListGraphActivity.this, arrayList);
            symtomListView.setAdapter(adapter);
//            setGraph();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
         mHomeWatcher = new HomeWatcher(SymptomListGraphActivity.this);
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

    @Override
    public void onBackPressed() {

        finish();
    }

    boolean isActivityFound = false;

    @Override
    protected void onResume() {
        super.onResume();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(SymptomListGraphActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    public void setGraph(String name) {
        x = null;
        y = null;
        int size = symptomDetailWrapperArrayList.size();
        title = null;
        mChart = null;
        graph.removeAllViews();
        x = new int[size];
        y = new double[size];
        title = new String[size];
        arr = new DataPoint[dbAdapter.getSymtomDetailList().size()];
        List<Long> longList = new ArrayList<Long>();
//        Collections.reverse(symptomDetailWrapperArrayList);

        for (int i = 0; i <symptomDetailWrapperArrayList.size(); i++) {
                x[i] = i;
            int severity = 0;
            try {
                severity = Integer.parseInt(symptomDetailWrapperArrayList.get(i).getSeverity() );
                severity = severity;
            }catch (Exception e){
                e.toString();
            }
                y[i] =  severity;
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println("dates" + symptomDetailWrapperArrayList.get(i).getSymptomdate());
                title[i] = String.valueOf(formateDate1(symptomDetailWrapperArrayList.get(i).getSymptomdate()));
                System.out.println("title" + title[i]);
        }
        openChart("");
 }

    private String formateDate1(String dateString) {
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
    private Date formateDate(String dateString) {
        Date date = null;
        try {
//            07-22-2016
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd yyyy");
            date = originalFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void onFinish() {

    }

    private class SymptomAdapter extends BaseAdapter {
        Context context;
        ArrayList<SymptomDetailWrapper> rowItems;

        public SymptomAdapter(Context context, ArrayList<SymptomDetailWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
        }
        /* private view holder class */
        private class ViewHolder {
            TextView dateTxt, descriptionTxt, boneBlastTxt;
            ImageView delete_btn;
        }

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
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.img_arrow_medicalhistory);

            int rh = rightDrawable.getIntrinsicHeight();
            int rw = rightDrawable.getIntrinsicWidth();
            rightDrawable.setBounds(0, 0, rw, rh);
            holder.boneBlastTxt.setCompoundDrawables(null, null, rightDrawable, null);
            holder.descriptionTxt.setText(rowItems.get(position).getSubsymptom_str());
            holder.dateTxt.setText(rowItems.get(position).getSymptomdate());
            int severity=0;
            if (!TextUtils.isEmpty(rowItems.get(position).getSeverity())) {
                severity = Integer.parseInt(rowItems.get(position).getSeverity());
                severity=severity;
            }
            holder.boneBlastTxt.setText(rowItems.get(position).getSeverity());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                  showCarGiverDialog(rowItems.get(position), "Update Caregiver");

                    startActivity(new Intent(SymptomListGraphActivity.this, SymptomTrackerActivity.class).putExtra("id", rowItems.get(position).getSrowid()));
                }
            });
            holder.delete_btn.setVisibility(View.VISIBLE);
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item ?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteSYMPTOINFO(String.valueOf(rowItems.get(position).getSrowid()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),SymptomListGraphActivity.this);
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
//        XYMultipleSeriesRenderer dataset = new XYMultipleSeriesRenderer();
        // Adding Expense Series to dataset
        dataset.addSeries(expenseSeries);
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();


        expenseRenderer.setColor(getResources().getColor(R.color.brown_theme));
        expenseRenderer.setPointStyle(PointStyle.CIRCLE);
        //expenseRenderer.setPointSize(20f);
//         XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
//        mRenderer.setPointSize(20);
        expenseRenderer.setLineWidth(4);

        expenseRenderer.setPointStrokeWidth(8);
        expenseRenderer.setChartValuesTextSize(25);
        expenseRenderer.setDisplayBoundingPoints(true);
        expenseRenderer.setFillPoints(false);

        expenseRenderer.setDisplayChartValues(false);
//        expenseRenderer.setDisplayChartValuesDistance(25);
        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setGridColor(getResources().getColor(R.color.black));
        multiRenderer.setShowGridX(true);
        multiRenderer.setShowGridY(true);
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setAxesColor(Color.BLACK);
        multiRenderer.setYLabelsPadding(5);
        multiRenderer.setYAxisMax(10);
        multiRenderer.setYAxisMin(0);
//        multiRenderer.setMargins(new int[]{ 60, 60, 60, 60 });
        multiRenderer.setYLabelsColor(0,getResources().getColor(R.color.black));
        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);

        multiRenderer.setXLabelsAlign(Paint.Align.RIGHT);
        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
//        multiRenderer.setXLabelsAngle(-90);
        multiRenderer.setLabelsTextSize(25);
        multiRenderer.setXLabelsColor(getResources().getColor(R.color.black));
        multiRenderer.setBarWidth(20);
        multiRenderer.setChartTitle(chartTitle);
        multiRenderer.setYAxisMax(10);
        multiRenderer.setAntialiasing(false);
        System.out.println("width"+multiRenderer.getBarWidth());
        multiRenderer.setInScroll(false);
//        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
//        multiRenderer.setAxisTitleTextSize(25);
        multiRenderer.setZoomButtonsVisible(false);
        for (int i = 0; i < x.length; i++) {
            multiRenderer.addXTextLabel(i, title[i]);
        }
        multiRenderer.setXAxisMin(-0.5);
        multiRenderer.setXAxisMax(4);
        multiRenderer.setApplyBackgroundColor(true);
//        multiRenderer.setPanLimits(panLimits);
        multiRenderer.setPanEnabled(true, false);
//        multiRenderer.setP
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);
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
                    String month = title[(int)seriesSelection.getXValue()];
                    // Getting the y value

                    // Getting the y value
                    int amount = (int) seriesSelection.getValue();
                    Toast.makeText(
                            getBaseContext(),month+","+amount ,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Adding the Line Chart to the LinearLayout

        graph.addView(mChart);


//        mChart.set
    }
    public void setGraphType() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.transfusionspinner_layout, sysptomnameArr) {
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

                symptomDetailWrapperArrayList = new ArrayList<SymptomDetailWrapper>();

                for (int k = 0; k <arrayListGraph.size(); k++) {
                    if (typetransfusionSpinner.getSelectedItem().toString().equalsIgnoreCase(arrayListGraph.get(k).getSubsymptom_str())) {
                        symptomDetailWrapperArrayList.add(arrayListGraph.get(k));
                    }
                }
                setGraph(typetransfusionSpinner.getSelectedItem().toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
