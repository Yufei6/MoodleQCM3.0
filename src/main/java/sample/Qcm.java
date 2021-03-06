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

public class Qcm extends QuestionStorage {

    public Qcm(){
        super();
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



    public static Qcm Import(String xml_path, SuperBank super_bank0){
        String new_name=xml_path.substring(xml_path.lastIndexOf("/")+1,xml_path.lastIndexOf("."));
        new_name.trim();
        int slash_pos = xml_path.lastIndexOf("/")+1;
        if (slash_pos == 0) {
            slash_pos = xml_path.lastIndexOf("\\")+1;
            new_name=xml_path.substring(slash_pos,xml_path.lastIndexOf("."));
        }
        String qcm_dir_path = "./target/Qcm/";
        Qcm new_qcm = new Qcm(qcm_dir_path + new_name +".xml", new_name, super_bank0);
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
                int ret = super_bank0.addQuestion(new_question);
                System.out.println("Here");
                if (ret == -1) {
                    System.out.println("Question name already exists");
                    continue;
                }
                try {
                    new_qcm.addQuestion(super_bank0.findQuestion("" + ret));
                }catch (WrongQuestionTypeException e) {

                }
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

    public List<String> Export(String xml_path, String name_for_xml){
        return super.Export(xml_path, name_for_xml, false);
    }


    public TreeItemWithQcmAndBank<String> createQuestionTree(TreeItemWithQcmAndBank<String> root){
        for(Question q : super.list_question){
            TreeItemWithQuestion<String> treeItem = new TreeItemWithQuestion<String>(q.getName().trim(),q);
            root.getChildren().addAll(treeItem);
        }
        return root;
    }


}
