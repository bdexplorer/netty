package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * decode会根据接收的数据，被调用多次，直到确定没有新的元素被添加到list，或者ByteBuf没有更多的可读字节为止。
     *
     * 如果list out不为空，就会将list的内容传递给下一个ChannelInboundHandler处理，该处理器的方法也会被调用多次。
     *
     * 这可能导致读取到的数据不是连续的
     *
     * @param ctx
     * @param in
     * @param out 解码后的数据传递给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // long 8个字节
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
    /**
     * ReplayingDecoder
     *  可用于判断是否有可读字节
     *
     *  LineBasedFrameDecoder
     *
     *  DelimiterBasedFrameDecoder
     *
     *
     */


}
