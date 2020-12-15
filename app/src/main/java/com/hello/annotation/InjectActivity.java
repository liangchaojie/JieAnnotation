package com.hello.annotation;

import android.annotation.SuppressLint;
import android.util.ArrayMap;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import java.lang.reflect.Method;

public class InjectActivity {

    @SuppressLint("NewApi")
    private static final ArrayMap<String, Object> injectMap = new ArrayMap<>();

    public static void inject(AppCompatActivity activity) {
        Log.i("TAG", "inject: 111");
        String className = activity.getClass().getName();
        try {

            Object inject = injectMap.get(className);

            if (inject == null) {
                //加载build生成的类
                Class<?> aClass = Class.forName(className + "$$InjectActivity");
                inject = aClass.newInstance();
                injectMap.put(className, inject);
            }
            //反射执行方法
            Method m1 = inject.getClass().getDeclaredMethod("inject", activity.getClass());
            m1.invoke(inject, activity);
            Log.i("TAG", "inject: 222");

        } catch (Exception e) {
            Log.i("TAG", "inject: 333 "+e.getMessage());
            e.printStackTrace();
        }
    }
}


