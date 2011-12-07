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
package xmet.tools.application;

import java.util.Map;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import n.io.net.ProxyMode;
import n.ui.SwingUtils;
import n.ui.patterns.callback.Callback;
import n.ui.patterns.propertySheet.BooleanPSE;
import n.ui.patterns.propertySheet.FolderSelectionPSE;
import n.ui.patterns.propertySheet.IntegerPSE;
import n.ui.patterns.propertySheet.MaskedStringPSE;
import n.ui.patterns.propertySheet.ObjectChoicePSE;
import n.ui.patterns.propertySheet.PropertySheetItem;
import n.ui.patterns.propertySheet.StringPSE;
import xmet.tools.DefaultTool;
import xmet.tools.Tool;
import xmet.tools.ToolInstance;

/**
 * Tool for allowing configuration editing.
 * @author Nahid Akbar
 */
public class SettingsTool
    extends DefaultTool
    implements
    ToolInstance {

    /* == Tool Interface == */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "application.settings";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolInstance invoke(
        final Map<String, Object> params) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleInstance() {
        return true;
    }

    /* == ToolInstance Interface == */

    /** The panel. */
    private JPanel panel = null;

    /** The pane. */
    private JScrollPane pane;

    /** The local metadata directory. */
    private final FolderSelectionPSE localMetadataDirectory =
        new FolderSelectionPSE();

    /** The show toolbar labels. */
    private final BooleanPSE showToolbarLabels = new BooleanPSE();

    /** The show toolbar icons. */
    private final BooleanPSE showToolbarIcons = new BooleanPSE();

    /** The toolbar icon width. */
    private final IntegerPSE toolbarIconWidth = new IntegerPSE();

    /** The proxy mode. */
    private final ObjectChoicePSE proxyMode = new ObjectChoicePSE();

    /** The proxy address. */
    private final StringPSE proxyAddress = new StringPSE();

    /** The proxy port. */
    private final IntegerPSE proxyPort = new IntegerPSE();

    /** The proxy username. */
    private final StringPSE proxyUsername = new StringPSE();

    /** The proxy password. */
    private final MaskedStringPSE proxyPassword = new MaskedStringPSE();

    /** The proxy no proxy. */
    private final StringPSE proxyNoProxy = new StringPSE();

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getDisplayPanel() {
        revertSettings();
        return pane;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "Program Settings";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tool getTool() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onClose(
        final boolean force) {

        return true;
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
    public void onInstantiation() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEvent(
        final int applicationEvent) {

    }

    /* == Misc == */
    /**
     * Save settings.
     */
    public void saveSettings() {
        getContext().getConfig().setLocalMetadataDirectory(
            (String) localMetadataDirectory.getValue());
        getContext().getConfig().setShowToolbarIcons(
            (Boolean) showToolbarIcons.getValue());
        getContext().getConfig().setShowToolbarLabels(
            (Boolean) showToolbarLabels.getValue());
        getContext().getConfig().setToolbarIconWidth(
            (Integer) toolbarIconWidth.getValue());

        getContext().getConfig().getProxyInformation().setMode(
            (ProxyMode) proxyMode.getValue());
        getContext().getConfig().getProxyInformation().setHttpProxy(
            (String) proxyAddress.getValue());
        getContext().getConfig().getProxyInformation().setHttpPort(
            (Integer) proxyPort.getValue());
        getContext().getConfig().getProxyInformation().setHttpsProxy(
            (String) proxyAddress.getValue());
        getContext().getConfig().getProxyInformation().setHttpsPort(
            (Integer) proxyPort.getValue());
        getContext().getConfig().getProxyInformation().setFtpProxy(
            (String) proxyAddress.getValue());
        getContext().getConfig().getProxyInformation().setFtpPort(
            (Integer) proxyPort.getValue());
        getContext().getConfig().getProxyInformation().setUserName(
            (String) proxyUsername.getValue());
        getContext().getConfig().getProxyInformation().setPassword(
            (String) proxyPassword.getValue());

        getContext().getConfig().getProxyInformation().setNoProxyFor(
            ((String) proxyNoProxy.getValue()));
        getContext().getConfig().saveSettings();
        revertSettings();
    }

    /**
     * Revert settings.
     */
    // CHECKSTYLE OFF: MethodLength
    public void revertSettings() {
        if (panel != null) {
            panel.removeAll();
        } else {
            panel = SwingUtils.GridBag.getNew();
            pane = new JScrollPane(
                (panel));
        }
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h1>xMET Configuration"),
            "w=rem;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h2>Files And Folders"),
            "w=rem;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h3>Default Metadata Directory: "),
            "w=rel;");
        SwingUtils.GridBag.add(
            panel,
            localMetadataDirectory.getEditor(
                getContext().getConfig().getLocalMetadataDirectory(),
                new PropertySheetItem(
                    null,
                    null)),
            "w=rem;f=h;wx=0.8;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h2>Look And Feel"),
            "w=rem;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h3>Show Toolbar Labels: "),
            "w=rel;");
        SwingUtils.GridBag.add(
            panel,
            showToolbarLabels.getEditor(
                getContext().getConfig().isShowToolbarLabels(),
                new PropertySheetItem(
                    null,
                    null)),
            "w=rem;f=h;wx=0.8;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h3>Show Toolbar Icons: "),
            "w=rel;");
        SwingUtils.GridBag.add(
            panel,
            showToolbarIcons.getEditor(
                getContext().getConfig().isShowToolbarIcons(),
                new PropertySheetItem(
                    null,
                    null)),
            "w=rem;f=h;wx=0.8;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h3>Toolbar Icon Width: "),
            "w=rel;");
        SwingUtils.GridBag.add(
            panel,
            toolbarIconWidth.getEditor(
                getContext().getConfig().getToolbarIconWidth(),
                new PropertySheetItem(
                    null,
                    null)),
            "w=rem;f=h;wx=0.8;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h2>Proxy"),
            "w=rem;");

        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h3>Proxy Mode: "),
            "w=rel;");
        getContext().getConfig().getProxyInformation().getMode();
        proxyMode.setChoices(ProxyMode.values());
        SwingUtils.GridBag.add(
            panel,
            proxyMode.getEditor(
                getContext().getConfig().getProxyInformation().getMode(),
                new PropertySheetItem(
                    null,
                    null)),
            "w=rem;f=h;wx=0.8;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h3>Proxy Address: "),
            "w=rel;");
        SwingUtils.GridBag.add(
            panel,
            proxyAddress.getEditor(
                getContext().getConfig().getProxyInformation().getHttpProxy(),
                new PropertySheetItem(
                    null,
                    null)),
            "w=rem;f=h;wx=0.8;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h3>Proxy Port: "),
            "w=rel;");
        SwingUtils.GridBag.add(
            panel,
            proxyPort.getEditor(
                getContext().getConfig().getProxyInformation().getHttpPort(),
                new PropertySheetItem(
                    null,
                    null)),
            "w=rem;f=h;wx=0.8;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h3>Proxy Username: "),
            "w=rel;");
        SwingUtils.GridBag.add(
            panel,
            proxyUsername.getEditor(
                getContext().getConfig().getProxyInformation().getUserName(),
                new PropertySheetItem(
                    null,
                    null)),
            "w=rem;f=h;wx=0.8;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h3>Proxy Password: "),
            "w=rel;");
        SwingUtils.GridBag.add(
            panel,
            proxyPassword.getEditor(
                getContext().getConfig().getProxyInformation().getPassword(),
                new PropertySheetItem(
                    null,
                    null)),
            "w=rem;f=h;wx=0.8;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h3>Dont Use Proxy For: "),
            "w=rel;");
        SwingUtils.GridBag.add(
            panel,
            proxyNoProxy.getEditor(
                getContext().getConfig().getProxyInformation().getNoProxyFor(),
                new PropertySheetItem(
                    null,
                    null)),
            "w=rem;f=h;wx=0.8;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "<html><h4>Do not forget to save settings."
                    + " Some settings will only take effect after "
                    + "re-opening the application."),
            "w=rem;");
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNewH(
                "Save Settings",
                getContext().getResources().getImageIconResource(
                    "images/settings.icon.save.png"),
                new Callback() {

                    @Override
                    public void callback() {
                        saveSettings();
                    }
                }),
            "w=rem;");
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNewH(
                "Revert Settings",
                getContext().getResources().getImageIconResource(
                    "images/settings.icon.revert.png"),
                new Callback() {

                    @Override
                    public void callback() {
                        revertSettings();
                    }
                }),
            "w=rem;");
        SwingUtils.GridBag.add(
            panel,
            Box.createGlue(),
            "w=rem;f=b;wx=1;wy=1;");
    }
    // CHECKSTYLE ON: MethodLength
}
