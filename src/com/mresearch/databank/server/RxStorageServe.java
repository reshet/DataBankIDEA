package com.mresearch.databank.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ejb.EJB;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import com.mplatforma.amr.service.remote.UserAccountBeanRemote;
import com.mresearch.databank.shared.RxStoredDTO;

@RemoteServiceRelativePath("serve")
public class RxStorageServe extends HttpServlet {
   // private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	@EJB private RxStorageBeanRemote eao;
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
	


	
	
public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException {
        //BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
        //BlobInfo info = f.loadBlobInfo(blobKey);
		String id = req.getParameter("blob-key");
		long idd = Integer.parseInt(id);
        RxStoredDTO dto = eao.getFileInfo(idd);
	
		//res.setHeader("Content-Type", dto.getDesc());
        res.setHeader("Content-Type", "application/octet-stream");

        String filename =  URLEncoder.encode(dto.getName(), "UTF8");
        res.setHeader( "Content-Disposition", "attachment; filename=\"" + filename +"\"" );
        byte[] arr = eao.getFileContents(idd);

    //res.getWriter().
        //res.getOutputStream().print(new String(arr));
        ServletOutputStream output = res.getOutputStream();
        //output.write(arr);
        String str =  new String(arr);
        res.setHeader("Content-Length", String.valueOf(arr.length));
   // output.print(str);
    OutputStream stream = output;
    stream.write(arr);
        //res.getWriter().print(str);
        //res.getWriter().close();
    //output.write();
    //res.getOutputStream()
    //res.getOutputStream().
        //res.getOutputStream().close();
        //blobstoreService.serve(blobKey, res);
       
    }
}