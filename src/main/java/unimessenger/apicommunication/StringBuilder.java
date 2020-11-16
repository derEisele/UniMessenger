package unimessenger.apicommunication;

import java.util.ArrayList;

public class StringBuilder
{
    private ArrayList<String> keys;
    private ArrayList<String> values;

    public StringBuilder()
    {
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void addStringEntry(String key, String value)
    {
        keys.add("\"" + key + "\"");
        values.add("\"" + value + "\"");
    }
    public void addEntry(String key, String value)
    {
        keys.add(key);
        values.add(value);
    }
    public void addIntEntry(String key, int value)
    {
        keys.add("\"" + key + "\"");
        values.add("" + value);
    }

    public String getJSONString()
    {
        String ret = "{";

        while(!keys.isEmpty())
        {
            ret += keys.get(0);
            ret += ":";
            ret += values.get(0);

            keys.remove(0);
            values.remove(0);

            if(!keys.isEmpty()) ret += ",";
        }

        ret += "}";

        return ret;
    }
    public String[] getCombinationsAsArray()
    {
        String[] ret = new String[keys.size() + values.size()];

        for(int i = 0; i < keys.size(); i++)
        {
            ret[i] = keys.get(i / 2);
            i++;
            ret[i] = values.get(i / 2);
        }

        keys.clear();
        values.clear();

        return ret;
    }
}