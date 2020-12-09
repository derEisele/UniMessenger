package unimessenger.userinteraction.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import unimessenger.abstraction.APIAccess;
import unimessenger.abstraction.interfaces.IData;
import unimessenger.util.enums.SERVICE;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatListController implements Initializable
{
    private TabController tabController;
    private MessengerController messengerController;

    @FXML
    private VBox chatList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }
    public void loadChats(SERVICE service)
    {
        new APIAccess().getConversationInterface(service).requestAllConversations();

        IData data = new APIAccess().getDataInterface(service);
        ArrayList<String> ids = data.getAllConversationIDs();
        ids.remove(null);

        for(String id : ids)
        {
            Label l = new Label(data.getConversationNameFromID(id));
            l.setId(id);
            l.setFont(new Font(18));

            l.setOnMouseClicked(mouseEvent ->
            {
                if(!id.equals(messengerController.getCurrentChatID())) messengerController.openChat(l.getId());
            });

            chatList.getChildren().add(l);
        }
    }

    public void setTabController(TabController controller)
    {
        tabController = controller;
    }
    public void setMessengerController(MessengerController controller)
    {
        messengerController = controller;
    }
}
