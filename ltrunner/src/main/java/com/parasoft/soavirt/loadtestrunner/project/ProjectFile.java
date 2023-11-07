package com.parasoft.soavirt.loadtestrunner.project;

import java.nio.file.Files;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

import com.parasoft.api.Application;

public class ProjectFile {

	private String tst, scenario;
	private Map<String, QosMetric> metrics; 
	
	public ProjectFile() { 
		metrics = new HashMap<String, QosMetric>();
		metrics.put("SUCCES", new QosMetric("100% Success","","{{SUCCES}}","0"));
		metrics.put("FAILURES", new QosMetric("No Failures","","{{FAILURES}}","0"));
		metrics.put("HITS", new QosMetric("Fast Hit Rate","","{{HITS}}","10"));
		metrics.put("EXECUTION", new QosMetric("Low Execution Time","","{{EXECUTION}}","750"));
		metrics.put("SERVER", new QosMetric("Low Server Time","","{{SERVER}}","500"));
	}
	
	public void setTst(String tst) {
		this.tst = tst;
	}
	
	public void setScenario(String scenario) {
		this.scenario = scenario;
	}
	
	public String generateProjectFileContents() {

		String content = "";
		
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("project.lt").getFile());
			content = Files.readString(file.toPath());
			
			// replace TST file location and scenario
	        content = content.replace("{{TST}}", tst).replace("{{SCENARIO}}", scenario);
	        
	        // replace metric values
	        for (QosMetric metric : metrics.values())
	        	content = content.replace(metric.getPlaceholder(), metric.getThreshold());
	        
		} catch (Exception e) {
			Application.showMessage("Error reading project file template: " + e);
		}
		
		return content;
	}
	
	public QosMetric getMetric(String key) {
		
		return metrics.get(key);
	}
}