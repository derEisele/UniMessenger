package unimessenger.abstraction.interfaces.wire;

import com.waz.model.Messages;
import unimessenger.abstraction.storage.Message;
import unimessenger.abstraction.wire.structures.WireConversation;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.MESSAGETYPE;

import java.sql.Timestamp;


public class WireMessageSorter
{
    public static boolean handleReceivedMessage(Messages.GenericMessage message, WireConversation conversation, Timestamp time, String senderUser)
    {
        Message msg;
        switch(getMessageType(message))//TODO: Make more differences between types
        {
            case PICTURE:
                msg = new Message("IMAGE", time, senderUser);
                conversation.addMessage(msg);
                break;
            case PRE_VIDEO:
            case PRE_VIDEO_2:
            case PRE_VOICE_MESSAGE:
            case PRE_FILE:
            case GIF_TEXT:
                break;
            case VIDEO:
                msg = new Message("VIDEO", time, senderUser);
                conversation.addMessage(msg);
                break;
            case VOICE_MESSAGE:
                msg = new Message("VOICE MESSAGE", time, senderUser);
                conversation.addMessage(msg);
                break;
            case FILE:
                msg = new Message("FILE", time, senderUser);
                conversation.addMessage(msg);
                break;
            case GIF_FILE:
                //TODO: Store gifs differently
            case TEXT:
                msg = new Message(message.getText().getContent(), time, senderUser);
                conversation.addMessage(msg);
                break;
            case PING:
                Outputs.create("You have been pinged in: '" + conversation.getConversationName() + "'").always().ALERT().print();
                break;
            case CALL:
                //TODO: Find out the difference between start and end of a call
                Outputs.create("Sommeone is calling you, please accept the call on a different client").always().ALERT().print();
                break;
            case CONFIRMATION:
                Outputs.create("Your message has been received").verbose().INFO().print();
                break;
            case DELETED:
                Outputs.create("Message deletion request received").verbose().INFO().print();
                //TODO: Delete the deleted message on local storage
                break;
            case EDITED:
                Outputs.create("Message editing request received").verbose().INFO().print();
                //TODO: Update the edited message on storage
                break;
            case TIMED:
                Outputs.create("Timed message received").verbose().INFO().print();
                //TODO: Handle timed message
                break;
            case LOCATION:
                //TODO: Give more information about the location
                Outputs.create("Location has been shared").always().INFO().print();
                break;
            case UNKNOWN:
                Outputs.create("Unknown message type received").verbose().INFO().print();
                return false;
            default:
                Outputs.create("Error in detecting the received message type", "WireMessageSorter").debug().ERROR().print();
                return false;
        }
        return true;
    }

    public static MESSAGETYPE getMessageType(Messages.GenericMessage message)
    {
        if(message.hasKnock()) return MESSAGETYPE.PING;
        else if(message.hasText())
        {
            //TODO: Filter gifs
            return MESSAGETYPE.TEXT;
        } else if(message.hasAsset()) return getAssetMessageType(message.getAsset());
        else if(message.hasCalling()) return MESSAGETYPE.CALL;
        else if(message.hasConfirmation()) return MESSAGETYPE.CONFIRMATION;
        else if(message.hasDeleted()) return MESSAGETYPE.DELETED;
        else if(message.hasEdited()) return MESSAGETYPE.EDITED;
        else if(message.hasEphemeral()) return MESSAGETYPE.TIMED;
        else if(message.hasLocation()) return MESSAGETYPE.LOCATION;
        else return MESSAGETYPE.UNKNOWN;
    }
    private static MESSAGETYPE getAssetMessageType(Messages.Asset msg)
    {
        if(msg.hasExpectsReadConfirmation() && msg.hasLegalHoldStatus())
        {
            if(msg.hasOriginal()) return MESSAGETYPE.PRE_FILE;
            else if(msg.hasUploaded()) return MESSAGETYPE.FILE;
        } else if(msg.hasUploaded())
        {
            if(msg.hasPreview()) return MESSAGETYPE.VIDEO;
            else if(msg.getOriginal().hasAudio()) return MESSAGETYPE.VOICE_MESSAGE;
            else if(msg.getOriginal().getMimeType().equals("image/gif")) return MESSAGETYPE.GIF_FILE;
            else if(msg.getOriginal().getMimeType().equals("image/jpeg")) return MESSAGETYPE.PICTURE;
        } else if(msg.hasPreview())
        {
            return MESSAGETYPE.PRE_VIDEO_2;
        }
        else if(msg.hasOriginal())
        {
            if(msg.getOriginal().hasAudio()) return MESSAGETYPE.PRE_VOICE_MESSAGE;
            else if(msg.getOriginal().hasVideo()) return MESSAGETYPE.PRE_VIDEO;
        }
        return MESSAGETYPE.UNKNOWN;
    }
}