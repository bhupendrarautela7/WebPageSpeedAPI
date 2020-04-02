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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.NoSuchElementException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestReportReader {
	
	static String LoadTime;
	static String TimeToFirstBite;
	static String FullyLoadTime;

	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

		
		FileInputStream newFile1 = new FileInputStream("./Input/Main3.xls"); 
		HSSFWorkbook workbook = new HSSFWorkbook(newFile1);
		HSSFSheet sheet = workbook.getSheetAt(0);
		
		
        for(int i=1;i<=sheet.getLastRowNum();i++)
        {
        	String ResultURL = "https://www.webpagetest.org/xmlResult/";
        	String TestPage =sheet.getRow(i).getCell((short) 0).getStringCellValue(); 
        	String TestId = sheet.getRow(i).getCell((short) 2).getStringCellValue();
        	String newURL = "http://www.webpagetest.org/runtest.php?url=https://www.proptiger.com&runs=1&f=xml&k=A.cb8c3418338480e1e32dcdbb33033372";
        	
			Row CurrentRow = sheet.getRow(i);
      	
           System.out.println(TestPage+": : "+" => "+ResultURL+TestId);
           
           System.out.println("Result" +": : "+" => "+"https://www.webpagetest.org/result/"+TestId);
           	
           	URL url = new URL(ResultURL+TestId+"/");
           
           //URL url = new URL(newURL);
           	
    		URLConnection urlc = url.openConnection();
    		
    		urlc.setDoOutput(true);
    		urlc.setAllowUserInteraction(false);
    		
    		PrintStream ps = new PrintStream(urlc.getOutputStream());
    		
    		ps.close();
    		
    		//---------------------------------------
    		
    		BufferedReader br = new BufferedReader(new InputStreamReader(
    				urlc.getInputStream()));
    		String l = null;
    		
    		File f = new File ("Output/FinalResult.xml");
			
			if (f.exists())
			{
				f.delete();
				f.createNewFile();
			}

			PrintWriter output = new PrintWriter(new FileWriter("Output/FinalResult.xml", true));
			PrintWriter pw = new PrintWriter(output);
			
    		while ((l = br.readLine()) != null) {
    				pw.append(l+"\n");
    			//	System.out.println(l);  //On/Off the console output print
    		}

    		br.close();
    		output.close();
    		
    		// Reading from the team file=================// 
    		
    		File fXmlFile = new File("Output/FinalResult.xml");
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory
    				.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		Document doc = dBuilder.parse(fXmlFile);

    		doc.getDocumentElement().normalize();
    		
    		System.out.println("----------------------------");

    		System.out.println("PAGE PERFORMANCE TEST DATA - ");
    		
    		try{

    		NodeList nList = doc.getElementsByTagName("data");

    		for (int temp = 0; temp < nList.getLength(); temp++) {

    			Node nNode = nList.item(temp);

    			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

    				Element eElement = (Element) nNode;
    				
    				 
    				TimeToFirstBite = eElement.getElementsByTagName("TTFB").item(0)
     						.getTextContent();
    				LoadTime = eElement.getElementsByTagName("loadTime").item(0)
     						.getTextContent();
    				FullyLoadTime = eElement.getElementsByTagName("fullyLoaded").item(0)
     						.getTextContent();
    				
    				int numTTFB = Integer.parseInt(TimeToFirstBite);
    				int numloadtime = Integer.parseInt(LoadTime);
    				int numfullyLoaded = Integer.parseInt(FullyLoadTime);
    				
    				    System.out.println("Time to First Bite Load  : " + numTTFB);
    				    System.out.println("Time to Load Page  : " + numloadtime);
        				System.out.println("Time to fully Page Load   : " + numfullyLoaded);

    			}}
    		
    		//testId = eElement.getElementsByTagName("testId").item(0)
				//	.getTextContent();
    		
    		CurrentRow.createCell(3).setCellValue(TimeToFirstBite);
 			CurrentRow.createCell(4).setCellValue(LoadTime);			
 			CurrentRow.createCell(5).setCellValue(FullyLoadTime);

 			
 			//System.out.println(CurrentRow.getCell(2).getStringCellValue());
 		    		
    		System.out.println("----------------------------");
    		}
    		catch(NoSuchElementException e) {
    			
     			System.out.println("This web page performance test analysis is still in queue, Please try after some time !!!");
    			
    			
    		}
    		
    		  FileOutputStream fos = new FileOutputStream("Input/Main3.xls");
    			workbook.write(fos);
    			fos.close();
    	
    			      	
        }
        FileOutputStream fos = new FileOutputStream("Input/Main3.xls");
		workbook.write(fos);
		fos.close();

    		
        }


}
	

