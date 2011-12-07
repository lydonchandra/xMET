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

import static xmet.tools.ToolInstanceEvents.CLOSE_DOCUMENT;
import static xmet.tools.ToolInstanceEvents.CLOSE_TOOL_INSTANCE;
import static xmet.tools.ToolInstanceEvents.HELP;
import static xmet.tools.ToolInstanceEvents.NEW_DOCUMENT;
import static xmet.tools.ToolInstanceEvents.NEXT_PAGE;
import static xmet.tools.ToolInstanceEvents.NEXT_TOOL_INSTANCE;
import static xmet.tools.ToolInstanceEvents.OPEN_DOCUMENT;
import static xmet.tools.ToolInstanceEvents.PREVIOUS_PAGE;
import static xmet.tools.ToolInstanceEvents.PREVIOUS_TOOL_INSTANCE;
import static xmet.tools.ToolInstanceEvents.SAVE_DOCUMENT;
import static xmet.tools.ToolInstanceEvents.SAVE_DOCUMENT_AS;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Event;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import n.reporting.Reporting;
import n.ui.JOptionPaneUtils;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.tools.DocumentEditingTI;
import xmet.tools.MultipageTI;
import xmet.tools.ToolInstance;
import xmet.tools.ToolInstanceManager;

/**
 * ClientAppSwingGUI
 * <p/>
 * A GUI for the ClientApplication built with Swing. All the public methods must
 * be executed on the EDT as they are not thread-safe.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class SubstanceTabbedToolInstanceManagerContainer
    extends JFrame
    implements
    KeyEventDispatcher {

    /** Main Window JFrame. */
    private final JFrame mainWindowFrame;

    /** The main toolbar. */
    private final JMenuBar mainMenubar = new JMenuBar();

    /** Reference to the tool instance manager. */
    private transient ToolInstanceManager toolInstanceManager;

    /**
     * Instantiates a new client swing gui.
     * @param aToolInstanceManager the tool instance manager
     */
    public SubstanceTabbedToolInstanceManagerContainer(
        final SubstanceTabbedToolInstanceManager aToolInstanceManager) {
        super(aToolInstanceManager
            .getContext()
            .getConfig()
            .getApplicationName());
        this.toolInstanceManager = aToolInstanceManager;
        mainWindowFrame = this;

        final JPanel mainPanel = SwingUtils.BorderLayouts.getNew();

        mainWindowFrame.setContentPane(mainPanel);

        if (aToolInstanceManager.getContext().getConfig().getTempSetting(
            "noSideBar",
            null) == null
            || aToolInstanceManager.getContext().getConfig().getTempSetting(
                "admin",
                null) != null) {
            mainPanel.add(
                mainMenubar,
                BorderLayout.NORTH);
        }
        mainPanel.add(aToolInstanceManager.getDisplayPanel());

        SwingUtils.WINDOW.centreSizeWindow(
            mainWindowFrame,
            Integer.parseInt(aToolInstanceManager
                .getContext()
                .getConfig()
                .getTempSetting(
                    "border",
                    "50")));

        mainWindowFrame.setVisible(true);
        final Object[] params = {};

        SwingUtils.WINDOW.addCloseButtonCallback(
            mainWindowFrame,
            new ClassMethodCallback(
                this,
                "onClose",
                params));

        mainWindowFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        KeyboardFocusManager
            .getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(
                this);

        rebuildMainMenubar(null);

        mainWindowFrame.setIconImage(aToolInstanceManager
            .getContext()
            .getResources()
            .getImageResource(
                "images/xmet_logo.png"));

    }

    /* CHECKSTYLE OFF: MethodLength */
    /**
     * Rebuild main menubar.
     * @param currentToolinstance the current toolinstance
     */
    private void rebuildMainMenubar(
        final ToolInstance currentToolinstance) {
        mainMenubar.removeAll();
        // {
        final JMenu fileMenu = new JMenu(
            "File");
        if (currentToolinstance instanceof DocumentEditingTI) {
            final Object[] params = {};
            fileMenu.add(SwingUtils.MENUITEM.getNew(
                "New",
                new ClassMethodCallback(
                    this,
                    "newButton",
                    params)));
            fileMenu.add(SwingUtils.MENUITEM.getNew(
                "Open",
                new ClassMethodCallback(
                    this,
                    "openButton",
                    params)));
            fileMenu.add(SwingUtils.MENUITEM.getNew(
                "Save",
                new ClassMethodCallback(
                    this,
                    "saveButton",
                    params)));
            final Object[] params3 = {};
            fileMenu.add(SwingUtils.MENUITEM.getNew(
                "Save As",
                new ClassMethodCallback(
                    this,
                    "saveAsButton",
                    params3)));
            final Object[] params4 = {};
            fileMenu.add(SwingUtils.MENUITEM.getNew(
                "Close",
                new ClassMethodCallback(
                    this,
                    "closeButton",
                    params4)));
            fileMenu.addSeparator();
        }
        final Object[] params = {};
        fileMenu.add(SwingUtils.MENUITEM.getNew(
            "Start",
            new ClassMethodCallback(
                this,
                "onStart",
                params)));

        fileMenu.add(SwingUtils.MENUITEM.getNew(
            "Exit",
            new ClassMethodCallback(
                this,
                "onClose",
                params)));
        mainMenubar.add(fileMenu);
        // }

        // {
        final JMenu toolsMenu = new JMenu(
            "Tools");
        if (currentToolinstance instanceof MultipageTI) {
            toolsMenu.add(SwingUtils.MENUITEM.getNew(
                "Next Page",
                new ClassMethodCallback(
                    this,
                    "nextPageButton",
                    params)));
            toolsMenu.add(SwingUtils.MENUITEM.getNew(
                "Previous Page",
                new ClassMethodCallback(
                    this,
                    "prevPageButton",
                    params)));
            toolsMenu.addSeparator();
        }
        toolsMenu.add(SwingUtils.MENUITEM.getNew(
            "Next",
            new ClassMethodCallback(
                this,
                "nextToolInstanceButton",
                params)));
        toolsMenu.add(SwingUtils.MENUITEM.getNew(
            "Previous",
            new ClassMethodCallback(
                this,
                "prevToolInstanceButton",
                params)));

        toolsMenu.add(SwingUtils.MENUITEM.getNew(
            "Close",
            new ClassMethodCallback(
                this,
                "closeToolInstanceButton",
                params)));
        mainMenubar.add(toolsMenu);
        // }

        if (toolInstanceManager.getContext().getConfig().getTempSetting(
            "admin",
            null) != null) {
            final JMenu helpMenu = new JMenu(
                "Administration");
            helpMenu.add(SwingUtils.MENUITEM.getNew(
                "Tools Manager",
                new ClassMethodCallback(
                    this,
                    "toolsManagerButton",
                    params)));
            helpMenu.addSeparator();
            helpMenu.add(SwingUtils.MENUITEM.getNew(
                "Start Screen Designer",
                new ClassMethodCallback(
                    this,
                    "startScreenDesignerButton",
                    params)));
            helpMenu.addSeparator();
            helpMenu.add(SwingUtils.MENUITEM.getNew(
                "Semi Custom View Designer",
                new ClassMethodCallback(
                    this,
                    "semiCustomViewDesignerButton",
                    params)));
            mainMenubar.add(helpMenu);
        }

        // {
        final JMenu helpMenu = new JMenu(
            "Help");
        helpMenu.add(SwingUtils.MENUITEM.getNew(
            "Show Help",
            new ClassMethodCallback(
                this,
                "helpButton")));
        helpMenu.add(SwingUtils.MENUITEM.getNew(
            "About xMET",
            new ClassMethodCallback(
                this,
                "aboutButton",
                params)));
        helpMenu.add(SwingUtils.MENUITEM.getNew(
            "Show xMET Feedback Blog",
            new ClassMethodCallback(
                this,
                "blogButton",
                params)));
        mainMenubar.add(helpMenu);
        // }

    }

    /* CHECKSTYLE ON: MethodLength */

    /* == Button Handlers == */

    /**
     * On start.
     */
    public void onStart() {
        toolInstanceManager.getContext().getTools().invokeToolByName(
            toolInstanceManager.getContext().getConfig().getTempSetting(
                "default",
                "application.start"));
    }

    /**
     * On close.
     */
    public void onClose() {
        if (JOptionPaneUtils.getYesNoConfirmation("Exit the Program?")) {
            toolInstanceManager.getContext().shutdown();
        }
        // else {
        // }
    }

    /**
     * Tools tool button.
     */
    public void toolsToolButton() {
        toolInstanceManager.getContext().getTools().invokeToolByName(
            "application.tools");
    }

    /**
     * Start button.
     */
    public void startButton() {
        toolInstanceManager.getContext().getTools().invokeToolByName(
            toolInstanceManager.getContext().getConfig().getTempSetting(
                "default",
                "application.start"));
    }

    /**
     * New button.
     */
    public void newButton() {
        toolInstanceManager.processEvent(NEW_DOCUMENT);
    }

    /**
     * Open button.
     */
    public void openButton() {
        toolInstanceManager.processEvent(OPEN_DOCUMENT);
    }

    /**
     * Save button.
     */
    public void saveButton() {
        toolInstanceManager.processEvent(SAVE_DOCUMENT);
    }

    /**
     * Save as button.
     */
    public void saveAsButton() {
        toolInstanceManager.processEvent(SAVE_DOCUMENT_AS);
    }

    /**
     * Close button.
     */
    public void closeButton() {
        toolInstanceManager.processEvent(CLOSE_DOCUMENT);
    }

    /**
     * Tools manager button.
     */
    public void toolsManagerButton() {
        toolInstanceManager.getContext().getTools().invokeToolByName(
            "application.tools");
    }

    /**
     * Start screen designer button.
     */
    public void startScreenDesignerButton() {
        toolInstanceManager.getContext().getTools().invokeToolByName(
            "application.start.designer");
    }

    /**
     * Semi custom view designer button.
     */
    public void semiCustomViewDesignerButton() {
        toolInstanceManager.getContext().getTools().invokeToolByName(
            "scv.designer");
    }

    /**
     * Help button.
     */
    public void helpButton() {
        toolInstanceManager.processEvent(HELP);
    }

    /**
     * Blog button.
     */
    public void blogButton() {
        try {
            Desktop.getDesktop().browse(
                new URI(
                    "http://www.mymaps.gov.au/blogs/xmet/"));
        } catch (final IOException e) {
            Reporting.reportUnexpected(e);
        } catch (final URISyntaxException e) {
            Reporting.reportUnexpected(e);
        }
    }

    /**
     * About button.
     */
    public void aboutButton() {
        (new SubstanceToolInstanceManagerAboutDialog())
            .showAboutDialog(toolInstanceManager.getContext());
    }

    /**
     * Next page button.
     */
    public void nextPageButton() {
        toolInstanceManager.processEvent(NEXT_PAGE);
    }

    /**
     * Prev page button.
     */
    public void prevPageButton() {
        toolInstanceManager.processEvent(PREVIOUS_PAGE);
    }

    /**
     * Next tool instance button.
     */
    public void nextToolInstanceButton() {
        toolInstanceManager.processEvent(NEXT_TOOL_INSTANCE);
    }

    /**
     * Prev tool instance button.
     */
    public void prevToolInstanceButton() {
        toolInstanceManager.processEvent(PREVIOUS_TOOL_INSTANCE);
    }

    /**
     * Close tool instance button.
     */
    public void closeToolInstanceButton() {
        toolInstanceManager.processEvent(CLOSE_TOOL_INSTANCE);
    }

    /* == KeyEventDispatcher implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchKeyEvent(
        final KeyEvent e) {
        final boolean discardEvent = false;

        switch (e.getID()) {
        case KeyEvent.KEY_PRESSED:
            if ((e.getKeyChar() == Event.TAB)
                && (e.getModifiers() == Event.CTRL_MASK)) {
                nextPageButton();
            } else if ((e.getKeyChar() == Event.TAB)
                && (e.getModifiers() == (Event.CTRL_MASK | Event.SHIFT_MASK))) {
                prevPageButton();
            } else if ((e.getKeyChar() == Event.TAB)
                && (e.getModifiers() == (Event.CTRL_MASK | Event.ALT_MASK))) {
                nextToolInstanceButton();
            } else if ((e.getKeyChar() == Event.TAB)
                && (e.getModifiers() == (Event.CTRL_MASK
                    | Event.ALT_MASK | Event.SHIFT_MASK))) {
                prevToolInstanceButton();
            } else if ((e.getKeyCode() == KeyEvent.VK_W)
                && (e.getModifiers() == (Event.CTRL_MASK))) {
                closeToolInstanceButton();
            } else if ((e.getKeyCode() == KeyEvent.VK_E)
                && (e.getModifiers() == (Event.CTRL_MASK))) {
                closeButton();
            } else if ((e.getKeyCode() == KeyEvent.VK_Q)
                && (e.getModifiers() == (Event.CTRL_MASK))) {
                onClose();
            } else if ((e.getKeyCode() == KeyEvent.VK_S)
                && (e.getModifiers() == (Event.CTRL_MASK))) {
                saveButton();
            } else if ((e.getKeyCode() == KeyEvent.VK_O)
                && (e.getModifiers() == (Event.CTRL_MASK))) {
                openButton();
            } else if ((e.getKeyCode() == KeyEvent.VK_S)
                && (e.getModifiers() == (Event.CTRL_MASK | Event.SHIFT_MASK))) {
                saveAsButton();
            } else if ((e.getKeyCode() == KeyEvent.VK_N)
                && (e.getModifiers() == (Event.CTRL_MASK))) {
                newButton();
            } else if (e.getKeyCode() == KeyEvent.VK_F1) {
                helpButton();
            }
            // else {
            // /* Reporting.log("" + e.getKeyCode()); */
            // }
            break;
        case KeyEvent.KEY_TYPED:
        default:
            break;
        }

        return discardEvent;
    }

    /**
     * Update gui.
     * @param ti the ti
     */
    public void updateGUI(
        final ToolInstance ti) {
        rebuildMainMenubar(ti);
    }
}
