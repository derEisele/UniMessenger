package unimessenger.userinteraction;

import unimessenger.Main;
import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.IData;
import unimessenger.abstraction.storage.StorageCrypto;
import unimessenger.userinteraction.menu.MenuChat;
import unimessenger.userinteraction.menu.MenuConversationList;
import unimessenger.userinteraction.menu.MenuLogin;
import unimessenger.userinteraction.menu.MenuMain;
import unimessenger.util.enums.MENU;
import unimessenger.util.enums.SERVICE;

import java.security.UnrecoverableKeyException;
import java.util.Scanner;

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

    public static void preRun(){
        checkPassword();
    }

    private static void checkPassword() {
        Scanner s = new Scanner(System.in);
        String in;
        do{
            System.out.println("Do you want to encrypt your stored information on disc? Y/N");
            System.out.println("If you have ever encrypted anything, you won't be able to read it without the Password");
            System.out.println("If you forgot your password or want to delete your local data for any other reason, you can delete it with D");
            in = s.next();
        }while(in.equalsIgnoreCase("Y") && in.equalsIgnoreCase("N") && in.equalsIgnoreCase("D"));
        if(in.equalsIgnoreCase("N")){
            StorageCrypto.setPassphrase("");
        }
        else if(in.equalsIgnoreCase("D")){
            System.out.println("Are you certain? All files will be deleted beyond recovery! Type DELETE to confirm:");
            in = s.next();
            if(in.equalsIgnoreCase("DELETE")){
                StorageCrypto.removeAll();
            }
            //TODO possibly go back to the start or something
            System.exit(0);
        }
        else{
            System.out.println("Please enter your Password (if you used encryption on this device before, decrypt by entering the old password): ");
            in=s.next();
            StorageCrypto.setPassphrase(in);
            //Creating a crypto file to test the validity of the passphrase
            try {
                new StorageCrypto();
            } catch (UnrecoverableKeyException e) {
                System.out.println("Wrong Password, please try again.");
            }
        }
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