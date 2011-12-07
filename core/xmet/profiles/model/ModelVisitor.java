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

/**
 * Do not use this directly. use DefaultModelVisitor
 * @see DefaultModelVisitor
 */
/**
 * @author u59611
 */
public interface ModelVisitor {

    /**
     * Post visit all group.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitAllGroup(
        AllGroup item,
        ModelVisitor visitor);

    /**
     * Pre visit all group.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitAllGroup(
        AllGroup item,
        ModelVisitor visitor);

    /**
     * Pre visit attributes list.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitAttributesList(
        AttributesList item,
        ModelVisitor visitor);

    /**
     * Post visit attributes list.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitAttributesList(
        AttributesList item,
        ModelVisitor visitor);

    /**
     * Post visit choice group.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitChoiceGroup(
        ChoiceGroup item,
        ModelVisitor visitor);

    /**
     * Pre visit choice group.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitChoiceGroup(
        ChoiceGroup item,
        ModelVisitor visitor);

    /**
     * Visit comment.
     * @param item the item
     * @param visitor the visitor
     */
    void visitComment(
        Comment item,
        ModelVisitor visitor);

    /**
     * Post visit element declaration.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitElementDeclaration(
        ElementDeclaration item,
        ModelVisitor visitor);

    /**
     * Pre visit element declaration.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitElementDeclaration(
        ElementDeclaration item,
        ModelVisitor visitor);

    /**
     * Visit extra.
     * @param item the item
     * @param visitor the visitor
     */
    void visitExtra(
        Extra item,
        ModelVisitor visitor);

    /**
     * Post visit implicit group.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitImplicitGroup(
        ImplicitGroup item,
        ModelVisitor visitor);

    /**
     * Pre visit implicit group.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitImplicitGroup(
        ImplicitGroup item,
        ModelVisitor visitor);

    /**
     * Post visit list.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitList(
        List item,
        ModelVisitor visitor);

    /**
     * Pre visit list.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitList(
        List item,
        ModelVisitor visitor);

    /**
     * Post visit optional.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitOptional(
        Optional item,
        ModelVisitor visitor);

    /**
     * Pre visit optional.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitOptional(
        Optional item,
        ModelVisitor visitor);

    /**
     * Post visit repeated.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitRepeated(
        Repeated item,
        ModelVisitor visitor);

    /**
     * Pre visit repeated.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitRepeated(
        Repeated item,
        ModelVisitor visitor);

    /**
     * Visit restriction.
     * @param item the item
     * @param visitor the visitor
     */
    void visitRestriction(
        Restriction item,
        ModelVisitor visitor);

    /**
     * Post visit sequence group.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitSequenceGroup(
        SequenceGroup item,
        ModelVisitor visitor);

    /**
     * Pre visit sequence group.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitSequenceGroup(
        SequenceGroup item,
        ModelVisitor visitor);

    /**
     * Post visit simple.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitSimple(
        Simple item,
        ModelVisitor visitor);

    /**
     * Pre visit simple.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitSimple(
        Simple item,
        ModelVisitor visitor);

    /**
     * Visit element attribute.
     * @param aSrc the src
     * @param visitor the visitor
     */
    void visitElementAttribute(
        ElementAttribute aSrc,
        ModelVisitor visitor);

    /**
     * Post visit union.
     * @param item the item
     * @param visitor the visitor
     */
    void postVisitUnion(
        Union item,
        ModelVisitor visitor);

    /**
     * Pre visit union.
     * @param item the item
     * @param visitor the visitor
     */
    void preVisitUnion(
        Union item,
        ModelVisitor visitor);

    /**
     * Visit any.
     * @param item the item
     * @param visitor the visitor
     */
    void visitAny(
        Any item,
        ModelVisitor visitor);

}
