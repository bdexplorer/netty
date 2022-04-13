package io.nio.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端连接
        SocketChannel serverChannel = serverSocketChannel.accept();
        while (true) {
            int byteRead = 0;
            while (byteRead < 8) {
                long l = serverChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead: " + l);
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> "position= " + byteBuffer.position() + "limit: " + byteBuffer.limit()).forEach(System.out::println);
            }
            // 将所有buffer进行flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());
            // 显示到客户端
            long byteWrites = 0;
            while (byteWrites < 8) {
                long l = serverChannel.write(byteBuffers);
                byteWrites += l;
                System.out.println("byteWrites " + l);
            }

        }


    }
}
