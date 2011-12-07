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

import n.math.MathUtils;

/**
 * Test case for Date Picker control.
 * @author Nahid Akbar
 */
public class DatePickerTestCase
    extends AbstractGUIObjectTestCase {

    /**
     * Instantiates a new date picker test case.
     */
    public DatePickerTestCase() {
        super(DatePicker.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getExampleValue() {
        // CHECKSTYLE OFF: MagicNumber
        return String.format(
            "%1$04d-%2$02d-%3$02d",
            MathUtils.getRandomInt(
                1900,
                2100),
            MathUtils.getRandomInt(
                1,
                12),
            MathUtils.getRandomInt(
                0,
                28));
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getChangeValueMethods() {
        return new String[] {
            "Enter \"asdf\"|asdf"
        };
    }
}
