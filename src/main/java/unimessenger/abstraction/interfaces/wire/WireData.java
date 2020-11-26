package unimessenger.abstraction.interfaces.wire;

import unimessenger.abstraction.interfaces.IData;
import unimessenger.abstraction.storage.MessengerStructure.WireConversation;
import unimessenger.abstraction.storage.MessengerStructure.WirePerson;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.Outputs;

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
                //TODO: Return name of chat partner if conversation type is NORMAL
                return con.conversationName;
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
}