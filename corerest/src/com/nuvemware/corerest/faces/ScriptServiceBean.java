package com.nuvemware.corerest.faces;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.ObjectifyService;
import com.nuvemware.corerest.framework.RestParser;
import com.nuvemware.corerest.pojo.Script;
import com.nuvemware.corerest.pojo.dao.ScriptDataObject;

@ManagedBean(name="scriptService")
@RequestScoped
public class ScriptServiceBean implements Serializable {

	private static final long serialVersionUID = -4692833236038414694L;

	static 

    {   

     // Data Container Init 
	ObjectifyService.register(Script.class);  

     }  
	private Script script;
	
	private String scriptName;
	
	private RestParser parser;


	private List<Script> scripts;
	
	protected ScriptDataObject dao = new ScriptDataObject(Script.class);
	
	public ScriptServiceBean() {
		
		if (null ==script) {
              
			script = new Script();
		}
		
	}
	



	public void setScript(Script script) {
		this.script = script;
	}

	public Script getScript() {
		return script;
	}
	
	public String save(){
		
	 	 dao.add(script);
		
         return "/services"	;	
	}
	
	public String run(){
		
	 	 dao.add(script);
		
        return "/services"	;	
	}
	
	public List<Script> getScripts() {
		setScripts(dao.findAll(100));
		return scripts;
	}




	public void setScripts(List<Script> scripts) {
		this.scripts = scripts;
	}




	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}




	public String getScriptName() {
		return scriptName;
	}

	
	public String loadScript(){
		
		try {
			this.script = (Script) dao.get("name", getScriptName());
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "pretty:edit";
	}
	
	public String loadScript4Run(){
		
		try {
			this.script = (Script) dao.get("name", getScriptName());
			
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		return "pretty:run";
	}




	public void setParser(RestParser parser) {
		this.parser = parser;
	}




	public RestParser getParser() {
		return parser;
	}
	
	public Map<Integer, String> getVars(){
		this.parser = new RestParser(this.script.getUri());
		return parser.parseTemplate();
	}
}
