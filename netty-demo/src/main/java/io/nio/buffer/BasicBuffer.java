package io.nio.buffer;

import java.nio.IntBuffer;

public class BasicBuffer {
    // Buffer使用
    public static void main(String[] args) {
        // 创建一个Buffer
        IntBuffer intBuf = IntBuffer.allocate(5);

        //存放数据
        intBuf.put(10);
        intBuf.put(11);
        intBuf.put(12);
        intBuf.put(13);
        intBuf.put(14);

        // 读取数据
        // 将buffer转换，读写切换
        intBuf.flip();
        while (intBuf.hasRemaining()) {
            System.out.println(intBuf.get());
        }

    }
}
