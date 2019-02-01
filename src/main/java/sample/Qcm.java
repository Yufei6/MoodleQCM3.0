package sample;


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

//    public static Qcm Import(String xml_path, String new_name){
//        super(xml_path, new_name, false);
//    }

    public void Export(String xml_path, String name_for_xml, boolean isBank){
        super.Export(xml_path, name_for_xml, false);
    }


    public String getPath() {
        return path;
    }
}
