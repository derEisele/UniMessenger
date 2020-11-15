package unimessenger.userinteraction.menu;

import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;

public class MenuWireChat
{
    public static void showMenu()
    {
        //TODO: Show wire chat

        System.out.println("1) Write new Message");
        System.out.println("2) Show Wire Overview");
        System.out.println("3) Show Main Menu");
        System.out.println("4) Exit Program");
        int userInput = Outputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                sendMessage();
                break;
            case 2:
                CLI.currentMenu = CLI.MENU.WireOverview;
                break;
            case 3:
                CLI.currentMenu = CLI.MENU.MainMenu;
                break;
            case 4:
                CLI.currentMenu = CLI.MENU.EXIT;
                break;
            default:
                Outputs.cannotHandleUserInput();
                break;
        }
    }

    private static void sendMessage()
    {
        //TODO: Handle message sending
    }
}