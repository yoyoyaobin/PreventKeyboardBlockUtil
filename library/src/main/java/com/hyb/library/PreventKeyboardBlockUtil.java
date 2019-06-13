package com.hyb.library;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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

    PreventKeyboardBlockUtil preventKeyboardBlockUtil;
    Activity activity;
    View btnView;
    ViewGroup contentLayout;
    ViewGroup rootView;
    boolean isclContentMove;

    public void getInstance(Activity activity){
        this.activity = activity;
        rootView = ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
        if(preventKeyboardBlockUtil == null){
            preventKeyboardBlockUtil = new PreventKeyboardBlockUtil();
            Utils.init(activity.getApplicationContext());
        }
    }

    public PreventKeyboardBlockUtil setBtnView(View btnView) {
        this.btnView = btnView;
        return this;
    }

    public PreventKeyboardBlockUtil setContentLayout(ViewGroup contentLayout) {
        this.contentLayout = contentLayout;
        return this;
    }

    public void make(){

        if(contentLayout instanceof ConstraintLayout){
            KeyboardUtils.registerSoftInputChangedListener(activity, onSoftInputChangedListener);
        }


    }

    KeyboardUtils.OnSoftInputChangedListener onSoftInputChangedListener = new KeyboardUtils.OnSoftInputChangedListener() {
        @Override
        public void onSoftInputChanged(int height) {
            if (height == 0) {//键盘收起
                if(isclContentMove){
                    TransitionManager.beginDelayedTransition(llRoot);
                    ConstraintSet set = new ConstraintSet();
                    set.clone(llRoot);
                    set.connect(contentLayout.getId(), ConstraintSet.BOTTOM, llRoot.getId(), ConstraintSet.BOTTOM, 0);
                    set.applyTo(llRoot);
                    isclContentMove = true;
                }
            } else {//键盘打开
                if (marginBottom == 0) {
                    marginBottom = ScreenUtils.getScreenHeight() - getViewBottomInScreen(tvRegister);
                }
                if (marginBottom > height) {
                    return;
                }

                TransitionManager.beginDelayedTransition(llRoot);
                int margin = height - marginBottom + ConvertUtils.dp2px(30f);
                ConstraintSet set = new ConstraintSet();
                set.clone(llRoot);
                set.connect(clContent.getId(), ConstraintSet.BOTTOM, llRoot.getId(), ConstraintSet.BOTTOM, margin);
                set.applyTo(llRoot);
                isclContentMove = true;
            }
        }
    };

    public int getViewBottomInScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1] + view.getHeight();
    }

}
