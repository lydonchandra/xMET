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
package xmet.tools.metadata.manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import n.reporting.Reporting;
import n.ui.CachableTreeModel;
import n.ui.CachingTreeModel;
import n.ui.GenericSelectionPaneListener;
import n.ui.JOptionPaneUtils;
import n.ui.SwingUtils;
import n.ui.patterns.callback.Callback;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.utils.PreferencesUtil;

/**
 * This is the main component (the metadata file tree and the plugin selection
 * drop down list) of the metadata manager seperated for re-use.
 * @author Nahid Akbar
 */
public class MetadataManagementViewingPanel
    implements
    CachableTreeModel,
    TreeSelectionListener,
    ComboBoxModel {

    /** The panel. */
    private final JPanel panel = new JPanel();

    /** link to client context. */
    private ClientContext client;

    /** The mgmt. */
    private MetadataManagementTool mgmt;

    /** The plugins combo box. */
    private JComboBox pluginsComboBox;

    /** The files tree. */
    private JTree filesTree;

    /** The search text field. */
    // CHECKSTYLE OFF: MagicNumber
    private final JTextField searchTextField = new JTextField(
        10);
    // CHECKSTYLE ON: MagicNumber
    {
        searchTextField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                searchButtonCallback();
            }
        });
    }

    /** The selection listener. */
    private GenericSelectionPaneListener selectionListener;

    /** The show preview pane check box. */
    private JCheckBox showPreviewPaneCheckBox;

    /** The show preview pane check box action listener. */
    private ActionListener showPreviewPaneCheckBoxActionListener;

    /** The renderer. */
    private MMVPTCR renderer;

    /** The mpc. */
    private MetadataPreviewControl mpc;

    /**
     * Instantiates a new metadata management viewing panel.
     * @param aClient the client
     */
    public MetadataManagementViewingPanel(
        final ClientContext aClient) {
        this.client = aClient;
        renderer = new DefaultMMVPTCR(
            aClient);

        mgmt = (MetadataManagementTool) aClient.getTools().findTool(
            MetadataManagementTool.TOOL_NAME);

        panel.setLayout(new BorderLayout());
        pluginsComboBox = new JComboBox(
            this);
        /* pluginsComboBox.addActionListener(this); */
        pluginsComboBox.setRenderer(new ListCellRenderer() {

            private final JLabel lbl = new JLabel();

            @Override
            public Component getListCellRendererComponent(
                final JList list,
                final Object value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {
                if (value instanceof MMGMTPlugin) {
                    /* Reporting.log("%1$s", value); */
                    lbl.setText(((MMGMTPlugin) value).getName());
                } else {
                    lbl.setText(String.format(
                        "%1$s",
                        value));
                }
                if (isSelected) {
                    lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                } else {
                    lbl.setBorder(BorderFactory.createEmptyBorder());
                }
                return lbl;
            }
        });
        // {
        showPreviewPaneCheckBox = new JCheckBox(
            "Show Preview Pane",
            MetadataManagementViewingPanel.getShowPreviewPane());
        showPreviewPaneCheckBoxActionListener = new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent arg0) {
                MetadataManagementViewingPanel
                    .setShowPreviewPane(showPreviewPaneCheckBox.isSelected());
                refresh();
            }
        };
        showPreviewPaneCheckBox
            .addActionListener(showPreviewPaneCheckBoxActionListener);
        // }
        // {
        mpc = new MetadataPreviewControl(
            null,
            null,
            null);
        // }
        refresh();
    }

    /**
     * rebuilds the panel.
     */
    public void refresh() {
        // CHECKSTYLE OFF: MagicNumber
        pluginsComboBox.setModel(new DefaultComboBoxModel());
        pluginsComboBox.setModel(this);
        JPanel topPanel = null;
        // {
        /* rebuild setup panel */
        topPanel = SwingUtils.BoxLayouts.getHorizontalPanel();
        topPanel.add(new JLabel(
            "Location: "));
        topPanel.add(pluginsComboBox);
        final Object[] params = {};
        topPanel.add(SwingUtils.BUTTON.getNew(
            "Close Location",
            new ClassMethodCallback(
                this,
                "closePluginButtonCallback",
                params)));
        final Object[] params1 = {};
        topPanel.add(SwingUtils.BUTTON.getNew(
            "Refresh Location",
            new ClassMethodCallback(
                this,
                "clearButtonCallback",
                params1)));
        final Object[] params2 = {};
        topPanel.add(SwingUtils.BUTTON.getNew(
            "New Location",
            new ClassMethodCallback(
                this,
                "addPluginButtonCallback",
                params2)));
        if (mgmt.getSelectedPlugin() instanceof FilterableMMGMTPlugin) {
            topPanel.add(new JLabel(
                " Search:"));
            topPanel.add(searchTextField);
            final Object[] params3 = {};
            topPanel.add(SwingUtils.BUTTON.getNew(
                getContext().getResources().getImageIconResourceResize(
                    "images/mm.icon.search.png",
                    16,
                    16),
                new ClassMethodCallback(
                    this,
                    "searchButtonCallback",
                    params3)));
        }
        // ...
        topPanel.add(Box.createGlue());
        // }
        JPanel bottomPanel = null;
        // {
        bottomPanel = SwingUtils.BoxLayouts.getHorizontalPanel();
        bottomPanel.add(showPreviewPaneCheckBox);
        topPanel.add(Box.createGlue());
        showPreviewPaneCheckBox
            .removeActionListener(showPreviewPaneCheckBoxActionListener);
        showPreviewPaneCheckBox.setSelected(MetadataManagementViewingPanel
            .getShowPreviewPane());
        showPreviewPaneCheckBox
            .addActionListener(showPreviewPaneCheckBoxActionListener);
        // }
        // {
        panel.removeAll();
        if (!MetadataManagementViewingPanel.getShowPreviewPane()) {
            filesTree = new JTree();
            panel.add(new JScrollPane(
                filesTree));
        } else {
            final JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                true);
            panel.add(splitPane);
            filesTree = new JTree();
            splitPane.setTopComponent(new JScrollPane(
                filesTree));
            splitPane.setBottomComponent(mpc);
            splitPane.setDividerSize(3);
            splitPane.setDividerLocation(0.5);
            splitPane.setResizeWeight(0.5);
            mpc.setPreferredSize(filesTree.getPreferredSize());
            /* splitPane.resetToPreferredSizes(); */
        }
        filesTree.setModel(new CachingTreeModel(
            filesTree,
            this));
        filesTree.setEditable(false);
        renderer.installCellRenderer(this);
        filesTree.addTreeSelectionListener(this);
        SwingUtils.MOUSE.onDoubleClick(
            filesTree,
            new Callback() {

                @Override
                public void callback() {
                    if (selectionListener != null) {
                        selectionListener
                            .selectionMade(MetadataManagementViewingPanel.this);
                    }
                }
            });

        panel.add(
            topPanel,
            BorderLayout.NORTH);

        panel.add(
            bottomPanel,
            BorderLayout.SOUTH);

        // }
        panel.validate();
        // CHECKSTYLE ON: MagicNumber
    }

    // --------------------------------------
    // -- CachingTreeModelHelper Implementation
    /* for JTree to get children - this way, method calls to the plugin is */
    /* reduced */
    // --------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRoot() {
        return mgmt.getSelectedPlugin().getRoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getChildren(
        final Object parent) {
        return ((MetadataFile) parent).getChildren();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf(
        final Object node) {
        return !((MetadataFile) node).isFolder();
    }

    /* == Button Callbacks == */

    /**
     * Search button callback.
     */
    public void searchButtonCallback() {
        if (mgmt.getSelectedPlugin() instanceof FilterableMMGMTPlugin) {
            final String text = searchTextField.getText().trim();
            if (text.length() > 0) {
                if (((FilterableMMGMTPlugin) mgmt.getSelectedPlugin())
                    .filterMetadata(
                        text,
                        false)) {
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "No results found");
                }
            } else {
                ((FilterableMMGMTPlugin) mgmt.getSelectedPlugin())
                    .resetMetadataFilter();
                refresh();
            }
        }
    }

    /**
     * Clear button callback.
     */
    public void clearButtonCallback() {
        if (mgmt.getSelectedPlugin() instanceof FilterableMMGMTPlugin) {
            ((FilterableMMGMTPlugin) mgmt.getSelectedPlugin())
                .resetMetadataFilter();
        }
        refresh();
    }

    /**
     * Adds the plugin button callback.
     */
    public void addPluginButtonCallback() {

        final MMGMTPluginWrapper[] pluginWrappers =
            getContext()
                .getServices()
                .<MMGMTPluginWrapper>getServiceProviderList(
                    MMGMTPluginWrapper.class);

        if (pluginWrappers != null) {
            final ArrayList<String> labels = new ArrayList<String>();
            for (final MMGMTPluginWrapper wrapper : pluginWrappers) {
                labels.add(wrapper.getNameLabel());
            }
            final String choice = (String) JOptionPaneUtils.getUserChoice(
                "Please select a type of location to add",
                labels.toArray());
            if (choice != null) {
                for (final MMGMTPluginWrapper wrapper : pluginWrappers) {
                    if (wrapper.getNameLabel().equals(
                        choice)) {
                        final MMGMTPlugin instantiation =
                            wrapper.instantiateNewPlugin(getContext());
                        if (instantiation != null) {
                            mgmt.getPlugins().add(
                                instantiation);
                            mgmt.setSelectedPlugin(mgmt.getPlugins().get(
                                mgmt.getPlugins().size() - 1));
                        }
                        break;
                    }
                }
                refresh();
            }
        }
    }

    /**
     * Close plugin button callback.
     */
    public void closePluginButtonCallback() {
        if (getSelectedPlugin() != mgmt.getPlugins().get(
            0)) { /*
                   * dont close the
                   */
            /* first default */
            /* metadata */
            /* plugin */
            final ArrayList<MMGMTPlugin> plugins = mgmt.getPlugins();
            if (plugins.size() > 1) {
                final MMGMTPlugin toRemove = getSelectedPlugin();
                toRemove.onCloseCallback();
                plugins.remove(toRemove);
                mgmt.setSelectedPlugin(plugins.get(0));
                refresh();
            }
        } else {
            Reporting.reportUnexpected("This plugin can not be closed");
        }
    }

    /* == Misc helper methods == */

    /**
     * Gets the selected plugin.
     * @return the selected plugin
     */
    public MMGMTPlugin getSelectedPlugin() {
        return mgmt.getSelectedPlugin();
    }

    /**
     * Gets the selected or parent folder.
     * @return the selected or parent folder
     */
    public MetadataFile getSelectedOrParentFolder() {
        MetadataFile parent = null;
        if (filesTree.getSelectionCount() > 0) {
            parent =
                (MetadataFile) filesTree
                    .getSelectionPath()
                    .getLastPathComponent();
        }
        if (parent == null) {
            parent = mgmt.getSelectedPlugin().getRoot();
        }
        if (!parent.isFolder()) {
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * Gets the selected file.
     * @return the selected file
     */
    public MetadataFile getSelectedFile() {
        MetadataFile selected = null;
        if (filesTree.getSelectionCount() > 0) {
            selected =
                (MetadataFile) filesTree
                    .getSelectionPath()
                    .getLastPathComponent();
        }
        return selected;
    }

    /* == TreeSelectionListener == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(
        final TreeSelectionEvent ev) {
        if (MetadataManagementViewingPanel.getShowPreviewPane()) {
            try {
                if (mpc != null) {
                    final MetadataFile file = getSelectedFile();
                    if ((file != null)
                        && !file.isFolder()) {
                        final String profileName = file.getProfileName();
                        final Profile profile =
                            getContext().getProfiles().getProfileByKeyword(
                                profileName);
                        if (profile != null) {
                            final String varPreviewTemplateFile =
                                profile.getPreviewTemplateFile();
                            if ((varPreviewTemplateFile != null)
                                && (varPreviewTemplateFile.length() > 0)) {
                                mpc.update(
                                    file,
                                    getContext().getResources().getResourceURI(
                                        varPreviewTemplateFile),
                                    getContext()
                                        .getProfiles()
                                        .getDataCodecByFileName(
                                            file.getName()));
                            } else {
                                mpc.update("Profile does not"
                                    + " have a configured preview template");
                            }
                        } else {
                            mpc.update(
                                file,
                                getContext().getResources().getResourceURI(
                                    "profiles/default/missing-preview.xsl"),
                                getContext()
                                    .getProfiles()
                                    .getDataCodecByFileName(
                                        file.getName()));
                        }
                    }
                }
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        }
        if (selectionListener != null) {
            selectionListener.selectionChanged(this);
        }
    }

    /**
     * Sets the selection listener - only one supported.
     * @param aSelectionListener the new selection listener
     */
    public void setSelectionListener(
        final GenericSelectionPaneListener aSelectionListener) {
        this.selectionListener = aSelectionListener;
    }

    /**
     * Gets the files tree for wrapper processing.
     * @return the files tree
     */
    public JTree getFilesTree() {
        return filesTree;
    }

    /**
     * Returns the display panel.
     * @return display panel
     */
    public JPanel getPanel() {
        return panel;
    }

    // ------------------------------------------
    // -- ComboBoxModel Implementation
    /* for the plugins combo box */
    // ------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void addListDataListener(
        final ListDataListener l) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListDataListener(
        final ListDataListener l) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getElementAt(
        final int index) {
        return mgmt.getPlugins().get(
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getSelectedItem() {
        return mgmt.getSelectedPlugin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedItem(
        final Object anItem) {
        mgmt.setSelectedPlugin((MMGMTPlugin) anItem);
        refresh();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return mgmt.getPlugins().size();
    }

    /**
     * Sets the renderer.
     * @param aRenderer the new renderer
     */
    public void setRenderer(
        final MMVPTCR aRenderer) {
        this.renderer = aRenderer;
        refresh();
    }

    /* == Show Preview Pane == */
    /** The show preview pane flag. */
    private static Boolean showPreviewPaneFlag = null;

    /**
     * Gets the show preview pane.
     * @return the show preview pane
     */
    public static boolean getShowPreviewPane() {
        if (showPreviewPaneFlag == null) {
            final PreferencesUtil prefs = new PreferencesUtil(
                MetadataManagementTool.TOOL_NAME);
            showPreviewPaneFlag = prefs.getBoolean(
                "showPreviewPane",
                true);
        }
        return showPreviewPaneFlag;
    }

    /**
     * Sets the show preview pane.
     * @param spp the new show preview pane
     */
    public static void setShowPreviewPane(
        final boolean spp) {
        if (spp != MetadataManagementViewingPanel.showPreviewPaneFlag) {
            final PreferencesUtil prefs = new PreferencesUtil(
                MetadataManagementTool.TOOL_NAME);
            prefs.putBoolean(
                "showPreviewPane",
                spp);
            MetadataManagementViewingPanel.showPreviewPaneFlag = spp;
        }
    }

    /**
     * Gets the context.
     * @return the client
     */
    ClientContext getContext() {
        return client;
    }

}
