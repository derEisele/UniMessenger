package unimessenger.abstraction.wire.structures;

import unimessenger.abstraction.storage.Message;
import unimessenger.util.enums.CONVERSATIONTYPE;

import java.io.Serializable;
import java.util.ArrayList;

public class WireConversation implements Serializable
{
    public ArrayList<String> access;
    public String creatorID;
    public String accessRole;
    public ArrayList<WirePerson> members;
    private String conversationName;
    public String team;
    public String id;
    public CONVERSATIONTYPE conversationType;
    public String receipt_mode;
    public String last_event_time;
    public String message_timer;
    public String last_event;
    private final ArrayList<Message> newMessages;
    private final ArrayList<Message> messages;

    public WireConversation()
    {
        access = new ArrayList<>();
        creatorID = null;
        accessRole = null;
        members = new ArrayList<>();
        conversationName = null;
        team = null;
        id = null;
        conversationType = CONVERSATIONTYPE.UNKNOWN;
        receipt_mode = null;
        last_event_time = null;
        message_timer = null;
        last_event = null;
        newMessages = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public void setConversationType(int type)
    {
        switch(type)
        {
            case 0:
                conversationType = CONVERSATIONTYPE.GROUP;
                break;
            case 1:
                conversationType = CONVERSATIONTYPE.OTHER;
                break;
            case 2:
                conversationType = CONVERSATIONTYPE.NORMAL;
                break;
            default:
                conversationType = CONVERSATIONTYPE.UNKNOWN;
                break;
        }
    }

    public void addMessage(Message m)
    {
        newMessages.add(m);
    }
    public ArrayList<Message> getMessages()
    {
        if(newMessages.size() > 0) getNewMessages();

        messages.removeIf(message -> !message.isValid());

        return messages;
    }
    public ArrayList<Message> getNewMessages()
    {
        ArrayList<Message> msgs = new ArrayList<>();

        while(!newMessages.isEmpty())
        {
            messages.add(newMessages.get(0));
            msgs.add(newMessages.get(0));
            newMessages.remove(0);
        }
        return msgs;
    }
    public String getConversationName()
    {
        return conversationName;
    }
    public void setConversationName(String conversationName)
    {
        this.conversationName = conversationName;
    }
}