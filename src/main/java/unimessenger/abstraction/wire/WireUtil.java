package unimessenger.abstraction.wire;

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

public class WireUtil implements IUtil
{
    @Override
    public boolean refreshSession()
    {
        String url = URL.WIRE + URL.WIRE_ACCESS + "?access_token=" + WireStorage.wireBearerToken;
        String[] headers = new String[]{
                "cookie", WireStorage.wireAccessCookie,
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
                WireStorage.wireBearerToken = obj.get("access_token").toString();
                WireStorage.setWireBearerTime(Integer.parseInt(obj.get("expires_in").toString()));
                Outputs.printDebug("Successfully refreshed token");
                return true;
            } catch(ParseException ignored)
            {
                Outputs.printError("Failed refreshing token");
            }
        } else
        {
            Outputs.printDebug("Response code is " + response.statusCode() + ". Deleting Wire access cookie...");
            WireStorage.wireAccessCookie = null;
            WireStorage.deleteFile(WireStorage.wireDataFile);
        }
        return false;
    }

}
