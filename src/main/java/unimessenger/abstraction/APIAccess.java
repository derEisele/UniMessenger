package unimessenger.abstraction;

import unimessenger.abstraction.interfaces.IConversations;
import unimessenger.abstraction.interfaces.ILoginOut;
import unimessenger.abstraction.interfaces.IMessages;
import unimessenger.abstraction.interfaces.IUtil;
import unimessenger.abstraction.wire.WireConversations;
import unimessenger.abstraction.wire.WireLogin;
import unimessenger.abstraction.wire.WireMessages;
import unimessenger.abstraction.wire.WireUtil;
import unimessenger.util.Variables;

public class APIAccess
{
    private final IConversations WIRE_CON = new WireConversations();
    private final ILoginOut WIRE_LOGIN = new WireLogin();
    private final IMessages WIRE_MESSAGES = new WireMessages();
    private final IUtil WIRE_UTIL = new WireUtil();

    public IConversations getConversationInterface(Variables.SERVICE service)
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
    public ILoginOut getLoginInterface(Variables.SERVICE service)
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
    public IMessages getMessageInterface(Variables.SERVICE service)
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
    public IUtil getUtilInterface(Variables.SERVICE service)
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
}