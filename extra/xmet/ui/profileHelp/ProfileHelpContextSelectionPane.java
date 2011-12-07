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
package xmet.ui.profileHelp;

import java.awt.BorderLayout;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import n.reporting.Reporting;
import n.ui.CachableTreeModel;
import n.ui.CachingTreeModel;
import n.ui.GenericSingleItemSelectionListener;
import xmet.profiles.Profile;
import xmet.resources.ResourceManager;
import xmet.utils.BusyScreenUtil;

/**
 * Selection used by ProfileHelpContextSelectionDialog for selecting a help
 * context id given a profile. Separated for possible re-use potential
 * @author Nahid Akbar
 */
public class ProfileHelpContextSelectionPane
    implements
    CachableTreeModel,
    TreeSelectionListener {

    /** The display panel. */
    private final JPanel panel = new JPanel();

    /**
     * Gets the panel.
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }

    /** The profile. */
    private Profile profile;

    /** list of help files under the profile. */
    private ArrayList<HelpFile> files;

    /** The selection tree. */
    private final JTree selectionTree;

    /**
     * Instantiates a new profile help context selection pane.
     * @param aProfile the profile
     * @param resources the resources
     */
    @SuppressWarnings("serial")
    public ProfileHelpContextSelectionPane(
        final Profile aProfile,
        final ResourceManager resources) {
        this.profile = aProfile;
        try {
            BusyScreenUtil.startBusy("Indexing Profile HELP Files");
            reindexProfileHelpInfo(resources);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        } finally {
            BusyScreenUtil.endBusy();
        }

        /* reindexProfileHelpInfo(resources); */
        selectionTree = new JTree() {

            @Override
            protected void setExpandedState(
                final TreePath path,
                final boolean state) {
                super.setExpandedState(
                    path,
                    true);
            }
        };
        selectionTree.setRootVisible(false);
        selectionTree.setModel(new CachingTreeModel(
            selectionTree,
            this));
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(
            selectionTree));
        selectionTree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
        selectionTree.addTreeSelectionListener(this);
        for (int i = 0; i < selectionTree.getRowCount(); i++) {
            selectionTree.expandRow(i);
        }

    }

    /* == TreeSelectionListener implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(
        final TreeSelectionEvent arg0) {
        final TreePath tp = selectionTree.getSelectionPath();
        final ArrayList<GenericSingleItemSelectionListener> listeners =
            selListeners;
        if (tp == null) {
            for (final GenericSingleItemSelectionListener l : listeners) {
                l.selectionChanged(
                    this,
                    null,
                    -1);
            }
        } else {
            final Object item = tp.getLastPathComponent();
            final int index = selectionTree.getRowForPath(tp);
            for (final GenericSingleItemSelectionListener l : listeners) {
                l.selectionChanged(
                    this,
                    item,
                    index);
            }
        }
    }

    /**
     * Converts the selected tree path into help context id for reference.
     * @return the selected path
     */
    public String getSelectedPath() {
        TreePath tp = selectionTree.getSelectionPath();
        final Stack<TreePath> path = new Stack<TreePath>();
        while (tp != null) {
            path.push(tp);
            tp = tp.getParentPath();
        }
        if (path.size() > 0) {
            final StringBuilder sb = new StringBuilder();
            while (!path.empty()) {
                tp = path.pop();
                final Object o = tp.getLastPathComponent();
                if (o == this) {
                    continue;
                } else if ((o.getClass() == HelpFile.class)
                    || (o.getClass() == HelpFileSection.class)) {
                    sb.append(o.toString());
                } else {
                    Reporting.logUnexpected();
                }
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    /* == Selection Listeners == */

    /** The selection listeners. */
    private final ArrayList<GenericSingleItemSelectionListener> selListeners =
        new ArrayList<GenericSingleItemSelectionListener>();

    /**
     * Adds the selection listener.
     * @param listener the listener
     */
    public void addSelectionListener(
        final GenericSingleItemSelectionListener listener) {
        selListeners.add(listener);
    }

    /**
     * Removes the selection listener.
     * @param listener the listener
     */
    public void removeSelectionListener(
        final GenericSingleItemSelectionListener listener) {
        selListeners.remove(listener);
    }

    /* == CachingTreeModelHelper implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRoot() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getChildren(
        final Object parent) {
        if (parent == this) {
            return files.toArray();
        }
        if (parent.getClass() == HelpFile.class) {
            return ((HelpFile) parent).sections.toArray();
        }
        return new Object[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf(
        final Object node) {
        return node.getClass() == HelpFileSection.class;
    }

    /* == Indexing and shit == */
    /**
     * This method indexes the html files in the profile pointed by the profile
     * field's help directory.
     * @param resources the resources
     */
    private void reindexProfileHelpInfo(
        final ResourceManager resources) {
        files = new ArrayList<ProfileHelpContextSelectionPane.HelpFile>();
        if (profile.getHelpDirPath() != null) {
            final File[] helpDirs =
                resources.getFoldersList(profile.getHelpDirPath());

            for (final File helpDir : helpDirs) {
                indexFolder(
                    resources,
                    helpDir,
                    profile.getHelpDirPath()
                        + "/");
            }
        }
    }

    /**
     * Index folder.
     * @param resources the resources
     * @param helpDir the help dir
     * @param prefixDir the prefix dir
     */
    private void indexFolder(
        final ResourceManager resources,
        final File helpDir,
        final String prefixDir) {
        /*
         * BusyScreenUtil.tickBusy("Indexing " + prefixDir + helpDir.getName());
         */
        BusyScreenUtil.tickBusy();
        final File[] helpFileNames = helpDir.listFiles();
        for (final File helpFile : helpFileNames) {
            final String name = helpFile.getName().toLowerCase();
            if (!name.startsWith(".")) {
                if (helpFile.isFile()) {
                    if (name.endsWith(".htm")
                        || name.endsWith(".html")) {
                        files.add(new HelpFile(
                            prefixDir
                                + helpFile.getName(),
                            resources));
                    }
                } else if (helpFile.isDirectory()) {
                    indexFolder(
                        resources,
                        helpFile,
                        prefixDir
                            + helpFile.getName()
                            + "/");
                }
            }
        }
    }

    /**
     * Represents a html file.
     */
    static class HelpFile {

        /** url of the file. */
        private final String url;

        /** The sections. */
        private final ArrayList<HelpFileSection> sections =
            new ArrayList<ProfileHelpContextSelectionPane.HelpFileSection>();

        /**
         * Instantiates a new help file.
         * @param aUrl the url
         * @param resources the resources
         */
        public HelpFile(
            final String aUrl,
            final ResourceManager resources) {
            this.url = aUrl;
            final ByteBuffer contents = resources.getResourceContents(aUrl);
            if (contents != null) {
                final String cnt = new String(
                    contents.array());
                final String[] matches = cnt.split("a name=");
                for (final String match : matches) {
                    if (match.startsWith("\"")
                        && (match.indexOf(
                            '"',
                            2) != -1)) {
                        final String name = match.substring(
                            1,
                            match.indexOf(
                                '"',
                                1));
                        Reporting.logExpected(name);
                        sections.add(new HelpFileSection(
                            name));
                    }
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return url;
        }
    }

    /**
     * Represents a named anchor in the html file.
     */
    static class HelpFileSection {

        /** name of the anchor. */
        private String sectionName;

        /**
         * Instantiates a new help file section.
         * @param name the name
         */
        public HelpFileSection(
            final String name) {
            setSectionName("#"
                + name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return getSectionName();
        }

        /**
         * Gets the name of the anchor.
         * @return the name of the anchor
         */
        public String getSectionName() {
            return sectionName;
        }

        /**
         * Sets the name of the anchor.
         * @param aSectionName the new name of the anchor
         */
        public void setSectionName(
            final String aSectionName) {
            sectionName = aSectionName;
        }

    }

    /**
     * Gets the profile.
     * @return the profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the profile.
     * @param aProfile the new profile
     */
    public void setProfile(
        final Profile aProfile) {
        profile = aProfile;
    }

    /**
     * Gets the list of help files under the profile.
     * @return the list of help files under the profile
     */
    public ArrayList<HelpFile> getFiles() {
        return files;
    }

    /**
     * Sets the list of help files under the profile.
     * @param aFiles the new list of help files under the profile
     */
    public void setFiles(
        final ArrayList<HelpFile> aFiles) {
        files = aFiles;
    }

}
