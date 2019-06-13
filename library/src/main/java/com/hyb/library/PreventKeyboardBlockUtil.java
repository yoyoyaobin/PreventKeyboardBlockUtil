package com.hyb.library;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;

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
    static ViewGroup contentLayout;
    static ViewGroup rootView;
    static boolean isMove;
    static int marginBottom = 0;

    public static PreventKeyboardBlockUtil getInstance(Activity activity){
        Utils.init(activity);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        if(preventKeyboardBlockUtil == null){
            preventKeyboardBlockUtil = new PreventKeyboardBlockUtil();
        }

        preventKeyboardBlockUtil.activity = activity;
        rootView = (ViewGroup) ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
        //如果布局没有设置id则动态生成
        if(rootView.getId() == -1){
            rootView.setId(View.generateViewId());
        }
        isMove = false;
        marginBottom = 0;

        return preventKeyboardBlockUtil;
    }

    public PreventKeyboardBlockUtil setBtnView(View btnView) {
        preventKeyboardBlockUtil.btnView = btnView;
        return preventKeyboardBlockUtil;
    }

    public PreventKeyboardBlockUtil setContentLayout(ViewGroup contentLayout) {
        preventKeyboardBlockUtil.contentLayout = contentLayout;
        return preventKeyboardBlockUtil;
    }

    public void register(){

        if(contentLayout instanceof ConstraintLayout){
            KeyboardUtils.registerSoftInputChangedListener(activity, onSoftInputChangedListenerForConstraintLayout);
        }else{
            KeyboardUtils.registerSoftInputChangedListener(activity, onSoftInputChangedListenerForAnotherLayout);
        }


    }

    KeyboardUtils.OnSoftInputChangedListener onSoftInputChangedListenerForConstraintLayout = new KeyboardUtils.OnSoftInputChangedListener() {
        @Override
        public void onSoftInputChanged(int height) {
            if (height == 0) {//键盘收起
                if(isMove){
                    TransitionManager.beginDelayedTransition(rootView);
                    ConstraintSet set = new ConstraintSet();
                    set.clone((ConstraintLayout) rootView);
                    set.connect(contentLayout.getId(), ConstraintSet.BOTTOM, rootView.getId(), ConstraintSet.BOTTOM, 0);
                    set.applyTo((ConstraintLayout) rootView);
                    isMove = true;
                }
            } else {//键盘打开
                if (marginBottom == 0) {
                    marginBottom = ScreenUtils.getScreenHeight() - getViewBottomInScreen(btnView);
                }
                if (marginBottom > height) {
                    return;
                }

                TransitionManager.beginDelayedTransition(rootView);
                int margin = height - marginBottom + ConvertUtils.dp2px(30f);
                ConstraintSet set = new ConstraintSet();
                set.clone((ConstraintLayout) rootView);
                set.connect(contentLayout.getId(), ConstraintSet.BOTTOM, rootView.getId(), ConstraintSet.BOTTOM, margin);
                set.applyTo((ConstraintLayout) rootView);
                isMove = true;
            }
        }
    };


    KeyboardUtils.OnSoftInputChangedListener onSoftInputChangedListenerForAnotherLayout = new KeyboardUtils.OnSoftInputChangedListener() {
        @Override
        public void onSoftInputChanged(int height) {
            if(height == 0){

            }else{
                if (marginBottom == 0) {
                    marginBottom = ScreenUtils.getScreenHeight() - getViewBottomInScreen(btnView);
                }
                if (marginBottom > height) {
                    return;
                }

                ViewGroup.LayoutParams params = contentLayout.getLayoutParams();
                ViewGroup.MarginLayoutParams marginParams = null;
                //获取view的margin设置参数
                if (params instanceof ViewGroup.MarginLayoutParams) {
                    marginParams = (ViewGroup.MarginLayoutParams) params;
                } else {
                    //不存在时创建一个新的参数
                    //基于View本身原有的布局参数对象
                    marginParams = new ViewGroup.MarginLayoutParams(params);
                }

                marginParams.bottomMargin = marginBottom;
                contentLayout.setLayoutParams(marginParams);

            }
        }
    };

    private int getViewBottomInScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1] + view.getHeight();
    }

}
