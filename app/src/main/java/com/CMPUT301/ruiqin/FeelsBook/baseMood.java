package com.CMPUT301.ruiqin.FeelsBook;

import java.util.Date;


public class baseMood {
    /**declare
     * 1 date type
     * 2 string type
     */
    private Date date;
    private String mood,comment;

    /**constructors
     *
     * @param date
     * @param mood
     * @param comment
     */
    public baseMood(Date date, String mood, String comment) {
        this.date = date;
        this.mood = mood;
        this.comment = comment;
    }

    /**getter & setters
     *
     */
    public String getComment() {
        return comment;
    }

    public String getMood() {
        return mood;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date){
        this.date = date;
    }
    public void setMood(String mood){
        this.mood=mood;

    }
    public void setComment(String comment){
        this.comment = comment;
    }

}
