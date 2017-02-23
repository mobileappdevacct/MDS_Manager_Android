package com.mymdsmanager.datacontrollers;


import com.mymdsmanager.wrapper.AddIpssContactWrapper;
import com.mymdsmanager.wrapper.BloodCountResultWrapper;
import com.mymdsmanager.wrapper.BoneMarrowResultWrapper;
import com.mymdsmanager.wrapper.ReminderWrapper;
import com.mymdsmanager.wrapper.TreatmentInfoWrapper;
import com.mymdsmanager.wrapper.TreatmentMedicineInfoWrapper;

import java.util.ArrayList;


public class DataManager {


    private ArrayList<MedicineWrapper> presciptionList = new ArrayList<>();
    private ArrayList<MedicineWrapper> overTheCounterList = new ArrayList<>();
    private ArrayList<MedicineWrapper> subscriptionList = new ArrayList<>();
    private ArrayList<AddIpssContactWrapper> addIpssContactWrappers = new ArrayList<>();
    ArrayList<String> symtomsubArrayList = new ArrayList<>();
    ArrayList<String> praticalsubArraylist = new ArrayList<>();
    ArrayList<String> frequencyArraylist = new ArrayList<>();
    ArrayList<String> refill_frequencyArraylist = new ArrayList<>();
    ArrayList<String> marital_statusArraylist = new ArrayList<>();
    ArrayList<String> living_statusArraylist = new ArrayList<>();
    ArrayList<String> units_Arraylist = new ArrayList<>();
    ArrayList<String> diagnosis_Arraylist = new ArrayList<>();
    ArrayList<String> diabnosis_testArraylist = new ArrayList<>();
    ArrayList<String> diabnosis_testUnitsArraylist = new ArrayList<>();

    public ArrayList<String> getMds_treatment_medineArr() {
        return mds_treatment_medineArr;
    }

    public void setMds_treatment_medineArr(ArrayList<String> mds_treatment_medineArr) {
        this.mds_treatment_medineArr = mds_treatment_medineArr;
    }

    ArrayList<String> mds_treatment_medineArr = new ArrayList<>();
    ReminderWrapper reminderWrapper = new ReminderWrapper();

    public ArrayList<String> getDiabnosis_testArraylist() {
        return diabnosis_testArraylist;
    }

    public void setDiabnosis_testArraylist(ArrayList<String> diabnosis_testArraylist) {
        this.diabnosis_testArraylist = diabnosis_testArraylist;
    }

    public ArrayList<String> getDiabnosis_testUnitsArraylist() {
        return diabnosis_testUnitsArraylist;
    }

    public void setDiabnosis_testUnitsArraylist(ArrayList<String> diabnosis_testUnitsArraylist) {
        this.diabnosis_testUnitsArraylist = diabnosis_testUnitsArraylist;
    }
//    public ArrayList<TreatmentMedicineInfoWrapper> getTreatmentMedicineInfoWrappers() {
//        return treatmentMedicineInfoWrappers;
//    }
//
//    public void setTreatmentMedicineInfoWrappers(ArrayList<TreatmentMedicineInfoWrapper> treatmentMedicineInfoWrappers) {
//        this.treatmentMedicineInfoWrappers = treatmentMedicineInfoWrappers;
//    }

    public ArrayList<TreatmentMedicineInfoWrapper> treatmentMedicineInfoWrappers = new ArrayList<>();
    public ArrayList<TreatmentMedicineInfoWrapper> updatetreatmentMedicineInfoWrappers = new ArrayList<>();
    public TreatmentMedicineInfoWrapper getTreatmentMedicineInfoWrapper() {
        return treatmentMedicineInfoWrapper;
    }

    public void setTreatmentMedicineInfoWrapper(TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper) {
        this.treatmentMedicineInfoWrapper = treatmentMedicineInfoWrapper;
    }

    TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper =null;

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    String provider_name ="";
    public ReminderWrapper getReminderWrapper() {
        return reminderWrapper;
    }

    public void setReminderWrapper(ReminderWrapper reminderWrapper) {
        this.reminderWrapper = reminderWrapper;
    }

    public ArrayList<String> getFrequencyArraylist() {
        return frequencyArraylist;
    }

    public void setFrequencyArraylist(ArrayList<String> frequencyArraylist) {
        this.frequencyArraylist = frequencyArraylist;
    }

    public ArrayList<String> getRefill_frequencyArraylist() {
        return refill_frequencyArraylist;
    }

    public void setRefill_frequencyArraylist(ArrayList<String> refill_frequencyArraylist) {
        this.refill_frequencyArraylist = refill_frequencyArraylist;
    }

    public ArrayList<String> getMarital_statusArraylist() {
        return marital_statusArraylist;
    }

    public void setMarital_statusArraylist(ArrayList<String> marital_statusArraylist) {
        this.marital_statusArraylist = marital_statusArraylist;
    }

    public ArrayList<String> getLiving_statusArraylist() {
        return living_statusArraylist;
    }

    public void setLiving_statusArraylist(ArrayList<String> living_statusArraylist) {
        this.living_statusArraylist = living_statusArraylist;
    }

    public ArrayList<String> getUnits_Arraylist() {
        return units_Arraylist;
    }

    public void setUnits_Arraylist(ArrayList<String> units_Arraylist) {
        this.units_Arraylist = units_Arraylist;
    }

    public ArrayList<String> getDiagnosis_Arraylist() {
        return diagnosis_Arraylist;
    }

    public void setDiagnosis_Arraylist(ArrayList<String> diagnosis_Arraylist) {
        this.diagnosis_Arraylist = diagnosis_Arraylist;
    }

    public static DataManager getdManager() {
        return dManager;
    }

    public static void setdManager(DataManager dManager) {
        DataManager.dManager = dManager;
    }

    public ArrayList<String> getSymtomsubArrayList() {
        return symtomsubArrayList;
    }

    public void setSymtomsubArrayList(ArrayList<String> symtomsubArrayList) {
        this.symtomsubArrayList = symtomsubArrayList;
    }

    public ArrayList<String> getPraticalsubArraylist() {
        return praticalsubArraylist;
    }

    public void setPraticalsubArraylist(ArrayList<String> praticalsubArraylist) {
        this.praticalsubArraylist = praticalsubArraylist;
    }

    AddIpssContactWrapper addIpssContactWrapper = new AddIpssContactWrapper();

    public AddIpssContactWrapper getAddIpssContactWrapper() {
        return addIpssContactWrapper;
    }

    public void setAddIpssContactWrapper(AddIpssContactWrapper addIpssContactWrapper) {
        this.addIpssContactWrapper = addIpssContactWrapper;
    }

    public ArrayList<String> getReminderTimeList() {
        return reminderTimeList;
    }

    public void setReminderTimeList(ArrayList<String> reminderTimeList) {
        this.reminderTimeList = reminderTimeList;
    }

    private ArrayList<String> reminderTimeList=new ArrayList<>();

    public ArrayList<MedicineWrapper> getPresciptionList() {
        return presciptionList;
    }


    public TreatmentInfoWrapper getTreatmentInfoWrapper() {
        return treatmentInfoWrapper;
    }

    public void setTreatmentInfoWrapper(TreatmentInfoWrapper treatmentInfoWrapper) {
        this.treatmentInfoWrapper = treatmentInfoWrapper;
    }

    public BoneMarrowResultWrapper getBoneMarrowResultWrapper() {
        return boneMarrowResultWrapper;
    }

    public void setBoneMarrowResultWrapper(BoneMarrowResultWrapper boneMarrowResultWrapper) {
        this.boneMarrowResultWrapper = boneMarrowResultWrapper;
    }

    public BloodCountResultWrapper getBloodCountResultWrapper() {
        return bloodCountResultWrapper;
    }

    public void setBloodCountResultWrapper(BloodCountResultWrapper bloodCountResultWrapper) {
        this.bloodCountResultWrapper = bloodCountResultWrapper;
    }

    private TreatmentInfoWrapper treatmentInfoWrapper = new TreatmentInfoWrapper();
    private BoneMarrowResultWrapper boneMarrowResultWrapper = new BoneMarrowResultWrapper();
    private BloodCountResultWrapper bloodCountResultWrapper = new BloodCountResultWrapper();


    public void setPresciptionList(ArrayList<MedicineWrapper> presciptionList) {
        this.presciptionList = presciptionList;
    }

    public ArrayList<MedicineWrapper> getOverTheCounterList() {
        return overTheCounterList;
    }

    public void setOverTheCounterList(ArrayList<MedicineWrapper> overTheCounterList) {
        this.overTheCounterList = overTheCounterList;
    }

    public ArrayList<MedicineWrapper> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(ArrayList<MedicineWrapper> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    public static DataManager dManager;

    public static DataManager getInstance() {
        if (dManager == null)
            dManager = new DataManager();
        return dManager;
    }
}
