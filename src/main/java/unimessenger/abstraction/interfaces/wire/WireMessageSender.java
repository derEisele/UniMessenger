package unimessenger.abstraction.interfaces.wire;

import com.waz.model.Messages;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.abstraction.wire.crypto.Prekey;
import unimessenger.abstraction.wire.crypto.WireCryptoHandler;
import unimessenger.communication.HTTP;
import unimessenger.userinteraction.tui.Outputs;
import unimessenger.util.enums.REQUEST;

import java.lang.constant.Constable;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class WireMessageSender
{
    public boolean sendMessage(String chatID, Messages.GenericMessage msg)
    {
        if(msg == null) return false;

        String url = URL.WIRE + URL.WIRE_CONVERSATIONS + "/" + chatID + URL.WIRE_OTR_MESSAGES + URL.wireBearerToken();
        String[] headers = new String[]{
                Headers.CONTENT, Headers.JSON,
                Headers.ACCEPT, Headers.JSON};
        String body = buildBody(chatID, msg);
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.POST, body, headers);

        if(response == null) Outputs.create("No response for sent message received", this.getClass().getName()).debug().WARNING().print();
        else if(response.statusCode() == 201)
        {
            Outputs.create("Message sent correctly").verbose().INFO().print();

            return true;
        } else Outputs.create("Response code was " + response.statusCode(), this.getClass().getName()).debug().WARNING().print();
        return false;
    }

    private String buildBody(String chatID, Messages.GenericMessage msg)
    {
        JSONObject obj = new JSONObject();

        obj.put("sender", WireStorage.clientID);
        obj.put("transient", true);

        ArrayList<String> members = new WireData().getConversationMembersFromID(chatID);

        JSONObject recipients = new JSONObject();

        for(String id : members)
        {
            Map<String, Constable> clientMap = new LinkedHashMap<>(members.size());
            ArrayList<String> userClients = getClientIDsFromUser(id);
            if(userClients != null)
            {
                while(!userClients.isEmpty())
                {
                    if(!(id.equals(WireStorage.userID) && userClients.get(0).equals(WireStorage.clientID)))
                    {
                        Prekey pk = getPreKeyForClient(id, userClients.get(0));
                        clientMap.put(userClients.get(0), WireCryptoHandler.encrypt(id, userClients.get(0), pk, msg.toByteArray()));
                    }
                    userClients.remove(0);
                }
            }
            recipients.put(id, clientMap);
        }

        obj.put("recipients", recipients);

        return obj.toJSONString();
    }

    private ArrayList<String> getClientIDsFromUser(String userID)
    {
        String url = URL.WIRE + URL.WIRE_USERS + "/" + userID + URL.WIRE_CLIENTS + URL.wireBearerToken();
        String[] headers = new String[]{
                Headers.ACCEPT, Headers.JSON};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null) Outputs.create("No client response", "WireMessages").debug().WARNING().print();
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
        } else Outputs.create("Response code is " + response.statusCode(), "WireMessages").debug().WARNING().print();

        return null;
    }

    private Prekey getPreKeyForClient(String userID, String clientID)
    {
        String url = URL.WIRE + URL.WIRE_USERS + "/" + userID + URL.WIRE_PREKEY + "/" + clientID + URL.wireBearerToken();
        String[] headers = new String[]{
                Headers.ACCEPT, Headers.JSON};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null) Outputs.create("Could not get a prekey for a client", "WireMessages").debug().WARNING().print();
        else if(response.statusCode() == 200)
        {
            try
            {
                JSONObject obj = (JSONObject) new JSONParser().parse(response.body());
                JSONObject key = (JSONObject) new JSONParser().parse(obj.get("prekey").toString());
                int prekeyID = Integer.parseInt(key.get("id").toString());
                String prekeyKey = key.get("key").toString();
                return new Prekey(prekeyID, prekeyKey);
            } catch(ParseException ignored)
            {
                Outputs.create("Could not get a prekey", "WireMessages").debug().WARNING().print();
            }
        } else Outputs.create("Response code was " + response.statusCode(), "WireMessages").debug().WARNING().print();
        return null;
    }
}