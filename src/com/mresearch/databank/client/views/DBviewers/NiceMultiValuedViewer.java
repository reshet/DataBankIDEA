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

public class NiceMultiValuedViewer extends Composite {

	private static MultiValuedFieldUiBinder uiBinder = GWT
			.create(MultiValuedFieldUiBinder.class);

	interface MultiValuedFieldUiBinder extends
			UiBinder<Widget, NiceMultiValuedViewer> {
	}

	public NiceMultiValuedViewer() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Label field_name;
	@UiField FlexTable subunits_table;
	public MetaUnitMultivaluedDTO dto;
	private HashMap<String,String> filling;
	private String base_name;
	private ArrayList<String> exclude_fields;
	public NiceMultiValuedViewer(boolean root,MetaUnitMultivaluedDTO dto,HashMap<String,String> fill, String base_name,ArrayList<String> exclude_fields) {
		initWidget(uiBinder.createAndBindUi(this));
		this.dto = dto;
		this.filling = fill;
		this.exclude_fields = exclude_fields;
		if(!root)field_name.setText(dto.getDesc());
		//subunits_table.getColumnFormatter().setWidth(0, "150px");
		//subunits_table.getColumnFormatter().setWidth(1, "250px");
		
		this.base_name = base_name.equals("")?dto.getUnique_name():base_name+"_"+dto.getUnique_name();
		renderSubUnits();
	}
	
	private int counter = 0;
	private void doShowItems(MetaUnitMultivaluedDTO dt,String suffix,int left_margin)
	{
		
		Collection<MetaUnitDTO> base = dt.getSub_meta_units();
		if(base!=null)
		for(MetaUnitDTO dto:base)
		{
			if(dto instanceof MetaUnitMultivaluedStructureDTO)
			{
				MetaUnitMultivaluedStructureDTO dto_str = (MetaUnitMultivaluedStructureDTO)dto;
				subunits_table.setWidget(counter, 0, new SimpleFieldNameViewer(dto, null, null,left_margin));
				subunits_table.getCellFormatter().setWordWrap(counter, 0, true);
				subunits_table.setCellSpacing(5);
				subunits_table.setCellPadding(3);
				counter++;
				String local_suff = "";
				if(!suffix.equals(""))
					local_suff = suffix+"_"+dto_str.getUnique_name();
				else local_suff = new String(dto_str.getUnique_name());
			
				
				
				doShowItems(dto_str,local_suff,left_margin+24);
			}else
			//if(dto instanceof MetaUnitStringDTO)
			{
				//MetaUnitStringDTO dto_str = (MetaUnitStringDTO)dto;
				String def_val= null;
				String key = null;
				if(suffix!= null && !suffix.equals(""))key = base_name+"_"+suffix+"_"+dto.getUnique_name();
					else key = base_name+"_"+dto.getUnique_name();
				
				if(filling.containsKey(key))def_val = filling.get(key);
				if(exclude_fields == null || !exclude_fields.contains(key))
				{
					subunits_table.setWidget(counter, 0, new SimpleFieldNameViewer(dto, null, def_val,left_margin));
					subunits_table.setWidget(counter, 1, new SimpleFieldValueViewer(def_val));
					subunits_table.getCellFormatter().setWordWrap(counter, 0, true);
					subunits_table.getCellFormatter().setWordWrap(counter, 1, true);
					subunits_table.setCellSpacing(5);
					subunits_table.setCellPadding(3);
					
					counter++;
				}
			}
		}
	}
	private void renderSubUnits()
	{
		
		subunits_table.clear();
		counter = 0;
		
		doShowItems(dto,"",0);
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
//			else
//			if(dto instanceof MetaUnitMultivaluedEntityDTO)
//			{
//				MetaUnitMultivaluedEntityDTO dto_str = (MetaUnitMultivaluedEntityDTO)dto;
//				subunits_table.setWidget(i++, 0, new MultiValuedEntityViewer(dto_str,null,filling,base_name));
//			}
		//}
	}	
}
