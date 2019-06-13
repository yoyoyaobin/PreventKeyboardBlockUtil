package com.hyb.preventkeyboardblock;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.WindowManager;
import android.widget.Button;

import com.hyb.library.PreventKeyboardBlockUtil;

public class AnotherLayoutActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotherlayout);

        Button btnView = findViewById(R.id.btn_submit);
        ConstraintLayout contentLayout = findViewById(R.id.cl_content);
        PreventKeyboardBlockUtil.getInstance(this).setBtnView(btnView).setContentLayout(contentLayout).register();
    }
}
