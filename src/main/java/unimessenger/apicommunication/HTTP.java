package unimessenger.apicommunication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTP
{
    private static final String API_URL_EXAMPLE = "https://jsonplaceholder.typicode.com/todos/1";

    private static final String URL_WIRE = "https://staging-nginz-https.zinfra.io";
    private static final String URL_WIRE_LOGIN = "https://staging-nginz-https.zinfra.io/login?persist=true";

    static HttpClient client = HttpClient.newHttpClient();

    public static void sendRequest(String url, REQUESTTYPE type, String body, String... headers)
    {
        HttpRequest request = null;
        HttpResponse<String> response = null;

        switch(type)
        {
            case GET:
                request = HttpRequest.newBuilder().GET().header("accept", "application/json").uri(URI.create(url)).build();
                break;
            case PUT:
                //TODO: Add PUT request
                break;
            case POST:
                //TODO: Add POST request
                break;
            default:
                break;
        }

        try
        {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch(Exception ignored)
        {
        }
        assert response != null;
        System.out.println(response.body());
    }

    public enum REQUESTTYPE
    {
        GET,
        POST,
        PUT
    }
}