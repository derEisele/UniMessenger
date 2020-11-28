package unimessenger.abstraction.storage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unimessenger.abstraction.storage.MessengerStructure.WireConversation;
import unimessenger.abstraction.storage.MessengerStructure.WireProfile;
import unimessenger.userinteraction.Outputs;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class WireStorage
{
    public static String userID;
    public static String clientID = "7ec6cfc08fc9db51";
    public static boolean persistent = false;
    private static String bearerToken;
    public static String cookie;
    private static Timestamp bearerExpiringTime;
    public static Timestamp lastNotification = null;
    public static final String storageFile = "DataStorage/access.json";

    public static WireProfile selfProfile = new WireProfile();
    public static ArrayList<WireConversation> conversations = new ArrayList<>();

    public static void saveDataInFile(String accessCookie)
    {
        if(accessCookie == null) clearFile();
        else
        {
            cookie = accessCookie;
            JSONObject obj = new JSONObject();
            obj.put("accessCookie", accessCookie);
            obj.put("bearerToken", bearerToken);
            obj.put("bearerTime", bearerExpiringTime.getTime());
            obj.put("clientID", clientID);
            if(lastNotification != null) obj.put("lastNotification", lastNotification.getTime());

            try
            {
                FileWriter fw = new FileWriter(storageFile);
                fw.write(obj.toJSONString());
                fw.close();
                Outputs.create("Successfully wrote to Wire file").verbose().INFO().print();
            } catch(IOException ignored)
            {
                Outputs.create("Could not write to Wire file", "WireStorage").debug().WARNING().print();
            }
        }
    }

    public static void saveDataInFile()
    {
        saveDataInFile(cookie);
    }

    public static void setBearerToken(String token, int ttl)
    {
        bearerToken = token;
        bearerExpiringTime = new Timestamp(System.currentTimeMillis() + (ttl * 900));
    }

    public static String getBearerToken()
    {
        return bearerToken;
    }

    public static boolean isBearerTokenStillValid()
    {
        if(bearerToken == null) Outputs.create("Bearer token is null").verbose().INFO().print();
        else if(bearerExpiringTime == null) Outputs.create("Bearer token has no expiring time").verbose().INFO().print();
        else if(bearerExpiringTime.getTime() <= System.currentTimeMillis()) Outputs.create("Bearer token expired").verbose().INFO().print();
        else return true;
        return false;
    }

    public static void clearUserData()
    {
        userID = null;
        bearerToken = null;
        cookie = null;
        bearerExpiringTime = null;
        lastNotification = null;
        clearFile();
    }

    public static void readDataFromFiles()
    {
        try
        {
            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(storageFile));
            cookie = obj.get("accessCookie").toString();
            bearerToken = obj.get("bearerToken").toString();
            bearerExpiringTime = new Timestamp((long) obj.get("bearerTime"));
            clientID = obj.get("clientID").toString();
            lastNotification = new Timestamp((long) obj.get("lastNotification"));
        } catch(Exception ignored)
        {
            Outputs.create("Failed to load Wire file", "WireStorage").debug().WARNING().print();
        }
    }

    public static void clearFile()
    {
        try
        {
            FileWriter fw = new FileWriter(storageFile);
            fw.write("{}");
            fw.close();
            Outputs.create("Successfully cleared Wire file").verbose().INFO().print();
        } catch(IOException ignored)
        {
            Outputs.create("Could not clear Wire file", "Wire Storage").debug().WARNING().print();
        }
    }
}