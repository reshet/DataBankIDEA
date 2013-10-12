/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedDTO;

import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


/**
 *
 * @author reshet
 */
@Entity
@DiscriminatorValue("MultiTypeEntity")
public class MetaUnitMultivaluedEntity extends MetaUnitMultivalued implements Serializable {
    private static final long serialVersionUID = 1L;
    private long isMultiSelected = 0;
    
    @OneToMany(cascade= CascadeType.ALL)
    private Collection<MetaUnitEntityItem> items;
    
    public MetaUnitMultivaluedEntity() {
        super();
        items = new ArrayList<MetaUnitEntityItem>();
    }
    public MetaUnitMultivaluedEntity(String unique_name,String desc,long isMultiSelected) {
        super(unique_name, desc);
        this.isMultiSelected = isMultiSelected;
        this.items = new ArrayList<MetaUnitEntityItem>();
    }
    public MetaUnitMultivaluedEntity(String unique_name,String desc,long isMultiSelected,ArrayList<MetaUnit> sub_meta) {
        super(unique_name,desc);
        this.isMultiSelected = isMultiSelected;
        super.setSub_meta_units(sub_meta);
        this.items = new ArrayList<MetaUnitEntityItem>();
    }


    /**
     * @return the sub_meta_units


    /**
     * @param sub_meta_units the sub_meta_units to set
     */


    /**
     * @return the tagged_entities
     */
    //public ArrayList<MetaEntityType> getTagged_entities() {


    /**
     * @param tagged_entities the tagged_entities to set
     */
    
    //public void setTagged_entities(ArrayList<MetaEntityType> tagged_entities) {


    /**
     * @return the isCatalogizable
     */
   
    @Override
    public MetaUnitDTO toDTO() {
        
        //EcliseLink behaves weired so method is moved to bean;
        MetaUnitMultivaluedEntityDTO dto = new MetaUnitMultivaluedEntityDTO();
//        dto = (MetaUnitMultivaluedDTO)base_toDTO(dto);
//        dto.setIsCatalogizable(isCatalogizable!=0);
//        dto.setIsSplittingEnabled(isSplittingEnabled!=0);
//        dto.setArrayView(array_view!=0);
//        dto.setTagged_entities(tagged_entities);
//        ArrayList<MetaUnitDTO> sub_meta_units_dtos = new ArrayList<MetaUnitDTO>();
//        for(MetaUnit unit:this.sub_meta_units)
//        {
//            if(unit instanceof MetaUnitMultivalued)
//            {
//                MetaUnitDTO dt;
//                //DONE FOR SPLIITING DOWNLOAD OF DEEP TREE
//                if(((MetaUnitMultivalued)unit).getIsSplittingEnabled()!=0)
//                {
//                    dt = new MetaUnitMultivaluedDTO(super.getId(),super.getDescription(),super.getUnique_name());
//                    ((MetaUnitMultivaluedDTO)dt).setIsCatalogizable(((MetaUnitMultivalued)unit).getIsCatalogizable()!=0);
//                    ((MetaUnitMultivaluedDTO)dt).setIsSplittingEnabled(((MetaUnitMultivalued)unit).getIsSplittingEnabled()!=0);
//                    ((MetaUnitMultivaluedDTO)dt).setArrayView(((MetaUnitMultivalued)unit).getArray_view()!=0);
//                }
//                else
//                {
//                    dt = unit.toDTO();
//                }
//                sub_meta_units_dtos.add(dt);
//            }else
//            {
//                sub_meta_units_dtos.add(unit.toDTO());
//            }
//        }
//        dto.setSub_meta_units(sub_meta_units_dtos);
        
        return dto;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the isSplittingEnabled
     */
   

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
//    private boolean sub_meta_units_contains(Long id)
//    {
//        if(id != null)
//        for(MetaUnit unit:sub_meta_units)
//        {
//            if(unit.getId()==id)return true;
//        }
//        return false;
//    }
//    private MetaUnit sub_meta_units_get(Long id)
//    {
//        if(id != null)
//        for(MetaUnit unit:sub_meta_units)
//        {
//            if(unit.getId()==id)return unit;
//        }
//        return null;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetaUnitMultivaluedEntity)) {
            return false;
        }
        MetaUnitMultivaluedEntity other = (MetaUnitMultivaluedEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.MetaUnitMultivaluedEntity[ id=" + getId() + " ]";
    }

    /**
     * @return the isMultiSelected
     */
    public long getIsMultiSelected() {
        return isMultiSelected;
    }

    /**
     * @param isMultiSelected the isMultiSelected to set
     */
    public void setIsMultiSelected(long isMultiSelected) {
        this.isMultiSelected = isMultiSelected;
    }

    /**
     * @return the items
     */
    public Collection<MetaUnitEntityItem> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(Collection<MetaUnitEntityItem> items) {
        this.items = items;
    }

    /**
     * @return the multiselected
     */
 

    /**
     * @return the isStructure
     */
    
}
