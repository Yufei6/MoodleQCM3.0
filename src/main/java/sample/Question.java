package sample;


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
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Question {

    private boolean loaded;

    private int id;

    private boolean single;
    private boolean shuffleanswers;
    private int hidden;
    private String name, questiontext, generalfeedback, correctfeedback, partiallycorrectfeedback, incorrectfeedback;
    private String qt_format, gf_format, cf_format, pcf_format, if_format;
    private String answernumbering;
    private double defaultgrade;
    private double penalty;

    private List<Answer> answers;

    private Map<String, String> answer_numbering_map;

    private Question() {
        answer_numbering_map_init();
    }

    public Question(Element question, SuperBank s_bank) {
        this();
        id = s_bank.generateId();
        load_from_element(question);
        loaded = true;
    }

    public Question(String path) throws WrongQuestionTypeException {
        this();
        loaded = false;
        init(path);
    }

    public Question(String name, int id) {
        this.name = name;
        this.id = id;
    }

    private void answer_numbering_map_init() {
        answer_numbering_map = new HashMap<>();
        answer_numbering_map.put("abc", "a, b, c");
        answer_numbering_map.put("ABC", "A, B, C");
        answer_numbering_map.put("123", "1, 2, 3");
        answer_numbering_map.put("iii", "i, ii, iii");
        answer_numbering_map.put("III", "I, II, III");
        answer_numbering_map.put("none", "Sans Numérotation");
    }

    public Answer getAnswerByIndex(int index) {
        if (index >= answers.size()) {
            return null;
        }
        return answers.get(index);
    }

    public void addAnswer(Answer new_answer) {
        answers.add(new_answer);
    }

    public void removeAnswer(int index) {
        if (index >= answers.size()) {
            return;
        }
        answers.remove(index);
    }

    public ArrayList<String> getAnswerNumberingChoices() {
        return new ArrayList<>(answer_numbering_map.values());
    }

    public String getAnswerNumberingDisplay() {
        return answer_numbering_map.get(answernumbering);
    }

    public ArrayList<String> getAnswersDisplay() {
        ArrayList<String> display = new ArrayList<>();
        int index = 0;
        for (Answer a : answers) {
            index++;
            display.add("" + index + " [" + a.getFraction() + "%]");
        }
        return display;
    }

    public String toString() {
        if (!loaded) {
            return "Name: " + name + " Id: " + Integer.toString(id);
        }
        String str = "";
        str += "id: " + id + " single: " + single + " shuffleanswers: " + shuffleanswers + " hidden: " + hidden + "\n";
        str += name + "\n";
        str += questiontext + "\n";
        str += generalfeedback + "\n";
        str += correctfeedback + "\n";
        str += partiallycorrectfeedback + "\n";
        str += incorrectfeedback + "\n";
        str += answernumbering + "\n";
        str += defaultgrade + "\n";
        str += penalty + "\n";
        int i = 0;
        for (Answer a : answers) {
            i++;
            str += "Answer [" + i + "] :" + "\n";
            str += a.getText() + "\n";
            str += a.getTextFormat() + "\n";
            str += a.getFeedback() + "\n";
            str += a.getFeedbackFormat() + "\n";
            str += a.getFraction() + "\n";
        }
        return str;
    }

    public Element getQuestionXml(Document document) {

        Element root = null;

            /////////////////////
            root = document.createElement("question");
            root.setAttribute("type", "multichoice");

             Element x_name = document.createElement("name");
             root.appendChild(x_name);
             Element x_name_text = document.createElement("text");
             x_name.appendChild(x_name_text);
             x_name_text.appendChild(document.createTextNode(name));

            Element x_questiontext = document.createElement("questiontext");
            x_questiontext.setAttribute("format", qt_format);
            Element questiontext_content = document.createElement("text");
            questiontext_content.appendChild(document.createCDATASection(questiontext));
            x_questiontext.appendChild(questiontext_content);
            root.appendChild(x_questiontext);   //Write QuestionText

            Element x_generalfeeback = document.createElement("generalfeedback");
            x_generalfeeback.setAttribute("format", gf_format);
            Element generalfeedback_content = document.createElement("text");
            generalfeedback_content.appendChild(document.createCDATASection(generalfeedback));
            x_generalfeeback.appendChild(generalfeedback_content);
            root.appendChild(x_generalfeeback);  //Write GeneralFeedback

            Element x_correctfeeback = document.createElement("correctfeedback");
            x_correctfeeback.setAttribute("format", cf_format);
            Element correctfeedback_content = document.createElement("text");
            correctfeedback_content.appendChild(document.createCDATASection(correctfeedback));
            x_correctfeeback.appendChild(correctfeedback_content);
            root.appendChild(x_correctfeeback);  //Write CorrectFeedback

            Element x_partiallycorrectfeeback = document.createElement("partiallycorrectfeedback");
            x_partiallycorrectfeeback.setAttribute("format", pcf_format);
            Element partiallycorrectfeedback_content = document.createElement("text");
            partiallycorrectfeedback_content.appendChild(document.createCDATASection(partiallycorrectfeedback));
            x_partiallycorrectfeeback.appendChild(partiallycorrectfeedback_content);
            root.appendChild(x_partiallycorrectfeeback);  //Write PartiallyCorrectFeedback

            Element x_incorrectfeeback = document.createElement("incorrectfeedback");
            x_incorrectfeeback.setAttribute("format", if_format);
            Element incorrectfeedback_content = document.createElement("text");
            incorrectfeedback_content.appendChild(document.createCDATASection(incorrectfeedback));
            x_incorrectfeeback.appendChild(incorrectfeedback_content);
            root.appendChild(x_incorrectfeeback);  //Write InCorrectFeedback


            Element defaultgrade_content = document.createElement("defaultgrade");
            defaultgrade_content.appendChild(document.createTextNode(Double.toString(defaultgrade)));
            root.appendChild(defaultgrade_content);

            Element penalty_content = document.createElement("penalty");
            penalty_content.appendChild(document.createTextNode(Double.toString(penalty)));
            root.appendChild(penalty_content);

            Element hidden_content = document.createElement("hidden");
            hidden_content.appendChild(document.createTextNode(Integer.toString(hidden)));
            root.appendChild(hidden_content);

            Element single_content = document.createElement("single");
            single_content.appendChild(document.createTextNode(Boolean.toString(single)));
            root.appendChild(single_content);

            Element answernumbering_content = document.createElement("answernumbering");
            answernumbering_content.appendChild(document.createTextNode(answernumbering));
            root.appendChild(answernumbering_content);

            Element shuffleanswers_content = document.createElement("shuffleanswers");
            shuffleanswers_content.appendChild(document.createTextNode(Boolean.toString(shuffleanswers)));
            root.appendChild(shuffleanswers_content);

            for (Answer ans : answers) {
                Element answer = document.createElement("answer");
                answer.setAttribute("fraction", Double.toString(ans.getFraction()));
                answer.setAttribute("format", ans.getTextFormat());
                Element text = document.createElement("text");
                text.appendChild(document.createCDATASection(ans.getText()));
                answer.appendChild(text);
                Element feedback = document.createElement("feedback");
                feedback.setAttribute("format", ans.getFeedbackFormat());
                Element f_text = document.createElement("text");
                f_text.appendChild(document.createCDATASection(ans.getFeedback()));
                feedback.appendChild(f_text);
                answer.appendChild(feedback);

                root.appendChild(answer);
            }


        return root;
    }

    public void save(String xml_path) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("question_data");
            document.appendChild(root);
            Element x_header = document.createElement("id_header");
            root.appendChild(x_header);

            Element x_id = document.createElement("id");
            x_header.appendChild(x_id);
            x_id.appendChild(document.createTextNode(Integer.toString(id)));

            Element x_name = document.createElement("name");
            x_header.appendChild(x_name);
            Element x_name_text = document.createElement("text");
            x_name.appendChild(x_name_text);
            x_name_text.appendChild(document.createTextNode(name));

            Element x_body = getQuestionXml(document);
            root.appendChild(x_body);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult sortie = new StreamResult(new File(xml_path));
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

    private void init(String xml_path) throws WrongQuestionTypeException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xml_path));
            Element root = document.getDocumentElement();
            Element id_header = (Element) root.getElementsByTagName("id_header").item(0);

            name = ((Element)id_header.getElementsByTagName("name").item(0)).getElementsByTagName("text").item(0).getTextContent(); // Init name
            id = Integer.parseInt(id_header.getElementsByTagName("id").item(0).getTextContent());  //Init ID

            final Element x_question = (Element) document.getElementsByTagName("question").item(0);
            if (!x_question.getAttribute("type").equals("multichoice")) {
                throw new WrongQuestionTypeException();
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load_from_element(Element x_question) {
        Element x_questiontext = (Element) x_question.getElementsByTagName("questiontext").item(0);
        qt_format = x_questiontext.getAttribute("format");     // Init qt_format
        questiontext = x_questiontext.getElementsByTagName("text").item(0).getTextContent();  // Init questiontext

        Element x_generalfeeback = (Element) x_question.getElementsByTagName("generalfeedback").item(0);
        gf_format = x_generalfeeback.getAttribute("format");     // Init gf_format
        generalfeedback = x_generalfeeback.getElementsByTagName("text").item(0).getTextContent();  // Init generalfeedback

        Element x_correctfeedback = (Element) x_question.getElementsByTagName("correctfeedback").item(0);
        cf_format = x_correctfeedback.getAttribute("format");     // Init cf_format
        correctfeedback = x_correctfeedback.getElementsByTagName("text").item(0).getTextContent();  // Init correctfeedback

        Element x_partiallycorrectfeedback = (Element) x_question.getElementsByTagName("partiallycorrectfeedback").item(0);
        pcf_format = x_partiallycorrectfeedback.getAttribute("format");     // Init pcf_format
        partiallycorrectfeedback = x_partiallycorrectfeedback.getElementsByTagName("text").item(0).getTextContent();  // Init partiallycorrectfeedback

        Element x_incorrectfeedback = (Element) x_question.getElementsByTagName("incorrectfeedback").item(0);
        if_format = x_incorrectfeedback.getAttribute("format");     // Init if_format
        incorrectfeedback = x_incorrectfeedback.getElementsByTagName("text").item(0).getTextContent();  // Init incorrectfeedback

        defaultgrade = Double.parseDouble(x_question.getElementsByTagName("defaultgrade").item(0).getTextContent());
        penalty = Double.parseDouble(x_question.getElementsByTagName("penalty").item(0).getTextContent());
        hidden = Integer.parseInt(x_question.getElementsByTagName("hidden").item(0).getTextContent());
        single = Boolean.parseBoolean(x_question.getElementsByTagName("single").item(0).getTextContent());
        answernumbering = x_question.getElementsByTagName("answernumbering").item(0).getTextContent();
        shuffleanswers = Boolean.parseBoolean(x_question.getElementsByTagName("shuffleanswers").item(0).getTextContent());

        answers = new ArrayList<>();
        NodeList x_answers = x_question.getElementsByTagName("answer");
        int answers_nb = x_answers.getLength();
        for (int ans = 0; ans < answers_nb; ans++) {
            Element answer = (Element) x_answers.item(ans);
            Double fraction = Double.parseDouble(answer.getAttribute("fraction"));
            String text = answer.getElementsByTagName("text").item(0).getTextContent();
            String t_format = answer.getAttribute("format");
            Element feedback = (Element) answer.getElementsByTagName("feedback").item(0);
            String f_format = feedback.getAttribute("format");
            String q_feedback = feedback.getElementsByTagName("text").item(0).getTextContent();
            answers.add(new Answer(fraction, text, t_format, q_feedback, f_format));
        }
    }

    public void load(String xml_path) {

        if (loaded) {
            return;
        }

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xml_path));

            Element root = document.getDocumentElement();

            Element x_question = (Element) document.getElementsByTagName("question").item(0);

            load_from_element(x_question);
        }
        catch(final ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch(final SAXException e) {
            e.printStackTrace();
        }
        catch(final IOException e) {
            e.printStackTrace();
        }

        loaded = true;
    }

    public int getID() {
        return id;
    }



    public boolean isLoaded() {
        return loaded;
    }

    //////////////// [ Getters ] ////////////////

    public boolean isSingle() {
        return single;
    }

    public boolean isShuffleanswers() {
        return shuffleanswers;
    }

    public int getHidden() {
        return hidden;
    }

    public String getName() {
        return name;
    }

    public String getQuestiontext() {
        return questiontext;
    }

    public String getGeneralfeedback() {
        return generalfeedback;
    }

    public String getCorrectfeedback() {
        return correctfeedback;
    }

    public String getPartiallycorrectfeedback() {
        return partiallycorrectfeedback;
    }

    public String getIncorrectfeedback() {
        return incorrectfeedback;
    }

    public String getQt_format() {
        return qt_format;
    }

    public String getGf_format() {
        return gf_format;
    }

    public String getCf_format() {
        return cf_format;
    }

    public String getPcf_format() {
        return pcf_format;
    }

    public String getIf_format() {
        return if_format;
    }

    public String getAnswernumbering() {
        return answernumbering;
    }

    public double getDefaultgrade() {
        return defaultgrade;
    }

    public double getPenalty() {
        return penalty;
    }

    //////////////// [ Setters ] ////////////////

    public void setId(int id) {
        this.id = id;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public void setShuffleanswers(boolean shuffleanswers) {
        this.shuffleanswers = shuffleanswers;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestiontext(String questiontext) {
        this.questiontext = questiontext;
    }

    public void setGeneralfeedback(String generalfeedback) {
        this.generalfeedback = generalfeedback;
    }

    public void setCorrectfeedback(String correctfeedback) {
        this.correctfeedback = correctfeedback;
    }

    public void setPartiallycorrectfeedback(String partiallycorrectfeedback) {
        this.partiallycorrectfeedback = partiallycorrectfeedback;
    }

    public void setIncorrectfeedback(String incorrectfeedback) {
        this.incorrectfeedback = incorrectfeedback;
    }

    public void setQt_format(String qt_format) {
        this.qt_format = qt_format;
    }

    public void setGf_format(String gf_format) {
        this.gf_format = gf_format;
    }

    public void setCf_format(String cf_format) {
        this.cf_format = cf_format;
    }

    public void setPcf_format(String pcf_format) {
        this.pcf_format = pcf_format;
    }

    public void setIf_format(String if_format) {
        this.if_format = if_format;
    }

    public void setAnswernumbering(String answernumbering) {
        for (Map.Entry<String, String> entry : answer_numbering_map.entrySet()) {
            if (entry.getValue().equals(answernumbering)) {
                this.answernumbering = entry.getKey();
                return;
            }
        }
        this.answernumbering = "abc";  //Défaut si problème
    }

    public void setDefaultgrade(double defaultgrade) {
        this.defaultgrade = defaultgrade;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Question other = (Question) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}

