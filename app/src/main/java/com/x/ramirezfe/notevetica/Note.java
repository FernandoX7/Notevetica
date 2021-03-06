package com.x.ramirezfe.notevetica;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by Fernando on 3/28/16.
 */

/**
 * Sugar ORM
 * -Extends SugarRecord so we can persist the Note object for offline capabilities
 */

public class Note extends SugarRecord {

    private String title;
    private String description;
    // Backendless properties
    private Date created;
    private Date updated;
    private String objectId;
    //  updatedData' is used to sort notes
    private String updatedDate;

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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Note() {
        // Empty
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", objectId='" + objectId + '\'' +
                '}';
    }
}
