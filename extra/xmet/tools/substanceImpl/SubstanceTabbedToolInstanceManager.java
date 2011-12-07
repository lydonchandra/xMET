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
package xmet.tools.substanceImpl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import n.reporting.ReportType;
import n.reporting.Reporting;
import n.reporting.ReportingCallback;
import n.reporting.ReportingCallbackImpl;
import n.ui.SwingUtils;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.tabbed.VetoableTabCloseListener;

import xmet.ClientContext;
import xmet.tools.ToolInstance;
import xmet.tools.ToolInstanceManager;
import xmet.utils.LookAndFeelUtil;

/**
 * Tool instance manager display implemented with tabs using the Substance Look
 * and Feel tab close button extension.
 */
@SuppressWarnings("serial")
public class SubstanceTabbedToolInstanceManager
    extends ToolInstanceManager
    implements
    VetoableTabCloseListener,
    ChangeListener,
    ReportingCallback {

    /** The panel. */
    private final JPanel panel = new JPanel();

    /** Tabbed pane control. */
    private final JTabbedPane tabbedPane;

    /** The Substance tabbed tool instance manager container. */

    /** Gui Associated with this - using the standard one. */
    private final SubstanceTabbedToolInstanceManagerContainer gui;

    /**
     * Instantiates a new substance tabbed tool instance manager.
     * @param context the context
     */
    public SubstanceTabbedToolInstanceManager(
        final ClientContext context) {
        // CHECKSTYLE OFF: MagicNumber
        super(context);
        LookAndFeelUtil.initializeGUIFixes();
        /* initialize layout */
        panel.setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
        panel.add(tabbedPane);
        SwingUtils.FONT.increaseFontSize(
            tabbedPane,
            4.0f);

        /* initialize gui */
        gui = new SubstanceTabbedToolInstanceManagerContainer(
            this);

        SubstanceLookAndFeel.registerTabCloseChangeListener(
            tabbedPane,
            this);
        tabbedPane.addChangeListener(this);
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Gets the display panel.
     * @return the display panel
     */
    public Component getDisplayPanel() {
        return panel;
    }

    /* == ToolInstanceManager Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addToolInstanceUI(
        final ToolInstance e,
        final int index) {
        final Container displayPanel = e.getDisplayPanel();
        final String title = e.getTitle();
        if (displayPanel != null) {
            final JComponent jc = (JComponent) tabbedPane.add(
                title,
                displayPanel);
            jc.putClientProperty(
                SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY,
                Boolean.TRUE);
            new TabTitleObserver(
                e);
            stateChanged(null);
        } else {
            Reporting.logExpected("tool instance has no display panel");
        }
    }

    /**
     * An asynchronous update interface for receiving notifications about
     * TabTitle information as the TabTitle is constructed.
     * @author Nahid Akbar Custom control for adding a close button to the tab
     *         labels
     */
    private class TabTitleObserver
        extends JPanel
        implements
        Observer {

        /** The instance. */
        private final ToolInstance instance;

        /**
         * This method is called when information about an TabTitle which was
         * previously requested using an asynchronous interface becomes
         * available.
         * @param ins the ins
         */
        public TabTitleObserver(
            final ToolInstance ins) {
            instance = ins;
            if (ins instanceof Observable) {
                ((Observable) ins).addObserver(this);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void update(
            final Observable arg0,
            final Object arg1) {
            for (int i = 0; i < getToolInstancesCount(); i++) {
                if (getToolInstance(i) == instance) {
                    tabbedPane.setTitleAt(
                        i,
                        instance.getTitle());
                    tabbedPane.setComponentAt(
                        i,
                        instance.getDisplayPanel());
                    final String varProperties =
                        SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY;
                    instance.getDisplayPanel().putClientProperty(
                        varProperties,
                        Boolean.TRUE);
                }
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void clearInstancesUI() {
        tabbedPane.removeAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void focusToolInstanceUI(
        final int i) {
        if ((i >= 0)
            && (i < getToolInstancesCount())) {
            tabbedPane.setSelectedIndex(i);
            stateChanged(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeToolInstanceUI(
        final int index) {
        tabbedPane.removeTabAt(index);
        stateChanged(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void disposeUI() {
        gui.dispose();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ToolInstance getActiveToolInstance() {
        final int active = tabbedPane.getSelectedIndex();
        ToolInstance varToolInstance = null;
        if (((active >= 0) && (active < getToolInstancesCount()))) {
            varToolInstance = getToolInstance(active);
        } else {
            varToolInstance = null;
        }
        return varToolInstance;
    }

    /* == VetoableTabCloseListener Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void tabClosed(
        final JTabbedPane arg0,
        final Component arg1) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tabClosing(
        final JTabbedPane arg0,
        final Component arg1) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean vetoTabClosing(
        final JTabbedPane arg0,
        final Component arg1) {
        for (int i = 0; i < getToolInstancesCount(); i++) {
            if (getToolInstance(
                i).getDisplayPanel() == arg1) {
                removeToolInstance(i);
            }
        }
        return true;
    }

    /* == ChangeListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void stateChanged(
        final ChangeEvent arg0) {
        final int i = tabbedPane.getSelectedIndex();
        if (i != -1) {
            final ToolInstance ti = getToolInstance(i);
            gui.updateGUI(ti);
        }
    }

    /* == Misc == */
    /**
     * {@inheritDoc}
     */
    @Override
    public ReportingCallback getReportingCallback() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewToolInstance(
        final xmet.tools.Tool tool,
        final java.util.Map<String, Object> params) {
        getDisplayPanel().setCursor(
            Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        super.createNewToolInstance(
            tool,
            params);
        getDisplayPanel().setCursor(
            Cursor.getDefaultCursor());
    };

    /* == ReportingCallback implementation == */
    /** The dri. */
    private final ReportingCallbackImpl dri = new ReportingCallbackImpl();

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleReport(
        final ReportType aType,
        final boolean aReportToUser,
        final Date aDate,
        final String aMessage,
        final StackTraceElement[] aStackTrace,
        final Throwable aThrowable) {

        if (aType == ReportType.expected) {
            if (aReportToUser) {
                JOptionPane.showMessageDialog(
                    null,
                    aMessage);
            }
        } else if (aType == ReportType.unexpected) {
            if (aReportToUser) {
                final ErrorInfo info = new ErrorInfo(
                    "Unexpected Event",
                    aMessage,
                    aMessage,
                    aType.toString()
                        + " event",
                    aThrowable,
                    Level.ALL,
                    null);
                JXErrorPane.showDialog(
                    panel,
                    info);
            }
        } else {
            Reporting.logUnexpected();
        }
        dri.handleReport(
            aType,
            aReportToUser,
            aDate,
            aMessage,
            aStackTrace,
            aThrowable);
    }
}
