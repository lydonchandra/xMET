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
package xmet.ui.mapping;

import xmet.ClientContext;

/**
 * An Abstract Mapping Module. To Make new mapping modules, extend from this and
 * implement ServiceProvider<MappingModule> and then register the service
 * through a service configuration file.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public abstract class MappingModule {

    /**
     * Supports.
     * @param useCase the use case
     * @return true, if successful
     */
    public abstract boolean supports(
        Class useCase);

    /**
     * Gets the.
     * @param useCase the use case
     * @param context the context
     * @return the object
     */
    public abstract Object get(
        Class useCase,
        ClientContext context);

    /**
     * Gets the module by use case.
     * @param useCase the use case
     * @param context the context
     * @return the module by use case
     */
    public static Object getModuleByUseCase(
        final Class useCase,
        final ClientContext context) {
        try {
            final MappingModule[] modules =
                context.getServices().<MappingModule>getServiceProviderList(
                    MappingModule.class);
            if (modules != null
                && modules.length > 0) {
                for (final MappingModule module : modules) {
                    if (module.supports(useCase)) {
                        return module.get(
                            useCase,
                            context);
                    }
                }
            }
            return null;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks for module of use case.
     * @param useCase the use case
     * @param context the context
     * @return true, if successful
     */
    public static boolean hasModuleOfUseCase(
        final Class<SpatialExtentListModifierMMUC> useCase,
        final ClientContext context) {
        try {
            final MappingModule[] modules =
                context.getServices().<MappingModule>getServiceProviderList(
                    MappingModule.class);
            if (modules != null
                && modules.length > 0) {
                for (final MappingModule module : modules) {
                    if (module.supports(useCase)) {
                        return true;
                    }
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
