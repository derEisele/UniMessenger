package unimessenger.userinteraction.tui.menu;

import unimessenger.userinteraction.tui.CLI;
import unimessenger.userinteraction.tui.Inputs;
import unimessenger.userinteraction.tui.Outputs;
import unimessenger.util.enums.MENU;
import unimessenger.util.enums.SERVICE;

public class MenuMain
{
    public static void showMenu()
    {
        System.out.println("1) Wire");
        System.out.println("2) Telegram");
        System.out.println("3) Exit Program");

        int userInput = Inputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                CLI.currentService = SERVICE.WIRE;
                CLI.currentMenu = MENU.LOGIN;
                break;
            case 2:
                CLI.currentService = SERVICE.TELEGRAM;
                CLI.currentMenu = MENU.LOGIN;
                break;
            case 3:
                CLI.currentMenu = MENU.EXIT;
                break;
            default:
                Outputs.create("Invalid option").always().WARNING().print();
                break;
        }
    }
}