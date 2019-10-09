
### 8-netty-rpc-zookeeper-shutdownHook
优雅停机

通过 JDK 的 ShutdownHook 来完成优雅停机的，所以如果用户使用 kill -9 PID 等强制关闭指令，是不会执行优雅停机的，只有通过 kill PID 时，才会执行

相关类：
ZookeeperRegister