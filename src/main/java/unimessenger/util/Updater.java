package unimessenger.util;

import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.ILoginOut;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.tui.Outputs;
import unimessenger.util.enums.SERVICE;

import java.util.ArrayList;

public class Updater implements Runnable
{
    private static ArrayList<SERVICE> runningServices;

    @Override
    public void run()
    {
        runningServices = new ArrayList<>();
        int seconds = 0;

        while(!Thread.interrupted())
        {
            for(SERVICE service : runningServices)
            {
                if(validateAccess(service))
                {
                    APIAccess access = new APIAccess();
                    if(seconds % 10 == 0) access.getConversationInterface(service).requestAllConversations();//TODO: Refresh only changed conversations if possible
                    if(seconds % 2 == 0 && !access.getMessageInterface(service).receiveNewMessages())//TODO: Might need to change to /await
                    {
                        Outputs.create("Error receiving new messages", this.getClass().getName()).verbose().WARNING().print();
                    }
                } else removeService(service);
            }
            try
            {
                Thread.sleep(1000);
            } catch(InterruptedException ignored)
            {
                return;
            }
            seconds++;
            if(seconds >= 60) seconds = 0;
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
                    if(login.needsRefresh() && WireStorage.getBearerToken() != null) return access.getUtilInterface(service).refreshSession();
                    else return true;
                } else if(WireStorage.getBearerToken() != null && access.getUtilInterface(service).refreshSession()) return true;
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