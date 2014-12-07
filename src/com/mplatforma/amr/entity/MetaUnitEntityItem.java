/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;


/**
 *
 * @author reshet
 */
@Entity
public class MetaUnitEntityItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    
    private Long id_metaunitentity;
    private String v_value;
    private HashMap<String,String> mapped_values;
    
    @OneToMany(mappedBy="parent_item",cascade= CascadeType.ALL)
    private Collection<MetaUnitEntityItem> subitems;
    
    @ManyToOne(cascade= CascadeType.REMOVE) 
    private  MetaUnitEntityItem parent_item;
            
            
    @OrderColumn
    private List<Long> tagged_entities_ids;
    @OrderColumn
    private List<String> tagged_entities_identifiers;
    
    public MetaUnitEntityItem()
    {
        this.mapped_values = new HashMap<String, String>();
    }
    public MetaUnitEntityItem(Long id_meta,String value)
    {
        this.id_metaunitentity = id_meta;
        this.v_value = value;
        this.mapped_values = new HashMap<String, String>();
    }
    
    public MetaUnitEntityItem(String value)
    {
        this.v_value = value;
        this.mapped_values = new HashMap<String, String>();
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
        if (!(object instanceof MetaUnitEntityItem)) {
            return false;
        }
        MetaUnitEntityItem other = (MetaUnitEntityItem) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.MetaUnitEntityItem[ id=" + getId() + " ]";
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
        return id_metaunitentity;
    }

    /**
     * @param id_metaunit the id_metaunit to set
     */
    public void setId_metaunit(Long id_metaunit) {
        this.id_metaunitentity = id_metaunit;
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
     * @return the mapped_values
     */
    public HashMap<String,String> getMapped_values() {
        return mapped_values;
    }

    /**
     * @param mapped_values the mapped_values to set
     */
    public void setMapped_values(HashMap<String,String> mapped_values) {
        this.mapped_values = mapped_values;
    }

    /**
     * @return the subitems
     */
    public Collection<MetaUnitEntityItem> getSubitems() {
        return subitems;
    }

    /**
     * @param subitems the subitems to set
     */
    public void setSubitems(Collection<MetaUnitEntityItem> subitems) {
        this.subitems = subitems;
    }

    /**
     * @return the tagged_entities_ids
     */
    public List<Long> getTagged_entities_ids() {
        return tagged_entities_ids;
    }

    /**
     * @param tagged_entities_ids the tagged_entities_ids to set
     */
    public void setTagged_entities_ids(List<Long> tagged_entities_ids) {
        this.tagged_entities_ids = tagged_entities_ids;
    }

    /**
     * @return the tagged_entities_identifiers
     */
    public List<String> getTagged_entities_identifiers() {
        return tagged_entities_identifiers;
    }

    /**
     * @param tagged_entities_identifiers the tagged_entities_identifiers to set
     */
    public void setTagged_entities_identifiers(List<String> tagged_entities_identifiers) {
        this.tagged_entities_identifiers = tagged_entities_identifiers;
    }

    /**
     * @return the tagged_entities_names
     */


    /**
     * @return the meta_identity
     */
  

    /**
     * @return the root
     */
    //public MetaUnitMultivalued<MetaEntityType> getRoot() {
   
    
}
