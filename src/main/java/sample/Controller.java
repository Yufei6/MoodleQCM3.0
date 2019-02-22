package sample;
import com.sun.tools.corba.se.idl.SymtabEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
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
    private ContextMenu contextMenu, contextMenuBank, contextMenuQcm;
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
        if(bank.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQcmAndBank) {
            TreeItemWithQcmAndBank<String> treeItem = (TreeItemWithQcmAndBank<String>) bank.getSelectionModel().getSelectedItems().get(0);
            if (treeItem.getBank() != null) {
                treeItem.getBank().Export(path+"/", treeItem.getBank().getName());
            } else {
            }
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
        if(qcm.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQcmAndBank) {
            TreeItemWithQcmAndBank<String> treeItem = (TreeItemWithQcmAndBank<String>) qcm.getSelectionModel().getSelectedItems().get(0);
            if (treeItem.getQcm() != null) {
                treeItem.getQcm().Export(path+"/", treeItem.getQcm().getName());
            }
        }
    }


    @FXML void creerBank(ActionEvent event){
        Stage window = new Stage();
        window.setTitle("Nouveau Banque");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);
        Button button = new Button("Annuler");
        button.setOnAction(e -> window.close());
        Button button2 = new Button("OK");
        TextField notification = new TextField ();
        notification.setPromptText("Nom du banque");
        notification.clear();
        button2.setOnAction((ActionEvent e) -> {
            createBank(notification.getText());
            window.close();
        });
        Label label = new Label("Entrez le nom du nouveau banque");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label , notification, button, button2);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }




    @FXML void creerQcm(ActionEvent event){
        Stage window = new Stage();
        window.setTitle("Nouveau Qcm");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);
        Button button = new Button("Annuler");
        button.setOnAction(e -> window.close());
        Button button2 = new Button("OK");
        TextField notification = new TextField ();
        notification.setPromptText("Nom du qcm");
        notification.clear();
        button2.setOnAction((ActionEvent e) -> {
            createQcm(notification.getText());
            window.close();
        });
        Label label = new Label("Entrez le nom du nouveau qcm");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label , notification, button, button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    @FXML void createBank(String name_0){
        Bank new_bank = new Bank(sys_bank_path+name_0+".xml",name_0,superBank);
        new_bank.save();
        bankList.add(new_bank);
        displayBanks();
    }


    @FXML void createQcm(String name_0){
        Qcm new_qcm = new Qcm(sys_qcm_path+name_0+".xml",name_0,superBank);
        new_qcm.save();
        qcmList.add(new_qcm);
        displayQcms();
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
            TreeItemWithQcmAndBank<String> treeItem = new TreeItemWithQcmAndBank<>(b.getName(),b);
            treeItem = b.createQuestionTree(treeItem);
            root_bank.getChildren().addAll(treeItem);
        }
        bank.setRoot(root_bank);
        bank.setShowRoot(false);
    }


    private void displayQcms(){
        TreeItem<String> root_qcm = new TreeItem<>();
        for(Qcm q: qcmList){
            TreeItemWithQcmAndBank<String> treeItem = new TreeItemWithQcmAndBank<>(q.getName(),q);
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

    public static void deleteFile(String sPath) {
        File file = new File(sPath);
        if(file.isFile() && file.exists()) {
            file.delete();
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

        contextMenuBank.getItems().get(0).setText("Ajouter banque");
        contextMenuBank.getItems().get(0).setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        contextMenuBank.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                creerBank(e);
            }
        });
        MenuItem menuItemBank1 = new MenuItem("Import banque");
        menuItemBank1.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        menuItemBank1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                importBank(e);
            }
        });
        MenuItem menuItemBank2 = new MenuItem("Export banque");
        menuItemBank2.setAccelerator(KeyCombination.keyCombination("Ctrl+G"));
        menuItemBank2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                exportBank(e);
            }
        });
        MenuItem menuItemBank3 = new MenuItem("Supprimer banque");
        menuItemBank3.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
        menuItemBank3.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(bank.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQcmAndBank) {
                    deleteFile(sys_bank_path+bank.getSelectionModel().getSelectedItems().get(0).getValue()+".xml");
                    initBanksAndQcms(superBank);
                }
            }
        });
        MenuItem menuItemBank4 = new MenuItem("Modifier nom d'une banque");
        menuItemBank4.setAccelerator(KeyCombination.keyCombination("Ctrl+K"));
        menuItemBank4.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(bank.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQcmAndBank) {
                    Stage window = new Stage();
                    window.setTitle("Modification nom de bank");
                    window.initModality(Modality.APPLICATION_MODAL);
                    window.setMinWidth(300);
                    window.setMinHeight(150);
                    Button button = new Button("Annuler");
                    button.setOnAction(e1 -> window.close());
                    Button button2 = new Button("OK");
                    TextField notification = new TextField();
                    notification.setPromptText("Nouveau nom de banque");
                    notification.clear();
                    button2.setOnAction((ActionEvent e2) -> {
                        String name_0 = bank.getSelectionModel().getSelectedItems().get(0).getValue();
                        for(Bank b : bankList) {
                            if (name_0 == b.getName()){
                                deleteFile(b.getPath());
                                b.changeName(notification.getText());
                                b.changePath(sys_bank_path+notification.getText()+".xml");
                                b.save();
                            }
                        }
                        initBanksAndQcms(superBank);
                        window.close();
                    });
                    Label label = new Label("Entrez le nom du nouveau banque");
                    VBox layout = new VBox(10);
                    layout.getChildren().addAll(label, notification, button, button2);
                    layout.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(layout);
                    window.setScene(scene);
                    window.showAndWait();
                }
            }
        });
        contextMenuBank.getItems().addAll(menuItemBank1,menuItemBank2,menuItemBank4, menuItemBank3);


        contextMenuQcm.getItems().get(0).setText("Ajouter qcm");
        contextMenuQcm.getItems().get(0).setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
        contextMenuQcm.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                creerQcm(e);
            }
        });
        MenuItem menuItemQcm1 = new MenuItem("Import qcm");
        menuItemQcm1.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        menuItemQcm1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                importQcm(e);
            }
        });
        MenuItem menuItemQcm2 = new MenuItem("Export qcm");
        menuItemQcm2.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
        menuItemQcm2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                exportQcm(e);
            }
        });
        MenuItem menuItemQcm3 = new MenuItem("Supprimer qcm");
        menuItemQcm3.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
        menuItemQcm3.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(qcm.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQcmAndBank) {
                    deleteFile(sys_qcm_path+qcm.getSelectionModel().getSelectedItems().get(0).getValue()+".xml");
                    initBanksAndQcms(superBank);
                }
            }
        });
        MenuItem menuItemQcm4 = new MenuItem("Modifier nom d'une qcm");
        menuItemQcm4.setAccelerator(KeyCombination.keyCombination("Ctrl+J"));
        menuItemQcm4.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(qcm.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQcmAndBank) {
                    Stage window = new Stage();
                    window.setTitle("Modification nom de qcm");
                    window.initModality(Modality.APPLICATION_MODAL);
                    window.setMinWidth(300);
                    window.setMinHeight(150);
                    Button button = new Button("Annuler");
                    button.setOnAction(e1 -> window.close());
                    Button button2 = new Button("OK");
                    TextField notification = new TextField();
                    notification.setPromptText("Nouveau nom de qcm");
                    notification.clear();
                    button2.setOnAction((ActionEvent e2) -> {
                        String name_0 = qcm.getSelectionModel().getSelectedItems().get(0).getValue();
                        for(Qcm q : qcmList) {
                            if (name_0 == q.getName()){
                                deleteFile(q.getPath());
                                q.changeName(notification.getText());
                                q.changePath(sys_qcm_path+notification.getText()+".xml");
                                q.save();
                            }
                        }
                        initBanksAndQcms(superBank);
                        window.close();
                    });
                    Label label = new Label("Entrez le nom du nouveau qcm");
                    VBox layout = new VBox(10);
                    layout.getChildren().addAll(label, notification, button, button2);
                    layout.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(layout);
                    window.setScene(scene);
                    window.showAndWait();
                }
            }
        });
        contextMenuQcm.getItems().addAll(menuItemQcm1,menuItemQcm2,menuItemQcm4,menuItemQcm3);




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

//            qcm1.Export("demo/", "qcm1");

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
        if(qcm.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQuestion) {
            TreeItemWithQuestion<String> treeItem2 = (TreeItemWithQuestion<String>) qcm.getSelectionModel().getSelectedItems().get(0);
            if (treeItem2.getQuestion() != null) {
                selectQuestion(treeItem2.getQuestion());
            }
        }
        //TODO : sauvegarder

    }



    public void clickOnBank(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {
        if(bank.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQuestion) {
            TreeItemWithQuestion<String> treeItem3 = (TreeItemWithQuestion<String>) bank.getSelectionModel().getSelectedItems().get(0);
            if (treeItem3.getQuestion() != null) {
                selectQuestion(treeItem3.getQuestion());
            }
        }

    }


    public void reloadTree(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {

    }



}




