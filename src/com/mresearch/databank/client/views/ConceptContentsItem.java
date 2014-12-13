package com.mresearch.databank.client.views;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
import com.mresearch.databank.shared.ICatalogizable;


public class ConceptContentsItem extends TreeItem implements WrappedCustomLabel{
	private long contents_id;
	private Label l = new Label();
	public ConceptContentsItem(ICatalogizable dto)
	{
		super();
		this.contents_id = dto.getID();
		int end = dto.getTextRepresent().length();
            //> VarDescItem.LABEL_SHORTAGE_NUMBER ? VarDescItem.LABEL_SHORTAGE_NUMBER : dto.getTextRepresent().length();
		l.setWordWrap(true);
		l.setWidth("300px");
		l.setText(dto.getTextRepresent().substring(0, end));
		this.setWidget(l);
		l.setStylePrimaryName("gwt-TreeItem");
		if(dto.getTextRepresent()!=null) {
      this.setTitle(dto.getTextRepresent());
    }
	}
	public Label getLabel(){return l;}
	public long getContents_id() {
		return contents_id;
	}

}
