package com.mplatforma.amr.service;
import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 9/9/13
 * Time: 9:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class JMSProperties {
    //private static JMSProperties ourInstance = new JMSProperties();

    /*public static JMSProperties getInstance() {
        return ourInstance;
    }*/
    //private static Properties props;
   /* static{
        try {
            props = new Properties();
            props.load(JMSProperties.class.getResourceAsStream("jms.properties"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }*/
    public static final String sss = "dd";
   // public static final String jms_mapped_name = props.getProperty("jms/spss_parse");;
   /* public static final String getJMSName()
    {
        return props.getProperty("jms/spss_parse");
    }*/
    private JMSProperties() {

    }
}
