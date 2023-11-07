package com.parasoft.soavirt.loadtestrunner.project;

public class ExecutionScript {
	
	private String minutes, vus; 
	
	public ExecutionScript() { }
	
	public void setMinutes (String minutes) {
		this.minutes = minutes;
	}
	
	public void setVus (String vus) {
		this.vus = vus;
	}
	
	public String generateScript() {

		String line1 = "open project.lt";
		String line2 = "loadtest -settings localsettings.properties -minutes "+minutes+" -vus "+vus+" -allReports report \"Steady Load\"";
		
		return line1 + "\n" + line2;
	}
}