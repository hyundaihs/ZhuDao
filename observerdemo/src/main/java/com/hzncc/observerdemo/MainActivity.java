package com.hzncc.observerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hzncc.observerdemo.db.DBManager;
import com.hzncc.observerdemo.db.StudentDao;
import com.hzncc.observerdemo.entity.Student;
import com.hzncc.observerdemo.observer.StudentObserver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StudentDao studentDao;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        DBManager dbManager = DBManager.getInstance(this);
        dbManager.openDatabase();
        studentDao = new StudentDao(dbManager.getDb());
        StudentObserver studentObserver = new StudentObserver(this, new MyHandler(this));
        studentDao.registerObserver(this, false, studentObserver);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentDao.add(new Student(0, "kevin", 10, 10.8, "hzncc"));
            }
        });
        List<Student> list1 = new ArrayList<Student>();
        List<Student> list2 = new ArrayList<Student>();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                list1.add(new Student(i, "kevin", 10, 10.8, "hzncc"));
                continue;
            }
            list2.add(new Student(i, "kevin", 10, 10.8, "hzncc"));
        }
        if (list1.equals(list2)) {
            Log.d("Main", "list1 same to list2");
        } else {
            Log.d("Main", "list1 not same list2");
        }
    }

    private void refresh(String text) {
        button.setText(text);
    }

    private static class MyHandler extends Handler {

        private MainActivity activity;

        private MyHandler(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            if (!activity.isFinishing()) {
                activity.refresh(msg.what + "");
            }
        }
    }
}
