package unimessenger.util.MessengerStructure;

public class WirePerson
{
    public String hidden_ref;
    public int status;
    public String service;
    public String otr_muted_ref;
    public ROLE conversation_role;
    public String status_time;
    public boolean hidden;
    public String status_ref;
    public String id;
    public boolean otr_archived;
    public String otr_muted_status;
    public boolean otr_muted;
    public String otr_archived_ref;

    public WirePerson()
    {
        hidden_ref = null;
        status = -1;
        service = null;
        otr_muted_ref = null;
        conversation_role = ROLE.NULL;
        status_time = null;
        hidden = false;
        status_ref = null;
        id = null;
        otr_archived = false;
        otr_muted_status = null;
        otr_muted = false;
        otr_archived_ref = null;
    }

    public enum ROLE
    {
        NULL,
        WIRE_ADMIN;
    }
}