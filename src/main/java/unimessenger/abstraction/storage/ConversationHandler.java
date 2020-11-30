package unimessenger.abstraction.storage;

import unimessenger.abstraction.storage.MessengerStructure.WireConversation;
import unimessenger.userinteraction.Outputs;

import java.io.*;
import java.util.ArrayList;

public class ConversationHandler implements Serializable
{

    private static final String FILEPATH = WireStorage.storageDirectory + "/Chats";

    private static ConversationHandler cH;

    private ArrayList<WireConversation> conversations;

    public ConversationHandler()
    {
        conversations = new ArrayList<>();
    }

    public static void clearFile()
    {
        //TODO delteFILE
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

    /*
     * Returns a conversation by ID
     * Returns null if conv is not found
     *
     * */

    public static ConversationHandler getInstance()
    {
        if(cH == null)
        {
            try(FileInputStream fis = new FileInputStream(FILEPATH);
                ObjectInputStream ois = new ObjectInputStream(fis))
            {
                // read object from file
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

        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Deprecated
    public static void Test()
    {

    }
}
