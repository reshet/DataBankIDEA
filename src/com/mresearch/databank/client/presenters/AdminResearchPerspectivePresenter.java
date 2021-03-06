package com.mresearch.databank.client.presenters;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.event.AddResearchDisabledEvent;
import com.mresearch.databank.client.event.AddResearchDisabledEventHandler;
import com.mresearch.databank.client.event.AddResearchEnabledEvent;
import com.mresearch.databank.client.event.AddResearchEnabledEventHandler;
import com.mresearch.databank.client.event.CreateConceptDisabledEventHandler;
import com.mresearch.databank.client.event.CreateConceptEnabledEvent;
import com.mresearch.databank.client.event.CreateConceptDisabledEvent;
import com.mresearch.databank.client.event.CreateConceptEnabledEventHandler;
import com.mresearch.databank.client.event.CreateConceptEvent;
import com.mresearch.databank.client.event.CreateConceptEventHandler;
import com.mresearch.databank.client.event.ShowPlaceEvent;
import com.mresearch.databank.client.event.ShowResearchDetailsEvent;
import com.mresearch.databank.client.event.ShowResearchDetailsEventHandler;
import com.mresearch.databank.client.event.ShowStartPageMainEvent;
import com.mresearch.databank.client.event.ShowStartPageMainEventHandler;
import com.mresearch.databank.client.event.ShowVarDetailsEvent;
import com.mresearch.databank.client.event.ShowVarDetailsEventHandler;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.AdminSocioResearchServiceAsync;
import com.mresearch.databank.client.service.CatalogService;
import com.mresearch.databank.client.service.CatalogServiceAsync;
import com.mresearch.databank.client.service.UserSocioResearchService;
//import com.mresearch.databank.client.service.StartPageServiceAsync;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.client.views.AdminResearchAdvancedFilesEditView;
import com.mresearch.databank.client.views.AdminResearchDetailedView;
import com.mresearch.databank.client.views.AdminResearchEditView;
import com.mresearch.databank.client.views.AdminResearchFilesEditView;
import com.mresearch.databank.client.views.AdminResearchGroupEditView;
import com.mresearch.databank.client.views.AdminResearchVarGeneralizeS1View;
import com.mresearch.databank.client.views.AdminResearchVarsView;
import com.mresearch.databank.client.views.ConceptContentsItem;
import com.mresearch.databank.client.views.ConceptItem;
import com.mresearch.databank.client.views.ConceptItemEntity;
import com.mresearch.databank.client.views.ConceptItemItem;
import com.mresearch.databank.client.views.IPickBinder;
import com.mresearch.databank.client.views.PickElementsTableView;
import com.mresearch.databank.client.views.RealVariableDetailedView;
import com.mresearch.databank.client.views.RealVariableEditView;
import com.mresearch.databank.client.views.ResearchDescItem;
import com.mresearch.databank.client.views.ResearchVarList;
import com.mresearch.databank.client.views.RootConceptsList;
import com.mresearch.databank.client.views.SearchResultsView;
import com.mresearch.databank.client.views.SimpleResearchList;
import com.mresearch.databank.client.views.TextVariableDetailedView;
import com.mresearch.databank.client.views.TextVariableEditView;
import com.mresearch.databank.client.views.UserResearchDetailedView;
import com.mresearch.databank.client.views.UserResearchVar2DDView;
import com.mresearch.databank.client.views.VarDescItem;
import com.mresearch.databank.client.views.VariableDetailedView;
import com.mresearch.databank.client.views.VariableEditView;
import com.mresearch.databank.client.views.WrappedCustomLabel;
import com.mresearch.databank.client.views.addResearchUI;
//import com.mresearch.databank.server.CatalogServiceImpl;
//import com.mresearch.databank.server.domain.SocioResearch;
import com.mresearch.databank.shared.ArticleDTO;
import com.mresearch.databank.shared.CatalogConceptDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedStructureDTO;
import com.mresearch.databank.shared.NewsDTO;
import com.mresearch.databank.shared.NewsSummaryDTO;
import com.mresearch.databank.shared.RealVarDTO_Detailed;
import com.mresearch.databank.shared.ResearchBundleDTO;
import com.mresearch.databank.shared.ShowCatalogParameters;
import com.mresearch.databank.shared.SocioResearchDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.TextVarDTO_Detailed;
import com.mresearch.databank.shared.VarDTO;
import com.mresearch.databank.shared.VarDTO_Detailed;
import com.mresearch.databank.shared.VarDTO_Light;
import com.mresearch.databank.shared.ICatalogizable;
import com.mresearch.databank.shared.IPickableElement;
import com.mresearch.databank.shared.ConceptBinder;


import com.sun.java.swing.plaf.windows.resources.windows;

public class AdminResearchPerspectivePresenter implements Presenter
{
	
	
	 public interface Display {
		 Tree getTreeWhole();
		 HasMouseDownHandlers getTree();
		 
		 HasOpenHandlers<TreeItem> getTreeForOpen();
		 HasSelectionHandlers<TreeItem> getTreeForSelection();
		 
		 HasClickHandlers getResearchItem(int index);
		 HasClickHandlers getVarItem(int index);
		 void setResearchListData(ArrayList<SocioResearchDTO_Light> data);
		 void setVarListData(TreeItem item, ArrayList<VarDTO_Light> data);
		 Widget asWidget();
		 void showLoadingLabel();
		 TreeItem getSelectedItem();
		// String getSelectedConceptID();
		// void showResearchDetailes(AdminResearchDetailedPresenter presenter);
		 VerticalPanel getCenterPanel();
		 //HasClickHandlers getEditButton();
		 //HasClickHandlers getDeleteButton();
		 HasEnabled getAddResearchBtn();
		 HasClickHandlers getAddResearchBt();
		 //HasEnabled getCreateConceptBtn();
		 //HasEnabled getDeleteConceptBtn();
		 //HasClickHandlers getCreateConceptBt();
		 VerticalPanel asRoot();
		 void showCreateConceptPopup(int x, int y,String c_type);
		 void hideConceptPopup();
		 void setRootConceptUpdateMode(boolean isRoot);
		 Widget getPrevCenterState();
		 void setPrevCenterState(Widget w);
		 
	 }
	
	 
	 private final UserSocioResearchServiceAsync rpcUserService;
	 private final AdminSocioResearchServiceAsync rpcAdminService;
	 private final CatalogServiceAsync rpcCatalogService;
	 
	 private final SimpleEventBus eventBus;
	 private final Display display;
	 
	 public AdminResearchPerspectivePresenter(UserSocioResearchServiceAsync rpcUserService, AdminSocioResearchServiceAsync rpcAdminService, SimpleEventBus eventBus,
		      Display view) {
		    this.rpcUserService = rpcUserService;
		    this.rpcAdminService = rpcAdminService;
		    this.eventBus = eventBus;
		    this.display = view;
		    this.rpcCatalogService = GWT.create(CatalogService.class);
		    bind();
		  }
	@Override
	public void go(HasWidgets container,ArrayList<String> p_names,ArrayList<String> p_values) {
		 container.clear();
		 container.add(display.asWidget());
		 fetchResearchListData();
		 //fetchRootCatalogConcepts();
	}
	
	
	
	
	
	
	public void bind()
	{
		display.getTreeForSelection().addSelectionHandler(new SelectionHandler<TreeItem>() {
			private WrappedCustomLabel prevRes;
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem it = display.getSelectedItem();
				UserResearchPerspectivePresenter.removeParentStylesRoot(display.getTreeWhole());
				if(prevRes!=null)prevRes.getLabel().removeStyleDependentName("selected");
			
				if (it instanceof SimpleResearchList)
				{
					//fetchResearchListData();
					//eventBus.fireEvent(new AddResearchEnabledEvent());
				}else if (it instanceof ResearchDescItem)
				{
					ResearchDescItem rv = (ResearchDescItem)it;
					rv.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rv;
					
					//fetchResearchVarData(it, rv.getResearch_id());
					eventBus.fireEvent(new ShowResearchDetailsEvent(rv.getContents_id()));
				//	eventBus.fireEvent(new AddResearchDisabledEvent());
				}else if (it instanceof VarDescItem)
				{
					VarDescItem rv = (VarDescItem)it;
					rv.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rv;
					
					//fetchResearchVarData(it, rv.getResearch_id());
					eventBus.fireEvent(new ShowVarDetailsEvent(rv.getVar_id()));
				//	eventBus.fireEvent(new AddResearchDisabledEvent());
				}else if (it instanceof RootConceptsList)
				{
					//RootConceptsList rcl = (RootConceptsList)it;
					//rcl.refreshContents();
					//eventBus.fireEvent(new CreateConceptEnabledEvent(true));
				}
				else if (it instanceof ResearchVarList)
				{
					ResearchVarList rv = (ResearchVarList)it;
					display.getCenterPanel().clear();	
					display.getCenterPanel().add(new AdminResearchVarGeneralizeS1View(rv.getResearch_id(), display.getCenterPanel()));
				}
				else if (it instanceof ConceptItemItem)
				{
					//eventBus.fireEvent(new CreateConceptEnabledEvent(true));
					display.getCenterPanel().clear();
					display.getCenterPanel().add(new HTML("<h1>Загрузка, подождите...</h1>"));
					final long concept_id = ((ConceptItemItem)it).getEntity_id();
					final ConceptItemItem rcl = (ConceptItemItem)it;
					rcl.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rcl;
					rcl.refreshTaggedEntitiesIDs(new ConceptItemItem.AsyncAction() {
						@Override
						public void doAction() {
							//eventBus.fireEvent(new ShowPlaceEvent("user-research", "catalog", new ShowCatalogParameters(rcl.getAll_tagged_ids(), rcl.getAll_tagged_idents())));
						}
					});
				}
				else if (it instanceof ConceptItemEntity)
				{
					final ConceptItemEntity rcl = (ConceptItemEntity)it;
					rcl.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rcl;
					//rcl.refreshContents();
					//eventBus.fireEvent(new CreateConceptEnabledEvent(false));
				}
				else if (it instanceof ConceptItem<?>)
				{
					//eventBus.fireEvent(new CreateConceptEnabledEvent(false));
					display.getCenterPanel().clear();
					display.getCenterPanel().add(new HTML("<h1>Загрузка, подождите...</h1>"));
					final long concept_id = ((ConceptItem<?>)it).getConcept_id();
					//ArrayList<SocioResearchDTO> dtos = new ArrayList<SocioResearchDTO>();
					new RPCCall<ArrayList<SocioResearchDTO_Light>>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Error getting researh summaries");
						}
						@Override
						public void onSuccess(ArrayList<SocioResearchDTO_Light> result) {
							final ArrayList<IPickableElement> arr = new ArrayList<IPickableElement>();
							for(SocioResearchDTO_Light dto:result)
							{
								arr.add(dto);
							}
							new RPCCall<ArrayList<Long>>() {
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Error while fetching already selected elements");
								}

								@Override
								public void onSuccess(ArrayList<Long> result) {
									PickElementsTableView pickResearchesToTag = new PickElementsTableView(arr, result,
										new IPickBinder() {
											@Override
											public void processPickChoice( ArrayList<Long> selected_keys) {
												final ArrayList<Long> sel_keys = selected_keys;
												new RPCCall<Boolean>() {
													@Override
													public void onFailure(
															Throwable caught) {
													}
													@Override
													public void onSuccess(Boolean v) {
														Window.alert("Концепт успешно распространен!");
													}
													@Override
													protected void callService(
															AsyncCallback<Boolean> cb) {
													rpcCatalogService.setCatalogContentsIDs(concept_id, sel_keys, cb);
													}
												}.retry(2);
											}
											
											@Override
											public String getCommandName() {
												return "Добавить концепт к выбранным!";
											}

											@Override
											public String getTitle() {
												// TODO Auto-generated method stub
												return "Доступные исследования:";
											}
										}			
									);
									display.getCenterPanel().clear();
									display.getCenterPanel().add(pickResearchesToTag);
									//display.getCenterPanel().add(new HTML("<h2>LOADED</h2>"));
									//display.getCenterPanel().add(new SearchResultsView(result));
									
								}

								@Override
								protected void callService(
										AsyncCallback<ArrayList<Long>> cb) {
									rpcCatalogService.getCatalogContentsIDs(concept_id, cb);
								}
							}.retry(2);
						}
						@Override
						protected void callService(
								AsyncCallback<ArrayList<SocioResearchDTO_Light>> cb) {
							rpcUserService.getResearchSummaries(cb);
						}
					}.retry(2);
				}
				
				if (!(it instanceof RootConceptsList) && !(it instanceof ConceptItemItem) && !(it instanceof ConceptItemEntity))
				{
					//eventBus.fireEvent(new CreateConceptDisabledEvent());
				}
				if (!(it instanceof SimpleResearchList))
				{
					//eventBus.fireEvent(new AddResearchDisabledEvent());
				}
				UserResearchPerspectivePresenter.setParentStyles(it.getParentItem());
			}
		});
		display.getTreeForOpen().addOpenHandler(new OpenHandler<TreeItem>() {
			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				TreeItem it = event.getTarget();
				if (it instanceof SimpleResearchList)
				{
					fetchResearchListData();
					eventBus.fireEvent(new AddResearchEnabledEvent());
				}else if (it instanceof ResearchVarList)
				{
					ResearchVarList rv = (ResearchVarList)it;
					fetchResearchVarData(it, rv.getResearch_id());
					//eventBus.fireEvent(new ShowResearchDetailsEvent(rv.getResearch_id()));
					//eventBus.fireEvent(new AddResearchDisabledEvent());
				}else if (it instanceof RootConceptsList)
				{
					RootConceptsList rcl = (RootConceptsList)it;
					rcl.refreshContents();
					//eventBus.fireEvent(new CreateConceptEnabledEvent());
				}
				else if (it instanceof ConceptItemItem)
				{
					ConceptItemItem rcl = (ConceptItemItem)it;
					rcl.refreshContents();
					eventBus.fireEvent(new CreateConceptEnabledEvent(false));
				}
				else if (it instanceof ConceptItemEntity)
				{
					ConceptItemEntity rcl = (ConceptItemEntity)it;
					rcl.refreshContents();
					//eventBus.fireEvent(new CreateConceptEnabledEvent(false));
				}
				else if (it instanceof ConceptItem<?>)
				{
					try{
						//expecting there only SocioResearch concepts ((( not safe but eatable;
						@SuppressWarnings("unchecked")
						ConceptItem<SocioResearchDTO_Light> ct = (ConceptItem<SocioResearchDTO_Light>)it;
						if (!ct.isBinderAttached())
						{
							ct.attachCBinder(new ConceptBinder<SocioResearchDTO_Light>(){
								@Override
								public void loadContents(
										final ArrayList<Long> keys) {
									new RPCCall<ArrayList<SocioResearchDTO_Light>>() {

										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Error fetching research dtos by ids");	
										}

										@Override
										public void onSuccess(ArrayList<SocioResearchDTO_Light> result) {
											ArrayList<SocioResearchDTO_Light> handler = getDtos_handler();
											handler.clear();
											for(SocioResearchDTO_Light dto:result)
											{
												//if (!handler.contains(dto))
													handler.add(dto);
											}
											setLoaded(true);
										}

										@Override
										protected void callService(
												AsyncCallback<ArrayList<SocioResearchDTO_Light>> cb) {
											rpcUserService.getResearchDTOs(keys, cb);
										}
									}.retry(3);
								}

								@Override
								public ConceptContentsItem composeContentsItem(
										SocioResearchDTO_Light dto) {
									ResearchDescItem item = new ResearchDescItem(dto);
									ResearchVarList research_node = new ResearchVarList(dto);
									item.addItem(research_node);
									return item;
								}
								
								});
						}
						
						ct.refreshContents();
					}
					finally{}	
				}
				UserResearchPerspectivePresenter.setParentStyles(it.getParentItem());
			}			
		});
		
		display.getAddResearchBt().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.getCenterPanel().clear();
				display.getCenterPanel().add(new addResearchUI("First name"));
			}
		});
		eventBus.addHandler(ShowResearchDetailsEvent.TYPE, new ShowResearchDetailsEventHandler() {
			@Override
			public void onShowResearchDetails(ShowResearchDetailsEvent event) {
				display.getCenterPanel().clear();
				display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
				fetchResearchDetailes(event.getResearch_id());
			}
		});
		//ShowVarDetailsEventHandler
		eventBus.addHandler(ShowVarDetailsEvent.TYPE, new ShowVarDetailsEventHandler() {
			@Override
			public void onShowVarDetails(ShowVarDetailsEvent event) {
				display.setPrevCenterState(display.getCenterPanel().getWidget(0));
				display.getCenterPanel().clear();
				//display.getCenterPanel().add(new VariableDetailedView(event.get))
				display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
				fetchVariableDetailes(event.getVar_id());
				//fetchResearchDetailes(event.getResearch_id());
			}
		});
		eventBus.addHandler(AddResearchEnabledEvent.TYPE, new AddResearchEnabledEventHandler() {
			@Override
			public void onAddResearchEnabled(AddResearchEnabledEvent event) {
				display.getAddResearchBtn().setEnabled(true);
			}
		});
		
		
//		eventBus.addHandler(AddResearchDisabledEvent.TYPE, new AddResearchDisabledEventHandler() {
//			//@Override
//			public void onAddResearchDisabled(AddResearchDisabledEvent event) {
//				display.getAddResearchBtn().setEnabled(false);
//			}
//		});
		eventBus.addHandler(CreateConceptEnabledEvent.TYPE,new CreateConceptEnabledEventHandler() {
			@Override
			public void onCreateConceptEnabled(CreateConceptEnabledEvent event) {
				//display.getCreateConceptBtn().setEnabled(true);
				display.setRootConceptUpdateMode(event.isRootConcept());
			}
		});
		eventBus.addHandler(CreateConceptDisabledEvent.TYPE,new CreateConceptDisabledEventHandler() {
			@Override
			public void onCreateConceptDisabled(CreateConceptDisabledEvent event) {
				//display.getCreateConceptBtn().setEnabled(false);
			}
		});
//		display.getCreateConceptBt().addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				display.showCreateConceptPopup(event.getClientX(), event.getClientY(),"SocioResearch");
//			}
//		});
		eventBus.addHandler(CreateConceptEvent.TYPE, new CreateConceptEventHandler() {
			@Override
			public void onCreateConcept(CreateConceptEvent event) {
				CatalogConceptDTO dto = event.getDto();
				updateCatalogueConcept(dto);
			}
		});
		
//		eventBus.addHandler(ShowStartPageMainEvent.TYPE, new ShowStartPageMainEventHandler() {
//			@Override
//			public void onShowStartPageMain(ShowStartPageMainEvent event) {
//				showMainPageArticle();
//			}
//		});
	}

	private void fetchResearchListData()
	{
		//final ArrayList<NewsDTO> newsData = new ArrayList<NewsDTO>();
		
		new RPCCall<ArrayList<SocioResearchDTO_Light>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching " +
						" news: "
			            + caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<SocioResearchDTO_Light> result) {
				display.setResearchListData(result);
			}

			@Override
			protected void callService(
					AsyncCallback<ArrayList<SocioResearchDTO_Light>> cb) {
				rpcUserService.getResearchSummaries(cb);
			}
		}.retry(3);
	}
	
	private void fetchRootCatalogConcepts()
	{
		new RPCCall<ArrayList<CatalogConceptDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error while fetching root catalog concepts");
			}

			@Override
			public void onSuccess(ArrayList<CatalogConceptDTO> result) {
				Window.alert("Success fetching root catalog concepts!");
			}

			@Override
			protected void callService(
					AsyncCallback<ArrayList<CatalogConceptDTO>> cb) {
				rpcCatalogService.getRootCatalogConcepts("SocioResearch", cb);
			}
		}.retry(3);
	}
	
	
	

	
	
	private void updateCatalogueConcept(final CatalogConceptDTO dto)
	{
		if(dto.getItem_type().equals("entity"))
		{
			new RPCCall<Void>() {

				@Override
				public void onFailure(Throwable arg0) {
					
				}

				@Override
				public void onSuccess(Void arg0) {
				}

				@Override
				protected void callService(AsyncCallback<Void> cb) {
					rpcAdminService.addEntityItem(dto.getSuper_concept_ID(), dto.getName(), new HashMap<String,String>(), cb);
				}
			}.retry(2);
		}else
		{
			new RPCCall<Void>() {

				@Override
				public void onFailure(Throwable arg0) {
					
				}

				@Override
				public void onSuccess(Void arg0) {
				}

				@Override
				protected void callService(AsyncCallback<Void> cb) {
					rpcAdminService.addSubEntityItem(dto.getSuper_concept_ID(), dto.getName(), new HashMap<String,String>(), cb);
				}
			}.retry(2);
		}
//		new RPCCall<CatalogConceptDTO>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				Window.alert("Error while updating concept");
//			}
//
//			@Override
//			public void onSuccess(CatalogConceptDTO result) {
//				//fetchRootCatalogConcepts();	
//			}
//
//			@Override
//			protected void callService(AsyncCallback<CatalogConceptDTO> cb) {
//				rpcCatalogService.updateCatalogConcept(dto, cb);
//			}
//		}.retry(3);
	}
	private void fetchResearchVarData(final TreeItem item,final long id_research)
	{
		new RPCCall<ArrayList<VarDTO_Light>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching " +
						" news: "
			            + caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<VarDTO_Light> result) {
				display.setVarListData(item,result);
			}

			@Override
			protected void callService(
					AsyncCallback<ArrayList<VarDTO_Light>> cb) {
				rpcUserService.getResearchVarsSummaries(id_research, cb);
			}
		}.retry(3);
	}
	
	
	
	
	
	private void fetchResearchDetailes(final long id_research)
	{
		new RPCCall<ResearchBundleDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on getting research detailes :"+caught.getMessage());
			}

			
			@Override
			public void onSuccess(final ResearchBundleDTO result) {
				
				AdminResearchDetailedView ad_view = new AdminResearchDetailedView(new UserResearchDetailedView(result.getDto(),result.getMeta()));
				AdminResearchEditView ed_view = new AdminResearchEditView(result.getDto(), result.getMeta());
				AdminResearchGroupEditView gr_ed_view = new AdminResearchGroupEditView(result.getDto(),result.getMeta());
				AdminResearchAdvancedFilesEditView files_ed_view = new AdminResearchAdvancedFilesEditView(id_research, result.getFiles_dto());
				AdminResearchVarsView varsview = new AdminResearchVarsView(result.getDto());
				AdminResearchVarGeneralizeS1View gen = new AdminResearchVarGeneralizeS1View(result.getDto().getId(), display.getCenterPanel());
				AdminResearchDetailedPresenter presenter = new AdminResearchDetailedPresenter(rpcUserService,rpcAdminService, eventBus, ad_view, varsview,ed_view,gr_ed_view,files_ed_view,gen);
				
				presenter.go(display.getCenterPanel(),null,null);
				
//				new RPCCall<MetaUnitMultivaluedEntityDTO>() {
//					@Override
//					public void onFailure(Throwable caught) {
//						Window.alert("Error on fetching DB structure"+caught.getMessage());
//						
//					}
//					
//					
//					@Override
//					public void onSuccess(MetaUnitMultivaluedEntityDTO result2) {
//					
//					}
//
//				
//					
//					@Override
//					protected void callService(
//							
//							AsyncCallback<MetaUnitMultivaluedEntityDTO> cb) {
//						rpcUserService.getDatabankStructure("socioresearch", cb);
//					}
//				}.retry(2);
			}

			@Override
			protected void callService(AsyncCallback<ResearchBundleDTO> cb) {
				rpcUserService.getResearchBundle(id_research, cb);
			}
		}.retry(3);
	}
	
	private void fetchVariableDetailes(final long id_var)
	{
		new RPCCall<VarDTO_Detailed>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on getting var detailes :"+caught.getMessage());
			}

			@Override
			public void onSuccess(final VarDTO_Detailed result) {
			//	AdminResearchDetailedView ad_view = new AdminResearchDetailedView(new UserResearchDetailedView(result));
			//	AdminResearchEditView ed_view = new AdminResearchEditView(result);
			//	AdminResearchDetailedPresenter presenter = new AdminResearchDetailedPresenter(rpcUserService,rpcAdminService, eventBus, ad_view, ed_view);
			//	presenter.go(display.getCenterPanel());
				
				
				
				
				
				new RPCCall<MetaUnitMultivaluedEntityDTO>() {

					@Override
					public void onFailure(Throwable arg0) {
					}

					@Override
					public void onSuccess(MetaUnitMultivaluedEntityDTO res) {
						display.getCenterPanel().clear();
						//display.getCenterPanel().add(new VariableDetailedView(result));
						if (result instanceof RealVarDTO_Detailed)
							display.getCenterPanel().add(new RealVariableEditView((RealVarDTO_Detailed)result,res,display));
						else if (result instanceof TextVarDTO_Detailed)
							display.getCenterPanel().add(new TextVariableEditView((TextVarDTO_Detailed)result,res,display));
						else
							display.getCenterPanel().add(new VariableEditView(display,result,res));
					}

					@Override
					protected void callService(
							AsyncCallback<MetaUnitMultivaluedEntityDTO> cb) {
						UserSocioResearchService.Util.getInstance().getDatabankStructure("sociovar", cb);
					}
				}.retry(2);
			}

			@Override
			protected void callService(AsyncCallback<VarDTO_Detailed> cb) {
				rpcUserService.getVarDetailed(id_var, cb);
			}
		}.retry(3);
	}
}
