package com.example.birkan.remindme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    SQliteHelper db = new SQliteHelper(context);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent = new Intent(this, Main2Activity.class);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        ListView listView = (ListView) findViewById(R.id.lstView);
        ArrayList<String> remindItems = new ArrayList<String>();

        for (RemindItem item: db.GetReminderList()) {
            remindItems.add(item.getTitle());
        }
        ArrayAdapter<String> remindItemArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.item,R.id.itemid,remindItems);
        listView.setAdapter(remindItemArrayAdapter);

    }


}
