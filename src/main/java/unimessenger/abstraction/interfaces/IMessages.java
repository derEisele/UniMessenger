package unimessenger.abstraction.interfaces;

import java.io.File;

public interface IMessages
{
    boolean sendTextMessage(String chatID, String text);
    boolean sendFile(String chatID, File file);

    boolean receiveNewMessages();
}