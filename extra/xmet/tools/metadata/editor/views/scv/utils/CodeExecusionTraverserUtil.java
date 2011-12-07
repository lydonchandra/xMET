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

import java.lang.reflect.Field;
import java.security.InvalidParameterException;

import n.java.ReflectionUtils;
import xmet.tools.metadata.editor.views.scv.impl.Code;
import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.Page;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedPage;

/**
 * Goes through all the items in a sheet, extracts their code elements of the
 * given field name and executes it.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class CodeExecusionTraverserUtil
    extends DefaultModelVisitor {

    /** The field. */
    private final Field field;

    /** The executor used to execute the code. */
    private final CodeExecutorUtil executor;

    /** The executor used to execute the code. */
    private final Class typeClass;

    /**
     * Instantiates a new code execusion traverser.
     * @param sheet the sheet
     * @param fieldName the field name
     * @param aExecutor the executor
     * @param clazz the clazz
     */
    public CodeExecusionTraverserUtil(
        final ModelItem sheet,
        final String fieldName,
        final CodeExecutorUtil aExecutor,
        final Class clazz) {

        this.executor = aExecutor;
        typeClass = clazz;
        field = ReflectionUtils.getClassFieldByName(
            fieldName,
            clazz);
        if (field == null) {
            throw new InvalidParameterException(
                "invalid field name specified");
        }
        SCVUtils.accept(
            sheet,
            this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitItem(
        final Item item) {
        super.postVisitItem(item);
        if (typeClass == Item.class) {
            try {
                final Code code = (Code) ReflectionUtils.getFieldValue(
                    field,
                    item);
                if (code != null) {
                    executor.execute(
                        item.getIc().getSheet(),
                        item,
                        code);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {
        super.postVisitCompositeItem(item);
        if (typeClass == Item.class) {
            try {
                final Code code = (Code) ReflectionUtils.getFieldValue(
                    field,
                    item);
                if (code != null) {
                    executor.execute(
                        item.getIc().getSheet(),
                        item,
                        code);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitPage(
        final Page page) {
        super.postVisitPage(page);
        if (typeClass == Page.class) {
            try {
                final Code code = (Code) ReflectionUtils.getFieldValue(
                    field,
                    page);
                if (code != null) {
                    executor.execute(
                        page.getIc().getSheet(),
                        page,
                        code);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPage(
        final RepeatedPage repeated) {
        super.postVisitRepeatedPage(repeated);
        if (typeClass == Page.class) {
            try {
                final Code code = (Code) ReflectionUtils.getFieldValue(
                    field,
                    repeated);
                if (code != null) {
                    executor.execute(
                        repeated.getIc().getSheet(),
                        repeated,
                        code);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * For item.
     * @param sheet the sheet
     * @param fieldName the field name
     * @param executor the executor
     * @return the code execusion traverser util
     */
    public static CodeExecusionTraverserUtil forItem(
        final ModelItem sheet,
        final String fieldName,
        final CodeExecutorUtil executor) {
        return new CodeExecusionTraverserUtil(
            sheet,
            fieldName,
            executor,
            Item.class);
    }

    /**
     * For page.
     * @param sheet the sheet
     * @param fieldName the field name
     * @param executor the executor
     * @return the code execusion traverser util
     */
    public static CodeExecusionTraverserUtil forPage(
        final ModelItem sheet,
        final String fieldName,
        final CodeExecutorUtil executor) {
        return new CodeExecusionTraverserUtil(
            sheet,
            fieldName,
            executor,
            Page.class);
    }
}
