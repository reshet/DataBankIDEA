/*
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mresearch.databank.jobs;

import java.io.Serializable;

/**
 *
 * @author reshet
 */
public class IndexVarJob implements Serializable{
    private long id_var;
    public IndexVarJob(long id_var)
    {
        this.id_var = id_var;
    }
    public IndexVarJob(){}
    /**
     * @return the id_file
     */
    public long getId_Var() {
        return id_var;
    }

    /**
     * @param id_file the id_file to set
     */
    public void setId_Research(long id_v) {
        this.id_var = id_v;
    }

    /**
     * @return the length
     */
   
}
