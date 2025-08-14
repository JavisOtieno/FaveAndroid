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

public class ViewPaymentActivity extends AppCompatActivity implements OnSuccessListener {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private Button endPaymentButton;
    private Boolean isPaymentRunning = false;
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
    private String paymentId;
    private SharedPreferences mPrefs;

    private TextView locationDetectTextview;
    //    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    //    private LocationRequest locationRequest;
//    private LocationCallback locationCallback;
    private Boolean isTaskRunning = false;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Button addStopoverButton;

    private String mPaymentId;
    private String endLocationName;
    private Button destinationDirectionsButton;
    private Button contactCustomerButton;
    private LinearLayout customerLayout;
    private EditText statusEditText;
    private EditText paymentIdEditText;
    private EditText dateEditText;
    private EditText paymentDetailsEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_payment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("View Payment");

//        dbHelper = new Mylocationdatabasehelper(getApplicationContext());
//        dbHelper.createTable();





        statusEditText = (EditText) findViewById(R.id.statusEditText);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        paymentIdEditText = (EditText) findViewById(R.id.paymentIdEditText);



        Intent intent = getIntent();
        paymentId = intent.getStringExtra("paymentId");
        endpoint = "payment/"+paymentId;


        NetworkUtils.
                fetchData("GET",endpoint,
                        null, ViewPaymentActivity.this,
                        ViewPaymentActivity.this);

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
//                         Intent intent = new Intent(ViewPaymentActivity.this,
//                                 AddStopoverActivity.class);
//                         intent.putExtra("paymentId",paymentId);
//                         System.out.println("paymentId: "+paymentId);
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

            if (result != null && result.contains("payment") &&
                    endpoint.equals("payment/"+paymentId)){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("payment") &&
                        jsonObject.get("payment") instanceof JSONObject
                ) {

                    JSONObject payment = jsonObject.getJSONObject("payment");


                    statusEditText.setText(payment.
                            getString("status"));
                    amountEditText.setText(payment.
                            getString("total"));
                    paymentIdEditText.setText("#"+
                            payment.getString("id"));
//                    dateEditText.setText(
//                            payment.getString("date"));







//                    if ( payment.getString("status").equals("start") ) {
//
//                        if (payment.has("customer") &&
//                                payment.get("customer") instanceof JSONObject
//                        ) {
//                            contactCustomerButton.setVisibility(View.VISIBLE);
//                            customerLayout.setVisibility(View.VISIBLE);
//                            customerNameEditText.setText(payment.getJSONObject("customer").getString("name"));
//                            customerPhoneEditText.setText(payment.getJSONObject("customer").getString("phone"));
//
//                        }
////                        else{
////                            contactCustomerButton.setVisibility(View.GONE);
////                            customerLayout.setVisibility(View.GONE);
////                        }
//
//                        locationDetectTextview.setVisibility(View.VISIBLE);
//                        endPaymentButton.setVisibility(View.VISIBLE);
//                        addStopoverButton.setVisibility(View.VISIBLE);
//
//
//
//
//                    }


                    String createdAt = payment.getString("created_at");
                    ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                    String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
                    String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                    dateEditText.setText(formattedDate + " "+formattedTime);
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
                    Toast.makeText(getBaseContext(), "Error Loading Payment. Please Try Again", Toast.LENGTH_SHORT).show();
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }
            else if (result != null && result.contains("success") &&
                    endpoint.equals("endpayment/"+paymentId))
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {
                    String message = jsonObject.getString("message");

                    mPrefs = getSharedPreferences("label", 0);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
//                    mPaymentId = mPrefs.getString("paymentId", null);



                    mEditor.putString("paymentId", null).commit();

//                    Toasts.toastIconSuccess(EditMeetingActivity.this,message);
                    Toast.makeText(ViewPaymentActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ViewPaymentActivity.this,
                            ViewPaymentActivity.class);
                    intent.putExtra("paymentId", paymentId);
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
                    Toast.makeText(ViewPaymentActivity.this,
                            "Error Ending Payment. Please Try Again",Toast.LENGTH_SHORT).show();
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Error Ending Payment Error: "+result));

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
                Intent intent = new Intent(ViewPaymentActivity.this, PaymentsActivity.class);
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