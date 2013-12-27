package com.mresearch.databank.client.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.*;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.StartupBundleDTO;
import com.mresearch.databank.shared.ZaconDTO_Light;

import java.util.ArrayList;

//import com.mresearch.databank.client.service.StartPageServiceAsync;
//import com.mresearch.databank.server.CatalogServiceImpl;
//import com.mresearch.databank.server.domain.SocioResearch;

public class AdminAccessPresenter implements Presenter
{


	 public interface Display {
		 Widget asWidget();
		 void setLawsAll(ArrayList<ZaconDTO_Light> arr);
		 void setResearchesAll(ArrayList<SocioResearchDTO_Light> arr);
		 void setLawsSel(ArrayList<ZaconDTO_Light> arr);
		 void setResearchesSel(ArrayList<SocioResearchDTO_Light> arr);
		 void setPubsSel(Long howmuch);
		 ArrayList<ZaconDTO_Light> getLawsSel();
		 ArrayList<SocioResearchDTO_Light> getResearchesSel();
		 Long getPubsSel();
		 HasClickHandlers getSaveBtn();
	 }


	 private final StartPageServiceAsync rpcService;
	 private final AdminSocioResearchServiceAsync rpcAdminService;
	 private final UserSocioResearchServiceAsync researchUserService = GWT.create(UserSocioResearchService.class);
	 private final AdminArticleServiceAsync articleUserService = GWT.create(AdminArticleService.class);

	 private final SimpleEventBus eventBus;
	 private final Display display;

	 public AdminAccessPresenter(StartPageServiceAsync rpcUserService, AdminSocioResearchServiceAsync rpcAdminService, SimpleEventBus eventBus,
                                 Display view) {
		    this.rpcService = rpcUserService;
		    this.rpcAdminService = rpcAdminService;
		    this.eventBus = eventBus;
		    this.display = view;
		    bind();
		  }
	@Override
	public void go(HasWidgets container,ArrayList<String> p_names,ArrayList<String> p_values) {
		 container.clear();
		 container.add(display.asWidget());
		 fetchResearches();
	}
	private void fetchResearches()
	{
	/*	new RPCCall<ArrayList<SocioResearchDTO_Light>>() {
			@Override
			public void onFailure(Throwable arg0) {
			}
			@Override
			public void onSuccess(ArrayList<SocioResearchDTO_Light> res) {
				display.setResearchesAll(res);
				fetchLaws();
			}
			@Override
			protected void callService(
					AsyncCallback<ArrayList<SocioResearchDTO_Light>> cb) {
				researchUserService.getResearchSummaries(cb);
			}
		}.retry(2);*/
	}


	public void bind()
	{
		/*display.getSaveBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				final StartupBundleDTO dto = new StartupBundleDTO();
				dto.setImportant_laws(display.getLawsSel());
				dto.setTop_researchs(display.getResearchesSel());
				dto.setPubs_last_count(display.getPubsSel());
				new RPCCall<Void>() {

					@Override
					public void onFailure(Throwable arg0) {
						Window.alert("Failed!");
					}

					@Override
					public void onSuccess(Void arg0) {
						Window.alert("Success!");
					}

					@Override
					protected void callService(AsyncCallback<Void> cb) {
						rpcAdminService.setStartupContent(dto, cb);
					}
				}.retry(2);
			}
		});*/
	}
}
