package com.hyb.preventkeyboardblock;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.hyb.library.PreventKeyboardBlockUtil;


public class AnotherLayoutActivity extends Activity {

    Button btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotherlayout);
        btnView = findViewById(R.id.btn_submit);


    }

    @Override
    protected void onStart() {
        super.onStart();

        PreventKeyboardBlockUtil.getInstance(this).setBtnView(btnView).register();
    }
}
