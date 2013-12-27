package com.mresearch.databank.client.views;

import com.mresearch.databank.shared.FilterBaseDTO;

public interface IFilterProvider {
	public FilterBaseDTO getFilterDTO();
    public abstract boolean isFilterUsed();
    public abstract void clearFilter();
    
}
