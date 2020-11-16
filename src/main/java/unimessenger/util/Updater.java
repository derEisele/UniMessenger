package unimessenger.util;

import java.util.ArrayList;

public class Updater implements Runnable
{
    public static ArrayList<Variables.SERVICE> runningServices = new ArrayList<>();

    @Override
    public void run()
    {
        initializeServices();

        while(!runningServices.isEmpty())
        {
            for(Variables.SERVICE service : runningServices)
            {
                sendRequestToServer(service);
            }
        }
    }

    private void sendRequestToServer(Variables.SERVICE service)
    {
        //TODO: Send HTTPRequest to server of specified service and ask for new messages
    }

    private static void initializeServices()
    {
        runningServices.add(Variables.SERVICE.WIRE);
    }
}