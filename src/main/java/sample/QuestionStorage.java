package sample;


import javafx.scene.control.TreeItem;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;


public abstract class QuestionStorage{
    protected Set<Question> list_question;
    private String name, path;
    public SuperBank super_bank;

    public QuestionStorage(){
        list_question = new HashSet<Question>();
        name = "Error QS";
    }

    public QuestionStorage(String xml_path, String name_0, SuperBank super_bank_0){
        path = xml_path;
        name = name_0;
        super_bank = super_bank_0;
        list_question = new HashSet<Question>();
    }

    public QuestionStorage(String xml_path, SuperBank super_bank_0){
        path = xml_path;
        super_bank = super_bank_0;
        list_question = new HashSet<Question>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document= builder.parse(new File(xml_path));
            Element racine = document.getDocumentElement();
            Element name_0 = (Element)racine.getElementsByTagName("name").item(0);

            name = name_0.getTextContent();
            final NodeList list_Id = racine.getElementsByTagName("question_id");
            final int nbIDsElements = list_Id.getLength();
            for(int i =  0; i<nbIDsElements; i++) {
                final Element Id = (Element) list_Id.item(i);
                Question new_question = new Question(super_bank.find(Id.getTextContent()));
                list_question.add(new_question);
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
        } catch (WrongQuestionTypeException e) {
            e.printStackTrace();
        }
    }

    private String latexEscape(String to_escape) {
        char[] to_escape_array = to_escape.toCharArray();
        String escaped = "";
        char[] specials = {'&', '%', '$', '#', '_', '{', '}'};
        for (Character c : to_escape_array) {
            if (c == '~') {
                escaped += "\\textasciitilde";
                continue;
            }
            if (c == '^') {
                escaped += "\\textasciicircum";
                continue;
            }
            if (c == '\\') {
                escaped += "\\textbackslash";
                continue;
            }
            if (Arrays.asList(specials).contains(c)) {
                escaped += '\\';
            }
            escaped += c;
        }
        return escaped;
    }

    public String getLatex() {
        String latex = "\\documentclass{article}\\usepackage[utf8]{inputenc}";
        latex += "\\title{" + name + "}\\date{}\\begin{document}\\maketitle\\centerline{Nom:\\hspace{15em}Prenom:}\\vskip5em";
        for (Question q : list_question) {
            q.load(super_bank.find(""+q.getID()));
            latex += "\\section{" + latexEscape(q.getQuestiontext().replaceAll("\\<[^>]*>","").trim()) + "}";
            for (Answer a : q.getAnswers()) {
                latex += "- " + latexEscape(a.getText().replaceAll("\\<[^>]*>","").trim()) + "\\newline";
            }
        }
        latex += "\\end{document}";
        return latex;
    }




    public void save(boolean isBank){
        System.out.println("cccc"+this.path);
        File file = new File(this.path);
        if(!file.exists()){
            try {
                file.createNewFile();
                System.out.println("dddd"+this.path);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.newDocument();
            Element racine = null;
            Comment commentaire = null;
            if(isBank){
                racine = document.createElement("Bank");
                commentaire = document.createComment("Question Bank");
            }
            else{
                racine = document.createElement("Qcm");
                commentaire = document.createComment("Question Qcm");
                System.out.println("eeee"+this.path);

            }
            document.appendChild(racine);
            racine.appendChild(commentaire);
            final Element name_0 = document.createElement("name");
            final Element question_id_list = document.createElement("question_id_list");
            racine.appendChild(name_0);
            racine.appendChild(question_id_list);
            name_0.appendChild(document.createTextNode(name));

            for (Question q:list_question) {
                final Element question_id = document.createElement("question_id");
                question_id_list.appendChild(question_id);
                question_id.appendChild(document.createTextNode(q.getID()+""));
            }
            Calendar c = Calendar.getInstance();
            final Element date = document.createElement("date");
            racine.appendChild(date);
            date.appendChild(document.createTextNode(c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH)+1+"/"+c.get(Calendar.YEAR)));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult sortie = new StreamResult(file);
            transformer.setOutputProperty(OutputKeys.VERSION,"1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(source, sortie);
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    public List<String> ExportLatex(String tex_path, String name_for_tex) {
        List<String> invalid = new ArrayList<>();
        String path = tex_path+name_for_tex+".tex";
        for (Question q:list_question) {
            q.load(super_bank.find("" + q.getID()));
            if (!q.isValid()) {
                invalid.add(q.getName());
            }
        }
        if (invalid.size() > 0) {
            return invalid;
        }
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), "utf-8"));
            writer.write(getLatex());
        } catch (IOException ex) {
            // Report
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
        return invalid;
    }


    public List<String> Export(String xml_path, String name_for_xml, boolean isBank){
        List<String> invalid = new ArrayList<>();
        File file = new File(xml_path+name_for_xml+".xml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.newDocument();
            Element racine = null;
            Comment commentaire = null;
            if(isBank){
                racine = document.createElement("quiz");
                commentaire = document.createComment("Question Bank");
            }
            else{
                racine = document.createElement("quiz");
                commentaire = document.createComment("Question Qcm");
            }
            document.appendChild(racine);
            racine.appendChild(commentaire);

            for (Question q:list_question) {
                q.load(super_bank.find(""+q.getID()));
                if (!q.isValid()) {
                    invalid.add(q.getName());
                }
                Comment commentaire_question = null;
                commentaire_question = document.createComment(" question: "+q.getID()+" ");
                racine.appendChild(commentaire_question);
                racine.appendChild(q.getQuestionXml(document));
            }

            if (invalid.size() > 0) {
                return invalid;
            }

            Calendar c = Calendar.getInstance();
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource source = new DOMSource(document);
            final StreamResult sortie = new StreamResult(file);
            transformer.transform(source, sortie);

        }
        catch (final ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        catch (TransformerException e) {
            e.printStackTrace();
        }

        return invalid;

    }

    public void addQuestion(Question question){
        list_question.add(question);
    }

    public void deleteQuestion(Question question){
        list_question.remove(question);
    }

    public void changeName(String name_0){
        name = name_0;
    }

    public void changePath(String new_path){
        path = new_path;
    }

    public String getName(){
        return name;
    }


    public String getPath(){
        return path;
    }


    public int getQuestionQuantite(){
        return list_question.size();
    }
}
