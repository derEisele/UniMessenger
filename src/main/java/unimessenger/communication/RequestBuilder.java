package unimessenger.communication;

import java.net.URI;
import java.net.http.HttpRequest;

public class RequestBuilder
{
    public static HttpRequest getGETRequest(String url, String... headers)
    {
        if(url == null) return null;

        return HttpRequest.newBuilder().GET().uri(URI.create(url)).headers(headers).build();
    }

    public static HttpRequest getPUTRequest(String url, String body, String... headers)
    {
        if(url == null) return null;
        if(body == null) return null;

        return HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(body)).uri(URI.create(url)).headers(headers).build();
    }

    public static HttpRequest getPOSTRequest(String url, String body, String... headers)
    {
        if(url == null) return null;
        if(body == null) return null;

        return HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body)).uri(URI.create(url)).headers(headers).build();
    }
}