package unimessenger.userinteraction.menu;

import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.IData;
import unimessenger.abstraction.interfaces.IMessages;
import unimessenger.abstraction.interfaces.wire.WireMessageSender;
import unimessenger.abstraction.storage.Message;
import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Inputs;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.MENU;
import unimessenger.util.enums.SERVICE;

import java.util.ArrayList;

public class MenuChat
{
    public static void showMenu()
    {
        System.out.println("1) Show Messages");
        System.out.println("2) Write new Message");
        System.out.println("3) Show all Conversations in '" + CLI.currentService + "'");
        System.out.println("4) Show Main Menu");
        System.out.println("5) Exit Program");
        if(CLI.currentService == SERVICE.WIRE) System.out.println("6) PING!");
        int userInput = Inputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                showMessages();
                break;
            case 2:
                if(sendMessage())
                {
                    Outputs.create("Message successfully sent").verbose().INFO().print();
                } else System.out.println("Couldn't send the message");
                break;
            case 3:
                CLI.currentChatID = null;
                CLI.currentMenu = MENU.CONVERSATION_LIST;
                break;
            case 4:
                CLI.currentChatID = null;
                CLI.currentService = SERVICE.NONE;
                CLI.currentMenu = MENU.MAIN;
                break;
            case 5:
                CLI.currentMenu = MENU.EXIT;
                break;
            case 6:
                //TODO: Fix to send correct ping message
                if(CLI.currentService == SERVICE.WIRE) new WireMessageSender().sendMessage(CLI.currentChatID, "");
                break;
            default:
                Outputs.create("Invalid option").always().WARNING().print();
                break;
        }
    }

    private static boolean sendMessage()
    {
        String text = Inputs.getTextAnswerFrom("Please enter the text you would like to send.");

        if(text.equals(""))
        {
            System.out.println("No message to send");
            return true;
        }
        System.out.println("Sending: " + text);
        //TODO: Add timed messages

        IMessages msg = new APIAccess().getMessageInterface(CLI.currentService);
        return msg.sendMessage(CLI.currentChatID, text);
    }
    private static void showMessages()
    {
        System.out.println("1) Show all messages");
        System.out.println("2) Show new Messages");
        System.out.println("3) Show a certain amount of messages");

        int in = Inputs.getIntAnswerFrom("Select an option");
        ArrayList<Message> messages;
        IData data = new APIAccess().getDataInterface(CLI.currentService);
        switch(in)
        {
            case 1:
                messages = data.getAllMessagesFromConversation(CLI.currentChatID);
                break;
            case 2:
                messages = data.getNewMessagesFromConversation(CLI.currentChatID);
                break;
            case 3:
                int msgAmount = Inputs.getIntAnswerFrom("How many messages would you like to see?");
                if(msgAmount == -1)
                {
                    Outputs.create("Invalid option").always().WARNING().print();
                    return;
                } else messages = data.getLastXMessagesFromConversation(CLI.currentChatID, msgAmount);
                break;
            default:
                Outputs.create("Invalid option").always().WARNING().print();
                return;
        }
        if(messages == null || messages.isEmpty()) return;

        for(Message msg : messages)
        {
            //TODO: Change to display actual name
            System.out.println(msg.getTime() + " -- " + msg.getSenderID() + ": " + msg.getText());
        }
    }
}