package unimessenger.userinteraction;

import unimessenger.Main;
import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.encryption.WireCrypto.CryptoFactory;
import unimessenger.abstraction.interfaces.IData;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.menu.MenuChat;
import unimessenger.userinteraction.menu.MenuConversationList;
import unimessenger.userinteraction.menu.MenuLogin;
import unimessenger.userinteraction.menu.MenuMain;
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
        Outputs.create("Stopping update thread...").verbose().INFO().print();
        Main.updt.interrupt();
        Outputs.create("Update thread stopped").verbose().INFO().print();

        Outputs.create("Writing data to file...").verbose().INFO().print();
        WireStorage.saveDataInFile();
        Outputs.create("Storage written to file").verbose().INFO().print();

        Outputs.create("Cleaning the Box").verbose().INFO().print();
        CryptoFactory.closeBox();
        Outputs.create("Box Clean").verbose().INFO().print();

        Outputs.create("Exiting program...").verbose().INFO().print();
    }
}