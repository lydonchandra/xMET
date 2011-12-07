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
package xmet.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import n.reporting.Reporting;
import n.reporting.ReportingCallback;
import xmet.ClientContext;

/**
 * Manages the list of tool instances at runtime. This is just the non-ui part
 * implementation which has abstract methods for appropriate UI changes. The
 * idea is that any type of UI implementation e.g. tabs or multiple windows etc
 * can extend this and provide desired gui implementation for whatever the
 * container level functionality requirements are
 * @author Nahid Akbar
 */
public abstract class ToolInstanceManager {

    /* == Methods To Be implemented by UI Wrapper == */
    /** The context. */
    private final ClientContext context;

    /**
     * UI callback for when a new tool instance is added.
     * @param newToolInstance the e
     * @param newIndex the index
     */
    protected abstract void addToolInstanceUI(
        ToolInstance newToolInstance,
        int newIndex);

    /**
     * UI Callback for when all tool instances are removed.
     */
    protected abstract void clearInstancesUI();

    /**
     * UI callback for when a particular tool instance is removed.
     * @param index the index
     */
    protected abstract void removeToolInstanceUI(
        int index);

    /**
     * UI callback for when a particular single instance tool with existing tool
     * instance is re-invoked - default behaviour should be to refocus it.
     * @param index the i
     */
    protected abstract void focusToolInstanceUI(
        int index);

    /**
     * UI callback for when the client is exiting.
     */
    protected abstract void disposeUI();

    /**
     * Called when the active tool instance is needed to be determined.
     * @return the active tool instance
     */
    protected abstract ToolInstance getActiveToolInstance();

    /* == Misc == */

    /**
     * Default Constructor.
     * @param aContext the context
     */
    public ToolInstanceManager(
        final ClientContext aContext) {
        instances = new ArrayList<ToolInstance>();
        this.context = aContext;
    }

    /* == Functions for managing tool instances == */
    /** Will contain a list of tool instances. */
    private final ArrayList<ToolInstance> instances;

    /**
     * Adds the new tool instance.
     * @param e the e
     */
    public synchronized void addNewToolInstance(
        final ToolInstance e) {
        final int index = instances.size();
        instances.add(
            index,
            e);
        e.onInstantiation();
        addToolInstanceUI(
            e,
            index);
        reFocusToolInstance(
            e,
            new HashMap<String, Object>());
    }

    /**
     * Clear all tool instances.
     */
    public void clearAllToolInstances() {
        for (final ToolInstance ti : instances) {
            ti.onClose(true);
            ti.onDisposal();
        }
        instances.clear();
        clearInstancesUI();
    }

    /**
     * Gets the tool instance.
     * @param index the index
     * @return the tool instance
     */
    public ToolInstance getToolInstance(
        final int index) {
        return instances.get(index);
    }

    /**
     * Checks for tool instances.
     * @return true, if successful
     */
    public boolean hasToolInstances() {
        return !instances.isEmpty();
    }

    /**
     * Removes the tool instance.
     * @param index the index
     * @return the tool instance
     */
    public synchronized ToolInstance removeToolInstance(
        final int index) {
        assert ((index >= 0) && (index < instances.size()));
        if (instances.get(
            index).onClose(
            false)) {
            instances.get(
                index).onDisposal();
            final ToolInstance ti = instances.remove(index);
            removeToolInstanceUI(index);
            return ti;
        } else {
            return null;
        }
    }

    /**
     * Removes the tool instance.
     * @param isntance the isntance
     */
    public void removeToolInstance(
        final ToolInstance isntance) {
        removeToolInstance(instances.indexOf(isntance));
    }

    /**
     * Gets the tool instance indec.
     * @param instance the instance
     * @return the tool instance indec
     */
    public int getToolInstanceIndec(
        final ToolInstance instance) {
        return instances.indexOf(instance);
    }

    /**
     * Gets the tool instances count.
     * @return the tool instances count
     */
    public int getToolInstancesCount() {
        return instances.size();
    }

    /**
     * Focus tool instance.
     * @param ti the ti
     * @param params the params
     */
    protected void reFocusToolInstance(
        final ToolInstance ti,
        final Map<String, Object> params) {
        final int i = instances.indexOf(ti);
        if ((i >= 0)
            && (i < getToolInstancesCount())) {
            instances.get(
                i).onRefocus(
                params);
            focusToolInstanceUI(i);
        }
    }

    /**
     * Focus tool instance.
     * @param i the i
     */
    private void focusToolInstance(
        final int i) {
        if ((i >= 0)
            && (i < getToolInstancesCount())) {
            focusToolInstanceUI(i);
        }
    }

    /* == Methods for finding and invoking tools == */

    /**
     * Main invoke tool method.
     * @param tool the tool
     * @param params the params
     */
    public void createNewToolInstance(
        final Tool tool,
        final Map<String, Object> params) {

        if (tool.isSingleInstance()) {
            for (final ToolInstance ti : instances) {
                if (ti.getTool() == tool) {
                    Reporting.logExpected(tool.getName()
                        + " already exists");
                    reFocusToolInstance(
                        ti,
                        params);
                    return;
                }
            }
        }
        try {
            ToolInstance ti;
            ti = tool.invoke(params);
            for (final ToolInstance eti : instances) {
                if (ti == eti) {
                    Reporting.logExpected("tool instance already exists");
                    reFocusToolInstance(
                        ti,
                        params);
                    return;
                }
            }
            if (ti != null) {
                addNewToolInstance(ti);
            } else {
                Reporting.reportUnexpected(
                    "Tool invocation failed. %1$s",
                    tool);
            }
        } catch (final TransparentToolException t) {
            Reporting.logExpected(t);
        }
    }

    /* == Events Processing == */
    /**
     * Process event.
     * @param event the event
     */
    public void processEvent(
        final int event) {
        processEvent(
            null,
            event);
    }

    /**
     * Process event.
     * @param toolInstance the tool instance
     * @param event the event
     */
    public void processEvent(
        final ToolInstance toolInstance,
        final int event) {
        ToolInstance varTI = toolInstance;
        assert (varTI != null)
            || ToolInstanceEvents.isGenericEvent(event)
            || (getActiveToolInstance() != null);
        try {
            if (varTI == null) {
                varTI = getActiveToolInstance();
            }
            if (ToolInstanceEvents.isGenericEvent(event)) {
                switch (event) {
                case ToolInstanceEvents.NEXT_TOOL_INSTANCE:
                    if (varTI != null) {
                        focusToolInstance(getToolInstanceIndec(varTI) + 1);
                    }
                    break;
                case ToolInstanceEvents.PREVIOUS_TOOL_INSTANCE:
                    if (varTI != null) {
                        focusToolInstance(getToolInstanceIndec(varTI) - 1);
                    }
                    break;
                case ToolInstanceEvents.CLOSE_TOOL_INSTANCE:
                    if (varTI != null) {
                        removeToolInstance(varTI);
                    }
                    break;
                case ToolInstanceEvents.EXIT_APPLICATION:
                    /* Better not handeled here */
                    break;
                default:
                    Reporting.logUnexpected("unhandled event "
                        + event);
                    break;
                }
            } else {
                if (varTI != null) {
                    varTI.onEvent(event);
                } else {
                    Reporting.logExpected("no active tool instance");
                }
            }
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
    }

    /* == Misc Methods == */
    /**
     * Called when this instance of tool manager is to be disposed.
     */
    public void shutdown() {
        clearAllToolInstances();
        disposeUI();
    }

    /**
     * Gets the reporting callback interface where log messages and exceptions
     * and errors will be reported.
     * @return the reporting callback, null if not relevant
     */
    public ReportingCallback getReportingCallback() {
        return null;
    }

    /**
     * Gets the context.
     * @return the context
     */
    public ClientContext getContext() {
        return context;
    }

}
