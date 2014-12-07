package com.mresearch.databank.client.views.DBfillers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.AdminSocioResearchServiceAsync;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.shared.JSON_Representation;
import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitEntityItemDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
	private Long previous_item_id = null;
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
		if (this.filling == null) {
      this.filling = new HashMap<String,String>();
    }
		this.base_name = base_name.equals("") ? dto.getUnique_name() : base_name + "_" + dto.getUnique_name();

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

	  public void addCmd(ClickEvent ev) {
	    PopupPanel p = new PopupPanel();
	    p.setTitle("Добавление экземпляра сущности...");
	    p.setModal(true);
	    p.setSize("100%", "100%");
	    p.setWidget(new ItemCreator(new MultiValuedField(this.dto, null, new HashMap<String,String>(),dto.getUnique_name()), this, p));
	    p.show();
	    p.center();
	  }
	  public void addCmdSub(ClickEvent ev) {
	    PopupPanel p = new PopupPanel();
	    p.setTitle("Добавление подэкземпляра сущности...");
	    p.setModal(true);
	    p.setSize("100%", "100%");
	    int ind = this.items_list.getSelectedIndex();
	    if (ind > 0)
	    {
	      Long id = this.dto.getItem_ids().get(ind - 1);
	      p.setWidget(new SubItemCreator(id, new MultiValuedField(this.dto, null, new HashMap(),dto.getUnique_name()), this, p));
	      p.show();
	      p.center();
	    }
	  }

	public void delCmd(ClickEvent ev) {
		final int ind = items_list.getSelectedIndex();
		if(ind > 0)
		{
			final Long id = dto.getItem_ids().get(ind - 1);
			final Long id_ent = this.dto.getId();
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

	public void editCmd(ClickEvent ev) {
		PopupPanel p = new PopupPanel();
		p.setModal(true);
		p.setSize("100%", "100%");
		p.setWidget(new FieldEditor(new MultiValuedField(dto, null, filling,dto.getUnique_name()),p));
		p.show();	
		p.center();
	}
	
	public void editItemCmd(ClickEvent ev)
	{
		int ind = items_list.getSelectedIndex();
		if(ind > 0)
		{
			final Long id = dto.getItem_ids().get(ind - 1);
			final String name = dto.getItem_names().get(ind - 1);
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
					p.setSize("100%", "100%");
          MultiValuedField f = new MultiValuedField(MultiValuedEntity.this.dto, null, result,dto.getUnique_name());
          EntityItemEditor ed = new EntityItemEditor(f, MultiValuedEntity.this, id, name, p);
          p.setWidget(ed);
          p.show();
          p.center();
				}

				@Override
				protected void callService(AsyncCallback<HashMap<String, String>> cb) {
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
    items_list.addItem("");
    ArrayList<String> base = dto.getItem_names();
    if (base != null) {
      for(String dto_name:base)
      {
        items_list.addItem(dto_name);
      }
    }

		if (filling.containsKey(base_name)) {
			  String val = this.filling.get(base_name);
        if (val != null && !val.equals("")) {
          int index = this.dto.getItem_names().indexOf(val);
          if(index >= 0)
          {
            this.items_list.setSelectedIndex(index + 1);
            this.previous_item_id = this.dto.getItem_ids().get(index);
          }
        }
		}
	}
	
	private void rebuildJSON()
	{
		JSONObject obj = new JSONObject();
		int index = -1;
		index = this.items_list.getSelectedIndex();
    if (index >= 0) {
      obj.put(base_name, new JSONString(this.items_list.getItemText(index)));
    }
    this.current_json = new JSON_Representation(obj);
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
		    if (this.items_list.getItemCount() <= 0) {
          return null;
        }
		    if (this.items_list.getSelectedIndex() != -1) {
          index = this.items_list.getSelectedIndex();
        }
		    LinkedList<Integer> selectedItems = new LinkedList<Integer>();
		    for (int i = 0; i < this.items_list.getItemCount(); i++) {
		      if (this.items_list.isItemSelected(i)) {
		        selectedItems.add(i);
		      }
		    }
		    StringBuilder bu = new StringBuilder();
		    String val = null;
		    if (selectedItems.size() > 1)
		    {
		      for (int i = 0; i < selectedItems.size() - 1; i++)
		      {
		        bu.append(this.items_list.getItemText(selectedItems.get(i)));
		        bu.append("||");
		      }
		      bu.append(this.items_list.getItemText(selectedItems.get(selectedItems.size() - 1)));
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

	private void deletePreviousTag(final MetaUnitEntityItemDTO result,final Long id_sys_ent,final Long id_item_sel,final String identifier)
	{
		 if (result.getTagged_entities_ids().contains(id_sys_ent)){
			     result.getTagged_entities_identifiers().remove(result.getTagged_entities_ids().indexOf(id_sys_ent));
	         result.getTagged_entities_ids().remove(id_sys_ent);
     }

    new RPCCall<Void>()
    {
      public void onFailure(Throwable caught)
      {
        Window.alert("Error updating links "+caught.getMessage());
      }

      public void onSuccess(Void result) {
        if (id_item_sel != null) {
          simpleUpdateTag(id_sys_ent, id_item_sel, identifier);
        }
      }

      protected void callService(AsyncCallback<Void> cb) {
        MultiValuedEntity.this.service.updateMetaUnitEntityItemLinks(result, cb);
      }
    }.retry(2);
	}
	
	private void simpleUpdateTag(final Long id,final Long id_item_selected, final String identifier)
	{
	     new RPCCall<MetaUnitEntityItemDTO>() {
           public void onFailure(Throwable caught) {
          	  Window.alert("Error getting ItemDTO 2 " + caught.getMessage());
    	      }

           public void onSuccess(final MetaUnitEntityItemDTO result) {
             if (!result.getTagged_entities_ids().contains(id))
             {
               result.getTagged_entities_ids().add(id);
               result.getTagged_entities_identifiers().add(identifier);
             }
             new RPCCall<Void>(){
               public void onFailure(Throwable caught)
               {
              	  Window.alert("Error updating links "+caught.getMessage());
        	     }
               public void onSuccess(Void result) {}
               protected void callService(AsyncCallback<Void> cb) {
                 MultiValuedEntity.this.service.updateMetaUnitEntityItemLinks(result, cb);
               }
             }.retry(2);
           }

           protected void callService(AsyncCallback<MetaUnitEntityItemDTO> cb)
           {
             MultiValuedEntity.this.userService.getEntityItemDTO(id_item_selected, cb);
           }
       }.retry(2);

	}
	private void getPreviousDistrib(final Long prev_id,final Long id_sys_ent,final Long id_item_sel,final String identifier)
	{
		if (prev_id != null) {
			new RPCCall<MetaUnitEntityItemDTO>() {
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
	    }.retry(2);
		} else {
      if (id_item_sel != null) {
        simpleUpdateTag(id_sys_ent, id_item_sel, identifier);
      }
		}
	}
	@Override
	public void populateItemsLinksTo(final Long id_sys_ent, final String identifier) {
	    int index = 0;
	    if ((this.items_list.getItemCount() > 0) && (this.items_list.getSelectedIndex() != -1))
	    {
	      index = this.items_list.getSelectedIndex();
        Long id_item_selected = null;
        if (index > 0) {
          id_item_selected = this.dto.getItem_ids().get(index - 1);
        }

	      if (id_item_selected == null || !id_item_selected.equals(this.previous_item_id))
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
