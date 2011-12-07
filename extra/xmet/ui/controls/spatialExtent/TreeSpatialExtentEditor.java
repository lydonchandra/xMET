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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import n.reporting.Reporting;
import n.ui.TreeModelListenersList;
import xmet.profiles.geometry.Point;
import xmet.profiles.geometry.impl2d.BoundingBox2d;
import xmet.profiles.geometry.impl2d.ExtentId2d;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Polyline2d;
import xmet.profiles.geometry.impl2d.SpatialExtent2d;
import xmet.profiles.geometry.impl2d.Surface2d;
import xmet.profiles.geometry.impl2d.SurfaceHole2d;

/**
 * Spatial Extent Editor View which uses a tree for editing and showing.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class TreeSpatialExtentEditor
    implements
    SpatialExtentView,
    TreeModel,
    TreeCellEditor,
    TreeCellRenderer {

    /** The control. */
    private final SpatialExtentControl control;

    /** The extent. */
    private SpatialExtent2d extent;

    /**
     * Instantiates a new tree spatial extent editor.
     * @param aControl the control
     */
    public TreeSpatialExtentEditor(
        final SpatialExtentControl aControl) {
        super();
        this.control = aControl;
    }

    /** The editing tree. */
    private JTree editingTree = null;

    /** The editing tree scroll pane. */
    private JScrollPane editingTreeScrollPane;

    /* == Helper Notification == */
    /**
     * Helper met to be called to notify that a child node has changed. The
     * method sends a path changed notification to the listeners and then sends
     * a path children children notification and then expands all the rows and
     * then notifies the control for any changes
     */
    public void changeNotification() {
        TreePath selectionPath = editingTree.getSelectionPath();
        if (selectionPath == null) {
            selectionPath = editingTree.getPathForRow(0);
            editingTree.setSelectionPath(selectionPath);
        }
        listeners.pathChanged(selectionPath);
        Reporting.logExpected(
            "pathChanged %1$s",
            editingTree.getSelectionRows()[0]);
        listeners.pathChildrenChanged(selectionPath);
        for (int i = 0; i < editingTree.getRowCount(); i++) {
            editingTree.expandRow(i);
        }
        control.notifyObserversIfChanged();
    }

    /**
     * Helper met to be called to notify that a child node has changed. The
     * method selects the specified row and then sends a path changed
     * notification to the listeners and then sends a path children children
     * notification and then expands all the rows and then notifies the control
     * for any changes
     * @param rowNo the row no
     */
    public void changeNotification(
        final int rowNo) {
        editingTree.setSelectionRow(0);
        final TreePath selectionPath = editingTree.getSelectionPath();
        listeners.pathChanged(selectionPath);
        Reporting.logExpected(
            "pathChanged %1$s",
            rowNo);
        listeners.pathChildrenChanged(selectionPath);
        for (int i = 0; i < editingTree.getRowCount(); i++) {
            editingTree.expandRow(i);
        }
        control.notifyObserversIfChanged();
    }

    /**
     * Helper met to be called to notify that a child node has changed. The
     * method selects the specified row and then sends a path changed
     * notification to the listeners and then sends a path children children
     * notification and then expands all the rows and then notifies the control
     * for any changes
     * @param rowNo the row no
     */
    public void softChangeNotification(
        final int rowNo) {
        /* editingTree.setSelectionRow(0); */
        final TreePath selectionPath = editingTree.getSelectionPath();
        listeners.pathChanged(selectionPath);
        Reporting.logExpected(
            "pathChanged %1$s",
            rowNo);
        /* listeners.pathChildrenChanged(selectionPath); */
        /* for (int i = 0; i < editingTree.getRowCount(); i++) { */
        /* editingTree.expandRow(i); */
        // }
        control.notifyObserversIfChanged();
    }

    /* == SpatialExtentView Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(
        final SpatialExtent2d aExtent) {
        this.extent = aExtent;
        /* build tree and panel */
        editingTree = new JTree() {

            @Override
            protected void setExpandedState(
                final TreePath path,
                final boolean state) {
                super.setExpandedState(
                    path,
                    true);
            };
        };
        listeners = new TreeModelListenersList(
            editingTree);
        editingTree.setModel(this);
        editingTree.setRootVisible(true);
        editingTree.setCellRenderer(this);
        editingTree.setCellEditor(this);
        editingTree.setEditable(true);
        editingTree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
        editingTree.setSelectionRow(0);
        editingTreeScrollPane = new JScrollPane(
            editingTree);
        /* refresh all */
        changeNotification();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpatialExtent2d commit() {
        /* stop editing */
        /* editingTree.stopEditing(); */
        return extent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getUI() {
        return editingTreeScrollPane;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawPointCallback() {
        extent.getShapes().add(
            new Point2d(
                0,
                0));
        changeNotification(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawPolyLineCallback() {
        final Polyline2d polyLine = new Polyline2d(
            new ArrayList<Point>());
        polyLine.getPoints().add(
            new Point2d(
                0,
                0));
        polyLine.getPoints().add(
            new Point2d(
                0,
                0));
        extent.getShapes().add(
            polyLine);
        changeNotification(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawBoundingBoxCallback() {
        extent.getShapes().add(
            new BoundingBox2d());
        changeNotification(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawSurfaceCallback() {
        extent.getShapes().add(
            new Surface2d());
        changeNotification(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawSurfaceHoleCallback() {
        final TreePath selectPdf = editingTree.getSelectionPath();
        if (selectPdf != null) {
            final Object node = selectPdf.getLastPathComponent();
            if (node instanceof Surface2d) {
                ((Surface2d) node).getInteriorHoles().add(
                    new SurfaceHole2d());
            }
        }
        changeNotification();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawCodeCallback() {
        extent.getShapes().add(
            new ExtentId2d(
                "",
                "",
                true));
        changeNotification(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteItemCallback() {
        Object lastPathItem = null;
        Object lstParentPathItem = null;
        // {
        TreePath selectedPath = editingTree.getSelectionPath();
        if (selectedPath != null) {
            lastPathItem = selectedPath.getLastPathComponent();
            selectedPath = selectedPath.getParentPath();
            if (selectedPath != null) {
                lstParentPathItem = selectedPath.getLastPathComponent();
                if ((lastPathItem != null)
                    && (lstParentPathItem != null)) {
                    if (lstParentPathItem == this) {
                        extent.getShapes().remove(
                            lastPathItem);
                    } else {
                        if (lstParentPathItem instanceof Point2d) {
                            Reporting.logExpected("Nothing to delete");
                        } else if (lstParentPathItem instanceof Polyline2d) {
                            ((Polyline2d) lstParentPathItem)
                                .getPoints()
                                .remove(
                                    lastPathItem);
                        } else if (lstParentPathItem instanceof BoundingBox2d) {
                            Reporting.logExpected("Nothing to delete");
                        } else if (lstParentPathItem instanceof Surface2d) {
                            if (lastPathItem instanceof Point2d) {
                                ((Surface2d) lstParentPathItem)
                                    .getExteriorPoints()
                                    .remove(
                                        lastPathItem);
                            } else {
                                ((Surface2d) lstParentPathItem)
                                    .getInteriorHoles()
                                    .remove(
                                        lastPathItem);
                            }
                        } else if (lstParentPathItem instanceof SurfaceHole2d) {
                            ((SurfaceHole2d) lstParentPathItem)
                                .getBoundaryPoints()
                                .remove(
                                    lastPathItem);
                        } else {
                            Reporting.logUnexpected();
                        }
                    }
                }
            }
            editingTree.setSelectionPath(selectedPath);
            changeNotification();
        }
        // }
    }

    /** The listeners. */
    private TreeModelListenersList listeners;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTreeModelListener(
        final TreeModelListener arg0) {
        listeners.addTreeModelListener(arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getChild(
        final Object node,
        final int index) {
        if (node == this) {
            return extent.getShapes().get(
                index);
        } else {
            if (node instanceof Point2d) {
                return null;
            } else if (node instanceof Polyline2d) {
                return ((Polyline2d) node).getPoints().get(
                    index);
            } else if (node instanceof BoundingBox2d) {
                return null;
            } else if (node instanceof Surface2d) {
                if (index < ((Surface2d) node).getExteriorPoints().size()) {
                    return ((Surface2d) node).getExteriorPoints().get(
                        index);
                } else {
                    return ((Surface2d) node).getInteriorHoles().get(
                        index
                            - ((Surface2d) node).getExteriorPoints().size());
                }
            } else if (node instanceof SurfaceHole2d) {
                return ((SurfaceHole2d) node).getBoundaryPoints().get(
                    index);
            } else {
                Reporting.logUnexpected();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getChildCount(
        final Object node) {
        if (node == this) {
            return extent.getShapes().size();
        } else {
            if (node instanceof Point2d) {
                return 0;
            } else if (node instanceof Polyline2d) {
                return ((Polyline2d) node).getPoints().size();
            } else if (node instanceof BoundingBox2d) {
                return 0;
            } else if (node instanceof SurfaceHole2d) {
                return ((SurfaceHole2d) node).getBoundaryPoints().size();
            } else if (node instanceof Surface2d) {
                return ((Surface2d) node).getInteriorHoles().size()
                    + ((Surface2d) node).getExteriorPoints().size();
            } else if (node instanceof ExtentId2d) {
                return 0;
            } else {
                Reporting.logUnexpected();
            }
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndexOfChild(
        final Object node,
        final Object child) {
        if (node == this) {
            return extent.getShapes().indexOf(
                child);
        } else {
            if (node instanceof Point2d) {
                return -1;
            } else if (node instanceof Polyline2d) {
                return ((Polyline2d) node).getPoints().indexOf(
                    child);
            } else if (node instanceof BoundingBox2d) {
                return -1;
            } else if (node instanceof Surface2d) {
                int varI;
                if (((Surface2d) node).getExteriorPoints() == child) {
                    varI = 0;
                } else {
                    varI = ((Surface2d) node).getInteriorHoles().indexOf(
                        child) + 1;
                }
                return varI;
            } else if (node instanceof SurfaceHole2d) {
                return ((SurfaceHole2d) node).getBoundaryPoints().indexOf(
                    child);
            } else {
                Reporting.logUnexpected();
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRoot() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf(
        final Object node) {
        return ((node instanceof Point2d) || (node instanceof ExtentId2d));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTreeModelListener(
        final TreeModelListener arg0) {
        listeners.removeTreeModelListener(arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueForPathChanged(
        final TreePath arg0,
        final Object arg1) {

    }

    /** The editor listeners. */
    private final ArrayList<CellEditorListener> editorListeners =
        new ArrayList<CellEditorListener>();

    /** The current editor. */

    private TSEETreeCellEditor currentEditor;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCellEditorListener(
        final CellEditorListener arg0) {
        editorListeners.add(arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelCellEditing() {
        stopCellEditing();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCellEditorValue() {
        Reporting.logExpected("getCellEditorValue");
        if (currentEditor != null) {
            return currentEditor.getItem();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(
        final EventObject event) {
        if (event != null) {
            if (event.getSource() instanceof JTree) {
                if (event instanceof MouseEvent) {
                    final TreePath path = editingTree.getPathForLocation(
                        ((MouseEvent) event).getX(),
                        ((MouseEvent) event).getY());
                    if (path != null) {
                        final Object value = path.getLastPathComponent();
                        if (value != null) {

                            return true;

                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCellEditorListener(
        final CellEditorListener arg0) {
        editorListeners.remove(arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldSelectCell(
        final EventObject arg0) {
        /* Reporting.log("shouldSelectCell"); */
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopCellEditing() {
        Reporting.logExpected("stopCellEditing");
        if (currentEditor != null) {
            currentEditor.commit();
            for (final CellEditorListener listener : editorListeners) {
                listener.editingStopped(new ChangeEvent(
                    currentEditor));
            }
            changeNotification(currentEditor.getLastRow());
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellEditorComponent(
        final JTree tree,
        final Object value,
        final boolean isSelected,
        final boolean expanded,
        final boolean leaf,
        final int row) {
        /* Reporting.log("Editor Component %1$d", row); */
        currentEditor = (TSEETreeCellEditor) getTreeCellRendererComponent(
            tree,
            value,
            true,
            expanded,
            leaf,
            row,
            true);
        return currentEditor;
    }

    /** The editors. */
    private final Map<Object, TSEETreeCellEditor> editors =
        new HashMap<Object, TSEETreeCellEditor>();

    /** The background color. */

    private static Color backgroundColor;

    /** The selection background color. */

    private static Color selectionBackgroundColor;
    static {
        backgroundColor = UIManager.getColor("Tree.background");
        selectionBackgroundColor =
            UIManager.getColor("Tree.selectionBackground");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(
        final JTree tree,
        final Object value,
        final boolean selected,
        final boolean expanded,
        final boolean leaf,
        final int row,
        final boolean hasFocus) {
        /* Reporting.log("Renderer Component %1$d", row); */
        if (value != null) {
            TSEETreeCellEditor retVal = editors.get(value);
            if (retVal == null) {
                if (value instanceof TreeSpatialExtentEditor) {
                    retVal = new LabelTSEETCE(
                        "Extent",
                        this,
                        control);
                } else if (value instanceof Point2d) {
                    retVal = new PointTSEETCE(
                        this,
                        control);
                } else if (value instanceof Polyline2d) {
                    retVal = new LineTSEETCE(
                        this,
                        control);
                } else if (value instanceof Surface2d) {
                    retVal = new SurfaceTSEETCE(
                        this,
                        control);
                } else if (value instanceof BoundingBox2d) {
                    retVal = new BoundingBoxTSEETCE(
                        this,
                        control);
                } else if (value instanceof SurfaceHole2d) {
                    retVal = new SurfaceHoleTSEETCE(
                        this,
                        control);
                } else if (value instanceof ExtentId2d) {
                    retVal = new CodeTSEETCE(
                        this,
                        control);
                } else {
                    Reporting.logUnexpected(
                        "%1$s",
                        value);
                    retVal = new LabelTSEETCE(
                        "Unimplemented",
                        this,
                        control);
                }
                retVal.configureFor(value);
                editors.put(
                    value,
                    retVal);
            }
            if (retVal != null) {
                retVal.setLastRow(row);
                if (selected) {
                    retVal.setBackground(selectionBackgroundColor);
                } else {
                    retVal.setBackground(backgroundColor);
                }
            }
            return retVal;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasConfigurationDialog() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showConigurationDialogCallback() {

    }
}
