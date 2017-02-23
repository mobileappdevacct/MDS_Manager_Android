package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.NotesWrapper;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nitin on 9/9/15.
 */
public class NotesActivity extends AppCompatActivity implements OnFinishActivity{

    @Bind(R.id.boneMarrow_ll)
    LinearLayout boneMarrowLl;
    @Bind(R.id.topContainer)
    LinearLayout topContainer;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.addResultsBtn)
    Button addResultsBtn;


    DBAdapter dbAdapter;
    private Toolbar toolbar;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);


        dbAdapter = new DBAdapter(NotesActivity.this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.mipmap.icn_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Notes");
        mHomeWatcher = new HomeWatcher(NotesActivity.this);
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
    }

    boolean isActivityFound = false;

    @Override
    public void onBackPressed() {
        mHomeWatcher.stopWatch();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
        mHomeWatcher.startWatch();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dbAdapter.openMdsDB();
        ArrayList<NotesWrapper> notesList = dbAdapter.getNotesList();
        Collections.reverse(notesList);
        TreatMentAdapter adapter = new TreatMentAdapter(NotesActivity.this, notesList);
        listView.setAdapter(adapter);
        if (dbAdapter.getNotesList().isEmpty()) {
            boneMarrowLl.setVisibility(View.GONE);
        } else {
            boneMarrowLl.setVisibility(View.VISIBLE);

        }
        dbAdapter.closeMdsDB();

        if (isActivityFound) {
            new UpdateDataDialog(NotesActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @OnClick
            (R.id.addResultsBtn)

    public void addResultsBtn() {

        startActivity(new Intent(NotesActivity.this, AddNotesActivity.class));
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

    @Override
    public void onFinish() {

    }

    private class TreatMentAdapter extends BaseAdapter {
        Context context;
        ArrayList<NotesWrapper> rowItems;

        public TreatMentAdapter(Context context,
                                ArrayList<NotesWrapper> arrayList) {
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
                            dbAdapter.deleteNotes(String.valueOf(rowItems.get(position).getNrowid()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),NotesActivity.this);
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
            holder.treatmentTxt.setText(rowItems.get(position).getTopic());
            holder.treatmentTxt.setVisibility(View.VISIBLE);
            holder.dateTxt.setText(rowItems.get(position).getDate());

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    showCarGiverDialog(rowItems.get(position), "Update Caregiver");

//                    mHomeWatcher.stopWatch();
                    startActivity(new Intent(NotesActivity.this, AddNotesActivity.class)
                            .putExtra("id", rowItems.get(position).getNrowid()));
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

}
