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

import java.util.Locale;

import n.math.MathUtils;

/**
 * Test case for CheckedList control.
 * @author Nahid Akbar
 */
public class CheckedListTestCase
    extends AbstractGUIObjectTestCase {

    /**
     * Instantiates a new checked list test case.
     */
    public CheckedListTestCase() {
        super(CheckedList.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getExampleValue() {
        // CHECKSTYLE OFF: MagicNumber
        final StringBuilder sb = new StringBuilder();
        final Locale[] locales = Locale.getAvailableLocales();
        for (int i = 0; i < MathUtils.getRandomInt(
            0,
            20); i++) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            String s = locales[MathUtils.getRandomInt(
                0,
                locales.length - 1)].getDisplayName();
            while (sb.toString().indexOf(
                s) >= 0) {
                s = locales[MathUtils.getRandomInt(
                    0,
                    locales.length - 1)].getDisplayName();
            }
            sb.append(s);
        }
        return sb.toString();
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getChangeValueMethods() {
        return new String[] {
            "Check \"asdf\"|asdf"
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp()
        throws Exception {
        super.setUp();
        ((CheckedList) getInstance()).setCustomList("asdf");
    }

}
