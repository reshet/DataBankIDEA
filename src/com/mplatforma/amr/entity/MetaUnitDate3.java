/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitDateDTO;
import java.io.Serializable;

/**
 *
 * @author reshet
 */
//@Entity
public class MetaUnitDate3 extends MetaUnit3 implements Serializable {
    private static final long serialVersionUID = 1L;
    public MetaUnitDate3() {
    }
    public MetaUnitDate3(String desc,String unique_name) {
        super(unique_name, desc);
    }

    @Override
    public MetaUnitDTO toDTO() {
       return new MetaUnitDateDTO(super.getId(),super.getDescription(),super.getUnique_name());
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void doUpdateFromDTO(MetaUnitDTO dto) {
        //
    }
}
