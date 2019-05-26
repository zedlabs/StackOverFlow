package com.zedled.app.view.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zedled.app.R;
import com.zedled.app.service.model.Question;
import com.zedled.app.view.fragments.QuestionFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionTitleAdapter extends RecyclerView.Adapter<QuestionTitleAdapter.ViewHolder> {

    private Context context;
    private List<Question> questionList = new ArrayList<>();
    private QCallback qCallback;


    public QuestionTitleAdapter(Context context) {
        this.qCallback = (QCallback) context;
        this.context = context;
    }

    public QuestionTitleAdapter(QuestionFragment questionFragment, Context context) {
        this.qCallback = (QCallback) questionFragment;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Question question = questionList.get(position);

        viewHolder.userNameTV.setText(question.getOwner().getDisplayName());

        String creationTime = getTime(question.getCreationDate());
        viewHolder.updateTimeTV.setText(creationTime);

        viewHolder.questionTV.setText(question.getTitle());
        viewHolder.questionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qCallback.questioniClicked(question);
            }
        });

        //viewHolder.scoreTV.setText(String.valueOf(question.getScore()));
        viewHolder.viewsTV.setText(String.valueOf(question.getViewCount()));
        viewHolder.answerTV.setText(String.valueOf(question.getAnswerCount()));

        viewHolder.tagsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        QuesitonTagAdapter questionTagAdapter = new QuesitonTagAdapter(context, question.getTags());
        viewHolder.tagsList.setAdapter(questionTagAdapter);

        if (!question.isAnswered()) {
            viewHolder.questionAnswered.setVisibility(View.INVISIBLE);
        }

        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        viewHolder.questionTV.setTypeface(custom_font);
    }

    private String getTime(Long seconds) {
        long timeMilli = new Date().getTime();
        long currentSecond = timeMilli/1000;
        seconds = currentSecond - seconds;
        if (seconds > 59) {
            long minutes = seconds / 60;
            if (minutes > 59) {
                long hours = minutes / 60;
                if (hours > 23) {
                    int days = (int) hours / 24;
                    if (days > 364) {
                        int year = days / 365;
                        return "asked " + String.valueOf(year) + " years ago";
                    } else {
                        return "asked " + String.valueOf(days) + " days ago";
                    }
                } else {
                    return "asked " + String.valueOf(hours) + " hours ago";
                }
            } else {
                return "asked " + String.valueOf(minutes) + " mins ago";
            }
        } else {
            return "asked " + String.valueOf(seconds) + " seconds ago";
        }
    }

    @Override
    public int getItemCount() {
        return questionList == null ? 0 : questionList.size();
    }

    public void swapData(List<Question> questionList) {
        this.questionList = questionList;
        notifyDataSetChanged();
    }

    public void setData(List<Question> questionList, boolean interestChanged) {
        if (interestChanged) {
            this.questionList.clear();
        }
        this.questionList.addAll(questionList);
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTV, updateTimeTV;
        private TextView questionTV, scoreTV, answerTV, viewsTV;
        private RecyclerView tagsList;
        private ImageView questionAnswered;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTV = itemView.findViewById(R.id.question_user_name);
            updateTimeTV = itemView.findViewById(R.id.question_time);
            questionTV = itemView.findViewById(R.id.question_detail);
            answerTV = itemView.findViewById(R.id.question_answer);
            viewsTV = itemView.findViewById(R.id.question_views);
            tagsList = itemView.findViewById(R.id.question_tag_list);
            questionAnswered = itemView.findViewById(R.id.question_correct);

        }

    }

    public interface QCallback {
        void questioniClicked(Question question);
    }
}
