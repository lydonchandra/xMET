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
package xmet.tools.screen;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

import n.io.bin.Files;
import n.io.xml.CSXMLSerializationCodec;
import n.java.ReflectionUtils;
import n.reporting.Reporting;
import n.ui.JFileChooserUtils;
import n.ui.JOptionPaneUtils;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import n.ui.patterns.propertySheet.ReflectionPropertySheet;
import n.ui.patterns.sdm.SimpleSingleDocumentManagedUnit;
import n.ui.patterns.sdm.SimpleSingleDocumentManager;
import n.ui.patterns.stb.JSimpleTreeBuilder;
import n.ui.patterns.stb.JSimpleTreeBuilderModelExtended;
import xmet.tools.DefaultTool;
import xmet.tools.Tool;
import xmet.tools.ToolInstance;
import xmet.tools.ToolInstanceEvents;

/**
 * The non-WYSIWYG start screen designer.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"unused",
"rawtypes"
})
public class StartScreenDesignerTool
    extends DefaultTool {

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
        return "application.start.designer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ToolInstance invoke(
        final Map<String, Object> params) {
        return new Instance();
    }

    /**
     * The tool instance class.
     */
    private class Instance
        extends Observable
        implements
        ToolInstance,
        JSimpleTreeBuilderModelExtended,
        SimpleSingleDocumentManagedUnit<File>,
        TreeCellRenderer,
        ActionListener {

        /** The screen. */
        private StartScreen screen;

        /** The ssdm. */
        private final SimpleSingleDocumentManager<File> ssdm =
            new SimpleSingleDocumentManager<File>(
                this);

        /** The modified. */
        private boolean modified = true;

        /** The stb. */
        private JSimpleTreeBuilder stb;

        /** The panel. */
        private JPanel panel;

        /** The main panes. */
        private JSplitPane mainPanes;
        {
            // CHECKSTYLE OFF: MagicNumber
            screen = new StartScreen();
            panel = SwingUtils.BorderLayouts.getNew();
            mainPanes = new JSplitPane();
            panel.add(mainPanes);
            mainPanes.setResizeWeight(0.5);
            stb = new JSimpleTreeBuilder(
                this);
            mainPanes.setLeftComponent(stb);
            stb.setButtonPanelPosition(JSimpleTreeBuilder.BOTTOM);
            mainPanes.setRightComponent(new JLabel(
                "TODO:"));

            final JToolBar mainToolbar = new JToolBar(
                "Main Toolbar",
                SwingConstants.HORIZONTAL);
            final Object[] params = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "New Screen",
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.newDocument.png"),
                new ClassMethodCallback(
                    ssdm,
                    "onNewFile",
                    params)));
            final Object[] params1 = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Open Screen",
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.openDocument.png"),
                new ClassMethodCallback(
                    ssdm,
                    "onOpenFile",
                    params1)));
            final Object[] params2 = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Save Screen",
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.saveDocument.png"),
                new ClassMethodCallback(
                    ssdm,
                    "onSaveFile",
                    params2)));
            final Object[] params3 = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Save Screen As",
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.saveDocumentAs.png"),
                new ClassMethodCallback(
                    ssdm,
                    "onSaveFileAs",
                    params3)));
            final Object[] params4 = {
                false
            };
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Close Screen",
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.closeDocument.png"),
                new ClassMethodCallback(
                    ssdm,
                    "onCloseFile",
                    params4)));
            mainToolbar.addSeparator();
            final Object[] params5 = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Preview Screen",
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.previewDocument.png"),
                new ClassMethodCallback(
                    this,
                    "buttonPreview",
                    params5)));
            panel.add(
                mainToolbar,
                BorderLayout.NORTH);
            // CHECKSTYLE ON: MagicNumber
        }

        /** The editor. */
        private ReflectionPropertySheet editor = null;

        /* == Button Handlers == */
        /**
         * Button preview.
         */
        public void buttonPreview() {
            final boolean sticky = screen.isSticky();
            screen.setSticky(false);
            getContext().getTools().invokeToolByName(
                "application.start",
                "screenXml",
                convertSheetToString());
            screen.setSticky(sticky);
        }

        /* == ToolInstance Implementation == */

        /**
         * {@inheritDoc}
         */
        @Override
        public Tool getTool() {
            return StartScreenDesignerTool.this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle() {
            if (ssdm.getFile() == null) {
                return "New Screen";
            } else {
                return ssdm.getFile().getName();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getDisplayPanel() {
            return panel;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onInstantiation() {
            try {
                ssdm.onLoad();
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDisposal() {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onClose(
            final boolean force) {
            try {
                return ssdm.onCloseFile(force);
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
                return true;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onRefocus(
            final Map<String, Object> params) {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onEvent(
            final int event) {
            try {
                switch (event) {
                case ToolInstanceEvents.NEXT_PAGE:
                case ToolInstanceEvents.PREVIOUS_PAGE:
                    break;
                case ToolInstanceEvents.SAVE_DOCUMENT:
                    ssdm.onSaveFile();
                    break;
                case ToolInstanceEvents.SAVE_DOCUMENT_AS:
                    ssdm.onSaveFileAs();
                    break;
                case ToolInstanceEvents.NEW_DOCUMENT:
                    ssdm.onNewFile();
                    break;
                case ToolInstanceEvents.OPEN_DOCUMENT:
                    ssdm.onOpenFile();
                    break;
                default:
                    Reporting.logUnexpected("unhandled event "
                        + event);
                    break;
                }
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        }

        /* == JSimpleTreeBuilderModelExtended Implementation == */

        /**
         * {@inheritDoc}
         */
        @Override
        public Class[] getAllowedChildrenClasses(
            final Object parent) {
            if (parent == null) {
                return new Class[] {
                    StartScreen.class
                };
            }
            if (parent instanceof StartScreen) {
                return new Class[] {
                LogoStartScreenItem.class,
                ToolLinkStartScreenItem.class,
                NewMetadataStartScreenItem.class,
                StartScreenLinkItem.class,
                TextStartScreenItem.class
                };
            }
            return new Class[0];
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addChildren(
            final Object parent,
            final Class childClass) {
            if (parent == null) {
                screen = new StartScreen();
            } else {
                StartScreenItem ssi = null;
                if (childClass == LogoStartScreenItem.class) {
                    ssi = new LogoStartScreenItem();
                } else if (childClass == ToolLinkStartScreenItem.class) {
                    ssi = new ToolLinkStartScreenItem();
                } else if (childClass == NewMetadataStartScreenItem.class) {
                    ssi = new NewMetadataStartScreenItem();
                } else if (childClass == StartScreenLinkItem.class) {
                    ssi = new StartScreenLinkItem();
                } else if (childClass == TextStartScreenItem.class) {
                    ssi = new TextStartScreenItem();
                } else {
                    ssi =
                        (StartScreenItem) ReflectionUtils
                            .getNewInstanceOfClass(childClass);
                }
                if (ssi != null) {
                    screen.getItems().add(
                        ssi);
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void moveChildrenUp(
            final Object parent,
            final Object child) {
            screen.getItems().moveItemUp(
                child);

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void moveChildrenDown(
            final Object parent,
            final Object child) {
            screen.getItems().moveItemDown(
                child);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeChildren(
            final Object parent,
            final Object child) {
            screen.getItems().removeItem(
                child);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMaximumChildrenCount(
            final Object parent) {
            if (parent == screen) {
                return -1;
            }
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getChild(
            final Object parent,
            final int index) {
            return screen.getItems().getItem(
                index);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getChildCount(
            final Object parent) {
            return screen.getItems().size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIndexOfChild(
            final Object parent,
            final Object child) {
            return screen.getItems().getIndexOfChild(
                child);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getRoot() {
            return screen;
        }

        /** The selected item. */
        private Object selectedItem;

        /** The selected row. */
        private int selectedRow;

        /**
         * {@inheritDoc}
         */
        @Override
        public void selectionChanged(
            final Object aSelectedItem,
            final int aSelectedRow) {
            modified = true;
            if (editor != null) {
                editor.commit();
                editor = null;
            }
            if (aSelectedItem != null) {
                editor = new ReflectionPropertySheet(
                    aSelectedItem,
                    "client",
                    getContext(),
                    "item",
                    aSelectedItem,
                    "actionListener",
                    this);
                mainPanes.setRightComponent(new JScrollPane(
                    editor));
            } else {
                mainPanes.setRightComponent(new JScrollPane(
                    new JLabel(
                        "nothing")));
            }
            this.selectedItem = aSelectedItem;
            this.selectedRow = aSelectedRow;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getClassTitle(
            final Class classObject) {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JButton getAddButton() {
            return new JButton(
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.addItem.png"));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JButton getRemoveButton() {
            return new JButton(
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.removeItem.png"));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JButton getUpButton() {
            return new JButton(
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.moveItemUp.png"));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JButton getDownButton() {
            return new JButton(
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.moveItemDown.png"));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TreeCellRenderer getCellRenderer() {
            return this;
        }

        /* == SimpleSingleDocumentManagedUnit Implementation == */

        /**
         * {@inheritDoc}
         */
        @Override
        public void newFileCallback() {
            screen = new StartScreen();
            stb.rowChildrenChanged(0);
            modified = false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void saveFileCallback(
            final File file) {
            Reporting.logExpected(
                "Writing to %1$s",
                file.getAbsolutePath());
            final String encoded = convertSheetToString();
            Files.write(
                file,
                ByteBuffer.wrap(encoded.getBytes()));
            modified = false;
            setChanged();
            notifyObservers();
        }

        /**
         * Convert sheet to string.
         * @return the string
         */
        private String convertSheetToString() {
            if (editor != null) {
                editor.commit();
            }
            final CSXMLSerializationCodec encoder =
                new CSXMLSerializationCodec();
            encoder.setPrintClasses(false);
            final String encoded = encoder.encodeObject(screen);
            return encoded;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void saveAsFileCallback(
            final File file) {
            saveFileCallback(file);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void loadFileCallback(
            final File file) {
            try {
                final ByteBuffer encoded = Files.read(file);
                final CSXMLSerializationCodec decoder =
                    new CSXMLSerializationCodec();
                decoder.includeClasses(StartScreenTool.getStartScreenClasses());
                screen = (StartScreen) decoder.decodeObject(new String(
                    encoded.array()));
                modified = false;
                stb.rowChildrenChanged(0);
                setChanged();
                notifyObservers();
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
                if (screen == null) {
                    screen = new StartScreen();
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void closeFileCallback(
            final File file) {
            /* nothing */
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getSaveFileConfirmation() {
            return JOptionPaneUtils
                .getYesNoConfirmation("Do you want to save the screen?");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getCancellableSaveFileConirmation(
            final File file) {
            switch (JOptionPaneUtils.getYesNoCancelConfirmation(
                "Save",
                "Do you want to save the screen?")) {
            case JOptionPane.YES_OPTION:
                return 1;
            case JOptionPane.NO_OPTION:
                return 0;
            default:
                return -1;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public File getNewSaveFileCallback() {
            return JFileChooserUtils.getSingleSaveFileWithExtension(
                "Start screen description file (xml)",
                "xml");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getReplaceFileCallback(
            final File file) {
            return JOptionPaneUtils.getYesNoConfirmation("File Already exists."
                + " Do you want to replace the file?");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public File getNewOpenFileCallback() {
            return JFileChooserUtils.getSingleOpenFileWithExtension(
                "Start screen description file (xml)",
                "xml");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasModified() {
            return modified;
        }

        /* == TreeCellRenderer Implementation == */
        /** The label. */
        private JLabel label;

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
            if (label == null) {
                label = new JLabel();
            }
            if (label != null) {
                final StringBuilder sb = new StringBuilder();

                if (value != null) {
                    sb.append(value.toString());
                } else {
                    sb.append("");
                }

                if (selected) {
                    label.setBackground(UIManager
                        .getColor("Tree.selectionBackground"));
                } else {
                    label.setBackground(UIManager.getColor("Tree.background"));
                }
                label.setText(sb.toString());
            }
            return label;
        }

        /* == ActionListener Implementation == */
        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(
            final ActionEvent e) {
            selectionChanged(
                selectedItem,
                selectedRow);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean fileExists(
            final File file) {
            return file.exists();
        }
    }

}
