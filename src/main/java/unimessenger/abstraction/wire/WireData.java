package unimessenger.abstraction.wire;

import unimessenger.abstraction.interfaces.IData;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.MessengerStructure.WireConversation;
import unimessenger.util.MessengerStructure.WirePerson;

import java.util.ArrayList;

public class WireData implements IData
{
    @Override
    public ArrayList<String> getAllConversationIDs()
    {
        //TODO: Check if conversations are already sorted; if not, sort after most recent activity
        ArrayList<String> ids = new ArrayList<>();

        for(WireConversation con : WireStorage.conversations)
        {
            ids.add(con.id);
        }

        Outputs.printDebug("Returning " + ids.size() + " Conversation IDs");
        return ids;
    }

    @Override
    public String getConversationNameFromID(String id)
    {
        for(int i = 0; i < WireStorage.conversations.size(); i++)
        {
            if(WireStorage.conversations.get(i).id.equals(id)) return WireStorage.conversations.get(i).conversationName;
        }
        return null;
    }

    @Override
    public ArrayList<String> getConversationMembersFromID(String id)//TODO: Check if members are in conversation list and if all are returned
    {
        WireConversation conversation = null;
        for(int i = 0; i < WireStorage.conversations.size(); i++)
        {
            if(WireStorage.conversations.get(i).id.equals(id)) conversation = WireStorage.conversations.get(i);
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