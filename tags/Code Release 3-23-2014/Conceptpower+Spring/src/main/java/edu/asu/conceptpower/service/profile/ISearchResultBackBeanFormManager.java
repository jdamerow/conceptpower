package edu.asu.conceptpower.service.profile;

import java.util.List;

import edu.asu.conceptpower.web.profile.impl.SearchResultBackBean;

/**
 * the interface used for setting the searchresult to SearchResultBackBean class
 * retrieved from viaf service
 * 
 * @author rohit pendbhaje
 * 
 */
public interface ISearchResultBackBeanFormManager {

	public abstract List<SearchResultBackBean> getsearchResultBackBeanList(
			String serviceId, String term);

}