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
package com.nuvemware.corerest.framework;

import java.security.InvalidParameterException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * This is the class to render the variables from a REST standardized URI
 * @author <a href="mailto:porcelli@gmail.com">Alexandre Porcelli</a>
 * @author <a href="mailto:edgar.silva@gmail.com">Edgar Silva</a>
 * @version 1.0
 */
public class RestParser {

	final char[] content;
	final Map<Integer, String> positionedVariables = new TreeMap<Integer, String>();
	int p = 0;

	public static void main(String[] args) {
		RestParser test = new RestParser("/A/V/A/AA2323/sdf23/{1}/{afff}");
		System.out.println(test.parseTemplate().toString()
				.equals("{6=1, 7=afff}"));

		System.out.println(test
				.getVariableValues("/A/V/A/AA2323/sdf23/teste/valor")
				.toString().equals("{1=teste, afff=valor}"));

		RestParser test2 = new RestParser("/A/V/A/{dsdfgd}/sddsd/{aaa");
		System.out.println(test2.parseTemplate().toString()
				.equals("{4=dsdfgd}"));
		System.out.println(test2
				.getVariableValues("/A/V/A/aquialguma #coisa/sddsd/aaa")
				.toString().equals("{dsdfgd=aquialguma #coisa}"));

		RestParser test3 = new RestParser("/A/V/A/{dsdfgd}/sddsd/{aaa}");
		System.out.println(test3.parseTemplate().toString()
				.equals("{4=dsdfgd, 6=aaa}"));
		System.out.println(test3.getVariableValues("/A/V/A/algum%valor/sddsd")
				.toString().equals("{dsdfgd=algum%valor}"));
		System.out.println(test3
				.getVariableValues("/A/V/A/algum%valor/sddsd/a").toString()
				.equals("{dsdfgd=algum%valor, aaa=a}"));

		RestParser test4 = new RestParser("/A/V/A/{dsdfgd/sddsd/aaa}");
		System.out.println(test4.parseTemplate().toString().equals("{}"));
		System.out.println(test4.getVariableValues("/A/V/A/dsdfgd/sddsd/aaa")
				.toString().equals("{}"));

		RestParser test5 = new RestParser("/A/V/A/dsdfgd/sddsd/aaa");
		System.out.println(test5.parseTemplate().toString().equals("{}"));
		System.out.println(test5.getVariableValues("/A/V/A/dsdfgd/sddsd/aaa")
				.toString().equals("{}"));
		
		RestParser test6 = new RestParser("usa/{zipcode}");
		
		for (Entry<Integer, String> variable : test6.parseTemplate().entrySet()) {
			   System.out.println(String.format("var pos >>>>>>>>>>>>>>  %s, value %s",variable.getKey(),variable.getValue()));
			}
		System.out.println("Template: " + test6.getVariableValues("/usa/90210"));
		
		
		for (Entry<String, String> variable : test6.getVariableValues("/usa/90210").entrySet()) {
		   System.out.println(String.format("var %s, value %s",variable.getKey(),variable.getValue()));
		}
		
	}

	public RestParser(String text) {
		if (text == null) {
			throw new InvalidParameterException("text cannot be null");
		}
		this.content = text.toCharArray();
	}

	public Map<Integer, String> parseTemplate() {
		if (p > 0) {
			return positionedVariables;
		}
		int slashPos = 0;
		StringBuilder buf = new StringBuilder();
		while (p < content.length) {
			switch (content[p]) {
			case '/':
				slashPos++;
				buf = new StringBuilder();
				p++;
				break;
			case '{':
				boolean gotEndBracket = true;
				p++;
				do {
					buf.append(content[p]);
					p++;
					if (p >= content.length || content[p] == '/') {
						gotEndBracket = false;
						break;
					}
				} while (content[p] != '}');
				p++;
				if (gotEndBracket) {
					positionedVariables.put(slashPos, buf.toString());
				}
				buf = new StringBuilder();
				break;
			default:
				buf.append(content[p]);
				p++;
				break;
			}
		}
		return positionedVariables;
	}

	public Map<String, String> getVariableValues(String input) {
		Map<String, String> returnValue = new LinkedHashMap<String, String>();
		if (positionedVariables.size() == 0) {
			return returnValue;
		}
		String[] content = input.split("/");
		for (Entry<Integer, String> variable : positionedVariables.entrySet()) {
			int pos = variable.getKey();
			if (pos < content.length) {
				returnValue
						.put(variable.getValue(), content[variable.getKey()]);
			}
		}
		return returnValue;
	}
}