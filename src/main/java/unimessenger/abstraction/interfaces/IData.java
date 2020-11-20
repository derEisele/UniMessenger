package unimessenger.abstraction.interfaces;

import java.util.ArrayList;

public interface IData
{
    ArrayList<String> getAllConversationIDs();
    String getConversationNameFromID(String id);
    ArrayList<String> getConversationMembersFromID(String id);
}