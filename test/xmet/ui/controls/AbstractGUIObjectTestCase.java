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

import java.util.Observable;
import java.util.Observer;

import junit.framework.TestCase;
import n.java.ReflectionUtils;
import n.reporting.Reporting;
import n.ui.SwingUtils;
import xmet.Client;

/**
 * This tests generic aspects of a GUIObject.
 * @author Nahid Akbar
 */
public abstract class AbstractGUIObjectTestCase
    extends TestCase
    implements
    Observer {

    /** The Constant allowUserTestCases. */
    static final boolean ALLOW_USER_TEST_CASES = false;

    /** The test class. */
    @SuppressWarnings("rawtypes")
    private final Class testClass;

    /** The state. */
    private TestStates state = TestStates.INITIAL;

    /** The instance. */
    private GUIObject instance = null;

    /** The observers notified. */
    private boolean observersNotified = false;

    /**
     * Instantiates a new generic gui object test case.
     * @param clazz the clazz
     */
    @SuppressWarnings("rawtypes")
    public AbstractGUIObjectTestCase(
        final Class clazz) {
        super(clazz
            + " test case");
        testClass = clazz;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp()
        throws Exception {
        state = TestStates.INITIAL;
        try {
            setInstance((GUIObject) testClass.newInstance());
        } catch (final Exception e) {
            Client.preInitialize(new String[0]);
            setInstance((GUIObject) ReflectionUtils.getNewInstanceOfClass(
                testClass,
                Client.getContext()));
        }
        getInstance().addObserver(
            this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown()
        throws Exception {
        state = null;
        setInstance(null);
    }

    /**
     * Tests the initial state of the GUIObject. It should return ""
     */
    public void testInitial() {
        assertNotNull(getInstance().getValue());
        assertEquals(
            "",
            getInstance().getValue());
    }

    /**
     * Test set value.
     */
    public void testSetValue() {
        // CHECKSTYLE OFF: MagicNumber
        state = TestStates.VALUE_SET;
        try {
            for (int i = 0; i < 16; i++) {
                /* test example value */
                String exampleValue;
                exampleValue = getExampleValue();
                getInstance().setValue(
                    exampleValue);
                assertNotNull(getInstance().getValue());
                assertEquals(
                    exampleValue,
                    getInstance().getValue());
                Reporting.logExpected(
                    "%1$s",
                    exampleValue);
                /* test value reseting */
                getInstance().setValue(
                    null);
                testInitial();
            }
        } catch (final Exception e) {
            assertFalse(true);
            e.printStackTrace();
        }
        state = TestStates.INITIAL;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Test enter value.
     */
    public void testEnterValue() {
        // CHECKSTYLE OFF: MagicNumber
        state = TestStates.VALUE_ENTER;
        if (ALLOW_USER_TEST_CASES) {
            try {
                final String[] changeValueMethods = getChangeValueMethods();

                for (String string : changeValueMethods) {
                    /* expected value */
                    String expectedValue = null;
                    if (string.indexOf('|') >= 0) {
                        expectedValue =
                            string.substring(string.lastIndexOf('|') + 1);
                        string = string.substring(
                            0,
                            string.lastIndexOf('|'));
                    }
                    /* test null value */
                    getInstance().setValue(
                        null);
                    testInitial();
                    /* test value entering */
                    setObserversNotified(false);
                    SwingUtils.DIALOG.wrapContainerAndShow(
                        getInstance(),
                        string
                            + " and close the window",
                        800,
                        600,
                        true);
                    final String value = getInstance().getValue();
                    if (expectedValue != null
                        && expectedValue.length() > 0) {
                        assertEquals(
                            expectedValue,
                            value);
                    } else {
                        assertTrue(!value.equals(""));
                    }
                    assertTrue(isObserversNotified());
                    /* test value reseting */
                    getInstance().setValue(
                        null);
                    testInitial();
                }
                if (changeValueMethods.length != 0) {

                    /* test null value */
                    getInstance().setValue(
                        null);
                    testInitial();

                    /* test value entering */
                    setObserversNotified(false);
                    getInstance().setValue(
                        getExampleValue());
                    SwingUtils.DIALOG.wrapContainerAndShow(
                        getInstance(),
                        "Please change the value to nothing"
                            + " and close the window",
                        800,
                        600,
                        true);
                    final String value = getInstance().getValue();

                    assertEquals(
                        value,
                        "");

                    assertTrue(isObserversNotified());
                    /* test value reseting */
                    getInstance().setValue(
                        null);
                    testInitial();
                }
            } catch (final Exception e) {
                assertFalse(true);
                e.printStackTrace();
            }
        }
        state = TestStates.INITIAL;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Gets the change value methods.
     * @return the change value methods
     */
    protected abstract String[] getChangeValueMethods();

    /**
     * Gets the example value.
     * @return the example value
     */
    protected abstract String getExampleValue();

    /* == Observer Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(
        final Observable o,
        final Object arg) {
        assertEquals(
            "Improper Notification Callback Test",
            state,
            TestStates.VALUE_ENTER);
        /* Reporting.log("%1$s", instance.getValue()); */
        setObserversNotified(true);
    }

    /**
     * @return the instance
     */
    public GUIObject getInstance() {
        return instance;
    }

    /**
     * @param aInstance the instance to set
     */
    public void setInstance(
        final GUIObject aInstance) {
        this.instance = aInstance;
    }

    /**
     * @return the observersNotified
     */
    private boolean isObserversNotified() {
        return observersNotified;
    }

    /**
     * @param aObserversNotified the observersNotified to set
     */
    private void setObserversNotified(
        final boolean aObserversNotified) {
        this.observersNotified = aObserversNotified;
    }
}
