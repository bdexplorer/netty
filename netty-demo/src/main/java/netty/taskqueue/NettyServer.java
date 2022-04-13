package netty.taskqueue;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        // 1. 创建BossGroup和WorkerGroup
        // bossGroup只是处理连接请求，真正的客户端业务处理会交给workerGroup
        // 两个都是无限循环
        /**
         * bossGroup和workerGroup含有的子线程（NioEventLoop）的个数，默认为CPU核数*2
         *      默认 DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
         *      io.netty.eventLoopThreads可以通过java -D参数指定
         *
         *      NioEventLoop由EventExecutor管理。
         *          NioEventLoop由自己的select和tasksqueue
         *
         *      BossGroup只是处理连接不需要那么多线程。 new NioEventLoopGroup(1);
         *
         *      Pipeline底层是一个双向链表。
         *
         *      channel和pipeline相互包含。
         *
         *      ctx中有channel和pipeline
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 使用链式编程设置
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象
                        // 给Pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 非当前Reactor线程调用Channel的各种方法：
                            // 每个客户端的SocketChannel都不一样，可以使用集合管理Channel，在推送消息时，
                            // 可以将业务加入到各个channel对应的NIOEventLoop的taskQueue或者scheduleTaskQueue
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    }); // 给workergroup的EventLoop对应的通道设置处理器
            System.out.println("server ready");
            // 绑定一个端口并且同步，生成一个ChannelFuture对象
            // 启动服务器
            ChannelFuture sync = serverBootstrap.bind(6668).sync();
            sync.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("监听端口6668成功");
                    } else {
                        System.out.println("监听端口6668失败");
                    }
                }
            });


            // 对关闭通道进行监听
            sync.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
