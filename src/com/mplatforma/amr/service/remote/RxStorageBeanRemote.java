package com.mplatforma.amr.service.remote;

import com.mresearch.databank.shared.RxStoredDTO;
import javax.ejb.Remote;
import java.io.FileInputStream;


@Remote
public interface RxStorageBeanRemote {
   long storeFile(long length, String name, String desc);
   byte[] getFileContents(long id);
   //FileInputStream getFileStream(long id);
   RxStoredDTO getFileInfo(long id);
   boolean deleteFile(long file_id);
}
