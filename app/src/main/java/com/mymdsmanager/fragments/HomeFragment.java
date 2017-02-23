package com.mymdsmanager.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mymdsmanager.R;
import com.mymdsmanager.activities.AddIpssActivity;
import com.mymdsmanager.activities.AddResultsActivity;
import com.mymdsmanager.activities.AdditionalResourcesActivity;
import com.mymdsmanager.activities.CalenderActivity;
import com.mymdsmanager.activities.ClinicalActivity;
import com.mymdsmanager.activities.InsuranceActivity;
import com.mymdsmanager.activities.LabResultActivity;
import com.mymdsmanager.activities.MainActivity;
import com.mymdsmanager.activities.MedicalProfessionalsActivity;
import com.mymdsmanager.activities.MedicationActivity;
import com.mymdsmanager.activities.NotesActivity;
import com.mymdsmanager.activities.ProfileActivity;
import com.mymdsmanager.activities.SearchActivity;
import com.mymdsmanager.activities.SymptomTrackerActivity;
import com.mymdsmanager.activities.UpdateDataDialog;
import com.mymdsmanager.datacontrollers.Constants;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragment extends Fragment {


    @Bind(R.id.menuSpinner)
    Spinner menuSpinner;
    @Bind(R.id.symptomTrackerTxt)
    TextView symptomTrackerTxt;
    @Bind(R.id.medicineTxt)
    TextView medicineTxt;
    @Bind(R.id.calenderTxt)
    TextView calenderTxt;
    @Bind(R.id.notesTxt)
    TextView notesTxt;
    @Bind(R.id.insuranceTxt)
    TextView insuranceTxt;
    @Bind(R.id.medicalProfTxt)
    TextView medicalProfTxt;
    @Bind(R.id.treatmentTxt)
    TextView treatmentTxt;
    @Bind(R.id.transfusionsTxt)
    TextView transfusionsTxt;
    @Bind(R.id.clinicalTrialsTxt)
    TextView clinicalTrialsTxt;
    @Bind(R.id.additionalResourcesTxt)
    TextView additionalResourcesTxt;
    @Bind(R.id.profileTxt)
    TextView profileTxt;
    @Bind(R.id.initialLabResultTxt)
    TextView initialLabResultTxt;
    @Bind(R.id.boneMarrowTxt)
    TextView boneMarrowTxt;
    @Bind(R.id.bloodCountTxt)
    TextView bloodCountTxt;
    @Bind(R.id.treatmentsInnerTxt)
    TextView treatmentsInnerTxt;
    @Bind(R.id.ongoingLabResultsTxt)
    TextView ongoingLabResultsTxt;

    @Bind(R.id.treatmentContainer)
    LinearLayout treatmentContainer;
    @Bind(R.id.ipss_r_score_Txt)
    TextView ipssRScoreTxt;
    @Bind(R.id.transfusionssTxt)
    TextView transfusionssTxt;
    HomeWatcher mHomeWatcher;
    boolean isActivityFound = false;
    private String[] menuArray = new String[]{"Medical Professionals", "Symptom Tracker Notes", "Medicine", "Notes Section"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, null);

        getUiComponents(v);
        ButterKnife.bind(this, v);
        ((MainActivity) getActivity()).toolbar.setTitle(getString(R.string.main_activity_title));
//        Intent i = new Intent(getActivity(), DataUploadService.class);
//        getActivity().startService(i);
        mHomeWatcher = new HomeWatcher(getActivity());
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

        return v;


    }

    @Override
    public void onResume() {
        super.onResume();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(getActivity(), R.style.Dialog);
            isActivityFound = false;
        }
    }

    private void getUiComponents(View v) {

        Button searchBtn = (Button) v.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));

            }
        });
        menuSpinner = (Spinner) v.findViewById(R.id.menuSpinner);
        ArrayAdapter<String> ageGroupAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, R.id.textview, menuArray);
//        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    if (!TextUtils.isEmpty(searchEdt.getText().toString()))
//                        startActivity(new Intent(getActivity(), SearchActivity.class).putExtra("search", searchEdt.getText().toString()));
//                    else
//                        Toast.makeText(getActivity(), "Seached keyword cannot be empty", Toast.LENGTH_LONG).show();
//                    return true;
//                }
//                return false;
//            }
//        });
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


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick(R.id.treatmentTxt)
    public void treatmentTxt() {

        if (treatmentContainer.getVisibility() == View.VISIBLE) {
            treatmentTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.icn_treatment, 0, R.mipmap.icn_expand, 0);
            treatmentContainer.setVisibility(View.GONE);
        } else {
            treatmentTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.icn_treatment, 0, R.mipmap.icn_collapse, 0);

            treatmentContainer.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.treatmentsInnerTxt)
    public void treatmentsInnerTxt() {


        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), AddResultsActivity.class).putExtra("title", "MDS Treatments"));
    }

    @OnClick(R.id.ipss_r_score_Txt)
    public void ipss_r_score_Txt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), AddIpssActivity.class).putExtra("title", "IPSS-R Score"));
    }

    @OnClick(R.id.medicalProfTxt)
    public void medicalProfTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), MedicalProfessionalsActivity.class));
    }

    @OnClick(R.id.initialLabResultTxt)
    public void initialLabResultTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), LabResultActivity.class).putExtra("title", "Initial Lab Results"));
    }

    @OnClick(R.id.ongoingLabResultsTxt)
    public void ongoingLabResultsTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), LabResultActivity.class).putExtra("title", "Ongoing Lab Results"));
    }

    @OnClick(R.id.notesTxt)
    public void notesTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), NotesActivity.class));
    }

    @OnClick(R.id.calenderTxt)
    public void calenderTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), CalenderActivity.class));
    }

    @OnClick(R.id.insuranceTxt)
    public void insuranceTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), InsuranceActivity.class));
    }

    @OnClick(R.id.medicineTxt)
    public void medicineTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), MedicationActivity.class));
    }

    @OnClick(R.id.profileTxt)
    public void profileTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    @OnClick(R.id.symptomTrackerTxt)
    public void symptomTrackerTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), SymptomTrackerActivity.class));
    }

    @OnClick(R.id.transfusionsTxt)
    public void transfusionsTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), AddResultsActivity.class).putExtra("title", "Transfusions"));
    }
    @OnClick(R.id.transfusionssTxt)
    public void transfusionssTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), AddResultsActivity.class).putExtra("title", "Transfusions"));
    }
    @OnClick(R.id.bloodCountTxt)
    public void bloodCountTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), AddResultsActivity.class).putExtra("title", "Blood Counts"));
    }

    @OnClick(R.id.boneMarrowTxt)
    public void boneMarrowTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), AddResultsActivity.class).putExtra("title", "Bone Marrow Results"));
    }

    @OnClick(R.id.clinicalTrialsTxt)
    public void clinicalTrialsTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), ClinicalActivity.class));
    }

    @OnClick(R.id.additionalResourcesTxt)
    public void additionalResourcesTxt() {
        mHomeWatcher.stopWatch();
        startActivity(new Intent(getActivity(), AdditionalResourcesActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
