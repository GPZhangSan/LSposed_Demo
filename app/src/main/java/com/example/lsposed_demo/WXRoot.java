package com.example.lsposed_demo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class WXRoot implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 打印到此的模块名
        XposedBridge.log("init: " + lpparam.packageName);

        // 过滤不必要的应用
        if (!lpparam.packageName.equals("com.tencent.mm")) return;
        // 当匹配上时进入处理
        hook(lpparam);
    }

    private void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        // 需要Hook的类,替换成对应版本的类即可
        final Class<?> clazz = XposedHelpers.findClass("uu4.e", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz, "a", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(0);
            }
        });
    }
}
