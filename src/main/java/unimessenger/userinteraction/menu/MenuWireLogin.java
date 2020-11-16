package unimessenger.userinteraction.menu;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.Commands;
import unimessenger.util.Parsers;
import unimessenger.util.Storage;
import unimessenger.util.Variables;

import java.net.http.HttpResponse;

public class MenuWireLogin
{
    public static void showMenu()
    {
        System.out.println("1) Enter Login Information");
        System.out.println("2) Show Main Menu");
        System.out.println("3) Exit Program");
        System.out.println("4) AutoLogin");

        int userInput = Outputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                if(handleUserLogin()) CLI.currentMenu = CLI.MENU.WireOverview;
                else System.out.println("Failed to log in");
                break;
            case 2:
                CLI.currentMenu = CLI.MENU.MainMenu;
                break;
            case 3:
                CLI.currentMenu = CLI.MENU.EXIT;
                break;
            case 4:
                autoLogin();
                CLI.currentMenu = CLI.MENU.WireOverview;
                break;
            default:
                Outputs.cannotHandleUserInput();
                break;
        }
    }

    private static boolean handleUserLogin()
    {
        //TODO: Add more login options (phone)
        String mail = Outputs.getStringAnswerFrom("Please enter your E-Mail");//TestAccount: pechtl97@gmail.com
        String pw = Outputs.getStringAnswerFrom("Please enter your password");//TestAccount: Passwort1!
        boolean persist = Outputs.getBoolAnswerFrom("Do you want to stay logged in?");

        String url = Variables.URL_WIRE + Commands.LOGIN;
        if(persist) url += Commands.PERSIST;

        JSONObject obj = new JSONObject();
        obj.put("email", mail);
        obj.put("password", pw);
        String body = obj.toJSONString();

        String[] headers = new String[] {"content-type", "application/json", "accept", "application/json"};

        return handleResponse(CLI.userHTTP.sendRequest(url, Variables.REQUESTTYPE.POST, body, headers));
    }
    private static boolean autoLogin()
    {
        boolean persist = Outputs.getBoolAnswerFrom("Do you want to stay logged in?");

        String url = Variables.URL_WIRE + Commands.LOGIN;
        if(persist) url += Commands.PERSIST;

        JSONObject obj = new JSONObject();
        obj.put("email", "pechtl97@gmail.com");
        obj.put("password", "Passwort1!");
        String body = obj.toJSONString();

        String[] headers = new String[] {"content-type", "application/json", "accept", "application/json"};

        return handleResponse(CLI.userHTTP.sendRequest(url, Variables.REQUESTTYPE.POST, body, headers));
    }
    private static boolean handleResponse(HttpResponse<String> response)
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