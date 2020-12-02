package unimessenger.abstraction.wire.structures;

import java.io.Serializable;

public class WirePerson implements Serializable
{
    public String hidden_ref;
    public int status;
    public String service;
    public String otr_muted_ref;
    public String conversation_role;
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
        conversation_role = null;
        status_time = null;
        hidden = false;
        status_ref = null;
        id = null;
        otr_archived = false;
        otr_muted_status = null;
        otr_muted = false;
        otr_archived_ref = null;
    }
}