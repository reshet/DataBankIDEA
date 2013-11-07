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

import com.mresearch.databank.shared.User2DD_Choices;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;


/**
 * The UserAccount persistence-capable class holds information about a
 * given user of Connectr.  A bidirectional JDO owned relationship is used to
 * manage its child Friend data objects.
 */
@Entity
@Table(name = "USERMASSIVEANALISYS")
//@NamedQueries({
//    @NamedQuery(name = "UserAccount.getDefUser", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email AND x.accountType = :type AND x.password = :pswd"),
//    @NamedQuery(name = "UserAccount.getAccount", query = "SELECT x FROM UserAccount x WHERE x.emailAddress = :email AND x.password = :pswd")
//
//})
public class UserMassiveLocalAnalisys implements Serializable{
	//  @PrimaryKey
//  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Transient
    private EntityManager em;
   
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    private UserMassiveLocalSetting setting;
    private ArrayList<Double> distribution;
    private ArrayList<Double> distribution_valid;
    private String distr_type;
    private String name;
    private String comment;
    private String user2dd_choice = USER_2DD_FREQ;
    
    @Transient
    public static String USER_2DD_FREQ = "fr";
    @Transient
    public static String USER_2DD_PERC_COL = "pc";
    @Transient
    public static String USER_2DD_PERC_ROW = "pr";
    @Transient
    public static String USER_2DD_PERC_TABLE = "pt";
    
     @Temporal(TemporalType.TIMESTAMP)
    private Date date_saved;
    
    
   
    @ManyToOne(cascade={CascadeType.ALL})
    private Var var_involved_first;
    @ManyToOne(cascade={CascadeType.ALL})
    private Var var_involved_second;
    
    
    
    
  public UserMassiveLocalAnalisys() {
	  //accountType = "defaultUser";
  }
    public UserMassiveLocalAnalisys(UserAnalysisSaveDTO dto,EntityManager em) {
        setting = new UserMassiveLocalSetting(dto.getSeting());
        distribution = dto.getDistribution();
        distribution_valid = dto.getValid_distribution();
        distr_type = dto.getDistr_type();
        name = dto.getName();
        
        if(dto.getUser2dd_choice().equals(User2DD_Choices.FREQ))
        {
            user2dd_choice = USER_2DD_FREQ;
        }
        if(dto.getUser2dd_choice().equals(User2DD_Choices.PERC_ROW))
        {
            user2dd_choice = USER_2DD_PERC_ROW;
        }
        if(dto.getUser2dd_choice().equals(User2DD_Choices.PERC_COL))
        {
            user2dd_choice = USER_2DD_PERC_COL;
        }
        if(dto.getUser2dd_choice().equals(User2DD_Choices.PERC_ALL))
        {
            user2dd_choice = USER_2DD_PERC_TABLE;
        }

        /*switch(dto.getUser2dd_choice()){
            case FREQ:user2dd_choice = USER_2DD_FREQ; break;
            case PERC_COL:user2dd_choice = USER_2DD_PERC_COL; break;
            case PERC_ROW:user2dd_choice = USER_2DD_PERC_ROW; break;
            case PERC_ALL:user2dd_choice = USER_2DD_PERC_TABLE; break;
        }*/

        if(dto.getVar_1()!=null)
        {
            long var_id_1 = dto.getVar_1().getId();
            Var v1 = em.find(Var.class, var_id_1);
            var_involved_first = v1;
        }
        
        if(dto.getVar_2()!=null){
            long var_id_2 = dto.getVar_2().getId();
            Var v2 = em.find(Var.class, var_id_2);
             var_involved_second = v2;
        }
       // var_involved_first = 
	  //accountType = "defaultUser";
  }
  public UserMassiveLocalAnalisys(EntityManager emm) {
	  //accountType = "defaultUser";
          this.em = emm;
  }
  
  public UserAnalysisSaveDTO toDTO(EntityManager em)
  {
      UserAnalysisSaveDTO dto = new UserAnalysisSaveDTO();
      dto.setId(id);
      dto.setDistribution(new ArrayList<Double>(distribution));
      dto.setValid_distribution(new ArrayList<Double>(distribution_valid));
      dto.setDistr_type(distr_type);
      dto.setSeting(setting.toDTO(em));
      dto.setName(name);
      
      if(user2dd_choice.equals(USER_2DD_FREQ))dto.setUser2dd_choice(User2DD_Choices.FREQ);
      else if(user2dd_choice.equals(USER_2DD_PERC_COL))dto.setUser2dd_choice(User2DD_Choices.PERC_COL);
      else if(user2dd_choice.equals(USER_2DD_PERC_ROW))dto.setUser2dd_choice(User2DD_Choices.PERC_ROW);
      else if(user2dd_choice.equals(USER_2DD_PERC_TABLE))dto.setUser2dd_choice(User2DD_Choices.PERC_ALL);
      
      if(var_involved_first != null)dto.setVar_1(var_involved_first.toDTO_DetailedNoCalc(em));
      if(var_involved_second != null)dto.setVar_2(var_involved_second.toDTO_DetailedNoCalc(em));
      
      return dto;
  }
  public Integer getId() {
    return id;
  }

    /**
     * @return the setting
     */
    public UserMassiveLocalSetting getSetting() {
        return setting;
    }

    /**
     * @param setting the setting to set
     */
    public void setSetting(UserMassiveLocalSetting setting) {
        this.setting = setting;
    }

    /**
     * @return the distribution
     */
    public ArrayList<Double> getDistribution() {
        return distribution;
    }

    /**
     * @param distribution the distribution to set
     */
    public void setDistribution(ArrayList<Double> distribution) {
        this.distribution = distribution;
    }

    /**
     * @return the distr_type
     */
    public String getDistr_type() {
        return distr_type;
    }

    /**
     * @param distr_type the distr_type to set
     */
    public void setDistr_type(String distr_type) {
        this.distr_type = distr_type;
    }

 
 
}
