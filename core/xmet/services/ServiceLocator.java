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
package xmet.services;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import n.io.bin.Files;
import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;

import org.jdom.Element;

import xmet.ClientContext;

/**
 * Class which maps service interfaces to their serving servers. This was
 * introduced for minimising complexity due to cross unit dependency. Services
 * are grouped and located via specifying the service interface. <br />
 * <br />
 * Services in xMet are used to handle high level polymorphism to allow for
 * future extensions without modifying xMet code and also to reduce circular
 * dependencies. <br />
 * <br />
 * For example, the Metadata Manager tool can potentially sit on any type of
 * back end such as local file system or a database or a Geonetwork CSW node
 * etc. Hard coding support for all of these back ends in the main xMet code
 * will require modifications in the xMet code every time a new extension is
 * coded and is bad practice. <br />
 * <br />
 * In addition, this also serves to reduce circular dependencies. Although
 * coding with circular dependencies is considered to be a bad practice, real
 * life systems are complex and seldom reducible to a simple top/down acyclic
 * dependency graph. For example, the file name extension of editor view
 * sub-systems is a property of the editor view. However, the profile manager
 * needs a list of file name extensions to correctly identify editor view
 * configuration files when loading. In addition, the profile manager and the
 * profiles are used by the editor views to perform their function. Hard coding
 * a list of known extension files in profile manager may help to eliminate this
 * dependency but is bad practice due to reasons mentioned above. Exposing
 * services implementations of service interfaces and using services through
 * this interface rather than directly inducing dependency allows maintaining
 * that ideal acyclic dependency graph while performing the necessary
 * functionality. Although there are other possible solutions, this solution was
 * chose to allow easy bridging of new service interfaces and implementations.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class ServiceLocator
    implements
    Comparator<ServiceProvider>,
    Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    /** The service providers. Hashed for faster runtime access. */
    private final HashMap<Class, PriorityQueue<ServiceProvider>> providers =
        new HashMap<Class, PriorityQueue<ServiceProvider>>();

    /* == Access Interface == */
    /**
     * Register a list of service providers.
     * @param aServiceProviders the service providers
     * @return the number of reegistered service providers
     */
    public int registerServiceProviders(
        final ServiceProvider... aServiceProviders) {
        int counter = 0;
        for (final ServiceProvider serviceProvider : aServiceProviders) {
            if (addServiceProviderHelper(serviceProvider)) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Gets the highest priority service provider based on an interface class.
     * @param <E> the element type
     * @param serviceInterface the service interface
     * @return the service provider
     */
    @SuppressWarnings("unchecked")
    public <E> E getServiceProvider(
        final Class serviceInterface) {
        try {
            final PriorityQueue<ServiceProvider> services = this.providers
                .get(serviceInterface);
            if (services != null) {
                try {
                    return (E) services
                        .peek();
                } catch (final Exception e) {
                    e
                        .printStackTrace();
                }
            }
        } catch (final Exception e) {
            Reporting
                .reportUnexpected(e);
        }
        return null;
    }

    /**
     * Gets the list of service providers based on an interface class, sorted
     * descending by their priorities Make sure that the paramater E and the
     * serviceInterface refer to the same thing.
     * @param <E> the element type (for casting)
     * @param serviceInterface the service interface (for finding service
     *            providers)
     * @return the service provider list
     */
    @SuppressWarnings("unchecked")
    public <E> E[] getServiceProviderList(
        final Class serviceInterface) {
        try {
            final PriorityQueue<ServiceProvider> services = this.providers
                .get(serviceInterface);
            if (services != null) {
                /* convert to array */
                final E[] providersList = (E[]) Array
                    .newInstance(
                        serviceInterface,
                        services
                            .size());
                final ServiceProvider[] array = services
                    .toArray(new ServiceProvider[0]);
                Arrays
                    .sort(
                        array,
                        this);
                for (int i = 0; i < array.length; i++) {
                    try {
                        providersList[i] = (E) array[i];
                    } catch (final Exception e) {
                        Reporting
                            .reportUnexpected(e);
                    }
                }
                return providersList;
            }
        } catch (final Exception e) {
            Reporting
                .reportUnexpected(e);
        }
        return (E[]) Array
            .newInstance(
                serviceInterface,
                0);
    }

    /**
     * This method gets all the folders with specified name and then tries to
     * find files with specified postfix and then tries to load those files as
     * services config file.
     * @param folderName the folder name
     * @param filenamePostfix the filename postfix
     * @param context the context
     */
    public void initialize(
        final String folderName,
        final String filenamePostfix,
        final ClientContext context) {
        final File[] configFolders = context
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
                        .logExpected("Loading Service Configuratoion "
                            + files[i]);
                    loadServicesFromConfig(files[i]);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(
        final ServiceProvider arg0,
        final ServiceProvider arg1) {
        try {
            return arg0
                .getPriority()
                .compareTo(
                    arg1
                        .getPriority());
        } catch (final Exception e) {
            e
                .printStackTrace();
        }
        return 0;
    }

    /* == Actual Implementation == */
    /**
     * helper method or adding service providers.
     * @param serviceProvider the service provider
     * @return true, if successful
     */
    private boolean addServiceProviderHelper(
        final ServiceProvider serviceProvider) {
        boolean returnValue = false;
        if (serviceProvider != null) {
            /* get the class types */
            final Class classType = serviceProvider
                .getClass();
            /* get the interfaces implemented by the class */
            final Type[] interfaceTypes = classType
                .getGenericInterfaces();
            for (final Type interfaceType : interfaceTypes) {
                /* go through the interface to find parameterised types */
                if (interfaceType instanceof ParameterizedType) {
                    final ParameterizedType pt =
                        (ParameterizedType) interfaceType;
                    if (pt
                        .getRawType() == ServiceProvider.class
                        && pt
                            .getActualTypeArguments().length == 1) {
                        /*
                         * extract the parameter type and add it to the services
                         */
                        PriorityQueue<ServiceProvider> services =
                            this.providers
                                .get(pt
                                    .getActualTypeArguments()[0]);
                        if (services == null) {
                            services = new PriorityQueue<ServiceProvider>(
                                1,
                                this);
                        }
                        services
                            .add(serviceProvider);
                        this.providers
                            .put(
                                (Class) pt
                                    .getActualTypeArguments()[0],
                                services);
                        returnValue = true;
                    }
                }
            }
        }
        return returnValue;
    }

    /**
     * Load services from config.
     * @param file the file
     */
    private void loadServicesFromConfig(
        final File file) {
        /* load the config file */
        final java.nio.ByteBuffer contents = Files
            .read(file);
        if (contents != null) {
            /* parse as xml */
            final Element tools = JDOMXmlUtils
                .elementFromXml(new String(
                    contents
                        .array()));
            if (tools != null) {
                /* get a list of "service" child elements and parse their */
                /* content as service provider class names */
                final List toolsList = tools
                    .getChildren("service");
                if (toolsList
                    .size() > 0) {
                    for (final Object o : toolsList) {
                        final Element e = (Element) o;
                        loadServiceClass(e
                            .getTextTrim());
                    }
                }
            }
        }
    }

    /**
     * Loads a service class.
     * @param name the full name of the class
     */
    private void loadServiceClass(
        final String name) {
        Reporting
            .logExpected("Loading "
                + name);
        final ClassLoader loader = this
            .getClass()
            .getClassLoader();
        try {
            /* load class */
            final Class c = loader
                .loadClass(name);
            /* instantiate object */
            final Object o = c
                .newInstance();
            /* try to load it as a service provider */
            registerServiceProviders((ServiceProvider) o);
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
}
