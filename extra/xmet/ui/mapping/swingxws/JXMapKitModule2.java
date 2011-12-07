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
package xmet.ui.mapping.swingxws;

import xmet.ClientContext;
import xmet.services.ServiceProvider;
import xmet.ui.mapping.MappingModule;
import xmet.ui.mapping.SpatialExtentListModifierMMUC;

/**
 * A Mapping Module Service Provider implemented with JXMapKit.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class JXMapKitModule2
    extends MappingModule
    implements
    ServiceProvider<MappingModule> {

    /** The Constant FACTORY_WMS. */
    static final String FACTORY_WMS = "WMS";

    /**
     * Instantiates a new jX map kit module2.
     */
    public JXMapKitModule2() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(
        final Class useCase) {
        return ((useCase == SpatialExtentListModifierMMUC.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(
        final Class useCase,
        final ClientContext context) {
        return new JXMapKitModule2Instance(
            useCase,
            context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPriority() {
        return 0;
    }

}
