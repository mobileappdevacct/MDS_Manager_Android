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
import com.mymdsmanager.wrapper.BoneMarrowResultWrapper;

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
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.janmuller.android.simplecropimage.CropImage;


public class AddResultsBoneMarrowActivity extends AppCompatActivity implements OnFinishActivity {
    @Bind(R.id.dateBtn)
    Button dateBtn;
    @Bind(R.id.percentBlastBtn)
    Button percentBlastBtn;
    @Bind(R.id.cytogeneticsEdt)
    EditText cytogeneticsEdt;
    @Bind(R.id.notesEdt)
    EditText notesEdt;
    @Bind(R.id.addImageImgBtn)
    ImageButton addImageImgBtn;
    @Bind(R.id.addImageBtn)
    Button addImageBtn;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    private final int DATE_DIALOG_ID = 1;
    @Bind(R.id.inflate_image_layout)
    LinearLayout inflateImageLayout;
    private int year;
    private int month;
    private int day;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x53;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x52;
    public static final int REQUEST_CODE_GALLERY = 0x51;
    private File mFileTemp;
    ArrayList<String> image_pathArraylist = new ArrayList<>();
    String[] imagearr;

    private String imagePath = "";
    private DBAdapter dbAdapter;
    private Toolbar toolbar;
    private int id;
    HomeWatcher mHomeWatcher;
    private int image_store_row_count = 0;
    private int image_store_columan_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_results_bone_marrow);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        id = getIntent().getIntExtra("id", -1);

        if (id == -1) {
            getSupportActionBar().setTitle("Add Result");
            saveBtn.setText("SAVE");
        } else {
            getSupportActionBar().setTitle("Update Result");
            saveBtn.setText("SAVE");
        }
        image_store_columan_count = getIntent().getIntExtra("imageclm", 0);
        setData();

        notesEdt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveData();
                }

                return false;
            }
        });
        cytogeneticsEdt.setCursorVisible(false);

        cytogeneticsEdt.addTextChangedListener(textWatcher);
        percentBlastBtn.addTextChangedListener(textWatcher);
        dateBtn.addTextChangedListener(textWatcher);
        notesEdt.addTextChangedListener(textWatcher);
        mHomeWatcher = new HomeWatcher(AddResultsBoneMarrowActivity.this);
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

    @OnClick(R.id.cytogeneticsEdt)
    public void cytogeneticsEdt() {
        cytogeneticsEdt.setCursorVisible(true);
    }

    private void setData() {
        BoneMarrowResultWrapper wrapper = DataManager.getInstance().getBoneMarrowResultWrapper();
        cytogeneticsEdt.setText(wrapper.getDescription());
        percentBlastBtn.setText(wrapper.getMarrowblast());
        dateBtn.setText(wrapper.getDate());
        notesEdt.setText(wrapper.getNotes());
        imagearr = wrapper.getBoneimages().split(",");




        if (imagearr.length != 0) {
            for (int i = 0; i < imagearr.length; i++) {
                image_pathArraylist.add(imagearr[i]);
                image_store_row_count++;
            }
        }

        for (int i = 0; i < imagearr.length; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dim_60), (int) getResources().getDimension(R.dimen.dim_60));
            layoutParams.setMargins((int) getResources().getDimension(R.dimen.dim_5), 0, 0, 0);
            imageView.setLayoutParams(layoutParams);
            inflateImageLayout.addView(imageView);
            if (!TextUtils.isEmpty(imagearr[i])) {
                File file = new File(String.valueOf(loadImageFormSdCard(imagearr[i])));
                if (file.exists()) {
                    MyApplication.getApplication().loader.displayImage("file://"
                            + String.valueOf(loadImageFormSdCard(imagearr[i])), imageView);
                }
            }
            imageView.setTag(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos =(int)v.getTag();
                    startActivity(new Intent(AddResultsBoneMarrowActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(imagearr[pos]))));
                }
            });

//            else {
//                String replacedString = imagearr[i].replace("/MyMDSManagerImages/", "/MyMDSManagerImages/MyMDSManagerImages/");
//                MyApplication.getApplication().loader.displayImage("file://"
//                        + replacedString, imageView, MyApplication.options);
//            }

        }
    }

    private void saveData() {
        if (TextUtils.isEmpty(dateBtn.getText().toString()) || TextUtils.isEmpty(percentBlastBtn.getText().toString())) {
            Toast.makeText(AddResultsBoneMarrowActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
        } else {

            dbAdapter = new DBAdapter(AddResultsBoneMarrowActivity.this);
            dbAdapter.openMdsDB();
            BoneMarrowResultWrapper wrapper = new BoneMarrowResultWrapper();
            String image_paths = TextUtils.join(",", image_pathArraylist);
            wrapper.setBoneimages(image_paths);
            wrapper.setDate(dateBtn.getText().toString());
            wrapper.setDescription(cytogeneticsEdt.getText().toString());
            wrapper.setMarrowblast(percentBlastBtn.getText().toString());
            wrapper.setNotes(notesEdt.getText().toString());
            if (image_pathArraylist.size() > 0) {
                for (int i = 0; i < image_pathArraylist.size(); i++) {
                    image_store_row_count++;
                }
            }

            if (id == -1) {


                dbAdapter.saveBoneMarrowResult(wrapper);
                MyApplication.saveLocalData(true);

            } else {
                dbAdapter.updateBoneMarrowData(wrapper, id);
                MyApplication.saveLocalData(true);
            }

            dbAdapter.closeMdsDB();
//            Intent service_intent = new Intent(AddResultsBoneMarrowActivity.this, DataUploadService.class);
//            startService(service_intent);
            new UpdateOnClass(MyApplication.getApplication(), this);

            finish();
        }
    }

    @OnClick(R.id.saveBtn)
    public void saveBtn() {
        saveData();


    }

    @OnClick(R.id.dateBtn)
    public void dateBtn() {

        showDialog(DATE_DIALOG_ID);

    }

    @OnClick(R.id.percentBlastBtn)
    public void percentBlastBtn() {

        showPopUp("Select Bone Marrow Blasts %", new String[31]);

    }

    @OnClick(R.id.addImageBtn)
    public void addImageBtn() {

        uploadImage();
    }

    @OnClick(R.id.addImageImgBtn)
    public void addImageImgBtn() {


        uploadImage();
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

    private void setimagepath() {

//        String fileName = "IMG_"
//                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
//                .toString() + ".jpg";
        String fileName = image_store_columan_count + "_bonemarrowResult_" + image_store_row_count+".png";
        File sdIconStorageDir1 = new File(
                Environment.getExternalStorageDirectory() + "/"
                        + getResources().getString(R.string.imagepath) + "/");
        // create storage directories, if they don't exist
        sdIconStorageDir1.mkdirs();

        mFileTemp = new File(Environment.getExternalStorageDirectory()
                + "/" + getResources().getString(R.string.imagepath) + "/",
                fileName);
        if (mFileTemp.exists())
        {
            mFileTemp.delete();
        }
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
            Intent intent = new Intent(AddResultsBoneMarrowActivity.this, CropImage.class);
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
//                MyApplication.getApplication().loader.displayImage("file://"
//                        + imagePath, addImageImgBtn, MyApplication.options);

                    image_pathArraylist.add(image_store_columan_count + "_bonemarrowResult_" + image_store_row_count+".png");
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
                                startActivity(new Intent(AddResultsBoneMarrowActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(image_pathArraylist.get(pos)))));
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {

//                startCropImage();
                imagePath = mFileTemp.getPath();
//                MyApplication.getApplication().loader.displayImage("file://"
//                        + imagePath, addImageImgBtn, MyApplication.options);

                image_pathArraylist.add(image_store_columan_count + "_bonemarrowResult_" + image_store_row_count+".png");
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
                            startActivity(new Intent(AddResultsBoneMarrowActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(image_pathArraylist.get(pos)))));
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
//                MyApplication.getApplication().loader.displayImage("file://"
//                        + imagePath, addImageImgBtn, MyApplication.options);

                image_pathArraylist.add(image_store_columan_count + "_bonemarrowResult_" + image_store_row_count+".png");
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
                            startActivity(new Intent(AddResultsBoneMarrowActivity.this,FullScreenImage.class).putExtra("image",String.valueOf(loadImageFormSdCard(image_pathArraylist.get(pos)))));
                        }
                    });
                }
                System.out.println("image path:" + mFileTemp.getPath());
            }

            // bundle2.putString("path", mFileTemp.getPath());


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void uploadImage() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AddResultsBoneMarrowActivity.this);

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
                                setimagepath();
                                takePicture();

                            }

                        })
                .setNegativeButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just closeMdsDB
                                // the dialog box and do nothing
                                dialog.cancel();
                                setimagepath();
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

    private void showPopUp(final String title, final String[] arr) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
//        if (title.equalsIgnoreCase("Select Bone Marrow Blasts %")) {
        arr[0] = ("<1") + "%";
        ;
        for (int i = 1; i < arr.length; i++) {
            arr[i] = (i) + "%";
        }
//        }

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
                percentBlastBtn.setText(arr[position]);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(c.getTime());
//        dateBtn.setText(formattedDate);
        if (isActivityFound) {
            new UpdateDataDialog(AddResultsBoneMarrowActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @Override
    public void onBackPressed() {

        if (dataChanged&&(!TextUtils.isEmpty(dateBtn.getText().toString()) || !TextUtils.isEmpty(percentBlastBtn.getText().toString()) || !TextUtils.isEmpty(cytogeneticsEdt.getText().toString()) || !TextUtils.isEmpty(notesEdt.getText().toString()))) {
            saveAlert();
        } else {

            finish();
        }

    }

    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddResultsBoneMarrowActivity.this);
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
        } catch (Exception ex) {
            ex.printStackTrace();
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
}
