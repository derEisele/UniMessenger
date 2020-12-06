package unimessenger.abstraction.wire.crypto;

import com.waz.model.Messages;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import unimessenger.abstraction.Headers;
import unimessenger.abstraction.URL;
import unimessenger.abstraction.wire.messages.*;
import unimessenger.communication.HTTP;
import unimessenger.userinteraction.Outputs;
import unimessenger.util.enums.REQUEST;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

public class MessageCreator
{
    public static Messages.GenericMessage createGenericTextMessage(String text)
    {
        MessageText msg = new MessageText(text);
        return msg.createGenericMsg();
    }

    public static Messages.GenericMessage createGenericPingMessage()
    {
        Ping msg = new Ping();
        return msg.createGenericMsg();
    }

    public static Messages.GenericMessage createGenericFilePreviewMessage(File file, UUID id)
    {
        String mimeType = null;
        try
        {
            mimeType = Files.probeContentType(file.toPath());
        } catch(IOException ignored)
        {
        }
        if(mimeType == null) mimeType = "content/unknown";
        FileAssetPreview preview = new FileAssetPreview(file, mimeType, id);
        return preview.createGenericMsg();
    }
    public static Messages.GenericMessage createGenericFileMessage(File file, UUID id)
    {
        try
        {
            String mimeType = Files.probeContentType(file.toPath());
            if(mimeType == null) mimeType = "content/unknown";
            FileAsset asset = new FileAsset(file, mimeType, id);

            AssetKey ak = uploadAsset(asset);
            if(ak == null) return null;

            asset.setAssetKey(ak.key);
            asset.setAssetToken(ak.token);

            return asset.createGenericMsg();
        } catch(Exception ignored)
        {
            Outputs.create("Could not create FileAsset", "MessageCreator").verbose().WARNING().print();
        }
        return null;
    }

    private static AssetKey uploadAsset(IAsset asset) throws Exception
    {
        String boundary = "frontier" + UUID.randomUUID().toString();
        /*
         Code from lithium
         */
        StringBuilder sb = new StringBuilder();
        String strMetadata = String.format("{\"public\": %s, \"retention\": \"%s\"}", asset.isPublic(), asset.getRetention());
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Type: application/json; charset=utf-8\r\n");
        sb.append("Content-Length: ").append(strMetadata.length()).append("\r\n\r\n");
        sb.append(strMetadata).append("\r\n");
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Type: ").append(asset.getMimeType()).append("\r\n");
        sb.append("Content-Length: ").append(asset.getEncryptedData().length).append("\r\n");
        sb.append("Content-MD5: ").append(Util.calcMd5(asset.getEncryptedData())).append("\r\n\r\n");
        System.out.println("String: "+ sb.toString());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        os.write(asset.getEncryptedData());
        os.write(("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

        String url = URL.WIRE + URL.WIRE_ASSETS + URL.wireBearerToken();
        String[] headers = new String[]{
                Headers.CONTENT, Headers.MIXED + "; boundary=" + boundary,
                Headers.ACCEPT, Headers.JSON};
        String body = os.toString();
        HttpResponse<String> response = new HTTP().sendRequest(url, REQUEST.POST, body, headers);

        System.out.println("RESPONSE CODE: " + response.statusCode());
        System.out.println("Response content: " + response.toString());
        System.out.println("Response Head: " + response.headers());
        System.out.println("Response Body: " + response.body());

        if(response == null) Outputs.create("No HTTP response received", "MessagesCreator").debug().INFO().print();
        else if(response.statusCode() == 200 || response.statusCode() == 201)
        {
            Outputs.create("Successfully uploaded asset").verbose().INFO().print();
            return keyFromResponse(response);
        } else Outputs.create("Response code is " + response.statusCode()).verbose().WARNING().print();

        return null;
    }
    private static AssetKey keyFromResponse(HttpResponse<String> response) throws ParseException
    {
        JSONObject obj = (JSONObject) new JSONParser().parse(response.body());

        if(obj.containsKey("key") && obj.containsKey("token")) return new AssetKey(obj.get("key").toString(), obj.get("token").toString());
        return null;
    }
}