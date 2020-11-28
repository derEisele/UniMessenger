package unimessenger.abstraction;

import unimessenger.abstraction.storage.WireStorage;

public class URL
{
    public static final String WIRE = "https://prod-nginz-https.wire.com";

    public static final String WIRE_LOGIN = "/login";
    public static final String WIRE_PERSIST = "?persist=true";
    public static final String WIRE_ACCESS = "/access";
    public static final String WIRE_LOGOUT = "/access/logout";
    public static final String WIRE_PREKEY = "/prekeys";

    public static final String WIRE_SELF = "/self";
    public static final String WIRE_NAME = "/self/name";
    public static final String WIRE_USERS = "/users";
    public static final String WIRE_CLIENTS = "/clients";

    public static final String WIRE_CONVERSATIONS = "/conversations";
    public static final String WIRE_OTR_MESSAGES = "/otr/messages";
    public static final String WIRE_NOTIFICATIONS = "/notifications";
    public static final String WIRE_LAST_NOTIFICATION = "/notifications/last";

    public static String wireBearerToken()
    {
        return "?access_token=" + WireStorage.getBearerToken();
    }
}