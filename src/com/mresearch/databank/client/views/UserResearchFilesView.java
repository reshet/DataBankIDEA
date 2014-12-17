package com.mresearch.databank.client.views;

import com.mresearch.databank.client.DatabankApp;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.presenters.AdminResearchDetailedPresenter;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.AdminSocioResearchServiceAsync;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.shared.SocioResearchFilesDTO;
import com.sun.java.swing.plaf.windows.resources.windows;

public class UserResearchFilesView extends Composite implements AdminResearchDetailedPresenter.FilesEditDisplay{
	private static AdminResearchFilesEditViewUiBinder uiBinder = GWT
			.create(AdminResearchFilesEditViewUiBinder.class);

	interface AdminResearchFilesEditViewUiBinder extends
			UiBinder<Widget, UserResearchFilesView> {
	}
	private final AdminSocioResearchServiceAsync adminResearchService = GWT
		      .create(AdminSocioResearchService.class);
	private final UserSocioResearchServiceAsync userResearchService = GWT
		      .create(UserSocioResearchService.class);
	
	private long research_id;
	private String category;
	public UserResearchFilesView(long research_id, String category, SocioResearchFilesDTO dto) {
		initWidget(uiBinder.createAndBindUi(this));
		this.research_id = research_id;
		this.category = category;
		fillWithFetched(dto);
		//uploadPanel.add(up);
	}
	
	
	@UiField FlexTable files_table;
	
	private void fillWithFetched(SocioResearchFilesDTO dto)
	{
		files_table.clear();
		String realPath = GWT.getModuleBaseURL();
        String user_class  = "simpleUser";
        if(DatabankApp.get().getCurrentUser().getId()!=0)
        {
            user_class = DatabankApp.get().getCurrentUser().getAccountType();
        }

        for(int i = 0; i < dto.getFiles_ids().size();i++)
		{
			files_table.setWidget(i, 0, new Label(String.valueOf(i)));
			files_table.setWidget(i, 1, new Label(dto.getFiles_descs().get(i)));		
			final long file_id = dto.getFiles_ids().get(i);
			if(user_class.equals("grantedUser")){
                files_table.setWidget(i, 2,
                        new HTML("<a href=\""+realPath+"serve?blob-key="+file_id+"\">" +
                                DatabankApp.langConstants.researchDetailedFilesDownload()+ "</a>"));
            }
    	}
	}
}
