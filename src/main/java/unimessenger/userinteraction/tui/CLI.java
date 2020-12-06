package unimessenger.userinteraction.tui;

import unimessenger.Main;
import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.IData;
import unimessenger.userinteraction.tui.menu.MenuChat;
import unimessenger.userinteraction.tui.menu.MenuConversationList;
import unimessenger.userinteraction.tui.menu.MenuLogin;
import unimessenger.userinteraction.tui.menu.MenuMain;
import unimessenger.util.enums.MENU;
import unimessenger.util.enums.SERVICE;

public class CLI implements Runnable
{
    public static MENU currentMenu;
    public static SERVICE currentService;
    public static String currentChatID;

    @Override
    public void run()
    {
        startCLI();
    }

    public static void startCLI()
    {
        currentMenu = MENU.MAIN;
        currentService = SERVICE.NONE;
        currentChatID = null;

        while(currentMenu != MENU.EXIT)
        {
            IData data = new APIAccess().getDataInterface(currentService);
            System.out.println("\n=================================");
            if(currentMenu == MENU.CHAT) System.out.println("Conversation: " + data.getConversationNameFromID(currentChatID));
            else System.out.println("Current Menu: " + currentMenu);
            System.out.println("=================================");
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
                    Outputs.create("Unknown menu state", "CLI").debug().ERROR().print();
                    Outputs.create("Switching to main menu", "CLI").debug().verbose().WARNING().print();
                    currentMenu = MENU.MAIN;
                    break;
            }
        }
        Main.stp.start();
    }
}