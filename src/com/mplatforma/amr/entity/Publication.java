package com.mplatforma.amr.entity;


import java.util.ArrayList;
import java.util.Date;

import com.mresearch.databank.shared.PublicationDTO;
import com.mresearch.databank.shared.PublicationDTO_Light;
import java.util.List;
import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "Publication.getAllLight", query = "SELECT NEW com.mresearch.databank.shared.PublicationDTO_Light(x.id, x.name,x.date_publ) FROM Publication x ORDER BY x.date_publ DESC"),
    @NamedQuery(name = "Publication.getLightIN", query = "SELECT NEW com.mresearch.databank.shared.PublicationDTO_Light(x.id, x.name,x.date_publ) FROM Publication x WHERE x.id IN :idlist ORDER BY x.date_publ DESC"),
    @NamedQuery(name = "Publication.getFullIN", query = "SELECT NEW com.mresearch.databank.shared.PublicationDTO(x.id, x.name, x.subheading, x.contents, x.date_publ, x.enclosure_key, y.mapped_values) "
                                                 + "FROM Publication x INNER JOIN x.entity_item y"
                                                    + " WHERE x.id IN :idlist ORDER BY x.date_publ DESC")

})
public class Publication {
	@Id
        @GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	private String name;
 	
        @Column(columnDefinition="TEXT")
	private String contents;
        private String subheading;
        @Temporal(TemporalType.TIMESTAMP)
        private Date date_publ;
	private Long enclosure_key;
       
        
        @OneToOne(cascade= CascadeType.PERSIST)
        private MetaUnitEntityItem entity_item;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Publication()
	{
             this.entity_item = new MetaUnitEntityItem("Publication");
        }
	public Publication(PublicationDTO dto)
	{
                this.entity_item = new MetaUnitEntityItem(dto.getHeader());
                this.entity_item.setMapped_values(dto.getFilling());
                this.name = dto.getHeader();
		this.setContents(dto.getContents());
		this.setEnclosure_key(dto.getEnclosure_key());
                this.setDate_publ(dto.getDate());
                this.setSubheading(dto.getSubheading());
                //this.setDate(dto.getDate());
		//this.setAccept_date(dto.getAccept_date());
		//this.setDecline_date(dto.getDecline_date());
		//this.setKey_words(dto.getKey_words());
		//this.setAuthors(dto.getAuthors());
	}	
        
         public static ArrayList<PublicationDTO_Light> getAllPublicationLightDTOs(EntityManager em)
        {
            TypedQuery<PublicationDTO_Light> q = (TypedQuery<PublicationDTO_Light>)em.createNamedQuery("Publication.getAllLight");
            List<PublicationDTO_Light> l = q.getResultList();
            return new ArrayList<PublicationDTO_Light>(l);
        }
         public static ArrayList<PublicationDTO_Light> getPublicationLightDTOs(EntityManager em,int limit,int offset)
        {
            TypedQuery<PublicationDTO_Light> q = (TypedQuery<PublicationDTO_Light>) em.createNamedQuery("Publication.getAllLight");
            q.setFirstResult(offset);
            q.setMaxResults(limit);
            List<PublicationDTO_Light> l = q.getResultList();
            return new ArrayList<PublicationDTO_Light>(l);
        }
        public static ArrayList<PublicationDTO_Light> getPublicationLightDTOs(EntityManager em, ArrayList<Long> ids)
        {
            TypedQuery<PublicationDTO_Light> q = (TypedQuery<PublicationDTO_Light>)em.createNamedQuery("Publication.getLightIN");
            q.setParameter("idlist", ids);
            List<PublicationDTO_Light> l = q.getResultList();
            return new ArrayList<PublicationDTO_Light>(l);
        }
        public static ArrayList<PublicationDTO> getPublicationFullDTOs(EntityManager em, ArrayList<Long> ids)
        {
            TypedQuery<PublicationDTO> q = (TypedQuery<PublicationDTO>) em.createNamedQuery("Publication.getFullIN");
            q.setParameter("idlist", ids);
            List<PublicationDTO> l = q.getResultList();
            return new ArrayList<PublicationDTO>(l);
        }
        public void updateFromDTO(PublicationDTO rDTO)
	{
                if(this.getEntity_item() == null) this.entity_item = new MetaUnitEntityItem(rDTO.getHeader());
                this.getEntity_item().setMapped_values(rDTO.getFilling());
                this.setName(rDTO.getHeader());
                this.setContents(rDTO.getContents());
                this.setSubheading(rDTO.getSubheading());
                this.setDate_publ(rDTO.getDate());
                this.setEnclosure_key(rDTO.getEnclosure_key());
	}
	public PublicationDTO toDTO()
	{
		PublicationDTO dto = new PublicationDTO(getId(), getName(), getSubheading(),getContents(), getDate_publ(),getEnclosure_key(),getEntity_item().getMapped_values());
                //dto.setId(this.getId());
		//dto.setEnclosure_key(getEnclosure_key());
		//dto.setDate(date);
		//dto.setAccept_date(accept_date);
		//dto.setDecline_date(decline_date);
		//dto.setKey_words(key_words);
	//	dto.setNumber(number);
                //dto.setFilling(entity_item.getMapped_values());
		//dto.setAuthors(authors);
		return dto;
	}
        public PublicationDTO_Light toDTO_Light()
	{
		PublicationDTO_Light dto = new PublicationDTO_Light(getId(), getName(), getDate_publ());
		//dto.setId(this.getId());
		//dto.setEnclosure_key(getEnclosure_key());
		//dto.setDate(date);
		//dto.setAccept_date(accept_date);
		//dto.setDecline_date(decline_date);
		//dto.setKey_words(key_words);
		//dto.setNumber(number);
                //dto.setFilling(entity_item.getMapped_values());
		//dto.setAuthors(authors);
		return dto;
	}
	public Long getId() {
		return id;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Long getEnclosure_key() {
		return enclosure_key;
	}
	public void setEnclosure_key(Long enclosure_key) {
		this.enclosure_key = enclosure_key;
	}
//	public Date getDate() {
//		return date;
//	}
//	public void setDate(Date date) {
//		this.date = date;
//	}
//	public Date getAccept_date() {
//		return accept_date;
//	}
//	public void setAccept_date(Date accept_date) {
//		this.accept_date = accept_date;
//	}
//	public Date getDecline_date() {
//		return decline_date;
//	}
//	public void setDecline_date(Date decline_date) {
//		this.decline_date = decline_date;
//	}
//	public ArrayList<String> getKey_words() {
//		return key_words;
//	}
//	public void setKey_words(ArrayList<String> key_words) {
//		this.key_words = key_words;
//	}
	
//	public ArrayList<String> getAuthors() {
//		return authors;
//	}
//	public void setAuthors(ArrayList<String> authors) {
//		this.authors = authors;
//	}

    /**
     * @return the subheading
     */
    public String getSubheading() {
        return subheading;
    }

    /**
     * @param subheading the subheading to set
     */
    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    /**
     * @return the date_publ
     */
    public Date getDate_publ() {
        return date_publ;
    }

    /**
     * @param date_publ the date_publ to set
     */
    public void setDate_publ(Date date_publ) {
        this.date_publ = date_publ;
    }

    /**
     * @return the entity_item
     */
    public MetaUnitEntityItem getEntity_item() {
        return entity_item;
    }
}
