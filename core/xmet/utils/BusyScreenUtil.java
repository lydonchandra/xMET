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

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import n.reporting.Reporting;
import n.ui.SwingUtils;

import org.jdesktop.swingx.JXBusyLabel;

/**
 * Simple utility for showing a busy screen when the program does something
 * expensive such as loading a profile or generating an editing interface so the
 * user knows that the system is working.
 * @author Nahid Akbar
 */
public final class BusyScreenUtil {

    /**
     * Instantiates a new busy screen util.
     */
    private BusyScreenUtil() {

    }

    /** the window. */
    private static Window busyScreen;

    /** busy icon. */
    private static JXBusyLabel busyLabel;

    /** busy label. */
    private static JLabel busyLabelText;

    /** Our event queue for doing stuff. */
    private static CustomEventQueue busyEventQueueImplementation;

    /** System's default event queue. */
    private static EventQueue originalEventQueue;

    /** Stack of action strings. */
    private static Stack<String> actions;
    static {
        init();
    }

    /**
     * initializes the class ready for use.
     */
    private static void init() {
        if (busyScreen == null) {
            originalEventQueue =
                Toolkit.getDefaultToolkit().getSystemEventQueue();
            busyEventQueueImplementation = new CustomEventQueue();
            busyScreen = new Window(
                null);
            busyScreen.removeAll();
            busyScreen.setLayout(new BoxLayout(
                busyScreen,
                BoxLayout.LINE_AXIS));
            busyLabel = new JXBusyLabel();
            busyScreen.add(busyLabel);
            busyLabelText = new JLabel();
            busyScreen.add(busyLabelText);
            actions = new Stack<String>();
        }
    }

    /**
     * This must be called to make the busy label appear <code>
     * use the pattern
     * startBusy()
     * try {
     *   ...
     * } catch (...) {
     * } finally {
     *   endBusy()
     * }
     * </code>
     * @param activity the activity
     */
    public static void startBusy(
        final String activity) {
        Reporting.logExpected(activity);
        init();
        busyLabel.setBusy(true);
        actions.push(activity);
        setActivity(activity);
        busyScreen.pack();
        SwingUtils.WINDOW.centreWindow(busyScreen);
        busyScreen.setVisible(true);
        tickBusy();
    }

    /**
     * this must be called to make the busy label disappear.
     */
    public static void endBusy() {
        init();
        if (actions.size() > 1) {
            actions.pop();
        }
        if (actions.size() > 1) {
            setActivity(actions.peek());
        } else {
            busyScreen.setVisible(false);
            busyLabel.setBusy(false);
        }
    }

    /**
     * This must be called every so often to keep animating the busy icon.
     */
    public static void tickBusy() {
        init();
        busyLabel.setBusy(true);
        busyEventQueueImplementation.clean();
        busyScreen.toFront();
    }

    /**
     * Call this method to update the activity string.
     * @param activity the activity
     */
    public static void tickBusy(
        final String activity) {
        setActivity(activity);
        tickBusy();
    }

    /**
     * Sets the activity.
     * @param activity the new activity
     */
    private static void setActivity(
        final String activity) {
        busyLabel.setToolTipText(activity);
        busyLabelText.setText(activity);
        busyLabelText.setToolTipText(activity);
    }

    /**
     * Custom event queue implementation.
     */
    static class CustomEventQueue
        extends EventQueue {

        /**
         * Method gets all the pending events and dispatches them - this had to
         * be a subclass due to dispatchEvent being a protected method.
         */
        public void clean() {
            AWTEvent event = null;
            try {
                while (originalEventQueue.peekEvent() != null) {
                    try {
                        event = originalEventQueue.getNextEvent();
                        dispatchEvent(event);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        };
    };
}
