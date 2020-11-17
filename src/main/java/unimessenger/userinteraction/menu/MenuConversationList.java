package unimessenger.userinteraction.menu;

import unimessenger.abstraction.APIAccess;
import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.Variables;

public class MenuConversationList
{
    public static void showMenu()
    {
        System.out.println("1) List all '" + CLI.currentService + "' Chats");
        System.out.println("2) Open specific '" + CLI.currentService + "' Chat");
        System.out.println("3) Log out of '" + CLI.currentService + "'");
        System.out.println("4) Show Main Menu");
        System.out.println("5) Exit Program");
        System.out.println("10) Refresh Token");//TODO: Remove

        int userInput = Outputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                listAllConversations();
                break;
            case 2:
                selectChat();
                break;
            case 3:
                new APIAccess().getLoginInterface(CLI.currentService).logout();
            case 4:
                CLI.currentService = Variables.SERVICE.NONE;
                CLI.currentMenu = CLI.MENU.MAIN;
                break;
            case 5:
                CLI.currentMenu = CLI.MENU.EXIT;
                break;
            case 10:
                if(new APIAccess().getUtilInterface(CLI.currentService).refreshSession()) System.out.println("Successfully refreshed your bearer token");
                break;
            default:
                Outputs.cannotHandleUserInput();
                break;
        }
    }

    private static void listAllConversations()
    {
        System.out.println("List of all conversations in '" + CLI.currentService + "':");
        //TODO: Show all conversations of current service
    }

    private static void selectChat()
    {
        String userInput = Outputs.getStringAnswerFrom("Please type in the name of the person or group you would like to see the chat from");
        //TODO: Check if named conversation exists in Wire and open it if true
    }
}