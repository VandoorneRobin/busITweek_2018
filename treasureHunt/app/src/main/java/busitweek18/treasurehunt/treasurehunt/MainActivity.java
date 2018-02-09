package busitweek18.treasurehunt.treasurehunt;

        import android.content.Intent;
        import android.content.res.AssetFileDescriptor;
        import android.graphics.Typeface;
        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;

        import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";
    private MediaPlayer player;

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

        try {
            AssetFileDescriptor afd = getAssets().openFd("music/pirates.mp3");
           player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void goToTreasures(View view) {
        Log.i(TAG,"User clicked the start button");
        Intent intent = new Intent(this, TreasurepickActivity.class);
        startActivity(intent);
    }

/*
    @Override
    protected void onResume() {
        super.onResume();
        player.reset();
    }
*/
    @Override
    protected void onStop() {
        try {
            super.onStop();
            player.reset();
            player.prepare();
            player.stop();
            player.release();
        }catch (Exception ex){

        }
    }
}
