package unimessenger.userinteraction;

import unimessenger.apicommunication.HTTP;
import unimessenger.userinteraction.menu.MenuMain;
import unimessenger.userinteraction.menu.MenuWireChat;
import unimessenger.userinteraction.menu.MenuWireLogin;
import unimessenger.userinteraction.menu.MenuWireOverview;
import unimessenger.util.Storage;
import unimessenger.util.Variables;

public class CLI implements Runnable
{
    public static MENU currentMenu;
    public static HTTP userHTTP;

    @Override
    public void run()
    {
        startCLI();
    }

    public static void startCLI()
    {
        currentMenu = MENU.MainMenu;
        userHTTP = new HTTP();
        while(currentMenu != MENU.EXIT)
        {
            System.out.println("\n=================================");
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
        Variables.updt.stop();
        Outputs.printDebug("Update thread stopped");

        Outputs.printDebug("Writing data to file...");
        Storage.writeDataToFile();
        Outputs.printDebug("Storage written to file");

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