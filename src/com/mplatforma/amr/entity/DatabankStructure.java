/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author reshet
 */
@Entity
public class DatabankStructure implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String meta_identity;
    @OneToOne(cascade = CascadeType.PERSIST)
    private MetaUnitMultivaluedEntity root;
    
    @OneToMany(cascade = CascadeType.PERSIST)
    private Map<Long,MetaUnitEntityItem> items;

    public void putItem(Long entity_id,MetaUnitEntityItem filling)
    {
        this.items.put(entity_id, filling);
    }
    public MetaUnitEntityItem getItem(Long key)
    {
        return this.items.get(key);
    }
    //private MetaUnitMultivalued<MetaEntityType> root;
    public DatabankStructure() {
    }

    public DatabankStructure(String meta_entity_type) {
        this.meta_identity = meta_entity_type;
        this.items = new HashMap<Long, MetaUnitEntityItem>();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (meta_identity != null ? meta_identity.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatabankStructure)) {
            return false;
        }
        DatabankStructure other = (DatabankStructure) object;
        if ((this.meta_identity == null && other.meta_identity != null) || (this.meta_identity != null && !this.meta_identity.equals(other.meta_identity))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.DatabankStructure[ id=" + meta_identity + " ]";
    }

    /**
     * @return the meta_identity
     */
    public String getMeta_identity() {
        return meta_identity;
    }

    /**
     * @param meta_identity the meta_identity to set
     */
    public void setMeta_identity(String meta_identity) {
        this.meta_identity = meta_identity;
    }

    /**
     * @return the root
     */
    //public MetaUnitMultivalued<MetaEntityType> getRoot() {
    public MetaUnitMultivaluedEntity getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    // public void setRoot(MetaUnitMultivalued<MetaEntityType> root) {
    public void setRoot(MetaUnitMultivaluedEntity root) {
        this.root = root;
    }

    /**
     * @return the items
     */
}
