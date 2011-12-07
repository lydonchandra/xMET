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
package xmet.tools.metadata.editor.views.generated;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import xmet.profiles.model.Comment;

/**
 * Editor for xml comments.
 * @author Nahid Akbar
 */
public class CommentGeneratedViewEditor
    extends GeneratedViewEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The item. */
    private final Comment item;

    /** The comment editor. */
    private final JTextArea commentEditor;

    /**
     * Instantiates a new comment generated view editor.
     * @param aItem the item
     * @param actionListener the action listener
     */
    public CommentGeneratedViewEditor(
        final Comment aItem,
        final ActionListener actionListener) {

        this.item = aItem;
        setLayout(new GridBagLayout());
        // {
        // {
        final JPanel panel = GridBag.getNew();
        // {
        // CHECKSTYLE OFF: MagicNumber
        commentEditor = new JTextArea(
            80,
            5);
        commentEditor.setLineWrap(true);
        commentEditor.setText(aItem.getComment());
        commentEditor.setMinimumSize(new Dimension(
            80,
            300));
        commentEditor.setPreferredSize(new Dimension(
            80,
            300));
        // CHECKSTYLE ON: MagicNumber
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Comment"),
            "w=rem;");
        SwingUtils.GridBag.add(
            panel,
            new JScrollPane(
                commentEditor),
            "w=rem;f=h;wx=1;wy=1;");
        // }
        panel.setBorder(BorderFactory.createTitledBorder("Constraints"));
        SwingUtils.GridBag.add(
            this,
            panel,
            "w=rem;f=h;");
        // }
        // }
        SwingUtils.GridBag.add(
            this,
            Box.createGlue(),
            "w=rem;f=b;wx=1;wy=1;");
        setBorder(BorderFactory.createTitledBorder("Comment Item"));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    void stopEditting() {
        String comment = commentEditor.getText();
        if (comment != null
            && comment.trim().length() != 0) {
            comment = comment.trim();
            item.setComment(comment);
        } else {
            item.setComment(null);
        }
    }
}
