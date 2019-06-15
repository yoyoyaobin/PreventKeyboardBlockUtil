# PreventKeyboardBlockUtil

![image](https://github.com/yoyoyaobin/PreventKeyboardBlockUtil/blob/master/app/src/main/assets/1.gif)

## 目的
当前官方提供的三个软键盘方案，一个会顶起输入框并将布局往上推（SOFT_INPUT_ADJUST_PAN），一个会顶起输入框并挤压布局（SOFT_INPUT_ADJUST_RESIZE），一个直接弹出键盘其他不做任何处理（SOFT_INPUT_ADJUST_NOTHING，而前两个方案最多只能将获得焦点的输入框顶起。本库为了解决当键盘弹出时挡住了其他控件（默认只会顶起当前获得焦点的输入框），傻瓜式一键配置，欢迎使用。

## 使用方式
### 1.在project的build.gradle里配置
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### 2.在app的build.gradle里配置
 ```
 dependencies {
	        implementation 'com.github.yoyoyaobin:PreventKeyboardBlockUtil:1.0.0'
	}
 ```
 
 ### 3.在你的activity用上
 ```
 @Override
    protected void onStart() {
        super.onStart();
        PreventKeyboardBlockUtil.getInstance(this).setBtnView(btnSubmit).register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreventKeyboardBlockUtil.getInstance(this).unRegister();
    }
 ```
 其中，setBtnView传入的view是你想要在键盘弹出时置于键盘上的最后一个控件，比如触发登录的按钮。
