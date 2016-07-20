package com.icom.draganddrop.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.icom.draganddrop.R;

/**
 * Created by davidcordova on 12/04/16.
 */
public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String message = getIntent().getStringExtra("MESSAGE");

        TextView tv = (TextView) findViewById(R.id.tv_message);
        tv.setText(message);
    }
}
