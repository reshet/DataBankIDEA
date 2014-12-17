package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.TreeItem;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.service.CatalogService;
import com.mresearch.databank.client.service.CatalogServiceAsync;
import com.mresearch.databank.shared.ICatalogizationConcept;
import com.mresearch.databank.shared.SocioResearchDTO_Light;

public class ResearchVarList extends TreeItem implements ICatalogizationConcept{
	private final CatalogServiceAsync catalogService = GWT
    .create(CatalogService.class);
	private long research_id;
	public ResearchVarList(SocioResearchDTO_Light dto)
	{
		super();
		this.research_id = dto.getId();
		this.setText(DatabankApp.langConstants.researchDetailedVarsList());
		addItem(DatabankApp.langConstants.researchMainListLoad());
	}
	@Override
	public void refreshContents() {
//		catalogService.getResearchVarList(research_id,new AsyncCallback<ArrayList<VarDTO>>() {
//			@Override
//			public void onSuccess(ArrayList<VarDTO> result) {
//				ResearchVarList.this.removeItems();
//				for(VarDTO dto:result)
//				{
//					TreeItem var_node = new TreeItem(dto.getCode());
//					var_node.addItem(dto.getLabel());
//					//for(String )
//					ResearchVarList.this.addItem(var_node);
//				}
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//			
//				Window.alert("Failure on refresh contents of catalog");
//			}
//		});
	}
	public long getResearch_id() {
		return research_id;
	}

}
