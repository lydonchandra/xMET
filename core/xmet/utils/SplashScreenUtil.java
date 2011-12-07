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
package xmet.utils;

import java.awt.Color;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import n.reporting.Report;
import n.reporting.ReportType;
import n.reporting.ReportingCallback;
import n.reporting.ReportingCallbackImpl;
import n.ui.SwingUtils;

/**
 * Class for displaying our splash screen. <br />
 * <h3>usage</h3> <code>
 * openSplash(); <br />
 * ...<br />
 * Reporting.log(...);<br />
 * ...<br />
 * closeSplash();
 * </code>
 * @author Nahid Akbar
 */
public final class SplashScreenUtil {

    /**
     * Instantiates a new splash screen util.
     */
    protected SplashScreenUtil() {

    }

    /**
     * The actual implementation class.
     */
    static class SplashScreenWindow
        implements
        ReportingCallback {

        /** The window. */
        private JWindow window = null;

        /** The content. */
        private final JPanel content;

        /** The progress. */
        private final JProgressBar progress;

        /** The Constant PB_HEIGHT. */
        private static final int PB_HEIGHT = 18;

        /**
         * Instantiates a new splash screen window.
         * @param icon the icon
         */
        SplashScreenWindow(
            final Icon icon) {
            progress = new JProgressBar();
            progress.setSize(
                icon.getIconWidth(),
                PB_HEIGHT);
            progress.setIndeterminate(true);
            progress.setStringPainted(true);
            content = SwingUtils.BoxLayouts.getVerticalPanel();
            content.setBackground(Color.white);
            content.add(new JLabel(
                icon));
            content.add(progress);
            window = new JWindow();
            window.setContentPane(content);
            window.setSize(
                icon.getIconWidth(),
                icon.getIconHeight()
                    + PB_HEIGHT);
            SwingUtils.WINDOW.centreWindow(window);
            window.setVisible(true);
        }

        /**
         * Progress update.
         * @param status the status
         */
        public void progressUpdate(
            final String status) {
            progress.setString(status);
        }

        /** The dri. */
        private final ReportingCallbackImpl dri = new ReportingCallbackImpl();

        @Override
        public void handleReport(
            final ReportType aType,
            final boolean aReportToUser,
            final Date aDate,
            final String aMessage,
            final StackTraceElement[] aStackTrace,
            final Throwable aThrowable) {
            progressUpdate(aMessage);
            dri.handleReport(new Report(
                aType,
                aReportToUser,
                aDate,
                aMessage,
                aStackTrace,
                aThrowable));
        }

        /**
         * Close.
         */
        public void close() {
            if (window != null) {
                window.setVisible(false);
                window.dispose();
                window = null;
            }
        }
    }

    /** The splash. */
    private static SplashScreenWindow splash = null;

    /**
     * This method must be called first.
     * @param icon the icon
     */
    public static void openSplash(
        final Icon icon) {
        if (java.awt.SplashScreen.getSplashScreen() != null) {
            java.awt.SplashScreen.getSplashScreen().close();
        }
        closeSplash();
        splash = new SplashScreenWindow(
            icon);
    }

    /**
     * Gets the reporting callback.
     * @return the reporting callback
     */
    public static ReportingCallback getReportingCallback() {
        return splash;
    }

    /**
     * This method should be called to close the splash window.
     */
    public static void closeSplash() {
        if (splash != null) {
            splash.close();
            splash = null;
        }
    }
}
