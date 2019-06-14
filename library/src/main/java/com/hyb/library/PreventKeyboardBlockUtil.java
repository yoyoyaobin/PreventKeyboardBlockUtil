package com.hyb.library;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 类描述：防止软键盘弹出时挡住相关按钮或布局
 * 创建人：huangyaobin
 * 创建时间：2019/6/13
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class PreventKeyboardBlockUtil {

    static PreventKeyboardBlockUtil preventKeyboardBlockUtil;
    static Activity activity;
    static View btnView;
    static ViewGroup rootView;
    static boolean isMove;
    static int marginBottom = 0;
    static KeyboardHeightProvider keyboardHeightProvider;
    int keyBoardHeight = 0;

    public static PreventKeyboardBlockUtil getInstance(Activity activity) {

        if (preventKeyboardBlockUtil == null) {
            Utils.init(activity);
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            preventKeyboardBlockUtil.activity = activity;
            rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            if (rootView.getId() == -1) {
                rootView.setId(View.generateViewId());
            }
            isMove = false;
            marginBottom = 0;
            preventKeyboardBlockUtil = new PreventKeyboardBlockUtil();
            keyboardHeightProvider = new KeyboardHeightProvider(activity);
        }

        return preventKeyboardBlockUtil;
    }

    public PreventKeyboardBlockUtil setBtnView(View btnView) {
        preventKeyboardBlockUtil.btnView = btnView;
        return preventKeyboardBlockUtil;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startAnim(msg.arg1);
        }
    };

    void startAnim(int transY){
        float curTranslationY = rootView.getTranslationY();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(rootView, "translationY", curTranslationY, transY);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(objectAnimator);
        animSet.setDuration(200);
        animSet.start();
    }



    public void register() {

        keyboardHeightProvider.setKeyboardHeightObserver(new KeyboardHeightObserver() {
            @Override
            public void onKeyboardHeightChanged(int height, int orientation) {
                Log.i("tag" , "onKeyboardHeightChanged:"+ height);
                if(keyBoardHeight == height){
                    return;
                }else{
                    keyBoardHeight = height;
                }

                if (keyBoardHeight == 0) {//键盘收起
                    if (isMove) {

                        Message message = new Message();
                        message.arg1 = 0;
                        mHandler.sendMessage(message);

                        isMove = true;
                    }
                } else {//键盘打开

                    int keyBorardTopY = ScreenUtils.getAppScreenHeight() - keyBoardHeight;
                    if(keyBorardTopY > (getViewLocationYInScreen(btnView) + btnView.getHeight())){
                        return;
                    }
                    int margin = keyBorardTopY - (getViewLocationYInScreen(btnView) + btnView.getHeight());
                    Log.i("tag" , "margin:" + margin);
                    Message message = new Message();
                    message.arg1 = margin;
                    mHandler.sendMessage(message);

                    isMove = true;
                }

            }
        });

        btnView.post(new Runnable() {
            @Override
            public void run() {
                keyboardHeightProvider.start();
            }
        });

    }

    public void unRegister(){
        keyboardHeightProvider.setKeyboardHeightObserver(null);
        keyboardHeightProvider.close();
    }

    private int getViewLocationYInScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }

}
