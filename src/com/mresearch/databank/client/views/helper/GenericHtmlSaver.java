package com.mresearch.databank.client.views.helper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.mresearch.databank.client.views.DialogBoxFactory;
import com.mresearch.databank.client.views.HTML_Saver;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 12/26/13
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class GenericHtmlSaver extends PushButton{
    private FormPanel form;
    private TextBox save_name = new TextBox();
    private TextBox content = new TextBox();
    String realPath = GWT.getModuleBaseURL();
    HTML_Saver provider;
    private void initSaveOption()
    {
        content.setVisible(false);
        content.setName("content");
        save_name.setVisible(false);
        save_name.setName("name");

        form = new FormPanel();
        form.setAction(realPath+"htmlSave");
        form.setMethod(FormPanel.METHOD_POST);
        VerticalPanel panel = new VerticalPanel();
        panel.add(content);
        panel.add(save_name);

        form.add(panel);
    }
    public GenericHtmlSaver(HTML_Saver provider){
        initSaveOption();
        this.setStyleName("htmlBtn");
        this.setHeight("32px");
        this.setWidth("32px");

        this.provider = provider;
        this.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                //eventBus.fireEvent(new SaveHTMLEvent());

                final TextBox tb = new TextBox();
                Label l = new Label("Введите имя файла:");
                VerticalPanel vp = new VerticalPanel();
                vp.add(l);
                vp.add(tb);
                PopupPanel b = DialogBoxFactory.createDialogBox("Сохранение результатов анализа в HTML", vp, new DialogBoxFactory.closeAction() {
                    @Override
                    public void doAction() {
                        String nam = tb.getText();
                        if (nam.equals("")) nam = "cохраненное распределение.html";
                        final String name = nam;

                        save_name.setText(name);
                        content.setText(GenericHtmlSaver.this.provider.composeSpecificContent());

                        form.submit();

                        //save_dto.setName(name);
                        //save_dto.getSeting().setWeights_var_id(new Integer(getCurrentWeightId()));
                        //save_dto.getSeting().setWeights_use(getWeightsUseState());
                        //User;
                    }
                }, "Сохранить");
                b.show();
            }
        });
    }


}
