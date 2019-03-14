package sample;

import javafx.scene.control.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;


public class SuperBank {
    private File dirBank;
    private ArrayList<String[]> questionList;
    private DocumentBuilder builder;
    private static int maxId=0;
    private ArrayList<Question> questions;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public SuperBank() throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {
        dirBank =new File("./target/Superbank");
        questionList = new ArrayList<>();
        questions = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        //extractId_Path();
        //loadPartQuestion();

    }


    public File getDirBank() {
        return dirBank;
    }



    public boolean havefiles() {
        return dirBank.listFiles().length != 0;
    }
    private boolean havefiles(File dir){
        return dir.listFiles().length != 0;
    }

    public ArrayList extractId_Path() throws IOException, SAXException {
        return extractId_Path(dirBank);

    }
    public int generateId(){
        maxId++;
        return maxId;
    }

    public ArrayList extractPath_Name_Question() {
        return extractNamePath(dirBank);
    }

    private ArrayList extractNamePath(File file){
        if (!havefiles(file)) return null;
        for (File dir : file.listFiles()){
            if (dir.isDirectory()) extractNamePath(dir);
            if (isXmlFile(dir)){
                String[] strings= extractNameQuestion(dir);
                if (strings != null){
                    questionList.add(strings);
                }
            }
        }
        return questionList;
    }

    private String[] extractNameQuestion(File file1) {
        String[] strings = new String[2];
        try {
            Question question = new Question(file1.getPath());
            strings[0]= String.valueOf(question.getID());
            strings[1]=file1.getPath();
        } catch (WrongQuestionTypeException e) {
            e.printStackTrace();
            return null;
        }
        return strings;

    }

    private ArrayList extractId_Path(File dirBank) throws IOException, SAXException {
        if (!havefiles(dirBank)) return null;
        for (File dir : dirBank.listFiles()){
            if (dir.isDirectory()) extractId_Path(dir);
            if (isXmlFile(dir)){
                if (extractQuestion(dir) != null){
                    questionList.add(extractQuestion(dir));
                }
            }
        }
        return questionList;
    }

    public boolean isXmlFile(File file) {
        return file.isFile() && (file.getName().contains("xml"));
    }

    private ArrayList<String[]> getQuestionList() {
        return questionList;
    }

    public int addQuestion(String path) {
        int new_id = generateId();
        String[] new_question_entry = {""+new_id, path};
        questionList.add(new_question_entry);
        return new_id;
    }

    public int addQuestion(Question question) {
        int new_id = generateId();
        String q_path  = "./target/Superbank/" + question.getName() + ".xml";
        String[] new_question_entry = {""+new_id, q_path};
        questionList.add(new_question_entry);
        question.setId(new_id);
        question.save(q_path);
        return new_id;
    }

    public String[] extractQuestion(File file) throws IOException, SAXException {
        String[] strings=new String[2];
        Element nodeId_Header;
        Document document = builder.parse(file);
        Element element = document.getDocumentElement();
        NodeList nodeList = element.getChildNodes();
        if (Objects.equals(nodeList.item(1).getNodeName(), "id_header")) {
            nodeId_Header = (Element) nodeList.item(1);
            strings[0]= nodeId_Header.getElementsByTagName("id").item(0).getTextContent();
            if (maxId <= Integer.parseInt(strings[0])) maxId = Integer.parseInt(strings[0]);
            strings[1]=file.getCanonicalPath();
            return strings;
        }
        return null;
    }


    public String find(String s) {
        for (String[] strings : getQuestionList()){
            if (s.equals(strings[0])){
                return strings[1];
            }
        }
        return null;
    }


    public Question findQuestion(String id) throws WrongQuestionTypeException {
        for(Question q : questions){
            if(q.getID()+""==id){
                return q;
            }
        }
        return null;
    }



    public TreeItemWithRepertoire<String> generateTreeWithQuestion() throws IOException, SAXException, WrongQuestionTypeException {
        TreeItemWithRepertoire root = new TreeItemWithRepertoire("[SuperBank]", dirBank.getPath());
        root.setExpanded(true);
        for(File file : dirBank.listFiles()){
            if (file.isDirectory()){
                root.getChildren().addAll(generateItem(file));
            } else {
                if(isXmlFile(file)) {
                    Question question = new Question(file.getPath());
                    if (question.getName() != null) {
                        questions.add(question);
                        TreeItemWithQuestion<String> treeItem = new TreeItemWithQuestion<>(question.getName(), question);
                        root.getChildren().addAll(treeItem);
                    }
                }
            }
        }
        return root;
    }

    private TreeItemWithRepertoire<String> generateItem(File file) throws IOException, SAXException, WrongQuestionTypeException {
        TreeItemWithRepertoire<String> treeItem = new TreeItemWithRepertoire("["+file.getName()+"]",file.getPath());

        for (File file1 : file.listFiles()) {
            if (file1.isDirectory()){
                treeItem.getChildren().addAll(generateItem(file1));
            } else {
                Question question = new Question(file1.getPath());
                if (question != null && isXmlFile(file1)) {
                    questions.add(question);
                    TreeItemWithQuestion<String> treeItem1 = new TreeItemWithQuestion<>(question.getName(), question);
                    treeItem.getChildren().addAll(treeItem1);
                }
            }
        }
        return treeItem;
    }
    public File addNewDirectory(final String directory, String nameFile){
        if (directory.equals(dirBank.getName())){
            File file = new File("bank/"+nameFile);
            return file;

        }
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.equals(directory)){
                    return true;
                }
                return false;
            }
        };

        File[] paths;
        paths = dirBank.listFiles(filter);
        File[] files = dirBank.listFiles();
        while(paths.length==0){
            for (File file : files){
                if (file.isDirectory()) paths = findChildren(file,filter);
            }
        }
        File fileToAdd = paths[0];
        fileToAdd = new File(fileToAdd.getParentFile().getPath()+"/"+nameFile);
        fileToAdd.mkdir();
        return fileToAdd;
    }

    private File[] findChildren(File file,FilenameFilter filter) {
        File[] files;
        if(file.listFiles().length==0) return null;
        files = file.listFiles(filter);
        if (files.length!=0) return files;
        for (File file1 : file.listFiles()){
            String s = file1.getName();
            files = file1.listFiles(filter);
            if (files.length !=0) return files;
            findChildren(file1,filter);
        }
        return null;
    }




}
