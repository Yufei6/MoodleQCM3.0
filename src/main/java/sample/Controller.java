package sample;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private TreeView<String> tree, bank, qcm;

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
        try {
            superBank.extractId_Path();
        } catch (org.xml.sax.SAXException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        tree.setRoot(root);





        ArrayList<Bank> bank_tab = new ArrayList<Bank>();
        File banks = new File("./target/Bank");
        TreeItem<String> root_bank = new TreeItem<>("Banque");
        for(File b : banks.listFiles()){
            Bank new_bank = new Bank("./target/Bank/"+b.getName(),superBank);
            bank_tab.add(new_bank);
            TreeItem<String> treeItem = new TreeItem<>(new_bank.getName());
            treeItem = new_bank.createQuestionTree(treeItem);
            root_bank.getChildren().addAll(treeItem);
        }
        bank.setRoot(root_bank);



        ArrayList<Qcm> qcm_tab = new ArrayList<Qcm>();
        File qcms = new File("./target/Qcm");
        TreeItem<String> root_qcm = new TreeItem<>("Qcm");
        for(File q : qcms.listFiles()){
            Qcm new_qcm = new Qcm("./target/Qcm/"+q.getName(),superBank);
            qcm_tab.add(new_qcm);
            TreeItem<String> treeItem = new TreeItem<>(new_qcm.getName());
            treeItem = new_qcm.createQuestionTree(treeItem);
            root_qcm.getChildren().addAll(treeItem);
        }
        qcm.setRoot(root_qcm);




    }
}


