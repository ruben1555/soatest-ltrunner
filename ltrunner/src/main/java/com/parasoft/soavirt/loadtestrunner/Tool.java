package com.parasoft.soavirt.loadtestrunner;

import com.parasoft.api.tool.*;
import com.parasoft.soavirt.loadtestrunner.project.ExecutionScript;
import com.parasoft.soavirt.loadtestrunner.project.ProjectFile;
import com.parasoft.soavirt.loadtestrunner.project.ReportReader;
import com.parasoft.soavirt.loadtestrunner.project.WorkingDirectory;

public class Tool implements ICustomTool{

	public boolean acceptsInput(IToolInput input, ICustomToolConfiguration config) {
		
		return true;
	}
	
	public boolean isValidConfig(ICustomToolConfiguration config) {
		
		//TODO check input fields
		
		return true;
	}

	public boolean execute(IToolInput input, IToolContext context) throws CustomToolException, InterruptedException {
		
		ICustomToolConfiguration config = context.getConfiguration();
		
		// create execution directory
		WorkingDirectory dir = new WorkingDirectory(context.getAbsolutePathFile("work"));
		
		// generate settings file
		dir.writeToFile("localsettings.properties", "console.log=enable");
		
		// loadtest script
		ExecutionScript script = new ExecutionScript();
		script.setMinutes(config.getString("scenario_minutes"));
		script.setVus(config.getString("scenario_vus"));
			
		// generate loadtest script
		dir.writeToFile("loadtest.script", script.generateScript());
		
		// loadtest project file
		ProjectFile project = new ProjectFile();
		project.setTst(context.getAbsolutePathFile(config.getString("context_tst")).getPath());
		project.setScenario(config.getString("context_scenario"));
		
		// read QoS values and inject in report (left default when empty)
		project.getMetric("SUCCES").setThreshold(config.getString("qos_succes"));
		project.getMetric("FAILURES").setThreshold(config.getString("qos_failures"));
		project.getMetric("HITS").setThreshold(config.getString("qos_hits"));
		project.getMetric("EXECUTION").setThreshold(config.getString("qos_execution"));
		project.getMetric("SERVER").setThreshold(config.getString("qos_server"));
		
		// generate project file based on template
		dir.writeToFile("project.lt", project.generateProjectFileContents());
		
		// execute lt.exe
		new Executor(dir.getPath());
		
		// read report files
		ReportReader rr = new ReportReader(dir); 
		
		// read failed metrics and report to SOAtest
		rr.getFailedMetrics().forEach((m) -> context.report(m.toString(), m.getDetails()));
		
		// generate XML output
		IToolInput xml = new DefaultDomInput(rr.getXmlDoc());
		context.getOutputManager().runOutput("report_xml", xml, context);

		// generate HTML output
		IDataInput test = new DefaultDataInput(rr.getHtmlRedirect().getBytes(), null, "text/html", "html");
		context.getOutputManager().runOutput("report_html", test, context);
		
		return true;
	}
}