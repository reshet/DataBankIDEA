/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.service;

import com.mplatforma.amr.entity.Consultation;
import com.mplatforma.amr.service.remote.AdminJuryBeanRemote;
import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import com.mplatforma.amr.service.remote.SearchServicesBeanRemote;
import com.mplatforma.amr.service.remote.UserSocioResearchBeanRemote;
import com.mresearch.databank.jobs.DeleteIndexiesJob;
import com.mresearch.databank.jobs.IndexJuryJobFast;
import com.mresearch.databank.shared.*;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 *
 * @author reshet
 */
//@WebService
@Stateless(mappedName="SearchServicesRemoteBean",name="SearchServicesRemoteBean")
public class SearchServicesBean implements SearchServicesBeanRemote{
    @EJB ESClientBean clientbean;
    @EJB UserSocioResearchBeanRemote U_bean;
    private Client client;

    @PostConstruct
    public void init() {
        client = clientbean.getClient();
    }
    @Override
    public void perform_delete_indexies(ArrayList<Long> ids, String type) {
        try {
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            String[] indecies = new String[ids.size()];
            int i = 0;
            for (Long ind : ids) {
                indecies[i++] = String.valueOf(ind);
                bulkRequest.add(client.prepareDelete(AdminSocioResearchMDB.INDEX_NAME, type, String.valueOf(ind)));
            }
           BulkResponse resp = bulkRequest.execute().actionGet();
           System.out.println(resp.toString());

        } catch (Exception ex) {
            Logger.getLogger(SearchServicesBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }

    @Override
    public void perform_indexing(long id_research) {
        SocioResearchDTO dto = U_bean.getResearch(id_research);
        try {
            IndexResponse response = client.prepareIndex(AdminSocioResearchMDB.INDEX_NAME, "research", String.valueOf(dto.getID())).setSource(dto.getJson_descriptor() //                    jsonBuilder()
            ).execute().actionGet();

            Logger.getLogger(SearchServicesBean.class.getName()).log(Level.INFO, "IndexQueryDoc:" + dto.getJson_descriptor());
            Logger.getLogger(SearchServicesBean.class.getName()).log(Level.INFO, "IndexResponse:"+response.toString());
            Logger.getLogger(SearchServicesBean.class.getName()).log(Level.INFO, "IndexResponse2:"+response.getIndex()+" "+response.getId()+" "+response.getVersion()+" "+response.getMatches());

        } catch (Exception ex) {
            Logger.getLogger(SearchServicesBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }
    private ArrayList<VarDTO_Detailed> vars_waiting_indexing;

    @Override
    public void launchIndexingVarBULKED(VarDTO_Detailed dto) {
        vars_waiting_indexing.add(dto);
    }

    @Override
    public void perform_var_bulk_indexing() {
        try {
            if (vars_waiting_indexing != null) {
                //Client client = node.client();
                BulkRequestBuilder bulkRequest = client.prepareBulk();
                for (VarDTO_Detailed dto : vars_waiting_indexing) {
                    bulkRequest.add(client.prepareIndex(AdminSocioResearchMDB.INDEX_NAME, "sociovar", String.valueOf(dto.getId())).setSource(generateVarJSONDesc(dto)));
                }
                BulkResponse resp = bulkRequest.execute().actionGet();
                System.out.println("Indexed vars count:" + resp.getItems().length + " ,takes time:" + resp.getTook().toString());
                vars_waiting_indexing = null;
            }

        } catch (Exception ex) {
            Logger.getLogger(ES_indexing_Bean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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
                Logger.getLogger(SearchServicesBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return json;
    }


    @Override
    public void init_bulk_indexing() {
        vars_waiting_indexing = new ArrayList<VarDTO_Detailed>(100);
    }
}
