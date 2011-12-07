/******************************************************************************
 * xMet - eXtensible Metadata Editor Copyright (C) 2010-2011 - Office Of Spatial
 * Data Management This is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version. This software is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. For a copy of the GNU
 * General Public License, see http://www.gnu.org/licenses/
 ******************************************************************************/
package xmet.profiles;

import java.io.File;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import n.io.bin.Files;
import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;

import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import xmet.ClientContext;
import xmet.profiles.catalogs.CatalogCache;
import xmet.profiles.catalogs.model.Codelist;
import xmet.profiles.catalogs.model.CodelistCatalog;
import xmet.profiles.codecs.ContactsInformationCodec;
import xmet.profiles.codecs.KeywordsListCodec;
import xmet.profiles.codecs.ProfileCodec;
import xmet.profiles.codecs.SpatialExtentCodec;
import xmet.profiles.editorSheet.EditorSheetFilenameExtension;
import xmet.profiles.editorSheet.EditorSheetTemplate;
import xmet.profiles.editorSheet.ProfileEditorSheet;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.verification.ConstraintsVerification;
import xmet.utils.BusyScreenUtil;
import xmet.utils.SchematronUtil;

/**
 * A collection of configuration elements related to a profile for allowing the
 * creation and editing of metadata of that profile.<br/>
 * @author Nahid Akbar
 */
/**
 * @author shinta
 */
@SuppressWarnings("rawtypes")
public final class Profile
    implements
    Serializable {

    /** The Constant ISO Schematron skeleton path. */
    private static final String ISO_SC_SKELETON_PATH = "client/"
        + "profiles/"
        + "default/"
        + "iso_schematron_skeleton_for_saxon.xsl";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* == Identification == */
    /** id - the folder name. */
    private String id;

    /** The name - as in config file. */
    private String name;

    /** full relative path to and including the profile folder. */
    private final String folderPath;

    /* == Schema and Codecs == */
    /** The root element name; preferably using the namespace if applicable. */
    private String rootElement;

    /** The profile schemas. */
    private final Map<String, ProfileSchema> profileSchemas =
        new HashMap<String, ProfileSchema>();

    /** The profile codec name. */
    private String profileCodecName;

    /** The spatial codec name. */
    private String spatialCodecName;

    /** The contacts codec name. */
    private String contactsCodecName;

    /** The keywords codec name. */
    private String keywordsCodecName;

    /**
     * The element substitutions. (UNTESTED, not so much needed any more with
     * SCV type EditorViews)
     */
    private final Map<String, String> elementSubstitutions =
        new HashMap<String, String>();
    /* == Preview == */

    /** The preview template path. */
    private String previewTemplatePath;

    /* == Validation == */
    /** The schematron files. */
    private final List<String> schematronFilePaths = new ArrayList<String>();
    /* == Codelists == */
    /** The code lists. */
    private final List<CatalogCache> codelistCatalogs =
        new ArrayList<CatalogCache>();
    /* == Editor Sheets == */
    /** The editor sheets. */
    private final List<ProfileEditorSheet> editorSheets =
        new ArrayList<ProfileEditorSheet>();
    /* == Help files == */

    /** The help directory path. */
    private String helpDirectoryPath = null;
    /* == Metadata on Metadata == */

    /** The uuid xpaths. */
    private final List<String> uuidXpaths = new ArrayList<String>();

    /** The abstract xpath. */
    private String abstractXpath;

    /** The title xpath. */
    private String titleXpath;
    /* == Caching == */

    /** The model root. */
    private transient Entity modelRoot = null;

    /* == Constructors == */

    /**
     * Instantiates a new profile.
     * @param profileDirectory the profile directory
     * @param profilesDirectory the profiles directory
     * @param context the context
     * @throws Exception the exception
     */
    public Profile(
        final String profileDirectory,
        final String profilesDirectory,
        final ClientContext context)
        throws Exception {
        this(
            loadProfileConfig(
                profileDirectory,
                context),
            profileDirectory,
            profilesDirectory,
            context);
    }

    /**
     * Instantiates a new profile.
     * @param configurationElement the configuration element
     * @param profileDirectory the profile directory
     * @param profilesDirectory the profiles directory
     * @param context the context
     * @throws Exception the exception
     */
    public Profile(
        final Element configurationElement,
        final String profileDirectory,
        final String profilesDirectory,
        final ClientContext context)
        throws Exception {
        /* dont want a profile object for the root profile */
        if (profileDirectory
            .equals("profiles/default")) {
            throw new ProfileLoadingFailedException(
                "Root profile does not need to be loaded.");
        }

        Reporting
            .logExpected(
                "Probing Profile Directory %1$s",
                profileDirectory);

        /* set folder */
        folderPath = profileDirectory;

        // {
        /* unwind stuff in presentation folder(s) */
        final File[] presentationFolders = context
            .getResources()
            .getFoldersList(
                profileDirectory
                    + "/presentation");

        for (final File presentationFolder : presentationFolders) {
            final String[] files = presentationFolder
                .list();
            if (files != null) {
                for (final String file : files) {
                    if (!file
                        .startsWith(".")) {
                        if (file
                            .endsWith(".preview")) {
                            setPreviewTemplate(getFolderPath()
                                + "/presentation/"
                                + file);
                        } else if (doesHaveEditorViewExtension(
                            file,
                            context)) {
                            getEditorSheets()
                                .add(
                                    new ProfileEditorSheet(
                                        this,
                                        getFolderPath()
                                            + "/presentation/"
                                            + file));
                        } else {
                            Reporting
                                .reportUnexpected(
                                    "Could not determine"
                                        + " the type of the file %1$s",
                                    Arrays
                                        .toString(context
                                            .getResources()
                                            .getFilesList(
                                                getFolderPath()
                                                    + "/presentation/"
                                                    + file)));
                        }
                    }
                }
            }
        }
        // }
        // {
        /* unwind stuff in template folder(s) */
        final File[] templateFolders = context
            .getResources()
            .getFoldersList(
                profileDirectory
                    + "/templates");
        for (final File templateFolder : templateFolders) {
            final String[] files = templateFolder
                .list();
            if (files != null) {
                for (final String file : files) {
                    if (!file
                        .startsWith(".")) {
                        final String edsName = EditorSheetTemplate
                            .extractTemplateEditorName(file);
                        if (edsName != null
                            && edsName
                                .trim()
                                .length() > 0) {
                            final ProfileEditorSheet ed =
                                getEditorSheetByName(edsName
                                    .trim());
                            if (ed != null) {
                                ed
                                    .includeProfileTemplate(getFolderPath()
                                        + "/templates/"
                                        + file);
                            }
                        } else {
                            Reporting
                                .logExpected(
                                    "Editor sheet %1$s not"
                                        + " found for template %2$s",
                                    edsName,
                                    file);
                        }
                    }
                }
            }
        }
        // }

        // {
        /* unwind the help dir */
        final File[] helpFilders = context
            .getResources()
            .getFoldersList(
                profileDirectory
                    + "/help");
        if ((helpFilders != null)
            && (helpFilders.length > 0)) {
            helpDirectoryPath = profileDirectory
                + "/help";
        }
        // }
        //
        // {
        /* load config */
        parseProfileConfig(
            configurationElement,
            getFolderPath(),
            profilesDirectory,
            context);
        // }
    }

    /**
     * Parses the profile config.
     * @param configurationElement the configuration element
     * @param profileDirectory the profile directory
     * @param profilesDirectory the profiles directory
     * @param context the context
     * @throws ProfileLoadingFailedException the profile loading failed
     *             exception
     */
    // CHECKSTYLE OFF: MethodLength
    private void parseProfileConfig(
        final Element configurationElement,
        final String profileDirectory,
        final String profilesDirectory,
        final ClientContext context)
        throws ProfileLoadingFailedException {

        if (configurationElement != null) {
            // {
            /* administrator mode only flag */
            final String adminMode = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "adminModeOnly");
            if (adminMode != null) {
                if (adminMode
                    .toLowerCase()
                    .equals(
                        "true")
                    && context
                        .getConfig()
                        .getTempSetting(
                            "admin",
                            null) == null) {
                    throw new ProfileLoadingFailedException(
                        "Administration Mode Is Not On");
                }
            }
            // }
            // {
            /* base configuration */
            String baseProfile = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "baseProfile");

            /* all profiles inherit from the default profile by default */
            if ((baseProfile == null)
                && !profileDirectory
                    .endsWith("default")) {
                baseProfile = "default";
            }

            /* load the config of the base profile */
            if (baseProfile != null) {
                try {
                    parseProfileConfig(
                        loadProfileConfig(
                            profileDirectory
                                + "/../"
                                + baseProfile,
                            context),
                        profilesDirectory
                            + "/"
                            + baseProfile,
                        profilesDirectory,
                        context);
                } catch (final ProfileLoadingFailedException e) {
                    Reporting
                        .reportUnexpected(e);
                }
            }
            // }

            // {
            /* profiles must all have unique names even if they have a base */
            /* profile */
            name = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "name");
            if (name == null) {
                Reporting
                    .reportUnexpected("profile name not specified");
            }
            // }

            // {
            /* root element */
            final String rootElement1 = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "root");
            if (rootElement1 != null) {
                rootElement = rootElement1;
            }

            /* profiles should all also have a root element */
            // if (rootElement == null) {
            // /* Reporting.err("root not specified"); */
            // }
            // }
            // {
            /* preview template */
            final String pf = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "previewTemplate");
            if (pf != null) {
                setPreviewTemplate(profileDirectory
                    + "/"
                    + pf);
            }
            // }
            // {
            /* schema/ns */
            final List namespaceAbbreviations = configurationElement
                .getContent(new ElementFilter(
                    "profileSchema"));
            for (final Object namespaceAbbreviationo : namespaceAbbreviations) {
                final Element namespaceAbbreviation =
                    (Element) namespaceAbbreviationo;
                final Element nsUriElement = namespaceAbbreviation
                    .getChild("nsUri");
                final Element nsPrefixElement = namespaceAbbreviation
                    .getChild("nsPrefix");
                final Element schemaPathElement = namespaceAbbreviation
                    .getChild("schemaPath");
                final Element schemaURLElement = namespaceAbbreviation
                    .getChild("schemaURL");

                if ((schemaPathElement != null)
                    || (schemaURLElement != null)
                    || ((nsUriElement != null) && (nsPrefixElement != null))) {
                    String nsPrefix = null;
                    if (nsPrefixElement != null) {
                        nsPrefix = nsPrefixElement
                            .getTextTrim();
                    } else {
                        nsPrefix = "";
                    }
                    String nsUri = null;
                    if (nsUriElement != null) {
                        nsUri = nsUriElement
                            .getTextTrim()
                            .toLowerCase();
                    } else {
                        nsUri = "";
                    }
                    String schemaPath = null;
                    if (schemaPathElement != null) {
                        schemaPath = profileDirectory
                            + "/"
                            + schemaPathElement
                                .getTextTrim();
                    } else {
                        schemaPath = null;
                    }
                    String schemaURL = null;
                    if (schemaURLElement != null) {
                        schemaURL = schemaURLElement
                            .getTextTrim();
                    } else {
                        schemaURL = null;
                    }
                    getProfileSchemas()
                        .put(
                            nsUri,
                            new ProfileSchema(
                                nsPrefix,
                                nsUri,
                                schemaPath,
                                schemaURL));
                }
            }
            // }

            // {
            /* editor sheets */
            final List aEditorSheets = configurationElement
                .getContent(new ElementFilter(
                    "editorSheet"));
            for (final Object editorSheeto : aEditorSheets) {
                final Element editorSheet = (Element) editorSheeto;
                getEditorSheets()
                    .add(
                        new ProfileEditorSheet(
                            this,
                            profileDirectory
                                + "/"
                                + editorSheet
                                    .getTextTrim()));
            }
            // }
            // {
            /* profile codec */
            final String pc = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "profileCodec");
            if (pc != null) {
                profileCodecName = pc;
            }
            // }
            // {
            /* spatial codec */
            final String sc = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "spatialCodec");
            if (sc != null) {
                spatialCodecName = sc;
            }
            // }
            // {
            /* spatial codec */
            final String kc = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "keywordsCodec");
            if (kc != null) {
                keywordsCodecName = kc;
            }
            // }
            // {
            /* contacts codec */
            final String cc = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "contactsCodec");
            if (cc != null) {
                contactsCodecName = cc;
            }
            // }
            // {
            /* substitutation files */
            final List substitutionFiles = configurationElement
                .getContent(new ElementFilter(
                    "substitutionFile"));
            for (final Object substitutionFileo : substitutionFiles) {
                final Element substitutionFile = (Element) substitutionFileo;
                final ByteBuffer contents = context
                    .getResources()
                    .getResourceContents(
                        profileDirectory
                            + "/"
                            + substitutionFile
                                .getTextTrim());
                if (contents != null) {
                    final Element element = JDOMXmlUtils
                        .elementFromXml(new String(
                            contents
                                .array()));
                    assert (element
                        .getName()
                        .equals("substitutions"));
                    final List substitutables = element
                        .getContent(new ElementFilter(
                            "substitutable"));
                    for (final Object obj : substitutables) {
                        final Element sub = (Element) obj;
                        assert (sub
                            .getName()
                            .equals("substitutable"));
                        final Element abstractElement = sub
                            .getChild("abstract");
                        final Element childElement = sub
                            .getChild("child");
                        if ((abstractElement != null)
                            && (abstractElement
                                .getTextTrim()
                                .length() > 0)
                            && (childElement != null)
                            && (childElement
                                .getTextTrim()
                                .length() > 0)) {
                            elementSubstitutions
                                .put(
                                    abstractElement
                                        .getTextTrim(),
                                    childElement
                                        .getTextTrim());
                        }
                    }
                }
            }
            // }
            // {
            final List codeListCatalogs = configurationElement
                .getContent(new ElementFilter(
                    "codeListCatalog"));
            for (final Object codeListCatalogo : codeListCatalogs) {
                final Element codeListCatalog = (Element) codeListCatalogo;
                final Element nameElement = codeListCatalog
                    .getChild("name");
                final Element fileElement = codeListCatalog
                    .getChild("file");
                final Element urlElement = codeListCatalog
                    .getChild("url");
                final CatalogCache c = new CatalogCache();
                if (nameElement != null) {
                    c
                        .setName(nameElement
                            .getTextTrim());
                }

                if (fileElement != null) {
                    c
                        .setFileName(profileDirectory
                            + "/"
                            + fileElement
                                .getTextTrim());
                }
                if (urlElement != null) {
                    c
                        .setUrl(urlElement
                            .getTextTrim());
                }
                if ((c
                    .getUrl() != null)
                    || (c
                        .getFileName() != null)) {
                    codelistCatalogs
                        .add(c);
                }

            }
            // }
            // {
            final List schematrons = configurationElement
                .getContent(new ElementFilter(
                    "schematron"));
            for (final Object schematrono : schematrons) {
                final Element schematron = (Element) schematrono;
                schematronFilePaths
                    .add(profileDirectory
                        + "/"
                        + schematron
                            .getTextTrim());
            }
            // }
            // {
            final String ap = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "abstractPath");
            if (ap != null) {
                abstractXpath = ap;
            }
            // }
            // {
            final String tp = JDOMXmlUtils
                .extractChildElementTextTrim(
                    configurationElement,
                    "titlePath");
            if (tp != null) {
                titleXpath = tp;
            }
            // }
            // {
            final List uuidPaths = configurationElement
                .getContent(new ElementFilter(
                    "uuidPath"));
            for (final Object uuidPatho : uuidPaths) {
                final Element uuidPath = (Element) uuidPatho;
                this.uuidXpaths
                    .add(uuidPath
                        .getTextTrim());
            }
            // }
        } else {
            throw new ProfileLoadingFailedException(
                "Not Profile");
        }
    }

    // CHECKSTYLE ON: MethodLength

    /* == Identification == */
    /**
     * Gets the id.
     * @return the id
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the id.
     * @param aId the new id
     */
    public void setId(
        final String aId) {
        this.id = aId;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the model.
     * @param context the context
     * @return the model
     */
    public Entity getModel(
        final ClientContext context) {
        final String pcName = profileCodecName;
        final String reName = rootElement;
        synchronized (this) {
            BusyScreenUtil
                .startBusy(String
                    .format(
                        "Loading %1$s profile",
                        name));
            try {
                if (modelRoot == null) {
                    // {
                    /* if not cached, get the profile codec and decode the */
                    /* schema */

                    final ProfileCodec profileCodecTmp = context
                        .getProfiles()
                        .getProfileCodec(
                            pcName);
                    if (profileCodecTmp != null) {
                        profileCodecTmp
                            .setProfileSchemas(profileSchemas);
                        profileCodecTmp
                            .setElementSubstitutions(elementSubstitutions);
                        try {
                            modelRoot = profileCodecTmp
                                .loadProfileModel(
                                    reName,
                                    context);
                        } catch (final Exception e) {
                            Reporting
                                .reportUnexpected(e);
                        }
                    }
                    // }
                }
            } catch (final Exception e) {
                e
                    .printStackTrace();
            } finally {
                BusyScreenUtil
                    .endBusy();
            }
            /* return a soft clone */
            if (modelRoot != null) {
                return ModelUtils
                    .cloneEntity(modelRoot);
            } else {
                return null;
            }
        }
    }

    /* == Verification == */

    /**
     * Verify model using schema constraints.
     * @param model the model
     * @return true, if successful
     */
    public synchronized boolean verifyModelConstraints(
        final Entity model) {
        return (new ConstraintsVerification())
            .verify(model);
    }

    /**
     * Verify model using schematron constraints that is configured.
     * @param xmlContents the xml contents
     * @param context the context
     * @return the string
     */
    public synchronized String verifyModelSchematron(
        final ByteBuffer xmlContents,
        final ClientContext context) {
        BusyScreenUtil
            .startBusy("Performing Schematron Validation...");
        try {
            String results = "";
            for (final String file : schematronFilePaths) {
                BusyScreenUtil
                    .tickBusy();
                try {
                    String result = SchematronUtil
                        .validate(
                            context
                                .getResources()
                                .getResourceURI(
                                    ISO_SC_SKELETON_PATH),
                            context
                                .getResources()
                                .getResourceURI(
                                    file),
                            xmlContents);
                    result = result
                        .replaceAll(
                            "(\\<)(.*\\?)(\\>)",
                            "");
                    result = result
                        .trim();
                    if (result
                        .length() > 0) {
                        results += "\n"
                            + result;
                        results = results
                            .trim();
                    }
                } catch (final Exception e) {
                    Reporting
                        .reportUnexpected(e);
                }
                BusyScreenUtil
                    .tickBusy();
            }
            return results;
        } catch (final Exception e) {
            Reporting
                .reportUnexpected(e);
        } finally {
            BusyScreenUtil
                .endBusy();
        }
        return null;
    }

    /* == preview template files == */

    /**
     * Gets the preview template file.
     * @return the previewTemplate
     */
    public String getPreviewTemplateFile() {
        return previewTemplatePath;
    }

    /**
     * Checks for preview template file.
     * @return the previewTemplate
     */
    public boolean hasPreviewTemplateFile() {
        return (previewTemplatePath != null)
            && Files
                .fileExists(getPreviewTemplateFile());
    }

    /**
     * Sets the preview template.
     * @param previewTemplate the previewTemplate to set
     */
    public void setPreviewTemplate(
        final String previewTemplate) {
        this.previewTemplatePath = previewTemplate;
    }

    /* == Codelists == */

    // -- use the following interface to access information about codelists
    /**
     * returns the number of codelist catalogs specified with this profile.
     * @return the codelist catalog count
     */
    public int getCodelistCatalogCount() {
        return codelistCatalogs
            .size();
    }

    /**
     * get the url of a catalog.
     * @param index index of catalog
     * @return the codelist catalog url
     */
    public String getCatalogURL(
        final int index) {
        return codelistCatalogs
            .get(
                index)
            .getUrl();
    }

    /**
     * Gets the CodelistCatalog object of a catalog by its URL.
     * @param catalogURL the url
     * @param context the context
     * @return the codelist catalog
     */
    public CodelistCatalog getCatalogByURL(
        final String catalogURL,
        final ClientContext context) {
        /* get catalog and return the CodelistCatalog object from within it */
        final CatalogCache catalog = getCatalogCache(
            catalogURL,
            context);
        if (catalog != null) {
            return catalog.getCache();
        }
        return null;
    }

    /**
     * Gets the codelist by URL.
     * @param codelistURL the url
     * @param context the context
     * @return the codelist
     */
    public Codelist getCodelistByURL(
        final String codelistURL,
        final ClientContext context) {
        String codelistName = null;
        /* split catalog url and codelist name */
        if (codelistURL
            .indexOf('#') != -1) {
            codelistName = codelistURL
                .substring(codelistURL
                    .indexOf('#') + 1);
        }
        /* get the catalog by URL and get the codelist from the catalog */
        final CatalogCache catalog = getCatalogCache(
            codelistURL,
            context);
        if (catalog != null) {
            if (catalog.getCache() != null) {
                return catalog.getCache().getCodelistByIdentifier(
                    codelistName);
            }
            return null;
        }
        return null;
    }

    /**
     * Gets the catalog by a specified URL.
     * @param catalogURL the url
     * @param context the context
     * @return the catalog
     */
    public CatalogCache getCatalogCache(
        final String catalogURL,
        final ClientContext context) {
        BusyScreenUtil
            .startBusy("Loading Catalog "
                + catalogURL);
        try {
            String uri = catalogURL;
            if (catalogURL
                .indexOf('#') != -1) {
                uri = catalogURL
                    .substring(
                        0,
                        catalogURL
                            .indexOf('#'));
            }
            for (final CatalogCache profileCatalogItem : codelistCatalogs) {
                BusyScreenUtil
                    .tickBusy();
                if (profileCatalogItem
                    .getUrl()
                    .equalsIgnoreCase(
                        uri)) {
                    if (!profileCatalogItem
                        .isLoaded()) {
                        profileCatalogItem
                            .loadCatalog(
                                context
                                    .getProfiles()
                                    .getCodelistCatalogForURI(
                                        uri),
                                context);
                    }
                    if (profileCatalogItem
                        .isLoaded()) {
                        return profileCatalogItem;
                    } else {
                        /* failed loading */
                        return null;
                    }
                }
            }
            final CatalogCache profileCatalogItem = context
                .getProfiles()
                .getCatalogs()
                .getCatalog(
                    uri);
            if (profileCatalogItem != null) {
                if (!profileCatalogItem
                    .isLoaded()) {
                    profileCatalogItem
                        .loadCatalog(
                            context
                                .getProfiles()
                                .getCodelistCatalogForURI(
                                    uri),
                            context);
                }
                if (profileCatalogItem
                    .isLoaded()) {
                    return profileCatalogItem;
                } else {
                    /* failed loading */
                    return null;
                }
            }
            return null;
        } catch (final Exception e) {
            e
                .printStackTrace();
            return null;
        } finally {
            BusyScreenUtil
                .endBusy();
        }
    }

    /* == Profile Schemas and whatnot == */
    /**
     * Gets the profile schemas list.
     * @return map of namespace abbreviations url->ProfileSchema object
     */
    public Map<String, ProfileSchema> getProfileSchemas() {
        return profileSchemas;
    }

    /* == Templates == */

    /**
     * Gets the editor sheets.
     * @return the editor sheets
     */
    public List<ProfileEditorSheet> getEditorSheets() {
        return editorSheets;
    }

    /**
     * Gets the editor sheet by name.
     * @param editorSheetName the editor sheet name
     * @return the editor sheet by name
     */
    public ProfileEditorSheet getEditorSheetByName(
        final String editorSheetName) {
        for (final ProfileEditorSheet es : editorSheets) {
            if (es
                .getName()
                .equals(
                    editorSheetName)) {
                return es;
            }
        }
        return null;
    }

    /**
     * Gets the editor sheet by file.
     * @param editorSheetFile the editor sheet file
     * @return the editor sheet by file
     */
    public ProfileEditorSheet getEditorSheetByFile(
        final File editorSheetFile) {
        return new ProfileEditorSheet(
            this,
            editorSheetFile
                .getAbsolutePath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Gets the spatial codec.
     * @param context the context
     * @return the spatial codec
     */
    public SpatialExtentCodec getSpatialCodec(
        final ClientContext context) {
        return context
            .getProfiles()
            .getSpatialCodec(
                spatialCodecName);
    }

    /**
     * Gets the keywords list codec.
     * @param context the context
     * @return the keywords list codec
     */
    public KeywordsListCodec getKeywordsListCodec(
        final ClientContext context) {
        return context
            .getProfiles()
            .getKeywordsListCodec(
                keywordsCodecName);
    }

    /**
     * Gets the contacts codec.
     * @param context the context
     * @return the contacts codec
     */
    public ContactsInformationCodec getContactsCodec(
        final ClientContext context) {
        return context
            .getProfiles()
            .getContactCodec(
                contactsCodecName);
    }

    /**
     * Gets the help dir path.
     * @return the help dir path
     */
    public String getHelpDirPath() {
        return helpDirectoryPath;
    }

    /**
     * Helper method that loads the profile config file or throws exception.
     * @param profileDirectory the profile directory
     * @param context the context
     * @return the element
     * @throws ProfileLoadingFailedException the profile loading failed
     *             exception
     */
    private static Element loadProfileConfig(
        final String profileDirectory,
        final ClientContext context)
        throws ProfileLoadingFailedException {
        final ByteBuffer configFile = context
            .getResources()
            .getResourceContents(
                profileDirectory
                    + "/config.xml");
        if (configFile == null) {
            throw new ProfileLoadingFailedException(
                "Not a Profile");
        } else {
            return JDOMXmlUtils
                .elementFromXml(new String(
                    configFile
                        .array()));
        }
    }

    /* == MetaMetaData == */
    /**
     * Gets the uuid paths.
     * @return the uuid paths
     */
    public List<String> getUuidPaths() {
        return uuidXpaths;
    }

    /**
     * Gets the abstract path.
     * @return the abstract path
     */
    public String getAbstractPath() {
        return abstractXpath;
    }

    /**
     * Gets the title path.
     * @return the title path
     */
    public String getTitlePath() {
        return titleXpath;
    }

    /**
     * Gets the namespace uri from specified prefix.
     * @param namespacePrefix the string
     * @return the namespace uri of the specified prefix
     */
    public String getNamespaceURI(
        final String namespacePrefix) {
        for (final Map.Entry<String, ProfileSchema> schema : profileSchemas
            .entrySet()) {
            if (namespacePrefix
                .equals(schema
                    .getValue()
                    .getNamespacePrefix())) {
                return schema
                    .getValue()
                    .getNamespaceUri();
            }
        }
        return null;
    }

    /**
     * Helper checks to see if an editor view exists which takes in config files
     * with specified filename extension.
     * @param fileName the file name
     * @param context the context
     * @return true, if successful
     */
    private boolean doesHaveEditorViewExtension(
        final String fileName,
        final ClientContext context) {
        final EditorSheetFilenameExtension[] extensions = context
            .getServices()
            .getServiceProviderList(
                EditorSheetFilenameExtension.class);
        if (extensions != null) {
            for (final EditorSheetFilenameExtension esfne : extensions) {
                if (fileName
                    .endsWith("."
                        + esfne
                            .getFilenameExtension())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return the folderPath
     */
    public String getFolderPath() {
        return folderPath;
    }

}
