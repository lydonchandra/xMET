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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import n.io.CS;
import n.io.CSC;
import n.io.CSL;
import xmet.ui.controls.GUIObject;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Panel.
 * @author Shaan
 */
@SuppressWarnings("serial")
@CSC("Panel")
public class Panel
    extends GUIObject {

    /** The panels. */
    private final List<Panel> panels = new ArrayList<Panel>();

    /** The children. */
    @CSL
    @CSC
    private List<GUIObject> children = new ArrayList<GUIObject>();

    /** The page. */
    private Page page;

    /** The row spec. */
    @CS
    private String rowSpec = "";

    /** The col spec. */
    @CS
    private String colSpec = "";

    /** The create new page. */
    @CS
    private boolean createNewPage;

    /** The init repeated entities. */
    @CS
    private boolean initRepeatedEntities;

    /** The showing repeated entity panel. */
    @CS
    private boolean showingRepeatedEntityPanel;

    //
    // /** The entity. */
    /* private Entity entity; */

    /**
     * Instantiates a new panel.
     */
    public Panel() {
        super();
        setColSpec("fill:pref:grow,fill:pref:grow");
        setRowSpec("fill:pref:grow");
        setCreateNewPage(false);
        setInitRepeatedEntities(true);
        setShowingRepeatedEntityPanel(false);
    }

    /**
     * Update layout.
     */
    private void updateLayout() {
        if (!colSpec.isEmpty()
            && !rowSpec.isEmpty()) {
            setLayout(new FormLayout(
                colSpec,
                rowSpec));
        }
    }

    /**
     * Sets the row spec.
     * @param aRowSpec the rowSpec to set
     */
    public void setRowSpec(
        final String aRowSpec) {
        this.rowSpec = aRowSpec;
        updateLayout();
    }

    /**
     * Gets the row spec.
     * @return the rowSpec
     */
    public String getRowSpec() {
        return rowSpec;
    }

    /**
     * Sets the col spec.
     * @param aColSpec the colSpec to set
     */
    public void setColSpec(
        final String aColSpec) {
        this.colSpec = aColSpec;
        updateLayout();
    }

    /**
     * Gets the col spec.
     * @return the colSpec
     */
    public String getColSpec() {
        return colSpec;
    }

    /**
     * Sets the creates the new page.
     * @param aCreateNewPage the createNewPage to set
     */
    public void setCreateNewPage(
        final Boolean aCreateNewPage) {
        this.createNewPage = aCreateNewPage;
    }

    /**
     * Gets the creates the new page.
     * @return the createNewPage
     */
    public Boolean getCreateNewPage() {
        return createNewPage;
    }

    /**
     * Sets the inits the repeated entities.
     * @param aInitRepeatedEntities the initRepeatedEntities to set
     */
    public void setInitRepeatedEntities(
        final Boolean aInitRepeatedEntities) {
        this.initRepeatedEntities = aInitRepeatedEntities;
    }

    /**
     * Gets the inits the repeated entities.
     * @return the initRepeatedEntities
     */
    public Boolean getInitRepeatedEntities() {
        return initRepeatedEntities;
    }

    /**
     * Sets the showing repeated entity panel.
     * @param aShowingRepeatedEntityPanel the new showing repeated entity panel
     */
    public void setShowingRepeatedEntityPanel(
        final Boolean aShowingRepeatedEntityPanel) {
        this.showingRepeatedEntityPanel = aShowingRepeatedEntityPanel;
    }

    /**
     * Checks if is showing repeated entity panel.
     * @return the showRepeatedEntityPanel
     */
    public Boolean isShowingRepeatedEntityPanel() {
        return showingRepeatedEntityPanel;
    }

    /**
     * Adds the repeated panel.
     * @return the panel
     */
    public Panel addRepeatedPanel() {
        final Panel panel = new Panel();
        /* panel.setLayout(new FormLayout(getColSpec(), getRowSpec())); */
        /* for (final GUIObject guiObject : children) { */
        /* GUIObject clonedObject; */
        /* try { */
        /* clonedObject = (GUIObject) guiObject.clone(); */
        /* panel.add(clonedObject, */
        /* new CellConstraints(guiObject.getConstraints())); */
        // } catch (final CloneNotSupportedException e) {
        /* e.printStackTrace(); */
        // }
        // }
        /* final FormLayout layout = (FormLayout) getLayout(); */
        /* layout.appendRow(RowSpec.decode("$rgap")); */
        /* layout.appendRow(RowSpec.decode("f:d")); */
        /* this.add(panel, CC.xywh(2, layout.getRowCount(), */
        /* layout.getColumnCount() - 2, 1)); */
        /* panels.add(panel); */
        //
        /* layout.appendRow(RowSpec.decode("$rgap")); */
        /* layout.appendRow(RowSpec.decode("f:d")); */
        /* final JButton btnRemove = new JButton("Remove"); */
        /* btnRemove.addActionListener(new RemovePanelAction(panel)); */
        //
        /* this.add(btnRemove, CC.xywh(2, layout.getRowCount(), 1, 1)); */
        //
        /* revalidate(); */
        /* this.repaint(); */

        return panel;
    }

    /**
     * Removes the repeated panel.
     * @param panel the panel
     */
    private void removeRepeatedPanel(
        final Panel panel) {
        panels.remove(panel);
        /* remove everything and reconstruct the panel */
        removeAll();
        /* create and set default layout */
        final FormLayout layout = new FormLayout(
            "$ugap,f:d,$rgap,f:d,$rgap,f:p:grow,$ugap",
            "$ugap,f:d,$rgap");
        setLayout(layout);
        /* add the Add button */
        final JButton btnAdd = new JButton(
            "Add");
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                addRepeatedPanel();
            }

        });
        this.add(
            btnAdd,
            CC.xywh(
                2,
                2,
                1,
                1));
        /* add all repeated panels one by one */
        for (final Panel rPanel : panels) {
            layout.appendRow(RowSpec.decode("$rgap"));
            layout.appendRow(RowSpec.decode("f:d"));
            this.add(
                rPanel,
                CC.xywh(
                    2,
                    layout.getRowCount(),
                    layout.getColumnCount() - 2,
                    1));

            layout.appendRow(RowSpec.decode("$rgap"));
            layout.appendRow(RowSpec.decode("f:d"));
            final JButton btnRemove = new JButton(
                "Remove");
            btnRemove.addActionListener(new RemovePanelAction(
                rPanel));
            this.add(
                btnRemove,
                CC.xywh(
                    2,
                    layout.getRowCount(),
                    1,
                    1));
        }
        revalidate();
        this.repaint();
    }

    /*
     * Call method after setting the row and col specs and after all child gui
     * objects have been added and all properties have been set
     */
    /**
     * Initialise.
     */
    public void initialise() {
        /* create and set default layout */
        final FormLayout layout = new FormLayout(
            "$ugap,f:d,$rgap,f:d,$rgap,f:p:grow,$ugap",
            "$ugap,f:d,$rgap");
        setLayout(layout);
        /* add the Add button */
        final JButton btnAdd = new JButton(
            "Add");
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                addRepeatedPanel();
            }

        });
        this.add(
            btnAdd,
            CC.xywh(
                2,
                2,
                1,
                1));

    }

    // /**
    // * The main method.
    // *
    // * @param strArgs
    // * the arguments
    // */
    /* public static void main(final String strArgs[]) { */
    /* final Panel panel = new Panel(); */
    /* panel.setColSpec("center:pref,$rgap,fill:pref:grow"); */
    /* panel.setRowSpec("fill:100px:grow"); */
    //
    /* final Label label = new Label("Test"); */
    /* label.setConstraints("1,1,1,1"); */
    /* final MultiLineText text = new MultiLineText(); */
    /* text.setConstraints("3,1,1,1"); */
    //
    /* panel.add(label); */
    /* panel.add(text); */
    /* panel.setShowingRepeatedEntityPanel(true); */
    /* panel.setCreateNewPage(false); */
    //
    /* panel.initialise(); */
    //
    // /*panel.addRepeatedPanel(); */
    // /*panel.addRepeatedPanel(); */
    // /*panel.addRepeatedPanel(); */
    //
    /* final JFrame frame = new JFrame("Wizard Panel Test"); */
    /* frame.setSize(new Dimension(800, 600)); */
    /* frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); */
    /* frame.setContentPane(new JScrollPane(panel)); */
    /* frame.setVisible(true); */
    // }
    //
    // /**
    // * Gets the entity.
    // *
    // * @return the entity
    // * @see xmet.ui.controls.GUIObject#getEntity()
    // */
    // @Override
    /* public Entity getEntity() { */
    /* return entity; */
    // }
    //
    // /**
    // * Sets the entity.
    // *
    // * @param entity
    // * the new entity
    // * @see xmet.ui.controls.GUIObject#setEntity(xmet.profiles.model.Entity)
    // */
    // @Override
    /* public void setEntity(final Entity entity) { */
    /* this.entity = entity; */
    // }

    /**
     * Sets the page.
     * @param aPage the page to set
     */
    public void setPage(
        final Page aPage) {
        this.page = aPage;
    }

    /**
     * Gets the page.
     * @return the page
     */
    public Page getPage() {
        return page;
    }

    /**
     * Adds the.
     * @param guiObject the gui object
     */
    public void add(
        final GUIObject guiObject) {
        children.add(guiObject);
    }

    /**
     * Gets the children.
     * @return the children
     */
    public List<GUIObject> getChildren() {
        return children;
    }

    /**
     * Sets the children.
     * @param aChildren the new children
     */
    public void setChildren(
        final List<GUIObject> aChildren) {
        this.children = aChildren;
    }

    /**
     * RemovePanelAction.
     */
    private static class RemovePanelAction
        implements
        ActionListener {

        /** The panel. */
        private final Panel panel;

        /**
         * Instantiates a new removes the panel action.
         * @param aPanel the panel
         */
        public RemovePanelAction(
            final Panel aPanel) {
            this.panel = aPanel;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(
            final ActionEvent e) {
            final Panel parent = (Panel) panel.getParent();
            parent.removeRepeatedPanel(panel);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return getInternalValue();
    }

}
