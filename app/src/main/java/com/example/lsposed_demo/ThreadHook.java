package com.example.lsposed_demo;

import java.util.ArrayList;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ThreadHook implements IXposedHookLoadPackage {

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
        // hook
        XposedHelpers.findAndHookMethod(Thread.class, "getStackTrace", lpparam.classLoader, new XC_MethodHook() {
            // 执行完之后
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // 判断返回值的类型
                if (param.getResult() instanceof StackTraceElement[]) {
                    // 获取返回值
                    StackTraceElement[] stackTraceElementsArr = (StackTraceElement[]) param.getResult();
                    // 定义一个ArrayList用于过滤保存栈
                    ArrayList<StackTraceElement> arrayList = new ArrayList<>();
                    // 不为空才处理
                    if (stackTraceElementsArr != null) {
                        // 循环遍历StackTraceElement[]
                        for (StackTraceElement stackTraceElement : stackTraceElementsArr) {
                            // 获取当前条的ClassName
                            String className = stackTraceElement.getClassName();
                            // 判断当前条的ClassName是否为敏感信息,不是就用arrList保存
                            if (isInStack(className)) {
                                arrayList.add(stackTraceElement);
                            }
                        }

                        // 将过滤过的栈重新保存
                        stackTraceElementsArr = new StackTraceElement[arrayList.size()];
                        for (int i = 0; i < arrayList.size(); i++) {
                            stackTraceElementsArr[i] = arrayList.get(i);
                        }
                        // 将返回值设置为过滤过的栈
                        param.setResult(stackTraceElementsArr);
                    }
                }
            }
        });
    }

    // 用于判断是否包含敏感信息
    protected static boolean isInStack(String className) {
        if (className.contains("xposed") ||
                className.contains("root") ||
                className.contains("magisk") ||
                className.contains("com.zte.heartyservice.SCC.FrameworkBridge") ||
                className.contains("ZUK") ||
                className.contains("zuk")) {

            return false;
        }
        return true;
    }
}
