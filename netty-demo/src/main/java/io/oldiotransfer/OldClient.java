package io.oldiotransfer;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class OldClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 7001);
        String filename = "E:\\百度云\\资料.zip";
        FileInputStream fileInputStream = new FileInputStream(filename);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();

        while ((readCount = fileInputStream.read(buffer)) > 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime)); // 耗时：2210
        dataOutputStream.close();
        socket.close();
        fileInputStream.close();
    }
}
