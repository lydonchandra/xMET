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
package xmet.profiles.codecs.impl;

import junit.framework.TestCase;
import n.io.xml.JDOMXmlUtils;

import org.jdom.Element;

import xmet.profiles.model.Any;
import xmet.profiles.model.ElementDeclaration;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.SequenceGroup;

/**
 * Test class for XMLDecoder.
 * @author Nahid Akbar
 */
public class XMLDecoderTest
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
     * Test any type.
     */
    public void testAnyType() {
        // {
        final ElementDeclaration wrapperBase = new ElementDeclaration(
            null,
            "wrapper",
            null);
        SequenceGroup group;
        group = new SequenceGroup(
            wrapperBase);
        wrapperBase.setGroup(group);
        group.getChildren().add(
            new Any(
                group,
                "any",
                null));

        // {
        final ElementDeclaration simpleWrapper =
            ModelUtils
                .asElementDeclaration(ModelUtils.cloneEntity(wrapperBase));
        final Element wrapperData = new Element(
            "wrapper");
        (new XMLDecoder()).loadData(
            simpleWrapper,
            wrapperData);
        assertSame(
            ModelUtils.getSetable(
                simpleWrapper).getValue(),
            null);
        wrapperData.setText("asdf");
        (new XMLDecoder()).loadData(
            simpleWrapper,
            wrapperData);
        assertEquals(
            ModelUtils.getSetable(
                simpleWrapper).getValue(),
            "asdf");
        assertEquals(
            JDOMXmlUtils.xmlFromElement(new XMLEncoder().encodeData(
                simpleWrapper,
                null)),
            "<wrapper>asdf</wrapper>");
        // }
        // }
    }
}
