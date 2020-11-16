package unimessenger.util;

public class Parsers
{
    public static String parseCookieID(String raw)
    {
        String[] arr = raw.split("zuid=");
        if(arr.length > 1) arr = arr[1].split(";");
        else return null;

        return "zuid=" + arr[0];
    }
}
