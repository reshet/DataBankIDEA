/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.entity;

import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.StartupBundleDTO;
import com.mresearch.databank.shared.ZaconDTO_Light;
import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

/**
 *
 * @author reshet
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "SP.getSingle", query = "SELECT x FROM DatabankStartPage x")
})
public class DatabankStartPage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
   
    
    @OrderColumn
    @OneToMany
    private List<SocioResearch> res;
    @OrderColumn
    @OneToMany
    private List<Zacon> laws;
    
    private Long pubs_last_show;
   
    //private MetaUnitMultivalued<MetaEntityType> root;
    
    public DatabankStartPage() {
        this.res = new ArrayList<SocioResearch>();
        this.laws = new ArrayList<Zacon>();
    }
     public static DatabankStartPage getStartPageSingleton(EntityManager em)
        {
            TypedQuery<DatabankStartPage> q = (TypedQuery<DatabankStartPage>) em.createNamedQuery("SP.getSingle");
            DatabankStartPage d =  q.getSingleResult();
            return d;
        }
    public void updateFromDTO(EntityManager em,StartupBundleDTO rDTO)
	{
              setPubs_last_show(rDTO.getPubs_last_count());
              for(Zacon l:getLaws())
              {
                  if(!rDTO.getImportant_laws().contains(l.toDTO_Light()))getLaws().remove(l);
              }
              for(SocioResearch r:getRes())
              {
                  if(!rDTO.getTop_researchs().contains(r.toLightDTO()))getRes().remove(r);
              }
              for(ZaconDTO_Light dto:rDTO.getImportant_laws())
              {
                  Zacon z = em.find(Zacon.class, dto.getID());
                  getLaws().add(z);
              }
              for(SocioResearchDTO_Light dto:rDTO.getTop_researchs())
              {
                  SocioResearch s = em.find(SocioResearch.class, dto.getID());
                  getRes().add(s);
              }
              
	}
	public StartupBundleDTO toDTO()
	{
            StartupBundleDTO dto = new StartupBundleDTO();
            ArrayList<SocioResearchDTO_Light> re = new ArrayList<SocioResearchDTO_Light>();
            ArrayList<ZaconDTO_Light> lo = new ArrayList<ZaconDTO_Light>();
            for(Zacon l:getLaws())
            {
                lo.add(l.toDTO_Light());
            }
            for(SocioResearch s:getRes())
            {
                re.add(s.toLightDTO());
            }
            dto.setImportant_laws(lo);
            dto.setTop_researchs(re);
            dto.setPubs_last_count(getPubs_last_show());
            return dto;
	}
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatabankStartPage)) {
            return false;
        }
        DatabankStartPage other = (DatabankStartPage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mplatforma.amr.entity.DatabankStructure[ id=" + id + " ]";
    }

    /**
     * @return the pubs_last_show
     */
    public Long getPubs_last_show() {
        return pubs_last_show;
    }

    /**
     * @param pubs_last_show the pubs_last_show to set
     */
    public void setPubs_last_show(Long pubs_last_show) {
        this.pubs_last_show = pubs_last_show;
    }

    /**
     * @return the res
     */
    public List<SocioResearch> getRes() {
        return res;
    }

    /**
     * @param res the res to set
     */
    public void setRes(List<SocioResearch> res) {
        this.res = res;
    }

    /**
     * @return the laws
     */
    public List<Zacon> getLaws() {
        return laws;
    }

    /**
     * @param laws the laws to set
     */
    public void setLaws(List<Zacon> laws) {
        this.laws = laws;
    }
}
