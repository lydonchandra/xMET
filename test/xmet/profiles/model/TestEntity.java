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
 * Nothing to test really.
 */
public class TestEntity
    extends TestCase {

    /**
     * The Class ConcreteEntity.
     */
    private static class ConcreteEntity
        extends Entity {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new concrete entity.
         * @param parent the parent
         * @param name the name
         * @param namespace the namespace
         */
        protected ConcreteEntity(
            final Entity parent,
            final String name,
            final String namespace) {
            super(parent, name, namespace);
        }
    }

    /**
     * Test name.
     */
    public void testName() {
        ConcreteEntity ce = new ConcreteEntity(
            null,
            null,
            null);
        assertNull(ce.getQualifiedName());
        ce = new ConcreteEntity(
            null,
            "name",
            null);
        assertEquals(
            ce.getQualifiedName(),
            "name");
        ce.setQualifiedName(null);
        assertNull(ce.getQualifiedName());
        ce.setQualifiedName("fred");
        assertEquals(
            ce.getQualifiedName(),
            "fred");
    }

    /**
     * Test namespace.
     */
    public void testNamespace() {
        ConcreteEntity ce = new ConcreteEntity(
            null,
            null,
            null);
        assertNull(ce.getNamespace());
        ce = new ConcreteEntity(
            null,
            null,
            "name");
        assertEquals(
            ce.getNamespace(),
            "name");
        ce.setNamespace(null);
        assertNull(ce.getNamespace());
        ce.setNamespace("fred");
        assertEquals(
            ce.getNamespace(),
            "fred");
    }

    /**
     * Test parent.
     */
    public void testParent() {
        ConcreteEntity cep = new ConcreteEntity(
            null,
            null,
            null);
        ConcreteEntity ce = new ConcreteEntity(
            null,
            null,
            null);
        assertNull(ce.getParent());
        ce = new ConcreteEntity(
            cep,
            null,
            null);
        assertEquals(
            ce.getParent(),
            cep);
        ce.setParent(null);
        assertNull(ce.getParent());
        cep = new ConcreteEntity(
            null,
            null,
            null);
        ce.setParent(cep);
        assertEquals(
            ce.getParent(),
            cep);
    }

    /**
     * Test valid.
     */
    public void testValid() {
        ConcreteEntity ce = new ConcreteEntity(
            null,
            null,
            null);
        assertTrue(ce.isValid());
        ce = new ConcreteEntity(
            null,
            null,
            null);
        ce.setValidationError(null);
        assertTrue(ce.isValid());
        ce.setValidationError("asdf");
        assertFalse(ce.isValid());
        ce.setValidationError("asdf");
        assertFalse(ce.isValid());
        ce.setValidationError(null);
        assertTrue(ce.isValid());
        ce.setValidationError(null);
        assertTrue(ce.isValid());
    }

}
