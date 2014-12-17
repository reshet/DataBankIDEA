package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.views.DBviewers.NiceMultiValuedViewer;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.SocioResearchDTO;

import java.util.ArrayList;

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
	@UiField Label weights, textWeights, textMetadataTitle;
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
        this.textWeights.setText(DatabankApp.langConstants.researchDetailedMetadataWeights());
        this.textMetadataTitle.setText(DatabankApp.langConstants.researchDetailedMetadataTitle());
		this.db = dt;
		this.weights.setText(DatabankApp.langConstants.researchDetailedMetadataWeightsNone());
		if(dto.getVar_weight_names()!=null && dto.getVar_weight_names().size()>0)
		{
			String text = "";
			for(String st:dto.getVar_weight_names())
			{
				text+=st+";";
			}
			this.weights.setText(text);
		}
		renderDBfillers();
	}
	
	private void renderDBfillers()
	{
		elasticDBfieldNames.clear();
		NiceMultiValuedViewer mv = new NiceMultiValuedViewer(true,db,dto.getFilling(),"",null);
		elasticDBfieldNames.add(mv);
	}
}
