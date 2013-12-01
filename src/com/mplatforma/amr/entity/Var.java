package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;
//import org.apache.lucene.util.fst.PairOutputs;

@Entity
@NamedQueries({
    @NamedQuery(name = "Var.getResearchVarsLight", query = "SELECT NEW com.mresearch.databank.shared.VarDTO_Light(x.id, x.code, x.label) FROM Var x WHERE x.research_id = :id ORDER BY x.id"),
    @NamedQuery(name = "Var.getResearchVarsWeightCandidates", query = "SELECT NEW com.mresearch.databank.shared.VarDTO_Light(x.id, x.code, x.label) FROM Var x WHERE x.research_id = :id AND x.var_type = 'REAL_VAR_TYPE' ORDER BY x.id"),
    @NamedQuery(name = "Var.getResearchVarsLightIN", query = "SELECT NEW com.mresearch.databank.shared.VarDTO_Light(x.id, x.code, x.label) FROM Var x WHERE x.id IN :idlist ORDER BY x.id"),
    @NamedQuery(name = "Var.getResearchVarsLightIN_unordered", query = "SELECT NEW com.mresearch.databank.shared.VarDTO_Light(x.id, x.code, x.label) FROM Var x WHERE x.id IN :idlist"),
    @NamedQuery(name = "Var.deleteList", query = "DELETE FROM Var x WHERE x.research_id = :res_id"),
    @NamedQuery(name = "Var.deleteAnalisys", query = "DELETE FROM UserMassiveLocalAnalisys x WHERE x.var_involved_first.id IN (SELECT v.id FROM Var v WHERE v.research_id = :res_id) OR x.var_involved_second.id IN (SELECT v2.id FROM Var v2 WHERE v2.research_id = :res_id)"),
    @NamedQuery(name = "Var.getIDsList", query = "SELECT x.id FROM Var x WHERE x.research_id = :res_id ORDER BY x.id")
})
public class Var {

    @Transient
    private EntityManager em;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long research_id;
    private String code;
    @Column(columnDefinition = "TEXT")
    private String label;
    private String missing1,missing2,missing3;
    private String code_schema_id;
    private String var_type;

    @ElementCollection private List<Double> v_label_codes;
    @ElementCollection private List<String> v_label_values;
    @ElementCollection private List<Long> generalized_var_ids  = new ArrayList<Long>();
    @ElementCollection private List<Double> cortage = new ArrayList<Double>();
    @ElementCollection private List<String> cortage_string  = new ArrayList<String>();
    @OneToOne(cascade = CascadeType.ALL)
    private MetaUnitEntityItem entity_item;
    @ElementCollection private Map<String, String> filling = new HashMap<String, String>();

    public Map<String, String> getFilling() {
        return filling;
    }

    public void setFilling(HashMap<String, String> filling) {
        this.filling = filling;
    }

    public Var() {
    }

    public void setEM(EntityManager em) {
        this.em = em;
    }

    public static ArrayList<VarDTO_Light> getResearchVarsLight(EntityManager em, long research_id) {

        TypedQuery<VarDTO_Light> q = em.createNamedQuery("Var.getResearchVarsLight", VarDTO_Light.class);
        q.setParameter("id", research_id);
        List<VarDTO_Light> l = q.getResultList();
        return new ArrayList<VarDTO_Light>(l);
    }
        public static ArrayList<VarDTO_Light> getResearchVarsWeightCandidates(EntityManager em, long research_id) {

        TypedQuery<VarDTO_Light> q = em.createNamedQuery("Var.getResearchVarsWeightCandidates", VarDTO_Light.class);
        q.setParameter("id", research_id);
        List<VarDTO_Light> l = q.getResultList();
        return new ArrayList<VarDTO_Light>(l);
    }

    public static ArrayList<VarDTO_Light> getResearchVarsLightDTOs(EntityManager em, ArrayList<Long> ids) {

        TypedQuery<VarDTO_Light> q = em.createNamedQuery("Var.getResearchVarsLightIN", VarDTO_Light.class);
        q.setParameter("idlist", ids);
        List<VarDTO_Light> l = q.getResultList();
        return new ArrayList<VarDTO_Light>(l);
    }
    public static ArrayList<VarDTO_Light> getResearchVarsLightDTOsUnordered(EntityManager em, ArrayList<Long> ids) {

        TypedQuery<VarDTO_Light> q = em.createNamedQuery("Var.getResearchVarsLightIN_unordered", VarDTO_Light.class);
        q.setParameter("idlist", ids);
        //q.set
        List<VarDTO_Light> l = q.getResultList();
        return new ArrayList<VarDTO_Light>(l);
    }
    public static ArrayList<Long> getResearchVarsIDs(EntityManager em, Long id) {

        TypedQuery<Long> q = em.createNamedQuery("Var.getIDsList", Long.class);
        q.setParameter("res_id", id);
        List<Long> l = q.getResultList();
        return new ArrayList<Long>(l);
    }

    public static int deleteResearchVars(EntityManager em, Long id) {

        Query q_an = em.createNamedQuery("Var.deleteAnalisys");
        q_an.setParameter("res_id", id);
        q_an.executeUpdate();
        
        Query q = em.createNamedQuery("Var.deleteList");
        q.setParameter("res_id", id);
        return q.executeUpdate();
        //List<VarDTO_Light> l = q.getResultList();
        //return new ArrayList<VarDTO_Light>(l);
    }

    public ArrayList<String> getCortage_string() {
        return (ArrayList<String>) cortage_string;
    }

    public void setCortage_string(ArrayList<String> cortage_string) {
        this.cortage_string = cortage_string;
    }

    public long getID() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setCode_schema_id(String code_schema_id) {
        this.code_schema_id = code_schema_id;
    }

    public String getCode_schema_id() {
        return code_schema_id;
    }

    public void setVar_type(String var_type) {
        this.var_type = var_type;
    }

    public String getVar_type() {
        return var_type;
    }

    public VarDTO toDTO() {
        VarDTO dto = new VarDTO();
        dto.setCode(code);
        dto.setLabel(label);
        dto.setId(id);
        if( v_label_codes!=null)dto.setV_label_codes(new ArrayList<Double>( v_label_codes));
        if( v_label_values!=null)dto.setV_label_values(new ArrayList<String>( v_label_values));
        //dto.setDistribution(calcDistributionSimple());
        return dto;
    }

    public VarDTO_Light toDTO_Light() {
        VarDTO_Light dto = new VarDTO_Light();
        dto.setCode(code);
        dto.setLabel(label);
        dto.setId(id);
        return dto;
    }

    public VarDTO toDTO(UserAccountDTO watching_user,UserHistoryDTO hist_dto,EntityManager em) {
        VarDTO dto = new VarDTO();
        dto.setCode(code);
        dto.setLabel(label);
        dto.setId(id);
        if( v_label_codes!=null)dto.setV_label_codes(new ArrayList<Double>(v_label_codes));
        if( v_label_values!=null)dto.setV_label_values(new ArrayList<String>( v_label_values));
        dto.setResearch_id(research_id);
        if (var_type.equals(VarDTO_Detailed.alt_var_type)) {
            if (watching_user != null) {
                calcDistribution(hist_dto,watching_user, dto, em);
            } else {
                PairDistr pd = calcDistributionSimple();
                dto.setDistribution(pd.distr);
                dto.setValid_distribution(pd.valid_distr);
            }
        }
        return dto;
    }

    public void updateFromDTO(VarDTO_Detailed rDTO, EntityManager em) {
        this.em = em;
        if (this.entity_item == null) {
            this.entity_item = new MetaUnitEntityItem(rDTO.getLabel());
        }
        this.entity_item.setMapped_values(rDTO.getFilling());
        //updateEntityRepresent(id_search_repres, name,em);
        //if (vars_tagcloud_created==0)generateVarsTagCloud();
        //this.org_prompter =rDTO.getOrg_prompter();
    }

    public VarDTO_Detailed toDTO_Detailed(UserAccountDTO watching_user, UserHistoryDTO hist_dto, EntityManager em) {
        VarDTO_Detailed dto;
        if (var_type.equals(VarDTO_Detailed.alt_var_type)) {
            dto = new VarDTO_Detailed();
        } else if (var_type.equals(VarDTO_Detailed.real_var_type)) {
            dto = new RealVarDTO_Detailed();
        } else {
            dto = new TextVarDTO_Detailed();
        }

        dto.setCode(code);
        dto.setLabel(label);
        dto.setId(id);
        if( v_label_codes!=null)dto.setV_label_codes(new ArrayList<Double>(v_label_codes));
        if( v_label_values!=null)dto.setV_label_values(new ArrayList<String>(v_label_values));
        if( generalized_var_ids!=null)dto.setGen_vars_ids(new ArrayList<Long>(generalized_var_ids));
        dto.setGen_var_names(getGenVarsNames(generalized_var_ids, em));
        dto.setGen_research_names(getGenResearchesNames(generalized_var_ids, em));
        dto.setGen_research_ids(getGenResearchesIds(generalized_var_ids, em));
        dto.setVar_type(var_type);

        SocioResearch r = em.find(SocioResearch.class, research_id);
        if(r == null) return null;
        dto.setResearch_name(r.getName());
        dto.setResearch_id(research_id);
        if (entity_item != null) {
            dto.setFilling(entity_item.getMapped_values());
        }
        if (r.getEntity_item() != null) {
            dto.setResearch_meta_filling(r.getEntity_item().getMapped_values());
        }

        if (dto instanceof RealVarDTO_Detailed) {
            ((RealVarDTO_Detailed) dto).setFiltered_cortage(new ArrayList<Double>(this.cortage));
            ((RealVarDTO_Detailed) dto).calc_statstics();
            ((RealVarDTO_Detailed) dto).setNumber_of_records(cortage.size());

        } else if (dto instanceof TextVarDTO_Detailed) {
            ((TextVarDTO_Detailed) dto).setFiltered_cortage(new ArrayList<String>(this.cortage_string));
            ((TextVarDTO_Detailed) dto).setNumber_of_records(cortage_string.size());
        } else if (dto instanceof VarDTO_Detailed) {
            if (watching_user != null) {
                PairDistr pd = calcDistribution(hist_dto,watching_user, dto, em);
                dto.setDistribution(pd.distr);
                dto.setValid_distribution(pd.valid_distr);
            } else {
                PairDistr pd = calcDistributionSimple();
                dto.setDistribution(pd.distr);
                dto.setValid_distribution(pd.valid_distr);
            }
        }

        return dto;
    }

    public VarDTO_Detailed toDTO_DetailedNoCalc(EntityManager em) {
        VarDTO_Detailed dto;
        if (var_type.equals(VarDTO_Detailed.alt_var_type)) {
            dto = new VarDTO_Detailed();
        } else if (var_type.equals(VarDTO_Detailed.real_var_type)) {
            dto = new RealVarDTO_Detailed();
        } else {
            dto = new TextVarDTO_Detailed();
        }

        dto.setCode(code);
        dto.setLabel(label);
        dto.setId(id);
        if(v_label_codes!=null)dto.setV_label_codes(new ArrayList<Double>(v_label_codes));
        if(v_label_values!=null)dto.setV_label_values(new ArrayList<String>(v_label_values));
        if(generalized_var_ids!=null)dto.setGen_vars_ids(new ArrayList<Long>(generalized_var_ids));
        dto.setGen_var_names(getGenVarsNames(generalized_var_ids, em));
        dto.setGen_research_names(getGenResearchesNames(generalized_var_ids, em));
        dto.setGen_research_ids(getGenResearchesIds(generalized_var_ids, em));
        dto.setVar_type(var_type);

        SocioResearch r = em.find(SocioResearch.class, research_id);
        dto.setResearch_name(r.getName());
        dto.setResearch_id(research_id);
        if (entity_item != null) {
            dto.setFilling(entity_item.getMapped_values());
        }
        if (r.getEntity_item() != null) {
            dto.setResearch_meta_filling(r.getEntity_item().getMapped_values());
        }

        if (dto instanceof RealVarDTO_Detailed) {
            ((RealVarDTO_Detailed) dto).setFiltered_cortage(new ArrayList<Double>(this.cortage));
            ((RealVarDTO_Detailed) dto).calc_statstics();
            ((RealVarDTO_Detailed) dto).setNumber_of_records(cortage.size());

        } else if (dto instanceof TextVarDTO_Detailed) {
            ((TextVarDTO_Detailed) dto).setFiltered_cortage(new ArrayList<String>(this.cortage_string));
            ((TextVarDTO_Detailed) dto).setNumber_of_records(cortage_string.size());
        }
        return dto;
    }

    private ArrayList<String> getGenVarsNames(List<Long> gen_var_ids, EntityManager em) {
        ArrayList<String> names = new ArrayList<String>();
        if (gen_var_ids != null) {
            try {
                for (Long id : gen_var_ids) {
                    Var var, detached_var;
                    var = em.find(Var.class, id);
                    if (var != null) {
                        //detached_var = pm.detachCopy(var);
                        names.add(var.getLabel());
                        //	return detached_var;
                    }
                }
            } finally {
                ////em.close();
            }
        }

        return names;
    }

    private ArrayList<String> getGenResearchesNames(List<Long> gen_var_ids, EntityManager em) {
        ArrayList<String> names = new ArrayList<String>();
        if (gen_var_ids != null) {
            try {
                for (Long id : gen_var_ids) {
                    Var var;
                    var = em.find(Var.class, id);
                    if (var != null) {
                        SocioResearch research, res_detached;
                        research = em.find(SocioResearch.class, var.getResearch_id());
                        //res_detached = em.detachCopy(research);
                        names.add(research.getName());
                        //	return detached_var;
                    }
                }
            } finally {
                ////em.close();
            }
        }

        return names;
    }

    private ArrayList<Long> getGenResearchesIds(List<Long> gen_var_ids, EntityManager em) {
        ArrayList<Long> ids = new ArrayList<Long>();
        if (gen_var_ids != null) {
            try {
                for (Long id : gen_var_ids) {
                    Var var;
                    var = em.find(Var.class, id);
                    if (var != null) {
                        SocioResearch research, res_detached;
                        research = em.find(SocioResearch.class, var.getResearch_id());
                        ids.add(research.getID());
                        //	return detached_var;
                    }
                }
            } finally {
                ////em.close();
            }
        }

        return ids;
    }
    private boolean isMissingValue(double d){
        String mymis = String.valueOf(d);
        
        return missing1!=null && missing1.equals(mymis) 
               || missing2!=null && missing2.equals(mymis)
                || missing3!=null && missing3.equals(mymis);
        
//        if(missing2.equals("\"\"") && missing3.equals("\"\""))
//        {
//            return missing1.equals(String.valueOf(d));
//        }else{
//            if(missing3.equals("\"\"")){
//                double mis1 = Double.parseDouble(missing1);
//                double mis2 = Double.parseDouble(missing2);
//                return (mis1 <= d && d <= mis2);
//            }
//            else{
//               return missing3.equals(String.valueOf(d));
//            }
//        }
    }
    private PairDistr calcDistributionSimple() {
        //PersistenceManager pm = PMF.get().getPersistenceManager();
        //UserAccount account = 

        //if(v_label_codes = null) v_label_codes
        
        //missing values stored in tail of list only in dist.
        ArrayList<Double> distr = new ArrayList<Double>(v_label_codes.size());
        ArrayList<Double> valid_distr = new ArrayList<Double>(v_label_codes.size());
        //Double missAccum = 0.0;
        
        for (int i = 0; i < v_label_codes.size(); i++) {
            distr.add(new Double(0));
            valid_distr.add(new Double(0));
        }
        for (Double value : cortage) {
            if (!value.equals(Double.NaN)) {
                int setIndex = v_label_codes.indexOf(value);
                if (setIndex >= 0) {
                    Double val = distr.get(setIndex);
                    distr.set(setIndex, val + 1);
                    if(!isMissingValue(value))
                    {
                        Double val_v = valid_distr.get(setIndex);
                        valid_distr.set(setIndex, val_v + 1);
                    }
                }
                
            }
        }
        return new PairDistr(distr, valid_distr);
    }

    private Var findVar(String code) {
        //suppose codes unique
        Var var, detached_var;
        try {
            Query q = em.createQuery("select x from Var x where x.code = :code");
            q.setParameter("code", code);
            var = (Var) q.getSingleResult();
            if (var != null) {
                //detached_var = pm.detachCopy(var);
                return var;
            }
        } finally {
            ////em.close();
        }
        return null;
    }

    private Var getVar(long id, EntityManager em) {
        //suppose codes unique
        Var var, detached_var;
        try {
            var = em.find(Var.class, id);
            if (var != null) {
                //detached_var = pm.detachCopy(var);
                em.detach(var);
                return var;
            }
        } finally {
            ////em.close();
        }
        return null;
    }

    public ArrayList<VarDTO_Light> getResearchVarsSummaries(Long research_id, EntityManager em) {
        ArrayList<VarDTO_Light> list = new ArrayList<VarDTO_Light>();
        SocioResearch dsResearch;
        try {
            dsResearch = em.find(SocioResearch.class, research_id);
            //detached = pm.detachCopy(dsResearch);
            List<Long> var_ids = dsResearch.getVar_ids();
            for (Long var_id : var_ids) {
                Var var = em.find(Var.class, var_id);
                //Var detached_var = em.detachCopy(var);
                list.add(var.toDTO_Light());
            }
        } finally {
            ////em.close();
        }
        return list;
    }

    private ArrayList<Double> processFilters(ArrayList<String> filters, ArrayList<Integer> filtered_indecies, EntityManager em) {
        ArrayList<Double> filtered_cortage = new ArrayList<Double>();
        ArrayList<VarDTO_Light> vars = getResearchVarsSummaries(research_id, em);
        for (Double val : cortage) {
            filtered_cortage.add(val);
        }
        ArrayList<Integer> init_filtered_indecies = new ArrayList<Integer>();
        int k = 0;
        for (Double val : filtered_cortage) {
            init_filtered_indecies.add(k++);
        }

        for (String filter : filters) {
            ArrayList<String> var_codes = new ArrayList<String>();
            ArrayList<VarDTO_Light> vars_to_use = new ArrayList<VarDTO_Light>();
            ArrayList<Double> step_filtered_cortage = new ArrayList<Double>();
            ArrayList<Integer> step_filtered_indecies = new ArrayList<Integer>();

            for (Double elem : filtered_cortage) {
                step_filtered_cortage.add(elem);
            }
            for (VarDTO_Light vardto : vars) {
                //in formula varcode is presented in [] - by protocol
                if (filter.contains(vardto.getCode() + " ")) {
                    var_codes.add(vardto.getCode());
                    if (!vardto.getCode().equals(this.code)) {
                        vars_to_use.add(vardto);
                    }
                }
            }
            //use indecies???
            int i = 0;
            int ik = step_filtered_cortage.size();
            Map<String, Object> map = new HashMap<String, Object>();
            Expression e = ExpressionBuilder.build(filter);
            ArrayList<Var> varsss = new ArrayList<Var>();
            for (VarDTO_Light vardto : vars_to_use) {
                Var v = getVar(vardto.getId(), em);
                varsss.add(v);
            }
            for (Double val : step_filtered_cortage) {
                try {
                    map.clear();
                    //new ExpressionBuilder().testBuilder();
                    if (var_codes.contains(this.code)) {
                        map.put(this.code, val);
                    }
                    for (Var va : varsss) {
                        map.put(va.getCode(), va.getCortage().get(init_filtered_indecies.get(i)));
                    }
                    // Expression e = ExpressionBuilder.build("str != 'qwerty' && n1 / n2 >= 3 * (n2 + 10 / n1 * (2+3))");
                    Boolean a = (Boolean) e.execute(map);
                    if (a) {
                        step_filtered_indecies.add(i);
                    } else {
                        filtered_cortage.remove(i - (step_filtered_cortage.size() - ik));
                        ik--;
                    }
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                i++;
            }
            init_filtered_indecies = step_filtered_indecies;
        }
        for (Integer in : init_filtered_indecies) {
            filtered_indecies.add(in);
        }
        //filtered_indecies = init_filtered_indecies;
        return filtered_cortage;
    }

    public PairDistr calcDistribution(UserHistoryDTO hist,UserAccountDTO watching_user, VarDTO dto_calcing_for, EntityManager em) {
        SocioResearch research;
        List<Long> var_ids = new ArrayList<Long>();
        long weight_var_id = 0;
        Integer weights_use = 0;
        Integer filters_use = 0;


        //todo
        //ArrayList<String> filters = watching_user.getFiltersToProcess(research_id);
        ArrayList<String> filters = new ArrayList<String>();




        try {
            research = em.find(SocioResearch.class, this.research_id);
            //detached_res = pm.detachCopy(research);
            var_ids = research.getVar_ids();
            //weight_var_id = research.getVar_weight_id();

            //TODO here
            //new UserAccount(em).updateAccountResearchState(em,watching_user);
            UserAccount acc = new UserAccount(em).getUserAccountUnsafe(watching_user.getId());
            //hard-coded admin exclusion.
            if (acc == null || (acc != null && !acc.getAccountType().equals("researchAdmin"))) {
                if(hist==null && acc != null)hist = UserAccount.toHistoryDTO(acc,research_id,em);
                if(hist != null){
                    weight_var_id = hist.getCurrent_research().getWeights_var_id();
                    weights_use = hist.getCurrent_research().getWeights_use();
                    filters_use = hist.getCurrent_research().getFilters_use();
                    filters = hist.getCurrent_research().getFiltersToProcess();
                }
                
            }
            // = UserAccount.toHistoryDTO(new UserAccount(em).getUserAccount(watching_user.getEmailAddress(), "pswd"))
        } finally {
            ////em.close();
        }
        //UserAccount account = 

        //last item for missing values!
        ArrayList<Double> distr = new ArrayList<Double>(v_label_codes.size());
        ArrayList<Double> valid_distr = new ArrayList<Double>(v_label_codes.size());
       
        for (int i = 0; i < v_label_codes.size(); i++) {
            distr.add(new Double(0));
            valid_distr.add(new Double(0));
        }
        //last item
        //distr.add(0.0);
        //prepare filters
        ArrayList<Double> filtered = new ArrayList<Double>();
        ArrayList<Integer> filtered_indecies = new ArrayList<Integer>();
        //ArrayList<String> filters = watching_user.getFiltersToProcess(research_id);
        if (filters_use != 0 && filters != null && filters.size() > 0 && var_ids != null) {
            filtered = processFilters(filters, filtered_indecies, em);
        } else {
            //plumb
            int i = 0;
            for (Double d : cortage) {
                filtered_indecies.add(i++);
            }
            filtered = new ArrayList<Double>(cortage);
        }

        ArrayList<Integer> match = new ArrayList<Integer>();

        //with weights
        if (weight_var_id != 0 && weights_use == 1) {
            Var weight_var;
            List<Double> weights;
            try {
                weight_var = em.find(Var.class, weight_var_id);
                //detached_w_var = pmm.detachCopy(weight_var);
                weights = weight_var.getCortage();
            } finally {
                ////em.close();
            }
            if (weights != null && weights.size() >= filtered.size()) {
                int j = 0;
                for (Double value : filtered) {
                    if (!value.equals(Double.NaN)) {
                        int setIndex = v_label_codes.indexOf(value);
                        Double increment = weights.get(filtered_indecies.get(j));
                        if (setIndex >= 0) {
                            Double val = distr.get(setIndex);
                            distr.set(setIndex, val + increment);
                             if(!isMissingValue(value))
                            {
    //                            Double val = distr.get(distr.size()-1);
    //                            distr.set(distr.size()-1, val + increment);
                                Double val_v = valid_distr.get(setIndex);
                                valid_distr.set(setIndex, val_v + increment);
                            }
                        }
                       
                    }
                    j++;
                }
            }
        } else {
            int kkk = 0;
            for (Double value : filtered) {
                if (!value.equals(Double.NaN)) {
                    double d = Math.round(value);
                    int setIndex = v_label_codes.indexOf(value);
                    
                    if (setIndex >= 0) {
                        Double val = distr.get(setIndex);
                        distr.set(setIndex, val + 1);
                      
                        match.add(kkk);
                         if(!isMissingValue(value))
                            {
                                Double val_v = valid_distr.get(setIndex);
                                valid_distr.set(setIndex, val_v + 1);
        //                        Double val = distr.get(distr.size()-1);
        //                        distr.set(distr.size()-1, val + 1);
                            }
                    }
                   
                }
                kkk++;
            }
        }

        if (dto_calcing_for != null) {
            dto_calcing_for.setDistribution(distr);
            dto_calcing_for.setValid_distribution(valid_distr);
            if (dto_calcing_for instanceof RealVarDTO_Detailed) {
                ((RealVarDTO_Detailed) dto_calcing_for).setFiltered_cortage(filtered);
            }
            if (dto_calcing_for instanceof TextVarDTO_Detailed) {
                ArrayList<String> cortage = new ArrayList<String>();
                for (Integer index : filtered_indecies) {
                    cortage.add(this.cortage_string.get(index));
                }
                ((TextVarDTO_Detailed) dto_calcing_for).setFiltered_cortage(cortage);
            }
            if (dto_calcing_for instanceof VarDTO_Detailed) {
                ((VarDTO_Detailed) dto_calcing_for).setNumber_of_records(filtered.size());
            }
        }
        return new PairDistr(distr,valid_distr);
    }

    public static ArrayList<Double> calc2DDistribution(long var1_id, long var2_id, UserAccountDTO watching_user,UserHistoryDTO hist_dto,EntityManager em) {

        Logger.getLogger(Var.class.getName()).log(Level.INFO, "Start Calc 2DD distribution");
        long t1 = System.currentTimeMillis();

        long weight_var_id = 0;
        Integer weights_use = 0;
        Integer filters_use = 0;
        ArrayList<String> filters = new ArrayList<String>();
        ArrayList<Integer> filters_usage = new ArrayList<Integer>();
        //UserHistoryDTO hist_dto = new UserHistoryDTO();
//        new UserAccount(em).updateAccountResearchState(em,watching_user);
        UserAccount acc = new UserAccount(em).getUserAccountUnsafe(watching_user.getId());
        //hard-cored admin exclusion.
        if (acc == null || (acc != null && !acc.getAccountType().equals("researchAdmin"))) {
            if(hist_dto == null && acc != null)
                hist_dto = UserAccount.toHistoryDTO(acc,watching_user.getCurrent_research(),em);
            if(hist_dto!=null){
                weight_var_id = hist_dto.getCurrent_research().getWeights_var_id();
                weights_use = hist_dto.getCurrent_research().getWeights_use();
                filters_use = hist_dto.getCurrent_research().getFilters_use();
                filters_usage = hist_dto.getCurrent_research().getFilters_usage();
                filters = hist_dto.getCurrent_research().getFiltersToProcess();
            }
            

        }

        //todo
        //ArrayList<String> filters = watching_user.getFiltersToProcess(research_id);




        ArrayList<Double> distr = new ArrayList<Double>();

        Var dsVar1, dsVar2;

        // @PersistenceContext EntityManager pm;
        try {
            dsVar1 = em.find(Var.class, var1_id);
            // dsVar1 = pm.detachCopy(dsVar1);
            dsVar2 = em.find(Var.class, var2_id);
            //dsVar2 = pm.detachCopy(dsVar2);
        } finally {
            ////em.close();
        }
        if (!dsVar1.var_type.equals(VarDTO_Detailed.alt_var_type) || !dsVar2.var_type.equals(VarDTO_Detailed.alt_var_type)) {
            return new ArrayList<Double>();
        }
        ArrayList<Integer> initial_filters_usage = new ArrayList<Integer>();
        if (filters_usage.size() > 0) {
            for (Integer use : filters_usage) {
                initial_filters_usage.add(use);
            }
        }

        if (filters_use == null || !(filters_use == 1)) {
            //if filters not to be used we disable all possible filters in account to calculate 2DD with its own filters
            //otherwise filters in 2DD used both with user-defined filters
            ArrayList<Integer> new_usage = filters_usage;
            for (int i = 0; i < new_usage.size(); i++) {
                new_usage.set(i, (Integer) 0);
            }
            
            //watching_user.setFilters_usage(new_usage, dsVar1.getResearch_id());
            hist_dto.getCurrent_research().setFilters_usage(new_usage);
        }
        Integer filters_use_initial = filters_use;
        
        //watching_user.setFilters_use((Integer) 1);
        hist_dto.getCurrent_research().setFilters_use((Integer)1);
        
        //HERE REAL BUGG!!! INFLUENCE 2dd distribd with user self-defined filters!


        //watching_user.setFilters(new ArrayList<String>());
        // hist_dto.getCurrent_research().setFilters(new ArrayList<String>());
        //REMOVE THIS UPPER STRING LATER!!!
         
        filters = hist_dto.getCurrent_research().getFilters();
        //ArrayList<Long> filter_categs = watching_user.getFilters_categories();

        ArrayList<Integer> usage = hist_dto.getCurrent_research().getFilters_usage();

        for (int i = 0; i < dsVar2.getV_label_codes().size(); i++) {
            ArrayList<Double> distrib_per_alternative = new ArrayList<Double>();
            String filter = dsVar2.getCode() + " == " + dsVar2.getV_label_codes().get(i);
            filters.add(filter);
            //filter_categs.add(dsVar1.getResearch_id());
            filters_usage.add((Integer) 1);
            //watching_user.setFilters_usage(usage, dsVar1.getResearch_id());

            distrib_per_alternative = dsVar1.calcDistribution(hist_dto,watching_user, null, em).distr;
            for (Double d : distrib_per_alternative) {
                distr.add(d);
            }

            filters.remove(filters.size() - 1);
            //filter_categs.remove(filter_categs.size() - 1);
            filters_usage.remove(filters_usage.size() - 1);
            
        //    watching_user.setFilters_usage(usage, dsVar1.getResearch_id());
            hist_dto.getCurrent_research().setFilters(filters);
            hist_dto.getCurrent_research().setFilters_usage(filters_usage);
          
            //	watching_user.getF
            //TODO
            //here adding filters, calculating distrib and then restoring filters and weights in original state. 
        }
          hist_dto.getCurrent_research().setFilters_use(filters_use_initial);
          hist_dto.getCurrent_research().setFilters_usage(initial_filters_usage);
        
          
        //watching_user.setFilters_use(filters_use_initial);
        //watching_user.setFilters_usage(initial_filters_usage, dsVar1.getResearch_id());
        
        
        //watching_user.getF
        //here make real 2DD
        Logger.getLogger(Var.class.getName()).log(Level.INFO, "END Calc 2DD distribution");
        long t2 = System.currentTimeMillis();
        Logger.getLogger(Var.class.getName()).log(Level.INFO, "Calc time spent: " + (t2 - t1) + " ms.");

        return distr;
    }

    public List<Double> getV_label_codes() {
        return v_label_codes;
    }

    public void setV_label_codes(ArrayList<Double> v_label_codes) {
        this.v_label_codes = v_label_codes;
    }

    public List<String> getV_label_values() {
        return v_label_values;
    }

    public void setV_label_values(ArrayList<String> v_label_values) {
        this.v_label_values = v_label_values;
    }

    public List<Double> getCortage() {
        return cortage;
    }

    public void setCortage(ArrayList<Double> cortage) {
        this.cortage = cortage;
    }

    public Long getResearch_id() {
        return research_id;
    }

    public void setResearch_id(Long research_id) {
        this.research_id = research_id;
    }

    public List<Long> getGeneralized_var_ids() {
        return generalized_var_ids;
    }

    public void setGeneralized_var_ids(ArrayList<Long> generalized_var_ids) {
        this.generalized_var_ids = generalized_var_ids;
    }

    /**
     * @return the missing1
     */
    public String getMissing1() {
        return missing1;
    }

    /**
     * @param missing1 the missing1 to set
     */
    public void setMissing1(String missing1) {
        this.missing1 = missing1;
    }

    /**
     * @return the missing2
     */
    public String getMissing2() {
        return missing2;
    }

    /**
     * @param missing2 the missing2 to set
     */
    public void setMissing2(String missing2) {
        this.missing2 = missing2;
    }

    /**
     * @return the missing3
     */
    public String getMissing3() {
        return missing3;
    }

    /**
     * @param missing3 the missing3 to set
     */
    public void setMissing3(String missing3) {
        this.missing3 = missing3;
    }

    /**
     * @return the missing_code
     */
    
}
