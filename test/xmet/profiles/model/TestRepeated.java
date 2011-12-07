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

import junit.framework.TestCase;

/**
 * The Class TestRepeated.
 */
public class TestRepeated
    extends TestCase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp()
        throws Exception {
        super.setUp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown()
        throws Exception {
        super.tearDown();
    }

    /**
     * Test repeated.
     */
    public void testRepeated() {
        final ElementDeclaration element = new ElementDeclaration(
            null,
            "test",
            null);
        final Repeated repeated = new Repeated(
            null,
            element);
        assertEquals(
            0,
            repeated.entityCount());
    }
}
