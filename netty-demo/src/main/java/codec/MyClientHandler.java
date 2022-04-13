package codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {

    }

    /**
     * 如果发送：ctx.writeAndFlush(Unpooled.copiedBuffer("123456aascaaabcd", CharsetUtil.UTF_8));
     *
     * "123456aascaaabcd"有16个字节
     *
     * 该处理的前一个Handler是MyLongToByteEncoder，此时不会被调用
     *
     * MyLongToByteEncoder父类是MessageToByteEncoder
     *
     * MessageToByteEncoder
     *  1. 判断msg是不是应该处理的类型
     *      if (acceptOutboundMessage(msg)) {}
     *      MessageToByteEncoder<Long>即类型是否为Long
     *  2. 如果是就处理，不是则跳过处理。
     *
     *
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发送数据");
        ctx.writeAndFlush(123456L);
    }
}
