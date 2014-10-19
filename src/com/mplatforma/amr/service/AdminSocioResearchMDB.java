/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.service;

import com.mplatforma.amr.entity.SocioResearch;
import com.mplatforma.amr.entity.Var;
import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import com.mplatforma.amr.service.remote.UserSocioResearchBeanRemote;
import com.mresearch.databank.jobs.*;
import com.mresearch.databank.shared.*;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.opendatafoundation.data.FileFormatInfo;
import org.opendatafoundation.data.FileFormatInfo.Format;
import org.opendatafoundation.data.mod3.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

//import org.opendatafoundation.data.FileFormatInfo;
//import org.opendatafoundation.data.FileFormatInfo.Format;
//import org.opendatafoundation.data.mod.SPSSFile;
//import org.opendatafoundation.data.mod.SPSSFileException;
//import org.opendatafoundation.data.mod.SPSSNumericVariable;
//import org.opendatafoundation.data.mod.SPSSStringVariable;
//import org.opendatafoundation.data.mod.SPSSVariable;
//import org.opendatafoundation.data.mod2.SPSSNumericVariable;
//import org.opendatafoundation.data.mod2.SPSSStringVariable;
//import org.opendatafoundation.data.mod2.SPSSVariable;
//import org.opendatafoundation.data.mod2.SPSSFile;
//import org.opendatafoundation.data.mod2.SPSSFileException;
//import org.opendatafoundation.data.spss.*;

/**
 *
 * @author reshet
 */
@MessageDriven(mappedName = "jms/kiis/spss_parse", activationConfig = {@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"), @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
/*@MessageDriven(name = "admin_mdb",activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")*/
//  @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/myQCF")
//})
public class AdminSocioResearchMDB implements MessageListener {

//    static {
//        Locale locale = Locale.getDefault();
//        System.out.println("Before setting, Locale is = " + locale);
//        locale = new Locale("ru", "RU");
//        //  // Setting default locale  
//        // // locale = Locale.ITALY;
//        Locale.setDefault(locale);
//        System.out.println("After setting, Locale is = " + locale);
//    }
    @PersistenceContext
    private EntityManager em;
//    @Resource
//    private MessageDrivenContext mdc;
    @EJB
    private RxStorageBeanRemote store;
    public static String INDEX_NAME = "databankkiis";
    public static String STORAGE_VAULT = "/home/reshet/databank/"+INDEX_NAME+"/";
    /*@Resource(name="indexname")
    public  String INDEX_NAME;
*/
    public AdminSocioResearchMDB() {
        super();
    }

    @Override
    public void onMessage(Message message) {
        //message.
        ObjectMessage msg = null;
        try {
            if (message instanceof ObjectMessage) {
                msg = (ObjectMessage) message;
                System.out.println(msg.getStringProperty("title"));
                Object obj = msg.getObject();

                if (obj instanceof ParseSpssJob) {
                    ParseSpssJob job = (ParseSpssJob) obj;
                    parseSPSS(job.getId_file(), job.getLength());
                } else if (obj instanceof IndexResearchJob) {
                    IndexResearchJob job = (IndexResearchJob) obj;
                    perform_indexing(job.getId_Research());
                } else if (obj instanceof IndexVarJob) {
                    IndexVarJob job = (IndexVarJob) obj;
                    VarDTO_Detailed dto = U_bean.getVarDetailed(job.getId_Var(), null, null);
                    perform_indexing_var(dto);
                } else if (obj instanceof IndexVarJobFast) {
                    IndexVarJobFast job = (IndexVarJobFast) obj;
                    perform_indexing_var(job.getDto());
                } else if (obj instanceof IndexLawJobFast) {
                    IndexLawJobFast job = (IndexLawJobFast) obj;
                    perform_indexing_law(job.getDto());
                } else if (obj instanceof IndexPubJobFast) {
                    IndexPubJobFast job = (IndexPubJobFast) obj;
                    perform_indexing_pub(job.getDto());
                } else if (obj instanceof IndexJuryJobFast) {
                    IndexJuryJobFast job = (IndexJuryJobFast) obj;
                    perform_indexing_jury(job.getDto());
                } else if (obj instanceof DeleteIndexiesJob) {
                    DeleteIndexiesJob job = (DeleteIndexiesJob) obj;
                    perform_delete_indexies(job.getIds(), job.getType());
                }
            }
        } catch (Throwable te) {
            te.printStackTrace();
        }
    }
//    public static void main(String [] args)
//    {
//        new AdminSocioResearchMDB().perform_indexing(0);
//    }
//    private Node node;
    private Client client;

    @EJB ESClientBean clientbean;
    @Resource(mappedName = "jms/kiis/myQCF")
    //@Resource(name = "jmsQCF")
    private QueueConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/kiis/spss_parse")
    //@Resource(name = "jmsqueue")
    private Queue queue;
    private QueueConnection connection;
    private QueueSession session;
    QueueSender q_sender;

    @PostConstruct
    public void init() {
        client = clientbean.getClient();
        //node = nodeBuilder().clusterName("elasticsearch_databankalliance_Prj_Cluster").client(false).node();
        try {
            connection = connectionFactory.createQueueConnection();
            session = connection.createQueueSession(false, 0);
            q_sender = session.createSender(queue);

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

    @PreDestroy
    public void release() {
        //node.close();

        try {
            q_sender.close();
            connection.close();
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    @EJB
    UserSocioResearchBeanRemote U_bean;

    private void perform_delete_indexies(ArrayList<Long> ids, String type) {
        //SocioResearchDTO dto = new SocioResearchDTO();
        //dto.setId((long)1);
        //dto.setName("name");
        //dto.setMethod("method");
        //dto.setOrg_impl_name("org.impl.name");
        //String t = System.getProperty("java.classpath");
        // Node node = nodeBuilder().client(true).node();
        try {


            //Client client = node.client();
            BulkRequestBuilder bulkRequest = client.prepareBulk();

            String[] indecies = new String[ids.size()];
            int i = 0;
            for (Long ind : ids) {
                indecies[i++] = String.valueOf(ind);
//            DeleteResponse response = client.prepareDelete(INDEX_NAME, type, String.valueOf(ind)) 
//             .execute() 
//              .actionGet(); 
                bulkRequest.add(client.prepareDelete(INDEX_NAME, type, String.valueOf(ind)));

            }

            //DeleteByQueryRequest req = new DeleteByQueryRequest().types(type).indices(indecies);
            //   DeleteByQueryResponse resp = client.prepareDeleteByQuery(type).setQuery(QueryBuilders.idsQuery(indecies)).execute().actionGet();

            //DeleteByQueryResponse resp = client.prepareDeleteByQuery(INDEX_NAME).setTypes(type).setQuery(QueryBuilders.idsQuery(indecies)).execute().actionGet();

            // System.out.println(resp.toString());
            BulkResponse resp = bulkRequest.execute().actionGet();
            System.out.println(resp.toString());

        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // node.close();
        }
    }

    private void perform_indexing(long id_research) {

        SocioResearchDTO dto = U_bean.getResearch(id_research);
        //SocioResearchDTO dto = new SocioResearchDTO();
        //dto.setId((long)1);
        //dto.setName("name");
        //dto.setMethod("method");
        //dto.setOrg_impl_name("org.impl.name");
        //String t = System.getProperty("java.classpath");
        // Node node = nodeBuilder().client(true).node();
        try {


            //Client client = node.client();

            // on shutdown


//            IndexResponse response = client.prepareIndex("twitter", "tweet")
//            .setSource(jsonBuilder()
//                        .startObject()
//                            .field("user", "kimchy")
//                            .field("postDate", new Date())
//                            .field("message", "trying out Elastic Search")
//                        .endObject()
//                      )
//            .execute()
//            .actionGet();

            IndexResponse response = client.prepareIndex(INDEX_NAME, "research", String.valueOf(dto.getID())).setSource(dto.getJson_descriptor() //                    jsonBuilder()
                    //                        .startObject()
                    //                            .field("ID", dto.getID())
                    //                            .field("name", dto.getName())
                    //                            .field("method", dto.getMethod())
                    //                            .field("org_impl_name", dto.getOrg_impl_name())
                    //                            .field("org_order_name", dto.getOrg_order_name())
                    //                            .field("gen_geathering", dto.getGen_geathering())
                    //                            .field("sel_complexity", dto.getSel_complexity())
                    //                            .field("sel_randomity", dto.getSel_randomity())
                    //                            .field("start_date", dto.getStart_date())
                    //                            .field("end_date", dto.getEnd_date())
                    //                            .field("sel_size", dto.getSelection_size()) 
                    //                            .array("publications",dto.getPublications().toArray())
                    //                            .array("researchers", dto.getResearchers().toArray())
                    //                            .array("concepts", dto.getConcepts().toArray())
                    //                        .endObject()
                    ).execute().actionGet();

            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexQueryDoc:" + dto.getJson_descriptor());
            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexResponse:"+response.toString());
                Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexResponse2:"+response.getIndex()+" "+response.getId()+" "+response.getVersion()+" "+response.getMatches());
//            GetResponse response2 = client.prepareGet("twitter", "tweet", "1")
//                 .execute()
//                 .actionGet();

            //System.out.println(response.toString());

        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // node.close();
        }
    }

//    @Resource(mappedName="jms/ES_index")
//    private  Queue index_queue;
    private void launchIndexingVar(long var_id) {
        try {
            ObjectMessage message = session.createObjectMessage();
            message.setStringProperty("title", "command to index SocioResearch var");
            // here we create NewsEntity, that will be sent in JMS message
            // ParseSpssJob job = new ParseSpssJob(blobkey, length);
            IndexVarJob job = new IndexVarJob(var_id);
            message.setObject(job);
            // message.setJMSDestination(queue);
            q_sender.send(message);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    private ArrayList<VarDTO_Detailed> vars_waiting_indexing;

    private void launchIndexingVarBULKED(VarDTO_Detailed dto) {
        vars_waiting_indexing.add(dto);
    }

    private void perform_var_bulk_indexing() {
        try {
            if (vars_waiting_indexing != null) {
                //Client client = node.client();
                BulkRequestBuilder bulkRequest = client.prepareBulk();
                for (VarDTO_Detailed dto : vars_waiting_indexing) {
                    bulkRequest.add(client.prepareIndex(INDEX_NAME, "sociovar", String.valueOf(dto.getId())).setSource(generateVarJSONDesc(dto)));
                }
                BulkResponse resp = bulkRequest.execute().actionGet();
                System.out.println("Indexed vars count:" + resp.getItems().length + " ,takes time:" + resp.getTook().toString());
                vars_waiting_indexing = null;
            }

        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // node.close();
        }


    }

    private String generateVarJSONDesc(VarDTO_Detailed dto) {
        String json = "";
        if (dto != null) {
            try {

                String[] empt = new String[0];
                    XContentBuilder bld = jsonBuilder().startObject();
               // json = jsonBuilder().startObject()
                        bld.field("sociovar_ID", dto.getId() == 0 ? "" : dto.getId())
                        .field("sociovar_code", dto.getCode() == null ? "" : dto.getCode())
                        .field("sociovar_name", dto.getLabel() == null ? "" : dto.getLabel())
                        .array("sociovar_alt_codes", dto.getV_label_codes() == null ? empt : dto.getV_label_codes().toArray())
                        .array("sociovar_alt_values", dto.getV_label_values() == null ? empt : dto.getV_label_values().toArray());
                if(dto.getFilling()!= null)
                for(String key:dto.getFilling().keySet())
                  {
                      bld.field(key,dto.getFilling().get(key));
                  }
                  json = bld.endObject().string();

                return json;
            } catch (IOException ex) {
                Logger.getLogger(AdminSocioResearchMDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return json;
    }

    private void perform_indexing_var(VarDTO_Detailed dto) {


        //SocioResearchDTO dto = new SocioResearchDTO();
        //dto.setId((long)1);
        //dto.setName("name");
        //dto.setMethod("method");
        //dto.setOrg_impl_name("org.impl.name");
        //String t = System.getProperty("java.classpath");
        // Node node = nodeBuilder().client(true).node();
        try {


            //Client client = node.client();

            // on shutdown


//            IndexResponse response = client.prepareIndex("twitter", "tweet")
//            .setSource(jsonBuilder()
//                        .startObject()
//                            .field("user", "kimchy")
//                            .field("postDate", new Date())
//                            .field("message", "trying out Elastic Search")
//                        .endObject()
//                      )
//            .execute()
//            .actionGet();
            String jsondesc = generateVarJSONDesc(dto);
            IndexResponse response = client.prepareIndex(INDEX_NAME, "sociovar", String.valueOf(dto.getId())).setSource(jsondesc).execute().actionGet();

//            GetResponse response2 = client.prepareGet("twitter", "tweet", "1")
//                 .execute()
//                 .actionGet();
             Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexQueryDoc:" + jsondesc);
            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexResponse:"+response.toString());
                Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexResponse2:"+response.getIndex()+" "+response.getId()+" "+response.getVersion()+" "+response.getMatches());
            //System.out.println(response.index());

        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // node.close();
        }
    }

    private void perform_indexing_law(ZaconDTO dto) {


        //SocioResearchDTO dto = new SocioResearchDTO();
        //dto.setId((long)1);
        //dto.setName("name");
        //dto.setMethod("method");
        //dto.setOrg_impl_name("org.impl.name");
        //String t = System.getProperty("java.classpath");
        // Node node = nodeBuilder().client(true).node();
        try {


            //Client client = node.client();

            // on shutdown


//            IndexResponse response = client.prepareIndex("twitter", "tweet")
//            .setSource(jsonBuilder()
//                        .startObject()
//                            .field("user", "kimchy")
//                            .field("postDate", new Date())
//                            .field("message", "trying out Elastic Search")
//                        .endObject()
//                      )
//            .execute()
//            .actionGet();

            IndexResponse response = client.prepareIndex(INDEX_NAME, "law", String.valueOf(dto.getId())).setSource(dto.getJson_desctiptor()).execute().actionGet();

//            GetResponse response2 = client.prepareGet("twitter", "tweet", "1")
//                 .execute()
//                 .actionGet();

            System.out.println(response.toString());
            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexQueryDoc:" + dto.getJson_desctiptor());

        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // node.close();
        }
    }

    private void perform_indexing_pub(PublicationDTO dto) {
        try {
            //Client client = node.client();
            IndexResponse response = client.prepareIndex(INDEX_NAME, "publication", String.valueOf(dto.getId())).setSource(dto.getJson_desctiptor()).execute().actionGet();
            System.out.println(response.toString());

        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }

    private void perform_indexing_jury(ConsultationDTO dto) {
        try {
            //Client client = node.client();
            IndexResponse response = client.prepareIndex(INDEX_NAME, "consultation", String.valueOf(dto.getId())).setSource(dto.getJson_desctiptor()).execute().actionGet();
            System.out.println(response.toString());

        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }

    private byte[] makeSyntax(byte[] arr) {
        String newName1 = "/home/reshet/spss_conv_files/spss_file_" + new Date() + "_" + "incoming" + ".sav";
        //save file
        saveFileBytes(arr, newName1);

        try {
            // Execute a command without arguments
            String command = "ls";
            Process child = Runtime.getRuntime().exec(command);
            child.waitFor();
            // Execute a command with an argument
        } catch (InterruptedException ex) {
            Logger.getLogger(AdminSocioResearchMDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
        }
        //execute syntax

        //load file
        String newName2 = "/home/reshet/spss_conv_files/spss_file_" + new Date() + "_" + "utf8_uncompressed" + ".sav";
        byte[] ar = getFileBytes(newName2);

        return ar;
    }

    private byte[] getFileBytes(String path) {
        File f = new File(path);
        FileInputStream fin = null;
        FileChannel ch = null;
        try {
            fin = new FileInputStream(f);
            ch = fin.getChannel();
            int size = (int) ch.size();
            MappedByteBuffer buf = ch.map(FileChannel.MapMode.READ_ONLY, 0, size);
            byte[] bytes = new byte[size];
            buf.get(bytes);
            return bytes;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
                if (ch != null) {
                    ch.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    private void saveFileBytes(byte[] arr, String name) {
        File f = new File(name);
        FileOutputStream fout = null;
        FileChannel ch = null;
        try {
            fout = new FileOutputStream(f);
            ch = fout.getChannel();
            int size = (int) ch.size();
            MappedByteBuffer buf = ch.map(FileChannel.MapMode.READ_ONLY, 0, size);
            byte[] bytes = new byte[size];
            buf.put(arr);
            //return newName;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (ch != null) {
                    ch.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // return null;
    }

    private void parseSPSS(long blobkey, long length) {
        try {
//            System.out.println("PARSE SPSS MDB, em = " + em);
//
//         
//         Locale locale = Locale.getDefault();
//         System.out.println("Before setting, Locale is = " + locale);
//         locale = new Locale("ru","RU");
//         Locale.setDefault(locale);
//         System.out.println("After setting, Locale is = " + locale);
    
          
            Long socioresearch_key = null;
            //byte[] arr = new byte[(int) length];





            RxStoredDTO dto = store.getFileInfo(blobkey);
            File spssfile = new File(AdminSocioResearchMDB.STORAGE_VAULT+blobkey);
            //arr = store.getFileContents(blobkey);


            //byte [] arr2 = makeSyntax(arr);
            //here make syntax


/*            byte[] buf = new byte[4096];
            UniversalDetector detector = new UniversalDetector(null);
//
            ByteArrayInputStream fis = new ByteArrayInputStream(arr);
//            // (2)
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            // (3)
            detector.dataEnd();*/

            // (4)
            //String encoding = detector.getDetectedCharset();
            boolean isCP1251 = false;
            boolean isKOI8_R = false;

            /*if (encoding != null) {
                System.out.println("Detected encoding = " + encoding);
                if (encoding.equals("WINDOWS-1251")) {
                    isCP1251 = true;
                }
                if (encoding.equals("KOI8-R")) {
                    isKOI8_R = true;
                }
            } else {
                System.out.println("No encoding detected.");
            }*/

            //String fileName = "/home/reshet/spssfiles/" + dto.getName();

//            BufferedOutputStream bs = null;
//            File file = new File(fileName);
//            try {
//
//                FileOutputStream fs = new FileOutputStream(file);
//                bs = new BufferedOutputStream(fs);
//                bs.write(arr);
//                bs.close();
//                bs = null;
//
//            } catch (Exception e) {
//}
//
//            if (bs != null) try { bs.close(); } catch (Exception e) {}
//            

            SPSSFile s = new SPSSFile(spssfile);
            String st = s.getDDI3DefaultPhysicalDataProductID(new FileFormatInfo(Format.SPSS));
            String ans = "";
            String answer = "";
//            ans = "file_cr";
 //           ans+= "length = "+arr.length;
            //ans+="fetch_size = "+String.valueOf(b_serv.MAX_BLOB_FETCH_SIZE)+ " ";
//            byte [] arr_first = new byte[100];
//            for(int i = 0; i < 100;i++)
//            {
//                    arr_first[i] = arr[i];
//            }
//            ans+=new String(arr_first);
            //s.


            try {
                //s.setIsCP1251(isCP1251);
               // s.setIsKOI8_R(isKOI8_R);
                s.loadMetadata();
                //        ans+=" meta_loaded";
                //s.setIsCP1251(false);
                //s.setIsKOI8_R(false);
                
                s.loadData();
                //        ans+=" data_loaded";
                //org.w3c.dom.Document doc1 = s.getDDI3LogicalProduct();
                //Document doc2 = s.getDDI3PhysicalDataProduct(new FileFormatInfo(Format.SPSS));
                //        ans+=" doc created";

                socioresearch_key = createEmptyResearch(dto.getName(), blobkey);
                addSPSStoSocioResearch(socioresearch_key, s, blobkey, "");

            } catch (SPSSFileException ex) {
                Logger.getLogger(AdminSocioResearchMDB.class.getName()).log(Level.SEVERE, null, ex);
            }

            //return ans+"  "+socioresearch_key + " : vars :  "+answer;
            //return socioresearch_key;
        } catch (IOException ex) {
            Logger.getLogger(AdminSocioResearchMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return ans+"  "+socioresearch_key + " : vars :  "+answer;
        //return socioresearch_key;
    }

    private long createEmptyResearch(String name, long file_id) {

        SocioResearch research = null;
        Long research_id = null;
        try {
            research = new SocioResearch(em);
            research.setName(name);
            ResearchFilesDTO dto = new ResearchFilesDTO();
            dto.addFile(ResearchFilesDTO.CG_arrays, name, file_id);
            research.updateFileAccessor(em, dto);
            em.persist(research);
            research_id = research.getID();
            // currentUser.getFriends().add(friend);
        } finally {
        }
        return research_id;
    }

    private String addSPSStoSocioResearch(long socioresearch_id, SPSSFile spss, long spss_blobkey, String ans) {
        String ans1 = "";
        ArrayList<Long> var_ids = new ArrayList<Long>();
        vars_waiting_indexing = new ArrayList<VarDTO_Detailed>(100);
        for (int i = 0; i < spss.getVariableCount(); i++) {
            System.out.println(i);
            SPSSVariable s_var = spss.getVariable(i);
            //ans1+=s_var.getLabel()+" ";
            //		try {
            //			Element el = s_var.getDDI3CodeScheme(doc);
            //			int a = 2;
            //			a+=a;
            //		} catch (DOMException e) {
            //			// TODO Auto-generated catch block
            //			e.printStackTrace();
            //		} catch (SPSSFileException e) {
            //			// TODO Auto-generated catch block
            //			e.printStackTrace();
            //		}
            long s_key = createVar(s_var, socioresearch_id);
            //ans1 += s_key;
            var_ids.add(s_key);
        }
        
            
        
        SocioResearch dsResearch;
        try {
            dsResearch = em.find(SocioResearch.class, socioresearch_id);
            dsResearch.setSpssFile(spss_blobkey);
            dsResearch.setVar_ids(var_ids);
            dsResearch.setSelection_size(spss.getRecordCount());
            //HERE HARD CODE CONVENTION
            dsResearch.getEntity_item().getMapped_values().put(SocioResearch.SEL_SIZE_NAME, String.valueOf(spss.getRecordCount()));
            dsResearch.getEntity_item().getMapped_values().put(SocioResearch._NAME, dsResearch.getName());

            em.persist(dsResearch);
            
           
        } finally {
            
        }
         perform_var_bulk_indexing();
        return ans1;
    }

    private long createVar(SPSSVariable s_var, long research_id) {
        Var var = null;
        Long var_id = null;

        try {
            var = new Var();

            var.setResearch_id(research_id);

            String label = s_var.getLabel();
            var.setCode(new String(s_var.getName()));
            var.setLabel(label);
            if (s_var.valueLabelRecord != null) {
                ArrayList<String> labels_encoding = s_var.valueLabelRecord.getVLabelValues();
                
                Set<String> missing_codes = new HashSet<String>();
                missing_codes.add(s_var.getMissing1());
                missing_codes.add(s_var.getMissing2());
                missing_codes.add(s_var.getMissing3());


                
                int missings_count = 0;
                for (int i = 0; i < labels_encoding.size(); i++) {
                    if(missing_codes.contains(
                            String.valueOf(s_var.valueLabelRecord.getVLabelCodes().get(i)))) {
                      missings_count++;
                    }
                }
                if(missings_count < labels_encoding.size()) {
                  var.setVar_type(VarDTO_Detailed.alt_var_type);
                } else {
                     if (s_var instanceof SPSSNumericVariable) {
                      var.setVar_type(VarDTO_Detailed.real_var_type);
                     }
                     if (s_var instanceof SPSSStringVariable) {
                        var.setVar_type(VarDTO_Detailed.text_var_type);
                     }
                }
                var.setV_label_codes(s_var.valueLabelRecord.getVLabelCodes());
                var.setV_label_values(labels_encoding);
            } else {
                if (s_var instanceof SPSSNumericVariable) {
                    var.setVar_type(VarDTO_Detailed.real_var_type);
                }
                if (s_var instanceof SPSSStringVariable) {
                    var.setVar_type(VarDTO_Detailed.text_var_type);
                    SPSSStringVariable str_var = (SPSSStringVariable) s_var;
                    ArrayList<String> str_arr = new ArrayList<String>();
                    for (String str : str_var.data) {
                        str_arr.add(str);
                    }
                    var.setCortage_string(str_arr);
                }
            }

            ArrayList<Double> values = new ArrayList<Double>();
            //SPSS
            if (s_var instanceof SPSSNumericVariable) {

                SPSSNumericVariable s_var_numeric = (SPSSNumericVariable) s_var;
                for (Iterator<Double> it = s_var_numeric.data.iterator(); it.hasNext();) {
                    Double value = it.next();
                    if(value!=null && value!=Double.NaN && value < 1E+6 && value > (-1 * 1E+6)){
                        values.add(value);
                    }
                    else {
                        values.add(0.0);
                    }
                }
            }
            var.setCortage(values);
            
            var.setMissing1(s_var.getMissing1());
            var.setMissing2(s_var.getMissing2());
            var.setMissing3(s_var.getMissing3());
            
            //var.setV_label_map(map);
            em.persist(var);
            var_id = var.getID();
            VarDTO_Detailed ddto = var.toDTO_Detailed(null, null, em);
            launchIndexingVarBULKED(ddto);
            em.flush();
            //em.getTransaction().commit();
            //launchIndexingVar(var_id);
//     }  catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(AdminSocioResearchSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
            // testDDI3work();
        } finally {
        }
        
         return var_id;
        //return var_id;
    }
}
