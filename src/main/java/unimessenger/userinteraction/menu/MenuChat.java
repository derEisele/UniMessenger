package unimessenger.userinteraction.menu;

import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.IMessages;
import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Inputs;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.MENU;
import unimessenger.util.enums.SERVICE;

public class MenuChat
{
    public static void showMenu()
    {
        //TODO: Show chat of current service

        System.out.println("1) Write new Message");
        System.out.println("2) Show all Conversations in '" + CLI.currentService + "'");
        System.out.println("3) Show Main Menu");
        System.out.println("4) Exit Program");
        int userInput = Inputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                if(sendMessage())
                {
                    Outputs.create("Message successfully sent").verbose().INFO().print();
                } else System.out.println("Couldn't send the message");
                break;
            case 2:
                CLI.currentChatID = null;
                CLI.currentMenu = MENU.CONVERSATION_LIST;
                break;
            case 3:
                CLI.currentChatID = null;
                CLI.currentService = SERVICE.NONE;
                CLI.currentMenu = MENU.MAIN;
                break;
            case 4:
                CLI.currentMenu = MENU.EXIT;
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
}