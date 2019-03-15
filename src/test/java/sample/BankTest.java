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

public class BankTest {
    private SuperBank sb1;

    @Before
    public void setUp() throws javax.xml.parsers.ParserConfigurationException, SAXException, IOException, WrongQuestionTypeException {
        sb1 = new SuperBank();
        sb1.generateTreeWithQuestion();
        sb1.extractId_Path();
    }



    @Test
    public void InitOK1(){
        Bank b1 = new Bank("./target/Bank/bank_test_001","bank_test_001", sb1);
        assertEquals(b1.getName(),"bank_test_001");
    }




    @Test
    public void AddAndDeleteQuestion(){
        Bank b1 = new Bank("./target/Bank/bank_test_001","bank_test_001", sb1);
        assertEquals(b1.getName(),"bank_test_001");
        try {
            Question q1 = sb1.findQuestion("3");
            assertFalse(b1.hasThisNameQuestion(q1.getName()));
            b1.addQuestion(q1);
            assertTrue(b1.hasThisNameQuestion(q1.getName()));
            b1.deleteQuestion(q1);
            assertFalse(b1.hasThisNameQuestion(q1.getName()));
        }catch(WrongQuestionTypeException e){
            e.printStackTrace();
        }

    }

    @Test
    public void ImportUneBanque(){
        Bank b1 = Bank.Import("./target/Bank/bank_test_001.xml",sb1);
        assertEquals(b1.getName(),"bank_test_001");
    }

    @Test
    public void ExporttUneBanqueOK(){
        Bank b1 = new Bank("./target/Bank/bank_test_001","bank_test_001", sb1);
        assertEquals(b1.getName(),"bank_test_001");
        assertTrue(b1.Export("./target/Bank_Export/", "bank_test_001_export").size()==0);
        Bank b2 = new Bank("./target/Bank_Export/bank_test_001_export.xml", "bank_test_001_export",sb1);
        assertEquals(b2.getName(),"bank_test_001_export");
    }

    @Test
    public void ExporttUneBanqueKO(){
        Bank b1 = new Bank("./target/Bank/bank_test_001","bank_test_001", sb1);
        assertEquals(b1.getName(),"bank_test_001");
        try {
            b1.addQuestion(sb1.findQuestion("2"));
            assertTrue(b1.Export("./target/Bank_Export", "bank_test_001_export1").size()==0);
            b1.addQuestion(sb1.findQuestion("50"));
            b1.deleteQuestion(sb1.findQuestion("2"));
            b1.deleteQuestion(sb1.findQuestion("50"));
        }catch(WrongQuestionTypeException e){
            e.printStackTrace();
        }
    }


    @Test
    public void CreerQuestionTreeAndGetQuestionQuantite(){
        Bank b1 = new Bank("./target/Bank/bank_test_001","bank_test_001", sb1);
        assertEquals(b1.getName(),"bank_test_001");
        TreeItemWithQcmAndBank<String> root = new TreeItemWithQcmAndBank<>();
        try {
            Question q1 = sb1.findQuestion("3");
            assertFalse(b1.hasThisNameQuestion(q1.getName()));
            assertEquals(b1.getQuestionQuantite(),0);
            b1.addQuestion(q1);
            assertTrue(b1.hasThisNameQuestion(q1.getName()));
            root = b1.createQuestionTree(root);
            assertEquals(root.getChildren().size(),1);
            assertEquals(b1.getQuestionQuantite(),1);
            b1.deleteQuestion(q1);
            assertFalse(b1.hasThisNameQuestion(q1.getName()));
            root = b1.createQuestionTree(root);
            assertEquals(root.getChildren().size(),1);
            assertEquals(b1.getQuestionQuantite(),0);
        }catch(WrongQuestionTypeException e){
            e.printStackTrace();
        }
    }


    @Test
    public void ChangeNameAndChangePath(){
        Bank b1 = new Bank("./target/Bank/bank_test_001","bank_test_001", sb1);
        assertEquals(b1.getName(),"bank_test_001");
        b1.changeName("new_name");
        assertEquals(b1.getName(),"new_name");
        b1.changeName("bank_test_001");
        assertEquals(b1.getName(),"bank_test_001");
        assertEquals(b1.getPath(),"./target/Bank/bank_test_001");
        b1.changeName("new_path");
        assertEquals(b1.getName(),"new_path");
        b1.changeName("/target/Bank/bank_test_001.xml");
        assertEquals(b1.getName(),"/target/Bank/bank_test_001.xml");
    }




}
