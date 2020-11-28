package unimessenger.util;

import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.ILoginOut;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.SERVICE;

import java.util.ArrayList;

public class Updater implements Runnable
{
    public static ArrayList<SERVICE> runningServices;

    @Override
    public void run()
    {
        runningServices = new ArrayList<>();

        while(!Thread.interrupted())
        {
            for(SERVICE service : runningServices)
            {
                if(validateAccess(service))
                {
                    new APIAccess().getConversationInterface(service).requestAllConversations();//TODO: Refresh only changed conversations if possible
                    //TODO: Refresh messages (Might need to use /await)
                }
                else removeService(service);
            }
            try
            {
                Thread.sleep(2000);
            } catch(InterruptedException ignored)
            {
                return;
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
                    if(login.needsRefresh() && WireStorage.getBearerToken() != null)
                    {
                        return access.getUtilInterface(service).refreshSession();
                    }
                    else return true;
                }
                else if(WireStorage.getBearerToken() != null && access.getUtilInterface(service).refreshSession()) return true;
                return login.login();
            case NONE:
            default:
                Outputs.create("Unknown service: " + service, this.getClass().getName()).debug().ERROR().print();
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