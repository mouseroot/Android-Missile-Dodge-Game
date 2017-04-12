package com.example.mouseroot.mygame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Remove titlebar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        setContentView(new GamePanel(this,sharedPref));
    }
}
