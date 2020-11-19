package unimessenger.apicommunication;

import unimessenger.util.enums.REQUEST;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTP
{
    static HttpClient client = HttpClient.newHttpClient();

    public HttpResponse<String> sendRequest(String url, REQUEST type, String body, String... headers)
    {
        HttpRequest request = null;
        HttpResponse<String> response = null;

        switch(type)
        {
            case GET:
                request = RequestBuilder.getGETRequest(url, headers);
                break;
            case PUT:
                request = RequestBuilder.getPUTRequest(url, body, headers);
                break;
            case POST:
                request = RequestBuilder.getPOSTRequest(url, body, headers);
                break;
            default:
                break;
        }

        if(request != null)
        {
            try
            {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch(Exception ignored)
            {
            }
        }

        return response;
    }
}