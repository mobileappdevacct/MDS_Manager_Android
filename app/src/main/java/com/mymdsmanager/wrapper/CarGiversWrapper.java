package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 5/9/15.
 */
public class CarGiversWrapper {


    public String getCaregivername() {
        return caregivername;
    }

    public void setCaregivername(String caregivername) {
        this.caregivername = caregivername;
    }

    public String getCaregivercontact() {
        return caregivercontact;
    }

    public void setCaregivercontact(String caregivercontact) {
        this.caregivercontact = caregivercontact;
    }

    public String getCaregiverrelation() {
        return caregiverrelation;
    }

    public void setCaregiverrelation(String caregiverrelation) {
        this.caregiverrelation = caregiverrelation;
    }

    private String caregivername = "";
    private String caregivercontact = "";
    private String caregiverrelation = "";
    private String caregiveremail="";

    public String getCaregiveremail() {
        return caregiveremail;
    }

    public void setCaregiveremail(String caregiveremail) {
        this.caregiveremail = caregiveremail;
    }


}
