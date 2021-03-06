package com.mresearch.databank.client.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mresearch.databank.shared.ComparativeSearchParamsDTO;
import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitDateDTO;
import com.mresearch.databank.shared.MetaUnitDoubleDTO;
import com.mresearch.databank.shared.MetaUnitEntityItemDTO;
import com.mresearch.databank.shared.MetaUnitIntegerDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedStructureDTO;
import com.mresearch.databank.shared.MetaUnitStringDTO;
import com.mresearch.databank.shared.OrgDTO;
import com.mresearch.databank.shared.ResearchFilesDTO;
import com.mresearch.databank.shared.SocioResearchDTO;
import com.mresearch.databank.shared.SocioResearchFilesDTO;
import com.mresearch.databank.shared.StartupBundleDTO;
import com.mresearch.databank.shared.VarDTO_Detailed;
import com.mresearch.databank.shared.VarDTO_Light;

public interface AdminSocioResearchServiceAsync {

	void updateResearch(SocioResearchDTO research,
			AsyncCallback<SocioResearchDTO> callback);
	void deleteResearch(long id, AsyncCallback<Boolean> callback);
	void parseSPSS(long blobkey, long length, AsyncCallback<Void> callback);
	void addOrgImpl(OrgDTO dto, AsyncCallback<Long> callback);
	void updateResearchGrouped(SocioResearchDTO research,
			AsyncCallback<SocioResearchDTO> callback);
	void generalizeVar(long var_id, ArrayList<Long> gen_var_ids,
			AsyncCallback<VarDTO_Detailed> callback);
	void addSSE(String clas, String kind, String value,
			AsyncCallback<Boolean> callback);
	void updateFileAccessor(long research_id, ResearchFilesDTO dto,
			AsyncCallback<Boolean> callback);
	void addFileToAccessor(long id_research, long id_file, String desc,
			String category, AsyncCallback<Boolean> callback);
	void deleteFileFromAccessor(long id_research, long id_file,
			AsyncCallback<Boolean> callback);
	void updateMetaUnitStructure(MetaUnitDTO dto, AsyncCallback<Void> callback);
	void addMetaUnit(MetaUnitDTO dto, Long parent_unit_id,
			AsyncCallback<MetaUnitDTO> callback);
	void addEntityItem(Long entity_id, String value,
			HashMap<String, String> filling, AsyncCallback<Void> callback);
	void deleteMetaUnit(Long id, Long unit_parent_id,
			AsyncCallback<Void> callback);
	void deleteEntityItem(Long id, Long entity_id, AsyncCallback<Void> callback);
	void editEntityItem(Long entity_id, String value,
			HashMap<String, String> filling, AsyncCallback<Void> callback);
	void addSubEntityItem(Long paramLong, String paramString,
			HashMap<String, String> paramHashMap, AsyncCallback<Void> callback);
	void updateMetaUnitEntityItemLinks(
			MetaUnitEntityItemDTO paramMetaUnitEntityItemDTO1,
			MetaUnitEntityItemDTO paramMetaUnitEntityItemDTO2,
			AsyncCallback<Void> callback);
	void updateMetaUnitEntityItemLinks(
			MetaUnitEntityItemDTO paramMetaUnitEntityItemDTO,
			AsyncCallback<Void> callback);
	
	void updateMetaUnitEntityItemLinks(Long item_id,
			ArrayList<Long> tagged_ids, String identifier,
			AsyncCallback<Void> callback);
	void updateVar(VarDTO_Detailed var, AsyncCallback<Void> callback);
	void setStartupContent(StartupBundleDTO dto, AsyncCallback<Void> callback);
	void findVarsLikeThis(Long var_id, ComparativeSearchParamsDTO params,
			AsyncCallback<ArrayList<VarDTO_Detailed>> callback);
	void generalizeVars(ArrayList<Long> gen_var_ids,
			AsyncCallback<Void> callback);
	
	
}
