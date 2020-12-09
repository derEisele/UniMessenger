package unimessenger.abstraction.storage;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable
{
    private final String text;
    private final Timestamp time;
    private final String senderID;
    public final long expires;

    public Message(String text, Timestamp time, String senderID)
    {
        this(text, time, senderID, -1);
    }
    public Message(String text, Timestamp time, String senderID, long millis)
    {
        this.text = text;
        this.time = time;
        this.senderID = senderID;

        if(millis < 0) expires = -1;
        else expires = (time.getTime() + millis + (1000 * 60 * 60));
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

    public boolean isValid()
    {
        if(expires < 0) return true;
        return expires > System.currentTimeMillis();
    }
}
