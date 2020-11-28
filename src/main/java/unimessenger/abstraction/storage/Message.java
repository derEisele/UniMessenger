package unimessenger.abstraction.storage;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
    private String text;
    private Timestamp time;

    public Message(String text, Timestamp time){
        this.text=text;
        this.time=time;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getText() {
        return text;
    }
}
