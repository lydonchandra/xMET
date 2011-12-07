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
package xmet.tools.metadata.editor.views.scv.model;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import n.io.CS;
import n.io.CSC;
import n.reporting.Reporting;

/**
 * Validation Information for an Item.
 * @author Nahid Akbar
 */
/**
 * @author Nahid Akbar
 */
@CSC("validationInfo")
public class ValidationInformation
    implements
    Serializable {

    /** The Constant DEFAULT_LENGTH_CONSTRAINTS. */
    private static final int DEFAULT_LENGTH_CONSTRAINTS = -1;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The mandatory. */
    @CS
    private boolean mandatory = false;

    /** The min length. */
    @CS
    private int minLength = DEFAULT_LENGTH_CONSTRAINTS;

    /** The max length. */
    @CS
    private int maxLength = DEFAULT_LENGTH_CONSTRAINTS;

    /** The pattern. */
    @CS
    private String pattern = null;

    /** The type. */
    @CS
    private ValidationDataType type = ValidationDataType.STRING;

    /** The depreciated. */
    @CS
    private boolean depreciated = false;

    /**
     * Checks for default values.
     * @return true, if successful
     */
    public boolean hasDefaultValues() {
        return !mandatory
            && minLength == DEFAULT_LENGTH_CONSTRAINTS
            && maxLength == DEFAULT_LENGTH_CONSTRAINTS
            && (pattern == null || pattern.trim().length() == 0)
            && (type == null || type == ValidationDataType.STRING)
            && (!depreciated);
    }

    /**
     * Gets the min length.
     * @return the min length
     */
    public int getMinLength() {
        return minLength;
    }

    /**
     * Gets the max length.
     * @return the max length
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Gets the pattern.
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public ValidationDataType getType() {
        return type;
    }

    /**
     * Checks if is the depreciated.
     * @return the depreciated
     */
    public boolean isDepreciated() {
        return depreciated;
    }

    /**
     * Sets the pattern.
     * @param aPattern the new pattern
     */
    public void setPattern(
        final String aPattern) {
        pattern = aPattern;
    }

    /**
     * Checks if is the mandatory.
     * @return the mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * Sets the mandatory.
     * @param aMandatory the new mandatory
     */
    public void setMandatory(
        final boolean aMandatory) {
        mandatory = aMandatory;
    }

    /**
     * Sets the min length.
     * @param aMinLength the new min length
     */
    public void setMinLength(
        final int aMinLength) {
        minLength = aMinLength;
    }

    /**
     * Sets the max length.
     * @param aMaxLength the new max length
     */
    public void setMaxLength(
        final int aMaxLength) {
        maxLength = aMaxLength;
    }

    /**
     * Sets the depreciated.
     * @param aDepreciated the new depreciated
     */
    public void setDepreciated(
        final boolean aDepreciated) {
        depreciated = aDepreciated;
    }

    /**
     * Sets the type.
     * @param aType the new type
     */
    public void setType(
        final ValidationDataType aType) {
        type = aType;
    }

    /**
     * Validate value.
     * @param value the value
     * @return the string
     */
    public String validateValue(
        final String value) {
        String varValue = value;
        if (varValue != null
            && varValue.trim().length() > 0) {
            varValue = varValue.trim();
            /* check if depreciated field */
            if (depreciated) {
                return "This field is depreciated.";
            }
            /* if there is some content */
            /* check type */

            if (type != null) {
                switch (type) {
                case STRING:
                    /* anything will do */
                    break;
                case INTEGER:
                    try {
                        Integer.parseInt(varValue);
                    } catch (final Exception e) {
                        return "Only whole numbers are accepted";
                    }
                    break;
                case REAL:
                    try {
                        Double.parseDouble(varValue);
                    } catch (final Exception e) {
                        return "Only numbers are accepted";
                    }
                    break;
                default:
                    Reporting.logUnexpected("unhandelled type");
                    break;
                }
            }

            /* check minLength */
            if (minLength > 0
                && varValue.length() < minLength) {
                return String.format(
                    "A minimum of %1$d characters expected",
                    minLength);
            }
            /* check maxLength */
            if (maxLength > 0
                && varValue.length() > maxLength) {
                return String.format(
                    "A maximum of %1$d characters allowed",
                    maxLength);
            }
            /* check pattern */
            if (pattern != null) {
                pattern = pattern.trim();
                if (pattern.length() > 0) {
                    try {
                        final Pattern pattObj = Pattern.compile(this.pattern);
                        if (pattObj != null) {
                            final Matcher matcher = pattObj.matcher(varValue);
                            if (!matcher.matches()) {
                                return "Unacceptable input format.";
                            }
                        } else {
                            throw new Exception(
                                "Pattern Could Not Be Compiled;");
                        }
                    } catch (final Exception e) {
                        Reporting.logExpected(
                            "Error in pattern matching: %1$s",
                            e.getLocalizedMessage());
                    }
                }
            }
            return null;
        } else {
            /* if there is no content */
            if (mandatory) {
                return "Item Is Mandatory";
            } else {
                return null;
            }
        }
    }

    /**
     * Gets the default length constraints.
     * @return the default length constraints
     */
    public static int getDefaultLengthConstraints() {
        return DEFAULT_LENGTH_CONSTRAINTS;
    }

}
