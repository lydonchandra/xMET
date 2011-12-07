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
package xmet.tools.metadata.manager.gn;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import n.io.net.CharacterSetUtils;
import n.io.net.XMLWSConnection;
import n.io.net.XMLWSSessionInformation;
import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;

import xmet.ClientContext;
import xmet.profiles.codecs.DataCodec;
import xmet.tools.metadata.manager.MetadataFile;

/**
 * Main class that handles interaction with Geonetwork through apache HTTP
 * client.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"unused",
"rawtypes"
})
public class Geonetwork
    implements
    MetadataFile {

    /** The Constant HTTP_RESPONSE_SUCCESS. */
    private static final int HTTP_RESPONSE_SUCCESS = 200;

    /** The Constant CSW_VER_2_0_2. */
    private static final String CSW_VER_2_0_2 = "2.0.2";

    /* Current State */
    /**
     * The Enum State.
     */
    public enum State {

        /** The START. */
        START,
        /** The LOGGIN g_ in. */
        LOGGING_IN,
        /** The CONNECTED. */
        CONNECTED,
        /** The BUSY. */
        BUSY
    }

    /** The state. */
    private State state = State.START;

    // /////////////////////////////////////////////////////////////////////////
    /* authentication details */
    /** The geonetwork user details. */
    private final GeonetworkDetails geonetworkUserDetails;

    /** The client. */
    // ClientContext client;

    /** The session information. */
    private final XMLWSSessionInformation session =
        new XMLWSSessionInformation();

    /** The files. */
    private MetadataFile[] files;

    // /////////////////////////////////////////////////////////////////////////

    /**
     * Instantiates a new geonetwork.
     * @param data the data
     * @param client the client
     */
    public Geonetwork(
        final GeonetworkDetails data,
        final ClientContext client) {
        geonetworkUserDetails = data;
        if (data == null) {
            throw new IllegalArgumentException();
        }
        // this.client = client;
    }

    // /////////////////////////////////////////////////////////////////////////
    // -- Helper Methods
    /**
     * returns the full service url from service name e.g. "xml.user.login" to
     * "http://xyz/gn/srv/en/xml.user.login"
     * @param service the service name
     * @return the service url
     */
    public String getServiceUrl(
        final String service) {
        return geonetworkUserDetails.getUrl()
            + geonetworkUserDetails.getServicePrefix()
            + service;
    }

    /**
     * Send post request.
     * @param request the request
     * @param encoding the encoding
     * @param service the service
     * @return the http response
     */
    private ByteBuffer sendPostRquest(
        final Element request,
        final String encoding,
        final String service) {
        final String url = getServiceUrl(service);
        Reporting.logExpected(url);
        try {
            final XMLWSConnection connection = new XMLWSConnection(
                new URL(
                    url),
                session);
            final String postData = JDOMXmlUtils.xmlFromDocument(new Document(
                request));
            final ByteBuffer result =
                connection.executePost(ByteBuffer.wrap(postData.getBytes()));
            try {
                Reporting.logExpected(
                    "Response %1$s",
                    new String(
                        result.array()));
            } catch (final Exception e) {
                Reporting.logExpected(
                    "ResponseEx %1$s",
                    result);
            }
            return result;
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Shows error message.
     * @param errorMessage the error message
     */
    private void showErrorMessage(
        final String errorMessage) {
        Reporting.reportUnexpected(
            "%1$s",
            errorMessage.replaceAll(
                "\\<\\!.*\\>",
                ""));
    }

    /*
     * == Geonetwork connection class to do all the networking stuff
     * ================
     */
    /** The cswns. */
    private final Namespace cswns = Namespace.getNamespace(
        "csw",
        "http://www.opengis.net/cat/csw/2.0.2");

    // /** The owsns. */
    // Namespace owsns = Namespace.getNamespace(
    // "ows", "http://www.opengis.net/ows");

    /** The ogcns. */
    private final Namespace ogcns = Namespace.getNamespace(
        "ogc",
        "http://www.opengis.net/ogc");

    // /** The gmlns. */
    // Namespace gmlns = Namespace.getNamespace(
    // "gml", "http://www.opengis.net/gml");

    /** The dcns. */
    private final Namespace dcns = Namespace.getNamespace(
        "dc",
        "http://purl.org/dc/elements/1.1/");

    /** The dctns. */
    private final Namespace dctns = Namespace.getNamespace(
        "dct",
        "http://purl.org/dc/terms/");

    /* geonetwork specific */
    /**
     * Send login request.
     * @return true, if successful
     */
    private boolean sendLoginRequest() {
        /* Create request XML */
        state = State.LOGGING_IN;
        final Element request =
            new Element(
                "request")
                .addContent(
                    new Element(
                        "username").setText(geonetworkUserDetails.getUser()))
                .addContent(
                    new Element(
                        "password").setText(geonetworkUserDetails.getPass()));
        /* send request */
        ByteBuffer result;
        result = sendPostRquest(
            request,
            "application/xml",
            "xml.user.login");
        if ((result) != null) {
            /* interpret results */
            final boolean success =
                session.getResponseCode() == HTTP_RESPONSE_SUCCESS;
            if (success) {
                state = State.CONNECTED;
            } else {
                state = State.START;
                /* String s = new String(result.array()); */
                /* showErrorMessage(s); */
            }
            return success;

        }
        state = State.START;
        return false;

    }

    /**
     * Send logout request.
     */
    private void sendLogoutRequest() {
        final Element request = new Element(
            "request");
        sendPostRquest(
            request,
            "application/xml",
            "xml.user.logout");
        state = State.START;
    }

    /* geonetwork's implementation of CSW specific */
    /**
     * Send csw search request.
     * @param term the term
     * @return the element
     */
    public Element sendCSWSearchRequest(
        final String term) {

        final Element cswGetRecords = new Element(
            "GetRecords",
            cswns);
        // {
        /* build request */
        cswGetRecords.setAttribute(
            "REQUEST",
            "GetRecords");
        cswGetRecords.setAttribute(
            "service",
            "CSW");
        cswGetRecords.setAttribute(
            "version",
            CSW_VER_2_0_2);
        cswGetRecords.setAttribute(
            "resultType",
            "results");
        cswGetRecords.setAttribute(
            "maxRecords",
            "500");
        final Element cswQuery = new Element(
            "Query",
            cswns);
        // {
        cswQuery.setAttribute(
            "typeNames",
            "csw:Record");
        final Element cswConstraint = new Element(
            "Constraint",
            cswns);
        // {
        cswConstraint.setAttribute(
            "version",
            "1.1.0");
        final Element ogcFilter = new Element(
            "Filter",
            ogcns);
        // {
        final Element ogcPropertyIsLike = new Element(
            "PropertyIsLike",
            ogcns);
        ogcPropertyIsLike.setAttribute(
            "wildCard",
            "*");
        ogcPropertyIsLike.setAttribute(
            "singleChar",
            "_");
        ogcPropertyIsLike.setAttribute(
            "escapeChar",
            "\\\\");
        // {
        final Element ogcPropertyName = new Element(
            "PropertyName",
            ogcns);
        // {
        ogcPropertyName.setText("AnyText");
        // }
        ogcPropertyIsLike.addContent(ogcPropertyName);
        final Element ogcLiteral = new Element(
            "Literal",
            ogcns);
        // {
        if ((term == null)
            || (term.trim().length() == 0)) {
            ogcLiteral.setText("*");
        } else {
            ogcLiteral.setText(String.format(
                "*%1$s*",
                term));
        }
        // }
        ogcPropertyIsLike.addContent(ogcLiteral);

        // }
        ogcFilter.addContent(ogcPropertyIsLike);
        // }
        // {
        final Element ogcPropertyIsLike2 = new Element(
            "PropertyIsLike",
            ogcns);
        ogcPropertyIsLike2.setAttribute(
            "wildCard",
            "*");
        ogcPropertyIsLike2.setAttribute(
            "singleChar",
            "_");
        ogcPropertyIsLike2.setAttribute(
            "escapeChar",
            "\\\\");
        // {
        final Element ogcPropertyName2 = new Element(
            "PropertyName",
            ogcns);
        // {
        ogcPropertyName2.setText("_schema");
        // }
        ogcPropertyIsLike2.addContent(ogcPropertyName2);
        final Element ogcLiteral2 = new Element(
            "Literal",
            ogcns);
        // {
        ogcLiteral2.setText("iso19139*");
        // }
        ogcPropertyIsLike2.addContent(ogcLiteral2);

        // }
        ogcFilter.addContent(ogcPropertyIsLike2);
        // }
        cswConstraint.addContent(ogcFilter);
        // }
        cswQuery.addContent(cswConstraint);
        // }
        cswGetRecords.addContent(cswQuery);
        // }

        Reporting.logExpected(JDOMXmlUtils
            .indentedXmlFromElement(cswGetRecords));
        ByteBuffer result;
        result = sendPostRquest(
            cswGetRecords,
            "application/xml",
            "csw");
        if (result != null) {
            return returnAndConsumeResult(result);
        }
        return null;
    }

    /**
     * Send csw get record by id request.
     * @param id the id
     * @return the element
     */
    public Element sendCSWGetRecordByIdRequest(
        final String id) {

        final Element cswGetRecordById = new Element(
            "GetRecordById",
            cswns);
        // {
        /* build request */
        cswGetRecordById.setAttribute(
            "REQUEST",
            "GetRecordById");
        cswGetRecordById.setAttribute(
            "service",
            "CSW");
        cswGetRecordById.setAttribute(
            "version",
            CSW_VER_2_0_2);
        cswGetRecordById.setAttribute(
            "outputSchema",
            "http://www.isotc211.org/2005/gmd");
        final Element cswId = new Element(
            "Id",
            cswns);
        // {
        cswId.setText(id);
        // }
        cswGetRecordById.addContent(cswId);
        final Element cswElementSetName = new Element(
            "ElementSetName",
            cswns);
        // {
        cswElementSetName.setText("full");
        // }
        cswGetRecordById.addContent(cswElementSetName);
        // }

        Reporting.logExpected(
            "Sending %1$s",
            JDOMXmlUtils.indentedXmlFromElement(cswGetRecordById));
        ByteBuffer result;
        result = sendPostRquest(
            cswGetRecordById,
            "application/xml",
            "csw");
        if ((result) != null) {
            return returnAndConsumeResult(result);
        }
        return null;
    }

    /**
     * Send csw transaction update request.
     * @param uuid the uuid
     * @param contents the contents
     * @return the element
     */
    public Element sendCSWTransactionUpdateRequest(
        final String uuid,
        final ByteBuffer contents) {
        final Element cswTransaction = new Element(
            "Transaction",
            cswns);
        // {
        /* build request */
        cswTransaction.setAttribute(
            "REQUEST",
            "Transaction");
        cswTransaction.setAttribute(
            "service",
            "CSW");
        cswTransaction.setAttribute(
            "version",
            CSW_VER_2_0_2);

        final Element cswUpdate = new Element(
            "Update",
            cswns);
        // {
        final Element gmdMdMetadata = JDOMXmlUtils.elementFromXml(new String(
            contents.array()));

        cswUpdate.addContent(gmdMdMetadata);

        final Element cswConstraint = new Element(
            "Constraint",
            cswns);
        // {
        cswConstraint.setAttribute(
            "version",
            "1.1.0");
        final Element ogcFilter = new Element(
            "Filter",
            ogcns);
        // {
        final Element ogcPropertyIsLike = new Element(
            "PropertyIsLike",
            ogcns);
        ogcPropertyIsLike.setAttribute(
            "wildcard",
            "*");
        ogcPropertyIsLike.setAttribute(
            "singleChar",
            "_");
        ogcPropertyIsLike.setAttribute(
            "escape",
            "\\\\");
        // {
        final Element ogcPropertyName = new Element(
            "PropertyName",
            ogcns);
        // {
        ogcPropertyName.setText("Identifier");
        // }
        ogcPropertyIsLike.addContent(ogcPropertyName);
        final Element ogcLiteral = new Element(
            "Literal",
            ogcns);
        // {
        ogcLiteral.setText(uuid);
        // }
        ogcPropertyIsLike.addContent(ogcLiteral);

        // }
        ogcFilter.addContent(ogcPropertyIsLike);
        // }
        cswConstraint.addContent(ogcFilter);
        // }
        cswUpdate.addContent(cswConstraint);

        // }
        cswTransaction.addContent(cswUpdate);

        // }

        Reporting.logExpected(JDOMXmlUtils
            .indentedXmlFromElement(cswTransaction));
        ByteBuffer result;
        result = sendPostRquest(
            cswTransaction,
            "application/xml",
            "csw");
        if ((result) != null) {
            return returnAndConsumeResult(result);
        }
        return null;
    }

    /**
     * Return and consume result.
     * @param result the result
     * @return the element
     */
    private Element returnAndConsumeResult(
        final ByteBuffer result) {
        final String s = CharacterSetUtils.decodeBuffer(
            result,
            "UTF-8");
        try {
            Element e = JDOMXmlUtils.elementFromXml(s);
            if (e == null) {
                e = JDOMXmlUtils.elementFromXml(s.replaceAll(
                    "[^\\p{Graph}\\{Space}]",
                    " "));
                if (e == null) {
                    throw new NullPointerException();
                }
            }
            return e;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send csw transaction insert request.
     * @param contents the contents
     * @return the element
     */
    public Element sendCSWTransactionInsertRequest(
        final ByteBuffer contents) {
        final Element cswTransaction = new Element(
            "Transaction",
            cswns);
        // {
        /* build request */
        cswTransaction.setAttribute(
            "REQUEST",
            "Transaction");
        cswTransaction.setAttribute(
            "service",
            "CSW");
        cswTransaction.setAttribute(
            "version",
            CSW_VER_2_0_2);

        final Element cswUpdate = new Element(
            "Insert",
            cswns);
        // {
        final Element gmdMdMetadata = JDOMXmlUtils.elementFromXml(new String(
            contents.array()));

        cswUpdate.addContent(gmdMdMetadata);
        // }
        cswTransaction.addContent(cswUpdate);

        // }

        Reporting.logExpected(JDOMXmlUtils
            .indentedXmlFromElement(cswTransaction));
        ByteBuffer result;
        result = sendPostRquest(
            cswTransaction,
            "application/xml",
            "csw");
        if ((result) != null) {
            return returnAndConsumeResult(result);
        }
        return null;
    }

    /* bit more generic wrappers */
    /**
     * Gets the file contents request.
     * @param uuid the uuid
     * @return the file contents request
     */
    public ByteBuffer getFileContentsRequest(
        final String uuid) {
        final Element response = sendCSWGetRecordByIdRequest(uuid);
        if (response.getName().equals(
            "GetRecordByIdResponse")) {
            final Element gmdMdMetadata = response.getChild(
                "MD_Metadata",
                Namespace.getNamespace("http://www.isotc211.org/2005/gmd"));
            if (gmdMdMetadata != null) {
                return ByteBuffer.wrap(JDOMXmlUtils.xmlFromElement(
                    gmdMdMetadata).getBytes());
            }
        }
        Reporting.logExpected(
            "Received %1$s",
            JDOMXmlUtils.xmlFromElement(response));
        return null;
    }

    /**
     * Send search request.
     * @param term the term
     * @return the metadata file[]
     */
    public MetadataFile[] sendSearchRequest(
        final String term) {
        final Element response = sendCSWSearchRequest(term);
        if (response != null) {
            if (response.getName().equals(
                "GetRecordsResponse")) {
                final Element cswSearchResults = response.getChild(
                    "SearchResults",
                    cswns);
                if (cswSearchResults != null) {
                    final List cswSummaryRecs =
                        cswSearchResults.getContent(new ElementFilter(
                            "SummaryRecord",
                            cswns));
                    if (cswSummaryRecs != null
                        && cswSummaryRecs.size() > 0) {
                        final ArrayList<GeonetworkMetadataFile> results =
                            new ArrayList<GeonetworkMetadataFile>();
                        for (final Object cswSummaryReco : cswSummaryRecs) {
                            try {
                                final Element cswSummaryRecord =
                                    (Element) cswSummaryReco;
                                final Element dcIdent =
                                    cswSummaryRecord.getChild(
                                        "identifier",
                                        dcns);
                                final Element dcTitle =
                                    cswSummaryRecord.getChild(
                                        "title",
                                        dcns);
                                final Element dctAbstract =
                                    cswSummaryRecord.getChild(
                                        "abstract",
                                        dctns);
                                if (dcIdent != null
                                    && dcTitle != null
                                    && dcIdent.getTextTrim().length() > 0) {
                                    String varAbstractT = null;
                                    if (dctAbstract != null) {
                                        varAbstractT =
                                            dctAbstract.getTextTrim();
                                    } else {
                                        varAbstractT = null;
                                    }
                                    results.add(GeonetworkMetadataFile
                                        .getNewFromUUID(
                                            this,
                                            dcIdent.getTextTrim(),
                                            dcTitle.getTextTrim(),
                                            varAbstractT));
                                }
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                        }
                        final MetadataFile[] aFiles =
                            new MetadataFile[results.size()];
                        for (int i = 0; i < aFiles.length; i++) {
                            aFiles[i] = results.get(i);
                        }
                        return aFiles;
                    }
                }
            }
        }
        return new MetadataFile[0];
    }

    /* bit more generic wrappers */
    /**
     * Sets the file contents request.
     * @param uuid the uuid
     * @param contents the contents
     * @return true, if successful
     */
    public boolean setFileContentsRequest(
        final String uuid,
        final ByteBuffer contents) {
        final Element response = sendCSWTransactionUpdateRequest(
            uuid,
            contents);
        if (response.getName().equals(
            "TransactionResponse")) {
            return true;
        }
        Reporting.logExpected(
            "Received %1$s",
            JDOMXmlUtils.xmlFromElement(response));
        return false;
    }

    /* bit more generic wrappers */
    /**
     * New file request.
     * @param contents the contents
     * @return true, if successful
     */
    public boolean newFileRequest(
        final ByteBuffer contents) {
        final Element response = sendCSWTransactionInsertRequest(contents);
        if (response.getName().equals(
            "TransactionResponse2")) {
            return true;
        }
        Reporting.logExpected(
            "Received %1$s",
            JDOMXmlUtils.xmlFromElement(response));
        return false;
    }

    /* == MetadataFile Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return geonetworkUserDetails.getUrl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFolder() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataFile[] getChildren() {
        if (files == null) {
            search("");
        }
        return files.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetadataFile getParent() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer getPreviewContents(
        final DataCodec codec) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfileName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEditorSheetName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAbstract() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUUID() {
        return null;
    }

    // /////////////////////////////////
    // -- Misc
    // /////////////////////////////////

    /**
     * Search.
     * @param terms the terms
     */
    public void search(
        final String terms) {
        if (!connect()) {
            Reporting.reportUnexpected(
                "Connection failed \n%1$s",
                session.getResponseMessage());
        }
        files = sendSearchRequest(terms);
    }

    /**
     * Connect.
     * @return true, if successful
     */
    public boolean connect() {
        return (state != State.START)
            || sendLoginRequest();
    }

    /**
     * Disconnect.
     */
    public void disconnect() {
        sendLogoutRequest();
    }

    /**
     * returns the details of geonetwork module configuration.
     * @return the details of geonetwork module configuration
     */
    public Object getDetails() {
        return geonetworkUserDetails;
    }
}
