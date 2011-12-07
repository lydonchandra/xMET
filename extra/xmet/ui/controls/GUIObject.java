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
package xmet.ui.controls;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import n.io.CS;
import n.io.CSC;
import xmet.ClientContext;

/**
 * Root of all Controls in xMET.
 * @author shaan
 */
@CSC("GUIObject")
public abstract class GUIObject
    extends JPanel {

    /**
     * The Class AlwaysModifiedObservable.
     */
    private static final class AlwaysModifiedObservable
        extends Observable {

        /**
         * {@inheritDoc}
         */
        @Override
        public void notifyObservers(
            final Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The notifications enabled flag. */
    private transient boolean notificationsEnabled = true;

    /** The context. */
    private transient ClientContext context;
    //
    // /** The entity. */
    /* Entity entity; */
    //
    // /** The entity name. */
    // @CS
    /* String entityName = ""; */

    /** The value. */
    @CS
    private transient String value = "";

    /*
     * this is probably the most inappropriate field here
     */

    // /** The constraints. */
    // @CS
    /* String constraints; */

    /**
     * Default constructor.
     */
    public GUIObject() {
        this(null);
    }

    /**
     * Instantiates a new gUI object.
     * @param context2 the context2
     */
    public GUIObject(
        final ClientContext context2) {
        this.context = context2;
    }

    //
    // /**
    // * Sets the entity that is paired with the GUIObject.
    // *
    // * @param entity
    // * The XML entity paired with the object.
    // */
    /* public void setEntity(final Entity entity) { */
    /* this.entity = entity; */
    /* setEntityName(ModelUtils.getPath(entity)); */
    // }
    //
    // /**
    // * Returns the XML entity paired with the object.
    // *
    // * @return The XML entity paired with the object.
    // */
    /* public Entity getEntity() { */
    /* return entity; */
    // }
    //
    // /**
    // * Returns the XPath to the XML entity paired with the object if an
    // entity
    // * has been set. Otherwise returns null or a custom value set by calling
    // * setEntityName.
    // *
    // * @return The XPath for the entity paired with the object.
    // */
    /* public String getEntityName() { */
    /* return entityName; */
    // }
    //
    // /**
    // * Sets the XPath for the entity. This function should be used only when
    // * saving and loading GUIObjects from file. Use setEntity.
    // *
    // * @param entityName
    // * The XPath for the entity paired with the object.
    // */
    /* public void setEntityName(final String entityName) { */
    /* this.entityName = entityName; */
    // }

    /**
     * Returns a string representation of the value displayed.
     * @return The value displayed by the object.
     */
    public abstract String getValue();

    /**
     * Note: This must not produce any observer notifications <br />
     * Sets the value to be displayed by the object.
     * @param aValue The value displayed by the object.
     */
    public void setValue(
        final String aValue) {
        /* Reporting.log("setValue %1$s", value); */
        this.value = aValue;
    }

    // /**
    // * Returns the layout constraints for the object. The layout constraints
    /* is */
    // * based on formLayout and should not be used with any other layout.
    // *
    // * @return The formLayout constraints for the object.
    // */
    /* public String getConstraints() { */
    /* return constraints; */
    // }
    //
    // /**
    // * Sets the formLayout based constraints for the object.
    // *
    // * @param constraints
    // * The formLayout constraints for the object.
    // */
    /* public void setConstraints(final String constraints) { */
    /* this.constraints = constraints; */
    // }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone()
        throws CloneNotSupportedException {
        try {
            final GUIObject clonedObject = this.getClass().newInstance();
            final BeanInfo beanInfo = Introspector.getBeanInfo(
                this.getClass(),
                GUIObject.class.getSuperclass());
            final PropertyDescriptor[] descriptors =
                beanInfo.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++) {
                /* get from original */
                Object aValue = null;
                final Method getter = descriptors[i].getReadMethod();
                Class<?>[] paramTypes = getter.getParameterTypes();
                Object[] args = new Object[paramTypes.length];
                aValue = getter.invoke(
                    this,
                    args);
                /* set on clone */
                if (aValue != null) {
                    final Method setter = descriptors[i].getWriteMethod();
                    paramTypes = setter.getParameterTypes();
                    args = new Object[paramTypes.length];
                    args[0] = aValue;
                    setter.invoke(
                        clonedObject,
                        args);
                }
            }
            return clonedObject;
        } catch (final InstantiationException e) {
            e.printStackTrace();
            throw new AssertionError(
                e);
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
            throw new AssertionError(
                e);
        } catch (final IntrospectionException e) {
            e.printStackTrace();
            throw new AssertionError(
                e);
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
            throw new AssertionError(
                e);
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
            throw new AssertionError(
                e);
        }
    }

    /* == Observable Implementation == */
    /** The observable. */
    private transient Observable observable;

    /**
     * Adds the observer.
     * @param o the o
     */
    public void addObserver(
        final Observer o) {
        if (observable == null) {
            observable = new AlwaysModifiedObservable();
        }
        observable.addObserver(o);
    }

    /**
     * Delete observer.
     * @param o the o
     */
    public void deleteObserver(
        final Observer o) {
        if (observable != null) {
            observable.deleteObserver(o);
        }
    }

    /**
     * Notify observers.
     */
    private void notifyObservers() {
        if (observable != null) {
            observable.notifyObservers(this);
        }
    }

    /**
     * Delete observers.
     */
    public void deleteObservers() {
        if (observable != null) {
            observable.deleteObservers();
        }
    }

    /**
     * Enable notifications.
     */
    protected void enableNotifications() {
        notificationsEnabled = true;
    }

    /**
     * Disable notifications.
     */
    protected void disableNotifications() {
        notificationsEnabled = false;
    }

    /**
     * Notify observers if changed.
     */
    protected void notifyObserversIfChanged() {
        /* Reporting.log("notifyObserversIfChanged %1$s", getValue()); */
        try {
            if (notificationsEnabled) {
                final String t = getValue();
                if (t != null
                    && !t.equals(value)) {
                    /* EventQueue.invokeLater(new Runnable() { */
                    //
                    // @Override
                    /* public void run() { */
                    /* notifyObservers(); */
                    // }
                    // });
                    /* Reporting.log("Notify %1$s", getValue()); */
                    notifyObservers();
                    value = t;
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if is notification enabled.
     * @return true, if is notification enabled
     */
    protected boolean isNotificationEnabled() {
        return notificationsEnabled;
    }

    /* == Misc Helper Methods == */
    /**
     * Checks if is present.
     * @return true, if is present
     */
    public boolean isPresent() {
        final String value2 = getValue();
        return (value2 != null)
            && ((value2.trim().length() > 0));
    }

    /**
     * Gets the context.
     * @return the context
     */
    public ClientContext getContext() {
        return context;
    }

    /**
     * Sets the context.
     * @param aContext the new context
     */
    public void setContext(
        final ClientContext aContext) {
        this.context = aContext;
    }

    /**
     * Gets the internal value.
     * @return the internal value
     */
    public String getInternalValue() {

        return value;
    }
}
