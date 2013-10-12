/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.service;

import com.mresearch.databank.jobs.IndexResearchJob;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author reshet
 */
//@MessageDriven(mappedName = "jms/ES_index",  activationConfig = {
//    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
//    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
//    @ActivationConfigProperty(propertyName = "destinationName", propertyValue = "jms/ES_index")
//    //@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/myQCF")
//        
//        
//})
public class ES_indexing_Bean implements MessageListener {
    
    // @PersistenceContext
    //private EntityManager em;
     
    public ES_indexing_Bean() {
    }
    
    @Override
    public void onMessage(Message message) {
        
//         ObjectMessage msg = null;
//        try {
//            if (message instanceof ObjectMessage) {
//                msg = (ObjectMessage) message;
//                System.out.println(msg.getStringProperty("title"));
//                Object obj = msg.getObject();
//                if (obj instanceof IndexResearchJob)
//                {
//                    IndexResearchJob job = (IndexResearchJob)obj;
//                    perform_indexing(job.getId_Research());         
//                }else
//                {
//                    //message.
//                }
//            }
//
//        } catch (Throwable te) {
//            te.printStackTrace();
//        }
    }
    //@EJB UserSocioResearchBeanRemote U_bean;
   
    private void perform_indexing(long id_research)
    {
//        //SocioResearchDTO dto = U_bean.getResearch(id_research);
//        SocioResearchDTO dto = new SocioResearchDTO();
//        dto.setId((long)1);
//        dto.setName("name");
//        Node node = nodeBuilder().node();
//        try {
//            
//            Client client = node.client();
//
//    // on shutdown
//
//           
////            IndexResponse response = client.prepareIndex("twitter", "tweet")
////            .setSource(jsonBuilder()
////                        .startObject()
////                            .field("user", "kimchy")
////                            .field("postDate", new Date())
////                            .field("message", "trying out Elastic Search")
////                        .endObject()
////                      )
////            .execute()
////            .actionGet();
//
////            IndexResponse response = client.prepareIndex("databank", "research")
////            .setSource(jsonBuilder()
////                        .startObject()
////                            .field("ID", dto.getID())
////                            .field("name", dto.getName())
////                            .field("method", dto.getMethod())
////                            .field("org_impl_name", dto.getOrg_impl_name())
////                            .field("org_order_name", dto.getOrg_order_name())
////                            .field("gen_geathering", dto.getGen_geathering())
////                            .field("sel_complexity", dto.getSel_complexity())
////                            .field("sel_randomity", dto.getSel_randomity())
////                            .field("start_date", dto.getStart_date())
////                            .field("end_date", dto.getEnd_date())
////                            .field("sel_size", dto.getSelection_size()) 
////                            .array("publications",dto.getPublications().toArray())
////                            .array("researchers", dto.getResearchers().toArray())
////                            .array("concepts", dto.getConcepts().toArray())
////                        .endObject()
////                      )
////            .execute()
////            .actionGet();
//
////            GetResponse response2 = client.prepareGet("twitter", "tweet", "1")
////                 .execute()
////                 .actionGet();
//            
//          //  System.out.println(response.toString());
//            
//        } catch (IOException ex) {
//            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        finally
//        {
//             node.close();
//        }
    }

//    public static void main(String [] args)
//    {
//        new ES_indexing_Bean().perform_indexing(5353);
//    }
    
}
