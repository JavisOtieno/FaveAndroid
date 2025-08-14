package com.javy.fave;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.javy.fave.MainActivity;
import com.javy.fave.adapters.AdapterListPayments;
import com.javy.fave.models.Payment;

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

import okhttp3.ResponseBody;

public class PaymentsActivity extends AppCompatActivity implements OnSuccessListener {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private JSONArray payments = null;
    private AdapterListPayments mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payments);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Payments");
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        emptyView = (TextView) findViewById(R.id.empty_view);
//        errorLoadingRelativeLayout = view.findViewById(R.id.errorLoading);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(PaymentsActivity.this));
        recyclerView.setHasFixedSize(true);


        NetworkUtils.
                fetchData("GET","payments",null
                        ,PaymentsActivity.this,PaymentsActivity.this);


    }

    @Override
    public void onSuccess(ResponseBody responseBodyReceived) {

        Log.d("API Response", responseBodyReceived.toString());
        System.out.println("result: "+responseBodyReceived);

        try {
            // Get the response body as a string
            String responseBody = responseBodyReceived.string();
            System.out.println("Response: "+responseBody);

            // Create a JSONObject from the response string
            JSONObject jsonObject = new JSONObject(responseBody);
            List<Payment> items = new ArrayList<>();

            payments = jsonObject.getJSONArray("payments");

            System.out.println("Payments length: "+payments.length());

            for (int i = 0; i < payments.length(); i++) {
                Payment obj = new Payment();
                //obj.image = drw_arr.getResourceId(i, -1);
                obj.id = payments.getJSONObject(i).getString("id");
//                obj.date = payments.getJSONObject(i).getString("date");
                obj.status = payments.getJSONObject(i).getString("status");
                obj.total = payments.getJSONObject(i).getString("total");


//                obj.endlat = payments.getJSONObject(i).getString("end_lat");

//                if (payments.getJSONObject(i).has("end_lat") &&
//                        !payments.getJSONObject(i).isNull("end_lat")) {
//                    obj.endlat = payments.getJSONObject(i).getString("end_lat");
//                }

//
////                obj.endlong = payments.getJSONObject(i).getString("end_long");






//
//                Toasts.toastIconError(getContext(),obj.status);

//                String accountName = "";

//                if (
//                payments.getJSONObject(i).has("lead") &&
//                        payments.getJSONObject(i).get("lead") instanceof JSONObject
//                ) {
//                    accountName = payments.getJSONObject(i).
//                            getJSONObject("lead").getString("name");
//                }else if(
//                        payments.getJSONObject(i).has("account") &&
//                                payments.getJSONObject(i).get("account") instanceof JSONObject
//                ){

//                }
//                accountName = payments.getJSONObject(i).
//                        getJSONObject("account").optString("name");
//
//                obj.account_name = accountName;

                String createdAt = payments.getJSONObject(i).getString("created_at");
                ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
                obj.date = formatted;
                //obj.imageDrw = drw_arr.getDrawable(obj.image);
                items.add(obj);

            }

            if (items.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }


            //set data and list adapter
            mAdapter = new AdapterListPayments(items);
            recyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new AdapterListPayments.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Payment obj, int position) {

                    Payment payment = items.get(position);

                    Intent intent = new Intent(PaymentsActivity.this,
                            ViewPaymentActivity.class);
                    intent.putExtra("paymentId",payment.id);
                    System.out.println("paymentId: "+payment.id);
                    startActivity(intent);



                }
            });

//                        mAdapter.setOnItemClickListener(new AdapterListAccounts.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, Account obj, int position) {
//
//                                Account account = items.get(position);
//
//                                PreventDoubleClickOnLists.
//                                        showActivityWithoutDoubleClick(
//                                                "account",AccountsActivity.this,
//                                                account, IndividualAccountActivity.class);
//
//
//
//                            }
//                        });


            // Extract values from the JSON object
//                        JSONArray accounts = jsonObject.getJSONArray("payments");
//                        String name = accounts.getJSONObject(0).getString("name");
//                        String message = jsonObject.getString("message");
//                        String name = jsonObject.getString("name");

            // Show a toast with the message
//                        Toasts.toastIconSuccess(AccountsActivity.this, name);

            // Log the values for debugging
//                        Log.d("API Response", "Message: " + name + ", Name: " + name);
        } catch (Exception e) {
//            Toasts.toastIconError(getContext(),"Error Loading");
            Toast.makeText(PaymentsActivity.this, "Error Loading",
                    Toast.LENGTH_SHORT).show();
            Log.e("API Response", "JSON Parsing error: " + e.getMessage());
        }

    }


    @Override
    public void onComplete() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            Intent intent = new Intent(PaymentsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(PaymentsActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}