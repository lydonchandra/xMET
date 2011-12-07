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

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import n.io.bin.Files;
import n.reporting.Reporting;
import n.ui.JFileChooserUtils;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.profiles.geometry.Point;
import xmet.profiles.geometry.Shape;
import xmet.profiles.geometry.SurfaceHole;
import xmet.profiles.geometry.impl2d.BoundingBox2d;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Surface2d;

import com.jhlabs.map.proj.Projection;
import com.jhlabs.map.proj.ProjectionFactory;

/**
 * A dialog for importing Spatial Extent information.
 * @author Nahid Akbar
 */
public class SpatialExtentImportDialog {

    /** The Constant SHP_FILE_HEADER_QUAD. */
    private static final int SHP_FILE_HEADER_QUAD = 0x0000270a;

    /** The Constant PROJECTION_WGS84. */
    private static final String PROJECTION_WGS84 = "WGS84";

    /** The Constant TYPE_CSV. */
    private static final String TYPE_CSV = "CSV";

    /** The Constant TYPE_SHP. */
    private static final String TYPE_SHP = "SHP";

    /** The Constant ACTION_NONE. */
    private static final String ACTION_NONE = "None";

    /** The Constant ACTION_CONVERT_TO_BOUNDING_BOX. */
    private static final String ACTION_CONVERT_TO_BOUNDING_BOX =
        "Convert To Bounding Box";

    /** The items. */
    private final ArrayList<Shape> items = new ArrayList<Shape>();

    /** The file text field. */
    private JTextField fileTextField;

    /** The type combo box. */
    private JComboBox typeComboBox;

    /** The post process combo box. */
    private JComboBox postProcessComboBox;

    /** The projection combo box. */
    private JComboBox projectionComboBox;

    /** Ok button callback. */
    public void okButtonCallback() {
        String filePath = fileTextField.getText();
        if (filePath != null
            && filePath.trim().length() != 0) {
            filePath = filePath.trim();
            final File file = new File(
                filePath);
            if (file.exists()) {
                final ArrayList<Point> points = extractFilePoints(
                    file,
                    (String) typeComboBox.getSelectedItem(),
                    (String) projectionComboBox.getSelectedItem());
                if (points.size() > 0) {
                    final Object varSelectedItem =
                        postProcessComboBox.getSelectedItem();
                    final boolean varNoAction = varSelectedItem == ACTION_NONE;
                    final boolean varConvBbox =
                        varSelectedItem == ACTION_CONVERT_TO_BOUNDING_BOX;
                    if (varNoAction) {
                        items.add(new Surface2d(
                            points,
                            new ArrayList<SurfaceHole>(),
                            true));
                    } else

                    if (varConvBbox) {
                        if (points.size() > 1) {
                            items.add(extractBBOX(points));
                        } else {
                            Reporting
                                .reportUnexpected("need at least two points"
                                    + " for bounding box conversion");
                        }
                    } else {
                        Reporting.logUnexpected();
                    }

                } else {
                    Reporting.reportUnexpected("no latitude and longitude"
                        + " information could be extracted");
                }
                cancelButtonCallback();
            } else {
                Reporting.reportUnexpected("File specified does not exist");
            }
        } else {
            Reporting.reportUnexpected("File not specified");
        }
    }

    /**
     * Extract bbox.
     * @param points the points
     * @return the bounding box2d
     */
    private BoundingBox2d extractBBOX(
        final ArrayList<Point> points) {
        double minLat = points.get(
            0).getY();
        double maxLat = minLat;
        double minLon = points.get(
            0).getX();
        double maxLon = minLon;
        for (final Point point : points) {
            final double lat = point.getY();
            final double lon = point.getX();
            if (lat < minLat) {
                minLat = lat;
            }
            if (lat > maxLat) {
                maxLat = lat;
            }
            if (lon < minLon) {
                minLon = lon;
            }
            if (lon > maxLon) {
                maxLon = lon;
            }
        }
        return new BoundingBox2d(
            new Point2d(
                minLon,
                maxLat),
            new Point2d(
                maxLon,
                minLat),
            true);
    }

    /**
     * Extract file points.
     * @param file the file
     * @param format the format
     * @param projection the projection
     * @return the array list
     */
    private ArrayList<Point> extractFilePoints(
        final File file,
        final String format,
        final String projection) {
        // CHECKSTYLE OFF: MagicNumber
        final ArrayList<Point> points = new ArrayList<Point>();
        if (format.equals(TYPE_CSV)) {
            try {
                final Scanner scanner = new Scanner(
                    file);
                while (scanner.hasNext()) {
                    try {
                        String line = scanner.nextLine();
                        if (line != null
                            && line.trim().length() != 0) {
                            line = line.trim();
                            final String[] fields = line.split(",");
                            if ((fields.length > 0)
                                && (fields.length % 2 == 0)) {
                                for (int i = 0; i < fields.length; i += 2) {
                                    Double lat = Double.NaN;
                                    Double lon = Double.NaN;
                                    try {
                                        final String latitude = fields[i];
                                        lat = Double.parseDouble(latitude);
                                        final String longitude = fields[i + 1];
                                        lon = Double.parseDouble(longitude);
                                    } catch (final NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    if (!Double.isNaN(lat)
                                        && !Double.isNaN(lon)) {
                                        points.add(new Point2d(
                                            lon,
                                            lat));
                                    }
                                }
                            }
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (format.equals(TYPE_SHP)) {
            try {
                final ByteBuffer contents = Files.readFirst(
                    file,
                    100);
                final int fileCode = contents.getInt();
                if (fileCode == SHP_FILE_HEADER_QUAD) {
                    for (int i = 0; i < 5; i++) {
                        /* unused */
                        contents.getInt();
                    }
                    /* file length */
                    contents.getInt();
                    /* version */
                    contents.getInt();
                    /* shape types */
                    contents.getInt();
                    final double minX = contents.getFloat();
                    final double minY = contents.getFloat();
                    final double maxX = contents.getFloat();
                    final double maxY = contents.getFloat();
                    /* dont need to proceed any further with the file */
                    if (!Double.isNaN(minX)
                        && !Double.isNaN(minY)
                        && !Double.isNaN(maxX)
                        && !Double.isNaN(maxY)) {
                        points.add(new Point2d(
                            minX,
                            minY));
                        points.add(new Point2d(
                            minX,
                            maxY));
                        points.add(new Point2d(
                            maxX,
                            maxY));
                        points.add(new Point2d(
                            maxX,
                            minY));
                    }
                } else {
                    Reporting
                        .reportUnexpected("Invalid File Code in Shape File");
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            Reporting.logUnexpected();
        }

        if (PROJECTION_WGS84.equals(projection)) {
            Reporting.logExpected("nothing to do");
        } else {
            final Projection proj =
                ProjectionFactory.getNamedProjection(projection);
            if (proj != null) {
                for (final Point point : points) {
                    final Point2D.Double pt = new Point2D.Double();
                    proj.projectInverse(
                        point.getX(),
                        point.getY(),
                        pt);
                    Reporting.logExpected(
                        "Projected %1$f,%2$f to %3$f,%4$f",
                        point.getX(),
                        point.getY(),
                        pt.getX(),
                        pt.getY());
                    ((Point2d) point).setLocation(
                        pt.getX(),
                        pt.getY());
                }
            } else {
                Reporting.logUnexpected(
                    "%1$s",
                    projection);
            }
        }
        return points;
        // CHECKSTYLE ON: MagicNumber
    }

    /** Browse button callback. */
    public void browseButtonCallback() {
        final File f = JFileChooserUtils.getSingleOpenFileWithExtension(
            typeComboBox.getSelectedItem()
                + " Files",
            (String) typeComboBox.getSelectedItem());
        if (f != null) {
            fileTextField.setText(f.getAbsolutePath());
        }
    }

    /**
     * Rebuild editor panel.
     * @return the container
     */
    private Container rebuildEditorPanel() {
        // CHECKSTYLE OFF: MagicNumber
        final JPanel panel = SwingUtils.GridBag.getNew();
        fileTextField = new JTextField(
            20);
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "File:"),
            "x=0;y=0;i=2,2,2,2;");
        SwingUtils.GridBag.add(
            panel,
            fileTextField,
            "x=1;y=0;f=h;wx=1;");
        final Object[] params = {};
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNew(
                "Browse",
                new ClassMethodCallback(
                    this,
                    "browseButtonCallback",
                    params)),
            "x=2;y=0;i=2,2,2,2;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Format:"),
            "x=0;y=1;i=2,2,2,2;");
        typeComboBox = new JComboBox();
        SwingUtils.GridBag.add(
            panel,
            typeComboBox,
            "x=1;y=1;f=h;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Projection:"),
            "x=0;y=2;i=2,2,2,2;");
        projectionComboBox = new JComboBox();
        SwingUtils.GridBag.add(
            panel,
            projectionComboBox,
            "x=1;y=2;f=h;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Post Process:"),
            "x=0;y=3;i=2,2,2,2;");
        postProcessComboBox = new JComboBox();
        SwingUtils.GridBag.add(
            panel,
            postProcessComboBox,
            "x=1;y=3;f=h;");
        final Object[] params1 = {};
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNew(
                "Cancel",
                new ClassMethodCallback(
                    this,
                    "cancelButtonCallback",
                    params1)),
            "x=2;y=2;i=2,2,2,2;f=b;");
        final Object[] params2 = {};
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNew(
                "Ok",
                new ClassMethodCallback(
                    this,
                    "okButtonCallback",
                    params2)),
            "x=2;y=3;i=2,2,2,2;f=b;");
        panel.setBorder(BorderFactory.createEmptyBorder(
            5,
            5,
            5,
            5));
        typeComboBox.addItem(TYPE_CSV);
        typeComboBox.addItem(TYPE_SHP);
        typeComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                fileTextField.setText("");
            }
        });
        postProcessComboBox.addItem(ACTION_NONE);
        postProcessComboBox.addItem(ACTION_CONVERT_TO_BOUNDING_BOX);
        projectionComboBox.addItem(PROJECTION_WGS84);
        for (final Object p : ProjectionFactory.getOrderedProjectionNames()) {
            final Projection projection =
                ProjectionFactory.getNamedProjection((String) p);
            if (projection != null) {
                projectionComboBox.addItem(p);
            }
        }
        return panel;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Show import dialog.
     * @param title the title
     * @return the array list
     */
    public final ArrayList<Shape> showImportDialog(
        final String title) {
        // CHECKSTYLE OFF: MagicNumber
        // {
        dialog = SwingUtils.DIALOG.createDialog(
            rebuildEditorPanel(),
            title,
            800,
            600,
            true);
        dialog.pack();
        SwingUtils.WINDOW.centreWindow(dialog);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // }
        return items;
        // CHECKSTYLE ON: MagicNumber
    }

    /** The dialog. */
    private JDialog dialog = null;

    /** Cancel button callback. */
    public final void cancelButtonCallback() {
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
        }
    }
}
