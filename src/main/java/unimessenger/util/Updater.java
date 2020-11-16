package unimessenger.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
            try
            {
                Thread.sleep(2000);
            } catch(InterruptedException ignored)
            {
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
    private boolean validateAccess(Variables.SERVICE service)
    {
        switch(service)
        {
            case WIRE:
                if(!Storage.isWireBearerTokenStillValid())
                {
                    if(Storage.wireAccessCookie != null) refreshAccess();
                    return false;
                }
                break;
            default:
                Outputs.printError("Unknown service: " + service);
                break;
        }
        return true;
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
            JSONObject obj;
            try
            {
                assert false;
                obj = (JSONObject) new JSONParser().parse(response.body());
                Storage.wireBearerToken = obj.get("access_token").toString();
                Storage.setWireBearerTime(Integer.parseInt(obj.get("expires_in").toString()));
                Outputs.printDebug("Successfully refreshed token");
            } catch(ParseException ignored)
            {
                Outputs.printError("Failed refreshing token");
            }
        } else
        {
            Outputs.printDebug("Response code is " + response.statusCode() + ". Deleting Wire access cookie...");
            Storage.wireAccessCookie = null;
            Storage.deleteFile(Storage.wireDataFile);
        }
    }

    private static void initializeServices()
    {
        runningServices.add(Variables.SERVICE.WIRE);
    }
}