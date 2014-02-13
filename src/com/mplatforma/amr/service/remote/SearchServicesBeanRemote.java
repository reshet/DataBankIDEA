package com.mplatforma.amr.service.remote;

import com.mresearch.databank.shared.VarDTO_Detailed;

import javax.ejb.Remote;
import java.util.ArrayList;

@Remote
public interface SearchServicesBeanRemote {
    public void perform_delete_indexies(ArrayList<Long> ids, String type);
    public void perform_indexing(long id_research);
    public void launchIndexingVarBULKED(VarDTO_Detailed dto);
    public void perform_var_bulk_indexing();
    public void init_bulk_indexing();
}
