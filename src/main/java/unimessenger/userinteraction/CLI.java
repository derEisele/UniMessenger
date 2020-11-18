package unimessenger.userinteraction;

import unimessenger.Main;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.menu.MenuChat;
import unimessenger.userinteraction.menu.MenuConversationList;
import unimessenger.userinteraction.menu.MenuLogin;
import unimessenger.userinteraction.menu.MenuMain;
import unimessenger.util.enums.SERVICE;

public class CLI implements Runnable
{
    public static MENU currentMenu;
    public static SERVICE currentService;

    @Override
    public void run()
    {
        startCLI();
    }

    public static void startCLI()
    {
        currentMenu = MENU.MAIN;
        currentService = SERVICE.NONE;
        while(currentMenu != MENU.EXIT)
        {
            System.out.println("\n=================================");
            System.out.println("Current Menu: " + currentMenu);
            System.out.println("Options:");
            switch(currentMenu)
            {
                case MAIN:
                    MenuMain.showMenu();
                    break;
                case LOGIN:
                    MenuLogin.showMenu();
                    break;
                case CONVERSATION_LIST:
                    MenuConversationList.showMenu();
                    break;
                case CHAT:
                    MenuChat.showMenu();
                    break;
                default:
                    Outputs.printError("Unknown menu state");
                    Outputs.printDebug("Switching to main menu...");
                    currentMenu = MENU.MAIN;
                    break;
            }
        }
        Outputs.printDebug("Stopping update thread...");
        Main.updt.stop();
        Outputs.printDebug("Update thread stopped");

        Outputs.printDebug("Writing data to file...");
        WireStorage.saveDataInFile();
        Outputs.printDebug("Storage written to file");

        Outputs.printInfo("Exiting program...");
    }

    public enum MENU
    {
        MAIN,
        LOGIN,
        CONVERSATION_LIST,
        CHAT,
        EXIT
    }
}