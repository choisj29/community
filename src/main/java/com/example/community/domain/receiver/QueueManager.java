package com.example.community.domain.receiver;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.util.*;



@Slf4j
public class QueueManager {

    public static List<Object> getQueue(String mid, String keyword) throws IOException, InterruptedException {
        List<Object> returnObj = new ArrayList<>();
        //make a client socket
        Socket socket = new Socket();
//        String hostName = "127.0.0.1";  //local
//        String hostName = "3.34.172.42"; //aws
        String hostName = "101.101.211.172"; //naver
        int port = 9999;
        SocketAddress address = new InetSocketAddress(hostName, port);
        try {
            socket.connect(address);
        }catch (Exception e){
            log.info("socket.connect error {}", e.getStackTrace());
            returnObj.add("connection error");
            return returnObj;
        }

        //add thread parameter "Queue"
        Queue<String> queue = new LinkedList<>();

        //start receiver
        log.info("ReceiverThread {} {} {} ", queue, socket);
        log.info("ReceiverThread keyword {} ", keyword);
        Runnable receiverThread = new ReceiverThread(queue, socket); //[itemNum]
        //new Thread(receiverThread).start();
        Thread thread = new Thread(receiverThread);
        thread.start();

        //get item info
        Map<String, Object> itemList = new HashMap<String, Object>();
        itemList.put("mid", mid); //[mid]
        itemList.put("keyword", keyword); //[keywords]

        JSONObject jsonObject = new JSONObject(itemList); //[mid, keywords]
        log.info("getQueue jsonObject {}", jsonObject);
        //send data to server
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.println(jsonObject.toString());
        out.flush();

        //receive queue
        thread.join();
        log.info("queue {}", queue);
        log.info("queue.size {}", queue.size());
        log.info("queue peek {}", queue.peek());

        while(!queue.isEmpty()){
            String queuePoll = queue.poll();
            if (queuePoll.contains("{")) { //json 형태로 rank 조회됨
                JSONObject returnJsonObj = new JSONObject(queuePoll);
                log.info("returnJsonObj {}", returnJsonObj);

                String resultRank = String.valueOf(returnJsonObj.get("rank"));
                String itemName = (String) returnJsonObj.get("item_name");
                String resultImgURL = (String) returnJsonObj.get("img_url");
                String renewDate = String.valueOf(LocalDateTime.now());
                HashMap<String, String> map = new HashMap<>();
                map.put("rank", resultRank);
                map.put("itemName", itemName);
                map.put("imgLink", resultImgURL);
                map.put("renewDate", renewDate);
                map.put("keyword", keyword);
                log.info("map {}", map);

                JSONObject jo = new JSONObject(map);
                returnObj.add(jo);

            } else {
                log.info("no match keyword");
                HashMap<String, String> map = new HashMap<>();
                map.put("rank", null);
                map.put("itemName", null);
                map.put("imgLink", null);
                log.info("map {}", map);

                JSONObject jo = new JSONObject(map);
                returnObj.add(jo);
                return returnObj;
            }
        }
        log.info("returnObj {}", returnObj);

        return returnObj;
    }
}
