package jobs;

import play.Logger;
import play.cache.Cache;
import play.jobs.*;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

@OnApplicationStart(async=true)
public class Bootstrap extends Job {

  private boolean pause = false;
  final Object    sync = new Object();

  public static final String MESSAGES_CACHE_KEY = "MESSAGES_CACHE_KEY";

  public void doJob() {
    DatagramSocket serverSocket = null;
    try {
      serverSocket = new DatagramSocket(49161 );
      byte[] receiveData = new byte[1024];
      while(true)
      {
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        String sentence = new String( receivePacket.getData());
        Logger.info("RECEIVED FROM ORCA: " + sentence);
        List<String> messages = (ArrayList<String>) Cache.get(MESSAGES_CACHE_KEY);
        if (messages == null){
          messages = new ArrayList<>();
        }
        messages.add(0, sentence);
        messages = new ArrayList<String>(messages.subList(0, Math.min(messages.size(), 16)));
        Cache.set(MESSAGES_CACHE_KEY, messages);

      }
    } catch (Exception e) {
      Logger.error("Datagram error", e);
    }



  }



  public static void sendUDPMessage(String sentence){
    try {
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName("localhost");
      //InetAddress IPAddress = InetAddress.getByName("192.168.1.140");
      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];
      sendData = sentence.getBytes();
       // 49161 is Pilot default UDP port
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 49161);
      clientSocket.send(sendPacket);
      clientSocket.close();
    } catch (Exception e) {
      Logger.error("sendUDPMessage: Datagram error", e);
    }
  }

}