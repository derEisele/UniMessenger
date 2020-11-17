package unimessenger.userinteraction.menu;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.URL;
import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.Parsers;
import unimessenger.util.Storage;
import unimessenger.util.Variables;

import java.net.http.HttpResponse;

public class MenuLogin
{
    public static void showMenu()
    {
        System.out.println("1) '" + CLI.currentService + "' login");
        System.out.println("2) Show Main Menu");
        System.out.println("3) Exit Program");
        System.out.println("10) AutoLogin");//TODO: Remove

        int userInput = Outputs.getIntAnswerFrom("Please enter the number of the option you would like to choose.");
        switch(userInput)
        {
            case 1:
                tryUserLogin();
                break;
            case 2:
                CLI.currentService = Variables.SERVICE.NONE;
                CLI.currentMenu = CLI.MENU.MAIN;
                break;
            case 3:
                CLI.currentMenu = CLI.MENU.EXIT;
                break;
            case 10:
                autoLogin();
                CLI.currentMenu = CLI.MENU.CONVERSATION_LIST;
                break;
            default:
                Outputs.cannotHandleUserInput();
                break;
        }
    }

    private static void tryUserLogin()
    {
        boolean loginSuccessful = false;
        switch(CLI.currentService)
        {
            case WIRE:
                if(loginWire()) loginSuccessful = true;
                break;
            case TELEGRAM:
                if(loginTelegram()) loginSuccessful = true;
                break;
            case NONE:
            default:
                Outputs.printError("User tried to log in to an unknown service");
                break;
        }

        if(loginSuccessful) CLI.currentMenu = CLI.MENU.CONVERSATION_LIST;
    }

    private static boolean loginWire()
    {
        //TODO: Test if user is logged in already and ask for credentials if not
        return false;
    }
    private static boolean loginTelegram()
    {
        //TODO: Test if user is logged in already and ask for credentials if not
        return false;
    }



    private static boolean autoLogin()//TODO: Remove
    {
        boolean persist = Outputs.getBoolAnswerFrom("Do you want to stay logged in?");

        String url = URL.WIRE + URL.WIRE_LOGIN;
        if(persist) url += URL.WIRE_PERSIST;

        JSONObject obj = new JSONObject();
        obj.put("email", "pechtl97@gmail.com");
        obj.put("password", "Passwort1!");
        String body = obj.toJSONString();

        String[] headers = new String[] {"content-type", "application/json", "accept", "application/json"};

        return handleResponse(CLI.userHTTP.sendRequest(url, Variables.REQUESTTYPE.POST, body, headers));
    }
    public static boolean handleResponse(HttpResponse<String> response)//TODO: Remove
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