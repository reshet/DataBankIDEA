/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.service;

import com.mplatforma.amr.entity.*;
import com.mplatforma.amr.service.remote.AdminPubBeanRemote;
import com.mplatforma.amr.service.remote.UserAccountBeanRemote;
import com.mresearch.databank.shared.*;
import org.elasticsearch.common.jackson.core.JsonFactory;
import org.elasticsearch.common.jackson.core.JsonParseException;
import org.elasticsearch.common.jackson.core.JsonParser;
import org.elasticsearch.common.jackson.core.JsonToken;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author reshet
 */
@WebService
@Stateless(mappedName="UserAccountRemoteBean",name="UserAccountRemoteBean")
public class UserAccountSessionBean implements UserAccountBeanRemote{

     //    (mappedName="UserSocioResearchRemoteBean",
        //    lookup="java:global/DatabankEnterprise-ejb/UserSocioResearchRemoteBean") 
    
//     static
//    {
//         Locale locale = Locale.getDefault();
//           System.out.println("Before setting, Locale is = " + locale);
//         locale = new Locale("ru","RU");
//        //  // Setting default locale  
//        // // locale = Locale.ITALY;
//         Locale.setDefault(locale);
//          System.out.println("After setting, Locale is = " + locale);
//    }
//    
    @PersistenceContext
    private EntityManager em;
    
    @EJB 
    private AdminPubBeanRemote pubbean;
//    @EJB
//    private UserSocioResearchSessionBean userbean;
   
    public static String PUBLICATION_TOPIC="topic";
    public static String CONSULTATION_TOPIC="topic";
    @Override
    public UserAccountDTO getUserAccount(String email, String password) {
        
        
       //initDefaults();
       //createDefaultDatabankStructure();
       //return new UserAccountDTO(email,email, password);
       //createDefaultDatabankVarStructure();
       //  createDefaultDatabankVarStructure();
       //createDefaultDatabankLawStructure();
       //    initDefaults();
       // createDefaultStartPage();
       // script();
       return UserAccount.toDTO(new UserAccount(em).getUserAccount(email, password));
    } 
    @Override
    public UserAccountDTO getUserAccountOrRegisterByOAuthToken(String token) {
        LoginInfo info = loginDetails(token);
        
        
        String email = info.email;
        String name = info.name;
        UserAccount acc = new UserAccount(em).findUserAccount(email);
        if (acc==null)acc = registerUser(email,token,"simpleUser",name);
              
        return UserAccount.toDTO(acc);
    }
    
    private UserAccount registerUser(String email,String password,String type_access,String name){
         UserAccount defRegUser = new UserAccount();
	    	defRegUser.setAccountType(type_access);
	    	defRegUser.setEmailAddress(email);
	    	defRegUser.setPassword(password);
                defRegUser.setName(name);
	    	em.persist(defRegUser);
         return em.find(UserAccount.class, defRegUser.getId());
    }
    private static final Logger log = Logger.getLogger(UserAccountSessionBean.class.getCanonicalName());
    private class LoginInfo{
        public String email="",name="";
    }
    
    private LoginInfo loginDetails(final String token) {
		String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token;

		final StringBuffer r = new StringBuffer();
		try {
			final URL u = new URL(url);
			final URLConnection uc = u.openConnection();
			final int end = 30000;
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				isr = new InputStreamReader(uc.getInputStream());
				br = new BufferedReader(isr);
				final int chk = 0;
				while ((url = br.readLine()) != null) {
					if ((chk >= 0) && ((chk < end))) {
						r.append(url).append('\n');
					}
				}
			} catch (final java.net.ConnectException cex) {
				r.append(cex.getMessage());
			} catch (final Exception ex) {
				log.log(Level.SEVERE, ex.getMessage());
			} finally {
				try {
					br.close();
				} catch (final Exception ex) {
					log.log(Level.SEVERE, ex.getMessage());
				}
			}
		} catch (final Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}

		final LoginInfo loginInfo = new LoginInfo();
		try {
			final JsonFactory f = new JsonFactory();
			JsonParser jp;
			jp = f.createJsonParser(r.toString());
			jp.nextToken();
			while (jp.nextToken() != JsonToken.END_OBJECT) {
				final String fieldname = jp.getCurrentName();
				jp.nextToken();
//				if ("picture".equals(fieldname)) {
//					loginInfo.setPictureUrl(jp.getText());
//				} else 
//                                    
        if ("name".equals(fieldname)) {
					loginInfo.name = jp.getText();
				} else if ("email".equals(fieldname)) {
					loginInfo.email = jp.getText();
				}
			}
		} catch (final JsonParseException e) {
			log.log(Level.SEVERE, e.getMessage());
		} catch (final IOException e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		return loginInfo;
	}

    @Override
    public void initDefaults() {

        System.out.println("Initing defaults!");
        
        new UserAccount(em).createDefaults();
        createDefaultDatabankStructure();
        createDefaultDatabankVarStructure();
//        //createDefaultDatabankLawStructure();
//        createDefaultDatabankPubStructure();
//        //createDefaultDatabankJuryStructure();
//        createDefaultStartPage();
//        
        System.out.println("Initing defaults finished!");
        
    }
     private void createDefaultDatabankStructure()
      {
          //DatabankStructure<SocioResearch> db = new DatabankStructure<SocioResearch>("socioresearch");
          //MetaUnitMultivalued<SocioResearch> root = new MetaUnitMultivalued<SocioResearch>("Socioresearch Metadata Structure");
          
          DatabankStructure db = new DatabankStructure("socioresearch");
         
          MetaUnitMultivaluedEntity root = new MetaUnitMultivaluedEntity("socioresearch","Socioresearch Metadata Structure",0);
          ArrayList<MetaUnit> arr = new ArrayList<MetaUnit>();
          
          arr.add(new MetaUnitString("name","Название исследования"));
          
          //MetaUnitMultivalued<SocioResearch> dates = new MetaUnitMultivalued<SocioResearch>("Даты исследования");
          MetaUnitMultivaluedStructure dates = new MetaUnitMultivaluedStructure("dates","Даты исследования");
          ArrayList<MetaUnit> arr_d = new ArrayList<MetaUnit>();
          arr_d.add(new MetaUnitDate("start_date","Дата начала полевого этапа"));
          arr_d.add(new MetaUnitDate("end_date","Дата конца полевого этапа"));
          dates.setSub_meta_units(arr_d);
          arr.add(dates);
          
          //MetaUnitMultivalued<SocioResearch> gen_geath = new MetaUnitMultivalued<SocioResearch>("Генеральная совокупность");          
          MetaUnitMultivaluedEntity gen_geath = new MetaUnitMultivaluedEntity("gen_geath","Генеральная совокупность",0);          
          arr.add(gen_geath);
          
          arr.add(new MetaUnitInteger("sel_size","Объем выборки"));
          
          //MetaUnitMultivalued<SocioResearch> selection = new MetaUnitMultivalued<SocioResearch>("Способ генерации выборки");          
          MetaUnitMultivaluedStructure selection = new MetaUnitMultivaluedStructure("generation_type","Способ генерации выборки");          
          ArrayList<MetaUnit> selection_variants = new ArrayList<MetaUnit>();
          selection.setSub_meta_units(selection_variants);
          
          ArrayList<MetaUnitEntityItem> selection_randomity = new ArrayList<MetaUnitEntityItem>();
          ArrayList<MetaUnitEntityItem> selection_levels = new ArrayList<MetaUnitEntityItem>();
          
          MetaUnitMultivaluedEntity randomity = new MetaUnitMultivaluedEntity("randomity","случайность", 0);
          MetaUnitMultivaluedEntity levels = new MetaUnitMultivaluedEntity("levels","ступенчастость", 0);
          
          
          
          selection_levels.add(new MetaUnitEntityItem("одноступенчатая"));
          selection_levels.add(new MetaUnitEntityItem("многоступенчатая"));
          selection_randomity.add(new MetaUnitEntityItem("случайная"));
          selection_randomity.add(new MetaUnitEntityItem("неслучайная"));
          
          randomity.setItems(selection_randomity);
          levels.setItems(selection_levels);
          
          selection_variants.add(randomity);
          selection_variants.add(levels);
          
          arr.add(selection);
          
          MetaUnitMultivaluedEntity organization = new MetaUnitMultivaluedEntity("organization","Организация", 0);
          ArrayList<MetaUnit> org_subfields = new ArrayList<MetaUnit>();
          org_subfields.add(new MetaUnitString("name","Название"));
          org_subfields.add(new MetaUnitString("phone","Телефон"));
          org_subfields.add(new MetaUnitString("adress","Адрес"));
          organization.setSub_meta_units(org_subfields);
          
          ArrayList<MetaUnitEntityItem> orgs = new ArrayList<MetaUnitEntityItem>();
          HashMap<String,String> org1m,org2m;
          org1m = new HashMap<String, String>();
          org2m = new HashMap<String, String>();
          org1m.put("name", "КМИС");
          org1m.put("phone", "0445632234");
          
          org2m.put("name", "Альянс");
          org2m.put("phone", "04412121212");
          
          MetaUnitEntityItem org1,org2;
          org1 = new MetaUnitEntityItem("КМИС");
          org1.setMapped_values(org1m);
          
          ArrayList<MetaUnitEntityItem> subitems = new ArrayList<MetaUnitEntityItem>();
          subitems.add(new MetaUnitEntityItem("подразделение Киева"));
          org1.setSubitems(subitems);
          
          org2 = new MetaUnitEntityItem("Альянс");
          org2.setMapped_values(org2m);
          orgs.add(org1);
          orgs.add(org2);
          
          organization.setItems(orgs);
          
          MetaUnitMultivaluedStructure orgs_in_research = new MetaUnitMultivaluedStructure("orgs", "Организации");
          
          MetaUnitMultivaluedStructure orgs_order = new MetaUnitMultivaluedStructure("org_order", "Организация-заказчик");
          MetaUnitMultivaluedStructure orgs_impl = new MetaUnitMultivaluedStructure("org_impl", "Организация-исполнитель");
          ArrayList<MetaUnit> orgs_order_sub = new ArrayList<MetaUnit>();
          orgs_order_sub.add(organization);
          ArrayList<MetaUnit> orgs_impl_sub = new ArrayList<MetaUnit>();
          orgs_impl_sub.add(organization);
          orgs_order.setSub_meta_units(orgs_order_sub);
          orgs_impl.setSub_meta_units(orgs_impl_sub);
          
          
          
          ArrayList<MetaUnit> orgs_variants = new ArrayList<MetaUnit>();
          orgs_variants.add(orgs_order);
          orgs_variants.add(orgs_impl);
          orgs_in_research.setSub_meta_units(orgs_variants);
          
          arr.add(orgs_in_research);
          
          
          //MetaUnitMultivalued<SocioResearch> researchers = new MetaUnitMultivalued<SocioResearch>("Исследователи, связанные с проектом");          
          MetaUnitMultivalued researchers = new MetaUnitMultivaluedEntity("researchers","Исследователи, связанные с проектом",1);          
          ArrayList<MetaUnit> arr_r = new ArrayList<MetaUnit>();
          researchers.setSub_meta_units(arr_r);
          
          root.setSub_meta_units(arr);
//          em.persist(root);
          db.setRoot(root);
          em.persist(db);
          
          int b = 2;
      }
    private void createDefaultDatabankVarStructure()
      {
          //DatabankStructure<SocioResearch> db = new DatabankStructure<SocioResearch>("socioresearch");
          //MetaUnitMultivalued<SocioResearch> root = new MetaUnitMultivalued<SocioResearch>("Socioresearch Metadata Structure");
          
          DatabankStructure db = new DatabankStructure("sociovar");
         
          MetaUnitMultivaluedEntity root = new MetaUnitMultivaluedEntity("sociovar","Var Metadata Structure",0);
          ArrayList<MetaUnit> arr = new ArrayList<MetaUnit>();
          
          arr.add(new MetaUnitString("name","Текст вопроса"));
          //arr.add(new MetaUnitString("code","Код переменной"));
          
          //arr.add(new MetaUnitString("alt_codes","Коды альтернатив"));
          arr.add(new MetaUnitString("alt_values","Тексты альтернатив"));
          
//          MetaUnitMultivaluedStructure alternatives = new MetaUnitMultivaluedStructure("alt","Даты исследования");
//          ArrayList<MetaUnit> arr_d = new ArrayList<MetaUnit>();
//          arr_d.add(new MetaUnitMultivaluedEntity("code","Код альтернативы",0));
//          arr_d.add(new MetaUnitMultivaluedEntity("value","Текст альтернативы",0));
//          alternatives.setSub_meta_units(arr_d);
//          arr.add(alternatives);
          arr.add(new MetaUnitMultivaluedEntity("concept","Концепты",1));
         
          
          root.setSub_meta_units(arr);
//          em.persist(root);
          db.setRoot(root);
          em.persist(db);
          
          int b = 2;
      }
        private void createDefaultDatabankLawStructure()
      {
          //DatabankStructure<SocioResearch> db = new DatabankStructure<SocioResearch>("socioresearch");
          //MetaUnitMultivalued<SocioResearch> root = new MetaUnitMultivalued<SocioResearch>("Socioresearch Metadata Structure");
          
          DatabankStructure db = new DatabankStructure("law");
         
          MetaUnitMultivaluedEntity root = new MetaUnitMultivaluedEntity("law","Law Metadata Structure",0);
          ArrayList<MetaUnit> arr = new ArrayList<MetaUnit>();
          
          arr.add(new MetaUnitString("name","Название закона"));
          arr.add(new MetaUnitString("number","Номер закона"));
          arr.add(new MetaUnitString("contents","Краткое содержание"));
       
          arr.add(new MetaUnitDate("date","Дата принятия"));
          arr.add(new MetaUnitDate("date_decline","Дата отмены"));
          
          MetaUnitMultivaluedEntity auth = new MetaUnitMultivaluedEntity("authors","Авторы", 1);
          arr.add(auth);
          
          MetaUnitMultivaluedEntity catalog = new MetaUnitMultivaluedEntity("catalog","Каталогизация", 0);
          arr.add(catalog);
          
          
          
//          MetaUnitMultivaluedStructure alternatives = new MetaUnitMultivaluedStructure("alt","Даты исследования");
//          ArrayList<MetaUnit> arr_d = new ArrayList<MetaUnit>();
//          arr_d.add(new MetaUnitMultivaluedEntity("code","Код альтернативы",0));
//          arr_d.add(new MetaUnitMultivaluedEntity("value","Текст альтернативы",0));
//          alternatives.setSub_meta_units(arr_d);
//          arr.add(alternatives);
          root.setSub_meta_units(arr);
//          em.persist(root);
          db.setRoot(root);
          em.persist(db);
          
          int b = 2;
      }
        
        private void createDefaultDatabankPubStructure()
      {
          //DatabankStructure<SocioResearch> db = new DatabankStructure<SocioResearch>("socioresearch");
          //MetaUnitMultivalued<SocioResearch> root = new MetaUnitMultivalued<SocioResearch>("Socioresearch Metadata Structure");
          
          DatabankStructure db = new DatabankStructure("publication");
         
          MetaUnitMultivaluedEntity root = new MetaUnitMultivaluedEntity("publication","Publication Metadata Structure",0);
          ArrayList<MetaUnit> arr = new ArrayList<MetaUnit>();
          
          arr.add(new MetaUnitString("name","Название публикации"));
          arr.add(new MetaUnitString("subheading","Доп. информация, источник"));
          arr.add(new MetaUnitString("contents","Содержание"));
       
          arr.add(new MetaUnitDate("date","Дата публикации"));
          
          MetaUnitMultivaluedEntity auth = new MetaUnitMultivaluedEntity("authors","Авторы", 1);
          arr.add(auth);
          
          MetaUnitMultivaluedEntity catalog = new MetaUnitMultivaluedEntity(PUBLICATION_TOPIC,"Рубрика", 0);
          arr.add(catalog);
          
          
          
//          MetaUnitMultivaluedStructure alternatives = new MetaUnitMultivaluedStructure("alt","Даты исследования");
//          ArrayList<MetaUnit> arr_d = new ArrayList<MetaUnit>();
//          arr_d.add(new MetaUnitMultivaluedEntity("code","Код альтернативы",0));
//          arr_d.add(new MetaUnitMultivaluedEntity("value","Текст альтернативы",0));
//          alternatives.setSub_meta_units(arr_d);
//          arr.add(alternatives);
          root.setSub_meta_units(arr);
//          em.persist(root);
          db.setRoot(root);
          em.persist(db);
          
          int b = 2;
          
      }

      private void createDefaultDatabankJuryStructure()
      {
          //DatabankStructure<SocioResearch> db = new DatabankStructure<SocioResearch>("socioresearch");
          //MetaUnitMultivalued<SocioResearch> root = new MetaUnitMultivalued<SocioResearch>("Socioresearch Metadata Structure");
          
          DatabankStructure db = new DatabankStructure("consultation");
         
          MetaUnitMultivaluedEntity root = new MetaUnitMultivaluedEntity("consultation","consultation Metadata Structure",0);
          ArrayList<MetaUnit> arr = new ArrayList<MetaUnit>();
          
          arr.add(new MetaUnitString("question","Вопрос"));
          arr.add(new MetaUnitString("asker","Имя задавшего вопрос пользователя"));
          arr.add(new MetaUnitString("answer","Ответ специалиста"));
          arr.add(new MetaUnitDate("date_ask","Дата вопроса"));
          arr.add(new MetaUnitDate("date_ans","Дата ответа"));
         
          MetaUnitMultivaluedEntity catalog = new MetaUnitMultivaluedEntity(CONSULTATION_TOPIC,"Рубрика", 0);
          arr.add(catalog);
          
          
          
//          MetaUnitMultivaluedStructure alternatives = new MetaUnitMultivaluedStructure("alt","Даты исследования");
//          ArrayList<MetaUnit> arr_d = new ArrayList<MetaUnit>();
//          arr_d.add(new MetaUnitMultivaluedEntity("code","Код альтернативы",0));
//          arr_d.add(new MetaUnitMultivaluedEntity("value","Текст альтернативы",0));
//          alternatives.setSub_meta_units(arr_d);
//          arr.add(alternatives);
          root.setSub_meta_units(arr);
//          em.persist(root);
          db.setRoot(root);
          em.persist(db);
          
          int b = 2;
      }
       private void createDefaultStartPage()
      {
          //DatabankStructure<SocioResearch> db = new DatabankStructure<SocioResearch>("socioresearch");
          //MetaUnitMultivalued<SocioResearch> root = new MetaUnitMultivalued<SocioResearch>("Socioresearch Metadata Structure");
          
          DatabankStartPage p = new DatabankStartPage();
          p.setPubs_last_show(new Long(5));
          em.persist(p);
      }
    @Override
    public UserHistoryDTO updateAccountResearchState(UserHistoryDTO dto,long acc_id) {
        UserAccount account;
        UserHistoryDTO returnBack = dto;
        account = em.find(UserAccount.class,acc_id);
        if (account != null)
        {
                if(dto.getCurrent_research()!=null && dto.getCurrent_research().getResearh()!= null)
                {
                  account.updateAccountResearchState(em,dto);
                  returnBack = UserAccount.toHistoryDTO(account, dto.getCurrent_research().getResearh().getID(), em);
        }       }
               
        return returnBack;
    }

    

    @Override
    public UserAccountDTO getDefaultUser() {
            return UserAccount.toDTO(new UserAccount(em).getDefaultUser());
    }

    @Override
    public StartupBundleDTO getStartupContent() {
        
        DatabankStartPage d = DatabankStartPage.getStartPageSingleton(em);
        ArrayList<PublicationDTO_Light> pubs = pubbean.getPublications(d.getPubs_last_show().intValue(), 0);
        StartupBundleDTO dto = d.toDTO();
        dto.setIndex_last(pubs);
        return dto;
    }
    
    private void script()
    {
          DatabankStructure db = em.find(DatabankStructure.class,"socioresearch");
         
          Collection<MetaUnit> items = db.getRoot().getSub_meta_units();
          
          ArrayList<MetaUnitEntityItem> selection_randomity = new ArrayList<MetaUnitEntityItem>();
          
          MetaUnitMultivaluedEntity randomity = new MetaUnitMultivaluedEntity("concepts","Концепты", 1);
          
          selection_randomity.add(new MetaUnitEntityItem("образование"));
          
          randomity.setItems(selection_randomity);
          items.add(randomity);
          
          em.persist(db);
    }

    @Override
    public UserHistoryDTO getUserResearchHistory(long user_id,long research_id) {
        //throw new UnsupportedOperationException("Not supported yet.");
         return UserAccount.toHistoryDTO(new UserAccount(em).getUserAccountUnsafe(user_id),research_id,em);
    }
    @Override
    public List<SocioResearchDTO_Light> getUserMyResearches(long user_id) {
        List<SocioResearchDTO_Light> lst = new ArrayList<SocioResearchDTO_Light>();
         UserAccount acc = new UserAccount(em).getUserAccountUnsafe(user_id);
         if(acc!=null)
         {
             List<UserMassiveLocalSetting> list = acc.getHistory().getFavouriteMassives();
            for(UserMassiveLocalSetting setting:list)
            {
                long id = setting.getResearch_id();
                    SocioResearch res = em.find(SocioResearch.class, id);
                    if(res!=null)
                    {
                            SocioResearchDTO dto =  res.toDTO();
                        //SocioResearchDTO dto = userbean.getResearch(id);
                        SocioResearchDTO_Light ll = new SocioResearchDTO_Light(id, dto.getName());
                        lst.add(ll);     
                    }
               
            }
         }
       
        return lst;  
    }

    @Override
    public void addToSelectedResearches(UserResearchSettingDTO dto,long user_id) {
          UserAccount acc = new UserAccount(em).getUserAccountUnsafe(user_id);
         if(acc!=null)
         {
            RegisteredUserHistory hist = acc.getHistory();
            if(hist!= null)
            {
                UserMassiveLocalSetting setting = new UserMassiveLocalSetting(dto);
                hist.addToFavouriteMassives(setting);
                em.persist(hist);
            }
         }
        
    }

    @Override
    public List<UserAnalysisSaveDTO> getUserAllAnalisys(long user_id) {
         List<UserAnalysisSaveDTO> lst = new ArrayList<UserAnalysisSaveDTO>();
         UserAccount acc = new UserAccount(em).getUserAccountUnsafe(user_id);
         if(acc!=null)
         {
                RegisteredUserAnalysisRoot anal_root = acc.getAnalysis();
                List<UserMassiveLocalAnalisys> list = anal_root.getLocal_research_analisys();
                for(UserMassiveLocalAnalisys anal:list)
                {
                    lst.add(anal.toDTO(em));
                }
         }
      
        return lst;
    }

    @Override
    public List<UserAnalysisSaveDTO> getUserResearchAnalisys(Long research_id,long user_id) {
        List<UserAnalysisSaveDTO> lst = new ArrayList<UserAnalysisSaveDTO>();
        new UserAccount(em).getUserAccountUnsafe(user_id);
        return lst;
    }

    @Override
    public void saveResearchAnalisys(UserAnalysisSaveDTO dto,long user_id) {
        UserMassiveLocalAnalisys anal = new UserMassiveLocalAnalisys(dto,em);
         UserAccount acc = new UserAccount(em).getUserAccountUnsafe(user_id);
         if(acc!=null)
         {
            RegisteredUserAnalysisRoot anal_root = acc.getAnalysis();
            anal_root.getLocal_research_analisys().add(anal);
            em.persist(anal_root);
         }
    }

    @Override
    public UserResearchSettingDTO getUserResearchSetting(Long id, long user_id) {
        UserAccount acc = new UserAccount(em).getUserAccountUnsafe(user_id);
       if (acc!=null)
       {
        List<UserMassiveLocalSetting> list = acc.getHistory().getLocal_research_settings();
      
        for(UserMassiveLocalSetting sett:list)
        {
            if(sett.getResearch_id().equals(id)){
                return sett.toDTO(em);
            }
        }
       }
       
       return null;
       // throw new UnsupportedOperationException("Not supported yet.");
    }

  
    
}
