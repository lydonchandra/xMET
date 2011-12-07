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

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JComponent;

import n.reporting.Reporting;
import xmet.tools.DefaultTool;
import xmet.tools.Tool;
import xmet.tools.ToolInstance;
import xmet.tools.TransparentToolException;

/**
 * Tool for displaying user manual.
 * @author Kyle
 */
public class UserManualTool
    extends DefaultTool
    implements
    ToolInstance {

    /* == Tool Interface == */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "application.manual";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolInstance invoke(
        final Map<String, Object> params)
        throws TransparentToolException {
        try {
            final HelpBroker hb = getHelpSet().createHelpBroker();
            hb.setViewDisplayed(true);
            (new CSH.DisplayHelpFromSource(
                hb)).actionPerformed(new ActionEvent(
                getContext().getTools().getInstances().getToolInstance(
                    0).getDisplayPanel(),
                0,
                "Asdf"));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        throw new TransparentToolException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleInstance() {
        return true;
    }

    /* == ToolInstance Interface == */
    /* the helpset */
    /** The help set. */
    private HelpSet helpSet = null;

    /**
     * Gets the help set.
     * @return the help set
     */
    public HelpSet getHelpSet() {
        if (helpSet == null) {
            final UserManualHelpSet[] helpSets =
                getContext()
                    .getServices()
                    .<UserManualHelpSet>getServiceProviderList(
                        UserManualHelpSet.class);
            if (helpSets != null) {
                for (final UserManualHelpSet hs : helpSets) {
                    if (!hs.isAdminModeOnly()) {
                        try {
                            final HelpSet hsi = hs.getHelpSet();
                            if (hsi != null) {
                                if (helpSet == null) {
                                    helpSet = hsi;
                                } else {
                                    helpSet.add(hsi);
                                }
                            }
                        } catch (final Exception e) {
                            Reporting.reportUnexpected(e);
                        }
                    }
                }
                if (getContext().getConfig().getTempSetting(
                    "admin",
                    null) != null) {
                    for (final UserManualHelpSet hs : helpSets) {
                        if (hs.isAdminModeOnly()) {
                            try {
                                final HelpSet hsi = hs.getHelpSet();
                                if (hsi != null) {
                                    if (helpSet == null) {
                                        helpSet = hsi;
                                    } else {
                                        helpSet.add(hsi);
                                    }
                                }
                            } catch (final Exception e) {
                                Reporting.reportUnexpected(e);
                            }
                        }
                    }
                }
            }

        }
        return helpSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getDisplayPanel() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "User Manual";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tool getTool() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onClose(
        final boolean force) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRefocus(
        final Map<String, Object> params) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInstantiation() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEvent(
        final int applicationEvent) {

    }

}
