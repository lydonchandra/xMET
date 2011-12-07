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
package xmet.tools.screen;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import n.io.bin.Files;
import n.io.xml.CSXMLSerializationCodec;
import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import xmet.tools.DefaultTool;
import xmet.tools.Tool;
import xmet.tools.ToolInstance;

/**
 * The main SS tool which loads Start Screens.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class StartScreenTool
    extends DefaultTool {

    /** The default start screen url. */
    public static final String DEFAULT_START_SCREEN_PATH = "config/start.xml";

    /** The Constant APPLICATION_START. */
    public static final String APPLICATION_START = "application.start";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return APPLICATION_START;
    }

    /** The instances map. */
    private Map<String, Instance> instancesMap =
        new HashMap<String, Instance>();

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolInstance invoke(
        final Map<String, Object> params) {
        StartScreen screen = (StartScreen) params.get("screen");
        if (screen == null) {
            final String screenFileName = (String) params.get("screenFileName");
            if (screenFileName == null) {
                final String screenXml = (String) params.get("screenXml");
                if (screenXml != null) {
                    screen = fromXML(screenXml);
                }
            } else {
                screen = fromURI(getContext().getResources().getResourceURI(
                    screenFileName));
            }
        }
        if (screen == null) {
            screen = fromURI(getContext().getResources().getResourceURI(
                getContext().getConfig().getTempSetting(
                    "start",
                    DEFAULT_START_SCREEN_PATH)));
        }
        if (screen == null) {
            return null;
        }

        Instance instance = instancesMap.get(screen.toString());
        if ((instance == null)
            || !screen.isSticky()) {
            instance = new Instance(
                screen);
        }
        if (screen.isSticky()) {
            instancesMap.put(
                screen.toString(),
                instance);
        }
        return instance;
    }

    /**
     * The tool instance.
     */
    private class Instance
        implements
        ToolInstance {

        /** The screen. */
        private final StartScreen screen;

        /** The panel. */
        private JPanel panel;

        /**
         * Instantiates a new instance.
         * @param aScreen the screen
         */
        public Instance(
            final StartScreen aScreen) {
            this.screen = aScreen;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Tool getTool() {
            return StartScreenTool.this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle() {
            return screen.getTitle();
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
            // CHECKSTYLE OFF: MagicNumber

            panel = GridBag.getNew();

            panel.setBorder(BorderFactory.createEmptyBorder(
                5,
                5,
                5,
                5));

            if (screen.getScreenTitle() != null) {
                SwingUtils.GridBag.add(
                    panel,
                    new JLabel(
                        "<html><h1>"
                            + screen.getScreenTitle()),
                    "w=rem;wx=1.0;f=h");
            }

            int maxRow = 0;
            if (screen.getItems() != null) {
                if (screen.getItems().size() != 0) {
                    if (screen.getItems().get(
                        0).getRow() == -1) {
                        int row = 1;
                        for (final StartScreenItem ssi : screen.getItems()) {
                            ssi.setRow(++row);
                            ssi.setColumn(1);
                            ssi.setWidth(1);
                            ssi.setScale(1.0);
                            ssi.setHeight(1);
                        }
                    }
                }

                for (final StartScreenItem ssi : screen.getItems()) {
                    if (ssi.isDevModeOnly()
                        && !(getContext().getConfig().getTempSetting(
                            "admin",
                            null) != null)) {
                        continue;
                    }
                    try {
                        Component displayComponent = null;

                        if (ssi instanceof LogoStartScreenItem) {
                            displayComponent = new LogoSSIDC(
                                (LogoStartScreenItem) ssi);
                        } else if (ssi instanceof NewMetadataStartScreenItem) {
                            displayComponent = new NewMetadataSSIDC(
                                (NewMetadataStartScreenItem) ssi);
                        } else if (ssi instanceof StartScreenLinkItem) {
                            displayComponent = new StartScreenLinkSSIDC(
                                (StartScreenLinkItem) ssi);
                        } else if (ssi instanceof ToolLinkStartScreenItem) {
                            displayComponent = new ToolLinkSSIDC(
                                (ToolLinkStartScreenItem) ssi);
                        } else if (ssi instanceof TextStartScreenItem) {
                            displayComponent = new TextSSIDC(
                                (TextStartScreenItem) ssi);
                        } else {
                            Reporting.logUnexpected(
                                "unhandled start screen item type %1$s",
                                ssi.getClass().getName());
                        }

                        if (displayComponent != null) {
                            SwingUtils.GridBag.add(
                                panel,
                                displayComponent,
                                "x=%1$d;y=%2$d;wx=%3$f;w=%4$d;"
                                    + "h=%5$d;wy=%6$f;f=b;i=5,0,5,0",
                                ssi.getColumn(),
                                ssi.getRow(),
                                ssi.getScale(),
                                ssi.getWidth(),
                                ssi.getHeight(),
                                ssi.getVscale());
                        }
                        if (maxRow < ssi.getRow()) {
                            maxRow = ssi.getRow();
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            SwingUtils.GridBag.add(
                panel,
                Box.createGlue(),
                "y=%1$d;w=rem;wy=1.0;f=b",
                maxRow + 1);
            // CHECKSTYLE ON: MagicNumber
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
            return !screen.isSticky()
                || force;
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
            final int applicationEvent) {

        }

        /**
         * Display context for Logos.
         */
        private class LogoSSIDC
            extends JButton
            implements
            ActionListener {

            /**
             *
             */
            private static final long serialVersionUID = 1L;
            /**
             * The link.
             */
            private final String link;

            /**
             * Instantiates a new logo ssidc.
             * @param lssi the lssi
             */
            public LogoSSIDC(
                final LogoStartScreenItem lssi) {
                super((ImageIcon) null);
                ImageIcon varImageIcon = null;
                if (((lssi.getLogoHeight() > 0) && (lssi.getLogoWidth() > 0))) {
                    varImageIcon =
                        getContext().getResources().getImageIconResourceResize(
                            lssi.getLogoUrl(),
                            lssi.getLogoWidth(),
                            lssi.getLogoHeight());
                } else {
                    varImageIcon =
                        getContext().getResources().getImageIconResource(
                            lssi.getLogoUrl());
                }

                super.setIcon(varImageIcon);
                setLayout(new BorderLayout());
                setAlignmentX(SwingConstants.LEFT);
                setCursor(new Cursor(
                    Cursor.HAND_CURSOR));
                setBorder(new LineBorder(
                    UIManager.getDefaults().getColor(
                        "Button.background"),
                    1));
                setContentAreaFilled(false);
                setOpaque(false);
                link = lssi.getLinkUrl();
                addActionListener(this);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                try {
                    Desktop.getDesktop().browse(
                        new URI(
                            link));
                } catch (final Exception e) {
                    Reporting.reportUnexpected(e);
                }
            }
        }

        /**
         * Display context for Nameds.
         */
        private class NamedSSIDC
            extends JButton
            implements
            ActionListener {

            /**
             * The tool name.
             * @param nssi the nssi
             */
            /* String toolName; */

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            /**
             * Instantiates a new named ssidc.
             * @param nssi the nssi
             */
            public NamedSSIDC(
                final NamedStartScreenItem nssi) {
                super("<html><h2>"
                    + nssi.getTitle()
                    + "<p>"
                    + nssi.getDescription(), getContext()
                    .getResources()
                    .getImageIconResource(
                        nssi.getIconURL()));
                setHorizontalAlignment(SwingConstants.LEFT);
                addActionListener(this);
                setCursor(new Cursor(
                    Cursor.HAND_CURSOR));
                /* setBorder(null); */
                setBorder(new LineBorder(
                    UIManager.getDefaults().getColor(
                        "Button.background"),
                    1));
                setContentAreaFilled(false);
                setOpaque(false);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void actionPerformed(
                final ActionEvent arg0) {

            }
        }

        /**
         * Display context for Nameds.
         */
        private class TextSSIDC
            extends JLabel
            implements
            ActionListener {

            /**
             * The tool name.
             * @param tssi the tssi
             */
            /* String toolName; */

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            /**
             * Instantiates a new named ssidc.
             * @param tssi the nssi
             */
            public TextSSIDC(
                final TextStartScreenItem tssi) {
                super("<html><font style=\" "
                    + tssi.getFontStyle()
                    + " \">"
                    + tssi.getText());
                setHorizontalAlignment(SwingConstants.LEFT);
                /* setCursor(new Cursor(Cursor.HAND_CURSOR)); */
                /* setBorder(null); */
                /* setBorder(new LineBorder(UIManager.getDefaults().getColor( */
                // "Button.background"), 1));
                setOpaque(false);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void actionPerformed(
                final ActionEvent arg0) {

            }
        }

        /**
         * Display context for ToolLinks.
         */
        private class ToolLinkSSIDC
            extends xmet.tools.screen.StartScreenTool.Instance.NamedSSIDC {

            /**
             *
             */
            private static final long serialVersionUID = 1L;
            /**
             * The tool name.
             */
            private String toolName;

            /**
             * Instantiates a new tool link ssidc.
             * @param tlssi the tlssi
             */
            public ToolLinkSSIDC(
                final ToolLinkStartScreenItem tlssi) {
                super(tlssi);
                setToolName(tlssi.getToolName());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                getContext().getTools().invokeToolByName(
                    getToolName());
            }

            /**
             * Gets the tool name.
             * @return the tool name
             */
            public String getToolName() {
                return toolName;
            }

            /**
             * Sets the tool name.
             * @param aToolName the new tool name
             */
            public void setToolName(
                final String aToolName) {
                toolName = aToolName;
            }

        }

        /**
         * Display context for NewMetadatas.
         */
        private class NewMetadataSSIDC
            extends xmet.tools.screen.StartScreenTool.Instance.ToolLinkSSIDC {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            /**
             * The profile.
             */
            private String profile;

            /**
             * The stylesheet.
             */
            private String stylesheet;

            /**
             * Instantiates a new new metadata ssidc.
             * @param nmssi the nmssi
             */
            public NewMetadataSSIDC(
                final NewMetadataStartScreenItem nmssi) {
                super(nmssi);
                setProfile(nmssi.getProfileName());
                setStylesheet(nmssi.getProfileSheet());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                getContext().getTools().invokeToolByName(
                    getToolName(),
                    "profile",
                    getContext().getProfiles().getProfileByName(
                        getProfile()),
                    "editorSheetName",
                    getStylesheet());
            }

            /**
             * Gets the profile.
             * @return the profile
             */
            public String getProfile() {
                return profile;
            }

            /**
             * Sets the profile.
             * @param aProfile the new profile
             */
            public void setProfile(
                final String aProfile) {
                profile = aProfile;
            }

            /**
             * Gets the stylesheet.
             * @return the stylesheet
             */
            public String getStylesheet() {
                return stylesheet;
            }

            /**
             * Sets the stylesheet.
             * @param aStylesheet the new stylesheet
             */
            public void setStylesheet(
                final String aStylesheet) {
                stylesheet = aStylesheet;
            }
        }

        /**
         * Display context for StartScreenLinks.
         */
        private class StartScreenLinkSSIDC
            extends xmet.tools.screen.StartScreenTool.Instance.ToolLinkSSIDC {

            /**
             *
             */
            private static final long serialVersionUID = 1L;
            /** The name. */
            private String name;

            /**
             * Instantiates a new start screen link ssidc.
             * @param nmssi the nmssi
             */
            public StartScreenLinkSSIDC(
                final StartScreenLinkItem nmssi) {
                super(nmssi);
                setName(nmssi.getScreenFileName());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                getContext().getTools().invokeToolByName(
                    getToolName(),
                    "screenFileName",
                    getName());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String getName() {
                return name;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void setName(
                final String aName) {
                name = aName;
            }
        }

    }

    /**
     * From uri.
     * @param resourceURI the resource uri
     * @return the start screen
     */
    private StartScreen fromURI(
        final URI resourceURI) {
        try {
            return fromFile(new File(
                resourceURI));
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
            return null;
        }
    }

    /**
     * From file.
     * @param file the file
     * @return the start screen
     */
    private StartScreen fromFile(
        final File file) {
        ByteBuffer contents = null;
        try {
            contents = Files.read(file);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
        if (contents != null) {
            return fromXML(new String(
                contents.array()));
        }
        return null;
    }

    /**
     * The Constant StartScreenClasses.
     */
    private static final Class[] START_SCREEN_CLASSES = {
    StartScreen.class,
    StartScreenItem.class,
    LogoStartScreenItem.class,
    NewMetadataStartScreenItem.class,
    ToolLinkStartScreenItem.class,
    StartScreenLinkItem.class,
    TextStartScreenItem.class
    };

    /**
     * From xml.
     * @param screenXml the screen xml
     * @return the start screen
     */
    private StartScreen fromXML(
        final String screenXml) {
        final CSXMLSerializationCodec decoder = new CSXMLSerializationCodec();
        return (StartScreen) decoder.decodeClasses(
            screenXml,
            getStartScreenClasses());
    }

    /**
     * Gets the default start screen url.
     * @return the default start screen url
     */
    public static String getDefaultStartScreenUrl() {
        return DEFAULT_START_SCREEN_PATH;
    }

    /**
     * Gets the instances map.
     * @return the instances map
     */
    public Map<String, Instance> getInstancesMap() {
        return instancesMap;
    }

    /**
     * Sets the instances map.
     * @param aInstancesMap the new instances map
     */
    public void setInstancesMap(
        final Map<String, Instance> aInstancesMap) {
        instancesMap = aInstancesMap;
    }

    /**
     * Gets the default start screen path.
     * @return the default start screen path
     */
    protected static String getDefaultStartScreenPath() {
        return DEFAULT_START_SCREEN_PATH;
    }

    /**
     * Gets the application start.
     * @return the application start
     */
    protected static String getApplicationStart() {
        return APPLICATION_START;
    }

    /**
     * Gets the start screen classes.
     * @return the start screen classes
     */

    protected static Class[] getStartScreenClasses() {
        return START_SCREEN_CLASSES;
    }

}
