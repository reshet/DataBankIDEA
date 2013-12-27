
package com.mresearch.databank.client.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.opendatafoundation.data.spss.mod.SPSSUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.presenters.UserResearchPerspectivePresenter;
import com.mresearch.databank.client.views.DBviewers.MultiValuedFieldViewer;
import com.mresearch.databank.client.views.DBviewers.VarMultiValuedFieldViewer;
import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.RealVarDTO_Detailed;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.TextVarDTO_Detailed;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.VarDTO;
import com.mresearch.databank.shared.VarDTO_Detailed;
//import com.sun.xml.rpc.tools.wscompile.Main;

public class TextVariableDetailedView extends Composite implements HTML_Saver{

	private static RealVariableDetailedViewUiBinder uiBinder = GWT
			.create(RealVariableDetailedViewUiBinder.class);

	interface RealVariableDetailedViewUiBinder extends
			UiBinder<Widget, TextVariableDetailedView> {
	}

	@UiField Label varText,number_of_records;
	@UiField FlexTable generalizedTbl,values_table;
	private MetaUnitMultivaluedEntityDTO db;
	private VarDTO_Detailed dto;
	@UiField VerticalPanel research_link;
	@UiField HorizontalPanel analysis_bar;
	@UiField Label concept_name,concept_value;
	private UserResearchPerspectivePresenter.Display display;
	@UiField HTMLPanel main_html;
	public TextVariableDetailedView(TextVarDTO_Detailed dto,MetaUnitMultivaluedEntityDTO dt,SimpleEventBus bus,UserResearchPerspectivePresenter.Display display,UserAnalysisSaveDTO sv_dt) {
		initWidget(uiBinder.createAndBindUi(this));
		this.display = display;
		this.dto = dto;
		this.db = dt;
		UserAccountDTO user = DatabankApp.get().getCurrentUser();
        if(DatabankApp.get().getCurrentUser().getId()!=0)
        {
            String user_class = DatabankApp.get().getCurrentUser().getAccountType();
            if(user_class.equals("grantedUser")){
                analysis_bar.add(new AnalisysBarView(bus, display,sv_dt,this));
            }
        }

		research_link.add(new ResearchDescItemView(new SocioResearchDTO_Light(dto.getResearch_id(),dto.getResearch_name())));
		
		//varCode.setText(dto.getCode());
		varText.setWidth("400px");
		varText.setWordWrap(true);
		varText.setText(dto.getCode()+" "+dto.getLabel());
		number_of_records.setText(String.valueOf(dto.getNumber_of_records()));
		int i = 0;
        // formatter.
         //formatter.setMaximumFractionDigits(2);
        // String myNumber = formatter.format(new Double(dto.getDistribution().get(i)/total)*100);
		//codeSchemeTbl.insertCell(beforeRow, beforeColumn)
		for(String value:dto.getFiltered_cortage())
		{
			values_table.setWidget(i, 0, new Label(value));
		}
						
        generalizedTbl.setSize("600px", "350px");
		for(int j = 0; j < dto.getGen_var_names().size();j++)
		{
			generalizedTbl.setWidget(j, 0, new Label(" как "));
			generalizedTbl.setWidget(j, 1, new Label(dto.getGen_var_names().get(j)));
			generalizedTbl.setWidget(j, 2, new Label(" в исследовании "));
			generalizedTbl.setWidget(j, 3, new Label(dto.getGen_research_names().get(j)));
		}
		renderDBfillers();
	}
	@UiHandler(value="back_btn")
	public void back_action(ClickEvent e)
	{
		History.back();
	}
	private void renderDBfillers()
	{
		//elasticDBfields.clear();
		//VarMultiValuedFieldViewer mv = new VarMultiValuedFieldViewer(db,dto.getFilling(),"");
		//elasticDBfields.add(mv);
		Collection<MetaUnitDTO> base = db.getSub_meta_units();
		int i = 0;
		if(base!=null)
		for(MetaUnitDTO dto:base)
		{
			if(dto instanceof MetaUnitMultivaluedEntityDTO)
			{
				MetaUnitMultivaluedEntityDTO dto_str = (MetaUnitMultivaluedEntityDTO)dto;
				concept_name.setText(dto_str.getDesc());
				String base_name = this.db.getUnique_name()+"_"+dto.getUnique_name();
				if(this.dto.getFilling()!=null)
					if(this.dto.getFilling().containsKey(base_name))
					{
						  String val = (String)this.dto.getFilling().get(base_name);
					      if (val != null)
					      {
					    	  concept_value.setText(val);
					      }
					}
				break;
			}
		}
	
	}

	@Override
	public String composeSpecificContent() {
		return main_html.toString();
	}
}
