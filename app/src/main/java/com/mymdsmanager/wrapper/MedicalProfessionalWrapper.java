package com.mymdsmanager.wrapper;

import java.io.Serializable;

/**
 * Created by nitin on 5/9/15.
 */
public class MedicalProfessionalWrapper implements Serializable {
    private String providername = "";
    private String providerspeciality = "";
    private String referredby = "";
    private String address = "";
    private String phone = "";
    private String fax = "";
    private String email = "";
    private String countrycode="";
    int id;

    public String getProvidername() {
        return providername;
    }

    public void setProvidername(String providername) {
        this.providername = providername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReferredby() {
        return referredby;
    }

    public void setReferredby(String referredby) {
        this.referredby = referredby;
    }

    public String getProviderspeciality() {
        return providerspeciality;
    }

    public void setProviderspeciality(String providerspeciality) {
        this.providerspeciality = providerspeciality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }


    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
