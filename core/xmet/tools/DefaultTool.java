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

import xmet.ClientContext;

/**
 * Default implementation of a Tool.
 * @author Kyle
 */
public abstract class DefaultTool
    implements
    Tool {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleInstance() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize() {
        assert (getContext() != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisposal() {
    }

    /* == Helper elements == */

    /** The client. */
    private ClientContext context;

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientContext getContext() {
        return context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContext(
        final ClientContext aClient) {
        this.context = aClient;
    }

}
