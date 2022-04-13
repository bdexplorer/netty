package io.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ZeroCopyServer {
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);
        ServerSocketChannel se = ServerSocketChannel.open();
        ServerSocket socket = se.socket();
        socket.bind(inetSocketAddress);
        ByteBuffer allocate = ByteBuffer.allocate(4096);
        while (true) {
            SocketChannel accept = se.accept();
            System.out.println("客户端连接成功");
            int readCount = 0;
            while (readCount!=-1) {
                readCount = accept.read(allocate);
            }
            // 倒带，position变为0，mark标记作废
            allocate.rewind();
        }
    }
}
