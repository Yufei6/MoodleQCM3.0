package sample;

import javafx.scene.control.TreeItem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class QcmTest {
    private SuperBank sb1;

    @Before
    public void setUp() throws javax.xml.parsers.ParserConfigurationException, SAXException, IOException, WrongQuestionTypeException {
        sb1 = new SuperBank();
        sb1.generateTreeWithQuestion();
        sb1.extractId_Path();
    }



    @Test
    public void InitOK1(){
        Qcm qcm1 = new Qcm("./target/Qcm/qcm_test_001","qcm_test_001", sb1);
        assertEquals(qcm1.getName(),"qcm_test_001");
    }




    @Test
    public void AddAndDeleteQuestion(){
        Qcm qcm1 = new Qcm("./target/Qcm/qcm_test_001","qcm_test_001", sb1);
        assertEquals(qcm1.getName(),"qcm_test_001");
        try {
            Question q1 = sb1.findQuestion("3");
            assertFalse(qcm1.hasThisNameQuestion(q1.getName()));
            qcm1.addQuestion(q1);
            assertTrue(qcm1.hasThisNameQuestion(q1.getName()));
            qcm1.deleteQuestion(q1);
            assertFalse(qcm1.hasThisNameQuestion(q1.getName()));
        }catch(WrongQuestionTypeException e){
            e.printStackTrace();
        }

    }

    @Test
    public void ImportUneBanque(){
        Qcm qcm1 = Qcm.Import("./target/Qcm/qcm_test_001.xml",sb1);
        assertEquals(qcm1.getName(),"qcm_test_001");
    }

    @Test
    public void ExporttUneBanqueOK(){
        Qcm qcm1 = new Qcm("./target/Qcm/qcm_test_001","qcm_test_001", sb1);
        assertEquals(qcm1.getName(),"qcm_test_001");
        assertTrue(qcm1.Export("./target/qcm_Export/", "qcm_test_001_export").size()==0);
        Qcm qcm2 = new Qcm("./target/Qcm_Export/qcm_test_001_export.xml", "qcm_test_001_export",sb1);
        assertEquals(qcm2.getName(),"qcm_test_001_export");
    }

    @Test
    public void ExporttUneBanqueKO(){
        Qcm qcm1 = new Qcm("./target/Qcm/qcm_test_001","qcm_test_001", sb1);
        assertEquals(qcm1.getName(),"qcm_test_001");
        try {
            qcm1.addQuestion(sb1.findQuestion("2"));
            assertTrue(qcm1.Export("./target/Qcm_Export", "qcm_test_001_export1").size()==0);
            qcm1.addQuestion(sb1.findQuestion("50"));
            qcm1.deleteQuestion(sb1.findQuestion("2"));
            qcm1.deleteQuestion(sb1.findQuestion("50"));
        }catch(WrongQuestionTypeException e){
            e.printStackTrace();
        }
    }


    @Test
    public void CreerQuestionTreeAndGetQuestionQuantite(){
        Qcm qcm1 = new Qcm("./target/Qcm/bank_test_001","qcm_test_001", sb1);
        assertEquals(qcm1.getName(),"qcm_test_001");
        TreeItemWithQcmAndBank<String> root = new TreeItemWithQcmAndBank<>();
        try {
            Question q1 = sb1.findQuestion("3");
            assertFalse(qcm1.hasThisNameQuestion(q1.getName()));
            assertEquals(qcm1.getQuestionQuantite(),0);
            qcm1.addQuestion(q1);
            assertTrue(qcm1.hasThisNameQuestion(q1.getName()));
            root = qcm1.createQuestionTree(root);
            assertEquals(root.getChildren().size(),1);
            assertEquals(qcm1.getQuestionQuantite(),1);
            qcm1.deleteQuestion(q1);
            assertFalse(qcm1.hasThisNameQuestion(q1.getName()));
            root = qcm1.createQuestionTree(root);
            assertEquals(root.getChildren().size(),1);
            assertEquals(qcm1.getQuestionQuantite(),0);
        }catch(WrongQuestionTypeException e){
            e.printStackTrace();
        }
    }


    @Test
    public void ChangeNameAndChangePath(){
        Qcm qcm1 = new Qcm("./target/Qcm/qcm_test_001","qcm_test_001", sb1);
        assertEquals(qcm1.getName(),"qcm_test_001");
        qcm1.changeName("new_name");
        assertEquals(qcm1.getName(),"new_name");
        qcm1.changeName("qcm_test_001");
        assertEquals(qcm1.getName(),"qcm_test_001");
        assertEquals(qcm1.getPath(),"./target/Qcm/qcm_test_001");
        qcm1.changeName("new_path");
        assertEquals(qcm1.getName(),"new_path");
        qcm1.changeName("/target/Qcm/qcm_test_001.xml");
        assertEquals(qcm1.getName(),"/target/Qcm/qcm_test_001.xml");
    }




}
