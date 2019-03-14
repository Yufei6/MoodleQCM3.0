package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;


public class PopupController implements Initializable {

    @FXML
    private Text text;
    @FXML
    private TextField nameFile;
    private SuperBank superBank;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            superBank = new SuperBank();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (WrongQuestionTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        System.out.println(Controller.getText().getText());
        String string = Controller.getText().getText();
        text.setText(string);

    }

    @FXML
    void clicked(ActionEvent event) {
       superBank.addNewDirectory(Controller.getNameFile(),nameFile.getText());
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }

}
