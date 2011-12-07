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
package xmet.profiles;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import n.io.bin.Files;
import n.io.xml.CSXMLSerializationCodec;
import n.io.xml.JDOMXmlUtils;
import n.java.ReflectionUtils;
import n.reporting.Reporting;
import n.ui.FileNamePostfixFilter;

import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import xmet.ClientContext;
import xmet.profiles.catalogs.CatalogCacheManager;
import xmet.profiles.codecs.CodelistCatalogCodec;
import xmet.profiles.codecs.ContactsInformationCodec;
import xmet.profiles.codecs.DataCodec;
import xmet.profiles.codecs.DefaultCodec;
import xmet.profiles.codecs.KeywordsListCodec;
import xmet.profiles.codecs.ProfileCodec;
import xmet.profiles.codecs.SpatialExtentCodec;
import xmet.profiles.contacts.AddressBook;
import xmet.profiles.xslt.TransformerSheet;
import xmet.profiles.xslt.TransformerSheetList;

/**
 * Manages a list of profiles and codecs and catalogs and what not.
 * @author Nahid Akbar
 */
public class ProfileManager {

    /* == Lists == */

    /** The profiles. */
    private final ArrayList<Profile> profiles = new ArrayList<Profile>();

    /** The data codecs. */
    private final ArrayList<DataCodec> dataCodecs = new ArrayList<DataCodec>();

    /** The profile codecs. */
    private final Map<String, ProfileCodec> profileCodecs =
        new HashMap<String, ProfileCodec>();

    /** The codelist codecs. */
    private final Map<String, CodelistCatalogCodec> codelistCodecs =
        new HashMap<String, CodelistCatalogCodec>();

    /** The spatial codecs. */
    private final Map<String, SpatialExtentCodec> spatialCodecs =
        new HashMap<String, SpatialExtentCodec>();

    /** The contact codecs. */
    private final Map<String, ContactsInformationCodec> contactCodecs =
        new HashMap<String, ContactsInformationCodec>();

    /** The keywords list codecs. */
    private final Map<String, KeywordsListCodec> keywordsListCodecs =
        new HashMap<String, KeywordsListCodec>();

    /** The transformer sheet list. */
    private TransformerSheetList transformerSheetList;

    /** The contacts. */
    private AddressBook contacts = null;

    /** The catalogs. */
    private CatalogCacheManager catalogs = null;

    /** The default schema locations. */
    private ProfileSchemaCache schemaCache = null;

    /* == Default Location Constants == */

    /** The Constant catalogFolder. */
    public static final String CATALOG_FOLDER =
        "client/profiles/default/catalogs";
    /** The Constant catalogFile. */
    public static final String CATALOG_FILE = CATALOG_FOLDER
        + "/list.xml";

    /* == Constructors == */

    /**
     * Instantiates a new profile manager.
     * @param profileDirectory the profile directory
     * @param context the context
     */
    public ProfileManager(
        final String profileDirectory,
        final ClientContext context) {
        findProfiles(
            profileDirectory,
            context);
        setContacts(AddressBook.loadContacts());
        // {
        Reporting.logExpected("Loading codelist cache");
        setCatalogs(CatalogCacheManager.loadCatalogCache(
            CATALOG_FILE,
            CATALOG_FOLDER));
        // }

        setSchemaCache(new ProfileSchemaCache(
            context,
            profileDirectory));

    }

    /* == Helper Getters and Setters == */
    /**
     * Gets the schema cache.
     * @return the schema cache
     */
    public ProfileSchemaCache getSchemaCache() {
        return schemaCache;
    }

    /* Profiles ----------------- */

    /**
     * Gets the profile by name.
     * @param name the name
     * @return the profile by name
     */
    public Profile getProfileByName(
        final String name) {
        for (final Profile p : profiles) {
            if (p.getName().equals(
                name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Gets the profiles.
     * @return the profiles
     */
    public ArrayList<Profile> getProfiles() {
        return profiles;
    }

    /**
     * Gets the profile by keyword.
     * @param keyword the keyword
     * @return the profile by keyword
     */
    public Profile getProfileByKeyword(
        final String keyword) {
        if (keyword != null) {
            for (final Profile p : profiles) {
                if (p.getName().equals(
                    keyword)
                    || p.getID().equals(
                        keyword)) {
                    return p;
                }
            }
            for (final Profile p : profiles) {
                if (p.getName().contains(
                    keyword)) {
                    return p;
                }
            }
        }
        return null;
    }

    /* Profile Codecs ---------------- */

    /**
     * Gets the profile codec of the speccified name.
     * @param profileCodec the profile codec
     * @return the profile codec, will return default if not found
     */
    public ProfileCodec getProfileCodec(
        final String profileCodec) {
        ProfileCodec pc = profileCodecs.get(profileCodec);
        if (pc == null) {
            for (final String profileCodecId : profileCodecs.keySet()) {
                pc = profileCodecs.get(profileCodecId);
                if ((pc) instanceof DefaultCodec) {
                    break;
                }
            }
        }
        return pc;
    }

    /* Data Codecs ----------------- */
    /**
     * Gets the data codec by file name.
     * @param fileName the name of the file
     * @return the data codec by file name
     */
    public DataCodec getDataCodecByFileName(
        final String fileName) {
        for (int i = 0; i < dataCodecs.size(); i++) {
            final DataCodec codec = dataCodecs.get(i);
            if (codec.isFileFormatAllowed(fileName)) {
                return codec;
            }
        }
        for (final DataCodec codec : dataCodecs) {
            if (codec instanceof DefaultCodec) {
                return codec;
            }
        }

        return null;
    }

    /**
     * Checks if is supported data format of the specified filename.
     * @param fileName the name of
     * @return true, if is supported data format
     */
    public boolean isSupportedDataFormat(
        final String fileName) {
        for (int i = 0; i < dataCodecs.size(); i++) {
            final DataCodec codec = dataCodecs.get(i);
            if (codec.isFileFormatAllowed(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the data codecs.
     * @return the data codecs
     */
    public ArrayList<DataCodec> getDataCodecs() {
        return dataCodecs;
    }

    /* XSLs ----------------- */
    /**
     * Gets the transformer sheet list.
     * @return the transformer sheet list
     */
    public TransformerSheetList getTransformerSheetList() {
        return transformerSheetList;
    }

    /* Default Schema Locations ----------------- */

    /* Catalog Codecs ----------------- */
    /**
     * Gets the codelist catalog for uri.
     * @param uri the uri
     * @return the codelist catalog for uri
     */
    public CodelistCatalogCodec getCodelistCatalogForURI(
        final String uri) {
        for (final String id : codelistCodecs.keySet()) {
            final CodelistCatalogCodec codec = codelistCodecs.get(id);
            if (uri.toLowerCase().indexOf(
                codec.getSupportedExtension().toLowerCase()) != -1) {
                return codec;
            }
        }
        Reporting.logUnexpected(
            "Catalog not found: %1$s",
            uri);
        return null;
    }

    /* Spatial Codecs ----------------- */
    /**
     * Gets the spatial codec.
     * @param spatialCodec the spatial codec
     * @return the spatial codec
     */
    public SpatialExtentCodec getSpatialCodec(
        final String spatialCodec) {
        SpatialExtentCodec sc = spatialCodecs.get(spatialCodec);
        if (sc == null) {
            for (final String spatialCodecId : spatialCodecs.keySet()) {
                sc = spatialCodecs.get(spatialCodecId);
                if ((sc) instanceof DefaultCodec) {
                    break;
                }
            }
        }
        return sc;
    }

    /* Keywords List Codecs ----------------- */
    /**
     * Gets the spatial codec.
     * @param spatialCodec the spatial codec
     * @return the spatial codec
     */
    public KeywordsListCodec getKeywordsListCodec(
        final String spatialCodec) {
        KeywordsListCodec kc = keywordsListCodecs.get(spatialCodec);
        if (kc == null) {
            for (final String spatialCodecId : keywordsListCodecs.keySet()) {
                kc = keywordsListCodecs.get(spatialCodecId);
                if ((kc) instanceof DefaultCodec) {
                    break;
                }
            }
        }
        return kc;
    }

    /* Contacts Codecs ----------------- */
    /**
     * Gets the contact codec.
     * @param contactsCodec the contacts codec
     * @return the contact codec
     */
    public ContactsInformationCodec getContactCodec(
        final String contactsCodec) {
        ContactsInformationCodec cc = contactCodecs.get(contactsCodec);
        if (cc == null) {
            for (final String contactsCodecId : contactCodecs.keySet()) {
                cc = contactCodecs.get(contactsCodecId);
                if ((cc) instanceof DefaultCodec) {
                    break;
                }
            }
        }
        return cc;
    }

    /* == Profiles Finding == */
    /**
     * Load profiles.
     * @param profileDirectoryName the profile directory name
     * @param context the context
     */
    public void findProfiles(
        final String profileDirectoryName,
        final ClientContext context) {
        /* load the list of codecs and put them in proper lists */
        final File[] profilesFolders = context.getResources().getFoldersList(
            profileDirectoryName);
        // {
        final File[] profileCodecsFiles = context.getResources().getFilesList(
            "plugins",
            new FileNamePostfixFilter(
                "Codecs List",
                false,
                ".codecs"));
        for (final File profileCodecsFile : profileCodecsFiles) {
            if ((profileCodecsFile != null)
                && profileCodecsFile.exists()) {
                loadCodecsFile(profileCodecsFile);
            }
        }
        // }
        /* probe for profiles folders and profile folders within them */
        for (final File profilesFolder : profilesFolders) {
            assert (profilesFolder.isDirectory());
            final File[] profileFolders = profilesFolder.listFiles();
            for (final File profileFolder : profileFolders) {
                if (profileFolder.isDirectory()
                    && !profileFolder.getName().startsWith(
                        ".")) {
                    try {
                        Profile p;
                        p = new Profile(
                            profileDirectoryName
                                + "/"
                                + profileFolder.getName(),
                            profileDirectoryName,
                            context);
                        profiles.add(p);
                        p.setId(profileFolder.getName());
                    } catch (final ProfileLoadingFailedException e) {
                        Reporting.logExpected(
                            "%1$s loading failed: %2$s",
                            profileFolder.isDirectory(),
                            e.getLocalizedMessage());
                        continue;
                    } catch (final Exception e) {
                        Reporting.reportUnexpected(e);
                    }
                }
            }
        }
        /* load a list of XSL Stylesheets */
        loadTransformerSheetList(
            profileDirectoryName,
            context);
    }

    /* == Codecs Loading == */

    /**
     * Loads a codecs list file.
     * @param profileCodecsFile the profile codecs file
     */
    @SuppressWarnings("rawtypes")
    private void loadCodecsFile(
        final File profileCodecsFile) {
        final ByteBuffer contents = Files.read(profileCodecsFile);
        if (contents == null) {
            return;
        }
        final Element rootElement = JDOMXmlUtils.elementFromXml(new String(
            contents.array()));
        if (rootElement == null) {
            return;
        }

        final List codecClasses = rootElement.getContent(new ElementFilter(
            "codecClass"));

        assert (codecClasses != null);

        for (final Object codecClassObj : codecClasses) {

            final Element codecClassElem = (Element) codecClassObj;

            final String className = codecClassElem.getTextTrim();

            if ((className != null)
                && (className.length() > 0)) {
                try {
                    Object codec = null;
                    final Class codecClassByName =
                        ReflectionUtils.getClassByName(className);
                    if (codecClassByName != null) {
                        codec =
                            ReflectionUtils
                                .getNewInstanceOfClass(codecClassByName);
                    }
                    if (codec != null) {
                        if (codec instanceof DataCodec) {
                            final DataCodec dataCodec = (DataCodec) codec;
                            Reporting.logExpected(
                                "Adding Data Codec: %1$s",
                                dataCodec.getId());
                            dataCodecs.add(dataCodec);
                        }
                        if (codec instanceof ProfileCodec) {
                            final ProfileCodec profileCodec =
                                (ProfileCodec) codec;
                            Reporting.logExpected(
                                "Adding Profile Codec: %1$s",
                                profileCodec.getId());
                            profileCodecs.put(
                                profileCodec.getId(),
                                profileCodec);
                        }
                        if (codec instanceof CodelistCatalogCodec) {
                            final CodelistCatalogCodec codelistCodec =
                                (CodelistCatalogCodec) codec;
                            Reporting.logExpected(
                                "Adding Catalog Codec: %1$s",
                                codelistCodec.getId());
                            codelistCodecs.put(
                                codelistCodec.getId(),
                                codelistCodec);
                        }
                        if (codec instanceof SpatialExtentCodec) {
                            final SpatialExtentCodec spatialCodec =
                                (SpatialExtentCodec) codec;
                            Reporting.logExpected(
                                "Adding Spatial Codec: %1$s",
                                spatialCodec.getId());
                            spatialCodecs.put(
                                spatialCodec.getId(),
                                spatialCodec);
                        }
                        if (codec instanceof KeywordsListCodec) {
                            final KeywordsListCodec spatialCodec =
                                (KeywordsListCodec) codec;
                            Reporting.logExpected(
                                "Adding Keywords Codec: %1$s",
                                spatialCodec.getId());
                            keywordsListCodecs.put(
                                spatialCodec.getId(),
                                spatialCodec);
                        }
                        if (codec instanceof ContactsInformationCodec) {
                            final ContactsInformationCodec contactsCodec =
                                (ContactsInformationCodec) codec;
                            Reporting.logExpected(
                                "Adding Contacts Codec: %1$s",
                                contactsCodec.getId());
                            contactCodecs.put(
                                contactsCodec.getId(),
                                contactsCodec);
                        }
                    }
                } catch (final RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* == XSLs Loading == */

    /**
     * Load transformer sheet list.
     * @param profilesDir the profiles dir
     * @param context the context
     */
    public void loadTransformerSheetList(
        final String profilesDir,
        final ClientContext context) {
        final File[] displayDirFiles = context.getResources().getFilesList(
            profilesDir
                + "/default/transformers.xml");
        for (final File aTransformerSheetList : displayDirFiles) {
            if (aTransformerSheetList.isFile()) {
                final ByteBuffer contents = Files.read(aTransformerSheetList);
                if (contents != null) {
                    try {
                        final TransformerSheetList tsl =
                            (TransformerSheetList) CSXMLSerializationCodec
                                .decodeClassesS(
                                    new String(
                                        contents.array()),
                                    TransformerSheetList.class,
                                    TransformerSheet.class);
                        if (tsl != null) {
                            for (final TransformerSheet ts : tsl.getSheets()) {
                                ts.setFileName(profilesDir
                                    + "/default/"
                                    + ts.getFileName());
                            }
                            if (this.transformerSheetList != null) {
                                this.transformerSheetList.getSheets().addAll(
                                    tsl.getSheets());
                            } else {
                                this.transformerSheetList = tsl;
                            }
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (transformerSheetList == null) {
            transformerSheetList = new TransformerSheetList();
        }
    }

    /* == Default Schema Locations Loading == */

    /**
     * Gets the addresses.
     * @return the addresses
     */
    public AddressBook getContacts() {
        return contacts;
    }

    /**
     * Gets the catalogs.
     * @return the catalogs
     */
    public CatalogCacheManager getCatalogs() {
        return catalogs;
    }

    /**
     * Shutdown callback for cleaning up resources and what not.
     */
    public void shutdown() {
        getCatalogs().save();
    }

    /**
     * Sets the contacts.
     * @param aContacts the new contacts
     */
    private void setContacts(
        final AddressBook aContacts) {
        contacts = aContacts;
    }

    /**
     * Sets the catalogs.
     * @param aCatalogs the new catalogs
     */
    private void setCatalogs(
        final CatalogCacheManager aCatalogs) {
        catalogs = aCatalogs;
    }

    /**
     * Sets the default schema locations.
     * @param aSchemaCache the new default schema locations
     */
    private void setSchemaCache(
        final ProfileSchemaCache aSchemaCache) {
        schemaCache = aSchemaCache;
    }

}
