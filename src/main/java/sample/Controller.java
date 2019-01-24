package sample;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class Controller implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TreeView<String> tree;

    @FXML
    void treeDrag(ActionEvent event) {

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SuperBank superBank = null;
        try {
            superBank = new SuperBank();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        TreeItem root = new TreeItem();
        try {
            root=superBank.generateTree();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        tree.setRoot(root);

    }
}


