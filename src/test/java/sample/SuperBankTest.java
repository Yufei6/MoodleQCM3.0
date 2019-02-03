package sample;

import javafx.scene.control.TreeItem;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class SuperBankTest {
    SuperBank superBank;
    @Before
    public void setUp() throws ParserConfigurationException, IOException, SAXException, WrongQuestionTypeException {
        superBank=new SuperBank();
    }
    @Test
    public void testBank(){
        System.out.println(superBank.getQuestions().get(1).getName());
        assertEquals(3,superBank.getQuestions().size());
    }
    @Test
    public void testBankAsDirectory(){
        assertEquals(true,superBank.getDirBank().isDirectory());
    }

    @Test
    public void testHaveDirectory(){
        assertEquals(false,superBank.havefiles());
    }
    @Test
    public void testIsXMLFile(){
        File fileXML = new File("bank/fich1.xml");
        assertEquals(true,superBank.isXmlFile(fileXML));
    }
    @Test
    public void testExtractQuestion() throws IOException, SAXException {
        String[] strings = new String[2];
        strings[0] = "655656";
        strings[1] = "C:\\Users\\Louis Berthier\\Desktop\\Informatique\\L3\\MoodleQCM\\MoodleQCM\\bank\\fich1.xml";
        File file = new File("bank/fich1.xml");
        assertEquals(strings[0],superBank.extractQuestion(file)[0]);
        assertEquals(strings[1],superBank.extractQuestion(file)[1]);

    }
    @Test
    public void testExtractId_Path() throws IOException, SAXException {
        String string = "655656";
        ArrayList arrayList = superBank.extractId_Path();
        String[] strings = (String[]) arrayList.get(0);
        assertEquals(string, strings[0]);
    }

    @Test
    public void testExtractID_PathSize() throws IOException,SAXException{
        assertEquals(3,superBank.extractId_Path().size());
    }

    @Test
    public void testFindId() throws IOException, SAXException {
        superBank.extractId_Path();
        assertEquals("C:\\Users\\Louis Berthier\\Desktop\\Informatique\\L3\\MoodleQCM\\MoodleQCM\\bank\\Maths\\question1.xml",superBank.find("655644"));

    }
    @Test
    public void testAddNewDir() {
        File file1 =superBank.addNewDirectory("question2.xml","test_1");
        File file = new File("bank/maths/carre/test_1");
        assertEquals(file1,file);

    }






}
