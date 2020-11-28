package unimessenger.abstraction.interfaces;

public interface IMessages
{
    boolean sendMessage(String chatID, String text);
    boolean receiveNewMessages();
}