package unimessenger.abstraction.storage;

import unimessenger.userinteraction.Outputs;

import java.io.*;
import java.util.LinkedList;

public class ConversationHandler implements Serializable {

    private static final String FILEPATH="DataStorage/Chats";

    private static ConversationHandler cH;

    private LinkedList<Conversation> conversations;

    public ConversationHandler(){
        conversations = new LinkedList<Conversation>();
    }

    public LinkedList<Conversation> getConversations(){
        return conversations;
    }

    public void newConversation(Conversation c){
        conversations.add(c);
    }

    /*
    * Returns a conversation by ID
    * Returns null if conv is not found
    *
    *
    * */
    public Conversation getConvByID(String convID){
        for(Conversation c : conversations){
            if(c.getConvID()==convID){
                return c;
            }
        }
        return null;
    }

    //TODO test
    public static ConversationHandler getInstance(){
        if(cH == null){
            try (FileInputStream fis = new FileInputStream(FILEPATH);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                // read object from file
                cH = (ConversationHandler) ois.readObject();

            } catch (IOException | ClassNotFoundException ex) {
                Outputs.create("ConnectionHandler not on disc or not loaded, Generating new one");
                cH = new ConversationHandler();
            }
        }
        return cH;
    }

    public static void save(){
        try {
            FileOutputStream fileOut = new FileOutputStream(FILEPATH);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(cH);
            objectOut.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Deprecated
    public static void Test(){
        ConversationHandler ch = ConversationHandler.getInstance();
        String PartnerReadable = "Partner";
        ch.newConversation(new Conversation("1234", "sadf", PartnerReadable));
        ConversationHandler.save();
        cH = null;
        ch = ConversationHandler.getInstance();
        System.out.println(ch.getConversations().get(0).getPartnerReadable());
        System.out.println("Here Assert complete");
    }
}
