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

import java.awt.EventQueue;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import n.io.jar.JarUtils;
import n.reporting.Reporting;
import xmet.config.Config;
import xmet.profiles.ProfileManager;
import xmet.resources.ResourceManager;
import xmet.services.ServiceLocator;
import xmet.tools.ToolsManager;
import xmet.utils.SplashScreenUtil;

/**
 * The entry point to the xMET Application. <br />
 * The purpose of this class is to initialize a context for tools to run and
 * then and invoke the default tool.<br />
 * This class is named "Client" due to originally being the "fat-client" part of
 * the xMET client server application. However, the project was reduced changed
 * and the original name remained. <br />
 * @author Nahid Akbar
 */
public final class Client {

    /** Configuration singleton. */
    private static volatile Config config = null;

    /** Resources and related items singleton. */
    private static volatile ResourceManager resources = null;

    /** Services singleton. */
    private static volatile ServiceLocator services = null;

    /** Profiles and related items singleton. */
    private static volatile ProfileManager profiles = null;

    /** Tools singleton. */
    private static volatile ToolsManager tools = null;

    /* == Entry Point == */

    /**
     * Main Client Entry Point.
     * @param args the arguments
     */
    public static void main(
        final String[] args) {
        /* Initialization step 1 */
        preInitialize(args);
        /* Initialization step 2 */
        openSplashScreen();
        /* Initialization step 3 */
        postInitialize();
    }

    /* == Initializer methods == */

    /**
     * Initialization step 1.<br />
     * <ol>
     * <li>Add jars to classpath
     * <li>initialize configuration
     * <li>initialize resource manager
     * </ol>
     * @param args the program initialization paramaters - passed onto
     *            configuration for dynamic property setting
     */
    public static void preInitialize(
        final String[] args) {
        /* dynamically add class paths to the application */
        Reporting
            .logExpected("Looking for Libraries and Extensions");
        addDirectoryJarsToClasspath("lib");
        addDirectoryJarsToClasspath("client/plugins");
        addDirectoryJarsToClasspath("client/help");

        Reporting
            .logExpected("Initializing configuration");
        /* config singleton */
        if (config == null) {
            final Config configuration = new Config();
            configuration
                .loadSettings();
            configuration
                .parseParams(args);
            Client.config = configuration;
        }

        Reporting
            .logExpected("Initializing resource manager");
        /* resources singleton */
        if (resources == null) {
            resources = new ResourceManager(
                ".");
        }

        /*
         * Initialize graphics properties. Hack for anti-aliased text on linux.
         */
        /* For now, explicitly enable anti-aliasing. */
        /*
         * TODO: Move it somewhere more suitable making sure it is called before
         * any calls to swing are made.
         */
        System
            .setProperty(
                "awt.useSystemAAFontSettings",
                "on");
    }

    /**
     * Initialization step 3.<br/>
     * Synchronously
     * <ol>
     * <li>initializes services,
     * <li>initializes profiles (profile manager).
     * <li>initializes tools,
     * <li>and closes splash screen
     * </ol>
     * in the event dispatch thread.
     */
    public static void postInitialize() {
        final AtomicBoolean donePostInitializing = new AtomicBoolean(
            false);
        EventQueue
            .invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {

                        Reporting
                            .logExpected("Initializing services");
                        services = new ServiceLocator();
                        /*
                         * look in plugins and config folder for *.services
                         * files
                         */
                        services
                            .initialize(
                                "plugins",
                                ".services",
                                getContext());
                        services
                            .initialize(
                                "config",
                                ".services",
                                getContext());

                        Reporting
                            .logExpected("Initializing profiles manager");
                        profiles = new ProfileManager(
                            "profiles",
                            getContext());

                        Reporting
                            .logExpected("Initializing client tools");
                        tools = new ToolsManager(
                            getContext());
                        tools
                            .initialize(
                                "plugins",
                                ".tools",
                                getContext());
                        tools
                            .initialize(
                                "config",
                                ".tools",
                                getContext());
                        tools
                            .postInitialize();

                        Reporting
                            .logExpected("Closing splash screen");
                        closeSplashScreen();
                    } catch (final Exception e) {
                        Reporting
                            .reportUnexpected(e);
                    } finally {
                        donePostInitializing
                            .compareAndSet(
                                false,
                                true);
                    }
                }
            });
        /*
         * Block main thread until event queue is done. This is so that wrapper
         * projects can execute code straight after wrapping main.
         */
        while (!donePostInitializing
            .getAndSet(false)) {
            Thread
                .yield();
        }
    }

    /* == Shutdown == */
    /**
     * Shutdown step 1.<br/>
     * This method is to be called for initiating shutdown sequence.
     */
    public static void shutdown() {
        /* TODO Make Shutdown abortable */
        Reporting
            .logExpected("Shutting down");
        tools
            .shutdown();
        profiles
            .shutdown();
        System
            .exit(0);
    }

    /* == Global Client Context Access == */
    /**
     * Implementation class of ClientContext - what the rest of the programs get
     * to access.
     * @see ClientContext
     */
    private static class ClientContextImpl
        implements
        ClientContext {

        /**
         * {@inheritDoc}
         */
        @Override
        public ProfileManager getProfiles() {
            return Client.profiles;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ToolsManager getTools() {
            return Client.tools;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ResourceManager getResources() {
            return Client.resources;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void shutdown() {
            Client
                .shutdown();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Config getConfig() {
            return Client.config;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ServiceLocator getServices() {
            return Client.services;
        }
    }

    /**
     * The client access interface. <br />
     * To reduce coupling, tools access client resources through this rather
     * than directly.
     */
    private static ClientContextImpl clientContextSingleton =
        new ClientContextImpl();

    /**
     * Gets the client access interface. <br />
     * Make sure Client.preInitialize() and Client.postInitialize() is called
     * before using this object. Note that those methods are automatically
     * called if the program was invoked via the main method in this class.
     * @return the client access interface
     */
    public static ClientContext getContext() {
        return clientContextSingleton;
    }

    /* == Internal Helper methods == */
    /**
     * Helper method dynamically adds the files with .jar extension into
     * classpath. does non-recursive search
     * @param dir the dir to search jars in
     */
    @SuppressWarnings("deprecation")
    public static void addDirectoryJarsToClasspath(
        final String dir) {
        final File d = new File(
            dir);
        if (d
            .exists()
            && d
                .isDirectory()) {
            final File[] files = d
                .listFiles();
            for (final File file : files) {
                if (file
                    .getName()
                    .endsWith(
                        ".jar")
                    && !file
                        .isDirectory()) {
                    try {
                        JarUtils
                            .addJarToClasspath(file
                                .toURL());
                    } catch (final Exception e) {
                        Reporting
                            .reportUnexpected(e);
                    }
                }
            }
        }
    }

    /* == Splash screen related method == */
    /**
     * Initialize splash screen. <br />
     * Assumes resource manager has been initialized. This method also sets the
     * reporting callback to report to the splash screen.
     */
    protected static void openSplashScreen() {
        SplashScreenUtil
            .openSplash(resources
                .getImageIconResource("images/splash.bmp"));
        Reporting
            .setCallback(SplashScreenUtil
                .getReportingCallback());
    }

    /**
     * Close splash screen.
     */
    protected static void closeSplashScreen() {
        SplashScreenUtil
            .closeSplash();
    }

    /* == Misc == */

    /**
     * Private constructor to satisfy Checkstyle.
     */
    private Client() {

    }
}
