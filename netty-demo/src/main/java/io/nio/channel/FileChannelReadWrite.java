package io.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelReadWrite {
    public static void main(String[] args) throws IOException {

        // 创建一个输出流
        FileInputStream fileInputStream = new FileInputStream("d:\\file01.txt");
        FileChannel fileChannel = fileInputStream.getChannel();

       /* ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        fileChannel.read(byteBuffer);
        byteBuffer.flip();*/

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file02.txt");
        FileChannel outChannel = fileOutputStream.getChannel();
        fileChannel.transferTo(0, fileChannel.size(), outChannel);
    }
}
