package busitweek18.treasurehunt.treasurehunt;

        import android.content.Intent;
        import android.graphics.Typeface;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SET HERE SCREEN TO LAUNCH ON START
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        // use this to adapt the font-size of this activity
        Button button = findViewById(R.id.startButton);
      
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/BlackPearl.ttf");
        button.setTypeface(tf);
        // use this to adapt the font-size of this activity

    }

    public void goToTreasures(View view) {
        Log.i(TAG,"User clicked the start button");
        Intent intent = new Intent(this, TreasurepickActivity.class);
        startActivity(intent);
    }



}
