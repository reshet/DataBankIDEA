package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.presenters.UserSearchPerspectivePresenter;
import com.mresearch.databank.shared.SearchResultDTO;
import com.mresearch.databank.shared.SearchTaskLawDTO;
import com.mresearch.databank.shared.SearchTaskResearchDTO;

import java.util.*;

public class UserSearchPerspectiveView extends Composite implements UserSearchPerspectivePresenter.Display{

	private static UserResearchPerspectiveViewUiBinder uiBinder = GWT
			.create(UserResearchPerspectiveViewUiBinder.class);

	interface UserResearchPerspectiveViewUiBinder extends
			UiBinder<Widget, UserSearchPerspectiveView> {
	}
	
	
	@UiField VerticalPanel centerPanel,root_panel;
	@UiField TextBox contains_one_of,contains_exact,contains_exact_too,contains_or,contains_none_of,not_contains_exact;
	@UiField Button search;
    @UiField Label searchLevel, searchTitle, textContains, textOneOfTheWords, textPhrase, textPhrase2, textAlsoContains,
            textOr, textButNotContains, textNoneOfTheWords, textOrPhrase;
	//@UiField FlexTable res_table;
	//@UiField Label queryStr;
	SimpleResearchList simpleResearchListItem;
	@UiField CheckBox socioresearch,sociovars;//,publications;//laws,consults;
	//@UiField ImprovedSearchView impr_search_view;
	private ArrayList<SearchResultDTO> resultsList;
	public UserSearchPerspectiveView() {
		initWidget(uiBinder.createAndBindUi(this));
        searchTitle.setText(DatabankApp.langConstants.researchSearchTitle());

        textContains.setText(DatabankApp.langConstants.researchSearchInputContains());
        textOneOfTheWords.setText(DatabankApp.langConstants.researchSearchInputOneOfTheWords());
        textPhrase.setText(DatabankApp.langConstants.researchSearchInputFullText());
        textPhrase2.setText(DatabankApp.langConstants.researchSearchInputFullText());
        textAlsoContains.setText(DatabankApp.langConstants.researchSearchInputAlsoContains());
        textOr.setText(DatabankApp.langConstants.researchSearchInputOr());
        textButNotContains.setText(DatabankApp.langConstants.researchSearchInputButNotContains());
        textNoneOfTheWords.setText(DatabankApp.langConstants.researchSearchInputNoneOfTheWords());
        textOrPhrase.setText(DatabankApp.langConstants.researchSearchInputOrFullText());

		search.setText(DatabankApp.langConstants.researchSearchButtonText());
        searchLevel.setText(DatabankApp.langConstants.researchSearchLevel());
        socioresearch.setText(DatabankApp.langConstants.researchSearchLevelResearches());
        sociovars.setText(DatabankApp.langConstants.researchSearchLevelVars());

	}
	private void displaySearchResults()
	{
	}
	@Override
	public void setSearchResultsData(ArrayList<SearchResultDTO> data) {
		resultsList = data;
		displaySearchResults();
	}
	@Override
	public VerticalPanel getCenterPanel() {
		// TODO Auto-generated method stub
		return centerPanel;
	}
	@Override
	public void setQueryStr(String str) {
		//queryStr.setText(str);
		contains_one_of.setText(str);
	}
	@Override
	public void setSearchTaskResearchDTO(SearchTaskResearchDTO dto) {
		//impr_search_view.setSearchTaskResearch(dto);
	}
	@Override
	public void setResearchTabVisible() {
		//impr_search_view.setResearchTabVisible();
	}
	@Override
	public void setGeneralTabVisible() {
		//impr_search_view.setGeneralTabVisible();
	}
	@Override
	public void setLawTabVisible() {
		//impr_search_view.setLawTabVisible();
	}
	@Override
	public void setGeneralQueryText(String query) {
		//impr_search_view.setQueryText(query);
	}
	@Override
	public void setSearchTaskLawDTO(SearchTaskLawDTO dto) {
		//impr_search_view.setSearchTaskLaw(dto);
	}
	public HasClickHandlers getSearchButton() {
		return search;
	}
	@Override
	public String getContainsOneOf() {
		return contains_one_of.getText();
	}
	@Override
	public String getContainsExact() {
		return contains_exact.getText();
	}
	@Override
	public String getContainsExactToo() {
		return contains_exact_too.getText();
	}
	@Override
	public String getContainsOr() {
		return contains_or.getText();
	}
	@Override
	public String getContainsNoneOf() {
		return contains_none_of.getText();
	}
	@Override
	public String getNotContainsExact() {
		return not_contains_exact.getText();
	}
	@Override
	public VerticalPanel asRoot() {
		// TODO Auto-generated method stub
		return root_panel;
	}

	@Override
	public String[] getTypesToSearch() {
		ArrayList<String> types = new ArrayList<String>();
		if(socioresearch.getValue())types.add("research");
		if(sociovars.getValue())types.add("sociovar");
		//if(laws.getValue())types.add("law");
		//if(publications.getValue())types.add("publication");
		//if(consults.getValue())types.add("consultation");
		String [] arr = new String[types.size()];
		types.toArray(arr);
		return arr;
	}

  @Override
  public void setTypesToSearch(String[] types) {
    final Set<String> searchTypes = new HashSet<String>(Arrays.asList(types));
    socioresearch.setValue(searchTypes.contains("research"));
    sociovars.setValue(searchTypes.contains("sociovar"));
    //publications.setValue(searchTypes.contains("publication"));
  }
}
