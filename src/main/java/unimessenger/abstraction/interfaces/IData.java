package unimessenger.abstraction.interfaces;

import unimessenger.abstraction.storage.Message;

import java.util.ArrayList;

public interface IData
{
    ArrayList<String> getAllConversationIDs();
    String getConversationNameFromID(String id);
    ArrayList<String> getConversationMembersFromID(String id);
    ArrayList<Message> getNewMessagesFromConversation(String conversationID);
    ArrayList<Message> getAllMessagesFromConversation(String conversationID);
    ArrayList<Message> getLastXMessagesFromConversation(String conversationID, int messages);
}