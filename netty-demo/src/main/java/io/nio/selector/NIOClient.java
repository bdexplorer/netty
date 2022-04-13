package io.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        // 提供服务器端的IP端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，不会阻塞");
            }
        }

        String str = "Hello World";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 发送数据，将Buffer数据写入Channel
        socketChannel.write(buffer);
        System.in.read();
    }
}
