package com.hyb.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;

import java.util.LinkedList;
import java.util.List;
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
    static Activity mActivity;
    static View mBtnView;
    static ViewGroup rootView;
    static boolean isMove;
    static int marginBottom = 0;
    static KeyboardHeightProvider keyboardHeightProvider;
    int keyBoardHeight = 0;
    int btnViewY = 0;
    boolean isRegister = false;
    AnimatorSet animSet = new AnimatorSet();

    public static PreventKeyboardBlockUtil getInstance(Activity activity) {
        if (preventKeyboardBlockUtil == null) {
            preventKeyboardBlockUtil = new PreventKeyboardBlockUtil();
        }

        initData(activity);

        return preventKeyboardBlockUtil;
    }

    private static void initData(Activity activity) {
        mActivity = activity;
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        rootView = (ViewGroup) ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);
        isMove = false;
        marginBottom = 0;
        keyboardHeightProvider = new KeyboardHeightProvider(mActivity);
    }

    public PreventKeyboardBlockUtil setBtnView(View btnView) {
        mBtnView = btnView;
        return preventKeyboardBlockUtil;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startAnim(msg.arg1);
        }
    };

    void startAnim(int transY) {
        float curTranslationY = rootView.getTranslationY();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(rootView, "translationY", curTranslationY, transY);
        animSet.play(objectAnimator);
        animSet.setDuration(200);
        animSet.start();
    }

    public void register() {

        isRegister = true;

        keyboardHeightProvider.setKeyboardHeightObserver(new KeyboardHeightObserver() {
            @Override
            public void onKeyboardHeightChanged(int height, int orientation) {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    return;
                }
                if(!isRegister){
                    return;
                }
//                Log.i("tag" , "onKeyboardHeightChanged:"+ height);
//                if(lastTime != 0L){
//                    Log.i("tag" , "height:" + height + "    time:" + (System.currentTimeMillis() - lastTime));
//                }
//                lastTime = System.currentTimeMillis();

                if (keyBoardHeight == height) {
                    return;
                } else {
                    keyBoardHeight = height;
                }

                if (keyBoardHeight == 0) {//键盘收起
                    if (isMove) {

                        sendHandlerMsg(0);

                        isMove = true;
                    }
                } else {//键盘打开

                    int keyBorardTopY = ScreenUtils.getAppScreenHeight() - keyBoardHeight;
                    if (keyBorardTopY > (btnViewY + mBtnView.getHeight())) {
                        return;
                    }
                    int margin = keyBorardTopY - (btnViewY + mBtnView.getHeight());
                    Log.i("tag", "margin:" + margin);
                    sendHandlerMsg(margin);

                    isMove = true;
                }

            }
        });

        mBtnView.post(new Runnable() {
            @Override
            public void run() {
                btnViewY = getViewLocationYInScreen(mBtnView);
                keyboardHeightProvider.start();
            }
        });

    }

    public void unRegister() {
        isRegister = false;
        KeyboardUtils.hideSoftInput(mActivity);
        sendHandlerMsg(0);

        keyboardHeightProvider.setKeyboardHeightObserver(null);
        keyboardHeightProvider.close();
    }

    private void sendHandlerMsg(int i) {
        Message message = new Message();
        message.arg1 = i;
        mHandler.sendMessage(message);
    }

    private int getViewLocationYInScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }

}
