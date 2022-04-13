package io.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ZeroCopyClient {
    public static void main(String[] args) throws IOException {
        SocketChannel open = SocketChannel.open();
        open.connect(new InetSocketAddress("127.0.0.1", 7001));
        String filename = "E:\\百度云\\资料.zip";
        FileChannel channel = new FileInputStream(filename).getChannel();
        long startTime = System.currentTimeMillis();
        // 在linux下一个transferTo就可以完成传输
        // 在window下一次调用transferTo只能发送8M，需要分段传输，注意起点和分段。
        long size = 8 * 1024;
        long position = 0;
        long filesize = channel.size();
        long count = 0;
        System.out.println(filesize);
        long l = channel.transferTo(position, filesize, open);
      /*  while (position < filesize) {
            System.out.println(position);

            position = position + l + 1;
            System.out.println(position);
            count += l;
        }*/
        System.out.println("传输大小：" + count + "耗时：" + (System.currentTimeMillis() - startTime));
        open.close();
        channel.close();
    }
}
