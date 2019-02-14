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

public class Qcm extends QuestionStorage {
    private String path;
//     private static int name_default_nomber = 0;

    public Qcm(){
        super();
//         changeName("Qcmdefaut" + name_default_nomber);
//         name_default_nomber++;
    }

    public Qcm(String path, String new_name, SuperBank sb){
        super(path,new_name,sb);
    }

    public Qcm(String path, SuperBank super_bank){
        super(path, super_bank);
    }


    public void save(){
        super.save(false);
    }



    public static Qcm Import(String xml_path, String new_name, SuperBank super_bank0){
        String qcm_dir_path = "./target/Qcm/";
        Qcm new_qcm = new Qcm(qcm_dir_path + new_name , new_name, super_bank0);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document= builder.parse(new File(xml_path));
            Element racine = document.getDocumentElement();

            final NodeList list_Id = racine.getElementsByTagName("question");
            final int nbIDsElements = list_Id.getLength();
            for(int i =  0; i<nbIDsElements; i++) {
                final Element question = (Element) list_Id.item(i);
                Question new_question = new Question(question ,super_bank0);
                new_qcm.addQuestion(new_question);
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
        new_qcm.save();
        return new_qcm;
    }

    public void Export(String xml_path, String name_for_xml){
        super.Export(xml_path, name_for_xml, false);
    }


    public TreeItemWithQcmAndBank<String> createQuestionTree(TreeItemWithQcmAndBank<String> root){
        for(Question q : super.list_question){
            TreeItemWithQuestion<String> treeItem = new TreeItemWithQuestion<String>(q.getName(),q);
            root.getChildren().addAll(treeItem);
        }
        return root;
    }


    public String getPath() {
        return path;
    }
}
