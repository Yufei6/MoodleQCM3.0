package sample;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;

public class QcmTest {
    private SuperBank sb1;

    @Before
    public void setUp() throws javax.xml.parsers.ParserConfigurationException {
        sb1 = spy(new SuperBank());
    }


    @Test
    public void InitWithoutParamaterOK1() {
        Qcm b1 = new Qcm();
        assertEquals(b1.getName(), "Qcmdefaut0");
        Qcm b2 = new Qcm();
        assertEquals(b2.getName(), "Qcmdefaut1");
        Qcm b3 = new Qcm();
        assertEquals(b3.getName(), "Qcmdefaut2");
    }


    @Test
    public void InitWithParameterOK1() {
        when(sb1.find("100")).thenReturn("./target/Question/100.xml");
        when(sb1.find("200")).thenReturn("./target/Question/200.xml");
        when(sb1.find("300")).thenReturn("./target/Question/200.xml");
        Qcm b1 = new Qcm("./target/Qcm/Qcm_test_001.xml", sb1);
        assertEquals(b1.getName(), "Qcm_test_001");
    }


    @Test
    public void saveOK1() throws WrongQuestionTypeException {
        when(sb1.find("100")).thenReturn("./target/Question/100.xml");
        when(sb1.find("200")).thenReturn("./target/Question/200.xml");
        when(sb1.find("300")).thenReturn("./target/Question/300.xml");
        Qcm b1 = new Qcm("./target/Qcm/Qcm_test_001.xml", sb1);
        b1.save();
        Qcm b2 = new Qcm("./target/Qcm/Qcm_test_001.xml", sb1);
        assertEquals("Qcm_test_001", b2.getName());
    }


    @Test
    public void addAndDeleteQuestionOK1() throws WrongQuestionTypeException {
        when(sb1.find("100")).thenReturn("./target/Question/100.xml");
        when(sb1.find("200")).thenReturn("./target/Question/200.xml");
        when(sb1.find("300")).thenReturn("./target/Question/300.xml");
        Qcm b1 = new Qcm("./target/Qcm/Qcm_test_001.xml", sb1);
        Question q1 = new Question("./target/Question/300.xml");
        b1.addQuestion(q1);
        b1.save();
        assertEquals(3, b1.getQuestionQuantite());
        b1.deleteQuestion(q1);
        b1.save();
        assertEquals(2, b1.getQuestionQuantite());
    }


    @Test
    public void exportOK1() {
        when(sb1.find("100")).thenReturn("./target/Question/100.xml");
        when(sb1.find("200")).thenReturn("./target/Question/200.xml");
        when(sb1.find("300")).thenReturn("./target/Question/300.xml");
        Qcm b1 = new Qcm("./target/Qcm/Qcm_test_001.xml", sb1);

    }

}