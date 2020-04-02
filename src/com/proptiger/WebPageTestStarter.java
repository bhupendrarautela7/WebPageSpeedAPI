package com.proptiger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WebPageTestStarter {
	
	static String testId = null;

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

		FileInputStream newFile1 = new FileInputStream("./Input/Main3.xls"); 
		HSSFWorkbook workbook = new HSSFWorkbook(newFile1);
		HSSFSheet sheet = workbook.getSheetAt(0);

		
       // for(int i=1;i<=4;i++)
       for(int i=1;i<=sheet.getLastRowNum();i++)

        {
			 Row CurrentRow = sheet.getRow(i);
			// Cell URLcell =CurrentRow.getCell(0);
        	
        	String TestURL =sheet.getRow(i).getCell((short) 1).getStringCellValue();        	
        	
        	String Domain ="http://beta.makaan-ws.com/";
        	//String Domain ="http://www.makaan.com/";
        	
        	//Key 1= A.9465251edf07ff97245e3ea1c17593d6
        	//Key 2 = A.cb8c3418338480e1e32dcdbb33033372
        			
        	
           	String URLs= "http://www.webpagetest.org/runtest.php?url="+Domain+TestURL+"&runs=1&f=xml&k=A.cb8c3418338480e1e32dcdbb33033372";
	
           	System.out.println(URLs);
           	
           	URL url = new URL(URLs);
           	
    		URLConnection urlc = url.openConnection();
    		
    		urlc.setDoOutput(true);
    		urlc.setAllowUserInteraction(false);
    		
    		PrintStream ps = new PrintStream(urlc.getOutputStream());
    		// ps.print(query);
    		ps.close();
    		
    		
    		
    		BufferedReader br = new BufferedReader(new InputStreamReader(
    				urlc.getInputStream()));
    		String l = null;
    		
    		File f = new File ("Output/TestData.xml");
			
			if (f.exists())
			{
				f.delete();
				f.createNewFile();
			}

			PrintWriter output = new PrintWriter(new FileWriter("Output/TestData.xml", true));
			PrintWriter pw = new PrintWriter(output);
			
    		while ((l = br.readLine()) != null) {
    				pw.append(l+"\n");
    				System.out.println(l);  //ON/OF PRINTING DATA
    		}

    		br.close();
    		output.close();
    		
    		File fXmlFile = new File("Output/TestData.xml");
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory
    				.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		Document doc = dBuilder.parse(fXmlFile);

    		doc.getDocumentElement().normalize();
    		
    		System.out.println("----------------------------");

    		System.out.println("Reading xml test data");

    		NodeList nList = doc.getElementsByTagName("data");

    		for (int temp = 0; temp < nList.getLength(); temp++) {

    			Node nNode = nList.item(temp);

    			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

    				Element eElement = (Element) nNode;
    				 
    				 testId = eElement.getElementsByTagName("testId").item(0)
 							.getTextContent();

    				System.out.println("TEST START ID  : " + testId);}}
    		
 			CurrentRow.createCell(2).setCellValue(testId);

 			CurrentRow.createCell(6).setCellValue("https://www.webpagetest.org/result/"+testId);
 			
 			
 			System.out.println(CurrentRow.getCell(2).getStringCellValue());
 		    		
    		System.out.println("----------------------------");
    		
    		 FileOutputStream fos = new FileOutputStream("./Input/Main3.xls");
    			workbook.write(fos);
    			fos.close();
    		    		
    			      	
        }
        FileOutputStream fos = new FileOutputStream("./Input/Main3.xls");
		workbook.write(fos);
		fos.close();

       //s newFile1.close();

		
	

}
}