package com.example.lsposed_demo;

import android.util.Log;

import android.os.Process;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XDebuggable implements IXposedHookZygoteInit {

    private static final int DEBUG_ENABLE_DEBUGGER = 0x1;

    private XC_MethodHook debugAppsHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            // 获取进程名
            String processName = (String) param.args[0];
            XposedBridge.log("Attempting to modify debug flags for process: " + processName);

            // 获取原始的 debugFlags
            int id = 5;
            int flags = (Integer) param.args[id];
            XposedBridge.log("Original debugFlags: " + flags);

            // 修改 debugFlags
            if ((flags & DEBUG_ENABLE_DEBUGGER) == 0) {
                flags |= DEBUG_ENABLE_DEBUGGER;
                param.args[id] = flags;
                XposedBridge.log("Modified debugFlags: " + flags);
            }
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            // 获取修改后的 debugFlags
            int modifiedDebugFlags = (int) param.args[5];

            // 判断是否修改成功
            if ((modifiedDebugFlags & DEBUG_ENABLE_DEBUGGER) != 0) {
                XposedBridge.log("Success: Debug flags modified for process.");
            } else {
                XposedBridge.log("Failed: Debug flags not modified for process.");
            }

            // 输出最终结果
            XposedBridge.log("Final debugFlags: " + modifiedDebugFlags);
        }
    };

    @Override
    public void initZygote(final IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        // Hook 类 android.os.Process 的 start 函数
        XposedBridge.log("Initializing XDebugable in Zygote");
        XposedBridge.hookAllMethods(Process.class, "start", debugAppsHook);
    }
}