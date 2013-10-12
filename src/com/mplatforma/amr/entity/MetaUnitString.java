/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitStringDTO;
import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author reshet
 */
@Entity
@DiscriminatorValue("StringType")
public class MetaUnitString extends MetaUnit implements Serializable{
    public MetaUnitString() {
    }
    public MetaUnitString(String unique_name,String desc) {
        super(unique_name, desc);
    }
    
    @Override
    public MetaUnitDTO toDTO() {
        return new MetaUnitStringDTO(super.getId(),super.getDescription(),super.getUnique_name());
    }

    @Override
    protected void doUpdateFromDTO(MetaUnitDTO dto) {
        //
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (super.getId() != null ? super.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetaUnitString)) {
            return false;
        }
        MetaUnitString other = (MetaUnitString) object;
        if ((super.getId() == null && other.getId()!= null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.MetaUnitString[ id=" + getId() + " ]";
    } 
}
