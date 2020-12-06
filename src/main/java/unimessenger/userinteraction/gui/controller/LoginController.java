package unimessenger.userinteraction.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.ILoginOut;
import unimessenger.abstraction.storage.WireStorage;
import unimessenger.userinteraction.tui.Outputs;
import unimessenger.util.Updater;

import java.io.IOException;

public class LoginController
{
    private TabController tabController;

    @FXML
    private TextField txtMail;
    @FXML
    private TextField txtPW;
    @FXML
    private CheckBox cbPermanent;

    @FXML
    private void login()
    {
        String mail = txtMail.getText();
        String pw = txtPW.getText();
        if(cbPermanent.isSelected()) WireStorage.persistent = true;

        ILoginOut login = new APIAccess().getLoginInterface(tabController.getService());
        if(login == null)
        {
            Outputs.create("Unable to obtain login interface", this.getClass().getName()).debug().WARNING().print();
            return;
        }

        if(login.login(mail, pw))
        {
            Updater.addService(tabController.getService());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/messenger.fxml"));
            HBox box;

            try
            {
                box = loader.load();
                MessengerController controller = loader.getController();
                controller.setTabController(tabController);
                controller.loadChats();

                tabController.clearTab();
                tabController.addToTab(box);
            } catch(IOException ignored)
            {
                Outputs.create("Error loading messenger", this.getClass().getName()).debug().WARNING().print();
            }
        }
    }

    @FXML
    private void cancel()
    {
        tabController.closeTab();
    }

    public void setTabController(TabController controller)
    {
        tabController = controller;
    }
}
