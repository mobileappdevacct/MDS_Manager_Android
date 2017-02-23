package com.mymdsmanager.datacontrollers;

/**
 * Created by nitin on 22/7/15.
 */
public class MedicineWrapper {

    String drugName = "", genericName = "",
            dosage = "";


    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
}
