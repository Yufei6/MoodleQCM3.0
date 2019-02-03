package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


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

    @FXML
    private RadioButton multiple_answers_choice;

    @FXML
    private RadioButton shuffle_answers_choice;

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
        TreeItem<String> root_bank = new TreeItem<>();
        for(File b : banks.listFiles()){
             Bank new_bank = new Bank("./target/Bank/"+b.getName(),superBank);
            bank_tab.add(new_bank);
            TreeItem<String> treeItem = new TreeItem<>(new_bank.getName());
            treeItem = new_bank.createQuestionTree(treeItem);
            root_bank.getChildren().addAll(treeItem);
        }
        bank.setRoot(root_bank);
        bank.setShowRoot(false);



        ArrayList<Qcm> qcm_tab = new ArrayList<Qcm>();
//        qcm_tab.add(Qcm.Import("./target/Qcm_Import/import1.xml", "import1", superBank));
//        qcm_tab.add(Qcm.Import("./target/Qcm_Import/import1.xml", "import1", superBank));
        File qcms = new File("./target/Qcm");
        TreeItem<String> root_qcm = new TreeItem<>();
        for(File q : qcms.listFiles()){
            Qcm new_qcm = new Qcm("./target/Qcm/"+q.getName(),superBank);
            qcm_tab.add(new_qcm);
            TreeItem<String> treeItem = new TreeItem<>(new_qcm.getName());
            treeItem = new_qcm.createQuestionTree(treeItem);
            root_qcm.getChildren().addAll(treeItem);
        }
        qcm.setRoot(root_qcm);
        qcm.setShowRoot(false);
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

        shuffle_answers_choice.setSelected(question.isShuffleanswers());
        multiple_answers_choice.setSelected(!question.isSingle());
    }

    private void questionFieldsGet(Question question) {  // TODO : Collecter les erreurs et les champs incomplets
        question.setName(question_name_field.getText());
        question.setQuestiontext(question_text_field.getHtmlText());
        question.setGeneralfeedback(general_feebdack_field.getHtmlText());
        question.setIncorrectfeedback(incorrect_feedback_field.getHtmlText());
        question.setPartiallycorrectfeedback(partially_correct_feedback_field.getHtmlText());
        question.setCorrectfeedback(correct_feedback_field.getHtmlText());
        question.setAnswernumbering(question_choice_type.getValue());
        question.setDefaultgrade(Double.parseDouble(defaultgrade_field.getText()));
        question.setPenalty(Double.parseDouble(penalty_field.getText()));
        question.setSingle(!(multiple_answers_choice.isSelected()));
        question.setShuffleanswers(shuffle_answers_choice.isSelected());
    }

    public Question getCurrent_question() {
        return current_question;
    }

    public void setCurrent_question(Question current_question) {
        this.current_question = current_question;
    }

    @FXML
    void questionSaved(ActionEvent event) {
        questionFieldsGet(current_question);
        current_question.save("42.xml");
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
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TreeItem root = new TreeItem();
        try {
            root=superBank.generateTreeWithQuestion();
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        } catch (WrongQuestionTypeException e) {
            e.printStackTrace();
        }
        try {
            superBank.extractId_Path();
        } catch (SAXException | IOException e){
            e.printStackTrace();
        }
        tree.setRoot(root);
        contextMenu.getItems().get(0).setText("Ajouter Dossier");
        SuperBank finalSuperBank = superBank;
        contextMenu.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                nameFile = tree.getSelectionModel().getSelectedItems().get(0).getValue();
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


        initBanksAndQcms(superBank);
        questionFieldsInit(new_q);
        setCurrent_question(new_q);



    }


    public void clickOnItem(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {
        TreeItemWithQuestion<String> treeItem = (TreeItemWithQuestion<String>) tree.getSelectionModel().getSelectedItems().get(0);
        if (treeItem.getQuestion() != null){
            System.out.println(treeItem.getQuestion().getName());
            treeItem.getQuestion().load(superBank.find(String.valueOf(treeItem.getQuestion().getID())));
            System.out.println(treeItem.getQuestion().getQuestiontext());
            String stringHtml = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p>"+treeItem.getQuestion().getQuestiontext()+"</p></body></html>";
            question_name_field.setText(treeItem.getQuestion().getName());
            question_text_field.setHtmlText(stringHtml);
            System.out.println(treeItem.getQuestion().getGeneralfeedback());
            general_feebdack_field.setHtmlText(treeItem.getQuestion().getGeneralfeedback());
            correct_feedback_field.setHtmlText(treeItem.getQuestion().getCorrectfeedback());
            partially_correct_feedback_field.setHtmlText(treeItem.getQuestion().getPartiallycorrectfeedback());
            incorrect_feedback_field.setHtmlText(treeItem.getQuestion().getIncorrectfeedback());
            //TODO : question choice type ??
            defaultgrade_field.setText(String.valueOf(treeItem.getQuestion().getDefaultgrade()));
            penalty_field.setText(String.valueOf(treeItem.getQuestion().getPenalty()));
        }

        //TODO : sauvegarder

    }

    public void reloadTree(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {

    }



}




