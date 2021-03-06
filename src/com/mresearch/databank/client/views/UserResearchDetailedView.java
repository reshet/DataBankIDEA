package com.mresearch.databank.client.views;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.mresearch.databank.client.views.DBfillers.MultiValuedField;
import com.mresearch.databank.client.views.DBviewers.MultiValuedFieldViewer;
import com.mresearch.databank.client.views.DBviewers.NiceMultiValuedFieldViewer;
import com.mresearch.databank.client.views.DBviewers.NiceMultiValuedValuesViewer;
import com.mresearch.databank.client.views.DBviewers.NiceMultiValuedViewer;
import com.mresearch.databank.shared.MetaUnitEntityItemDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.SearchTaskResearchDTO;
import com.mresearch.databank.shared.SocioResearchDTO;

public class UserResearchDetailedView extends Composite {

	private static UserResearchDetailedViewUiBinder uiBinder = GWT
			.create(UserResearchDetailedViewUiBinder.class);

	interface UserResearchDetailedViewUiBinder extends
			UiBinder<Widget, UserResearchDetailedView> {
	}

	public UserResearchDetailedView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	@UiField VerticalPanel elasticDBfieldNames;
	//;,elasticDBfieldValues;
	@UiField Label weights;
	public static String arrToStr(ArrayList<String> data)
	{
		String conc = "";
		if (data == null) return conc;
			
		for(int i = 0; i < data.size()-1;i++)
		{
			conc+=data.get(i)+",";
		}
		if(data.size()>0)conc+=data.get(data.size()-1);
		return conc;
	}
	private MetaUnitMultivaluedEntityDTO db;
	private SocioResearchDTO dto;
	public UserResearchDetailedView(SocioResearchDTO dto,MetaUnitMultivaluedEntityDTO dt)
	{
		this();
		this.dto = dto;
//		nameResearch.setText(dto.getName());
//		orgPrompt.setText(dto.getOrg_order_name());
//		orgImpl.setText(dto.getOrg_impl_name());
//		this.concepts.setText(arrToStr(dto.getConcepts()));
	//	this.researchers.setText(arrToStr(dto.getResearchers()));
		this.db = dt;
		
		
		//this.publ.setText(arrToStr(dto.getPublications()));
		int i = 0;
//		if(dto.getPublications()!= null)
//		for (String publName:dto.getPublications())
//		{
//			String doi = null;
//			if(i < dto.getPublications_dois().size())doi = dto.getPublications_dois().get(i);
//			String url = null;
//			if(i < dto.getPublications_urls().size())url = dto.getPublications_urls().get(i);
//			
//			VerticalPanel panel = new VerticalPanel();
//			panel.add(new Label(publName));
//			panel.add(new HTML("<a href=\"http://dx.doi.org/"+doi+"\" align=\"right\" target=\"_blank\">"+doi+"</a>"));
//			panel.add(new HTML("<a href=\""+url+"\" align=\"right\" target=\"_blank\">"+"Перейти"+"</a>"));
//			//flexPubl_tbl.setWidget(i, 0, panel);	
//			i++;
//		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		if(dto.getStart_date() != null && dto.getEnd_date() != null)
//			this.dateResearch.setText(dto.getStart_date().toString()+" - "+dto.getEnd_date().toString());
//		this.genGeathering.setText(dto.getGen_geathering());
		//this.selectionSize.setText(String.valueOf(dto.getSelection_size()));
		String sel_appr = "";
//		if (dto.getSel_randomity() != null && dto.getSel_randomity().equals(SearchTaskResearchDTO.SELECTION_APPR_RANDOM)) sel_appr +="Случайная";
//			else sel_appr +="Не случайная";
//		if(dto.getSel_complexity() != null && dto.getSel_complexity().equals(SearchTaskResearchDTO.SELECTION_APPR_COMPLEXITY_UNI)) sel_appr +=", одноступенчатая";
//			else sel_appr +=", многоступенчатая";

//		this.selectionAppr.setText(sel_appr);
//		//this.selectionAppr.setText(dto.getSelection_appr());
//		this.method.setText(dto.getMethod());
		this.weights.setText("Без взвешивания");
		if(dto.getVar_weight_names()!=null && dto.getVar_weight_names().size()>0)
		{
			String text = "";
			for(String st:dto.getVar_weight_names())
			{
				text+=st+";";
			//	this.weights.addItem(st);
			}
			this.weights.setText(text);
		}
		//this.weights.setSelectedIndex(0);
		//orgImpl.setText(text);
		renderDBfillers();
	}
	
	private void renderDBfillers()
	{
		elasticDBfieldNames.clear();
		//elasticDBfieldValues.clear();
		//ArrayList<String> exclude = new ArrayList<String>();
		NiceMultiValuedViewer mv = new NiceMultiValuedViewer(true,db,dto.getFilling(),"",null);
		//NiceMultiValuedFieldViewer mv = new NiceMultiValuedFieldViewer(db,dto.getFilling(),"");
		//NiceMultiValuedValuesViewer mv2 = new NiceMultiValuedValuesViewer(db,dto.getFilling(),"");
		
		
		elasticDBfieldNames.add(mv);
		//elasticDBfieldValues.add(mv2);
		
	}
}
