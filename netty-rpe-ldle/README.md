# 配置IdleStateHandler心跳机制
心跳机制

﻿心跳是在TCP长连接中，客户端和服务端定时向对方发送数据包通知对方自己还在线，保证连接的有效性的一种机制
在服务器和客户端之间一定时间内没有数据交互时, 即处于 idle 状态时, 客户端或服务器会发送一个特殊的数据包给对方, 当接收方收到这个数据报文后, 也立即发送一个特殊的数据报文, 回应发送方, 此即一个 PING-PONG 交互. 自然地, 当某一端收到心跳消息后, 就知道了对方仍然在线, 这就确保 TCP 连接的有效性.
心跳实现

使用TCP协议层的Keeplive机制，但是该机制默认的心跳时间是2小时，依赖操作系统实现不够灵活；

应用层实现自定义心跳机制，比如Netty实现心跳机制；