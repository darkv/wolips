/* ====================================================================
*
* The ObjectStyle Group Software License, Version 1.0
*
* Copyright (c) 2004 The ObjectStyle Group
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

package org.objectstyle.wolips.variables;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.objectstyle.woenvironment.env.WOEnvironment;
import org.objectstyle.woenvironment.env.WOVariables;
import org.objectstyle.wolips.ant.runner.RunAnt;
/**
 * The main plugin class to be used in the desktop.
 */
public class VariablesPlugin extends AbstractUIPlugin {
	private static final String build_user_home_properties = "woproperties.xml";
	private static final String build_user_home_properties_pde_info =
		"PDE User please copy "
			+ VariablesPlugin.build_user_home_properties
			+ " from the woproject/projects/buildscripts to the wolips variables plugin.";

	private static final String PLUGIN_ID = "org.objectstyle.wolips.variables";
	//The shared instance.
	private static VariablesPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	private WOEnvironment woEnvironment;
	/**
	 * The constructor.
	 */
	public VariablesPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		try {
			resourceBundle = ResourceBundle
					.getBundle("org.objectstyle.wolips.variables.VariablesPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		this.myStartup();
	}
	private void logError(String string) {
		this.getLog().log(		new Status(
				IStatus.ERROR,
				VariablesPlugin.getPluginId(),
				IStatus.ERROR,
				string,
				null));
	}
	private void myStartup() {
		try {
			this.setUpPreferencesForPropertiesFile();
			this.writePropertiesFileToUserHome();
			this.setUpPreferencesAfterPropertiesFile();
		} catch (Exception anException) {
			this.logError(
				VariablesPlugin.build_user_home_properties_pde_info + " " + anException.getStackTrace());
		}
	}
	/**
	 * Method setUpPreferencesForPropertiesFile.
	 */
	private void setUpPreferencesForPropertiesFile() {
		String currentVersion =
			VariablesPlugin
				.getDefault()
				.getDescriptor()
				.getVersionIdentifier()
				.toString();
		String preferencesVersion =
			Preferences.getPREF_WOLIPS_VERSION_EARLY_STARTUP();
		if (!currentVersion.equals(preferencesVersion))
			Preferences.setPREF_REBUILD_WOBUILD_PROPERTIES_ON_NEXT_LAUNCH(true);
	}
	/**
	 * Method setUpPreferencesAfterPropertiesFile.
	 */
	private void setUpPreferencesAfterPropertiesFile() {
		Preferences.setPREF_REBUILD_WOBUILD_PROPERTIES_ON_NEXT_LAUNCH(false);
		Preferences.setPREF_WOLIPS_VERSION_EARLY_STARTUP(
				VariablesPlugin
				.getDefault()
				.getDescriptor()
				.getVersionIdentifier()
				.toString());
	}
	/**
	 * Method writePropertiesFileToUserHome.
	 * @throws Exception
	 */
	private void writePropertiesFileToUserHome() throws Exception {
		if (!Preferences.getPREF_REBUILD_WOBUILD_PROPERTIES_ON_NEXT_LAUNCH())
			return;
		URL relativeBuildFile = null;
		URL buildFile = null;
		IProgressMonitor monitor = null;
		relativeBuildFile =
			new URL(
					VariablesPlugin.baseURL(),
					VariablesPlugin.build_user_home_properties);
		buildFile = Platform.asLocalURL(relativeBuildFile);
		monitor = new NullProgressMonitor();
		RunAnt runAnt = new RunAnt();
		try {
			runAnt.asAnt(buildFile.getFile().toString(), monitor, null);
		} catch (Throwable throwable) {
			//this will allways fail for the first time
		} finally {
			relativeBuildFile = null;
			buildFile = null;
			monitor = null;
			runAnt = null;
			relativeBuildFile = null;
			buildFile = null;
			monitor = null;
		}
	}
/**
	 * Returns the shared instance.
	 */
	public static VariablesPlugin getDefault() {
		return plugin;
	}
	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = VariablesPlugin.getDefault()
				.getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}
	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	/**
	 * @return the path to the local root
	 */
	public IPath getLocalRoot() {
		return new Path(this.getWOEnvironment().getWOVariables().localRoot());
	}
	
	/**
	 * @return the path to the system root
	 */
	public IPath getSystemRoot() {
		return new Path(this.getWOEnvironment().getWOVariables().systemRoot());
	}
	
	/**
	 * @return the path to the user home
	 */
	public IPath getUserHome() {
		return new Path(this.getWOVariables().userHome());
	}
	
	/**
	 * @return the path to external build root
	 */
	public IPath getExternalBuildRoot() {
		return new Path(this.getWOVariables().userHome() + "/Roots");
	}
	/**
	 * @return the names of the framework roots
	 */
	public String[] getFrameworkRootsNames() {
		return new String[] { "a", "b", "c", "d"};
	}
	
	/**
	 * @return the paths to the framework roots
	 */
	public IPath[] getFrameworkRoots() {
		IPath[] paths = new IPath[4];
		paths[0] = this.getExternalBuildRoot();
		paths[1] = this.appendLibraryFrameworks(this.getUserHome());
		paths[2] = this.appendLibraryFrameworks(this.getLocalRoot());
		paths[3] = this.appendLibraryFrameworks(this.getSystemRoot());
		return paths;
	}
	private IPath appendLibraryFrameworks(IPath path) {
		return path.append("Library").append("Frameworks");
	}
	private WOVariables getWOVariables() {
		return this.getWOEnvironment().getWOVariables();
	}
	
	private WOEnvironment getWOEnvironment() {
		if (woEnvironment == null)
			woEnvironment = new WOEnvironment();
		return woEnvironment;
	}

	/**
	 * Returns the PluginID.
	 * @return
	 */
	public static String getPluginId() {
		if (plugin != null) {
			return getDefault().getDescriptor().getUniqueIdentifier();
		} else
			return VariablesPlugin.PLUGIN_ID;
	}
	/**
	 * Method baseURL.
	 * @return URL
	 */
	public static URL baseURL() {
		return VariablesPlugin.getDefault().getDescriptor().getInstallURL();
	}
		
}