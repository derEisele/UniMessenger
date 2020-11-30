package unimessenger.abstraction.storage;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable
{
    private String text;
    private Timestamp time;
    private String sender;

    public Message(String text, Timestamp time, String sender)
    {
        this.text = text;
        this.time = time;
        this.sender = sender;
    }

    public Timestamp getTime()
    {
        return time;
    }

    public String getText()
    {
        return text;
    }

    public String getSender()
    {
        return sender;
    }
}
