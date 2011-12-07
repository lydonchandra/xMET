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

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.tools.DefaultTool;
import xmet.tools.Tool;
import xmet.tools.ToolInstance;
import xmet.tools.ToolInstanceManager;

/**
 * Tools for seeing a list of tools and stuff.
 * @author Nahid Akbar
 */
public class ToolsTool
    extends DefaultTool
    implements
    ToolInstance {

    /* == Tool Interface == */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "application.tools";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolInstance invoke(
        final Map<String, Object> params) {
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

    /** The table. */
    private JTable table;

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getDisplayPanel() {
        reload();
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "tools";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tool getTool() {
        return this;
    }

    /**
     * Reload.
     */
    public void reload() {
        final String[] toolsCols = new String[] {
        "Name",
        "Calss",
        "SingleInstance"
        };
        final Tool[] tools = getContext().getTools().getAsArray();
        final String[][] toolsRows = new String[tools.length][toolsCols.length];

        for (int i = 0; i < tools.length; i++) {
            toolsRows[i][0] = tools[i].getName();
            toolsRows[i][1] = tools[i].getClass().getName();
            String varString = null;
            if (tools[i].isSingleInstance()) {
                varString = "yes";
            } else {
                varString = "no";
            }
            toolsRows[i][2] = varString;
        }
        table = new JTable(
            toolsRows,
            toolsCols);
        final JScrollPane toolsTable = new JScrollPane(
            table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        final String[] tICols = new String[] {
        "Title",
        "Tool"
        };
        final ToolInstanceManager tim = getContext().getTools().getInstances();
        final String[][] toolsInstanceRows =
            new String[tim.getToolInstancesCount()][tICols.length];

        for (int i = 0; i < tim.getToolInstancesCount(); i++) {
            final ToolInstance ti = tim.getToolInstance(i);
            toolsInstanceRows[i][0] = ti.getTitle();
            toolsInstanceRows[i][1] = ti.getTool().getName();
        }
        JTable toolInstanceTable;
        toolInstanceTable = new JTable(
            toolsInstanceRows,
            tICols);
        final JScrollPane toolsInstancesTablePane = new JScrollPane(
            toolInstanceTable);
        toolInstanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.removeAll();

        panel.add(new JLabel(
            "Tools"));
        panel.add(toolsTable);
        panel.add(new JLabel(
            "Instances"));
        panel.add(toolsInstancesTablePane);

        final JPanel buttonPanel = SwingUtils.BoxLayouts.getHorizontalPanel();
        panel.add(buttonPanel);
        final Object[] params = {};
        buttonPanel.add(SwingUtils.BUTTON.getNewV(
            "Refresh",
            getContext().getResources().getImageIconResource(
                "images/toolbar.common.refresh.png"),
            new ClassMethodCallback(
                this,
                "reload",
                params)));
        final Object[] params1 = {};
        buttonPanel.add(SwingUtils.BUTTON.getNewV(
            "Invoke Tool",
            getContext().getResources().getImageIconResource(
                "images/toolbar.common.run.png"),
            new ClassMethodCallback(
                this,
                "invokeSelected",
                params1)));

        panel.validate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize() {
        panel = SwingUtils.BoxLayouts.getVerticalPanel();
        /* reload(); */
    }

    /**
     * Invoke selected.
     */
    public void invokeSelected() {
        final int i = table.getSelectedRow();
        if (i >= 0) {
            final Tool[] tools = getContext().getTools().getAsArray();
            getContext().getTools().invokeToolByParamsList(
                tools[i],
                null,
                null);
        } else {
            Reporting.reportUnexpected("sads "
                + i);
        }
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
