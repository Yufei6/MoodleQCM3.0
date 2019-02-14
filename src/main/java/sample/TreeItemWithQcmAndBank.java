package sample;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class TreeItemWithQcmAndBank<S> extends TreeItem {
    private Qcm qcm=null;
    private Bank bank=null;

    public TreeItemWithQcmAndBank(Qcm qcm_0) {
        this.qcm = qcm_0;
    }

    public TreeItemWithQcmAndBank() {
    }

    public TreeItemWithQcmAndBank(Object value) {
        super(value);
    }

    public TreeItemWithQcmAndBank(Object value, Qcm qcm_0) {
        super(value);
        this.qcm = qcm_0;
    }

    public TreeItemWithQcmAndBank(Object value, Bank bank_0) {
        super(value);
        this.bank = bank_0;
    }


    public TreeItemWithQcmAndBank(Object value, Node graphic){
        super(value,graphic);
    }

    public TreeItemWithQcmAndBank(Object value, Node graphic, Qcm qcm_0) {
        super(value, graphic);
        this.qcm = qcm_0;
    }

    public Qcm getQcm() {
        return this.qcm;
    }

    public Bank getBank(){
        return this.bank;
    }
}
