package com.parasoft.soavirt.loadtestrunner.project;

public class QosMetric {

	private String name, description, result;
	private String threshold, placeholder;
	
	public QosMetric(String name, String description, String placeholder, String defaultValue) {
		this.name = name;
		this.placeholder = placeholder;
		this.threshold = defaultValue;
	}
	
	public QosMetric(String name, String description, String result) {
		this.name = name;
		this.description = description;
		this.result = result;
	}	
	
	public String toString() {
		return "Expected: " + name + "; Actual: " + result;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDetails() {
		return description;
	}
	
	public void setThreshold(String value) {
		if(value != null && !value.isEmpty())
			threshold = value;
	}
	
	public String getThreshold() {
		return threshold;
	}
	
	public String getPlaceholder() {
		return placeholder;
	}
}
