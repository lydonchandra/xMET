/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br>
 * <br>
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br>
 * <br>
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br>
 * <br>
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br>
 * <br>
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet;

import xmet.config.Config;
import xmet.profiles.ProfileManager;
import xmet.resources.ResourceManager;
import xmet.services.ServiceLocator;
import xmet.tools.ToolsManager;

/**
 * Supplied access interface through which all components of the system should
 * be accessed. <br>
 * This is for not producing needless circular dependencies and also for easy
 * porting tools which work with this framework. The only allowed exception to
 * this rule is unit testing for which Client.getContext() should be used.
 * @author Nahid Akbar
 */
public interface ClientContext {

    /**
     * Gets the services manager. <br />
     * <br />
     * @see ServiceLocator
     * @return the services
     */
    ServiceLocator getServices();

    /**
     * Gets the tools manager. <br />
     * @see ToolsManager
     * @return the tools
     */
    ToolsManager getTools();

    /**
     * Gets the profiles manager. <br />
     * @see ProfileManager
     * @return the profile manager
     */
    ProfileManager getProfiles();

    /**
     * Gets the resources manager. <br />
     * @see ResourceManager
     * @return the resource manager
     */
    ResourceManager getResources();

    /**
     * Gets the configuration manager. <br />
     * @see Config
     * @return the configuration manager
     */
    Config getConfig();

    /**
     * Callback for initiating shutdown. <br />
     * Do not call this method without confirming with the user.
     */
    void shutdown();

}
