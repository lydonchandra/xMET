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
package xmet.tools.metadata.editor.views.scv.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import n.io.bin.BinarySerializationUtils;
import n.io.xml.CSXMLSerializationCodec;
import n.io.xml.JDOMXmlUtils;
import n.java.ReflectionUtils;
import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.model.Entity;
import xmet.tools.metadata.editor.views.scv.impl.AlertUserCode;
import xmet.tools.metadata.editor.views.scv.impl.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.impl.Choices;
import xmet.tools.metadata.editor.views.scv.impl.Code;
import xmet.tools.metadata.editor.views.scv.impl.CodeBlock;
import xmet.tools.metadata.editor.views.scv.impl.CodeParent;
import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.Default;
import xmet.tools.metadata.editor.views.scv.impl.EditorType;
import xmet.tools.metadata.editor.views.scv.impl.ExecItemCodeCode;
import xmet.tools.metadata.editor.views.scv.impl.GetPathValueCode;
import xmet.tools.metadata.editor.views.scv.impl.Group;
import xmet.tools.metadata.editor.views.scv.impl.GroupSubitem;
import xmet.tools.metadata.editor.views.scv.impl.IfCode;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.LabeledGroup;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.NamedItem;
import xmet.tools.metadata.editor.views.scv.impl.Page;
import xmet.tools.metadata.editor.views.scv.impl.PageSubitem;
import xmet.tools.metadata.editor.views.scv.impl.Param;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.RepeatCode;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.impl.SetItemMandatoryCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemValidCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemValueCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemVisibleCode;
import xmet.tools.metadata.editor.views.scv.impl.SetPathValueCode;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;
import xmet.tools.metadata.editor.views.scv.impl.ValidationInfo;
import xmet.tools.metadata.editor.views.scv.view.DataLoaderUtil;
import xmet.tools.metadata.editor.views.scv.view.DefaultValuesLoaderUtil;
import xmet.tools.metadata.editor.views.scv.view.PanelBuilderUtil;
import xmet.utils.BusyScreenUtil;

/**
 * Just an interface class for a lot of model manipulation stuff for SCV.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"rawtypes",
"unchecked"
})
public final class SCVUtils {

    /**
     * Instantiates a new sCV utils.
     */
    private SCVUtils() {

    }

    /* == Some global helper methods == */
    /** The scv model classes list - use this list for main reference. */
    private static Class[] scvModelClasses = {
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
    ValidationInfo.class
    };

    /**
     * The all code classes.
     */
    private static Class[] allCodeTags;
    static { /* dynamically make the list base don reflection */
        final ArrayList<Class<Code>> codeTags = new ArrayList<Class<Code>>();
        for (final Class clazz : scvModelClasses) {
            if ((clazz != Code.class)
                && ReflectionUtils.doesExendFromClass(
                    clazz,
                    Code.class)) {
                codeTags.add(clazz);
            }
        }
        allCodeTags = codeTags.toArray(new Class[0]);
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
        /**
         * Reverse compatibility hack
         */
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
        codec.includeClasses(scvModelClasses);
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
        codec.includeClasses(scvModelClasses);
        final String encodeString = codec.encodeObject(item);
        return JDOMXmlUtils.indentXML(encodeString);
    }

    /**
     * Builds the panel associated with this model item, preferably a sheet.
     * @param sheet the sheet
     */
    public static void buildPanel(
        final ModelItem sheet) {
        new PanelBuilderUtil(
            sheet);
    }

    /**
     * Initialize the sheet and build its panels and load default values and
     * whatnot.
     * @param sheet the sheet
     * @param root the root
     * @param profile the profile
     * @param context the context
     */
    public static void initializeSheet(
        final Sheet sheet,
        final Entity root,
        final Profile profile,
        final ClientContext context) {
        BusyScreenUtil.tickBusy();
        new InitializerUtil(
            sheet,
            root,
            profile,
            context);
        BusyScreenUtil.tickBusy();
        buildPanel(sheet);
        BusyScreenUtil.tickBusy();
        loadDefaultValues(sheet);
        BusyScreenUtil.tickBusy();
        executeOnPageDataLoad(sheet);
        BusyScreenUtil.tickBusy();
    }

    /**
     * Initialize item.
     * @param sheet the sheet
     * @param parent the parent
     * @param item the item
     * @param parentPath the parent path
     */
    public static void initializeItem(
        final Sheet sheet,
        final ParentItem parent,
        final ModelItem item,
        final String parentPath) {
        new InitializerUtil(
            sheet,
            parent,
            item,
            sheet.getIc().getRoot(),
            parentPath);
        executeOnPageDataLoad(item);
        /* buildSheetPanel(item); */
        /*
         * not doing the other two because we might be doing this due to there
         * being extra repeated items in the model and not in ui and then
         * loading default values and executing on load would corrupt original
         * model data
         */
        /* loadDefaultValues(item); */
        /* executeOnLoad(item); */
    }

    /**
     * Uninitialize item.
     * @param sheet the sheet
     * @param parent the parent
     * @param item the item
     * @param parentPath the parent path
     */
    public static void uninitializeItem(
        final Sheet sheet,
        final ParentItem parent,
        final ModelItem item,
        final String parentPath) {
        new UninitializerUtil(
            sheet,
            parent,
            item,
            sheet.getIc().getRoot(),
            parentPath);
    }

    /**
     * Uninitialize sheet.
     * @param sheet the sheet
     */
    public static void uninitializeSheet(
        final Sheet sheet) {
        if (sheet.getDc() != null) {
            sheet.getDc().stopValidationUpdateThread();
        }
    }

    /**
     * Retrace item.
     * @param selectedItem the selected item
     */
    public static void retraceItem(
        final ModelItem selectedItem) {
        new RetraceUtil(
            selectedItem);
    }

    /**
     * Execute on load code of the items.
     * @param sheet the sheet
     */
    public static void executeOnItemDataLoad(
        final Sheet sheet) {
        CodeExecusionTraverserUtil.forItem(
            sheet,
            "onDataLoad",
            new CodeExecutorUtil());
        if (sheet != null) {
            sheet.getIc().setOnLoadExecuted(
                true);
            if (sheet.getDc() != null) {
                sheet.getDc().reValidate(
                    sheet);
            }
        }
    }

    /**
     * Execute on page data load.
     * @param sheet the sheet
     */
    public static void executeOnPageDataLoad(
        final ModelItem sheet) {
        CodeExecusionTraverserUtil.forPage(
            sheet,
            "onInitialize",
            new CodeExecutorUtil());
        if (sheet instanceof Sheet) {
            final Sheet shit = (Sheet) sheet;
            shit.getIc().setOnLoadExecuted(
                true);
            if (shit.getDc() != null) {
                shit.getDc().reValidate(
                    shit);
            }
        }
    }

    /**
     * Load default values of items in the specified sheet.
     * @param sheet the sheet
     */
    public static void loadDefaultValues(
        final ModelItem sheet) {
        new DefaultValuesLoaderUtil(
            sheet);
    }

    /**
     * Update the ui panel by loading data from model onto it.
     * @param sheet the sheet
     */
    public static void loadDataOntoUI(
        final Sheet sheet) {
        new DataLoaderUtil(
            sheet);
    }

    /* == Misc Helper Methods == */
    /**
     * Go to the next page of the sheet.
     * @param sheet the sheet
     */
    public static void nextPage(
        final Sheet sheet) {
        if (sheet.getDc() != null) {
            sheet.getDc().nextPage();
        }
    }

    /**
     * Go to the previous page of the sheet.
     * @param sheet the sheet
     */
    public static void previousPage(
        final Sheet sheet) {
        if (sheet.getDc() != null) {
            sheet.getDc().previousPage();
        }
    }

    /**
     * Extracts the display panel of the sheet.
     * @param sheet the sheet
     * @return the sheet panel
     */
    public static JComponent getSheetPanel(
        final Sheet sheet) {
        return sheet.getDc().getDisplayPanel();
    }

    /**
     * Sets the modified flag of the sheet.
     * @param sheet the sheet
     * @param modified the modified
     */
    public static void setSheetModified(
        final Sheet sheet,
        final boolean modified) {
        if ((sheet != null)
            && (sheet.getIC() != null)) {
            sheet.getIc().setModified(
                modified);
        }
    }

    /**
     * Checks if is sheet modified flag is set.
     * @param sheet the sheet
     * @return true, if is sheet modified
     */
    public static boolean isSheetModified(
        final Sheet sheet) {
        return (sheet != null)
            && (sheet.getIC() != null)
            && sheet.getIc().isModified();
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
    // CHECKSTYLE OFF: MethodLength
    public static void accept(
        final ModelItem item,
        final SCVModelVisitor visitor,
        final CodeParent codeItem) {
        if (item != null) {
            if (item.getClass() == Choices.class) {
                final Choices src = (Choices) item;
                final int selected = visitor.preVisitChoices(src);
                if ((selected > -1)
                    && (src.getItems() != null)
                    && (selected < src.getItems().size())) {
                    visitor.preVisitSelectedChoiceItem(
                        src.getItems().get(
                            selected),
                        src,
                        selected);
                    accept(
                        src.getItems().get(
                            selected).getItem(),
                        visitor);
                    visitor.postVisitSelectedChoiceItem(
                        src.getItems().get(
                            selected),
                        src,
                        selected);
                }
                visitor.postVisitChoices(src);
            } else if (item.getClass() == CodeBlock.class) {
                final CodeBlock src = (CodeBlock) item;
                visitor.preVisitCodeBlock(
                    codeItem,
                    src);
                if (src.getCode() != null) {
                    for (final Code c : src.getCode()) {
                        accept(
                            c,
                            visitor,
                            codeItem);
                    }
                }
                visitor.postVisitCodeBlock(
                    codeItem,
                    src);
            } else if (item.getClass() == Group.class) {
                final Group src = (Group) item;
                visitor.preVisitGroup(src);
                for (final GroupSubitem i : src.getItems()) {
                    accept(
                        i,
                        visitor,
                        null);
                }
                visitor.postVisitGroup(src);
            } else if (item.getClass() == IfCode.class) {
                final IfCode src = (IfCode) item;
                if (visitor.preVisitIfCode(
                    codeItem,
                    src)) {
                    if (src.getCode() != null) {
                        accept(
                            src.getCode(),
                            visitor,
                            codeItem);
                    }
                } else {
                    if (src.getElseCode() != null) {
                        accept(
                            src.getElseCode(),
                            visitor,
                            codeItem);
                    }
                }
                visitor.postVisitIfCode(
                    codeItem,
                    src);
            } else if (item.getClass() == ExecItemCodeCode.class) {
                final ExecItemCodeCode src = (ExecItemCodeCode) item;
                visitor.visitExecItemCodeCode(
                    codeItem,
                    src);
            } else if (item.getClass() == Item.class) {
                final Item src = (Item) item;
                visitor.preVisitItem(src);
                if (src.getOnDataLoad() != null) {
                    accept(
                        src.getOnDataLoad(),
                        visitor,
                        src);
                }
                if (src.getOnDataChange() != null) {
                    accept(
                        src.getOnDataChange(),
                        visitor,
                        src);
                }
                visitor.postVisitItem(src);
            } else if (item.getClass() == CompositeItem.class) {
                final CompositeItem src = (CompositeItem) item;
                visitor.preVisitCompositeItem(src);
                if (src.getOnDataLoad() != null) {
                    accept(
                        src.getOnDataLoad(),
                        visitor,
                        src);
                }
                if (src.getOnDataChange() != null) {
                    accept(
                        src.getOnDataChange(),
                        visitor,
                        src);
                }
                visitor.postVisitCompositeItem(src);
            } else if (item.getClass() == LabeledGroup.class) {
                final LabeledGroup src = (LabeledGroup) item;
                visitor.preVisitLabeledGroup(src);
                for (final GroupSubitem i : src.getItems()) {
                    accept(
                        i,
                        visitor,
                        null);
                }
                visitor.postVisitLabeledGroup(src);
            } else if (item.getClass() == Page.class) {
                final Page src = (Page) item;
                visitor.preVisitPage(src);
                if (src.getOnInitialize() != null) {
                    accept(
                        src.getOnInitialize(),
                        visitor,
                        src);
                }
                if (src.getOnValidation() != null) {
                    accept(
                        src.getOnValidation(),
                        visitor,
                        src);
                }
                for (final PageSubitem iog : src.getItems()) {
                    accept(
                        iog,
                        visitor,
                        null);
                }
                visitor.postVisitPage(src);
            } else if (item.getClass() == RepeatCode.class) {
                final RepeatCode src = (RepeatCode) item;
                final int count = visitor.preVisitRepeatCode(
                    codeItem,
                    src);
                for (int i = 0; i < count; i++) {
                    visitor.preVisitRepeatCodeIndex(
                        codeItem,
                        src,
                        i);
                    final Code code = visitor.getRepeatedCodeIndexItem(
                        codeItem,
                        src,
                        i);
                    if (code != null) {
                        accept(
                            code,
                            visitor,
                            codeItem);
                    }
                    visitor.postVisitRepeatCodeIndex(
                        codeItem,
                        src,
                        i);
                }
                visitor.postVisitRepeatCode(
                    codeItem,
                    src);
            } else if (item.getClass() == RepeatedPage.class) {
                final RepeatedPage src = (RepeatedPage) item;
                final int count = visitor.preVisitRepeatedPage(src);
                for (int i = 0; i < count; i++) {
                    visitor.preVisitRepeatedPageIndex(
                        src,
                        i);
                    for (final PageSubitem j : visitor
                        .getRepeatedPageIndexItems(
                            src,
                            i)
                        .getItems()) {
                        accept(
                            j,
                            visitor,
                            null);
                    }
                    visitor.postVisitRepeatedPageIndex(
                        src,
                        i);
                }
                visitor.postVisitRepeatedPage(src);
            } else if (item.getClass() == RepeatedGroup.class) {
                final RepeatedGroup src = (RepeatedGroup) item;
                final int count = visitor.preVisitRepeatedGroup(src);
                for (int i = 0; i < count; i++) {
                    visitor.preVisitRepeatedGroupIndex(
                        src,
                        i);
                    for (final GroupSubitem j : visitor
                        .getRepeatedGroupIndexItems(
                            src,
                            i)
                        .getItems()) {
                        accept(
                            j,
                            visitor,
                            null);
                    }
                    visitor.postVisitRepeatedGroupIndex(
                        src,
                        i);
                }
                visitor.postVisitRepeatedGroup(src);
            } else if (item.getClass() == SetItemMandatoryCode.class) {
                final SetItemMandatoryCode src = (SetItemMandatoryCode) item;
                visitor.visitSetItemMandatoryCode(
                    codeItem,
                    src);
            } else if (item.getClass() == SetItemValueCode.class) {
                final SetItemValueCode src = (SetItemValueCode) item;
                visitor.visitSetItemValueCode(
                    codeItem,
                    src);
            } else if (item.getClass() == GetPathValueCode.class) {
                final GetPathValueCode src = (GetPathValueCode) item;
                visitor.visitGetPathValueCode(
                    codeItem,
                    src);
            } else if (item.getClass() == SetItemVisibleCode.class) {
                final SetItemVisibleCode src = (SetItemVisibleCode) item;
                visitor.visitSetItemVisibleCode(
                    codeItem,
                    src);
            } else if (item.getClass() == SetPathValueCode.class) {
                final SetPathValueCode src = (SetPathValueCode) item;
                visitor.visitSetPathValueCode(
                    codeItem,
                    src);
            } else if (item.getClass() == SetItemValidCode.class) {
                final SetItemValidCode src = (SetItemValidCode) item;
                visitor.visitSetItemValidCode(
                    codeItem,
                    src);
            } else if (item.getClass() == AlertUserCode.class) {
                final AlertUserCode src = (AlertUserCode) item;
                visitor.visitAlertUserCode(
                    codeItem,
                    src);
            } else if (item.getClass() == Sheet.class) {
                final Sheet src = (Sheet) item;
                visitor.preVisitSheet(src);
                for (final PageSubitem igp : src.getItems()) {
                    accept(
                        igp,
                        visitor,
                        null);
                }
                visitor.postVisitSheet(src);
            } else {
                Reporting.logUnexpected(
                    "%1$s",
                    item);
            }
        }
        // else {
        //
        // }
    }

    // CHECKSTYLE ON: MethodLength

    /**
     * model visitor helper method.
     * @param item the item
     * @param visitor the visitor
     */
    public static void accept(
        final ModelItem item,
        final SCVModelVisitor visitor) {
        accept(
            item,
            visitor,
            null);
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
        if (sheet.getChildren() != null) {
            final ArrayList pages = new ArrayList();
            for (int i = 0; i < sheet.getChildren().size(); i++) {
                final Object item = sheet.getChildren().get(
                    i);
                if (item instanceof Page) {
                    pages.add(item);
                    sheet.getChildren().remove(
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
            sheet.getChildren().addAll(
                pages);
        }
    }

    /** The default column span. */
    private static Map<EditorType, Integer> defaultColumnSpan =
        new HashMap<EditorType, Integer>() {

            // CHECKSTYLE OFF: MagicNumber
            /** The Constant serialVersionUID. */
            private static final long serialVersionUID = 1L;
            {
                put(
                    EditorType.Checkbox,
                    1);
                put(
                    EditorType.CheckedList,
                    5);
                put(
                    EditorType.CodelistCatalogList,
                    6);
                put(
                    EditorType.ContactInfoEditor,
                    2);
                put(
                    EditorType.DatePicker,
                    1);
                put(
                    EditorType.DateTimePicker,
                    1);
                put(
                    EditorType.DigitalContentSizeEditor,
                    1);
                put(
                    EditorType.DropDownList,
                    1);
                put(
                    EditorType.FileBrowser,
                    1);
                put(
                    EditorType.Label,
                    1);
                put(
                    EditorType.MoveOverList,
                    5);
                put(
                    EditorType.MultiLineText,
                    5);
                put(
                    EditorType.SingleLineText,
                    1);
                put(
                    EditorType.SpatialExtent,
                    20);
                put(
                    EditorType.KeywordsListEditor,
                    20);
                put(
                    EditorType.Unspecified,
                    1);
            }
            // CHECKSTYLE ON: MagicNumber
        };

    /** The default auto scale. */
    private static Map<EditorType, Boolean> defaultAutoScale =
        new HashMap<EditorType, Boolean>() {

            /** The Constant serialVersionUID. */
            private static final long serialVersionUID = 1L;

            {
                put(
                    EditorType.Checkbox,
                    false);
                put(
                    EditorType.CheckedList,
                    false);
                put(
                    EditorType.CodelistCatalogList,
                    false);
                put(
                    EditorType.ContactInfoEditor,
                    false);
                put(
                    EditorType.DatePicker,
                    false);
                put(
                    EditorType.DateTimePicker,
                    false);
                put(
                    EditorType.DigitalContentSizeEditor,
                    false);
                put(
                    EditorType.DropDownList,
                    false);
                put(
                    EditorType.FileBrowser,
                    false);
                put(
                    EditorType.Label,
                    true);
                put(
                    EditorType.MoveOverList,
                    true);
                put(
                    EditorType.MultiLineText,
                    false);
                put(
                    EditorType.SingleLineText,
                    false);
                put(
                    EditorType.SpatialExtent,
                    false);
                put(
                    EditorType.KeywordsListEditor,
                    true);
                put(
                    EditorType.Unspecified,
                    false);
            }
        };

    /**
     * Gets the preferred column span.
     * @param type the type
     * @return the preferred column span
     */
    public static int getPreferredColumnSpan(
        final EditorType type) {
        final Integer scale = defaultColumnSpan.get(type);
        if (scale == null) {
            return 1;
        } else {
            return scale;
        }
    }

    /**
     * Does auto scale.
     * @param type the type
     * @return true, if successful
     */
    public static boolean doesAutoScale(
        final EditorType type) {
        final Boolean scale = defaultAutoScale.get(type);
        if (scale == null) {
            return false;
        } else {
            return scale;
        }
    }

    /**
     * Adds the named item to the list of items - overwrites previous if an
     * existing named item is already present.
     * @param item the item
     * @param parent the parent
     */
    public static void registerNamedItem(
        final NamedItem item,
        final ParentItem parent) {
        if ((item.getName() != null)
            && (item.getName().trim().length() != 0)) {
            final String name = item.getName().trim();
            if (name.indexOf('|') == -1) {
                if ((parent != null)
                    && (parent.getIC() != null)) {
                    parent.getIC().addNamedItem(
                        name,
                        item);
                }
            } else {
                final String[] names = name.split("\\|");
                for (final String tName : names) {
                    if ((parent != null)
                        && (parent.getIC() != null)) {
                        parent.getIC().addNamedItem(
                            tName,
                            item);
                    }
                }
            }
        }
    }

    /**
     * Unregister named item.
     * @param item the item
     * @param parent the parent
     */
    public static void unregisterNamedItem(
        final NamedItem item,
        final ParentItem parent) {
        if ((item.getName() != null)
            && (item.getName().trim().length() != 0)) {
            final String name = item.getName().trim();
            if (name.indexOf('|') == -1) {
                if ((parent != null)
                    && (parent.getIC() != null)) {
                    parent.getIC().removeNamedItem(
                        name,
                        item);
                }
            } else {
                final String[] names = name.split("|");
                for (final String tName : names) {
                    if ((parent != null)
                        && (parent.getIC() != null)) {
                        parent.getIC().addNamedItem(
                            tName,
                            item);
                    }
                }
            }
        }
    }

    /**
     * Validation callback.
     * @param metadataContents the metadata contents
     * @param sheet the sheet
     */
    public static void validationCallback(
        final ByteBuffer metadataContents,
        final Sheet sheet) {
        if (sheet != null) {
            if (sheet.getDc() != null) {
                sheet.getDc().fullRevalidate(
                    metadataContents);
            }
        }
    }

}
