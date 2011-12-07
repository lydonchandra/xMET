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
package xmet.ui.controls;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JToolBar;

import n.reporting.Reporting;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.ClientContext;
import xmet.profiles.catalogs.model.CodeItem;
import xmet.profiles.catalogs.model.Codelist;
import xmet.profiles.catalogs.model.CodelistCatalog;
import xmet.profiles.catalogs.model.SECodeItem;
import xmet.profiles.codecs.impl.StringSpatialExtentCodec;
import xmet.profiles.geometry.BoundingBox;
import xmet.profiles.geometry.ExtentId;
import xmet.profiles.geometry.Point;
import xmet.profiles.geometry.Polyline;
import xmet.profiles.geometry.Shape;
import xmet.profiles.geometry.Surface;
import xmet.profiles.geometry.SurfaceHole;
import xmet.profiles.geometry.impl2d.BoundingBox2d;
import xmet.profiles.geometry.impl2d.ExtentId2d;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Polyline2d;
import xmet.profiles.geometry.impl2d.SpatialExtent2d;
import xmet.profiles.geometry.impl2d.Surface2d;
import xmet.profiles.geometry.impl2d.SurfaceHole2d;
import xmet.ui.ToolbarBuilder;
import xmet.ui.ToolbarItem;
import xmet.ui.controls.spatialExtent.MapsSpatialExtentEditor;
import xmet.ui.controls.spatialExtent.SpatialExtentControl;
import xmet.ui.controls.spatialExtent.SpatialExtentImportDialog;
import xmet.ui.controls.spatialExtent.SpatialExtentView;
import xmet.ui.controls.spatialExtent.TreeSpatialExtentEditor;
import xmet.ui.mapping.MappingModule;
import xmet.ui.mapping.SpatialExtentListModifierMMUC;

/**
 * For editing SpatialExtent.
 * @author Nahid Akbar
 */
@SuppressWarnings({
    "rawtypes"
})
public class SpatialExtent
    extends GUIObject
    implements
    SpatialExtentGUIObject,
    SpatialExtentControl {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The extent. */
    private SpatialExtent2d extent = new SpatialExtent2d();

    /**
     * The Enum EditorMode.
     */
    private static enum EditorMode {

        /** The TRE e_ tabl e_ editor. */
        TREE_EDITOR,
        /** The MAP s_ editor. */
        MAPS_EDITOR
    }

    /** The current mode. */
    private EditorMode currentMode = EditorMode.TREE_EDITOR;

    /** The view. */
    private transient SpatialExtentView view;

    /* == Code catalog editing == */
    /** The catalog. */
    private CodelistCatalog catalog;

    /**
     * Sets the catalog.
     * @param aCatalog the new catalog
     */
    public void setCatalog(
        final CodelistCatalog aCatalog) {
        this.catalog = aCatalog;
        rebuildPanel();
    }

    /* == MAPS_EDITOR == */

    /* == Toolbar == */
    /** The toolbar. */
    private JToolBar toolbar;

    /**
     * Rebuild toolbar.
     */
    void rebuildToolbar() {
        // CHECKSTYLE OFF: MagicNumber

        final ToolbarBuilder tBuilder = new ToolbarBuilder(
            getContext(),
            "Spatial Exent",
            false,
            true);

        tBuilder.getItems().add(
            new ToolbarItem(
                "Add Geographic Identifier",
                "images/control.spatialExtent.geoId.png",
                new ClassMethodCallback(
                    this,
                    "draw",
                    ExtentId2d.class)));
        tBuilder.getItems().add(
            new ToolbarItem(
                "Add Point",
                "images/control.spatialExtent.point.png",
                new ClassMethodCallback(
                    this,
                    "draw",
                    Point2d.class)));
        tBuilder.getItems().add(
            new ToolbarItem(
                "Add Line",
                "images/control.spatialExtent.line.png",
                new ClassMethodCallback(
                    this,
                    "draw",
                    Polyline2d.class)));
        tBuilder.getItems().add(
            new ToolbarItem(
                "Add Bounding Box",
                "images/control.spatialExtent.boundingBox.png",
                new ClassMethodCallback(
                    this,
                    "draw",
                    BoundingBox2d.class)));
        tBuilder.getItems().add(
            new ToolbarItem(
                "Add Polygon Surface",
                "images/control.spatialExtent.polygon.png",
                new ClassMethodCallback(
                    this,
                    "draw",
                    Surface2d.class)));
        tBuilder.getItems().add(
            new ToolbarItem(
                "Add Patch In Selected Polygon",
                "images/control.spatialExtent.polygonPatch.png",
                new ClassMethodCallback(
                    this,
                    "draw",
                    SurfaceHole2d.class)));
        tBuilder.getItems().add(
            new ToolbarItem(
                "Delete Selected Item",
                "images/control.spatialExtent.delete.png",
                new ClassMethodCallback(
                    this,
                    "delete")));
        tBuilder.getItems().add(
            new ToolbarItem(
                "Import Items From External Files",
                "images/control.spatialExtent.import.png",
                new ClassMethodCallback(
                    this,
                    "importExtent")));

        tBuilder.getItems().add(
            new ToolbarItem(
                "Add Enclosing Bounding Box",
                "images/control.spatialExtent.bound.png",
                new ClassMethodCallback(
                    this,
                    "addEnclosingBoundingBox")));

        if (MappingModule.hasModuleOfUseCase(
            SpatialExtentListModifierMMUC.class,
            getContext())) {
            tBuilder.getItems().add(
                new ToolbarItem(
                    "Switch View",
                    "images/control.spatialExtent.switch.png",
                    new ClassMethodCallback(
                        this,
                        "switchView")));
        }

        if (view != null
            && view.hasConfigurationDialog()) {
            tBuilder.getItems().add(
                new ToolbarItem(
                    "Settings",
                    "images/control.spatialExtent.configure.png",
                    new ClassMethodCallback(
                        view,
                        "showConigurationDialogCallback")));
        }

        toolbar = tBuilder.buildCustomToolbar(
            24,
            false,
            true);
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Gets the toolbar.
     * @return the toolbar
     */
    private Component getToolbar() {
        return toolbar;
    }

    /* == Setup Methods == */

    /**
     * Instantiates a new spatial extent.
     * @param context the context
     */
    public SpatialExtent(
        final ClientContext context) {
        super(context);
        rebuildPanel();
    }

    /**
     * Switch view.
     */
    void switchView() {
        extent = view.commit();
        switch (currentMode) {
        case MAPS_EDITOR:
            currentMode = EditorMode.TREE_EDITOR;
            break;
        case TREE_EDITOR:
            currentMode = EditorMode.MAPS_EDITOR;
            break;
        default:
            break;
        }
        rebuildPanel();
    }

    /**
     * Rebuild panel.
     */
    private void rebuildPanel() {
        removeAll();
        setLayout(new BorderLayout(
            2,
            2));
        if (view != null) {
            extent = view.commit();
        }
        switch (currentMode) {
        case MAPS_EDITOR:
            view = new MapsSpatialExtentEditor(
                this,
                getContext());
            break;
        case TREE_EDITOR:
            view = new TreeSpatialExtentEditor(
                this);
            break;

        default:
            break;
        }
        view.update(extent);
        add(view.getUI());
        rebuildToolbar();
        add(
            getToolbar(),
            BorderLayout.EAST);
        revalidate();
    }

    /**
     * Draw.
     * @param clazz the clazz
     */
    public void draw(
        final Class clazz) {
        if (clazz == Point2d.class) {
            view.drawPointCallback();
        } else if (clazz == Polyline2d.class) {
            view.drawPolyLineCallback();
        } else if (clazz == BoundingBox2d.class) {
            view.drawBoundingBoxCallback();
        } else if (clazz == Surface2d.class) {
            view.drawSurfaceCallback();
        } else if (clazz == SurfaceHole2d.class) {
            view.drawSurfaceHoleCallback();
        } else if (clazz == ExtentId2d.class) {
            view.drawCodeCallback();
        }
    }

    /**
     * Import extent.
     */
    public void importExtent() {
        extent = view.commit();
        final ArrayList<Shape> imports =
            new SpatialExtentImportDialog()
                .showImportDialog("Import Spatial Extent");
        if ((imports != null)
            && (imports.size() > 0)) {
            for (final Shape shape : imports) {
                extent.getShapes().add(
                    shape);
            }
            view.update(extent);
        }
    }

    /**
     * Adds the enclosing bounding box.
     */
    public void addEnclosingBoundingBox() {
        extent = view.commit();
        final BoundingBox2d bbox = extractEnclosingBoundingBox(extent);
        if (bbox != null) {
            extent.getShapes().add(
                bbox);
        }
        view.update(extent);
        rebuildPanel();
    }

    /**
     * Delete.
     */
    public void delete() {
        view.deleteItemCallback();
    }

    /* == Misc GUIObject overrides == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        setSpatialExtent(StringSpatialExtentCodec.extractSpatialExtent(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return StringSpatialExtentCodec.encodeSpatialExtent(getSpatialExtent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpatialExtent(
        final xmet.profiles.geometry.SpatialExtent aExtent) {
        super.disableNotifications();
        if (aExtent instanceof SpatialExtent2d) {
            this.extent = (SpatialExtent2d) aExtent;
            if (view != null) {
                view.update(this.extent);
            }
            rebuildPanel();
        }
        super.setValue(getValue());
        super.enableNotifications();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public xmet.profiles.geometry.SpatialExtent getSpatialExtent() {
        extent = view.commit();
        return extent;
    }

    /* == SpatialExtentControl implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObserversIfChanged() {
        super.notifyObserversIfChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodelistCatalog getCatalog() {
        return catalog;
    }

    /* == Misc Helper Methods == */
    /**
     * Extract enclosing bounding box helper method.
     * @param aExtent the extent
     * @return the bounding box2d
     */
    // CHECKSTYLE OFF: MethodLength
    private BoundingBox2d extractEnclosingBoundingBox(
        final xmet.profiles.geometry.SpatialExtent aExtent) {
        if (aExtent != null) {
            double minLat = Double.NaN;
            double maxLat = Double.NaN;
            double minLon = Double.NaN;
            double maxLon = Double.NaN;
            for (final Shape shape : aExtent.getShapes()) {
                if (shape instanceof Surface) {
                    for (final Point point : ((Surface) shape)
                        .getExteriorPoints()) {
                        if (Double.isNaN(maxLat)
                            || point.getY() > maxLat) {
                            maxLat = point.getY();
                        }
                        if (Double.isNaN(minLat)
                            || point.getY() < minLat) {
                            minLat = point.getY();
                        }
                        if (Double.isNaN(maxLon)
                            || point.getX() > maxLon) {
                            maxLon = point.getX();
                        }
                        if (Double.isNaN(minLon)
                            || point.getX() < minLon) {
                            minLon = point.getX();
                        }
                    }
                    for (final SurfaceHole hole : ((Surface) shape)
                        .getInteriorHoles()) {
                        for (final Point point : hole.getBoundaryPoints()) {
                            if (Double.isNaN(maxLat)
                                || point.getY() > maxLat) {
                                maxLat = point.getY();
                            }
                            if (Double.isNaN(minLat)
                                || point.getY() < minLat) {
                                minLat = point.getY();
                            }
                            if (Double.isNaN(maxLon)
                                || point.getX() > maxLon) {
                                maxLon = point.getX();
                            }
                            if (Double.isNaN(minLon)
                                || point.getX() < minLon) {
                                minLon = point.getX();
                            }
                        }
                    }
                } else if (shape instanceof Polyline) {
                    for (final Point point : ((Polyline) shape).getPoints()) {
                        if (Double.isNaN(maxLat)
                            || point.getY() > maxLat) {
                            maxLat = point.getY();
                        }
                        if (Double.isNaN(minLat)
                            || point.getY() < minLat) {
                            minLat = point.getY();
                        }
                        if (Double.isNaN(maxLon)
                            || point.getX() > maxLon) {
                            maxLon = point.getX();
                        }
                        if (Double.isNaN(minLon)
                            || point.getX() < minLon) {
                            minLon = point.getX();
                        }
                    }
                } else if (shape instanceof BoundingBox) {
                    Point point = ((BoundingBox) shape).getNorthWest();
                    // {
                    if (Double.isNaN(maxLat)
                        || point.getY() > maxLat) {
                        maxLat = point.getY();
                    }
                    if (Double.isNaN(minLat)
                        || point.getY() < minLat) {
                        minLat = point.getY();
                    }
                    if (Double.isNaN(maxLon)
                        || point.getX() > maxLon) {
                        maxLon = point.getX();
                    }
                    if (Double.isNaN(minLon)
                        || point.getX() < minLon) {
                        minLon = point.getX();
                    }
                    // }
                    point = ((BoundingBox) shape).getSouthEast();
                    // {
                    if (Double.isNaN(maxLat)
                        || point.getY() > maxLat) {
                        maxLat = point.getY();
                    }
                    if (Double.isNaN(minLat)
                        || point.getY() < minLat) {
                        minLat = point.getY();
                    }
                    if (Double.isNaN(maxLon)
                        || point.getX() > maxLon) {
                        maxLon = point.getX();
                    }
                    if (Double.isNaN(minLon)
                        || point.getX() < minLon) {
                        minLon = point.getX();
                    }
                    // }
                } else if (shape instanceof Point) {
                    final Point point = (Point) shape;
                    // {
                    if (Double.isNaN(maxLat)
                        || point.getY() > maxLat) {
                        maxLat = point.getY();
                    }
                    if (Double.isNaN(minLat)
                        || point.getY() < minLat) {
                        minLat = point.getY();
                    }
                    if (Double.isNaN(maxLon)
                        || point.getX() > maxLon) {
                        maxLon = point.getX();
                    }
                    if (Double.isNaN(minLon)
                        || point.getX() < minLon) {
                        minLon = point.getX();
                    }
                    // }
                } else if (shape instanceof ExtentId) {
                    final ExtentId code = (ExtentId) shape;
                    final String codeSpace = code.getCodeSpace();
                    if ((codeSpace != null)
                        && getCatalog() != null
                        && codeSpace.startsWith(getCatalog().getCatalogURI())) {
                        if (codeSpace.indexOf('#') != -1) {
                            final String codeListName =
                                codeSpace.substring(codeSpace.indexOf('#') + 1);
                            final Codelist codeList =
                                getCatalog().getCodelistByIdentifier(
                                    codeListName);
                            if (codeList != null) {
                                for (final CodeItem itm : codeList.getItems()) {
                                    if (itm.getValue().equals(
                                        code.getCode())) {
                                        if (itm instanceof SECodeItem) {
                                            final SECodeItem sitm =
                                                (SECodeItem) itm;
                                            final BoundingBox2d bbox =
                                                extractEnclosingBoundingBox(sitm
                                                    .getExtent());
                                            if (bbox != null) {
                                                Point point =
                                                    bbox.getNorthWest();
                                                // {
                                                if (Double.isNaN(maxLat)
                                                    || point.getY() > maxLat) {
                                                    maxLat = point.getY();
                                                }
                                                if (Double.isNaN(minLat)
                                                    || point.getY() < minLat) {
                                                    minLat = point.getY();
                                                }
                                                if (Double.isNaN(maxLon)
                                                    || point.getX() > maxLon) {
                                                    maxLon = point.getX();
                                                }
                                                if (Double.isNaN(minLon)
                                                    || point.getX() < minLon) {
                                                    minLon = point.getX();
                                                }
                                                // }
                                                point = bbox.getSouthEast();
                                                // {
                                                if (Double.isNaN(maxLat)
                                                    || point.getY() > maxLat) {
                                                    maxLat = point.getY();
                                                }
                                                if (Double.isNaN(minLat)
                                                    || point.getY() < minLat) {
                                                    minLat = point.getY();
                                                }
                                                if (Double.isNaN(maxLon)
                                                    || point.getX() > maxLon) {
                                                    maxLon = point.getX();
                                                }
                                                if (Double.isNaN(minLon)
                                                    || point.getX() < minLon) {
                                                    minLon = point.getX();
                                                }
                                                // }
                                            }
                                        }
                                        break;
                                    }
                                }
                            } else {
                                Reporting.logUnexpected();
                            }
                        } else {
                            Reporting.logUnexpected();
                        }
                    }
                    // else {
                    // }

                } else {
                    Reporting.logUnexpected();
                }
            }
            if (!Double.isNaN(minLat)
                && !Double.isNaN(maxLat)
                && !Double.isNaN(minLon)
                && !Double.isNaN(maxLon)) {
                return new BoundingBox2d(
                    new Point2d(
                        minLon,
                        maxLat),
                    new Point2d(
                        maxLon,
                        minLat),
                    true);
            }
        }
        return null;
    }
    // CHECKSTYLE ON: MethodLength

}
