/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitIntegerDTO;
import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author reshet
 */
@Entity
@DiscriminatorValue("IntegerType")
public class MetaUnitInteger extends MetaUnit implements Serializable {
    private static final long serialVersionUID = 1L;
    public MetaUnitInteger() {
    }
    public MetaUnitInteger(String unique_name,String desc) {
        super(unique_name, desc);
    }

    @Override
    public MetaUnitDTO toDTO() {
       return new MetaUnitIntegerDTO(super.getId(),super.getDescription(),super.getUnique_name());
        //throw new UnsupportedOperationException("Not supported yet.");
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
        if (!(object instanceof MetaUnitInteger)) {
            return false;
        }
        MetaUnitInteger other = (MetaUnitInteger) object;
        if ((getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.MetaUnitInteger[ id=" + getId()+ " ]";
    }
    
}
