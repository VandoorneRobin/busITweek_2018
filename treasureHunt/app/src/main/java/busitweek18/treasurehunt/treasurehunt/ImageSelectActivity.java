package busitweek18.treasurehunt.treasurehunt;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.mendelu.busItWeek.library.ImageSelectPuzzle;
import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.Task;

public class ImageSelectActivity extends AppCompatActivity {

    private TextView question;
    private List<Integer> answers;
    private RecyclerView listOfAnswers;
    private StoryLine storyLine;
    private Task currentTask;
    private ImageSelectPuzzle puzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        storyLine = StoryLine.open(this, TreasureHuntStoryLineDbHelper.class);
        currentTask = storyLine.currentTask();
        puzzle = (ImageSelectPuzzle) currentTask.getPuzzle();

        question = findViewById(R.id.question);
        question.setText(puzzle.getQuestion());

        listOfAnswers = findViewById(R.id.imageList);
        ImageSelectActivity.AnswerAdapter adapter = new ImageSelectActivity.AnswerAdapter();
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(this);
        listOfAnswers.setLayoutManager(LayoutManager);
        listOfAnswers.setAdapter(adapter);
        listOfAnswers.setNestedScrollingEnabled(false);

        answers = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : puzzle.getImages().entrySet()) {
            answers.add(entry.getKey());
        }

        TextView textView = findViewById(R.id.question);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/BlackPearl.ttf");
        textView.setTypeface(tf);
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {

        public ImageView answer;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.image);
        }
    }

    private class AnswerAdapter extends RecyclerView.Adapter<AnswerViewHolder> {
        @Override
        public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_image_select_list, parent, false);
            return new AnswerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final AnswerViewHolder holder, int position) {
            Integer answer = answers.get(position);
            //holder.answer.setImageResource(answer);
            Picasso.with(ImageSelectActivity.this)
                    .load(answer)
                    .into(holder.answer);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (puzzle.getAnswerForImage(holder.getAdapterPosition())) {
                        storyLine.currentTask().finish(true);
                        finish();
                    } else {
                        Toast.makeText(ImageSelectActivity.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
