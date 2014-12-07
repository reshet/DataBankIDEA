/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.service;


import com.mplatforma.amr.entity.*;
import com.mplatforma.amr.service.remote.UserSocioResearchBeanRemote;
import com.mresearch.databank.shared.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
@Stateless(mappedName="UserSocioResearchRemoteBean",name="UserSocioResearchRemoteBean")
public class UserSocioResearchSessionBean implements UserSocioResearchBeanRemote{
    
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

    public static Logger logger = Logger.getLogger(UserSocioResearchSessionBean.class.getName());

    @PersistenceContext
    private EntityManager em; 
    //@EJB 
    //private UserAccountBeanRemote useracc; 
    @Override
    public SocioResearchDTO getResearch(long id) {
        
        SocioResearch res = em.find(SocioResearch.class, id);
        return res.toDTO();
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public VarDTO getVar(long id,UserAccountDTO dto,UserHistoryDTO hist_dto) {
        Var v = em.find(Var.class, id);
        v.setEM(em);
        return v.toDTO(dto,hist_dto,em);
    }
    public VarDTO_Light getVarLight(long id) {
        Var v = em.find(Var.class, id);
        if(v != null){
            v.setEM(em);
            return v.toDTO_Light();
        } else return null;
    }
    @Override
    public VarDTO_Detailed getVarDetailed(long id,UserAccountDTO dto,UserHistoryDTO hist_dto) {
         Var v = em.find(Var.class, id);
         if(v != null)
         {
            v.setEM(em);
            if(dto!= null && dto.getId()!= 0 && dto.getCurrent_research() == 0)
            {
                dto.setCurrent_research(v.getResearch_id());
            }
           return v.toDTO_Detailed(dto,hist_dto,em);
         }else return null;
     }

    
    @Override
    public ArrayList<SocioResearchDTO_Light> getResearchSummaries() {
   //     ArrayList<SocioResearchDTO_Light> list = new ArrayList<SocioResearchDTO_Light>();
        
   // SocioResearch dsResearch, detached;
//	try {
//		TypedQuery<SocioResearch> q = em.createQuery("SELECT x FROM SocioResearch x", SocioResearch.class);
//		List<SocioResearch> res = (List<SocioResearch>)q.getResultList();
//		for(SocioResearch research:res)
//		{
//			list.add(research.toLightDTO());
//		}
//	} finally {
//        }
        
        
        return SocioResearch.getResearchsLight(em);
   //     throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    public ArrayList<Long> intersection(ArrayList<Long> first,ArrayList<Long> second)
    {
            ArrayList<Long> first_cl = new ArrayList<Long>();
            for(Long id:second)
            {
                    for(Long id_f:first)
                    {
                            if (id_f.equals(id)) 
                            {
                                    first_cl.add(id_f);
                                    break;
                            }
                    }
            }
            return first_cl;
    }

    public ArrayList<Long> union(ArrayList<Long> first,ArrayList<Long> second)
    {
            ArrayList<Long> first_cl = new ArrayList<Long>();
            for(Long id:first)
            {
                    first_cl.add(id);
            }
            for(Long id:second)
            {
                    if(!first_cl.contains(id))first_cl.add(id);
            }
            return first_cl;
    }
    public ArrayList<Long> getSubFiltered(ArrayList<Long> processed,FilterBaseDTO next_filter)
    {
        //TODO just rewritten from appengine variant. Here we can optimize on multiple filters in one sql statement!!!! 
	ArrayList<Long> ids = processed;
//	if(next_filter instanceof FilterDiapasonDTO)
//	{
//		for(FilterBaseDTO dto_sub:((FilterDiapasonDTO) next_filter).getMulti_dto_proxy().getFilters())
//		{
//			//here we suppose that multifilter is AND-filter (for diapa)
//			ids = getSubFiltered(processed, dto_sub);
//		}
//	}else
	if(next_filter instanceof FilterMultiDTO)
	{
		ArrayList<Long> or_ids = new ArrayList<Long>();
		for(FilterBaseDTO dto_sub:((FilterMultiDTO) next_filter).getFilters())
		{
			//here we suppose that multifilter is OR-filter
			ArrayList<Long > curr_ids = getSubFiltered(processed, dto_sub);
			or_ids = union(or_ids,curr_ids);
		}
		if (((FilterMultiDTO) next_filter).getFilters().size()>0)ids = or_ids;
	} else
	{
		String filt = next_filter.getFilter();
		if (filt!=null && !filt.equals(""))
		{
			//Query q = pm.newQuery(SocioResearch.class);
                        String q_str = "SELECT x FROM SocioResearch AS x WHERE ";
                        if(next_filter.getTarget_class_name().equals("SocioResearch"))
			{
                            q_str+=filt;
                            //q.setFilter(filt);
			}
                        TypedQuery<SocioResearch> tq = em.createQuery(q_str, SocioResearch.class);
                        ArrayList<Long> current_ids = new ArrayList<Long>();
			List<SocioResearch> res = tq.getResultList();
			for(SocioResearch research:res)
			{
				current_ids.add(research.getID());
			}
			ids = intersection(ids, current_ids);
		}
	}
	return ids;
    }    
    
    @Override
    public ArrayList<SocioResearchDTO_Light> getResearchSummaries(List<FilterBaseDTO> filters) {
//        ArrayList<SocioResearchDTO_Light> list = new ArrayList<SocioResearchDTO_Light>();
////	ArrayList<Long> all_ids = new ArrayList<Long>();
////	ArrayList<SocioResearchDTO_Light> all_dtos = getResearchSummaries();
////	for(SocioResearchDTO_Light dto: all_dtos)
////	{
////		all_ids.add(dto.getId());
////	}
////	try {
////            for(FilterBaseDTO dto:filters)
////            {
////                    all_ids = getSubFiltered(all_ids, dto);
////            }
////            list = getResearchDTOs(all_ids);
////	} finally {
////        }
//	return list;        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<VarDTO_Light> getResearchVarsSummaries(long research_id) {
// Variant 1
//        ArrayList<VarDTO_Light> list = new ArrayList<VarDTO_Light>();
//        SocioResearch dsResearch,detached;
//	try {
//		 dsResearch = em.find(SocioResearch.class, research_id);
//	     ArrayList<Long> var_ids = dsResearch.getVar_ids();
//	     for(Long var_id:var_ids)
//	     {
//	    	 Var var = em.find(Var.class,var_id);
//	    	 list.add(var.toDTO_Light());
//	     }
//	} finally 
//        {
//        }
        // Variant 2
//        TypedQuery<Var> q = em.createQuery("SELECT x FROM Var x WHERE x.research_id = :id ORDER BY x.id", Var.class);
//        q.setParameter("id", research_id);
//        List<Var> l = q.getResultList();
//        for(Var v:l)
//        {
//            list.add(v.toDTO_Light());
//        }
        //Variant 3
	return Var.getResearchVarsLight(em, research_id);
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Double> get2DDistribution(long var_id1, long var_id2,UserAccountDTO dto,UserHistoryDTO hist_dto) {
        return Var.calc2DDistribution(var_id1, var_id2, dto,hist_dto, em);
    }

    @Override
    public ResearchFilesDTO getResearchFiles(long research_id) {
	    SocioResearch dsResearch;
	    ResearchFilesDTO dto;
	    try {
	      dsResearch = em.find(SocioResearch.class, research_id);
	      dto = dsResearch.toFilesDTO(em);
	    } finally {
	    }
	 return dto;
    }

    @Override
    public SocioResearchFilesDTO getResearchFilesInCategory(long research_id, String category) {
        // WTF???????????//
        SocioResearch dsResearch, detached;
        ResearchFilesDTO dto;
        SocioResearchFilesDTO dto2;
        try {
          dsResearch = em.find(SocioResearch.class, research_id);
          //dsResearch.addFile(id_file, desc);
          dto = dsResearch.toFilesDTO(em);
          dto2 = new SocioResearchFilesDTO();
          dto2.setFiles_ids(dto.getFileIds(category));
          dto2.setFiles_descs(dto.getFileNames(category));
        } finally {
        }
        return dto2;
    }

    @Override
    public ArrayList<SSE_DTO> getSSEs(String clas, String kind) {
	ArrayList<SSE_DTO> list = new ArrayList<SSE_DTO>();
	/*try {
		List<SingleStringEntity> res = SingleStringEntity.getMatching(em, clas, kind);
		for(SingleStringEntity ent:res)
		{
			list.add(ent.toDTO());
		}

	} finally {
        }*/
	return list;
    }

    @Override
    public ArrayList<OrgDTO> getOrgList() {
        ArrayList<OrgDTO> list = new ArrayList<OrgDTO>();
  	try {
		TypedQuery<Organization> q = em.createQuery("SELECT x FROM Organization x", Organization.class);
		List<Organization> res = (List<Organization>)q.getResultList();
		for(Organization org:res)
		{
			list.add(org.toDTO());
		}
	} finally {
        }
        return list;
    }

    @Override
    public ArrayList<SocioResearchDTO_Light> getResearchDTOs(ArrayList<Long> ids) {
//       ArrayList<SocioResearchDTO> arr = new ArrayList<SocioResearchDTO>();
//	if (ids != null)
//		for(Long key:ids)
//		{
//			SocioResearchDTO dto = getResearch(key);
//			arr.add(dto);
//		}
       
       
	return SocioResearch.getResearchsLightDTOs(em, ids);
    }
     @Override
    public ArrayList<VarDTO_Light> getVarDTOs(ArrayList<Long> ids) {
//       ArrayList<VarDTO_Light> arr = new ArrayList<VarDTO_Light>();
//	if (ids != null)
//		for(Long key:ids)
//		{
//			VarDTO_Light dto = getVarLight(key);
//			arr.add(dto);
//		}
	return Var.getResearchVarsLightDTOs(em, ids);
    }
    @Override
     public ArrayList<VarDTO_Detailed> getVarDTOsAsOrdered(ArrayList<Long> ids) {
       ArrayList<VarDTO_Detailed> arr = new ArrayList<VarDTO_Detailed>();
	if (ids != null)
		for(Long key:ids)
		{
			VarDTO_Detailed dto = getVarDetailed(key,null,null);
			if(dto!=null)arr.add(dto);
		}
        return arr;
	//return Var.getResearchVarsLightDTOsUnordered(em, ids);
    }
    
    //private Node node;
    private Client client;
    @EJB
    ESClientBean clientbean;
    //@Resource(name="indexname")
    public  String INDEX_NAME = "databankkiis";

    @PostConstruct
    private void init()
    {
        client = clientbean.getClient();
        //node = nodeBuilder().clusterName("elasticsearch_"+INDEX_NAME+"_Prj_Cluster").client(true).node();
    }
    
    @PreDestroy
    private void release()
    {
      //node.close();
    }
    @Override
    public String doIndexSearch(String json_query,String [] types_to_search)
    {
        return doIndexSearchMaxResults(json_query, types_to_search, 100);
    }
    @Override
    public String doIndexSearchMaxResults(String json_query,String [] types_to_search,int max_results)
    {
             
        
        try {
//            QueryBuilder qb = termQuery("value", "superradio");
//            QueryBuilder qb1 = termQuery("name", "kimchy");
//
//            QueryBuilder qb2 = boolQuery()
//                        .must(termQuery("content", "test1"))
//                        .must(termQuery("content", "test4"))
//                        .mustNot(termQuery("content", "test2"))
//                        .should(termQuery("content", "test3"));
//
//            QueryBuilder qb3 = filteredQuery(
//                termQuery("name.first", "shay"), 
//                rangeFilter("age")
//                    .from(23)
//                    .to(54)
//                    .includeLower(true)
//                    .includeUpper(false)
//                );
    
            
            //Client client = node.client();
//            Settings settings = ImmutableSettings.settingsBuilder()
//                .put("cluster.name", "elasticsearch_DataBankPrj_Cluster").build();
//             Client client = new TransportClient(settings)
//                .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));

            
            SearchResponse response = client.prepareSearch("databankkiis").setTypes(types_to_search)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(json_query)
                .setExplain(true)
                    .addSort(new ScoreSortBuilder().order(SortOrder.DESC))
                //.setHighlighterEncoder("\"fields\" : {\"_all\" : {}}")
                .addHighlightedField("_all")
                 //   .setS
                 .setSize(max_results)
                 .execute()
                .actionGet();
            
//            String ss =  client.prepareSearch("databank").setTypes(types_to_search)
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(json_query)
//                .setExplain(true)
//                //.setHighlighterEncoder("\"fields\" : {\"_all\" : {}}")
//                .addHighlightedField("_all")
//                    .toString();
            int b = 2;
           // String s = response.hits().getAt(0).getHighlightFields().toString();
            
            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "SearchQuery:"+json_query);
            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "SearchAnswer:"+response.getHits().hits().toString());
            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "SearchAnswerDet:"+response.toString());
            
            return response.toString();
            
        }
        catch(Exception ex)
        {
           // ex.printStackTrace();
            Logger.getLogger(UserSocioResearchSessionBean.class.getName()).log(Level.INFO, "SearchAnswerException:{0}", ex.getMessage());
            return "Error: "+ ex.getMessage();
        }
        finally{
          
        }
   
    }
    
    @Override
    public MetaUnitMultivaluedEntityDTO getDatabankStructure(String db_name) {
        DatabankStructure db;
        try
        {
            db = em.find(DatabankStructure.class, db_name);
            MetaUnitMultivaluedEntity root = db.getRoot();
            MetaUnitMultivaluedEntityDTO dto = toMultivaluedEntityDTO(root,false);
            return dto;
        }
        finally
        {
        }
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private MetaUnitMultivaluedStructureDTO toMultivaluedStructureDTO(MetaUnitMultivaluedStructure m)
    {
        MetaUnitMultivaluedStructureDTO dto = new MetaUnitMultivaluedStructureDTO();
        try
        {
            dto.setId(m.getId());
            dto.setDesc(m.getDescription());
            dto.setUnique_name(m.getUnique_name());
            dto.setIsCatalogizable(m.getIsCatalogizable()!=0);
            dto.setIsSplittingEnabled(m.getIsSplittingEnabled()!=0);
            
            ArrayList<Long> tagged_ent = new ArrayList<Long>();
            if(m.getTagged_entities()!=null)
            for(Long ent:m.getTagged_entities())
            {
                tagged_ent.add(ent);
            }
            dto.setTagged_entities(tagged_ent);
            ArrayList<MetaUnitDTO> sub_meta_units_dtos = new ArrayList<MetaUnitDTO>();
            if(m.getSub_meta_units()!= null)
            for(MetaUnit unit:m.getSub_meta_units())
            {
                if(unit != null)
                {
                    if(unit instanceof MetaUnitMultivaluedStructure)
                    {
                        MetaUnitDTO dt;
                        //DONE FOR SPLIITING DOWNLOAD OF DEEP TREE
                        if(((MetaUnitMultivalued)unit).getIsSplittingEnabled()!=0)
                        {
                            dt = new MetaUnitMultivaluedStructureDTO(unit.getId(),unit.getUnique_name(),unit.getDescription());
                            ((MetaUnitMultivaluedDTO)dt).setIsCatalogizable(((MetaUnitMultivalued)unit).getIsCatalogizable()!=0);
                            ((MetaUnitMultivaluedDTO)dt).setIsSplittingEnabled(((MetaUnitMultivalued)unit).getIsSplittingEnabled()!=0);
                        }
                        else
                        {
                            dt = toMultivaluedStructureDTO((MetaUnitMultivaluedStructure)unit);
                        }
                        if(dt!=null)
                        sub_meta_units_dtos.add(dt);
                    }
                    else if(unit instanceof MetaUnitMultivaluedEntity)
                    {
                        MetaUnitDTO dt;
                        //DONE FOR SPLIITING DOWNLOAD OF DEEP TREE
                        if(((MetaUnitMultivalued)unit).getIsSplittingEnabled()!=0)
                        {
                            dt = new MetaUnitMultivaluedEntityDTO(unit.getId(),unit.getUnique_name(),unit.getDescription());
                            ((MetaUnitMultivaluedDTO)dt).setIsCatalogizable(((MetaUnitMultivalued)unit).getIsCatalogizable()!=0);
                            ((MetaUnitMultivaluedDTO)dt).setIsSplittingEnabled(((MetaUnitMultivalued)unit).getIsSplittingEnabled()!=0);
                            ((MetaUnitMultivaluedEntityDTO)dt).setIsMultiselected(((MetaUnitMultivaluedEntity)unit).getIsMultiSelected()!=0);
                            Collection<MetaUnitEntityItem> items = ((MetaUnitMultivaluedEntity)unit).getItems();
                            ArrayList<String> names = new ArrayList<String>();
                            ArrayList<Long> ids = new ArrayList<Long>();
                            for(MetaUnitEntityItem item:items)
                            {
                                names.add(item.getValue());
                                ids.add(item.getId());
                            }
                            ((MetaUnitMultivaluedEntityDTO)dt).setItem_ids(ids);
                            ((MetaUnitMultivaluedEntityDTO)dt).setItem_names(names);
                        }
                        else
                        {
                            dt = toMultivaluedEntityDTO((MetaUnitMultivaluedEntity)unit,false);
                        }
                        if(dt!=null)
                        sub_meta_units_dtos.add(dt);
                    }
                    else        
                    {
                        sub_meta_units_dtos.add(unit.toDTO());
                    }
                }
            }
            dto.setSub_meta_units(sub_meta_units_dtos);

            return dto;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }finally
        {
            return dto;
        }
    }
    
    private MetaUnitEntityItemDTO toEntityItemDTO(MetaUnitEntityItem item)
    {
        MetaUnitEntityItemDTO dto = new MetaUnitEntityItemDTO(item.getId(), item.getValue(), item.getMapped_values());
        ArrayList<Long> ent_ids = new ArrayList<Long>();
        for(Long id:item.getTagged_entities_ids())
        {
            ent_ids.add(id);
        }
        
        ArrayList<String> ent_idents = new ArrayList<String>();
        for(String ident:item.getTagged_entities_identifiers())
        {
            ent_idents.add(ident);
        }
        dto.setTagged_entities_ids(ent_ids);
        dto.setTagged_entities_identifiers(ent_idents);
        
        ArrayList<MetaUnitEntityItemDTO> subitems = new ArrayList<MetaUnitEntityItemDTO>();
        if(item.getSubitems()!=null)
            for(MetaUnitEntityItem it:item.getSubitems())
            {
                subitems.add(toEntityItemDTO(it));
            }
        dto.setSubitems(subitems);
        return dto;
    }
        private MetaUnitEntityItemDTO toEntityItemDTO_Light(MetaUnitEntityItem item)
    {
        MetaUnitEntityItemDTO dto = new MetaUnitEntityItemDTO(item.getId(), item.getValue(), item.getMapped_values());
        ArrayList<Long> ent_ids = new ArrayList<Long>();
        if(item.getTagged_entities_ids()!=null)
        for(Long id:item.getTagged_entities_ids())
        {
            ent_ids.add(id);
        }
        
        ArrayList<String> ent_idents = new ArrayList<String>();
         if(item.getTagged_entities_identifiers()!=null)
        for(String ident:item.getTagged_entities_identifiers())
        {
            ent_idents.add(ident);
        }
        dto.setTagged_entities_ids(ent_ids);
        dto.setTagged_entities_identifiers(ent_idents);
        
        ArrayList<MetaUnitEntityItemDTO> subitems = new ArrayList<MetaUnitEntityItemDTO>();
        dto.setSubitems(subitems);
        return dto;
    }
    private ArrayList<MetaUnitEntityItemDTO> toEntitySubitemsDTOs(MetaUnitEntityItem item)
    {
        ArrayList<MetaUnitEntityItemDTO> subitems = new ArrayList<MetaUnitEntityItemDTO>();
        if(item.getSubitems()!=null)
            for(MetaUnitEntityItem it:item.getSubitems())
            {
                subitems.add(toEntityItemDTO_Light(it));
            }
        return subitems;
    }    
    
    private void getRecursiveItemsFlattened(ArrayList<String> names,ArrayList<Long> ids,MetaUnitEntityItem ite,String shift)
    {
        if(ite.getSubitems()!=null && ite.getSubitems().size()>0)
        for(MetaUnitEntityItem item:ite.getSubitems())
        {
            names.add(shift+item.getValue());
            ids.add(item.getId());
            getRecursiveItemsFlattened(names, ids, item,". "+shift);
        }
    }
            
    
    private MetaUnitMultivaluedEntityDTO toMultivaluedEntityDTO(MetaUnitMultivaluedEntity m,boolean flattened_items)
    {
        MetaUnitMultivaluedEntityDTO dto = new MetaUnitMultivaluedEntityDTO();
        try
        {
            dto.setId(m.getId());
            dto.setDesc(m.getDescription());
            dto.setUnique_name(m.getUnique_name());
            dto.setIsCatalogizable(m.getIsCatalogizable()!=0);
            dto.setIsSplittingEnabled(m.getIsSplittingEnabled()!=0);
            dto.setIsMultiselected(m.getIsMultiSelected()!=0);
            Collection<MetaUnitEntityItem> items = m.getItems();
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<Long> ids = new ArrayList<Long>();
            for(MetaUnitEntityItem item:items)
            {
                names.add(item.getValue());
                ids.add(item.getId());
                if(flattened_items)getRecursiveItemsFlattened(names, ids, item, ". ");
//                {
//                    for(MetaUnitEntityItem it:item.getSubitems())
//                    {
//                        names.add(".  "+it.getValue());
//                        ids.add(it.getId());
//                    }
//                }
            }
            dto.setItem_ids(ids);
            dto.setItem_names(names);
            ArrayList<Long> tagged_ent = new ArrayList<Long>();
            if(m.getTagged_entities()!=null)
            for(Long ent:m.getTagged_entities())
            {
                tagged_ent.add(ent);
            }
            dto.setTagged_entities(tagged_ent);
            ArrayList<MetaUnitDTO> sub_meta_units_dtos = new ArrayList<MetaUnitDTO>();
            if(m.getSub_meta_units()!= null)
            for(MetaUnit unit:m.getSub_meta_units())
            {
                if(unit != null)
                {
                    if(unit instanceof MetaUnitMultivaluedEntity)
                    {
                        MetaUnitDTO dt;
                        //DONE FOR SPLIITING DOWNLOAD OF DEEP TREE
                        if(((MetaUnitMultivalued)unit).getIsSplittingEnabled()!=0)
                        {
                            dt = new MetaUnitMultivaluedEntityDTO(unit.getId(),unit.getUnique_name(),unit.getDescription());
                            ((MetaUnitMultivaluedDTO)dt).setIsCatalogizable(((MetaUnitMultivalued)unit).getIsCatalogizable()!=0);
                            ((MetaUnitMultivaluedDTO)dt).setIsSplittingEnabled(((MetaUnitMultivalued)unit).getIsSplittingEnabled()!=0);
                            ((MetaUnitMultivaluedEntityDTO)dt).setIsMultiselected(((MetaUnitMultivaluedEntity)unit).getIsMultiSelected()!=0);
                             Collection<MetaUnitEntityItem> items2 = ((MetaUnitMultivaluedEntity)unit).getItems();
                            ArrayList<String> names2 = new ArrayList<String>();
                            ArrayList<Long> ids2 = new ArrayList<Long>();
                            for(MetaUnitEntityItem item:items2)
                            {
                                names2.add(item.getValue());
                                ids2.add(item.getId());
                            }
                            ((MetaUnitMultivaluedEntityDTO)dt).setItem_ids(ids2);
                            ((MetaUnitMultivaluedEntityDTO)dt).setItem_names(names2);

                            
                        }
                        else
                        {
                            dt = toMultivaluedEntityDTO((MetaUnitMultivaluedEntity)unit,flattened_items);
                        }
                        if(dt!=null)
                        sub_meta_units_dtos.add(dt);
                    }else
                    if(unit instanceof MetaUnitMultivaluedStructure)
                    {
                        MetaUnitDTO dt;
                        //DONE FOR SPLIITING DOWNLOAD OF DEEP TREE
                        if(((MetaUnitMultivalued)unit).getIsSplittingEnabled()!=0)
                        {
                            dt = new MetaUnitMultivaluedStructureDTO(unit.getId(),unit.getUnique_name(),unit.getDescription());
                            ((MetaUnitMultivaluedDTO)dt).setIsCatalogizable(((MetaUnitMultivalued)unit).getIsCatalogizable()!=0);
                            ((MetaUnitMultivaluedDTO)dt).setIsSplittingEnabled(((MetaUnitMultivalued)unit).getIsSplittingEnabled()!=0);
                        }
                        else
                        {
                            dt = toMultivaluedStructureDTO((MetaUnitMultivaluedStructure)unit);
                        }
                        if(dt!=null)
                        sub_meta_units_dtos.add(dt);
                    }else
                    {
                        sub_meta_units_dtos.add(unit.toDTO());
                    }
                }
            }
            dto.setSub_meta_units(sub_meta_units_dtos);

            return dto;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }finally
        {
            return dto;
        }
    }

    @Override
    public MetaUnitMultivaluedStructureDTO getMetaUnitMultivaluedStructureDTO(long id) {
      MetaUnitMultivaluedStructure unit;
        try
        {
            unit = em.find(MetaUnitMultivaluedStructure.class, id);
            MetaUnitMultivaluedStructureDTO dto = toMultivaluedStructureDTO(unit);
            return dto;
        }
        finally
        {
        }
    }

    @Override
    public HashMap<String, String> getEntityItem(Long id) {
        MetaUnitEntityItem item = em.find(MetaUnitEntityItem.class,id);
        return item.getMapped_values();
    }

    @Override
    public MetaUnitMultivaluedEntityDTO getMetaUnitMultivaluedEntityDTO(long id) {
        MetaUnitMultivaluedEntity unit = em.find(MetaUnitMultivaluedEntity.class,id);
        return toMultivaluedEntityDTO(unit,false);
    }

    @Override
    public ArrayList<String> getEntityItemSubitemsNames(Long id_item) {
        MetaUnitEntityItem item = em.find(MetaUnitEntityItem.class,id_item);
        Collection<MetaUnitEntityItem> items = item.getSubitems();
        ArrayList<String> arr = new ArrayList<String>();
        for(MetaUnitEntityItem it:items)
        {
            arr.add(it.getValue());
        }
        return arr;
    }

    @Override
    public ArrayList<Long> getEntityItemSubitemsIDs(Long id_item) {
        MetaUnitEntityItem item = em.find(MetaUnitEntityItem.class,id_item);
        Collection<MetaUnitEntityItem> items = item.getSubitems();
        ArrayList<Long> arr = new ArrayList<Long>();
        for(MetaUnitEntityItem it:items)
        {
            arr.add(it.getId());
        }
        return arr;
    }

    @Override
    public ArrayList<Long> getEntityItemTaggedEntitiesIDs(Long id_item) {
        MetaUnitEntityItem item = em.find(MetaUnitEntityItem.class,id_item);
        ArrayList<Long> arr = new ArrayList<Long>();
        if(item != null)
        {
            List<Long> ids = item.getTagged_entities_ids();
            if(ids != null)
            for(Long id:ids)
            {
                arr.add(id);
            }
        }
        
        return arr;
    }

    @Override
    public ArrayList<String> getEntityItemTaggedEntitiesIdentifiers(Long id_item) {
        MetaUnitEntityItem item = em.find(MetaUnitEntityItem.class,id_item);
        List<String> ids = item.getTagged_entities_identifiers();
        ArrayList<String> arr = new ArrayList<String>();
        if(ids !=null)
        for(String id:ids)
        {
            arr.add(id);
        }
        return arr;
    }
    
    @Override
    public ArrayList<Long> getEntityItemTaggedEntitiesIDs(Long id_item,String identif) {
        MetaUnitEntityItem item = em.find(MetaUnitEntityItem.class,id_item);
        List<Long> ids = item.getTagged_entities_ids();
        List<String> idents = item.getTagged_entities_identifiers();
        ArrayList<Long> arr = new ArrayList<Long>();
        int i = 0;
        if(ids !=null)
        for(Long id:ids)
        {
            if(idents.get(i).equals(identif))arr.add(id);
            i++;
        }
        return arr;
    }
    
    @Override
    public MetaUnitEntityItemDTO getEntityItemDTO(Long id) {
        MetaUnitEntityItem p_item = em.find(MetaUnitEntityItem.class, id);
        MetaUnitEntityItemDTO dto = toEntityItemDTO_Light(p_item);
        logger.log(Level.INFO, "GetCatalogLinks: " + dto.toString());
        return dto;
    }

    @Override
    public ArrayList<MetaUnitEntityItemDTO> getEntityItemSubitemsDTOs(Long id) {
         MetaUnitEntityItem p_item = em.find(MetaUnitEntityItem.class, id);
        return toEntitySubitemsDTOs(p_item);
    }

    @Override
    public MetaUnitMultivaluedEntityDTO getMetaUnitMultivaluedEntityDTO_FlattenedItems(long id) {
        MetaUnitMultivaluedEntity unit = em.find(MetaUnitMultivaluedEntity.class,id);
        return toMultivaluedEntityDTO(unit,true);
    }

    @Override
    public ArrayList<VarDTO_Research> getVarsResearchNames(ArrayList<Long> keys) {
       ArrayList<VarDTO_Research> map = new ArrayList<VarDTO_Research>();
       for(Long k:keys)
       {
            Var v = em.find(Var.class, k);
            if(v!=null)
            {
                    v.setEM(em);
                    VarDTO_Detailed dto =  v.toDTO_Detailed(null,null,em);   
                    map.add(new VarDTO_Research(dto.getId(),dto.getResearch_id().intValue(), dto.getResearch_name()));
            }
       }
       return map;
    }

    @Override
    public ArrayList<VarDTO_Light> getResearchVarsWeightCandidates(long research_id) {
        return Var.getResearchVarsWeightCandidates(em, research_id);
    }

   

    
}
