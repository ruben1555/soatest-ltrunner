package com.parasoft.soavirt.loadtestrunner.project;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import com.parasoft.api.Application;

public class ReportReader {

	private Document xmlDoc;
	private WorkingDirectory dir;
	private ArrayList<QosMetric> failedMetrics;
	
	public ReportReader(WorkingDirectory dir) {
		
		this.dir = dir;
		this.xmlDoc = readXmlReport();
	}
	
	private Document readXmlReport() {
		Document xmlDoc = null;
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			File xmlReport = dir.readFile("report" + File.separator + "report.xml");
			xmlDoc = db.parse(xmlReport);
		
		} catch (Exception e) {
			Application.showMessage("Error reading report: " + e);
		}
		
		failedMetrics = extractFailedMetrics(xmlDoc);
		
		return xmlDoc;
	}
	
	private ArrayList<QosMetric> extractFailedMetrics(Document doc) {
		failedMetrics = new ArrayList<QosMetric>();
		
		// read metric results from XML report
		NodeList qosMetrics = doc.getElementsByTagName("QoSMetric");
		
		// save every failed metric
		for (int i = 0; i < qosMetrics.getLength(); ++i) {
			Element qosMetric = (Element) qosMetrics.item(i);
			
            if (qosMetric.getAttribute("status").equals("failure"))    
                failedMetrics.add(new QosMetric(
                		qosMetric.getAttribute("name"),
                		qosMetric.getElementsByTagName("MetricDescription").item(0).getAttributes().getNamedItem("value").getTextContent(),
                		qosMetric.getElementsByTagName("StatusDetails").item(0).getAttributes().getNamedItem("value").getTextContent()));
		}
		
		return failedMetrics;
	}
	
	public ArrayList<QosMetric> getFailedMetrics() {
		return failedMetrics;
	}
	
	public Document getXmlDoc() {
		
		return xmlDoc;
	}
	
	public String getHtmlRedirect() {
		
		return "<!DOCTYPE html><html><head><meta http-equiv=\"refresh\" content=\"0; url='" + dir.getPath() + "/report/index.html'\" /></head><body></body></html>";
	}
}