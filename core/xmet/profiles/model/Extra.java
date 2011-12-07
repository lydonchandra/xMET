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
 * Contains extra data found in an xml document which is not in the model.
 * @author Nahid Akbar
 */
public class Extra
    extends Entity {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    /**
     * The content.
     */
    private String content;

    /**
     * Instantiates a new extra.
     * @param parent the parent
     * @param name the name
     * @param aContent the content
     */
    public Extra(
        final Entity parent,
        final String name,
        final String aContent) {
        super(parent, name, null);
        this.setContent(aContent);
    }

    /**
     * Gets the content.
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content.
     * @param aContent the new content
     */
    public void setContent(
        final String aContent) {
        this.content = aContent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "extra data";
    }

}
