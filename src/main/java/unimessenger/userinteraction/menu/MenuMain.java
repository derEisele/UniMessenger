package unimessenger.userinteraction.menu;

import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.SERVICE;

public class MenuMain
{
    public static void showMenu()
    {
        System.out.println("1) Wire");
        System.out.println("2) Telegram");
        System.out.println("3) Exit Program");

        int userInput = Outputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                CLI.currentService = SERVICE.WIRE;
                CLI.currentMenu = CLI.MENU.LOGIN;
                break;
            case 2:
                CLI.currentService = SERVICE.TELEGRAM;
                CLI.currentMenu = CLI.MENU.LOGIN;
                break;
            case 3:
                CLI.currentMenu = CLI.MENU.EXIT;
                break;
            default:
                Outputs.cannotHandleUserInput();
                break;
        }
    }
}