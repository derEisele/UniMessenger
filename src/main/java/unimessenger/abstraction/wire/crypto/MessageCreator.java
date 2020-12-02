package unimessenger.abstraction.wire.crypto;

import com.waz.model.Messages;
import unimessenger.abstraction.wire.messages.MessageText;
import unimessenger.abstraction.wire.messages.Ping;

public class MessageCreator
{
    public static Messages.GenericMessage createGenericTextMessage(String text)
    {
        MessageText msg = new MessageText(text);
        return msg.createGenericMsg();
    }

    public static Messages.GenericMessage createGenericPingMessage()
    {
        Ping msg = new Ping();
        return msg.createGenericMsg();
    }
}