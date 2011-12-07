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
package xmet.tools.metadata.editor.views.scv.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataListener;

import n.reporting.Reporting;
import n.ui.SwingUtils;
import xmet.tools.metadata.editor.views.scv.impl.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.impl.Choices;

/**
 * Display context for Choices.
 * @author Nahid Akbar
 */
public class ChoiceDC
    extends DisplayContext
    implements
    ComboBoxModel,
    ListCellRenderer {

    /** The choice selection combo box. */
    private final JComboBox choiceSelectionComboBox;

    /** The display panel. */
    private final JPanel displayPanel;

    /** The choices. */
    private final Choices choices;

    /** The contents sub panel. */
    private JPanel contentsSubPanel = null;

    /** The choices selection combo box renderer. */
    private final JLabel choicesSelectionComboBoxRenderer = new JLabel();

    /**
     * Instantiates a new choice dc.
     * @param aChoices the choices
     */
    public ChoiceDC(
        final Choices aChoices) {
        this.choices = aChoices;
        choiceSelectionComboBox = new JComboBox(
            this);
        choiceSelectionComboBox.setBorder(BorderFactory
            .createLineBorder(Color.BLUE));
        choiceSelectionComboBox.setRenderer(this);
        displayPanel = SwingUtils.GridBag.getNew();
        rebuildInternal();
    }

    /* == DC Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public Component getDisplayPanel() {
        return displayPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildDisplayPanel() {
        new PanelBuilderUtil(
            choices);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshPanel() {
        displayPanel.revalidate();
    }

    /**
     * Rebuild internal.
     */
    private void rebuildInternal() {
        displayPanel.removeAll();
        SwingUtils.GridBag.add(
            displayPanel,
            choiceSelectionComboBox,
            "f=h;wx=0;wy=0;w=rel;");
        SwingUtils.GridBag.add(
            displayPanel,
            Box.createGlue(),
            "f=h;wx=1;wy=0;w=rem;");
        /* SwingUtils.GridBag.add(displayPanel, choiceSelectionComboBox, */
        // "f=h;wx=0;wy=0;w=rem;");
        if (contentsSubPanel != null) {
            SwingUtils.GridBag.add(
                displayPanel,
                contentsSubPanel,
                "f=h;wx=1;wy=1;w=rem;");
        }
        displayPanel.revalidate();
        displayPanel.repaint();
        Container panel = displayPanel.getParent();
        while (panel != null) {
            if (panel instanceof JComponent) {
                ((JComponent) panel).revalidate();
                break;
            } else {
                panel = panel.getParent();
            }
        }
    }

    /**
     * Sets the panel.
     * @param pop the new panel
     */
    public void setPanel(
        final JPanel pop) {
        contentsSubPanel = pop;
        rebuildInternal();
    }

    /* == ComboBoxModel implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void addListDataListener(
        final ListDataListener arg0) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getElementAt(
        final int arg0) {
        return choices.getItems().getItem(
            arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return choices.getItems().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListDataListener(
        final ListDataListener arg0) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getSelectedItem() {
        return choices.getItems().getItem(
            choices.getIc().getSelected());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedItem(
        final Object anItem) {
        /* ChoiceItem current = (ChoiceItem) getSelectedItem(); */

        final int i = choices.getItems().indexOf(
            anItem);
        if ((i >= 0)
            && (i < choices.getItems().size())) {
            Reporting.logExpected(
                "Select %1$d",
                i);
            /* choices.ic.setSelected(i); */
            final ChoiceItem newItem = choices.getItems().get(
                i);
            newItem.getIc().hardRetraceLastXpath();
            choices.getIc().updateSelected();
            new PanelBuilderUtil(
                newItem.getItem());
            if ((newItem.getItem() != null)
                && (newItem.getItem().getIC() != null)) {
                new DataResetUtil(
                    newItem.getItem(),
                    newItem.getIc().getLastParentPath());
            }
        }
        rebuildDisplayPanel();
    }

    /* == ListCellRenderer Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(
        final JList arg0,
        final Object arg1,
        final int arg2,
        final boolean selected,
        final boolean arg4) {
        if (arg1 instanceof ChoiceItem) {
            choicesSelectionComboBoxRenderer.setText(((ChoiceItem) arg1)
                .getLabel());
            if ((((ChoiceItem) arg1).getLabel() == null)
                || (((ChoiceItem) arg1).getLabel().trim().length() == 0)) {
                choicesSelectionComboBoxRenderer.setText(((ChoiceItem) arg1)
                    .getTestXpath());
            }
        } else {
            choicesSelectionComboBoxRenderer.setText(String.format(
                "%1$s",
                arg1));
        }
        if (selected) {
            choicesSelectionComboBoxRenderer.setBorder(BorderFactory
                .createLineBorder(Color.BLACK));
        } else {
            choicesSelectionComboBoxRenderer.setBorder(BorderFactory
                .createEmptyBorder());
        }
        return choicesSelectionComboBoxRenderer;
    }

}
