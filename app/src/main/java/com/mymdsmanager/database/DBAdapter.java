package com.mymdsmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.mymdsmanager.R;
import com.mymdsmanager.wrapper.AddIpssContactWrapper;
import com.mymdsmanager.wrapper.AllergyWrapper;
import com.mymdsmanager.wrapper.AppointmentsWrapper;
import com.mymdsmanager.wrapper.BloodCountResultWrapper;
import com.mymdsmanager.wrapper.BoneMarrowResultWrapper;
import com.mymdsmanager.wrapper.CarGiversWrapper;
import com.mymdsmanager.wrapper.ClinicalWrapper;
import com.mymdsmanager.wrapper.DiagnosisWrapper;
import com.mymdsmanager.wrapper.InsuranceWrapper;
import com.mymdsmanager.wrapper.LabResultInfoWrapper;
import com.mymdsmanager.wrapper.MedicalProfessionalWrapper;
import com.mymdsmanager.wrapper.MedicineInfoWrapper;
import com.mymdsmanager.wrapper.NotesWrapper;
import com.mymdsmanager.wrapper.OtherResultWrapper;
import com.mymdsmanager.wrapper.SymptomDetailWrapper;
import com.mymdsmanager.wrapper.SymptomWrapper;
import com.mymdsmanager.wrapper.Transfusionwrapper;
import com.mymdsmanager.wrapper.TreatmentInfoWrapper;
import com.mymdsmanager.wrapper.TreatmentMedicineInfoWrapper;
import com.mymdsmanager.wrapper.UserDetailsWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DBAdapter {


    private Context context;

    //-------------------------Table Country Code----------
    public static final String GLOBALAREACODES = "globalareacodes";
    public static final String COUNTRY = "Country";
    public static final String COUNTRYCODE = "CountryCode";

//    --------------- Table Insurance Detail --------------


    public static final String TABLE_INSURANCE_DETAIL = "InsuranceDetail";
    public static final String IROWID = "irowid";
    public static final String COMPANYOPTION = "companyoption";
    public static final String COMPANYNAME = "companyname";
    public static final String PHONENO = "phoneno";
    public static final String EMPLOYER = "employer";
    public static final String COMPANYGROUP = "companygroup";
    public static final String PRESCRIPTION = "prescription";
    public static final String ADDRESS = "address";
    public static final String I_CITY = "city";
    public static final String I_STATE = "state";
    public static final String I_ZIPCODE = "zipcode";
    public static final String COMPANYTYPE = "companytype";
    public static final String COMPANYIMAGENAME = "companyimagename";


    //    -------------- User Info -----------------
    public static final String TABLE_USERINFO = "userinfo";
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String BLOODGROUP = "bloodgroup";
    public static final String DOB = "dob";
    public static final String MARITALSTATUS = "maritalstatus";
    public static final String LIVINGSTATUS = "livingstatus";
    public static final String SSN = "ssn";
    public static final String ADDRESSLINE1 = "addressline1";
    public static final String ADDRESSLINE2 = "addressline2";
    public static final String ADDRESSLINE3 = "addressline3";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String ZIPCODE = "zipcode";
    public static final String EMAILID = "emailid";
    public static final String CONTACTHOME = "contacthome";
    public static final String CONTACTWORK = "contactwork";
    public static final String CONTACTCELL = "contactcell";
    public static final String GENDER = "gender";
    public static final String USERCOUNTRYCODE = "countrycodecell";
    public static final String USERCOUNTRYCODEHOME = "countrycodehome";
    public static final String USERCOUNTRYCODEWORK = "countrycodework";
    //    ============= Table Caregivers Info ============
//    caregiveremail
    public static final String TABLE_CAREGIVERSINFO = "caregiversinfo";
    public static final String CAREGIVERNAME = "caregivername";
    public static final String CAREGIVERCONTACT = "caregivercontact";
    public static final String CAREGIVERRELATION = "caregiverrelation";
    public static final String CAREGIVEREMAIL = "caregiveremail";

//================= Table Alergy ================

    public static final String TABLE_ALERGYINFO = "alergyinfo";
    public static final String ALLERGYTYPE = "allergytype";
    public static final String ALLERGYSUBSTANCE = "allergysubstance";


//    ===================== Medical Professional =================

    public static final String TABLE_MEDICAL_PROFESSIONAL = "medicalprovider";

    public static final String PROVIDERNAME = "providername";
    public static final String PROVIDERSPECIALITY = "providerspeciality";
    public static final String REFERREDBY = "referredby";
    public static final String ADDRESS_MP = "address";
    public static final String PHONE_MP = "phone";
    public static final String FAX = "fax";
    public static final String EMAIL_MP = "email";
    public static final String MROWID = "mrowid";
    public static final String COUNTRYODEPROVIDER = "countrycode";
//    ================== Blood Result Count Table ===========

    public static final String TABLE_BLOOD_RESULT = "bloodcountResult";
    public static final String BROID = "broid";

    public static final String DATE_BR = "date";
    public static final String HGB = "hgb";
    public static final String WBC = "wbc";
    public static final String ANC = "anc";
    public static final String PLATELETS = "platelets";
    public static final String DATE_TO_ORDER = "date_to_order";
//    public static final String TRANFUSION = "tranfusion";
    public static final String NOTES_BR = "notes";
    public static final String FERRITIN = "ferritin";
    //    public static final String BLOOD_TYPE = "bloodtype";
    public static final String BLOODNOTES = "bloodnotes";

    public static final String BLOODCOUNTIMAGES = "bloodcountsimages";


    //    ================== Bone Marrow Count Table ===========
    public static final String TABLE_BONE_MARROW = "bonemarrowResult";

    public static final String BROWID = "browid";

    public static final String DATE_BM = "date";
    public static final String MARROWBLAST = "marrowblast";
    public static final String NOTES_BM = "notes";
    public static final String BONEIMAGES = "boneimages";
  //================================Other result=========================

    public static final String TABLE_OTHER_BLOOD_RESULT="otherlabreult";
    public static final String OTHERID="otherrowid";
    public static final String BLOODCOUNTID="blabid";
    public static final String LABTITLE="labtitle";
    public static final String LABVALUE="labvalue";


    //    ===================== TreatMent Info Table =================
    public static final String TABLE_TREATEMENTINFO = "treatementInfo";

    public static final String TROWID = "trowid";

    public static final String TREATEMENT = "treatement";


//    ============ Table Symptom Detail =============

    public static final String TABLE_SYMPTOMINFO = "symptorInfo";

    public static final String SYMPTOMNAME = "symptomname";
    public static final String SEVERITY = "severity";
    public static final String SYMPTOMDATE = "symptomdate";
    public static final String SYMPTOMTIME = "symptomtime";
    public static final String DURATION = "duration";
    //    public static final String CREATEDATE = "createdate";
//    public static final String MODIFIEDDATE = "modifieddate";
    public static final String SYMPTOMSUBCAT = "symptomSubCat";
//    symptomSubCat

//    ============= Table Symptom Info =================

    public static final String TABLE_SYMPTOMSLIST = "symptomslist";

    public static final String SYMPTOM = "symptom";

    public static final String DESCRIPTION = "description";


    //    =============== Table Notes =======================

    public static final String TABLE_NOTESINFO = "notesInfo";


    public static final String NROWID = "nrowid";

    public static final String DATE = "date";
    public static final String TOPIC = "topic";


    //    =============== Diagnosis Table ======================
    public static final String TABLE_MEDICALDIAGNOSISHISTORY = "medicaldiagnosishistory";


    public static final String DROWID = "drowid";
    public static final String DIAGNOSIS = "diagnosis";
    public static final String MANAGINGPROVIDER = "managingprovider";
    public static final String HISTORYTYPE = "historytype";


//    =============== Table Medicine Info ===================


    public static final String TABLE_MEDICINEINFO = "MedicineInfo";

    public static final String DRUGNAME = "drugname";
    public static final String GENERICNAME = "genericname";
    public static final String DOSAGE = "dosage";
    public static final String TYPE = "type";
    public static final String FREQUENCY = "frequency";
    public static final String STARTDATE = "startdate";
    public static final String ENDDATE = "enddate";
    public static final String REFILDATE = "refildate";
    public static final String PRESCIRBEDBY = "prescirbedby";
    public static final String IMAGENAME = "imagename";
    public static final String REFILFREQUENCY = "refilfrequency";
    public static final String STARTTIMESTAMP = "starttimestamp";
    public static final String ENDTIMESTAMP = "endtimestamp";
    public static final String REMINDERSTRING = "reminderstring";
    public static final String RFREQUENCYTITLE = "rfrequencytitle";
    public static final String REMINDERCOUNTS = "remindercounts";
    public static final String REMINDERSTARTTIME = "reminderstarttime";
    public static final String SCHEDULEDAYS = "scheduledays";
    public static final String REMINDERSOUNDFILE = "remindersoundfile";
    public static final String REMINDERSOUNDFILEINDEX = "remindersoundindex";
    public static final String REMINDERIDS = "reminderIDS";
    public static final String MID = "mid";

//    ================== Table Lab Results ====================

    public static final String TABLE_LABRESULTINFO = "labresultinfo";


    public static final String DIAGNOSISTEST = "diagnosistest";
    public static final String RESULTS = "results";
    public static final String UNITS = "units";
    public static final String LABIMAGES = "labimages";
    public static final String LABRESULTTYPE = "labresulttype";
    public static final String LROWID = "lrowid";

    //================== Table IPSS SCORE ================
    public static final String TABLE_IPSSRSCOREINFO = "ipssrscoreInfo";

    public static final String DATEIPSS = "date";
    public static final String IPSSSCORE = "score";
    public static final String IPSSNOTES = "notes";
    public static final String IPROWID = "irowid";

//================== Table Appointments ================

    public static final String TABLE_APPOINTMENTSINFO = "appointmentsInfo";

    public static final String DATENTIME = "dateNtime";
    public static final String PROVIDER = "provider";
    public static final String NOTES = "notes";
    public static final String DATETIMESTAMP = "datetimestamp";

    public static final String AROWID = "arowid";
    public static final String date_search = "eventidentifier";


    //====================Table TreatmentMedicineInfo=================
    public static final String TABLE_TREATMENTMEDICINEINFO = "treatmentMedicineInfo";
    public static final String TREATMENTID = "treatementid";
    public static final String MEDICINENAME = "medicinename";
    public static final String TREATMENTDOSAGE = "dosage";
    public static final String DAYS = "days";
    public static final String CYCLENUMBER = "cyclenumber";
    public static final String TREATMENTNOTES = "notes";
    public static final String TREATMENTDATE = "date";
    public static final String TREATMENTTYPE = "type";
    public static final String OTHERTREATMENTNAME = "othertreatmentname";
    //=============================Table Transfusioninfo====================================
    public static final String TABLE_TRANSFUSIONINFO = "transfusioninfo";
    public static final String TRANSFUSIONDATE = "date";
    public static final String TRANSFUSIONTYPE = "ttype";
    public static final String TRANSFUSIONUNIT = "unit";
    public static final String TRANSFUSIONID = "trowid";
    public static final String TRANSFUSIONBLOOD_TYPE = "blood_type";
    //================================Table Clinical===========================================
    public static final String TABLE_CLINICAL = "clinicaltrial";
    public static final String Treatementid = "treatementid";
    public static final String TRIAL_NUMBER = "trial_number";
    public static final String NAME_OF_TRIAL = "name_of_trial";
    public static final String LOCATION = "location";

    private DatabaseHelperUser DBHelperUser;

    private DatabaseHelperMds DBHelperMds;
    private static final int DATABASE_VERSION = 1;
    // Database Name

    public static final String DATABASE_NAME_MDS = "MDSManagerDB.sqlite";
    public static final String DATABASE_NAME_MDS_ZIP = "MDSManagerDB.zip";

    public static final String DATABASE_NAME_USER = "MDSManager_UserInfo_DB.sqlite";
    public static final String DATABASE_NAME_USER_ZIP = "MDSManager_UserInfo_DB.zip";

    // Contacts table name
    private static final String DB_PATH_SUFFIX = "/databases/";
    private SQLiteDatabase dbMds;
    private SQLiteDatabase dbUser;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelperMds = new DatabaseHelperMds(context);
        DBHelperUser = new DatabaseHelperUser(context);

    }

    public void dropDatabase() {
        context.deleteDatabase(DATABASE_NAME_MDS);
    }

    public void dropuserDatabase() {
        context.deleteDatabase(DATABASE_NAME_USER);
    }

    private class DatabaseHelperMds extends SQLiteOpenHelper {
        DatabaseHelperMds(Context context) {
            super(context, DATABASE_NAME_MDS, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            // dbMds.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE_TIMELINE);

            onCreate(db);
        }
    }

    private class DatabaseHelperUser extends SQLiteOpenHelper {
        DatabaseHelperUser(Context context) {
            super(context, DATABASE_NAME_USER, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            // dbMds.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE_TIMELINE);

            onCreate(db);
        }
    }

    public void CopyUSERDataBaseFromAsset() throws IOException {

        InputStream myInputUser = context.getAssets().open(DATABASE_NAME_USER);

        // Path to the just created empty dbMds
        String outFileNameUser = getUserDatabasePath();

        // if the path doesn't exist first, create it
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty dbMds as the output stream
        OutputStream myOutputUser = new FileOutputStream(outFileNameUser);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInputUser.read(buffer)) > 0) {
            myOutputUser.write(buffer, 0, length);
        }

        // Close the streams
        myOutputUser.flush();
        myOutputUser.close();
        myInputUser.close();
    }

    public void CopyMDSDataBaseFromAsset() throws IOException {

        InputStream myInputMds = context.getAssets().open(DATABASE_NAME_MDS);

        // Path to the just created empty dbMds
        String outFileNameMds = getMdsDatabasePath();

        // if the path doesn't exist first, create it
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty dbMds as the output stream
        OutputStream myOutputMds = new FileOutputStream(outFileNameMds);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInputMds.read(buffer)) > 0) {
            myOutputMds.write(buffer, 0, length);
        }

        // Close the streams
        myOutputMds.flush();
        myOutputMds.close();
        myInputMds.close();

    }

    public void CopyMDSDataBaseFromSDCard() throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        String backupDBPath = context.getResources().getString(R.string.app_name) + "/"
                + DBAdapter.DATABASE_NAME_MDS;
        File backupDB = new File(sd, backupDBPath);

        InputStream myInput = new FileInputStream(backupDB);
        // context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = getMdsDatabasePath();

        // if the path doesn't exist first, create it
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void CopyMDSUserDataBaseFromSDCard() throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        String backupDBPath = context.getPackageName() + "/"
                + DBAdapter.DATABASE_NAME_USER;
        File backupDB = new File(sd, backupDBPath);

        InputStream myInput = new FileInputStream(backupDB);
        // context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = getUserDatabasePath();

        // if the path doesn't exist first, create it
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }



    private String getUserDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME_USER;
    }

    private String getMdsDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME_MDS;
    }

    public ArrayList<String> getCountryList(String type) {

        ArrayList<String> countryList = new ArrayList<>();

        try {
            String Query = "Select  *  from " + GLOBALAREACODES;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbUser.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {

                    countryList.add(cursor.getString(cursor
                            .getColumnIndex(type)));

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return countryList;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countryList;

    }

    public boolean updateAppointmentsData(AppointmentsWrapper wrapper, int id) {


        ContentValues initialValues = new ContentValues();
        try {

            initialValues.put(DATENTIME, wrapper.getDateNtime());
            initialValues.put(PROVIDER, wrapper.getProvider());
            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(DATETIMESTAMP, wrapper.getDatetimestamp());
            initialValues.put(date_search, wrapper.getSearch_date());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbMds
                .update(TABLE_APPOINTMENTSINFO, initialValues,
                        AROWID + "=" + id
                        , null) > 0;

    }

    public AppointmentsWrapper getAppointmentsWrapper(int id) {

        AppointmentsWrapper wrapper = new AppointmentsWrapper();

        if (isTableExists(dbMds,TABLE_APPOINTMENTSINFO)) {
            String Query = "Select  *  from " + TABLE_APPOINTMENTSINFO + " where " + AROWID + " = " + id;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {


                    wrapper.setDateNtime(cursor.getString(cursor
                            .getColumnIndex(DATENTIME)));
                    wrapper.setProvider(cursor.getString(cursor
                            .getColumnIndex(PROVIDER)));
                    wrapper.setNotes(cursor.getString(cursor
                            .getColumnIndex(NOTES)));
                    wrapper.setDatetimestamp(cursor.getString(cursor
                            .getColumnIndex(DATETIMESTAMP)));
                    wrapper.setArowid(cursor.getInt(cursor
                            .getColumnIndex(AROWID)));
                    wrapper.setSearch_date(cursor.getString(cursor.getColumnIndex(date_search)));

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return wrapper;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return wrapper;
    }


    public ArrayList<String> getAppointmentDatesList() {

        ArrayList<String> wrapperList = new ArrayList<String>();
        if (isTableExists(dbMds,TABLE_APPOINTMENTSINFO)) {
        String Query = "Select  " + DATETIMESTAMP + "  from " + TABLE_APPOINTMENTSINFO;
        System.out.println("Query>>>" + Query);

        Cursor cursor = dbMds.rawQuery(Query, null);
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            // wrapper.setStartDate(cursor.getString(cursor
            // .getColumnIndex(STARTDATETIME)));
            // wrapper.setEndDate(cursor.getString(cursor
            // .getColumnIndex(ENDDATETIME)));
            do {


                wrapperList.add(cursor.getString(cursor
                        .getColumnIndex(DATETIMESTAMP)));


                cursor.moveToNext();
            } while (!cursor.isAfterLast());

            return wrapperList;

            // if (startdate_db >= start_date && startdate_db <= end_date) {
            // return true;
            // }
        }
        }
        return wrapperList;
    }

    public boolean deleteAppointment(String id) {
        return dbMds.delete(TABLE_APPOINTMENTSINFO, "arowid" + "=" + id, null) > 0;
    }

    public ArrayList<AppointmentsWrapper> getAppointmentList(String date) {

        ArrayList<AppointmentsWrapper> notesList = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_APPOINTMENTSINFO)) {
            try {
//            String Query = "Select  *  from " + TABLE_LABRESULTINFO;
                String Query = "";
                if (TextUtils.isEmpty(date))
                    Query = "Select  *  from " + TABLE_APPOINTMENTSINFO;
                else
                    Query = "Select  *  from " + TABLE_APPOINTMENTSINFO + " where " + date_search + " = '" + date + "'";
                System.out.println("Query>>>" + Query);

                Cursor cursor = dbMds.rawQuery(Query, null);
                if (cursor.getCount() > 0) {

                    cursor.moveToFirst();
                    // wrapper.setStartDate(cursor.getString(cursor
                    // .getColumnIndex(STARTDATETIME)));
                    // wrapper.setEndDate(cursor.getString(cursor
                    // .getColumnIndex(ENDDATETIME)));
                    do {
                        AppointmentsWrapper wrapper = new AppointmentsWrapper();
                        wrapper.setDateNtime(cursor.getString(cursor
                                .getColumnIndex(DATENTIME)));
                        wrapper.setProvider(cursor.getString(cursor
                                .getColumnIndex(PROVIDER)));
                        wrapper.setNotes(cursor.getString(cursor
                                .getColumnIndex(NOTES)));
                        wrapper.setDatetimestamp(cursor.getString(cursor
                                .getColumnIndex(DATETIMESTAMP)));
                        wrapper.setArowid(cursor.getInt(cursor
                                .getColumnIndex(AROWID)));

                        cursor.moveToNext();
                        notesList.add(wrapper);
                    } while (!cursor.isAfterLast());

                    return notesList;

                    // if (startdate_db >= start_date && startdate_db <= end_date) {
                    // return true;
                    // }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return notesList;

    }

    public ArrayList<AppointmentsWrapper> getAppointmentListPerTicurDate(String date) {

        ArrayList<AppointmentsWrapper> notesList = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_APPOINTMENTSINFO)) {
            try {
//            String Query = "Select  *  from " + TABLE_LABRESULTINFO;
                String Query = "";
                if (TextUtils.isEmpty(date))
                    Query = "Select  *  from " + TABLE_APPOINTMENTSINFO;
                else
                    Query = "Select  *  from " + TABLE_APPOINTMENTSINFO + " where " + DATETIMESTAMP + " = '" + date + "'";
                System.out.println("Query>>>" + Query);

                Cursor cursor = dbMds.rawQuery(Query, null);
                if (cursor.getCount() > 0) {

                    cursor.moveToFirst();
                    // wrapper.setStartDate(cursor.getString(cursor
                    // .getColumnIndex(STARTDATETIME)));
                    // wrapper.setEndDate(cursor.getString(cursor
                    // .getColumnIndex(ENDDATETIME)));
                    do {
                        AppointmentsWrapper wrapper = new AppointmentsWrapper();
                        wrapper.setDateNtime(cursor.getString(cursor
                                .getColumnIndex(DATENTIME)));
                        wrapper.setProvider(cursor.getString(cursor
                                .getColumnIndex(PROVIDER)));
                        wrapper.setNotes(cursor.getString(cursor
                                .getColumnIndex(NOTES)));
                        wrapper.setDatetimestamp(cursor.getString(cursor
                                .getColumnIndex(DATETIMESTAMP)));
                        wrapper.setArowid(cursor.getInt(cursor
                                .getColumnIndex(AROWID)));

                        cursor.moveToNext();
                        notesList.add(wrapper);
                    } while (!cursor.isAfterLast());

                    return notesList;

                    // if (startdate_db >= start_date && startdate_db <= end_date) {
                    // return true;
                    // }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return notesList;

    }
    public void saveotherResult(OtherResultWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(BLOODCOUNTID, wrapper.getBlabid());
            initialValues.put(LABTITLE, wrapper.getLabtitle());
            initialValues.put(LABVALUE, wrapper.getLabvalue());


        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_OTHER_BLOOD_RESULT, null, initialValues);


    }
    public boolean updateOtherResult(OtherResultWrapper wrapper, int id) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(BLOODCOUNTID, wrapper.getBlabid());
            initialValues.put(LABTITLE, wrapper.getLabtitle());
            initialValues.put(LABVALUE, wrapper.getLabvalue());


            return dbMds
                    .update(TABLE_OTHER_BLOOD_RESULT, initialValues,
                            OTHERID + "=" + id
                            , null) > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }
    public ArrayList<OtherResultWrapper> getOtherResult(String id) {


        ArrayList<OtherResultWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_OTHER_BLOOD_RESULT)) {
            String Query = "Select  *  from " + TABLE_OTHER_BLOOD_RESULT + " where " + BLOODCOUNTID + " = " + id;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {


                    OtherResultWrapper wrapper = new OtherResultWrapper();

                    wrapper.setOtherrowid(cursor.getString(cursor.getColumnIndex(OTHERID)));
                    wrapper.setBlabid(cursor.getString(cursor.getColumnIndex(BLOODCOUNTID)));
                    wrapper.setLabtitle(cursor.getString(cursor.getColumnIndex(LABTITLE)));
                    wrapper.setLabvalue(cursor.getString(cursor.getColumnIndex(LABVALUE)));


                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return list;
    }

    public void saveIPSSScore(AddIpssContactWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(DATEIPSS, wrapper.getDate());
            initialValues.put(IPSSSCORE, wrapper.getIpss_score());
            initialValues.put(IPSSNOTES, wrapper.getNotes());


        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_IPSSRSCOREINFO, null, initialValues);


    }

    public boolean updateIPSSScore(AddIpssContactWrapper wrapper, int id) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(DATEIPSS, wrapper.getDate());
            initialValues.put(IPSSSCORE, wrapper.getIpss_score());
            initialValues.put(IPSSNOTES, wrapper.getNotes());


            return dbMds
                    .update(TABLE_IPSSRSCOREINFO, initialValues,
                            IPROWID + "=" + id
                            , null) > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public boolean deleteIPSSScore(String id) {
        return dbMds.delete(TABLE_IPSSRSCOREINFO, "irowid" + "=" + id, null) > 0;
    }

    public ArrayList<AddIpssContactWrapper> getIPSSScore() {


        ArrayList<AddIpssContactWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_IPSSRSCOREINFO)) {
        String Query = "Select  *  from " + TABLE_IPSSRSCOREINFO;
        System.out.println("Query>>>" + Query);

        Cursor cursor = dbMds.rawQuery(Query, null);
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            // wrapper.setStartDate(cursor.getString(cursor
            // .getColumnIndex(STARTDATETIME)));
            // wrapper.setEndDate(cursor.getString(cursor
            // .getColumnIndex(ENDDATETIME)));
            do {


                AddIpssContactWrapper wrapper = new AddIpssContactWrapper();

                wrapper.setId(cursor.getString(cursor.getColumnIndex(IPROWID)));
                wrapper.setDate(cursor.getString(cursor.getColumnIndex(DATEIPSS)));
                wrapper.setIpss_score(cursor.getString(cursor.getColumnIndex(IPSSSCORE)));
                wrapper.setNotes(cursor.getString(cursor.getColumnIndex(IPSSNOTES)));


                list.add(wrapper);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

            return list;

            // if (startdate_db >= start_date && startdate_db <= end_date) {
            // return true;
            // }
        }
        }
        return list;
    }

    public void saveAppoinementsData(AppointmentsWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(DATENTIME, wrapper.getDateNtime());
            initialValues.put(PROVIDER, wrapper.getProvider());
            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(DATETIMESTAMP, wrapper.getDatetimestamp());
            initialValues.put(date_search, wrapper.getSearch_date());

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_APPOINTMENTSINFO, null, initialValues);


    }

    public ArrayList<Transfusionwrapper> getTransfusion() {


        ArrayList<Transfusionwrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_TRANSFUSIONINFO)) {
            String Query = "Select  *  from " + TABLE_TRANSFUSIONINFO;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                do {
                    Transfusionwrapper wrapper = new Transfusionwrapper();
                    wrapper.setId(cursor.getString(cursor.getColumnIndex(TRANSFUSIONID)));
                    wrapper.setDate(cursor.getString(cursor.getColumnIndex(TRANSFUSIONDATE)));
                    wrapper.setTtype(cursor.getString(cursor.getColumnIndex(TRANSFUSIONTYPE)));
                    wrapper.setUnit(cursor.getString(cursor.getColumnIndex(TRANSFUSIONUNIT)));
                    wrapper.setBlood_type(cursor.getString(cursor.getColumnIndex(TRANSFUSIONBLOOD_TYPE)));
                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;
            }
        }
        return list;
    }

    public void saveTransfusionData(Transfusionwrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(TRANSFUSIONDATE, wrapper.getDate());
            initialValues.put(TRANSFUSIONTYPE, wrapper.getTtype());
            initialValues.put(TRANSFUSIONUNIT, wrapper.getUnit());
            initialValues.put(DATE_TO_ORDER, "");
            initialValues.put(TRANSFUSIONBLOOD_TYPE, wrapper.getBlood_type());

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_TRANSFUSIONINFO, null, initialValues);


    }

    public boolean deleteTransfusion(String id) {
        return dbMds.delete(TABLE_TRANSFUSIONINFO, "trowid" + "=" + id, null) > 0;
    }

    public boolean updateTransfusion(Transfusionwrapper wrapper, int id) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(TRANSFUSIONDATE, wrapper.getDate());
            initialValues.put(TRANSFUSIONTYPE, wrapper.getTtype());
            initialValues.put(TRANSFUSIONUNIT, wrapper.getUnit());
            initialValues.put(TRANSFUSIONBLOOD_TYPE, wrapper.getBlood_type());


            return dbMds.update(TABLE_TRANSFUSIONINFO, initialValues,
                    TRANSFUSIONID + "=" + id
                    , null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public boolean updateLabResultData(LabResultInfoWrapper wrapper, int id) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(DATE, wrapper.getDate());
            initialValues.put(RESULTS, wrapper.getResults());
            initialValues.put(UNITS, wrapper.getUnits());
            initialValues.put(LABRESULTTYPE, wrapper.getLabresulttype());
            initialValues.put(LABIMAGES, wrapper.getLabimages());
            initialValues.put(DIAGNOSISTEST, wrapper.getDiagnosistest());
            return dbMds
                    .update(TABLE_LABRESULTINFO, initialValues,
                            LROWID + "=" + id
                            , null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public LabResultInfoWrapper getLabResultInfoWrapper(int id) {

        LabResultInfoWrapper wrapper = new LabResultInfoWrapper();
        if (isTableExists(dbMds,TABLE_LABRESULTINFO)) {

            String Query = "Select  *  from " + TABLE_LABRESULTINFO + " where " + LROWID + " = " + id;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {
                    wrapper.setDiagnosistest(cursor.getString(cursor
                            .getColumnIndex(DIAGNOSISTEST)));
                    wrapper.setDate(cursor.getString(cursor
                            .getColumnIndex(DATE)));
                    wrapper.setNotes(cursor.getString(cursor
                            .getColumnIndex(NOTES)));
                    wrapper.setResults(cursor.getString(cursor
                            .getColumnIndex(RESULTS)));
                    wrapper.setUnits(cursor.getString(cursor
                            .getColumnIndex(UNITS)));
                    wrapper.setLabresulttype(cursor.getString(cursor
                            .getColumnIndex(LABRESULTTYPE)));
                    wrapper.setLabimages(cursor.getString(cursor
                            .getColumnIndex(LABIMAGES)));
                    wrapper.setLrowid(cursor.getInt(cursor
                            .getColumnIndex(LROWID)));
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return wrapper;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return wrapper;
    }

    public boolean deleteLabResults(String id) {
        return dbMds.delete(TABLE_LABRESULTINFO, "lrowid" + "=" + id, null) > 0;
    }

    public ArrayList<LabResultInfoWrapper> getLabResultsList(String type) {

        ArrayList<LabResultInfoWrapper> notesList = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_LABRESULTINFO)) {
            try {
//            String Query = "Select  *  from " + TABLE_LABRESULTINFO;

                String Query = "Select  *  from " + TABLE_LABRESULTINFO + " where " + LABRESULTTYPE + " = '" + type + "'";
                System.out.println("Query>>>" + Query);

                Cursor cursor = dbMds.rawQuery(Query, null);
                if (cursor.getCount() > 0) {

                    cursor.moveToFirst();
                    // wrapper.setStartDate(cursor.getString(cursor
                    // .getColumnIndex(STARTDATETIME)));
                    // wrapper.setEndDate(cursor.getString(cursor
                    // .getColumnIndex(ENDDATETIME)));
                    do {
                        LabResultInfoWrapper wrapper = new LabResultInfoWrapper();
                        wrapper.setDiagnosistest(cursor.getString(cursor
                                .getColumnIndex(DIAGNOSISTEST)));
                        wrapper.setDate(cursor.getString(cursor
                                .getColumnIndex(DATE)));
                        wrapper.setNotes(cursor.getString(cursor
                                .getColumnIndex(NOTES)));
                        wrapper.setResults(cursor.getString(cursor
                                .getColumnIndex(RESULTS)));
                        wrapper.setUnits(cursor.getString(cursor
                                .getColumnIndex(UNITS)));
                        wrapper.setLabresulttype(cursor.getString(cursor
                                .getColumnIndex(LABRESULTTYPE)));
                        wrapper.setLabimages(cursor.getString(cursor
                                .getColumnIndex(LABIMAGES)));
                        wrapper.setLrowid(cursor.getInt(cursor
                                .getColumnIndex(LROWID)));
                        cursor.moveToNext();
                        notesList.add(wrapper);
                    } while (!cursor.isAfterLast());

                    return notesList;

                    // if (startdate_db >= start_date && startdate_db <= end_date) {
                    // return true;
                    // }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return notesList;

    }

    public void saveLabResultData(LabResultInfoWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(DIAGNOSISTEST, wrapper.getDiagnosistest());

            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(DATE, wrapper.getDate());
            initialValues.put(RESULTS, wrapper.getResults());
            initialValues.put(UNITS, wrapper.getUnits());
            initialValues.put(LABRESULTTYPE, wrapper.getLabresulttype());
            initialValues.put(LABIMAGES, wrapper.getLabimages());


        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_LABRESULTINFO, null, initialValues);


    }


    public boolean updateDiagnosisData(DiagnosisWrapper wrapper, int id) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(DATE, wrapper.getDate());
            initialValues.put(DIAGNOSIS, wrapper.getDiagnosis());
            initialValues.put(MANAGINGPROVIDER, wrapper.getManagingprovider());
            initialValues.put(HISTORYTYPE, wrapper.getHistorytype());


            return dbMds
                    .update(TABLE_MEDICALDIAGNOSISHISTORY, initialValues,
                            DROWID + "=" + id
                            , null) > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public DiagnosisWrapper getDiagnosisWrapper(int id) {

        DiagnosisWrapper wrapper = new DiagnosisWrapper();
        if (isTableExists(dbMds,TABLE_MEDICALDIAGNOSISHISTORY)) {

        String Query = "Select  *  from " + TABLE_MEDICALDIAGNOSISHISTORY + " where " + DROWID + " = " + id;
        System.out.println("Query>>>" + Query);

        Cursor cursor = dbMds.rawQuery(Query, null);
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            // wrapper.setStartDate(cursor.getString(cursor
            // .getColumnIndex(STARTDATETIME)));
            // wrapper.setEndDate(cursor.getString(cursor
            // .getColumnIndex(ENDDATETIME)));
            do {

                wrapper.setDate(cursor.getString(cursor
                        .getColumnIndex(DATE)));
                wrapper.setNotes(cursor.getString(cursor
                        .getColumnIndex(NOTES)));
                wrapper.setManagingprovider(cursor.getString(cursor
                        .getColumnIndex(MANAGINGPROVIDER)));
                wrapper.setHistorytype(cursor.getString(cursor
                        .getColumnIndex(HISTORYTYPE)));
                wrapper.setDiagnosis(cursor.getString(cursor
                        .getColumnIndex(DIAGNOSIS)));

                cursor.moveToNext();
            } while (!cursor.isAfterLast());

            return wrapper;

            // if (startdate_db >= start_date && startdate_db <= end_date) {
            // return true;
            // }
        }
        }
        return wrapper;
    }

    public boolean deleteDiagnosis(String id) {
        return dbMds.delete(TABLE_MEDICALDIAGNOSISHISTORY, "drowid" + "=" + id, null) > 0;
    }

    public ArrayList<DiagnosisWrapper> getDiagnosisList(String type) {

        ArrayList<DiagnosisWrapper> notesList = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_MEDICALDIAGNOSISHISTORY)) {
            try {
                String Query = "Select  *  from " + TABLE_MEDICALDIAGNOSISHISTORY + " where " + HISTORYTYPE + " = '" + type + "'";
                System.out.println("Query>>>" + Query);

                Cursor cursor = dbMds.rawQuery(Query, null);
                if (cursor.getCount() > 0) {

                    cursor.moveToFirst();
                    // wrapper.setStartDate(cursor.getString(cursor
                    // .getColumnIndex(STARTDATETIME)));
                    // wrapper.setEndDate(cursor.getString(cursor
                    // .getColumnIndex(ENDDATETIME)));
                    do {
                        DiagnosisWrapper wrapper = new DiagnosisWrapper();

                        wrapper.setDate(cursor.getString(cursor
                                .getColumnIndex(DATE)));
                        wrapper.setNotes(cursor.getString(cursor
                                .getColumnIndex(NOTES)));
                        wrapper.setDiagnosis(cursor.getString(cursor
                                .getColumnIndex(DIAGNOSIS)));
                        wrapper.setManagingprovider(cursor.getString(cursor
                                .getColumnIndex(MANAGINGPROVIDER)));
                        wrapper.setHistorytype(cursor.getString(cursor
                                .getColumnIndex(HISTORYTYPE)));
                        wrapper.setDrowid(cursor.getInt(cursor
                                .getColumnIndex(DROWID)));
                        cursor.moveToNext();
                        notesList.add(wrapper);
                    } while (!cursor.isAfterLast());

                    return notesList;

                    // if (startdate_db >= start_date && startdate_db <= end_date) {
                    // return true;
                    // }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return notesList;

    }

    public void saveDiagnosisData(DiagnosisWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {


            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(DATE, wrapper.getDate());
            initialValues.put(HISTORYTYPE, wrapper.getHistorytype());
            initialValues.put(MANAGINGPROVIDER, wrapper.getManagingprovider());
            initialValues.put(DIAGNOSIS, wrapper.getDiagnosis());

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_MEDICALDIAGNOSISHISTORY, null, initialValues);


    }


    public boolean updateNotesData(NotesWrapper wrapper, int id) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(DATE, wrapper.getDate());
            initialValues.put(TOPIC, wrapper.getTopic());

            return dbMds
                    .update(TABLE_NOTESINFO, initialValues,
                            NROWID + "=" + id
                            , null) > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public NotesWrapper getNotesWrapper(int id) {

        NotesWrapper wrapper = new NotesWrapper();
        if (isTableExists(dbMds,TABLE_NOTESINFO)) {

        String Query = "Select  *  from " + TABLE_NOTESINFO + " where " + NROWID + " = " + id;
        System.out.println("Query>>>" + Query);

        Cursor cursor = dbMds.rawQuery(Query, null);
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            // wrapper.setStartDate(cursor.getString(cursor
            // .getColumnIndex(STARTDATETIME)));
            // wrapper.setEndDate(cursor.getString(cursor
            // .getColumnIndex(ENDDATETIME)));
            do {

                wrapper.setDate(cursor.getString(cursor
                        .getColumnIndex(DATE)));
                wrapper.setNotes(cursor.getString(cursor
                        .getColumnIndex(NOTES)));
                wrapper.setTopic(cursor.getString(cursor
                        .getColumnIndex(TOPIC)));

                cursor.moveToNext();
            } while (!cursor.isAfterLast());

            return wrapper;

            // if (startdate_db >= start_date && startdate_db <= end_date) {
            // return true;
            // }
        }
        }
        return wrapper;
    }

    public ArrayList<NotesWrapper> getNotesSearchedList(String search) {


        ArrayList<NotesWrapper> notesList = new ArrayList<>();
        if (isTableExists(dbMds,"notesInfo")) {
            String Query = "SELECT * FROM notesInfo where date LIKE '%" + search + "%' OR topic LIKE '%" + search + "%'  OR notes LIKE '%" + search + "%'";
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {
                    NotesWrapper wrapper = new NotesWrapper();

                    wrapper.setDate(cursor.getString(cursor
                            .getColumnIndex(DATE)));
                    wrapper.setNotes(cursor.getString(cursor
                            .getColumnIndex(NOTES)));
                    wrapper.setTopic(cursor.getString(cursor
                            .getColumnIndex(TOPIC)));
                    wrapper.setNrowid(cursor.getInt(cursor
                            .getColumnIndex(NROWID)));
                    cursor.moveToNext();
                    notesList.add(wrapper);
                } while (!cursor.isAfterLast());

                return notesList;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return notesList;
    }

    public boolean deleteNotes(String id) {
        return dbMds.delete(TABLE_NOTESINFO, "nrowid" + "=" + id, null) > 0;
    }

    public ArrayList<NotesWrapper> getNotesList() {


        ArrayList<NotesWrapper> notesList = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_NOTESINFO)) {
            String Query = "Select  *  from " + TABLE_NOTESINFO;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {
                    NotesWrapper wrapper = new NotesWrapper();

                    wrapper.setDate(cursor.getString(cursor
                            .getColumnIndex(DATE)));
                    wrapper.setNotes(cursor.getString(cursor
                            .getColumnIndex(NOTES)));
                    wrapper.setTopic(cursor.getString(cursor
                            .getColumnIndex(TOPIC)));
                    wrapper.setNrowid(cursor.getInt(cursor
                            .getColumnIndex(NROWID)));
                    cursor.moveToNext();
                    notesList.add(wrapper);
                } while (!cursor.isAfterLast());

                return notesList;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return notesList;
    }

    public void saveNotesData(NotesWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {


            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(DATE, wrapper.getDate());
            initialValues.put(TOPIC, wrapper.getTopic());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_NOTESINFO, null, initialValues);


    }

    public ArrayList<ClinicalWrapper> getClinicals() {


        ArrayList<ClinicalWrapper> clinicalList = new ArrayList<>();
        String Query = "Select  *  from " + TABLE_CLINICAL;
        System.out.println("Query>>>" + Query);

        Cursor cursor = dbMds.rawQuery(Query, null);
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            // wrapper.setStartDate(cursor.getString(cursor
            // .getColumnIndex(STARTDATETIME)));
            // wrapper.setEndDate(cursor.getString(cursor
            // .getColumnIndex(ENDDATETIME)));
            do {
                ClinicalWrapper wrapper = new ClinicalWrapper();

                wrapper.setTreatementid(cursor.getString(cursor
                        .getColumnIndex(Treatementid)));
                wrapper.setLocation(cursor.getString(cursor
                        .getColumnIndex(LOCATION)));
                wrapper.setTrialNumber(cursor.getString(cursor
                        .getColumnIndex(TRIAL_NUMBER)));
                wrapper.setName_ofTrial(cursor.getString(cursor
                        .getColumnIndex(NAME_OF_TRIAL)));
                cursor.moveToNext();
                clinicalList.add(wrapper);
            } while (!cursor.isAfterLast());

            return clinicalList;

            // if (startdate_db >= start_date && startdate_db <= end_date) {
            // return true;
            // }

        }
        return clinicalList;
    }

    public void saveClinicalData(ClinicalWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {


            initialValues.put(Treatementid, wrapper.getTreatementid());
            initialValues.put(TRIAL_NUMBER, wrapper.getTrialNumber());
            initialValues.put(NAME_OF_TRIAL, wrapper.getName_ofTrial());
            initialValues.put(LOCATION, wrapper.getLocation());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_CLINICAL, null, initialValues);


    }

    public boolean updateCarGiverData(CarGiversWrapper wrapper, String name) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(CAREGIVERNAME, wrapper.getCaregivername());
            initialValues.put(CAREGIVERCONTACT, wrapper.getCaregivercontact());
            initialValues.put(CAREGIVERRELATION, wrapper.getCaregiverrelation());
            initialValues.put(CAREGIVEREMAIL,wrapper.getCaregiveremail());

            return dbUser
                    .update(TABLE_CAREGIVERSINFO, initialValues,
                            CAREGIVERNAME + "=" + "'" + name
                                    + "'", null) > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public ArrayList<CarGiversWrapper> getCargivers() {


        ArrayList<CarGiversWrapper> list = new ArrayList<>();
        if (isTableExists(dbUser,TABLE_CAREGIVERSINFO)) {
        String Query = "Select  *  from " + TABLE_CAREGIVERSINFO;
        System.out.println("Query>>>" + Query);

        Cursor cursor = dbUser.rawQuery(Query, null);
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            // wrapper.setStartDate(cursor.getString(cursor
            // .getColumnIndex(STARTDATETIME)));
            // wrapper.setEndDate(cursor.getString(cursor
            // .getColumnIndex(ENDDATETIME)));
            do {
                CarGiversWrapper wrapper = new CarGiversWrapper();

                wrapper.setCaregivername(cursor.getString(cursor
                        .getColumnIndex(CAREGIVERNAME)));
                wrapper.setCaregivercontact(cursor.getString(cursor
                        .getColumnIndex(CAREGIVERCONTACT)));
                wrapper.setCaregiverrelation(cursor.getString(cursor
                        .getColumnIndex(CAREGIVERRELATION)));
                wrapper.setCaregiveremail(cursor.getString(cursor
                        .getColumnIndex(CAREGIVEREMAIL)));

                list.add(wrapper);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

            return list;

            // if (startdate_db >= start_date && startdate_db <= end_date) {
            // return true;
            // }
        }
        }
        return list;
    }

    public void saveCarGivers(CarGiversWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {


            initialValues.put(CAREGIVERNAME, wrapper.getCaregivername());
            initialValues.put(CAREGIVERCONTACT, wrapper.getCaregivercontact());
            initialValues.put(CAREGIVERRELATION, wrapper.getCaregiverrelation());
            initialValues.put(CAREGIVEREMAIL,wrapper.getCaregiveremail());

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbUser.insert(TABLE_CAREGIVERSINFO, null, initialValues);


    }

    public boolean updateMedicalProfessional(MedicalProfessionalWrapper wrapper, int id) {


        ContentValues initialValues = new ContentValues();
        try {


            initialValues.put(PROVIDERNAME, wrapper.getProvidername());
            initialValues.put(PROVIDERSPECIALITY, wrapper.getProviderspeciality());
            initialValues.put(REFERREDBY, wrapper.getReferredby());
            initialValues.put(ADDRESS_MP, wrapper.getAddress());

            initialValues.put(PHONE_MP, wrapper.getPhone());
            initialValues.put(FAX, wrapper.getFax());
            initialValues.put(EMAIL_MP, wrapper.getEmail());
            initialValues.put(COUNTRYODEPROVIDER, wrapper.getCountrycode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbMds
                .update(TABLE_MEDICAL_PROFESSIONAL, initialValues,
                        "mrowid" + "=" + id
                        , null) > 0;

    }

    public boolean deleteMedicalProfessional(int id) {
        return dbMds.delete(TABLE_MEDICAL_PROFESSIONAL, "mrowid" + "=" + id, null) > 0;
    }

    public ArrayList<MedicalProfessionalWrapper> getMedicalProfessionalWrapper() {


        ArrayList<MedicalProfessionalWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_MEDICAL_PROFESSIONAL)) {
            String Query = "Select  *  from " + TABLE_MEDICAL_PROFESSIONAL;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {


                    MedicalProfessionalWrapper wrapper = new MedicalProfessionalWrapper();
                    wrapper.setEmail(cursor.getString(cursor
                            .getColumnIndex(EMAIL_MP)));
                    wrapper.setFax(cursor.getString(cursor
                            .getColumnIndex(FAX)));
                    wrapper.setPhone(cursor.getString(cursor
                            .getColumnIndex(PHONE_MP)));
                    wrapper.setAddress(cursor.getString(cursor
                            .getColumnIndex(ADDRESS_MP)));
                    wrapper.setReferredby(cursor.getString(cursor
                            .getColumnIndex(REFERREDBY)));
                    wrapper.setProviderspeciality(cursor.getString(cursor
                            .getColumnIndex(PROVIDERSPECIALITY)));
                    wrapper.setProvidername(cursor.getString(cursor
                            .getColumnIndex(PROVIDERNAME)));
                    wrapper.setId(cursor.getInt(cursor.getColumnIndex(MROWID)));
                    wrapper.setCountrycode(cursor.getString(cursor.getColumnIndex(COUNTRYODEPROVIDER)));
                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return list;
    }

    public ArrayList<MedicalProfessionalWrapper> getMedicalProfessionalSearchedWrapper(String search) {


        ArrayList<MedicalProfessionalWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,"medicalprovider")) {
            String Query = "SELECT * FROM medicalprovider where providername LIKE '%" + search + "%' OR providerspeciality LIKE '%" + search + "%'  OR referredby LIKE '%" + search + "%'  OR address LIKE '%" + search + "%'  OR phone LIKE '%" + search + "%' OR email LIKE '%" + search + "%' OR fax LIKE '%" + search + "%'";
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {


                    MedicalProfessionalWrapper wrapper = new MedicalProfessionalWrapper();
                    wrapper.setEmail(cursor.getString(cursor
                            .getColumnIndex(EMAIL_MP)));
                    wrapper.setFax(cursor.getString(cursor
                            .getColumnIndex(FAX)));
                    wrapper.setPhone(cursor.getString(cursor
                            .getColumnIndex(PHONE_MP)));
                    wrapper.setAddress(cursor.getString(cursor
                            .getColumnIndex(ADDRESS_MP)));
                    wrapper.setReferredby(cursor.getString(cursor
                            .getColumnIndex(REFERREDBY)));
                    wrapper.setProviderspeciality(cursor.getString(cursor
                            .getColumnIndex(PROVIDERSPECIALITY)));
                    wrapper.setProvidername(cursor.getString(cursor
                            .getColumnIndex(PROVIDERNAME)));
                    wrapper.setCountrycode(cursor.getString(cursor.getColumnIndex(COUNTRYODEPROVIDER)));
                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return list;
    }


    public ArrayList<SymptomDetailWrapper> getSearchedSymtomList(String search) {


        ArrayList<SymptomDetailWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,"symptorInfo")) {
            String Query = "SELECT * FROM symptorInfo where symptomname LIKE '%" + search + "%' OR severity LIKE '%" + search + "%'  OR symptomdate LIKE '%" + search + "%'  OR symptomtime LIKE '%" + search + "%'  OR duration LIKE '%" + search + "%' OR frequency LIKE '%" + search + "%' OR notes LIKE '%" + search + "%'";
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                do {
                    SymptomDetailWrapper wrapper = new SymptomDetailWrapper();

//                wrapper.setModifieddate(cursor.getString(cursor
//                        .getColumnIndex(MODIFIEDDATE)));
                    wrapper.setSymptomname(cursor.getString(cursor
                            .getColumnIndex(SYMPTOMNAME)));
                    wrapper.setSeverity(cursor.getString(cursor
                            .getColumnIndex(SEVERITY)));
                    wrapper.setSymptomdate(cursor.getString(cursor
                            .getColumnIndex(SYMPTOMDATE)));
                    wrapper.setDuration(cursor.getString(cursor
                            .getColumnIndex(DURATION)));
                    wrapper.setSymptomtime(cursor.getString(cursor
                            .getColumnIndex(SYMPTOMTIME)));
                    wrapper.setFrequency(cursor.getString(cursor
                            .getColumnIndex(FREQUENCY)));
                    wrapper.setNotes(cursor.getString(cursor
                            .getColumnIndex(NOTES)));
//                wrapper.setCreatedate(cursor.getString(cursor
//                        .getColumnIndex(CREATEDATE)));
                    wrapper.setSrowid(cursor.getInt(cursor
                            .getColumnIndex("srowid")));
                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;


            }
        }
        return list;
    }


    public ArrayList<String> getSymtomList() {


        ArrayList<String> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_SYMPTOMSLIST)) {
            String Query = "Select  *  from " + TABLE_SYMPTOMSLIST;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                do {


                    list.add(cursor.getString(cursor
                            .getColumnIndex(SYMPTOM)));
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;


            }
        }
        return list;
    }

    public ArrayList<SymptomDetailWrapper> getSymtomDetailList() {


        ArrayList<SymptomDetailWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_SYMPTOMINFO)) {
            String Query = "Select  *  from " + TABLE_SYMPTOMINFO;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                do {
                    SymptomDetailWrapper wrapper = new SymptomDetailWrapper();

//                wrapper.setModifieddate(cursor.getString(cursor
//                        .getColumnIndex(MODIFIEDDATE)));
                    wrapper.setSymptomname(cursor.getString(cursor
                            .getColumnIndex(SYMPTOMNAME)));
                    wrapper.setSeverity(cursor.getString(cursor
                            .getColumnIndex(SEVERITY)));
                    wrapper.setSymptomdate(cursor.getString(cursor
                            .getColumnIndex(SYMPTOMDATE)));
                    wrapper.setDuration(cursor.getString(cursor
                            .getColumnIndex(DURATION)));
                    wrapper.setSymptomtime(cursor.getString(cursor
                            .getColumnIndex(SYMPTOMTIME)));
                    wrapper.setFrequency(cursor.getString(cursor
                            .getColumnIndex(FREQUENCY)));
                    wrapper.setNotes(cursor.getString(cursor
                            .getColumnIndex(NOTES)));
                    wrapper.setSubsymptom_str(cursor.getString(cursor.getColumnIndex(SYMPTOMSUBCAT)));
//                wrapper.setCreatedate(cursor.getString(cursor
//                        .getColumnIndex(CREATEDATE)));
                    wrapper.setSrowid(cursor.getInt(cursor
                            .getColumnIndex("srowid")));
                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;


            }
        }
        return list;
    }

    public boolean deleteSYMPTOINFO(String id) {
        return dbMds.delete(TABLE_SYMPTOMINFO, "srowid" + "=" + id, null) > 0;
    }

    public void saveSymptom(SymptomWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {


            initialValues.put(SYMPTOM, wrapper.getSymptom());
            initialValues.put(DESCRIPTION, wrapper.getDescription());

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_SYMPTOMSLIST, null, initialValues);


    }

    public void saveMedicineInfo(MedicineInfoWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {

            initialValues.put(DRUGNAME, wrapper.getDrugname());
            initialValues.put(GENERICNAME, wrapper.getGenericname());
            initialValues.put(DOSAGE, wrapper.getDosage());
            initialValues.put(TYPE, wrapper.getType());
            initialValues.put(FREQUENCY, wrapper.getFrequency());
            initialValues.put(STARTDATE, wrapper.getStartdate());
            initialValues.put(ENDDATE, wrapper.getEnddate());
            initialValues.put(REFILDATE, wrapper.getRefildate());
            initialValues.put(PRESCIRBEDBY, wrapper.getPrescirbedby());
            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(IMAGENAME, wrapper.getImagename());
            initialValues.put(REFILFREQUENCY, wrapper.getRefilfrequency());
            initialValues.put(STARTTIMESTAMP, wrapper.getStarttimestamp());
            initialValues.put(ENDTIMESTAMP, wrapper.getEndtimestamp());
            initialValues.put(REMINDERSTRING, wrapper.getReminderstring());
            initialValues.put(RFREQUENCYTITLE, wrapper.getRfrequencytitle());
            initialValues.put(REMINDERCOUNTS, wrapper.getRemindercounts());
            initialValues.put(REMINDERSTARTTIME, wrapper.getReminderstarttime());
            initialValues.put(SCHEDULEDAYS, wrapper.getScheduledays());
            initialValues.put(REMINDERSOUNDFILE, wrapper.getRemindersoundfile());
            initialValues.put(REMINDERSOUNDFILEINDEX, wrapper.getRemindersoundindex());

//            initialValues.put(MID,wrapper.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_MEDICINEINFO, null, initialValues);


    }

    public boolean updateMedicineInfo(MedicineInfoWrapper wrapper, int id) {


        ContentValues initialValues = new ContentValues();
        try {

            initialValues.put(DRUGNAME, wrapper.getDrugname());
            initialValues.put(GENERICNAME, wrapper.getGenericname());
            initialValues.put(DOSAGE, wrapper.getDosage());
            initialValues.put(TYPE, wrapper.getType());
            initialValues.put(FREQUENCY, wrapper.getFrequency());
            initialValues.put(STARTDATE, wrapper.getStartdate());
            initialValues.put(ENDDATE, wrapper.getEnddate());
            initialValues.put(REFILDATE, wrapper.getRefildate());
            initialValues.put(PRESCIRBEDBY, wrapper.getPrescirbedby());
            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(IMAGENAME, wrapper.getImagename());
            initialValues.put(REFILFREQUENCY, wrapper.getRefilfrequency());
            initialValues.put(STARTTIMESTAMP, wrapper.getStarttimestamp());
            initialValues.put(ENDTIMESTAMP, wrapper.getEndtimestamp());
            initialValues.put(REMINDERSTRING, wrapper.getReminderstring());
            initialValues.put(RFREQUENCYTITLE, wrapper.getRfrequencytitle());
            initialValues.put(REMINDERCOUNTS, wrapper.getRemindercounts());
            initialValues.put(REMINDERSTARTTIME, wrapper.getReminderstarttime());
            initialValues.put(SCHEDULEDAYS, wrapper.getScheduledays());
            initialValues.put(REMINDERSOUNDFILE, wrapper.getRemindersoundfile());
            initialValues.put(REMINDERSOUNDFILEINDEX, wrapper.getRemindersoundindex());
//            initialValues.put(MID,wrapper.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbMds
                .update(TABLE_MEDICINEINFO, initialValues,
                        "mid" + "=" + id
                        , null) > 0;

    }

    public boolean deleteMedicineInfoMap(String id) {
        return dbMds.delete(TABLE_MEDICINEINFO, "mid" + "=" + id, null) > 0;
    }

    public ArrayList<MedicineInfoWrapper> getMedicineInfoMap(String type) {


        ArrayList<MedicineInfoWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_MEDICINEINFO)) {
            String Query = "Select  *  from " + TABLE_MEDICINEINFO + " where " + TYPE + " = '" + type + "'";
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {
                    MedicineInfoWrapper wrapper = new MedicineInfoWrapper();
                    wrapper.setDrugname(cursor.getString(cursor.getColumnIndex(DRUGNAME)));
                    wrapper.setGenericname(cursor.getString(cursor.getColumnIndex(GENERICNAME)));
                    wrapper.setDosage(cursor.getString(cursor.getColumnIndex(DOSAGE)));
                    wrapper.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                    wrapper.setFrequency(cursor.getString(cursor.getColumnIndex(FREQUENCY)));
                    wrapper.setStartdate(cursor.getString(cursor.getColumnIndex(STARTDATE)));
                    wrapper.setEnddate(cursor.getString(cursor.getColumnIndex(ENDDATE)));
                    wrapper.setRefildate(cursor.getString(cursor.getColumnIndex(REFILDATE)));
                    wrapper.setPrescirbedby(cursor.getString(cursor.getColumnIndex(PRESCIRBEDBY)));
                    wrapper.setNotes(cursor.getString(cursor.getColumnIndex(NOTES)));
                    wrapper.setImagename(cursor.getString(cursor.getColumnIndex(IMAGENAME)));
                    wrapper.setRefilfrequency(cursor.getString(cursor.getColumnIndex(REFILFREQUENCY)));
                    wrapper.setStarttimestamp(cursor.getString(cursor.getColumnIndex(STARTTIMESTAMP)));
                    wrapper.setEndtimestamp(cursor.getString(cursor.getColumnIndex(ENDTIMESTAMP)));
                    wrapper.setReminderstring(cursor.getString(cursor.getColumnIndex(REMINDERSTRING)));
                    wrapper.setId(cursor.getString(cursor.getColumnIndex(MID)));
                    wrapper.setRemindercounts(cursor.getString(cursor.getColumnIndex(REMINDERCOUNTS)));
                    wrapper.setReminderstarttime(cursor.getString(cursor.getColumnIndex(REMINDERSTARTTIME)));
                    wrapper.setScheduledays(cursor.getString(cursor.getColumnIndex(SCHEDULEDAYS)));


                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return list;

    }

    public ArrayList<MedicineInfoWrapper> getMedicineSearched(String search) {


        ArrayList<MedicineInfoWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,"MedicineInfo")) {
        String Query = "SELECT * FROM MedicineInfo where drugname LIKE '%" + search + "%' OR genericname LIKE '%" + search + "%'  OR type LIKE '%" + search + "%'  OR frequency LIKE '%" + search + "%'  OR startdate LIKE '%" + search + "%' OR prescirbedby LIKE '%" + search + "%' OR notes LIKE '%" + search + "%'";
        System.out.println("Query>>>" + Query);

        Cursor cursor = dbMds.rawQuery(Query, null);
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            // wrapper.setStartDate(cursor.getString(cursor
            // .getColumnIndex(STARTDATETIME)));
            // wrapper.setEndDate(cursor.getString(cursor
            // .getColumnIndex(ENDDATETIME)));
            do {
                MedicineInfoWrapper wrapper = new MedicineInfoWrapper();
                wrapper.setDrugname(cursor.getString(cursor.getColumnIndex(DRUGNAME)));
                wrapper.setGenericname(cursor.getString(cursor.getColumnIndex(GENERICNAME)));
                wrapper.setDosage(cursor.getString(cursor.getColumnIndex(DOSAGE)));
                wrapper.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                wrapper.setFrequency(cursor.getString(cursor.getColumnIndex(FREQUENCY)));
                wrapper.setStartdate(cursor.getString(cursor.getColumnIndex(STARTDATE)));
                wrapper.setEnddate(cursor.getString(cursor.getColumnIndex(ENDDATE)));
                wrapper.setRefildate(cursor.getString(cursor.getColumnIndex(REFILDATE)));
                wrapper.setPrescirbedby(cursor.getString(cursor.getColumnIndex(PRESCIRBEDBY)));
                wrapper.setNotes(cursor.getString(cursor.getColumnIndex(NOTES)));
                wrapper.setImagename(cursor.getString(cursor.getColumnIndex(IMAGENAME)));
                wrapper.setRefilfrequency(cursor.getString(cursor.getColumnIndex(REFILFREQUENCY)));
                wrapper.setStarttimestamp(cursor.getString(cursor.getColumnIndex(STARTTIMESTAMP)));
                wrapper.setEndtimestamp(cursor.getString(cursor.getColumnIndex(ENDTIMESTAMP)));
                wrapper.setReminderstring(cursor.getString(cursor.getColumnIndex(REMINDERSTRING)));
                wrapper.setRemindercounts(cursor.getString(cursor.getColumnIndex(REMINDERCOUNTS)));
                wrapper.setReminderstarttime(cursor.getString(cursor.getColumnIndex(REMINDERSTARTTIME)));
                wrapper.setScheduledays(cursor.getString(cursor.getColumnIndex(SCHEDULEDAYS)));
                list.add(wrapper);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

            return list;

            // if (startdate_db >= start_date && startdate_db <= end_date) {
            // return true;
            // }
        }
        }
        return list;

    }

    public boolean updateSymtomData(SymptomDetailWrapper wrapper, int id) {


        ContentValues initialValues = new ContentValues();
        try {

            initialValues.put(SYMPTOMNAME, wrapper.getSymptomname());
            initialValues.put(SEVERITY, wrapper.getSeverity());
            initialValues.put(SYMPTOMDATE, wrapper.getSymptomdate());
            initialValues.put(SYMPTOMTIME, wrapper.getSymptomtime());
            initialValues.put(DURATION, wrapper.getDuration());
            initialValues.put(FREQUENCY, wrapper.getFrequency());
            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(SYMPTOMSUBCAT, wrapper.getSubsymptom_str());
//            initialValues.put(CREATEDATE, wrapper.getCreatedate());
//            initialValues.put(MODIFIEDDATE, wrapper.getModifieddate());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbMds
                .update(TABLE_SYMPTOMINFO, initialValues,
                        "srowid" + "=" + id
                        , null) > 0;

    }

    public SymptomDetailWrapper getSymtomWrapper(int id) {

        SymptomDetailWrapper wrapper = new SymptomDetailWrapper();

        if (isTableExists(dbMds,TABLE_SYMPTOMINFO)) {
            String Query = "Select  *  from " + TABLE_SYMPTOMINFO + " where " + "srowid" + " = " + id;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {


//                wrapper.setModifieddate(cursor.getString(cursor
//                        .getColumnIndex(MODIFIEDDATE)));
                    wrapper.setSymptomname(cursor.getString(cursor
                            .getColumnIndex(SYMPTOMNAME)));
                    wrapper.setSeverity(cursor.getString(cursor
                            .getColumnIndex(SEVERITY)));
                    wrapper.setSymptomdate(cursor.getString(cursor
                            .getColumnIndex(SYMPTOMDATE)));
                    wrapper.setDuration(cursor.getString(cursor
                            .getColumnIndex(DURATION)));
                    wrapper.setSymptomtime(cursor.getString(cursor
                            .getColumnIndex(SYMPTOMTIME)));
                    wrapper.setFrequency(cursor.getString(cursor
                            .getColumnIndex(FREQUENCY)));
                    wrapper.setNotes(cursor.getString(cursor
                            .getColumnIndex(NOTES)));
//                wrapper.setCreatedate(cursor.getString(cursor
//                        .getColumnIndex(CREATEDATE)));
                    wrapper.setSubsymptom_str(cursor.getString(cursor.getColumnIndex(SYMPTOMSUBCAT)));

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return wrapper;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return wrapper;
    }

    public void saveSymptomDetail(SymptomDetailWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {

            initialValues.put(SYMPTOMNAME, wrapper.getSymptomname());
            initialValues.put(SEVERITY, wrapper.getSeverity());
            initialValues.put(SYMPTOMDATE, wrapper.getSymptomdate());
            initialValues.put(SYMPTOMTIME, wrapper.getSymptomtime());
            initialValues.put(DURATION, wrapper.getDuration());
            initialValues.put(FREQUENCY, wrapper.getFrequency());
            initialValues.put(NOTES, wrapper.getNotes());
            initialValues.put(DATE_TO_ORDER,"");

//            initialValues.put(CREATEDATE, wrapper.getCreatedate());
//            initialValues.put(MODIFIEDDATE, wrapper.getModifieddate());
            initialValues.put(SYMPTOMSUBCAT, wrapper.getSubsymptom_str());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_SYMPTOMINFO, null, initialValues);


    }

    public void saveMedicalProfessional(MedicalProfessionalWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {


            initialValues.put(PROVIDERNAME, wrapper.getProvidername());
            initialValues.put(PROVIDERSPECIALITY, wrapper.getProviderspeciality());
            initialValues.put(REFERREDBY, wrapper.getReferredby());
            initialValues.put(ADDRESS_MP, wrapper.getAddress());

            initialValues.put(PHONE_MP, wrapper.getPhone());
            initialValues.put(FAX, wrapper.getFax());
            initialValues.put(EMAIL_MP, wrapper.getEmail());
            initialValues.put(COUNTRYODEPROVIDER, wrapper.getCountrycode());

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_MEDICAL_PROFESSIONAL, null, initialValues);


    }

    public boolean updateAllergyData(AllergyWrapper wrapper, String name) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(ALLERGYTYPE, wrapper.getAllergytype());
            initialValues.put(ALLERGYSUBSTANCE, wrapper.getAllergysubstance());


            return dbUser
                    .update(TABLE_ALERGYINFO, initialValues,
                            ALLERGYTYPE + "=" + "'" + name
                                    + "'", null) > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public ArrayList<AllergyWrapper> getAllergiess() {


        ArrayList<AllergyWrapper> list = new ArrayList<>();
        if (isTableExists(dbUser,TABLE_ALERGYINFO)) {
            String Query = "Select  *  from " + TABLE_ALERGYINFO;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbUser.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {
                    AllergyWrapper wrapper = new AllergyWrapper();

                    wrapper.setAllergysubstance(cursor.getString(cursor
                            .getColumnIndex(ALLERGYSUBSTANCE)));
                    wrapper.setAllergytype(cursor.getString(cursor
                            .getColumnIndex(ALLERGYTYPE)));

                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return list;
    }


    public void saveAllergy(AllergyWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {


            initialValues.put(ALLERGYSUBSTANCE, wrapper.getAllergysubstance());
            initialValues.put(ALLERGYTYPE, wrapper.getAllergytype());

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbUser.insert(TABLE_ALERGYINFO, null, initialValues);


    }

    public void saveBoneMarrowResult(BoneMarrowResultWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(DATE_BM, wrapper.getDate());
            initialValues.put(MARROWBLAST, wrapper.getMarrowblast());
            initialValues.put(DESCRIPTION, wrapper.getDescription());
            initialValues.put(NOTES_BM, wrapper.getNotes());
            initialValues.put(BONEIMAGES, wrapper.getBoneimages());


        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_BONE_MARROW, null, initialValues);


    }

    public boolean deleteBoneMarrow(String id) {
        return dbMds.delete(TABLE_BONE_MARROW, "browid" + "=" + id, null) > 0;
    }

    public ArrayList<BoneMarrowResultWrapper> getBoneMarrow() {


        ArrayList<BoneMarrowResultWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_BONE_MARROW)) {
            String Query = "Select  *  from " + TABLE_BONE_MARROW;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {

                    BoneMarrowResultWrapper wrapper = new BoneMarrowResultWrapper();

                    wrapper.setDate(cursor.getString(cursor
                            .getColumnIndex(DATE_BM)));
                    wrapper.setMarrowblast(cursor.getString(cursor
                            .getColumnIndex(MARROWBLAST)));
                    wrapper.setDescription(cursor.getString(cursor
                            .getColumnIndex(DESCRIPTION)));
                    wrapper.setNotes(cursor.getString(cursor
                            .getColumnIndex(NOTES_BM)));
                    wrapper.setBoneimages(cursor.getString(cursor
                            .getColumnIndex(BONEIMAGES)));
                    wrapper.setBrowid(cursor.getInt(cursor
                            .getColumnIndex(BROWID)));
                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return list;
    }


    public void saveBloodCount(BloodCountResultWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {


            initialValues.put(DATE_BR, wrapper.getDate());
            initialValues.put(HGB, wrapper.getHgb());
            initialValues.put(WBC, wrapper.getWbc());
            initialValues.put(ANC, wrapper.getAnc());
            initialValues.put(PLATELETS, wrapper.getPlatelets());
//            initialValues.put(RBCS, wrapper.getRbcs());
            initialValues.put(DATE_TO_ORDER, "");
            initialValues.put(NOTES_BR, wrapper.getNotes());
            initialValues.put(FERRITIN, wrapper.getFerritin());
//            initialValues.put(BLOOD_TYPE, wrapper.getBlood_type());
            initialValues.put(BLOODNOTES, wrapper.getBlood_notes());
            initialValues.put(BLOODCOUNTIMAGES, wrapper.getImage_path());

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_BLOOD_RESULT, null, initialValues);


    }

    public boolean deleteTreatMentInfo(String id) {
        return dbMds.delete(TABLE_TREATEMENTINFO, "trowid" + "=" + id, null) > 0;
    }

    public ArrayList<TreatmentInfoWrapper> getTreatMentInfo() {


        ArrayList<TreatmentInfoWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_TREATEMENTINFO)) {
            String Query = "Select  *  from " + TABLE_TREATEMENTINFO;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {

                    TreatmentInfoWrapper wrapper = new TreatmentInfoWrapper();

                    wrapper.setStartdate(cursor.getString(cursor
                            .getColumnIndex(STARTDATE)));
                    wrapper.setEnddate(cursor.getString(cursor
                            .getColumnIndex(ENDDATE)));
                    wrapper.setTreatement(cursor.getString(cursor
                            .getColumnIndex(TREATEMENT)));
                    wrapper.setNotes(cursor.getString(cursor
                            .getColumnIndex(NOTES)));
                    wrapper.setTrowid(cursor.getInt(cursor
                            .getColumnIndex(TROWID)));

                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return list;
    }


    public void saveTreatMentInfo(TreatmentInfoWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(STARTDATE, wrapper.getStartdate());
            initialValues.put(ENDDATE, wrapper.getEnddate());
            initialValues.put(TREATEMENT, wrapper.getTreatement());
            initialValues.put(NOTES, wrapper.getNotes());


        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_TREATEMENTINFO, null, initialValues);


    }

    public int getTreatMentID(String treamentname) {
        int treatment_id = 0;
        if (isTableExists(dbMds,TABLE_TREATEMENTINFO)) {
            String Query = "Select  *  from " + TABLE_TREATEMENTINFO + " where " + NOTES + " = '" + treamentname + "'";

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {
                    treatment_id = cursor.getInt(cursor.getColumnIndex("trowid"));

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return treatment_id;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return treatment_id;


    }

    public void saveTreatMentMedicineInfo(ArrayList<TreatmentMedicineInfoWrapper> wrapper, int treatment_id) {


        ContentValues initialValues = new ContentValues();
        try {
            for (int i = 0; i < wrapper.size(); i++) {
                initialValues.put(TREATMENTID, treatment_id);
                initialValues.put(MEDICINENAME, wrapper.get(i).getMedicinename());
                initialValues.put(TREATMENTDOSAGE, wrapper.get(i).getDosage());
                initialValues.put(TREATMENTNOTES, wrapper.get(i).getNotes());
                initialValues.put(TREATMENTDATE, wrapper.get(i).getDate());
                initialValues.put(DAYS, wrapper.get(i).getDays());
                initialValues.put(TREATMENTTYPE, wrapper.get(i).getType());
                initialValues.put(CYCLENUMBER, wrapper.get(i).getCyclenumber());
                initialValues.put(OTHERTREATMENTNAME, wrapper.get(i).getOthertreatmentname());
                dbMds.insert(TABLE_TREATMENTMEDICINEINFO, null, initialValues);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public boolean updateTreatMentMedicineInfo(ArrayList<TreatmentMedicineInfoWrapper> wrapper, int treatment_id) {

        ContentValues initialValues = new ContentValues();
        try {
            for (int i = 0; i < wrapper.size(); i++) {
                initialValues.put(TREATMENTID, treatment_id);
                initialValues.put(MEDICINENAME, wrapper.get(i).getMedicinename());
                initialValues.put(TREATMENTDOSAGE, wrapper.get(i).getDosage());
                initialValues.put(TREATMENTNOTES, wrapper.get(i).getNotes());
                initialValues.put(TREATMENTDATE, wrapper.get(i).getDate());
                initialValues.put(DAYS, wrapper.get(i).getDays());
                initialValues.put(TREATMENTTYPE, wrapper.get(i).getType());
                initialValues.put(CYCLENUMBER, wrapper.get(i).getCyclenumber());
                return dbMds
                        .update(TABLE_TREATMENTMEDICINEINFO, initialValues,
                                TREATMENTID + "=" + treatment_id
                                , null) > 0;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public boolean deleteTreatMentMedicineInfo(String id) {
        return dbMds.delete(TABLE_TREATMENTMEDICINEINFO, "trowid" + "=" + id, null) > 0;
    }
    public ArrayList<TreatmentMedicineInfoWrapper> getTreatMentMedicineInfo(int treatment_id) {

        ArrayList<TreatmentMedicineInfoWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_TREATMENTMEDICINEINFO)) {
//        String Query = "Select  *  from " + TABLE_TREATMENTMEDICINEINFO;
            String Query = "Select  *  from " + TABLE_TREATMENTMEDICINEINFO + " where " + "treatementid" + " = " + treatment_id;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {

                    TreatmentMedicineInfoWrapper wrapper = new TreatmentMedicineInfoWrapper();

                    wrapper.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                    wrapper.setMedicinename(cursor.getString(cursor.getColumnIndex(MEDICINENAME)));
                    wrapper.setDosage(cursor.getString(cursor.getColumnIndex(TREATMENTDOSAGE)));
                    wrapper.setDays(cursor.getString(cursor.getColumnIndex(DAYS)));
                    wrapper.setCyclenumber(cursor.getString(cursor.getColumnIndex(CYCLENUMBER)));
                    wrapper.setDate(cursor.getString(cursor.getColumnIndex(TREATMENTDATE)));
                    wrapper.setTreatementid(cursor.getInt(cursor.getColumnIndex(TREATMENTID)));
                    wrapper.setNotes(cursor.getString(cursor.getColumnIndex(TREATMENTNOTES)));
                    wrapper.setId(cursor.getString(cursor.getColumnIndex("trowid")));
                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return list;
    }

    public boolean updateBoneMarrowData(BoneMarrowResultWrapper wrapper, int id) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(DATE_BM, wrapper.getDate());
            initialValues.put(MARROWBLAST, wrapper.getMarrowblast());
            initialValues.put(DESCRIPTION, wrapper.getDescription());
            initialValues.put(NOTES_BM, wrapper.getNotes());
            initialValues.put(BONEIMAGES, wrapper.getBoneimages());

            return dbMds
                    .update(TABLE_BONE_MARROW, initialValues,
                            BROWID + "=" + id
                            , null) > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public boolean updateTreatmentData(TreatmentInfoWrapper wrapper, int id) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(STARTDATE, wrapper.getStartdate());
            initialValues.put(ENDDATE, wrapper.getEnddate());
            initialValues.put(TREATEMENT, wrapper.getTreatement());
            initialValues.put(NOTES, wrapper.getNotes());
            return dbMds
                    .update(TABLE_TREATEMENTINFO, initialValues,
                            TROWID + "=" + id
                            , null) > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public boolean updateBloodCountData(BloodCountResultWrapper wrapper, int id) {

        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(DATE_BR, wrapper.getDate());
            initialValues.put(HGB, wrapper.getHgb());
            initialValues.put(WBC, wrapper.getWbc());
            initialValues.put(ANC, wrapper.getAnc());
            initialValues.put(PLATELETS, wrapper.getPlatelets());
//            initialValues.put(RBCS, wrapper.getRbcs());
//            initialValues.put(TRANFUSION, wrapper.getTranfusion());
            initialValues.put(NOTES_BR, wrapper.getNotes());
            initialValues.put(FERRITIN, wrapper.getFerritin());
//            initialValues.put(BLOOD_TYPE, wrapper.getBlood_type());
            initialValues.put(BLOODNOTES, wrapper.getBlood_notes());
            initialValues.put(BLOODCOUNTIMAGES, wrapper.getImage_path());
            return dbMds
                    .update(TABLE_BLOOD_RESULT, initialValues,
                            BROID + "=" + id
                            , null) > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public boolean deleteBloodCount(String id) {
        return dbMds.delete(TABLE_BLOOD_RESULT, "broid" + "=" + id, null) > 0;
    }
    public boolean deleteUserdata(String id) {
        return dbUser.delete(TABLE_USERINFO, "uid" + "=" + id, null) > 0;
    }
    public ArrayList<BloodCountResultWrapper> getBloodCount() {


        ArrayList<BloodCountResultWrapper> list = new ArrayList<>();
        if (isTableExists(dbMds,TABLE_BLOOD_RESULT)) {
            String Query = "Select  *  from " + TABLE_BLOOD_RESULT;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {


                    BloodCountResultWrapper wrapper = new BloodCountResultWrapper();

                    wrapper.setDate(cursor.getString(cursor
                            .getColumnIndex(DATE_BR)));
                    wrapper.setHgb(cursor.getString(cursor
                            .getColumnIndex(HGB)));
                    wrapper.setWbc(cursor.getString(cursor
                            .getColumnIndex(WBC)));
                    wrapper.setAnc(cursor.getString(cursor
                            .getColumnIndex(ANC)));
                    wrapper.setPlatelets(cursor.getString(cursor
                            .getColumnIndex(PLATELETS)));
//                    wrapper.setRbcs(cursor.getString(cursor
//                            .getColumnIndex(RBCS)));
                    wrapper.setNotes(cursor.getString(cursor
                            .getColumnIndex(NOTES_BR)));
                    wrapper.setBroid(cursor.getInt(cursor
                            .getColumnIndex(BROID)));
                    wrapper.setFerritin(cursor.getString(cursor
                            .getColumnIndex(FERRITIN)));
//                    wrapper.setTranfusion(cursor.getString(cursor
//                            .getColumnIndex(TRANFUSION)));
//                wrapper.setBlood_type(cursor.getString(cursor.getColumnIndex(BLOOD_TYPE)));
                    wrapper.setBlood_notes(cursor.getString(cursor.getColumnIndex(BLOODNOTES)));
                    wrapper.setImage_path(cursor.getString(cursor.getColumnIndex(BLOODCOUNTIMAGES)));
                    list.add(wrapper);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return list;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return list;
    }


    public UserDetailsWrapper getUserDetails() {

        UserDetailsWrapper wrapper = new UserDetailsWrapper();
        if (isTableExists(dbUser,TABLE_USERINFO)) {
//            String Query = "Select  *  from " + TABLE_USERINFO + " where " + UID + " = " + "1";
            String Query = "Select  *  from " + TABLE_USERINFO + " where " + UID + " = '" + 1 + "'";
//            String Query = "Select  *  from " + TABLE_USERINFO;
//        + " where " + "treatementid" + " = " + treatment_id;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbUser.rawQuery(Query, null);
            System.out.println("cursor.getCount()" + cursor.getCount());

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {
                    wrapper.setUserId(cursor.getString(cursor
                            .getColumnIndex(UID)));
                    wrapper.setAddressline1(cursor.getString(cursor
                            .getColumnIndex(ADDRESSLINE1)));
                    wrapper.setAddressline2(cursor.getString(cursor
                            .getColumnIndex(ADDRESSLINE2)));
                    wrapper.setAddressline3(cursor.getString(cursor
                            .getColumnIndex(ADDRESSLINE3)));
                    wrapper.setBloodgroup(cursor.getString(cursor
                            .getColumnIndex(BLOODGROUP)));
                    wrapper.setCity(cursor.getString(cursor
                            .getColumnIndex(CITY)));
                    wrapper.setContactcell(cursor.getString(cursor
                            .getColumnIndex(CONTACTCELL)));
                    wrapper.setContacthome(cursor.getString(cursor
                            .getColumnIndex(CONTACTHOME)));
                    wrapper.setContactwork(cursor.getString(cursor
                            .getColumnIndex(CONTACTWORK)));
                    wrapper.setDob(cursor.getString(cursor
                            .getColumnIndex(DOB)));
                    wrapper.setEmailid(cursor.getString(cursor
                            .getColumnIndex(EMAILID)));
                    wrapper.setHeight(cursor.getString(cursor
                            .getColumnIndex(HEIGHT)));
                    wrapper.setLivingstatus(cursor.getString(cursor
                            .getColumnIndex(LIVINGSTATUS)));
                    wrapper.setMaritalstatus(cursor.getString(cursor
                            .getColumnIndex(MARITALSTATUS)));
                    wrapper.setName(cursor.getString(cursor
                            .getColumnIndex(NAME)));
                    wrapper.setState(cursor.getString(cursor
                            .getColumnIndex(STATE)));
                    wrapper.setSsn(cursor.getString(cursor
                            .getColumnIndex(SSN)));
                    wrapper.setWeight(cursor.getString(cursor
                            .getColumnIndex(WEIGHT)));
                    wrapper.setZipcode(cursor.getString(cursor
                            .getColumnIndex(ZIPCODE)));
                    wrapper.setHeight(cursor.getString(cursor
                            .getColumnIndex(HEIGHT)));
                    wrapper.setGender(cursor.getString(cursor
                            .getColumnIndex(GENDER)));
                    wrapper.setCountrycodecell(cursor.getString(cursor
                            .getColumnIndex(USERCOUNTRYCODE)));
                    wrapper.setCountrycodehome(cursor.getString(cursor.getColumnIndex(USERCOUNTRYCODEHOME)));
                    wrapper.setCountrycodework(cursor.getString(cursor.getColumnIndex(USERCOUNTRYCODEWORK)));
//                wrapper.setProfile_image(cursor.getString(cursor
//                        .getColumnIndex(PROFILE_IMAGE)));

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return wrapper;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
        return wrapper;
    }


    public void saveUserDetailsp(UserDetailsWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {

            initialValues.put(UID, wrapper.getUserId());
            initialValues.put(NAME, wrapper.getName());
            initialValues.put(HEIGHT, wrapper.getHeight());
            initialValues.put(WEIGHT, wrapper.getWeight());
            initialValues.put(BLOODGROUP, wrapper.getBloodgroup());
            initialValues.put(DOB, wrapper.getDob());
            initialValues.put(MARITALSTATUS, wrapper.getMaritalstatus());
            initialValues.put(LIVINGSTATUS, wrapper.getLivingstatus());
            initialValues.put(SSN, wrapper.getSsn());
            initialValues.put(ADDRESSLINE1, wrapper.getAddressline1());
            initialValues.put(ADDRESSLINE2, wrapper.getAddressline2());
            initialValues.put(ADDRESSLINE3, wrapper.getAddressline3());
            initialValues.put(ZIPCODE, wrapper.getZipcode());
            initialValues.put(CITY, wrapper.getCity());
            initialValues.put(STATE, wrapper.getState());
            initialValues.put(EMAILID, wrapper.getEmailid());
            initialValues.put(CONTACTHOME, wrapper.getContacthome());
            initialValues.put(CONTACTWORK, wrapper.getContactwork());
            initialValues.put(CONTACTCELL, wrapper.getContactcell());
            initialValues.put(USERCOUNTRYCODE, wrapper.getCountrycodecell());
            initialValues.put(USERCOUNTRYCODEHOME, wrapper.getCountrycodehome());
            initialValues.put(USERCOUNTRYCODEWORK, wrapper.getCountrycodework());
//            initialValues.put(PROFILE_IMAGE, wrapper.getProfile_image());
            initialValues.put(GENDER, wrapper.getGender());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbUser.insert(TABLE_USERINFO, null, initialValues);


    }

    public boolean updateUserDetailsp(UserDetailsWrapper wrapper,int id) {


        ContentValues initialValues = new ContentValues();
        try {

            initialValues.put(NAME, wrapper.getName());
            initialValues.put(HEIGHT, wrapper.getHeight());
            initialValues.put(WEIGHT, wrapper.getWeight());
            initialValues.put(BLOODGROUP, wrapper.getBloodgroup());
            initialValues.put(DOB, wrapper.getDob());
            initialValues.put(MARITALSTATUS, wrapper.getMaritalstatus());
            initialValues.put(LIVINGSTATUS, wrapper.getLivingstatus());
            initialValues.put(SSN, wrapper.getSsn());
            initialValues.put(ADDRESSLINE1, wrapper.getAddressline1());
            initialValues.put(ADDRESSLINE2, wrapper.getAddressline2());
            initialValues.put(ADDRESSLINE3, wrapper.getAddressline3());
            initialValues.put(ZIPCODE, wrapper.getZipcode());
            initialValues.put(CITY, wrapper.getCity());
            initialValues.put(STATE, wrapper.getState());
            initialValues.put(EMAILID, wrapper.getEmailid());
            initialValues.put(CONTACTHOME, wrapper.getContacthome());
            initialValues.put(CONTACTWORK, wrapper.getContactwork());
            initialValues.put(CONTACTCELL, wrapper.getContactcell());
            initialValues.put(USERCOUNTRYCODE, wrapper.getCountrycodecell());
//            initialValues.put(PROFILE_IMAGE, wrapper.getProfile_image());
            initialValues.put(GENDER, wrapper.getGender());

            return dbUser
                    .update(TABLE_USERINFO, initialValues,
                            UID + "=" + "'"+id+"'"
                            , null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
//        dbUser.insert(TABLE_USERINFO, null, initialValues);


    }

    public HashMap<String, InsuranceWrapper> getInsuranceHashMap() {

            HashMap<String, InsuranceWrapper> insuranceHashMap = new HashMap<>();

        if (isTableExists(dbMds,TABLE_INSURANCE_DETAIL))
        {
            String Query = "Select  *  from " + TABLE_INSURANCE_DETAIL;
            System.out.println("Query>>>" + Query);

            Cursor cursor = dbMds.rawQuery(Query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                // wrapper.setStartDate(cursor.getString(cursor
                // .getColumnIndex(STARTDATETIME)));
                // wrapper.setEndDate(cursor.getString(cursor
                // .getColumnIndex(ENDDATETIME)));
                do {


                    InsuranceWrapper wrapper = new InsuranceWrapper();


                    wrapper.setAddress(cursor.getString(cursor
                            .getColumnIndex(ADDRESS)));
                    wrapper.setCompanygroup(cursor.getString(cursor
                            .getColumnIndex(COMPANYGROUP)));
                    wrapper.setCompanyimagename(cursor.getString(cursor
                            .getColumnIndex(COMPANYIMAGENAME)));
                    wrapper.setCompanyname(cursor.getString(cursor
                            .getColumnIndex(COMPANYNAME)));
                    wrapper.setCompanyoption(cursor.getString(cursor
                            .getColumnIndex(COMPANYOPTION)));
                    wrapper.setI_city(cursor.getString(cursor
                            .getColumnIndex(I_CITY)));
                    wrapper.setI_state(cursor.getString(cursor
                            .getColumnIndex(I_STATE)));
                    wrapper.setI_zipcode(cursor.getString(cursor
                            .getColumnIndex(I_ZIPCODE)));
                    wrapper.setPhoneno(cursor.getString(cursor
                            .getColumnIndex(PHONENO)));
                    wrapper.setEmployer(cursor.getString(cursor
                            .getColumnIndex(EMPLOYER)));
                    wrapper.setPrescription(cursor.getString(cursor
                            .getColumnIndex(PRESCRIPTION)));
                    wrapper.setI_zipcode(cursor.getString(cursor
                            .getColumnIndex(ZIPCODE)));
                    insuranceHashMap.put(cursor.getString(cursor
                            .getColumnIndex(COMPANYTYPE)), wrapper);


                    cursor.moveToNext();
                } while (!cursor.isAfterLast());

                return insuranceHashMap;

                // if (startdate_db >= start_date && startdate_db <= end_date) {
                // return true;
                // }

            }
        }
            return insuranceHashMap;

    }


    public void saveInsuranceHashMap(String type, InsuranceWrapper wrapper) {


        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(COMPANYTYPE, type);
            initialValues.put(COMPANYOPTION, wrapper.getCompanyoption());
            initialValues.put(COMPANYNAME, wrapper.getCompanyname());
            initialValues.put(PHONENO, wrapper.getPhoneno());
            initialValues.put(EMPLOYER, wrapper.getEmployer());
            initialValues.put(COMPANYGROUP, wrapper.getCompanygroup());
            initialValues.put(PRESCRIPTION, wrapper.getPrescription());
            initialValues.put(ADDRESS, wrapper.getAddress());
            initialValues.put(I_CITY, wrapper.getI_city());
            initialValues.put(I_STATE, wrapper.getI_state());
            initialValues.put(EMPLOYER, wrapper.getEmployer());
            initialValues.put(COMPANYIMAGENAME, wrapper.getCompanyimagename());
            initialValues.put(ZIPCODE, wrapper.getI_zipcode());


        } catch (Exception e) {
            e.printStackTrace();
        }
        dbMds.insert(TABLE_INSURANCE_DETAIL, null, initialValues);


    }

    public void openMDSMainSQLDataBase() throws SQLException {
        File dbFileMds = context.getDatabasePath(DATABASE_NAME_MDS);

        if (!dbFileMds.exists()) {
            try {
                CopyMDSDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        SQLiteDatabase.openDatabase(dbFileMds.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public void openMDSUserSQLDataBase() throws SQLException {
        File dbFileUser = context.getDatabasePath(DATABASE_NAME_USER);

        if (!dbFileUser.exists()) {
            try {
                CopyUSERDataBaseFromAsset();
                //
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }
        SQLiteDatabase.openDatabase(dbFileUser.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public DBAdapter openUSERDataBase() throws SQLException {

        File sd = Environment.getExternalStorageDirectory();
        String backupDBPath = context.getPackageName() + "/"
                + DBAdapter.DATABASE_NAME_USER;
        File backupDB = new File(sd, backupDBPath);

//        if (backupDB.exists()) {
//            openSDCardUserDataBase();
//        } else {
            openMDSUserSQLDataBase();
//        }
        dbUser = DBHelperUser.getWritableDatabase();
        return this;
    }

    public DBAdapter openMdsDB() throws SQLException {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        String backupDBPath = context.getResources().getString(R.string.app_name) + "/"
                + DBAdapter.DATABASE_NAME_MDS;
        File backupDB = new File(sd, backupDBPath);

//        if (backupDB.exists()) {
//            openMainSDCardDataBase();
//        } else {
           
            openMDSMainSQLDataBase();

//        }
        dbMds = DBHelperMds.getWritableDatabase();
        return this;
    }

    public SQLiteDatabase openMainSDCardDataBase() throws SQLException {
        File dbFile = context.getDatabasePath(DATABASE_NAME_MDS);

        if (!dbFile.exists()) {
            try {
                CopyMDSDataBaseFromSDCard();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS
                        | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public SQLiteDatabase openSDCardUserDataBase() throws SQLException {
        File dbFile = context.getDatabasePath(DATABASE_NAME_USER);

        if (!dbFile.exists()) {
        try {
            CopyMDSUserDataBaseFromSDCard();
            System.out.println("Copying sucess from Assets folder");
        } catch (IOException e) {
            throw new RuntimeException("Error creating source database", e);
        }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS
                        | SQLiteDatabase.CREATE_IF_NECESSARY);
    }
//    public void openUserDB() {
//        openUSERDataBase();
//
//
//
//    }

    // ---closes the database---
    public void closeMdsDB() {
        DBHelperMds.close();
    }

    public void closeUserDB() {
        DBHelperUser.close();
    }


    private boolean isTableExists(SQLiteDatabase db, String tableName)
    {
        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }


    public void deleteDatabase()
    {
        File sd = Environment.getExternalStorageDirectory();
        String userbackupDBPath = context.getPackageName() + "/"
                + DBAdapter.DATABASE_NAME_USER;
        File userbackupDB = new File(sd, userbackupDBPath);
        if (userbackupDB.exists())
        {
            userbackupDB.delete();
        }

        String mdsbackupDBPath = context.getResources().getString(R.string.app_name) + "/"
                + DBAdapter.DATABASE_NAME_MDS;
        File mdsbackupDB = new File(sd, mdsbackupDBPath);
        if (mdsbackupDB.exists())
        {
            mdsbackupDB.delete();
        }
        File dir = new File(Environment.getExternalStorageDirectory()+"/" + context.getResources().getString(R.string.imagepath) + "/");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }
}
