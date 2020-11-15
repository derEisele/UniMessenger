package unimessenger.userinteraction.menu;

import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;

public class MenuMain
{
    public static void showMenu()
    {
        System.out.println("1) Wire");
        System.out.println("2) Exit Program");

        int userInput = Outputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                //TODO: Validate Wire account information and either log user in or show login menu
                CLI.currentMenu = CLI.MENU.WireLogin;
                CLI.currentMenu = CLI.MENU.WireOverview;
                break;
            case 2:
                CLI.currentMenu = CLI.MENU.EXIT;
                break;
            default:
                Outputs.cannotHandleUserInput();
                break;
        }
    }
}