package unimessenger.abstraction.storage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.MessengerStructure.WireConversation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class WireStorage
{
    public static String userID;
    private static String bearerToken;
    public static String cookie;
    private static Timestamp bearerExpiringTime;
    public static final String storageFile = "dataWire.json";

    public static ArrayList<WireConversation> conversations = new ArrayList<>();

    public static void saveDataInFile(String accessCookie)
    {
        if(accessCookie == null) deleteFile();
        else
        {
            cookie = accessCookie;
            JSONObject obj = new JSONObject();
            obj.put("accessCookie", accessCookie);
            obj.put("bearerToken", bearerToken);
            obj.put("bearerTime", bearerExpiringTime.getTime());

            try
            {
                FileWriter fw = new FileWriter(storageFile);
                fw.write(obj.toJSONString());
                fw.close();
                Outputs.printDebug("Successfully wrote to Wire file");
            } catch(IOException ignored)
            {
                Outputs.printError("Couldn't write to Wire file");
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
        return bearerToken != null && bearerExpiringTime != null && bearerExpiringTime.getTime() > System.currentTimeMillis();
    }

    public static void clearUserData()
    {
        userID = null;
        bearerToken = null;
        cookie = null;
        bearerExpiringTime = null;
        deleteFile();
    }

    public static void readDataFromFiles()
    {
        try
        {
            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(storageFile));
            cookie = (String) obj.get("accessCookie");
            bearerToken = (String) obj.get("bearerToken");
            bearerExpiringTime = new Timestamp((long) obj.get("bearerTime"));
        } catch(Exception ignored)
        {
            Outputs.printError("Failed to load Wire file");
        }
    }

    public static void deleteFile()
    {
        File obj = new File(storageFile);
        //TODO: Fix deletion of file
        if(obj.delete()) Outputs.printDebug("File '" + storageFile + "' deleted");
        else Outputs.printError("Failed to delete '" + storageFile + "'");
    }
}