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
package xmet.ui.controls.spatialExtent;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.profiles.geometry.impl2d.SpatialExtent2d;
import xmet.ui.mapping.MappingModule;
import xmet.ui.mapping.SpatialExtentListModifierMMUC;

/**
 * SEV for the mapping component.
 * @author Nahid Akbar
 */
public class MapsSpatialExtentEditor
    implements
    SpatialExtentView,
    ActionListener {

    /** The control. */
    private final SpatialExtentControl control;

    /** The map. */
    private SpatialExtentListModifierMMUC map = null;

    /**
     * Instantiates a new maps spatial extent editor.
     * @param aControl the control
     * @param context TODO
     */
    public MapsSpatialExtentEditor(
        final SpatialExtentControl aControl,
        final ClientContext context) {
        super();
        this.control = aControl;
        map = (SpatialExtentListModifierMMUC) MappingModule.getModuleByUseCase(
            SpatialExtentListModifierMMUC.class,
            context);
    }

    /* == SpatialExtentView implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(
        final SpatialExtent2d extent) {
        if (map != null) {
            map.setChangeNotificationListener(this);
            map.setExtent(extent);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpatialExtent2d commit() {
        return (SpatialExtent2d) map.getExtent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getUI() {
        if (map != null) {
            return map.getDisplayComponent();
        } else {
            Reporting.reportUnexpected("Mapping Module Is Not Present");
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawPointCallback() {
        map.addPointCallback();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawPolyLineCallback() {
        map.addPolyLineCallback();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawBoundingBoxCallback() {
        map.addBoundingBoxCallback();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawSurfaceCallback() {
        map.addPolyGonCallback();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawSurfaceHoleCallback() {
        map.addPolyGonHoleCallback();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawCodeCallback() {
        Reporting.logUnexpected();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteItemCallback() {
        map.deleteCallback();

    }

    /* == ActionListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent e) {
        control.notifyObserversIfChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasConfigurationDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showConigurationDialogCallback() {
        map.showConfigurationCallback();
    }

}
