package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.AddIpssContactWrapper;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddIpssActivity extends AppCompatActivity implements OnFinishActivity{
    ListView listView;
    Button addcontactBtn;
    @Bind(R.id.top_row)
    LinearLayout topRow;
    private Toolbar toolbar;
    String title = "";
    DBAdapter dbAdapter;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ipss_score_layout);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        listView = (ListView) findViewById(R.id.listView);
        addcontactBtn = (Button) findViewById(R.id.addcontactBtn);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.mipmap.icn_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(title);
        addcontactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AddIpssActivity.this, AddIpssContactActivity.class).putExtra("id", "-1"));
            }
        });
        mHomeWatcher = new HomeWatcher(AddIpssActivity.this);
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
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        fillList();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(AddIpssActivity
                    .this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    private void fillList() {
        dbAdapter = new DBAdapter(AddIpssActivity.this);
        dbAdapter.openMdsDB();
        if (dbAdapter.getIPSSScore().size() == 0) {
            topRow.setVisibility(View.GONE);
        } else {
            topRow.setVisibility(View.VISIBLE);

        }
        ArrayList<AddIpssContactWrapper> list = dbAdapter.getIPSSScore();
        Collections.reverse(list);
        IPSSAdapter adapter = new IPSSAdapter(AddIpssActivity.this, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onFinish() {

    }

    private class IPSSAdapter extends BaseAdapter {
        Context context;
        ArrayList<AddIpssContactWrapper> rowItems;

        public IPSSAdapter(Context context,
                           ArrayList<AddIpssContactWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
        }

        /* private view holder class */
        private class ViewHolder {
            TextView dateTxt, score;
            ImageView delete_btn;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.ipss_list_item,
                        null);
                holder = new ViewHolder();
                holder.dateTxt = (TextView) convertView
                        .findViewById(R.id.dateTxt);
                holder.score = (TextView) convertView
                        .findViewById(R.id.score);
                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.score.setText(rowItems.get(position).getIpss_score());
            holder.dateTxt.setText(rowItems.get(position).getDate());
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteIPSSScore(String.valueOf(rowItems.get(position).getId()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),AddIpssActivity.this);
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
                    DataManager.getInstance().setAddIpssContactWrapper(rowItems.get(position));
                    startActivity(new Intent(AddIpssActivity.this, AddIpssContactActivity.class)
                            .putExtra("id", rowItems.get(position).getId()));
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
    public void onBackPressed() {
        super.onBackPressed();
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
