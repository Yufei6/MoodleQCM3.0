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
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Objects;


public class SuperBank {
    private File dirBank;
    private ArrayList<String[]> questionList;
    private DocumentBuilder builder;
    private int maxId=0;
    private ArrayList<Question> questions;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public SuperBank() throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {
        dirBank =new File("bank");
        questionList = new ArrayList<>();
        questions = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        extractId_Path();
        loadPartQuestion();

    }

    private void loadPartQuestion() throws WrongQuestionTypeException {
        ListIterator listIterator = questionList.listIterator();
        for (int i = 0; i < questionList.size(); i++) {
            questions.add(new Question(questionList.get(0)[1]));
        }

    }

    public File getDirBank() {
        return dirBank;
    }

    String newQuestion(Question question){
        return "bank/"+question.getID();
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
        return file.isFile() && file.getName().contains(".xml");
    }

    private ArrayList<String[]> getQuestionList() {
        return questionList;
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
            System.out.println("S : "+s);
            System.out.println("ID :"+ strings[0]);
            System.out.println("Path :"+ strings[1]);
            if (s.equals(strings[0])){
                return strings[1];
            }
        }
        return null;
    }
    public Question findQuestion(String s) throws WrongQuestionTypeException {
        return new Question(find(s));
    }

    public TreeItem<String> generateTree() throws IOException, SAXException {
        TreeItem<String> root = new TreeItem<>("bank");
        root.setExpanded(true);
        for(File file : dirBank.listFiles()){
            if (file.isDirectory()){
                root.getChildren().addAll(generateItem(file));
            }
            if (isXmlFile(file) && extractQuestion(file)!=null){
                String[] strings = extractQuestion(file);
                TreeItem<String> treeItem = new TreeItem<>(file.getName());
                root.getChildren().addAll(treeItem);
            }
        }
        return root;
    }

    private TreeItem<String> generateItem(File file) throws IOException, SAXException {
        TreeItem<String> treeItem = new TreeItem<>(file.getName());

        for (File file1 : file.listFiles()) {
            if (file1.isDirectory()){
                treeItem.getChildren().addAll(generateItem(file1));
            }
            if (isXmlFile(file1) && extractQuestion(file1)!=null){
                String[] strings= extractQuestion(file1);
                TreeItem<String> treeItem1 = new TreeItem<String>(file1.getName());
                treeItem.getChildren().addAll(treeItem1);
            }
        }
        return treeItem;
    }
    public File addNewDirectory(String directory,String nameFile){
        System.out.println("dirBank"+dirBank.getName());
        if (directory.equals(dirBank.getName())){
            File file = new File("bank/"+nameFile);
            System.out.println(file.getPath());
            System.out.println(file.mkdir());
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
        System.out.println(paths[0].getName());
        System.out.println(fileToAdd.getPath());
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
