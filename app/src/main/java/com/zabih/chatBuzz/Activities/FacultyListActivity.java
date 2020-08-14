package com.zabih.chatBuzz.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.zabih.chatBuzz.Activities.Adapters.ViewPagerAdapter;

import com.zabih.chatBuzz.Activities.Fragments.faculty_list;
import com.zabih.chatBuzz.Activities.Fragments.hod_list;
import com.zabih.chatBuzz.Activities.Fragments.student_list;
import com.zabih.chatBuzz.R;

import androidx.viewpager.widget.ViewPager;

public class FacultyListActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_list);
        initializations();

    }

    private void initializations() {
        tabLayout = findViewById(R.id.facultyList_tablayout);
        viewPager = findViewById(R.id.facultyList_ViewPager);

//        Setting toolbar

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new faculty_list(), "Faculty");
        viewPagerAdapter.addFragment(new hod_list(), "HOD");
        viewPagerAdapter.addFragment(new student_list(), "Students");

        viewPager.setAdapter(viewPagerAdapter);


    }
}
