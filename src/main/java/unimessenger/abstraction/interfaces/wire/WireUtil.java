package unimessenger.abstraction.interfaces.wire;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.IUtil;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.apicommunication.HTTP;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.REQUEST;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class WireUtil implements IUtil
{
    @Override
    public boolean refreshSession()
    {
        String url = URL.WIRE + URL.WIRE_ACCESS + URL.WIRE_TOKEN + WireStorage.getBearerToken();
        String[] headers = new String[]{
                "cookie", WireStorage.cookie,
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};

        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.POST, "", headers);

        if(response == null)
        {
            Outputs.printError("Couldn't get a HTTP response");
        } else if(response.statusCode() == 200)
        {
            JSONObject obj;
            try
            {
                assert false;
                obj = (JSONObject) new JSONParser().parse(response.body());
                WireStorage.setBearerToken(obj.get("access_token").toString(), Integer.parseInt(obj.get("expires_in").toString()));
                WireStorage.userID = obj.get("user").toString();
                Outputs.printDebug("Successfully refreshed token");
                return true;
            } catch(ParseException ignored)
            {
                Outputs.printError("Failed refreshing token");
            }
        } else
        {
            Outputs.printDebug("Response code is " + response.statusCode() + ". Deleting Wire access cookie...");
            WireStorage.cookie = null;
            WireStorage.clearFile();
        }
        return false;
    }

    @Override
    public boolean loadProfile()
    {
        String url = URL.WIRE + URL.WIRE_SELF + URL.WIRE_TOKEN + WireStorage.getBearerToken();
        String[] headers = new String[]{
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};

        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null) Outputs.printError("No response received");
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
                Outputs.printError("Json parsing error");
            }
        } else Outputs.printError("Http response was " + response.statusCode());

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

        String id = registerClient(WireStorage.persistent);
        if(id == null)
        {
            WireStorage.persistent = false;
            return registerClient(false);
        }
        else return id;
    }
    private static ArrayList<String> getAllClientIDs() throws ParseException
    {
        String url = URL.WIRE + URL.WIRE_CLIENTS + URL.WIRE_TOKEN + WireStorage.getBearerToken();
        String[] headers = new String[]{
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};

        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null)
        {
            Outputs.printError("No response received");
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
        } else Outputs.printError("Response code is " + response.statusCode());

        return null;
    }
    private static boolean compareCookie(String clientID) throws ParseException
    {
        String url = URL.WIRE + URL.WIRE_CLIENTS + "/" + clientID + URL.WIRE_TOKEN + WireStorage.getBearerToken();
        String[] headers = new String[]{
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};

        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.GET, "", headers);

        if(response == null) Outputs.printError("No response received");
        else if(response.statusCode() == 200)
        {
            JSONObject obj = (JSONObject) new JSONParser().parse(response.body());
            if(obj.containsKey("cookie"))
            {
                if(obj.get("cookie").toString().equals(WireStorage.cookie))
                {
                    Outputs.printDebug("Client ID found");
                    return true;
                }
            } else Outputs.printDebug("Client response didn't contain a cookie");
        } else Outputs.printError("Response code is " + response.statusCode());

        return false;
    }
    private static String registerClient(boolean persistent) throws ParseException
    {
        String url = URL.WIRE + URL.WIRE_CLIENTS + URL.WIRE_TOKEN + WireStorage.getBearerToken();
        String[] headers = new String[]{
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};

        JSONObject obj = new JSONObject();
        obj.put("cookie", WireStorage.cookie);

        //TODO: Use working prekeys
        JSONObject lastkey = new JSONObject();
        lastkey.put("key", "pQABARn//wKhAFgg7+EhYE0H+m7FsRt6FCvrTSmrplzvlNhesJhenAscbUADoQChAFggItugmAU3gvKV4+pjlQmJV6DnzbWpY/F0UTYmJqji+C0E9g==");
        lastkey.put("id", 65535);
        obj.put("lastkey", lastkey);

        JSONObject sigkeys = new JSONObject();
        sigkeys.put("enckey", "");
        sigkeys.put("mackey", "");
        obj.put("sigkeys", sigkeys);

        String pw = Outputs.getStringAnswerFrom("Please enter your password to register this client");
        obj.put("password", pw);

        if(persistent) obj.put("type", "permanent");
        else obj.put("type", "temporary");

        //TODO: Use working prekeys
        JSONArray prekeys = new JSONArray();
        JSONObject key1 = new JSONObject();
        key1.put("key", "pQABARgYAqEAWCADQw20K/g80LRnGbesNG0x2tqtX0GgE7SvxDg7aWDz4AOhAKEAWCBsGf44CRwNNIm0Z0KLpP7fRCF/WMvsGAkSdNqGquNc9wT2");
        key1.put("id", 24);
        prekeys.add(key1);
        obj.put("prekeys", prekeys);

        obj.put("class", "desktop");

        obj.put("label", "Custom Wire Client");

        String body = obj.toJSONString();

        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.POST, body, headers);

        if(response == null) Outputs.printError("No response received");
        else if(response.statusCode() == 201)
        {
            JSONObject resObj = (JSONObject) new JSONParser().parse(response.body());
            WireStorage.clientID = resObj.get("id").toString();
            Outputs.printDebug("Client ID stored");
            return WireStorage.clientID;
        } else Outputs.printError("Response code is " + response.statusCode());
        return null;
    }
}
