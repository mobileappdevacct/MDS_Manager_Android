package com.mymdsmanager.wrapper;

public class ClinicalWrapper {
    String trialNumber = "";
    String name_ofTrial = "";
    String location = "";
    String treatementid = "";

    public String getTreatementid() {
        return treatementid;
    }

    public void setTreatementid(String treatementid) {
        this.treatementid = treatementid;
    }


    public String getTrialNumber() {
        return trialNumber;
    }

    public void setTrialNumber(String trialNumber) {
        this.trialNumber = trialNumber;
    }

    public String getName_ofTrial() {
        return name_ofTrial;
    }

    public void setName_ofTrial(String name_ofTrial) {
        this.name_ofTrial = name_ofTrial;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
