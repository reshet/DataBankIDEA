package com.mresearch.databank.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("serial")
public class ComparativeSearchParamsDTO implements Serializable{
    private double barrier_variance;
    //we dont have on server JSON filter build implementation as on GWT client!!!
    private String research_filter;
    //private ShowFilterParameters research_filter;
   // private ArrayList<Long> filter_by_ids;
    public ComparativeSearchParamsDTO()
    {
    }

    /**
     * @return the barrier_variance
     */
    public double getBarrier_variance() {
        return barrier_variance;
    }

    /**
     * @param barrier_variance the barrier_variance to set
     */
    public void setBarrier_variance(double barrier_variance) {
        this.barrier_variance = barrier_variance;
    }

	public String getResearch_filter() {
		return research_filter;
	}

	public void setResearch_filter(String research_filter) {
		this.research_filter = research_filter;
	}

//	public ShowFilterParameters getResearch_filter() {
//		return research_filter;
//	}
//
//	public void setResearch_filter(ShowFilterParameters research_filter) {
//		this.research_filter = research_filter;
//	}

//	public ArrayList<Long> getFilter_by_ids() {
//		return filter_by_ids;
//	}
//
//	public void setFilter_by_ids(ArrayList<Long> filter_by_ids) {
//		this.filter_by_ids = filter_by_ids;
//	}

	
}
