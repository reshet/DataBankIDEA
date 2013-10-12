/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.service;

import com.mplatforma.amr.service.remote.AdminPubBeanRemote;
import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import com.mplatforma.amr.service.remote.UserSocioResearchBeanRemote;
import com.mplatforma.amr.entity.*;
import com.mresearch.databank.jobs.DeleteIndexiesJob;
import com.mresearch.databank.jobs.IndexLawJobFast;
import com.mresearch.databank.jobs.IndexPubJobFast;
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
@Stateless(mappedName="AdminPubRemoteBean",name="AdminPubRemoteBean")
public class AdminPubSessionBean implements AdminPubBeanRemote{

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
       private void launchIndexingPub(PublicationDTO dto)
    {
         try {
            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to index PUB");
            // here we create NewsEntity, that will be sent in JMS message
           // ParseSpssJob job = new ParseSpssJob(blobkey, length);
            IndexPubJobFast job = new IndexPubJobFast(dto);
            message.setObject(job);    
           // message.setJMSDestination(queue);
            q_sender.send(message);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    
        @Override
    public Boolean deletePublication(Long id) {
	try {
        Publication Publication = em.find(Publication.class, id);
        if (Publication != null) {
            ArrayList<Long> r_id = new ArrayList<Long>();
            r_id.add(Publication.getId());
            launchDeleteIndexing(r_id, "publication");
            em.remove(Publication);
            
        }
        } finally {
        }
        return true;

    }

    @Override
    public PublicationDTO getPublication(Long id) {
        Publication dsPublication, detached;

        try {
        dsPublication = em.find(Publication.class, id);
        } finally {
        }

        return dsPublication.toDTO();

    }
    
    public PublicationDTO_Light getPublicationLight(Long id) {
        Publication dsPublication, detached;

        try {
        dsPublication = em.find(Publication.class, id);
        } finally {
        }

        return dsPublication.toDTO_Light();

    }
    
    @Override
    public PublicationDTO updatePublication(PublicationDTO aDTO) {
	if (aDTO.getId() == 0){ // create new
	      Publication newPublication = addPublication(aDTO);
	      return newPublication.toDTO();
	    }

	    Publication Publication = null;
	    try {
	      Publication = em.find(Publication.class, aDTO.getId());
	      //Publication.setName(aDTO.getHeader());
	      //Publication.setContents(aDTO.getContents());
	      //Publication.setEnclosure_key(aDTO.getEnclosure_key());
              Publication.updateFromDTO(aDTO);
              em.persist(Publication);
              launchIndexingPub(aDTO);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	    }
	    return aDTO;
    }

    @Override
    public ArrayList<PublicationDTO_Light> getPublicationDTOs(ArrayList<Long> keys) {
	ArrayList<PublicationDTO_Light> arr = new ArrayList<PublicationDTO_Light>();
	if (keys != null)
            return Publication.getPublicationLightDTOs(em, keys);
//            for(Long key:keys)
//            {
//                PublicationDTO_Light dto = this.getPublicationLight(key);
//                arr.add(dto);
//            }
	return arr;
    }

    @Override
    public ArrayList<PublicationDTO_Light> getPublicationsAll() {
//	ArrayList<PublicationDTO_Light> list = new ArrayList<PublicationDTO_Light>();
//   // SocioResearch dsResearch, detached;
//	try {
//            TypedQuery<Publication> q = em.createQuery("select x from Publication x",Publication.class);
//		List<Publication> res = (List<Publication>)q.getResultList();
//		for(Publication art:res)
//		{
//			list.add(art.toDTO_Light());
//		}
//            } finally {
//        }
//	return list;
        return Publication.getAllPublicationLightDTOs(em);

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")


private Publication addPublication(PublicationDTO publicationDTO) {
    Publication publication = null;
    try {
    // for this version of the app, just get hardwired 'default' user
    //UserAccount currentUser = UserAccount.getDefaultUser(); // detached object
    //currentUser = pm.makePersistent(currentUser); // attach
      publication = new Publication(publicationDTO);
      em.persist(publication);
      
      PublicationDTO dto = publication.toDTO();
      dto.setJson_desctiptor(publicationDTO.getJson_desctiptor());
      launchIndexingPub(dto);
    } finally {
    }
    return publication;
}

    @Override
    public ArrayList<PublicationDTO> getPublicationDTOs_Normal(ArrayList<Long> keys) {
       ArrayList<PublicationDTO> arr = new ArrayList<PublicationDTO>();
	if (keys != null)
            return Publication.getPublicationFullDTOs(em, keys);
//            for(Long key:keys)
//            {
//                PublicationDTO_Light dto = this.getPublicationLight(key);
//                arr.add(dto);
//            }
	return arr;
    }

    @Override
    public ArrayList<PublicationDTO_Light> getPublications(int limit, int offset) {
       return Publication.getPublicationLightDTOs(em,limit,offset);
    }

    @Override
    public ArrayList<TopicDTO> getTopics() {
        MetaUnitMultivaluedEntityDTO dt = wheredb.getDatabankStructure("publication");
        ArrayList<MetaUnitDTO> arr = dt.getSub_meta_units();
        ArrayList<TopicDTO> tops = new ArrayList<TopicDTO>();
        for(MetaUnitDTO dto:arr)
        {
            if(dto instanceof MetaUnitMultivaluedEntityDTO && dto.getUnique_name().equals(UserAccountSessionBean.PUBLICATION_TOPIC))
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
    public PublicationsBundleDTO getStartup() {
        MetaUnitMultivaluedEntityDTO meta = wheredb.getDatabankStructure("publication");
        ArrayList<PublicationDTO_Light> last_light = getPublications(10, 0);
        
        ArrayList<Long> ids = new ArrayList<Long>();
        for(PublicationDTO_Light l:last_light)
        {
                ids.add(l.getId());
        }
        
        ArrayList<PublicationDTO> last = getPublicationDTOs_Normal(ids);
        PublicationsBundleDTO dto = new PublicationsBundleDTO(meta,last_light,last,getTopics());
        return dto;
    }

   

}
