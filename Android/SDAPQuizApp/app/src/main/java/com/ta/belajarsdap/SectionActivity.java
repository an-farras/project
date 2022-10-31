package com.ta.belajarsdap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ta.belajarsdap.objects.Section;
import com.ta.belajarsdap.persistence.LessonsLDH;

public class SectionActivity extends AppCompatActivity {
    TextView pageTv;
    ImageView navprecBt;
    ImageView navnextBt;
    ImageView closeBt;
    TextView sectiontitleTv;
    ImageView sectionimage;
    TextView sectiontextTv;
    Section section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        Intent intent = getIntent();
        final int lessonid = intent.getIntExtra("lessonid", 0);
        final int section = intent.getIntExtra("section", 0);

        LessonsLDH lessonsLDH = LessonsLDH.getInstance(this);
        this.section = lessonsLDH.getSection(lessonid, section);

        if(this.section == null){
            Toast.makeText(SectionActivity.this, "Problems while loading section "
                    + Integer.toString(section), Toast.LENGTH_LONG).show();
        }

        pageTv = findViewById(R.id.page);
        navprecBt  = findViewById(R.id.navprec);
        navnextBt = findViewById(R.id.navnext);
        closeBt = findViewById(R.id.closebt);
        sectiontitleTv = findViewById(R.id.sectiontitle);
        sectionimage = findViewById(R.id.sectionimage);
        sectiontextTv = findViewById(R.id.sectiontext);

        sectiontitleTv.setText(this.section.getTitle());
        String imagename = "s" + Integer.toString(lessonid) + Integer.toString(section);
        sectionimage.setImageResource(this.getResources().getIdentifier(imagename,
                "drawable", getPackageName()));
        sectiontextTv.setText(this.section.getText());

        String page = this.section.getLessonTitle() + "  " + Integer.toString(section+1)
                + "/" + Integer.toString(this.section.getLessonSections());
        pageTv.setText(page);

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SectionActivity.this, HomeActivity.class);
                startActivity(intent);

                finish();
            }
        });

        if(section == 0){
            navprecBt.setVisibility(View.GONE);
        } else {
            navprecBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeSection(lessonid, section-1);
                }
            });
        }

        navnextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(section< SectionActivity.this.section.getLessonSections()-1){
                    changeSection(lessonid, section+1);
                } else {
                    //startQuiz(lessonid);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void changeSection(int lessonid, int section){
        Intent intent = new Intent(SectionActivity.this, SectionActivity.class);
        intent.putExtra("section", section);
        intent.putExtra("lessonid", lessonid);
        startActivity(intent);
    }

    /*void startQuiz(int lessonid){
        Intent intent = new Intent(SectionActivity.this, QuizActivity.class);
        intent.putExtra("lessonid", lessonid);
        startActivity(intent);
    }*/
}