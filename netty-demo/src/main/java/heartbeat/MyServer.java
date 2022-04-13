package heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) // 在bossGroup增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /**
                             * IdleStateChannel是netty提供的处理空闲状态的处理器
                             * readerIdleTime：表示有多长时间没有读，就会发送一个心跳检测包检测是否连接
                             * writerIdleTime：表示有多长时间没有写，就会发送一个心跳检测包检测客户端是否连接
                             * allIdleTime：表示有多长时间没有读写，就会发送一个心跳检测包检测客户端是否连接
                             *
                             * Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                             * read, write, or both operation for a while.
                             *
                             * 当IdleStateEvent触发后，会传递给管道的下一个handler处理。通过userEventTriggered处理IdleStateEvent。
                             */
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS))
                                    .addLast(new MyServerHandler()); //加入自定义空闲检测handler
                        }
                    });
            ChannelFuture sync = bootstrap.bind(7000).sync();
            sync.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
