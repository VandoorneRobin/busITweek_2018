package busitweek18.treasurehunt.treasurehunt;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.mendelu.busItWeek.library.ChoicePuzzle;
import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.Task;

public class TextSelectActivity extends AppCompatActivity {

    private TextView question;
    private RecyclerView listOfAnswers;
    private List<String> answers;
    private Task currentTask;
    private StoryLine storyLine;
    private ChoicePuzzle puzzle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        question = findViewById(R.id.question);
        listOfAnswers = findViewById(R.id.answer_list);
        AnswerAdapter adapter = new AnswerAdapter();
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(this);
        listOfAnswers.setLayoutManager(LayoutManager);
        listOfAnswers.setAdapter(adapter);

        storyLine = StoryLine.open(this, TreasureHuntStoryLineDbHelper.class);
        currentTask = storyLine.currentTask();
        puzzle = (ChoicePuzzle) currentTask.getPuzzle();
        question.setText(puzzle.getQuestion());
        answers = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry: puzzle.getChoices().entrySet()){
            answers.add(entry.getKey());
        }
        adapter.notifyDataSetChanged();

        //stil has to be fixed,
//            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/BlackPearl.ttf");
//            listOfAnswers.setTypeface(tf);
        //stil has to be fixed,
    }

    public static class AnswerViewHolder extends RecyclerView.ViewHolder{

        public TextView answer;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.answer);
        }
    }

    public class AnswerAdapter extends RecyclerView.Adapter<AnswerViewHolder>{

        @Override
        public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_text_choice, parent, false);
            return new AnswerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final AnswerViewHolder holder, final int position) {

            String answer = answers.get(position);
            holder.answer.setText(answer);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (puzzle.getAnswerForChoice(holder.getAdapterPosition())){
                        storyLine.currentTask().finish(true);
                        finish();
                    }else{
                        Toast.makeText(TextSelectActivity.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return answers.size();
        }
    }
}
