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
package xmet.profiles.xslt;

import java.nio.ByteBuffer;

import n.io.CS;
import n.io.CSC;
import n.io.xml.SAXONUtils;
import xmet.resources.ResourceManager;

/**
 * Represents a XSLT Stylesheet.
 * @author Nahid Akbar
 */
@CSC("XSLTStyleSheet")
public class TransformerSheet {

    /** the name of the stylesheet file. */
    @CS
    private String fileName;

    /** description of the sheet. */
    @CS
    private String description;

    /** name of the sheet. */
    @CS
    private String name;

    /** the destination profile this sheet transforms source profile data to. */
    @CS
    private String destProfile;

    /** source profile this sheet transforms data of. */
    @CS
    private String sourceProfile;

    /** The m_ transformer sheet list. */
    private TransformerSheetList objTransformerSheetList;

    /**
     * Instantiates a new transformer sheet.
     */
    public TransformerSheet() {

    }

    /**
     * Transform string.
     * @param contents the contents
     * @param resourceManager the resource manager
     * @return the string
     */
    public String transformString(
        final String contents,
        final ResourceManager resourceManager) {
        try {
            final ByteBuffer xslt =
                resourceManager.getResourceContents(getFileName());
            return SAXONUtils.transformString(
                new String(
                    xslt.array()),
                contents);
        } catch (final Exception e) {
            e.printStackTrace();
            return contents;
        }
    }

    /**
     * Gets the name of the stylesheet file.
     * @return the name of the stylesheet file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the name of the stylesheet file.
     * @param aFileName the new name of the stylesheet file
     */
    public void setFileName(
        final String aFileName) {
        fileName = aFileName;
    }

    /**
     * Gets the description of the sheet.
     * @return the description of the sheet
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the sheet.
     * @param aDescription the new description of the sheet
     */
    public void setDescription(
        final String aDescription) {
        description = aDescription;
    }

    /**
     * Gets the name of the sheet.
     * @return the name of the sheet
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the sheet.
     * @param aName the new name of the sheet
     */
    public void setName(
        final String aName) {
        name = aName;
    }

    /**
     * Gets the destination profile this sheet transforms source profile data
     * to.
     * @return the destination profile this sheet transforms source profile data
     *         to
     */
    public String getDestProfile() {
        return destProfile;
    }

    /**
     * Sets the destination profile this sheet transforms source profile data
     * to.
     * @param aDestProfile the new destination profile this sheet transforms
     *            source profile data to
     */
    public void setDestProfile(
        final String aDestProfile) {
        destProfile = aDestProfile;
    }

    /**
     * Gets the source profile this sheet transforms data of.
     * @return the source profile this sheet transforms data of
     */
    public String getSourceProfile() {
        return sourceProfile;
    }

    /**
     * Sets the source profile this sheet transforms data of.
     * @param aSourceProfile the new source profile this sheet transforms data
     *            of
     */
    public void setSourceProfile(
        final String aSourceProfile) {
        sourceProfile = aSourceProfile;
    }

    /**
     * Gets the m_ transformer sheet list.
     * @return the m_ transformer sheet list
     */
    public TransformerSheetList getObjTransformerSheetList() {
        return objTransformerSheetList;
    }

    /**
     * Sets the m_ transformer sheet list.
     * @param aObjTransformerSheetList the new m_ transformer sheet list
     */
    public void setObjTransformerSheetList(
        final TransformerSheetList aObjTransformerSheetList) {
        objTransformerSheetList = aObjTransformerSheetList;
    }
}
