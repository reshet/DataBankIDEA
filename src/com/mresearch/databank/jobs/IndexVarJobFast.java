/*
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mresearch.databank.jobs;

import com.mresearch.databank.shared.VarDTO_Detailed;
import java.io.Serializable;

/**
 *
 * @author reshet
 */
public class IndexVarJobFast implements Serializable{
    private VarDTO_Detailed dto;
    public IndexVarJobFast(VarDTO_Detailed dto)
    {
        this.dto = dto;
    }
    public IndexVarJobFast(){}

    /**
     * @return the dto
     */
    public VarDTO_Detailed getDto() {
        return dto;
    }

    /**
     * @param dto the dto to set
     */
    public void setDto(VarDTO_Detailed dto) {
        this.dto = dto;
    }
    /**
     * @return the id_file
     */
   

    /**
     * @return the length
     */
   
}
