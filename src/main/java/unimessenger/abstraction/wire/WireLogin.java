package unimessenger.abstraction.wire;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.ILoginOut;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.apicommunication.HTTP;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.REQUEST;
import unimessenger.util.enums.SERVICE;

import java.net.http.HttpResponse;

public class WireLogin implements ILoginOut
{
    @Override
    public boolean checkIfLoggedIn()
    {
        //TODO: Verify if user is already logged in
        return false;
    }

    @Override
    public boolean login()
    {
        //TODO: Add more login options (phone)
        String mail = Outputs.getStringAnswerFrom("Please enter your E-Mail");//TestAccount: pechtl97@gmail.com
        String pw = Outputs.getStringAnswerFrom("Please enter your password");//TestAccount: Passwort1!
        boolean persist = Outputs.getBoolAnswerFrom("Do you want to stay logged in?");

        String url = URL.WIRE + URL.WIRE_LOGIN;
        if(persist) url += URL.WIRE_PERSIST;

        JSONObject obj = new JSONObject();
        obj.put("email", mail);
        obj.put("password", pw);
        String body = obj.toJSONString();

        String[] headers = new String[] {
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};

        return handleResponse(new HTTP().sendRequest(url, REQUEST.POST, body, headers));
    }

    @Override
    public boolean logout()
    //Todo dont put this into the link but into the header because best practices see wire docs
    {
        String url = URL.WIRE + URL.WIRE_LOGOUT + "?access_token=" + WireStorage.wireBearerToken;
        String[] headers = new String[]{
                "cookie", WireStorage.wireAccessCookie,
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};

        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.POST, "", headers);

        if(response == null)
        {
            Outputs.printError("Couldn't get a HTTP response");
            return false;
        } else if(response.statusCode() == 200)
        {
            Outputs.printDebug("Successfully logged out");
            WireStorage.clearUserData(SERVICE.WIRE);
            return true;
        } else
        {
            Outputs.printDebug("Response code is not 200");
            return false;
        }
        //TODO make it so the Data is not cleared if the user is not logged out and data is certainly cleared if user is logged out
    }

    @Override
    public boolean needsRefresh()
    {
        //TODO: Check if bearer token needs to be refreshed
        return false;
    }

    @Override
    public boolean refresh()
    {
        //TODO: Refresh bearer token
        return false;
    }

    private boolean handleResponse(HttpResponse<String> response)
    {
        if(response == null || response.statusCode() != 200) return false;

        JSONObject obj;
        try
        {
            obj = (JSONObject) new JSONParser().parse(response.body());
            WireStorage.wireUserID = obj.get("user").toString();
            WireStorage.wireBearerToken = obj.get("access_token").toString();
            WireStorage.setWireBearerTime(Integer.parseInt(obj.get("expires_in").toString()));

            String cookieArr = response.headers().map().get("set-cookie").get(0);
            String[] arr = cookieArr.split("zuid=");
            if(arr.length > 1) arr = arr[1].split(";");
            WireStorage.wireAccessCookie = "zuid=" + arr[0];

            Outputs.printDebug("Token Type: " + obj.get("token_type"));
            Outputs.printDebug("Expires in: " + obj.get("expires_in"));
            Outputs.printDebug("Access Token: " + WireStorage.wireBearerToken);
            Outputs.printDebug("User: " + WireStorage.wireUserID);
            Outputs.printDebug("Cookie: " + WireStorage.wireAccessCookie);
        } catch(ParseException ignored)
        {
            return false;
        }
        return true;
    }
}