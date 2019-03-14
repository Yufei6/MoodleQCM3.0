package sample;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class TreeItemWithQuestion<S> extends TreeItemWithQcmAndBank {
    private Question question;


    public TreeItemWithQuestion(Object value, Question question) {
        super(value);
        this.question = question;
    }


    public Question getQuestion() {
        return question;
    }
}
