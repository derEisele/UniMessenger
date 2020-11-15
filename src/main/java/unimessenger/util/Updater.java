package unimessenger.util;

import unimessenger.apicommunication.RequestBuilder;

import java.util.ArrayList;

public class Updater implements Runnable
{
    public static ArrayList<RequestBuilder.SERVICE> runningServices = new ArrayList<>();

    @Override
    public void run()
    {
        initializeServices();

        while(!runningServices.isEmpty())
        {
            for(RequestBuilder.SERVICE service : runningServices)
            {
                sendRequestToServer(service);
            }
        }
    }

    private void sendRequestToServer(RequestBuilder.SERVICE service)
    {
        //TODO: Send HTTPRequest to server of specified service and ask for new messages
    }

    private static void initializeServices()
    {
        runningServices.add(RequestBuilder.SERVICE.WIRE);
    }
}