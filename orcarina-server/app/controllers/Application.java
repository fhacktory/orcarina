package controllers;

import jobs.Bootstrap;
import org.apache.commons.lang.StringUtils;
import play.*;
import play.cache.Cache;
import play.mvc.*;

import java.util.*;

public class Application extends Controller {

  public static void getMessages(){
    List<String> cache = dumpCache();
    List<String> notes = new ArrayList<>();
    Iterator<String> iterator = cache.iterator();
    while (iterator.hasNext()) {
      String fullNote = iterator.next();
    //for (String fullNote : cache) {
      String noteOnly = fullNote.substring(2, 3);
      //Logger.info("fullNote: " + fullNote);
      //Logger.info("noteOnly: " + noteOnly);
      notes.add(noteOnly);
    }
    String serial = StringUtils.join(notes, ";");
    renderText(serial);
  }

  public static void index() throws Exception {
    ArrayList<String> messages = new ArrayList<String>(
        Arrays.asList("A",
            "B",
            "C",
            "D",
            "E",
            "F"));
    String messagesSent = StringUtils.join(messages, ";");
    //sendToPilot(messages);

    List<String> cache = dumpCache();

    render(messagesSent, cache);
  }

  public static List<String> dumpCache(){
    Object messages = Cache.get(Bootstrap.MESSAGES_CACHE_KEY);
    if (messages == null){
      messages = new ArrayList<String>();
    }
    return (List<String>) messages;
  }

  public static boolean sendToPilot(List<String> messages) {

    int DELAY_SECONDS = 2;
    int size = messages.size();
    for (int i = 0; i < size; i++) {
      String message = messages.get(i);
      message = "75" + message;
      Logger.info("Sending to Pilot: " + message);
      Bootstrap.sendUDPMessage(message);
      if (i != size - 1) {
        try {
          Thread.sleep(DELAY_SECONDS * 1000);
        } catch (InterruptedException e) {
          Logger.error("", e);
        }
      }

    }
    return false;
  }


}