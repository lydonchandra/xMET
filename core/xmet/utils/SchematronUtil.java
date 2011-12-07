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
package xmet.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import n.io.xml.SAXONUtils;
import n.reporting.Reporting;

import org.xml.sax.SAXException;

/**
 * Classes related to Schematron Processing.
 * @author Matt
 */
public class SchematronUtil {

    /**
     * Instantiates a new schematron util.
     */
    protected SchematronUtil() {

    }

    /**
     * Validate.
     * @param validationFile schematron xslt
     * @param schematronFile schematron
     * @param testFile metadata file
     * @return the string
     * @throws SAXException the sAX exception
     * @throws TransformerException the transformer exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String validate(
        final String validationFile,
        final String schematronFile,
        final String testFile)
        throws SAXException,
        TransformerException,
        IOException {
        /* Create a transform factory instance. */
        final File schValidation = new File(
            validationFile);
        final File xsltSchematron = new File(
            schematronFile);
        final File xmlTest = new File(
            testFile);

        /* Create a transformer for the stylesheet. */
        Transformer transformer = SAXONUtils.getTfactory().newTransformer(
            new StreamSource(
                xsltSchematron));

        /* Transform the source XML */
        final ByteArrayOutputStream xslOut = new ByteArrayOutputStream();
        transformer.transform(
            new StreamSource(
                schValidation),
            new StreamResult(
                xslOut));
        xslOut.close();
        transformer = SAXONUtils.getTfactory().newTransformer(
            new StreamSource(
                new ByteArrayInputStream(
                    xslOut.toByteArray())));
        final ByteArrayOutputStream resultOut = new ByteArrayOutputStream();
        transformer.transform(
            new StreamSource(
                xmlTest),
            new StreamResult(
                resultOut));
        return resultOut.toString();
    }

    /**
     * Validate.
     * @param validationSchematronFile the validation schematron file
     * @param schematronFile the schematron file
     * @param testFileBuffer the test file buffer
     * @return the string
     * @throws TransformerException the transformer exception
     */
    public static String validate(
        final String validationSchematronFile,
        final String schematronFile,
        final ByteBuffer testFileBuffer)
        throws TransformerException {
        /* Create a transform factory instance. */

        final File schValidation = new File(
            schematronFile);
        final File xsltSchematron = new File(
            validationSchematronFile);

        /* Create a transformer for the stylesheet. */
        Transformer transformer = SAXONUtils.getTfactory().newTransformer(
            new StreamSource(
                xsltSchematron));

        /* Transform the source XML */
        final ByteArrayOutputStream xslOut = new ByteArrayOutputStream();
        transformer.transform(
            new StreamSource(
                schValidation),
            new StreamResult(
                xslOut));
        try {
            xslOut.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        transformer = SAXONUtils.getTfactory().newTransformer(
            new StreamSource(
                new ByteArrayInputStream(
                    xslOut.toByteArray())));
        final ByteArrayOutputStream resultOut = new ByteArrayOutputStream();
        transformer.transform(
            new StreamSource(
                new ByteArrayInputStream(
                    testFileBuffer.array())),
            new StreamResult(
                resultOut));
        return resultOut.toString();
    }

    /**
     * Validate.
     * @param validationSchematronFile the validation schematron file
     * @param schematronFile the schematron file
     * @param testFileBuffer the test file buffer
     * @return the string
     */
    public static String validate(
        final URI validationSchematronFile,
        final URI schematronFile,
        final ByteBuffer testFileBuffer) {
        try {
            return validate(
                validationSchematronFile.getPath(),
                schematronFile.getPath(),
                testFileBuffer);
        } catch (final TransformerException e) {
            Reporting.reportUnexpected(e);
            return null;
        }
    }
}
