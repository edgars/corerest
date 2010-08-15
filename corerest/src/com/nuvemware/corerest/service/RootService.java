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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;


/**
 * This is the base REST Service based on JAX-RS, this will allow any request to be processed
 *
 * @author <a href="mailto:edgar.silva@gmail.com">Edgar Silva</a>
 * @author <a href="mailto:jefferson@facilitait.com.br">Jefferson Ankiewsky Silva</a>
 * @version 1.0
 */
@Provider
@Path("/")
public class RootService {
	
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
	

}
