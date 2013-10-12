/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitDoubleDTO;
import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author reshet
 */
@Entity
@DiscriminatorValue("DoubleType")
public class MetaUnitDouble extends MetaUnit implements Serializable {
    private static final long serialVersionUID = 1L;
    public MetaUnitDouble() {
    }
    public MetaUnitDouble(String unique_name,String desc) {
        super(unique_name, desc);
    }

    @Override
    public MetaUnitDTO toDTO() {
       return new MetaUnitDoubleDTO(super.getId(),super.getDescription(),super.getUnique_name());
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
        if (!(object instanceof MetaUnitDouble)) {
            return false;
        }
        MetaUnitDouble other = (MetaUnitDouble) object;
        if ((getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.MetaUnitDouble[ id=" + getId()+ " ]";
    }
    
}