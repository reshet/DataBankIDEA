/** 
 * Copyright 2010 Daniel Guermeur and Amy Unruh
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   See http://connectrapp.appspot.com/ for a demo, and links to more information 
 *   about this app and the book that it accompanies.
 */
package com.mplatforma.amr.entity;


import java.util.ArrayList;

import com.mresearch.databank.shared.UserHistoryDTO;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;


/**
 * The UserAccount persistence-capable class holds information about a
 * given user of Connectr.  A bidirectional JDO owned relationship is used to
 * manage its child Friend data objects.
 */
@Entity
@Table(name = "USERHISTORY")
//@NamedQueries({
//    @NamedQuery(name = "UserAccount.getDefUser", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email AND x.accountType = :type AND x.password = :pswd"),
//    @NamedQuery(name = "UserAccount.getAccount", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email AND x.password = :pswd")
//
//})
public class RegisteredUserHistory implements Serializable{
	//  @PrimaryKey
//  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Transient
    private EntityManager em;
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval=true)
    @OrderColumn
    private List<UserMassiveLocalSetting> local_research_settings;
    
//    private Integer weights_use = 0;
//    private Integer filters_use = 0;
//    private ArrayList<String> filters = new ArrayList<String>();
//    private ArrayList<Integer> filters_usage = new ArrayList<Integer>();
//    private ArrayList<Long> filters_categories = new ArrayList<Long>();
//  
    private ArrayList<Long> favourite_massives = new ArrayList<Long>();
    
  public RegisteredUserHistory() {
	  //accountType = "defaultUser";
  }
  public RegisteredUserHistory(EntityManager emm) {
	  //accountType = "defaultUser";
          this.em = emm;
  }
   
  public String getId() {
    return id;
  }

//done in upper layer.
//    public UserMassiveLocalSetting getAccountResearchState(long res_id) {
//        UserMassiveLocalSetting setting = new UserMassiveLocalSetting();    
//        if(local_research_settings == null)   local_research_settings = new ArrayList<UserMassiveLocalSetting>();
//            if(!local_research_settings.isEmpty())
//            {
//                for(UserMassiveLocalSetting st:local_research_settings)
//                {
//                    if(st.getResearch_id().equals(res_id))
//                    {
//                        setting.setFilters(st.getFilters());
//                        setting.setWeights_use(st.getWeights_use());
//                        setting.setFilters_use(st.getFilters_use());
//                        setting.setFilters_usage(st.getFilters_usage());
//                        setting.setWeights_var_id(st.getWeights_var_id());
//                        break;
//                    }
//                }
//            }
//            return setting;
//     }
  public void updateAccountResearchState(UserHistoryDTO dto) {
            long res_id = dto.getCurrent_research().getResearh().getId();
            if(local_research_settings == null)   local_research_settings = new ArrayList<UserMassiveLocalSetting>();
            boolean found = false;
            if(!local_research_settings.isEmpty())
            {
                for(UserMassiveLocalSetting st:local_research_settings)
                {
                    if(st.getResearch_id().equals(res_id))
                    {
                        st.setFilters(dto.getCurrent_research().getFilters());
                        st.setWeights_use(dto.getCurrent_research().getWeights_use());
                        st.setFilters_use(dto.getCurrent_research().getFilters_use());
                        st.setFilters_usage(dto.getCurrent_research().getFilters_usage());
                        st.setWeights_var_id(dto.getCurrent_research().getWeights_var_id());
                        found = true;
                        break;
                    }
                }
            }
            if(!found){
                UserMassiveLocalSetting st = new UserMassiveLocalSetting();
                st.setResearch_id(res_id);
                st.setFilters(dto.getCurrent_research().getFilters());
                st.setWeights_use(dto.getCurrent_research().getWeights_use());
                st.setFilters_use(dto.getCurrent_research().getFilters_use());
                st.setFilters_usage(dto.getCurrent_research().getFilters_usage());
                st.setWeights_var_id(dto.getCurrent_research().getWeights_var_id());
                local_research_settings.add(st);
            }
            
             
//	    this.setFilters(dto.getFilters());
//	    this.setWeights_use(dto.getWeights_use());
//	    this.setFilters_use(dto.getFilters_use());
//	    this.setFilters_usage(dto.getFilters_usage());
//	    this.setFilters_categories(dto.getFilters_categories());
	    //return dto;
	  }

//public ArrayList<Integer> getFilters_usage() {
//	return filters_usage;
//}
//public void setFilters_usage(ArrayList<Integer> filters_usage) {
//	this.filters_usage = filters_usage;
//}
//public ArrayList<Long> getFilters_categories() {
//	return filters_categories;
//}
//public void setFilters_categories(ArrayList<Long> filters_categories) {
//	this.filters_categories = filters_categories;
//}
//
//
//public Integer getWeights_use() {
//	return weights_use;
//}
//public void setWeights_use(Integer weights_use) {
//	this.weights_use = weights_use;
//}
//
//public Integer getFilters_use() {
//	return filters_use;
//}
//
//public void setFilters_use(Integer filters_use) {
//	this.filters_use = filters_use;
//}
//
//public ArrayList<String> getFilters() {
//	return filters;
//}
//
//public void setFilters(ArrayList<String> filters) {
//	this.filters = filters;
//}

    /**
     * @return the local_research_settings
     */
    public List<UserMassiveLocalSetting> getLocal_research_settings() {
        return local_research_settings;
    }
    public List<UserMassiveLocalSetting> getFavouriteMassives() {
        List<UserMassiveLocalSetting> res = new ArrayList<UserMassiveLocalSetting>();
        for(UserMassiveLocalSetting set:local_research_settings){
            if(favourite_massives.contains(set.getResearch_id())) res.add(set);
        }
        return res;
    }
    public void addToFavouriteMassives(UserMassiveLocalSetting setting){
        
         if(!favourite_massives.contains(setting.getResearch_id())){
              boolean found = false;
                for(UserMassiveLocalSetting sett:local_research_settings)
                {
                    if(sett.getResearch_id().equals(setting.getResearch_id())){
                        found = true;
                        
                        break;
                    }
                }
                if (!found)local_research_settings.add(setting);
                
              favourite_massives.add(setting.getResearch_id());
         }               
    }

}
