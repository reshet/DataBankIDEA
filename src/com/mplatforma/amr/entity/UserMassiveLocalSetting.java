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

import com.mresearch.databank.shared.UserResearchSettingDTO;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The UserAccount persistence-capable class holds information about a
 * given user of Connectr.  A bidirectional JDO owned relationship is used to
 * manage its child Friend data objects.
 */
@Entity
@Table(name = "USERMASSIVESETTING")
//@NamedQueries({
//    @NamedQuery(name = "UserAccount.getDefUser", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email AND x.accountType = :type AND x.password = :pswd"),
//    @NamedQuery(name = "UserAccount.getAccount", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email AND x.password = :pswd")
//
//})
public class UserMassiveLocalSetting implements Serializable{
	//  @PrimaryKey
//  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Transient
    private EntityManager em;
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    
    private Long research_id;
    private Integer weights_use = 0;
    private Integer filters_use = 0;
    private ArrayList<String> filters = new ArrayList<String>();
    private ArrayList<Integer> filters_usage = new ArrayList<Integer>();
    private ArrayList<Integer> weights_usage = new ArrayList<Integer>();
    private Integer weights_var_id = 0;
    
    
    
  public UserMassiveLocalSetting() {
	  //accountType = "defaultUser";
  }
   public UserMassiveLocalSetting(UserResearchSettingDTO dto) {
       this.research_id = dto.getResearh().getID();
       this.setFilters(dto.getFilters());
       this.setFilters_usage(dto.getFilters_usage());
       this.setFilters_use(dto.getFilters_use());
       this.setWeights_use(dto.getWeights_use());
       this.setWeights_var_id(dto.getWeights_var_id());
	  //accountType = "defaultUser";
  }
   public UserResearchSettingDTO toDTO(EntityManager emm) {
       UserResearchSettingDTO dto = new UserResearchSettingDTO();
       SocioResearch res = emm.find(SocioResearch.class, research_id); 
       if(res!=null)dto.setResearh(res.toDTO());
       dto.setFilters(getFilters());
       dto.setFilters_usage(getFilters_usage());
       dto.setFilters_use(getFilters_use());
       dto.setWeights_use(getWeights_use());
       dto.setWeights_var_id(getWeights_var_id());
	  //accountType = "defaultUser";
       return dto;
  } 
  public UserMassiveLocalSetting(EntityManager emm) {
	  //accountType = "defaultUser";
          this.em = emm;
  }
  
 
  public Integer getId() {
    return id;
  }

 
 
public ArrayList<Integer> getFilters_usage() {
	return filters_usage;
}
public void setFilters_usage(ArrayList<Integer> filters_usage) {
	this.filters_usage = filters_usage;
}

public Integer getWeights_use() {
	return weights_use;
}
public void setWeights_use(Integer weights_use) {
	this.weights_use = weights_use;
}

public Integer getFilters_use() {
	return filters_use;
}

public void setFilters_use(Integer filters_use) {
	this.filters_use = filters_use;
}

public ArrayList<String> getFilters() {
	return filters;
}

public void setFilters(ArrayList<String> filters) {
	this.filters = filters;
}

    /**
     * @return the weights_usage
     */
    public ArrayList<Integer> getWeights_usage() {
        return weights_usage;
    }

    /**
     * @param weights_usage the weights_usage to set
     */
    public void setWeights_usage(ArrayList<Integer> weights_usage) {
        this.weights_usage = weights_usage;
    }

    /**
     * @return the weights_var_id
     */
    public Integer getWeights_var_id() {
        return weights_var_id;
    }

    /**
     * @param weights_var_id the weights_var_id to set
     */
    public void setWeights_var_id(Integer weights_var_id) {
        this.weights_var_id = weights_var_id;
    }

    /**
     * @return the research_id
     */
    public Long getResearch_id() {
        return research_id;
    }

    /**
     * @param research_id the research_id to set
     */
    public void setResearch_id(Long research_id) {
        this.research_id = research_id;
    }

    /**
     * @return the weights_var_ids
     */
   

}
