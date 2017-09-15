package com.example.birkan.remindme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    Context context = this;
    SQliteHelper db = new SQliteHelper(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //db.onCreate(db.getWritableDatabase());
    }

    public void insert(View v) {
        TextView textView = (TextView) findViewById(R.id.editText);
        db.AddReminder(textView.getText().toString());
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
