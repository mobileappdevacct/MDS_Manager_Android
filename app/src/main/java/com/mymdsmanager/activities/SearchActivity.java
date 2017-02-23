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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.Constants;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.MedicalProfessionalWrapper;
import com.mymdsmanager.wrapper.MedicineInfoWrapper;
import com.mymdsmanager.wrapper.NotesWrapper;
import com.mymdsmanager.wrapper.SymptomDetailWrapper;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
public class SearchActivity extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.dateTxt)
    TextView dateTxt;
    @Bind(R.id.symptomTxt)
    TextView symptomTxt;
    @Bind(R.id.severityTxt)
    TextView severityTxt;
    @Bind(R.id.searchedListView)
    ListView searchedListView;
    @Bind(R.id.container)
    LinearLayout container;
    @Bind(R.id.menuSpinner)
    Spinner menuSpinner;
    @Bind(R.id.searchEdt)
    EditText searchEdt;
    private String search = "";
    private DBAdapter dbAdapter;
    Toolbar toolbar;
    HomeWatcher mHomeWatcher;
    private String[] menuArray = new String[]{"Where would you like to search?","Medical Professionals","Medicine", "Notes Section","Symptom Tracker Notes"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        ArrayAdapter<String> ageGroupAdapter = new ArrayAdapter<String>(SearchActivity.this,
                R.layout.spinner_item, R.id.textview, menuArray);
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(searchEdt.getText().toString())) {
                        MyApplication.getApplication().hideSoftKeyBoard(SearchActivity.this);
                        search = searchEdt.getText().toString();
                        setData();
                    } else
                        Toast.makeText(SearchActivity.this, "Seached keyword cannot be empty", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Constants.searched_type = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        menuSpinner.setAdapter(ageGroupAdapter);


        dbAdapter = new DBAdapter(SearchActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.mipmap.icn_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Search Results");
         mHomeWatcher = new HomeWatcher(SearchActivity.this);
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

    @Override
    public void onBackPressed() {

        MyApplication.getApplication().hideSoftKeyBoard(SearchActivity.this);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(SearchActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    private void setData() {
        try {
            if (Constants.searched_type == Constants.SYMPTOM_TRACKER) {
                container.setVisibility(View.VISIBLE);
                dateTxt.setText("Date");
                symptomTxt.setText("Symptom");
                severityTxt.setText("Severity");
                dbAdapter.openMdsDB();
                ArrayList<SymptomDetailWrapper> arrayList = dbAdapter.getSearchedSymtomList(search);
                SymptomAdapter adapter = new SymptomAdapter(SearchActivity.this, arrayList);
                searchedListView.setAdapter(adapter);
                dbAdapter.closeMdsDB();
            } else if (Constants.searched_type == Constants.MEDICINE) {
                container.setVisibility(View.VISIBLE);
                dateTxt.setText("Drug Name");
                symptomTxt.setText("Generic Name");
                severityTxt.setText("Dosage");
                dbAdapter.openMdsDB();
                MedicineAdapter diagnosisAdapter = new MedicineAdapter(SearchActivity.this, dbAdapter.getMedicineSearched(search));
                searchedListView.setAdapter(diagnosisAdapter);
                dbAdapter.closeMdsDB();
            } else if (Constants.searched_type == Constants.MEDICAL_PROFESSIONAL) {
                container.setVisibility(View.GONE);
                dateTxt.setText("Date");
                symptomTxt.setText("Symptom");
                severityTxt.setText("Severity");
                dbAdapter.openMdsDB();
                MedicalProfessionalAdapter diagnosisAdapter = new MedicalProfessionalAdapter(SearchActivity.this, dbAdapter.getMedicalProfessionalSearchedWrapper(search));
                searchedListView.setAdapter(diagnosisAdapter);
                dbAdapter.closeMdsDB();
            } else if (Constants.searched_type == Constants.NOTES) {
                container.setVisibility(View.VISIBLE);
                dateTxt.setText("Date");
                symptomTxt.setText("Topic");
                severityTxt.setText("Notes");
                dbAdapter.openMdsDB();
                TreatMentAdapter adapter = new TreatMentAdapter(SearchActivity.this, dbAdapter.getNotesSearchedList(search));
                searchedListView.setAdapter(adapter);
                dbAdapter.closeMdsDB();
            }else if (Constants.searched_type == 0) {
                Toast.makeText(SearchActivity.this, "Please select search category before serach!", Toast.LENGTH_LONG).show();
            }
            if (searchedListView.getAdapter().getCount() == 0) {
                container.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, "No searched data found", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            holder.phoneTxt.setText(rowItems.get(position).getPhone());
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
                            new UpdateOnClass(MyApplication.getApplication(),SearchActivity.this);
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
                    startActivity(new Intent(SearchActivity.this, AddMedicalProfessionalActivity.class).putExtra("update", "update").putExtra("model", rowItems.get(position)));


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

            holder.treatmentTxt.setText(rowItems.get(position).getDate());
            holder.dateTxt.setText(rowItems.get(position).getTopic());
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
                            new UpdateOnClass(MyApplication.getApplication(),SearchActivity.this);
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


                    startActivity(new Intent(SearchActivity.this, AddNotesActivity.class)
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

    public class MedicineAdapter extends BaseAdapter {
        Context context;
        List<MedicineInfoWrapper> rowItems;

        public MedicineAdapter(Context context, List<MedicineInfoWrapper> items) {
            this.context = context;
            this.rowItems = items;
        }

        /*private view holder class*/
        private class ViewHolder {

            TextView dosageTxt, genericNameTxt, drugNameTxt;
            ImageView delete_btn;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_medicine_item, null);
                holder = new ViewHolder();
                holder.dosageTxt = (TextView) convertView.findViewById(R.id.dosageTxt);
                holder.genericNameTxt = (TextView) convertView.findViewById(R.id.genericNameTxt);
                holder.drugNameTxt = (TextView) convertView.findViewById(R.id.drugNameTxt);
                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MedicineInfoWrapper rowItem = (MedicineInfoWrapper) getItem(position);

            holder.dosageTxt.setText(rowItem.getDosage());
            holder.genericNameTxt.setText(rowItem.getGenericname());
            holder.drugNameTxt.setText(rowItem.getDrugname());
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteMedicineInfoMap(String.valueOf(rowItems.get(position).getId()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),SearchActivity.this);
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
            return rowItems.indexOf(getItem(position));
        }
    }

    private class SymptomAdapter extends BaseAdapter {
        Context context;
        ArrayList<SymptomDetailWrapper> rowItems;

        public SymptomAdapter(Context context,
                              ArrayList<SymptomDetailWrapper> arrayList) {
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
                holder.delete_btn.setVisibility(View.VISIBLE);
                holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("'Are you sure you want to delete this item?")
                                .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbAdapter.openMdsDB();
                                dbAdapter.deleteSYMPTOINFO(String.valueOf(rowItems.get(position).getSrowid()));
                                rowItems.remove(position);
                                new UpdateOnClass(MyApplication.getApplication(),SearchActivity.this);
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
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.descriptionTxt.setText(rowItems.get(position).getSymptomname());
            holder.dateTxt.setText(rowItems.get(position).getSymptomdate());
            holder.boneBlastTxt.setText(rowItems.get(position).getSeverity());


            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    showCarGiverDialog(rowItems.get(position), "Update Caregiver");

                    startActivity(new Intent(SearchActivity.this, SymptomTrackerActivity.class).putExtra("id", rowItems.get(position).getSrowid()));
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
