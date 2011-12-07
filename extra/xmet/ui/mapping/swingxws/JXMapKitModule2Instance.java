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

import java.awt.event.ActionListener;
import java.net.ProxySelector;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import n.io.net.AuthentificationProxySelector;
import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.patterns.wysiwyg.Item;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.wms.WMSService;
import org.jdesktop.swingx.mapviewer.wms.WMSTileFactory;

import xmet.ClientContext;
import xmet.profiles.geometry.Shape;
import xmet.profiles.geometry.SpatialExtent;
import xmet.profiles.geometry.impl2d.BoundingBox2d;
import xmet.profiles.geometry.impl2d.ExtentId2d;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Polyline2d;
import xmet.profiles.geometry.impl2d.SpatialExtent2d;
import xmet.profiles.geometry.impl2d.Surface2d;
import xmet.ui.mapping.SpatialExtentListModifierMMUC;
import xmet.utils.PreferencesUtil;

/**
 * An instance class of this mapping module.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
class JXMapKitModule2Instance
    implements
    SpatialExtentListModifierMMUC {

    /** The Constant AUSTRALIA_CENTRE_GEOPOSITION. */
    private static final GeoPosition AUSTRALIA_CENTRE_GEOPOSITION =
        new GeoPosition(
            -25.0,
            135.0);
    /** The display panel. */
    private final JPanel displayPanel;
    /** The map. */
    private JXMapKit map;

    /** The overlay editor. */
    private JXMapKitModule2OverlayEditor overlayEditor;

    /**
     * Instantiates a new instance.
     * @param useCase the use case
     * @param context TODO
     */

    public JXMapKitModule2Instance(
        final Class useCase,
        final ClientContext context) {
        displayPanel = SwingUtils.BorderLayouts.getNew();

        ProxySelector.setDefault(new AuthentificationProxySelector(
            context.getConfig().getProxyInformation()));

        reconfigureTileFactory();

    }

    /**
     * Reconfigure tile factory.
     */
    private void reconfigureTileFactory() {
        // CHECKSTYLE OFF: MagicNumber

        map = new JXMapKit();
        /** The configuration. */
        JXMapKitModule2Configuration configuration = null;
        try {
            final PreferencesUtil prefs = new PreferencesUtil(
                "xmet.client.ui.mapping.swingxws");
            configuration =
                (JXMapKitModule2Configuration) prefs.getObject("setting");
        } catch (final Exception e) {
            e.printStackTrace();
        }
        if (configuration == null) {
            configuration = new JXMapKitModule2Configuration(
                "http://www.mymaps.gov.au:80/geoserver/wms?SERVICE=WMS&",
                "gn:world",
                JXMapKitModule2.FACTORY_WMS);
        }

        TileFactory tileFactory = null;

        if (configuration.getType().equals(
            JXMapKitModule2.FACTORY_WMS)) {
            final WMSService wms = new WMSService();
            wms.setLayer(configuration.getLayer());
            wms.setBaseUrl(configuration.getBaseURL());
            tileFactory = new WMSTileFactory(
                wms);
        } else {
            Reporting.logUnexpected();
        }
        if (tileFactory != null) {
            map.setTileFactory(tileFactory);
        } else {
            Reporting.reportUnexpected("Tile Factory not SET");
        }
        displayPanel.removeAll();
        displayPanel.add(map);
        map.getMainMap().setHorizontalWrapped(
            true);
        map.setMiniMapVisible(false);
        map.setZoom(14);
        map.setCenterPosition(AUSTRALIA_CENTRE_GEOPOSITION);
        overlayEditor = new JXMapKitModule2OverlayEditor(
            map.getMainMap());
        map.getMainMap().setOverlayPainter(
            overlayEditor);
        displayPanel.revalidate();
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getDisplayComponent() {
        return displayPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExtent(
        final SpatialExtent extent) {
        final ArrayList<Item> items = new ArrayList<Item>();
        for (final Shape shape : extent.getShapes()) {
            if (shape instanceof Point2d) {
                items.add(new EPosition(
                    ((Point2d) shape)));
            } else if (shape instanceof Polyline2d) {
                items.add(new ELine(
                    (Polyline2d) shape));
            } else if (shape instanceof BoundingBox2d) {
                items.add(new EBoundingBox(
                    (BoundingBox2d) shape));
            } else if (shape instanceof Surface2d) {
                items.add(new ESurface(
                    (Surface2d) shape));
            } else if (shape instanceof ExtentId2d) {
                items.add(new EExtentId(
                    (ExtentId2d) shape));
            } else {
                Reporting.logUnexpected();
            }
        }
        overlayEditor.setItems(items);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpatialExtent getExtent() {
        final ArrayList<Shape> shapes = new ArrayList<Shape>();
        for (final Item item : overlayEditor.getItems()) {
            if (item instanceof EPosition) {
                shapes.add(((EPosition) item).getPosition());
            } else if (item instanceof ELine) {
                shapes.add(((ELine) item).getLine());
            } else if (item instanceof EBoundingBox) {
                shapes.add(((EBoundingBox) item).getBoundingBox());
            } else if (item instanceof ESurface) {
                shapes.add(((ESurface) item).getSurface());
            } else if (item instanceof EExtentId) {
                shapes.add(((EExtentId) item).getExtentId());
            } else {
                Reporting.logUnexpected();
            }
        }
        return new SpatialExtent2d(
            shapes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChangeNotificationListener(
        final ActionListener listener) {
        overlayEditor.setActionListener(
            this,
            listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPointCallback() {
        overlayEditor.addPoint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPolyLineCallback() {
        overlayEditor.addPolyLine();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPolyGonCallback() {
        overlayEditor.addPolyGon();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPolyGonHoleCallback() {
        overlayEditor.addPolyGonHole();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBoundingBoxCallback() {
        overlayEditor.addBoundingBox();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCallback() {
        overlayEditor.deleteCallback();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showConfigurationCallback() {
        final ArrayList<Item> items = overlayEditor.getItems();
        JXMapKitModule2ConfigurationPane.showConfigurationDialog();
        reconfigureTileFactory();
        overlayEditor.setItems(items);
    }
}
