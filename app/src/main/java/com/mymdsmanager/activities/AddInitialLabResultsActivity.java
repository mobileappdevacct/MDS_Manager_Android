package com.mymdsmanager.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.LabResultInfoWrapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.janmuller.android.simplecropimage.CropImage;
public class AddInitialLabResultsActivity extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.diagnosisBtn)
    Button diagnosisBtn;
    @Bind(R.id.DateBtn)
    Button DateBtn;
    @Bind(R.id.notesEdt)
    EditText notesEdt;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    @Bind(R.id.resultsEdt)
    EditText resultsEdt;
    @Bind(R.id.unitBtn)
    Button unitBtn;
    @Bind(R.id.addImageImgBtn)
    ImageButton addImageImgBtn;
    @Bind(R.id.addImageBtn)
    Button addImageBtn;
    @Bind(R.id.inflate_image_layout)
    LinearLayout inflateImageLayout;
    //    @Bind(R.id.saveAndbackBtn)
//    Button saveAndbackBtn;
    private Toolbar toolbar;
    String title = "";
    private final int DATE_DIALOG_ID = 1;
    private int year;
    private int month;
    private int day;
    private DBAdapter dbAdapter;
    private int id;
    private String type;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x53;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x52;
    public static final int REQUEST_CODE_GALLERY = 0x51;
    private File mFileTemp;
    private final String SELECT_DIAGNOSIS_TEST = "Select Diagnosis Test";
    private final String UNITS = "Select Unit";
    HomeWatcher mHomeWatcher;
    private ArrayList<String> testArr = new ArrayList<String>();
    private ArrayList<String> unitArr = new ArrayList<String>();
    ArrayList<String> image_pathArraylist = new ArrayList<>();
    String[] imagearr;
    private String imagePath = "";
    LabResultInfoWrapper wrapper;
    private String formattedDate;
    private int image_store_row_count=0;
    private int image_store_columan_count=0;
    private void setArrayList() {

        if (DataManager.getInstance().getDiabnosis_testUnitsArraylist().size() == 0) {
            testArr.add("Hemoglobin");
            testArr.add("White Blood Cell Count");
            testArr.add("Absolute Neutrophil Count");
            testArr.add("Serum Erythropoietin");
            testArr.add("Serum Folate");
            testArr.add("Ferritin");
            testArr.add("Other");
            unitArr.add("gm/dl");
            unitArr.add("k/ul");
            unitArr.add("pmol/L");
            unitArr.add("mmHg");
            unitArr.add("g/L");

            DataManager.getInstance().setDiabnosis_testArraylist(testArr);
            DataManager.getInstance().setDiabnosis_testUnitsArraylist(unitArr);

        }

    }
    private void showAddOthersDialog(final String title) {
        final Dialog dialog = new Dialog(AddInitialLabResultsActivity.this, R.style.AppCompatTheme);
        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.pop_up_add_others);
// Check if no view has focus:
        final EditText enterValueEdt = (EditText) dialog.findViewById(R.id.enterValueEdt);
        enterValueEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (TextUtils.isEmpty(enterValueEdt.getText().toString())) {
                            Toast.makeText(AddInitialLabResultsActivity.this, "Please fill required field", Toast.LENGTH_LONG).show();
                        } else {


                            if (title.equalsIgnoreCase(UNITS)) {
                                DataManager.getInstance().getUnits_Arraylist().add(DataManager.getInstance().getUnits_Arraylist().size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");

                                unitBtn.setText(enterValueEdt.getText().toString());

                            } else if (title.equalsIgnoreCase(SELECT_DIAGNOSIS_TEST)) {
                                DataManager.getInstance().getDiabnosis_testArraylist().add(DataManager.getInstance().getDiabnosis_testArraylist().size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");

                                diagnosisBtn.setText(enterValueEdt.getText().toString());


                            }
                            dialog.dismiss();

                        }
                }
                return true;

            }
        });

        Button addBtn, closeBtn;
        addBtn = (Button) dialog.findViewById(R.id.addBtn);
        closeBtn = (Button) dialog.findViewById(R.id.closeBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(enterValueEdt.getText().toString())) {
                    Toast.makeText(AddInitialLabResultsActivity.this, "Please fill required field", Toast.LENGTH_LONG).show();
                } else {


                    if (title.equalsIgnoreCase(UNITS)) {
                        DataManager.getInstance().getUnits_Arraylist().add(DataManager.getInstance().getUnits_Arraylist().size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");
                        unitBtn.setText(enterValueEdt.getText().toString());
                    } else if (title.equalsIgnoreCase(SELECT_DIAGNOSIS_TEST)) {
                        DataManager.getInstance().getDiabnosis_testArraylist().add(DataManager.getInstance().getDiabnosis_testArraylist().size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");

                        diagnosisBtn.setText(enterValueEdt.getText().toString());


                    }
                    dialog.dismiss();


                }
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_initial_lab_results);
        setArrayList();
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("id", -1);
        type = getIntent().getStringExtra("type");


        dbAdapter = new DBAdapter(AddInitialLabResultsActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.mipmap.icn_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        if (id == -1) {
            getSupportActionBar().setTitle("Add Lab Result");
            saveBtn.setText("SAVE");

        } else {
            getSupportActionBar().setTitle("Update Lab Result");
            saveBtn.setText("SAVE");
        }

        notesEdt.setCursorVisible(false);
        notesEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesEdt.setCursorVisible(true);

            }
        });
        notesEdt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveData();
                }
                return false;
            }
        });
        image_store_columan_count=getIntent().getIntExtra("imageclm",0);
        if (id == -1) {
//            Calendar c = Calendar.getInstance();
//
//
//            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
//
//            String formattedDate = df.format(c.getTime());
//            DateBtn.setText(formattedDate);
        } else {
            setData();
        }
        resultsEdt.setCursorVisible(false);
        resultsEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultsEdt.setCursorVisible(true);
            }
        });
        resultsEdt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    showPopUp(UNITS, DataManager.getInstance().getUnits_Arraylist());
                }
                return false;
            }
        });
        diagnosisBtn.requestFocus();

        DateBtn.addTextChangedListener(textWatcher);
        notesEdt.addTextChangedListener(textWatcher);
        unitBtn.addTextChangedListener(textWatcher);
        diagnosisBtn.addTextChangedListener(textWatcher);

         mHomeWatcher = new HomeWatcher(AddInitialLabResultsActivity.this);
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
    protected void onResume() {
        super.onResume();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(AddInitialLabResultsActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }
    boolean dataChanged;
    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            dataChanged = true;

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            dataChanged = true;
        }
    };
    private void saveData() {
        if (TextUtils.isEmpty(DateBtn.getText().toString()) || TextUtils.isEmpty(diagnosisBtn.getText().toString())) {
            Toast.makeText(AddInitialLabResultsActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
        } else {
            dbAdapter.openMdsDB();
            LabResultInfoWrapper wrapper = new LabResultInfoWrapper();

            wrapper.setNotes(notesEdt.getText().toString());
//            if (imagearr.length!=0)
//            {
//                for (int i = 0; i < imagearr.length; i++) {
//                    image_pathArraylist.add(imagearr[i]);
//                }
//            }
            String image_paths = TextUtils.join(",", image_pathArraylist);
            System.out.println("image_paths" + image_paths);
            wrapper.setLabimages(image_paths);
            wrapper.setLabresulttype(type);
            wrapper.setResults(resultsEdt.getText().toString());
            wrapper.setDate(DateBtn.getText().toString());
            wrapper.setDiagnosistest(diagnosisBtn.getText().toString());
            wrapper.setUnits(unitBtn.getText().toString());
            if (id == -1) {


                dbAdapter.saveLabResultData(wrapper);
                dbAdapter.closeMdsDB();
                MyApplication.saveLocalData(true);

                new UpdateOnClass(MyApplication.getApplication(),this);
                finish();
            } else {

                dbAdapter.updateLabResultData(wrapper, id);
                dbAdapter.closeMdsDB();
                MyApplication.saveLocalData(true);
                new UpdateOnClass(MyApplication.getApplication(),this);
                finish();
            }


            DateBtn.setText("");
            notesEdt.setText("");
            imagePath = "";
            resultsEdt.setText("");
            diagnosisBtn.setText("");
            unitBtn.setText("");

//            finish();
        }
    }

    @OnClick(R.id.unitBtn)
    public void unitBtn() {
        showPopUp(UNITS, DataManager.getInstance().getUnits_Arraylist());


    }

    @OnClick(R.id.diagnosisBtn)
    public void diagnosisBtn() {
        showPopUp(SELECT_DIAGNOSIS_TEST, DataManager.getInstance().getDiabnosis_testArraylist());


    }


    @OnClick(R.id.addImageBtn)
    public void addImageBtn() {

        setimagepath();
        uploadImage();
    }

    @Override
    public void onBackPressed() {

        if (dataChanged) {
            saveAlert();
        } else {

            finish();
        }

    }

    @OnClick(R.id.addImageImgBtn)
    public void addImageImgBtn() {

        setimagepath();
        uploadImage();
    }

    private void setimagepath() {
//        String fileName = "IMG_"
//                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
//                .toString() + ".jpg";
        String fileName=  image_store_columan_count+"_labresultinfo_"+image_store_row_count+".png";
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            File sdIconStorageDir = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + getResources().getString(R.string.imagepath) + "/");
            // create storage directories, if they don't exist
            sdIconStorageDir.mkdirs();

            mFileTemp = new File(Environment.getExternalStorageDirectory()
                    + "/" + getResources().getString(R.string.imagepath) + "/",
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
            Intent intent = new Intent(AddInitialLabResultsActivity.this, CropImage.class);
            intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
            intent.putExtra(CropImage.SCALE, true);

            intent.putExtra(CropImage.ASPECT_X, 1);
            intent.putExtra(CropImage.ASPECT_Y, 1);

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

//                    startCropImage();
                    imagePath = mFileTemp.getPath();

                    image_pathArraylist.add(image_store_columan_count+"_labresultinfo_"+image_store_row_count+".png");
                    image_store_row_count++;
//                MyApplication.getApplication().loader.displayImage("file://"
//                        + imagePath, addImageImgBtn, MyApplication.options);
                    if (inflateImageLayout != null) {
                        inflateImageLayout.removeAllViews();
                    }
                    for (int i = 0; i < image_pathArraylist.size(); i++) {


                        ImageView imageView = new ImageView(this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dim_60), (int) getResources().getDimension(R.dimen.dim_60));
                        layoutParams.setMargins((int) getResources().getDimension(R.dimen.dim_5), 0, 0, 0);
                        imageView.setLayoutParams(layoutParams);
                        inflateImageLayout.addView(imageView);


                        MyApplication.getApplication().loader.displayImage("file://"
                                + loadImageFormSdCard(image_pathArraylist.get(i)), imageView);

                        System.out.println("image path:" + mFileTemp.getPath());
                        imageView.setTag(i);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = (int)v.getTag();
                                startActivity(new Intent(AddInitialLabResultsActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(image_pathArraylist.get(pos)))));
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {

                imagePath = mFileTemp.getPath();

                image_pathArraylist.add(image_store_columan_count+"_labresultinfo_"+image_store_row_count+".png");
                image_store_row_count++;
//                MyApplication.getApplication().loader.displayImage("file://"
//                        + imagePath, addImageImgBtn, MyApplication.options);
                if (inflateImageLayout != null) {
                    inflateImageLayout.removeAllViews();
                }
                for (int i = 0; i < image_pathArraylist.size(); i++) {


                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dim_60), (int) getResources().getDimension(R.dimen.dim_60));
                    layoutParams.setMargins((int) getResources().getDimension(R.dimen.dim_5), 0, 0, 0);
                    imageView.setLayoutParams(layoutParams);
                    inflateImageLayout.addView(imageView);


                    MyApplication.getApplication().loader.displayImage("file://"
                            + loadImageFormSdCard(image_pathArraylist.get(i)), imageView);

                    System.out.println("image path:" + mFileTemp.getPath());
                    imageView.setTag(i);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = (int)v.getTag();
                            startActivity(new Intent(AddInitialLabResultsActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(image_pathArraylist.get(pos)))));
                        }
                    });
                }

            }
            else if (requestCode == REQUEST_CODE_CROP_IMAGE) {

                System.out.println("on activity result crop");
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {

                    return;
                }


                imagePath = mFileTemp.getPath();

                image_pathArraylist.add(image_store_columan_count+"_labresultinfo_"+image_store_row_count+".png");
                image_store_row_count++;
//                MyApplication.getApplication().loader.displayImage("file://"
//                        + imagePath, addImageImgBtn, MyApplication.options);
                if (inflateImageLayout != null) {
                    inflateImageLayout.removeAllViews();
                }
                for (int i = 0; i < image_pathArraylist.size(); i++) {


                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dim_60), (int) getResources().getDimension(R.dimen.dim_60));
                    layoutParams.setMargins((int) getResources().getDimension(R.dimen.dim_5), 0, 0, 0);
                    imageView.setLayoutParams(layoutParams);
                    inflateImageLayout.addView(imageView);


                    MyApplication.getApplication().loader.displayImage("file://"
                            + loadImageFormSdCard(image_pathArraylist.get(i)), imageView);

                    System.out.println("image path:" + mFileTemp.getPath());
                    imageView.setTag(i);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = (int)v.getTag();
                            startActivity(new Intent(AddInitialLabResultsActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(image_pathArraylist.get(pos)))));
                        }
                    });
                }

            }

            // bundle2.putString("path", mFileTemp.getPath());


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void uploadImage() {
        setimagepath();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AddInitialLabResultsActivity.this);
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

    private void showPopUp(final String title, final ArrayList<String> arr) {
        MyApplication.getApplication().hideSoftKeyBoard(AddInitialLabResultsActivity.this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
//        Collections.sort(arr, new Comparator<String>() {
//            @Override
//            public int compare(String s1, String s2) {
//                return s1.compareToIgnoreCase(s2);
//            }
//        });
        ListView modeList = new ListView(this);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, arr);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);
        final Dialog dialog = builder.create();

        dialog.show();

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();

                if (title.equalsIgnoreCase(SELECT_DIAGNOSIS_TEST)) {
                    diagnosisBtn.setText(arr.get(position));
                    if (arr.get(position).equalsIgnoreCase("Other")) {
                        showAddOthersDialog(SELECT_DIAGNOSIS_TEST);
                    }
                }

                if (title.equalsIgnoreCase(UNITS)) {
                    unitBtn.setText(arr.get(position));
                    if (arr.get(position).equalsIgnoreCase("Other")) {
                        showAddOthersDialog(UNITS);
                    }
                }


            }
        });

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title);

//
//        if (title.equalsIgnoreCase(SELECT_WEIGHT)) {
//            for (int i = 0; i < typeArr.length; i++) {
//                typeArr[i] = (i + 1) + " lbs";
//            }
//        }

//        final ListView modeList = new ListView(this);
//        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, typeArr);
//        modeList.setAdapter(modeAdapter);

//        final Dialog dialog = builder.create();
//
//        builder.setView(modeList);
//
//        dialog.show();

    }


    @OnClick(R.id.DateBtn)
    public void DateBtn() {

        showDialog(DATE_DIALOG_ID);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date

                return new DatePickerDialog(this, datePickerListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));


        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {


        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String dayString = day + "", monthString = (month + 1) + "";

            if ((month + 1) < 10) {
                monthString = "0" + (month + 1);
            }
            if (day < 10) {
                dayString = "0" + day;
            }

            String dateString = monthString + "-" + dayString + "-" + year;
            // set selected date into textview
            DateBtn.setText(formateDate(dateString));


        }
    };

    private void setData() {
        dbAdapter.openMdsDB();

        wrapper = dbAdapter.getLabResultInfoWrapper(id);


        DateBtn.setText(wrapper.getDate());

        notesEdt.setText(wrapper.getNotes());

        resultsEdt.setText(wrapper.getResults());
        unitBtn.setText(wrapper.getUnits());
        imagearr = wrapper.getLabimages().split(",");
        if (imagearr.length != 0) {
            for (int i = 0; i < imagearr.length; i++) {
                image_pathArraylist.add(imagearr[i]);
                image_store_row_count++;
            }
        }



//        if (!TextUtils.isEmpty(MyApplication.getApplication().getProfileImage())) {
//            MyApplication.getApplication().loader.displayImage("file://"
//                    + MyApplication.getApplication().getProfileImage(), userImgView, MyApplication.option2);
//        } else {
//            String fileName = "IMG_"
//                    + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
//                    .toString() + ".jpg";
//            String state = Environment.getExternalStorageState();
//            if (Environment.MEDIA_MOUNTED.equals(state)) {
//                File sdIconStorageDir = new File(
//                        Environment.getExternalStorageDirectory() + "/"
//                                + getResources().getString(R.string.imagepath) + "/");
//                // create storage directories, if they don't exist
//                sdIconStorageDir.mkdirs();
//                mFileTemp = new File(Environment.getExternalStorageDirectory()
//                        + "/" + getResources().getString(R.string.imagepath) + "/" + getResources().getString(R.string.imagepath) + "/",
//                        "profileimage.jpg");
//            } else {
//                mFileTemp = new File(getFilesDir(), "profileimage.jpg");
//            }
//            if (mFileTemp.exists()) {
//                MyApplication.getApplication().loader.displayImage("file://"
//                        + mFileTemp.toString(), userImgView, MyApplication.option2);
//            }
        for (int i = 0; i < imagearr.length; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dim_60), (int) getResources().getDimension(R.dimen.dim_60));
            layoutParams.setMargins((int) getResources().getDimension(R.dimen.dim_5), 0, 0, 0);
            imageView.setLayoutParams(layoutParams);
            inflateImageLayout.addView(imageView);
            File file =new File(String.valueOf(loadImageFormSdCard(imagearr[i])));

            if (file.exists())
            {
                MyApplication.getApplication().loader.displayImage("file://"
                        + String.valueOf(loadImageFormSdCard(imagearr[i])), imageView);
            }
            imageView.setTag(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int)v.getTag();
                    startActivity(new Intent(AddInitialLabResultsActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(imagearr[pos]))));
                }
            });
//            else
//            {
//                String replacedString = imagearr[i].replace("/MyMDSManagerImages/", "/MyMDSManagerImages/MyMDSManagerImages/");
//                MyApplication.getApplication().loader.displayImage("file://"
//                        +replacedString, imageView, MyApplication.options);
//            }

        }

        diagnosisBtn.setText(wrapper.getDiagnosistest());

        dbAdapter.closeMdsDB();
    }

    @OnClick(R.id.saveBtn)
    public void saveBtn() {
        saveData();
    }

    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddInitialLabResultsActivity.this);
        builder.setTitle("" + getResources().getString(R.string.app_name3));
        builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                finish();


                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
    private String formateDate(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd,yyyy");
            Date date = originalFormat.parse(dateString);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onFinish() {

//        finish();
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
    private File loadImageFormSdCard(String fileName) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            mFileTemp = new File(Environment.getExternalStorageDirectory()
                    + "/" + getResources().getString(R.string.imagepath) + "/",
                    fileName);
        } else {
            mFileTemp = new File(getFilesDir(), fileName);
        }
        if (mFileTemp.exists()) {
            return mFileTemp;
        }
        return null;
    }
}
