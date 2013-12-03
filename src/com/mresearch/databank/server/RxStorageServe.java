package com.mresearch.databank.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ejb.EJB;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mplatforma.amr.entity.RxBlobStored;
import com.mplatforma.amr.service.AdminSocioResearchMDB;
import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import com.mplatforma.amr.service.remote.UserAccountBeanRemote;
import com.mresearch.databank.shared.RxStoredDTO;

@RemoteServiceRelativePath("serve")
public class RxStorageServe extends HttpServlet {
   // private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	@EJB private RxStorageBeanRemote eao;
    @PersistenceContext private EntityManager em;
	/*static
	{
		Object obj = new String("some");
		try {
		  InitialContext ic = new InitialContext();
		  System.out.println("start lookup");
		//  final String jndiName = "java:global/DatabankEnterprise-ejb/RxStorageRemoteBean";
		  final String jndiName = "java:global/DatabankEnterprise-ejb/RxStorageRemoteBean";
			  obj = ic.lookup(jndiName);
		  System.out.println("lookup returned " + obj);
		  eao = (RxStorageBeanRemote) obj;
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}*/
	


	
//stream version
public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException {
        //BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
        //BlobInfo info = f.loadBlobInfo(blobKey);
		String id = req.getParameter("blob-key");
		long idd = Integer.parseInt(id);
        RxStoredDTO dto = eao.getFileInfo(idd);
	    res.setHeader("Content-Type", "application/octet-stream");
        String filename =  URLEncoder.encode(dto.getName(), "UTF8");
        res.setHeader( "Content-Disposition", "attachment; filename=\"" + filename +"\"" );
        ServletOutputStream output = res.getOutputStream();
        res.setHeader("Content-Length", String.valueOf(dto.getSize()));
        OutputStream stream = output;
        //stream.write(arr);


    FileInputStream inputStream = null;

        try
        {
            //inputStream = new FileInputStream(path);
            inputStream = getFileStream(idd);

            byte[] buffer = new byte[1024];
            int bytesRead = 0;

            do
            {
                bytesRead = inputStream.read(buffer, 0, buffer.length);
                stream.write(buffer, 0, bytesRead);
            }
            while (bytesRead == buffer.length);

            stream.flush();
        }
        finally
        {
            if(inputStream != null)
                inputStream.close();
        }
    }

    private FileInputStream getFileStream(long id) {
        File content = new File("/home/reshet/databank/"+ AdminSocioResearchMDB.INDEX_NAME+"/"+id);
        FileInputStream fis = null;
        try {
           fis = new FileInputStream(content);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return fis;
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}




/*

//byte [] version
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        //BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
        //BlobInfo info = f.loadBlobInfo(blobKey);
        String id = req.getParameter("blob-key");
        long idd = Integer.parseInt(id);
        RxStoredDTO dto = eao.getFileInfo(idd);
        res.setHeader("Content-Type", "application/octet-stream");
        String filename =  URLEncoder.encode(dto.getName(), "UTF8");
        res.setHeader( "Content-Disposition", "attachment; filename=\"" + filename +"\"" );
        byte[] arr = eao.getFileContents(idd);
        ServletOutputStream output = res.getOutputStream();
        String str =  new String(arr);
        res.setHeader("Content-Length", String.valueOf(arr.length));
        OutputStream stream = output;
        stream.write(arr);
    }
}*/
