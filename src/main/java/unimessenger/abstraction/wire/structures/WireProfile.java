package unimessenger.abstraction.wire.structures;

import java.util.ArrayList;

public class WireProfile
{
    public String email;
    public String phone;
    public String handle;
    public String locale;
    public String managed_by;
    public int accent_id;
    public String userName;
    public String id;
    public boolean deleted;
    public ArrayList<String> userAssets;

    public WireProfile()
    {
        email = null;
        phone = null;
        handle = null;
        locale = null;
        managed_by = null;
        accent_id = -1;
        userName = null;
        id = null;
        deleted = false;
        userAssets = new ArrayList<>();
    }
}