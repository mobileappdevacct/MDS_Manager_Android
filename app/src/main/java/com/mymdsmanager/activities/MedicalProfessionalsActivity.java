package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.mymdsmanager.wrapper.MedicalProfessionalWrapper;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nitin on 5/9/15.
 */
public class MedicalProfessionalsActivity extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.professionalsListView)
    ListView professionalsListView;
    @Bind(R.id.addContactBtn)
    Button addContactBtn;
    @Bind(R.id.addExportLayout)
    LinearLayout addExportLayout;
    private DBAdapter dbAdapter;
    private Toolbar toolbar;
    HomeWatcher mHomeWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_professional);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
//        exportContact("", userNameEdt.getText().toString(), homeCellEdt.getText().toString(), emailIdEdt.getText().toString());
        toolbar.setNavigationIcon(R.mipmap.icn_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        getSupportActionBar().setTitle("Medical Professionals");

        dbAdapter = new DBAdapter(MedicalProfessionalsActivity.this);
        mHomeWatcher = new HomeWatcher(MedicalProfessionalsActivity.this);
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

        finish();
    }

    @OnClick(R.id.addContactBtn)
    public void addContactBtn() {

        startActivity(new Intent(MedicalProfessionalsActivity.this, AddMedicalProfessionalActivity.class));
    }


    private void showProfessionalList() {
        dbAdapter.openMdsDB();

        ArrayList<MedicalProfessionalWrapper> wrapperList = dbAdapter.getMedicalProfessionalWrapper();
        Collections.reverse(wrapperList);
        MedicalProfessionalAdapter adapter = new MedicalProfessionalAdapter(MedicalProfessionalsActivity.this, wrapperList);
        professionalsListView.setAdapter(adapter);
        dbAdapter.closeMdsDB();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomeWatcher.startWatch();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        showProfessionalList();
        if (isActivityFound) {
            new UpdateDataDialog(MedicalProfessionalsActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @Override
    public void onFinish() {

    }

    private class MedicalProfessionalAdapter extends BaseAdapter {
        Context context;
        ArrayList<MedicalProfessionalWrapper> rowItems;

        public MedicalProfessionalAdapter(Context context,
                                          ArrayList<MedicalProfessionalWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
        }

        /* private view holder class */
        private class ViewHolder {
            TextView phoneTxt, specialityTxt, nameTxt;
            ImageView delete_btn;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_medical_professional_item,
                        null);
                holder = new ViewHolder();
                holder.nameTxt = (TextView) convertView
                        .findViewById(R.id.nameTxt);
                holder.phoneTxt = (TextView) convertView
                        .findViewById(R.id.phoneTxt);
                holder.specialityTxt = (TextView) convertView
                        .findViewById(R.id.specialityTxt);
                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.nameTxt.setText(rowItems.get(position).getProvidername());
            if (!TextUtils.isEmpty(rowItems.get(position).getPhone())) {
                holder.phoneTxt.setText(rowItems.get(position).getPhone());
            }else
            {
                holder.phoneTxt.setText("Contact Info not available");
            }

            holder.specialityTxt.setText(rowItems.get(position).getProviderspeciality());
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteMedicalProfessional(rowItems.get(position).getId());
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),MedicalProfessionalsActivity.this);
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

                    startActivity(new Intent(MedicalProfessionalsActivity.this, AddMedicalProfessionalActivity.class).putExtra("update", "update").putExtra("model", rowItems.get(position)));

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
