package sample;

import javafx.scene.control.TreeItem;

public class TreeItemWithRepertoire<S> extends TreeItem {
    private String path;

    TreeItemWithRepertoire(){
        super();
    }

    TreeItemWithRepertoire(String value){
        super(value);
    }

    TreeItemWithRepertoire(String value, String path_0){
        super(value);
        path=path_0;
    }

    public String getPath(){
        return this.path;
    }
}
