/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitStringDTO;
import java.io.Serializable;

/**
 *
 * @author reshet
 */
//@Entity
public class MetaUnitString3 extends MetaUnit3 implements Serializable{
    public MetaUnitString3() {
    }
    public MetaUnitString3(String desc,String unique_name) {
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
}
