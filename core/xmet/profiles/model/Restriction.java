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

import java.io.Serializable;

import n.reporting.Reporting;

/**
 * A restriction is a constraint on a simple type.
 * @author Nahid Akbar
 */
public abstract class Restriction
    implements
    Serializable {

    /* TODO: Look at the standard and fill out unimplemented stuff */

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * FractionDigits restr.
     */
    public static class FractionDigits
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            Reporting.logUnexpected();
            return null;
        }
    }

    /**
     * MaxInclusive restriction.
     */
    public static class MaxInclusive
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            if (Integer.parseInt(val) <= Integer.parseInt(getValue())) {
                return null;
            } else {
                return "above max inclusive";
            }
        }
    }

    /**
     * /** MaxExclusive restriction.
     */
    public static class MaxExclusive
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            if (Integer.parseInt(val) < Integer.parseInt(getValue())) {
                return null;
            } else {
                return "above max exclusive";
            }
        }
    }

    /**
     * MaxLength restriction.
     */
    public static class MaxLength
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            if (val.trim().length() <= Integer.parseInt(getValue())) {
                return null;
            } else {
                return "amove maximum length";
            }
        }
    }

    /**
     * MinExclusive restriction.
     */
    public static class MinExclusive
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            if (Integer.parseInt(val) > Integer.parseInt(getValue())) {
                return null;
            } else {
                return "below min exclusive";
            }
        }

    }

    /**
     * MinInclusive restriction.
     */
    public static class MinInclusive
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            if (Integer.parseInt(val) >= Integer.parseInt(getValue())) {
                return null;
            } else {
                return "below min inclusive";
            }
        }

    }

    /**
     * MinLength restriction.
     */
    public static class MinLength
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            if (val.trim().length() >= Integer.parseInt(getValue())) {
                return null;
            } else {
                return "below minimum length";
            }
        }

    }

    /**
     * Pattern restriction.
     */
    public static class Pattern
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            Reporting.logUnexpected();
            return null;
        }

    }

    /**
     * TotalDigits restriction.
     */
    public static class TotalDigits
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            Reporting.logUnexpected();
            return null;
        }

    }

    /**
     * Whitespace restriction.
     */
    public static class Whitespace
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            /* nothing to do */
            return null;
        }

    }

    /**
     * Length restriction.
     */
    public static class Length
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            if (val.trim().length() == Integer.parseInt(getValue())) {
                return null;
            } else {
                return "does not match required length";
            }
        }

    }

    /**
     * Enumeration restriction.
     */
    public static class Enumeration
        extends Restriction {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getValidationErrorMessage(
            final String val) {
            if (val.equals(getValue())) {
                return null;
            } else {
                return "not equal";
            }
        }

        /**
         * Checks if is equal.
         * @param val the val
         * @return true, if is equal
         */
        public boolean isEqual(
            final String val) {
            return (val != null)
                && val.equals(getValue());
        }

    }

    /** The value. */
    private String value;

    /**
     * Gets the value.
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     * @param aValue the new value
     */
    public void setValue(
        final String aValue) {
        this.value = aValue;
    }

    /**
     * Gets the validation error message.
     * @param val the val
     * @return the validation error message
     */
    public abstract String getValidationErrorMessage(
        String val);

    /**
     * Validate.
     * @param simple the simple
     * @return true, if successful
     */
    public static boolean validate(
        final Simple simple) {
        simple.setValidationError(null);
        final String value = simple.getValue();
        if ((simple.getRestrictions() != null)
            && (simple.getRestrictions().size() > 0)) {
            boolean ec = false;
            String validationError = null;
            for (int i = 0; (i < simple.getRestrictions().size())
                && (validationError == null); i++) {
                final Restriction r = simple.getRestrictions().get(
                    i);
                if (r instanceof Restriction.Enumeration) {
                    if (!ec) {
                        Reporting.logUnexpected();
                        /* EC */
                        boolean found = false;
                        for (int j = i; (j < simple.getRestrictions().size())
                            && !found; j++) {
                            final Restriction s = simple.getRestrictions().get(
                                j);
                            if (s instanceof Restriction.Enumeration) {
                                if (((Restriction.Enumeration) s)
                                    .isEqual(value)) {
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            validationError =
                                "value not found in acceptable list";
                        }
                        ec = true;
                    }
                } else {
                    validationError = r.getValidationErrorMessage(value);
                }
            }
            simple.setValidationError(validationError);
        }
        return simple.isValid();
    }

    /**
     * Accept.
     * @param visitor the visitor
     */
    public void accept(
        final ModelVisitor visitor) {
        visitor.visitRestriction(
            this,
            visitor);
    }
}
