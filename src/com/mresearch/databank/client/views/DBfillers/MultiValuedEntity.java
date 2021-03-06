package com.mresearch.databank.client.views.DBfillers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.AdminSocioResearchServiceAsync;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.shared.JSON_Representation;
import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitDateDTO;
import com.mresearch.databank.shared.MetaUnitEntityItemDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.MetaUnitStringDTO;

public class MultiValuedEntity extends Composite implements MetaUnitFiller,MetaUnitEntityItemRegistrator{

	private static MultiValuedEntityUiBinder uiBinder = GWT
			.create(MultiValuedEntityUiBinder.class);

	interface MultiValuedEntityUiBinder extends
			UiBinder<Widget, MultiValuedEntity> {
	}

	public MultiValuedEntity() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	private AdminSocioResearchServiceAsync service = AdminSocioResearchService.Util.getInstance();
	private UserSocioResearchServiceAsync userService = UserSocioResearchService.Util.getInstance();
	
	@UiField Label entity_name;
	@UiField ListBox items_list;
	private MetaUnitMultivaluedEntityDTO dto;
	private JSON_Representation current_json;
	private HashMap<String,String> filling;
	private long previous_item_id;
	private String previous_item_name;
	@UiField ToggleButton editBtn;
	private String base_name;
	public DependentItemsConsumer consumer;
	public MultiValuedEntity(MetaUnitMultivaluedEntityDTO dto,JSON_Representation represent,HashMap<String,String> filling,String base_name,DependentItemsConsumer cons) {
		initWidget(uiBinder.createAndBindUi(this));
		this.dto = dto;
		entity_name.setText(dto.getDesc());
		this.filling = filling;
		this.consumer = cons;
		if(this.filling == null)this.filling = new HashMap<String,String>();
		this.base_name = base_name.equals("")?dto.getUnique_name():base_name+"_"+dto.getUnique_name();
		//items_list.setMultipleSelect(dto.isIsMultiselected());
		//items_list.setVisibleItemCount(1);
		//renderSubUnits();
		//items_list.se
		editBtn.setStyleName("metaoptsBtn");
		
		refreshMembersList();
	}
	
	@UiHandler(value="editBtn")
	  public void editBtnCmd(ClickEvent ev) {
	    final PopupPanel p = new PopupPanel();
	    //p.setTitle("Добавление экземпляра сущности...");
	    //p.setModal(false);
	    p.setAutoHideEnabled(true);
	    Widget source = (Widget) ev.getSource();
        int left = source.getAbsoluteLeft();
        int top = source.getAbsoluteTop();
        p.setPopupPosition(left, top);
	    //p.setPopupPosition(200, 200);
	    //p.setSize("190px", "100px");
	    p.setSize("100%", "100%");
	    Anchor addAnch = new Anchor("Добавить");
	    Anchor addsubAnch = new Anchor("Добавить подобъект");
	    Anchor delAnch = new Anchor("Удалить");
	    Anchor editAnch = new Anchor("Изменить структуру");
	    Anchor editstructAnch = new Anchor("Редактировать объект");
	    addAnch.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent ev) {
				addCmd(ev);
				p.hide();
			}
		});
	    addsubAnch.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent ev) {
				addCmdSub(ev);
				p.hide();
			}
		});
	    delAnch.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent ev) {
				delCmd(ev);
				p.hide();
			}
		});
	    editAnch.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent ev) {
				editCmd(ev);
				p.hide();
			}
		});
	    editstructAnch.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent ev) {
				editItemCmd(ev);
				p.hide();
			}
		});
	    
	    VerticalPanel pnl = new VerticalPanel();
	    pnl.add(addAnch);
	    pnl.add(addsubAnch);
	    pnl.add(editstructAnch);
	    pnl.add(delAnch);
	    pnl.add(editAnch);
	    p.add(pnl);
		p.show();
	  }
	 // @UiHandler(value="add")
	  public void addCmd(ClickEvent ev) {
	    PopupPanel p = new PopupPanel();
	    p.setTitle("Добавление экземпляра сущности...");
	    p.setModal(true);
	    //p.setPopupPosition(200, 200);
	    //p.setSize("300px", "400px");
	    p.setSize("100%", "100%");
	    p.setWidget(new ItemCreator(new MultiValuedField(this.dto, null, new HashMap<String,String>(),dto.getUnique_name()), this, p));
	    p.show();
	    p.center();
	  }
	 //@UiHandler(value="addsub")
	  public void addCmdSub(ClickEvent ev) {
	    PopupPanel p = new PopupPanel();
	    p.setTitle("Добавление подэкземпляра сущности...");
	    p.setModal(true);
	    //p.setPopupPosition(200, 200);
	    p.setSize("100%", "100%");
	    int ind = this.items_list.getSelectedIndex();
	    if (ind >= 0)
	    {
	      Long id = (Long)this.dto.getItem_ids().get(ind);
	      Long id_ent = Long.valueOf(this.dto.getId());
	      p.setWidget(new SubItemCreator(id.longValue(), new MultiValuedField(this.dto, null, new HashMap(),dto.getUnique_name()), this, p));
	      p.show();
	      p.center();
	    }
	  }
	 
	 
	//@UiHandler(value="delete") 
	public void delCmd(ClickEvent ev)
	{
		final int ind = items_list.getSelectedIndex();
		if(ind >=0)
		{
			final Long id = dto.getItem_ids().get(ind);
			final Long id_ent = Long.valueOf(this.dto.getId());
			new RPCCall<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Ошибка удаления значения!"+caught.getMessage());
				}
				@Override
				public void onSuccess(Void result) {
					Window.alert("Значение успешно удалено");
			          MultiValuedEntity.this.items_list.removeItem(ind);
				}
				@Override
				protected void callService(AsyncCallback<Void> cb) {
					service.deleteEntityItem(id,id_ent, cb);
				}
			}.retry(2);
		}
	}
	//@UiHandler(value="edit") 
	public void editCmd(ClickEvent ev)
	{
		PopupPanel p = new PopupPanel();
		//p.setTitle("Редактирование поля...");
		p.setModal(true);
		//p.setPopupPosition(400, 400);
		p.setSize("100%", "100%");
		p.setWidget(new FieldEditor(new MultiValuedField(dto, null, filling,dto.getUnique_name()),p));
		p.show();	
		p.center();
	}
	
	//@UiHandler(value="edit_item") 
	public void editItemCmd(ClickEvent ev)
	{
		int ind = items_list.getSelectedIndex();
		if(ind >=0)
		{
			final Long id = dto.getItem_ids().get(ind);
			final String name = dto.getItem_names().get(ind);
			new RPCCall<HashMap<String, String>>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error on getting entity item info!"+caught.getMessage());
				}
				@Override
				public void onSuccess(HashMap<String, String> result) {
					PopupPanel p = new PopupPanel();
					p.setTitle("Редактирование объекта...");
					p.setModal(true);
					//p.setPopupPosition(400, 400);
					p.setSize("100%", "100%");
					//p.setWidget(new EntityItemEditor(new MultiValuedField(dto, null, result),id,name,p));
					//p.show();	
					
			          MultiValuedField f = new MultiValuedField(MultiValuedEntity.this.dto, null, result,dto.getUnique_name());
			          EntityItemEditor ed = new EntityItemEditor(f, MultiValuedEntity.this, id, name, p);
			          p.setWidget(ed);
			          p.show();
			          p.center();
				}

				@Override
				protected void callService(
						AsyncCallback<HashMap<String, String>> cb) {
					userService.getEntityItem(id, cb);
				}
			}.retry(2);
		}
	}
	  public void refreshMembersList() {
		    new RPCCall<MetaUnitMultivaluedEntityDTO>()
		    {
		      public void onFailure(Throwable caught)
		      {
		        Window.alert("Error on updating memebers list!" + caught.getMessage());
		      }

		      public void onSuccess(MetaUnitMultivaluedEntityDTO result)
		      {
		        MultiValuedEntity.this.dto = result;
		        MultiValuedEntity.this.renderSubUnits();
		        updateConsumerItems();
		      }

		      protected void callService(AsyncCallback<MetaUnitMultivaluedEntityDTO> cb)
		      {
		        MultiValuedEntity.this.userService.getMetaUnitMultivaluedEntityDTO_FlattenedItems(MultiValuedEntity.this.dto.getId(), cb);
		      }
		    }
		    .retry(2);
		  }
	private void renderSubUnits()
	{
		items_list.clear();
		ArrayList<String> base = dto.getItem_names();
		int i = 0;
		if(base!=null)
		for(String dto_name:base)
		{
//			if(dto instanceof MetaUnitStringDTO)
//			{
//				MetaUnitStringDTO dto_str = (MetaUnitStringDTO)dto;
//				subunits_table.setWidget(i++, 0, new SimpleStringField(dto_str,null));
//			}else
//			if(dto instanceof MetaUnitDateDTO)
//			{ 
//				MetaUnitDateDTO dto_str = (MetaUnitDateDTO)dto;
//				subunits_table.setWidget(i++, 0, new SimpleDateField(dto_str,null));
//			}else
//			if(dto instanceof MetaUnitMultivaluedDTO)
//			{
//				MetaUnitMultivaluedDTO dto_str = (MetaUnitMultivaluedDTO)dto;
//				items_list.addItem(dto_str.getDesc());
//				//subunits_table.setWidget(i++, 0, new MultiValuedEntity(dto_str,null));
//			}
			items_list.addItem(dto_name);
			//. Подзаконные акты
			//.  Подзаконные акты
			
		}
		if(filling.containsKey(base_name))
		{
			  String val = (String)this.filling.get(base_name);
		      if (val != null)
		      {
		        int index = this.dto.getItem_names().indexOf(val);
		        if(index>=0)
		        {
		        	this.items_list.setSelectedIndex(index);

			        this.previous_item_id = ((Long)this.dto.getItem_ids().get(index)).longValue();

		        }		 
		      }
		}
	}
	
	
	private void rebuildJSON()
	{
		JSONObject obj = new JSONObject();
		int index =-1;
		index = this.items_list.getSelectedIndex();
	    if(index>=0)obj.put(base_name, new JSONString(this.items_list.getItemText(index)));
	    this.current_json = new JSON_Representation(obj);
	    
	    //this.populateItemsLinksTo(id, identifier);
		//here to build JSON from children;
	}
	@Override
	public String getUniqueName() {
		return dto.getUnique_name();
	}
	@Override
	public JSON_Representation getJSON() {
		rebuildJSON();
		return current_json;
	}

	@Override
	public String getFilledValue() {
		 int index = 0;
		    if (this.items_list.getItemCount() <= 0) return null;
		    if (this.items_list.getSelectedIndex() != -1) index = this.items_list.getSelectedIndex();
		    LinkedList<Integer> selectedItems = new LinkedList<Integer>();
		    for (int i = 0; i < this.items_list.getItemCount(); i++) {
		      if (this.items_list.isItemSelected(i)) {
		        selectedItems.add(Integer.valueOf(i));
		      }
		    }
		    StringBuilder bu = new StringBuilder();
		    String val = null;
		    if (selectedItems.size() > 1)
		    {
		      for (int i = 0; i < selectedItems.size() - 1; i++)
		      {
		        bu.append(this.items_list.getItemText(((Integer)selectedItems.get(i)).intValue()));
		        bu.append("||");
		      }
		      bu.append(this.items_list.getItemText(((Integer)selectedItems.get(selectedItems.size() - 1)).intValue()));
		      val = bu.toString();
		    }
		    else {
		      val = this.items_list.getItemText(index);
		    }
		    return val;
	}
	@Override
	public MetaUnitDTO getDTO() {
		return dto;
	}
	
	
	
	
	private void deletePreviousTag(final MetaUnitEntityItemDTO result,final Long id_sys_ent,final long id_item_sel,final String identifier)
	{
		 if (result.getTagged_entities_ids().contains(id_sys_ent))
         {
			 result.getTagged_entities_identifiers().remove(result.getTagged_entities_ids().indexOf(id_sys_ent));
	         result.getTagged_entities_ids().remove(id_sys_ent);
         }

         new RPCCall<MetaUnitEntityItemDTO>()
         {
           public void onFailure(Throwable caught) {
          	  Window.alert("Error getting ItemDTO 2"+caught.getMessage());
    	      }

           public void onSuccess(final MetaUnitEntityItemDTO result2) {
             if (!result2.getTagged_entities_ids().contains(id_sys_ent))
             {
               result2.getTagged_entities_ids().add(id_sys_ent);
               result2.getTagged_entities_identifiers().add(identifier);
             }
             new RPCCall<Void>()
             {
               public void onFailure(Throwable caught)
               {
              	  Window.alert("Error updating links "+caught.getMessage());
        	      }

               public void onSuccess(Void result) {
            	   simpleUpdateTag(id_sys_ent, id_item_sel, identifier);
               }

               protected void callService(AsyncCallback<Void> cb) {
                 MultiValuedEntity.this.service.updateMetaUnitEntityItemLinks(result, result2, cb);
               }
             }
             .retry(2);
           }

           protected void callService(AsyncCallback<MetaUnitEntityItemDTO> cb)
           {
             MultiValuedEntity.this.userService.getEntityItemDTO(Long.valueOf(id_item_sel), cb);
           }
         }
         .retry(2);

	}
	
	private void simpleUpdateTag(final Long id,final long idd,final String identifier)
	{
	     new RPCCall<MetaUnitEntityItemDTO>()
         {
           public void onFailure(Throwable caught) {
          	  Window.alert("Error getting ItemDTO 2"+caught.getMessage());
    	      }

           public void onSuccess(final MetaUnitEntityItemDTO result2) {
             if (!result2.getTagged_entities_ids().contains(id))
             {
               result2.getTagged_entities_ids().add(id);
               result2.getTagged_entities_identifiers().add(identifier);
             }
             new RPCCall<Void>()
             {
               public void onFailure(Throwable caught)
               {
              	  Window.alert("Error updating links "+caught.getMessage());
        	      }

               public void onSuccess(Void result) {
               }

               protected void callService(AsyncCallback<Void> cb) {
                 MultiValuedEntity.this.service.updateMetaUnitEntityItemLinks(result2, cb);
               }
             }
             .retry(2);
           }

           protected void callService(AsyncCallback<MetaUnitEntityItemDTO> cb)
           {
             MultiValuedEntity.this.userService.getEntityItemDTO(Long.valueOf(idd), cb);
           }
         }
         .retry(2);

	}
	private void getPreviousDistrib(final Long prev_id,final Long id_sys_ent,final Long id_item_sel,final String identifier)
	{
		if(prev_id != null && prev_id != 0)
		{
			new RPCCall<MetaUnitEntityItemDTO>()
	        {
	          public void onFailure(Throwable caught) {
	        	  Window.alert("Error getting ItemDTO "+caught.getMessage());
	          }

	          public void onSuccess(final MetaUnitEntityItemDTO result) {
	        	  deletePreviousTag(result, id_sys_ent, id_item_sel, identifier);
	          }

	          protected void callService(AsyncCallback<MetaUnitEntityItemDTO> cb)
	          {
	            MultiValuedEntity.this.userService.getEntityItemDTO(prev_id, cb);
	          }
	        }
	        .retry(2);
		}
		else
		{
			simpleUpdateTag(id_sys_ent, id_item_sel, identifier);
		}
	}
	@Override
	public void populateItemsLinksTo(final Long id_sys_ent, final String identifier) {
	    int index = 0;
	    if ((this.items_list.getItemCount() > 0) && (this.items_list.getSelectedIndex() != -1))
	    {
	      index = this.items_list.getSelectedIndex();
	      final long id_item_selected = ((Long)this.dto.getItem_ids().get(index)).longValue();

	      if (id_item_selected != this.previous_item_id)
	      {
	    	  getPreviousDistrib(this.previous_item_id,id_sys_ent,id_item_selected,identifier);
	      }
	    	
	    }
	}
	
	public void updateConsumerItems()
	{
		if(consumer!=null)consumer.updateItems(dto.getItem_names(), dto.getItem_ids());
	}
}
