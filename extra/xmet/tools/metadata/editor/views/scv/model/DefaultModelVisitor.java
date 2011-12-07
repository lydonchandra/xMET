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

/**
 * Default Implementation of Model Visitor all other classes are to extend from.
 * This should automatically takes care of xpath issues to do with repeated
 * entities. If not sure, always be sure to include a super.visit...(...) call
 * in extending visitor implementations. Note that preVisit... method should be
 * called before your extending code and postVisit after. See DataLoaderUtil for
 * an example.
 * @author Nahid Akbar
 */
public class DefaultModelVisitor
    implements
    ModelVisitor {

    /**
     * Instantiates a new default model visitor.
     */
    public DefaultModelVisitor() {
        super();
    }

    /* == Sheet Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitSheet(
        final Sheet sheet) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitSheet(
        final Sheet sheet) {

    }

    /* == Page Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitPage(
        final Page page) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitPage(
        final Page page) {

    }

    /* == Items implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitItem(
        final Item item) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitItem(
        final Item item) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitCompositeItem(
        final CompositeItem item) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {

    }

    /* == Group Implementaton == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitGroup(
        final Group group) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitGroup(
        final Group group) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitLabeledGroup(
        final LabeledGroup labeledGroup) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitLabeledGroup(
        final LabeledGroup labeledGroup) {

    }

    /* == Repeated implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        preVisitRepeatedItem(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        postVisitRepeatedItem(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatCode(
        final RepeatCode repeated) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatCode(
        final RepeatCode repeated) {
        postVisitRepeatedItem(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPage(
        final RepeatedPage repeated) {
        postVisitRepeatedItem(repeated);
    }

    /**
     * Pre visit repeated item.
     * @param <E> the element type
     * @param repeated the repeated
     * @return the int
     */
    public <E> int preVisitRepeatedItem(
        final RepeatedItem<E> repeated) {
        return 0;
    }

    /**
     * Pre visit repeated item index.
     * @param <E> the element type
     * @param repeated the repeated
     * @param index the index
     */
    public <E> void preVisitRepeatedItemIndex(
        final RepeatedItem<E> repeated,
        final int index) {

    }

    /**
     * Post visit repeated item index.
     * @param <E> the element type
     * @param repeated the repeated
     * @param index the index
     */
    public <E> void postVisitRepeatedItemIndex(
        final RepeatedItem<E> repeated,
        final int index) {

    }

    /**
     * Gets the repeated item index item.
     * @param <E> the element type
     * @param src the src
     * @param i the i
     * @return the repeated item index item
     */
    public <E> E getRepeatedItemIndexItem(
        final RepeatedItem<E> src,
        final int i) {

        return null;
    }

    /**
     * Post visit repeated item.
     * @param <E> the element type
     * @param repeated the repeated
     */
    public <E> void postVisitRepeatedItem(
        final RepeatedItem<E> repeated) {
    }

    /* == Choices Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitChoices(
        final Choices choices) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitChoiceItem(
        final ChoiceItem choice,
        final Choices choices,
        final int index) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitChoiceItem(
        final ChoiceItem choice,
        final Choices choices,
        final int index) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitChoices(
        final Choices choices) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetPathValueCode(
        final SetPathValueCode setPathValue) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitGetPathValueCode(
        final GetPathValueCode getPathValue) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitExecItemCodeCode(
        final ExecItemCodeCode execItemCode) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemMandatoryCode(
        final SetItemMandatoryCode makeMamdatory) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemValueCode(
        final SetItemValueCode setItemValue) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemValidCode(
        final SetItemValidCode setValid) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitAlertUserCode(
        final AlertUserCode alertUser) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitCodeBlock(
        final CodeBlock aCodeBlock) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitCode(
        final CodeParent aParent,
        final String aBlockName) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCode(
        final CodeParent aParent,
        final String aBlockName) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCodeBlock(
        final CodeBlock aCodeBlock) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitIfCode(
        final IfCode aIfcode) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitIfCode(
        final IfCode aIfcode) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemVisibleCode(
        final SetItemVisibleCode aMakeVisible) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedPage(
        final RepeatedPage aRepeated) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitIfThenCode(
        final Code aIfCode) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitIfThenCode(
        final Code aCode) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitIfElseCode(
        final Code aElseCode) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitIfElseCode(
        final Code aElseCode) {
        // TODO Auto-generated method stub

    }

}
