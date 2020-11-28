package unimessenger.abstraction.storage;

import java.io.Serializable;
import java.util.LinkedList;

public class Conversation implements Serializable {
    private String convID;
    private String partnerID;
    private String partnerReadable;
    private LinkedList<Message> messages;

    public Conversation(String convID, String partnerID, String partnerReadable){
        this.convID = convID;
        this.partnerID = partnerID;
        this.partnerReadable = partnerReadable;
        messages = new LinkedList<Message>();
    }

    public void addMessage(Message m){
        messages.add(m);
    }

    public LinkedList<Message> getMessages() {
        return messages;
    }

    public String getConvID() {
        return convID;
    }

    public String getPartnerID() {
        return partnerID;
    }

    public String getPartnerReadable() {
        return partnerReadable;
    }
}
