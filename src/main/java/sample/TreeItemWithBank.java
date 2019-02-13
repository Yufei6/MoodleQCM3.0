package sample;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class TreeItemWithBank<S> extends TreeItem {
    private Bank bank;

    public TreeItemWithBank(Bank bank_0) {
        this.bank = bank_0;
    }

    public TreeItemWithBank() {
    }

    public TreeItemWithBank(Object value) {
        super(value);
    }

    public TreeItemWithBank(Object value, Bank bank_0) {
        super(value);
        this.bank = bank_0;
    }

    public TreeItemWithBank(Object value, Node graphic, Bank bank_0) {
        super(value, graphic);
        this.bank = bank_0;
    }

    public Bank getBank() {
        return this.bank;
    }
}
