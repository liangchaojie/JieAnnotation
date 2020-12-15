package com.hello.annotation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hello.inject_annotation.TestAnno;

@TestAnno
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectActivity.inject(this);//调用build生成的类
    }
}