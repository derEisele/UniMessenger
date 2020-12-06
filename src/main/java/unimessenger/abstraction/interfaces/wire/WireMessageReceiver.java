package unimessenger.abstraction.interfaces.wire;

import com.google.protobuf.InvalidProtocolBufferException;
import com.waz.model.Messages;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.abstraction.wire.crypto.WireCryptoHandler;
import unimessenger.abstraction.wire.structures.WireConversation;
import unimessenger.communication.HTTP;
import unimessenger.userinteraction.tui.Outputs;
import unimessenger.util.enums.REQUEST;

import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.UUID;

public class WireMessageReceiver
{
    public boolean receiveNewMessages()
    {
        String client = "?client=" + WireStorage.clientID;
        String since = "&since=2020-11-27T10:47:39.941Z";//TODO: Fix string
        String token = URL.wireBearerToken();
        String url = URL.WIRE + URL.WIRE_NOTIFICATIONS + client + since + token;
        String[] headers = new String[]{
                Headers.CONTENT, Headers.JSON,
                Headers.ACCEPT, Headers.JSON};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null)
            Outputs.create("No response received", this.getClass().getName()).debug().WARNING().print();
        else if(response.statusCode() == 200 || response.statusCode() == 404)
        {
            try
            {
                JSONObject obj = (JSONObject) new JSONParser().parse(response.body());
                JSONArray arr = (JSONArray) obj.get("notifications");
                for(Object o : arr)
                {
                    JSONObject note = (JSONObject) o;
                    JSONArray payload = (JSONArray) note.get("payload");
                    for(Object pl : payload)
                    {
                        JSONObject load = (JSONObject) pl;
                        if(load.get("type").equals("conversation.otr-message-add"))
                        {
                            if(!handleMessage(load)) Outputs.create("Error receiving text of a notification").verbose().WARNING().print();
                        }
                    }
                }
                if(obj.containsKey("has_more") && Boolean.getBoolean(obj.get("has_more").toString()))
                {
                    if(!receiveNewMessages()) return false;//TODO: Find out if it works
                } else WireStorage.saveDataInFile();
            } catch(ParseException ignored)
            {
                Outputs.create("Something went wrong when parsing the HTTP response", this.getClass().getName()).debug().WARNING();
            }
            return true;
        } else Outputs.create("Response code was " + response.statusCode(), this.getClass().getName()).debug().WARNING().print();
        return false;
    }

    private boolean handleMessage(JSONObject payload)
    {
        String conversationID;
        String senderUser = null;
        Timestamp time = null;
        Messages.GenericMessage message;

        if(payload.containsKey("conversation")) conversationID = payload.get("conversation").toString();
        else
        {
            Outputs.create("Conversation notification has no 'conversation' key", this.getClass().getName()).debug().WARNING().print();
            return false;
        }

        if(!payload.containsKey("data"))
        {
            Outputs.create("Conversation notification has no 'data' key", this.getClass().getName()).debug().WARNING().print();
            return false;
        }

        if(payload.containsKey("from")) senderUser = payload.get("from").toString();
        else Outputs.create("Conversation notification has no 'from' key").verbose().WARNING().print();

        if(payload.containsKey("time"))
        {
            time = Timestamp.valueOf(payload.get("time").toString().replace("T", " ").replace("Z", ""));
            if(WireStorage.lastNotification != null && time.getTime() <= WireStorage.lastNotification.getTime())
            {
                Outputs.create("Notification filtered because of timestamp").verbose().INFO().print();
                return false;
            } else WireStorage.lastNotification = time;
        } else Outputs.create("Conversation notification has no 'time' key").verbose().WARNING().print();

        JSONObject data = (JSONObject) payload.get("data");

        if(!data.get("recipient").toString().equals(WireStorage.clientID))
        {
            Outputs.create("Message is not for this client").verbose().INFO().print();
            return false;
        }

        byte[] decrypted = WireCryptoHandler.decrypt(UUID.fromString(payload.get("from").toString()), data.get("sender").toString(), data.get("text").toString());
        try
        {
            message = Messages.GenericMessage.parseFrom(decrypted);
        } catch(InvalidProtocolBufferException e)
        {
            Outputs.create("Unable to parse to a generic message", this.getClass().getName()).debug().WARNING().print();
            return false;
        }

        WireConversation conversation = WireStorage.getConversationByID(conversationID);
        if(conversation == null)
        {
            Outputs.create("ConversationID not found", this.getClass().getName()).debug().WARNING().print();
            return false;
        }

        senderUser = WireConversations.getNameFromUserID(senderUser);
        return WireMessageSorter.handleReceivedMessage(message, conversation, time, senderUser);
    }
}