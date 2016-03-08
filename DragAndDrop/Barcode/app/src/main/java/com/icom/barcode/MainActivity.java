package com.icom.barcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tagmanager.Container;

/**
 * Created by davidcordova on 26/11/15.
 */
public class MainActivity extends AppCompatActivity {

    private Container container;

    @Override
    protected void onStart() {
        super.onStart();
        Utils.pushOpenScreenEvent(this, "MainScreen");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.pushCloseScreenEvent(this, "MainScreen");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = ContainerHolderSingleton.getContainerHolder().getContainer();
    }
}
