/*
/*
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mresearch.databank.jobs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author reshet
 */
public class DeleteIndexiesJob implements Serializable{
    private ArrayList<Long> ids;
    private String type;
    
    public DeleteIndexiesJob( ArrayList<Long> ids,String type)
    {
        this.ids = ids;
        this.type = type;
    }
    public DeleteIndexiesJob(){}

    /**
     * @return the ids
     */
    public ArrayList<Long> getIds() {
        return ids;
    }

    /**
     * @param ids the ids to set
     */
    public void setIds(ArrayList<Long> ids) {
        this.ids = ids;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the id_file
     */
  

    /**
     * @return the length
     */
   
}
