package com.example.birkan.remindme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {
    Context context = this;
    SQliteHelper db = new SQliteHelper(context);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent intent = getIntent();
        int i = intent.getIntExtra("remindItem", 1);

        TextView textView = (TextView) findViewById(R.id.txtTitle);
        textView.setText(db.GetReminder(i).getTitle());


    }
}
