# PreventKeyboardBlockUtil
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
