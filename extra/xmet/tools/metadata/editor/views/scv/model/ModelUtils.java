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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import n.io.bin.BinarySerializationUtils;
import n.io.xml.CSXMLSerializationCodec;
import n.io.xml.JDOMXmlUtils;
import n.java.ReflectionUtils;
import xmet.tools.metadata.editor.views.scv.designer.XPathExtractionUtil;

/**
 * Just an interface class for a lot of model manipulation stuff for SCV.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"rawtypes",
"unchecked"
})
/**
 * The Class ModelUtils.
 */
public final class ModelUtils {

    /**
     * Instantiates a new model utils.
     */
    private ModelUtils() {

    }

    /* == Some global helper methods == */
    /** The scv model classes list - use this list for main reference. */
    private static final Class[] SCV_MODEL_CLASSES = {
    Sheet.class,
    Page.class,
    Group.class,
    Code.class,
    CodeBlock.class,
    Default.class,
    IfCode.class,
    SetItemVisibleCode.class,
    Param.class,
    RepeatCode.class,
    LabeledGroup.class,
    SetPathValueCode.class,
    SetItemValueCode.class,
    Item.class,
    RepeatedGroup.class,
    RepeatedPage.class,
    CompositeItem.class,
    GetPathValueCode.class,
    Choices.class,
    ChoiceItem.class,
    SetItemMandatoryCode.class,
    ExecItemCodeCode.class,
    SetItemValidCode.class,
    AlertUserCode.class,
    ValidationInformation.class
    };

    /**
     * The all code classes.
     */
    private static Class[] allCodeTags;
    static { /* dynamically make the list base don reflection */
        final ArrayList<Class<Code>> codeTags = new ArrayList<Class<Code>>();
        for (final Class clazz : SCV_MODEL_CLASSES) {
            if ((clazz != Code.class)
                && ReflectionUtils.doesExendFromClass(
                    clazz,
                    Code.class)) {
                codeTags.add(clazz);
            }
        }
        ModelUtils.allCodeTags = codeTags.toArray(new Class[codeTags.size()]);
        Arrays.sort(
            allCodeTags,
            new Comparator<Class>() {

                @Override
                public int compare(
                    final Class o1,
                    final Class o2) {
                    return o1.getSimpleName().compareTo(
                        o2.getSimpleName());
                }
            });
    }

    /**
     * Load a SCV Sheet from string contents (xml).
     * @param contents the contents
     * @return the sheet
     */
    public static Sheet loadSheetFromContents(
        final String contents) {
        return (Sheet) modelItemFromString(contents);
    }

    /**
     * Convert SCV sheet into string (xml).
     * @param sheet the sheet
     * @return the string
     */
    public static String sheetToString(
        final Sheet sheet) {
        return modelItemToString(sheet);
    }

    /**
     * Load model items from contents.
     * @param contents the contents
     * @return the model item
     */
    public static ModelItem modelItemFromString(
        final String contents) {
        final String cnt = contents.replaceAll(
            "xmet\\"
                + ".tools\\"
                + ".metadata\\"
                + ".editor\\"
                + ".views\\"
                + ".semiCustom"
                + ".model\\"
                + ".ValidationInformation",
            "validationInfo");
        final CSXMLSerializationCodec codec = new CSXMLSerializationCodec();
        codec.setPrintClasses(false);
        codec.includeClasses(SCV_MODEL_CLASSES);
        return (ModelItem) codec.decodeObject(cnt);
    }

    /**
     * Model item to string.
     * @param item the item
     * @return the string
     */
    public static String modelItemToString(
        final ModelItem item) {
        final CSXMLSerializationCodec codec = new CSXMLSerializationCodec();
        codec.setPrintClasses(false);
        codec.includeClasses(SCV_MODEL_CLASSES);
        final String encodeString = codec.encodeObject(item);
        return JDOMXmlUtils.indentXML(encodeString);
    }

    /* == Visitor == */
    /**
     * Implementation of model visitor accept method - kept at a central place
     * for easy modification Although sorta violates oop principles, this does
     * not make the model code messy and does not create circular dependency
     * between the model and the visitor which is more messy to fix when need to
     * change or extend model.
     * @param item the item
     * @param visitor the visitor
     * @param codeItem the code item
     */
    public static void accept(
        final ModelItem item,
        final ModelVisitor visitor,
        final CodeParent codeItem) {
        item.accept(visitor);
    }

    /**
     * model visitor helper method.
     * @param item the item
     * @param visitor the visitor
     */
    public static void accept(
        final ModelItem item,
        final ModelVisitor visitor) {
        item.accept(visitor);
    }

    /* == Clonning == */
    /**
     * Clone method. Put here for one place modification.
     * @param item the item
     * @return the object
     */
    public static ModelItem clone(
        final ModelItem item) {
        if (item != null) {
            return (ModelItem) BinarySerializationUtils
                .simpleCloneSerializableObject(item);
        }
        return null;
    }

    /**
     * Sort sheet pages.
     * @param sheet the sheet
     */
    public static void sortSheetPages(
        final ParentItem sheet) {
        if (sheet.getItems() != null) {
            final ArrayList pages = new ArrayList();
            for (int i = 0; i < sheet.getItems().size(); i++) {
                final Object item = sheet.getItems().get(
                    i);
                if (item instanceof Page) {
                    pages.add(item);
                    sheet.getItems().remove(
                        item);
                    i--;
                }
                if (item instanceof ParentItem) {
                    sortSheetPages((ParentItem) item);
                }
            }
            if (pages.size() > 0) {
                Collections.sort(
                    pages,
                    new Comparator<Page>() {

                        @Override
                        public int compare(
                            final Page o1,
                            final Page o2) {

                            if ((o1).getOrder() > (o2).getOrder()) {
                                return 1;
                            } else if ((o1).getOrder() == (o2).getOrder()) {
                                return 0;
                            } else {
                                return -1;
                            }
                        }
                    });
            }
            sheet.getItems().addAll(
                pages);
        }
    }

    /**
     * Extract x paths.
     * @param sheet the sheet
     * @return the array list
     */
    public static ArrayList<String> extractXPaths(
        final Sheet sheet) {
        return (new XPathExtractionUtil(
            sheet)).getXpaths();
    }

    /**
     * Gets the all code classes.
     * @return the all code classes
     */
    public static Class[] getAllCodeTags() {
        return allCodeTags.clone();
    }

    /**
     * Gets the scv model classes.
     * @return the scv model classes
     */
    public static Class[] getScvModelClasses() {
        return SCV_MODEL_CLASSES.clone();
    }
}
