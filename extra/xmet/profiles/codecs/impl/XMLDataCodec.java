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
package xmet.profiles.codecs.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import n.io.net.CharacterSetUtils;
import n.io.xml.JDOMXmlUtils;

import org.jdom.Document;
import org.jdom.Element;

import xmet.profiles.ProfileSchema;
import xmet.profiles.codecs.ContentsCouldNotBeDecodedException;
import xmet.profiles.codecs.DataCodec;
import xmet.profiles.codecs.DefaultCodec;
import xmet.profiles.model.Entity;

/**
 * Plain XML Data codec.
 * @author Nahid Akbar
 */
public class XMLDataCodec
    implements
    DataCodec,
    DefaultCodec {

    /** The xml encoder. */
    private XMLEncoder xmlEncoder;

    /** The xml decoder. */
    private XMLDecoder xmlDecoder;

    /**
     * Instantiates a new xML data codec.
     * @param encoder the encoder
     * @param decoder the decoder
     */
    public XMLDataCodec(
        final XMLEncoder encoder,
        final XMLDecoder decoder) {
        super();
        this.setXmlEncoder(encoder);
        this.setXmlDecoder(decoder);
    }

    /**
     * Instantiates a new xML data codec.
     */
    public XMLDataCodec() {
        this(new XMLEncoder(), new XMLDecoder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer encodeModelContentsToXML(
        final Entity profileModelRoot,
        final Map<String, ProfileSchema> profileSchemas) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(
            baos);
        final Element e = getXmlEncoder().encodeData(
            profileModelRoot,
            profileSchemas);
        if (e != null) {
            final Document doc = new Document(
                e);
            ps.print(JDOMXmlUtils.xmlFromDocument(doc));
            ps.close();
        }
        return ByteBuffer.wrap(baos.toByteArray());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decodeModelContentsFromXML(
        final Entity profileModelRoot,
        final ByteBuffer dataContents)
        throws ContentsCouldNotBeDecodedException {
        final org.jdom.Document document =
            JDOMXmlUtils.documentFromXml(CharacterSetUtils.decodeBuffer(
                dataContents,
                "UTF-8"));
        if (document != null) {
            final Element rootElement = document.getRootElement();
            getXmlDecoder().loadData(
                profileModelRoot,
                rootElement);
        } else {
            throw new ContentsCouldNotBeDecodedException(
                "Failed parsing document");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFileFormatAllowed(
        final String fileName) {
        return ((fileName != null) && fileName.endsWith(".xml"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FileFilter> getFileFormatFiltersList() {
        return Arrays.asList((FileFilter) new FileNameExtensionFilter(
            "Xml Metadata Files",
            "xml"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer decodeFileContents(
        final ByteBuffer dataContents) {
        return dataContents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer encodeFileContents(
        final ByteBuffer xmlContents) {
        return xmlContents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "xml";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPriority() {
        return 0;
    }

    /**
     * Gets the xml encoder.
     * @return the xml encoder
     */
    public XMLEncoder getXmlEncoder() {
        return xmlEncoder;
    }

    /**
     * Sets the xml encoder.
     * @param aXmlEncoder the new xml encoder
     */
    public void setXmlEncoder(
        final XMLEncoder aXmlEncoder) {
        xmlEncoder = aXmlEncoder;
    }

    /**
     * Gets the xml decoder.
     * @return the xml decoder
     */
    public XMLDecoder getXmlDecoder() {
        return xmlDecoder;
    }

    /**
     * Sets the xml decoder.
     * @param aXmlDecoder the new xml decoder
     */
    public void setXmlDecoder(
        final XMLDecoder aXmlDecoder) {
        xmlDecoder = aXmlDecoder;
    }

}
