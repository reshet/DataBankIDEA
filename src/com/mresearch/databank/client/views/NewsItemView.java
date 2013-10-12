/** 
 * Copyright 2010 Daniel Guermeur and Amy Unruh
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   See http://connectrapp.appspot.com/ for a demo, and links to more information 
 *   about this app and the book that it accompanies.
 */
package com.mresearch.databank.client.views;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class NewsItemView extends Composite {

  private static NewsItemUiBinder uiBinder = GWT.create(NewsItemUiBinder.class);

  interface NewsItemUiBinder extends UiBinder<Widget, NewsItemView> {}

  @UiField Label title, description;

  @UiField Label date;

  public NewsItemView() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @SuppressWarnings("deprecation")
  public NewsItemView(String title, String description, Date createdOn) {
    this();
    this.title.setText(title);
    this.description.setText(description);
    this.date.setText(createdOn.toLocaleString());
  }

}
