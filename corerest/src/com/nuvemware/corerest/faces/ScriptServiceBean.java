package com.nuvemware.corerest.faces;


import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.googlecode.objectify.ObjectifyService;
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


	private List<Script> scripts;
	
	protected ScriptDataObject dao = new ScriptDataObject(Script.class);
	
	public ScriptServiceBean() {
		
		script = new Script();
		
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
	
	public List<Script> getScripts() {
		setScripts(dao.findAll(100));
		return scripts;
	}




	public void setScripts(List<Script> scripts) {
		this.scripts = scripts;
	}

}
