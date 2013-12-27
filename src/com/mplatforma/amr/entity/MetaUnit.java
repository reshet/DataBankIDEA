/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import java.io.Serializable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author reshet
 */
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name="PROJ_TYPE")
public abstract class MetaUnit implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private String unique_name;

    public MetaUnit() {
    }
    public abstract MetaUnitDTO toDTO();
    protected MetaUnitDTO base_toDTO(MetaUnitDTO dto)
    {
        dto.setId(id);
        dto.setDesc(description);
        dto.setUnique_name(unique_name);
        return dto;
    }
    
    protected MetaUnit(String unique_name,String desc)
    {
        this.unique_name = unique_name;
        this.description = desc;
    }
    protected abstract void doUpdateFromDTO(MetaUnitDTO dto);
    public void updateFromDTO(MetaUnitDTO dto)
    {
        this.id = dto.getId();
        this.description = dto.getDesc();
        this.unique_name = dto.getUnique_name();
        doUpdateFromDTO(dto);
    }
    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetaUnit3)) {
            return false;
        }
        MetaUnit other = (MetaUnit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.MetaUnit[ id=" + id + " ]";
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the unique_name
     */
    public String getUnique_name() {
        return unique_name;
    }

    /**
     * @param unique_name the unique_name to set
     */
    public void setUnique_name(String unique_name) {
        this.unique_name = unique_name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
