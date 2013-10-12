/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.service;

import com.mplatforma.amr.service.remote.AdminJuryBeanRemote;
import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import com.mplatforma.amr.service.remote.UserSocioResearchBeanRemote;
import com.mplatforma.amr.entity.*;
import com.mresearch.databank.jobs.DeleteIndexiesJob;
import com.mresearch.databank.jobs.IndexLawJobFast;
import com.mresearch.databank.jobs.IndexJuryJobFast;
import com.mresearch.databank.shared.*;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author reshet
 */
@WebService
@Stateless(mappedName="AdminJuryRemoteBean",name="AdminJuryRemoteBean")
public class AdminJurySessionBean implements AdminJuryBeanRemote{

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private RxStorageBeanRemote store;
    @EJB
    private UserSocioResearchBeanRemote wheredb;
    

   
        @Resource(mappedName="jms/myQCF")
    private  QueueConnectionFactory connectionFactory;

    @Resource(mappedName="jms/spss_parse")
    private  Queue queue;

    
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
       private void launchIndexingJury(ConsultationDTO dto)
    {
         try {
            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to index Jury");
            // here we create NewsEntity, that will be sent in JMS message
           // ParseSpssJob job = new ParseSpssJob(blobkey, length);
            IndexJuryJobFast job = new IndexJuryJobFast(dto);
            message.setObject(job);    
           // message.setJMSDestination(queue);
            q_sender.send(message);
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
            
            DeleteIndexiesJob job = new DeleteIndexiesJob(ids, type);
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
    
    
        @Override
    public Boolean deleteConsultation(Long id) {
	try {
        Consultation Consultation = em.find(Consultation.class, id);
        if (Consultation != null) {
            ArrayList<Long> r_id = new ArrayList<Long>();
            r_id.add(Consultation.getId());
           launchDeleteIndexing(r_id, "consultation");
            em.remove(Consultation);
           
            
        }
        } finally {
        }
        return true;

    }

    @Override
    public ConsultationDTO getConsultation(Long id) {
        Consultation dsConsultation, detached;

        try {
        dsConsultation = em.find(Consultation.class, id);
        } finally {
        }

        return dsConsultation.toDTO();

    }
    
    public ConsultationDTO_Light getConsultationLight(Long id) {
        Consultation dsConsultation, detached;

        try {
        dsConsultation = em.find(Consultation.class, id);
        } finally {
        }

        return dsConsultation.toDTO_Light();

    }
    
    @Override
    public ConsultationDTO updateConsultation(ConsultationDTO aDTO) {
	if (aDTO.getId() == 0){ // create new
	      Consultation newConsultation = addConsultation(aDTO);
	      return newConsultation.toDTO();
	    }

	    Consultation Consultation = null;
	    try {
	      Consultation = em.find(Consultation.class, aDTO.getId());
	      Consultation.updateFromDTO(aDTO);
              
              em.persist(Consultation);
              launchIndexingJury(aDTO);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	    }
	    return aDTO;
    }

    @Override
    public ArrayList<ConsultationDTO_Light> getConsultationDTOs(ArrayList<Long> keys) {
	ArrayList<ConsultationDTO_Light> arr = new ArrayList<ConsultationDTO_Light>();
	if (keys != null)
            return Consultation.getConsultationLightDTOs(em, keys);
//            for(Long key:keys)
//            {
//                ConsultationDTO_Light dto = this.getConsultationLight(key);
//                arr.add(dto);
//            }
	return arr;
    }

    @Override
    public ArrayList<ConsultationDTO_Light> getConsultationsAll() {
//	ArrayList<ConsultationDTO_Light> list = new ArrayList<ConsultationDTO_Light>();
//   // SocioResearch dsResearch, detached;
//	try {
//            TypedQuery<Consultation> q = em.createQuery("select x from Consultation x",Consultation.class);
//		List<Consultation> res = (List<Consultation>)q.getResultList();
//		for(Consultation art:res)
//		{
//			list.add(art.toDTO_Light());
//		}
//            } finally {
//        }
//	return list;
        return Consultation.getAllConsultationLightDTOs(em);

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")


private Consultation addConsultation(ConsultationDTO ConsultationDTO) {
    Consultation Consultation = null;
    try {
    // for this version of the app, just get hardwired 'default' user
    //UserAccount currentUser = UserAccount.getDefaultUser(); // detached object
    //currentUser = pm.makePersistent(currentUser); // attach
      Consultation = new Consultation(ConsultationDTO);
      em.persist(Consultation);
      ConsultationDTO dto = Consultation.toDTO();
      dto.setJson_desctiptor(ConsultationDTO.getJson_desctiptor());
      launchIndexingJury(dto);
    } finally {
    }
    return Consultation;
}

    @Override
    public ArrayList<ConsultationDTO> getConsultationDTOs_Normal(ArrayList<Long> keys) {
       ArrayList<ConsultationDTO> arr = new ArrayList<ConsultationDTO>();
	if (keys != null)
            return Consultation.getConsultationFullDTOs(em, keys);
//            for(Long key:keys)
//            {
//                ConsultationDTO_Light dto = this.getConsultationLight(key);
//                arr.add(dto);
//            }
	return arr;
    }

    @Override
    public ArrayList<ConsultationDTO_Light> getConsultations(int limit, int offset) {
       return Consultation.getConsultationLightDTOs(em,limit,offset);
    }

    @Override
    public ArrayList<TopicDTO> getTopics() {
        MetaUnitMultivaluedEntityDTO dt = wheredb.getDatabankStructure("consultation");
        ArrayList<MetaUnitDTO> arr = dt.getSub_meta_units();
        ArrayList<TopicDTO> tops = new ArrayList<TopicDTO>();
        for(MetaUnitDTO dto:arr)
        {
            if(dto instanceof MetaUnitMultivaluedEntityDTO && dto.getUnique_name().equals(UserAccountSessionBean.CONSULTATION_TOPIC))
            {
                MetaUnitMultivaluedEntityDTO topics = (MetaUnitMultivaluedEntityDTO)dto;
                int i = 0;
                MetaUnitMultivaluedEntityDTO flat = wheredb.getMetaUnitMultivaluedEntityDTO_FlattenedItems(topics.getId());
                for(Long id: flat.getItem_ids())
                {
                    String name = flat.getItem_names().get(i);
                    ArrayList<Long> tagged = wheredb.getEntityItemTaggedEntitiesIDs(id);
                    TopicDTO top = new TopicDTO(id, name, tagged);
                    tops.add(top);
                    i++;
                }
                break;
            }
        }
        return tops;
    }

    @Override
    public JuryBundleDTO getStartup() {
        MetaUnitMultivaluedEntityDTO meta = wheredb.getDatabankStructure("consultation");
        ArrayList<ConsultationDTO_Light> last_light = getConsultations(10, 0);
        
        ArrayList<Long> ids = new ArrayList<Long>();
        for(ConsultationDTO_Light l:last_light)
        {
                ids.add(l.getId());
        }
        
        ArrayList<ConsultationDTO> last = getConsultationDTOs_Normal(ids);
        JuryBundleDTO dto = new JuryBundleDTO(meta, last,getTopics());
        return dto;
    }
    
}
