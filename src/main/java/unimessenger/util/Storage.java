package unimessenger.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unimessenger.userinteraction.Outputs;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Storage
{
    public static String wireUserID;
    public static String wireBearerToken;
    public static String wireAccessCookie;
    private static Timestamp wireBearerTokenExpiringTime;
    public static final String wireDataFile = "dataWire.json";

    public static void storeWireAccessCookie(String accessCookie)
    {
        if(accessCookie == null) deleteFile(wireDataFile);
        else
        {
            wireAccessCookie = accessCookie;
            JSONObject obj = new JSONObject();
            obj.put("accessCookie", accessCookie);
            obj.put("bearerToken", wireBearerToken);
            obj.put("bearerTime", wireBearerTokenExpiringTime);

            try
            {
                FileWriter fw = new FileWriter(wireDataFile);
                fw.write(obj.toJSONString());
                fw.close();
            } catch(IOException ignored)
            {
            }
        }
    }
    public static void setWireBearerTime(int expiresInSec)
    {
        wireBearerTokenExpiringTime = new Timestamp(System.currentTimeMillis() + (expiresInSec * 900));
    }
    public static boolean isWireBearerTokenStillValid()
    {
        return wireBearerToken != null && wireBearerTokenExpiringTime != null && wireBearerTokenExpiringTime.getTime() > System.currentTimeMillis();
    }
    public static void clearUserData(Variables.SERVICE service)
    {
        switch(service)
        {
            case WIRE:
                wireUserID = null;
                wireBearerToken = null;
                wireAccessCookie = null;
                wireBearerTokenExpiringTime = null;
                deleteFile(wireDataFile);
                break;
            default:
                break;
        }
    }

    public static void writeDataToFile()
    {
        storeWireAccessCookie(wireAccessCookie);
    }
    public static void readDataFromFiles()
    {
        try
        {
            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(wireDataFile));
            wireAccessCookie = (String) obj.get("accessCookie");
            wireBearerToken = (String) obj.get("bearerToken");
            wireBearerTokenExpiringTime = (Timestamp) obj.get("bearerTime");
        } catch(Exception ignored)
        {
        }
    }
    public static void deleteFile(String filename)
    {
        File obj = new File(filename);
        //TODO: Fix deletion of file
        if(obj.delete()) Outputs.printDebug("File '" + filename + "' deleted");
        else Outputs.printError("Failed to delete '" + filename + "'");
    }
}