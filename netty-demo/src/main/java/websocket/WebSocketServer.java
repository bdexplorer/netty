package websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpServerCodec()) // 因为基于http协议，使用http的编码和解码器
                                    .addLast(new ChunkedWriteHandler()) // 添加ChunkedWriterHandler
                                    /**
                                     * 说明
                                     * 1. http数据在传输过程中是分段的，HTTPObjectAggregate可以将多个段聚合
                                     * 2. 这就是为什么当浏览器发送大量数据时，就会发出多次http请求。
                                     * 3.
                                     */
                                    .addLast(new HttpObjectAggregator(8192))
                                    /**
                                     * 对应Websocket，它的数据是以帧形式传递。
                                     *
                                     * 可以看到WebSocketFrame下面有6个子类
                                     *
                                     * 浏览器请求时ws://localhost:7000/hello
                                     *
                                     * WebSocketServerProtocolHandler将一个http协议升级为ws协议，保持长连接
                                     */
                                    .addLast(new WebSocketServerProtocolHandler("/hello"))
                                    .addLast(new TextWebSocketFrameHandler());
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
