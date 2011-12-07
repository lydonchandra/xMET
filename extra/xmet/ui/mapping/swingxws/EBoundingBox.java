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

import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import n.reporting.Reporting;
import n.ui.patterns.wysiwyg.DrawingContext;
import xmet.profiles.geometry.impl2d.BoundingBox2d;
import xmet.profiles.geometry.impl2d.Point2d;

/**
 * Bounding Box Item.
 * @author Nahid Akbar
 */
public class EBoundingBox
    extends EAbstractShape {

    /* model we are using internally is just x,y coordinates */
    /** The x. */
    private double x;

    /** The y. */
    private double y;

    /** The width. */
    private double width;

    /** The height. */
    private double height;

    /* == methods for external stuff == */

    /**
     * Instantiates a new e bounding box.
     * @param boundingBox the bounding box
     */
    public EBoundingBox(
        final BoundingBox2d boundingBox) {
        super(boundingBox.isInclusive());
        x = boundingBox.getNorthWest().getX();
        y = boundingBox.getNorthWest().getY();
        width = boundingBox.getSouthEast().getX()
            - x;
        height = boundingBox.getSouthEast().getY()
            - y;
        /* fixRectOrientation(); */
        Reporting.logExpected(
            "inclusive %1$s",
            isInclusive());
    }

    /**
     * Instantiates a new e bounding box.
     * @param aX the x
     * @param aY the y
     * @param w the w
     * @param h the h
     * @param i the i
     */
    public EBoundingBox(
        final double aX,
        final double aY,
        final double w,
        final double h,
        final boolean i) {
        super(i);
        this.x = aX;
        this.y = aY;
        width = w;
        height = h;
    }

    /**
     * Gets the bounding box.
     * @return the bounding box
     */
    BoundingBox2d getBoundingBox() {
        Reporting.logExpected(
            "inclusive %1$s",
            isInclusive());
        final Point2d northWest = new Point2d(
            x,
            y);
        final Point2d southEast = new Point2d(
            x
                + width,
            y
                + height);
        return new BoundingBox2d(
            northWest,
            southEast,
            isInclusive());
    }

    /**
     * Clone rect.
     * @return the e bounding box
     */
    EBoundingBox cloneRect() {
        return new EBoundingBox(
            x,
            y,
            width,
            height,
            isInclusive());
    }

    //
    // // ---------------------------------------
    // // -- Helper methods
    // // ---------------------------------------
    //
    /* private void fixRectOrientation() { */
    /* if (width < 0) { */
    /* x = x + width; */
    /* width = -1 * width; */
    // }
    /* if (height < 0) { */
    /* y = y + height; */
    /* height = -1 * height; */
    // }
    // }

    /**
     * Gets the modifier point at helper.
     * @param destX the dest x
     * @param destY the dest y
     * @param srcX the src x
     * @param srcY the src y
     * @param modifierDiameterH the modifier diameter horizontal
     * @param modifierDiamV the modifier diameter vertical
     * @return the modifier point at helper
     */
    private Point2D getModifierPointAtHelper(
        final double destX,
        final double destY,
        final double srcX,
        final double srcY,
        final double modifierDiameterH,
        final double modifierDiamV) {
        if (EPosition.isNearPoint(
            destX,
            destY,
            srcX,
            srcY,
            modifierDiameterH,
            modifierDiamV)) {
            return new Point2D.Double(
                destX,
                destY);
        }
        return null;
    }

    /* == EAbstractShape/Item Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
        final Point2D point,
        final DrawingContext dc) {

        return (((x < point.getX()) && (point.getX() < x
            + width)) || ((x > point.getX()) && (point.getX() > x
            + width)))
            && (((y < point.getY()) && (point.getY() < y
                + height)) || ((y > point.getY()) && (point.getY() > y
                + height)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawItem(
        final Graphics g,
        final DrawingContext dc) {
        setIncColor(getInclusiveColor());
        setExcColor(getExclusiveColor());
        g.setColor(getMainColor());
        Reporting.logExpected(
            "%1$s",
            getMainColor());
        final Point2D srcp = dc.translatePointToCanvas(new Point2D.Double(
            x,
            y));
        final Point2D pt = dc.translatePointToCanvas(new Point2D.Double(
            x
                + width,
            y
                + height));
        final Line2D l2d = new Line2D.Double(
            srcp,
            pt);
        final Rectangle2D rect = l2d.getBounds2D();
        g.fillRect(
            (int) rect.getX(),
            (int) rect.getY(),
            (int) rect.getWidth(),
            (int) rect.getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(
        final double dx,
        final double dy) {
        x += dx;
        y += dy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawSelectedOverlay(
        final Graphics g,
        final double dx,
        final double dy,
        final DrawingContext dc) {
        g.setColor(getMainColor());
        drawItem(
            g,
            dc);

        final EBoundingBox rect = cloneRect();
        rect.translate(
            dx,
            dy);

        rect.setIncColor(dc.getSelectionOverlayColor());
        rect.setExcColor(dc.getSelectionOverlayColor());
        rect.drawItem(
            g,
            dc);

        drawModifierPoint(
            g,
            dc,
            x,
            y);
        drawModifierPoint(
            g,
            dc,
            x
                + width,
            y);
        drawModifierPoint(
            g,
            dc,
            x
                + width,
            y
                + height);
        drawModifierPoint(
            g,
            dc,
            x,
            y
                + height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point2D getModifierPointAt(
        final Point2D point,
        final double modifierDiamH,
        final double modifierDiamV) {
        Point2D p = null;
        p = getModifierPointAtHelper(
            x,
            y,
            point.getX(),
            point.getY(),
            modifierDiamH,
            modifierDiamV);
        if (p != null) {
            return p;
        }
        p = getModifierPointAtHelper(
            x
                + width,
            y,
            point.getX(),
            point.getY(),
            modifierDiamH,
            modifierDiamV);
        if (p != null) {
            return p;
        }
        p = getModifierPointAtHelper(
            x,
            y
                + height,
            point.getX(),
            point.getY(),
            modifierDiamH,
            modifierDiamV);
        if (p != null) {
            return p;
        }
        p = getModifierPointAtHelper(
            x
                + width,
            y
                + height,
            point.getX(),
            point.getY(),
            modifierDiamH,
            modifierDiamV);
        if (p != null) {
            return p;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawResizingOverlay(
        final Graphics g,
        final double dx,
        final double dy,
        final Point2D point,
        final DrawingContext dc) {
        drawItem(
            g,
            dc);
        final EBoundingBox rect = cloneRect();
        if (point != null) {
            rect.translateModifierPoint(
                point,
                dx,
                dy);
        }
        rect.setIncColor(dc.getSelectionOverlayColor());
        rect.setExcColor(dc.getSelectionOverlayColor());
        rect.drawSelectedOverlay(
            g,
            0,
            0,
            dc);
    }

    /**
     * Draw modifier point.
     * @param g the g
     * @param dc the dc
     * @param aX the x
     * @param aY the y
     */
    void drawModifierPoint(
        final Graphics g,
        final DrawingContext dc,
        final double aX,
        final double aY) {
        final Point2D pt = dc.translatePointToCanvas(new Point2D.Double(
            aX,
            aY));
        dc.drawModifierPoint(
            g,
            pt.getX(),
            pt.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translateModifierPoint(
        final Point2D p,
        final double dx,
        final double dy) {
        final Point2D northEast = new Point2D.Double(
            x,
            y);
        final Point2D northWest = new Point2D.Double(
            x
                + width,
            y);
        final Point2D southWest = new Point2D.Double(
            x
                + width,
            y
                + height);
        final Point2D southEast = new Point2D.Double(
            x,
            y
                + height);

        if (northEast.equals(p)) {
            /* top left point */
            x += dx;
            y += dy;
            width -= dx;
            height -= dy;
        }
        if (northWest.equals(p)) {
            /* top right point */
            y += dy;
            width += dx;
            height -= dy;
        }
        if (southWest.equals(p)) {
            /* bottom right point */
            width += dx;
            height += dy;
        }
        if (southEast.equals(p)) {
            /* bottom left */
            x += dx;
            width -= dx;
            height += dy;
        }
        if (width < 0) {
            x = x
                + width;
            width = -1
                * width;
        }
        if (height < 0) {
            y = y
                + height;
            height = -1
                * height;
        }
    }

}
