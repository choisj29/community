package com.example.community.domain.receiver;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Queue;

@Slf4j
public class ReceiverThread implements Runnable {

    Queue<String> queue;
    Socket socket;
    BufferedReader reader;
    InputStream recv;
//    int BUFFER_SIZE = 2048;
    int BUFFER_SIZE = 4;
    String result;

    public ReceiverThread(Queue<String> queue, Socket socket) throws IOException {
        this.queue = queue;
        this.socket = socket;
        recv = socket.getInputStream();
    }

    @Override
    public void run() {
        log.info("ReceiverThread in ---");
        while(true) {
            // 버퍼 생성
            byte[] data = new byte[BUFFER_SIZE];
            // 메시지를 받는다.
            try {
                recv.read(data, 0, data.length);
                ByteBuffer b = ByteBuffer.wrap(data);
                b.order(ByteOrder.LITTLE_ENDIAN);
                int length = b.getInt();
                // 데이터를 받을 버퍼를 선언한다.
                data = new byte[length];
                // 데이터를 받는다.
                recv.read(data, 0, length);

                // byte형식의 데이터를 string형식으로 변환한다.
                result = new String(data, "UTF-8");
         } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("receive {}", result);

            //queue
            queue.add(new String(result));
            log.info("add queue {}", queue.peek());

            break;

        }
        try {
            this.socket.close();
            log.info("receiver is close.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
