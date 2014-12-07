/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitFileDTO;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author reshet
 */
@Entity
@DiscriminatorValue("FileType")
public class MetaUnitFile extends MetaUnit implements Serializable {
    private static final long serialVersionUID = 1L;
    public MetaUnitFile() {
    }
    private String file_name;
    private long file_id;
    public MetaUnitFile(String unique_name,String desc) {
        super(unique_name, desc);
    }

    @Override
    public MetaUnitDTO toDTO() {
       //return new MetaUnitFileDTO(super.getId(),super.getUnique_name(),super.getDescription(),file_name,file_id);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void doUpdateFromDTO(MetaUnitDTO dto) {
        //
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetaUnitFile)) {
            return false;
        }
        MetaUnitFile other = (MetaUnitFile) object;
        if ((getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.MetaUnitFile[ id=" + getId()+ " ]";
    }

    /**
     * @return the file_type
     */
    public String getFile_type() {
        return file_name;
    }

    /**
     * @param file_type the file_type to set
     */
    public void setFile_type(String file_name) {
        this.file_name = file_name;
    }

    /**
     * @return the file_id
     */
    public long getFile_id() {
        return file_id;
    }

    /**
     * @param file_id the file_id to set
     */
    public void setFile_id(long file_id) {
        this.file_id = file_id;
    }
    
}
