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
package com.mresearch.databank.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mresearch.databank.shared.SocioResearchDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserHistoryDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;


@RemoteServiceRelativePath("userAccountService")
public interface UserAccountService extends RemoteService {
  
  public UserAccountDTO login(String email, String password,String token);
  public void logout();
  public void initDefaultUsers();
  public UserHistoryDTO updateResearchState(UserHistoryDTO dto);
List<UserAnalysisSaveDTO> getUserAllAnalisysList();
List<SocioResearchDTO_Light> getMyResearchesList();
void saveResearchAnalisys(UserAnalysisSaveDTO dto);
void addToSelectedResearches(SocioResearchDTO dt);
UserResearchSettingDTO getResearchSetting(Long research_id);
}
