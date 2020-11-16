package unimessenger.util;

import unimessenger.apicommunication.HTTP;
import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Updater implements Runnable
{
    public static ArrayList<Variables.SERVICE> runningServices = new ArrayList<>();
    private static HTTP updateHTTP;

    @Override
    public void run()
    {
        updateHTTP = new HTTP();
        initializeServices();

        while(!runningServices.isEmpty())
        {
            for(Variables.SERVICE service : runningServices)
            {
                validateAccess(service);
                sendRequestToServer(service);
            }
        }
    }

    private void sendRequestToServer(Variables.SERVICE service)
    {
        switch(service)
        {
            case WIRE:
                //TODO: Send HTTPRequest to server and ask for new messages
                break;
            default:
                break;
        }
    }
    private void validateAccess(Variables.SERVICE service)
    {
        switch(service)
        {
            case WIRE:
                if(!Storage.isWireBearerTokenStillValid())
                {
                    if(Storage.wireAccessCookie != null) refreshAccess();
                }
                break;
            default:
                Outputs.printError("Unknown service: " + service);
                break;
        }
    }
    public static void refreshAccess()
    {
        String url = Variables.URL_WIRE + Commands.ACCESS + "?access_token=" + Storage.wireBearerToken;
        String[] headers = new String[]{
                "cookie", Storage.wireAccessCookie,
                "content-type", "application/json",
                "accept", "application/json"};

        HttpResponse<String> response = CLI.userHTTP.sendRequest(url, Variables.REQUESTTYPE.POST, "", headers);

        if(response == null)
        {
            Outputs.printError("Couldn't get a HTTP response");
        } else if(response.statusCode() == 200)
        {
            Outputs.printDebug("Successfully refreshed token");
        } else
        {
            Outputs.printDebug("Response code is not 200. Deleting Wire access cookie");
            Storage.wireAccessCookie = null;
            Storage.deleteFile(Storage.wireDataFile);
        }
    }

    private static void initializeServices()
    {
        runningServices.add(Variables.SERVICE.WIRE);
    }
}