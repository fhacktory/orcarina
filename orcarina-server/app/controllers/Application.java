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
      String noteOnly = fullNote.substring(1, 2);
      String noteString;
      if ("A".equalsIgnoreCase(noteOnly) || "2".equalsIgnoreCase(noteOnly)){
        noteString = "1";
      } else if ("B".equalsIgnoreCase(noteOnly) || "4".equalsIgnoreCase(noteOnly)){
        noteString = "2";
      } else if ("C".equalsIgnoreCase(noteOnly) || "6".equalsIgnoreCase(noteOnly)){
        noteString = "3";
      } else if ("D".equalsIgnoreCase(noteOnly) || "8".equalsIgnoreCase(noteOnly)){
        noteString = "4";
      } else {
        noteString = "1";
      }
      notes.add(noteString);
    }
    String serial = StringUtils.join(notes, ";");
    renderText(serial);
  }

  public static void index() throws Exception {
    //List<String> messages = dumpCache();
    //messages = new ArrayList<>();
    //messages.add("A");
    //sendToPilot(messages);
    //String messagesSent = StringUtils.join(messages, ";");
    render();
  }

  public static List<String> dumpCache(){
    Object messages = Cache.get(Bootstrap.MESSAGES_CACHE_KEY);
    if (messages == null){
      messages = new ArrayList<String>();
    }
    return (List<String>) messages;
  }

  public static boolean sendToPilot(List<String> messages) {

    int size = messages.size();
    for (int i = 0; i < size; i++) {
      String message = messages.get(i);
      message = message.substring(2, 3);
      message = "75" + message;
      Logger.info("Sending to Pilot: " + message);
      Bootstrap.sendUDPMessage(message);
      if (i != size - 1) {

        try {
          Thread.sleep(2 * 100);
        } catch (InterruptedException e) {
          Logger.error("", e);
        }

      }

    }
    return false;
  }

  public static void master(){
    List<String> cache = dumpCache();
    render(cache);
  }


}