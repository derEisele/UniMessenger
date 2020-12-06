package unimessenger.abstraction.interfaces.wire;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.IConversations;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.abstraction.wire.structures.WireConversation;
import unimessenger.abstraction.wire.structures.WirePerson;
import unimessenger.communication.HTTP;
import unimessenger.userinteraction.tui.Outputs;
import unimessenger.util.enums.CONVERSATIONTYPE;
import unimessenger.util.enums.REQUEST;

import java.net.http.HttpResponse;

public class WireConversations implements IConversations
{
    @Override
    public boolean requestAllConversations()
    {
        String url = URL.WIRE + URL.WIRE_CONVERSATIONS + URL.wireBearerToken();
        String[] headers = new String[]{
                Headers.ACCEPT, Headers.JSON};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null)
        {
            Outputs.create("Could not get a HTTP response", this.getClass().getName()).debug().WARNING().print();
            return false;
        } else if(response.statusCode() == 200)
        {
            //TODO: If "has more" key in body is true, ask for more conversations
            //TODO: Sort chats after most recent activity
            try
            {
                JSONObject obj = (JSONObject) new JSONParser().parse(response.body());
                JSONArray conArr = (JSONArray) obj.get("conversations");
                for(Object o : conArr)
                {
                    WireConversation newConversation = getConversation((JSONObject) new JSONParser().parse(o.toString()));
                    if(newConversation.getConversationName() != null)
                    {
                        boolean exists = false;
                        for(WireConversation con : WireStorage.conversations)
                        {
                            if(con.id.equals(newConversation.id))
                            {
                                exists = true;
                                break;
                            }
                        }
                        if(!exists) WireStorage.conversations.add(newConversation);
                    }
                }
                Outputs.create("Successfully reloaded all conversations").verbose().INFO().print();
                return true;
            } catch(ParseException ignored)
            {
            }
            Outputs.create("Failed to reload all conversations", this.getClass().getName()).debug().WARNING().print();
            return false;
        } else
        {
            Outputs.create("Response code is " + response.statusCode(), this.getClass().getName()).debug().WARNING().print();
            return false;
        }
    }

    private static WireConversation getConversation(JSONObject conObj) throws ParseException
    {
        WireConversation con = new WireConversation();

        JSONArray access = (JSONArray) conObj.get("access");
        for(Object a : access)
        {
            con.access.add(a.toString());
        }

        con.creatorID = conObj.get("creator").toString();
        con.accessRole = conObj.get("access_role").toString();
        con.setConversationType(Integer.parseInt(conObj.get("type").toString()));

        JSONObject members = (JSONObject) new JSONParser().parse(conObj.get("members").toString());
        con.members.add(getSelf((JSONObject) new JSONParser().parse(members.get("self").toString())));
        JSONArray memArr = (JSONArray) members.get("others");
        for(Object o : memArr)
        {
            con.members.add(getPerson((JSONObject) new JSONParser().parse(o.toString())));
        }

        if(con.conversationType == CONVERSATIONTYPE.NORMAL)
        {
            if(con.members.size() > 1 && con.members.get(1) != null)
            {
                String partnerID = con.members.get(1).id;
                String conName = getNameFromUserID(partnerID);
                if(conName != null) con.setConversationName(conName);
            }
        } else if(conObj.get("name") != null) con.setConversationName(conObj.get("name").toString());
        if(conObj.get("team") != null) con.team = conObj.get("team").toString();
        con.id = conObj.get("id").toString();
        if(conObj.get("receipt_mode") != null) con.receipt_mode = conObj.get("receipt_mode").toString();
        con.last_event_time = conObj.get("last_event_time").toString();
        if(conObj.get("message_timer") != null) con.message_timer = conObj.get("message_timer").toString();
        con.last_event = conObj.get("last_event").toString();

        return con;
    }
    private static WirePerson getSelf(JSONObject self)
    {
        WirePerson person = getPerson(self);

        if(self.get("hidden_ref") != null) person.hidden_ref = self.get("hidden_ref").toString();
        if(self.get("service") != null) person.service = self.get("service").toString();
        if(self.get("otr_muted_ref") != null) person.otr_muted_ref = self.get("otr_muted_ref").toString();
        person.status_time = self.get("status_time").toString();
        person.hidden = Boolean.getBoolean(self.get("hidden").toString());
        person.status_ref = self.get("status_ref").toString();
        person.otr_archived = Boolean.getBoolean(self.get("otr_archived").toString());
        if(self.get("otr_muted_status") != null) person.otr_muted_status = self.get("otr_muted_status").toString();
        person.otr_muted = Boolean.getBoolean(self.get("otr_muted").toString());
        if(self.get("otr_archived_ref") != null) person.otr_archived_ref = self.get("otr_archived_ref").toString();

        return person;
    }
    private static WirePerson getPerson(JSONObject personObj)
    {
        WirePerson person = new WirePerson();

        person.status = Integer.parseInt(personObj.get("status").toString());
        person.conversation_role = personObj.get("conversation_role").toString();
        person.id = personObj.get("id").toString();

        return person;
    }
    public static String getNameFromUserID(String userID)
    {
        String url = URL.WIRE + URL.WIRE_USERS + URL.wireBearerToken() + "&ids=" + userID;
        String[] headers = new String[]{
                Headers.ACCEPT, Headers.JSON};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null) Outputs.create("Could not get a username", "WireConversations").verbose().WARNING().print();
        else if(response.statusCode() == 200)
        {
            JSONArray arr = null;
            try
            {
                arr = (JSONArray) new JSONParser().parse(response.body());
            } catch(ParseException ignored)
            {
            }
            if(arr.size() > 0)
            {
                JSONObject user = (JSONObject) arr.get(0);
                return user.get("name").toString();
            } else Outputs.create("No user returned", "WireConversations").debug().WARNING().print();
        } else Outputs.create("Response code of getting user was " + response.statusCode()).verbose().WARNING().print();

        return userID;
    }
}