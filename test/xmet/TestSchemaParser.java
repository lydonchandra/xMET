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
package xmet;

import java.io.File;

import junit.framework.TestCase;
import xmet.profiles.codecs.impl.SchemaParser;

/**
 * The Class TestSchemaParser.
 * @author Ian Maddox
 */
public class TestSchemaParser
    extends TestCase {

    /* don't need it for JUnit3.8 and later? (won't hurt...) */
    /**
     * Instantiates a new test schema parser.
     * @param name the name
     */
    public TestSchemaParser(
        final String name) {
        super(name);
    }

    /** The file. */
    private File file;

    /* Set everything up for testing */
    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() {
        setFile(new File(
            "./client/profiles/default/schemas/iso19115/gmd/gmd.xsd"));
    }

    /**
     * Test parse schema.
     */
    public void testParseSchema() {
        assertNotNull(
            "The parseSchema function returned null.",
            SchemaParser.parseSchema(
                getFile(),
                Client.getContext()));
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param aFile the file to set
     */
    public void setFile(
        final File aFile) {
        this.file = aFile;
    }

}
