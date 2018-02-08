package busitweek18.treasurehunt.treasurehunt;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
        setContentView(R.layout.activity_simple_puzzle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        answer  = findViewById(R.id.answer);
        question = findViewById(R.id.question);
        question.setText("Something");

        storyLine = StoryLine.open(this,TreasureHuntStoryLineDbHelper.class);
        currentTask = storyLine.currentTask();
        puzzle = (SimplePuzzle) currentTask.getPuzzle();
        question.setText(puzzle.getQuestion());
    }
    public void answerQuestion(View view){
        String userAnswer = answer.getText().toString();
        String correctAnswer = puzzle.getAnswer();
        if (userAnswer.equalsIgnoreCase(correctAnswer)){
            //correct answer
            storyLine.currentTask().finish(true);
            finish();
        } else {
            //wrong answer
            Toast.makeText(this,"Wrong answer",Toast.LENGTH_SHORT).show();
        }

    }
}
