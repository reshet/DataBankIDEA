package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.presenters.UserResearchPerspectivePresenter;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.VarDTO_Detailed;
import org.moxieapps.gwt.highcharts.client.*;
import org.moxieapps.gwt.highcharts.client.labels.XAxisLabels;
import org.moxieapps.gwt.highcharts.client.labels.YAxisLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.BarPlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;

import java.util.ArrayList;

public class CompareVariableGraphItemView extends Composite {

	private static CompareVariableTableItemUiBinder uiBinder = GWT
			.create(CompareVariableTableItemUiBinder.class);

	interface CompareVariableTableItemUiBinder extends
			UiBinder<Widget, CompareVariableGraphItemView> {
	}

	//@UiField Label varText;
	//@UiField FlexTable codeSchemeTbl;
	@UiField HTMLPanel main_html;
//	@UiField Button save_html_btn;
	//@UiField HorizontalPanel save_pnl;
	//@UiField VerticalPanel graph_pnl;
	//@UiField VerticalPanel elasticDBfields;
//	@UiField Label concept_name,concept_value;
//	@UiField Label dates,gen_geath,sel_size,org_impl,tag;
	//@UiField HorizontalPanel analysis_bar;
//	@UiField VerticalPanel research_link;
//	Hyperlink link;
//	private FormPanel form;
//	private TextBox hidden_box;
//	private TextBox hidden_box2;
//	private VarDTO_Detailed dto;
//	@UiHandler(value="save_html_btn")
//	public void save_html(ClickEvent e)
//	{
//		String s = main_html.toString();
//		hidden_box.setText(s);
//		hidden_box2.setText("DB_saved_var_distr_"+dto.getCode());
//		form.submit();
//		//s = URL.encode(s);
//		//save_pnl.clear();
//		//save_pnl.add(new HTML("<a href=\"/databank/htmlSave?tosave="+s+"\" target=\"_blank\">Скачать файл!</a>"));
//	}
	
//	
//	private native JavaScriptObject getJSON()/*-{
//		return eval(
//		{
//			"title":{"text":"ПРодажі Васі", "style":"font-size: 14px; font-family: Verdana; text-align: center;"}, 
//			"legend":{"visible":true, "bg_colour":"#fefefe", "position":"right", "border":true, "shadow":true},
//			"bg_colour":"#ffffff", 
//			"elements":
//				[
//					{"type":"pie",
//					 "tip":"#label# $#val#<br>#percent#", 
//					"values":[
//						{"value":1000, "label":"AU", "text":"Австралія"},
//						{"value":84000, "label":"USA", "text":"USA"},
//						{"value":37000, "label":"UK", "text":"United Kingdom"},
//						{"value":9000, "label":"JP", "text":"Japan"},
//						{"value":32000, "label":"EU", "text":"Europe"}],
//					"radius":130, 
//					"highlight":"alpha", 
//					"animate":true, 
//					"gradient-fill":true, 
//					"alpha":0.5, 
//					"no-labels":true, 
//					"colours":["#ff0000","#00aa00","#0000ff","#ff9900","#ff00ff"]
//					}
//				]
//			}
//	)
// }-*/;
	
	
	
	
	
	
	
	private MetaUnitMultivaluedEntityDTO db;
	private VarDTO_Detailed dto;
	private AnalisysBarView anal_bar_w;
	public CompareVariableGraphItemView(VarDTO_Detailed dto,MetaUnitMultivaluedEntityDTO dt,UserResearchPerspectivePresenter.Display display,UserAnalysisSaveDTO save_dto)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.db = dt;
		this.dto = dto;
		
		//setResearchMeta();
		UserAccountDTO user = DatabankApp.get().getCurrentUser();
		save_dto.setDistr_type(save_dto.DISTR_TYPE_1D);
		
		showPlot();
//		anal_bar_w = new AnalisysBarView(bus, display,save_dto);
//		analysis_bar.add(anal_bar_w);
		//form.a
		
		
		
		
		
		//save_pnl.add(new SaveHTMLAddon(main_html));
		
		//varCode.setText(dto.getCode());
//		varText.setWidth("400px");
//		varText.setWordWrap(true);
//		varText.setText(dto.getCode()+" "+dto.getLabel());
		ArrayList<Double> codes = dto.getV_label_codes();
		ArrayList<String> values = dto.getV_label_values();
		
//		int i = 0;
//		codeSchemeTbl.setSize("450px", "350px");
//		codeSchemeTbl.setWidget(0, 0, new Label("Код"));
//		codeSchemeTbl.setWidget(0, 1, new Label("Текст альтернативы"));
//		codeSchemeTbl.setWidget(0, 2, new Label("Проценты"));
		//codeSchemeTbl.insertCell(beforeRow, beforeColumn)
//		Double total = new Double(0);
//		
//		for(Double cnt:dto.getDistribution())
//		{
//			total+=cnt;
//		}
//        for(Double key:codes)
//        {
// 		   String label = values.get(i);
//           codeSchemeTbl.setWidget(i+1, 0, new Label(key.toString()));
//           codeSchemeTbl.setWidget(i+1, 1, new Label(label.toString()));
//           
//           //NumberFormat formatter = NumberFormat.getInstance();
//           NumberFormat formatter = NumberFormat.getFormat("0.0");
//          // formatter.
//           //formatter.setMaximumFractionDigits(2);
//           String myNumber = formatter.format(new Double(dto.getDistribution().get(i)/total)*100);
//           
//          // new NumberFormat();
//           codeSchemeTbl.setWidget(i+1, 2, new Label(myNumber+"%"));
//           //com.google.gwt.user.client.ui.MultiWordSuggestOracle c = new MultiWordSuggestOracle();
//           //c.requestSuggestions(request, callback)
//           //com.google.gwt.user.client.ui.
//           i++;
//        }
        
    
	}
	
	
	
	private boolean plot_viewed = false;
	public static String wrap(String in,int len) {
		in=in.trim();
		if(in.length()<len) return in;
		if(in.substring(0, len).contains("<br/>"))
		return in.substring(0, in.indexOf("<br/>")).trim() + "\n\n" + wrap(in.substring(in.indexOf("<br/>") + 1), len);
		int place=Math.max(Math.max(in.lastIndexOf(" ",len),in.lastIndexOf("\t",len)),in.lastIndexOf("-",len));
		return in.substring(0,place).trim()+"<br/>"+wrap(in.substring(place),len);
		}
	private void showPlot()
	{
		if(!plot_viewed)
		{
			plot_viewed = true;
			
//			ChartWidget widg = new ChartWidget();
//			JSONObject obj_json = new JSON_Construct(dto).getGraph();
//			String json = obj_json.toString();
//			widg.setJsonData(json);
//			widg.setPixelSize(600, 600);
//			
			
			//StringUtils.wordWrap(dto.getLabel(),50,Locale.getDefault());
			double dum = 0;
			for(Double d:dto.getDistribution())dum+=d;
			Chart chart = new Chart()
			   .setType(Series.Type.BAR)
//			   .setChartTitleText(wrap(dto.getLabel(),70))
	
			   .setChartTitle(new ChartTitle().setText(wrap(dto.getResearch_name()+": "+dto.getLabel(),70)).setAlign(org.moxieapps.gwt.highcharts.client.ChartTitle.Align.LEFT))
			   .setSizeToMatchContainer()
			   .setMarginRight(10)
			   			    .setLegend(new Legend()
				      .setAlign(Legend.Align.CENTER)
				      //.setVerticalAlign(Legend.Align.)
				      .setBackgroundColor("#CCCCCC")
				      .setShadow(true)
				   )
//			   .setWidth100()
//			   .setHeight100()
//			    .setLegend(new Legend()
//				      .setAlign(Legend.Align.RIGHT)
//				     // .setVerticalAlign(Legend.Align.LEFT)/
//				      .setBackgroundColor("#CCCCCC")
//				      .setShadow(true)
//				   )
//				
				   .setPlotBorderWidth(1)  
            .setPlotShadow(true)  
            .setBarPlotOptions(new BarPlotOptions()
            	.setAllowPointSelect(false)
            	.setCursor(PlotOptions.Cursor.POINTER)  
//                .setDataLabels(new DataLabels()  
//                    .setEnabled(false)  
//                )  
                //.setShowInLegend(true)
            		)
//            .setPiePlotOptions(new PiePlotOptions()  
//                .setAllowPointSelect(true)  
//                  
//            )  
            .setToolTip(new ToolTip()  
                .setFormatter(new ToolTipFormatter() {  
                    public String format(ToolTipData toolTipData) {  
                        return "<b>" + toolTipData.getPointName() + "</b>: " + toolTipData.getYAsDouble() + " %";  
                    }  
                })  
            )   
				   ;
			
			//Series series = chart.createSeries()
			//		   .setName("Варианты ответа")
					   //.setPoints(points)
			//Point [] points = new Point[dto.getV_label_codes().size()];
			int i = 0;
			for(String name:dto.getV_label_values())
			{
				  NumberFormat formatter = NumberFormat.getFormat("0.0");
		          // formatter.
		           //formatter.setMaximumFractionDigits(2);
				  Double d = new Double(0);
			      if(dto.getDistribution()!=null && dto.getDistribution().get(i)!=null)
				  {
				      d = dto.getDistribution().get(i);
				  }
				  double ddd = (d/dum)*100;
				  String st = formatter.format(ddd);
				  if(st.contains(","))st = st.replaceAll(",", ".");
		           double myNumber = Double.parseDouble(st);
		         
		           
				//points[i] = (new Point(name,myNumber));
		         Point point = new Point(name,myNumber);
		         Series serie = chart.createSeries().addPoint(point).setName(name);
		         	chart.addSeries(serie);
							
				i++;
			}
			
			//points[0].setSelected(true).setSliced(true);
//			new Point[]{  
//	                new Point("Firefox", 45.0),  
//	                new Point("IE", 26.8),  
//	                new Point("Chrome", 12.8)  
//	                    .setSliced(true)  
//	                    .setSelected(true),  
//	                new Point("Safari", 8.5),  
//	                new Point("Opera", 6.2),  
//	                new Point("Others", 0.7)  
//	            } 
//		   )
			chart.getYAxis()
				.setMin(0)
				.setAxisTitle(
						new AxisTitle().setText("Количество кейсов (%)").setAlign(org.moxieapps.gwt.highcharts.client.AxisTitle.Align.HIGH))
				.setLabels(new YAxisLabels().setEnabled(false));
				;
				chart.getXAxis().setLabels(new XAxisLabels().setEnabled(false));
			
					main_html.add(chart);
//			final PopupPanel dialogBox = createDialogBox(chart);
//		    dialogBox.setGlassEnabled(true);
//		    dialogBox.setAnimationEnabled(true);
//		    
//			dialogBox.center();
//	        dialogBox.show();
		}

	}
	
	private PopupPanel createDialogBox(Widget w) {
	    // Create a dialog box and set the caption text
	    final PopupPanel dialogBox = new PopupPanel();
	    //dialogBox.setText("График распределения");
	    dialogBox.setHeight("450px");
	    dialogBox.setWidth("700px");
//	    dialogBox.setHeight("100%");
//	    dialogBox.setWidth("100%");
	    // Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogContents.setWidth("100%");
	    dialogContents.setHeight("100%");
	    
	    dialogContents.setSpacing(4);
	    
	 
	    // Add some text to the top of the dialog
	   

	    // Add a close button at the bottom of the dialog
	    Button closeButton = new Button("Закрыть", new ClickHandler() {
	          public void onClick(ClickEvent event) {
	            dialogBox.hide();
	            plot_viewed = false;
	          }
	        });
	    dialogContents.add(w);
	    dialogContents.add(closeButton);
	    dialogBox.setWidget(dialogContents);
	    return dialogBox;
	    
	    
	  }
}
