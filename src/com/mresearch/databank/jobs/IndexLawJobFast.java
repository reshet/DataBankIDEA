/*
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mresearch.databank.jobs;

import com.mresearch.databank.shared.ZaconDTO;
import java.io.Serializable;

/**
 *
 * @author reshet
 */
public class IndexLawJobFast implements Serializable{
    private ZaconDTO dto;
    public IndexLawJobFast(ZaconDTO dto)
    {
        this.dto = dto;
    }
    public IndexLawJobFast(){}

    /**
     * @return the dto
     */
    public ZaconDTO getDto() {
        return dto;
    }

    /**
     * @param dto the dto to set
     */
    public void setDto(ZaconDTO dto) {
        this.dto = dto;
    }
    /**
     * @return the id_file
     */
   

    /**
     * @return the length
     */
   
}
