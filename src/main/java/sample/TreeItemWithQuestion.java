package sample;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class TreeItemWithQuestion<S> extends TreeItemWithQcmAndBank {
    private Question question;

    public TreeItemWithQuestion(Question question) {
        this.question = question;
    }

    public TreeItemWithQuestion() {
    }

    public TreeItemWithQuestion(Object value) {
        super(value);
    }

    public TreeItemWithQuestion(Object value, Question question) {
        super(value);
        this.question = question;
    }

    public TreeItemWithQuestion(Object value, Node graphic, Question question) {
        super(value, graphic);
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }
}
