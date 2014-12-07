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


import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserHistoryDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;


/**
 * The UserAccount persistence-capable class holds information about a
 * given user of Connectr.  A bidirectional JDO owned relationship is used to
 * manage its child Friend data objects.
 */
@Entity
@Table
@NamedQueries({
    @NamedQuery(name = "UserAccount.getDefUser", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email AND x.accountType = :type AND x.password = :pswd"),
    @NamedQuery(name = "UserAccount.getAccount", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email AND x.password = :pswd"),
     @NamedQuery(name = "UserAccount.findUser", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email")

})
public class UserAccount implements Serializable{
	//  @PrimaryKey
//  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Transient
    private EntityManager em;
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private String emailAddress;
    private String password;
    private String accountType;
//    private Integer weights_use = 0;
//    private Integer filters_use = 0;
//    private ArrayList<String> filters = new ArrayList<String>();
//    private ArrayList<Integer> filters_usage = new ArrayList<Integer>();
//    private ArrayList<Long> filters_categories = new ArrayList<Long>();
//    
    @OneToOne(cascade = CascadeType.PERSIST)
    private RegisteredUserHistory history;
      @OneToOne(cascade = CascadeType.PERSIST)
    private RegisteredUserAnalysisRoot analysis;
  
  public UserAccount() {
	  accountType = "defaultUser";
          history = new RegisteredUserHistory();
          analysis = new RegisteredUserAnalysisRoot();
  }
  public UserAccount(EntityManager emm) {
	  accountType = "defaultUser";
          history = new RegisteredUserHistory(emm);
           analysis = new RegisteredUserAnalysisRoot(emm);
          this.em = emm;
  }
  
  public UserAccount getUserAccount(String email,String password)
  {
       UserAccount account=null;
	try
	{
		TypedQuery<UserAccount> q = (TypedQuery<UserAccount>) em.createNamedQuery("UserAccount.getAccount");
		q.setParameter("email",email);
                q.setParameter("pswd", password);
                account = q.getSingleResult();
		
	}
        catch(Exception e)
        {
            e.printStackTrace();
        }
	finally
	{
		//em.close();
	}
        return account;
  }
   public UserAccount findUserAccount(String email)
  {
       UserAccount account=null;
	try
	{
		TypedQuery<UserAccount> q = (TypedQuery<UserAccount>) em.createNamedQuery("UserAccount.findUser");
		q.setParameter("email",email);
                account = q.getSingleResult();
		
	}
        catch(Exception e)
        {
            e.printStackTrace();
        }
	finally
	{
		//em.close();
	}
        return account;
  }
  public UserAccount getUserAccountUnsafe(Long id)
  {
       UserAccount account=null;
        if(!id.equals(0)){
            try
            {
                    account = em.find(UserAccount.class, id);

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                    //em.close();
            }
        }else {
            //account = new UserAccount();
            //account.setAccountType("simpleUser");
        }
        
        return account;
  }


/**
 * for this early version of the Connectr app, the 'current user' is always 
 * the same (the default user), so here this query is hardwired.
 * This model will change in the final version of the app.
 */
  public UserAccount getDefaultUser() {
    
    String defaultEmail = "default@user.com";
    
    
    UserAccount oneResult = null, detached = null;
    
    oneResult = new UserAccount();
    oneResult.setAccountType("simpleUser");
    oneResult.setEmailAddress(defaultEmail);
    oneResult.setPassword("pswd");
    oneResult.setName("Посетитель");
    
//    Query q = em.createNamedQuery("UserAccount.getDefUser");
//    q.setParameter("email", defaultEmail);
//    q.setParameter("type", "simpleUser");
//    q.setParameter("pswd", "default");
//    
//    try {
//      oneResult = (UserAccount) q.getSingleResult();
//      if (oneResult != null) {
//      // fetch friends list before detaching
//        //oneResult.getFriends();
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    } finally {
//     // em.close();
//    }
    
    return oneResult;
  }
  
  public void createDefaults()
  {
	    try {
	    	UserAccount defUser = new UserAccount();
	    	defUser.setAccountType("simpleUser");
	    	defUser.setEmailAddress("default@user.com");
	    	defUser.setPassword("default");
	    	em.persist(defUser);
	   
	    	UserAccount defRegUser = new UserAccount();
	    	defRegUser.setAccountType("simpleUser");
	    	defRegUser.setEmailAddress("registered@user.com");
	    	defRegUser.setPassword("default");
	    	em.persist(defRegUser);
	 	
	    	
	    	UserAccount defResearchAdmin = new UserAccount();
	    	defResearchAdmin.setAccountType("researchAdmin");
	    	defResearchAdmin.setEmailAddress("research@admin.com");
	    	defResearchAdmin.setPassword("default");
	    	em.persist(defResearchAdmin);
	    	
	    	UserAccount defLawAdmin = new UserAccount();
	    	defLawAdmin.setAccountType("lawAdmin");
	    	defLawAdmin.setEmailAddress("law@admin.com");
	    	defLawAdmin.setPassword("default");
	    	em.persist(defLawAdmin);
	    	
                UserAccount defPubAdmin = new UserAccount();
	    	defPubAdmin.setAccountType("pubAdmin");
	    	defPubAdmin.setEmailAddress("pub@admin.com");
	    	defPubAdmin.setPassword("default");
	    	em.persist(defPubAdmin);
	    	
	    	UserAccount defJuryConsultant = new UserAccount();
	    	defJuryConsultant .setAccountType("juryConsultant");
	    	defJuryConsultant .setEmailAddress("jury@consultant.com");
	    	defJuryConsultant.setPassword("default");
	    	em.persist(defJuryConsultant );
	    	
//	    	CatalogConcept concept = new CatalogConcept("SocioResearch");
//	    	concept.setName("Концепты каталогизации");
//	    	concept.setRoot(true);
//	    	em.persist(concept);
	    	
	    	UserAccount defSuperAdmin = new UserAccount();
	    	defSuperAdmin.setAccountType("superAdmin");
	    	defSuperAdmin.setEmailAddress("super@admin.com");
	    	defSuperAdmin.setPassword("superpassword");
	    	em.persist(defSuperAdmin);
	    	
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	    //  em.close();
	    }
  }
  

  public UserAccount getDefaultRegisteredUser() {
	    
	    String defaultEmail = "registered@user.com";
	    
	    UserAccount oneResult = null, detached = null;
            Query q = em.createNamedQuery("UserAccount.getDefUser");
            q.setParameter("email", defaultEmail);
            q.setParameter("type", "simpleUser");
            q.setParameter("pswd", "default");

            try {
              oneResult = (UserAccount) q.getSingleResult();
              if (oneResult != null) {
              // fetch friends list before detaching
                //oneResult.getFriends();
              }
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
             // em.close();
            }
            return oneResult;
	   // return detached;
	  }
  public UserAccount getDefaultResearchAdmin() {
	    
	    String defaultEmail = "research@admin.com";
	    
	    UserAccount oneResult = null, detached = null;
            Query q = em.createNamedQuery("UserAccount.getDefUser");
            q.setParameter("email", defaultEmail);
            q.setParameter("type", "researchAdmin");
            q.setParameter("pswd", "default");

            try {
              oneResult = (UserAccount) q.getSingleResult();
              if (oneResult != null) {
              // fetch friends list before detaching
                //oneResult.getFriends();
              }
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
            //  em.close();
            }
            return oneResult;
	  }

  public UserAccount getDefaultLawAdmin() {
	    
	    String defaultEmail = "law@admin.com";
	    
	    UserAccount oneResult = null, detached = null;
            Query q = em.createNamedQuery("UserAccount.getDefUser");
            q.setParameter("email", defaultEmail);
            q.setParameter("type", "lawAdmin");
            q.setParameter("pswd", "default");

            try {
              oneResult = (UserAccount) q.getSingleResult();
              if (oneResult != null) {
              // fetch friends list before detaching
                //oneResult.getFriends();
              }
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
             // em.close();
            }
            return oneResult;
	  }
  

  public UserAccount getDefaultJuryConsultant() {
	    
	    String defaultEmail = "jury@consultant.com";
	    
	    UserAccount oneResult = null, detached = null;
            Query q = em.createNamedQuery("UserAccount.getDefUser");
            q.setParameter("email", defaultEmail);
            q.setParameter("type", "juryConsultant");
            q.setParameter("pswd", "default");

            try {
              oneResult = (UserAccount) q.getSingleResult();
              if (oneResult != null) {
              // fetch friends list before detaching
                //oneResult.getFriends();
              }
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
             // em.close();
            }
            return oneResult;
	  }

  public UserAccount getDefaultSuperAdmin() {
	    
	    String defaultEmail = "super@admin.com";
	    
	    UserAccount oneResult = null, detached = null;
            Query q = em.createNamedQuery("UserAccount.getDefUser");
            q.setParameter("email", defaultEmail);
            q.setParameter("type", "superAdmin");
            q.setParameter("pswd", "default");

            try {
              oneResult = (UserAccount) q.getSingleResult();
              if (oneResult != null) {
              // fetch friends list before detaching
                //oneResult.getFriends();
              }
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
            //  em.close();
            }
            return oneResult;
	  }

  public void setBasicInfo(String name, String emailAddress) {
    this.name = name;
    this.emailAddress = emailAddress;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public static UserAccountDTO toDTO(UserAccount user) {
    if (user == null) {
      return null;
    }
    UserAccountDTO dto = new UserAccountDTO(user.getEmailAddress(), user.getName(), user.getAccountType());
    dto.setId(user.getId()==null?0:user.getId());
  //    dto.setFilters(user.history.getFilters());
  //  dto.setWeights_use(user.history.getWeights_use());
  // dto.setFilters_use(user.history.getFilters_use());
  // dto.setFilters_usage(user.history.getFilters_usage());
    
  //  dto.setFilters_categories(user.history.getFilters_categories());
    return dto;
  }
  public static UserHistoryDTO toHistoryDTO(UserAccount user,long research_id,EntityManager em) {
    if (user == null) {
      return null;
    }
    UserHistoryDTO dto = new UserHistoryDTO();
    UserResearchSettingDTO setting = new UserResearchSettingDTO();
    //long research_id = user.getHistory().updateAccountResearchState(dto);
    SocioResearch res = em.find(SocioResearch.class, research_id);
    setting.setResearh(res.toDTO());
    List<UserMassiveLocalSetting> lst = user.history.getLocal_research_settings();
    for(UserMassiveLocalSetting st:lst)
    {
        if(st.getResearch_id().equals(research_id))
        {
            setting.setFilters(st.getFilters());
            setting.setWeights_use(st.getWeights_use());
            setting.setFilters_use(st.getFilters_use());
            setting.setFilters_usage(st.getFilters_usage());
            setting.setWeights_var_id(st.getWeights_var_id());
            break;
        }
    }
    dto.setCurrent_research(setting);
//    dto.setFilters(user.history.getFilters());
//    dto.setWeights_use(user.history.getWeights_use());
//    dto.setFilters_use(user.history.getFilters_use());
//    dto.setFilters_usage(user.history.getFilters_usage());
//    
//    dto.setFilters_categories(user.history.getFilters_categories());
    return dto;
  }
  public UserHistoryDTO updateAccountResearchState(EntityManager em,UserHistoryDTO dto) {
    	    this.history.updateAccountResearchState(dto);
            em.persist(history);
            return dto;
	  }
//
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

public String getAccountType() {
	return accountType;
}

public void setAccountType(String accountType) {
	this.accountType = accountType;
}

public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
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
     * @return the history
     */
    public RegisteredUserHistory getHistory() {
        return history;
    }

    /**
     * @param history the history to set
     */
    public void setHistory(RegisteredUserHistory history) {
        this.history = history;
    }

    /**
     * @return the analysis
     */
    public RegisteredUserAnalysisRoot getAnalysis() {
        return analysis;
    }

}
