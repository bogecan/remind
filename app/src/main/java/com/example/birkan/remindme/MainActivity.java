package com.example.birkan.remindme;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.RequestQueue;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    Context context = this;
    SQliteHelper db = new SQliteHelper(context);
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, Main2Activity.class);//2.ekran açılması için inten oluşturuldu
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);//ana ekrandaki butona ulaşıldı.
        fab.setOnClickListener(new View.OnClickListener() {//butona click eventi atandı
            @Override
            public void onClick(View view) {
                startActivity(intent);//oluşturulan ıntent işlemi çağırıldı.
            }
        });
        final ListView listView = (ListView) findViewById(R.id.lstView);//listview itemi bulundu
        final ArrayList<String> remindItems = new ArrayList<String>();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);//her cihaz topic'e bu kodla kaydoluyor. daha sonra push notificatıonları bu targetlere(deviceId) api tarafında yazarak gönderıyoruz.



                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();


                }
            }
        };

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);




// Instantiate the RequestQueue.
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this); //yeni volley kuyruğu oluşturuldu
        String url ="http://www.guvencden.com/api/Reminds";//call yapacağımız url

// Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new  JsonArrayRequest(Request.Method.GET, url,null,//get için oluşturulan yapı jsonarray dönen response için eğer tek bir dönüş olsaydı jsonobject sadece string olsaydı stringrequest tipinde çağrı yapılırdı.
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {//get işlemi bitip response aldığında buraya düşer async çalışıyor o yuzden sonuc bekledıgımız ıslemlerı bu metod ıcınde yapmamız gerek
                        // Display the first 500 characters of the response string.
                        try {
                            List<RemindItem> itemList = new ArrayList<>();
                            for (int i=0;i<response.length();i++)
                           {
                               RemindItem remindItem = new Gson().fromJson(response.getString(i),RemindItem.class);
                               if (!remindItem.getDeleted()) {
                                   itemList.add(remindItem);//gson sınıfı ıle convert ıslemı yapıyoruz
                               }
                           }

                            setList(listView, itemList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
// Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(//request için ayarları burdan verıyoruz
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
MySingleton.getInstance(this).addToRequestQueue(stringRequest);//onceden olusturulan farklı bı kuyruk sınıfına gonderıyoruz. bunu yazmazsak apı call olmuyor.





    }

    private void setList(ListView listView, List<RemindItem> remindItems) {
       // final List<RemindItem> remindItemList = db.GetReminderList();
final List<RemindItem> remindItemList = remindItems;
        ArrayList remindArray = new ArrayList();

        for (RemindItem item : remindItems) {
            remindArray.add(item.getTitle());
        }


        ArrayAdapter<String> remindItemArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item,R.id.itemid,remindArray);
        listView.setAdapter(remindItemArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(context, Main3Activity.class);
                intent1.putExtra("remindItem", remindItemList.get(i).getId());
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
