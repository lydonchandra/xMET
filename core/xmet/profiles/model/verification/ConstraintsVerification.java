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
package xmet.profiles.model.verification;

import xmet.profiles.model.AllGroup;
import xmet.profiles.model.AttributesList;
import xmet.profiles.model.ChoiceGroup;
import xmet.profiles.model.DefaultModelVisitor;
import xmet.profiles.model.ElementAttribute;
import xmet.profiles.model.ElementDeclaration;
import xmet.profiles.model.Entity;
import xmet.profiles.model.Group;
import xmet.profiles.model.ImplicitGroup;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.ModelVisitor;
import xmet.profiles.model.MultiplicityConstraints;
import xmet.profiles.model.Optional;
import xmet.profiles.model.Repeated;
import xmet.profiles.model.Restriction;
import xmet.profiles.model.SequenceGroup;
import xmet.profiles.model.Simple;

/**
 * Class that does constraints verification on the model.
 * @author Nahid Akbar
 */
public class ConstraintsVerification
    extends DefaultModelVisitor {

    /** The Constant REQUIRED_ATTRIBUTE_MISSING_ERROR_MESSAGE. */
    private static final String REQUIRED_ATTRIBUTE_MISSING_ERROR_MESSAGE =
        "Required Attribute Is Not Present";
    /** The Constant INVALID_CHILD_ERROR_MESSAGE. */
    private static final String INVALID_CHILD_ERROR_MESSAGE = "invalid child";

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitElementDeclaration(
        final ElementDeclaration item,
        final ModelVisitor visitor) {
        if (!item.getGroup().isValid()) {
            item.setValidationError(INVALID_CHILD_ERROR_MESSAGE);
        } else if (item.getAttributes() != null
            && !item.getAttributes().isValid()) {
            item.setValidationError(INVALID_CHILD_ERROR_MESSAGE);
        } else {
            item.setValidationError(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitAttributesList(
        final AttributesList item,
        final ModelVisitor visitor) {
        item.setValid(true);
        for (final ElementAttribute elementAttribute : item) {
            if (!elementAttribute.isValid()) {
                item.setValid(false);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitElementAttribute(
        final ElementAttribute item,
        final ModelVisitor visitor) {
        if (item.isRequired()) {
            String varString = null;
            final String varValue = item.getValue();
            if ((varValue != null && varValue.trim().length() > 0)) {
                varString = null;
            } else {
                varString = REQUIRED_ATTRIBUTE_MISSING_ERROR_MESSAGE;
            }
            final String varValidationError = varString;
            item.setValidationError(varValidationError);
        } else {
            item.setValidationError(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeated(
        final Repeated item,
        final ModelVisitor visitor) {
        final MultiplicityConstraints mc =
            ModelUtils.getConstraints(item.getBaseEntity());
        item.setValidationError(null);
        if (mc != null) {
            if (item.getEntities().size() < mc.getMinOccurs()) {
                item.setValidationError(String.format(
                    "enough %1$s is not present",
                    item.getBaseEntity().getQualifiedName()));
            }
            if ((item.getEntities().size() > mc.getMaxOccurs())
                && (mc.getMaxOccurs() != -1)) {
                item.setValidationError(String.format(
                    "too many %1$s is present",
                    item.getBaseEntity().getQualifiedName()));
            }
        }
        if (item.isValid()) {
            boolean allValid = true;
            for (final Entity entity : item.getEntities()) {
                allValid = allValid
                    && entity.isValid();
            }
            String varValidationError = null;
            if (allValid) {
                varValidationError = null;
            } else {
                varValidationError = INVALID_CHILD_ERROR_MESSAGE;
            }
            item.setValidationError(varValidationError);
        }
    }

    /**
     * Post visit generic group.
     * @param item the item
     * @param visitor the visitor
     */
    public void postVisitGenericGroup(
        final Group item,
        final ModelVisitor visitor) {
        boolean allValid = true;
        for (final Entity e : item.getChildren()) {
            allValid = allValid
                && e.isValid();
        }
        String varValidationError = null;
        if (allValid) {
            varValidationError = null;
        } else {
            varValidationError = INVALID_CHILD_ERROR_MESSAGE;
        }
        item.setValidationError(varValidationError);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitSequenceGroup(
        final SequenceGroup item,
        final ModelVisitor visitor) {
        postVisitGenericGroup(
            item,
            visitor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitImplicitGroup(
        final ImplicitGroup item,
        final ModelVisitor visitor) {
        postVisitGenericGroup(
            item,
            visitor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitAllGroup(
        final AllGroup item,
        final ModelVisitor visitor) {
        postVisitGenericGroup(
            item,
            visitor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitChoiceGroup(
        final ChoiceGroup item,
        final ModelVisitor visitor) {
        final boolean allValid = item.getChildren().get(
            item.getSelected()).isValid();
        String varValidationError = null;
        if (allValid) {
            varValidationError = null;
        } else {
            varValidationError = INVALID_CHILD_ERROR_MESSAGE;
        }
        item.setValidationError(varValidationError);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitSimple(
        final Simple item,
        final ModelVisitor visitor) {
        Restriction.validate(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitOptional(
        final Optional item,
        final ModelVisitor visitor) {

    }

    /**
     * Verify.
     * @param model the model
     * @return true, if successful
     */
    public boolean verify(
        final Entity model) {
        ModelUtils.accept(
            model,
            this);
        return model.getValidationError() == null;
    }
}
