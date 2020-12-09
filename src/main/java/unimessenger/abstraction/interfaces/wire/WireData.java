package unimessenger.abstraction.interfaces.wire;

import unimessenger.abstraction.interfaces.IData;
import unimessenger.abstraction.storage.Message;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.abstraction.wire.structures.WireConversation;
import unimessenger.abstraction.wire.structures.WirePerson;
import unimessenger.userinteraction.tui.Outputs;

import java.util.ArrayList;

public class WireData implements IData
{
    @Override
    public ArrayList<String> getAllConversationIDs()
    {
        ArrayList<String> ids = new ArrayList<>();

        for(WireConversation con : WireStorage.conversations)
        {
            ids.add(con.id);
        }

        Outputs.create("Returning" + ids.size() + " conversation IDs").verbose().INFO().print();
        return ids;
    }

    @Override
    public String getConversationNameFromID(String id)
    {
        for(WireConversation con : WireStorage.conversations)
        {
            if(con.id.equals(id))
            {
                return con.getConversationName();
            }
        }
        return null;
    }

    @Override
    public ArrayList<String> getConversationMembersFromID(String id)
    {
        WireConversation conversation = null;
        for(WireConversation con : WireStorage.conversations)
        {
            if(con.id.equals(id)) conversation = con;
        }

        ArrayList<String> members = new ArrayList<>();
        if(conversation != null)
        {
            for(WirePerson mem : conversation.members)
            {
                members.add(mem.id);
            }
        }

        return members;
    }
    @Override
    public ArrayList<Message> getNewMessagesFromConversation(String conversationID)
    {
        WireConversation con = WireStorage.getConversationByID(conversationID);
        if(con == null) return null;
        return con.getNewMessages();
    }
    @Override
    public ArrayList<Message> getAllMessagesFromConversation(String conversationID)
    {
        WireConversation con = WireStorage.getConversationByID(conversationID);
        if(con == null || con.getMessages().isEmpty()) return null;
        return con.getMessages();
    }
    @Override
    public ArrayList<Message> getLastXMessagesFromConversation(String conversationID, int messages)
    {
        WireConversation con = WireStorage.getConversationByID(conversationID);
        if(con == null || con.getMessages().isEmpty()) return null;
        ArrayList<Message> msgs = con.getMessages();
        while(msgs.size() > messages)
        {
            msgs.remove(0);
        }
        return msgs;
    }
}