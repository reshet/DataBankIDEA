package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.presenters.Place;
import com.mresearch.databank.client.presenters.UserResearchPerspectivePresenter;
import com.mresearch.databank.shared.ResearchBundleDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.VarDTO_Light;

import java.util.ArrayList;
import java.util.List;

public class UserResearchPerspectiveView extends Composite implements UserResearchPerspectivePresenter.Display, Place{

	private static UserResearchPerspectiveViewUiBinder uiBinder = GWT
			.create(UserResearchPerspectiveViewUiBinder.class);

	interface UserResearchPerspectiveViewUiBinder extends
			UiBinder<Widget, UserResearchPerspectiveView> {
	}
	
	@Override
	public String getPlaceName() {
		return "user-research";
	}
	
	@UiField VerticalPanel centerPanel;
//	VerticalPanel centralPanel;
	@UiField Tree tree,F_S_tree,F_V_tree,My_researches_tree;
    @UiField Label filterTabHeader, myResearchesTabHeader;
	@UiField ScrollPanel centerChild;
	@UiField SplitLayoutPanel split_panel;
	//@UiField DecoratedTabPanel decortab;
	@UiField TabLayoutPanel tabLayout;
	SimpleResearchList simpleResearchListItem;
	UserOwnResearchList user_own_ResearchListItem;
	UserOwnAnalisysList analisys_list;
	RootConceptsList rootResearchConcepts, rootVarConcepts;
	private Long research_to_find =null;
	private List<SocioResearchDTO_Light> researchList;
	public UserResearchPerspectiveView(SimpleEventBus bus) {
		initWidget(uiBinder.createAndBindUi(this));
		if (DatabankApp.get().getCurrentUser().getId()!=0) {
          tabLayout.getTabWidget(2).getParent().setVisible(true);
        } else {
          tabLayout.getTabWidget(2).getParent().setVisible(false);
        }

        tree.setStyleName("research_section");
        filterTabHeader.setText(DatabankApp.langConstants.researchMainFilterHeader());
        myResearchesTabHeader.setText(DatabankApp.langConstants.researchMainMyResearchesHeader());

        split_panel.setWidgetMinSize(centerChild, 720);
        simpleResearchListItem = new SimpleResearchList();
        user_own_ResearchListItem = new UserOwnResearchList();
        analisys_list = new UserOwnAnalisysList();
        tree.addItem(simpleResearchListItem);
        My_researches_tree.setStyleName("research_section");
        My_researches_tree.addItem(user_own_ResearchListItem);
        My_researches_tree.addItem(analisys_list);

        rootResearchConcepts = new RootConceptsList("socioresearch", DatabankApp.langConstants.researchMainConceptsResearch());
        tree.addItem(rootResearchConcepts);

		rootVarConcepts = new RootConceptsList("sociovar", DatabankApp.langConstants.researchMainConceptsVars());
		tree.addItem(rootVarConcepts);
		
		RootFilterItemAdvanced f1 = new RootFilterItemAdvanced(this,bus,"socioresearch", DatabankApp.langConstants.researchMainFilterResearch());
		F_S_tree.addItem(f1);
		RootFilterItemAdvanced f2 = new RootFilterItemAdvanced(this,bus,"sociovar", DatabankApp.langConstants.researchMainFilterVars());
		F_V_tree.addItem(f2);

	}
	private void displayResearchList()
	{
		simpleResearchListItem.removeItems();
		for(SocioResearchDTO_Light dto:researchList)
		{
			ResearchDescItem research_node = new ResearchDescItem(dto);
			simpleResearchListItem.addItem(research_node);
			if (research_to_find!= null && research_node.getContents_id() == research_to_find)
			{
				research_node.setSelected(true);
				tree.setSelectedItem(research_node);
				research_to_find = null;
			}
		}
	}
	
	@Override
	public HasMouseDownHandlers getTree() {
		return tree;
	}
	@Override
	public HasClickHandlers getResearchItem(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HasClickHandlers getVarItem(int index)  {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setResearchListData(List<SocioResearchDTO_Light> data) {
		researchList = data;
		displayResearchList();
	}
	@Override
	public void setVarListData(TreeItem item, ArrayList<VarDTO_Light> data) {
		item.removeItems();
		for(VarDTO_Light dto:data)
		{
			VarDescItem var_node = new VarDescItem(dto,VarDescItem.LABEL_SHORTAGE_NUMBER);
			//var_node.addItem(dto.getLabel());
			item.addItem(var_node);
		}
	}
	@Override
	public void showLoadingLabel() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public TreeItem getSelectedItem() {
		// TODO Auto-generated method stub
		return tree.getSelectedItem();
	}
	@Override
	public void showResearchDetailes(final ResearchBundleDTO dto) {
		centerPanel.clear();
		UserResearchDescriptionView desc = new UserResearchDescriptionView(dto.getDto());
		UserResearchDetailedView view = new UserResearchDetailedView(dto.getDto(),dto.getMeta());
		UserResearchAdvancedFilesView files = new UserResearchAdvancedFilesView(dto.getDto().getID(),dto.getFiles_dto());
		UserResearchVarsView vars_v = new UserResearchVarsView(dto.getDto());
		UserResearchDetailedFrameView frame = new UserResearchDetailedFrameView(desc,view, files,vars_v);
	
		centerPanel.add(frame);
		}
	
	
	@Override
	public HasOpenHandlers<TreeItem> getTreeForOpen() {
		return tree;
	}
	@Override
	public VerticalPanel getCenterPanel() {
		// TODO Auto-generated method stub
		return centerPanel;
	}
	@Override
	public void findInResearchList(Long id) {
		simpleResearchListItem.setState(true);
		research_to_find = id;
	}
	@Override
	public HasSelectionHandlers<TreeItem> getTreeForSelection() {
		return tree;
	}
	@Override
	public Tree getTreeWhole() {
		// TODO Auto-generated method stub
		return tree;
	}
	@Override
	public HasOpenHandlers<TreeItem> getUserResearchTreeForOpen() {
		return My_researches_tree;
	}
	@Override
	public HasSelectionHandlers<TreeItem> getUserResearchTreeForSelection() {
		return My_researches_tree;
	}
	@Override
	public void setUserOwnResearchListData(List<SocioResearchDTO_Light> data) {
		
			user_own_ResearchListItem.removeItems();
			for(SocioResearchDTO_Light dto:data)
			{
				ResearchDescItem research_node = new ResearchDescItem(dto);
				//research_node.addItem(new ResearchMetadataItem(dto));
				//research_node.addItem(new ResearchVarList(dto));
				//for(String )
				user_own_ResearchListItem.addItem(research_node);
				if (research_to_find!= null && research_node.getContents_id() == research_to_find)
				{
					research_node.setSelected(true);
					My_researches_tree.setSelectedItem(research_node);
					//tree.fireEvent();
					research_to_find = null;
				}
			}
	}

	@Override
	public void setAllAnalysisListData(List<UserAnalysisSaveDTO> data) {
		analisys_list.removeItems();
		for(UserAnalysisSaveDTO dto:data)
		{
			AnalysisDescItem research_node = new AnalysisDescItem(dto);
			analisys_list.addItem(research_node);
			
		}
	}
	@Override
	public TreeItem getUserOwnSelectedItem() {
		return My_researches_tree.getSelectedItem();
	}
	
}
