package unimessenger.userinteraction.menu;

import unimessenger.abstraction.APIAccess;
import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.MENU;
import unimessenger.util.enums.SERVICE;

import java.util.ArrayList;

public class MenuChat
{
    public static void showMenu()
    {
        //TODO: Show chat of current service

        System.out.println("1) Write new Message");
        System.out.println("2) Show all Conversations in '" + CLI.currentService + "'");
        System.out.println("3) Show Main Menu");
        System.out.println("4) Exit Program");
        System.out.println("10) Send Test Message");//TODO: Remove
        int userInput = Outputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                if(sendMessage())
                {
                    Outputs.printDebug("Message successfully sent");
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
            case 10:
                if(sendTestMessage())
                {
                    System.out.println("Successfully sent message");
                } else System.out.println("Error sending message");
                break;
            default:
                Outputs.cannotHandleUserInput();
                break;
        }
    }

    private static boolean sendMessage()
    {
        //TODO: Handle message sending
        return false;
    }

    @Deprecated
    private static boolean sendTestMessage()
    {
        APIAccess access = new APIAccess();
        ArrayList<String> conID = access.getDataInterface(CLI.currentService).getAllConversationIDs();
        conID.remove(null);
        return access.getMessageInterface(CLI.currentService).sendMessage(conID.get(0), "Test");
    }
}