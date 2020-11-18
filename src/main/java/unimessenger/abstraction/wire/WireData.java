package unimessenger.abstraction.wire;

import unimessenger.abstraction.interfaces.IData;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.MessengerStructure.WireConversation;

import java.util.ArrayList;

public class WireData implements IData
{
    @Override
    public String[] getAllConversationNames()
    {
        ArrayList<String> names = new ArrayList<>();

        //TODO: Check if conversations are already sorted; if not, sort after time
        for(WireConversation con : WireStorage.conversations)
        {
            names.add(con.conversationName);
        }

        Outputs.printDebug("Returning " + names.size() + " Conversation names");
        return names.toArray(new String[0]);
    }
}