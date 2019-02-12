package sample;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class TreeItemWithQcm<S> extends TreeItem {
    private Qcm qcm;

    public TreeItemWithQcm(Qcm qcm_0) {
        this.qcm = qcm_0;
    }

    public TreeItemWithQcm() {
    }

    public TreeItemWithQcm(Object value) {
        super(value);
    }

    public TreeItemWithQcm(Object value, Qcm qcm_0) {
        super(value);
        this.qcm = qcm_0;
    }

    public TreeItemWithQcm(Object value, Node graphic, Qcm qcm_0) {
        super(value, graphic);
        this.qcm = qcm_0;
    }

    public Qcm getQcm() {
        return this.qcm;
    }
}
