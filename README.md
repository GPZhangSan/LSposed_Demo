# xposed_init
app/src/main/assets/xposed_init,xposed模块注册
里面只能放一个入口类
如需添加新的Hook类，需要在app/src/main/assets/xposed_init下修改文件全路径

# com.example.lsposed_demo.passHook
一个通用的Xposed空模块

直接下载导入Android Studio中使用即可

# com.example.lsposed_demo.XDebuggable
用于修改ro.debuggable =1 

# com.example.lsposed_demo.ThrowableHook
用于hook Throwable过滤敏感信息

# com.example.lsposed_demo.ThreadHook
用于hook Thread过滤敏感信息
