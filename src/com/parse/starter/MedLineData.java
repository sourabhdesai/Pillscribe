package com.parse.starter;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: neel
 * Date: 3/29/13
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class MedLineData {

        public static String caller(String disease) throws IOException
        {
            // Make a URL to the web page
            URL url = new URL("http://wsearch.nlm.nih.gov/ws/query?db=healthTopics&term="+disease+"&retmax=1");

            // Get the input stream through URL Connection
            URLConnection con = url.openConnection();
            InputStream is =con.getInputStream();

            // Once you have the Input Stream, it's just plain old Java IO stuff.

            // For this case, since you are interested in getting plain-text web page
            // I'll use a reader and output the text content to System.out.

            // For binary content, it's better to directly read the bytes from stream and write
            // to the target file.


            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = null, xml="";

            // read each line and write to System.out
            while ((line = br.readLine()) != null)
            {
                xml=xml+line;
            }
            xml=remove(xml);
            return xml;
        }
        static String remove (String xml)
        {
            System.out.print(xml);
            int a=xml.indexOf("<content name=\"FullSummary\">"),b;
            if (a<0) return "yo";
            xml=xml.substring(a,xml.length()-1);
            b=xml.indexOf("</content>");
            xml=xml.substring(28,b); //remove "<content name=\"FullSummary\">" from the start of the string
            while(xml.indexOf("&gt;")>=0)
                xml=inter(xml);
            return xml;
        }
        static String inter(String xml)
        {
            int a = xml.indexOf("&lt;");
            int b = xml.indexOf("&gt;");
            xml=xml.substring(0,a)+xml.substring(b+4,xml.length());
            return xml;
        }
    }

