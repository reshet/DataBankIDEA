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
import org.opendatafoundation.data.mod3.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


/**
 *
 * @author reshet
 */
@MessageDriven(mappedName = "jms/national/spss_parse", activationConfig = {@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"), @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class AdminSocioResearchMDB implements MessageListener {
    @PersistenceContext
    private EntityManager em;
    @EJB
    private RxStorageBeanRemote store;
    public static final String INDEX_NAME = "databanknational";
    public static final String STORAGE_VAULT = "/home/reshet/databank/databanknational/";
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
    @Resource(mappedName = "jms/national/myQCF")
    //@Resource(name = "jmsQCF")
    private QueueConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/national/spss_parse")
    //@Resource(name = "jmsqueue")
    private Queue queue;
    private QueueConnection connection;
    private QueueSession session;
    QueueSender q_sender;

    @PostConstruct
    public void init() {
        client = clientbean.getClient();
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
        try {
            BulkRequestBuilder bulkRequest = client.prepareBulk();

            String[] indecies = new String[ids.size()];
            int i = 0;
            for (Long ind : ids) {
                indecies[i++] = String.valueOf(ind);
                bulkRequest.add(client.prepareDelete(INDEX_NAME, type, String.valueOf(ind)));

            }
            BulkResponse resp = bulkRequest.execute().actionGet();
            System.out.println(resp.toString());

        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void perform_indexing(long id_research) {

        SocioResearchDTO dto = U_bean.getResearch(id_research);
        try {
            IndexResponse response = client.prepareIndex(INDEX_NAME, "research", String.valueOf(dto.getID()))
                    .setSource(dto.getJson_descriptor())
                    .execute()
                    .actionGet();

            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexQueryDoc:" + dto.getJson_descriptor());
            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexResponse:"+response.toString());
                Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexResponse2:"+response.getIndex()+" "+response.getId()+" "+response.getVersion()+" "+response.getMatches());
        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
        try {
            String jsondesc = generateVarJSONDesc(dto);
            IndexResponse response = client.prepareIndex(INDEX_NAME, "sociovar", String.valueOf(dto.getId())).setSource(jsondesc).execute().actionGet();

             Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexQueryDoc:" + jsondesc);
            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexResponse:"+response.toString());
                Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexResponse2:"+response.getIndex()+" "+response.getId()+" "+response.getVersion()+" "+response.getMatches());

        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void perform_indexing_law(ZaconDTO dto) {
        try {
            IndexResponse response = client.prepareIndex(INDEX_NAME, "law", String.valueOf(dto.getId())).setSource(dto.getJson_desctiptor()).execute().actionGet();
            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "IndexQueryDoc:" + dto.getJson_desctiptor());
        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void perform_indexing_pub(PublicationDTO dto) {
        try {
            IndexResponse response = client.prepareIndex(INDEX_NAME, "publication", String.valueOf(dto.getId())).setSource(dto.getJson_desctiptor()).execute().actionGet();
        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void perform_indexing_jury(ConsultationDTO dto) {
        try {
            IndexResponse response = client.prepareIndex(INDEX_NAME, "consultation", String.valueOf(dto.getId())).setSource(dto.getJson_desctiptor()).execute().actionGet();
        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseSPSS(long blobkey, long length) {
        try {
           RxStoredDTO dto = store.getFileInfo(blobkey);
            File spssfile = new File(AdminSocioResearchMDB.STORAGE_VAULT+blobkey);
            SPSSFile s = new SPSSFile(spssfile);
            try {
                s.loadMetadata();
                s.loadData();
                Long socioresearch_key = createEmptyResearch(dto.getName(), blobkey);
                addSPSStoSocioResearch(socioresearch_key, s, blobkey, "");
            } catch (SPSSFileException ex) {
                Logger.getLogger(AdminSocioResearchMDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(AdminSocioResearchMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            long s_key = createVar(s_var, socioresearch_id);
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
        Var var = new Var();
        var.setResearch_id(research_id);
        String label = s_var.getLabel();
        // Text vars often dont have a label, only code
        if (label == null || label.equals("")) {
          label = s_var.getName();
        }

        var.setCode(s_var.getName());
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
                var.setCortage_string(new ArrayList<String>(str_var.data));
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
                  // Add system missing here?
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
        Long var_id = var.getID();
        VarDTO_Detailed ddto = var.toDTO_Detailed(null, null, em);
        launchIndexingVarBULKED(ddto);
        em.flush();
        
        return var_id;
    }
}
