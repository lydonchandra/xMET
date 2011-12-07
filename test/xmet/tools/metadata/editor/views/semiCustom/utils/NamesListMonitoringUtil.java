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
package xmet.tools.metadata.editor.views.semiCustom.utils;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import n.ui.SwingUtils;
import n.ui.patterns.callback.Callback;
import xmet.tools.metadata.editor.views.scv.utils.InitializationContext;

/**
 * Class made for visually debugging sheet and its properties. No documentation
 * necessary
 * @author Nahid Akbar
 */
public class NamesListMonitoringUtil
    extends Thread {

    /** Sheet being debugged. */
    private InitializationContext ic;

    /** Visual Dialog. */
    private JDialog dialog;

    /** The names list. */
    private JList namesList;

    /**
     * Instantiates a new names list monitoring util.
     * @param aIc the ic
     */
    public NamesListMonitoringUtil(
        final InitializationContext aIc) {
        // CHECKSTYLE OFF: MagicNumber
        super(String.format(
            "%1$s names list debug refresh thread",
            aIc.getItem()));
        this.setIc(aIc);
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final JPanel panel = SwingUtils.GridBag.getNew();

                // {
                setNamesList(new JList(
                    getNamesListModel()));
                getNamesList().setCellRenderer(
                    getListCellRenderer());
                // }

                SwingUtils.GridBag.add(
                    panel,
                    new JScrollPane(
                        getNamesList()),
                    "w=rem;f=b;wx=1;wy=1;");

                setDialog(SwingUtils.DIALOG.createDialog(
                    panel,
                    String.format(
                        "%1$s names list debug",
                        NamesListMonitoringUtil.this.getIc().getItem()),
                    800,
                    600,
                    false));

                getDialog().setVisible(
                    true);

                SwingUtils.WINDOW.addCloseButtonCallback(
                    getDialog(),
                    new Callback() {

                        @Override
                        public void callback() {
                            NamesListMonitoringUtil.this.interrupt();
                        }
                    });
                start();
            }

        });
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Gets the names list model.
     * @return the names list model
     */
    private ListModel getNamesListModel() {
        return new ListModel() {

            @Override
            public void removeListDataListener(
                final ListDataListener arg0) {

            }

            @Override
            public int getSize() {
                return getIc().getNamedItems().keySet().size();
            }

            @Override
            public Object getElementAt(
                final int arg0) {
                return getIc().getNamedItems().keySet().toArray()[arg0];
            }

            @Override
            public void addListDataListener(
                final ListDataListener arg0) {

            }
        };
    }

    /**
     * Gets the list cell renderer.
     * @return the list cell renderer
     */
    @SuppressWarnings("serial")
    private ListCellRenderer getListCellRenderer() {
        return new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                final JList list,
                final Object value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {
                final String label = String.format(
                    "%1$s - %2$s (%3$s)",
                    value,
                    getIc().getNamedItem(
                        (String) value),
                    getIc().getNamedItem(
                        (String) value).getClass().getSimpleName());
                return super.getListCellRendererComponent(
                    list,
                    label,
                    index,
                    isSelected,
                    cellHasFocus);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        try {
            while (true) {
                // CHECKSTYLE OFF: MagicNumber
                Thread.sleep(1000);
                // CHECKSTYLE ON: MagicNumber
                getNamesList().setModel(
                    getNamesListModel());
                /* dialog.pack(); */
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Dispose.
     */
    public void dispose() {
        interrupt();
        if (getDialog() != null) {
            getDialog().setVisible(
                false);
            getDialog().dispose();
            setDialog(null);
        }

    }

    /**
     * @return the ic
     */
    public InitializationContext getIc() {
        return ic;
    }

    /**
     * @param aIc the ic to set
     */
    public void setIc(
        final InitializationContext aIc) {
        this.ic = aIc;
    }

    /**
     * @return the dialog
     */
    public JDialog getDialog() {
        return dialog;
    }

    /**
     * @param aDialog the dialog to set
     */
    public void setDialog(
        final JDialog aDialog) {
        this.dialog = aDialog;
    }

    /**
     * @return the namesList
     */
    public JList getNamesList() {
        return namesList;
    }

    /**
     * @param aNamesList the namesList to set
     */
    public void setNamesList(
        final JList aNamesList) {
        this.namesList = aNamesList;
    }

}
