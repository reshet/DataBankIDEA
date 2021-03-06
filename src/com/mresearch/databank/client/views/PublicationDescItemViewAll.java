package com.mresearch.databank.client.views;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.AppController;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.UserAppController;
import com.mresearch.databank.client.event.ShowPublicationDetailsEvent;
import com.mresearch.databank.shared.ArticleDTO;
import com.mresearch.databank.shared.PublicationDTO_Light;
import com.mresearch.databank.shared.ZaconDTO;

public class PublicationDescItemViewAll extends PublicationDescItemView{

	

	
	public PublicationDescItemViewAll(PublicationDTO_Light dto) {
		super(dto);
		pub_link.setHTML("<a class=\"more\">Все публикации &rarr;</a>");
	}
	
	
	
	@UiHandler(value="pub_link")
	public void doGo(ClickEvent ev)
	{
		AppController app = DatabankApp.get().getAppController();
		if(app instanceof UserAppController)
		{
			UserAppController uapp = (UserAppController)app;
			uapp.doViewUserPub();
		}
	}

}
