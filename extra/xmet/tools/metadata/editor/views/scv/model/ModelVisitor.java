/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br />
 * <br />
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br />
 * <br />
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br />
 * <br />
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br />
 * <br />
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.tools.metadata.editor.views.scv.model;

/**
 * The visitor interface for the SCV model Do not implement it directly - extend
 * DefaultModelVisitor Why? - So the model can be extended without mucking up
 * your model implementation the "post" prefixed methods are called after
 * structure and "pre" prefixed ones before if these prefixes are absent ... it
 * most likely does not have any child structure rule for adding new methods: if
 * it has children, add a postVisit... and preVisit... method - add dummy
 * implementation in DefaultModelVisitor.java rule for converting plain visit...
 * to pre/postVisit leave the visit method as a preVisit... method. in addition,
 * add preVisit... and postVisit... methods as above. annotate the normal method
 * as depreciated
 * @author Nahid Akbar
 */
public interface ModelVisitor {

    /**
     * Pre visit sheet.
     * @param sheet the sheet
     */
    void preVisitSheet(
        Sheet sheet);

    /**
     * Post visit sheet.
     * @param sheet the sheet
     */
    void postVisitSheet(
        Sheet sheet);

    /**
     * Pre visit page.
     * @param page the page
     */
    void preVisitPage(
        Page page);

    /**
     * Post visit page.
     * @param page the page
     */
    void postVisitPage(
        Page page);

    /**
     * Pre visit group.
     * @param group the group
     */
    void preVisitGroup(
        Group group);

    /**
     * Post visit group.
     * @param group the group
     */
    void postVisitGroup(
        Group group);

    /**
     * Pre visit repeated group.
     * @param repeated the repeated
     */
    void preVisitRepeatedGroup(
        RepeatedGroup repeated);

    /**
     * Post visit repeated group.
     * @param repeated the repeated
     */
    void postVisitRepeatedGroup(
        RepeatedGroup repeated);

    /**
     * Pre visit labeled group.
     * @param labeledGroup the labeled group
     */
    void preVisitLabeledGroup(
        LabeledGroup labeledGroup);

    /**
     * Post visit labeled group.
     * @param labeledGroup the labeled group
     */
    void postVisitLabeledGroup(
        LabeledGroup labeledGroup);

    /**
     * Pre visit item.
     * @param item the item
     */
    void preVisitItem(
        Item item);

    /**
     * Post visit item.
     * @param item the item
     */
    void postVisitItem(
        Item item);

    /**
     * Pre visit composite item.
     * @param item the item
     */
    void preVisitCompositeItem(
        CompositeItem item);

    /**
     * Post visit composite item.
     * @param item the item
     */
    void postVisitCompositeItem(
        CompositeItem item);

    /**
     * Pre visit code block.
     * @param codeBlock the code block
     */
    void preVisitCodeBlock(
        CodeBlock codeBlock);

    /**
     * Pre visit code.
     * @param parent the parent
     * @param blockName the block name
     */
    void preVisitCode(
        CodeParent parent,
        String blockName);

    /**
     * Post visit code.
     * @param parent the parent
     * @param blockName the block name
     */
    void postVisitCode(
        CodeParent parent,
        String blockName);

    /**
     * Post visit code block.
     * @param codeBlock the code block
     */
    void postVisitCodeBlock(
        CodeBlock codeBlock);

    /* return true if to explore if block contents...false for else block */
    /* contents */
    /**
     * Pre visit if code.
     * @param ifcode the ifcode
     */
    void preVisitIfCode(
        IfCode ifcode);

    /**
     * Post visit if code.
     * @param ifcode the ifcode
     */
    void postVisitIfCode(
        IfCode ifcode);

    /**
     * Visit make visible.
     * @param makeVisible the make visible
     */
    void visitSetItemVisibleCode(
        SetItemVisibleCode makeVisible);

    /**
     * Visit set item mandatory code.
     * @param makeMamdatory the make mamdatory
     */
    void visitSetItemMandatoryCode(
        SetItemMandatoryCode makeMamdatory);

    /**
     * Visit set item valid code.
     * @param setValid the set valid
     */
    void visitSetItemValidCode(
        SetItemValidCode setValid);

    /**
     * Visit alert user code.
     * @param alertUser the alert user
     */
    void visitAlertUserCode(
        AlertUserCode alertUser);

    /**
     * Visit get path value code.
     * @param getPathValue the get path value
     */
    void visitGetPathValueCode(
        GetPathValueCode getPathValue);

    /**
     * Pre visit repeat code.
     * @param repeatCode the repeat code
     * @return the int
     */

    /**
     * Pre visit repeat code index.
     * @param repeatCode the repeat code
     */
    void preVisitRepeatCode(
        RepeatCode repeatCode);

    /**
     * Post visit repeat code.
     * @param repeatCode the repeat code
     */
    void postVisitRepeatCode(
        RepeatCode repeatCode);

    /**
     * Post visit repeat code.
     * @param setCode the set code
     */

    /**
     * Visit set code.
     * @param setCode the set code
     */
    void visitSetPathValueCode(
        SetPathValueCode setCode);

    /**
     * Visit set item code.
     * @param setItemCode the set item code
     */
    void visitSetItemValueCode(
        SetItemValueCode setItemCode);

    /**
     * Visit exec item code code.
     * @param execItemCode the exec item code
     */
    void visitExecItemCodeCode(
        ExecItemCodeCode execItemCode);

    /**
     * Pre visit repeated page.
     * @param repeated the repeated
     */
    void preVisitRepeatedPage(
        RepeatedPage repeated);

    /**
     * Post visit repeated page.
     * @param repeated the repeated
     */
    void postVisitRepeatedPage(
        RepeatedPage repeated);

    /**
     * Pre visit choices.
     * @param choices the choices
     */
    void preVisitChoices(
        Choices choices);

    /**
     * Pre visit selected choice item.
     * @param choice the choice
     * @param choices the choices
     * @param index the index
     */
    void preVisitChoiceItem(
        ChoiceItem choice,
        Choices choices,
        int index);

    /**
     * Post visit selected choice item.
     * @param choice the choice
     * @param choices the choices
     * @param index the index
     */
    void postVisitChoiceItem(
        ChoiceItem choice,
        Choices choices,
        int index);

    /**
     * Post visit choices.
     * @param choices the choices
     */
    void postVisitChoices(
        Choices choices);

    /**
     * Pre visit if then code.
     * @param aIfCode the if code
     */
    void preVisitIfThenCode(
        Code aIfCode);

    /**
     * Post visit if then code.
     * @param aCode the code
     */
    void postVisitIfThenCode(
        Code aCode);

    /**
     * Pre visit if else code.
     * @param aElseCode the else code
     */
    void preVisitIfElseCode(
        Code aElseCode);

    /**
     * Post visit if else code.
     * @param aElseCode the else code
     */
    void postVisitIfElseCode(
        Code aElseCode);

}
