package com.ta.belajarsdap.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.VerticalGridView;
import androidx.lifecycle.ViewModelProvider;

import com.ta.belajarsdap.R;
import com.ta.belajarsdap.adapter.CoursesListAdapter;
import com.ta.belajarsdap.adapter.LessonVerticalAdapter;
import com.ta.belajarsdap.databinding.FragmentLessonBinding;
import com.ta.belajarsdap.objects.Course;
import com.ta.belajarsdap.persistence.LessonsLDH;

import java.util.List;

public class LessonFragment extends Fragment {

    private LessonViewModel lessonViewModel;
    private FragmentLessonBinding binding;
    CoursesListAdapter coursesListAdapter;
    LessonsLDH lessonsLDH;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lessonViewModel =
                new ViewModelProvider(this).get(LessonViewModel.class);

        View root = inflater.inflate(R.layout.fragment_lesson, container, false);

        lessonsLDH = LessonsLDH.getInstance(inflater.getContext());
        List<Course> courses = lessonsLDH.getCourses();

        TextView tvCourseTitle = root.findViewById(R.id.course_title);
        tvCourseTitle.setText(courses.get(0).getTitle());

        LessonVerticalAdapter lessonListAdapter = new LessonVerticalAdapter(inflater.getContext(), courses.get(0).getLessons(), courses.get(0).getId());
        VerticalGridView listLesson = root.findViewById(R.id.grid_lessons);
        listLesson.setAdapter(lessonListAdapter);

        /*coursesListAdapter = new CoursesListAdapter(inflater.getContext(), courses);
        ListView listCourses = root.findViewById(R.id.courses_list);
        listCourses.setAdapter(coursesListAdapter);*/

        /*binding = FragmentLessonBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        lessonViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;*/

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}