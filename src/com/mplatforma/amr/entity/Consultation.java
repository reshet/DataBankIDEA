package com.mplatforma.amr.entity;


import java.util.ArrayList;
import java.util.Date;

import com.mresearch.databank.shared.ConsultationDTO;
import com.mresearch.databank.shared.ConsultationDTO_Light;
import java.util.List;
import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "Consultation.getAllLight", query = "SELECT NEW com.mresearch.databank.shared.ConsultationDTO_Light(x.id, x.question,x.date_ask) FROM Consultation x ORDER BY x.date_ask DESC"),
    @NamedQuery(name = "Consultation.getLightIN", query = "SELECT NEW com.mresearch.databank.shared.ConsultationDTO_Light(x.id, x.question,x.date_ask) FROM Consultation x WHERE x.id IN :idlist ORDER BY x.date_ask DESC"),
    @NamedQuery(name = "Consultation.getFullIN", query = "SELECT NEW com.mresearch.databank.shared.ConsultationDTO(x.id, x.question, x.answer, x.date_ask, x.date_ans, x.published, y.mapped_values) "
                                                 + "FROM Consultation x INNER JOIN x.entity_item y"
                                                      + " WHERE x.id IN :idlist AND x.answer IS NOT NULL AND LENGTH(x.answer) > 0 AND x.published = 1 ORDER BY x.date_ask DESC")

})
public class Consultation {
	@Id
        @GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
        @Column(columnDefinition="TEXT")
	private String question;
        @Column(columnDefinition="TEXT")
	private String answer;
        @Temporal(TemporalType.TIMESTAMP)
        private Date date_ask;
          @Temporal(TemporalType.TIMESTAMP)
        private Date date_ans;
	
        private Integer published;
        
        @OneToOne(cascade= CascadeType.PERSIST)
        private MetaUnitEntityItem entity_item;

	public Consultation()
	{
             this.entity_item = new MetaUnitEntityItem("consultation");
        }
        
        
	public Consultation(ConsultationDTO dto)
	{
                this.entity_item = new MetaUnitEntityItem(dto.getquestion());
                this.entity_item.setMapped_values(dto.getFilling());
		this.question = dto.getquestion();
                this.answer = dto.getAnswer();
                this.published = dto.getIsPublished();
                this.date_ask = new Date();
	}	
        
         public static ArrayList<ConsultationDTO_Light> getAllConsultationLightDTOs(EntityManager em)
        {
            TypedQuery<ConsultationDTO_Light> q = (TypedQuery<ConsultationDTO_Light>) em.createNamedQuery("Consultation.getAllLight");
            List<ConsultationDTO_Light> l = q.getResultList();
            return new ArrayList<ConsultationDTO_Light>(l);
        }
         public static ArrayList<ConsultationDTO_Light> getConsultationLightDTOs(EntityManager em,int limit,int offset)
        {
            TypedQuery<ConsultationDTO_Light> q = (TypedQuery<ConsultationDTO_Light>) em.createNamedQuery("Consultation.getAllLight");
            q.setFirstResult(offset);
            q.setMaxResults(limit);
            List<ConsultationDTO_Light> l = q.getResultList();
            return new ArrayList<ConsultationDTO_Light>(l);
        }
        public static ArrayList<ConsultationDTO_Light> getConsultationLightDTOs(EntityManager em, ArrayList<Long> ids)
        {
            TypedQuery<ConsultationDTO_Light> q = (TypedQuery<ConsultationDTO_Light>) em.createNamedQuery("Consultation.getLightIN");
            q.setParameter("idlist", ids);
            List<ConsultationDTO_Light> l = q.getResultList();
            return new ArrayList<ConsultationDTO_Light>(l);
        }
        public static ArrayList<ConsultationDTO> getConsultationFullDTOs(EntityManager em, ArrayList<Long> ids)
        {
            TypedQuery<ConsultationDTO> q = (TypedQuery<ConsultationDTO>) em.createNamedQuery("Consultation.getFullIN");
            q.setParameter("idlist", ids);
            List<ConsultationDTO> l = q.getResultList();
            return new ArrayList<ConsultationDTO>(l);
        }
        public void updateFromDTO(ConsultationDTO rDTO)
	{
                if(this.getEntity_item() == null) this.entity_item = new MetaUnitEntityItem(rDTO.getquestion());
                this.getEntity_item().setMapped_values(rDTO.getFilling());
                this.setQuestion(rDTO.getquestion());
                this.setAnswer(rDTO.getAnswer());
                this.setPublished(rDTO.getIsPublished());
                this.setDate_ans(new Date());
	}
	public ConsultationDTO toDTO()
	{
		ConsultationDTO dto = new ConsultationDTO(getId(), getQuestion(), getAnswer(),getDate_ask(), getDate_ans(),getPublished(),getEntity_item().getMapped_values());
		return dto;
	}
        public ConsultationDTO_Light toDTO_Light()
	{
		ConsultationDTO_Light dto = new ConsultationDTO_Light(getId(), getQuestion(), getDate_ask());

		return dto;
	}
	public Long getId() {
		return id;
	}




    

    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * @param answer the answer to set
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * @return the date_ask
     */
    public Date getDate_ask() {
        return date_ask;
    }

    /**
     * @param date_ask the date_ask to set
     */
    public void setDate_ask(Date date_ask) {
        this.date_ask = date_ask;
    }

    /**
     * @return the date_ans
     */
    public Date getDate_ans() {
        return date_ans;
    }

    /**
     * @param date_ans the date_ans to set
     */
    public void setDate_ans(Date date_ans) {
        this.date_ans = date_ans;
    }

    /**
     * @return the published
     */
    public Integer getPublished() {
        return published;
    }

    /**
     * @param published the published to set
     */
    public void setPublished(Integer published) {
        this.published = published;
    }

    /**
     * @return the entity_item
     */
    public MetaUnitEntityItem getEntity_item() {
        return entity_item;
    }

    /**
     * @param entity_item the entity_item to set
     */
    public void setEntity_item(MetaUnitEntityItem entity_item) {
        this.entity_item = entity_item;
    }
}
