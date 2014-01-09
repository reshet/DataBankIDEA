/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.service;

import argo.format.CompactJsonFormatter;
import argo.format.JsonFormatter;
import static argo.format.JsonNumberUtils.asBigDecimal;
import static argo.jdom.JsonNodeBuilders.*;
import argo.jdom.*;
import argo.saj.InvalidSyntaxException;
import com.mplatforma.amr.service.remote.AdminSocioResearchBeanRemote;
import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import com.mplatforma.amr.service.remote.UserSocioResearchBeanRemote;
import com.mplatforma.amr.entity.*;
import com.mresearch.databank.jobs.DeleteIndexiesJob;
import com.mresearch.databank.jobs.IndexResearchJob;
import com.mresearch.databank.jobs.IndexVarJobFast;
import com.mresearch.databank.jobs.ParseSpssJob;
import com.mresearch.databank.shared.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.jms.Queue;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author reshet
 */
//@WebService
@Stateless(mappedName="AdminSocioResearchRemoteBean",name="AdminSocioResearchRemoteBean")
public class AdminSocioResearchSessionBean implements AdminSocioResearchBeanRemote{

//    static
//    {
//         Locale locale = Locale.getDefault();
//           System.out.println("Before setting, Locale is = " + locale);
//         locale = new Locale("ru","RU");
//        //  // Setting default locale  
//        // // locale = Locale.ITALY;
//         Locale.setDefault(locale);
//          System.out.println("After setting, Locale is = " + locale);
//    }
    
    private static final JsonFormatter JSON_FORMATTER = new CompactJsonFormatter();
    private static final JdomParser JDOM_PARSER = new JdomParser();
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private RxStorageBeanRemote store;
    
    @EJB
    private UserSocioResearchBeanRemote user_bean;
   
    public UserHistoryDTO updateAccountResearchState(UserHistoryDTO dto) {
        UserAccount account;
        UserHistoryDTO returnBack = dto;
        account = em.find(UserAccount.class,dto.getId());
        if (account != null)
        {
                account.updateAccountResearchState(em,dto);
                returnBack = UserAccount.toHistoryDTO(account,dto.getCurrent_research().getResearh().getID(),em);
        }
        return returnBack;
    }

     

    @Override
    public Boolean deleteResearch(long id) {
        try
        {
            SocioResearch r = em.find(SocioResearch.class, id);
            ArrayList<Long> r_id = new ArrayList<Long>();
            r_id.add(r.getID());
            launchDeleteIndexing(r_id, "research");
            launchDeleteIndexing(Var.getResearchVarsIDs(em, id), "sociovar");
            em.flush();
            //DatabankStartPage sp = DatabankStartPage.getStartPageSingleton(em);
            //if(sp.getRes().contains(r))sp.getRes().remove(r);
            //em.persist(sp);
            //em.flush();
            em.remove(r);
            em.flush();
            Var.deleteResearchVars(em,id);
            return true;
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
       // throw new UnsupportedOperationException("Not supported yet.");
    }

     private SocioResearch addResearch(SocioResearchDTO researchDTO) {
        SocioResearch research = null;
        try {
          research = new SocioResearch(researchDTO,em);
          em.persist(research);
          research.updateEntityID(research.getID(),research.getId_search_repres(), em);
         // currentUser.getFriends().add(friend);
        } finally {
        }
        return research;
      }
    @Override
    public SocioResearchDTO updateResearch(SocioResearchDTO rDTO) {
       // throw new UnsupportedOperationException("Not supported yet.");
            if (rDTO.getId() == 0){ // create new
              SocioResearch newResearch = addResearch(rDTO);
              return newResearch.toDTO();
            }

            SocioResearch research = null;
            try {
             
                
              research = em.find(SocioResearch.class, rDTO.getId());
              research.updateFromDTO(rDTO,em);
              em.persist(research);
//              addSSE("SocioResearch","gengeath", rDTO.getGen_geathering());
//              addSSE("SocioResearch","method", rDTO.getMethod());
//              ArrayList<String> concepts = rDTO.getConcepts();
//              if(concepts!=null && concepts.size()>0)
//              {
//                  for(String concept:concepts)
//                  {
//                          addSSE("SocioResearch","concept", concept);
//                  }  
//              }
             // ArrayList<String> researchers = rDTO.getResearchers();
//              if(researchers!=null && researchers.size()>0)
//              {
//                  for(String researcher:researchers)
//                  {
//                          addSSE("SocioResearch","researcher", researcher);
//                  }
//              }
             // addSSE("SocioResearch","org_impl", rDTO.getOrg_impl_name());
              //addSSE("SocioResearch","org_order", rDTO.getOrg_order_name());
             // ArrayList<String> pubs = rDTO.getPublications();
//              if(pubs!=null && pubs.size()>0)
//              {
//                  for(String pub:pubs)
//                  {
//                          addSSE("SocioResearch","publication", pub);
//                  }
//              }
             // addSSE("SocioResearch","selection_complexity", rDTO.getSel_complexity());
             // addSSE("SocioResearch","selection_randomity", rDTO.getSel_randomity());
              
              launchIndexing(rDTO);
              
              int bb = 2;
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
            }
            
            return rDTO;

    }

    @Override
    public void updateVar(VarDTO_Detailed rDTO) {
       // throw new UnsupportedOperationException("Not supported yet.");
            

            Var var = null;
            try {
             
                
              var = em.find(Var.class, rDTO.getId());
              var.updateFromDTO(rDTO,em);
             
              em.persist(var);
              launchIndexingVar(rDTO);
              
              int bb = 2;
              
              //findVarsLikeThis(rDTO.getId(), new ComparativeSearchParamsDTO());
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
            }
    }
    
    @Resource(mappedName="jms/alliance/myQCF")
    private  QueueConnectionFactory connectionFactory;

    @Resource(mappedName="jms/alliance/spss_parse")
    private  Queue queue;
    
//    @Resource(mappedName="jms/ES_index")
//    private  Queue index_queue;
    
     private QueueConnection connection;
    private QueueSession session;
    QueueSender q_sender;
    @PostConstruct
    private void init()
    {
     try {
             connection = connectionFactory.createQueueConnection();
             session = connection.createQueueSession(false, 0);
             q_sender = session.createSender(queue);

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
    @PreDestroy
    private void release()
    {
       try {
            q_sender.close();
            connection.close();
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
     private void launchIndexingVar(VarDTO_Detailed dto)
    {
         try {
            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to index SocioResearch var");
            // here we create NewsEntity, that will be sent in JMS message
           // ParseSpssJob job = new ParseSpssJob(blobkey, length);
            IndexVarJobFast job = new IndexVarJobFast(dto);
            message.setObject(job);    
           // message.setJMSDestination(queue);
            q_sender.send(message);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
     
   
    
    private void launchIndexing(SocioResearchDTO dto)
    {
         try {
            
//            QueueConnection connection = connectionFactory.createQueueConnection();
//            QueueSession session = connection.createQueueSession(false, 0);
//            QueueSender q_sender = session.createSender(queue);

            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to index SocioResearch");
            // here we create NewsEntity, that will be sent in JMS message
           // ParseSpssJob job = new ParseSpssJob(blobkey, length);
            IndexResearchJob job = new IndexResearchJob(dto.getId());
            message.setObject(job);    
           // message.setJMSDestination(queue);
            q_sender.send(message);
//            q_sender.close();
//            connection.close();
            //response.sendRedirect("ListNews");

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
    
      private void launchDeleteIndexing(ArrayList<Long> ids,String type)
         {
         try {
            
//            QueueConnection connection = connectionFactory.createQueueConnection();
//            QueueSession session = connection.createQueueSession(false, 0);
//            QueueSender q_sender = session.createSender(queue);

            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to delete index "+type+" "+ids.size()+" elements");
            // here we create NewsEntity, that will be sent in JMS message
           // ParseSpssJob job = new ParseSpssJob(blobkey, length);
            if(ids.size()>0){
                DeleteIndexiesJob job = new DeleteIndexiesJob(ids, type);
                message.setObject(job);
                message.setJMSDestination(queue);
                q_sender.send(message);
            }

//            q_sender.close();
//            connection.close();
            //response.sendRedirect("ListNews");


        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public long parseSPSS(long blobkey, long length) {
        
        try {
            
//            QueueConnection connection = connectionFactory.createQueueConnection();
//            QueueSession session = connection.createQueueSession(false, 0);
//            QueueSender q_sender = session.createSender(queue);

            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to parse SPSS file");
            // here we create NewsEntity, that will be sent in JMS message
            ParseSpssJob job = new ParseSpssJob(blobkey, length);
          
            message.setObject(job);   
          //  message.setJMSDestination(queue);
            q_sender.send(message);
//            q_sender.close();
//            connection.close();
//            //response.sendRedirect("ListNews");

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
            
	return 0;
    }

   
    @Override
    public SocioResearchDTO updateResearchGrouped(SocioResearchDTO rDTO) {
        if (rDTO.getId() == 0){ // create new
            return rDTO;
        }

        SocioResearch research = null;
        try {
          research = em.find(SocioResearch.class, rDTO.getId());
          research.updateFromDTOGrouped(rDTO,em);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            
        }
        return rDTO;
    }

    @Override
    public VarDTO_Detailed generalizeVar(long var_id, ArrayList<Long> gen_var_ids,UserAccountDTO user) {
            VarDTO_Detailed detailed = null;
	    Var dsVar, detached;

	    try {
	      dsVar = em.find(Var.class, var_id);
	      dsVar.setGeneralized_var_ids(gen_var_ids);
	     // UserAccountDTO watching_user = (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	      detailed = dsVar.toDTO_Detailed(user,null,em);
	    } finally {
	    }
	    
	return detailed;
    }

    @Override
    public long addOrgImpl(OrgDTO dto) {
          Organization org = null;
	  long org_id = 0;
	    try {
	      org = new Organization(dto);
	      em.persist(org);
	      org_id = org.getId();
	    } finally {
	    }
	 return org_id;
    }

    @Override
    public Boolean addFileToAccessor(long id_research, long id_file, String desc, String category) {
	    SocioResearch dsResearch, detached;
            try {
	      dsResearch = em.find(SocioResearch.class, id_research);
	      ResearchFilesDTO dto = dsResearch.toFilesDTO(em);
	      dto.addFile(category, desc, id_file);
	      dsResearch.updateFileAccessor(em,dto);
	    } finally {
	    }
	 return true;
    }

    @Override
    public Boolean deleteFileFromAccessor(long id_research, long id_file) {
	    SocioResearch dsResearch, detached;
	    try {
	      dsResearch = em.find(SocioResearch.class, id_research);
	      ResearchFilesDTO dto = dsResearch.toFilesDTO(em);
	      dto.deleteFile(id_file);
	      dsResearch.updateFileAccessor(em,dto);
	      return store.deleteFile(id_file);
	    } finally {
                return false;
	    }
    }

    @Override
    public Boolean addSSE(String clas, String kind, String value) {
      /* SingleStringEntity entity = null;
        try {
          // for this version of the app, just get hardwired 'default' user
          //UserAccount currentUser = UserAccount.getDefaultUser(); // detached object
          //currentUser = pm.makePersistent(currentUser); // attach
            if(value!=null && !value.equals(""))
            {
                List<SingleStringEntity> res = SingleStringEntity.getMatchingFull(em, clas, kind, value);
                if(res.isEmpty())
                {
                   entity = new SingleStringEntity();
                   entity.setClas(clas);
                   entity.setKind(kind);
                   entity.setContents(value);
                   em.persist(entity);
                }
            }
            return true;
        } finally {
            return false;
        }*/
        return true;
    }

    @Override
    public Boolean updateFileAccessor(long research_id, ResearchFilesDTO dto) {
        SocioResearch research = null;
        ResearchFilesAccessor accessor;
        try {
          research = em.find(SocioResearch.class, research_id);
          accessor = em.find(ResearchFilesAccessor.class,research.getFile_accessor_id());
          accessor.updateFromDTO(dto);
          return true;
        } catch (Exception e) {
              e.printStackTrace();
        } finally {
            return false;
        }
    }

    
    
    private MetaUnitMultivalued updateMultivaluedUnitFromDTO(MetaUnitDTO dto)
    {
        MetaUnitMultivalued struct = null;
        if(dto instanceof MetaUnitMultivaluedStructureDTO)
        {
            struct = em.find(MetaUnitMultivaluedStructure.class, dto.getId());
            if(struct == null)
            {
                struct = new MetaUnitMultivaluedStructure();
                em.persist(struct);
            }
        }
        if(dto instanceof MetaUnitMultivaluedEntityDTO)
        {
            struct = em.find(MetaUnitMultivaluedEntity.class, dto.getId());
             if(struct == null)
            {
                struct = new MetaUnitMultivaluedEntity();
                ((MetaUnitMultivaluedEntity)struct).setIsMultiSelected(((MetaUnitMultivaluedEntityDTO)dto).isIsMultiselected()?1:0);
                em.persist(struct);
            }
        }

            MetaUnitMultivaluedDTO m_dto = (MetaUnitMultivaluedDTO)dto;
            
            struct.setIsCatalogizable(m_dto.isIsCatalogizable()?1:0);
            struct.setIsSplittingEnabled(m_dto.isIsSplittingEnabled()?1:0);
            struct.setTagged_entities(m_dto.getTagged_entities());
            struct.setUnique_name(dto.getUnique_name());
            struct.setDescription(dto.getDesc());
            int index = 0;
            if(m_dto.getSub_meta_units()!=null)
            for(int j = 0;j< m_dto.getSub_meta_units().size();j++)
            {
                MetaUnitDTO dt = m_dto.getSub_meta_units().get(j);
                if(struct.sub_meta_units_contains(dt.getId()))
                {
                    MetaUnit m_unit = struct.sub_meta_units_get(dt.getId());
                    int index_old = struct.sub_meta_units_get_order(dt.getId());
                    if(index != index_old)
                    {
                        ArrayList<MetaUnit> u = new ArrayList<MetaUnit>();
                        for(MetaUnit un:struct.getSub_meta_units())
                        {
                            u.add(un);
                        }
                        MetaUnit m_unit_sw = u.get(index);
                        u.set(index, m_unit);
                        u.set(index_old, m_unit_sw);
                        struct.setSub_meta_units(u);
                    }
                    
                    m_unit.updateFromDTO(dt);
                    if(m_unit instanceof MetaUnitMultivalued)
                    {
                        updateMultivaluedUnitFromDTO(dt);
                    }
       
                }else
                {
                    MetaUnit new_unit = null;
                    
                    if(dt instanceof MetaUnitDateDTO){new_unit = new MetaUnitDate();new_unit.updateFromDTO(dt);}
                    if(dt instanceof MetaUnitDoubleDTO){new_unit = new MetaUnitDouble();new_unit.updateFromDTO(dt);}
                    if(dt instanceof MetaUnitIntegerDTO){new_unit = new MetaUnitInteger();new_unit.updateFromDTO(dt);}
                    if(dt instanceof MetaUnitStringDTO){new_unit = new MetaUnitString();new_unit.updateFromDTO(dt);}
                    if(dt instanceof MetaUnitFileDTO){new_unit = new MetaUnitFile();new_unit.updateFromDTO(dt);}
                    if(dt instanceof MetaUnitMultivaluedStructureDTO)
                        {new_unit = new MetaUnitMultivaluedStructure();new_unit.updateFromDTO(dt);updateMultivaluedUnitFromDTO(dt);}
                    if(dt instanceof MetaUnitMultivaluedEntityDTO)
                        {new_unit = new MetaUnitMultivaluedEntity();new_unit.updateFromDTO(dt);updateMultivaluedUnitFromDTO(dt);}
                    
                    struct.getSub_meta_units().add(new_unit);
                }
                index++;
            }
            em.merge(struct);
            return struct;
           // MetaUnitMultivaluedStructure str = em.find(MetaUnitMultivaluedStructure.class, dto.getId());
    }
    
    @Override
    public MetaUnitDTO addMetaUnit(long parent_id, MetaUnitDTO dto) {
        MetaUnitMultivalued unit;
        MetaUnit new_unit = null;
        if(dto instanceof MetaUnitDateDTO){new_unit = new MetaUnitDate();new_unit.updateFromDTO(dto);}
        if(dto instanceof MetaUnitDoubleDTO){new_unit = new MetaUnitDouble();new_unit.updateFromDTO(dto);}
        if(dto instanceof MetaUnitIntegerDTO){new_unit = new MetaUnitInteger();new_unit.updateFromDTO(dto);}
        if(dto instanceof MetaUnitStringDTO){new_unit = new MetaUnitString();new_unit.updateFromDTO(dto);}
        if(dto instanceof MetaUnitFileDTO){new_unit = new MetaUnitFile();new_unit.updateFromDTO(dto);}
        if(dto instanceof MetaUnitMultivaluedStructureDTO){new_unit= updateMultivaluedUnitFromDTO(dto);}
        if(dto instanceof MetaUnitMultivaluedEntityDTO){new_unit= updateMultivaluedUnitFromDTO(dto);}
        
        try
        {
            unit = em.find(MetaUnitMultivalued.class, parent_id);    
            unit.getSub_meta_units().add(new_unit);
            em.persist(unit);
            em.persist(new_unit);
            em.flush();
            return new_unit.toDTO();
        }
        finally
        {
        }
    }



    @Override
    public void updateMetaUnitStructure(MetaUnitDTO dto) {
        MetaUnit unit;
        try
        {
            unit = em.find(MetaUnit.class, dto.getId());    
            unit.updateFromDTO(dto);
            if(unit instanceof MetaUnitMultivalued) updateMultivaluedUnitFromDTO(dto);
            em.persist(unit);
            //return true;
        }
        finally
        {
        }
    }

    @Override
    public void addEntityItem(Long entity_id, String value, HashMap<String, String> filling) {
        MetaUnitMultivaluedEntity entity = em.find(MetaUnitMultivaluedEntity.class, entity_id);
        MetaUnitEntityItem item = new MetaUnitEntityItem(value);
        item.setMapped_values(filling);
        entity.getItems().add(item);
        em.persist(entity);
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteMetaUnit(Long id, Long unit_parent_id) {
        MetaUnit unit;
        MetaUnitMultivalued punit;
        try
        {
            unit = em.find(MetaUnit.class, id); 
            punit = em.find(MetaUnitMultivalued.class, unit_parent_id);
            if(punit!=null)punit.getSub_meta_units().remove(unit);
            em.persist(punit);
            em.remove(unit);
        }
        finally
        {
        }
    }

    @Override
    public void editEntityItem(Long id, String value, HashMap<String, String> filling) {
        MetaUnitEntityItem item = em.find(MetaUnitEntityItem.class,id);
        item.setValue(value);
        item.setMapped_values(filling);
    }

    
    @Override
    public void deleteEntityItem(Long id, Long entity_id) {
        MetaUnitEntityItem item = em.find(MetaUnitEntityItem.class,id);
        MetaUnitMultivaluedEntity ent = em.find(MetaUnitMultivaluedEntity.class, entity_id);
        ent.getItems().remove(item);
        em.remove(item);
        em.persist(ent);
    }

    
    
    @Override
    public void addSubEntityItem(Long parent_id, String value, HashMap<String, String> filling) {
        MetaUnitEntityItem p_item = em.find(MetaUnitEntityItem.class, parent_id);
        MetaUnitEntityItem item = new MetaUnitEntityItem(value);
        item.setMapped_values(filling);
        p_item.getSubitems().add(item);
        em.persist(p_item);
    }

    @Override
    public void updateMetaUnitEntityItemLinks(MetaUnitEntityItemDTO old,MetaUnitEntityItemDTO nev){
        
        MetaUnitEntityItem item_old = em.find(MetaUnitEntityItem.class,old.getId());
        if(item_old != null)
         {
            item_old.setTagged_entities_identifiers(old.getTagged_entities_identifiers());
            item_old.setTagged_entities_ids(old.getTagged_entities_ids());
            em.persist(item_old);

            MetaUnitEntityItem item_new = em.find(MetaUnitEntityItem.class,nev.getId());
            item_new.setTagged_entities_identifiers(nev.getTagged_entities_identifiers());
            item_new.setTagged_entities_ids(nev.getTagged_entities_ids());
            em.persist(item_new);
         }
    }

    @Override
    public void updateMetaUnitEntityItemLinks(MetaUnitEntityItemDTO dto) {
         MetaUnitEntityItem item_old = em.find(MetaUnitEntityItem.class,dto.getId());
         if(item_old != null)
         {
            item_old.setTagged_entities_identifiers(dto.getTagged_entities_identifiers());
            item_old.setTagged_entities_ids(dto.getTagged_entities_ids());
            em.persist(item_old);
         }
        
    }

    @Override
    public void updateMetaUnitEntityItemLinks(Long item_id,ArrayList<Long> tagged_ids, String identifier) {
        MetaUnitEntityItem item_old = em.find(MetaUnitEntityItem.class,item_id);
         if(item_old != null)
         {
            ArrayList<String> new_tagged_idents = new ArrayList<String>();
            ArrayList<Long> new_tagged_ids = new ArrayList<Long>();
            int i = 0;
            if(item_old.getTagged_entities_identifiers() !=null)
            for(String ident:item_old.getTagged_entities_identifiers())
            {
                if(!ident.equals(identifier))
                {
                    new_tagged_idents.add(ident);
                    new_tagged_ids.add(item_old.getTagged_entities_ids().get(i));
                }
                i++;
            }
            for(Long id:tagged_ids)
            {
                 new_tagged_idents.add(identifier);
                 new_tagged_ids.add(id);
            }
            item_old.setTagged_entities_identifiers(new_tagged_idents);
            item_old.setTagged_entities_ids(new_tagged_ids);
            em.persist(item_old);
         }
    }

    @Override
    public void setStartupContent(StartupBundleDTO dto) {
        DatabankStartPage d = DatabankStartPage.getStartPageSingleton(em);
        d.updateFromDTO(em, dto);
        em.persist(d);
    }
    
    
     @Override
    public ArrayList<VarDTO_Detailed> findVarsLikeThis(Long var_id, ComparativeSearchParamsDTO params) {
         Var v = em.find(Var.class, var_id);
         v.setEM(em);
         VarDTO_Detailed dto =  v.toDTO_Detailed(null,null,em); 
         ArrayList<Long> varids = doSearchLikewise(dto, params);
         ArrayList<VarDTO_Detailed> lst = new ArrayList<VarDTO_Detailed>();
         if(varids.size()>0) lst = user_bean.getVarDTOsAsOrdered(varids);
         return lst;
     }

    @Override
    public long createEmptyResearch(String name) {
        SocioResearch research = null;
        Long research_id = null;
        try {
            research = new SocioResearch(em);
            research.setName(name);
            //ResearchFilesDTO dto = new ResearchFilesDTO();
            //research.updateFileAccessor(em, dto);
            em.persist(research);
            research_id = research.getID();
            // currentUser.getFriends().add(friend);
        } finally {
        }
        return research_id;
    }

    private ArrayList<String> cropSearchString(String str, int granularity, boolean overlap)
     {
        ArrayList<String> strs = new ArrayList<String>();
        
        String [] arr = str.split(" "); 
        String prev = "";
        for(int i = 0; i < arr.length;i+=granularity)
        {
            //arr[i] = arr[i].replaceAll("([^а-яіє’А-Я0-9]+)", "");
            //arr[i] = arr[i].replaceAll("[0-9]", "");
            if(!arr[i].equals(""))
            {
                StringBuilder strb = new StringBuilder();
                if(!prev.equals("")&& i > 0) prev = arr[i-1];
                if(overlap) {
                    strb.append(prev);
                    if((arr.length - i)>1)strb.append(" ");
                }
                for(int j = i; j < i+granularity && j < arr.length;j++)
                {
                   
                    strb.append(arr[j]);
                    if((arr.length - i)>1)strb.append(" ");
                }
                strs.add(strb.toString());
            }
            
        }
         //str.
         return strs;
     }
     
    // private  RussianAnalyzer russian = null;
    // private LuceneMorphology luceneMorph = null;
//     private RussianAnalyzer getRussAnal() throws IOException
//     {
//         if(russian == null)russian = new RussianAnalyzer();
//         return russian;
//     }
//      private  LuceneMorphology getMorph() throws IOException
//     {
//         if(luceneMorph == null)luceneMorph = new RussianLuceneMorphology();
//         return luceneMorph;
//     }
//     private ArrayList<String> morphSearchString(String str)
//     {
//        ArrayList<String> strs = new ArrayList<String>();
//        try {
//            //Use only main clause words.
//           //RussianAnalyzer russian = getRussAnal();
//           //russian.
//               //;
//                //luceneMorph
//                //luceneMorph.
//                if( getMorph().checkString(str)){
//                    List<String> wordBaseForms =  getMorph().getMorphInfo(str);
//                    List<String> wordBaseForms2 =  getMorph().getNormalForms(str);
//                }
//                // TokenStream tokenStream = new MorphlogyFilter(str, luceneMorph);
//     //List<String> wordBaseForms = luceneMorph.getMorphInfo(str);
//         //  russian.
//           //russian
//        } catch (IOException ex) {
//            Logger.getLogger(AdminSocioResearchSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return strs;
//     }
     private void buildTextPhraseVarTextPart(JsonArrayNodeBuilder arr_contains,VarDTO_Detailed origin_var)
     {
          String question_label = origin_var.getLabel();
         //ArrayList<String> sublabels = cropSearchString(question_label, 3);
         ArrayList<String> sublabels = cropSearchString(question_label, 2,true);
        
         for(int i = 0; i < sublabels.size();i++)
        {
            JsonObjectNodeBuilder obj_b = anObjectBuilder()
                .withField("match_phrase_prefix", anObjectBuilder()
                    .withField("sociovar_name", anObjectBuilder()
                        .withField("query", aStringBuilder(sublabels.get(i)))
                     )
               //     .withField("fuziness", aNumberBuilder("0.2"))
                 );
               // obj_b.put("text_phrase", obj_field_name);
                arr_contains.withElement(obj_b);
        }
     }
     private void buildTextPhraseVarAlternativesTextPart(JsonArrayNodeBuilder arr_contains,VarDTO_Detailed origin_var)
     {
          List<String> alt_labels = origin_var.getV_label_values();
         //ArrayList<String> sublabels = cropSearchString(question_label, 3);
          ArrayList<String> sublabels = new ArrayList<String>();
          for(String label:alt_labels)
          {
            ArrayList<String> subs = cropSearchString(label, 2,true);
            for(String sub:subs){sublabels.add(sub);}
          }
         
         for(int i = 0; i < sublabels.size();i++)
        {
            JsonObjectNodeBuilder obj_b = anObjectBuilder()
                .withField("match_phrase_prefix", anObjectBuilder()
                    .withField("sociovar_alt_values", anObjectBuilder()
                        .withField("query", aStringBuilder(sublabels.get(i)))
                     )
               //     .withField("fuziness", aNumberBuilder("0.2"))
                 );
               // obj_b.put("text_phrase", obj_field_name);
                arr_contains.withElement(obj_b);
        }
     }
     private void buildTextVarIdsFilter(JsonArrayNodeBuilder arr_contains,ArrayList<Long> varids)
     {
        JsonArrayNodeBuilder arr_values = anArrayBuilder();
        for (int i = 0; i < varids.size(); i++)
        {
            arr_values.withElement(aStringBuilder(String.valueOf(varids.get(i))));
        }
        JsonObjectNodeBuilder obj_b = anObjectBuilder()
                .withField("ids", anObjectBuilder()
                    //.withField("type", aStringBuilder("sociovar"))
                    .withField("values", arr_values)
                 );
        arr_contains.withElement(obj_b);
     }
     private ArrayList<Long> prefilterVarsOnResearchMetadata(String filt_query){
        ArrayList<Long> ids;
        String [] types = new String[]{"research"};
        String result = user_bean.doIndexSearch(filt_query, types);
        ids = doParseResearchMetaFilterSearchResult(result);
        return ids;
     }
     private ArrayList<Long> doParseResearchMetaFilterSearchResult(String result)
     {
         ArrayList<Long> var_ids = new ArrayList<Long>();
         ArrayList<Long> research_ids = new ArrayList<Long>();


        try {
            JsonRootNode res = JDOM_PARSER.parse(result);
            JsonNode hits = res.getNode("hits");
            List<JsonNode> hiters = hits.getArrayNode("hits");

            //JSONObject res = JSONObject.fromObject(result);
           // JSONObject res = (JSONObject)JSONParser.parse(result);
            //JSONObject hits = (JSONObject)res.get("hits");
            //JSONObject total = hits.get("total");

            BigDecimal totalRoyalties = asBigDecimal(hits.getNumberValue("total"));
            Integer tot = totalRoyalties.intValue();


            if(tot > 0){
                BigDecimal max_sc = asBigDecimal(hits.getNumberValue("max_score"));
                Double max_score = max_sc.doubleValue();

            }

            //Double score_barrier = max_score/variance;

            //JSONArray hits_arr = (JSONArray)hits.getJSONArray("hits");
           // if(hiters.isEmpty())
           // {
                        //display.getCenterPanel().clear();
                        //display.getCenterPanel().add(new HTML("<H2>По вашему запросу ничего не найдено. Попробуйте изменить параметры поиска</H2>"));
                    //return;
           // }

            Set<Long> ids_no_clones = new TreeSet<Long>();
            for (int i = 0; i < hiters.size(); i++)
            {
                JsonNode hit = (JsonNode)hiters.get(i);
                BigDecimal id = asBigDecimal(hit.getNode("_id").getStringValue());
                long resid = id.longValue();

                //BigDecimal sc = asBigDecimal(hit.getNumberValue("_score"));
                //Double score = sc.doubleValue();
                ids_no_clones.add(resid);
            }

            for(Long id:ids_no_clones){
                research_ids.add(id);
            }

        } catch (InvalidSyntaxException ex) {
            Logger.getLogger(AdminSocioResearchSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Long resid:research_ids){
            ArrayList<Long> varids = Var.getResearchVarsIDs(em, resid);
            for(Long varid:varids){
                var_ids.add(varid);
            }
        }

        return var_ids;
     }
     private String constructSearchLikewiseQuery(VarDTO_Detailed origin_var, ComparativeSearchParamsDTO params)
     {
        //here we compose first likewise search query
         // simplest form - search for question


         //for(String st:sublabels)
         //{
         //   morphSearchString(st);
         //}
         //ArrayList<String> sublabels = morphSearchString(question_label);

         //here filter vars by research metadata


         String query = "";



        //JSONObject obj_bool = new JSONObject();
        //JSONObject obj_must = new JSONObject();
        //JSONArray arr_must = new JSONArray();

//        JsonObjectNodeBuilder builder = anObjectBuilder()
//        .withField("name", aStringBuilder("Black Lace"))
//        .withField("sales", aNumberBuilder("110921"))
//        .withField("totalRoyalties", aNumberBuilder("10223.82"))
//        .withField("singles", anArrayBuilder()
//                .withElement(aStringBuilder("Superman"))
//                .withElement(aStringBuilder("Agadoo"))
//        );



        JsonArrayNodeBuilder arr_contains = anArrayBuilder();
        buildTextPhraseVarTextPart(arr_contains, origin_var);
        buildTextPhraseVarAlternativesTextPart(arr_contains, origin_var);

        JsonObjectNodeBuilder obj_bool_contains = null;
         if(params.getResearch_filter()!=null && !params.getResearch_filter().equals("{\"text\":{\"_all\":\"*\"}}")){
            ArrayList<Long> ids = prefilterVarsOnResearchMetadata(params.getResearch_filter());
            JsonArrayNodeBuilder arr_must_ids = anArrayBuilder();
            buildTextVarIdsFilter(arr_must_ids, ids);
             obj_bool_contains = anObjectBuilder()
                     .withField("bool", anObjectBuilder()
                             .withField("must", arr_must_ids)
                             .withField("should", arr_contains)
                     );
        }else{
             obj_bool_contains = anObjectBuilder()
                     .withField("bool", anObjectBuilder()
                             .withField("should", arr_contains)
                     );
        }

        //int index_c=0,index_c2=0,index_c3=0;



        //JSONObject obj_bool_contains = new JSONObject();
        //JsonObjectNodeBuilder obj_contains = anObjectBuilder();



        //obj_contains_too.put("should", arr_contains_too);
        //obj_bool_contains_too.put("bool", obj_contains_too);


        //if(arr_contains.size()>0)arr_must.set(0, obj_bool_contains);
        //if(arr_contains_too.size()>0)arr_must.set(1, obj_bool_contains_too);

        //obj_must.put("must", arr_must);
        //obj_bool.put("bool", obj_must);
//	      String quer = obj_bool.toString();
         JsonRootNode json = obj_bool_contains.build();
         //String str = json.toString();
         query = JSON_FORMATTER.format(json);
         return query;
     }

     private ArrayList<Long> doParseLikewiseSearchResult(VarDTO_Detailed origin_var, ComparativeSearchParamsDTO params,String result,double variance)
     {
         ArrayList<Long> var_ids = new ArrayList<Long>();

        try {
            JsonRootNode res = JDOM_PARSER.parse(result);
            JsonNode hits = res.getNode("hits");
            List<JsonNode> hiters = hits.getArrayNode("hits");

            //JSONObject res = JSONObject.fromObject(result);
           // JSONObject res = (JSONObject)JSONParser.parse(result);
            //JSONObject hits = (JSONObject)res.get("hits");
            //JSONObject total = hits.get("total");

            BigDecimal totalRoyalties = asBigDecimal(hits.getNumberValue("total"));
            Double max_score = 0.0;
            Integer tot = totalRoyalties.intValue();
            if(tot > 0){
                BigDecimal max_sc = asBigDecimal(hits.getNullableNumberValue("max_score"));
                max_score = max_sc.doubleValue();
            }
            //String st = hits.getNullableNumberValue("max_score");

            Double score_barrier = max_score/variance;
            
            //JSONArray hits_arr = (JSONArray)hits.getJSONArray("hits");
           // if(hiters.isEmpty())
           // {
                        //display.getCenterPanel().clear();
                        //display.getCenterPanel().add(new HTML("<H2>По вашему запросу ничего не найдено. Попробуйте изменить параметры поиска</H2>"));
                    //return;
           // }
            
            Set<Long> ids_no_clones = new TreeSet<Long>();
            for (int i = 0; i < hiters.size(); i++)
            {
                JsonNode hit = (JsonNode)hiters.get(i);
                BigDecimal varid = asBigDecimal(hit.getNode("_source").getNumberValue("sociovar_ID"));
                long ivarid = varid.longValue();
                
                BigDecimal sc = asBigDecimal(hit.getNumberValue("_score"));
                Double score = sc.doubleValue();
                if(score >= score_barrier && origin_var.getId()!=ivarid)ids_no_clones.add(ivarid);
            }
            
            for(Long id:ids_no_clones){
                var_ids.add(id);
            }
            
        } catch (InvalidSyntaxException ex) {
            Logger.getLogger(AdminSocioResearchSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return var_ids;
     }
     
     private ArrayList<Long> doSearchLikewise(VarDTO_Detailed origin_var, ComparativeSearchParamsDTO params)
     {
        String query = constructSearchLikewiseQuery(origin_var, params); 
        String [] types = new String[]{"sociovar"};
        String result = user_bean.doIndexSearchMaxResults(query, types,20);

        ArrayList<Long> var_ids = doParseLikewiseSearchResult(origin_var, params, result,params.getBarrier_variance());
        return var_ids;   
     }
     /*private ArrayList<Long> doSearchML(ArrayList<Long> var_ids){
         double [] ids = new double []{1,2,3,4};
         DenseInstance spi = new DenseInstance(ids);
         Classifier cl = new KNearestNeighbors(3);
         
         return var_ids;
     }*/
    @Override
    public void generalizeVars(ArrayList<Long> gen_var_ids) {
        
        
         try {
             for(Long var_id:gen_var_ids)
             {
                Var dsVar = em.find(Var.class, var_id);
                ArrayList<Long> other = new ArrayList<Long>();
                for(Long vid:gen_var_ids)if(!vid.equals(var_id))other.add(vid);
                dsVar.setGeneralized_var_ids(other);
                em.persist(dsVar);
             }
	    } finally {
	    }
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
