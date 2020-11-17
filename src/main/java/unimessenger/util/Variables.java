package unimessenger.util;

public class Variables
{
    public static boolean debug = false;
    public static boolean verbose = false;

    public static Thread cli;
    public static Thread updt;

    public enum SERVICE
    {
        NONE,
        WIRE,
        TELEGRAM
    }

    public enum REQUESTTYPE
    {
        GET,
        POST,
        PUT
    }
}