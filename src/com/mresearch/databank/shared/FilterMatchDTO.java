package com.mresearch.databank.shared;

import java.io.Serializable;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;


public class FilterMatchDTO extends FilterBaseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2443613870378329241L;
	private String filtering_operator = "==";
	private String filtering_value = "3.0";
	public FilterMatchDTO(){}
	public FilterMatchDTO(String target_class,String target_field, String operator,String value)
	{
		super(target_class,target_field);
		this.setFiltering_operator(operator);
		this.setFiltering_value(value);
	}
	public String getFiltering_operator() {
		return filtering_operator;
	}
	public void setFiltering_operator(String filtering_operator) {
		this.filtering_operator = filtering_operator;
	}
	public String getFiltering_value() {
		return filtering_value;
	}
	public void setFiltering_value(String filtering_value) {
		this.filtering_value = filtering_value;
	}
	@Override
	public String getFilter() {
		return super.getTarget_field_name()+" == '"+this.getFiltering_value()+"'";
	}
	@Override
	public JSONObject getJSONFilter() {
//	    JSONObject obj = new JSONObject();
//	    JSONObject q = new JSONObject();
//	    q.put(super.getTarget_field_name(), new JSONString(getFiltering_value()));
//	    obj.put("text", q);
//	    
	    JSONObject obj = new JSONObject();
	    JSONObject q = new JSONObject();
	    JSONObject qq = new JSONObject();
	    
	    //qq.put("value", new JSONString(getFiltering_value()));
	    //qq.put("min_similarity", new JSONNumber(0.5));
	    
	    q.put(super.getTarget_field_name(), new JSONString(getFiltering_value()));
	    obj.put("match_phrase", q);
	    
	    return obj;
	}
	
	
}

