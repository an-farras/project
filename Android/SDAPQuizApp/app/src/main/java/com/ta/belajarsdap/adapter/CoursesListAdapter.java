package com.ta.belajarsdap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.VerticalGridView;

import com.ta.belajarsdap.R;
import com.ta.belajarsdap.objects.Course;
import com.ta.belajarsdap.objects.Lesson;

import java.util.List;

public class CoursesListAdapter extends BaseAdapter {

    private Context context;
    private List<Course> courses;

    public CoursesListAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int i) {
        return courses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.course_item, viewGroup, false);

        TextView tvCourseTitle = view.findViewById(R.id.course_title);
        tvCourseTitle.setText(courses.get(i).getTitle());

        LessonVerticalAdapter lessonAdapter = new LessonVerticalAdapter(context, (List<Lesson>) courses.get(i).getLessons(), (int) courses.get(i).getId());
        VerticalGridView lessonsView = view.findViewById(R.id.grid_lessons);
        lessonsView.setAdapter(lessonAdapter);

        return view;
    }
}
