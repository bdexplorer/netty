package protobuf.example2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import protobuf.StudentPojo;

import java.util.Random;

public class HelloClientHandler2 extends ChannelInboundHandlerAdapter {
    /**
     * 当通道就绪就会触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机发送一个对象给服务端
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage message = null;
        if (0 == random) {
            message = MyDataInfo.MyMessage
                    .newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder()
                            .setId(1)
                            .setName("chenwan")
                            .build())
                    .build();
        } else {
            message = MyDataInfo.MyMessage
                    .newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                    .setStudent(MyDataInfo.Worker.newBuilder()
                            .setAge(28)
                            .setName("chenwan")
                            .build())
                    .build();
        }
        ctx.writeAndFlush(message);

    }

    /**
     * 当通道有读取事件时，会触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址：" + ctx.channel().remoteAddress());
    }
}
