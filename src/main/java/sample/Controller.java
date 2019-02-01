package sample;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

public class Controller implements Initializable {

    SuperBank superBank;
    List<Bank> bankList;
    List<Qcm> qcmList;

    QuestionStorage current_quizz;
    Question current_question;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TreeView<String> tree, bank, qcm;


    /////////////// Question Fields ////////////////////
    @FXML
    private TextField question_name_field;

    @FXML
    private HTMLEditor question_text_field;

    @FXML
    private HTMLEditor correct_feedback_field;

    @FXML
    private HTMLEditor partially_correct_feedback_field;

    @FXML
    private HTMLEditor incorrect_feedback_field;

    @FXML
    private HTMLEditor general_feebdack_field;

    @FXML
    private ChoiceBox<String> question_choice_type;
    private Window stage;


    @FXML
    private TextField defaultgrade_field;

    @FXML
    private TextField penalty_field;

    ////////////////////////////////////////////////////
    @FXML void importBank(ActionEvent event){
//        JFileChooser chooser = new JFileChooser();
//        ExampleFileFilter filter = new ExampleFileFilter();
//        filter.addExtension("xml");
//        chooser.setFileFilter(filter);
//        int returnVal = chooser.showOpenDialog(parent);
//
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File f = fileChooser.showOpenDialog(stage);


        String fileAsString = null;
        if (f != null) {
            fileAsString = f.toString();
        }
        System.out.println(fileAsString);

    }

    @FXML void exportBank(ActionEvent event){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        String path = file.getPath();
        System.out.println(path);
    }

    @FXML void importQcm(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File f = fileChooser.showOpenDialog(stage);


        String fileAsString = null;
        if (f != null) {
            fileAsString = f.toString();
        }
        System.out.println(fileAsString);
    }

    @FXML void exportQcm(ActionEvent event){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        String path = file.getPath();
        System.out.println(path);
    }


    @FXML void createBank(ActionEvent event){
//        Bank new_bank = new Bank("./target/Bank", );
    }


    @FXML void createQcm(ActionEvent event){

    }









    private void initBanksAndQcms(SuperBank superBank){
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






    private void questionFieldsInit(Question question) {

        question_name_field.setText(question.getName());
        question_text_field.setHtmlText(question.getQuestiontext());
        general_feebdack_field.setHtmlText(question.getGeneralfeedback());
        incorrect_feedback_field.setHtmlText(question.getIncorrectfeedback());
        partially_correct_feedback_field.setHtmlText(question.getPartiallycorrectfeedback());
        correct_feedback_field.setHtmlText(question.getCorrectfeedback());

        ObservableList<String> question_types = FXCollections.observableArrayList(question.getAnswerNumberingChoices());
        question_choice_type.setItems(question_types);
        question_choice_type.getSelectionModel().select(question.getAnswerNumberingDisplay());
        defaultgrade_field.setText(Double.toString(question.getDefaultgrade()));
        penalty_field.setText(Double.toString(question.getPenalty()));
    }






    @FXML
    void treeDrag(ActionEvent event) {

    }













    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Question new_q = null;
        try {
            superBank = new SuperBank();
            new_q = new Question("42.xml");
            new_q.load("42.xml");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (WrongQuestionTypeException e) {
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


        initBanksAndQcms(superBank);
        questionFieldsInit(new_q);
    



    }


}




