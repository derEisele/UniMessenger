package unimessenger.abstraction.wire;

import org.json.simple.JSONObject;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.IMessages;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.apicommunication.HTTP;
import unimessenger.util.enums.REQUEST;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class WireMessages implements IMessages
{
    @Override
    public boolean sendMessage(String chatID, String text)//TODO: Make this method work
    {
        String url = URL.WIRE + URL.WIRE_CONVERSATIONS + "/" + chatID + URL.WIRE_OTR_MESSAGES + URL.WIRE_TOKEN + WireStorage.getBearerToken();
        String[] headers = new String[]{
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};
        String body = buildBody(chatID, text);
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.POST, body, headers);

        System.out.println("Response code: " + response.statusCode());
        System.out.println("Headers: " + response.headers());
        System.out.println("Body: " + response.body());
        return false;
    }

    private static String buildBody(String chatID, String msg)
    {
        JSONObject obj = new JSONObject();

        obj.put("data", msg);
        obj.put("sender", WireStorage.userID);

        ArrayList<String> members = new WireData().getConversationMembersFromID(chatID);
        if(members.size() > 0) members.remove(0);
        Map recipients = new LinkedHashMap(1);
        Map clientMap = new LinkedHashMap(members.size());

        for(String id : members)
        {
            clientMap.put("", id);
        }

        recipients.put("", clientMap);
        obj.put("recipients", recipients);

        System.out.println("Body to send: " + obj.toJSONString());
        return obj.toJSONString();
    }

    @Deprecated
    public static void PrintNotifications()
    {
        HTTP msgSender = new HTTP();
        System.out.println("List of all conversations in Wire:");
        //TODO: Show a list of all Wire-conversations
        String url = URL.WIRE + URL.WIRE_LAST_NOTIFICATION + URL.WIRE_TOKEN + WireStorage.getBearerToken();
        String[] headers = new String[]{
                "accept", "application/json",
                "accept", "text/html"};
        HttpResponse<String> response = msgSender.sendRequest(url, REQUEST.GET, "", headers);
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Headers:" + response.headers());
        System.out.println("Body: " + response.body());
    }
}