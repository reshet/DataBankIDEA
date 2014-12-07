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
public class RegisteredUserAnalysisRoot implements Serializable{
	//  @PrimaryKey
//  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Transient
    private EntityManager em;
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval=true)
    @OrderColumn
    private List<UserMassiveLocalAnalisys> local_research_analisys;
    
    
  public RegisteredUserAnalysisRoot() {
	  //accountType = "defaultUser";
  }
  public RegisteredUserAnalysisRoot(EntityManager emm) {
	  //accountType = "defaultUser";
          this.em = emm;
  }
   
  public String getId() {
    return id;
  }

 
  public UserHistoryDTO updateAccountResearchState(UserHistoryDTO dto) {
//	    this.setFilters(dto.getFilters());
//	    this.setWeights_use(dto.getWeights_use());
//	    this.setFilters_use(dto.getFilters_use());
//	    this.setFilters_usage(dto.getFilters_usage());
//	    this.setFilters_categories(dto.getFilters_categories());
	    return dto;
	  }

    /**
     * @return the local_research_analisys
     */
    public List<UserMassiveLocalAnalisys> getLocal_research_analisys() {
        return local_research_analisys;
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

}
