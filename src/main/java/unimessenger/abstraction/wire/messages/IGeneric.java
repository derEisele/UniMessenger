package unimessenger.abstraction.wire.messages;

import com.waz.model.Messages;

import java.util.UUID;

public interface IGeneric
{
    Messages.GenericMessage createGenericMsg() throws Exception;

    UUID getMessageId();
}
