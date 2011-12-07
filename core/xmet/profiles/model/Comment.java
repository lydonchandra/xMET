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
package xmet.profiles.model;

/**
 * Represents a comment which might be found in a xml document.
 * @author Nahid Akbar
 */
public class Comment
    extends Entity {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new comment.
     * @param parent the parent
     * @param text the text
     */
    public Comment(
        final Entity parent,
        final String text) {
        super(parent, "comment", null);
        setComment(text);
    }

    /**
     * The comment.
     */
    private String comment;

    /**
     * Gets the comment.
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     * @param aComment the new comment
     */
    public void setComment(
        final String aComment) {
        this.comment = aComment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "comment";
    }

}
