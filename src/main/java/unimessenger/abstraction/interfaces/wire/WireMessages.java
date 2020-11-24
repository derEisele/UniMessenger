package unimessenger.abstraction.interfaces.wire;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.IMessages;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.communication.HTTP;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.REQUEST;

import java.lang.constant.Constable;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class WireMessages implements IMessages
{
    @Override
    public boolean sendMessage(String chatID, String text)
    {
        String url = URL.WIRE + URL.WIRE_CONVERSATIONS + "/" + chatID + URL.WIRE_OTR_MESSAGES + URL.WIRE_TOKEN + WireStorage.getBearerToken();
        String[] headers = new String[]{
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};
        String body = buildBody(chatID, text);
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.POST, body, headers);


        if(response == null) Outputs.printError("No response for sent message");
        else if(response.statusCode() == 201)
        {
            Outputs.printInfo("Message sent correctly");
            return true;
        } else Outputs.printError("Response code was " + response.statusCode());

        return false;
    }

    private static String buildBody(String chatID, String msg)
    {
        JSONObject obj = new JSONObject();

        obj.put("data", msg);
        obj.put("sender", WireStorage.clientID);
        obj.put("transient", true);

        ArrayList<String> members = new WireData().getConversationMembersFromID(chatID);
        JSONObject recipients = new JSONObject();

        for(String id : members)
        {
            Map<String, Constable> clientMap = new LinkedHashMap<>(members.size());
            ArrayList<String> userClients = getClientIDsFromUser(id);
            while(!userClients.isEmpty())
            {
                if(!(id.equals(WireStorage.userID) && userClients.get(0).equals(WireStorage.clientID)))
                {
                    //TODO: Use correct OTR Content
                    clientMap.put(userClients.get(0), "OTR Content");
                }
                userClients.remove(0);
            }
            recipients.put(id, clientMap);
        }

        obj.put("recipients", recipients);

        return obj.toJSONString();
    }

    private static ArrayList<String> getClientIDsFromUser(String userID)
    {
        String url = URL.WIRE + URL.WIRE_USERS + "/" + userID + URL.WIRE_CLIENTS + URL.WIRE_TOKEN + WireStorage.getBearerToken();
        String[] headers = new String[]{
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null) Outputs.printDebug("No client response");
        else if(response.statusCode() == 200)
        {
            try
            {
                JSONArray arr = (JSONArray) new JSONParser().parse(response.body());
                ArrayList<String> ids = new ArrayList<>();
                while(!arr.isEmpty())
                {
                    JSONObject obj = (JSONObject) arr.get(0);
                    ids.add(obj.get("id").toString());
                    arr.remove(0);
                }
                return ids;
            } catch(ParseException ignored)
            {
            }
        } else Outputs.printError("Response code is " + response.statusCode());

        return null;
    }

    @Deprecated
    public static void PrintNotifications()
    {
        HTTP msgSender = new HTTP();
        System.out.println("List of all conversations in Wire:");
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