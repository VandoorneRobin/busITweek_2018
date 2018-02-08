package busitweek18.treasurehunt.treasurehunt;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cz.mendelu.busItWeek.library.StoryLine;


public class TreasurepickActivity extends AppCompatActivity {

    private static final String TAG ="TreasurepickActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SET HERE SCREEN TO LAUNCH ON START
        setContentView(R.layout.activity_treasurepick);

        Button buttonMusea = findViewById(R.id.musea);
        Button buttonBar = findViewById(R.id.bar);
        Button buttonRestaurant = findViewById(R.id.restaurant);
        Button buttonFastfood = findViewById(R.id.fastfood);
        TextView textViewTreasure = findViewById(R.id.treasure);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/BlackPearl.ttf");
        buttonMusea.setTypeface(tf);
        buttonBar.setTypeface(tf);
        buttonRestaurant.setTypeface(tf);
        buttonFastfood.setTypeface(tf);
        textViewTreasure.setTypeface(tf);

    }

    //have to set this to mapactivity
    public void onPick(View view){
        Log.i(TAG,"User picked a treasure");
        //have to set this to mapactivity
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }



}
