package com.mresearch.databank.client.views.DBfillers;

import java.util.ArrayList;

import javax.xml.crypto.Data;

import com.google.gwt.core.client.GWT;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.AdminSocioResearchServiceAsync;
import com.mresearch.databank.shared.JSON_Representation;
import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitDateDTO;
import com.mresearch.databank.shared.MetaUnitStringDTO;


import org.omg.CORBA.DATA_CONVERSION;
import org.zenika.widget.client.datePicker.DatePicker;

public class FieldEditor extends Composite {

	private static FieldEditorUiBinder uiBinder = GWT
			.create(FieldEditorUiBinder.class);

	interface FieldEditorUiBinder extends
			UiBinder<Widget, FieldEditor> {
	}

	public FieldEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	private AdminSocioResearchServiceAsync service = AdminSocioResearchService.Util.getInstance();
	private MultiValuedField field;
	public MultiValuedField getField() {
		return field;
	}

	@UiField FlexTable host_table;
	private PopupPanel par;
	public FieldEditor(MultiValuedField field,PopupPanel parent) {
		initWidget(uiBinder.createAndBindUi(this));
		this.field = field;
		this.par = parent;
		initFields(field.dto.getSub_meta_units());
	}
	private void initFields(ArrayList<MetaUnitDTO> dtos)
	{
		host_table.clear();
		int i = 0;
		for(MetaUnitDTO dto:dtos)
		{
			//host_table.setWidget(i, 0, new FieldEditWrapper(field.subunits_table.getWidget(i, 0), this,i));
			host_table.setWidget(i, 0, new FieldEditWrapper(dto, this,i));
			i++;
		}
	}
	public void addNewField(MetaUnitDTO dto){
		host_table.setWidget(host_table.getRowCount(), 0, new FieldEditWrapper(dto, this,host_table.getRowCount()));
	}
	public void doSwap(int old_pos, int new_pos)
	{
		if(old_pos<0||new_pos<0||old_pos>=host_table.getRowCount()||new_pos>=host_table.getRowCount()||old_pos==new_pos) return;
		FieldEditWrapper w = (FieldEditWrapper)host_table.getWidget(old_pos, 0);
		FieldEditWrapper w2 = (FieldEditWrapper)host_table.getWidget(new_pos, 0);
		w.setPosition(new_pos);
		w2.setPosition(old_pos);
		host_table.setWidget(new_pos, 0, w);
		host_table.setWidget(old_pos, 0, w2);
		
	}
	public void doDelete(MetaUnitDTO dto)
	{
		field.dto.getSub_meta_units().remove(dto);
		field.renderSubUnits();
		initFields(field.dto.getSub_meta_units());
	}
	@UiHandler(value="doEdit") 
	public void doEdit(ClickEvent ev)
	{
		
//		field.subunits_table.clear();
//		for(int i = 0;i < host_table.getRowCount();i++)
//		{
//			field.subunits_table.setWidget(i, 0, ((FieldEditWrapper)host_table.getWidget(i, 0)).host.getWidget(0));
//		}
//		ArrayList<MetaUnitDTO> subunits = new ArrayList<MetaUnitDTO>();
//		for(int i = 0;i < field.subunits_table.getRowCount();i++)
//		{
//			FieldEditWrapper wrap = (FieldEditWrapper) field.subunits_table.getWidget(i,0);
//			//MetaUnitFiller cont = (MetaUnitFiller)wrap.getW();
//			//MetaUnitDTO dto = cont.getDTO();
//			MetaUnitDTO dto = wrap.getMU();
//			subunits.add(dto);
//		}
		
		ArrayList<MetaUnitDTO> subunits = new ArrayList<MetaUnitDTO>();
		for(int i = 0;i < host_table.getRowCount();i++)
		{
			FieldEditWrapper wrap = (FieldEditWrapper) host_table.getWidget(i,0);
			//MetaUnitFiller cont = (MetaUnitFiller)wrap.getW();
			//MetaUnitDTO dto = cont.getDTO();
			if(wrap != null){
				MetaUnitDTO dto = wrap.getMU();
				subunits.add(dto);	
			}
			
		}
		
		this.field.dto.setSub_meta_units(subunits);
		field.renderSubUnits();
		
		new RPCCall<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ошибка при обновлении структуры метаданных.");
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("Структура обновлена успешно!");
			}

			@Override
			protected void callService(AsyncCallback<Void> cb) {
				service.updateMetaUnitStructure(field.dto, cb);
			}
		}.retry(2);
		par.hide();
	}
	@UiHandler(value="doCancel") 
	public void doCancel(ClickEvent ev)
	{
		par.hide();
	}
	@UiHandler(value="doAddField") 
	public void doAddField(ClickEvent ev)
	{
		PopupPanel p = new PopupPanel();
		//p.setTitle("Добавить поле...");
		p.setModal(true);
		p.setSize("100%", "100%");
		p.setWidget(new FieldCreator(this,p));
		p.show();
		p.center();
	}
}
