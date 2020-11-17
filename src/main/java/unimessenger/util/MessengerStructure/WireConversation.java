package unimessenger.util.MessengerStructure;

import java.util.ArrayList;

public class WireConversation
{
    public ACCESS access;
    public String creatorID;
    public ACCESS accessRole;
    public ArrayList<WirePerson> members;
    public String conversationName;
    public String team;
    public String id;
    public int conversationType;
    public String receipt_mode;
    public String last_event_time;
    public String message_timer;
    public String last_event;

    public WireConversation()
    {
        access = ACCESS.NULL;
        creatorID = null;
        accessRole = ACCESS.NULL;
        members = new ArrayList<>();
        conversationName = null;
        team = null;
        id = null;
        conversationType = -1;
        receipt_mode = null;
        last_event_time = null;
        message_timer = null;
        last_event = null;
    }

    public enum ACCESS
    {
        NULL,
        PRIVATE
    }
}