package unimessenger.abstraction.wire;

import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.IConversations;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.apicommunication.HTTP;
import unimessenger.util.enums.REQUEST;

import java.net.http.HttpResponse;

public class WireConversations implements IConversations
{
    @Override
    public boolean requestAllConversations()
    {
        //TODO: Store all Wire conversations
        String url = URL.WIRE + URL.WIRE_CONVERSATIONS + "?access_token=" + WireStorage.wireBearerToken;
        String[] headers = new String[]{
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Headers:" + response.headers());
        System.out.println("Body: " + response.body());

        return false;
    }
}