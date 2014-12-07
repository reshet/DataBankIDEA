package com.mplatforma.amr.entity;


import com.mresearch.databank.shared.ArticleDTO;

public class Article {
	
	private Long id;
	private String name;
	private String contents;
	private String enclosure_key;
	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}
	public Article()
	{}
	public Article(ArticleDTO dto)
	{
		this.name = dto.getHeader();
		this.setContents(dto.getContents());
		this.setEnclosure_key(dto.getEnclosure_key());
	}	
	public ArticleDTO toDTO()
	{
		ArticleDTO dto = new ArticleDTO(name,getContents());
		dto.setId(this.getId());
		dto.setEnclosure_key(getEnclosure_key());
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
	public String getEnclosure_key() {
		return enclosure_key;
	}
	public void setEnclosure_key(String enclosure_key) {
		this.enclosure_key = enclosure_key;
	}
}
