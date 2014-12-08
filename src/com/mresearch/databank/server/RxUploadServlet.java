package com.mresearch.databank.server;

import com.mplatforma.amr.service.AdminSocioResearchMDB;
import com.mplatforma.amr.service.remote.AdminSocioResearchBeanRemote;
import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import org.apache.commons.fileupload.FileItem;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Hashtable;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//import javassist.bytecode.ByteArray;


/**
 * Servlet implementation class RxUploadServlet
 */
public class RxUploadServlet extends UploadAction {

	
	@EJB private RxStorageBeanRemote eao;
	@EJB private AdminSocioResearchBeanRemote eao2;
	/*static
	{
		Object obj = new String("some");
		try {
		  InitialContext ic = new InitialContext();
		  System.out.println("start lookup");
		  final String jndiName = "java:global/DatabankEnterprise-ejb/RxStorageRemoteBean";
		  obj = ic.lookup(jndiName);
		  System.out.println("lookup returned " + obj);
		  eao = (RxStorageBeanRemote) obj;
		  
		  
		  final String jndiName2 = "java:global/DatabankEnterprise-ejb/AdminSocioResearchRemoteBean";
		  obj = ic.lookup(jndiName2);
		  System.out.println("lookup returned " + obj);
		  eao2 = (AdminSocioResearchBeanRemote) obj;
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}*/
	  private static final long serialVersionUID = 1L;
	  
	  Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	  /**
	   * Maintain a list with received files and their content types. 
	   */
	  Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	  Hashtable<String, Long> receivedFileIds = new Hashtable<String, Long>();

	  /**
	   * Override executeAction to save the received files in a custom place
	   * and delete this items from session.  
	   */
	  @Override
	  public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
	   
		  
		String response = "";
	    int cont = 0;
	    for (FileItem item : sessionFiles) {
	      if (false == item.isFormField()) {
	        cont ++;
	        try {
	        	/// Save a list with the received files
	          String name =item.getName(); 
	          String ext = name.substring(name.lastIndexOf(".")+1,name.length());
	          int spss_extracted = 0;

	          if(ext.equals("zip"))
	          {
	        	  receivedContentTypes.put(item.getFieldName(), item.getContentType());
		          byte [] arr = new byte[(int) item.getSize()];
		          item.getInputStream().read(arr);
		          //byte[] buf = new byte[2048];
	              ZipInputStream zipinputstream = null;
	              ZipEntry zipentry;
	              zipinputstream = new ZipInputStream(new ByteArrayInputStream(arr));
	              zipentry = zipinputstream.getNextEntry();
	              while (zipentry != null)
	              { 
	                  String entryName = zipentry.getName();
	                  System.out.println(entryName);
                    String sav_ext = entryName.substring(entryName.length()-3,entryName.length());
                    System.out.println("Sav_ext: "+sav_ext);

	                  if(sav_ext.equals("sav"))
	                  {
                        int n = 0 ;
                        long id = eao.storeFile(zipentry.getSize(), entryName, zipentry.getComment());
                        FileOutputStream outputStream = null;
                        try
                        {
                            outputStream = getSaveFileStream(id);
                            byte[] buffer = new byte[4096];
                            while ((n = zipinputstream.read(buffer, 0, buffer.length)) > -1)
                            {
                              outputStream.write(buffer, 0, n);
                            }
                            outputStream.flush();
                        }
                        finally
                        {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        }
                        System.out.println("Here done reading zipped spss");
                        eao2.parseSPSS(id, zipentry.getSize());
                        spss_extracted++;
		 		            }
	                  zipinputstream.closeEntry();
	                  zipentry = zipinputstream.getNextEntry();
	              }

	              zipinputstream.close();
	              
	        	  response += "<ZipUploaded>" +spss_extracted+"</ZipUploaded>";
	          } else {
	        	  receivedContentTypes.put(item.getFieldName(), item.getContentType());
                  FileOutputStream outputStream = null;
                  long id = eao.storeFile(item.getSize(), item.getName(), item.getContentType());

                  try
                  {
                      outputStream = getSaveFileStream(id);
                      byte[] buffer = new byte[4096];
                      int bytesRead = 0;
                      InputStream input =  item.getInputStream();

                      do
                      {
                          bytesRead = input.read(buffer, 0, buffer.length);
                          outputStream.write(buffer, 0, bytesRead);
                      }
                      while (bytesRead == buffer.length);

                      outputStream.flush();
                  }
                  finally
                  {
                      if(outputStream != null)
                          outputStream.close();
                  }
		          receivedFileIds.put(item.getFieldName(),id);
			      /// Send a customized message to the client.
		          response += "<RxStoreId>" + id+"</RxStoreId>";
		       }
	        } catch (Exception e) {
	          throw new UploadActionException(e);
	        }
	      }
	    }
	    
	    /// Remove files from session because we have a copy of them
	    removeSessionFileItems(request);
	    
	    /// Send your customized message to the client.
	    return response;
	  }
	  
	  /**
	   * Get the content of an uploaded file.
	   */
	  @Override
	  public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
//	    String fieldName = request.getParameter(UploadServlet.PARAM_SHOW);
//	    File f = receivedFiles.get(fieldName);
//	    if (f != null) {
//	      response.setContentType(receivedContentTypes.get(fieldName));
//	      FileInputStream is = new FileInputStream(f);
//	      copyFromInputStreamToOutputStream(is, response.getOutputStream());
//	    } else {
//	      renderXmlResponse(request, response, ERROR_ITEM_NOT_FOUND);
//	   }
	  }
	  
	  /**
	   * Remove a file when the user sends a delete request.
	   */
	  @Override
	  public void removeItem(HttpServletRequest request, String fieldName)  throws UploadActionException {
//	    File file = receivedFiles.get(fieldName);
//	    receivedFiles.remove(fieldName);
//	    receivedContentTypes.remove(fieldName);
//	    if (file != null) {
//	      file.delete();
//	    }
	  }


    private FileOutputStream getSaveFileStream(long idd) {
        File content = new File(AdminSocioResearchMDB.STORAGE_VAULT+idd);
        FileOutputStream fos = null;
        try {
            content.createNewFile();
            fos = new FileOutputStream(content);
          } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return fos;

    }
	}
