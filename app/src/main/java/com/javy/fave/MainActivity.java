package com.javy.fave;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.javy.fave.adapters.AdapterListDashboardOrders;
import com.javy.fave.models.Order;
import com.javy.fave.util.EncryptedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements OnSuccessListener {

    private int REQUEST_LOCATION = 99;


    private String mName;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private String mUserId;
    private String mAuthToken;

    private boolean isFirstLocationUpdate = true;

    private ImageButton addDealButton;
//    private String mAuthToken;
    private View view;

    private RecyclerView recyclerView;
    private AdapterListDashboardOrders mAdapter;
    private TextView emptyView;
    private TextView totalDealsThisMonth;
    private TextView totalDealsUnitsThisMonth;
    //    private TextView targetTextView;
    private TextView monthTargetTitleTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Dashboard");

        EncryptedPrefsUtil.init(this);

        mUserId = EncryptedPrefsUtil.getString("userId", "0");
        mAuthToken = EncryptedPrefsUtil.getString("authToken", "");


        if (mUserId.equals("0")) {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        initNavigationMenu();
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            // should select the best provider but bypassing with NETWORK Provider for now
            // location = locationManager.getLastKnownLocation(provider);
            // location = locationManager.getLastKnownLocation(provider);\
//            Intent serviceIntent = new Intent(this, LocationService.class);
//            startForegroundService(serviceIntent);

        }

        totalDealsThisMonth = (TextView) findViewById(R.id.totalDealsThisMonth);
        totalDealsUnitsThisMonth = (TextView) findViewById(R.id.totalDealsUnitsThisMonth);
//        targetTextView = (TextView) view.findViewById(R.id.targetTextView);
        emptyView = (TextView) findViewById(R.id.empty_view);
        monthTargetTitleTextView = (TextView) findViewById(R.id.monthTargetTextView);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);

        NetworkUtils.
                fetchDataGet("dashboard",
                        mAuthToken,MainActivity.this,MainActivity.this);

        // Start the LocationService


//        scheduleLocationWorker();
//
//        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//        initToolbar();
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });



    }


    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
//                Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                if (item.getTitle().equals("Dashboard")) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //finish();
                    //startActivity(getIntent());
                } else if (item.getTitle().equals("Orders")) {
                    Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                    startActivity(intent);
                }
                else if (item.getTitle().equals("Payments")) {
                    Intent intent = new Intent(MainActivity.this, PaymentsActivity.class);
                    startActivity(intent);
                }
                else if (item.getTitle().equals("Products")) {
                    Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                    startActivity(intent);
                }
                else if (item.getTitle().equals("Customers")) {
                    Intent intent = new Intent(MainActivity.this, CustomersActivity.class);
                    startActivity(intent);
                }
                else if (item.getTitle().equals("Website")) {
                    Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
                    startActivity(intent);
                }
                else if (item.getTitle().equals("Profile")) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else if (item.getTitle().equals("Contact")) {
                    Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                    startActivity(intent);
                }
                else if (item.getTitle().equals("Log out")) {
//                    SharedPreferences.Editor mEditor = mPrefs.edit();
//                    mEditor.putString("userId", "0").commit();
                    EncryptedPrefsUtil.saveString("userId", "0");
//                    EncryptedPrefsUtil.saveString("authToken", "");
//                    EncryptedPrefsUtil.saveString("email", "");
//                    EncryptedPrefsUtil.saveString("password", "");

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

//                else if(item.getTitle().equals("Profile")){
//                    Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
//                    startActivity(intent);
//                }else if(item.getTitle().equals("POS Audits")){
//                    Intent intent = new Intent(MainActivity.this,POSAuditsListActivity.class);
//                    startActivity(intent);
//                }

                //actionBar.setTitle(item.getTitle());
                drawer.closeDrawers();
                return true;

            }
        });

        // open drawer at start
        //drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onSuccess(ResponseBody responseBodyObject) {

        Log.d("API Response", responseBodyObject.toString());
        System.out.println("result: "+responseBodyObject);

        try {
            // Get the response body as a string
            String responseBody = responseBodyObject.string();

            // Create a JSONObject from the response string
            JSONObject jsonObject = new JSONObject(responseBody);
            List<Order> items = new ArrayList<>();

            Integer dealsThisMonth = Integer.parseInt(jsonObject.getString("allorders"));
            String dealsThisMonthStr = NumberFormat.getNumberInstance(Locale.US).format(dealsThisMonth);
            totalDealsThisMonth.setText(dealsThisMonthStr);

            Integer target = Integer.parseInt(jsonObject.getString("allpayments"));
            String targetStr = NumberFormat.getNumberInstance(Locale.US).format(target);
            totalDealsUnitsThisMonth.setText(targetStr);
//            targetTextView.setText("Total Qty : "+targetStr+" units");

//            String monthYear = jsonObject.getString("monthYear");
//            monthTargetTitleTextView.setText(monthYear+" Targets");




            JSONArray  orders= jsonObject.getJSONArray("orders");
            System.out.println("Accounts length: "+orders.length());

            for (int i = 0; i < orders.length(); i++) {
                Order obj = new Order();
                //obj.image = drw_arr.getResourceId(i, -1);
                obj.id = orders.getJSONObject(i).getString("id");
                obj.customerName = orders.getJSONObject(i).getJSONObject("customer").getString("name");
                obj.value =  "KSh. " + NumberFormat.getNumberInstance(Locale.US).format(
                        Integer.valueOf(orders.getJSONObject(i).getString("value")));
                obj.orderDetails = orders.getJSONObject(i).getString("order_details");


                String createdAt = orders.getJSONObject(i).getString("created_at");
                ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
//                String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"));
                obj.date = formatted;
                //obj.imageDrw = drw_arr.getDrawable(obj.image);
                items.add(obj);

            }

//            JSONArray products = jsonObject.getJSONArray("products");
//            System.out.println("Accounts length: "+products.length());
//
//
//            for (int i = 0; i < products.length(); i++) {
//                Product obj = new Product();
//                //obj.image = drw_arr.getResourceId(i, -1);
//                obj.id = products.getJSONObject(i).getString("id");
//                obj.name = products.getJSONObject(i).getString("name");
//                obj.target_type = products.getJSONObject(i).getString("target_type");
//                Integer target_amount = Integer.valueOf(products.getJSONObject(i).getString("target_amount"));
//                String ordersSumAmount = products.getJSONObject(i).getString("orders_sum_amount");
//                Integer current_sold = 0;
//                if(!ordersSumAmount.equals("null")){
//                    current_sold = Integer.valueOf(ordersSumAmount);
//                }
//
////                Integer current_sold = 0;'leadsThisMonth',
//                Integer remainder = target_amount - current_sold;
//
//
//                if(obj.target_type.equals("value")){
//                    obj.remaining_amount =  "KSh. " + NumberFormat.getNumberInstance(Locale.US).format(
//                            remainder<0?0:remainder);
//                    obj.target_amount = "KSh. " + NumberFormat.getNumberInstance(Locale.US).format(
//                            target_amount);
//                }else{
//                    obj.remaining_amount =   NumberFormat.getNumberInstance(Locale.US).format(
//                            remainder<0?0:remainder)
//                            +" units";
//                    obj.target_amount = NumberFormat.getNumberInstance(Locale.US).format(
//                            target_amount)+" units";
//                }
//
//
//
//                String createdAt = products.getJSONObject(i).getString("created_at");
//                ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
//                String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"));
//                obj.date = formatted;
//                //obj.imageDrw = drw_arr.getDrawable(obj.image);
//                items.add(obj);
//
//            }
//
//
            if (items.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
//
//
            //set data and list adapter
            mAdapter = new AdapterListDashboardOrders(items);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.setAdapter(myAdapter);
            recyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new AdapterListDashboardOrders.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Order obj, int position) {

                    Order order = items.get(position);

                    Intent intent = new Intent(MainActivity.this, ViewOrderActivity.class);
                    intent.putExtra("orderId", order.id);
                    System.out.println("orderId: "+order.id);
                    startActivity(intent);



                }
            });


        } catch (Exception e) {
//            Toasts.toastIconError(getContext(),"Error Loading");
            Toast.makeText(
                    MainActivity.this,
                    "Error Loading. Please try again",
                    Toast.LENGTH_SHORT
            ).show();

            Log.e("API Response", "JSON Parsing error: " + e.getMessage());
        }

    }

    @Override
    public void onComplete() {

    }


//    private void updateLocationOnMap(Location location) {
//        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
////        Toast.makeText(MainActivity.this,
////                "Location found : Lat "+location.getLatitude()+
////                        " - Long: "+location.getLongitude(),
////                Toast.LENGTH_SHORT).show();
//        if (userMarker != null) {
////              userMarker.setPosition(userLocation);
//        } else {
    ////              userMarker = googleMap.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
//        }
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
//    }
















}