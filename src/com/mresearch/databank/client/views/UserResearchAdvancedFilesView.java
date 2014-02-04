package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.presenters.AdminResearchDetailedPresenter;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.shared.ResearchFilesDTO;
import com.mresearch.databank.shared.SocioResearchFilesDTO;

public class UserResearchAdvancedFilesView extends Composite implements AdminResearchDetailedPresenter.FilesEditDisplay{
	private static AdminResearchFilesEditViewUiBinder uiBinder = GWT
			.create(AdminResearchFilesEditViewUiBinder.class);

	interface AdminResearchFilesEditViewUiBinder extends
			UiBinder<Widget, UserResearchAdvancedFilesView> {
	}
	private final UserSocioResearchServiceAsync userResearchService = GWT
		      .create(UserSocioResearchService.class);
	
	private long research_id;
	private ResearchFilesDTO result;
    @UiField VerticalPanel arrays_panel,desc_panel, pretest_panel, instrument_panel, sample_panel;
	@UiField VerticalPanel mailto_panel,publications_panel;
	public UserResearchAdvancedFilesView(long research_id,ResearchFilesDTO dto) {
		initWidget(uiBinder.createAndBindUi(this));
		this.research_id = research_id;
		this.result = dto;
		doFetchFiles();
		
	}

	private void doFetchFiles()
	{
//		new RPCCall<ResearchFilesDTO>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				Window.alert("Error on fetching files "+caught.getMessage());
//			}
//
//			@Override
//			public void onSuccess(ResearchFilesDTO result) {
//							}
//
//			@Override
//			protected void callService(AsyncCallback<ResearchFilesDTO> cb) {
//				userResearchService.getResearchFiles(research_id, cb);
//			}
//		}.retry(2);

        SocioResearchFilesDTO arr_dto = new SocioResearchFilesDTO(result.getFileIds(ResearchFilesDTO.CG_arrays),result.getFileNames(ResearchFilesDTO.CG_arrays));
        UserResearchFilesView arr_view = new UserResearchFilesView(research_id, ResearchFilesDTO.CG_arrays, arr_dto);
        arrays_panel.add(arr_view);

        SocioResearchFilesDTO desc_dto = new SocioResearchFilesDTO(result.getFileIds(ResearchFilesDTO.CG_questionaries),result.getFileNames(ResearchFilesDTO.CG_questionaries));
        UserResearchFilesView desc_view = new UserResearchFilesView(research_id, ResearchFilesDTO.CG_questionaries, desc_dto);
        desc_panel.add(desc_view);

        SocioResearchFilesDTO qc_dto = new SocioResearchFilesDTO(result.getFileIds(ResearchFilesDTO.CG_questionary_cards),result.getFileNames(ResearchFilesDTO.CG_questionary_cards));
        UserResearchFilesView qc_view = new UserResearchFilesView(research_id, ResearchFilesDTO.CG_questionary_cards, qc_dto);
        pretest_panel.add(qc_view);

        SocioResearchFilesDTO pr_dto = new SocioResearchFilesDTO(result.getFileIds(ResearchFilesDTO.CG_protocols),result.getFileNames(ResearchFilesDTO.CG_protocols));
        UserResearchFilesView pr_view = new UserResearchFilesView(research_id, ResearchFilesDTO.CG_protocols,pr_dto);
        instrument_panel.add(pr_view);

        SocioResearchFilesDTO tr_dto = new SocioResearchFilesDTO(result.getFileIds(ResearchFilesDTO.CG_technical_reports),result.getFileNames(ResearchFilesDTO.CG_technical_reports));
        UserResearchFilesView tr_view = new UserResearchFilesView(research_id, ResearchFilesDTO.CG_technical_reports, tr_dto);
        sample_panel.add(tr_view);

		/*SocioResearchFilesDTO i_dto = new SocioResearchFilesDTO(result.getFileIds(ResearchFilesDTO.CG_instructions),result.getFileNames(ResearchFilesDTO.CG_instructions));
		UserResearchFilesView i_view = new UserResearchFilesView(research_id, ResearchFilesDTO.CG_instructions, i_dto);
		*/
        //mailto_panel.add(new HTML("<a href=\"mailto:reshet.ukr@gmail.com?subject=Запит на отримання масиву даних за дослідженням "+research_id+"&body=Прошу надати мені доступ до даних.\">Надіслати лист-запит</a>"));

		/*SocioResearchFilesDTO ar_dto = new SocioResearchFilesDTO(result.getFileIds(ResearchFilesDTO.CG_analytic_reports),result.getFileNames(ResearchFilesDTO.CG_analytic_reports));
		UserResearchFilesView ar_view = new UserResearchFilesView(research_id, ResearchFilesDTO.CG_analytic_reports, ar_dto);
		analytic_report_panel.add(ar_view);*/
        SocioResearchFilesDTO p_dto = new SocioResearchFilesDTO(result.getFileIds(ResearchFilesDTO.CG_publications),result.getFileNames(ResearchFilesDTO.CG_publications));
        UserResearchFilesView p_view = new UserResearchFilesView(research_id, ResearchFilesDTO.CG_publications, p_dto);
        publications_panel.add(p_view);

        String user_class  = "simpleUser";
        if(DatabankApp.get().getCurrentUser().getId()!=0)
        {
            user_class = DatabankApp.get().getCurrentUser().getAccountType();
        }
        if(!user_class.equals("grantedUser")){
            mailto_panel.add(new HTML("<a href=\"mailto:"+result.getRequestAccessEmail()+"?subject=Запит на отримання масиву даних за дослідженням "+research_id+"&body=Прошу надати мені доступ до даних.\">Надіслати лист-запит</a>"));
            if(result.getRequestAccessEmail()!=null)mailto_panel.add(new HTML("<p>e-mail відповідальної особи "+result.getRequestAccessEmail()+"</p>"));
        }
		/*SocioResearchFilesDTO ar_dto = new SocioResearchFilesDTO(result.getFileIds(ResearchFilesDTO.CG_analytic_reports),result.getFileNames(ResearchFilesDTO.CG_analytic_reports));
		UserResearchFilesView ar_view = new UserResearchFilesView(research_id, ResearchFilesDTO.CG_analytic_reports, ar_dto);
		analytic_report_panel.add(ar_view);*/
		/*
		SocioResearchFilesDTO p_dto = new SocioResearchFilesDTO(result.getFileIds(ResearchFilesDTO.CG_publications),result.getFileNames(ResearchFilesDTO.CG_publications));
		UserResearchFilesView p_view = new UserResearchFilesView(research_id, ResearchFilesDTO.CG_publications, p_dto);
		publications_panel.add(p_view);*/

	}
}
