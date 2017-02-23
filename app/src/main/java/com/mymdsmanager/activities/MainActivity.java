package com.mymdsmanager.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.fragments.ContactUsFragment;
import com.mymdsmanager.fragments.HomeFragment;
import com.mymdsmanager.fragments.MessageFragment;
import com.mymdsmanager.fragments.PrivacyPolicyFragment;
import com.mymdsmanager.fragments.SettingsFragment;
import com.mymdsmanager.task.CompleteListener;
import com.mymdsmanager.task.GetDataTask;
import com.mymdsmanager.task.HomeWatcher;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements LocationListener,GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult>,OnFinishActivity {

    //    String TITLES[] = {"Home", "Message", "Settings", "Contact Us", "Privacy Policy", "Download"};
    String TITLES[] = {"Home", "Message", "Settings", "Contact Us", "Privacy Policy"};
    int ICONS[] = {R.mipmap.icn_home_normal, R.mipmap.icn_message_normal,
            R.mipmap.icn_settings_normal, R.mipmap.icn_help_normal, R.mipmap.icn_privacy_normal, R.mipmap.icn_data};


    int ICONS_TOUCHED[] = {R.mipmap.icn_home_touch, R.mipmap.icn_message_touch,
            R.mipmap.icn_settings_touch, R.mipmap.icn_help_touch, R.mipmap.icn_privacy_touch, R.mipmap.icn_data_on};


    public Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public FrameLayout frameContainer;
    private DrawerLayout Drawer;
    private int selectedItem = -1;
    private ActionBarDrawerToggle mDrawerToggle;

    private SettingsFragment settingsFragment;

    private HomeFragment homeFragment;
    private MessageFragment messageFragment;
    private ContactUsFragment contactUsFragment;
    // // location
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    String nameStr, genderStr, fbIdStr, fNameStr, lNameStr, pictureStr,
            emailStr, latStr = "0", longStr = "0";

    private static final int RC_SIGN_IN = 0;
    private static final int PROFILE_PIC_SIZE = 400;
    private boolean mIntentInProgress;
    public static boolean mSignInClicked;
    private SignInButton signinButton;

    private ConnectionResult mConnectionResult;
    private Person currentPerson;
    public Bitmap googleProfileImageBitmap;
    private PrivacyPolicyFragment privacyPolicyFragment;
    int download_file_num = 0;
    boolean getdatabase_Fromserver = true;
    HomeWatcher mHomeWatcher;

    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getUiComponents();
        loadhomeFragemt();

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
//        if (!TextUtils.isEmpty(MyApplication.getEmailId())) {
//            updateData();
//        }
        getVersion();
        if (MyApplication.getLocalDataSave())
        {
         //   savedateUpload();
            Intent service_intent = new Intent(MainActivity.this, DataUploadService.class);
            startService(service_intent);
          //  MyApplication.saveLocalData(false);
        }
        if (!MyApplication.getDeletedatabase())
        {
            DBAdapter dbAdapter = new DBAdapter(MainActivity.this);
            dbAdapter.deleteDatabase();
            MyApplication.savedeleteDatabase(true);
        }

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getUiComponents() {
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (!MyApplication.getGooglepopup()&& TextUtils.isEmpty(MyApplication.getEmailId())) {
            googlePopup();
        }
        createLocationRequest();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

// Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MenuAdapter(TITLES, ICONS, ICONS_TOUCHED);

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        frameContainer = (FrameLayout) findViewById(R.id.frameContainer);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, Drawer,
                toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    public static boolean isBackPressed = false;

    @Override
    public void onBackPressed() {

        try {
            if (homeFragment != null && (homeFragment instanceof HomeFragment)) {
                backPressAlert();
            } else {
                loadhomeFragemt();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

    private void backPressAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("" + getResources().getString(R.string.app_name3));
        builder.setMessage("Are you sure you want to exit application")
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

    private void loadhomeFragemt() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        homeFragment = new HomeFragment();
        ft.replace(R.id.frameContainer, homeFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void loadPrivacyFragemt() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        privacyPolicyFragment = new PrivacyPolicyFragment();
        ft.replace(R.id.frameContainer, privacyPolicyFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void loadSettingsFragemt() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        settingsFragment = new SettingsFragment();
        ft.replace(R.id.frameContainer, settingsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void loadMessageFragemt() {

        startActivity(new Intent(MainActivity.this, MessageFragment.class));
    }

    private void loadContactFragemt() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        contactUsFragment = new ContactUsFragment();
        ft.replace(R.id.frameContainer, contactUsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);
        // ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RC_SIGN_IN) {

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFinish() {

    }


    public class MenuAdapter extends
            RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        private String mNavTitles[];
        private int mIcons[];
        private int mIconsTouched[];

        public class ViewHolder extends RecyclerView.ViewHolder {
            int Holderid;

            LinearLayout row_ll;

            TextView menu_text;
            ImageView menu_image, headerImgView;

            public ViewHolder(View itemView, int ViewType) {
                super(itemView);

                itemView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = getPosition();
                        Drawer.closeDrawers();
                        selectedItem = pos;
                        mAdapter.notifyDataSetChanged();
                        settingsFragment.isUploadClicked = false;

                        switch (pos) {
                            case 1:

                                loadhomeFragemt();

                                break;
                            case 2:
                                loadMessageFragemt();

                                break;
                            case 3:
                                loadSettingsFragemt();
                                break;
                            case 4:
                                loadContactFragemt();
                                break;
                            case 5:
                                loadPrivacyFragemt();
                                break;
                            case 6:
//                                downloadAlert();

                                break;
                            default:
                                break;
                        }
                    }
                });

                row_ll = (LinearLayout) itemView.findViewById(R.id.row_ll);
                headerImgView = (ImageView) itemView.findViewById(R.id.headerImgView);

                if (ViewType == TYPE_ITEM) {
                    menu_text = (TextView) itemView
                            .findViewById(R.id.menu_text);
                    menu_image = (ImageView) itemView
                            .findViewById(R.id.menu_image);
                    Holderid = 1;
                } else {

                    Holderid = 0;
                }
            }

        }


        public MenuAdapter(String Titles[], int Icons[], int IconsTouched[]) {
            mNavTitles = Titles;
            mIcons = Icons;
            mIconsTouched = IconsTouched;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.menu_row, parent, false);

            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder.Holderid == 1) {


                holder.menu_text.setText(mNavTitles[position - 1]);


                holder.row_ll.setVisibility(View.VISIBLE);
                if (selectedItem == position) {
                    holder.menu_image.setImageResource(mIconsTouched[position - 1]);

                    holder.row_ll.setBackgroundResource(R.mipmap.cell_selected_side);
                } else {
                    holder.menu_image.setImageResource(mIcons[position - 1]);

                    holder.row_ll.setBackgroundResource(0);
                }
                holder.headerImgView.setVisibility(View.GONE);

            } else {
                holder.row_ll.setVisibility(View.GONE);
                holder.headerImgView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mNavTitles.length + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;

            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

    }


    // Google plus code starts from here
    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        Log.d(TAG,
                "Firing onLocationChanged..............................................");
        mCurrentLocation = location;

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());

        if (!connectionResult.hasResolution()) {
//            GooglePlayServicesUtil.getErrorDialog(
//                    connectionResult.getErrorCode(), this, 0).show();
//            try {
//                // !!!
//                connectionResult.startResolutionForResult(this, 1000);
//            } catch (IntentSender.SendIntentException e) {
//                mGoogleApiClient.connect();
//            }
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.d(TAG, "onStart fired ..............");
//        mGoogleApiClient.connect();
//        applicationWillEnterForeground();
        isGooglePlusClicked = false;
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //  hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    private boolean isGooglePlusClicked = false;
    private void gmailLogin() {
        isGooglePlusClicked = true;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //  setPersonalInfo(currentPerson);
            if (isGooglePlusClicked) {
                String emailGoogle = acct.getEmail();

                String gender_ = "", dob_ = "", profile_ = "";

                if (acct.getPhotoUrl() != null) {
                    profile_ = acct.getPhotoUrl().toString();
                }

                String[] arr = new String[9];
                arr[0] = acct.getDisplayName(); //name
                arr[1] = acct.getEmail(); //email
                arr[2] = "";//phone
                arr[3] = dob_;//birthdate
                arr[4] = gender_;//gender
                arr[5] = "G";//typeoflogin
                arr[6] = "";//filter
                arr[7] = acct.getId();//social_id
                arr[8] = profile_;//profile

                System.out.println("Gender::" + (arr[4]));

                MyApplication.saveEmailId(acct.getEmail());

                if (settingsFragment != null && settingsFragment.isSettingsClick ) {
                    settingsFragment.emailTxt.setText("Login with "+MyApplication.getEmailId());
                    settingsFragment.logoutTxt.setText("Logout");
                    settingsFragment.isSettingsClick = false;
                    new UpdateDataDialog(MainActivity.this, R.style.Dialog).show();
//                new UpdateOnClass(MainActivity.this,this);// updateData();

                } else if (login_from_google_popup) {
//                updateData();
//                new UpdateOnClass(MainActivity.this,this);
                    new UpdateDataDialog(MainActivity.this, R.style.Dialog).show();
                    login_from_google_popup = false;
                }

            }

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            Log.d(TAG, "Location update resumed .....................");
        }


    }



    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.release();
            }
        } else {
            Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        try {
            if (mConnectionResult.hasResolution()) {
                try {
                    mIntentInProgress = true;
                    mConnectionResult.startResolutionForResult(MainActivity.this,
                            RC_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sign-in into google
     */
    public void signInWithGplus() {
        isGooglePlusClicked = true;
        mSignInClicked = true;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
//        if (mGoogleApiClient.isConnected()) {
//            mSignInClicked = true;
//            mGoogleApiClient.connect();
//        } else {
//            if (!mGoogleApiClient.isConnecting()) {
//                mSignInClicked = true;
//                resolveSignInError();
//            }
//        }

    }

    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            // updateUI(false);
                        }

                    });
        }
    }

    public void signOutFromGplus() {
        if (mGoogleApiClient.isConnected() && settingsFragment.isSettingsLogoutClick) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//            mGoogleApiClient.connect();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            MyApplication.saveEmailId("");
                            settingsFragment.emailTxt.setText(MyApplication.getEmailId());
                            settingsFragment.logoutTxt.setText("Login");
                            settingsFragment.isSettingsLogoutClick = false;
                        }
                    });



            // updateUI(false);
        }
    }

    boolean login_from_google_popup = false;

    private void googlePopup() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog);
        dialog.setContentView(R.layout.google_login_ask_dialog);
        dialog.show();
        final Button yesBtn, remindmelaterBtn, nevershowthisBtn;
        ImageView closeImg;
        yesBtn = (Button) dialog.findViewById(R.id.yesBtn);
        remindmelaterBtn = (Button) dialog.findViewById(R.id.remindmelaterBtn);
        nevershowthisBtn = (Button) dialog.findViewById(R.id.nevershowthisBtn);
        closeImg = (ImageView) dialog.findViewById(R.id.closeImg);
        remindmelaterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        nevershowthisBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getApplication().produceAnimation(nevershowthisBtn);
                MyApplication.saveGooglepopup(true);
                dialog.dismiss();

            }
        });

        yesBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getApplication().produceAnimation(yesBtn);
                MyApplication.saveGooglepopup(true);
                dialog.dismiss();
                login_from_google_popup = true;
                    signInWithGplus();



            }
        });
        closeImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

    }
    public void getVersion() {
        try {


            new GetDataTask(MainActivity.this, "app_version.php",
                    new CompleteListener() {

                        @Override
                        public void onRemoteErrorOccur(Object error) {
                            // actionBarFragment.mHandler
                            // .removeCallbacks(actionBarFragment.mRunnable);

                        }

                        @Override
                        public void onRemoteCallComplete(Object result) {

                            System.out.println("result" + result.toString());
                            try {
                                JSONObject object = new JSONObject(result.toString());
                                JSONObject dataObject = object.getJSONObject("data");

                                float androidVersion = Float.parseFloat(dataObject.getString("android_version"));
                                int android_force_download = dataObject.getInt("android_force_download");
                                float appVersion = Float.parseFloat(version);
                                if (androidVersion > (int) appVersion) {
                                    if (android_force_download == 1) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("" + getResources().getString(R.string.app_name3));
                                        builder.setMessage("Update app")
                                                .setCancelable(false)
                                                .setPositiveButton("Update",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                startActivity(new Intent(Intent.ACTION_VIEW,
                                                                        Uri.parse("market://details?id=com.mymdsmanager")));
                                                            }
                                                        });
                                        builder.show();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("" + getResources().getString(R.string.app_name3));
                                        builder.setMessage("Update app")
                                                .setCancelable(false)
                                                .setPositiveButton("Update",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                startActivity(new Intent(Intent.ACTION_VIEW,
                                                                        Uri.parse("market://details?id=com.mymdsmanager")));
                                                            }
                                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        });
                                        builder.show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }).execute();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}

