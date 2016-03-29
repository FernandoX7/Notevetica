package com.x.ramirezfe.notevetica;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 3/28/16.
 */

public class Note {

    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }


}
