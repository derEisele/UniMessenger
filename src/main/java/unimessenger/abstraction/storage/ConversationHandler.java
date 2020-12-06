package unimessenger.abstraction.storage;

import unimessenger.abstraction.wire.structures.WireConversation;
import unimessenger.userinteraction.Outputs;

import java.io.*;
import java.util.ArrayList;

public class ConversationHandler implements Serializable
{
    private static ConversationHandler cH;
    private static final String FILEPATH = WireStorage.storageDirectory + "/Chats";
    private ArrayList<WireConversation> conversations;

    private ConversationHandler()
    {
        conversations = new ArrayList<>();
    }

    public static void clearFile()
    {
        new File(FILEPATH).delete();

    }

    public ArrayList<WireConversation> getConversations()
    {
        return conversations;
    }

    public void newConversation(WireConversation c)
    {
        conversations.add(c);
    }

    public void clearConvs()
    {
        conversations = new ArrayList<>();
    }

    public static ConversationHandler getInstance()
    {
        if(cH == null)
        {
            try(FileInputStream fis = new FileInputStream(FILEPATH); ObjectInputStream ois = new ObjectInputStream(fis))
            {
                cH = (ConversationHandler) ois.readObject();
            } catch(IOException | ClassNotFoundException ex)
            {
                Outputs.create("ConnectionHandler not on disc or not loaded, Generating new one");
                cH = new ConversationHandler();
                save();
            }
        }
        return cH;
    }

    public static void save()
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(FILEPATH);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(cH);
            objectOut.close();

        } catch(Exception ignored)
        {
            Outputs.create("Error when saving Conversations to file", "ConversationHandler").debug().WARNING().print();
        }
    }
}
