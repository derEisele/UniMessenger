package unimessenger.userinteraction.menu;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.interfaces.ILoginOut;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.apicommunication.HTTP;
import unimessenger.userinteraction.CLI;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.Updater;
import unimessenger.util.enums.REQUEST;
import unimessenger.util.enums.SERVICE;

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
                if(connectUser())
                {
                    Updater.addService(CLI.currentService);
                    CLI.currentMenu = CLI.MENU.CONVERSATION_LIST;
                }
                break;
            case 2:
                CLI.currentService = SERVICE.NONE;
                CLI.currentMenu = CLI.MENU.MAIN;
                break;
            case 3:
                CLI.currentMenu = CLI.MENU.EXIT;
                break;
            case 10:
                autoLogin();
                Updater.addService(CLI.currentService);
                CLI.currentMenu = CLI.MENU.CONVERSATION_LIST;
                break;
            default:
                Outputs.cannotHandleUserInput();
                break;
        }
    }

    private static boolean connectUser()
    {
        APIAccess access = new APIAccess();
        ILoginOut login = access.getLoginInterface(CLI.currentService);

        if(login.checkIfLoggedIn() && access.getUtilInterface(CLI.currentService).refreshSession()) return true;
        if(login.login()) return true;

        System.out.println("Failed to log in");
        return false;
    }



    @Deprecated
    private static void autoLogin()
    {
        String url = URL.WIRE + URL.WIRE_LOGIN + URL.WIRE_PERSIST;

        JSONObject obj = new JSONObject();
        obj.put("email", "pechtl97@gmail.com");
        obj.put("password", "Passwort1!");
        String body = obj.toJSONString();

        String[] headers = new String[] {
                Headers.CONTENT_JSON[0], Headers.CONTENT_JSON[1],
                Headers.ACCEPT_JSON[0], Headers.ACCEPT_JSON[1]};

        handleResponse(new HTTP().sendRequest(url, REQUEST.POST, body, headers));
    }
    @Deprecated
    public static void handleResponse(HttpResponse<String> response)
    {
        if(response == null || response.statusCode() != 200) return;

        JSONObject obj;
        try
        {
            obj = (JSONObject) new JSONParser().parse(response.body());
            WireStorage.userID = obj.get("user").toString();
            WireStorage.setBearerToken(obj.get("access_token").toString(), Integer.parseInt(obj.get("expires_in").toString()));

            String raw = response.headers().map().get("set-cookie").get(0);
            String[] arr = raw.split("zuid=");
            if(arr.length > 1) arr = arr[1].split(";");
            WireStorage.cookie = "zuid=" + arr[0];

            Outputs.printDebug("Token Type: " + obj.get("token_type"));
            Outputs.printDebug("Expires in: " + obj.get("expires_in"));
            Outputs.printDebug("Access Token: " + WireStorage.getBearerToken());
            Outputs.printDebug("User: " + WireStorage.userID);
            Outputs.printDebug("Cookie: " + WireStorage.cookie);
        } catch(ParseException ignored)
        {
        }
    }
}