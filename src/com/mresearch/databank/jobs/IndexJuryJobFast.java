/*
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mresearch.databank.jobs;

import com.mresearch.databank.shared.ConsultationDTO;
import java.io.Serializable;

/**
 *
 * @author reshet
 */
public class IndexJuryJobFast implements Serializable{
    private ConsultationDTO dto;
    public IndexJuryJobFast(ConsultationDTO dto)
    {
        this.dto = dto;
    }
    public IndexJuryJobFast(){}

    /**
     * @return the dto
     */
    public ConsultationDTO getDto() {
        return dto;
    }

    /**
     * @param dto the dto to set
     */
    public void setDto(ConsultationDTO dto) {
        this.dto = dto;
    }
    /**
     * @return the id_file
     */
   

    /**
     * @return the length
     */
   
}
