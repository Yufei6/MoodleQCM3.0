package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Bank extends QuestionStorage{



    public Bank(){
        super();
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


    public static Bank Import(String xml_path, SuperBank super_bank0){
        String new_name=xml_path.substring(xml_path.lastIndexOf("/")+1,xml_path.lastIndexOf("."));
        new_name.trim();
        int slash_pos = xml_path.lastIndexOf("/") + 1;
        if (slash_pos == 0) {
            slash_pos = xml_path.lastIndexOf("\\") + 1;
            new_name=xml_path.substring(slash_pos,xml_path.lastIndexOf("."));
        }
        String bank_dir_path = "./target/Bank/";
        Bank new_bank = new Bank(bank_dir_path+new_name+".xml", new_name,super_bank0);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document= builder.parse(new File(xml_path));
            Element racine = document.getDocumentElement();

            final NodeList list_Id = racine.getElementsByTagName("question");
            final int nbIDsElements = list_Id.getLength();
            for(int i =  0; i<nbIDsElements; i++) {
                final Element question = (Element) list_Id.item(i);
                if (question.hasAttribute("type") && question.getAttribute("type").equals("category")) {
                    continue;
                }
                Question new_question = new Question(question);
                super_bank0.addQuestion(new_question);
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
        new_bank.save();
        return new_bank;
    }

    public List<String> Export(String xml_path, String name_for_xml){
        return super.Export(xml_path, name_for_xml, true);
    }


    public TreeItemWithQcmAndBank<String> createQuestionTree(TreeItemWithQcmAndBank<String> root){
        for(Question q : super.list_question){
            TreeItemWithQuestion<String> treeItem = new TreeItemWithQuestion<String>(q.getName().trim(),q);
            root.getChildren().addAll(treeItem);
        }
        return root;
    }



}
