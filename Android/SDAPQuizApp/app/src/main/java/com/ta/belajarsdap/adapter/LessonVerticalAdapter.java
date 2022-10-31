package com.ta.belajarsdap.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ta.belajarsdap.R;
import com.ta.belajarsdap.SectionActivity;
import com.ta.belajarsdap.layout.FitDoughnut;
import com.ta.belajarsdap.objects.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonVerticalAdapter extends RecyclerView.Adapter<LessonVerticalAdapter.SimpleViewHolder> {
    private Context context;
    private List<Lesson> lessons;
    private int courseId;

    public LessonVerticalAdapter(Context context, List<Lesson> lessons, int courseId) {
        this.context = context;
        this.lessons = lessons;
        this.courseId = courseId;
        if(this.lessons == null) {
            this.lessons = new ArrayList<>();
        }
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvLessonTitle;
        public final RelativeLayout allView;
        public final ImageView iconLesson;
        public final FitDoughnut doughnut;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLessonTitle = (TextView) itemView.findViewById(R.id.lesson_title);
            allView = (RelativeLayout) itemView.findViewById(R.id.all_view);
            doughnut = (FitDoughnut) itemView.findViewById(R.id.dougnutres);
            iconLesson = (ImageView) itemView.findViewById(R.id.lesson_icon);
        }
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        holder.tvLessonTitle.setText(lessons.get(position).getTitle());
        System.out.println("lessons title: " + lessons.get(position).getTitle());

        String backgroundName = "course" + Integer.toString(courseId);
        holder.iconLesson.setBackgroundResource(context.getResources().getIdentifier(backgroundName,
                "drawable", context.getPackageName()));

        String iconName = "z" + Integer.toString(lessons.get(position).getId());
        holder.iconLesson.setImageResource(context.getResources().getIdentifier(iconName,
                "drawable", context.getPackageName()));

        if (lessons.get(position).getResult() > 0) {
            holder.doughnut.setVisibility(View.VISIBLE);
            holder.doughnut.animateSetPercent(((float) lessons.get(position).getResult() * 10) - 0.01f);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Still developed", Toast.LENGTH_SHORT).show();
                if (lessons.get(position).getNumberOfSections() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Not available");
                    alertDialog.setMessage("Sorry, this lesson is not available yet. Try instead the lessons on Passwords, Viruses, VPN and GDPR.");
                    alertDialog.setPositiveButton("OK",    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    Intent intent = new Intent(context, SectionActivity.class);
                    intent.putExtra("section", 0);
                    intent.putExtra("lessonid", lessons.get(position).getId());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.lessons.size();
    }

}
