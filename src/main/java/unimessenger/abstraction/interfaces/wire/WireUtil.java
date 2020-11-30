package unimessenger.abstraction.interfaces.wire;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.encryption.WireCrypto.Prekey;
import unimessenger.abstraction.encryption.WireCrypto.WireCryptoHandler;
import unimessenger.abstraction.interfaces.IUtil;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.communication.HTTP;
import unimessenger.userinteraction.Inputs;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.REQUEST;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;

public class WireUtil implements IUtil
{
    @Override
    public boolean refreshSession()
    {
        String url = URL.WIRE + URL.WIRE_ACCESS + URL.wireBearerToken();
        String[] headers = new String[]{
                "cookie", WireStorage.cookie,
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.POST, "", headers);

        if(response == null) Outputs.create("Could not get a HTTP response", this.getClass().getName()).debug().WARNING().print();
        else if(response.statusCode() == 200)
        {
            JSONObject obj;
            try
            {
                assert false;
                obj = (JSONObject) new JSONParser().parse(response.body());
                WireStorage.setBearerToken(obj.get("access_token").toString(), Integer.parseInt(obj.get("expires_in").toString()));
                WireStorage.userID = obj.get("user").toString();
                Outputs.create("Successfully refreshed token").verbose().INFO().print();
                return true;
            } catch(ParseException ignored)
            {
                Outputs.create("Refreshing token failed", this.getClass().getName()).debug().WARNING().print();
            }
        } else
        {
            Outputs.create("Response code is " + response.statusCode() + ". Deleting Wire access cookie...", this.getClass().getName()).debug().ERROR().print();
            WireStorage.cookie = null;
            WireStorage.clearFile();
        }
        return false;
    }

    @Override
    public boolean loadProfile()
    {
        String url = URL.WIRE + URL.WIRE_SELF + URL.wireBearerToken();
        String[] headers = new String[]{
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null) Outputs.create("No response received", this.getClass().getName()).debug().WARNING().print();
        else if(response.statusCode() == 200)
        {
            try
            {
                JSONObject obj = (JSONObject) new JSONParser().parse(response.body());

                if(obj.containsKey("email")) WireStorage.selfProfile.email = obj.get("email").toString();
                if(obj.containsKey("phone")) WireStorage.selfProfile.phone = obj.get("phone").toString();
                if(obj.containsKey("handle")) WireStorage.selfProfile.handle = obj.get("handle").toString();
                WireStorage.selfProfile.locale = obj.get("locale").toString();
                if(obj.containsKey("managed_by")) WireStorage.selfProfile.managed_by = obj.get("managed_by").toString();
                if(obj.containsKey("accent_id")) WireStorage.selfProfile.accent_id = Integer.parseInt(obj.get("accent_id").toString());
                WireStorage.selfProfile.userName = obj.get("name").toString();
                WireStorage.selfProfile.id = obj.get("id").toString();
                if(obj.containsKey("deleted")) WireStorage.selfProfile.deleted = Boolean.getBoolean(obj.get("deleted").toString());
                WireStorage.selfProfile.userAssets = getUserAssets((JSONArray) obj.get("assets"));

                WireStorage.clientID = getClientID();
                WireStorage.saveDataInFile();
                return true;
            } catch(ParseException ignored)
            {
                Outputs.create("Json parsing error. Data not saved in file", this.getClass().getName()).debug().WARNING().print();
            }
        } else Outputs.create("Response code was " + response.statusCode(), this.getClass().getName()).debug().WARNING().print();

        return false;
    }

    private static ArrayList<String> getUserAssets(JSONArray assets)
    {
        ArrayList<String> ret = new ArrayList<>();
        while(!assets.isEmpty())
        {
            JSONObject asset = (JSONObject) assets.get(0);
            ret.add(asset.get("size").toString());
            ret.add(asset.get("type").toString());
            ret.add(asset.get("key").toString());
            assets.remove(0);
        }
        return ret;
    }
    private static String getClientID() throws ParseException
    {
        ArrayList<String> clientIDs = getAllClientIDs();
        if(clientIDs != null)
        {
            for(String id : clientIDs)
            {
                if(compareCookie(id)) return id;
            }
        }

        String pw = Inputs.getStringAnswerFrom("Please enter your password to register this client");
        String id = registerClient(WireStorage.persistent, pw);
        if(id == null)
        {
            WireStorage.persistent = false;
            return registerClient(false, pw);
        } else return id;
    }
    private static ArrayList<String> getAllClientIDs() throws ParseException
    {
        String url = URL.WIRE + URL.WIRE_CLIENTS + URL.wireBearerToken();
        String[] headers = new String[]{
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null)
        {
            Outputs.create("No response received", "WireUtil").debug().WARNING().print();
            return null;
        } else if(response.statusCode() == 200)
        {
            JSONArray clients = (JSONArray) new JSONParser().parse(response.body());
            ArrayList<String> ids = new ArrayList<>();
            while(!clients.isEmpty())
            {
                JSONObject client = (JSONObject) new JSONParser().parse(clients.get(0).toString());
                ids.add(client.get("id").toString());
                clients.remove(0);
            }
            return ids;
        } else Outputs.create("Response code is " + response.statusCode(), "WireUtil").debug().WARNING().print();

        return null;
    }
    private static boolean compareCookie(String clientID) throws ParseException
    {
        String url = URL.WIRE + URL.WIRE_CLIENTS + "/" + clientID + URL.wireBearerToken();
        String[] headers = new String[]{
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null) Outputs.create("No response received", "WireUtil").debug().WARNING().print();
        else if(response.statusCode() == 200)
        {
            JSONObject obj = (JSONObject) new JSONParser().parse(response.body());
            if(obj.containsKey("cookie"))
            {
                if(obj.get("cookie").toString().equals(WireStorage.cookie))
                {
                    Outputs.create("Client ID found").verbose().INFO().print();
                    return true;
                }
            } else Outputs.create("Client response contained no cookie", "WireUtil").debug().INFO().print();
        } else Outputs.create("Response code is " + response.statusCode(), "WireUtil").debug().WARNING().print();

        return false;
    }
    private static String registerClient(boolean persistent, String pw) throws ParseException
    {
        String url = URL.WIRE + URL.WIRE_CLIENTS + URL.wireBearerToken();
        String[] headers = new String[]{
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};

        JSONObject obj = new JSONObject();
        obj.put("cookie", WireStorage.cookie);

        Prekey lastKey = WireCryptoHandler.generateLastPrekey();

        JSONObject lastkey = new JSONObject();
        assert lastKey != null;
        lastkey.put("key", lastKey.getKey());
        lastkey.put("id", lastKey.getID());
        obj.put("lastkey", lastkey);

        //TODO: Find out if enckey and mackey are correct
        JSONObject sigkeys = new JSONObject();
        sigkeys.put("enckey", Base64.getEncoder().encodeToString(new byte[32]));
        sigkeys.put("mackey", Base64.getEncoder().encodeToString(new byte[32]));
        obj.put("sigkeys", sigkeys);

        obj.put("password", pw);

        if(persistent) obj.put("type", "permanent");
        else obj.put("type", "temporary");

        obj.put("prekeys", getPreKeys());

        obj.put("class", "desktop");

        obj.put("label", "Custom Wire Client");

        String body = obj.toJSONString();
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.POST, body, headers);

        if(response == null) Outputs.create("No response received", "WireUtil").debug().WARNING().print();
        else if(response.statusCode() == 201)
        {
            JSONObject resObj = (JSONObject) new JSONParser().parse(response.body());
            WireStorage.clientID = resObj.get("id").toString();
            Outputs.create("Client ID stored").verbose().INFO().print();
            return WireStorage.clientID;
        } else Outputs.create("Response code is " + response.statusCode(), "WireUtil").debug().WARNING().print();
        return null;
    }
    private static JSONArray getPreKeys()
    {
        Prekey[] keys = WireCryptoHandler.generatePreKeys(0, 50);

        JSONArray keyList = new JSONArray();
        for(Prekey key : keys)
        {
            JSONObject newKey = new JSONObject();
            newKey.put("key", key.getKey());
            newKey.put("id", key.getID());
            keyList.add(newKey);
        }

        return keyList;
    }
}
