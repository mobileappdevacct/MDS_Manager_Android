package com.mymdsmanager.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.mymdsmanager.wrapper.BloodCountResultWrapper;
import com.mymdsmanager.wrapper.OtherResultWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.janmuller.android.simplecropimage.CropImage;

public class AddResultsBloodCountTransfutionsActivity extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.dateBtn)
    Button dateBtn;
    @Bind(R.id.hgbEdt)
    EditText hgbEdt;
    @Bind(R.id.wbcEdt)
    EditText wbcEdt;
    @Bind(R.id.ancEdt)
    EditText ancEdt;
    @Bind(R.id.plateletsEdt)
    EditText plateletsEdt;
    @Bind(R.id.rbcsEdt)
    EditText rbcsEdt;
    @Bind(R.id.ferritinEdt)
    EditText ferritinEdt;
    @Bind(R.id.transfusionEdt)
    EditText transfusionEdt;
    @Bind(R.id.notesEdt)
    EditText notesEdt;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    private final int DATE_DIALOG_ID = 1;
    @Bind(R.id.bloodTypeBtn)
    Button bloodTypeBtn;
    @Bind(R.id.bloodtypenotesEdt)
    EditText bloodtypenotesEdt;
    @Bind(R.id.inflate_image_layout)
    LinearLayout inflateImageLayout;
    @Bind(R.id.addImageImgBtn)
    ImageButton addImageImgBtn;
    @Bind(R.id.addlabBtn)
    Button addlabBtn;
    @Bind(R.id.otherlabLayout)
    LinearLayout otherlabLayout;
    private int year;
    private int month;
    private int day;
    private DBAdapter dbAdapter;
    private Toolbar toolbar;
    private int id;
    private ArrayList<String> bloodGroupArr = new ArrayList<String>();
    String bloodTypeString;
    ArrayList<String> image_pathArraylist = new ArrayList<>();
    public static final int REQUEST_CODE_CROP_IMAGE = 0x53;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x52;
    public static final int REQUEST_CODE_GALLERY = 0x51;
    private File mFileTemp;
    private String imagePath = "", images_uptedates = "";
    LayoutInflater inflater;
    HashMap<String, String> otherresultStringHashMap = new HashMap<>();
    HashMap<TextView, EditText> updateotherresultStringHashMap = new HashMap<>();
    ArrayList<OtherResultWrapper> list;
    HomeWatcher mHomeWatcher;
    private int image_store_row_count = 0;
    private int image_store_columan_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new DBAdapter(AddResultsBloodCountTransfutionsActivity.this);

        setContentView(R.layout.activity_add_results_blood_count_transfutions);
        ButterKnife.bind(this);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        bloodGroupArr.add("A+");
        bloodGroupArr.add("A-");
        bloodGroupArr.add("B+");
        bloodGroupArr.add("B-");
        bloodGroupArr.add("AB+");
        bloodGroupArr.add("AB-");
        bloodGroupArr.add("O+");
        bloodGroupArr.add("O-");
        bloodGroupArr.add("Other");
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        id = getIntent().getIntExtra("id", -1);
        image_store_columan_count = getIntent().getIntExtra("imageclm", 0);
        if (id == -1) {
            getSupportActionBar().setTitle("Add Result");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
            String formattedDate = df.format(c.getTime());
//            dateBtn.setText(formattedDate);
        } else {
            getSupportActionBar().setTitle("Update Result");
            saveBtn.setText("SAVE");
            setData();
        }
        notesEdt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveData();
                }
                return false;
            }
        });



        ancEdt.addTextChangedListener(textWatcher);
        hgbEdt.addTextChangedListener(textWatcher);
        wbcEdt.addTextChangedListener(textWatcher);
        dateBtn.addTextChangedListener(textWatcher);
        plateletsEdt.addTextChangedListener(textWatcher);
        rbcsEdt.addTextChangedListener(textWatcher);
        ferritinEdt.addTextChangedListener(textWatcher);
        transfusionEdt.addTextChangedListener(textWatcher);
        notesEdt.addTextChangedListener(textWatcher);
        bloodTypeBtn.addTextChangedListener(textWatcher);
        bloodtypenotesEdt.addTextChangedListener(textWatcher);


        mHomeWatcher = new HomeWatcher(AddResultsBloodCountTransfutionsActivity.this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                // do something here...
                isActivityFound = true;
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
        if (isActivityFound) {
            new UpdateDataDialog(AddResultsBloodCountTransfutionsActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @OnClick(R.id.addlabBtn)
    public void addlabBtn() {
        final Dialog addotherlabDialog = new Dialog(AddResultsBloodCountTransfutionsActivity.this, R.style.AppCompatTheme);
        addotherlabDialog.setContentView(R.layout.pop_up_add_other_result);
        addotherlabDialog.show();
        final EditText otherlabEdt = (EditText) addotherlabDialog.findViewById(R.id.otherlabEdt);
        final EditText labresultEdt = (EditText) addotherlabDialog.findViewById(R.id.labresultEdt);
        TextView saveTxt = (TextView) addotherlabDialog.findViewById(R.id.saveTxt);
        TextView closeTxt = (TextView) addotherlabDialog.findViewById(R.id.closeTxt);
        saveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(otherlabEdt.getText().toString())||!TextUtils.isEmpty(labresultEdt.getText().toString())) {
                    View view = inflater.inflate(R.layout.inflate_other_lab_layout, null);
                    otherlabLayout.addView(view);
                    TextView otherlabtitleTxt = (TextView) view.findViewById(R.id.otherlabtitleTxt);
                    EditText otherlabvalueEdt = (EditText) view.findViewById(R.id.otherlabvalueEdt);
                    otherlabtitleTxt.setText(otherlabEdt.getText().toString());
                    otherlabvalueEdt.setText(labresultEdt.getText().toString());
                    otherresultStringHashMap.put(otherlabEdt.getText().toString(), labresultEdt.getText().toString());
                }
                addotherlabDialog.dismiss();
            }
        });
        closeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addotherlabDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.addImageImgBtn)
    public void addImageImgBtn() {

        uploadImage();
    }

    private void saveData() {
        if (TextUtils.isEmpty(dateBtn.getText().toString())) {
            Toast.makeText(AddResultsBloodCountTransfutionsActivity.this, "Please fill required field", Toast.LENGTH_LONG).show();
        } else {
            dbAdapter.openMdsDB();
            BloodCountResultWrapper wrapper = new BloodCountResultWrapper();
            String image_paths = TextUtils.join(",", image_pathArraylist);
            wrapper.setImage_path(image_paths);
            wrapper.setAnc(ancEdt.getText().toString());
            wrapper.setHgb(hgbEdt.getText().toString());
            wrapper.setWbc(wbcEdt.getText().toString());
            wrapper.setDate(dateBtn.getText().toString());
            wrapper.setPlatelets(plateletsEdt.getText().toString());
            wrapper.setRbcs(rbcsEdt.getText().toString());
            wrapper.setFerritin(ferritinEdt.getText().toString());
            wrapper.setTranfusion(transfusionEdt.getText().toString());
            wrapper.setNotes(notesEdt.getText().toString());
            wrapper.setBlood_type(bloodTypeBtn.getText().toString());
            wrapper.setBlood_notes(bloodtypenotesEdt.getText().toString());
            if (!TextUtils.isEmpty(image_paths)) {
                wrapper.setImage_path(image_paths);
            } else {
                wrapper.setImage_path(images_uptedates);
            }
            if (id == -1) {
                dbAdapter.saveBloodCount(wrapper);
                MyApplication.saveLocalData(true);
//                ArrayList<OtherResultWrapper> otherResultWrappers= new ArrayList<>();
                if (dbAdapter.getBloodCount().size()>0) {
                    BloodCountResultWrapper wrapper1 = dbAdapter.getBloodCount().get(dbAdapter.getBloodCount().size() - 1);


                    for (Map.Entry<String, String> entry : otherresultStringHashMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        OtherResultWrapper otherResultWrapper = new OtherResultWrapper();
                        otherResultWrapper.setBlabid("" + wrapper1.getBroid());
                        otherResultWrapper.setLabtitle(key);
                        otherResultWrapper.setLabvalue(value);
                        dbAdapter.saveotherResult(otherResultWrapper);
                        // do stuff
                    }
                }

            } else {
                dbAdapter.updateBloodCountData(wrapper, id);
                MyApplication.saveLocalData(true);
                if (otherresultStringHashMap.size() > 0) {
                    for (Map.Entry<String, String> entry : otherresultStringHashMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        OtherResultWrapper otherResultWrapper = new OtherResultWrapper();
                        otherResultWrapper.setBlabid("" + id);
                        otherResultWrapper.setLabtitle(key);
                        otherResultWrapper.setLabvalue(value);
                        dbAdapter.saveotherResult(otherResultWrapper);
                        // do stuff
                    }
                }
                for (Map.Entry<TextView, EditText> entry : updateotherresultStringHashMap.entrySet()) {
                    TextView key = entry.getKey();
                    EditText value = entry.getValue();
                    int i = 0;
                    OtherResultWrapper otherResultWrapper1 = list.get(i);
                    i++;
                    OtherResultWrapper otherResultWrapper = new OtherResultWrapper();
                    otherResultWrapper.setBlabid("" + id);
                    otherResultWrapper.setLabtitle(key.getText().toString());
                    otherResultWrapper.setLabvalue(value.getText().toString());
                    dbAdapter.updateOtherResult(otherResultWrapper, Integer.parseInt(otherResultWrapper1.getOtherrowid()));
                    // do stuff
                }
            }
            dbAdapter.closeMdsDB();
//            Intent service_intent = new Intent(AddResultsBloodCountTransfutionsActivity.this, DataUploadService.class);
//            startService(service_intent);
            new UpdateOnClass(MyApplication.getApplication(),this);
            finish();
        }
    }

    private void setData() {
        BloodCountResultWrapper wrapper = DataManager.getInstance().getBloodCountResultWrapper();

        ancEdt.setText(wrapper.getAnc());
        hgbEdt.setText(wrapper.getHgb());
        wbcEdt.setText(wrapper.getWbc());
        dateBtn.setText(wrapper.getDate());
        plateletsEdt.setText(wrapper.getPlatelets());
        rbcsEdt.setText(wrapper.getRbcs());
        ferritinEdt.setText(wrapper.getFerritin());
        transfusionEdt.setText(wrapper.getTranfusion());
        notesEdt.setText(wrapper.getNotes());
        images_uptedates = wrapper.getImage_path();
        bloodTypeBtn.setText(wrapper.getBlood_type());
        bloodtypenotesEdt.setText(wrapper.getBlood_notes());





       final String[] imagearr = wrapper.getImage_path().split(",");
        for (int i = 0; i < imagearr.length; i++) {
            image_pathArraylist.add(imagearr[i]);
            image_store_row_count++;
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dim_60), (int) getResources().getDimension(R.dimen.dim_60));
            layoutParams.setMargins((int) getResources().getDimension(R.dimen.dim_5), 0, 0, 0);
            imageView.setLayoutParams(layoutParams);
            inflateImageLayout.addView(imageView);
            File file = new File(String.valueOf(loadImageFormSdCard(imagearr[i])));


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
                    startActivity(new Intent(AddResultsBloodCountTransfutionsActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(imagearr[pos]))));
                }
            });
        }
        dbAdapter.openMdsDB();
        list = dbAdapter.getOtherResult("" + wrapper.getBroid());
        for (int i = 0; i < list.size(); i++) {
            if (!TextUtils.isEmpty(list.get(i).getLabtitle())||!TextUtils.isEmpty(list.get(i).getLabvalue())) {
                View view = inflater.inflate(R.layout.inflate_other_lab_layout, null);
                otherlabLayout.addView(view);
                final TextView otherlabtitleTxt = (TextView) view.findViewById(R.id.otherlabtitleTxt);
                final EditText otherlabvalueEdt = (EditText) view.findViewById(R.id.otherlabvalueEdt);
                otherlabtitleTxt.setTag(i);
                otherlabvalueEdt.setTag(i);
                otherlabtitleTxt.setText(list.get(i).getLabtitle());
                otherlabvalueEdt.setText(list.get(i).getLabvalue());
                updateotherresultStringHashMap.put(otherlabtitleTxt, otherlabvalueEdt);
            }
//            otherlabvalueEdt.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    String key = updateotherresultStringHashMap.get(otherlabtitleTxt.getText().toString());
//
//                }
//            });

        }
    }

    @OnClick(R.id.bloodTypeBtn)
    public void bloodTypeBtn() {
        showPopUp("", bloodGroupArr);
    }

    @OnClick(R.id.saveBtn)
    public void saveBtn() {
        saveData();
    }

    @OnClick(R.id.dateBtn)
    public void dateBtn() {
        MyApplication.getApplication().hideSoftKeyBoard(AddResultsBloodCountTransfutionsActivity.this);
        showDialog(DATE_DIALOG_ID);

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
            dateBtn.setText(formateDate(dateString));


        }
    };

    private void showPopUp(final String title, final ArrayList<String> arr) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
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
                bloodTypeString = arr.get(position);
                bloodTypeBtn.setText(bloodTypeString);
//                    if (bloodTypeString.equalsIgnoreCase("Others")) {
//                        showAddOthersDialog(SELECT_BLOOD_GORUP);
//                    }
            }
        });
    }

    private void setimagepath() {

        String fileName=  image_store_columan_count+"_bloodcountResult_"+image_store_row_count+".png";
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
            Intent intent = new Intent(AddResultsBloodCountTransfutionsActivity.this, CropImage.class);
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

                    imagePath = mFileTemp.getPath();

                    image_pathArraylist.add(image_store_columan_count+"_bloodcountResult_"+image_store_row_count+".png");
                    image_store_row_count++;
//                MyApplication.getApplication().loader.displayImage("file://"
//                        + imagePath, addImageImgBtn, MyApplication.options);
                    if (inflateImageLayout != null) {
                        inflateImageLayout.removeAllViews();
                    }
                    dataChanged =true;
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
                                int pos= (int)v.getTag();
                                startActivity(new Intent(AddResultsBloodCountTransfutionsActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(image_pathArraylist.get(pos)))));
                            }
                        });

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {

//                startCropImage();
                imagePath = mFileTemp.getPath();

                image_pathArraylist.add(image_store_columan_count+"_bloodcountResult_"+image_store_row_count+".png");
                image_store_row_count++;
//                MyApplication.getApplication().loader.displayImage("file://"
//                        + imagePath, addImageImgBtn, MyApplication.options);
                if (inflateImageLayout != null) {
                    inflateImageLayout.removeAllViews();
                }
                dataChanged =true;
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
                            int pos= (int)v.getTag();
                            startActivity(new Intent(AddResultsBloodCountTransfutionsActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(image_pathArraylist.get(pos)))));
                        }
                    });

                }

            } else if (requestCode == REQUEST_CODE_CROP_IMAGE) {

                System.out.println("on activity result crop");
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {

                    return;
                }


                imagePath = mFileTemp.getPath();

                image_pathArraylist.add(image_store_columan_count+"_bloodcountResult_"+image_store_row_count+".png");
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
                            int pos= (int)v.getTag();
                            startActivity(new Intent(AddResultsBloodCountTransfutionsActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(image_pathArraylist.get(pos)))));
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AddResultsBloodCountTransfutionsActivity.this);

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
                                setimagepath();
                                dialog.cancel();
                                takePicture();

                            }

                        })
                .setNegativeButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just closeMdsDB
                                // the dialog box and do nothing
                                setimagepath();
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

    @Override
    public void onBackPressed() {

        if (dataChanged) {
            saveAlert();
        } else {

            finish();
        }

    }

    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddResultsBloodCountTransfutionsActivity.this);
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
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
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


    boolean dataChanged=false;
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
}