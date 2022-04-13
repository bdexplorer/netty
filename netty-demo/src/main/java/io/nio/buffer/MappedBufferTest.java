package io.nio.buffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 可以让文件直接在内存（堆外内存）修改，即操作系统不需要拷贝一次。
 */
public class MappedBufferTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\file01.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        // 读写模式
        // 可以修改的起始位置
        // 映射到内存的大小，可以修改多少个字节
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte)'H');
        mappedByteBuffer.put(3, (byte)'9');
        // mappedByteBuffer.put(5, (byte)'x'); 不允许修改位置5，顾头不顾尾
        randomAccessFile.close();
    }
}
