package com.mresearch.databank.client.presenters;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.json.client.*;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mresearch.databank.client.event.ShowResearchDetailsEvent;
import com.mresearch.databank.client.event.ShowResearchDetailsEventHandler;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.SearchServiceAsync;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.client.views.SearchResultsGenericGrid;
import com.mresearch.databank.client.views.SearchResultsGrid;
import com.mresearch.databank.shared.*;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSearchPerspectivePresenter implements Presenter
{
	 public interface Display {
//		 HasMouseDownHandlers getTree();
//		 HasOpenHandlers<TreeItem> getTreeForOpen();
//		 
//		 HasClickHandlers getResearchItem(int index);
//		 HasClickHandlers getVarItem(int index);
		 void setSearchResultsData(ArrayList<SearchResultDTO> data);
		 void setSearchTaskResearchDTO(SearchTaskResearchDTO dto);
		 void setSearchTaskLawDTO(SearchTaskLawDTO dto);
		 void setGeneralTabVisible();
		 void setGeneralQueryText(String query);
		 void setResearchTabVisible();
		 void setLawTabVisible();
//		 void setVarListData(TreeItem item, ArrayList<VarDTO> data);
		 VerticalPanel asRoot();
//		 void showLoadingLabel();
//		 TreeItem getSelectedItem();
//		 void showResearchDetailes(SocioResearchDTO dto);
		 VerticalPanel getCenterPanel();
		 void setQueryStr(String str);
		 HasClickHandlers getSearchButton();
		 String getContainsOneOf();
		 String getContainsExact();
		 String getContainsExactToo();
		 String getContainsOr();
		 String getContainsNoneOf();
		 String getNotContainsExact();
		 String [] getTypesToSearch();
     void setTypesToSearch(String [] types);
	 }
	 private final UserSocioResearchServiceAsync rpcService;
	 private final SearchServiceAsync searchService;
	 private String qu = null;
	 private final SimpleEventBus eventBus;
	 private final Display display;
	 private HashMap<String, String> mapping = new HashMap();
	 
	 
	
	 public UserSearchPerspectivePresenter(SearchServiceAsync searchService, UserSocioResearchServiceAsync rpcService,SimpleEventBus eventBus,
		      Display view) {
		 	//this.query = query;
		    this.rpcService = rpcService;
		    this.eventBus = eventBus;
		    this.display = view;
		    this.searchService = searchService;
		    bind();
		  }
	 
	@Override
	public void go(HasWidgets container,ArrayList<String> p_names,ArrayList<String> p_values) {
		
		
//		if(p_names.contains("advancedSearchResearch"))
//		{
//			SearchTaskResearchDTO dto = new SearchTaskResearchDTO();
//			dto.deserialize(p_names,p_values);
//			display.setSearchTaskResearchDTO(dto);
//			display.setResearchTabVisible();
//			//fetchSearchResultsDataAdvancedResearch(dto);
//		} else
//		if(p_names.contains("advancedSearchLaw"))
//		{
//			SearchTaskLawDTO dto = new SearchTaskLawDTO();
//			dto.deserialize(p_names,p_values);
//			display.setSearchTaskLawDTO(dto);
//			display.setLawTabVisible();
//			//fetchSearchResultsDataAdvancedLaw(dto);
//		}
		 container.clear();
//		 ScrollPanel panel_scr = new ScrollPanel();
//		 panel_scr.setHeight("100%");
//		 panel_scr.setWidth("100%");
//		 VerticalPanel panel = new VerticalPanel();
//		 panel.setHeight("500px");
//		 panel.setWidth("100%");
		 container.add(display.asRoot());
//		 panel.add(new ImprovedSearchView());
//		 panel_scr.add(panel);
//		 container.add(panel_scr);
		 if (p_names.contains("query"))
			{
				this.qu = p_values.get(p_names.indexOf("query"));
				 display.setQueryStr(this.qu);

				//display.setGeneralTabVisible();
				//display.setGeneralQueryText(this.qu);
				getMappingStructure();
			}
    if (p_names.contains("types")) {
      String types = p_values.get(p_names.indexOf("types"));
      this.display.setTypesToSearch(types.split(","));
    }
	}

  public void bind()
	{	
		eventBus.addHandler(ShowResearchDetailsEvent.TYPE, new ShowResearchDetailsEventHandler() {
			@Override
			public void onShowResearchDetails(ShowResearchDetailsEvent event) {
				display.getCenterPanel().clear();
				display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
			//	fetchResearchDetailes(event.getResearch_id());
			}
		});
		//eventBus.
		display.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				getMappingStructure();
			}
		});
//		eventBus.addHandler(ShowStartPageMainEvent.TYPE, new ShowStartPageMainEventHandler() {
//			@Override
//			public void onShowStartPageMain(ShowStartPageMainEvent event) {
//				showMainPageArticle();
//			}
//		});
	}

	private void getMappingStructure()
	{
		if(display.getTypesToSearch().length>0 &&display.getTypesToSearch().length<=1)
		{
			
			String type = display.getTypesToSearch()[0];
			if(type.equals("research")) type = "socioresearch";
			final String type_def = type;
			new RPCCall<MetaUnitMultivaluedEntityDTO>()
		    {
		      public void onFailure(Throwable caught)
		      {
		        Window.alert("Error fetching databank structure " + caught.getMessage());
		      }

		      protected void callService(AsyncCallback<MetaUnitMultivaluedEntityDTO> cb)
		      {
		        UserSocioResearchService.Util.getInstance().getDatabankStructure(type_def, cb);
		      }

		      public void onSuccess(MetaUnitMultivaluedEntityDTO result)
		      {
		        buildMapping(result.getUnique_name() + "_", result.getSub_meta_units(),mapping);
		        fetchSearchResultsData();
		      }
		    }.retry(2);
		}
		else if(display.getTypesToSearch().length>1)
		{
			fetchSearchResultsData();
		}
		
		
	}
	
	private void fetchSearchResultsData()
	{
		JSONObject obj_bool = new JSONObject();
	      JSONObject obj_must = new JSONObject();
	      JSONArray arr_must = new JSONArray();
	      
	      JSONObject obj_bool_contains = new JSONObject();
	      JSONObject obj_contains = new JSONObject();
	      JSONArray arr_contains = new JSONArray();
	      
	      JSONObject obj_bool_contains_too = new JSONObject();
	      JSONObject obj_contains_too = new JSONObject();
	      JSONArray arr_contains_too = new JSONArray();
	      
	      JSONObject obj_bool_not_contains = new JSONObject();
	      JSONObject obj_not_contains = new JSONObject();
	      JSONArray arr_not_contains = new JSONArray();
	      
	      int index_c=0,index_c2=0,index_c3=0;
	      if(display.getContainsOneOf().length()>0)
	      {
	    	  JSONObject obj_b = new JSONObject();
		      JSONObject obj_text = new JSONObject();
		      obj_text.put("_all", new JSONString(display.getContainsOneOf()));
		      obj_b.put("match_phrase_prefix", obj_text);
		      //obj_b.put("prefix", obj_text);
		      
		      
		      arr_contains.set(index_c++, obj_b);

          String searchTypes = joinStringBy(display.getTypesToSearch(),",");
          History.newItem("search-results@query="+display.getContainsOneOf() + "&types=" + searchTypes.toString());
	      }if(display.getContainsExact().length()>0)
	      {
	    	  JSONObject obj_b = new JSONObject();
		      JSONObject obj_text = new JSONObject();
		      obj_text.put("_all", new JSONString(display.getContainsExact()));
		      obj_b.put("text_phrase", obj_text);
		      arr_contains.set(index_c++, obj_b);
		  }
	      
	      if(display.getContainsExactToo().length()>0)
	      {
	    	  JSONObject obj_b = new JSONObject();
		      JSONObject obj_text = new JSONObject();
		      obj_text.put("_all", new JSONString(display.getContainsExactToo()));
		      obj_b.put("text_phrase", obj_text);
		      arr_contains_too.set(index_c2++, obj_b);			    
	      }
	      if(display.getContainsOr().length()>0)
	      {
	    	  JSONObject obj_b = new JSONObject();
		      JSONObject obj_text = new JSONObject();
		      obj_text.put("_all", new JSONString(display.getContainsOr()));
		      obj_b.put("match_phrase_prefix", obj_text);
		      arr_contains_too.set(index_c2++, obj_b);
	      }

	      if(display.getNotContainsExact().length()>0)
	      {
	    	  JSONObject obj_b = new JSONObject();
		      JSONObject obj_text = new JSONObject();
		      obj_text.put("_all", new JSONString(display.getNotContainsExact()));
		      obj_b.put("text_phrase", obj_text);
		      arr_not_contains.set(index_c3++, obj_b);
	      }

	      if(display.getContainsNoneOf().length()>0)
	      {
	    	  JSONObject obj_b = new JSONObject();
		      JSONObject obj_text = new JSONObject();
		      obj_text.put("_all", new JSONString(display.getContainsNoneOf()));
		      obj_b.put("match_phrase_prefix", obj_text);
		      arr_not_contains.set(index_c3++, obj_b);
	      }
	     
	      
	      obj_contains.put("must", arr_contains);
	      obj_bool_contains.put("bool", obj_contains);
	      
	      obj_contains_too.put("should", arr_contains_too);
	      obj_bool_contains_too.put("bool", obj_contains_too);

	      obj_not_contains.put("must_not", arr_not_contains);
	      obj_bool_not_contains.put("bool", obj_not_contains);

	      if(arr_contains.size()>0)arr_must.set(0, obj_bool_contains);
	      if(arr_contains_too.size()>0)arr_must.set(1, obj_bool_contains_too);
	      if(arr_not_contains.size()>0)arr_must.set(2, obj_bool_not_contains);
	      
	      obj_must.put("must", arr_must);
	      obj_bool.put("bool", obj_must);

//	      String quer = obj_bool.toString();

		
		
		
//OLD PLANE QUERY		
//		  JSONObject obj_b = new JSONObject();
//	      JSONObject obj_text = new JSONObject();
//	      obj_text.put("_all", new JSONString(this.qu));
//	      obj_b.put("text", obj_text);

	      final String query = obj_bool.toString();
	      
		new RPCCall<String>()
	    {
	      public void onFailure(Throwable caught)
	      {
	        display.getCenterPanel().clear();
	        display.getCenterPanel().add(new Label("Error on performing search!    " + caught.getMessage()));
	      }

	      public void onSuccess(String result)
	      {
	    	
	    	  display.getCenterPanel().clear();
	        if(result.equals("Error"))
	        {
	        	display.getCenterPanel().add(new HTML("<H2>ПОИСКОВЫЙ ЗАПРОС:</H2><br><p>" + query + "</p>"));
	        	display.getCenterPanel().add(new HTML("<H3>ОТВЕТ ДВИЖКА:</H3><br><p>" + result + "</p>"));
	             return;
	        }
	        ArrayList<JSONObject> hiters = new ArrayList<JSONObject>();
	        JSONObject res = (JSONObject)JSONParser.parse(result);
	        JSONObject hits = (JSONObject)res.get("hits");
	        JSONNumber total = (JSONNumber)hits.get("total");
	        Integer tot = Integer.valueOf((int)total.getValue());
	        JSONArray hits_arr = (JSONArray)hits.get("hits");
	        if(hits_arr.size()==0)
	        {
	        	 display.getCenterPanel().clear();
	        	 display.getCenterPanel().add(new HTML("<H2>По вашему запросу ничего не найдено. Попробуйте изменить параметры поиска</H2>"));
	             return;
	        }
	        for (int i = 0; i < hits_arr.size(); i++)
	        {
	          JSONObject hit = (JSONObject)hits_arr.get(i);
	          hiters.add(hit);
	        }

	        
	        
	        
	        
	        
	        //display.getCenterPanel().add(new HTML("<H2>ПОИСКОВЫЙ ЗАПРОС:</H2><br><p>" + query + "</p>"));
	        //display.getCenterPanel().add(new HTML("<H3>ОТВЕТ ДВИЖКА:</H3><br><p>" + result + "</p>"));
	        if(display.getTypesToSearch().length == 1)
	        {
	        	if(display.getTypesToSearch()[0].equals("sociovar"))display.getCenterPanel().add(new SearchResultsGrid(true,null,true,1024,eventBus,tot, hiters, mapping));
	        	else display.getCenterPanel().add(new SearchResultsGrid(false,null,true,1024,eventBus,tot, hiters, mapping));
	        }
	        if(display.getTypesToSearch().length > 1)
	        {
	        	display.getCenterPanel().add(new SearchResultsGenericGrid(eventBus,tot, hiters));
	        }
	 
	      }

	      protected void callService(AsyncCallback<String> cb)
	      {
	        UserSocioResearchService.Util.getInstance().doIndexSearch(query,display.getTypesToSearch(), cb);
	      }
	    }
	    .retry(2);
		
	    
	    
		
	    
		
//		new RPCCall<ArrayList<SearchResultDTO>>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				Window.alert("Error fetching " +
//						" search results: "
//			            + caught.getMessage());
//			}
//
//			@Override
//			public void onSuccess(ArrayList<SearchResultDTO> result) {
//				display.setSearchResultsData(result);
//			}
//
//			@Override
//			protected void callService(
//					AsyncCallback<ArrayList<SearchResultDTO>> cb) {
//				searchService.performSearch(query, cb);
//			}
//		}.retry(3);
	}

  public static String joinStringBy(String [] strArray, String joiner) {
    StringBuilder searchTypes = new StringBuilder();
    for(int i = 0; i < strArray.length; i++) {
      searchTypes.append(strArray[i]);
      if (i < strArray.length - 1) {
        searchTypes.append(joiner);
      }
    }
    return searchTypes.toString();
  }

  private void buildMapping(String base_name, ArrayList<MetaUnitDTO> subunits, HashMap<String, String> map)
	  {
		    for (MetaUnitDTO dto : subunits)
		    {
		      map.put(base_name + dto.getUnique_name(), dto.getDesc());
		      
		     
		      
		      if ((dto instanceof MetaUnitMultivaluedStructureDTO))
		      {
		        MetaUnitMultivaluedStructureDTO ddto = (MetaUnitMultivaluedStructureDTO)dto;
		        buildMapping(base_name + ddto.getUnique_name() + "_", ddto.getSub_meta_units(), map);  
		      }
		      
		  }
	  }

	
	
	private void fetchSearchResultsDataAdvancedResearch(final SearchTaskResearchDTO dto)
	{
		new RPCCall<ArrayList<SearchResultDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching " +
						" search results: "
			            + caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<SearchResultDTO> result) {
				display.setSearchResultsData(result);
			}

			@Override
			protected void callService(
					AsyncCallback<ArrayList<SearchResultDTO>> cb) {
				searchService.performAdvancedSearchResearch(dto, cb);
			}
		}.retry(3);
	}
	private void fetchSearchResultsDataAdvancedLaw(final SearchTaskLawDTO dto)
	{
		new RPCCall<ArrayList<SearchResultDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching " +
						" search results: "
			            + caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<SearchResultDTO> result) {
				display.setSearchResultsData(result);
			}

			@Override
			protected void callService(
					AsyncCallback<ArrayList<SearchResultDTO>> cb) {
				searchService.performAdvancedSearchLaw(dto, cb);
			}
		}.retry(3);
	}
}
