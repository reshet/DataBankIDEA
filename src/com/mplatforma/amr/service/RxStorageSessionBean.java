/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mplatforma.amr.service;

import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import com.mplatforma.amr.entity.RxBlobStored;
import com.mplatforma.amr.entity.UserAccount;
import com.mresearch.databank.shared.RxStoredDTO;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.*;
import java.util.List;

/**
 *
 * @author reshet
 */
@WebService
@Stateless(mappedName="RxStorageRemoteBean",name="RxStorageRemoteBean")
public class RxStorageSessionBean implements RxStorageBeanRemote{

    @PersistenceContext
    private EntityManager em;
    
    public EntityManager getEM()
    {
        return em;
    }
    @Override
    public long storeFile(long length, String name, String desc) {
        RxBlobStored blob = new RxBlobStored(length, name, desc);
        em.persist(blob);

        long id = blob.getId();
        /*File content = new File("/home/reshet/databank/"+AdminSocioResearchMDB.INDEX_NAME+"/"+id);
        try {
            content.createNewFile();
            FileOutputStream fos = new FileOutputStream(content);
            fos.write(cont);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
        //storeDBtoFiles();

        return blob.getId();
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    private void storeDBtoFiles() {
        //List<RxStoredDTO> blob =
        //em.скуфеу;
        TypedQuery<Long> q = em.createQuery("SELECT b.id FROM RxBlobStored b",Long.class);
        List<Long> lst = q.getResultList();
        for(Long bl:lst){
            long id = bl;
            RxBlobStored blob = em.find(RxBlobStored.class,id);
            File content = new File("/home/reshet/databank/"+AdminSocioResearchMDB.INDEX_NAME+"/"+id);
            try {
                content.createNewFile();
                FileOutputStream fos = new FileOutputStream(content);
                fos.write(blob.getContents());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }


        //return blob.getId();
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] getFileContents(long id) {
        RxBlobStored stored = em.find(RxBlobStored.class, id);
        byte [] store = stored.getContents();
        if(store != null) return store;
        byte [] arr = new byte[0];
        File content = new File("/home/reshet/databank/"+AdminSocioResearchMDB.INDEX_NAME+"/"+id);
        try {
            arr = new byte[(int)content.length()];
            FileInputStream fis = new FileInputStream(content);
            fis.read(arr);
            //fis.
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return arr;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RxStoredDTO getFileInfo(long id) {
        RxBlobStored stored = em.find(RxBlobStored.class, id);
        return stored.toDTO();
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteFile(long file_id) {
         try
         {
            RxBlobStored stored = em.find(RxBlobStored.class, file_id);
            em.remove(stored);
             File content = new File("/home/reshet/databank/"+AdminSocioResearchMDB.INDEX_NAME+"/"+file_id);
             content.delete();
             return true;
         }
         finally
         {
             return false;
         }
    }
    
   
    
}
