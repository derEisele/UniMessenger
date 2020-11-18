package unimessenger.abstraction.wire;

import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.IMessages;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.apicommunication.HTTP;
import unimessenger.util.enums.REQUEST;

import java.net.http.HttpResponse;

public class WireMessages implements IMessages
{
    public static void PrintNotifications(){
        HTTP msgSender = new HTTP();
        System.out.println("List of all conversations in Wire:");
        //TODO: Show a list of all Wire-conversations
        String url = URL.WIRE + "/notifications/last" + "?access_token=" + WireStorage.wireBearerToken;
        String[] headers = new String[]{
                "accept", "application/json",
                "accept", "text/html"};
        HttpResponse<String> response = msgSender.sendRequest(url, REQUEST.GET, "", headers);
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Headers:" + response.headers());
        System.out.println("Body: " + response.body());
    }
}