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
package xmet.tools.metadata.editor.views.scv.designer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

import n.io.bin.Files;
import n.java.ReflectionUtils;
import n.reporting.Reporting;
import n.ui.GetTreeCellRendererParameters;
import n.ui.SwingUtils;
import n.ui.TreeCellRendererInterceptor;
import n.ui.patterns.callback.ClassMethodCallback;
import n.ui.patterns.propertySheet.BigStringPSE;
import n.ui.patterns.propertySheet.BooleanPSE;
import n.ui.patterns.propertySheet.IntegerPSE;
import n.ui.patterns.propertySheet.LongLabelPSE;
import n.ui.patterns.propertySheet.StringPSE;
import n.ui.patterns.sdm.SimpleSingleDocumentManagedUnit;
import n.ui.patterns.sdm.SimpleSingleDocumentManager;
import n.ui.patterns.stb.JSimpleTreeBuilder;
import n.ui.patterns.stb.JSimpleTreeBuilderModel;
import n.ui.patterns.stb.JSimpleTreeBuilderModelConvertable;
import n.ui.patterns.stb.JSimpleTreeBuilderModelDragable;
import n.ui.patterns.stb.JSimpleTreeBuilderModelExtended;
import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.model.Entity;
import xmet.tools.DefaultTool;
import xmet.tools.DocumentEditingTI;
import xmet.tools.MultipageTI;
import xmet.tools.Tool;
import xmet.tools.ToolInstance;
import xmet.tools.ToolInstanceEvents;
import xmet.tools.TransparentToolException;
import xmet.tools.metadata.editor.views.scv.SemiCustomView;
import xmet.tools.metadata.editor.views.scv.model.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.model.Choices;
import xmet.tools.metadata.editor.views.scv.model.CompositeItem;
import xmet.tools.metadata.editor.views.scv.model.EditorType;
import xmet.tools.metadata.editor.views.scv.model.Group;
import xmet.tools.metadata.editor.views.scv.model.GroupSubitem;
import xmet.tools.metadata.editor.views.scv.model.Item;
import xmet.tools.metadata.editor.views.scv.model.LabeledGroup;
import xmet.tools.metadata.editor.views.scv.model.ModelItem;
import xmet.tools.metadata.editor.views.scv.model.ModelUtils;
import xmet.tools.metadata.editor.views.scv.model.Page;
import xmet.tools.metadata.editor.views.scv.model.PageSubitem;
import xmet.tools.metadata.editor.views.scv.model.Param;
import xmet.tools.metadata.editor.views.scv.model.ParentItem;
import xmet.tools.metadata.editor.views.scv.model.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.model.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.model.Sheet;
import xmet.ui.JXHeaderPSE;
import xmet.ui.ProfileSelectionDialog;
import xmet.ui.profileHelp.ProfileHelpContextSelectionPSE;
import xmet.ui.profileModel.ModelBrowserDialog;

/**
 * The main designer tool and instance.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"static-access",
"rawtypes",
"unchecked"
})
public class SemiCustomViewDesignerTool
    extends DefaultTool {

    /* == DefaultTool abstract class methods implementation == */
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
    public String getName() {
        return "scv.designer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolInstance invoke(
        final Map<String, Object> params)
        throws TransparentToolException {
        Profile profile = (Profile) params.get("profile");
        final String fileName = (String) params.get("fileName");
        if (profile == null) {
            profile =
                ProfileSelectionDialog.getSelectedProfile(getContext()
                    .getProfiles());
        }
        if (profile != null) {
            try {
                return new Instance(
                    profile,
                    profile.getModel(getContext()),
                    fileName);
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        } else {
            throw new TransparentToolException();
        }
        return null;
    }

    /* == Class Instance == */

    /**
     * The Tool Instance Class.
     */
    public class Instance
        extends Observable
        implements
        ToolInstance,
        SimpleSingleDocumentManagedUnit<File>,
        ActionListener,
        JSimpleTreeBuilderModel,
        JSimpleTreeBuilderModelExtended,
        JSimpleTreeBuilderModelDragable,
        JSimpleTreeBuilderModelConvertable,
        MultipageTI,
        DocumentEditingTI,
        TreeCellRendererInterceptor {

        /** The modified. */
        private boolean modified = true;

        /**
         * The profile.
         */
        private final Profile profile;

        /**
         * The model.
         */
        private final Entity model;

        /**
         * Instantiates a new instance.
         * @param aProfile the profile
         * @param aModel the model
         * @param fileName the file name
         * @throws Exception the exception
         */
        public Instance(
            final Profile aProfile,
            final Entity aModel,
            final String fileName)
            throws Exception {
            this.profile = aProfile;
            this.model = aModel;

            File varFile = null;
            if (fileName == null) {
                varFile = null;
            } else {
                varFile = new File(
                    fileName);
            }
            editorFileManager = new SimpleSingleDocumentManager<File>(
                varFile,
                this);
            editorFileManager.onLoad();
        }

        /** The cell renderer. */
        private HelperTreeBuilderCellRenderer cellRenderer;

        /** The display panel. */
        private JPanel panel;

        /**
         * The main toolbar.
         */
        private JToolBar mainToolbar;

        /** The model tree builder. */
        private JSimpleTreeBuilder modelTreeBuilder;

        /**
         * The sub split pane.
         */
        private JSplitPane subSplitPane;

        /**
         * The sheet.
         */
        private Sheet sheet;

        /**
         * The selected editor.
         */
        private CustomReflectivePropertySheet selectedEditor;

        /**
         * The selected type editor.
         */
        private CustomReflectivePropertySheet selectedTypeEditor;

        /**
         * The selected editor row.
         */
        private int selectedEditorRow;

        /** The file manager. */
        private final SimpleSingleDocumentManager<File> editorFileManager;

        {
            // CHECKSTYLE OFF: MagicNumber
            /* initialization code */
            cellRenderer = new HelperTreeBuilderCellRenderer(
                getContext());
            panel = SwingUtils.BorderLayouts.getNew();

            mainToolbar = new JToolBar(
                "Main Toolbar",
                SwingConstants.HORIZONTAL);
            final Object[] params = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "New Editor",
                getContext().getResources().getImageIconResourceResize(
                    "images/toolbar.common.newDocument.png",
                    32,
                    32),
                new ClassMethodCallback(
                    this,
                    "buttonNew",
                    params)));
            final Object[] params1 = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Open Editor",
                getContext().getResources().getImageIconResourceResize(
                    "images/toolbar.common.openDocument.png",
                    32,
                    32),
                new ClassMethodCallback(
                    this,
                    "buttonOpen",
                    params1)));
            final Object[] params2 = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Save Editor",
                getContext().getResources().getImageIconResourceResize(
                    "images/toolbar.common.saveDocument.png",
                    32,
                    32),
                new ClassMethodCallback(
                    this,
                    "buttonSave",
                    params2)));
            final Object[] params3 = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Save Editor As",
                getContext().getResources().getImageIconResourceResize(
                    "images/toolbar.common.saveDocumentAs.png",
                    32,
                    32),
                new ClassMethodCallback(
                    this,
                    "buttonSaveAs",
                    params3)));
            final Object[] params4 = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Close Editor",
                getContext().getResources().getImageIconResourceResize(
                    "images/toolbar.common.closeDocument.png",
                    32,
                    32),
                new ClassMethodCallback(
                    this,
                    "buttonClose",
                    params4)));
            mainToolbar.addSeparator();
            final Object[] params5 = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Preview Editor",
                getContext().getResources().getImageIconResourceResize(
                    "images/toolbar.common.previewDocument.png",
                    32,
                    32),
                new ClassMethodCallback(
                    this,
                    "buttonPreview",
                    params5)));
            final Object[] params6 = {};
            mainToolbar.add(SwingUtils.BUTTON.getNewV(
                "Auto",
                getContext().getResources().getImageIconResourceResize(
                    "images/toolbar.common.codeGen.png",
                    32,
                    32),
                new ClassMethodCallback(
                    this,
                    "buttonAuto",
                    params6)));

            modelTreeBuilder = new JSimpleTreeBuilder(
                this);
            modelTreeBuilder.setButtonPanelPosition(JSimpleTreeBuilder.BOTTOM);

            subSplitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT);
            subSplitPane.setTopComponent(new JLabel(
                "Item specific editor"));
            subSplitPane.setBottomComponent(new JLabel(
                "ItemType specific editor"));
            subSplitPane.setResizeWeight(0.5);

            final JSplitPane mainSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT);

            mainSplitPane.setLeftComponent(modelTreeBuilder);
            mainSplitPane.setRightComponent(subSplitPane);
            mainSplitPane.setResizeWeight(0.7);

            panel.add(
                mainToolbar,
                BorderLayout.NORTH);
            panel.add(
                mainSplitPane,
                BorderLayout.CENTER);

            // CHECKSTYLE ON: MagicNumber
        }

        /* == Button callback method implementations == */

        /**
         * Button new.
         * @throws Exception the exception
         */
        public void buttonNew()
            throws Exception {
            commitEditorChanges();
            editorFileManager.onNewFile();
            modified = true;
        }

        /**
         * Button open.
         * @throws Exception the exception
         */
        public void buttonOpen()
            throws Exception {
            commitEditorChanges();
            editorFileManager.onOpenFile();
        }

        /**
         * Button save.
         * @throws Exception the exception
         */
        public void buttonSave()
            throws Exception {
            commitEditorChanges();
            modified = true;
            editorFileManager.onSaveFile();
        }

        /**
         * Button save as.
         * @throws Exception the exception
         */
        public void buttonSaveAs()
            throws Exception {
            commitEditorChanges();
            modified = true;
            editorFileManager.onSaveFileAs();
        }

        /**
         * Button close.
         * @throws Exception the exception
         */
        public void buttonClose()
            throws Exception {
            commitEditorChanges();
            editorFileManager.onCloseFile(false);
        }

        /**
         * Button preview.
         */
        public void buttonPreview() {
            commitEditorChanges();
            final File tempFile = Files.createTempFile(
                "xmet_",
                "."
                    + SemiCustomView.FILENAME_EXTENSION);
            saveFileCallback(tempFile);
            getContext().getTools().invokeToolByName(
                "metadata.editor",
                "profile",
                profile,
                "editorSheetFile",
                tempFile);
        }

        /**
         * The Auto Button.
         */
        public void buttonAuto() {
            final String action = (String) SwingUtils.OPTIONPANE.getUserChoice(
                "Please select an action",
                new Object[] {
                "01. Generate HTML Preview XSL",
                "02. Fix Repeated Children Xpath References",
                "03. Reorder Pages",
                "04. Sort Pages By Order",
                "05. Unorder Pages"
                });
            if (action != null) {
                commitEditorChanges();
                if (action.startsWith("01.")) {
                    buttonGeneratePreviewXSL();
                } else if (action.startsWith("02.")) {
                    buttonFixRepeatedChildrenXpaths();
                } else if (action.startsWith("03.")) {
                    buttonReorderPages(sheet);
                } else if (action.startsWith("04.")) {
                    buttonSortByOrder(sheet);
                } else if (action.startsWith("05.")) {
                    buttonUnorderPages(sheet);
                }
                produceNewEditorPanels(
                    modelTreeBuilder.getSelectedItem(),
                    modelTreeBuilder.getSelectedRow());
            }
        }

        /**
         * Button reorder pages.
         * @param aSheet the sheet
         */
        private void buttonReorderPages(
            final ParentItem aSheet) {
            // CHECKSTYLE OFF: MagicNumber
            int i = 1;
            for (final Object o : aSheet.getItems()) {
                if (o instanceof Page) {
                    ((Page) o).setOrder(i * 5);
                    i++;
                }
                if (o instanceof ParentItem) {
                    buttonReorderPages((ParentItem) o);
                }
            }
            // CHECKSTYLE ON: MagicNumber
        }

        /**
         * Button unorder pages.
         * @param aSheet the sheet
         */
        private void buttonUnorderPages(
            final ParentItem aSheet) {
            for (final Object o : aSheet.getItems()) {
                if (o instanceof Page) {
                    ((Page) o).setOrder(Page.DEFAULT_PAGE_ORDER);
                }
                if (o instanceof ParentItem) {
                    buttonUnorderPages((ParentItem) o);
                }
            }
        }

        /**
         * Button sort by order.
         * @param aSheet the sheet
         */
        private void buttonSortByOrder(
            final ParentItem aSheet) {
            ModelUtils.sortSheetPages(aSheet);
            modelTreeBuilder.rowChildrenChanged(0);
        }

        /**
         * Button fix repeated children xpaths.
         */
        public void buttonFixRepeatedChildrenXpaths() {
            new XpathFixUtil(
                sheet);
        }

        /**
         * Button generate preview xsl.
         */
        public void buttonGeneratePreviewXSL() {
            final XSLTemplateBuilderUtil xt = new XSLTemplateBuilderUtil();
            final String output = xt.visit(
                sheet,
                profile);
            /* Reporting.log(String.format("%1$s",output)); */
            final File file =
                SwingUtils.FILECHOOSER.getSingleSaveFileWithExtension(
                    "XSLT Stylesheets",
                    "xsl");
            if (file != null) {
                Files.write(
                    file,
                    ByteBuffer.wrap(output.getBytes()));
            }
        }

        /**
         * Commit editor changes.
         */
        void commitEditorChanges() {
            /* if (selectedEditor != null) { */
            /* modelTreeBuilder.updateSelectedRow(modelTreeBuilder */
            // .getSelectedRow());
            selectionChanged(
                modelTreeBuilder.getSelectedItem(),
                modelTreeBuilder.getSelectedRow());
            /* modified = true; */
            /* selectedEditor.commit(); */
            /* selectedTypeEditor.commit(); */
            /* tree.rowChanged(selectedEditorRow); */
            /* rebuildEntityHighlightTable(sheet); */
            /* tree. */
            // }
        }

        /**
         * Saves and updates editor panels.
         * @param selectedItem the selected item
         * @param selectedRow the selected row
         */
        void updateEditorPanels(
            final Object selectedItem,
            final int selectedRow) {
            saveExistingEditorPanels();
            produceNewEditorPanels(
                selectedItem,
                selectedRow);
        }

        /**
         * Produce new editor panels.
         * @param selectedItem the selected item
         * @param selectedRow the selected row
         */
        // CHECKSTYLE OFF: MethodLength
        private void produceNewEditorPanels(
            final Object selectedItem,
            final int selectedRow) {
            /* Reporting.log("Making New"); */
            if (selectedItem != null) {
                selectedEditorRow = selectedRow;

                final ArrayList params = new ArrayList();
                params.add("profile");
                params.add(profile);
                params.add("item");
                params.add(selectedItem);
                params.add("client");
                params.add(getContext());
                params.add("context");
                params.add(getContext());
                params.add("model");
                params.add(model);
                params.add("modelBorwserCellRenderInterceptor");
                params.add(this);

                if (selectedItem.getClass() == Sheet.class) {
                    final Sheet aSheet = (Sheet) selectedItem;
                    selectedEditor = getSheetEditor(
                        params,
                        aSheet);
                    updateItemTypeEditor(null);
                } else if (selectedItem.getClass() == Page.class) {
                    final Page page = (Page) selectedItem;
                    selectedEditor = getPageEditor(
                        params,
                        page);
                    updateItemTypeEditor(null);
                } else if (selectedItem.getClass() == RepeatedPage.class) {
                    final RepeatedPage page = (RepeatedPage) selectedItem;
                    selectedEditor = getRepeatedPageEditor(
                        params,
                        page);
                    updateItemTypeEditor(null);
                } else if (selectedItem.getClass() == LabeledGroup.class) {
                    final LabeledGroup group = (LabeledGroup) selectedItem;
                    selectedEditor = getLabeledGroupEditor(
                        params,
                        group);
                    updateItemTypeEditor(null);
                } else if (selectedItem.getClass() == RepeatedGroup.class) {
                    final RepeatedGroup group = (RepeatedGroup) selectedItem;
                    selectedEditor = getRepeatedGroupEditor(
                        params,
                        group);
                    updateItemTypeEditor(null);
                } else if (selectedItem.getClass() == Choices.class) {
                    selectedEditor = getNothingToEditEditor();
                    updateItemTypeEditor(null);
                } else if (selectedItem.getClass() == ChoiceItem.class) {
                    final ChoiceItem item = (ChoiceItem) selectedItem;
                    selectedEditor = getChoiceItemEditor(
                        params,
                        item);
                    updateItemTypeEditor(null);
                } else if (selectedItem.getClass() == Group.class) {
                    final Group group = (Group) selectedItem;
                    selectedEditor = new CustomReflectivePropertySheet(
                        new CustomReflectivePropertySheetItem[] {
                        new CustomReflectivePropertySheetItem(
                            "Group",
                            "* implies mandatory",
                            JXHeaderPSE.class,
                            null,
                            null),
                        new CustomReflectivePropertySheetItem(
                            "Name:",
                            null,
                            StringPSE.class,
                            group,
                            "name"),
                        new CustomReflectivePropertySheetItem(
                            "Visible:",
                            null,
                            BooleanPSE.class,
                            group,
                            "visible"),
                        },
                        params.toArray());
                    updateItemTypeEditor(null);
                } else if ((selectedItem.getClass() == Item.class)
                    || (selectedItem.getClass() == CompositeItem.class)) {
                    final Item item = (Item) selectedItem;
                    selectedEditor = getItemEditor(
                        params,
                        item,
                        selectedItem,
                        selectedRow);
                    updateItemTypeEditor(item);
                } else {
                    selectedEditor = getNothingToEditEditor();
                    updateItemTypeEditor(null);
                }

                subSplitPane.setTopComponent(selectedEditor);
            }
        }

        /**
         * Gets the item editor.
         * @param params the params
         * @param item the item
         * @param selectedItem the selected item
         * @param selectedRow the selected row
         * @return the item editor
         */
        private CustomReflectivePropertySheet getItemEditor(
            final ArrayList params,
            final Item item,
            final Object selectedItem,
            final int selectedRow) {

            params.add("callbackMethodName");
            params.add("updateEditorPanels");
            params.add("callbackMethodObject");
            params.add(this);
            params.add("callbackMethodParams");
            params.add(new Object[] {
            selectedItem,
            selectedRow
            });
            params.add("choices");
            params.add(EditorType.values());
            params.add("selectionMask");
            /* spatial encoding items need their special elements */
            if (item.getType() == EditorType.SpatialExtent) {
                params.add(ModelBrowserDialog.SELECT_ELEMENT_DECLARATION);
                params.add("nameSelectionMask");
                params.add(profile.getSpatialCodec(
                    getContext()).getSupportedNodeNames());
            } else if (item.getType() == EditorType.ContactInfoEditor) {
                params.add(ModelBrowserDialog.SELECT_ELEMENT_DECLARATION);
                params.add("nameSelectionMask");
                params.add(profile.getContactsCodec(
                    getContext()).getSupportedNodeNames());
            } else if (item.getType() == EditorType.KeywordsListEditor) {
                params.add(ModelBrowserDialog.SELECT_ELEMENT_DECLARATION);
                params.add("nameSelectionMask");
                params.add(profile.getKeywordsListCodec(
                    getContext()).getSupportedNodeNames());
            } else {
                final int itemMask =
                    ModelBrowserDialog.SELECT_SETABLE_ELEMENT_DECLARATION
                        | ModelBrowserDialog.SELECT_ATTRIBUTE;
                if (selectedItem.getClass() == CompositeItem.class) {
                    final int compositeItemMask = itemMask
                        | ModelBrowserDialog.SELECT_REPEATED;
                    params.add(compositeItemMask);
                } else {
                    params.add(itemMask);
                }
                params.add("nameSelectionMask");
                params.add(null);
            }

            if (item.getType() == EditorType.Unspecified) {
                if (item.getParams() != null) {
                    item.getParams().clear();
                    /* item.visible =false; */
                }
            }
            if (item.getClass() == Item.class) {
                return new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Item",
                        "* implies mandatory",
                        JXHeaderPSE.class,
                        null,
                        null),
                    new CustomReflectivePropertySheetItem(
                        "Label:",
                        null,
                        StringPSE.class,
                        item,
                        "title"),
                    new CustomReflectivePropertySheetItem(
                        "Description:",
                        null,
                        StringPSE.class,
                        item,
                        "description"),
                    new CustomReflectivePropertySheetItem(
                        "Hover:",
                        null,
                        StringPSE.class,
                        item,
                        "hover"),
                    new CustomReflectivePropertySheetItem(
                        "Type:",
                        null,
                        ItemTypePSE.class,
                        item,
                        "type",
                        "callbackMethodName",
                        "callbackMethodObject",
                        "callbackMethodParams",
                        "choices"),
                    new CustomReflectivePropertySheetItem(
                        "Visible:*",
                        null,
                        BooleanPSE.class,
                        item,
                        "visible"),
                    new CustomReflectivePropertySheetItem(
                        "Default",
                        null,
                        DefaultValueEditorPSE.class,
                        item,
                        "defaultValue"),
                    new CustomReflectivePropertySheetItem(
                        "Xpath:",
                        null,
                        XPathEditorPSE.class,
                        item,
                        "xpath",
                        "model",
                        "selectionMask",
                        "nameSelectionMask",
                        "modelBorwserCellRenderInterceptor"),
                    new CustomReflectivePropertySheetItem(
                        "Name:",
                        null,
                        StringPSE.class,
                        item,
                        "name"),
                    new CustomReflectivePropertySheetItem(
                        "onDataLoad:",
                        null,
                        CodeEditorPSE.class,
                        item,
                        "onDataLoad",
                        "model",
                        "context"),
                    new CustomReflectivePropertySheetItem(
                        "onDataChange:",
                        null,
                        CodeEditorPSE.class,
                        item,
                        "onDataChange",
                        "model",
                        "context"),
                    new CustomReflectivePropertySheetItem(
                        "Help Context:",
                        null,
                        ProfileHelpContextSelectionPSE.class,
                        item,
                        "helpContextID",
                        "client",
                        "profile"),
                    new CustomReflectivePropertySheetItem(
                        "Column Span:",
                        "Between 1 being a single "
                            + " line and 20 being spanning"
                            + " the whole page",
                        IntegerPSE.class,
                        item,
                        "columnSpan"),
                    new CustomReflectivePropertySheetItem(
                        "Validation:",
                        null,
                        ValidationInformationPSE.class,
                        item,
                        "validation"),

                    },
                    params.toArray());
            } else {
                return new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Composite Item",
                        "* implies mandatory",
                        JXHeaderPSE.class,
                        null,
                        null),
                    new CustomReflectivePropertySheetItem(
                        "Label:",
                        null,
                        StringPSE.class,
                        item,
                        "title"),
                    new CustomReflectivePropertySheetItem(
                        "Description:",
                        null,
                        StringPSE.class,
                        item,
                        "description"),
                    new CustomReflectivePropertySheetItem(
                        "Hover:",
                        null,
                        StringPSE.class,
                        item,
                        "hover"),
                    new CustomReflectivePropertySheetItem(
                        "Type:",
                        null,
                        ItemTypePSE.class,
                        item,
                        "type",
                        "callbackMethodName",
                        "callbackMethodObject",
                        "callbackMethodParams",
                        "choices"),
                    new CustomReflectivePropertySheetItem(
                        "Visible:*",
                        null,
                        BooleanPSE.class,
                        item,
                        "visible"),
                    new CustomReflectivePropertySheetItem(
                        "Default",
                        null,
                        DefaultValueEditorPSE.class,
                        item,
                        "defaultValue"),
                    new CustomReflectivePropertySheetItem(
                        "Name:",
                        null,
                        StringPSE.class,
                        item,
                        "name"),
                    new CustomReflectivePropertySheetItem(
                        "onDataLoad:",
                        null,
                        CodeEditorPSE.class,
                        item,
                        "onDataLoad",
                        "model",
                        "context"),
                    new CustomReflectivePropertySheetItem(
                        "onDataChange:",
                        null,
                        CodeEditorPSE.class,
                        item,
                        "onDataChange",
                        "model",
                        "context"),
                    new CustomReflectivePropertySheetItem(
                        "Help Context:",
                        null,
                        ProfileHelpContextSelectionPSE.class,
                        item,
                        "helpContextID",
                        "client",
                        "profile"),
                    new CustomReflectivePropertySheetItem(
                        "Column Span:",
                        "Between 1 being a single line"
                            + " and 20 being spanning "
                            + "the whole page",
                        IntegerPSE.class,
                        item,
                        "columnSpan"),
                    new CustomReflectivePropertySheetItem(
                        "Base:",
                        null,
                        XPathEditorPSE.class,
                        item,
                        "base",
                        "model",
                        "selectionMask",
                        "nameSelectionMask",
                        "modelBorwserCellRenderInterceptor"),
                    new CustomReflectivePropertySheetItem(
                        "Relative:",
                        null,
                        XPathEditorPSE.class,
                        item,
                        "relative",
                        "model",
                        "selectionMask",
                        "nameSelectionMask",
                        "modelBorwserCellRenderInterceptor"),
                    new CustomReflectivePropertySheetItem(
                        "Validation:",
                        null,
                        ValidationInformationPSE.class,
                        item,
                        "validation"),
                    },
                    params.toArray());
            }
        }

        /**
         * Gets the choice item editor.
         * @param params the params
         * @param item the item
         * @return the choice item editor
         */
        private CustomReflectivePropertySheet getChoiceItemEditor(
            final ArrayList params,
            final ChoiceItem item) {
            params.add("selectionMask");
            params.add(ModelBrowserDialog.SELECT_ELEMENT_DECLARATION);
            return new CustomReflectivePropertySheet(
                new CustomReflectivePropertySheetItem[] {
                new CustomReflectivePropertySheetItem(
                    "Choice",
                    "* implies mandatory",
                    JXHeaderPSE.class,
                    null,
                    null),
                new CustomReflectivePropertySheetItem(
                    "Label:",
                    null,
                    StringPSE.class,
                    item,
                    "label"),
                new CustomReflectivePropertySheetItem(
                    "Test xpath:**",
                    null,
                    XPathEditorPSE.class,
                    item,
                    "testXpath",
                    "model",
                    "selectionMask",
                    "modelBorwserCellRenderInterceptor"),
                },
                params.toArray());
        }

        /**
         * Gets the nothing to edit editor.
         * @return the nothing to edit editor
         */
        private CustomReflectivePropertySheet getNothingToEditEditor() {
            return new CustomReflectivePropertySheet(
                new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        null,
                        "Nothing to Edit",
                        LongLabelPSE.class,
                        null,
                        null)
                },
                null);
        }

        /**
         * Gets the repeated group editor.
         * @param params the params
         * @param group the group
         * @return the repeated group editor
         */
        private CustomReflectivePropertySheet getRepeatedGroupEditor(
            final ArrayList params,
            final RepeatedGroup group) {
            params.add("selectionMask");
            params.add(ModelBrowserDialog.SELECT_REPEATED);
            return new CustomReflectivePropertySheet(
                new CustomReflectivePropertySheetItem[] {
                new CustomReflectivePropertySheetItem(
                    "Repeated Group",
                    "* implies mandatory",
                    JXHeaderPSE.class,
                    null,
                    null),
                new CustomReflectivePropertySheetItem(
                    "Label:",
                    null,
                    StringPSE.class,
                    group,
                    "label"),
                new CustomReflectivePropertySheetItem(
                    "Xpath:",
                    null,
                    XPathEditorPSE.class,
                    group,
                    "base",
                    "selectionMask",
                    "model",
                    "modelBorwserCellRenderInterceptor"),
                new CustomReflectivePropertySheetItem(
                    "Compact Display:",
                    null,
                    BooleanPSE.class,
                    group,
                    "compact"),
                new CustomReflectivePropertySheetItem(
                    "Group",
                    "* implies mandatory",
                    JXHeaderPSE.class,
                    null,
                    null),
                new CustomReflectivePropertySheetItem(
                    "Name:",
                    null,
                    StringPSE.class,
                    group,
                    "name"),
                new CustomReflectivePropertySheetItem(
                    "Visible:",
                    null,
                    BooleanPSE.class,
                    group,
                    "visible")
                },
                params.toArray());
        }

        /**
         * Gets the labeled group editor.
         * @param params the params
         * @param group the group
         * @return the labeled group editor
         */
        private CustomReflectivePropertySheet getLabeledGroupEditor(
            final ArrayList params,
            final LabeledGroup group) {
            return new CustomReflectivePropertySheet(
                new CustomReflectivePropertySheetItem[] {
                new CustomReflectivePropertySheetItem(
                    "Labeled Group",
                    "* implies mandatory",
                    JXHeaderPSE.class,
                    null,
                    null),
                new CustomReflectivePropertySheetItem(
                    "Label:",
                    null,
                    StringPSE.class,
                    group,
                    "label"),
                new CustomReflectivePropertySheetItem(
                    "Group",
                    "* implies mandatory",
                    JXHeaderPSE.class,
                    null,
                    null),
                new CustomReflectivePropertySheetItem(
                    "Name:",
                    null,
                    StringPSE.class,
                    group,
                    "name"),
                new CustomReflectivePropertySheetItem(
                    "Visible:",
                    null,
                    BooleanPSE.class,
                    group,
                    "visible")
                },
                params.toArray());
        }

        /**
         * Gets the repeated page editor.
         * @param params the params
         * @param page the page
         * @return the repeated page editor
         */
        private CustomReflectivePropertySheet getRepeatedPageEditor(
            final ArrayList params,
            final RepeatedPage page) {
            params.add("selectionMask");
            params.add(ModelBrowserDialog.SELECT_REPEATED);
            return new CustomReflectivePropertySheet(
                new CustomReflectivePropertySheetItem[] {
                new CustomReflectivePropertySheetItem(
                    "Repeated Page",
                    "* implies mandatory",
                    JXHeaderPSE.class,
                    null,
                    null),
                new CustomReflectivePropertySheetItem(
                    "Xpath:",
                    null,
                    XPathEditorPSE.class,
                    page,
                    "base",
                    "selectionMask",
                    "model",
                    "modelBorwserCellRenderInterceptor"),
                new CustomReflectivePropertySheetItem(
                    "Page",
                    "* implies mandatory",
                    JXHeaderPSE.class,
                    null,
                    null),
                new CustomReflectivePropertySheetItem(
                    "Label:",
                    null,
                    StringPSE.class,
                    page,
                    "title"),
                new CustomReflectivePropertySheetItem(
                    "Name:",
                    null,
                    StringPSE.class,
                    page,
                    "name"),
                new CustomReflectivePropertySheetItem(
                    "Order:*",
                    null,
                    IntegerPSE.class,
                    page,
                    "order"),
                new CustomReflectivePropertySheetItem(
                    "Visible:*",
                    null,
                    BooleanPSE.class,
                    page,
                    "visible"),
                new CustomReflectivePropertySheetItem(
                    "onInitialize:",
                    null,
                    CodeEditorPSE.class,
                    page,
                    "onInitialize",
                    "model",
                    "context"),
                new CustomReflectivePropertySheetItem(
                    "onValidation:",
                    null,
                    CodeEditorPSE.class,
                    page,
                    "onValidation",
                    "model",
                    "context"),
                },
                params.toArray());
        }

        /**
         * Gets the page editor.
         * @param params the params
         * @param page the page
         * @return the page editor
         */
        private CustomReflectivePropertySheet getPageEditor(
            final ArrayList params,
            final Page page) {
            return new CustomReflectivePropertySheet(
                new CustomReflectivePropertySheetItem[] {
                new CustomReflectivePropertySheetItem(
                    "Page",
                    "* implies mandatory",
                    JXHeaderPSE.class,
                    null,
                    null),
                new CustomReflectivePropertySheetItem(
                    "Label:",
                    null,
                    StringPSE.class,
                    page,
                    "title"),
                new CustomReflectivePropertySheetItem(
                    "Name:",
                    null,
                    StringPSE.class,
                    page,
                    "name"),
                new CustomReflectivePropertySheetItem(
                    "Order:*",
                    null,
                    IntegerPSE.class,
                    page,
                    "order"),
                new CustomReflectivePropertySheetItem(
                    "Visible:*",
                    null,
                    BooleanPSE.class,
                    page,
                    "visible"),
                new CustomReflectivePropertySheetItem(
                    "onInitialize:",
                    null,
                    CodeEditorPSE.class,
                    page,
                    "onInitialize",
                    "model",
                    "context"),
                new CustomReflectivePropertySheetItem(
                    "onValidation:",
                    null,
                    CodeEditorPSE.class,
                    page,
                    "onValidation",
                    "model",
                    "context"),
                },
                params.toArray());
        }

        /**
         * Gets the sheet editor.
         * @param params the params
         * @param aSheet the sheet
         * @return the sheet editor
         */
        private CustomReflectivePropertySheet getSheetEditor(
            final ArrayList params,
            final Sheet aSheet) {
            return new CustomReflectivePropertySheet(
                new CustomReflectivePropertySheetItem[] {
                new CustomReflectivePropertySheetItem(
                    "Sheet",
                    "* implies mandatory",
                    JXHeaderPSE.class,
                    null,
                    null),
                new CustomReflectivePropertySheetItem(
                    "Title:",
                    null,
                    StringPSE.class,
                    aSheet,
                    "title"),
                new CustomReflectivePropertySheetItem(
                    "Validation XSL:",
                    null,
                    BigStringPSE.class,
                    aSheet,
                    "validationXSL")
                },
                params.toArray());
        }

        // CHECKSTYLE ON: MethodLength

        /**
         * Save existing editor panels.
         */
        private void saveExistingEditorPanels() {
            /* Reporting.log("Commiting"); */
            modified = true;
            if (selectedEditor != null) {
                selectedEditor.commit();
                selectedTypeEditor.commit();
                modelTreeBuilder.rowChanged(selectedEditorRow);
                selectedEditor = null;
                selectedTypeEditor = null;
                selectedEditorRow = -1;
                rebuildEntityHighlightTable(sheet);
            }
        }

        /* CHECKSTYLE OFF: MethodLength */
        /**
         * Update item type editor.
         * @param item the item
         */
        public void updateItemTypeEditor(
            final Item item) {
            modified = true;
            if ((item == null)
                || (item.getType() == null)) {
                selectedTypeEditor = getNothingToEditEditor();
            } else {
                switch (item.getType()) {
                case MoveOverList:
                    if (item.getParams() == null) {
                        item.setParams(new ArrayList<Param>());
                    } else {
                        for (int i = 0; i < item.getParams().size(); i++) {
                            if (item.getParams().get(
                                i).getName().equals(
                                "Editable")) {
                                item.getParams().remove(
                                    i--);
                            }
                        }
                    }
                    selectedTypeEditor = new CustomReflectivePropertySheet(
                        new CustomReflectivePropertySheetItem[] {
                        new CustomReflectivePropertySheetItem(
                            "List",
                            "for value, enter the codelist"
                                + " url or its or custom list items"
                                + " seperated by \"|\" e.g. A|B|C|D",
                            JXHeaderPSE.class,
                            null,
                            null),
                        new CustomReflectivePropertySheetItem(
                            null,
                            null,
                            ListEditorPSE.class,
                            item,
                            "params"),
                        },
                        null);
                    break;
                case CheckedList:
                case DropDownList:
                    if (item.getParams() == null) {
                        item.setParams(new ArrayList<Param>());
                    }
                    selectedTypeEditor = new CustomReflectivePropertySheet(
                        new CustomReflectivePropertySheetItem[] {
                        new CustomReflectivePropertySheetItem(
                            "List",
                            "for value, enter the"
                                + " codelist url or its or custom"
                                + " list items seperated by"
                                + " \"|\" e.g. A|B|C|D",
                            JXHeaderPSE.class,
                            null,
                            null),
                        new CustomReflectivePropertySheetItem(
                            null,
                            null,
                            ListEditorPSE.class,
                            item,
                            "params"),
                        new CustomReflectivePropertySheetItem(
                            "Editable",
                            null,
                            ListEditableEditorPSE.class,
                            item,
                            "params"),
                        },
                        null);
                    break;
                case SpatialExtent:
                case CodelistCatalogList:
                    if (item.getParams() == null) {
                        item.setParams(new ArrayList<Param>());
                    }
                    selectedTypeEditor = new CustomReflectivePropertySheet(
                        new CustomReflectivePropertySheetItem[] {
                        new CustomReflectivePropertySheetItem(
                            "Codelist Catalog List",
                            "only enter a catalog url",
                            JXHeaderPSE.class,
                            null,
                            null),
                        new CustomReflectivePropertySheetItem(
                            "URL:",
                            null,
                            CodelistCatalogURLPSE.class,
                            item,
                            "params"),
                        },
                        null);
                    break;
                case KeywordsListEditor:
                    if (item.getParams() == null) {
                        item.setParams(new ArrayList<Param>());
                    }
                    selectedTypeEditor = new CustomReflectivePropertySheet(
                        new CustomReflectivePropertySheetItem[] {
                        new CustomReflectivePropertySheetItem(
                            "Keywords Codelist",
                            "only enter a catalog url",
                            JXHeaderPSE.class,
                            null,
                            null),
                        new CustomReflectivePropertySheetItem(
                            "Codelist URLs:",
                            null,
                            CodelistURLPSE.class,
                            item,
                            "params"),
                        },
                        null);
                    break;
                case Checkbox:
                    if (item.getParams() == null) {
                        item.setParams(new ArrayList<Param>());
                    }
                    selectedTypeEditor = new CustomReflectivePropertySheet(
                        new CustomReflectivePropertySheetItem[] {
                        new CustomReflectivePropertySheetItem(
                            "Checkbox",
                            "",
                            JXHeaderPSE.class,
                            null,
                            null),
                        new CustomReflectivePropertySheetItem(
                            "Value if checked:",
                            null,
                            CheckboxEditorPSE.class,
                            item,
                            "params")
                        },
                        null);
                    break;
                case DigitalContentSizeEditor:
                    selectedTypeEditor = new CustomReflectivePropertySheet(
                        new CustomReflectivePropertySheetItem[] {
                        new CustomReflectivePropertySheetItem(
                            "Digital Content Size Editor",
                            "",
                            JXHeaderPSE.class,
                            null,
                            null),
                        new CustomReflectivePropertySheetItem(
                            "Metric: ",
                            null,
                            DigitalContentSizeEditorMetricPSE.class,
                            item,
                            "params")
                        },
                        null);
                    break;
                case ContactInfoEditor:
                case DateTimePicker:
                default:
                    selectedTypeEditor = getNothingToEditEditor();
                    if (item.getParams() != null) {
                        item.getParams().clear();
                    }
                    break;
                }
            }
            subSplitPane.setBottomComponent(selectedTypeEditor);
        }

        /* CHECKSTYLE ON: MethodLength */

        /* == ToolInstance implementation == */

        /**
         * {@inheritDoc}
         */
        @Override
        public Tool getTool() {
            return SemiCustomViewDesignerTool.this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle() {
            String varString = null;
            if (editorFileManager.getFile() == null) {
                varString = "New Semi Custom View Editor Design";
            } else {
                varString = editorFileManager.getFile().getName();
            }
            return varString;
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
                return editorFileManager.onCloseFile(force);
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
                case ToolInstanceEvents.SAVE_DOCUMENT:
                    buttonSave();
                    break;
                case ToolInstanceEvents.SAVE_DOCUMENT_AS:
                    buttonSaveAs();
                    break;
                case ToolInstanceEvents.NEW_DOCUMENT:
                    buttonNew();
                    break;
                case ToolInstanceEvents.OPEN_DOCUMENT:
                    buttonOpen();
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

        /**
         * {@inheritDoc}
         */
        @Override
        public void newFileCallback() {
            sheet = new Sheet();
            modelTreeBuilder.rowChildrenChanged(0);
            rebuildEntityHighlightTable(sheet);
            modified = false;
            setChanged();
            notifyObservers();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void saveFileCallback(
            final File file) {
            if (sheet != null) {
                final ByteBuffer bytes =
                    ByteBuffer.wrap(ModelUtils.sheetToString(
                        sheet).getBytes());
                Files.write(
                    file,
                    bytes);
                modified = false;
            }
            setChanged();
            notifyObservers();
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
            final ByteBuffer contents = Files.read(file);
            sheet = ModelUtils.loadSheetFromContents(new String(
                contents.array()));
            modelTreeBuilder.rowChildrenChanged(0);
            rebuildEntityHighlightTable(sheet);
            modified = false;
            setChanged();
            notifyObservers();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void closeFileCallback(
            final File file) {
            sheet = new Sheet();
            modelTreeBuilder.rowChildrenChanged(0);
            rebuildEntityHighlightTable(sheet);
            modified = false;
            setChanged();
            notifyObservers();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getSaveFileConfirmation() {
            return SwingUtils.OPTIONPANE
                .getYesNoConfirmation("Do you want to save the editor?");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getCancellableSaveFileConirmation(
            final File file) {
            switch (SwingUtils.OPTIONPANE.getYesNoCancelConfirmation(
                "Save",
                "Do you want to save the editor?")) {
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
            return SwingUtils.FILECHOOSER.getSingleSaveFileWithExtension(
                "xMET Semi Custom View Files",
                SemiCustomView.FILENAME_EXTENSION);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getReplaceFileCallback(
            final File file) {
            return SwingUtils.OPTIONPANE
                .getYesNoConfirmation("File Already exists."
                    + " Do you want to replace the file?");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public File getNewOpenFileCallback() {
            return SwingUtils.FILECHOOSER.getSingleOpenFileWithExtension(
                "xMET Semi Custom View Files",
                SemiCustomView.FILENAME_EXTENSION);
        }

        /* == ActionListener interface == */
        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(
            final ActionEvent e) {
            commitEditorChanges();
        }

        /* == JTreeBuilderModelExtended == */
        /**
         * The top classes.
         */
        private final Class[] topClasses = new Class[] {
            Sheet.class
        };

        /**
         * The sheet classes.
         */
        private final Class[] sheetClasses = new Class[] {
        Page.class,
        RepeatedPage.class
        };

        /** The choices classes. */
        private Class[] choicesClasses;

        /** The page sub item classes. */
        private Class[] pageSubItemClasses;

        /** The group sub item classes. */
        private Class[] groupSubItemClasses;

        {
            choicesClasses = new Class[] {
                ChoiceItem.class
            };
            /* code snippet automatically fills pageSubItemClasses and */
            /* groupSubItemClasses based on what the classes in the model */
            /* implements */
            // if (pageSubItemClasses == null) {
            final ArrayList<Class> pageSubs = new ArrayList<Class>();
            final ArrayList<Class> groupSubs = new ArrayList<Class>();
            for (final Class c : ModelUtils.getScvModelClasses()) {
                final boolean pageSub = ReflectionUtils.doesImplementInterface(
                    c,
                    PageSubitem.class);
                final boolean groupSub =
                    ReflectionUtils.doesImplementInterface(
                        c,
                        GroupSubitem.class);
                if (pageSub) {
                    if (pageSubs.indexOf(c) == -1) {
                        pageSubs.add(c);
                    }
                }
                if (groupSub) {
                    if (groupSubs.indexOf(c) == -1) {
                        groupSubs.add(c);
                    }
                }
            }
            pageSubItemClasses = pageSubs.toArray(new Class[pageSubs.size()]);
            groupSubItemClasses =
                groupSubs.toArray(new Class[groupSubs.size()]);
            // }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class[] getAllowedChildrenClasses(
            final Object parent) {
            if (parent instanceof Sheet) {
                return sheetClasses.clone();
            } else if (parent instanceof Page) {
                return pageSubItemClasses.clone();
            } else if (parent instanceof Group) {
                return groupSubItemClasses.clone();
            } else if (parent instanceof Choices) {
                return choicesClasses.clone();
            } else if (parent instanceof ChoiceItem) {
                return groupSubItemClasses.clone();
            } else if (parent == null) {
                return topClasses.clone();
            } else {
                Reporting.logUnexpected(parent.toString());
                return new Class[0];
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addChildren(
            final Object parent,
            final Class childClass) {
            if (childClass != null) {
                Object child = null;
                if (childClass == Sheet.class) {
                    child = new Sheet();
                } else if (childClass == Group.class) {
                    child = new Group();
                } else if (childClass == RepeatedGroup.class) {
                    child = new RepeatedGroup();
                } else if (childClass == LabeledGroup.class) {
                    child = new LabeledGroup();
                } else if (childClass == Item.class) {
                    child = new Item();
                } else if (childClass == Page.class) {
                    child = new Page();
                } else if (childClass == RepeatedPage.class) {
                    child = new RepeatedPage();
                } else if (childClass == CompositeItem.class) {
                    child = new CompositeItem();
                } else if (childClass == ChoiceItem.class) {
                    child = new ChoiceItem();
                } else if (childClass == Choices.class) {
                    child = new Choices();
                } else {
                    child = ReflectionUtils.getNewInstanceOfClass(childClass);
                    Reporting.logUnexpected(childClass.toString());
                }

                if (child != null) {
                    if (parent instanceof ParentItem) {
                        ((ParentItem) parent).getItems().addItem(
                            child);
                    } else if (parent == null) {
                        sheet = new Sheet();
                    } else if (parent instanceof Choices) {
                        ((Choices) parent).getItems().add(
                            (ChoiceItem) child);
                    } else if (parent instanceof ChoiceItem) {
                        ((ChoiceItem) parent).setItem((GroupSubitem) child);
                    } else {
                        Reporting.logUnexpected(parent.toString());
                    }
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
            if (parent instanceof ParentItem) {
                ((ParentItem) parent).getItems().moveItemUp(
                    child);
            } else if (parent instanceof Choices) {
                ((Choices) parent).getItems().moveItemUp(
                    child);
            } else {
                Reporting.logUnexpected(
                    "%1$s",
                    parent);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void moveChildrenDown(
            final Object parent,
            final Object child) {
            if (parent instanceof ParentItem) {
                ((ParentItem) parent).getItems().moveItemDown(
                    child);
            } else if (parent instanceof Choices) {
                ((Choices) parent).getItems().moveItemDown(
                    child);
            } else {
                Reporting.logUnexpected(
                    "%1$s",
                    parent);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeChildren(
            final Object parent,
            final Object child) {
            if (parent instanceof ParentItem) {
                ((ParentItem) parent).getItems().removeItem(
                    child);
            } else if (parent == null) {
                sheet = null;
            } else if (parent instanceof Choices) {
                ((Choices) parent).getItems().removeItem(
                    child);
            } else if (parent instanceof ChoiceItem) {
                ((ChoiceItem) parent).setItem(null);
            } else {
                Reporting.logUnexpected(
                    "%1$s",
                    parent);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMaximumChildrenCount(
            final Object parent) {
            if (parent instanceof ChoiceItem) {
                return 1;
            }
            if ((parent instanceof ParentItem)
                || (parent instanceof Choices)) {
                return -1;
            } else {
                return 0;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getChild(
            final Object parent,
            final int index) {
            if (parent instanceof ParentItem) {
                return ((ParentItem) parent).getItems().getItem(
                    index);
            } else if (parent instanceof Choices) {
                return ((Choices) parent).getItems().get(
                    index);
            } else if (parent instanceof ChoiceItem) {
                if (index == 0) {
                    return ((ChoiceItem) parent).getItem();
                } else {
                    return null;
                }
            } else {
                Reporting.logUnexpected(
                    "%1$s",
                    parent);
                return null;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getChildCount(
            final Object parent) {
            if (parent instanceof ParentItem) {
                return ((ParentItem) parent).getItems().getItemsSize();
            } else if (parent instanceof Choices) {
                return ((Choices) parent).getItems().size();
            } else if (parent instanceof ChoiceItem) {
                if (((ChoiceItem) parent).getItem() == null) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 0;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIndexOfChild(
            final Object parent,
            final Object child) {
            if (parent instanceof ParentItem) {
                return ((ParentItem) parent).getItems().getIndexOfChild(
                    child);
            } else if (parent instanceof Choices) {
                return ((Choices) parent).getItems().indexOf(
                    child);
            } else if (parent instanceof ChoiceItem) {
                if (((ChoiceItem) parent).getItem() == child) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getRoot() {
            return sheet;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void selectionChanged(
            final Object selectedItem,
            final int selectedRow) {
            updateEditorPanels(
                selectedItem,
                selectedRow);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getClassTitle(
            final Class classObject) {
            if (classObject == Sheet.class) {
                return "Sheet";
            } else if (classObject == Group.class) {
                return "Group";
            } else if (classObject == RepeatedGroup.class) {
                return "RepeatedGroup";
            } else if (classObject == LabeledGroup.class) {
                return "Labeled Group";
            } else if (classObject == Item.class) {
                return "Item";
            }
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
            return cellRenderer;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasModified() {
            return modified;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean fileExists(
            final File file) {
            return file.exists();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public synchronized boolean hasChanged() {
            return true;
        };

        /* == TreeCellRendererInterceptor implementation == */
        /** The highlighted entities. */
        private final Map<Entity, Boolean> highlightedEntities =
            new HashMap<Entity, Boolean>();

        /** The true color. */
        private final Color trueColor = Color.RED.brighter();

        /** The false color. */
        private final Color falseColor = Color.GREEN.brighter().brighter();

        /**
         * Rebuild entity highlight table.
         * @param aSheet the sheet
         */
        void rebuildEntityHighlightTable(
            final Sheet aSheet) {
            final Map<String, Boolean> xpathsH = new HashMap<String, Boolean>();
            highlightedEntities.clear();
            final ArrayList<String> xpaths = ModelUtils.extractXPaths(aSheet);
            for (final String xp : xpaths) {
                final Boolean b = xpathsH.get(xp);
                xpathsH.put(
                    xp,
                    (b != null));
                if (b != null) {
                    Reporting.logExpected(
                        "Conflicting xpath %1$s",
                        xp);
                }
                /* else { */
                /* Reporting.log("%1$s", xp); */
                // }
            }
            for (final Map.Entry<String, Boolean> xpath : xpathsH.entrySet()) {
                final Entity e = xmet.profiles.model.ModelUtils.hardTraceXPath(
                    model,
                    xpath.getKey());
                if (e != null) {
                    highlightedEntities.put(
                        e,
                        xpathsH.get(xpath.getValue()));
                }
            }
        }

        /**
         * The renderer label.
         * @param component the component
         * @param params the params
         * @return the component
         */
        // JLabel rendererLabel = new JLabel();

        @Override
        public Component interceptGetTreeCellRenderer(
            final Component component,
            final GetTreeCellRendererParameters params) {
            final Boolean b = highlightedEntities.get(params.getValue());
            if (b != null) {
                Color varColor = null;
                if (b) {
                    varColor = trueColor;
                } else {
                    varColor = falseColor;
                }
                component.setBackground(varColor);
            } else {
                if (params.isSel()) {
                    component
                        .setBackground(cellRenderer.selectionBackgroundColor);
                } else {
                    component.setBackground(cellRenderer.backgroundColor);
                }
            }
            return component;
        }

        /* == JSimpleTreeBuilderModelDragable Implementation == */
        /**
         * {@inheritDoc}
         */
        @Override
        public void attachChild(
            final Object parent,
            final Object child,
            final int index) {
            Reporting.logExpected(
                "attachChild(%1$s, %2$s, %3$d)",
                parent,
                child,
                index);
            if (parent instanceof ParentItem) {
                ((ParentItem) parent).getItems().add(
                    index,
                    child);
            } else if (parent instanceof ChoiceItem) {
                ((ChoiceItem) parent).setItem((GroupSubitem) child);
            } else if (parent instanceof Choices) {
                ((Choices) parent).getItems().add(
                    index,
                    (ChoiceItem) child);
            } else {
                Reporting.logUnexpected(
                    "%1$s",
                    parent);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object cloneElement(
            final Object item) {
            Reporting.logExpected(
                "cloneElement(%1$s)",
                item);
            return ModelUtils.clone((ModelItem) item);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void detachChild(
            final Object parent,
            final Object child) {
            Reporting.logExpected(
                "detachChild(%1$s, %2$s)",
                parent,
                child);
            if (parent instanceof ParentItem) {
                ((ParentItem) parent).getItems().remove(
                    child);
            } else if (parent instanceof ChoiceItem) {
                ((ChoiceItem) parent).setItem(null);
            } else if (parent instanceof Choices) {
                ((Choices) parent).getItems().remove(
                    child);
            } else {
                Reporting.logUnexpected(
                    "%1$s",
                    parent);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCloneSupported() {
            return true;
        }

        /*
         * == JSimpleTreeBuilderModelConvertable Implementation ================
         */
        /**
         * {@inheritDoc}
         */
        @Override
        public Class[] getCompatibleTypes(
            final Class type,
            final Object context) {
            // if (type == Choices.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (type == ChoiceItem.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (type == CodeBlock.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (type == Group.class) {
            // // return new Class[] {
            // // LabeledGroup.class, RepeatedGroup.class, Group.class
            // // };
            // } else if (type == IfCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (type == Item.class) {
            //
            // } else if (type == LabeledGroup.class) {
            // // return new Class[] {
            // // LabeledGroup.class, RepeatedGroup.class, Group.class
            // // };
            // } else if (type == Page.class) {
            //
            // } else if (type == RepeatCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (type == RepeatedGroup.class) {
            // // return new Class[] {
            // // LabeledGroup.class, RepeatedGroup.class, Group.class
            // // };
            // } else if (type == RepeatedPage.class) {
            //
            // } else if (type == SetItemValueCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (type == SetItemVisibleCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (type == SetPathValueCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (type == Sheet.class) {
            //
            // } else if (type == CompositeItem.class) {
            //
            // } else if (type == GetPathValueCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else {
            // Reporting.unimplemented();
            // }
            return new Class[0];
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object convertObjectType(
            final Object object,
            final Class newType) {
            // if (object.getClass() == Choices.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (object.getClass() == ChoiceItem.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (object.getClass() == CodeBlock.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (object.getClass() == Group.class) {
            // // final Group group = (Group) object;
            // // if (newType == LabeledGroup.class) {
            // // final LabeledGroup newGroup = new LabeledGroup();
            // // Group.copyGroup(
            // // group, newGroup);
            // // return group;
            // // } else if (newType == RepeatedGroup.class) {
            // // final RepeatedGroup newGroup = new RepeatedGroup();
            // // Group.copyGroup(
            // // group, newGroup);
            // // return newGroup;
            // // } else if (newType == Group.class) {
            // // final Group newGroup = new Group();
            // // Group.copyGroup(
            // // group, newGroup);
            // // return newGroup;
            // // } else {
            // // Reporting.unimplemented();
            // // }
            // } else if (object.getClass() == IfCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (object.getClass() == Item.class) {
            //
            // } else if (object.getClass() == LabeledGroup.class) {
            // // final LabeledGroup group = (LabeledGroup) object;
            // // if (newType == LabeledGroup.class) {
            // // final LabeledGroup newGroup = new LabeledGroup();
            // // Group.copyGroup(
            // // group, newGroup);
            // // newGroup.label = group.getLabel();
            // // return group;
            // // } else if (newType == RepeatedGroup.class) {
            // // final RepeatedGroup newGroup = new RepeatedGroup();
            // // Group.copyGroup(
            // // group, newGroup);
            // // newGroup.label = group.getLabel();
            // // return newGroup;
            // // } else if (newType == Group.class) {
            // // final Group newGroup = new Group();
            // // Group.copyGroup(
            // // group, newGroup);
            // // return newGroup;
            // // } else {
            // // Reporting.unimplemented();
            // // }
            // } else if (object.getClass() == Page.class) {
            //
            // } else if (object.getClass() == RepeatCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (object.getClass() == RepeatedGroup.class) {
            // // final RepeatedGroup group = (RepeatedGroup) object;
            // // if (newType == LabeledGroup.class) {
            // // final LabeledGroup newGroup = new LabeledGroup();
            // // Group.copyGroup(
            // // group, newGroup);
            // // newGroup.label = group.getLabel();
            // // return group;
            // // } else if (newType == RepeatedGroup.class) {
            // // final RepeatedGroup newGroup = new RepeatedGroup();
            // // Group.copyGroup(
            // // group, newGroup);
            // // return newGroup;
            // // } else if (newType == Group.class) {
            // // final Group newGroup = new Group();
            // // Group.copyGroup(
            // // group, newGroup);
            // // return newGroup;
            // // } else {
            // // Reporting.unimplemented();
            // // }
            // } else if (object.getClass() == RepeatedPage.class) {
            //
            // } else if (object.getClass() == SetItemValueCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (object.getClass() == SetItemVisibleCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (object.getClass() == SetPathValueCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else if (object.getClass() == Sheet.class) {
            //
            // } else if (object.getClass() == CompositeItem.class) {
            //
            // } else if (object.getClass() == GetPathValueCode.class) {
            // /* dont want to be able to convert these into anything */
            // } else {
            // Reporting.unimplemented();
            // }
            // Reporting.unimplemented();
            return object;
        }

    }

    /**
     * Helper Tree Builder Cell Renderer.
     */
    static class HelperTreeBuilderCellRenderer
        implements
        TreeCellRenderer {

        /* == TreeCellRenderer implementation == */
        /** The sheet icon. */
        private static Icon sheetIcon;

        /** The page icon. */
        private static Icon pageIcon;

        /** The group icon. */
        private static Icon groupIcon;

        /** The repeated group icon. */
        private static Icon repeatedGroupIcon;

        /** The repeated page icon. */
        private static Icon repeatedPageIcon;

        /** The labeled group icon. */
        private static Icon labeledGroupIcon;

        /** The item icon. */
        private static Icon itemIcon;

        /** The composite item icon. */
        private static Icon compositeItemIcon;

        /** The choice icon. */
        private static Icon choiceIcon;

        /** The choice item icon. */
        private static Icon choiceItemIcon;

        /** The renderer label. */
        private static JLabel rendererLabel;

        /** The background color. */
        private static Color backgroundColor;

        /** The selection background color. */
        private static Color selectionBackgroundColor;

        /**
         * Instantiates a new helper tree builder cell renderer.
         * @param client the client
         */
        public HelperTreeBuilderCellRenderer(
            final ClientContext client) {
            // CHECKSTYLE OFF: MagicNumber
            if (sheetIcon == null) {
                sheetIcon = client.getResources().getImageIconResourceResize(
                    "images/scv.designer.icon.sheet.png",
                    32,
                    32);
                pageIcon = client.getResources().getImageIconResourceResize(
                    "images/scv.designer.icon.page.png",
                    32,
                    32);
                groupIcon = client.getResources().getImageIconResourceResize(
                    "images/scv.designer.icon.group.png",
                    32,
                    32);
                repeatedGroupIcon =
                    client.getResources().getImageIconResourceResize(
                        "images/scv.designer.icon.repeatedGroup.png",
                        32,
                        32);
                repeatedPageIcon =
                    client.getResources().getImageIconResourceResize(
                        "images/scv.designer.icon.repeatedPage.png",
                        32,
                        32);
                labeledGroupIcon =
                    client.getResources().getImageIconResourceResize(
                        "images/scv.designer.icon.labeledGroup.png",
                        32,
                        32);
                itemIcon = client.getResources().getImageIconResourceResize(
                    "images/scv.designer.icon.item.png",
                    32,
                    32);
                compositeItemIcon =
                    client.getResources().getImageIconResourceResize(
                        "images/scv.designer.icon.compositeItem.png",
                        32,
                        32);
                choiceIcon = client.getResources().getImageIconResourceResize(
                    "images/scv.designer.icon.choices.png",
                    32,
                    32);
                choiceItemIcon =
                    client.getResources().getImageIconResourceResize(
                        "images/scv.designer.icon.choiceItem.png",
                        32,
                        32);
                rendererLabel = new JLabel();
                /*
                 * rendererLabel.setBorder(BorderFactory.createLineBorder(Color.
                 * black));
                 */
                backgroundColor = UIManager.getColor("Tree.background");
                selectionBackgroundColor =
                    UIManager.getColor("Tree.selectionBackground");
            }
            // CHECKSTYLE ON: MagicNumber
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
            if (rendererLabel != null
                && value != null) {

                Boolean visible = null;
                String label = null;
                String type = null;
                String subType = null;
                String name = null;
                Icon icon = null;

                final StringBuilder sb = new StringBuilder();
                if (value.getClass() == Sheet.class) {
                    label = ((Sheet) value).getTitle();
                    icon = sheetIcon;
                    type = "Sheet";
                } else if (value.getClass() == Page.class) {
                    visible = ((Page) value).isVisible();
                    type = "Page";
                    label = ((Page) value).getTitle();
                    name = ((Page) value).getName();
                    icon = pageIcon;
                } else if (value.getClass() == RepeatedPage.class) {
                    visible = ((RepeatedPage) value).isVisible();
                    type = "Repeated Page";
                    label = ((RepeatedPage) value).getTitle();
                    name = ((RepeatedPage) value).getName();
                    icon = repeatedPageIcon;
                } else if (value.getClass() == LabeledGroup.class) {
                    visible = ((Group) value).isVisible();
                    type = "Labeled Group";
                    label = ((LabeledGroup) value).getLabel();
                    name = ((Group) value).getName();
                    icon = labeledGroupIcon;
                } else if (value.getClass() == RepeatedGroup.class) {
                    visible = ((Group) value).isVisible();
                    type = "Repeated Group";
                    label = ((RepeatedGroup) value).getLabel();
                    name = ((Group) value).getName();
                    icon = repeatedGroupIcon;
                } else if (value.getClass() == Group.class) {
                    visible = ((Group) value).isVisible();
                    type = "Group";
                    name = ((Group) value).getName();
                    icon = groupIcon;
                    if (!((Group) value).isVisible()) {
                        sb.append("Invisible");
                    } else {
                        sb.append("Visible");
                    }
                } else if (value.getClass() == Item.class) {
                    visible = ((Item) value).isVisible();
                    type = "Item";
                    subType = ((Item) value).getType().toString();
                    label = ((Item) value).getTitle();
                    name = ((Item) value).getName();
                    icon = itemIcon;
                } else if (value.getClass() == CompositeItem.class) {
                    visible = ((Item) value).isVisible();
                    type = "Composite Item";
                    subType = ((Item) value).getType().toString();
                    label = ((Item) value).getTitle();
                    name = ((Item) value).getName();
                    icon = compositeItemIcon;
                } else if (value.getClass() == Choices.class) {
                    type = "Choice List";
                    icon = choiceIcon;
                } else if (value.getClass() == ChoiceItem.class) {
                    type = "Choice";
                    label = ((ChoiceItem) value).getLabel();
                    icon = choiceItemIcon;
                } else {
                    type = value.toString();
                    icon = null;
                }

                // {
                String varString2 = null;
                if (visible != null) {
                    if (visible) {
                        varString2 = "Visible";
                    } else {
                        varString2 = "Invisible";
                    }
                } else {
                    varString2 = "";
                }
                sb.append(varString2);
                sb.append(" ");
                sb.append(type);
                if (subType != null) {
                    sb.append(' ');
                    sb.append('[');
                    sb.append(subType);
                    sb.append(']');
                }
                if ((label != null)
                    && (label.trim().length() > 0)) {
                    sb.append(' ');
                    sb.append('"');
                    sb.append(label);
                    sb.append('"');
                }
                if ((name != null)
                    && (name.trim().length() > 0)) {
                    sb.append(' ');
                    sb.append('(');
                    sb.append(name);
                    sb.append(')');
                }
                rendererLabel.setIcon(icon);
                // }

                if (selected) {
                    rendererLabel.setBackground(selectionBackgroundColor);
                } else {
                    rendererLabel.setBackground(backgroundColor);
                }
                rendererLabel.setText(sb.toString());
            }
            return rendererLabel;
        }
    }

}
