/* ====================================================================
 * 
 * The ObjectStyle Group Software License, Version 1.0 
 *
 * Copyright (c) 2002 The ObjectStyle Group 
 * and individual authors of the software.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        ObjectStyle Group (http://objectstyle.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "ObjectStyle Group" and "Cayenne" 
 *    must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact andrus@objectstyle.org.
 *
 * 5. Products derived from this software may not be called "ObjectStyle"
 *    nor may "ObjectStyle" appear in their names without prior written
 *    permission of the ObjectStyle Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE OBJECTSTYLE GROUP OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the ObjectStyle Group.  For more
 * information on the ObjectStyle Group, please see
 * <http://objectstyle.org/>.
 *
 */
package org.objectstyle.wolips.core.resources;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.objectstyle.wolips.logging.WOLipsLog;

/**
 * @author ulrich
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WOLipsModel implements IWOLipsModel {
	private static WOLipsModel wolipsModel;
	private final static int UNKNOWN_RESOURCE_TYPE = -1;
	private final static int UNKNOWN_COMPILATION_UNIT_TYPE = -1;
	private final static String[] RESOURCE_TYPES =
		new String[] {
			WOComponentBundle.class.getName(),
			WOComponentDefinition.class.getName(),
			WOComponentHtml.class.getName(),
			WOComponentWoo.class.getName(),
			WOComponentApi.class.getName()};
	public final static String WOCOMPONENT_BUNDLE_EXTENSION = "wo";
	public final static String WOCOMPONENT_WOD_EXTENSION = "wod";
	public final static String WOCOMPONENT_HTML_EXTENSION = "html";
	public final static String WOCOMPONENT_WOO_EXTENSION = "woo";
	public final static String WOCOMPONENT_API_EXTENSION = "api";
	public final static String[] BUNDLE_TYPES =
		new String[] { WOLipsModel.WOCOMPONENT_BUNDLE_EXTENSION };
	public final static int[] BUNDLE_TYPES_TO_RESOURCE_TYPE_MAPPING =
		new int[] { IWOLipsResource.WOCOMPONENT_BUNDLE };
	public final static String[] FILE_TYPES =
		new String[] {
			WOLipsModel.WOCOMPONENT_WOD_EXTENSION,
			WOLipsModel.WOCOMPONENT_HTML_EXTENSION,
			WOLipsModel.WOCOMPONENT_WOO_EXTENSION,
			WOLipsModel.WOCOMPONENT_API_EXTENSION };
	public final static int[] FILE_TYPES_TO_RESOURCE_TYPE_MAPPING =
		new int[] {
			IWOLipsResource.WOCOMPONENT_WOD,
			IWOLipsResource.WOCOMPONENT_HTML,
			IWOLipsResource.WOCOMPONENT_WOO,
			IWOLipsResource.WOCOMPONENT_API };
	private static final String[] COMPILATION_UNITS =
		new String[] { WOComponentJava.class.getName()};
	private static final String[] COMPILATION_UNIT_SUPER_TYPES =
		new String[] { "WOComponent" };

	private WOLipsModel() {
		super();
	}

	/**
	 * @param Returns the IWOLipsResource if the resource is a WOLips resource. Otherwise null is returned.
	 * @return
	 */
	public final IWOLipsResource getWOLipsResource(IResource resource) {
		final int resourceType = this.getWOLipsResourceType(resource);
		if (resourceType == WOLipsModel.UNKNOWN_RESOURCE_TYPE)
			return null;
		Class clazz;
		WOLipsResource wolipsResource;
		try {
			clazz =
				this.getClass().getClassLoader().loadClass(
					RESOURCE_TYPES[resourceType]);
			wolipsResource = (WOLipsResource) clazz.newInstance();
			wolipsResource.setCorrespondingResource(resource);
		} catch (InstantiationException e) {
			wolipsResource = null;
			WOLipsLog.log(e);
		} catch (IllegalAccessException e) {
			wolipsResource = null;
			WOLipsLog.log(e);
		} catch (ClassNotFoundException e) {
			wolipsResource = null;
			WOLipsLog.log(e);
		} finally {
			clazz = null;
		}
		return wolipsResource;
	}
	/**
	 * @param resource
	 * @return Returns true is the resource is a WOLips resource.
	 */
	public final boolean isWOLipsResource(IResource resource) {
		return this.getWOLipsResourceType(resource)
			== WOLipsModel.UNKNOWN_RESOURCE_TYPE;
	}

	/**
	 * @param Returns the IWOLipsResource if the resource is a WOLips resource. Otherwise null is returned.
	 * @return
	 */
	public final IWOLipsCompilationUnit getWOLipsCompilationUnit(ICompilationUnit compilationUnit) {
		final int compilationUnitType =
			this.getWOLipsCompilationUnitType(compilationUnit);
		if (compilationUnitType == WOLipsModel.UNKNOWN_COMPILATION_UNIT_TYPE)
			return null;
		Class clazz;
		WOLipsCompilationUnit wolipsCompilationUnit;
		try {
			clazz =
				this.getClass().getClassLoader().loadClass(
					COMPILATION_UNITS[compilationUnitType]);
			wolipsCompilationUnit = (WOLipsCompilationUnit) clazz.newInstance();
			wolipsCompilationUnit.setCorrespondingCompilationUnit(
				compilationUnit);
		} catch (InstantiationException e) {
			wolipsCompilationUnit = null;
			WOLipsLog.log(e);
		} catch (IllegalAccessException e) {
			wolipsCompilationUnit = null;
			WOLipsLog.log(e);
		} catch (ClassNotFoundException e) {
			wolipsCompilationUnit = null;
			WOLipsLog.log(e);
		} finally {
			clazz = null;
		}
		return wolipsCompilationUnit;
	}

	/**
	 * @param resource
	 * @return Returns true is the ICompilationUnit is a WOLips CompilationUnit.
	 */
	public final boolean isWOLipsCompilationUnit(ICompilationUnit compilationUnit) {
		return this.getWOLipsCompilationUnitType(compilationUnit)
			== WOLipsModel.UNKNOWN_COMPILATION_UNIT_TYPE;
	}

	private final int getWOLipsResourceType(IResource resource) {
		int resourceType = WOLipsModel.UNKNOWN_RESOURCE_TYPE;
		if (resource.getType() == IResource.FOLDER) {
			for (int i = 0; i < WOLipsModel.BUNDLE_TYPES.length; i++) {
				if (resource.getName().endsWith(WOLipsModel.BUNDLE_TYPES[i])) {
					resourceType =
						WOLipsModel.BUNDLE_TYPES_TO_RESOURCE_TYPE_MAPPING[i];
					continue;
				}
			}
		} else if (resource.getType() == IResource.FILE) {
			for (int i = 0; i < WOLipsModel.FILE_TYPES.length; i++) {
				if (resource.getName().endsWith(WOLipsModel.FILE_TYPES[i])) {
					resourceType =
						WOLipsModel.FILE_TYPES_TO_RESOURCE_TYPE_MAPPING[i];
					continue;
				}
			}
		}
		return resourceType;
	}

	private final int getWOLipsCompilationUnitType(ICompilationUnit compilationUnit) {
		IType[] types = null;
		int compilationUnitType = WOLipsModel.UNKNOWN_COMPILATION_UNIT_TYPE;
		try {
			types = compilationUnit.getAllTypes();
			compilationUnitType = this.getWOLipsCompilationUnitType(types);
		} catch (JavaModelException e) {
			WOLipsLog.log(e);
		}
		return compilationUnitType;
	}

	public final int getWOLipsCompilationUnitType(IType[] types) {
		int compilationUnitType = WOLipsModel.UNKNOWN_COMPILATION_UNIT_TYPE;
		for (int i = 0; i < types.length; i++) {
			compilationUnitType = this.getWOLipsCompilationUnitType(types[i]);
			if (compilationUnitType
				!= WOLipsModel.UNKNOWN_COMPILATION_UNIT_TYPE)
				continue;
		}
		return compilationUnitType;
	}

	public final int getWOLipsCompilationUnitType(IType type) {
		int compilationUnitType = WOLipsModel.UNKNOWN_COMPILATION_UNIT_TYPE;
		try {
			for (int i = 0;
				i < WOLipsModel.COMPILATION_UNIT_SUPER_TYPES.length;
				i++) {
				if (WOLipsModel
					.COMPILATION_UNIT_SUPER_TYPES[i]
					.equals(type.getSuperclassName())) {
					compilationUnitType = i;
					continue;
				}

			}
		} catch (JavaModelException e) {
			WOLipsLog.log(e);
		}
		return compilationUnitType;
	}
	/**
	 * @return Returns the shared instance.
	 */
	public static IWOLipsModel getSharedWOLipsModel() {
		if (wolipsModel == null)
			wolipsModel = new WOLipsModel();
		return wolipsModel;
	}

}
