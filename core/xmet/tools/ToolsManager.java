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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import n.io.bin.Files;
import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;

import org.jdom.Element;

import xmet.ClientContext;

/**
 * Contains a list of tools, tool instances manager and methods for loading
 * configuration files with tools. <br />
 * The tools manager maintains a list of tools and also the tools instance
 * manager. Tools are like classes and tool instances like objects. xMET core
 * functionalities implemented as different Tools. Tools are invoked to produce
 * tool instances which the users interact with. There are some allowed
 * exceptions to this rule such as single instance tools and tools which do not
 * have produce an instance (transparent tools).
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class ToolsManager {

    /** The context. */
    private final ClientContext context;

    /**
     * Instantiates a new tools manager.
     * @param aContext the context
     */
    public ToolsManager(
        final ClientContext aContext) {
        super();
        this.context = aContext;
    }

    /** the list of tools. */
    private final ArrayList<Tool> tools = new ArrayList<Tool>();
    /** Tool Instanaces. */
    private ToolInstanceManager instances;

    /**
     * Loads a config file with
     * &lt;tools&gt;&lt;tool&gt;..&lt;/tool&gt;&lt;tool&gt;...
     * @param file the config file to load
     */

    public void loadToolsFromConfig(
        final File file) {
        final java.nio.ByteBuffer contents = Files
            .read(file);
        if (contents != null) {
            final Element aTools = JDOMXmlUtils
                .elementFromXml(new String(
                    contents
                        .array()));
            if (aTools != null) {
                final List toolsList = aTools
                    .getChildren("tool");
                if (toolsList
                    .size() > 0) {
                    for (final Object o : toolsList) {
                        final Element e = (Element) o;
                        loadToolClass(e
                            .getTextTrim());
                    }

                }
            }
        }
    }

    /**
     * Loads a class given a name.
     * @param name the name
     */
    private void loadToolClass(
        final String name) {
        Reporting
            .logExpected("Loading "
                + name);
        /* ClassLoader loader = ClassLoader.getSystemClassLoader(); */
        final ClassLoader loader = this
            .getClass()
            .getClassLoader();
        try {
            final Class c = loader
                .loadClass(name);
            final Object o = c
                .newInstance();
            final Tool tool = (Tool) o;
            Reporting
                .logExpected("initializing");
            tool
                .setContext(context);
            tools
                .add(tool);
            Reporting
                .logExpected("Tool "
                    + name
                    + " loaded successfully");
        } catch (final ClassNotFoundException e) {
            Reporting
                .reportUnexpected(e);
        } catch (final InstantiationException e) {
            Reporting
                .reportUnexpected(e);
        } catch (final IllegalAccessException e) {
            Reporting
                .reportUnexpected(e);
        } catch (final ClassCastException e) {
            Reporting
                .reportUnexpected(e);
        }
    }

    /**
     * Finds a tool Object given a name.
     * @param name the name
     * @return the tool
     */
    public Tool findTool(
        final String name) {
        Tool ret = null;
        for (final Tool t : tools) {
            if (t
                .getName()
                .equals(
                    name)) {
                ret = t;
            }
        }
        return ret;
    }

    /**
     * gets the list of tools as an array.
     * @return the as array
     */
    public Tool[] getAsArray() {
        final Tool[] na = new Tool[tools
            .size()];
        for (int i = 0; i < na.length; i++) {
            na[i] = tools
                .get(i);
        }
        return na;
    }

    /**
     * calls onDisposal of all the tools. called before the client exits
     */
    public void shutdown() {
        getInstances()
            .shutdown();
        for (final Tool t : tools) {
            t
                .onDisposal();
        }
    }

    /**
     * This method gets all the folders with specified name and then tries to
     * find files with specified postfix and then tries to load those files as
     * tools config file.
     * @param folderName the folder name
     * @param filenamePostfix the filename postfix
     * @param aContext the context
     */
    public void initialize(
        final String folderName,
        final String filenamePostfix,
        final ClientContext aContext) {
        final File[] configFolders = aContext
            .getResources()
            .getFoldersList(
                folderName);
        for (final File configDir : configFolders) {
            final File[] files = configDir
                .listFiles();
            Arrays
                .sort(files);
            for (int i = 0; i < files.length; i++) {
                if (files[i]
                    .getName()
                    .endsWith(
                        filenamePostfix)) {
                    Reporting
                        .logExpected("Loading Tool Configuratoion "
                            + files[i]);
                    loadToolsFromConfig(files[i]);
                }
            }
        }
        for (final Tool tool : tools) {
            tool
                .onInitialize();
        }
    }

    /**
     * Gets the instances.
     * @return the instances
     */
    public ToolInstanceManager getInstances() {
        return instances;
    }

    /**
     * To be called after all other initialization steps are done. <br />
     * This method initializes the tool instance manager and invokes the default
     * tool.
     */
    public void postInitialize() {
        try {
            /*
             * Find highest priority tool instance manager service provider.
             */
            final ToolInstanceManagerWrapperService wrapper = context
                .getServices()
                .<ToolInstanceManagerWrapperService>getServiceProvider(
                    ToolInstanceManagerWrapperService.class);
            if (wrapper != null) {
                this.instances = wrapper
                    .getNewInstance(context);
            }
            if (getInstances() != null) {
                /* invoke default tool */
                invokeToolByName(context
                    .getConfig()
                    .getTempSetting(
                        "default",
                        "application.start"));
                /*
                 * set reporting handler for n.reporting.Reporting. All the
                 * reporting in this program is made to there. Hence this allows
                 * the tool instance manager to handle all the reporting from
                 * the same place without inducing circular dependencies.
                 */
                Reporting
                    .setCallback(getInstances()
                        .getReportingCallback());
            } else {
                Reporting
                    .reportUnexpected("No Tool Instance Manager Configured");
                shutdown();
            }
        } catch (final Exception e) {
            Reporting
                .reportUnexpected(e);
        }

    }

    /**
     * Helper Method for invoking tool by name.
     * @param name the name
     */
    public void invokeToolByName(
        final String name) {
        invokeToolByName(
            name,
            new HashMap<String, Object>());
    }

    /**
     * Helper method for invoking tools by string, object pairs.
     * @param name the name
     * @param params the params
     */
    public void invokeToolByName(
        final String name,
        final Object... params) {
        assert (params.length % 2 == 0);
        final Map<String, Object> tmpParams = new HashMap<String, Object>();
        for (int i = 0; i < params.length; i += 2) {
            assert (params[i] instanceof String);
            tmpParams
                .put(
                    (String) params[i],
                    params[i + 1]);
        }
        invokeToolByName(
            name,
            tmpParams);
    }

    /**
     * Helper Method Invoke tool with list parameters (for backward
     * compatibility).
     * @param name the name
     * @param paramNames the param names
     * @param params the params
     */
    public void invokeToolByName(
        final String name,
        final List<String> paramNames,
        final List<Object> params) {
        Reporting
            .logExpected("Invoking "
                + name);
        final Tool tool = findTool(name);
        if (tool != null) {
            invokeToolByParamsList(
                tool,
                paramNames,
                params);
        } else {
            Reporting
                .reportUnexpected("Tool of name "
                    + name
                    + " not found");
        }
    }

    /**
     * Helper Method Invoke tool by name.
     * @param name the name
     * @param params the params
     */
    public void invokeToolByName(
        final String name,
        final Map<String, Object> params) {
        Reporting
            .logExpected("Invoking "
                + name);
        final Tool tool = findTool(name);
        if (tool != null) {
            invokeTool(
                tool,
                params);
        } else {
            Reporting
                .reportUnexpected("Tool of name "
                    + name
                    + " not found");
        }
    }

    /**
     * Helper Method Invoke tool by supplied tool and list params (for backward
     * compatibility).
     * @param tool the tool
     * @param paramNames the param names
     * @param paramsp the paramsp
     */
    public void invokeToolByParamsList(
        final Tool tool,
        final List<String> paramNames,
        final List<Object> paramsp) {
        final Map<String, Object> params = new HashMap<String, Object>();

        if ((paramNames != null)
            && (paramsp != null)) {
            for (int i = 0; i < paramNames
                .size(); i++) {
                params
                    .put(
                        paramNames
                            .get(i),
                        paramsp
                            .get(i));
            }
        }

        invokeTool(
            tool,
            params);
    }

    /**
     * Invoke tool.
     * @param tool the tool
     * @param params the params
     */
    public void invokeTool(
        final Tool tool,
        final Map<String, Object> params) {
        getInstances()
            .createNewToolInstance(
                tool,
                params);
    }
}
