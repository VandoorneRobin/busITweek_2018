package busitweek18.treasurehunt.treasurehunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import cz.mendelu.busItWeek.library.StoryLine;


public class TreasurepickActivity extends AppCompatActivity {

    private static final String TAG ="TreasurepickActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SET HERE SCREEN TO LAUNCH ON START
        setContentView(R.layout.activity_treasurepick);

    }

    //have to set this to mapactivity
    public void onPick(View view){
        Log.i(TAG,"User picked a treasure");
        //have to set this to mapactivity
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }



}
