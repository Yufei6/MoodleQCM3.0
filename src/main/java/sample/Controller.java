package sample;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;


public class Controller implements Initializable {
    private static String nameFile;
    private static Text textTitle;
    private boolean reload;

    public static String getNameFile() {
        return nameFile;
    }

    static Text getText(){
        return textTitle;
    }
    public ContextMenu getContextMenu() {
        return contextMenu;
    }
    public ResourceBundle getResources() {
        return resources;
    }

    public URL getLocation() {
        return location;
    }

    public TreeView<String> getTree() {
        return tree;
    }

    @FXML

    private ContextMenu contextMenu;
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
        contextMenu.getItems().get(0).setText("Ajouter Dossier");
        SuperBank finalSuperBank = superBank;
        contextMenu.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String string = tree.getSelectionModel().getSelectedItems().get(0).getValue();
                nameFile = string;
                textTitle = new Text("Ajouter Dossier");
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/popup.fxml"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Hello World");
                stage.setScene(new Scene(root, 300, 275));
                stage.show();

            }
        });
        MenuItem menuItem = new MenuItem("Ajouter Question");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                textTitle = new Text("Ajouter Question");
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/popup.fxml"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Hello World");
                stage.setScene(new Scene(root, 500, 500));
                stage.show();

            }
        });
        MenuItem menuItem1 = new MenuItem("Modifier");
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println("essaie");

                System.out.println("essaieReload"+reload);
                textTitle = new Text("Modifier");
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/popup.fxml"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Hello World");
                stage.setScene(new Scene(root, 500, 500));
                stage.show();

            }
        });
        contextMenu.getItems().addAll(menuItem,menuItem1);

    }

    public void clickRight(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException {
        SuperBank superBank = new SuperBank();
        if(mouseEvent.isPopupTrigger()){
            reload = true;
            String string = tree.getSelectionModel().getSelectedItems().get(0).getValue();

        }
    }

    public void reloadTree(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException {
        SuperBank superBank = new SuperBank();
        System.out.println(reload);
        if (reload == true){
            tree.setRoot(superBank.generateTree());
            reload = false;
        }
    }

}


