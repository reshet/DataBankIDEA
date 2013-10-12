/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 *
 * @author reshet
 */
@Entity
public class MetaUnitValue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long id_metaunit;
    private String v_value;
    //private MetaUnitMultivalued<MetaEntityType> root;
    
    public MetaUnitValue()
    {
    }
    public MetaUnitValue(Long id_meta,String value)
    {
        this.id_metaunit = id_meta;
        this.v_value = value;
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
        if (!(object instanceof MetaUnitValue)) {
            return false;
        }
        MetaUnitValue other = (MetaUnitValue) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.MetaUnitValue[ id=" + getId() + " ]";
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the id_metaunit
     */
    public Long getId_metaunit() {
        return id_metaunit;
    }

    /**
     * @param id_metaunit the id_metaunit to set
     */
    public void setId_metaunit(Long id_metaunit) {
        this.id_metaunit = id_metaunit;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return v_value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.v_value = value;
    }

    /**
     * @return the meta_identity
     */
  

    /**
     * @return the root
     */
    //public MetaUnitMultivalued<MetaEntityType> getRoot() {
   
    
}
