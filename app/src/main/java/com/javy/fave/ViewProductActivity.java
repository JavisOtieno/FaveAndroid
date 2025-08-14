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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.javy.fave.util.EncryptedPrefsUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;

public class ViewProductActivity extends AppCompatActivity implements OnSuccessListener {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private Button endProductButton;
    private Boolean isProductRunning = false;
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
    private String productId;
    private SharedPreferences mPrefs;

    private TextView locationDetectTextview;
    //    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    //    private LocationRequest locationRequest;
//    private LocationCallback locationCallback;
    private Boolean isTaskRunning = false;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Button addStopoverButton;

    private String mProductId;
    private String endLocationName;
    private Button destinationDirectionsButton;
    private Button contactCustomerButton;
    private LinearLayout customerLayout;
    private EditText categoryEditText;
    private EditText commissionEditText;
    private EditText dateEditText;
    private EditText productDetailsEditText;

    private ImageView productImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_product);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("View Product");

//        dbHelper = new Mylocationdatabasehelper(getApplicationContext());
//        dbHelper.createTable();





        amountEditText = (EditText) findViewById(R.id.amountEditText);
        productDetailsEditText = (EditText) findViewById(R.id.orderDetailsEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        categoryEditText = (EditText) findViewById(R.id.customerNameEditText);
        commissionEditText = (EditText) findViewById(R.id.customerPhoneEditText);
        productImageView = (ImageView) findViewById(R.id.productImageView);



        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        endpoint = "product/"+productId;


        NetworkUtils.
                fetchData("GET",endpoint,
                        null, ViewProductActivity.this,
                        ViewProductActivity.this);

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
//                         Intent intent = new Intent(ViewProductActivity.this,
//                                 AddStopoverActivity.class);
//                         intent.putExtra("productId",productId);
//                         System.out.println("productId: "+productId);
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

            return true;
        }
        else if (item.getItemId() == R.id.action_share) {

            String username = EncryptedPrefsUtil.getString(
                    "username", "username-not-found");


            String currentUrl = "https://"+username+".av.ke/product/"+productId;

            // Share the URL via an Intent
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Check this out!");
            intent.putExtra(Intent.EXTRA_TEXT, currentUrl);
            startActivity(Intent.createChooser(intent, "Share via"));
            return true;
        }


        return true;
    }

    @Override
    public void onSuccess(ResponseBody responseBody) {
        String responseBodyString = null;
        try {
            String result = responseBody.string();
            System.out.println("Result: "+result);

            if (result != null && result.contains("product") &&
                    endpoint.equals("product/"+productId)){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("product") &&
                        jsonObject.get("product") instanceof JSONObject
                ) {

                    JSONObject product = jsonObject.getJSONObject("product");


                    productDetailsEditText.setText(product.
                            getString("name"));
                    amountEditText.setText(product.
                            getString("price"));
                    if (product.has("category") &&
                                product.get("category") instanceof JSONObject
                        ) {

                        String categoryName = product.getJSONObject("category").getString("name");
                        String capitalizedName = categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1);
                        categoryEditText.setText(
                                capitalizedName);

                    }else{
                        categoryEditText.setText(
                                "");
                    }

                    commissionEditText.setText(
                            product.getString("commission"));

                    String imageUrl = product.getString("image");  // "/static/images/product1.jpg"
                    String cleanedUrl = imageUrl.replaceFirst("^/static/uploads", "");  // "/images/product1.jpg"
                    System.out.println("cleanedUrl: "+cleanedUrl);
                    Picasso.get()
                            .load(Constants.STORAGE_URL+cleanedUrl)
//                .placeholder(R.drawable.placeholder) // Optional placeholder image
                            .error(R.drawable.no_image_available) // Optional error image


                            .into(productImageView,new Callback() {
                                @Override
                                public void onSuccess() {
                                    Log.d("Picasso", "Image loaded successfully");
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.e("Picasso", "Error loading image", e);
                                }
                            });







//                    if ( product.getString("status").equals("start") ) {
//
//                        if (product.has("customer") &&
//                                product.get("customer") instanceof JSONObject
//                        ) {
//                            contactCustomerButton.setVisibility(View.VISIBLE);
//                            customerLayout.setVisibility(View.VISIBLE);
//                            customerNameEditText.setText(product.getJSONObject("customer").getString("name"));
//                            customerPhoneEditText.setText(product.getJSONObject("customer").getString("phone"));
//
//                        }
////                        else{
////                            contactCustomerButton.setVisibility(View.GONE);
////                            customerLayout.setVisibility(View.GONE);
////                        }
//
//                        locationDetectTextview.setVisibility(View.VISIBLE);
//                        endProductButton.setVisibility(View.VISIBLE);
//                        addStopoverButton.setVisibility(View.VISIBLE);
//
//
//
//
//                    }


                    String createdAt = product.getString("created_at");
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
                    Toast.makeText(getBaseContext(), "Error Loading Product. Please Try Again", Toast.LENGTH_SHORT).show();
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }
            else if (result != null && result.contains("success") &&
                    endpoint.equals("endproduct/"+productId))
            {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {
                    String message = jsonObject.getString("message");

                    mPrefs = getSharedPreferences("label", 0);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
//                    mProductId = mPrefs.getString("productId", null);



                    mEditor.putString("productId", null).commit();

//                    Toasts.toastIconSuccess(EditMeetingActivity.this,message);
                    Toast.makeText(ViewProductActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ViewProductActivity.this,
                            ViewProductActivity.class);
                    intent.putExtra("productId", productId);
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
                    Toast.makeText(ViewProductActivity.this,
                            "Error Ending Product. Please Try Again",Toast.LENGTH_SHORT).show();
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Error Ending Product Error: "+result));

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