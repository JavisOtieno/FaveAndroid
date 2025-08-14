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
import com.javy.fave.adapters.AdapterListProducts;
import com.javy.fave.models.Product;

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

public class ProductsActivity extends AppCompatActivity implements OnSuccessListener {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private JSONArray products = null;
    private AdapterListProducts mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Products");
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        emptyView = (TextView) findViewById(R.id.empty_view);
//        errorLoadingRelativeLayout = view.findViewById(R.id.errorLoading);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));
        recyclerView.setHasFixedSize(true);


        NetworkUtils.
                fetchData("GET","products",null
                        ,ProductsActivity.this,ProductsActivity.this);


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
            List<Product> items = new ArrayList<>();

            products = jsonObject.getJSONArray("products");

            System.out.println("Products length: "+products.length());

            for (int i = 0; i < products.length(); i++) {
                Product obj = new Product();
                //obj.image = drw_arr.getResourceId(i, -1);
                obj.id = products.getJSONObject(i).getString("id");
                obj.name = products.getJSONObject(i).getString("name");
                obj.price = products.getJSONObject(i).getString("price");
                obj.commission = products.getJSONObject(i).getString("commission");



//                obj.endlat = products.getJSONObject(i).getString("end_lat");

//                if (products.getJSONObject(i).has("end_lat") &&
//                        !products.getJSONObject(i).isNull("end_lat")) {
//                    obj.endlat = products.getJSONObject(i).getString("end_lat");
//                }

//
////                obj.endlong = products.getJSONObject(i).getString("end_long");
                String categoryName = "";
                if (products.getJSONObject(i).has("category") &&
                        !products.getJSONObject(i).isNull("category")) {
                    categoryName = products.getJSONObject(i).
                            getJSONObject("category").getString("name");
                    categoryName = categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1);

                }
                obj.category = categoryName;
//                String categoryName = product.getJSONObject("category").getString("name");
//                categoryEditText.setText(
//                        capitalizedName);




//
//                Toasts.toastIconError(getContext(),obj.status);

//                String accountName = "";

//                if (
//                products.getJSONObject(i).has("lead") &&
//                        products.getJSONObject(i).get("lead") instanceof JSONObject
//                ) {
//                    accountName = products.getJSONObject(i).
//                            getJSONObject("lead").getString("name");
//                }else if(
//                        products.getJSONObject(i).has("account") &&
//                                products.getJSONObject(i).get("account") instanceof JSONObject
//                ){

//                }
//                accountName = products.getJSONObject(i).
//                        getJSONObject("account").optString("name");
//
//                obj.account_name = accountName;

                String createdAt = products.getJSONObject(i).getString("created_at");
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
            mAdapter = new AdapterListProducts(items);
            recyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new AdapterListProducts.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Product obj, int position) {

                    Product product = items.get(position);

                    Intent intent = new Intent(ProductsActivity.this,
                            ViewProductActivity.class);
                    intent.putExtra("productId",product.id);
                    System.out.println("productId: "+product.id);
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
//                        JSONArray accounts = jsonObject.getJSONArray("products");
//                        String name = accounts.getJSONObject(0).getString("name");
//                        String message = jsonObject.getString("message");
//                        String name = jsonObject.getString("name");

            // Show a toast with the message
//                        Toasts.toastIconSuccess(AccountsActivity.this, name);

            // Log the values for debugging
//                        Log.d("API Response", "Message: " + name + ", Name: " + name);
        } catch (Exception e) {
//            Toasts.toastIconError(getContext(),"Error Loading");
            Toast.makeText(ProductsActivity.this, "Error Loading",
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
            Intent intent = new Intent(ProductsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(ProductsActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}