package sample;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class AppTest 
{
    @Test
    public void littleTestSimple(){
        try {
            SuperBank sb = new SuperBank();
            sb.generateTreeWithQuestion();
            Qcm qcm1 = new Qcm("./target/Superbank/question3.xml", sb);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (WrongQuestionTypeException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
}
