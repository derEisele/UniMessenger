package unimessenger.util;

public class Variables
{
    public static boolean debug = false;
    public static boolean verbose = false;

    public static final String URL_WIRE = "https://prod-nginz-https.wire.com";

    public enum SERVICE
    {
        WIRE
    }

    public enum REQUESTTYPE
    {
        GET,
        POST,
        PUT
    }
}