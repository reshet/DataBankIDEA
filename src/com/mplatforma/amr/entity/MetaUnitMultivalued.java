/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

/**
 *
 * @author reshet
 */
@Entity
@DiscriminatorValue("MultiType")
public abstract class MetaUnitMultivalued extends MetaUnit implements Serializable {
    private static final long serialVersionUID = 1L;
    private long isCatalogizable = 0;
    private long isSplittingEnabled = 0;

    
    
    @OneToMany(cascade= CascadeType.ALL)
    @OrderColumn
    private List<MetaUnit> sub_meta_units;
    private Collection<Long> tagged_entities;
    
    public MetaUnitMultivalued() {
        super();
    }
    public MetaUnitMultivalued(String unique_name,String desc) {
        super(unique_name, desc);
        sub_meta_units = new ArrayList<MetaUnit>();
        tagged_entities = new ArrayList<Long>();
    }


    /**
     * @return the sub_meta_units
     */
    public List<MetaUnit> getSub_meta_units() {
        return sub_meta_units;
    }

    /**
     * @param sub_meta_units the sub_meta_units to set
     */
    public void setSub_meta_units(List<MetaUnit> sub_meta_units) {
        this.sub_meta_units = sub_meta_units;
    }

    /**
     * @return the tagged_entities
     */
    //public ArrayList<MetaEntityType> getTagged_entities() {
    public Collection<Long> getTagged_entities() {
        return tagged_entities;
    }

    /**
     * @param tagged_entities the tagged_entities to set
     */
    
    //public void setTagged_entities(ArrayList<MetaEntityType> tagged_entities) {
    public void setTagged_entities(Collection<Long> tagged_entities) {
        this.tagged_entities = tagged_entities;
    }

    /**
     * @return the isCatalogizable
     */
    public long getIsCatalogizable() {
        return isCatalogizable;
    }

    /**
     * @param isCatalogizable the isCatalogizable to set
     */
    public void setIsCatalogizable(long isCatalogizable) {
        this.isCatalogizable = isCatalogizable;
    }

    /**
     * @return the isSplittingEnabled
     */
    public long getIsSplittingEnabled() {
        return isSplittingEnabled;
    }

    /**
     * @param isSplittingEnabled the isSplittingEnabled to set
     */
    public void setIsSplittingEnabled(long isSplittingEnabled) {
        this.isSplittingEnabled = isSplittingEnabled;
    }

    /**
     * @return the array_view
     */

    @Override
    protected void doUpdateFromDTO(MetaUnitDTO dto) {
        if(dto instanceof MetaUnitMultivaluedDTO)
        {
//            MetaUnitMultivaluedDTO m_dto = (MetaUnitMultivaluedDTO)dto;
//            this.array_view = m_dto.isArrayView()?1:0;
//            this.isCatalogizable = m_dto.isIsCatalogizable()?1:0;
//            this.isSplittingEnabled = m_dto.isIsSplittingEnabled()?1:0;
//            this.tagged_entities = m_dto.getTagged_entities();
//            for(MetaUnitDTO dt:m_dto.getSub_meta_units())
//            {
//                if(sub_meta_units_contains(dt.getId()))
//                {
//                    MetaUnit m_unit = sub_meta_units_get(dt.getId());
//                    m_unit.updateFromDTO(dt);
//                }else
//                {
//                    MetaUnit new_unit = null;
//                    
//                    if(dt instanceof MetaUnitDateDTO){new_unit = new MetaUnitDate();new_unit.updateFromDTO(dt);}
//                    if(dt instanceof MetaUnitDoubleDTO){new_unit = new MetaUnitDouble();new_unit.updateFromDTO(dt);}
//                    if(dt instanceof MetaUnitIntegerDTO){new_unit = new MetaUnitInteger();new_unit.updateFromDTO(dt);}
//                    if(dt instanceof MetaUnitStringDTO){new_unit = new MetaUnitString();new_unit.updateFromDTO(dt);}
//                    if(dt instanceof MetaUnitMultivaluedDTO){new_unit = new MetaUnitMultivalued();new_unit.updateFromDTO(dt);}
//                    
//                    sub_meta_units.add(new_unit);
//                }
//            }
        }
    }
    public boolean sub_meta_units_contains(Long id)
    {
        if(id != null)
        for(MetaUnit unit:sub_meta_units)
        {
            if(unit.getId().equals(id))return true;
        }
        return false;
    }
    public MetaUnit sub_meta_units_get(Long id)
    {
        if(id != null)
        for(MetaUnit unit:sub_meta_units)
        {
            if(unit.getId().equals(id))return unit;
        }
        return null;
    }
    public int sub_meta_units_get_order(Long id)
    {
        int i = 0;
        if(id != null)
        for(MetaUnit unit:sub_meta_units)
        {
            if(unit.getId().equals(id))return i;
            i++;
        }
        return -1;
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
        if (!(object instanceof MetaUnitMultivalued)) {
            return false;
        }
        MetaUnitMultivalued other = (MetaUnitMultivalued) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.MetaUnitMultivalued[ id=" + getId() + " ]";
    }

    /**
     * @return the multiselected
     */
 

    /**
     * @return the isStructure
     */
    
}
