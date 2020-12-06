package unimessenger.abstraction;

import unimessenger.abstraction.interfaces.*;
import unimessenger.abstraction.interfaces.wire.*;
import unimessenger.userinteraction.tui.Outputs;
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
        if(service == null)
        {
            Outputs.create("No service for interface provided", this.getClass().getName()).debug().WARNING().print();
            return null;
        }
        switch(service)
        {
            case WIRE:
                return WIRE_CON;
            case TELEGRAM:
                Outputs.create("Conversation interface not implemented yet", this.getClass().getName()).always().ERROR().print();
                return null;
            case NONE:
            default:
                return null;
        }
    }
    public ILoginOut getLoginInterface(SERVICE service)
    {
        if(service == null)
        {
            Outputs.create("No service for interface provided", this.getClass().getName()).debug().WARNING().print();
            return null;
        }
        switch(service)
        {
            case WIRE:
                return WIRE_LOGIN;
            case TELEGRAM:
                Outputs.create("Login interface not implemented yet", this.getClass().getName()).always().ERROR().print();
                return null;
            case NONE:
            default:
                return null;
        }
    }
    public IMessages getMessageInterface(SERVICE service)
    {
        if(service == null)
        {
            Outputs.create("No service for interface provided", this.getClass().getName()).debug().WARNING().print();
            return null;
        }
        switch(service)
        {
            case WIRE:
                return WIRE_MESSAGES;
            case TELEGRAM:
                Outputs.create("Messages interface not implemented yet", this.getClass().getName()).always().ERROR().print();
                return null;
            case NONE:
            default:
                return null;
        }
    }
    public IUtil getUtilInterface(SERVICE service)
    {
        if(service == null)
        {
            Outputs.create("No service for interface provided", this.getClass().getName()).debug().WARNING().print();
            return null;
        }
        switch(service)
        {
            case WIRE:
                return WIRE_UTIL;
            case TELEGRAM:
                Outputs.create("Util interface not implemented yet", this.getClass().getName()).always().ERROR().print();
                return null;
            case NONE:
            default:
                return null;
        }
    }
    public IData getDataInterface(SERVICE service)
    {
        if(service == null)
        {
            Outputs.create("No service for interface provided", this.getClass().getName()).debug().WARNING().print();
            return null;
        }
        switch(service)
        {
            case WIRE:
                return WIRE_DATA;
            case TELEGRAM:
                Outputs.create("Data interface not implemented yet", this.getClass().getName()).always().ERROR().print();
                return null;
            case NONE:
            default:
                return null;
        }
    }
}