package com.example.lsposed_demo;

import android.util.Log;

import android.os.Process;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XDebugable implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private static final int DEBUG_ENABLE_DEBUGGER = 0x1;

    private XC_MethodHook debugAppsHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param)
                throws Throwable {

            XposedBridge.log("-- beforeHookedMethod :" + param.args[1]);

            int id = 5;
            int flags = (Integer) param.args[id];
            // 修改类android.os.Process的start函数的第6个传入参数
            if ((flags & DEBUG_ENABLE_DEBUGGER) == 0) {
                // 增加开启Android调试选项的标志
                flags |= DEBUG_ENABLE_DEBUGGER;
            }
            param.args[id] = flags;


            if (BuildConfig.DEBUG) {
                XposedBridge.log("-- app debugable flags to 1 :" + param.args[1]);
            }
        }
    };

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

    }

    // 实现的接口IXposedHookZygoteInit的函数
    @Override
    public void initZygote(final IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {

        // /frameworks/base/core/java/android/os/Process.java
        // Hook类android.os.Process的start函数
        Log.e("hook ", "initZygote");
        XposedBridge.hookAllMethods(Process.class, "start", debugAppsHook);
    }
}
