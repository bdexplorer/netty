package protobuf;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 1. 自定义一个Handler，需要继承netty规定好的某个HandlerAdapter
 */
public class HelloServerHandler1 extends SimpleChannelInboundHandler<StudentPojo.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPojo.Student stu) throws Exception {
        System.out.println("客户端发送的数据, Id: " + stu.getId() + "名字：" + stu.getName());
    }

}
