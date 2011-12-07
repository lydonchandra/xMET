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
package xmet.tools.metadata.editor.views.scv.impl;

import n.io.CS;
import n.io.CSC;
import n.reporting.Reporting;
import xmet.profiles.model.Repeated;
import xmet.tools.metadata.editor.views.scv.utils.SetableIC;

/**
 * Code for conditional stuff.
 * @author Nahid Akbar
 */
@CSC("if")
public class IfCode
    extends Code {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * The condition enumeration.
     */
    public static enum Condition {
        /* String */
        /** The present. */
        present,
        /** The rep size less than. */
        repSizeLessThan,
        /** The rep size greater than. */
        repSizeGreaterThan,
        /** The rep size equals. */
        repSizeEquals,
        /** The equals. */
        equals,
        /** The matches. */
        matches,
        /** The starts with. */
        startsWith,
        /** The ends with. */
        endsWith,
        /** The contains. */
        contains,
        /** The empty. */
        empty,
        /** The None. */
        None
    }

    /* == Properties == */
    /** base xpath. use "." to refer to the current item */
    @CS
    private String base = "#";

    /** The condition expression. */
    @CS
    private Condition condition = Condition.None;

    /** value to compare condition with. */
    @CS
    private String expression;

    /** code to execute if condition is satisfied. */
    @CSC
    private Code code;

    /** The else code. */
    @CSC
    private Code elseCode;

    /* == Runtime Helper Stuff == */

    /** The initialization context. */
    private transient SetableIC ic;

    /**
     * Checks if is the specified value satisfies the condition.
     * @param value the value
     * @return true, if is condition satisfied
     */
    public boolean isConditionSatisfied(
        final String value) {
        return isConditionSatisfied(
            null,
            this);
    }

    /**
     * Checks if is condition satisfied.
     * @param item the item
     * @param ifcode the ifcode
     * @return true, if is condition satisfied
     */
    public static boolean isConditionSatisfied(
        final CodeParent item,
        final IfCode ifcode) {
        try {
            if (ifcode.getBase() != null
                && ifcode.getCondition() != null
                && ifcode.getCondition() != Condition.None) {
                final Condition condition = ifcode.getCondition();
                final String expression = ifcode.getExpression();
                String value = "";
                if (condition != Condition.present
                    && condition != Condition.repSizeEquals
                    && condition != Condition.repSizeGreaterThan
                    && condition != Condition.repSizeLessThan) {
                    value = getBaseValue(
                        item,
                        ifcode);

                    if (expression == null) {
                        return isEmptyExpressionConditionSatisfied(
                            condition,
                            value);
                    } else {
                        return isExpressionConditionSatisfied(
                            condition,
                            expression,
                            value);
                    }
                } else {
                    return isModelConditionSatisfied(
                        ifcode,
                        condition,
                        expression);
                }
            } else {
                Reporting.reportUnexpected("");
            }
        } catch (final RuntimeException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Checks if is model condition satisfied.
     * @param ifcode the ifcode
     * @param condition the condition
     * @param expression the expression
     * @return true, if is model condition satisfied
     */
    private static boolean isModelConditionSatisfied(
        final IfCode ifcode,
        final Condition condition,
        final String expression) {
        switch (condition) {
        case present:
            return ifcode.getIc().isXpathPresent(
                ifcode.getIc().getNodeXpath());
        case repSizeEquals:
            // {
            try {
                final Repeated repeated = ifcode.getIc().getRepeatedItem(
                    ifcode.getIc().getNodeXpath());
                if (repeated != null
                    && repeated.entityCount() == Integer.parseInt(expression)) {
                    return true;
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            break;
        // }
        case repSizeGreaterThan:
            // {
            try {
                final Repeated repeated = ifcode.getIc().getRepeatedItem(
                    ifcode.getIc().getNodeXpath());
                if (repeated != null
                    && repeated.entityCount() > Integer.parseInt(expression)) {
                    return true;
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            break;
        // }
        case repSizeLessThan:
            // {
            try {
                final Repeated repeated = ifcode.getIc().getRepeatedItem(
                    ifcode.getIc().getNodeXpath());
                if (repeated != null
                    && repeated.entityCount() < Integer.parseInt(expression)) {
                    return true;
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            break;
        // }
        default:
            Reporting.logUnexpected();
        }
        return false;
    }

    /**
     * Checks if is expression condition satisfied.
     * @param condition the condition
     * @param expression the expression
     * @param value the value
     * @return true, if is expression condition satisfied
     */
    private static boolean isExpressionConditionSatisfied(
        final Condition condition,
        final String expression,
        final String value) {
        switch (condition) {
        case equals:
            if (((value == null)
                && (expression != null) && (expression.length() == 0))
                || ((value != null)
                    && (value.length() == 0) && (expression == null))
                || ((value != null)
                    && (expression != null) && value.equals(expression))) {
                return true;
            }
            break;
        case empty:
            if ((value == null)
                || (value.trim().length() == 0)) {
                return true;
            }
            break;
        case startsWith:
            if (((value != null) && value.startsWith(expression))
                || (expression.length() == 0)) {
                return true;
            }
            break;
        case endsWith:
            if (((value != null) && value.endsWith(expression))
                || (expression.length() == 0)) {
                return true;
            }
            break;
        case contains:
            if (((value != null) && value.contains(expression))
                || (expression.length() == 0)) {
                return true;
            }
            break;
        case matches:
            try {
                if (value != null
                    && value.matches(expression)) {
                    return true;
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            break;
        default:
            Reporting.logUnexpected();
        }
        return false;
    }

    /**
     * Checks if is empty expression condition satisfied.
     * @param condition the condition
     * @param value the value
     * @return true, if is empty expression condition satisfied
     */
    private static boolean isEmptyExpressionConditionSatisfied(
        final Condition condition,
        final String value) {
        switch (condition) {
        case empty:
            if (value == null
                || value.trim().length() == 0) {
                return true;
            }
            break;
        case equals:
            return (value == null)
                || (value.length() == 0);
        case contains:
        case endsWith:
        case startsWith:
        case matches:
            return true;
        default:
            Reporting.logUnexpected();
            return true;
        }
        return false;
    }

    /**
     * Gets the base value.
     * @param item the item
     * @param ifcode the ifcode
     * @return the base value
     */
    private static String getBaseValue(
        final CodeParent item,
        final IfCode ifcode) {
        String value = "";
        final String aBase = ifcode.getBase();
        if (aBase.equals(".")) {
            /* legacy support */
            value = item.getValue();
        } else if (aBase.equals("#")) {
            value = item.getValue();
        } else if (aBase.startsWith("%")) {
            final String name = aBase.substring(1);
            if (ifcode.getIc() != null) {
                final NamedItem refItem = ifcode.getIc().getNamedItem(
                    name);
                if (refItem != null) {
                    if (refItem instanceof ContentNamedItem) {
                        value = ((ContentNamedItem) refItem).getValue();
                    } else {
                        Reporting
                            .logUnexpected("Inappropriate Named Item Type");
                    }
                } else {
                    Reporting.logUnexpected(
                        "Named item %1$s not found",
                        name);
                }
            } else {
                Reporting.reportUnexpected("Code not yet initialized");
            }
        } else {
            value = item.getIC().getPathValue(
                aBase);
        }
        return value;
    }

    /* == Helper Constructors == */

    /**
     * Instantiates a new if code.
     * @param aBase the base
     * @param aCondition the condition
     * @param aExpression the expression
     * @param aCode the code
     */
    public IfCode(
        final String aBase,
        final Condition aCondition,
        final String aExpression,
        final Code aCode) {
        super();
        this.setBase(aBase);
        this.setCondition(aCondition);
        this.setExpression(aExpression);
        this.setCode(aCode);
    }

    /**
     * Instantiates a new if code.
     */
    public IfCode() {
        super();
    }

    /**
     * Gets the base xpath.
     * @return the base xpath
     */
    public String getBase() {
        return base;
    }

    /**
     * Sets the base xpath.
     * @param aBase the new base xpath
     */
    public void setBase(
        final String aBase) {
        base = aBase;
    }

    /**
     * Gets the condition expression.
     * @return the condition expression
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Sets the condition expression.
     * @param aCondition the new condition expression
     */
    public void setCondition(
        final Condition aCondition) {
        condition = aCondition;
    }

    /**
     * Gets the value to compare condition with.
     * @return the value to compare condition with
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Sets the value to compare condition with.
     * @param aExpression the new value to compare condition with
     */
    public void setExpression(
        final String aExpression) {
        expression = aExpression;
    }

    /**
     * Gets the code to execute if condition is satisfied.
     * @return the code to execute if condition is satisfied
     */
    public Code getCode() {
        return code;
    }

    /**
     * Sets the code to execute if condition is satisfied.
     * @param aCode the new code to execute if condition is satisfied
     */
    public void setCode(
        final Code aCode) {
        code = aCode;
    }

    /**
     * Gets the else code.
     * @return the else code
     */
    public Code getElseCode() {
        return elseCode;
    }

    /**
     * Sets the else code.
     * @param aElseCode the new else code
     */
    public void setElseCode(
        final Code aElseCode) {
        elseCode = aElseCode;
    }

    /**
     * Gets the initialization context.
     * @return the initialization context
     */
    public SetableIC getIc() {
        return ic;
    }

    /**
     * Sets the initialization context.
     * @param aIc the new initialization context
     */
    public void setIc(
        final SetableIC aIc) {
        ic = aIc;
    }
}
