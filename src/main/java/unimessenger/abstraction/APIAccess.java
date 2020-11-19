package unimessenger.abstraction;

import unimessenger.abstraction.interfaces.*;
import unimessenger.abstraction.wire.*;
import unimessenger.util.enums.SERVICE;

public class APIAccess
{
    private final IConversations WIRE_CON = new WireConversations();
    private final ILoginOut WIRE_LOGIN = new WireLogin();
    private final IMessages WIRE_MESSAGES = new WireMessages();
    private final IUtil WIRE_UTIL = new WireUtil();
    private final IData WIRE_DATA = new WireData();

    public IConversations getConversationInterface(SERVICE service)
    {
        switch(service)
        {
            case WIRE:
                return WIRE_CON;
            case TELEGRAM:
                return null;
            case NONE:
            default:
                return null;
        }
    }
    public ILoginOut getLoginInterface(SERVICE service)
    {
        switch(service)
        {
            case WIRE:
                return WIRE_LOGIN;
            case TELEGRAM:
                return null;
            case NONE:
            default:
                return null;
        }
    }
    public IMessages getMessageInterface(SERVICE service)
    {
        switch(service)
        {
            case WIRE:
                return WIRE_MESSAGES;
            case TELEGRAM:
                return null;
            case NONE:
            default:
                return null;
        }
    }
    public IUtil getUtilInterface(SERVICE service)
    {
        switch(service)
        {
            case WIRE:
                return WIRE_UTIL;
            case TELEGRAM:
                return null;
            case NONE:
            default:
                return null;
        }
    }
    public IData getDataInterface(SERVICE service)
    {
        switch(service)
        {
            case WIRE:
                return WIRE_DATA;
            case TELEGRAM:
                return null;
            case NONE:
            default:
                return null;
        }
    }
}