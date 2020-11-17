package unimessenger.abstraction.wire;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.ILoginOut;
import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.Parsers;
import unimessenger.util.Storage;
import unimessenger.util.Variables;

import java.net.http.HttpResponse;

public class WireLogin implements ILoginOut
{
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

        String[] headers = new String[] {"content-type", "application/json", "accept", "application/json"};

        return handleResponse(CLI.userHTTP.sendRequest(url, Variables.REQUESTTYPE.POST, body, headers));
    }

    @Override
    public boolean logout()
    //Todo dont put this into the link but into the header because best practices see wire docs
    {
        String url = URL.WIRE + URL.WIRE_LOGOUT + "?access_token=" + Storage.wireBearerToken;
        String[] headers = new String[]{
                "cookie", Storage.wireAccessCookie,
                "content-type", "application/json",
                "accept", "application/json"};

        HttpResponse<String> response = CLI.userHTTP.sendRequest(url, Variables.REQUESTTYPE.POST, "", headers);

        if(response == null)
        {
            Outputs.printError("Couldn't get a HTTP response");
            return false;
        } else if(response.statusCode() == 200)
        {
            Outputs.printDebug("Successfully logged out");
            Storage.clearUserData(Variables.SERVICE.WIRE);
            return true;
        } else
        {
            Outputs.printDebug("Response code is not 200");
            return false;
        }
        //TODO make it so the Data is not cleared if the user is not logged out and data is certainly cleared if user is logged out
    }

    public static boolean handleResponse(HttpResponse<String> response)
    {
        if(response == null || response.statusCode() != 200) return false;

        JSONObject obj;
        try
        {
            obj = (JSONObject) new JSONParser().parse(response.body());
            Storage.wireUserID = obj.get("user").toString();
            Storage.wireBearerToken = obj.get("access_token").toString();
            Storage.setWireBearerTime(Integer.parseInt(obj.get("expires_in").toString()));
            Storage.wireAccessCookie = Parsers.parseCookieID(response.headers().map().get("set-cookie").get(0));

            Outputs.printDebug("Token Type: " + obj.get("token_type"));
            Outputs.printDebug("Expires in: " + obj.get("expires_in"));
            Outputs.printDebug("Access Token: " + Storage.wireBearerToken);
            Outputs.printDebug("User: " + Storage.wireUserID);
            Outputs.printDebug("Cookie: " + Storage.wireAccessCookie);
        } catch(ParseException ignored)
        {
            return false;
        }
        return true;
    }
}