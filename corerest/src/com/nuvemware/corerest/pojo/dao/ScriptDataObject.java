package com.nuvemware.corerest.pojo.dao;

import com.googlecode.objectify.ObjectifyService;
import com.nuvemware.corerest.framework.AbstractPersistentObject;
import com.nuvemware.corerest.pojo.Script;

public class ScriptDataObject extends AbstractPersistentObject<Script> {
	
	
	
    static {
        ObjectifyService.register(Script.class);
       
    }


	public ScriptDataObject(Class<Script> clazz) {
		super(clazz);
	}

}
