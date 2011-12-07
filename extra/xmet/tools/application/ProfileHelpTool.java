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

import java.io.File;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import n.ui.SwingUtils;
import xmet.tools.DefaultTool;
import xmet.tools.Tool;
import xmet.tools.ToolInstance;

/**
 * Tool for displaying profile specific help.
 * @author Nahid Akbar
 */
public class ProfileHelpTool
    extends DefaultTool
    implements
    ToolInstance {

    /* == Tool Interface == */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "application.profileHelp";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolInstance invoke(
        final Map<String, Object> params) {
        onRefocus(params);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleInstance() {
        return true;
    }

    /* == ToolInstance Interface == */

    /** The panel. */
    private JPanel panel = null;

    /** The editor pane. */
    private JEditorPane editorPane = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getDisplayPanel() {
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "Profile Help";
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
        panel = SwingUtils.BorderLayouts.getNew();
        editorPane = new JEditorPane();
        panel.add(new JScrollPane(
            editorPane,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
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
        final String url = (String) params.get("url");

        if (url != null) {
            String base = url;
            String extra = "";
            if (base.indexOf('#') >= 0) {
                extra = base.substring(base.indexOf('#'));
                base = base.substring(
                    0,
                    base.indexOf('#'));
            }
            final File[] files = getContext().getResources().getFilesList(
                base);
            if ((files != null)
                && (files.length > 0)) {
                try {
                    final String path = "file:///"
                        + files[0].getAbsolutePath()
                        + extra;
                    editorPane.setPage(path);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
        /* TODO Auto-generated method stub */

    }

}
