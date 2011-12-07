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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import n.io.CS;
import n.io.CSC;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

/**
 * View.
 * @author Shaan
 */
@SuppressWarnings("serial")
@CSC("View")
public class View
    extends JFrame {

    /** The btn cancel. */
    private JButton btnHelp, btnBack, btnNext, btnFinish, btnCancel;

    /** The editor pane. */
    private JScrollPane editorPane;

    /** The tree. */
    private JTree tree;

    // /** The curr page. */
    // private final int currPage = 0;

    /** The name. */
    @CS
    private String name;

    /** The description. */
    @CS
    private String description;

    /** The template. */
    @CS
    private String template;

    /** The root page. */
    @CSC
    private Page rootPage = new Page(
        "root");

    /**
     * Instantiates a new view.
     */
    public View() {
        super("Custom View");
    }

    /**
     * Instantiates a new view.
     * @param aName the name
     * @param aRootPage the root page
     */
    public View(
        final String aName,
        final Page aRootPage) {
        this();
        this.name = aName;
        this.rootPage = aRootPage;
    }

    /**
     * Show view.
     */
    public void showView() {
        // CHECKSTYLE OFF: MagicNumber
        initComponents();
        setSize(new Dimension(
            800,
            600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(
            buildWizardPanel());
        setVisible(true);
        // CHECKSTYLE ON: MagicNumber
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
                TreePath treePath = tree.getSelectionPath();
                /* tree.expandPath(treePath); */
                Page mypage;
                if (treePath == null) {
                    mypage = rootPage;
                    treePath = new TreePath(
                        mypage);
                } else {
                    mypage = (Page) treePath.getLastPathComponent();
                }
                if (mypage.getChildren().size() > 0) {
                    /* treePath = new TreePath(mypage.get(0)); */
                    treePath = treePath.pathByAddingChild(mypage.get(0));
                } else {
                    Page parentPage = mypage.getParent();
                    treePath = treePath.getParentPath();
                    int i = tree.getModel().getIndexOfChild(
                        parentPage,
                        mypage) + 1;
                    /* System.out.println("i="+i); */
                    while ((parentPage != null)
                        && (parentPage.getChildren().size() <= i)) {
                        /* System.out.println("while loop"); */
                        mypage = parentPage;
                        parentPage = mypage.getParent();
                        i = tree.getModel().getIndexOfChild(
                            parentPage,
                            mypage) + 1;
                        treePath = treePath.getParentPath();
                    }

                    if (parentPage != null) {
                        if (i >= parentPage.getChildren().size()) {
                            i = parentPage.getChildren().size() - 1;
                        }
                        if (i > -1) {
                            treePath =
                                treePath.pathByAddingChild(parentPage.get(i));
                        }
                    } else {
                        treePath = tree.getSelectionPath();
                    }
                }

                tree.setSelectionPath(treePath);

                /* System.out.println(treePath.getPathCount()); */
            }

        });

        btnBack.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                TreePath treePath = tree.getSelectionPath();
                if (treePath == null) {
                    return;
                }

                Page mypage = (Page) treePath.getLastPathComponent();
                if (mypage == null) {
                    return;
                }

                Page parentPage = mypage.getParent();
                final int index = tree.getModel().getIndexOfChild(
                    parentPage,
                    mypage) - 1;
                /*
                 * System.out.println("index="+index+",page="+mypage.getName());
                 */
                if (index > -1) {
                    mypage = parentPage.get(index);
                    treePath = treePath.getParentPath().pathByAddingChild(
                        mypage);
                    while (mypage.getChildren().size() > 0) {
                        parentPage = mypage;
                        mypage =
                            parentPage.get(parentPage.getChildren().size() - 1);
                        treePath = treePath.pathByAddingChild(mypage);
                    }

                } else {
                    treePath = treePath.getParentPath();
                }
                tree.setSelectionPath(treePath);
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

        tree = new JTree(
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
     * {@inheritDoc}
     */
    @Override
    public void setName(
        final String aName) {
        this.name = aName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
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
     * The main method.
     * @param strArgs the arguments
     * @throws Exception the exception
     */
    public static void main(
        final String[] strArgs)
        throws Exception {
        /*
         * String s; System.out.println(s = StorageXMLCodec.encode(view)); //
         * Object o = StorageXMLCodec.decode(s); Object o =
         * StorageXMLCodec.decodeClasses(s, Arrays.asList(View.class,
         * Page.class, GUIObject.class)); System.out.println(s =
         * StorageXMLCodec.encode(o));
         */
    }

    /**
     * Sets the description.
     * @param aDescription the description to set
     */
    public void setDescription(
        final String aDescription) {
        this.description = aDescription;
    }

    /**
     * Gets the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the template.
     * @param aTemplate the template to set
     */
    public void setTemplate(
        final String aTemplate) {
        this.template = aTemplate;
    }

    /**
     * Gets the template.
     * @return the template
     */
    public String getTemplate() {
        return template;
    }
}
