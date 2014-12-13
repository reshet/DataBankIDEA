package com.mresearch.databank.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mresearch.databank.client.constants.LangConstants;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.StartPageService;
import com.mresearch.databank.client.service.StartPageServiceAsync;
import com.mresearch.databank.client.service.UserAccountService;
import com.mresearch.databank.client.service.UserAccountServiceAsync;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserHistoryDTO;



public class DatabankApp implements EntryPoint {
	
	//interface DatabankAppUiBinder extends UiBinder<DockLayoutPanel, DatabankApp> {}
	interface DatabankAppUiBinder extends UiBinder<ScrollPanel, DatabankApp> {}
	private static final DatabankAppUiBinder binder = GWT.create(DatabankAppUiBinder.class);
  public static LangConstants langConstants = GWT.create(LangConstants.class);
	private static DatabankApp singleton;
	private RootLayoutPanel root;
	//private StartPagePerspectivePresenter startpagePresenter;
	private SimpleEventBus eventBus = new SimpleEventBus();
	private AppController appController;
	private UserAccountDTO currentUser;
	private UserHistoryDTO currentUserHistory = new UserHistoryDTO();
	
	public UserHistoryDTO getCurrentUserHistory() {
		return currentUserHistory;
	}
	public void setCurrentUserHistory(UserHistoryDTO currentUserHistory) {
		this.currentUserHistory = currentUserHistory;
	}
	private final UserAccountServiceAsync userService = GWT.create(UserAccountService.class);

	@UiField VerticalPanel verticalPanel;
	@UiField HTMLPanel pict_footer;
	public static DatabankApp get()
	{
		return singleton;
	}
	public UserAccountServiceAsync getUserService()
	{
		return userService;
	}
	
	
	public HTMLPanel getFooter()
	{
		return pict_footer;
	}
	
	public void onModuleLoad() {
		 singleton = this; 
		// initWidget(binder.createAndBindUi(this));
		login("email","password",null);
	}
	
	private void initDefUsers()
	{
		userService.initDefaultUsers(new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				Window.alert("Def users created!");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error creating def users");
			}
		});	
		
	}
	
	public void updateUserAccountState()
	{
		new RPCCall<UserHistoryDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on updating account state!");
			}

			@Override
			public void onSuccess(UserHistoryDTO result) {
				setCurrentUserHistory(result);
			}

			@Override
			protected void callService(AsyncCallback<UserHistoryDTO> cb) {
				userService.updateResearchState(getCurrentUserHistory(),cb);
			}
		}.retry(2);

	}
	public void login(String email,String password,String token)
	{
		    userService.login(email, password,token,
		        new AsyncCallback<UserAccountDTO>() {
		          public void onFailure(Throwable caught) {
		        	  Window.alert("An error occurred: "+caught.getMessage());
		          }

		          
		          public void onSuccess(final UserAccountDTO result) {
		        	  new RPCCall<UserHistoryDTO>() {

		      			@Override
		      			public void onFailure(Throwable caught) {
		      				Window.alert("Error on updating account state!");
		      			}

		      			@Override
		      			public void onSuccess(UserHistoryDTO result_hist) {
		      				 setCurrentUserHistory(result_hist);
		      				 loginSuccessful(result);
		      			}

		      			@Override
		      			protected void callService(AsyncCallback<UserHistoryDTO> cb) {
		      				userService.updateResearchState(getCurrentUserHistory(),cb);
		      			}
		      		}.retry(2);
		        	 
		          }
		        });
	}
	public void loginSuccessful(UserAccountDTO result)
	{
		   setCurrentUser(result);
           //Window.alert(result.getAccountType()+" "+result.getName());
           if (getCurrentUser() == null)
   		{
   			Window.alert("Error while login!");
   			return;
   		}

   		if (getCurrentUser().getAccountType().equals("simpleUser")||getCurrentUser().getAccountType().equals("registeredUser")||getCurrentUser().getAccountType().equals("grantedUser"))
   		{
   			StartPageServiceAsync startpageService = GWT.create(StartPageService.class);
   			appController = new UserAppController(startpageService, eventBus,DatabankApp.this);
   		}else 
   			
   		if (getCurrentUser().getAccountType().equals("researchAdmin"))
   		{
   			appController = new ResearchAdminAppController(eventBus);
   		}
   		
   		else if (getCurrentUser().getAccountType().equals("lawAdmin"))
   		{
   			appController = new LawAdminAppController(eventBus);
   		}
   		else if (getCurrentUser().getAccountType().equals("pubAdmin"))
   		{
   			appController = new PubAdminAppController(eventBus);
   		}
   		else if (getCurrentUser().getAccountType().equals("juryConsultant"))
   		{
   			appController = new JuryAdminAppController(eventBus);
   		}
   		createUI();
      
	}
	public void logout()
	{
		userService.logout(new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				Window.alert(langConstants.mainExitMessage());
			}
			
			@Override
			public void onFailure(Throwable caught) {
			
			}
		});
	}
	private void createUI()
	{
		ScrollPanel outer  = binder.createAndBindUi(this);
		root = RootLayoutPanel.get();
		root.clear();
//		ScrollPanel scrol = new ScrollPanel();
//		scrol.setWidth("100%");
//		scrol.setHeight("100%");
//		scrol.add(outer);
		root.add(outer);
		appController.go(verticalPanel);
		//appController.
		//startpagePresenter = new StartPagePerspectivePresenter(startpageService, eventBus, new StartPagePerspectiveView());
		//startpagePresenter.go(outer);
	}
	
	 public AppController getAppController() {
		return appController;
	}
	public SimpleEventBus getEventBus() {
		    return eventBus;
		  }

	public UserAccountDTO getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(UserAccountDTO currentUser) {
		this.currentUser = currentUser;
	}
}
