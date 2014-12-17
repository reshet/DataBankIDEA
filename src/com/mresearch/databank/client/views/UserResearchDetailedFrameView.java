package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.DatabankApp;

public class UserResearchDetailedFrameView extends Composite {

	private static UserResearchDetailedFrameViewUiBinder uiBinder = GWT
			.create(UserResearchDetailedFrameViewUiBinder.class);

	interface UserResearchDetailedFrameViewUiBinder extends
			UiBinder<Widget, UserResearchDetailedFrameView> {
	}
	@UiField VerticalPanel filesPanel,viewPanel,descPanel,varsPanel;
    @UiField Label descriptionTabHeader, metadataTabHeader, filesTabHeader, varsTabHeader;
	public UserResearchDetailedFrameView(UserResearchDescriptionView desc,UserResearchDetailedView view,UserResearchAdvancedFilesView files,UserResearchVarsView v_v) {
		initWidget(uiBinder.createAndBindUi(this));
        descriptionTabHeader.setText(DatabankApp.langConstants.researchDetailedTabDescription());
        metadataTabHeader.setText(DatabankApp.langConstants.researchDetailedTabMetadata());
        filesTabHeader.setText(DatabankApp.langConstants.researchDetailedTabFiles());
        varsTabHeader.setText(DatabankApp.langConstants.researchDetailedTabVars());
		descPanel.add(desc);
		filesPanel.add(files);
		viewPanel.add(view);
		varsPanel.add(v_v);
	}

}
