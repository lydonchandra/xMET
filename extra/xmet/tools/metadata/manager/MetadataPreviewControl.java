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
package xmet.tools.metadata.manager;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URI;
import java.nio.ByteBuffer;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import n.io.bin.Files;
import n.io.xml.SAXONUtils;
import n.reporting.Reporting;

import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXLabel;

import xmet.profiles.codecs.DataCodec;

/**
 * A control for previewing metadata.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class MetadataPreviewControl
    extends JPanel
    implements
    Runnable {

    /** The preview pane. */
    private JEditorPane htmlPreviewPane;

    /** The xml preview pane. */
    // private JXEditorPane xmlPreviewPane;

    /** The split view. */
    private JTabbedPane splitView;

    /** The file name. */
    private MetadataFile file;

    /** The preview file name. */
    private URI previewFile;

    /** The codec. */
    private DataCodec codec;

    /**
     * Instantiates a new metadata preview control.
     * @param aFile the file name
     * @param previewSheetFileName the preview sheet
     * @param aCodec the codec
     */
    public MetadataPreviewControl(
        final MetadataFile aFile,
        final URI previewSheetFileName,
        final DataCodec aCodec) {
        setLayout(new BorderLayout());
        add(new JScrollPane(
            new JXLabel(
                "<html><h2>Generating preview...")));
        update(
            aFile,
            previewSheetFileName,
            aCodec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        Reporting.logExpected("Start");
        try {
            if (previewFile != null) {
                if (splitView == null) {
                    removeAll();
                    splitView = new JTabbedPane(
                        SwingConstants.TOP);
                    add(splitView);
                    revalidate();
                }
                splitView.removeAll();
                final String contents = new String(
                    file.getPreviewContents(
                        codec).array());
                // {
                /* html view */
                final String content = SAXONUtils.transformString(
                    new String(
                        Files.read(
                            previewFile).array()),
                    contents);
                final File tempFile = Files.createTempFile(
                    "xmet_",
                    ".html");
                Files.write(
                    tempFile,
                    ByteBuffer.wrap(content.getBytes()));
                htmlPreviewPane = new JXEditorPane(
                    "file:///"
                        + tempFile.getAbsolutePath());
                /* Reporting.log(tempFile.getAbsolutePath()); */
                htmlPreviewPane.setEditable(false);
                splitView.addTab(
                    "Preview",
                    new JScrollPane(
                        htmlPreviewPane));
                // }
                // {
                /* xml view */
                htmlPreviewPane = new JXEditorPane();
                htmlPreviewPane.setText(contents);
                splitView.addTab(
                    "XML Preview",
                    new JScrollPane(
                        htmlPreviewPane));
                // }
            } else {
                update("please select a file to preview");
            }
        } catch (final Exception e) {
            final ByteArrayOutputStream naos = new ByteArrayOutputStream();
            final PrintStream ps = new PrintStream(
                naos);
            e.printStackTrace(ps);
            ps.close();
            update("preview generation failed <pre>"
                + naos.toString()
                + "</pre>");
            e.printStackTrace();
        }
        Reporting.logExpected("Finish");
    }

    /**
     * Update.
     * @param aFile the file
     * @param previewSheetFileName the preview sheet file name
     * @param aCodec the codec
     */
    public void update(
        final MetadataFile aFile,
        final URI previewSheetFileName,
        final DataCodec aCodec) {
        this.file = aFile;
        this.codec = aCodec;
        previewFile = previewSheetFileName;
        EventQueue.invokeLater(this);
    }

    /**
     * Update.
     * @param string the string
     */
    public void update(
        final String string) {
        removeAll();
        splitView = null;
        add(new JScrollPane(
            new JXLabel(
                "<html><h2>"
                    + string)));
    }
}
