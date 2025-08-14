package com.javy.fave;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;

public class ProfileActivity extends AppCompatActivity implements  OnSuccessListener {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView dateCreatedTextView;
    private TextView postTextView;
    private ImageView profileImageView;
    private TextView tripsNumberTextView;
    private TextView locationsNumberTextView;
    private TextView amountNumberTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Profile");

        nameTextView = (TextView) findViewById(R.id.profileNameTextView);
        phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        dateCreatedTextView = (TextView) findViewById(R.id.dateCreatedTextView);
//        postTextView = (TextView) findViewById(R.id.postTextView);
        profileImageView = (ImageView) findViewById(R.id.image);
        tripsNumberTextView = (TextView) findViewById(R.id.tripsNumberTextView);
//        locationsNumberTextView = (TextView) findViewById(R.id.locationsNumberTextView);
        amountNumberTextView = (TextView) findViewById(R.id.amountNumberTextView);




        NetworkUtils.
                fetchData("GET","profile",null
                        ,ProfileActivity.this,ProfileActivity.this);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });



//        NetworkUtils.
//                fetchData("GET","profile",null
//                        ,ProfileActivity.this,ProfileActivity.this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(ResponseBody responseBodyReceived) {

        Log.d("API Response", responseBodyReceived.toString());
        System.out.println("result: "+responseBodyReceived);

        try {
            // Get the response body as a string
            String responseBody = responseBodyReceived.string();

            // Create a JSONObject from the response string
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject user = jsonObject.getJSONObject("user");

//            locationsNumberTextView.setText(jsonObject.getString("locationscount"));
            tripsNumberTextView.setText(jsonObject.getString("orderscount"));
            amountNumberTextView.setText(jsonObject.getString("ordersamount"));

            nameTextView.setText(user.getString("name"));
            phoneTextView.setText(user.getString("phone"));
            emailTextView.setText(user.getString("email"));

//            String usertype = user.getString("usertype");
//            usertype = usertype.substring(0, 1).toUpperCase() +
//                    usertype.substring(1).toLowerCase();
//            postTextView.setText(usertype);

            String createdAt = user.getString("created_at");
            ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
            String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
            String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
            String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            dateCreatedTextView.setText(formattedDate);

//            System.out.println("Image Url: "+Constants.STORAGE_URL+user.getString("image"));

            Picasso.get()
                    .load(Constants.STORAGE_URL+""
//                            user.getString("image")
                    )
//                .placeholder(R.drawable.placeholder) // Optional placeholder image
                    .error(R.drawable.person_edited) // Optional error image
                    .into(profileImageView,new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Picasso", "Image loaded successfully");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Picasso", "Error loading image", e);
                        }
                    });



        } catch (Exception e) {
//            Toasts.toastIconError(getContext(),"Error Loading");
            Toast.makeText(ProfileActivity.this, "Error Loading",
                    Toast.LENGTH_SHORT).show();
            Log.e("API Response", "JSON Parsing error: " + e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }
}