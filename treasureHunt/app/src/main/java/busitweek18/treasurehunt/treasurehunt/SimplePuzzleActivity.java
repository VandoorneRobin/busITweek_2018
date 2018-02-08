package busitweek18.treasurehunt.treasurehunt;



import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cz.mendelu.busItWeek.library.SimplePuzzle;
import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.Task;

public class SimplePuzzleActivity extends AppCompatActivity {

    private EditText answer;
    private TextView question;

    private StoryLine storyLine;
    private Task currentTask;

    private SimplePuzzle puzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        answer = findViewById(R.id.answer);
        question = findViewById(R.id.question);

        storyLine = StoryLine.open(this, TreasureHuntStoryLineDbHelper.class);

        currentTask = storyLine.currentTask();
        puzzle = (SimplePuzzle) currentTask.getPuzzle();
        question.setText(puzzle.getQuestion());

        Button answer_question = findViewById(R.id.answer_question);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/BlackPearl.ttf");
        question.setTypeface(tf);
        answer_question.setTypeface(tf);

    }
    public void answerQuestion(View view){
        String userAnswer = answer.getText().toString();
        String correctAnswer = puzzle.getAnswer();

        if(userAnswer.equalsIgnoreCase(correctAnswer)){
            storyLine.currentTask().finish(true);
            finish();
        }else{
            Toast.makeText(this, "Wrong answer." , Toast.LENGTH_SHORT).show();
        }

    }
}
