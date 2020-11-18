package unimessenger.util;

import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.ILoginOut;
import unimessenger.apicommunication.HTTP;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.SERVICE;

import java.util.ArrayList;

public class Updater implements Runnable//TODO check if the updater works if the bearer token is outdated
{
    public static ArrayList<SERVICE> runningServices;
    private static HTTP updateHTTP;

    @Override
    public void run()
    {
        updateHTTP = new HTTP();
        runningServices = new ArrayList<>();

        while(true)//TODO: Use a more elegant way
        {
            for(SERVICE service : runningServices)
            {
                if(validateAccess(service))
                {
                    new APIAccess().getConversationInterface(service).requestAllConversations();//TODO: Refresh only changed conversations if possible
                    //TODO: Refresh messages
                }
                else removeService(service);
            }
            try
            {
                Thread.sleep(2000);
            } catch(InterruptedException ignored)
            {
            }
        }
    }

    private boolean validateAccess(SERVICE service)
    {
        ILoginOut login = new APIAccess().getLoginInterface(service);
        switch(service)
        {
            case WIRE:
            case TELEGRAM:
                if(login.checkIfLoggedIn())
                {
                    if(login.needsRefresh())
                    {
                        return login.refresh();
                    }
                    else return true;
                }
                else if(login.refresh()) return true;
                return login.login();
            case NONE:
            default:
                Outputs.printError("Unknown service: " + service);
                break;
        }
        return true;
    }

    public static void addService(SERVICE service)
    {
        if(!runningServices.contains(service)) runningServices.add(service);
    }

    public static void removeService(SERVICE service)
    {
        runningServices.remove(service);
    }
}