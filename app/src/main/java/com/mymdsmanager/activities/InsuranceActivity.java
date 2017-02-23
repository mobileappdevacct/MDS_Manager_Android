package com.mymdsmanager.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.Constants;
import com.mymdsmanager.wrapper.InsuranceWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.janmuller.android.simplecropimage.CropImage;

/**
 * Created by nitin on 22/7/15.
 */
public class InsuranceActivity extends AppCompatActivity {


    @Bind(R.id.editImgBtn_P)
    ImageButton editImgBtnP;
    @Bind(R.id.showHideImgBtn_P)
    ImageButton showHideImgBtnP;
    @Bind(R.id.insuranceImgView)
    ImageView insuranceImgView;
    @Bind(R.id.optionTxt)
    TextView optionTxt;
    @Bind(R.id.optionEdt)
    EditText optionEdt;
    @Bind(R.id.companyNameTxt)
    TextView companyNameTxt;
    @Bind(R.id.companyNameEdt)
    EditText companyNameEdt;
    @Bind(R.id.phoneNoTxt)
    TextView phoneNoTxt;
    @Bind(R.id.phoneNoEdt)
    EditText phoneNoEdt;
    @Bind(R.id.employerTxt)
    TextView employerTxt;
    @Bind(R.id.employerEdt)
    EditText employerEdt;
    @Bind(R.id.groupTxt)
    TextView groupTxt;
    @Bind(R.id.groupEdt)
    EditText groupEdt;
    @Bind(R.id.prescriptionTxt)
    TextView prescriptionTxt;
    @Bind(R.id.prescriptionEdt)
    EditText prescriptionEdt;
    @Bind(R.id.addressTxt)
    TextView addressTxt;
    @Bind(R.id.addressEdt)
    EditText addressEdt;
    @Bind(R.id.cityTxt)
    TextView cityTxt;
    @Bind(R.id.cityEdt)
    EditText cityEdt;
    @Bind(R.id.stateTxt)
    TextView stateTxt;
    @Bind(R.id.stateEdt)
    EditText stateEdt;
    @Bind(R.id.zipCodeTxt)
    TextView zipCodeTxt;
    @Bind(R.id.zipCodeEdt)
    EditText zipCodeEdt;
    @Bind(R.id.primaryInsurance_ll)
    LinearLayout primaryInsuranceLl;
    @Bind(R.id.editImgBtn_S)
    ImageButton editImgBtnS;
    @Bind(R.id.showHideImgBtn_S)
    ImageButton showHideImgBtnS;
    @Bind(R.id.insuranceImgView_S)
    ImageView insuranceImgViewS;
    @Bind(R.id.optionTxt_S)
    TextView optionTxtS;
    @Bind(R.id.optionEdt_S)
    EditText optionEdtS;
    @Bind(R.id.companyNameTxt_S)
    TextView companyNameTxtS;
    @Bind(R.id.companyNameEdt_S)
    EditText companyNameEdtS;
    @Bind(R.id.phoneNoTxt_S)
    TextView phoneNoTxtS;
    @Bind(R.id.phoneNoEdt_S)
    EditText phoneNoEdtS;
    @Bind(R.id.employerTxt_S)
    TextView employerTxtS;
    @Bind(R.id.employerEdt_S)
    EditText employerEdtS;
    @Bind(R.id.groupTxt_S)
    TextView groupTxtS;
    @Bind(R.id.groupEdt_S)
    EditText groupEdtS;
    @Bind(R.id.prescriptionTxt_S)
    TextView prescriptionTxtS;
    @Bind(R.id.prescriptionEdt_S)
    EditText prescriptionEdtS;
    @Bind(R.id.addressTxt_S)
    TextView addressTxtS;
    @Bind(R.id.addressEdt_S)
    EditText addressEdtS;
    @Bind(R.id.cityTxt_S)
    TextView cityTxtS;
    @Bind(R.id.cityEdt_S)
    EditText cityEdtS;
    @Bind(R.id.stateTxt_S)
    TextView stateTxtS;
    @Bind(R.id.stateEdt_S)
    EditText stateEdtS;
    @Bind(R.id.zipCodeTxt_S)
    TextView zipCodeTxtS;
    @Bind(R.id.zipCodeEdt_S)
    EditText zipCodeEdtS;
    @Bind(R.id.secondryInsurance_ll)
    LinearLayout secondryInsuranceLl;
    private Toolbar toolbar;

    private boolean isEditMode_P = false, isEditMode_S = false;
    private DBAdapter dBAdapter;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x53;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x52;
    public static final int REQUEST_CODE_GALLERY = 0x51;
    private File mFileTemp;

    private boolean isPrimaryImageSelected = false;
    private String imagePathPrimary = "";
    private String imagePathSecondry = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUiComponents();
        getPrimaryValues();
        getSecondryValues();
    }

    private void getUiComponents() {

        setContentView(R.layout.activity_insurance);
        ButterKnife.bind(this);

        dBAdapter = new DBAdapter(this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.mipmap.icn_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
        getSupportActionBar().setTitle("Insurance");


    }

    private void setimagepath() {

        String fileName = "IMG_"
                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
                .toString() + ".jpg";
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            File sdIconStorageDir = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + getResources().getString(R.string.app_name) + "/");
            // create storage directories, if they don't exist
            sdIconStorageDir.mkdirs();

            mFileTemp = new File(Environment.getExternalStorageDirectory()
                    + "/" + getResources().getString(R.string.app_name) + "/",
                    fileName);
        } else {
            mFileTemp = new File(getFilesDir(), fileName);
        }
    }

    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }


    private void startCropImage() {
        try {
            System.out.println("on activity result startcrop functions");
            Intent intent = new Intent(InsuranceActivity.this, CropImage.class);
            intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
            intent.putExtra(CropImage.SCALE, true);

            intent.putExtra(CropImage.ASPECT_X, 5);
            intent.putExtra(CropImage.ASPECT_Y, 3);

            startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            mImageCaptureUri = Uri.fromFile(mFileTemp);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);

        } catch (ActivityNotFoundException e) {

            Log.d("", "cannot take picture", e);
        }

    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public Uri getOutputMediaFileUri() {

        return Uri.fromFile(mFileTemp);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode != RESULT_OK) {

                return;
            }

            Bitmap bitmap;

            if (requestCode == REQUEST_CODE_GALLERY) {

                try {

                    System.out.println("on activity result gallery");
                    InputStream inputStream = getContentResolver()
                            .openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {

                startCropImage();

            } else if (requestCode == REQUEST_CODE_CROP_IMAGE) {

                System.out.println("on activity result crop");
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {

                    return;
                }

                if (isPrimaryImageSelected) {

                    imagePathPrimary = mFileTemp.getPath();
                    MyApplication.getApplication().loader.displayImage("file://"
                            + imagePathPrimary, insuranceImgView, MyApplication.options);
                } else {
                    imagePathSecondry = mFileTemp.getPath();

                    MyApplication.getApplication().loader.displayImage("file://"
                            + imagePathSecondry, insuranceImgViewS, MyApplication.options);
                }
                System.out.println("image path:" + mFileTemp.getPath());
            }

            // bundle2.putString("path", mFileTemp.getPath());


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void uploadImage() {
        setimagepath();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                InsuranceActivity.this);

        // set title
        alertDialogBuilder.setTitle("Choose option");

        // set dialog message
        alertDialogBuilder
                .setMessage("Please select image from ")
                .setCancelable(false)
                .setPositiveButton("Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, closeMdsDB
                                // current activity
                                dialog.cancel();
                                takePicture();

                            }

                        })
                .setNegativeButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just closeMdsDB
                                // the dialog box and do nothing
                                dialog.cancel();
                                openGallery();
                            }

                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setCancelable(true);
        // show it
        alertDialog.show();
    }

    @OnClick(R.id.insuranceImgView)

    public void insuranceImgView() {
        isPrimaryImageSelected = true;
        uploadImage();
    }


    @OnClick(R.id.insuranceImgView_S)

    public void insuranceImgView_S() {
        isPrimaryImageSelected = false;
        uploadImage();
    }


    @OnClick(R.id.editImgBtn_P)
    public void editImgBtn_P() {

        isEditMode_P = !isEditMode_P;
        if (isEditMode_P) {
            editImgBtnP.setImageResource(R.mipmap.icn_save);

            optionEdt.setVisibility(View.VISIBLE);
            companyNameEdt.setVisibility(View.VISIBLE);
            phoneNoEdt.setVisibility(View.VISIBLE);
            employerEdt.setVisibility(View.VISIBLE);
            groupEdt.setVisibility(View.VISIBLE);
            prescriptionEdt.setVisibility(View.VISIBLE);
            addressEdt.setVisibility(View.VISIBLE);
            cityEdt.setVisibility(View.VISIBLE);
            stateEdt.setVisibility(View.VISIBLE);
            zipCodeEdt.setVisibility(View.VISIBLE);
            primaryInsuranceLl.setVisibility(View.VISIBLE);
            optionTxt.setVisibility(View.GONE);
            companyNameTxt.setVisibility(View.GONE);
            phoneNoTxt.setVisibility(View.GONE);
            employerTxt.setVisibility(View.GONE);
            groupTxt.setVisibility(View.GONE);
            prescriptionTxt.setVisibility(View.GONE);
            addressTxt.setVisibility(View.GONE);
            cityTxt.setVisibility(View.GONE);
            stateTxt.setVisibility(View.GONE);
            zipCodeTxt.setVisibility(View.GONE);


        } else {
            editImgBtnP.setImageResource(R.mipmap.icn_edit_aboutlabel);

            optionEdt.setVisibility(View.GONE);
            companyNameEdt.setVisibility(View.GONE);
            phoneNoEdt.setVisibility(View.GONE);
            employerEdt.setVisibility(View.GONE);
            groupEdt.setVisibility(View.GONE);
            prescriptionEdt.setVisibility(View.GONE);
            addressEdt.setVisibility(View.GONE);
            cityEdt.setVisibility(View.GONE);
            stateEdt.setVisibility(View.GONE);
            zipCodeEdt.setVisibility(View.GONE);

            optionTxt.setVisibility(View.VISIBLE);
            companyNameTxt.setVisibility(View.VISIBLE);
            phoneNoTxt.setVisibility(View.VISIBLE);
            employerTxt.setVisibility(View.VISIBLE);
            groupTxt.setVisibility(View.VISIBLE);
            prescriptionTxt.setVisibility(View.VISIBLE);
            addressTxt.setVisibility(View.VISIBLE);
            cityTxt.setVisibility(View.VISIBLE);
            stateTxt.setVisibility(View.VISIBLE);
            zipCodeTxt.setVisibility(View.VISIBLE);

            setPrimaryValues();
            getPrimaryValues();

        }

    }


    @OnClick(R.id.editImgBtn_S)
    public void editImgBtn_S() {

        isEditMode_S = !isEditMode_S;
        if (isEditMode_S) {
//                editImgBtn_P.setImageResource(R.mipmap.);
            editImgBtnS.setImageResource(R.mipmap.icn_save);

            optionEdtS.setVisibility(View.VISIBLE);
            companyNameEdtS.setVisibility(View.VISIBLE);
            phoneNoEdtS.setVisibility(View.VISIBLE);
            employerEdtS.setVisibility(View.VISIBLE);
            groupEdtS.setVisibility(View.VISIBLE);
            prescriptionEdtS.setVisibility(View.VISIBLE);
            addressEdtS.setVisibility(View.VISIBLE);
            cityEdtS.setVisibility(View.VISIBLE);
            stateEdtS.setVisibility(View.VISIBLE);
            zipCodeEdtS.setVisibility(View.VISIBLE);
            secondryInsuranceLl.setVisibility(View.VISIBLE);
            optionTxtS.setVisibility(View.GONE);
            companyNameTxtS.setVisibility(View.GONE);
            phoneNoTxtS.setVisibility(View.GONE);
            employerTxtS.setVisibility(View.GONE);
            groupTxtS.setVisibility(View.GONE);
            prescriptionTxtS.setVisibility(View.GONE);
            addressTxtS.setVisibility(View.GONE);
            cityTxtS.setVisibility(View.GONE);
            stateTxtS.setVisibility(View.GONE);
            zipCodeTxtS.setVisibility(View.GONE);


        } else {
//                editImgBtnP.setImageResource(R.mipmap.icneditaboutlabel);
            editImgBtnS.setImageResource(R.mipmap.icn_edit_aboutlabel);

            optionEdtS.setVisibility(View.GONE);
            companyNameEdtS.setVisibility(View.GONE);
            phoneNoEdtS.setVisibility(View.GONE);
            employerEdtS.setVisibility(View.GONE);
            groupEdtS.setVisibility(View.GONE);
            prescriptionEdtS.setVisibility(View.GONE);
            addressEdtS.setVisibility(View.GONE);
            cityEdtS.setVisibility(View.GONE);
            stateEdtS.setVisibility(View.GONE);
            zipCodeEdtS.setVisibility(View.GONE);

            optionTxtS.setVisibility(View.VISIBLE);
            companyNameTxtS.setVisibility(View.VISIBLE);
            phoneNoTxtS.setVisibility(View.VISIBLE);
            employerTxtS.setVisibility(View.VISIBLE);
            groupTxtS.setVisibility(View.VISIBLE);
            prescriptionTxtS.setVisibility(View.VISIBLE);
            addressTxtS.setVisibility(View.VISIBLE);
            cityTxtS.setVisibility(View.VISIBLE);
            stateTxtS.setVisibility(View.VISIBLE);
            zipCodeTxtS.setVisibility(View.VISIBLE);


            setSecondryValues();
            getSecondryValues();



        }

    }

    private void getPrimaryValues() {

        dBAdapter.openMdsDB();
        InsuranceWrapper wrapper = new InsuranceWrapper();
        wrapper = dBAdapter.getInsuranceHashMap().get(Constants.INSURANCE_PRIMARY);
        if (wrapper != null) {
            optionTxt.setText(wrapper.getCompanyoption());
            companyNameTxt.setText(wrapper.getCompanyname());
            phoneNoTxt.setText(wrapper.getPhoneno());
            employerTxt.setText(wrapper.getEmployer());
            prescriptionTxt.setText(wrapper.getPrescription());
            addressTxt.setText(wrapper.getAddress());
            cityTxt.setText(wrapper.getI_city());
            stateTxt.setText(wrapper.getI_state());
            zipCodeTxt.setText(wrapper.getI_zipcode());
            groupTxt.setText(wrapper.getCompanygroup());

            MyApplication.getApplication().loader.displayImage("file://"
                    + wrapper.getCompanyimagename(), insuranceImgView, MyApplication.options);
            optionEdt.setText(wrapper.getCompanyoption());
            companyNameEdt.setText(wrapper.getCompanyname());
            phoneNoEdt.setText(wrapper.getPhoneno());
            employerEdt.setText(wrapper.getEmployer());
            prescriptionEdt.setText(wrapper.getPrescription());
            addressEdt.setText(wrapper.getAddress());
            cityEdt.setText(wrapper.getI_city());
            stateEdt.setText(wrapper.getI_state());
            zipCodeEdt.setText(wrapper.getI_zipcode());
            groupEdt.setText(wrapper.getCompanygroup());
            employerTxt.setText(wrapper.getEmployer());

        }
        dBAdapter.closeMdsDB();
    }

    private void getSecondryValues() {

        dBAdapter.openMdsDB();

        InsuranceWrapper wrapper = new InsuranceWrapper();
        wrapper = dBAdapter.getInsuranceHashMap().get(Constants.INSURANCE_SECONDRY);
        if (wrapper != null) {

            optionTxtS.setText(wrapper.getCompanyoption());
            companyNameTxtS.setText(wrapper.getCompanyname());
            phoneNoTxtS.setText(wrapper.getPhoneno());
            employerTxtS.setText(wrapper.getEmployer());
            prescriptionTxtS.setText(wrapper.getPrescription());
            addressTxtS.setText(wrapper.getAddress());
            cityTxtS.setText(wrapper.getI_city());
            stateTxtS.setText(wrapper.getI_state());
            zipCodeTxtS.setText(wrapper.getI_zipcode());
            groupTxtS.setText(wrapper.getCompanygroup());

            MyApplication.getApplication().loader.displayImage("file://"
                    + wrapper.getCompanyimagename(), insuranceImgViewS, MyApplication.options);
            optionEdtS.setText(wrapper.getCompanyoption());
            companyNameEdtS.setText(wrapper.getCompanyname());
            phoneNoEdtS.setText(wrapper.getPhoneno());
            employerEdtS.setText(wrapper.getEmployer());
            prescriptionEdtS.setText(wrapper.getPrescription());
            addressEdtS.setText(wrapper.getAddress());
            cityEdtS.setText(wrapper.getI_city());
            stateEdtS.setText(wrapper.getI_state());
            zipCodeEdtS.setText(wrapper.getI_zipcode());
            groupEdtS.setText(wrapper.getCompanygroup());
            employerTxtS.setText(wrapper.getEmployer());

        }
        dBAdapter.closeMdsDB();

    }

    private void setPrimaryValues() {
        dBAdapter.openMdsDB();

        InsuranceWrapper wrapper = new InsuranceWrapper();
        wrapper.setAddress(addressEdt.getText().toString());
        wrapper.setCompanygroup(groupEdt.getText().toString());
        wrapper.setCompanyimagename("");
        wrapper.setCompanyname(companyNameEdt.getText().toString());
        wrapper.setCompanyoption(optionEdt.getText().toString());
        wrapper.setI_city(cityEdt.getText().toString());
        wrapper.setI_state(stateEdt.getText().toString());
        wrapper.setI_zipcode(zipCodeEdt.getText().toString());
        wrapper.setPhoneno(phoneNoEdt.getText().toString());
        wrapper.setPrescription(prescriptionEdt.getText().toString());
        wrapper.setEmployer(employerEdt.getText().toString());
        wrapper.setCompanyimagename(imagePathPrimary);

        dBAdapter.saveInsuranceHashMap(Constants.INSURANCE_PRIMARY, wrapper);
        dBAdapter.closeMdsDB();

    }

    private void setSecondryValues() {
        dBAdapter.openMdsDB();

        InsuranceWrapper wrapper = new InsuranceWrapper();
        wrapper.setAddress(addressEdtS.getText().toString());
        wrapper.setCompanygroup(groupEdtS.getText().toString());
        wrapper.setCompanyimagename("");
        wrapper.setCompanyname(companyNameEdtS.getText().toString());
        wrapper.setCompanyoption(optionEdtS.getText().toString());
        wrapper.setI_city(cityEdtS.getText().toString());
        wrapper.setI_state(stateEdtS.getText().toString());
        wrapper.setI_zipcode(zipCodeEdtS.getText().toString());
        wrapper.setPhoneno(phoneNoEdtS.getText().toString());
        wrapper.setPrescription(prescriptionEdtS.getText().toString());
        wrapper.setEmployer(employerEdtS.getText().toString());
        wrapper.setCompanyimagename(imagePathSecondry);
        dBAdapter.saveInsuranceHashMap(Constants.INSURANCE_SECONDRY, wrapper);
        dBAdapter.closeMdsDB();

    }

    @OnClick(R.id.showHideImgBtn_P)
    public void showHideImgBtn_P() {


        if (primaryInsuranceLl.getVisibility() == View.VISIBLE) {
            showHideImgBtnP.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            primaryInsuranceLl.setVisibility(View.GONE);
        } else {
            showHideImgBtnP.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            primaryInsuranceLl.setVisibility(View.VISIBLE);
        }


    }


    @OnClick(R.id.showHideImgBtn_S)
    public void showHideImgBtn_S() {

        if (secondryInsuranceLl.getVisibility() == View.VISIBLE) {
            showHideImgBtnS.setImageResource(R.mipmap.icn_arrow_down_aboutlabel);
            secondryInsuranceLl.setVisibility(View.GONE);
        } else {
            showHideImgBtnS.setImageResource(R.mipmap.icn_arrowup_aboutlabel);
            secondryInsuranceLl.setVisibility(View.VISIBLE);
        }


    }

}
