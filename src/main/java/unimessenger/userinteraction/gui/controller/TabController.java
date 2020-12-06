package unimessenger.userinteraction.gui.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.ILoginOut;
import unimessenger.abstraction.interfaces.IUtil;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.gui.MainWindow;
import unimessenger.userinteraction.tui.Outputs;
import unimessenger.util.Updater;
import unimessenger.util.enums.SERVICE;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TabController implements Initializable
{
    private SERVICE service = SERVICE.NONE;

    @FXML
    private Tab tab;
    @FXML
    private AnchorPane anchor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    @FXML
    private void wire()
    {
        MainWindow.getInstance().resize();
        tab.setText("Wire");
        tab.setClosable(true);
        service = SERVICE.WIRE;
        clearTab();

        MainWindow.getInstance().addMessengerTab();

        APIAccess access = new APIAccess();
        ILoginOut login = access.getLoginInterface(service);
        IUtil util = access.getUtilInterface(service);
        if(login == null && (WireStorage.getBearerToken() == null || util == null))
        {
            Outputs.create("Could not load login interfaces").debug().INFO().print();
        } else if(login != null && login.checkIfLoggedIn())
        {
            Outputs.create("Still logged in").verbose().INFO().print();
        } else if(WireStorage.getBearerToken() != null && util != null && util.refreshSession())
        {
            Outputs.create("Refreshed session").verbose().INFO().print();
        } else
        {
            loadLogin();
            return;
        }

        if(!access.getUtilInterface(service).loadProfile()) Outputs.create("Could not load profile", this.getClass().getName()).verbose().debug().ERROR().print();
        loadMessenger();
        Updater.addService(service);
    }
    @FXML
    private void telegram()
    {
        MainWindow.getInstance().resize();
        tab.setText("Telegram");
        tab.setClosable(true);
        service = SERVICE.TELEGRAM;
        clearTab();

        MainWindow.getInstance().addMessengerTab();

        //TODO: Add Telegram stuff
        loadLogin();
    }

    private void loadLogin()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        VBox login;
        try
        {
            login = loader.load();
            loader.<LoginController>getController().setTabController(this);
        } catch(IOException ignored)
        {
            Outputs.create("Error loading login menu").debug().WARNING().print();
            return;
        }
        anchor.getChildren().add(login);
    }

    private void loadMessenger()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/messenger.fxml"));
        HBox box;

        try
        {
            box = loader.load();
            MessengerController controller = loader.getController();
            controller.setTabController(this);
            controller.loadChats();
        } catch(IOException ignored)
        {
            Outputs.create("Error loading messenger", this.getClass().getName()).debug().WARNING().print();
            return;
        }

        anchor.getChildren().add(box);
    }

    public void closeTab()
    {
        EventHandler<Event> handler = tab.getOnClosed();
        if(handler != null) handler.handle(null);
        else tab.getTabPane().getTabs().remove(tab);
    }
    public SERVICE getService()
    {
        return service;
    }
    public void clearTab()
    {
        while(anchor.getChildren().size() > 0)
        {
            anchor.getChildren().remove(0);
        }
    }
    public void addToTab(Node node)
    {
        anchor.getChildren().add(node);
    }
}
