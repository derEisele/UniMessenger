package unimessenger.communication;

import java.net.http.HttpRequest;
import java.util.ArrayList;

public class RequestHandler implements Runnable
{
    private static RequestHandler instance;
    private ArrayList<HttpRequest> requestQueue;

    private RequestHandler()
    {
        requestQueue = new ArrayList<>();
    }
    public static RequestHandler getInstance()
    {
        if(instance == null) instance = new RequestHandler();
        return instance;
    }
    @Override
    public void run()
    {
        while(!Thread.interrupted())
        {
        }
    }
}
