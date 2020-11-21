package unimessenger.abstraction.wire;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.IConversations;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.apicommunication.HTTP;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.MessengerStructure.WireConversation;
import unimessenger.util.MessengerStructure.WirePerson;
import unimessenger.util.enums.REQUEST;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class WireConversations implements IConversations
{
    @Override
    public boolean requestAllConversations()
    {
        String url = URL.WIRE + URL.WIRE_CONVERSATIONS + URL.WIRE_TOKEN + WireStorage.getBearerToken();
        String[] headers = new String[]{
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null)
        {
            Outputs.printError("Couldn't get a HTTP response");
            return false;
        } else if(response.statusCode() == 200)
        {
            //TODO: If "has more" key in body is true, ask for more conversations
            //TODO: Sort chats after most recent activity

            ArrayList<WireConversation> newConList = new ArrayList<>();

            try
            {
                JSONObject obj = (JSONObject) new JSONParser().parse(response.body());
                JSONArray conArr = (JSONArray) obj.get("conversations");
                for(Object o : conArr)
                {
                    newConList.add(getConversation((JSONObject) new JSONParser().parse(o.toString())));
                }

                WireStorage.conversations = newConList;
                Outputs.printDebug("Successfully reloaded all conversations");
                return true;
            } catch(ParseException ignored)
            {
            }
            Outputs.printDebug("Failed to reload all conversations");
            return false;
        } else
        {
            Outputs.printDebug("Response code is not 200");
            return false;
        }
    }

    private static WireConversation getConversation(JSONObject conObj) throws ParseException
    {
        WireConversation con = new WireConversation();

        //TODO: Store access

        con.creatorID = conObj.get("creator").toString();
        con.accessRole = conObj.get("access_role").toString();

        JSONObject members = (JSONObject) new JSONParser().parse(conObj.get("members").toString());
        con.members.add(getSelf((JSONObject) new JSONParser().parse(members.get("self").toString())));
        JSONArray memArr = (JSONArray) members.get("others");
        for(Object o : memArr)
        {
            con.members.add(getPerson((JSONObject) new JSONParser().parse(o.toString())));
        }

        if(conObj.get("name") != null) con.conversationName = conObj.get("name").toString();
        if(conObj.get("team") != null) con.team = conObj.get("team").toString();
        con.id = conObj.get("id").toString();
        con.conversationType = Integer.parseInt(conObj.get("type").toString());
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
}