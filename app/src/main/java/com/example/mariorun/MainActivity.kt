package com.example.mariorun

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window

// CLASE MAIN ACTIVITY =============================================================================
class MainActivity : Activity() {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main);
        val vista = GameView(this)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        setContentView(vista)
    }
}