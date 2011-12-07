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
package xmet.tools.metadata.editor.views.custom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import n.io.xml.CSXMLSerializationCodec;
import n.ui.SwingUtils;
import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.editorSheet.ProfileEditorSheet;
import xmet.profiles.model.Entity;
import xmet.tools.metadata.editor.EditorView;
import xmet.ui.controls.GUIObject;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

/**
 * CustomView EditorView.<br>
 * <br>
 * Modified to support the EditorView interface
 * @author Shaan
 */
@SuppressWarnings("serial")
public class CustomView
    extends EditorView {

    /**
     * The Class CustomVIewJTree.
     */
    private static final class CustomVIewJTree
        extends JTree {

        /**
         * Instantiates a new custom v iew j tree.
         * @param aNewModel the new model
         */
        private CustomVIewJTree(
            final TreeModel aNewModel) {
            super(aNewModel);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void setExpandedState(
            final TreePath path,
            final boolean state) {
            super.setExpandedState(
                path,
                true);
        }
    }

    /** The Constant FILENAME_EXTENSION. */
    public static final String FILENAME_EXTENSION = "xmetview";

    /** The Constant FILENAME_POSTFIX. */
    public static final String FILENAME_POSTFIX = "."
        + FILENAME_EXTENSION;

    /** The btn cancel. */
    private JButton btnHelp, btnBack, btnNext, btnFinish, btnCancel;

    /** The editor pane. */
    private JScrollPane editorPane;

    /** The tree. */
    private JTree tree;

    /** The curr page. */
    private int currPage = 0;

    /* private String name; */
    /** The root page. */
    private Page rootPage = new Page(
        "root");

    /** The main panel. */
    private final JPanel mainPanel = SwingUtils.BorderLayouts.getNew();

    /**
     * Instantiates a new custom view.
     * @param model the model
     * @param profile the profile
     * @param client the client
     * @param es the es
     */
    public CustomView(
        final Entity model,
        final Profile profile,
        final ClientContext client,
        final ProfileEditorSheet es) {

        super(model, profile, client);

        final ByteBuffer file = es.getFileContents(getClient().getResources());

        System.out.println("Loading custom view");
        final View view = (View) CSXMLSerializationCodec.decode(new String(
            file.array()));
        for (final Page page : view.getRootPage().getChildren()) {
            for (final GUIObject guiObject : page.getGuiObjects()) {
                if (guiObject instanceof Panel) {
                    System.err.println("Panel encountered");
                    ((Panel) guiObject).initialise();
                    ((Panel) guiObject).addRepeatedPanel();
                }
            }
        }

        rootPage = view.getRootPage();

        initComponents();

        mainPanel.add(buildWizardPanel());
        expandPages();

        GUIObjectToEntityMapper.mapGUIObjectsFromRootPage(
            getRoot(),
            rootPage);
    }

    /**
     * Expand pages.
     */
    private void expandPages() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        tree.setSelectionRow(currPage);
    }

    /**
     * Update selected.
     */
    public void updateSelected() {
        currPage = tree.getRowForPath(tree.getSelectionPath());
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        btnHelp = new JButton(
            "Help");
        btnBack = new JButton(
            "Back");
        btnNext = new JButton(
            "Next");
        btnFinish = new JButton(
            "Finish");
        btnCancel = new JButton(
            "Cancel");

        btnNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                if (currPage + 1 < tree.getRowCount()) {
                    tree.setSelectionRow(++currPage);
                }
            }

        });

        btnBack.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                if (currPage > 0) {
                    tree.setSelectionRow(--currPage);
                }
            }

        });
    }

    /**
     * Builds the wizard panel.
     * @return the j panel
     */
    private JPanel buildWizardPanel() {
        // CHECKSTYLE OFF: MagicNumber
        final FormLayout layout = new FormLayout(
            "80dlu:grow(0.25):grow, 3dlu, 200dlu:grow(0.75)",
            "fill:pref:grow,20dlu,pref");
        final DefaultFormBuilder builder = new DefaultFormBuilder(
            layout);
        builder.setDefaultDialogBorder();

        tree = new CustomVIewJTree(
            new PageTreeModel(
                rootPage));
        tree.setEditable(false);
        tree.setRootVisible(false);
        tree.setExpandsSelectedPaths(true);
        tree.setCellRenderer(new PageTreeCellRenderer());
        tree.addTreeSelectionListener(new PageTreeSelectionListener(
            this));
        tree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
        builder.append(new JScrollPane(
            tree));
        editorPane = new JScrollPane();
        editorPane.setViewportView(new JPanel());
        builder.append(editorPane);
        builder.nextLine();
        builder.add(
            ButtonBarFactory.buildWizardBar(
                new JButton[] {
                    btnHelp
                },
                btnBack,
                btnNext,
                new JButton[] {
                btnFinish,
                btnCancel
                }),
            CC.xyw(
                builder.getColumn(),
                builder.getRow(),
                3));

        return builder.getPanel();
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Gets the editor pane.
     * @return the editor pane
     */
    public JScrollPane getEditorPane() {
        return editorPane;
    }

    /**
     * Sets the editor pane.
     * @param aEditorPane the new editor pane
     */
    public void setEditorPane(
        final JScrollPane aEditorPane) {
        this.editorPane = aEditorPane;
    }

    /**
     * Gets the root page.
     * @return the root page
     */
    public Page getRootPage() {
        return rootPage;
    }

    /**
     * Sets the root page.
     * @param aRootPage the new root page
     */
    public void setRootPage(
        final Page aRootPage) {
        this.rootPage = aRootPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JPanel getEditorPanel() {
        /* TODO Auto-generated method stub */
        return mainPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postLoadCallback() {
        System.out.println("\n\n\n\nPOST LOAD CALLBACK CALLED!!!!\n\n\n\n");
        GUIObjectToEntityMapper.loadData(
            getRoot(),
            rootPage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isModelModified() {
        /* TODO Auto-generated method stub */
        return true;
    }

}
