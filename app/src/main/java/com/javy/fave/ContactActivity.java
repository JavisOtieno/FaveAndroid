package com.javy.fave;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;

public class ContactActivity extends AppCompatActivity implements OnSuccessListener {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private Button endUserButton;
    private Boolean isUserRunning = false;
    private EditText startLocationEditText;
    private EditText destinationLocationEditText;
    private EditText descriptionEditText;
    private EditText stopoverEditText;
    private EditText amountEditText;
    private String startLocation;
    private String destinationLocation;
    private String description;
    private String amount;
    private String endpoint;
    private String userId;
    private SharedPreferences mPrefs;

    private TextView locationDetectTextview;
    //    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    //    private LocationRequest locationRequest;
//    private LocationCallback locationCallback;
    private Boolean isTaskRunning = false;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Button addStopoverButton;

    private String mUserId;
    private String endLocationName;
    private Button destinationDirectionsButton;
    private Button contactCustomerButton;
    private LinearLayout customerLayout;

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Contact");

//        dbHelper = new Mylocationdatabasehelper(getApplicationContext());
//        dbHelper.createTable();





        nameEditText = (EditText) findViewById(R.id.nameEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);



        endpoint = "contact";


        NetworkUtils.
                fetchData("GET",endpoint,
                        null, ContactActivity.this,
                        ContactActivity.this);

        //START OF LATEST LOCATION CALL UPDATE

//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10000); // 10 seconds
//        locationRequest.setFastestInterval(5000); // 5 seconds'


        //END OF LATEST LOCATION CALL UPDATE

//        addStopoverButton.setOnClickListener(
//                new View.OnClickListener() {
//                     @Override
//                     public void onClick(View view) {
//
//                         Intent intent = new Intent(ViewUserActivity.this,
//                                 AddStopoverActivity.class);
//                         intent.putExtra("userId",userId);
//                         System.out.println("userId: "+userId);
//                         startActivity(intent);
//
//
//                     }
//                });





//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

    }

    @Override
    public void onSuccess(ResponseBody responseBody) {
        String responseBodyString = null;
        try {
            String result = responseBody.string();
            System.out.println("Result: "+result);

            if (result != null && result.contains("user") &&
                    endpoint.equals("contact")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("user") &&
                        jsonObject.get("user") instanceof JSONObject
                ) {

                    JSONObject user = jsonObject.getJSONObject("user");


                    nameEditText.setText(user.
                            getString("name"));
                    phoneEditText.setText(user.
                            getString("phone"));
                    emailEditText.setText(
                            user.getString("email"));
//                    dateEditText.setText(
//                            user.getString("date"));







//                    if ( user.getString("status").equals("start") ) {
//
//                        if (user.has("customer") &&
//                                user.get("customer") instanceof JSONObject
//                        ) {
//                            contactCustomerButton.setVisibility(View.VISIBLE);
//                            customerLayout.setVisibility(View.VISIBLE);
//                            customerNameEditText.setText(user.getJSONObject("customer").getString("name"));
//                            customerPhoneEditText.setText(user.getJSONObject("customer").getString("phone"));
//
//                        }
////                        else{
////                            contactCustomerButton.setVisibility(View.GONE);
////                            customerLayout.setVisibility(View.GONE);
////                        }
//
//                        locationDetectTextview.setVisibility(View.VISIBLE);
//                        endUserButton.setVisibility(View.VISIBLE);
//                        addStopoverButton.setVisibility(View.VISIBLE);
//
//
//
//
//                    }


                    String createdAt = user.getString("created_at");
                    ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                    String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
                    String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
//                    dateEditText.setText(formattedDate + " "+formattedTime);
//                    dateTextView.setText(formattedDate);
//                    timeTextView.setText(formattedTime);


                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
//                    Toasts.toastIconError(IndividualDealActivity.this,message);
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{
//                    Toasts.toastIconError(IndividualDealActivity.this,"Error Loading Account. Please Try Again");
                    Toast.makeText(getBaseContext(), "Error Loading User. Please Try Again", Toast.LENGTH_SHORT).show();
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }
            else if (result != null && result.contains("success") &&
                    endpoint.equals("enduser/"+userId))
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {
                    String message = jsonObject.getString("message");

                    mPrefs = getSharedPreferences("label", 0);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
//                    mUserId = mPrefs.getString("userId", null);



                    mEditor.putString("Id", null).commit();

//                    Toasts.toastIconSuccess(EditMeetingActivity.this,message);
                    Toast.makeText(ContactActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ContactActivity.this,
                            ContactActivity.class);
                    intent.putExtra("userId", userId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
//                    Toasts.toastIconError(EditMeetingActivity.this,message);
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

//                    Toasts.toastIconError(EditMeetingActivity.this,
//                            "Error Editing Meeting. Please Try Again");
                    Toast.makeText(ContactActivity.this,
                            "Error Ending User. Please Try Again",Toast.LENGTH_SHORT).show();
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Error Ending User Error: "+result));

                }

            }
            else{
//                Toasts.toastIconError(IndividualDealActivity.this,"Loading Account Failed. Please Try Again");
                Toast.makeText(getBaseContext(), "Something went wrong. Please Try Again", Toast.LENGTH_SHORT).show();
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

            }

//            Log.d("API Success", "Data: " + jsonObject.toString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onComplete() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //START OF LATEST LOCATION CODE










    //PICKED FROM OLD CODE BUT MIGHT BE VALID
    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


}