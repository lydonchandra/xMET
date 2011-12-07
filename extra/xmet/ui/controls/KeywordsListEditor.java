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
package xmet.ui.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.TreeModelListenersList;
import n.ui.patterns.callback.Callback;
import xmet.ClientContext;
import xmet.profiles.catalogs.model.CodeItem;
import xmet.profiles.catalogs.model.Codelist;
import xmet.profiles.codecs.impl.StringKeywordsCodec;
import xmet.profiles.keywords.Keyword;
import xmet.profiles.keywords.KeywordsList;
import xmet.profiles.keywords.KeywordsThesaurus;
import xmet.profiles.keywords.impl.KeywordImpl;
import xmet.profiles.keywords.impl.KeywordsListImpl;
import xmet.profiles.keywords.impl.KeywordsThesaurousImpl;

/**
 * An editor for Keywords list objects.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"serial",
"rawtypes",
"unchecked"
})
public class KeywordsListEditor
    extends GUIObject
    implements
    KeywordsListGUIObject {

    /* == UI == */
    /** The source keywords list. */
    private final JTree sourceKeywordsList;

    /** The source keywords list listeners. */
    private final TreeModelListenersList sourceKeywordsListListeners;

    /** The selected keywords list. */
    private final JTree selectedKeywordsList;

    /** The selected keywords list listeners. */
    private final TreeModelListenersList selectedKeywordsListListeners;

    /* == Lists == */
    /** The source lists. */
    private final ArrayList<Codelist> sourceLists = new ArrayList<Codelist>();

    /** The selected keywords. */
    private final ArrayList<ArrayList<Keyword>> selectedKeywords =
        new ArrayList<ArrayList<Keyword>>();

    /* == Constructors == */
    /**
     * Instantiates a new keywords list editor.
     * @param context the context
     */
    public KeywordsListEditor(
        final ClientContext context) {
        super(context);
        // {
        sourceKeywordsList = new JTree();
        sourceKeywordsListListeners = new TreeModelListenersList(
            sourceKeywordsList);
        sourceKeywordsList.setModel(getSouceTreeModel());
        sourceKeywordsList.setRootVisible(false);
        sourceKeywordsList.setShowsRootHandles(true);
        sourceKeywordsList.setCellRenderer(getSourceTreeCellRenderer());
        // }
        // {

        selectedKeywordsList = new JTree();
        selectedKeywordsListListeners = new TreeModelListenersList(
            selectedKeywordsList);

        selectedKeywordsList.setModel(getSelectedTreeModel());
        selectedKeywordsList.setRootVisible(false);
        selectedKeywordsList.setShowsRootHandles(true);
        selectedKeywordsList.setCellRenderer(getSelectedTreeCellRenderer());
        // }
        /* sourceKeywordsList.expandRow(0); */

        setLayout(new GridBagLayout());
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Available Keywords"),
            "w=rem;f=b;wx=1;");
        SwingUtils.GridBag.add(
            this,
            new JScrollPane(
                sourceKeywordsList),
            "w=rel;f=b;wx=1;wy=1;");
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "Add",
                new Callback() {

                    @Override
                    public void callback() {
                        addSelected();
                    }

                }),
            "w=rem;a=b;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Selected Keywords"),
            "w=rem;f=b;wx=1;");
        final JScrollPane selectedScrollPane = new JScrollPane(
            selectedKeywordsList);
        selectedScrollPane
            .setBorder(BorderFactory.createLineBorder(Color.BLUE));
        SwingUtils.GridBag.add(
            this,
            selectedScrollPane,
            "w=rel;f=b;wx=1;wy=1;");
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "Remove",
                new Callback() {

                    @Override
                    public void callback() {
                        removeSelected();
                    }
                }),
            "w=rem;a=b;");
    }

    /* == Codelist Management == */
    /**
     * Sets the code list.
     * @param codelist the new code list
     */
    public void setCodeList(
        final Codelist codelist) {
        if (sourceLists.indexOf(codelist) == -1) {
            sourceLists.add(codelist);
        }
        sourceKeywordsList.setModel(getSouceTreeModel());
    }

    /* == Get and Set Value == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        setKeywordsList(StringKeywordsCodec.extractKeywordsList(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return StringKeywordsCodec.encodeKeywordsList(getKeywordsList());
    }

    /* == KeywordsListGUIObject Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void setKeywordsList(
        final KeywordsList keywords) {
        super.disableNotifications();
        selectedKeywords.clear();
        if (keywords != null) {
            final HashMap<String, KeywordsThesaurus> thesauruses =
                new HashMap<String, KeywordsThesaurus>();
            /* build a list of thesauruses */
            for (final Keyword keyword : keywords.getKeywords()) {
                if (keyword.getThesaurous() != null) {
                    thesauruses.put(
                        keyword.getThesaurous().getIdentifier(),
                        keyword.getThesaurous());
                }
            }

            for (final Map.Entry<String, KeywordsThesaurus> thes : thesauruses
                .entrySet()) {
                if (thes.getValue() instanceof KeywordsThesaurousImpl) {
                    for (final Codelist codelist : sourceLists) {
                        if (thes.getKey().equalsIgnoreCase(
                            codelist.getCodelistURL())) {
                            ((KeywordsThesaurousImpl) thes.getValue())
                                .setTitle(codelist.getCodelistName());
                        }
                    }
                }
                final ArrayList<Keyword> thesList = new ArrayList<Keyword>();
                for (final Keyword keyword : keywords.getKeywords()) {
                    if (keyword.getThesaurous() != null
                        && keyword.getThesaurous().equals(
                            thes.getValue())) {
                        thesList.add(keyword);
                    }
                }
                selectedKeywords.add(thesList);
            }

            final ArrayList<Keyword> thesList = new ArrayList<Keyword>();
            for (final Keyword keyword : keywords.getKeywords()) {
                if (keyword.getThesaurous() == null) {
                    thesList.add(keyword);
                }
            }
            if (thesList.size() > 0) {
                selectedKeywords.add(thesList);
            }
        }
        refreshSelected();
        super.setValue(getValue());
        super.enableNotifications();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeywordsList getKeywordsList() {
        final ArrayList<Keyword> keywords = new ArrayList<Keyword>();
        for (final ArrayList<Keyword> keysList : selectedKeywords) {
            keywords.addAll(keysList);
        }
        return new KeywordsListImpl(
            keywords);
    }

    /* == TreeModels == */
    /**
     * Gets the selected tree model.
     * @return the selected tree model
     */
    private TreeModel getSelectedTreeModel() {

        return new TreeModel() {

            @Override
            public Object getChild(
                final Object parent,
                final int index) {
                if (parent == selectedKeywords) {
                    return selectedKeywords.get(index);
                } else if (parent instanceof ArrayList) {
                    final ArrayList list = (ArrayList) parent;
                    return list.get(index);
                } else if (parent instanceof Keyword) {
                    Reporting.logUnexpected();
                }
                return null;
            }

            @Override
            public int getChildCount(
                final Object parent) {
                if (parent == selectedKeywords) {
                    return selectedKeywords.size();
                } else if (parent instanceof ArrayList) {
                    return ((ArrayList) parent).size();
                } else if (parent instanceof Keyword) {
                    Reporting.logUnexpected();
                }
                return 0;
            }

            @Override
            public int getIndexOfChild(
                final Object parent,
                final Object child) {
                if (parent == selectedKeywords) {
                    return selectedKeywords.indexOf(child);
                } else if (parent instanceof ArrayList) {
                    return ((ArrayList) parent).indexOf(child);
                } else if (parent instanceof Keyword) {
                    Reporting.logUnexpected();
                }
                return -1;
            }

            @Override
            public Object getRoot() {
                return selectedKeywords;
            }

            @Override
            public boolean isLeaf(
                final Object parent) {
                if (parent == selectedKeywords) {
                    return false;
                } else if (parent instanceof ArrayList) {
                    return false;
                } else if (parent instanceof Keyword) {
                    return true;
                }
                return true;
            }

            @Override
            public void addTreeModelListener(
                final TreeModelListener l) {
                selectedKeywordsListListeners.addTreeModelListener(l);
            }

            @Override
            public void removeTreeModelListener(
                final TreeModelListener l) {
                selectedKeywordsListListeners.removeTreeModelListener(l);
            }

            @Override
            public void valueForPathChanged(
                final TreePath path,
                final Object newValue) {

            }

        };
    }

    /**
     * Gets the souce tree model.
     * @return the souce tree model
     */
    private TreeModel getSouceTreeModel() {

        return new TreeModel() {

            @Override
            public boolean isLeaf(
                final Object node) {
                /* Reporting.log("isLeaf(%1$s)", node); */
                if (node == sourceLists) {
                    return false;
                }
                if (node instanceof Codelist) {
                    return false;
                }
                if (node instanceof CodeItem) {
                    return true;
                }
                return true;
            }

            @Override
            public Object getRoot() {
                /* Reporting.log("getRoot()"); */
                return sourceLists;
            }

            @Override
            public int getIndexOfChild(
                final Object node,
                final Object child) {
                if (node == sourceLists) {
                    return sourceLists.indexOf(child);
                }
                if (node instanceof Codelist) {
                    return ((Codelist) node).getItems().indexOf(
                        child);
                }
                if (node instanceof CodeItem) {
                    Reporting.logUnexpected();
                }
                return -1;
            }

            @Override
            public int getChildCount(
                final Object node) {
                /* Reporting.log("getChildCount(%1$s)", node); */
                if (node == sourceLists) {
                    return sourceLists.size();
                }
                if (node instanceof Codelist) {
                    return ((Codelist) node).getItems().size();
                }
                if (node instanceof CodeItem) {
                    return 0;
                }
                return -1;
            }

            @Override
            public Object getChild(
                final Object node,
                final int index) {
                /* Reporting.log("getChild(%1$s, %2$d)", node, index); */
                if (node == sourceLists) {
                    return sourceLists.get(index);
                }
                if (node instanceof Codelist) {
                    return ((Codelist) node).getItems().get(
                        index);
                }
                if (node instanceof CodeItem) {
                    Reporting.logUnexpected();
                }
                return null;
            }

            @Override
            public void removeTreeModelListener(
                final TreeModelListener l) {
                sourceKeywordsListListeners.removeTreeModelListener(l);
            }

            @Override
            public void addTreeModelListener(
                final TreeModelListener l) {
                sourceKeywordsListListeners.addTreeModelListener(l);
            }

            @Override
            public void valueForPathChanged(
                final TreePath path,
                final Object newValue) {

            }

        };
    }

    /* == Tree Cell Renderers == */

    /**
     * Gets the source tree cell renderer.
     * @return the source tree cell renderer
     */
    private TreeCellRenderer getSourceTreeCellRenderer() {
        return new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(
                final JTree tree,
                final Object value,
                final boolean selected,
                final boolean expanded,
                final boolean leaf,
                final int row,
                final boolean hasFocus) {
                Object varValue = value;
                if (varValue instanceof Codelist) {
                    varValue = ((Codelist) varValue).getCodelistName();
                }
                if (varValue instanceof CodeItem) {
                    varValue = String.format(
                        "%2$s",
                        ((CodeItem) varValue).getLabel(),
                        ((CodeItem) varValue).getValue());
                }
                return super.getTreeCellRendererComponent(
                    tree,
                    varValue,
                    selected,
                    expanded,
                    leaf,
                    row,
                    hasFocus);
            }
        };
    }

    /**
     * Gets the selected tree cell renderer.
     * @return the selected tree cell renderer
     */
    private TreeCellRenderer getSelectedTreeCellRenderer() {
        return new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(
                final JTree tree,
                final Object value,
                final boolean selected,
                final boolean expanded,
                final boolean leaf,
                final int row,
                final boolean hasFocus) {
                Object varValue = value;
                if (varValue instanceof Keyword) {
                    varValue = ((Keyword) varValue).getValue();
                } else if (varValue instanceof ArrayList
                    && varValue != selectedKeywords) {
                    final ArrayList keywords = (ArrayList) varValue;
                    final Keyword keyword = (Keyword) keywords.get(0);
                    final KeywordsThesaurus thesaurus = keyword.getThesaurous();
                    if (thesaurus != null) {
                        varValue = String.format(
                            "%1$s",
                            (thesaurus).getTitle());
                    } else {
                        varValue = "Others";
                    }
                }
                // else {
                //
                // }
                return super.getTreeCellRendererComponent(
                    tree,
                    varValue,
                    selected,
                    expanded,
                    leaf,
                    row,
                    hasFocus);
            }
        };
    }

    /* == Add and remove methods == */
    /**
     * Adds the selected.
     */
    private void addSelected() {
        if (sourceKeywordsList.getSelectionCount() > 0) {
            final TreePath[] selectionPaths =
                sourceKeywordsList.getSelectionPaths();
            for (final TreePath stp : selectionPaths) {
                if (stp.getLastPathComponent() instanceof CodeItem) {
                    final CodeItem item = (CodeItem) stp.getLastPathComponent();
                    final TreePath clp = stp.getParentPath();
                    if (clp.getLastPathComponent() instanceof Codelist) {
                        final Codelist codelist =
                            (Codelist) clp.getLastPathComponent();
                        final KeywordsThesaurus thesaurus =
                            new KeywordsThesaurousImpl(
                                codelist.getCodelistURL(),
                                null,
                                codelist.getCodelistName());
                        boolean found = false;
                        for (final ArrayList<Keyword> kl : selectedKeywords) {
                            final KeywordsThesaurus thesaurous = kl.get(
                                0).getThesaurous();
                            if (thesaurous != null
                                && thesaurous.equals(thesaurus)) {
                                boolean found2 = false;
                                for (final Keyword keyword : kl) {
                                    if (keyword.getValue().equals(
                                        item.getValue())) {
                                        found2 = true;
                                        break;
                                    }
                                }
                                if (!found2) {
                                    kl.add(new KeywordImpl(
                                        item.getValue(),
                                        kl.get(
                                            0).getThesaurous()));
                                }
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            final ArrayList<Keyword> list =
                                new ArrayList<Keyword>();
                            list.add(new KeywordImpl(
                                item.getValue(),
                                thesaurus));
                            selectedKeywords.add(list);
                        }
                    }
                }
            }
        }
        refreshSelected();
        notifyObserversIfChanged();
    }

    /**
     * Removes the selected.
     */
    private void removeSelected() {
        if (selectedKeywordsList.getSelectionCount() > 0) {
            final TreePath[] selectionPaths =
                selectedKeywordsList.getSelectionPaths();
            for (final TreePath stp : selectionPaths) {
                if (stp.getLastPathComponent() instanceof Keyword) {
                    final Keyword item = (Keyword) stp.getLastPathComponent();
                    final TreePath clp = stp.getParentPath();
                    if (clp.getLastPathComponent() instanceof ArrayList) {
                        final ArrayList<Keyword> keywords =
                            (ArrayList<Keyword>) clp.getLastPathComponent();
                        keywords.remove(item);
                        if (keywords.size() == 0) {
                            selectedKeywords.remove(keywords);
                        }
                    }
                }
            }
        }
        refreshSelected();
        notifyObserversIfChanged();
    }

    /**
     * Refresh selected.
     */
    private void refreshSelected() {
        selectedKeywordsList.setModel(getSelectedTreeModel());
        for (int i = 0; i < selectedKeywordsList.getRowCount(); i++) {
            selectedKeywordsList.expandRow(i);
        }
    }
}
