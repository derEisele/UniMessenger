package unimessenger.userinteraction.menu;

import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.Storage;

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
                if(Storage.isWireBearerTokenStillValid() || Storage.wireAccessCookie != null) CLI.currentMenu = CLI.MENU.WireOverview;
                else CLI.currentMenu = CLI.MENU.WireLogin;
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