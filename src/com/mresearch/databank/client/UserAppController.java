
/** 
 * Copyright 2010 Daniel Guermeur and Amy Unruh
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   See http://connectrapp.appspot.com/ for a demo, and links to more information 
 *   about this app and the book that it accompanies.
 */
package com.mresearch.databank.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.mresearch.databank.client.event.*;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.presenters.*;
import com.mresearch.databank.client.service.*;
import com.mresearch.databank.client.views.*;
import com.mresearch.databank.shared.IShowPlaceParameters;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserHistoryDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;

import java.util.ArrayList;
import java.util.HashMap;







public class UserAppController implements ValueChangeHandler<String>, AppController {
  private final SimpleEventBus eventBus;
  private final StartPageServiceAsync startpageService;
  private final SearchServiceAsync searchService = GWT.create(SearchService.class);
  public static final HashMap<String,IShowPlaceParameters> query_log = new HashMap<String, IShowPlaceParameters>();
  //private final MessagesServiceAsync messagesService = GWT.create(MessagesService.class);
  private String currentFriendId;
  private Widget mainPanel;
  private static UserAppControllerUiBinder uiBinder = GWT
			.create(UserAppControllerUiBinder.class);

  
//	interface UserAppControllerUiBinder extends
//			UiBinder<DockLayoutPanel, UserAppController> {
//	}
  interface UserAppControllerUiBinder extends
	UiBinder<VerticalPanel, UserAppController> {
}	
  
  
  
  
  
  
  
	
  @UiField Anchor mainNav,researchNav;
          //,pubNav;
  //,lawNav,juryNav,pubNav;
  //@UiField Anchor mainNav,newsNav,researchNav,lawNav,juryNav,
  @UiField Anchor loginNav;
 // @UiField MenuItem mainNav,researchNav,lawNav,juryNav;
 // @UiField MenuBar menuBar,menuBar1,menuBar2,menuBar3,menuBar4;
  @UiField VerticalPanel centerPanel;
  @UiField TextBox searchBox;
  @UiField Label username;
///  @UiField PushButton searchBtn;
 // @UiField Button searchCmd;
  
  //private DockLayoutPanel thisDock;
  private VerticalPanel thisDock;
  private DatabankApp base;
  private final UserSocioResearchServiceAsync researchService = GWT.create(UserSocioResearchService.class);
  
  private final AdminArticleServiceAsync articleService = GWT.create(AdminArticleService.class);
  
  public UserAppController(StartPageServiceAsync rpcService, SimpleEventBus eventBus,  DatabankApp base) {
	//initWidget();
	thisDock = uiBinder.createAndBindUi(this);
    this.eventBus = eventBus;
    this.startpageService = rpcService;
    this.base = base;
    username.setText(base.getCurrentUser().getName());
//    
//    menuBar.setAutoOpen(true);
//    //menuBar.setWidth("500px");
//    menuBar.setAnimationEnabled(true);
//    menuBar1.setAutoOpen(true);
//    //menuBar.setWidth("500px");
//    menuBar1.setAnimationEnabled(true);
//    menuBar2.setAutoOpen(true);
//    //menuBar.setWidth("500px");
//    menuBar2.setAnimationEnabled(true);
//    menuBar3.setAutoOpen(true);
//    //menuBar.setWidth("500px");
//    menuBar3.setAnimationEnabled(true);
//    menuBar4.setAutoOpen(true);
//    //menuBar.setWidth("500px");
//    menuBar4.setAnimationEnabled(true);
//   // bind_menu();
    
    
    
    bind();
  }
  
//  @Override
//  public void updateUsername(String str){
//	  username.setText(str);
//  }
  private void bind() {
    History.addValueChangeHandler(this);
    //History.
//    mainNav.setCommand(new Command() {
//		@Override
//		public void execute() {
//			doViewStartPageMain();
//		}
//	});
    
   
    mainNav.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			doViewStartPageMain();
		}
	});
//     researchNav.setCommand(new Command() {
//		
//		@Override
//		public void execute() {
//			doViewUserResearch();
//		}
//	});
    researchNav.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			doViewUserResearch();
		}
	});
     
//    lawNav.setCommand(new Command() {
//		@Override
//		public void execute() {
//			doViewUserLaw();
//		}
//	});
  
//    lawNav.addClickHandler(new ClickHandler() {
//		@Override
//		public void onClick(ClickEvent event) {
//			doViewUserLaw();
//		}
//	});
   /* pubNav.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			doViewUserPub();
		}
	});*/
//    juryNav.addClickHandler(new ClickHandler() {
//		@Override
//		public void onClick(ClickEvent event) {
//			doViewUserJury();
//		}
//	});
    searchBox.addMouseDownHandler(new MouseDownHandler() {
		@Override
		public void onMouseDown(MouseDownEvent arg0) {
			searchBox.setText("");	
		}
	});
   searchBox.addMouseOverHandler(new MouseOverHandler() {
	@Override
	public void onMouseOver(MouseOverEvent ev) {
		searchBox.addStyleDependentName("highlight");
	}
   });

   searchBox.addMouseOutHandler(new MouseOutHandler() {
	@Override
	public void onMouseOut(MouseOutEvent arg0) {
		searchBox.removeStyleDependentName("highlight");
		searchBox.setText("Поиск");
	}
   });
   searchBox.addKeyDownHandler(new KeyDownHandler() {
	@Override
	public void onKeyDown(KeyDownEvent event) {
		// TODO Auto-generated method stub
		int code = event.getNativeKeyCode();
		if (code == 13)
		{
			doViewSearchResults(searchBox.getText());
		}
	}
   });
  // searchBtn.addStyleName("searchBtn");
//   searchBtn.addClickHandler(new ClickHandler() {
//	@Override
//	public void onClick(ClickEvent arg0) {
//		doViewSearchResults(searchBox.getText());
//	}
//   });
   
//    searchCmd.addClickHandler(new ClickHandler() {
//		@Override
//		public void onClick(ClickEvent event) {
//			doViewSearchResults(searchBox.getText());
//		}
//	});
    
    loginNav.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			PopupPanel popup = new PopupPanel(true);
			LoginView l_view = new LoginView(DatabankApp.get(), popup);
			popup.add(l_view);
			popup.setPopupPosition(500, 100);
			popup.show();
		}
	});
    
    
    eventBus.addHandler(ShowPlaceEvent.TYPE,new ShowPlaceEventHandler() {
		@Override
		public void onShowPlace(ShowPlaceEvent event) {
			query_log.put(event.getParams().toHash(), event.getParams());
			History.newItem(event.getPlace()+"@"+"action="+event.getAction()+"&params="+event.getParams().toHash());
			History.fireCurrentHistoryState();
			
		}
	});
    
    eventBus.addHandler(ShowResearchDetailsEvent.TYPE, new ShowResearchDetailsEventHandler() {
		@Override
		public void onShowResearchDetails(ShowResearchDetailsEvent event) {
			 //if(History.getToken().startsWith("search-results")||History.getToken().startsWith("user-main"))
				 doViewUserResearch(event.getResearch_id());
		}
	});
    
    
    
    eventBus.addHandler(ShowVarDetailsEvent.TYPE, new ShowVarDetailsEventHandler() {
		
		@Override
		public void onShowVarDetails(ShowVarDetailsEvent event) {
			//if(History.getToken().startsWith("search-results")||History.getToken().startsWith("user-main"))
				doViewUserResearchVar(event.getVar_id());
				History.fireCurrentHistoryState();
		}
	});
    
    eventBus.addHandler(RecalculateDistributionsEvent.TYPE, new RecalculateDistributionsEventHandler() {
		@Override
		public void onRecalculateDistributions(final RecalculateDistributionsEvent ev) {
//			TreeItem it = display.getSelectedItem();
//			if (it instanceof VarDescItem)
//			{
//				VarDescItem rv = (VarDescItem)it;
//				eventBus.fireEvent(new ShowVarDetailsEvent(rv.getVar_id()));
//				//weights will be recalculated on server; may be not syncronized!!!
//			}
			//History.newItem("");
				new RPCCall<UserHistoryDTO>() {
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error on updating account state!");
				}
		
				@Override
				public void onSuccess(UserHistoryDTO result) {
					DatabankApp.get().setCurrentUserHistory(result);
					result.setCurrant_var(ev.getVar_anal());
					//if(result.getCurrant_var().getDistr_type().equals(UserAnalysisSaveDTO.DISTR_TYPE_1D))
					if(ev.getVar_anal().getDistr_type().equals(UserAnalysisSaveDTO.DISTR_TYPE_1D))
					{
						DatabankApp.get().setCurrentUserHistory(result);
						eventBus.fireEvent(new ShowVarDetailsEvent(ev.getVar_anal().getVar_1().getId()));
					}else
					{
						ShowVar2DDEvent event = new ShowVar2DDEvent(result.getCurrent_research().getResearh().getID());
						result.getCurrant_var().setDistribution(null);
						event.setPre_saved(result.getCurrant_var());
						DatabankApp.get().setCurrentUserHistory(result);
						eventBus.fireEvent(event);
					}
					
					
				}
		
				@Override
				protected void callService(AsyncCallback<UserHistoryDTO> cb) {
					UserHistoryDTO dt = DatabankApp.get().getCurrentUserHistory();
					UserResearchSettingDTO dto = ev.getSetting();
					dt.setCurrent_research(dto);
					
					DatabankApp.get().getUserService().updateResearchState(dt,cb);
					
				}
			}.retry(1);
		
		}
	});

    
    eventBus.addHandler(ShowZaconDetailsEvent.TYPE, new ShowZaconDetailsEventHandler() {
		
		@Override
		public void onShowZaconDetails(ShowZaconDetailsEvent event) {
			//if(History.getToken().startsWith("search-results")||History.getToken().startsWith("user-main"))
				doViewUserZacon(event.getZacon_id());
		}
	});
    
    eventBus.addHandler(ShowPublicationDetailsEvent.TYPE, new ShowPublicationDetailsEventHandler() {
		
		@Override
		public void onShowPublicationDetails(ShowPublicationDetailsEvent event) {
			//if(History.getToken().startsWith("search-results")||History.getToken().startsWith("user-main"))
				doViewUserPublication(event.getPublication_id());
		}
	});
    eventBus.addHandler(ShowConsultationDetailsEvent.TYPE, new ShowConsultationDetailsEventHandler() {
		
  		@Override
  		public void onShowConsultationDetails(ShowConsultationDetailsEvent event) {
  			//if(History.getToken().startsWith("search-results")||History.getToken().startsWith("user-main"))
  				doViewUserConsult(event.getConsultation_id());
  		}
  	});
    //    eventBus.addHandler(FriendAddEvent.TYPE, new FriendAddEventHandler() {
//      public void onAddFriend(FriendAddEvent event) {
//        doAddNewFriend();
//      }
//    });
//
//    eventBus.addHandler(ShowFriendPopupEvent.TYPE, new ShowFriendPopupEventHandler() {
//      public void onShowFriendPopup(ShowFriendPopupEvent event) {
//        FriendPopupPresenter friendPopupPresenter = new FriendPopupPresenter(friendService, eventBus, new FriendPopupView(event.getFriend()
//            .getDisplayName(), event.getClickPoint()), event.getFriend());
//        friendPopupPresenter.go();
//      }
//    });

   
    
  }

  private void doViewStartPageMain() {
    History.newItem("user-main");
  }
  private void doViewStartPageNews() {
	    History.newItem("user-news");
  }
  public void doViewUserResearch() {
	    History.newItem("user-research");
 }
  private void doViewUserResearch(long id) {
	    History.newItem("user-research@showResearch="+id);
}
  private void doViewUserResearchVar(long id) {
	    History.newItem("user-research@showVar="+id);
  }
  public void doViewUserLaw() {
	    History.newItem("user-law");
  }
  public void doViewUserPub() {
    History.newItem("user-pub");
  }
  private void doViewUserPublication(long id) {
	    History.newItem("user-pub@showPub="+id);
  }
  private void doViewUserZacon(long id) {
	    History.newItem("user-law@showZacon="+id);
  }
  private void doViewUserConsult(long id) {
	    History.newItem("user-jury@showConsult="+id);
  }
  public void doViewUserJury() {
	    History.newItem("user-jury");
	  }
  private void doViewSearchResults(String searchstr)
  {
	  if (searchstr != null && !searchstr.equals("")&& !searchstr.equals("Поиск"))
	  {
		  searchBox.setText("Поиск");
		  History.newItem("search-results@query="+searchstr);
	      History.fireCurrentHistoryState();
	  }
  }

  
//public void go(DockLayoutPanel mainPanel)
  public void go(VerticalPanel mainPanel)
  {
	this.mainPanel = mainPanel;
	mainPanel.setWidth("100%");
	mainPanel.setHeight("100%");
	
	mainPanel.add(thisDock);
	if ("".equals(History.getToken())) {
      History.newItem("user-main");
    } else {
      History.fireCurrentHistoryState();
    }
  }
  private void clearMainPanel()
  {
//	  int w_count = thisDock.getWidgetCount();
//	  for(int i = 0; i < w_count;i++)
//	  {
//		  if(thisDock.getWidget(i) instanceof DockLayoutPanel)
//		  {
//			  thisDock.remove(i);
//			  break;
//		  }
//	  }
//	  if (w_count>1) thisDock.remove(2);
//	  while(thisDock.iterator().)
//      {
//    	  if(thisDock.iterator().next() instanceof DockLayoutPanel)
//    	  {
//    		  thisDock.iterator().remove();
//    		  break;
//    	  }
//      }
  }
  
  
  private void parsePathToken(String token,ArrayList<String> p_names,ArrayList<String> p_values)
  {
	  if (!token.contains("@")) return;
	  
	  String [] ar = token.split("@");
	  String [] params   = ar[1].split("&");
	  for(int i = 0 ; i < params.length;i++)
	  {
		  String [] key_value = params[i].split("=");
		  p_names.add(key_value[0]);
		  p_values.add(URL.decode(key_value[1]));
	  }
	
  }
  
  private String prevToken = "";
  private Presenter presenter = null;
  public void onValueChange(ValueChangeEvent<String> event) {
    String token = event.getValue();

    
    
    
    
    base.getFooter().setVisible(false);
    if (token != null) {
      //presenter.go();
      if (token.startsWith("user-main")) {
    	//clearMainPanel();
    	presenter = new StartPagePerspectivePresenter(startpageService, eventBus, new StartPagePerspectiveView());
    	base.getFooter().setVisible(true);
       //thisDock.add(presenter.getPlace());
    	presenter.go(centerPanel,null,null);
        return;
      } else if(token.equals("user-news")){
//    	  presenter = new UserNewsPerspectivePresenter(startpageService, eventBus, new UserNewsPerspectiveView());
//          presenter.go(centerPanel,null,null);
      } else if(token.startsWith("user-research")){
    	  if(!prevToken.startsWith("user-research"))presenter = new UserResearchPerspectivePresenter(researchService, eventBus, new UserResearchPerspectiveView(eventBus));
    	  
    	  ArrayList<String> param_names,param_values;
    	  param_names = new ArrayList<String>();
    	  param_values = new ArrayList<String>();
    	  parsePathToken(token, param_names, param_values);
    	  presenter.go(centerPanel,param_names,param_values);
      }else if(token.startsWith("user-law")){
    	  
    	  
    	  if(!prevToken.startsWith("user-law"))presenter = new UserLawPerspectivePresenter(articleService, eventBus, new UserLawPerspectiveView(eventBus));
    	  ArrayList<String> param_names,param_values;
    	  param_names = new ArrayList<String>();
    	  param_values = new ArrayList<String>();
    	  parsePathToken(token, param_names, param_values);
    	  presenter.go(centerPanel,param_names,param_values);
    	  
      }
      else if(token.startsWith("user-pub")){
    	  
    	  
    	  if(!prevToken.startsWith("user-pub"))presenter = new UserPubPerspectivePresenter(articleService, eventBus, new UserPublicationPerspectiveView(eventBus));
    	  ArrayList<String> param_names,param_values;
    	  param_names = new ArrayList<String>();
    	  param_values = new ArrayList<String>();
    	  parsePathToken(token, param_names, param_values);
    	  presenter.go(centerPanel,param_names,param_values);
    	  
      }
      else if(token.startsWith("user-jury")){
    	  
    	  
    	  presenter = new UserJuryPerspectivePresenter(articleService, eventBus, new UserConsultationPerspectiveView(eventBus));
    	  ArrayList<String> param_names,param_values;
    	  param_names = new ArrayList<String>();
    	  param_values = new ArrayList<String>();
    	  parsePathToken(token, param_names, param_values);
    	  presenter.go(centerPanel,param_names,param_values);
    	  
      }
      else if(token.startsWith("search-results")){
    	  String [] arr = token.split("=");
    	  String query = arr[1];
    	
    	  
    	  
    	  
    	  
    	  
     	  ArrayList<String> param_names,param_values;
    	  param_names = new ArrayList<String>();
    	  param_values = new ArrayList<String>();
    	  parsePathToken(token, param_names, param_values);
    	  if(!prevToken.startsWith("search-results"))presenter = new UserSearchPerspectivePresenter(searchService,researchService, eventBus, new UserSearchPerspectiveView());
          presenter.go(centerPanel,param_names,param_values);
          
          
          
          
          
          
         
          
          
      }
//      else if(token.equals("#"))
//      {
//    	  History.back();
//    	 return;
//      }
      prevToken = new String(token);
    	  
//      else if (token.equals("add")) {
//        presenter = new FriendEditPresenter(friendService, eventBus, new FriendEditView());
//        presenter.go(ConnectrApp.get().getMainPanel());
//        return;
//
//      } 
    }
    

  }
}
