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
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    private static String nameFile;
    private static Text textTitle;
    private boolean reload;
    private String sys_qcm_path = "./target/Qcm/";
    private String sys_bank_path = "./target/Bank/";

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
        Bank new_bank = new Bank(fileAsString,superBank);
        try {
            copyFileByStream(new File(fileAsString), new File(sys_bank_path+ new_bank.getName()+".xml"));
        }catch (IOException e){
            e.printStackTrace();
        }
        bankList.add(new_bank);
        displayBanks();
    }

    @FXML void exportBank(ActionEvent event){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        String path = file.getPath();
        System.out.println(path);
        TreeItemWithBank<String> treeItem = (TreeItemWithBank<String>) bank.getSelectionModel().getSelectedItems().get(0);
        if (treeItem.getBank() != null){
            treeItem.getBank().Export(path, treeItem.getBank().getName());
        }
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
        Qcm new_qcm = new Qcm(fileAsString,superBank);
        try {
            copyFileByStream(new File(fileAsString), new File(sys_qcm_path+ new_qcm.getName()+".xml"));
        }catch (IOException e){
            e.printStackTrace();
        }
        qcmList.add(new_qcm);
        displayQcms();

    }

    @FXML void exportQcm(ActionEvent event){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        String path = file.getPath();
        System.out.println(path);
        TreeItemWithQcm<String> treeItem = (TreeItemWithQcm<String>) qcm.getSelectionModel().getSelectedItems().get(0);
        if (treeItem.getQcm() != null){
            treeItem.getQcm().Export(path, treeItem.getQcm().getName());
        }
    }


    @FXML void createBank(ActionEvent event){
//        Bank new_bank = new Bank("./target/Bank", );
    }


    @FXML void createQcm(ActionEvent event){

    }


    private void initBanksAndQcms(SuperBank superBank){
        bankList = new ArrayList<Bank>();
        File banks = new File(sys_bank_path);
        for(File b : banks.listFiles()){
             Bank new_bank = new Bank(sys_bank_path+b.getName(),superBank);
            bankList.add(new_bank);
        }
        displayBanks();

        qcmList = new ArrayList<Qcm>();
        File qcms = new File(sys_qcm_path);
        for(File q : qcms.listFiles()){
            Qcm new_qcm = new Qcm(sys_qcm_path+q.getName(),superBank);
            qcmList.add(new_qcm);
        }
        displayQcms();
    }

    private void displayBanks(){
        TreeItem<String> root_bank = new TreeItem<>();
        for(Bank b : bankList){
            TreeItemWithBank<String> treeItem = new TreeItemWithBank<>(b.getName());
            treeItem = b.createQuestionTree(treeItem);
            root_bank.getChildren().addAll(treeItem);
        }
        bank.setRoot(root_bank);
        bank.setShowRoot(false);
    }


    private void displayQcms(){
        TreeItem<String> root_qcm = new TreeItem<>();
        for(Qcm q: qcmList){
            TreeItemWithQcm<String> treeItem = new TreeItemWithQcm<>(q.getName());
            treeItem = q.createQuestionTree(treeItem);
            root_qcm.getChildren().addAll(treeItem);
        }
        qcm.setRoot(root_qcm);
        qcm.setShowRoot(false);
    }


    public static void copyFileByStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest);){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
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

    @FXML
    void questionSaved(ActionEvent event) {
        questionFieldsGet(current_question);
        current_question.save(superBank.find(String.valueOf(current_question.getID())));
    }

    @FXML
    void treeDrag(ActionEvent event) {

    }








    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            superBank = new SuperBank();
            //new_q = new Question("42.xml");
          //  new_q.load("42.xml");
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
        /*questionFieldsInit(new_q);
        setCurrent_question(new_q);*/


        try {
            Question q1 = new Question("demo/1.xml");
            q1.load("demo/1.xml");
            Question q2 = new Question("demo/2.xml");
            q2.load("demo/2.xml");
            Question q3 = new Question("demo/3.xml");
            q3.load("demo/3.xml");

            Qcm qcm1 = new Qcm();
            qcm1.addQuestion(q1);
            qcm1.addQuestion(q2);
            qcm1.addQuestion(q3);

            qcm1.Export("demo/", "qcm1");

        }
        catch(WrongQuestionTypeException e) {
            e.printStackTrace();
        }


    }

    private void selectQuestion(Question question) {
        current_question = question;
        question.load(superBank.find(String.valueOf(question.getID())));
        questionFieldsInit(question);
    }

    public void clickOnItem(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {
        TreeItemWithQuestion<String> treeItem = (TreeItemWithQuestion<String>) tree.getSelectionModel().getSelectedItems().get(0);
        if (treeItem.getQuestion() != null){
            selectQuestion(treeItem.getQuestion());
        }
    }


    public void clickOnQcm(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {
        TreeItemWithQuestion<String> treeItem2 = (TreeItemWithQuestion<String>) qcm.getSelectionModel().getSelectedItems().get(0);
        if (treeItem2.getQuestion() != null){
            treeItem2.getQuestion().load(superBank.find(String.valueOf(treeItem2.getQuestion().getID())));
            String stringHtml = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p>"+treeItem2.getQuestion().getQuestiontext()+"</p></body></html>";
            question_name_field.setText(treeItem2.getQuestion().getName());
            question_text_field.setHtmlText(stringHtml);
            general_feebdack_field.setHtmlText(treeItem2.getQuestion().getGeneralfeedback());
            correct_feedback_field.setHtmlText(treeItem2.getQuestion().getCorrectfeedback());
            partially_correct_feedback_field.setHtmlText(treeItem2.getQuestion().getPartiallycorrectfeedback());
            incorrect_feedback_field.setHtmlText(treeItem2.getQuestion().getIncorrectfeedback());
            //TODO : question choice type ??
            defaultgrade_field.setText(String.valueOf(treeItem2.getQuestion().getDefaultgrade()));
            penalty_field.setText(String.valueOf(treeItem2.getQuestion().getPenalty()));
        }
        //TODO : sauvegarder

    }



    public void clickOnBank(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {
        TreeItemWithQuestion<String> treeItem3 = (TreeItemWithQuestion<String>) bank.getSelectionModel().getSelectedItems().get(0);
        if (treeItem3.getQuestion() != null){
            treeItem3.getQuestion().load(superBank.find(String.valueOf(treeItem3.getQuestion().getID())));
            String stringHtml = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p>"+treeItem3.getQuestion().getQuestiontext()+"</p></body></html>";
            question_name_field.setText(treeItem3.getQuestion().getName());
            question_text_field.setHtmlText(stringHtml);
            general_feebdack_field.setHtmlText(treeItem3.getQuestion().getGeneralfeedback());
            correct_feedback_field.setHtmlText(treeItem3.getQuestion().getCorrectfeedback());
            partially_correct_feedback_field.setHtmlText(treeItem3.getQuestion().getPartiallycorrectfeedback());
            incorrect_feedback_field.setHtmlText(treeItem3.getQuestion().getIncorrectfeedback());
            //TODO : question choice type ??
            defaultgrade_field.setText(String.valueOf(treeItem3.getQuestion().getDefaultgrade()));
            penalty_field.setText(String.valueOf(treeItem3.getQuestion().getPenalty()));
        }

    }


    public void reloadTree(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {

    }



}




