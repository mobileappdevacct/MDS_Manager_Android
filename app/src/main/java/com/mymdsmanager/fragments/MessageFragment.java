package com.mymdsmanager.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.task.CompleteListener;
import com.mymdsmanager.task.GetDataTask;
import com.mymdsmanager.wrapper.MessagesWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nitin on 20/7/15.
 */
public class MessageFragment extends AppCompatActivity {


    @Bind(R.id.messageLayout)
    ListView messageLayout;
    private ProgressDialog dialog;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_messages);
        ButterKnife.bind(this);
        getTrials();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.mipmap.icn_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Message");
    }


    public void getTrials() {
        try {

            if (!MyApplication.isConnectingToInternet(MessageFragment.this)) {
                MyApplication.ShowMassage(MessageFragment.this,
                        "Please connect to working Internet connection!");
                return;
            } else {

                dialog = ProgressDialog.show(MessageFragment.this, "", "Fetching Data....");

                System.out.println("deviceid"+MyApplication.getDeviceID());
                new GetDataTask(MessageFragment.this, "notification.php?device_id=" + MyApplication.getDeviceID(),
                        new CompleteListener() {

                            @Override
                            public void onRemoteErrorOccur(Object error) {
                                dialog.dismiss();
                                // actionBarFragment.mHandler
                                // .removeCallbacks(actionBarFragment.mRunnable);

                            }

                            @Override
                            public void onRemoteCallComplete(Object result) {
                                dialog.dismiss();

                                try {
                                    JSONObject object = new JSONObject(result.toString());

                                    ArrayList<MessagesWrapper> wrapperList = new ArrayList<MessagesWrapper>();
                                    JSONArray array = object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject inObject = array.getJSONObject(i);

                                        MessagesWrapper wrappper = new MessagesWrapper();
                                        wrappper.setCreatedon(inObject.getString("createdon"));

                                        wrappper.setNotification(inObject.getString("notification"));
                                        wrapperList.add(wrappper);
                                    }

                                    MessageAdapter adapter = new MessageAdapter(MessageFragment.this, wrapperList);
                                    messageLayout.setAdapter(adapter);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }).execute();

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class MessageAdapter extends BaseAdapter {
        Context context;
        ArrayList<MessagesWrapper> rowItems;

        public MessageAdapter(Context context,
                              ArrayList<MessagesWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
        }

        /* private view holder class */
        private class ViewHolder {
            TextView timeTxt, messageTxt;

        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_message_item,
                        null);
                holder = new ViewHolder();
                holder.timeTxt = (TextView) convertView
                        .findViewById(R.id.timeTxt);
                holder.messageTxt = (TextView) convertView
                        .findViewById(R.id.messageTxt);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.timeTxt.setText(rowItems.get(position).getCreatedon());
            holder.messageTxt.setText(rowItems.get(position).getNotification());


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
        finish();
    }
}
