package unimessenger.abstraction.interfaces.wire;

import unimessenger.abstraction.interfaces.IMessages;
import unimessenger.abstraction.storage.Message;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.abstraction.wire.crypto.MessageCreator;
import unimessenger.abstraction.wire.structures.WireConversation;
import unimessenger.userinteraction.Outputs;

import java.io.File;
import java.sql.Timestamp;
import java.util.UUID;

public class WireMessages implements IMessages
{
    private final WireMessageSender sender = new WireMessageSender();
    private final WireMessageReceiver receiver = new WireMessageReceiver();

    @Override
    public boolean sendTextMessage(String chatID, String text)
    {
        WireConversation conversation = WireStorage.getConversationByID(chatID);
        if(conversation != null) conversation.addMessage(new Message(text, new Timestamp(System.currentTimeMillis()), WireStorage.selfProfile.userName));
        else
        {
            Outputs.create("ConversationID not found", this.getClass().getName()).debug().WARNING().print();
            return false;
        }
        return sender.sendMessage(chatID, MessageCreator.createGenericTextMessage(text));
    }
    @Override
    public boolean sendFile(String chatID, File file)
    {
        WireConversation conversation = WireStorage.getConversationByID(chatID);
        if(conversation != null) conversation.addMessage(new Message("FILE", new Timestamp(System.currentTimeMillis()), WireStorage.selfProfile.userName));
        else
        {
            Outputs.create("ConversationID not found", this.getClass().getName()).debug().WARNING().print();
            return false;
        }
        UUID id = UUID.randomUUID();
        boolean previewSent = sender.sendMessage(chatID, MessageCreator.createGenericFilePreviewMessage(file, id));
        boolean assetSent = sender.sendMessage(chatID, MessageCreator.createGenericFileMessage(file, id));
        return previewSent && assetSent;
    }

    @Override
    public boolean receiveNewMessages()
    {
        return receiver.receiveNewMessages();
    }
}