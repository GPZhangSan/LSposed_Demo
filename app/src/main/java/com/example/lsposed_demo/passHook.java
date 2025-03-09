package com.example.lsposed_demo;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class passHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 打印到此的模块名
        XposedBridge.log("init: " + lpparam.packageName);

        // 过滤不必要的应用
        if (!lpparam.packageName.equals("com.smile.gifmaker")) return;
        // 当匹配上时进入处理
        hook(lpparam);
    }

    private void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        // 具体流程

        // 需要Hook的类
        final Class<?> clazz = XposedHelpers.findClass("com.xxx.xxx", lpparam.classLoader);
        // 当模块名匹配时进入处理,Hook的方法
        XposedHelpers.findAndHookMethod(clazz, "hookxxxmethod",  String.class, int.class, new XC_MethodHook() {
            // 进入方法前
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                // 获取参数值
                Object[] arrayobj = param.args;
                String arg0 = (String) arrayobj[0];
                int arg1 = (int) arrayobj[1];

                // 修改参数值
                arrayobj[0] = "修改的值";
                arrayobj[1] = "999";
                XposedBridge.log("arg0：" + arg0 + " arg1" + arg1);
            }

            // 进入方法后
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                // 获取返回值
                String result = (String) param.getResult();
                XposedBridge.log("result：" + result);
                // 修改返回值
                param.setResult("修改返回值");
            }
        });

    }
}
