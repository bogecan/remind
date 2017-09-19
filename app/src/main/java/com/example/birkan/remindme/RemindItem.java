package com.example.birkan.remindme;

/**
 * Created by birkan on 14.09.2017.
 */

public class RemindItem {
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = Title;
    }

    String Title;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = Id;
    }

    Integer Id;

    public Boolean getDeleted() {
        return IsDeleted;
    }

    public void setDeleted(Boolean deleted) {
        IsDeleted = deleted;
    }

    Boolean IsDeleted;
}
