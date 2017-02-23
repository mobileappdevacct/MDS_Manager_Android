package com.mymdsmanager.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.activities.MainActivity;
import com.mymdsmanager.database.DBAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class SettingsFragment extends Fragment {
    public static TextView logoutTxt;
    public static TextView emailTxt;
    @Bind(R.id.pushSwith)
    Switch pushSwith;
    private DBAdapter dbAdapter;
    @Bind(R.id.googleLoginRl)
    RelativeLayout googleLoginRl;
    @Bind(R.id.uploadDataBtn)
    Button uploadDataBtn;
    public static boolean isUploadClicked = false;
    @Bind(R.id.exportcsvTxt)
    TextView exportcsvTxt;
    public static boolean isExportdata = false;
    public static boolean isSettingsClick = false;
    public static boolean isSettingsLogoutClick = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, null);
        ButterKnife.bind(this, v);

        logoutTxt = (TextView) v.findViewById(R.id.logoutTxt);
        emailTxt = (TextView) v.findViewById(R.id.emailTxt);
        dbAdapter = new DBAdapter(getActivity());
        getUiComponents(v);
        ((MainActivity) getActivity()).toolbar.setTitle("Settings");

        if (!TextUtils.isEmpty(MyApplication.getEmailId())) {
            emailTxt.setText("Login with "+MyApplication.getEmailId());
            logoutTxt.setText("Logout");
        }
        if (MyApplication.getPushNotify())
        {
            pushSwith.setChecked(true);
        }
        pushSwith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
             if (b)
             {
                 MyApplication.savePushNotify(true);
             }else
             {
                 MyApplication.savePushNotify(false);
             }
            }
        });
        return v;


    }

    private void getUiComponents(View v) {
    }
    @OnClick(R.id.exportcsvTxt)
    public void exportcsvTxt() {
        exportAlert();
    }
    @OnClick(R.id.uploadDataBtn)
    public void uploadDataBtn() {
//        if (!TextUtils.isEmpty(MyApplication.getEmailId())) {
//            isUploadClicked = true;
//            ((MainActivity) getActivity()).uploadAlert();
//
//        } else {
//            isUploadClicked = false;
//            ((MainActivity) getActivity()).signInWithGplus();
//        }
    }
    @OnClick(R.id.googleLoginRl)
    public void googleLoginRl() {
        if (!TextUtils.isEmpty(MyApplication.getEmailId())) {
            logoutAlert();
        } else {
            isSettingsClick = true;
            ((MainActivity) getActivity()).signInWithGplus();
        }
    }

    /**
     * Sign-in into google
     */


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void logoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("" + getResources().getString(R.string.app_name3));
        builder.setMessage("Are you sure you want to logout application")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                MainActivity.mSignInClicked = false;
                                isSettingsLogoutClick = true;
                                ((MainActivity) getActivity()).signOutFromGplus();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    //    private class ExportDatabaseCSVTask extends AsyncTask<String,Boolean,Boolean> {
//        private final ProgressDialog dialog = new ProgressDialog(getActivity());
//        File file;
//
//        @Override
//        protected void onPreExecute() {
//
//            this.dialog.setMessage("Exporting database...");
//            this.dialog.show();
//
//        }
//    protected Boolean doInBackground(final String... args){
//
//        File dbFile=new File("file:///android_asset/MDSManagerDB.sqlite");
////        Log.v(TAG, "Db path is: "+dbFile);  //get the path of db
//
//        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
//        if (!exportDir.exists()) {
//            exportDir.mkdirs();
//        }
//
//        file = new File(exportDir, "MdsManagerCSV.csv");
//        try {
//
//            file.createNewFile();
//            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//
//            //ormlite core method
//            ArrayList<AddIpssContactWrapper> list=dbAdapter.getIPSSScore();
//            AddIpssContactWrapper addIpssContactWrapper=null;
//
//            // this is the Column of the table and same for Header of CSV file
//            String arrStr1[] ={"date", "score", "notes"};
//            csvWrite.writeNext(arrStr1);
//
//            if(list.size() > 1)
//            {
//                for(int index=0; index < list.size(); index++)
//                {
//                    addIpssContactWrapper=list.get(index);
//                    String arrStr[] ={addIpssContactWrapper.getDate(), addIpssContactWrapper.getIpss_score(), addIpssContactWrapper.getNotes()};
//                    csvWrite.writeNext(arrStr);
//                }
//            }
//
//            csvWrite.close();
//            return true;
//        }
//        catch (IOException e){
//            Log.e("MainActivity", e.getMessage(), e);
//            return false;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(final Boolean success) {
//
//        if (this.dialog.isShowing()){
//            this.dialog.dismiss();
//        }
//        if (success){
//            Toast.makeText(getActivity(), "Export successful!", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(getActivity(), "Export failed!", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
    public class ExportDatabaseToCSV extends AsyncTask<Void, Boolean, Boolean> {

        Context context;
        ProgressDialog dialog;

        public ExportDatabaseToCSV(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setTitle("Exporting SecureIt Data to CSV file");
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
//        CredentialDb db = new CredentialDb(context);//here CredentialDb is my database. you can create your db object.
            return exportData();

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result)
                Toast.makeText(context, "SqLite Data has been Exported!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "SqLite Data has not Exported", Toast.LENGTH_LONG).show();
        }
    }

    public boolean exportData() {
//        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
//        File exitDir = new File(Environment.getExternalStorageDirectory(), "MdsManagerCSV.csv");
//        if (!exportDir.exists()) {
//            exportDir.mkdirs();
//        }
//        if (exitDir.exists()) {
//            exitDir.delete();
//        }
//        File file = new File(exportDir, "MdsManagerCSV.csv");
//
//        try {
//            file.createNewFile();
//            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//            SQLiteDatabase sql_db = dbAdapter.openreamdMdsDB();//returning sql
//            Cursor curIPSS = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_IPSSRSCOREINFO, null);
//            if (curIPSS.getCount() != 0) {
//                csvWrite.writeNext(curIPSS.getColumnNames());
//            }
//
//            while (curIPSS.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curIPSS.getString(0), curIPSS.getString(1), curIPSS.getString(2), curIPSS.getString(3)};
//                csvWrite.writeNext(arrStr);
//            }
//            curIPSS.close();
//
//            Cursor curTREATMENTMEDICINEINFO = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_TREATMENTMEDICINEINFO, null);
//            if (curTREATMENTMEDICINEINFO.getCount() != 0) {
//                csvWrite.writeNext(curTREATMENTMEDICINEINFO.getColumnNames());
//            }
//
//            while (curTREATMENTMEDICINEINFO.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curTREATMENTMEDICINEINFO.getString(0), curTREATMENTMEDICINEINFO.getString(1), curTREATMENTMEDICINEINFO.getString(2), curTREATMENTMEDICINEINFO.getString(3)
//                        , curTREATMENTMEDICINEINFO.getString(4), curTREATMENTMEDICINEINFO.getString(5), curTREATMENTMEDICINEINFO.getString(6), curTREATMENTMEDICINEINFO.getString(7), curTREATMENTMEDICINEINFO.getString(8)};
//                csvWrite.writeNext(arrStr);
//            }
//            curTREATMENTMEDICINEINFO.close();
//
//            Cursor curBLOOD_RESULT = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_BLOOD_RESULT, null);
//            if (curBLOOD_RESULT.getCount() != 0) {
//                csvWrite.writeNext(curBLOOD_RESULT.getColumnNames());
//            }
//
//            while (curBLOOD_RESULT.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curBLOOD_RESULT.getString(0), curBLOOD_RESULT.getString(1), curBLOOD_RESULT.getString(2), curBLOOD_RESULT.getString(3)
//                        , curBLOOD_RESULT.getString(4), curBLOOD_RESULT.getString(5), curBLOOD_RESULT.getString(6), curBLOOD_RESULT.getString(7), curBLOOD_RESULT.getString(8), curBLOOD_RESULT.getString(9), curBLOOD_RESULT.getString(10)};
//                csvWrite.writeNext(arrStr);
//            }
//            curBLOOD_RESULT.close();
//
//            Cursor curMEDICAL_PROFESSIONAL = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_MEDICAL_PROFESSIONAL, null);
//            if (curMEDICAL_PROFESSIONAL.getCount() != 0) {
//                csvWrite.writeNext(curMEDICAL_PROFESSIONAL.getColumnNames());
//            }
//
//            while (curMEDICAL_PROFESSIONAL.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curMEDICAL_PROFESSIONAL.getString(0), curMEDICAL_PROFESSIONAL.getString(1), curMEDICAL_PROFESSIONAL.getString(2), curMEDICAL_PROFESSIONAL.getString(3)
//                        , curMEDICAL_PROFESSIONAL.getString(4), curMEDICAL_PROFESSIONAL.getString(5), curMEDICAL_PROFESSIONAL.getString(6), curMEDICAL_PROFESSIONAL.getString(7), curMEDICAL_PROFESSIONAL.getString(8)};
//                csvWrite.writeNext(arrStr);
//            }
//            curMEDICAL_PROFESSIONAL.close();
//
//            Cursor curMEDICINEINFO = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_MEDICINEINFO, null);
//            if (curMEDICINEINFO.getCount() != 0) {
//                csvWrite.writeNext(curMEDICINEINFO.getColumnNames());
//            }
//
//            while (curMEDICINEINFO.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curMEDICINEINFO.getString(0), curMEDICINEINFO.getString(1), curMEDICINEINFO.getString(2), curMEDICINEINFO.getString(3)
//                        , curMEDICINEINFO.getString(4), curMEDICINEINFO.getString(5), curMEDICINEINFO.getString(6), curMEDICINEINFO.getString(7), curMEDICINEINFO.getString(8), curMEDICINEINFO.getString(9), curMEDICINEINFO.getString(10), curMEDICINEINFO.getString(11), curMEDICINEINFO.getString(12), curMEDICINEINFO.getString(13), curMEDICINEINFO.getString(14), curMEDICINEINFO.getString(15), curMEDICINEINFO.getString(16), curMEDICINEINFO.getString(17), curMEDICINEINFO.getString(18), curMEDICINEINFO.getString(19)};
//                csvWrite.writeNext(arrStr);
//            }
//            curMEDICINEINFO.close();
//            dbAdapter.closeMdsDB();
//            SQLiteDatabase sql_dbuser = dbAdapter.openreadUSER();//returning sql
//            Cursor curUSERINFO = sql_dbuser.rawQuery("SELECT * FROM " + dbAdapter.TABLE_USERINFO, null);
//            if (curUSERINFO.getCount() != 0) {
//                csvWrite.writeNext(curUSERINFO.getColumnNames());
//            }
//
//            while (curUSERINFO.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curUSERINFO.getString(0), curUSERINFO.getString(1), curUSERINFO.getString(2), curUSERINFO.getString(3)
//                        , curUSERINFO.getString(4), curUSERINFO.getString(5), curUSERINFO.getString(6), curUSERINFO.getString(7), curUSERINFO.getString(8),
//                        curUSERINFO.getString(9), curUSERINFO.getString(10), curUSERINFO.getString(11),
//                        curUSERINFO.getString(12), curUSERINFO.getString(13), curUSERINFO.getString(14),
//                        curUSERINFO.getString(15), curUSERINFO.getString(16), curUSERINFO.getString(17),
//                        curUSERINFO.getString(18), curUSERINFO.getString(19), curUSERINFO.getString(20), curUSERINFO.getString(21)
//                        , curUSERINFO.getString(22)};
//                csvWrite.writeNext(arrStr);
//            }
//            curUSERINFO.close();
//
//            dbAdapter.closeUserDB();
//            sql_db = dbAdapter.openreamdMdsDB();
//            Cursor curINSURANCE_DETAIL = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_INSURANCE_DETAIL, null);
//            if (curINSURANCE_DETAIL.getCount() != 0) {
//                csvWrite.writeNext(curINSURANCE_DETAIL.getColumnNames());
//            }
//
//            while (curINSURANCE_DETAIL.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curINSURANCE_DETAIL.getString(0), curINSURANCE_DETAIL.getString(1), curINSURANCE_DETAIL.getString(2), curINSURANCE_DETAIL.getString(3)
//                        , curINSURANCE_DETAIL.getString(4), curINSURANCE_DETAIL.getString(5), curINSURANCE_DETAIL.getString(6), curINSURANCE_DETAIL.getString(7), curINSURANCE_DETAIL.getString(8),
//                        curINSURANCE_DETAIL.getString(9), curINSURANCE_DETAIL.getString(10), curINSURANCE_DETAIL.getString(11)};
//                csvWrite.writeNext(arrStr);
//            }
//            curINSURANCE_DETAIL.close();
//
////            Cursor curCAREGIVERSINFO = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_CAREGIVERSINFO, null);
////            csvWrite.writeNext(curCAREGIVERSINFO.getColumnNames());
////
////            while (curCAREGIVERSINFO.moveToNext()) {
////                //Which column you want to export you can add over here...
////                String arrStr[] = {curCAREGIVERSINFO.getString(0), curCAREGIVERSINFO.getString(1), curCAREGIVERSINFO.getString(2)};
////                csvWrite.writeNext(arrStr);
////            }
////            curCAREGIVERSINFO.close();
////
////            Cursor curALERGYINFO = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_ALERGYINFO, null);
////            csvWrite.writeNext(curALERGYINFO.getColumnNames());
////
////            while (curALERGYINFO.moveToNext()) {
////                //Which column you want to export you can add over here...
////                String arrStr[] = {curALERGYINFO.getString(0), curALERGYINFO.getString(1)};
////                csvWrite.writeNext(arrStr);
////            }
////            curALERGYINFO.close();
//
//
//            Cursor curBONE_MARROW = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_BONE_MARROW, null);
//            if (curBONE_MARROW.getCount() != 0) {
//                csvWrite.writeNext(curBONE_MARROW.getColumnNames());
//            }
//
//            while (curBONE_MARROW.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curBONE_MARROW.getString(0), curBONE_MARROW.getString(1),
//                        curBONE_MARROW.getString(2), curBONE_MARROW.getString(3), curBONE_MARROW.getString(4)};
//                csvWrite.writeNext(arrStr);
//            }
//            curBONE_MARROW.close();
//
//
//            Cursor curSYMPTOMINFO = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_SYMPTOMINFO, null);
//            if (curSYMPTOMINFO.getCount() != 0) {
//                csvWrite.writeNext(curSYMPTOMINFO.getColumnNames());
//            }
//
//            while (curSYMPTOMINFO.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curSYMPTOMINFO.getString(0), curSYMPTOMINFO.getString(1),
//                        curSYMPTOMINFO.getString(2), curSYMPTOMINFO.getString(3), curSYMPTOMINFO.getString(4), curSYMPTOMINFO.getString(5)};
//                csvWrite.writeNext(arrStr);
//            }
//            curSYMPTOMINFO.close();
//
////            Cursor curNOTESINFO = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_NOTESINFO, null);
////            csvWrite.writeNext(curNOTESINFO.getColumnNames());
////
////            while (curNOTESINFO.moveToNext()) {
////                //Which column you want to export you can add over here...
////                String arrStr[] = {curNOTESINFO.getString(0), curSYMPTOMINFO.getString(1),curNOTESINFO.getString(2),curNOTESINFO.getString(3)};
////                csvWrite.writeNext(arrStr);
////            }
////            curNOTESINFO.close();
//
//            Cursor curMEDICALDIAGNOSISHISTORY = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_MEDICALDIAGNOSISHISTORY, null);
//            if (curMEDICALDIAGNOSISHISTORY.getCount() != 0) {
//                csvWrite.writeNext(curMEDICALDIAGNOSISHISTORY.getColumnNames());
//            }
//
//            while (curMEDICALDIAGNOSISHISTORY.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curMEDICALDIAGNOSISHISTORY.getString(0), curMEDICALDIAGNOSISHISTORY.getString(1), curMEDICALDIAGNOSISHISTORY.getString(2), curMEDICALDIAGNOSISHISTORY.getString(3)};
//                csvWrite.writeNext(arrStr);
//            }
//            curMEDICALDIAGNOSISHISTORY.close();
//
//            Cursor curAPPOINTMENTSINFO = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_APPOINTMENTSINFO, null);
//            if (curAPPOINTMENTSINFO.getCount() != 0) {
//                csvWrite.writeNext(curAPPOINTMENTSINFO.getColumnNames());
//            }
//
//            while (curAPPOINTMENTSINFO.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curAPPOINTMENTSINFO.getString(0), curAPPOINTMENTSINFO.getString(1), curAPPOINTMENTSINFO.getString(2), curAPPOINTMENTSINFO.getString(3), curAPPOINTMENTSINFO.getString(4), curAPPOINTMENTSINFO.getString(5)};
//                csvWrite.writeNext(arrStr);
//            }
//            curAPPOINTMENTSINFO.close();
//
//            Cursor curLABRESULTINFO = sql_db.rawQuery("SELECT * FROM " + dbAdapter.TABLE_LABRESULTINFO, null);
//            if (curLABRESULTINFO.getCount() != 0) {
//                csvWrite.writeNext(curLABRESULTINFO.getColumnNames());
//            }
//
//
//            while (curLABRESULTINFO.moveToNext()) {
//                //Which column you want to export you can add over here...
//                String arrStr[] = {curLABRESULTINFO.getString(0), curLABRESULTINFO.getString(1), curLABRESULTINFO.getString(2), curLABRESULTINFO.getString(3), curLABRESULTINFO.getString(4), curLABRESULTINFO.getString(5)};
//                csvWrite.writeNext(arrStr);
//            }
//            curLABRESULTINFO.close();
//            csvWrite.close();
//            Uri u1 = null;
//            u1 = Uri.fromFile(file);
//
//            Intent sendIntent = new Intent(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Exported MDS Data");
//            sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
//            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{MyApplication.getEmailId()});
//            sendIntent.setType("text/html");
//            startActivity(sendIntent);
//            return true;
//        } catch (Exception sqlEx) {
//            Log.e("Error:", sqlEx.getMessage(), sqlEx);
//        }
        return false;
    }

    public void exportAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("" + getResources().getString(R.string.app_name));
        builder.setMessage("Are you sure you want to export data now.")
                .setCancelable(false)
                .setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (!TextUtils.isEmpty(MyApplication.getEmailId())) {
                                    new ExportDatabaseToCSV(getActivity()).execute();

                                } else {
                                    isExportdata = true;
                                    ((MainActivity) getActivity()).signInWithGplus();
                                }

                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}

