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
package xmet.tools.application;

import java.net.URL;

import javax.help.HelpSet;
import javax.help.HelpSetException;

import xmet.services.ServiceProvider;

/**
 * This is a binding service for the normal user manual.
 * @author Nahid Akbar
 */
public class OrdinaryUserManual
    implements
    UserManualHelpSet,
    ServiceProvider<UserManualHelpSet> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAdminModeOnly() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HelpSet getHelpSet() {
        final ClassLoader cl = this.getClass().getClassLoader();
        final URL url = HelpSet.findHelpSet(
            cl,
            "xMET.hs");
        HelpSet helpSet = null;
        try {
            helpSet = new HelpSet(
                cl,
                url);
        } catch (final HelpSetException e) {
            e.printStackTrace();
        }
        return helpSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPriority() {
        return 0;
    }

}
