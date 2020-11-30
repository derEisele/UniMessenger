package unimessenger.abstraction.storage;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable
{
    private final String text;
    private final Timestamp time;
    private final String senderID;

    public Message(String text, Timestamp time, String senderID)
    {
        this.text = text;
        this.time = time;
        this.senderID = senderID;
    }

    public Timestamp getTime()
    {
        return time;
    }

    public String getText()
    {
        return text;
    }

    public String getSenderID()
    {
        return senderID;
    }
}
