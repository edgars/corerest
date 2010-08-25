/******************************************************************************
 * NuvemWare                                                                  *
 * Copyright 2010, Nuvemware , LTDA, and individual                           *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/

package com.nuvemware.corerest.service;

import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.nuvemware.corerest.framework.RestParser;
import com.nuvemware.corerest.pojo.Script;
import com.nuvemware.corerest.pojo.dao.ScriptDataObject;

import javax.script.*;


/**
 * This is the base REST Service based on JAX-RS, this will allow any request to be processed
 *
 * @author <a href="mailto:edgar.silva@gmail.com">Edgar Silva</a>
 * @author <a href="mailto:jefferson@facilitait.com.br">Jefferson Ankiewsky Silva</a>
 * @version 1.0
 */
@Provider
@Path("/")

public class RootService implements java.io.Serializable {
	
	
	
	private static final long serialVersionUID = -2125264566068793898L;
	Script script = null;
	
	protected ScriptDataObject dao = new ScriptDataObject(Script.class);
	
    // create a script engine manager
    protected ScriptEngineManager factory = new ScriptEngineManager();
    
    protected ScriptEngine engineGroovy = factory.getEngineByName(Script.GROOVY);
    
    protected ScriptEngine engineRuby = factory.getEngineByName(Script.RUBY);

    protected RestParser parser ;


	@GET
	@Path("/")
	@Produces("text/plain")
	public String homePlain(){
		
		return "Welcome to RestEasy running on Google App - Client Plain/Text";
	}
	
	@GET
	@Path("/")
	@Produces("text/html")
	public String homeHTML(){
		
		return "<h1><font face='Verdanda'>Welcome to RestEasy running on Google App - Client HTML</font></h1>";
	}
	
	/**
	 * This is the Service Execution itself, here we will receive 2 parameters
	 * serviceName through the @PathParam name, and a list of VARs
	 * It will call the Script Engine from JVM and will execute the service!
	 * */
	@GET
	@Path("/{name}/{vars:.*}")
	@Produces("text/plain")	
	public String executeWithGet(@PathParam("name") String serviceName, @PathParam("vars") String vars){
		
		Object execution = null;
		try {
			script = (Script) dao.get(serviceName);
		} catch (EntityNotFoundException e) {
			return "A Service with the name " + serviceName + " was not found in our service Repository...";
		}
		
		parser = new RestParser(script.getUri());
		parser.parseTemplate();
		
		if (Script.GROOVY.equalsIgnoreCase(script.getLanguage())) {
			
			for (Entry<String, String> variable : parser.getVariableValues("/".concat(vars)).entrySet()) {

				engineGroovy.put(variable.getKey(), variable.getValue());
				
			}
			
			try {
				execution =  engineGroovy.eval(script.getSource());
			} catch (ScriptException e) {
				execution= "Errors found in the script execution: " + e.getMessage();
			}
			
		}
		
		if (Script.RUBY.equalsIgnoreCase(script.getLanguage())) {
			
			
			
			System.out.println("Factories: " + factory.getEngineFactories());
			
			for (ScriptEngineFactory var : factory.getEngineFactories()) {
				System.out.println(String.format("Engine %s , Language:(%s), Version %s",var.getEngineName(), var.getLanguageName(), var.getLanguageVersion()));
			}
			
			System.out.println("parsing >>>>" + parser.getVariableValues("/".concat(vars)) );
			System.out.println("VARs >>>>" + vars);
			System.out.println("uri>>>>" + script.getUri() );
			
			
			
			for (Entry<String, String> variable : parser.getVariableValues("/".concat(vars)).entrySet()) {
				   
                factory.put(variable.getKey(), variable.getValue());		    	
				
				
			} 
			
			try {
				engineRuby = factory.getEngineByName("ruby");
				execution =  engineRuby.eval(script.getSource());
				System.out.println("PASSOU PORRA!");
				
			} catch (Exception e) {
				execution= "Errors found in the script execution: " + e.getMessage();
				e.printStackTrace();
			}
			
		}		
		
		
		
		return execution.toString();
	}	
	

}
