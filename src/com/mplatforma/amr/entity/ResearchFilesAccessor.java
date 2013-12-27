package com.mplatforma.amr.entity;


import java.util.ArrayList;


import com.mresearch.databank.shared.ResearchFilesDTO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ResearchFilesAccessor {
	@Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        private Long id;
	private ArrayList<Long> file_ids = new ArrayList<Long>();
	private ArrayList<String> file_names = new ArrayList<String>();
	private ArrayList<String> file_categs = new ArrayList<String>();
    private String requestAccessEmail = "office@aidsalliance.org.ua";
	public ResearchFilesAccessor()
	{}
	
	public ResearchFilesAccessor(ResearchFilesDTO dto)
	{
		//this.project_id = project_id;
		updateFromDTO(dto);
	}	
//	public String getProject_id() {
//		return project_id;
//	}


	public void updateFromDTO(ResearchFilesDTO dto)
	{
		this.file_ids = dto.getFile_ids();
		this.file_categs = dto.getFile_categs();
		this.file_names = dto.getFile_names();
        this.requestAccessEmail = dto.getRequestAccessEmail();
	}
	public ResearchFilesDTO toDTO()
	{
		ResearchFilesDTO dto = new ResearchFilesDTO();
		dto.setId(this.getId());
		dto.setFile_categs(file_categs);
		dto.setFile_ids(file_ids);
		dto.setFile_names(file_names);
        dto.setRequestAccessEmail(requestAccessEmail);
		return dto;
	}
	public Long getId() {
		return id;
	}

    public String getRequestAccessEmail() {
        return requestAccessEmail;
    }

    public void setRequestAccessEmail(String requestAccessEmail) {
        this.requestAccessEmail = requestAccessEmail;
    }
}
