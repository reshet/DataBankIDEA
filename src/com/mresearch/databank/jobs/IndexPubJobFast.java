/*
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mresearch.databank.jobs;

import com.mresearch.databank.shared.PublicationDTO;
import java.io.Serializable;

/**
 *
 * @author reshet
 */
public class IndexPubJobFast implements Serializable{
    private PublicationDTO dto;
    public IndexPubJobFast(PublicationDTO dto)
    {
        this.dto = dto;
    }
    public IndexPubJobFast(){}

    /**
     * @return the dto
     */
    public PublicationDTO getDto() {
        return dto;
    }

    /**
     * @param dto the dto to set
     */
    public void setDto(PublicationDTO dto) {
        this.dto = dto;
    }
    /**
     * @return the id_file
     */
   

    /**
     * @return the length
     */
   
}
