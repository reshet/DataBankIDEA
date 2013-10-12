/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitIntegerDTO;
import java.io.Serializable;

/**
 *
 * @author reshet
 */
//@Entity
public class MetaUnitInteger3 extends MetaUnit3 implements Serializable{
    public MetaUnitInteger3() {
    }
    public MetaUnitInteger3(String desc,String unique_name) {
        super(unique_name, desc);
    }
    
    @Override
    public MetaUnitDTO toDTO() {
        return new MetaUnitIntegerDTO(super.getId(),super.getDescription(),super.getUnique_name());
    }

    @Override
    protected void doUpdateFromDTO(MetaUnitDTO dto) {
        //
    }
}
