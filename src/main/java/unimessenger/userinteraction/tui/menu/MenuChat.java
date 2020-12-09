package unimessenger.userinteraction.tui.menu;

import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.IData;
import unimessenger.abstraction.interfaces.IMessages;
import unimessenger.abstraction.interfaces.wire.WireMessageSender;
import unimessenger.abstraction.storage.Message;
import unimessenger.abstraction.wire.crypto.MessageCreator;
import unimessenger.userinteraction.tui.CLI;
import unimessenger.userinteraction.tui.Inputs;
import unimessenger.userinteraction.tui.Outputs;
import unimessenger.util.enums.MENU;
import unimessenger.util.enums.SERVICE;

import java.io.File;
import java.util.ArrayList;

public class MenuChat
{
    public static void showMenu()
    {
        System.out.println("1) Show Messages");
        System.out.println("2) Write new Message");
        System.out.println("3) Send special Message");
        System.out.println("4) Show all Conversations in '" + CLI.currentService + "'");
        System.out.println("5) Show Main Menu");
        System.out.println("6) Exit Program");
        if(CLI.currentService == SERVICE.WIRE) System.out.println("7) PING!");
        int userInput = Inputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                showMessages();
                break;
            case 2:
                if(sendMessage(0)) Outputs.create("Message successfully sent").verbose().INFO().print();
                else System.out.println("Couldn't send the message");
                break;
            case 3:
                if(sendSpecialMessage()) Outputs.create("Special message successfully sent").verbose().INFO().print();
                else System.out.println("Couldn't send the message");
                break;
            case 4:
                CLI.currentChatID = null;
                CLI.currentMenu = MENU.CONVERSATION_LIST;
                break;
            case 5:
                CLI.currentChatID = null;
                CLI.currentService = SERVICE.NONE;
                CLI.currentMenu = MENU.MAIN;
                break;
            case 6:
                CLI.currentMenu = MENU.EXIT;
                break;
            case 7:
                if(CLI.currentService == SERVICE.WIRE) new WireMessageSender().sendMessage(CLI.currentChatID, MessageCreator.createGenericPingMessage());
                break;
            default:
                Outputs.create("Invalid option").always().WARNING().print();
                break;
        }
    }

    private static void showMessages()
    {
        System.out.println("1) Show all messages");
        System.out.println("2) Show new Messages");
        System.out.println("3) Show a certain amount of messages");
        System.out.println("4) Abort");

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
            case 4:
                return;
            default:
                Outputs.create("Invalid option").always().WARNING().print();
                return;
        }
        if(messages == null || messages.isEmpty()) return;

        for(Message msg : messages)
        {
            System.out.println(msg.getTime() + " -- " + msg.getSenderID() + ": " + msg.getText());
        }
    }
    private static boolean sendMessage(long timed)
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
        if(timed == 0) return msg.sendTextMessage(CLI.currentChatID, text);
        else return msg.sendTimedText(CLI.currentChatID, text, timed);
    }
    private static boolean sendSpecialMessage()
    {
        System.out.println("1) Send file");
        System.out.println("2) Send timed message");
        System.out.println("3) Abort");

        int in = Inputs.getIntAnswerFrom("Select an option");

        switch(in)
        {
            case 1:
                String path = Inputs.getStringAnswerFrom("Please type in the path to the file and the filename you would like to send");
                File file = new File(path);
                return new APIAccess().getMessageInterface(CLI.currentService).sendFile(CLI.currentChatID, file);
            case 2:
                long time = Inputs.getIntAnswerFrom("How many seconds should the message stay?");
                sendMessage(time * 1000);
                break;
            case 3:
                return true;
            default:
                Outputs.create("Invalid option").always().WARNING().print();
                break;
        }

        return false;
    }
}