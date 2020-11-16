package unimessenger.userinteraction;

import unimessenger.Main;
import unimessenger.userinteraction.menu.MenuMain;
import unimessenger.userinteraction.menu.MenuWireChat;
import unimessenger.userinteraction.menu.MenuWireLogin;
import unimessenger.userinteraction.menu.MenuWireOverview;

public class CLI implements Runnable
{
    public static MENU currentMenu;

    @Override
    public void run()
    {
        startCLI();
    }

    public static void startCLI()
    {
        currentMenu = MENU.MainMenu;
        while(currentMenu != MENU.EXIT)
        {
            System.out.println("Current Menu: " + currentMenu);
            System.out.println("Options:");
            switch(currentMenu)
            {
                case MainMenu:
                    MenuMain.showMenu();
                    break;
                case WireLogin:
                    MenuWireLogin.showMenu();
                    break;
                case WireOverview:
                    MenuWireOverview.showMenu();
                    break;
                case WireChat:
                    MenuWireChat.showMenu();
                    break;
                default:
                    Outputs.printError("Unknown menu state");
                    Outputs.printDebug("Switching to main menu...");
                    currentMenu = MENU.MainMenu;
                    break;
            }
        }
        Outputs.printDebug("Stopping update thread...");
        Main.updt.stop();
        Outputs.printDebug("Update thread stopped");

        Outputs.printInfo("Exiting program...");
    }

    public enum MENU
    {
        MainMenu,
        WireLogin,
        WireOverview,
        WireChat,
        EXIT
    }
}