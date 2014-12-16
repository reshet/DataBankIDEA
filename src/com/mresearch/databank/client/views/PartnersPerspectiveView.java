package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PartnersPerspectiveView extends Composite{

	private static PartnersPerspectiveViewUiBinder uiBinder = GWT
			.create(PartnersPerspectiveViewUiBinder.class);

	interface PartnersPerspectiveViewUiBinder extends
			UiBinder<Widget, PartnersPerspectiveView> {
	}

	public PartnersPerspectiveView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	@Override
	public Widget asWidget()
	{
		return this;
	}
}
