/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitDoubleDTO;
import java.io.Serializable;

/**
 *
 * @author reshet
 */
//@Entity
public class MetaUnitDouble3 extends MetaUnit3 implements Serializable{
   
    public MetaUnitDouble3() {
    }
    public MetaUnitDouble3(String desc,String un_name) {
        super(un_name, desc);
    }

    @Override
    public MetaUnitDTO toDTO() {
        return new MetaUnitDoubleDTO(super.getId(),super.getDescription(),super.getUnique_name());  
    }

    @Override
    protected void doUpdateFromDTO(MetaUnitDTO dto) {
       //
    }
}
