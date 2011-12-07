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
package xmet.tools.metadata.editor.views.custom;

import java.util.Observable;
import java.util.Observer;

import xmet.profiles.model.Entity;
import xmet.profiles.model.Settable;
import xmet.ui.controls.GUIObject;

/**
 * GUIObjectToEntityMapper is currently a wrapper class for the
 * <code>mapGUIObjectsFromRootPage</code> method.
 * @author Ma'aadh
 */
public final class GUIObjectToEntityMapper {

    /**
     * Instantiates a new gUI object to entity mapper.
     */
    private GUIObjectToEntityMapper() {

    }

    /**
     * ModelUpdater.
     */
    public static class ModelUpdater
        implements
        Observer {

        /** The gui object. */
        private final GUIObject guiObject;

        /** The setable. */
        private final Settable setable;

        /**
         * Instantiates a new model updater.
         * @param aGuiObject the gui object
         * @param aSetable the setable
         */
        public ModelUpdater(
            final GUIObject aGuiObject,
            final Settable aSetable) {
            this.guiObject = aGuiObject;
            this.setable = aSetable;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void update(
            final Observable o,
            final Object arg) {
            setable.setValue(guiObject.getValue());
        }

    }

    /**
     * <code>mapGUIObjectsFromRootPage</code> goes through each
     * <code>GUIObject</code> in each <code>Page</code> in the
     * <code>rootPage</code> <code>Page</code>'s <code>children</code> and sets
     * the <code>GUIObject</code>s' Entity field based on the "xpath" XML
     * element associated with it. The "xpath" XML element is a child-element of
     * the guiobject element in the xmetview file that describes the CustomView
     * to which the <code>GUIObject</code> belongs. It also sets the value of
     * the GUIObject based on the value of the Entity. This method is
     * intentionally package-level (i.e. not public, private or protected).
     * @param rootEntity the root entity
     * @param rootPage The page whose child pages have the
     *            <code>GUIObject</code>s to map to <code>Entity</code>s.
     */
    static void mapGUIObjectsFromRootPage(
        final Entity rootEntity,
        final Page rootPage) {
        // /*TODO: refactor so this method doesn't have to be static. */
        /* for (final Page childPage : rootPage.getChildren()) { */
        /* mapGUIObjectsFromRootPage(rootEntity, childPage); */
        /* for (final GUIObject guiObject : childPage.getGuiObjects()) { */
        /* if (guiObject.getEntityName().length() < 1) { */
        /* continue; */
        // }
        /* System.out.println("entity=" + guiObject.getEntityName()); */
        /* final Entity entity = ModelUtils.hardTraceXPath(rootEntity, */
        /* guiObject.getEntityName()); */
        /* if (entity == null) { */
        /* continue; */
        // }
        //
        // /*guiObject.setEntity((Entity)entity); */
        /* final Settable setable = ModelUtils.getSetable(entity); */
        /* if (setable == null) { */
        /* continue; */
        // }
        //
        /* guiObject.addObserver(new ModelUpdater(guiObject, setable)); */
        // }
        // }
    }

    /**
     * Load data.
     * @param rootEntity the root entity
     * @param rootPage the root page
     */
    static void loadData(
        final Entity rootEntity,
        final Page rootPage) {
        /* TODO: refactor so this method doesn't have to be static. */
        /* for (final Page childPage : rootPage.getChildren()) { */
        /* mapGUIObjectsFromRootPage(rootEntity, childPage); */
        /* for (final GUIObject guiObject : childPage.getGuiObjects()) { */
        /* if (guiObject.getEntityName().length() < 1) { */
        /* continue; */
        // }
        /* System.out.println("entity=" + guiObject.getEntityName()); */
        /* final Entity entity = ModelUtils.hardTraceXPath(rootEntity, */
        /* guiObject.getEntityName()); */
        /* if (entity == null) { */
        /* continue; */
        // }
        //
        // /*guiObject.setEntity((Entity)entity); */
        /* final Settable setable = ModelUtils.getSetable(entity); */
        /* if (setable == null) { */
        /* continue; */
        // }
        /* System.out.println("setable=" + setable.getValue()); */
        /* guiObject.setValue(setable.getValue()); */
        /* System.out.println("guiobject=" + guiObject.getValue()); */
        // }
        // }
    }
}
