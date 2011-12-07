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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import n.reporting.Reporting;
import n.ui.patterns.wysiwyg.DefaultWYSIWYGMouseListener;
import n.ui.patterns.wysiwyg.Item;
import n.ui.patterns.wysiwyg.WYSIWYGEditor;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.Painter;

/**
 * The overlay painter and editor of the mapping module. This is what does all
 * the drawing and showing.
 * @author Nahid Akbar
 */
class JXMapKitModule2OverlayEditor
    implements
    Painter<JXMapViewer> {

    /** The map. */
    private JXMapViewer map;

    /** The action listener. */
    private ActionListener actionListener;

    /** The abstract editor. */
    private final WYSIWYGEditor abstractEditor = new WYSIWYGEditor() {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        @Override
        public void repaint() {
            map.repaint();
        }

        @Override
        public void changeNotification() {
            if (actionListener != null) {
                actionListener.actionPerformed(new ActionEvent(
                    map,
                    -1,
                    null));
            }
        }

        @Override
        public Point2D translatePointToCanvas(
            final Point2D point) {
            final Point2D gp = map.getTileFactory().geoToPixel(
                new GeoPosition(
                    point.getY(),
                    point.getX()),
                map.getZoom());
            return gp;
        };

        @Override
        public double lengthToPixelsH(
            final double displayWidth) {
            final Point2D gp = map.getTileFactory().geoToPixel(
                new GeoPosition(
                    0,
                    0),
                map.getZoom());
            final Point2D gps = map.getTileFactory().geoToPixel(
                new GeoPosition(
                    0,
                    displayWidth),
                map.getZoom());
            return gp.distance(gps);
        };

        @Override
        public double lengthToPixelsV(
            final double displayWidth) {
            final Point2D gp = map.getTileFactory().geoToPixel(
                new GeoPosition(
                    0,
                    0),
                map.getZoom());
            final Point2D gps = map.getTileFactory().geoToPixel(
                new GeoPosition(
                    displayWidth,
                    0),
                map.getZoom());
            return gp.distance(gps);
        };

        @Override
        public double pixelsToLengthH(
            final double displayWidth) {
            final GeoPosition gp = map.getTileFactory().pixelToGeo(
                new Point2D.Double(
                    0,
                    0),
                map.getZoom());
            final GeoPosition gps = map.getTileFactory().pixelToGeo(
                new Point2D.Double(
                    displayWidth,
                    0),
                map.getZoom());
            final double distance = Point2D.distance(
                gp.getLongitude(),
                gp.getLatitude(),
                gps.getLongitude(),
                gps.getLatitude());
            Reporting.logExpected(
                "%1$f = %2$f",
                displayWidth,
                distance);
            return distance;
        };

        @Override
        public double pixelsToLengthV(
            final double displayWidth) {
            /* TODO: Haven't figured out a consistent way to get position */
            /* differences - use the code for horozontal. seems to work */
            final GeoPosition gp = map.getTileFactory().pixelToGeo(
                new Point2D.Double(
                    0,
                    0),
                map.getZoom());
            final GeoPosition gps = map.getTileFactory().pixelToGeo(
                new Point2D.Double(
                    displayWidth,
                    0),
                map.getZoom());
            final double distance = Point2D.distance(
                gp.getLongitude(),
                gp.getLatitude(),
                gps.getLongitude(),
                gps.getLatitude());
            Reporting.logExpected(
                "%1$f = %2$f",
                displayWidth,
                distance);
            return distance;
        };
    };

    /** The mouse listener. */
    private final DefaultWYSIWYGMouseListener mouseListener =
        new DefaultWYSIWYGMouseListener(
            abstractEditor) {

            @Override
            protected void setPointCallback(
                final double x,
                final double y,
                final boolean addPoint,
                final boolean lastPointSignal) {
                final Rectangle rect = map.getViewportBounds();
                double varX = x;
                varX += rect.x;
                double varY = y;
                varY += rect.y;
                final GeoPosition gp = map.getTileFactory().pixelToGeo(
                    new Point2D.Double(
                        varX,
                        varY),
                    map.getZoom());
                super.setPointCallback(
                    gp.getLongitude(),
                    gp.getLatitude(),
                    addPoint,
                    lastPointSignal);
                if (!abstractEditor.isIdle()) {
                    map.setPanEnabled(false);
                    map.setZoomEnabled(false);
                } else {
                    map.setPanEnabled(true);
                    map.setZoomEnabled(true);
                }
            }
        };

    /**
     * Instantiates a new overlay editor.
     * @param aMap the map
     */
    public JXMapKitModule2OverlayEditor(
        final JXMapViewer aMap) {
        super();
        this.map = aMap;
        aMap.addMouseListener(mouseListener);
        aMap.addMouseMotionListener(mouseListener);
    }

    /**
     * Delete callback.
     */
    public void deleteCallback() {
        if (abstractEditor.getSelected() != null) {
            abstractEditor.getDrawnItems().remove(
                abstractEditor.getSelected());
            abstractEditor.setSelected(null);
            abstractEditor.repaint();
        }

    }

    /**
     * Sets the items.
     * @param items the new items
     */
    public void setItems(
        final ArrayList<Item> items) {
        abstractEditor.setDrawnItems(items);
    }

    /**
     * Gets the items.
     * @return the items
     */
    public ArrayList<Item> getItems() {
        abstractEditor.setPointCallback(
            null,
            true,
            true);
        return abstractEditor.getDrawnItems();
    }

    /**
     * Adds the bounding box.
     */
    public void addBoundingBox() {
        abstractEditor.drawNewShape(new EBoundingBoxDT());
    }

    /**
     * Adds the poly gon.
     */
    public void addPolyGon() {
        abstractEditor.drawNewShape(new ESurfaceDT());
    }

    /**
     * Adds the poly gon hole.
     */
    public void addPolyGonHole() {
        try {
            ESurface surface;
            surface = (ESurface) abstractEditor.getSelected();
            if ((surface) != null) {
                abstractEditor.drawNewShape(new ESurfaceHoleDT(
                    surface));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the poly line.
     */
    public void addPolyLine() {
        abstractEditor.drawNewShape(new ELineDT());
    }

    /**
     * Adds the point.
     */
    public void addPoint() {
        abstractEditor.drawNewShape(new EPositionDT());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(
        final Graphics2D g,
        final JXMapViewer arg1,
        final int arg2,
        final int arg3) {
        map = arg1;
        final Graphics2D varG = (Graphics2D) g.create();
        final Rectangle rect = map.getViewportBounds();
        varG.translate(
            -rect.x,
            -rect.y);

        if (abstractEditor != null) {
            abstractEditor.paintItems(varG);
        }
    }

    /**
     * Sets the action listener.
     * @param instance the instance
     * @param listener the listener
     */
    void setActionListener(
        final JXMapKitModule2Instance instance,
        final ActionListener listener) {
        actionListener = listener;
    }
}
