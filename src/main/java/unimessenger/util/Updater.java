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
        APIAccess access = new APIAccess();
        ILoginOut login = access.getLoginInterface(service);
        switch(service)
        {
            case WIRE:
            case TELEGRAM:
                if(login.checkIfLoggedIn())
                {
                    if(login.needsRefresh())
                    {
                        return access.getUtilInterface(service).refreshSession();
                    }
                    else return true;
                }
                else if(access.getUtilInterface(service).refreshSession()) return true;
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