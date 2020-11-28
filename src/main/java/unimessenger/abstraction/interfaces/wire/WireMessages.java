package unimessenger.abstraction.interfaces.wire;

import unimessenger.abstraction.interfaces.IMessages;

public class WireMessages implements IMessages
{
    private final WireMessageSender sender = new WireMessageSender();
    private final WireMessageReceiver receiver = new WireMessageReceiver();

    @Override
    public boolean sendMessage(String chatID, String text)
    {
        return sender.sendMessage(chatID, text);
    }

    @Override
    public boolean receiveNewMessages()
    {
        return receiver.receiveNewMessages();
    }
}