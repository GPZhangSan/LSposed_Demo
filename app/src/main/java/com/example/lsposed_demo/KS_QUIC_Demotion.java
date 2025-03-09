package com.example.lsposed_demo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class KS_QUIC_Demotion implements IXposedHookLoadPackage {
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

        // 需要Hook的方法
        final Class<?> clazz = XposedHelpers.findClass("com.kuaishou.aegon.Aegon", lpparam.classLoader);
        // 当模块名匹配时进入处理,Hook的方法
        XposedHelpers.findAndHookMethod(clazz, "nativeUpdateConfig", new Object[]{String.class, String.class, new XC_MethodHook() {
            // 进入方法前
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                XposedBridge.log("nativeUpdateConfig info ..." + param.args[0].toString());
                param.args[0] = "{\"nqe_params\":{\"HalfLifeSeconds\":\"20\", \"EffectiveConnectionTypeRecomputationInterval\":\"5\"},\"enable_quic\": false, \"enable_redirect_info_report\":true,\"enable_nqe_report\":true,\"congestion_control_frame_interval_sec\": 0, \"quic_max_v6_packet_size\": 1232, \"cdn_preresolver_ip_blacklist\":[\"0.0.0.0\",\"1.1.1.1\",\"127.0.0.1\"], \"preconnect_num_streams\": 2, \"quic_idle_timeout_sec\": 180, \"quic_use_bbr\": true, \"altsvc_broken_time_max\": 600, \"altsvc_broken_time_base\": 60, \"proxy_host_blacklist\": [\"*\"],\"max_os_version_libdispatch_fix_enable\":\"16.2\", \"enable_mtrequest_by_header\":true,\"enable_mtrequest_retry_by_ip\":true,\"resolver_ip_blacklist\":[\"0.0.0.0\",\"1.1.1.1\",\"127.0.0.1\"], \"enable_trace_id_report\": true, \"ipv6_probe_host\":\"net-detect.v4v6.kwd.inkuai.com\", \"disconnect_ipv4_and_connect_ipv6\":true,\"enable_httpdns_request_report\":true,\"enable_backgrounded_report\":true}";
                super.beforeHookedMethod(param);
            }

            // 进入方法后
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedBridge.log("nativeUpdateConfig after ...");
                XposedBridge.log("第1个参数的返回值为 ====" + param.args[0].toString());
                XposedBridge.log("第2个参数的返回值为 ====" + param.args[1].toString());
            }
        }});

    }
}
