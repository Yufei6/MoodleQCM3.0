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
import java.io.IOException;

public class Bank extends QuestionStorage{
//    private static int name_default_nomber = 0;
//    private static int number_default = 0;



    public Bank(){
        super();
        //path = "./Bank_default"+number_default+".xml";
//        changeName("Bankdefaut" + name_default_nomber);
//        name_default_nomber++;
    }

    public Bank(String path, String new_name, SuperBank sb){
        super(path,new_name,sb);
    }

    public Bank(String path, SuperBank super_bank){
        super(path, super_bank);
    }

    public void save(){
        super.save(true);
    }


    public static Bank Import(String xml_path, String new_name, SuperBank super_bank0){
        String bank_dir_path = "./target/Bank/";
        Bank new_bank = new Bank(bank_dir_path+new_name, new_name,super_bank0);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document= builder.parse(new File(xml_path));
            Element racine = document.getDocumentElement();

            final NodeList list_Id = racine.getElementsByTagName("question_list");
            final int nbIDsElements = list_Id.getLength();
            for(int i =  0; i<nbIDsElements; i++) {
                final Element question = (Element) list_Id.item(i);
                Question new_question = new Question(question,super_bank0);
                new_bank.addQuestion(new_question);
            }
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new_bank;
    }

    public void Export(String xml_path, String name_for_xml){
        super.Export(xml_path, name_for_xml, true);
    }


    public TreeItemWithQcmAndBank<String> createQuestionTree(TreeItemWithQcmAndBank<String> root){
        for(Question q : super.list_question){
            TreeItemWithQuestion<String> treeItem = new TreeItemWithQuestion<String>(q.getName(),q);
            root.getChildren().addAll(treeItem);
        }
        return root;
    }



}
