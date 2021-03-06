package com.mresearch.databank.client.views.DBviewers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.shared.JSON_Representation;
import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitDateDTO;
import com.mresearch.databank.shared.MetaUnitDoubleDTO;
import com.mresearch.databank.shared.MetaUnitFileDTO;
import com.mresearch.databank.shared.MetaUnitIntegerDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedStructureDTO;
import com.mresearch.databank.shared.MetaUnitStringDTO;
import com.mresearch.databank.shared.SocioResearchDTO;


public class VarMultiValuedFieldViewer extends Composite {

	private static MultiValuedFieldUiBinder uiBinder = GWT
			.create(MultiValuedFieldUiBinder.class);

	interface MultiValuedFieldUiBinder extends
			UiBinder<Widget, VarMultiValuedFieldViewer> {
	}

	public VarMultiValuedFieldViewer() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Label field_name;
	@UiField FlexTable subunits_table;
	public MetaUnitMultivaluedDTO dto;
	private HashMap<String,String> filling;
	private String base_name;
	public VarMultiValuedFieldViewer(MetaUnitMultivaluedDTO dto,HashMap<String,String> fill, String base_name) {
		initWidget(uiBinder.createAndBindUi(this));
		this.dto = dto;
		this.filling = fill;
		field_name.setText(dto.getDesc());
		this.base_name = base_name.equals("")?dto.getUnique_name():base_name+"_"+dto.getUnique_name();
		renderSubUnits();
	}
	
	private void renderSubUnits()
	{
		
		subunits_table.clear();
		Collection<MetaUnitDTO> base = dto.getSub_meta_units();
		int i = 0;
		if(base!=null)
		for(MetaUnitDTO dto:base)
		{
//			if(dto instanceof MetaUnitStringDTO)
//			{
//				MetaUnitStringDTO dto_str = (MetaUnitStringDTO)dto;
//				String def_val= null;
//				if(filling.containsKey(base_name+"_"+dto.getUnique_name()))def_val = filling.get(base_name+"_"+dto.getUnique_name());
//				subunits_table.setWidget(i++, 0, new SimpleStringFieldViewer(dto_str,null,def_val));
//			}else
//			if(dto instanceof MetaUnitDateDTO)
//			{
//				MetaUnitDateDTO dto_str = (MetaUnitDateDTO)dto;
//				String def_val= null;
//				if(filling.containsKey(base_name+"_"+dto.getUnique_name()))def_val = filling.get(base_name+"_"+dto.getUnique_name());
//				subunits_table.setWidget(i++, 0, new SimpleDateFieldViewer(dto_str,null,def_val));
//			}
//			if(dto instanceof MetaUnitIntegerDTO)
//			{
//				MetaUnitIntegerDTO dto_str = (MetaUnitIntegerDTO)dto;
//				String def_val= null;
//				if(filling.containsKey(base_name+"_"+dto.getUnique_name()))def_val = filling.get(base_name+"_"+dto.getUnique_name());
//				subunits_table.setWidget(i++, 0, new SimpleIntegerFieldViewer(dto_str,null,def_val));
//			}
//			if(dto instanceof MetaUnitDoubleDTO)
//			{
//				MetaUnitDoubleDTO dto_str = (MetaUnitDoubleDTO)dto;
//				String def_val= null;
//				if(filling.containsKey(base_name+"_"+dto.getUnique_name()))def_val = filling.get(base_name+"_"+dto.getUnique_name());
//				subunits_table.setWidget(i++, 0, new SimpleDoubleFieldViewer(dto_str,null,def_val));
//			}else
//			if(dto instanceof MetaUnitFileDTO)
//			{
//				MetaUnitFileDTO dto_str = (MetaUnitFileDTO)dto;
//				String def_val= null;
//				if(filling.containsKey(base_name+"_"+dto.getUnique_name()))def_val = filling.get(base_name+"_"+dto.getUnique_name());
//				subunits_table.setWidget(i++, 0, new SimpleFileFieldViewer(dto_str,null,def_val));
//			}else				
//			if(dto instanceof MetaUnitMultivaluedStructureDTO)
//			{
//				MetaUnitMultivaluedStructureDTO dto_str = (MetaUnitMultivaluedStructureDTO)dto;
//				subunits_table.setWidget(i++, 0, new VarMultiValuedFieldViewer(dto_str,filling,base_name));
//			}else
			if(dto instanceof MetaUnitMultivaluedEntityDTO)
			{
				MetaUnitMultivaluedEntityDTO dto_str = (MetaUnitMultivaluedEntityDTO)dto;
				subunits_table.setWidget(i++, 0, new MultiValuedEntityViewer(dto_str,null,filling,base_name));
			}
		}
	}	
}
