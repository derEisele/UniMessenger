package unimessenger.abstraction.wire;

import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.IConversations;
import unimessenger.userinteraction.CLI;
import unimessenger.util.Storage;
import unimessenger.util.Variables;

import java.net.http.HttpResponse;

public class WireConversations implements IConversations
{
    @Override
    public boolean requestAllConversations()
    {
        //TODO: Store all Wire conversations
        String url = URL.WIRE + URL.WIRE_CONVERSATIONS + "?access_token=" + Storage.wireBearerToken;
        String[] headers = new String[]{
                "accept", "application/json"};
        HttpResponse<String> response = CLI.userHTTP.sendRequest(url, Variables.REQUESTTYPE.GET, "", headers);
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Headers:" + response.headers());
        System.out.println("Body: " + response.body());

        return false;
    }
}