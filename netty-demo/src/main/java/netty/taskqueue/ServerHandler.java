package netty.taskqueue;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 1. 自定义一个Handler，需要继承netty规定好的某个HandlerAdapter
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取客户端发送过来的消息
     *
     * @param ctx 上下文对象，含有管道（pipeline），通道，地址
     * @param msg 就是客户端发送的数据，默认是Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 如果这里有一个非常耗时的业务->异步执行->提交到该channel对应的NioEventLoop的taskQueue中。

        /**
         * 这段代码会阻塞,例子
         TimeUnit.SECONDS.sleep(10);
         ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~ 2vesion", CharsetUtil.UTF_8));
         System.out.println("go on ...");
         **/
        /**
         * 解决方案1：用户程序自定义的普通任务
         * 提交到NioEventLoop TaskQueue里面
         */
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~ 2vesion", CharsetUtil.UTF_8));
                System.out.println("go on ...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 30s后返回
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(20);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~ 3vesion", CharsetUtil.UTF_8));
                System.out.println("go on ...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        /**
         * 用户自定义定时任务
         * 提交到NioEventLoop scheduleTaskQueue里面
         */
        ctx.channel().eventLoop().schedule(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~ 4vesion", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.SECONDS);
    }

    /**
     * 数据读取完毕
     *
     * @param ctx 上下文对象
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入缓存并刷新，一般对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~ 1version", CharsetUtil.UTF_8));
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
