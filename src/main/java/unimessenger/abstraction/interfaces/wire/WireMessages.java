package unimessenger.abstraction.interfaces.wire;

import unimessenger.abstraction.interfaces.IMessages;
import unimessenger.abstraction.storage.Message;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.abstraction.wire.crypto.MessageCreator;
import unimessenger.abstraction.wire.structures.WireConversation;
import unimessenger.userinteraction.Outputs;

import java.io.File;
import java.sql.Timestamp;

public class WireMessages implements IMessages
{
    private final WireMessageSender sender = new WireMessageSender();
    private final WireMessageReceiver receiver = new WireMessageReceiver();

    @Override
    public boolean sendTextMessage(String chatID, String text)
    {
        WireConversation conversation = WireStorage.getConversationByID(chatID);
        if(conversation != null) conversation.addMessage(new Message(text, new Timestamp(System.currentTimeMillis()), WireStorage.userID));
        else Outputs.create("ConversationID not found", this.getClass().getName()).debug().WARNING().print();
        return sender.sendMessage(chatID, MessageCreator.createGenericTextMessage(text));
    }
    @Override
    public boolean sendFile(String chatID, File file)
    {
        //TODO: Implement
        return false;
    }

    @Override
    public boolean receiveNewMessages()
    {
        return receiver.receiveNewMessages();
    }
}