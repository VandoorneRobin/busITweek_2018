package com.

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.Task;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";

    private StoryLine storyLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // SET HERE SCREEN TO LAUNCH ON START
        setContentView(R.layout.activity_main);

        storyLine = StoryLine.open(this,MyDemoStoryLineDBHelper.class);
    }


   
}
