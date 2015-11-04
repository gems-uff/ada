/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.javacode;

import java.util.Date;

/**
 *
 * @author wallace
 */
public class RevisionAuthor {
    private String id;
    private String author;
    private String commiter;
    private String shortMessage;
    private Date commitDate;
    
    
    public RevisionAuthor(String id, String author, String commiter, String shortMessage, Date commitDate){
        this.id = id;
        this.author = author;
        this.commiter = commiter;
        this.shortMessage = shortMessage;
        this.commitDate = commitDate;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return the commiter
     */
    public String getCommiter() {
        return commiter;
    }

    /**
     * @return the shortMessage
     */
    public String getShortMessage() {
        return shortMessage;
    }

    /**
     * @return the commitDate
     */
    public Date getCommitDate() {
        return commitDate;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
}
