package sample;
import com.sun.tools.corba.se.idl.SymtabEntry;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.*;
import javafx.stage.Window;
import javafx.util.Callback;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.List;


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

    @FXML
    private AnchorPane question_pane;

    @FXML
    private ChoiceBox<String> answers_box;

    @FXML
    private HTMLEditor answer_feedback_field;

    @FXML
    private HTMLEditor answer_text_field;

    @FXML
    private ChoiceBox<String> answer_fraction_box;

    ////////////////////////////////////////////////////
    @FXML void afficherError(String msg){
        Alert _alert = new Alert(Alert.AlertType.CONFIRMATION,msg, new ButtonType("OK", ButtonBar.ButtonData.YES));
        _alert.setTitle("Il y a une erreur");
        _alert.setHeaderText("Error!");
        _alert.initOwner(stage);
        Optional<ButtonType> _buttonType = _alert.showAndWait();
        if(_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
            _alert.close();
        }
    }


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
        if(bank.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQcmAndBank) {
            DirectoryChooser directoryChooser=new DirectoryChooser();
            File file = directoryChooser.showDialog(stage);
            String path = file.getPath();
            TreeItemWithQcmAndBank<String> treeItem = (TreeItemWithQcmAndBank<String>) bank.getSelectionModel().getSelectedItems().get(0);
            if (treeItem.getBank() != null) {
                treeItem.getBank().Export(path+"/", treeItem.getBank().getName());
            } else {
                afficherError("Il faut choisir une banque pour exportBanque");
            }
        }
        else{
            afficherError("Il faut choisir une banque pour exportBanque");
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
        if(qcm.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQcmAndBank) {
            DirectoryChooser directoryChooser=new DirectoryChooser();
            File file = directoryChooser.showDialog(stage);
            String path = file.getPath();
            TreeItemWithQcmAndBank<String> treeItem = (TreeItemWithQcmAndBank<String>) qcm.getSelectionModel().getSelectedItems().get(0);
            if (treeItem.getQcm() != null) {
                treeItem.getQcm().Export(path+"/", treeItem.getQcm().getName());
            }
            else{
                afficherError("Il faut choisir une qcm pour exportQcm");
            }
        }
        else{
            afficherError("Il faut choisir une qcm pour exportQcm");
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
        bank.setEditable(true);
        bank.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new TextFieldTreeCellImpl(superBank);
            }


        });
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
        qcm.setEditable(true);
        qcm.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new TextFieldTreeCellImpl(superBank);
            }
        });
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

        ObservableList<String> answers_display = FXCollections.observableArrayList(question.getAnswersDisplay());
        answers_box.setItems(answers_display);
        answers_box.getSelectionModel().select(0);

        answerFieldsInit(current_question.getAnswerByIndex(0));


    }

    private void answerFieldsInit(Answer answer) {
        answer_text_field.setHtmlText(answer.getText());
        answer_feedback_field.setHtmlText(answer.getFeedback());
        ObservableList<String> answer_fractions = FXCollections.observableArrayList(answer.getAvailableFractions());
        answer_fraction_box.setItems(answer_fractions);
        answer_fraction_box.getSelectionModel().select("" + answer.getFraction());
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
        tree.setEditable(true);
        tree.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new TextFieldTreeCellImplForSuperBank();
            }
        });

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

        MenuItem menuItemBank0 = new MenuItem("Ajouter banque");
        menuItemBank0.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        menuItemBank0.setOnAction(new EventHandler<ActionEvent>() {
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
                    HBox hbox = new HBox();
                    hbox.getChildren().addAll( button2, button);
                    hbox.setSpacing(10);
                    hbox.setAlignment(Pos.CENTER);
                    layout.getChildren().addAll(label, notification,hbox);
                    layout.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(layout);
                    window.setScene(scene);
                    window.showAndWait();
                }
                else{
                    afficherError("Il faut choisir une banque pour modifier son nom");
                }
            }
        });
        MenuItem menuItemBank5 = new MenuItem("Ajouter une question");
        menuItemBank5.setAccelerator(KeyCombination.keyCombination("Ctrl+M"));
        menuItemBank5.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                TreeItem it = bank.getSelectionModel().getSelectedItems().get(0);
                if((it instanceof TreeItemWithQcmAndBank) && !(it instanceof TreeItemWithQuestion)) {
                    Stage window = new Stage();
                    window.setTitle("Ajouter une question");
                    window.initModality(Modality.APPLICATION_MODAL);
                    window.setMinWidth(300);
                    window.setMinHeight(150);
                    Button button = new Button("Annuler");
                    button.setOnAction(e1 -> window.close());
                    Button button2 = new Button("OK");
                    TextField notification = new TextField();
                    notification.setPromptText("Numero de la question");
                    notification.clear();
                    button2.setOnAction((ActionEvent e2) -> {
                        String name_0 = bank.getSelectionModel().getSelectedItems().get(0).getValue();
                        for(Bank b : bankList) {
                            if (name_0 == b.getName()){
                                try {
                                    Question new_q = new Question(superBank.find(notification.getText()));
                                    b.addQuestion(new_q);
                                }catch(WrongQuestionTypeException e3){
                                    e3.printStackTrace();
                                }
                                b.save();
                            }
                        }
                        initBanksAndQcms(superBank);
                        window.close();
                    });
                    Label label = new Label("Entrez le numero de la question pour ajouter");
                    VBox layout = new VBox(10);
                    HBox hbox = new HBox();
                    hbox.setSpacing(10);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.getChildren().addAll(button2,button);
                    layout.getChildren().addAll(label, notification, hbox);
                    layout.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(layout);
                    window.setScene(scene);
                    window.showAndWait();
                }
                else{
                    afficherError("Il faut choisir une banque pour y ajouter question!");
                }
            }
        });
        MenuItem menuItemBank6 = new MenuItem("Supprimer une question");
        menuItemBank6.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        menuItemBank6.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                TreeItem it = bank.getSelectionModel().getSelectedItems().get(0);
                if(it instanceof TreeItemWithQuestion) {
                    Stage window = new Stage();
                    window.setTitle("Supprimer cette Question");
                    window.initModality(Modality.APPLICATION_MODAL);
                    window.setMinWidth(300);
                    window.setMinHeight(150);
                    Button button = new Button("Non");
                    button.setOnAction(e1 -> window.close());
                    Button button2 = new Button("Oui");
                    button2.setOnAction((ActionEvent e2) -> {
                        Question q = ((TreeItemWithQuestion) it).getQuestion();
                        TreeItem parent = it.getParent();
                        if(parent instanceof TreeItemWithQcmAndBank){
                            Bank b=((TreeItemWithQcmAndBank) parent).getBank();
                            b.deleteQuestion(q);
                            b.save();
                        }
                        displayBanks();
                        window.close();
                    });
                    Label label = new Label("Vous voulez supprimer cette question");
                    VBox layout = new VBox(10);
                    HBox hbox = new HBox();
                    hbox.setSpacing(10);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.getChildren().addAll(button2,button);
                    layout.getChildren().addAll(label,hbox);
                    layout.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(layout);
                    window.setScene(scene);
                    window.showAndWait();
                }
                else{
                    afficherError("Il faut choisir une question pour le supprimer!");
                }
            }
        });

        bank.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                TreeItem it = bank.getSelectionModel().getSelectedItems().get(0);
                if (it instanceof TreeItemWithQuestion){
                    contextMenuBank.getItems().setAll(menuItemBank6);
                }
                else if(it instanceof TreeItemWithQcmAndBank){
                    contextMenuBank.getItems().setAll(menuItemBank2,menuItemBank3,menuItemBank4,menuItemBank5);
                }
                else{
                    contextMenuBank.getItems().setAll(menuItemBank0,menuItemBank1);
                }
            }
        });


        MenuItem menuItemQcm0 = new MenuItem("Ajouter qcm");
        menuItemQcm0.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
        menuItemQcm0.setOnAction(new EventHandler<ActionEvent>() {
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
                else{
                    afficherError("Il faut choisir une qcm pour le supprimer!");
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
                    HBox hbox = new HBox();
                    hbox.setSpacing(10);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.getChildren().addAll(button2,button);
                    layout.getChildren().addAll(label, notification, hbox);
                    layout.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(layout);
                    window.setScene(scene);
                    window.showAndWait();
                }
                else{
                    afficherError("Il faut choisir une qcm pour le modifier son nom!");
                }
            }
        });
        MenuItem menuItemQcm5 = new MenuItem("Ajouter une question");
        menuItemQcm5.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        menuItemQcm5.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                TreeItem it =qcm.getSelectionModel().getSelectedItems().get(0);
                if(it instanceof TreeItemWithQcmAndBank && !(it instanceof TreeItemWithQuestion)) {
                    Stage window = new Stage();
                    window.setTitle("Ajouter une question");
                    window.initModality(Modality.APPLICATION_MODAL);
                    window.setMinWidth(300);
                    window.setMinHeight(150);
                    Button button = new Button("Annuler");
                    button.setOnAction(e1 -> window.close());
                    Button button2 = new Button("OK");
                    TextField notification = new TextField();
                    notification.setPromptText("Entrez numero de la question pour ajouter");
                    notification.clear();
                    button2.setOnAction((ActionEvent e2) -> {
                        String name_0 = qcm.getSelectionModel().getSelectedItems().get(0).getValue();
                        for(Qcm q : qcmList) {
                            if (name_0 == q.getName()){
                                try {
                                    Question new_q = new Question(superBank.find(notification.getText()));
                                    q.addQuestion(new_q);
                                }catch(WrongQuestionTypeException e3){
                                    e3.printStackTrace();
                                }
                                q.save();
                            }
                        }
                        initBanksAndQcms(superBank);
                        window.close();
                    });
                    Label label = new Label("Entrez le numero");
                    VBox layout = new VBox(10);
                    HBox hbox = new HBox();
                    hbox.setSpacing(10);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.getChildren().addAll(button2,button);
                    layout.getChildren().addAll(label, notification, hbox);
                    layout.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(layout);
                    window.setScene(scene);
                    window.showAndWait();
                }
                else{
                    afficherError("Il faut choisir une qcm pour y ajouter une question!");
                }
            }
        });
        MenuItem menuItemQcm6 = new MenuItem("Supprimer une question");
        menuItemQcm6.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        menuItemQcm6.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                TreeItem it = qcm.getSelectionModel().getSelectedItems().get(0);
                if(it instanceof TreeItemWithQuestion) {
                    Stage window = new Stage();
                    window.setTitle("Supprimer cette Question");
                    window.initModality(Modality.APPLICATION_MODAL);
                    window.setMinWidth(300);
                    window.setMinHeight(150);
                    Button button = new Button("Non");
                    button.setOnAction(e1 -> window.close());
                    Button button2 = new Button("Oui");
                    button2.setOnAction((ActionEvent e2) -> {
                        Question q = ((TreeItemWithQuestion) it).getQuestion();
                        TreeItem parent = it.getParent();
                        if(parent instanceof TreeItemWithQcmAndBank){
                            Qcm qcm=((TreeItemWithQcmAndBank) parent).getQcm();
                            qcm.deleteQuestion(q);
                            qcm.save();
                        }
                        displayQcms();
                        window.close();
                    });
                    Label label = new Label("Vous voulez supprimer cette question");
                    VBox layout = new VBox(10);
                    HBox hbox = new HBox();
                    hbox.setSpacing(10);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.getChildren().addAll(button2,button);
                    layout.getChildren().addAll(label,hbox);
                    layout.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(layout);
                    window.setScene(scene);
                    window.showAndWait();
                }
                else{
                    afficherError("Il faut choisir une question pour le supprimer!");
                }
            }
        });

        qcm.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                TreeItem it = qcm.getSelectionModel().getSelectedItems().get(0);
                if (it instanceof TreeItemWithQuestion){
                    contextMenuQcm.getItems().setAll(menuItemQcm6);
                }
                else if(it instanceof TreeItemWithQcmAndBank){
                    contextMenuQcm.getItems().setAll(menuItemQcm2,menuItemQcm3,menuItemQcm4,menuItemQcm5);
                }
                else{
                    contextMenuQcm.getItems().setAll(menuItemQcm0,menuItemQcm1);
                }
            }
        });


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

        answers_box.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    answerFieldsInit(current_question.getAnswerByIndex((int) newValue));
                }catch(IndexOutOfBoundsException e) {
                    // TODO : s'occuper de ça? (Est-ce un gros problème?)
                }
            }
        });


    }

    private void selectQuestion(Question question) {
        current_question = question;
        question.load(superBank.find(String.valueOf(question.getID())));
        questionFieldsInit(question);
    }

    public void clickOnItem(MouseEvent mouseEvent) throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {
        if(tree.getSelectionModel().getSelectedItems().get(0) instanceof TreeItemWithQuestion) {
            TreeItemWithQuestion<String> treeItem = (TreeItemWithQuestion<String>) tree.getSelectionModel().getSelectedItems().get(0);
            if (treeItem != null) {
                if (treeItem.getQuestion() != null) {
                    selectQuestion(treeItem.getQuestion());
                }
            }
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




    private final class TextFieldTreeCellImpl extends TreeCell<String>{
        private TextField textField;


        public TextFieldTreeCellImpl(SuperBank superbank) {
            this.setOnDragDetected((MouseEvent event)-> dragDetectedInBank(event, this));
            this.setOnDragDropped((DragEvent event) -> dragDrop(event, this, superbank));
            this.setOnDragOver((DragEvent event) -> dragOver(event, this));
            this.setOnDragExited((DragEvent event)->mouseExit(event, this));
        }

        @Override
        public void startEdit() {
            TreeItem it = getTreeItem();
            if ((it instanceof TreeItemWithQcmAndBank)&&(!(it instanceof TreeItemWithQuestion))) {
                super.startEdit();
                if (textField == null) {
                    createTextField();
                }
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText((String) getItem());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    TreeItem it = getTreeItem();
                    if ((t.getCode() == KeyCode.ENTER)&&(it instanceof TreeItemWithQcmAndBank)&&(textField.getText().length()>0)) {
                        commitEdit(textField.getText());
                        Bank bk=((TreeItemWithQcmAndBank) it).getBank();
                        Qcm qc=((TreeItemWithQcmAndBank) it).getQcm();
                        if(bk!=null){
                            bk.changeName(textField.getText());
                            deleteFile(bk.getPath());
                            bk.changeName(textField.getText());
                            bk.changePath(sys_bank_path+textField.getText()+".xml");
                            bk.save();
                        }
                        if(qc!=null){
                            qc.changeName(textField.getText());
                            deleteFile(qc.getPath());
                            qc.changeName(textField.getText());
                            qc.changePath(sys_bank_path+textField.getText()+".xml");
                            qc.save();
                        }
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }

    }

    private final class TextFieldTreeCellImplForSuperBank extends TreeCell<String>{
        private TextField textField;

        public TextFieldTreeCellImplForSuperBank() {
                this.setOnDragDetected((MouseEvent event) -> dragDetected(event, this));
                this.setOnDragDone((DragEvent event)->dragDone(event, this));
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    setText(null);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }

    }


    private void dragDetected(MouseEvent event, TreeCell treeCell) {
        TreeItem draggedItem = treeCell.getTreeItem();

        if(draggedItem instanceof TreeItemWithQuestion) {
            if(((TreeItemWithQuestion) draggedItem).getQuestion()!=null) {
                System.out.println("hello"+draggedItem.getValue());
                Dragboard db = treeCell.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(((TreeItemWithQuestion) draggedItem).getQuestion().getID() + "");
                System.out.println(((TreeItemWithQuestion) draggedItem).getQuestion().getID());
                db.setContent(content);
            }
        }
        event.consume();
    }

    private void dragDone(DragEvent event, TreeCell treeCell){
        if (event.getTransferMode() == TransferMode.MOVE) {
            treeCell.setText("");
        }
    }

    private void dragOver(DragEvent event, TreeCell treeCell){
        if (treeCell.getTreeItem() instanceof TreeItemWithQcmAndBank && event.getDragboard().hasString() && !(treeCell.getTreeItem() instanceof TreeItemWithQuestion)) {
            event.acceptTransferModes(TransferMode.ANY);
            treeCell.getTreeItem().setValue("+++++++++++++");
        }
        event.consume();
    }

    private void dragDrop(DragEvent event, TreeCell treeCell, SuperBank superbank) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        TreeItem it = treeCell.getTreeItem();
        if (db.hasString() && it instanceof TreeItemWithQcmAndBank && !(it instanceof TreeItemWithQuestion)) {
            if(((TreeItemWithQcmAndBank) it).getQcm()==null){
                Bank b=((TreeItemWithQcmAndBank) it).getBank();
                try {
                    b.addQuestion(new Question(superbank.find(db.getString())));
                }catch(WrongQuestionTypeException e){
                    e.printStackTrace();
                }
                b.save();
                displayBanks();
            }
            else{
                Qcm q=((TreeItemWithQcmAndBank) it).getQcm();
                try {
                    q.addQuestion(new Question(superbank.find(db.getString())));
                }catch(WrongQuestionTypeException e){
                    e.printStackTrace();
                }
                q.save();
                displayQcms();
            }
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void mouseExit(DragEvent event, TreeCell treeCell){
        TreeItem it = treeCell.getTreeItem();
        if(!(it instanceof TreeItemWithQuestion) && it instanceof TreeItemWithQcmAndBank) {
            if (((TreeItemWithQcmAndBank) it).getQcm() == null){
                treeCell.getTreeItem().setValue(((TreeItemWithQcmAndBank) it).getBank().getName());
            }
            else {
                treeCell.getTreeItem().setValue(((TreeItemWithQcmAndBank) it).getQcm().getName());
            }
        }
        event.consume();
    }

    private void dragDetectedInBank(MouseEvent event, TreeCell treeCell) {
        TreeItem draggedItem = treeCell.getTreeItem();

        if(draggedItem instanceof TreeItemWithQuestion) {
            if(((TreeItemWithQuestion) draggedItem).getQuestion()!=null) {
                System.out.println("helloInBank"+draggedItem.getValue());
                Dragboard db = treeCell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(((TreeItemWithQuestion) draggedItem).getQuestion().getID() + "");
                System.out.println(((TreeItemWithQuestion) draggedItem).getQuestion().getID());
                db.setContent(content);
            }
        }
        event.consume();
    }

}




