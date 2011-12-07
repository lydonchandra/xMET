<?xml version="1.0" encoding="UTF-8"?>
<!-- 
converts metadata instances conforming to the ANZMETA v2 definition 
(DTD at http://www.ga.gov.au/dtd/anzmeta-1.3.dtd) 
to the ANZLIC Profile

Version 1.8 Edited by John Hockaday 2008-10-28
- removed the mapping of jurisdic to anything.  The ANZLIC Metadata Profile working 
  group agreed that the Jurisdiction should not be mapped to anything and should 
  be dropped from the ANZLIC Metadata Profile.

Version 1.6 Edited by John Hockaday 2009-11-26
- Changed the element name "suppinf" to "supplinf" as per the ANZLIC Metadata Guidelines
	verion 2.0 (2001) 1.3 DTD and 1.3.2 RELAXNG DSDL.
	
Version 1.5 Edited by John Hockaday 2008-03-03
- Added the reference to ANZLIC Identifier authority
- Changed MD_LegalConstraints to MD_Constraints. Although ANZLIC V2 uses the term
access constraints and that maps to MD_LegalConstraints in ISO 19115 the
accessConstraints is a codeList and ANZLIC V2 access constraint is free text. 
Therefore I have mapped ANZLIC V2 access constraints to the
MD_Constraints/useLimitation free text field.
- Changed the output for BeginPosition and EndPosition to be empty if they are
not dates but are keywords.
- Changed the dummy citation to have the value of <gmd:title gco:nilReason="missing"/>
rather than <gmd:title>unknown</gmd:title>.  This is probably a truer
representation.
- Changed element pass value from Boolean true to gco:nilReason="missing"
because this is probably a true representation of the access constraints, positional accuracy, attribute accuracy and logical consistency.

Version 1.4 Edited by John Hockaday 2008-03-03 (Thanks to Grazia Roberts)
Changed the scehmaLocation so that it can find GML and XLINKS at:
"http://www.isotc211.org/2005/gml/gml.xsd"
"http://www.isotc211.org/2005/xlink/xlinks.xsd"

version 1.3 Edited by John Hockaday 2008-02-29
changed the  value of gco:Boolean from "1" to "true"

version 1.2 Edited by John Hockaday 2008-02-18: 
changed metadataStandardName to
"ANZLIC Metadata Profile: An Australian/New Zealand Profile of AS/NZS ISO19115:2005, Geographic information - Metadata"
changed default themesListLocation to:
"http://asdd.ga.gov.au/asdd/profileinfo/anzlic-theme.xml"
changed default codesListLocation for the GEN to:
"http://asdd.ga.gov.au/asdd/profileinfo/"

version 1.1 Edited by John Hockaday 2007-10-26 : changed metadataVersionName to
"1.1"
version 1.0	Edtied by John Hockaday 2007-09-27

PARAMETERS
fileUUID allows you to pass in an expression for the fileIdentifier

language allows you to pass in the language used.

hierarchyLevel allows you to pass in the value for the hierarchyLevel

hierarchyLevelName allows you to pass in the value for the hierarchyLevelName

themesListLocation allows you to pass in the URL of the themes list

codesListLocation allows you to pass in the URL of the geographic extent name
code lists.

metadataOrganisation allows you to pass in the organisation name for the
resource and metadata.

mdCreationDate allows you to pass in the date the metadata was created not last
updated.

topicCategory allows you to pass in a topic category.

resourceCreationDate allows you to enter the revision date of the resource.

Note the very rigid nested structure of the template matching expression, which matches
the schema of the destination as a canonical example.

For those relatively unfamiliar with xslt, note that where apply-templates is used, that
provides a conditional logic as well as a neat separation of a logical chunk of output.
The template will not be invoked if there is no matching element.

The other reasons for moving some content into apply-templates are to reduce complexity, reuse the same logic and to "move the context". 
eg: <xsl:apply-templates select="citeinfo/origin/custod"/>


Note the use of expressions like <xsl:value-of select="'dataset'"/> to guarantee a simple piece of text is inserted in the element without the inclusion newlines.

Comments such as "Mapping table row 1" refer to the Annex B in the ANZLIC Report accompaning this sheet.
The convention we are attempting to follow is that the mapping comment is immediately above the innermost element, eg:
xsl:element name="gmd:fileIdentifier"
	Mapping table row 1
		xsl:element name="gco:CharacterString"

There may be several layers of elements corresponding to a single mapping table entry and this is the easiest convention to keep unambiguous.

contact: Andy Dent
mailto:andrew.dent@csiro.au
phone: +61 416 249 271
-->
<xsl:stylesheet version="2.0" 
	xmlns="http://www.environment.gov.au/net/anzmeta/anzmeta-1.3.dtd" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:fn="http://www.w3.org/2005/xpath-functions" 
	xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" 
	xmlns:gmd="http://www.isotc211.org/2005/gmd" 
	xmlns:gco="http://www.isotc211.org/2005/gco" 
	xmlns:xlink="http://www.w3.org/1999/xlink" 
	xmlns:gts="http://www.isotc211.org/2005/gts" 
	xmlns:gsr="http://www.isotc211.org/2005/gsr" 
	xmlns:gss="http://www.isotc211.org/2005/gss" 
	xmlns:gmx="http://www.isotc211.org/2005/gmx" 
	xmlns:gml="http://www.opengis.net/gml" 
	xmlns:localFunctionsNS="http://www.environment.gov.au/net/anzmeta/functionsLocalToThisStylesheetMustHaveNamespace" 
	xsi:schemaLocation="http://www.isotc211.org/2005/gmd http://www.isotc211.org/2005/gmd/gmd.xsd"
	exclude-result-prefixes="localFunctionsNS">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:param name="fileUUID" select="'REPLACE THIS TEXT WITH YOUR UUID OR OTHER PREFERRED METADATA IDENTIFIER'"/>
	<xsl:param name="language" select="'eng'"/>
	<xsl:param name="hierarchyLevel" select="'dataset'"/>
	<xsl:param name="themesListLocation" select="'http://asdd.ga.gov.au/asdd/profileinfo/anzlic-theme.xml'"/><!-- Change this to canonical location when available -->
	<xsl:param name="codesListLocation" select="'http://asdd.ga.gov.au/asdd/profileinfo/'"/><!-- Change this to canonical location when available -->
	<xsl:param name="metadataOrganisation" /><!-- no default -->
	<xsl:param name="mdCreationDate" /><!-- no default -->
	<xsl:param name="topicCategory" /><!-- no default -->
	<xsl:param name="resourceCreationDate" /><!-- no default -->
	<xsl:variable name="hierarchyLevelName">
		<xsl:choose><xsl:when test="$hierarchyLevel='dataset'">dataset</xsl:when><xsl:otherwise><xsl:value-of select="$hierarchyLevel"/></xsl:otherwise></xsl:choose>
	</xsl:variable>
	
	<xsl:template match="/anzmeta">
		<!-- ERROR CHECKING FOR PARAMS -->
		<xsl:if test="not(contains('dataset,series,tile', $hierarchyLevel))">
			<xsl:message terminate="yes">Parameter 'hierarchyLevel' must be one of 'dataset', 'series' or 'tile'. Incorrect value '<xsl:value-of select="$hierarchyLevel"/>'.</xsl:message>
		</xsl:if>		
		<xsl:element name="gmd:MD_Metadata">
			<xsl:attribute name="xsi:schemaLocation">http://www.isotc211.org/2005/gmd http://www.isotc211.org/2005/gmd/gmd.xsd http://www.opengis.net/gml http://www.isotc211.org/2005/gml/gml.xsd http://www.w3.org/1999/xlink http://www.isotc211.org/2005/xlink/xlinks.xsd</xsl:attribute>
			<xsl:namespace name="xsi">http://www.w3.org/2001/XMLSchema-instance</xsl:namespace>
			<xsl:namespace name="gco">http://www.isotc211.org/2005/gco</xsl:namespace>
			<xsl:namespace name="gmd">http://www.isotc211.org/2005/gmd</xsl:namespace>
			<xsl:namespace name="gts">http://www.isotc211.org/2005/gts</xsl:namespace>
			<xsl:namespace name="gsr">http://www.isotc211.org/2005/gsr</xsl:namespace>
			<xsl:namespace name="gss">http://www.isotc211.org/2005/gss</xsl:namespace>
			<xsl:namespace name="gmx">http://www.isotc211.org/2005/gmx</xsl:namespace>
			<xsl:namespace name="gml">http://www.opengis.net/gml</xsl:namespace>
			<xsl:namespace name="xlink">http://www.w3.org/1999/xlink</xsl:namespace>
			<xsl:copy-of select="@*"/>
			<xsl:element name="gmd:fileIdentifier">
				<!-- Mapping table row 1 -->
				<xsl:element name="gco:CharacterString">
					<xsl:value-of select="$fileUUID"/>
					<!-- use parameter optionally passed into script instead of trying to make informal unique ID from processing date-time -->
					<!-- <xsl:value-of select="concat(normalize-space(citeinfo/uniqueid), current-dateTime())"/>  -->
				</xsl:element>
			</xsl:element><!-- /gmd:fileIdentifier -->
			 <!-- un-comment this if you want gmd:language for the metadata document as
			 	well as in the data-identification element -->

			<xsl:element name="gmd:language">
				<xsl:element name="gco:CharacterString"><xsl:value-of select="$language"/></xsl:element>
			</xsl:element>

			
			<xsl:element name="gmd:hierarchyLevel">
				<xsl:element name="gmd:MD_ScopeCode">
					<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#MD_ScopeCode</xsl:attribute>
					<xsl:attribute name="codeListValue" select="$hierarchyLevel"/>
					<xsl:value-of select="$hierarchyLevelName"/><!-- yes this is on purpose, we are supposed to have the slightly different content in the element, ie: Dataset vs dataset -->
				</xsl:element>
			</xsl:element>
			<!-- /gmd:hierarchyLevel -->
			<xsl:element name="gmd:hierarchyLevelName">
				<xsl:element name="gco:CharacterString"><xsl:value-of select="$hierarchyLevelName"/></xsl:element>
			</xsl:element>
			<!-- /gmd:hierarchyLevelName -->
			<xsl:element name="gmd:contact">
				<xsl:choose>
					<xsl:when test="$metadataOrganisation"><xsl:call-template name="simpleContactUsingMetadataOrganisation"/></xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="cntinfo">
							<xsl:with-param name="role" select="'custodian'"/>
							<!-- metadata-record/contact is mandatory. Use dataset-contact. -->
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:element>
			<!-- /gmd:contact -->
			<xsl:element name="gmd:dateStamp">
				<xsl:element name="gco:Date">
					<!-- Mapping table row 44 -->
					<xsl:choose>
						<xsl:when test="$mdCreationDate">
							<xsl:call-template name="formatISO8601date">
								<xsl:with-param name="indate" select="$mdCreationDate"/>
							</xsl:call-template>						
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="formatISO8601date">
								<xsl:with-param name="indate" select="normalize-space(metainfo/metd/date)"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>	
				</xsl:element>
				<!-- gco:Date -->
			</xsl:element>
			<!-- /gmd:dateStamp -->
			<xsl:element name="gmd:metadataStandardName"><xsl:element
				name="gco:CharacterString">ANZLIC Metadata Profile: An Australian/New Zealand Profile of AS/NZS ISO 19115:2005, Geographic information - Metadata</xsl:element>
			</xsl:element>
			<xsl:element name="gmd:metadataStandardVersion"><xsl:element
				name="gco:CharacterString">1.1</xsl:element></xsl:element>
			<xsl:element name="gmd:identificationInfo">
				<xsl:element name="gmd:MD_DataIdentification">
					<xsl:element name="gmd:citation">
						<xsl:element name="gmd:CI_Citation">
							<xsl:element name="gmd:title">
								<!-- Mapping table row 2 -->
								<xsl:element name="gco:CharacterString">
									<xsl:value-of select="normalize-space(citeinfo/title)"/>
								</xsl:element>
							</xsl:element>
							<!-- /gmd:title -->
							<xsl:element name="gmd:date">
								<xsl:element name="gmd:CI_Date">
									<xsl:choose>
										<xsl:when test="$resourceCreationDate">
											<xsl:element name="gmd:date">
												<xsl:element name="gco:Date">
													<xsl:call-template name="formatISO8601date">
														<xsl:with-param name="indate" select="$mdCreationDate"/>
													</xsl:call-template></xsl:element>
												<!-- gco:Date -->
											</xsl:element>
											<!-- /gmd:date -->						
											<xsl:element name="gmd:dateType">
												<xsl:element name="gmd:CI_DateTypeCode">
													<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode</xsl:attribute>
													<xsl:attribute name="codeListValue"><xsl:value-of select="'creation'"/></xsl:attribute>
													<xsl:value-of select="'creation'"/>
												</xsl:element>
												<!-- /gmd:CI_DateTypeCode -->
											</xsl:element>
											<!-- /gmd:dateType --></xsl:when>
										<xsl:otherwise>
											<xsl:element name="gmd:date">
												<xsl:element name="gco:Date">
													<xsl:call-template name="formatISO8601date">
														<xsl:with-param name="indate" select="normalize-space(metainfo/metd/date)"/>
													</xsl:call-template>
												</xsl:element>
												<!-- gco:Date -->
											</xsl:element>
											<!-- /gmd:date -->
											<xsl:element name="gmd:dateType">
												<xsl:element name="gmd:CI_DateTypeCode">
													<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode</xsl:attribute>
													<xsl:attribute name="codeListValue"><xsl:value-of select="'revision'"/></xsl:attribute>
													<xsl:value-of select="'revision'"/>
												</xsl:element>
												<!-- /gmd:CI_DateTypeCode -->
											</xsl:element>
											<!-- /gmd:dateType -->
										</xsl:otherwise>
									</xsl:choose>	
								</xsl:element>
								<!-- /gmd:CI_Date -->
							</xsl:element>
							<!-- /gmd:date -->
							<xsl:element name="gmd:identifier">
								<xsl:element name="gmd:MD_Identifier">
									<xsl:element name="gmd:authority">
										<xsl:element name="gmd:CI_Citation">
											<xsl:element name="gmd:title">
												<xsl:element name="gco:CharacterString">ANZLIC Identifier</xsl:element>
											</xsl:element>
											<xsl:element name="gmd:date">
												<xsl:element name="gmd:CI_Date">
													<xsl:element name="gmd:date">
														<xsl:element name="gco:Date">2001-02</xsl:element>
													</xsl:element>
													<xsl:element name="gmd:dateType">
														<xsl:element name="gmd:CI_DateTypeCode">
															<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode</xsl:attribute>
															<xsl:attribute name="codeListValue"><xsl:value-of select="'revision'"/></xsl:attribute>
															<xsl:value-of select="'revision'"/>
														</xsl:element>
													</xsl:element>
												</xsl:element>
											</xsl:element>
											<xsl:element name="gmd:edition">
												<xsl:element name="gco:CharacterString">Version 2</xsl:element>
											</xsl:element>
											<xsl:element name="gmd:editionDate">
												<xsl:element name="gco:Date">2001-02</xsl:element>
											</xsl:element>
											<xsl:element name="gmd:citedResponsibleParty">
												<xsl:element name="gmd:CI_ResponsibleParty">
													<xsl:element name="gmd:organisationName">
														<xsl:element name="gco:CharacterString">ANZLIC - the Spatial Information Council</xsl:element>
													</xsl:element>
													<xsl:element name="gmd:role">
														<xsl:element name="gmd:CI_RoleCode">
															<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode</xsl:attribute>
															<xsl:attribute name="codeListValue"><xsl:value-of select="'owner'"/></xsl:attribute>
															<xsl:value-of select="'owner'"/>
														</xsl:element>
													</xsl:element>
												</xsl:element>
											</xsl:element>
											<xsl:element name="gmd:otherCitationDetails">
												<xsl:element name="gco:CharacterString">Defined in ANZLIC Metadata Guidelines Version 2 http://www.anzlic.org.au/download.html?oid=2358011755</xsl:element>
											</xsl:element>
										</xsl:element>
									</xsl:element>
									<xsl:element name="gmd:code">
										<xsl:element name="gco:CharacterString">
											<!-- Mapping table row 1 -->
											<xsl:value-of select="normalize-space(citeinfo/uniqueid)"/>
										</xsl:element>
									</xsl:element>
									<!-- /gmd:code -->
								</xsl:element>
								<!-- /gmd:MD_Identifier -->
							</xsl:element>
							<!-- /gmd:identifier -->
							<xsl:choose>
								<xsl:when test="$metadataOrganisation"><xsl:call-template name="metadataOrganisationCitation"/></xsl:when>
								<xsl:otherwise><xsl:apply-templates select="citeinfo/origin/custod"/></xsl:otherwise>
							</xsl:choose>
						</xsl:element>
						<!-- /gmd:CI_Citation -->
					</xsl:element>
					<!-- /gmd:citation -->
					<xsl:element name="gmd:abstract">
						<!-- Mapping table row 6 -->
						<xsl:element name="gco:CharacterString">
							<xsl:value-of select="descript/abstract"/>
						</xsl:element>
					</xsl:element>
					<!-- /gmd:abstract -->
					<xsl:if test="status/progress/keyword != 'Not known' ">
						<xsl:element name="gmd:status">
							<xsl:variable name="keyword" select="normalize-space(status/progress/keyword)"></xsl:variable>
							<!-- Mapping table row 22 -->
							<xsl:element name="gmd:MD_ProgressCode">
								<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#MD_ProgressCode</xsl:attribute>
								<xsl:variable name="listValue"><xsl:choose>
									<xsl:when test="$keyword='Complete' ">completed</xsl:when>
									<xsl:when test="$keyword='In Progress' ">onGoing</xsl:when>
									<xsl:when test="$keyword='Planned' ">planned</xsl:when>
									<xsl:otherwise><xsl:value-of select="$keyword"/></xsl:otherwise>
								</xsl:choose></xsl:variable>
								<xsl:attribute name="codeListValue"><xsl:value-of select="$listValue"/></xsl:attribute>
								<xsl:value-of select="$listValue"/>
							</xsl:element>
							<!-- /gmd:MD_ProgressCode -->
						</xsl:element>
						<!-- /gmd:status -->
					</xsl:if>
					<xsl:element name="gmd:pointOfContact">
						<!-- Mapping table row 34..43 -->
						<xsl:apply-templates select="cntinfo">
							<xsl:with-param name="role" select="'pointOfContact'"/>
						</xsl:apply-templates>
					</xsl:element>
					<!-- /gmd:pointOfContact -->
					<xsl:element name="gmd:resourceMaintenance">
						<xsl:element name="gmd:MD_MaintenanceInformation">
							<xsl:element name="gmd:maintenanceAndUpdateFrequency">
								<!-- Mapping table row 23 -->
								<xsl:element name="gmd:MD_MaintenanceFrequencyCode">
									<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#MD_MaintenanceFrequencyCode</xsl:attribute>
									<xsl:variable name="listValue"><xsl:choose>
										<xsl:when test=".//status/update/keyword"><xsl:apply-templates select="//status/update/keyword"/>
											<!-- just factor out messy cleanups -->
										</xsl:when>
										<xsl:otherwise>unknown</xsl:otherwise>
									</xsl:choose></xsl:variable>
									<xsl:attribute name="codeListValue"><xsl:value-of select="$listValue"/></xsl:attribute>
									<xsl:value-of select="$listValue"/>
								</xsl:element>
								<!-- /gmd:MD_MaintenanceFrequencyCode -->
							</xsl:element>
							<!-- /gmd:maintenanceAndUpdateFrequency -->
						</xsl:element>
						<!-- /MD_MaintenanceInformation -->
					</xsl:element>
					<!-- /gmd:resourceMaintenance -->
					<xsl:apply-templates select="distinfo/native">
						<xsl:with-param name="formatElement" select="'gmd:resourceFormat'"/>
					</xsl:apply-templates>
					
					<!-- Map the jurisdiction to an ANZLIC jurisdiction keyword -->
					<xsl:apply-templates select="//citeinfo/origin/jurisdic">
					</xsl:apply-templates>
					
					<xsl:if test="descript/theme">
						<xsl:element name="gmd:descriptiveKeywords">
							<xsl:element name="gmd:MD_Keywords">
								<xsl:apply-templates select="descript/theme/keyword"/>
								<xsl:element name="gmd:type">
									<!-- Mapping table row 8 -->
									<xsl:element name="gmd:MD_KeywordTypeCode">
										<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#MD_KeywordTypeCode</xsl:attribute>
										<xsl:attribute name="codeListValue"><xsl:value-of select="'theme'"/></xsl:attribute>
										<xsl:value-of select="'theme'"/>
									</xsl:element>
									<!-- /gmd:MD_KeywordTypeCode -->
								</xsl:element>
								<!-- /gmd:type -->
								<xsl:element name="gmd:thesaurusName">
									<!-- Mapping table row 9 -->
									<xsl:element name="gmd:CI_Citation">
										<xsl:element name="gmd:title">
											<xsl:element name="gco:CharacterString">ANZLIC Search Words</xsl:element>
										</xsl:element>
										<!-- /gmd:title -->
										<xsl:element name="gmd:date">
											<xsl:element name="gmd:CI_Date">
												<xsl:element name="gmd:date">
													<xsl:element name="gco:Date">2001</xsl:element>
												</xsl:element>
												<!-- /gmd:date -->
												<xsl:element name="gmd:dateType">
													<xsl:element name="gmd:CI_DateTypeCode">
														<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode</xsl:attribute>
														<xsl:attribute name="codeListValue"><xsl:value-of select="'revision'"/></xsl:attribute>
														<xsl:value-of select="'revision'"/>
													</xsl:element>
													<!-- /gmd:CI_DateTypeCode -->
												</xsl:element>
												<!-- /gmd:dateType -->
											</xsl:element>
											<!-- /gmd:CI_Date -->
										</xsl:element>
										<!-- /gmd:date -->
										
										<xsl:element name="gmd:edition">
											<xsl:element name="gco:CharacterString">Version 2</xsl:element>
										</xsl:element><!-- /gmd:edition -->
										<xsl:element name="gmd:editionDate">
											<xsl:element name="gco:Date">2001-02</xsl:element>
										</xsl:element>	<!-- /gmd:editionDate -->
										<xsl:element name="gmd:identifier">
											<xsl:element name="gmd:MD_Identifier">
												<xsl:element name="gmd:code">
													<xsl:element name="gco:CharacterString"><xsl:value-of select="$themesListLocation"/></xsl:element>
												</xsl:element>
												<!-- /gmd:code -->
											</xsl:element>
											<!-- /gmd:MD_Identifier -->
										</xsl:element>
										<!-- /gmd:identifier -->
										<xsl:element name="gmd:citedResponsibleParty">
											<xsl:element name="gmd:CI_ResponsibleParty">
												<xsl:element name="gmd:organisationName">
													<xsl:element name="gco:CharacterString">ANZLIC - the Spatial Information Council</xsl:element>
												</xsl:element> <!-- /gmd:organisationName -->
												<xsl:element name="gmd:role">
													<xsl:element name="gmd:CI_RoleCode">
														<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode</xsl:attribute>
														<xsl:attribute name="codeListValue"><xsl:value-of select="'custodian'"/></xsl:attribute>
														<xsl:value-of select="'custodian'"/>
													</xsl:element>
													<!-- /CI_RoleCode -->
												</xsl:element>
												<!-- /gmd:role -->
											</xsl:element>
										</xsl:element>  <!-- /gmd:citedResponsibleParty -->
									</xsl:element>
									<!-- /gmd:CI_Citation -->
								</xsl:element>
								<!-- /gmd:thesaurusName -->
							</xsl:element>
							<!-- /gmd:MD_Keywords -->
						</xsl:element>
						<!-- /gmd:descriptiveKeywords -->
					</xsl:if>	<!-- have any descript/theme -->
						
					<xsl:apply-templates select="distinfo/accconst"/>
					<xsl:element name="gmd:language">
						<xsl:element name="gco:CharacterString"><xsl:value-of select="$language"/></xsl:element>
					</xsl:element>
					<!-- /gmd:language -->
					<xsl:if test="$hierarchyLevel != 'tiles'">						
						<xsl:choose>
							<xsl:when test="$topicCategory">
								<xsl:element name="gmd:topicCategory">
									<xsl:element name="gmd:MD_TopicCategoryCode"><xsl:value-of select="$topicCategory"/></xsl:element>
								</xsl:element> <!-- /gmd:topicCategory -->
							</xsl:when>
							<xsl:otherwise>
								<!-- <xsl:apply-templates select="//theme/keyword"/>  -->
								<!-- 
								deal with rather painful mapping that 
								a) produces multiple output elements from input elements
								b) the resulting translation often has duplicates that are separated
								using a technique relying on XSLT 2.0 where we first build a variable containing all maps then sort it and skip duplicates
								<xsl:for-each select=".//theme/keyword">
									<xsl:value-of select="localFunctionsNS:topicMapping( .)"/>
								-->
								<xsl:for-each select="fn:distinct-values( localFunctionsNS:topicMapping(//theme/keyword) )">
									<xsl:element name="gmd:topicCategory">
										<xsl:element name="gmd:MD_TopicCategoryCode"><xsl:value-of select="."/></xsl:element>
									</xsl:element><!-- /gmd:topicCategory -->
								</xsl:for-each>
							</xsl:otherwise>
						</xsl:choose>
						
					</xsl:if>  <!-- $hiearchyLevel is dataset or series -->
					<xsl:element name="gmd:extent">
						<xsl:element name="gmd:EX_Extent">
							<xsl:apply-templates select="descript/spdom/place/dsgpolyo"/>
							<xsl:apply-templates select="descript/spdom/bounding"/>
							<xsl:apply-templates select="descript/spdom/place/keyword"/>
						</xsl:element>
						<!-- /gmd:EX_Extent -->
					</xsl:element>
					<!-- /gmd:extent -->
					<xsl:apply-templates select="timeperd"/>
					<xsl:if test="supplinf">
						<xsl:element name="gmd:supplementalInformation">
							<xsl:element name="gco:CharacterString">
								<xsl:value-of select="supplinf"/>
							</xsl:element>
						</xsl:element>  <!-- /gmd:supplementalInformation -->
					</xsl:if>
				</xsl:element>
				<!-- /gmd:MD_DataIdentification -->
			</xsl:element>
			<!-- /gmd:identificationInfo -->
			<xsl:element name="gmd:distributionInfo">
				<xsl:element name="gmd:MD_Distribution">
					<xsl:apply-templates select="distinfo/avlform">
						<xsl:with-param name="formatElement" select="'gmd:distributionFormat'"/>
					</xsl:apply-templates>
				</xsl:element>
				<!-- /gmd:MD_Distribution -->
			</xsl:element>
			<!-- /gmd:distributionInfo -->
			<xsl:apply-templates select="dataqual"/>
			<!-- NO RECURSIVE PROCESSING OF DEFAULT ELEMENTS <xsl:apply-templates/>  -->
		</xsl:element>
		<!-- /gmd:MD_Metadata -->
	</xsl:template>
	<!-- match="/anzmeta" -->
	<!--
	                    S U B - E L E M E N T S
	-->
	<xsl:template match="//cntinfo">
		<xsl:param name="role"/>
		<!-- output within gmd:MD_DataIdentification -->
		<xsl:element name="gmd:CI_ResponsibleParty">
			<xsl:element name="gmd:organisationName">
				<!-- Mapping table row 34 -->
				<xsl:element name="gco:CharacterString">
					<xsl:value-of select="cntorg"/>
				</xsl:element>
			</xsl:element>
			<!-- /gmd:organisationName -->
			<xsl:if test="cntpos">
				<xsl:element name="gmd:positionName">
					<!-- Mapping table row 35 -->
					<xsl:element name="gco:CharacterString">
						<xsl:value-of select="cntpos"/>
					</xsl:element>
				</xsl:element>
				<!-- /gmd:positionName -->
			</xsl:if>
			<xsl:element name="gmd:contactInfo">
				<xsl:element name="gmd:CI_Contact">
					<xsl:if test="cntvoice or cntfax">
						<xsl:element name="gmd:phone">
							<xsl:element name="gmd:CI_Telephone">
								<xsl:apply-templates select="cntvoice"/>
								<xsl:apply-templates select="cntfax"/>
							</xsl:element>
							<!-- /gmd:CI_Telephone -->
						</xsl:element>
						<!-- /gmd:phone -->
					</xsl:if>
					<xsl:element name="gmd:address">
						<xsl:element name="gmd:CI_Address">
							<xsl:element name="gmd:deliveryPoint">
								<!-- Mapping table row 36 -->
								<xsl:element name="gco:CharacterString">
									<xsl:value-of select="address"/>
								</xsl:element>
							</xsl:element>
							<!-- /gmd:deliveryPoint -->
							<xsl:element name="gmd:city">
								<!-- Mapping table row 37 -->
								<xsl:element name="gco:CharacterString">
									<xsl:value-of select="normalize-space(city)"/>
								</xsl:element>
							</xsl:element>
							<!-- gmd:city -->
							<xsl:element name="gmd:administrativeArea">
								<!-- Mapping table row 38 -->
								<xsl:element name="gco:CharacterString">
									<xsl:value-of select="normalize-space(state)"/>
								</xsl:element>
							</xsl:element>
							<!-- /gmd:administrativeArea as state -->
							<xsl:element name="gmd:postalCode">
								<!-- Mapping table row 40 -->
								<xsl:element name="gco:CharacterString">
									<xsl:value-of select="normalize-space(postal)"/>
								</xsl:element>
							</xsl:element>
							<!-- /gmd:postalCode -->
							<xsl:element name="gmd:country">
								<!-- Mapping table row 39 -->
								<xsl:element name="gco:CharacterString">
									<xsl:value-of select="normalize-space(country)"/>
								</xsl:element>
							</xsl:element>
							<!-- /gmd:country -->
							<xsl:if test="cntemail">
								<xsl:element name="gmd:electronicMailAddress">
									<!-- Mapping table row 43 -->
									<xsl:element name="gco:CharacterString">
										<xsl:value-of select="cntemail"/>
									</xsl:element>
								</xsl:element>
								<!-- /gmd:electronicMailAddress -->
							</xsl:if>
						</xsl:element>
						<!-- /gmd:CI_Address -->
					</xsl:element>
					<!-- /gmd:address -->
				</xsl:element>
				<!-- /gmd:CI_Contact -->
			</xsl:element>
			<!-- /gmd:contactInfo -->
			<xsl:element name="gmd:role">
				<xsl:element name="gmd:CI_RoleCode">
					<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode</xsl:attribute>
					<xsl:attribute name="codeListValue"><xsl:value-of select="$role"/></xsl:attribute>
					<xsl:value-of select="$role"/>
				</xsl:element>
				<!-- /CI_RoleCode -->
			</xsl:element>
			<!-- /gmd:role -->
		</xsl:element>
		<!-- /gmd:CI_ResponsibleParty -->
	</xsl:template>
	<!-- /match="//cntinfo" -->
	<!--

	-->
	<xsl:template name="simpleContactUsingMetadataOrganisation"><!-- used instead of match="//cntinfo" -->
		<xsl:param name="role" select="'custodian'"/>
		<!-- output within gmd:MD_DataIdentification -->
		<xsl:element name="gmd:CI_ResponsibleParty">
			<xsl:element name="gmd:organisationName">
				<!-- Mapping table row 34 -->
				<xsl:element name="gco:CharacterString">
					<xsl:value-of select="$metadataOrganisation"/>
				</xsl:element>
			</xsl:element>
			<!-- /gmd:organisationName -->
			<xsl:element name="gmd:role">
				<xsl:element name="gmd:CI_RoleCode">
					<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode</xsl:attribute>
					<xsl:attribute name="codeListValue"><xsl:value-of select="$role"/></xsl:attribute>
					<xsl:value-of select="$role"/>
				</xsl:element>
				<!-- /CI_RoleCode -->
			</xsl:element>
			<!-- /gmd:role -->
		</xsl:element>
		<!-- /gmd:CI_ResponsibleParty -->
	</xsl:template>
	<!-- /name="simpleContactUsingMetadataOrganisation" -->
	<!--

	-->	<xsl:template match="//cntvoice">
		<xsl:element name="gmd:voice">
			<!-- Mapping table row 41 -->
			<xsl:element name="gco:CharacterString">
				<xsl:value-of select="normalize-space(.)"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<!-- /match="//cntvoice" -->
	<!--

	-->
	<xsl:template match="//cntfax">
		<xsl:element name="gmd:facsimile">
			<!-- Mapping table row 42 -->
			<xsl:element name="gco:CharacterString">
				<xsl:value-of select="normalize-space(.)"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<!-- /match="//cntfax" -->
	<!--

	-->
	<xsl:template match="//citeinfo/origin/custod">
		<xsl:element name="gmd:citedResponsibleParty">
			<xsl:element name="gmd:CI_ResponsibleParty">
				<xsl:element name="gmd:organisationName">
					<!-- Mapping table row 3 -->
					<xsl:element name="gco:CharacterString">
						<xsl:value-of select="normalize-space(.)"/>
					</xsl:element>
				</xsl:element>
				<!-- /gmd:organisationName -->
				<!-- Don't map jurisdiction to administrativeArea. -->
<!--				<xsl:apply-templates select="//citeinfo/origin/jurisdic"/> -->
				<xsl:element name="gmd:role">
					<!-- Mapping table row 4 -->
					<xsl:element name="gmd:CI_RoleCode">
						<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode</xsl:attribute>
						<xsl:attribute name="codeListValue"><xsl:value-of select="'custodian'"/></xsl:attribute>
						<xsl:value-of select="'custodian'"/>
					</xsl:element>
					<!-- /CI_RoleCode -->
				</xsl:element>
				<!-- /gmd:role -->
			</xsl:element>
			<!-- /gmd:CI_ResponsibleParty -->
		</xsl:element>
		<!-- /gmd:citedResponsibleParty -->
	</xsl:template>
	<!-- /match="//citeinfo/origin/custod" -->
	<!--
		
	-->
	<xsl:template name="metadataOrganisationCitation"><!-- used instead of a match="//citeinfo/origin/custod" -->
		<xsl:element name="gmd:citedResponsibleParty">
			<xsl:element name="gmd:CI_ResponsibleParty">
				<xsl:element name="gmd:organisationName">
					<!-- Mapping table row 3 -->
					<xsl:element name="gco:CharacterString">
						<xsl:value-of select="$metadataOrganisation"/>
					</xsl:element>
				</xsl:element>
				<!-- /gmd:organisationName -->
				<!-- Don't map jurisdiction to administrativeArea. -->
<!--				<xsl:apply-templates select="//citeinfo/origin/jurisdic"/> -->
				<xsl:element name="gmd:role">
					<!-- Mapping table row 4 -->
					<xsl:element name="gmd:CI_RoleCode">
						<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode</xsl:attribute>
						<xsl:attribute name="codeListValue"><xsl:value-of select="'custodian'"/></xsl:attribute>
						<xsl:value-of select="'custodian'"/>
					</xsl:element>
					<!-- /CI_RoleCode -->
				</xsl:element>
				<!-- /gmd:role -->
			</xsl:element>
			<!-- /gmd:CI_ResponsibleParty -->
		</xsl:element>
		<!-- /gmd:citedResponsibleParty -->
	</xsl:template>
	<!-- /name="metadataOrganisationCitation" -->
	<!--

	-->
	<!-- Map jurisdiction to ANZLIC Jurisdiction keywords. -->
	
	<xsl:template match="//citeinfo/origin/jurisdic">
		<xsl:element name="gmd:descriptiveKeywords">
				<xsl:element name="gmd:MD_Keywords">
					<xsl:element name="gmd:keyword">
						<xsl:element name="gco:CharacterString"><xsl:value-of select="normalize-space(//citeinfo/origin/jurisdic)" /></xsl:element>
					</xsl:element>
					<xsl:element name="gmd:type">
						<xsl:element name="gmd:MD_KeywordTypeCode">
							<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#MD_KeywordTypeCode</xsl:attribute>
							<xsl:attribute name="codeListValue"><xsl:value-of select="'theme'"/></xsl:attribute>
							<xsl:value-of select="'theme'"/>
						</xsl:element>
					</xsl:element>
					<xsl:element name="gmd:thesaurusName">
						<xsl:element name="gmd:CI_Citation">
							<xsl:element name="gmd:title">
								<xsl:element name="gco:CharacterString">ANZLIC Jurisdictions</xsl:element>
							</xsl:element>
							<xsl:element name="gmd:date">
								<xsl:element name="gmd:CI_Date">
									<xsl:element name="gmd:date">
										<xsl:element name="gco:Date">2008-10-29</xsl:element>
									</xsl:element>
									<xsl:element name="gmd:dateType">
										<xsl:element name="gmd:CI_DateTypeCode">
											<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode</xsl:attribute>
											<xsl:attribute name="codeListValue">revision</xsl:attribute>
											<xsl:value-of select="revision"/>
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:element>
							<xsl:element name="gmd:edition">
								<xsl:element name="gco:CharacterString">Version 2.1</xsl:element>
							</xsl:element>
							<xsl:element name="gmd:editionDate">
								<xsl:element name="gco:Date">2008-10-29</xsl:element>
							</xsl:element>
							<xsl:element name="gmd:identifier">
								<xsl:element name="gmd:MD_Identifier">
									<xsl:element name="gmd:code">
										<xsl:element name="gco:CharacterString">http://asdd.ga.gov.au/asdd/profileinfo/anzlic-jurisdic.xml#anzlic-jurisdic</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:element>
							<xsl:element name="gmd:citedResponsibleParty">
								<xsl:element name="gmd:CI_ResponsibleParty">
									<xsl:element name="gmd:organisationName">
										<xsl:element name="gco:CharacterString">ANZLIC the Spatial Information Council" </xsl:element>
									</xsl:element>
									<xsl:element name="gmd:role">
										<xsl:element name="gmd:CI_RoleCode">
											<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode</xsl:attribute>
											<xsl:attribute name="codeListValue">custodian</xsl:attribute>
											<xsl:value-of select="custodian" />
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:element>

	</xsl:template>
		
	<!--

	-->
	<xsl:template match="//distinfo/native|//distinfo/avlform">
		<xsl:param name="formatElement" select="'BADFORMAT'"/>
		<!-- output within gmd:MD_DataIdentification and gmd:MD_Distribution, multiple times, flattens the avlform/digform vs native/digform etc into just name -->
		<xsl:for-each select=".//formname">
			<xsl:element name="{$formatElement}">
				<xsl:element name="gmd:MD_Format">
					<!-- Mapping table row 24, 26 -->
					<xsl:element name="gmd:name">
						<xsl:element name="gco:CharacterString">
							<xsl:value-of select="normalize-space(.)"/>
						</xsl:element>
					</xsl:element>
					<!-- /gmd:name -->
					<xsl:element name="gmd:version">
						<!-- Mapping table row 27 -->
						<xsl:element name="gco:CharacterString">none</xsl:element>
					</xsl:element>
					<!-- /gmd:version -->
				</xsl:element>
				<!-- /gmd:MD_Format -->
			</xsl:element>
			<!-- /$formatElement either gmd:resourceFormat or gmd:distributionFormat-->
		</xsl:for-each>
	</xsl:template>
	<!-- / match="//distinfo/native|//distinfo/avlform" -->
	<!--

	-->
	<xsl:template match="//descript/theme/keyword">
		<!-- output within gmd:MD_Keywords, multiple times -->
		<xsl:element name="gmd:keyword">
			<!-- Mapping table row 7 -->
			<xsl:element name="gco:CharacterString">
				<xsl:value-of select="normalize-space(.)"/>
			</xsl:element>
		</xsl:element>
		<!-- /gmd:keyword -->		
	</xsl:template>
	<!-- / match="//descript/theme/keyword" -->
	<!--

	-->
	<xsl:template match="//distinfo/accconst">
		<!-- output within gmd:MD_DataIdentification, multiple times -->
		<xsl:element name="gmd:resourceConstraints">
			<!-- Mapping table row 28 -->
			<xsl:element name="gmd:MD_Constraints">
				<xsl:element name="gmd:useLimitation">
					<xsl:element name="gco:CharacterString">
						<xsl:value-of select="normalize-space(.)"/>
					</xsl:element>
				</xsl:element>
				<!-- /gmd:useLimitation -->
			</xsl:element>
			<!-- /gmd:MD_Constraints -->
		</xsl:element>
		<!-- /gmd:resourceConstraints -->
	</xsl:template>
	<!-- / match="//distinfo/accconst" -->
	<!--

	-->
	<xsl:template match="//descript/spdom/bounding">
		<!-- output within gmd:EX_Extent -->
		<xsl:element name="gmd:geographicElement">
			<!-- Mapping table row 15 -->
			<xsl:element name="gmd:EX_GeographicBoundingBox">
				<xsl:element name="gmd:westBoundLongitude">
					<!-- Mapping table row 19 -->
					<xsl:element name="gco:Decimal">
						<xsl:value-of select="normalize-space(westbc)"/>
					</xsl:element>
					<!-- /gco:Decimal -->
				</xsl:element>
				<xsl:element name="gmd:eastBoundLongitude">
					<!-- Mapping table row 18 -->
					<xsl:element name="gco:Decimal">
						<xsl:value-of select="normalize-space(eastbc)"/>
					</xsl:element>
					<!-- /gco:Decimal -->
				</xsl:element>
				<xsl:element name="gmd:southBoundLatitude">
					<!-- Mapping table row 17 -->
					<xsl:element name="gco:Decimal">
						<xsl:value-of select="normalize-space(southbc)"/>
					</xsl:element>
					<!-- /gco:Decimal -->
				</xsl:element>
				<xsl:element name="gmd:northBoundLatitude">
					<!-- Mapping table row 16 -->
					<xsl:element name="gco:Decimal">
						<xsl:value-of select="normalize-space(northbc)"/>
					</xsl:element>
					<!-- /gco:Decimal -->
				</xsl:element>
			</xsl:element>
			<!-- /gmd:EX_GeographicBoundingBox -->
		</xsl:element>
		<!-- /gmd:geographicElement -->
	</xsl:template>
	<!-- / match="//descript/spdom/bounding" -->
	<!--

	-->
	<xsl:template match="//descript/spdom/place/keyword">
		<!-- output within gmd:EX_Extent -->
		<xsl:element name="gmd:geographicElement">
			<!-- Mapping table row 13 -->
			<xsl:element name="gmd:EX_GeographicDescription">
				<xsl:element name="gmd:extentTypeCode">
					<xsl:element name="gco:Boolean">true</xsl:element>
				</xsl:element>
				<!-- /gmd:extentTypeCode -->
				<xsl:element name="gmd:geographicIdentifier">
					<xsl:element name="gmd:MD_Identifier">
						<xsl:element name="gmd:authority">
							<xsl:element name="gmd:CI_Citation">
								<xsl:element name="gmd:title">
									<xsl:element name="gco:CharacterString">ANZLIC Geographic Extent Names - <xsl:call-template name="GENMapping"><xsl:with-param name="GENkeyword" select="normalize-space(@thesaurus)"/></xsl:call-template></xsl:element>
								</xsl:element>

								<xsl:element name="gmd:date">
									<xsl:element name="gmd:CI_Date">
										<xsl:element name="gmd:date">
											<xsl:element name="gco:Date">
												<xsl:call-template name="formatISO8601date">
													<xsl:with-param name="indate" select="'2006-10-10'"/>
												</xsl:call-template>
											</xsl:element>
											<!-- gco:Date -->
										</xsl:element><!-- /gmd:date -->
										<xsl:element name="gmd:dateType">
											<xsl:element name="gmd:CI_DateTypeCode">
												<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode</xsl:attribute>
												<xsl:attribute name="codeListValue"><xsl:value-of select="'revision'"/></xsl:attribute>
												<xsl:value-of select="'revision'"/>
											</xsl:element>
											<!-- /gmd:CI_DateTypeCode -->
										</xsl:element>
										<!-- /gmd:dateType -->
									</xsl:element>
									<!-- /gmd:CI_Date -->
								</xsl:element>
								<!-- /gmd:date -->
								<xsl:element name="gmd:edition">
									<xsl:element name="gco:CharacterString">Version 2</xsl:element>
								</xsl:element> <!-- /gmd:edition -->
								<xsl:element name="gmd:editionDate">									
									<xsl:element name="gco:Date">
										<xsl:call-template name="formatISO8601date">
											<xsl:with-param name="indate" select="'2001-02'"/>
										</xsl:call-template>
									</xsl:element>
									<!-- gco:Date -->
								</xsl:element>
								<!-- /gmd:editionDate -->
								<xsl:element name="gmd:identifier">
									<xsl:element name="gmd:MD_Identifier">
										<xsl:element name="gmd:code">
											<xsl:element name="gco:CharacterString"><xsl:call-template name="GENMappingCode"><xsl:with-param name="GENkeyword" select="normalize-space(@thesaurus)"/></xsl:call-template></xsl:element>
										</xsl:element>
										<!-- /gmd:code -->
									</xsl:element>
									<!-- /gmd:MD_Identifier -->
								</xsl:element>
								<!-- /gmd:identifier -->
								<xsl:element name="gmd:citedResponsibleParty">
									<xsl:element name="gmd:CI_ResponsibleParty">
										<xsl:element name="gmd:organisationName">
											<xsl:element name="gco:CharacterString">ANZLIC - the Spatial Information Council</xsl:element>
										</xsl:element> <!-- /gmd:organisationName -->
										<xsl:element name="gmd:role">
											<xsl:element name="gmd:CI_RoleCode">
												<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode</xsl:attribute>
												<xsl:attribute name="codeListValue"><xsl:value-of select="'custodian'"/></xsl:attribute>
												<xsl:value-of select="'distributor'"/>
											</xsl:element>
											<!-- /CI_RoleCode -->
										</xsl:element>
										<!-- /gmd:role -->
									</xsl:element>
								</xsl:element>  <!-- /gmd:citedResponsibleParty -->
							</xsl:element>
							<!-- /gmd:CI_Citation -->
						</xsl:element>
						<!-- /gmd:authority -->
						<xsl:element name="gmd:code">
							<!-- Mapping table row 13 -->
							<xsl:element name="gco:CharacterString">
								<xsl:value-of select="normalize-space(.)"/>
							</xsl:element>
						</xsl:element>
						<!-- /gmd:code -->
					</xsl:element>
					<!-- /gmd:MD_Identifier -->
				</xsl:element>
				<!-- /gmd:geographicIdentifier -->
			</xsl:element>
			<!-- /gmd:EX_GeographicDescription -->
		</xsl:element>
		<!-- /gmd:geographicElement -->
	</xsl:template>
	<!-- / match="//descript/spdom/bounding" -->
	<!--

	-->
	<xsl:template match="//descript/spdom/place/dsgpolyo">
		<xsl:element name="gmd:geographicElement">
			<!-- Mapping table row 14 -->
			<xsl:element name="gmd:EX_BoundingPolygon">
				<xsl:element name="gmd:polygon">
					<xsl:element name="gml:Polygon">
						<xsl:attribute name="gml:id" select="generate-id()"/>
						<xsl:element name="gml:exterior"> 
							<xsl:element name="gml:LinearRing">
								<xsl:for-each select="vertex">
									<xsl:element name="gml:pos">
										<xsl:value-of select="concat(lat,' ',long)"/>
									</xsl:element>
								</xsl:for-each>
							</xsl:element><!-- /gml:LinearRing -->
						</xsl:element><!-- /gml:exterior -->
					</xsl:element> <!-- /gml:Polygon -->
				</xsl:element>
				<!-- /gmd:polygon -->
			</xsl:element>
			<!-- /gmd:EX_BoundingPolygon -->
		</xsl:element>
		<!-- /gmd:geographicElement -->
	</xsl:template>
	<!-- / match="//descript/spdom/dsgpolyo" -->
	<!--

	-->
	<xsl:template match="//timeperd">
		<!-- output within gmd:MD_DataIdentification, multiple times -->
		<xsl:element name="gmd:extent">
			<xsl:element name="gmd:EX_Extent">
				<xsl:element name="gmd:temporalElement">
					<xsl:element name="gmd:EX_TemporalExtent">
						<xsl:element name="gmd:extent">
							<!-- YES, THIS IS A NESTED gmd:extent -->
							<xsl:element name="gml:TimePeriod">
								<xsl:attribute name="gml:id" select="generate-id(.)"/>
								<!-- Mapping table row 20 -->
								<xsl:apply-templates select="begdate"/>
								<!-- Mapping table row 21 -->
								<xsl:apply-templates select="enddate"/>
							</xsl:element>
							<!-- /gml:TimePeriod -->
						</xsl:element>
						<!-- /gmd:EX_Extent -->
					</xsl:element>
					<!-- /gmd:EX_TemporalExtent -->
				</xsl:element>
				<!-- /gmd:temporalElement -->
			</xsl:element>
			<!-- /gmd:EX_Extent -->
		</xsl:element>
		<!-- /gmd:extent -->
	</xsl:template>
	<!-- / match="//timeperd" -->
	<!--

	-->
	<xsl:template match="//begdate/date">
		<xsl:element name="gml:beginPosition">
			<xsl:call-template name="formatISO8601date">
				<xsl:with-param name="indate" select="normalize-space(.)"/>
			</xsl:call-template>
		</xsl:element>
		<!-- gml:beginPosition -->
	</xsl:template>
	<!-- /match="//begdate/date -->
	<!--

	-->
	<xsl:template match="//begdate/keyword">
		<xsl:element name="gml:beginPosition">
			<xsl:choose><!-- left in here in case other cases come up or decide to vary rules, despite currently being effectively fixed content -->
				<xsl:when test="normalize-space(.) ='Not Known'">
					<xsl:attribute name="indeterminatePosition">unknown</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="indeterminatePosition">unknown</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
		<!-- gml:beginPosition -->
	</xsl:template>
	<!-- /match="//begdate/keyword -->
	<!--

	-->
	<xsl:template match="//enddate/date">
		<xsl:element name="gml:endPosition">
			<xsl:call-template name="formatISO8601date">
				<xsl:with-param name="indate" select="normalize-space(.)"/>
			</xsl:call-template>
		</xsl:element>
		<!-- gml:endPosition -->
	</xsl:template>
	<!-- /match="//enddate/date -->
	<!--

	-->
	<xsl:template match="//enddate/keyword">
		<xsl:element name="gml:endPosition">
			<xsl:choose>
				<xsl:when test="normalize-space(.)='Current'">
					<xsl:attribute name="indeterminatePosition">now</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="indeterminatePosition">unknown</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
		<!-- gml:endPosition -->
	</xsl:template>
	<!-- /match="//enddate/keyword -->
	<!--

	-->
	<xsl:template match="//dataqual">
		<xsl:element name="gmd:dataQualityInfo">
			<xsl:element name="gmd:DQ_DataQuality">
				<xsl:copy-of select="@uuid"/>
				<xsl:element name="gmd:scope">
					<xsl:element name="gmd:DQ_Scope">
						<xsl:element name="gmd:level">
							<xsl:element name="gmd:MD_ScopeCode">
								<xsl:attribute name="codeList">http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#MD_ScopeCode</xsl:attribute>
								<xsl:attribute name="codeListValue" select="$hierarchyLevel"/>
								<xsl:value-of select="$hierarchyLevelName"/><!-- yes this is on purpose, we are supposed to have the slightly different content in the element, ie: Dataset vs dataset -->
							</xsl:element>
						</xsl:element>
						<!-- /gmd:level -->
					</xsl:element>
					<!-- /gmd:DQ_Scope -->
				</xsl:element>
				<!-- /gmd:scope -->
				<xsl:element name="gmd:report">
					<xsl:if test="complete">
						<xsl:element name="gmd:DQ_CompletenessOmission">
							<xsl:element name="gmd:result">
								<xsl:element name="gmd:DQ_ConformanceResult">
									<xsl:element name="gmd:specification">
										<xsl:call-template name="DummyCitation"/>
									</xsl:element>
									<!-- /gmd:specification -->
									<xsl:element name="gmd:explanation">
										<xsl:element name="gco:CharacterString">
											<xsl:value-of select="normalize-space(complete)"/>
										</xsl:element>
									</xsl:element>
									<!-- /gmd:explanation -->
									<xsl:element name="gmd:pass">
										<xsl:attribute name="gco:nilReason">missing</xsl:attribute>
										<!--
										<xsl:element name="gco:Boolean">true</xsl:element>
										-->
									</xsl:element>
									<!-- /gmd:pass change to missing -->
								</xsl:element>
								<!-- /gmd:DQ_ConformanceResult -->
							</xsl:element>
							<!-- /gmd:result -->
						</xsl:element>
						<!-- /gmd:DQ_CompletenessOmission -->
					</xsl:if>
				</xsl:element>
				<!-- /gmd:report -->
				<xsl:apply-templates select="posacc"/>
				<!-- Mapping table row 30 -->
				<xsl:apply-templates select="attracc"/>
				<!-- Mapping table row 31 -->
				<xsl:apply-templates select="logic"/>
				<!-- Mapping table row 32 -->
				<!-- Mapping table row 33 -->
				<xsl:element name="gmd:lineage">
					<xsl:element name="gmd:LI_Lineage">
						<xsl:element name="gmd:statement">
							<!-- Mapping table row 29 -->
							<xsl:element name="gco:CharacterString">
								<xsl:value-of select="normalize-space(lineage)"/>
							</xsl:element>
						</xsl:element>
						<!-- /gmd:statement -->
					</xsl:element>
					<!-- /gmd:LI_Lineage -->
				</xsl:element>
				<!-- /gmd:lineage -->
			</xsl:element>
			<!-- /gmd:DQ_DataQuality -->
		</xsl:element>
		<!-- gmd:dataQualityInfo -->
	</xsl:template>
	<!-- /match="//enddate/keyword -->
	<!--

	-->
	<xsl:template match="//status/update/keyword">
		<!-- Mapping table row 23 -->
		<xsl:variable name="cleanKey" select="normalize-space(.)"/>
		<xsl:choose>
			<xsl:when test="$cleanKey='Not Planned'">notPlanned</xsl:when>
			<xsl:when test="$cleanKey='Annually'">annually</xsl:when>
			<xsl:when test="$cleanKey='As required'">asNeeded</xsl:when>
			<xsl:when test="$cleanKey='Bi-annually'">biAnnually</xsl:when>
			<xsl:when test="$cleanKey='Continual'">continual</xsl:when>
			<xsl:when test="$cleanKey='Daily'">daily</xsl:when>
			<xsl:when test="$cleanKey='Irregular'">irregular</xsl:when>
			<xsl:when test="$cleanKey='Monthly'">monthly</xsl:when>
			<xsl:when test="$cleanKey='Not Known'">unknown</xsl:when>
			<xsl:when test="$cleanKey='Not Planned'">notPlanned</xsl:when>
			<xsl:when test="$cleanKey='Quarterly'">quarterly</xsl:when>
			<xsl:when test="$cleanKey='Weekly'">week.ly</xsl:when><xsl:otherwise>
				<xsl:value-of select="$cleanKey"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- /match="//status/update/keyword" -->
	<!--

	-->
	<!--

	-->
	<xsl:template match="//dataqual/posacc">
		<!-- output within gmd:DQ_DataQuality -->
		<xsl:element name="gmd:report">
			<xsl:element name="gmd:DQ_AbsoluteExternalPositionalAccuracy">
				<xsl:element name="gmd:result">
					<xsl:element name="gmd:DQ_ConformanceResult">
						<xsl:element name="gmd:specification">
							<xsl:call-template name="DummyCitation"/>
						</xsl:element>
						<!-- /gmd:specification -->
						<xsl:element name="gmd:explanation">
							<xsl:element name="gco:CharacterString">
								<xsl:value-of select="normalize-space(.)"/>
							</xsl:element>
						</xsl:element>
						<!-- /gmd:explanation -->
						<xsl:element name="gmd:pass">
							<xsl:attribute name="gco:nilReason">missing</xsl:attribute>
							<!--
							<xsl:element name="gco:Boolean">true</xsl:element>
							-->
						</xsl:element>
						<!-- /gmd:pass changed to missing -->
					</xsl:element>
					<!-- /gmd:DQ_ConformanceResult -->
				</xsl:element>
				<!-- /gmd:result -->
			</xsl:element>
			<!-- /gmd:DQ_AbsoluteExternalPositionalAccuracy -->
		</xsl:element>
		<!-- /gmd:report -->
	</xsl:template>
	<!-- /match="//dataqual/posacc" -->
	<!--

	-->
	<xsl:template match="//logic">
		<!-- output within gmd:DQ_DataQuality -->
		<xsl:element name="gmd:report">
			<xsl:element name="gmd:DQ_ConceptualConsistency">
				<xsl:element name="gmd:result">
					<xsl:element name="gmd:DQ_ConformanceResult">
						<xsl:element name="gmd:specification">
							<xsl:call-template name="DummyCitation"/>
						</xsl:element>
						<!-- /gmd:specification -->
						<xsl:element name="gmd:explanation">
							<xsl:element name="gco:CharacterString">
								<xsl:value-of select="normalize-space(.)"/>
							</xsl:element>
						</xsl:element>
						<!-- /gmd:explanation -->
						<xsl:element name="gmd:pass">
							<xsl:attribute name="gco:nilReason">missing</xsl:attribute>
							<!--
							<xsl:element name="gco:Boolean">true</xsl:element>
							-->
						</xsl:element>
						<!-- /gmd:pass changed to missing. -->
					</xsl:element>
					<!-- /gmd:DQ_ConformanceResult -->
				</xsl:element>
				<!-- /gmd:result -->
			</xsl:element>
			<!-- /gmd:DQ_ConceptualConsistency -->
		</xsl:element>
		<!-- /gmd:report -->
	</xsl:template>
	<!-- /match="//dataqual/attracc" -->
	<!--
		
	-->
	<xsl:template match="//dataqual/attracc">
		<!-- output within gmd:DQ_DataQuality -->
		<xsl:element name="gmd:report">
			<xsl:element name="gmd:DQ_NonQuantitativeAttributeAccuracy">
				<xsl:element name="gmd:result">
					<xsl:element name="gmd:DQ_ConformanceResult">
						<xsl:element name="gmd:specification">
							<xsl:call-template name="DummyCitation"/>
						</xsl:element>
						<!-- /gmd:specification -->
						<xsl:element name="gmd:explanation">
							<xsl:element name="gco:CharacterString">
								<xsl:value-of select="normalize-space(.)"/>
							</xsl:element>
						</xsl:element>
						<!-- /gmd:explanation -->
						<xsl:element name="gmd:pass">
							<xsl:attribute name="gco:nilReason">missing</xsl:attribute>
							<!--
							<xsl:element name="gco:Boolean">true</xsl:element>
							-->
						</xsl:element>
						<!-- /gmd:pass changed to missing.-->
					</xsl:element>
					<!-- /gmd:DQ_ConformanceResult -->
				</xsl:element>
				<!-- /gmd:result -->
			</xsl:element>
			<!-- /gmd:DQ_NonQuantitativeAttributeAccuracy -->
		</xsl:element>
		<!-- /gmd:report -->
	</xsl:template><!-- /match=".//.logic" -->
	<!--
		S U B R O U T I N E S
	-->
	<xsl:template name="DummyCitation">
		<xsl:element name="gmd:CI_Citation">
			<xsl:element name="gmd:title">
				<xsl:attribute name="gco:nilReason">missing</xsl:attribute>
				<!--
				<xsl:element name="gco:CharacterString">
					<xsl:value-of select="'unknown'"/>
				</xsl:element>
				-->
			</xsl:element>
			<!-- /gmd:title -->
			<xsl:call-template name="UnknownDate"/>
		</xsl:element>
		<!-- /gmd:CI_Citation -->
	</xsl:template>
	<!-- /name="DummyCitation" -->
	<!--

	-->	<xsl:template name="UnknownDate">
		<xsl:element name="gmd:date">
			<xsl:element name="gmd:CI_Date">
				<xsl:element name="gmd:date">
					<xsl:attribute name="gco:nilReason" select="'unknown'"/>
				</xsl:element>
				<!-- /gmd:date -->
				<xsl:element name="gmd:dateType">
					<xsl:attribute name="gco:nilReason" select="'unknown'"/>
				</xsl:element>
				<!-- /gmd:dateType -->
			</xsl:element>
			<!-- /gmd:CI_Date -->
		</xsl:element>
		<!-- /gmd:date -->
	</xsl:template>
	<!-- /name="UnknownDate" -->
	<!--

	-->
	<xsl:template name="adminArea">
		<xsl:param name="keyword" select="'Australia'"/>
		<!-- should match an entry from 
	http://www.ga.gov.au/anzmeta/anzlic-jurisdic.lst 
		Australia
		Australian Capital Territory
		New South Wales
		New Zealand
		Northern Territory
		Queensland
		South Australia
		Tasmania
		Victoria
		Western Australia
		Other

	This subroutine allows us to include other common abbreviations in the source and map them across.
	If we wanted to use other than gco:CharacterString, eg: gmd:Country, we could do so
	-->
		<xsl:element name="gco:CharacterString">
			<xsl:choose>
				<xsl:when test="$keyword='Australia'">Australia</xsl:when>
				<xsl:when test="$keyword='Australian Capital Territory'">Australian Capital Territory</xsl:when>
				<xsl:when test="$keyword='New South Wales'">New South Wales</xsl:when>
				<xsl:when test="$keyword='New Zealand'">New Zealand</xsl:when>
				<xsl:when test="$keyword='Northern Territory'">Northern Territory</xsl:when>
				<xsl:when test="$keyword='Queensland'">Queensland</xsl:when>
				<xsl:when test="$keyword='South Australia'">South Australia</xsl:when>
				<xsl:when test="$keyword='Tasmania'">Tasmania</xsl:when>
				<xsl:when test="$keyword='Victoria'">Victoria</xsl:when>
				<xsl:when test="$keyword='Western Australia'">Western Australia</xsl:when>
				<xsl:when test="$keyword='Other'">Other</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="concat('UNKNOWN ADMINISTRATIVE AREA - ', $keyword)"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<!--

	-->
	<xsl:template name="formatISO8601date">
<!-- Function that is out of scope to deliver now but put here as nice central location to fix dates later -->
		<xsl:param name="indate" required="yes"/>
		<xsl:value-of select="$indate"/>
	</xsl:template>
	<!--
		M A P P I N G   T A B L E S
	-->
	<xsl:function name="localFunctionsNS:topicMapping">
		<!--  mapping values from http://asdd.ga.gov.au/asdd/work/anzlic-iso.txt.
		note how we potentially take a list of items so can be used directly on a matching expression returning multiple nodes
		-->
		<xsl:param name="topic-list"/>
		<xsl:for-each select="$topic-list">
			<xsl:variable name="topic" select="normalize-space(.)"/>
			<xsl:choose>
				<xsl:when test="$topic = 'AGRICULTURE'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'AGRICULTURE Crops'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'AGRICULTURE Horticulture'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'AGRICULTURE Irrigation'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'AGRICULTURE Livestock'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ATMOSPHERE'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ATMOSPHERE Air Quality'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ATMOSPHERE Greenhouse'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ATMOSPHERE Ozone'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ATMOSPHERE Pressure'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'BOUNDARIES'"><xsl:element name="topic">boundaries</xsl:element></xsl:when>
				<xsl:when test="$topic = 'BOUNDARIES Administrative'"><xsl:element name="topic">boundaries</xsl:element></xsl:when>
				<xsl:when test="$topic = 'BOUNDARIES Biophysical'"><xsl:element name="topic">boundaries</xsl:element><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'BOUNDARIES Cultural'"><xsl:element name="topic">boundaries</xsl:element><xsl:element name="topic">society</xsl:element></xsl:when>
				<xsl:when test="$topic = 'CLIMATE AND WEATHER'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'CLIMATE AND WEATHER Climate change'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'CLIMATE AND WEATHER Drought'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'CLIMATE AND WEATHER El Nino'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'CLIMATE AND WEATHER Extreme weather events'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'CLIMATE AND WEATHER Meteorology'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'CLIMATE AND WEATHER Radiation'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'CLIMATE AND WEATHER Rainfall'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'CLIMATE AND WEATHER Temperature'"><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'DEMOGRAPHY'"><xsl:element name="topic">society</xsl:element></xsl:when>
				<xsl:when test="$topic = 'DISEASE'"><xsl:element name="topic">health</xsl:element><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ECOLOGY'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ECOLOGY Community'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ECOLOGY Ecosystem'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ECOLOGY Habitat'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ECOLOGY Landscape'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ENERGY'"><xsl:element name="topic">utilitiesCommunication</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ENERGY Coal'"><xsl:element name="topic">utilitiesCommunication</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ENERGY Electricity'"><xsl:element name="topic">utilitiesCommunication</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ENERGY Petroleum'"><xsl:element name="topic">utilitiesCommunication</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ENERGY Renewable'"><xsl:element name="topic">utilitiesCommunication</xsl:element></xsl:when>
				<xsl:when test="$topic = 'ENERGY Use'"><xsl:element name="topic">utilitiesCommunication</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FAUNA'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FAUNA Exotic'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FAUNA Insects'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FAUNA Invertebrates'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FAUNA Native'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FAUNA Vertebrates'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FISHERIES'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FISHERIES Aquaculture'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FISHERIES Freshwater'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FISHERIES Marine'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FISHERIES Recreational'"><xsl:element name="topic">society</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FLORA'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FLORA Exotic'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FLORA Native'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FORESTS'"><xsl:element name="topic">farming</xsl:element><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FORESTS Agriforestry'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FORESTS Natural'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'FORESTS Plantation'"><xsl:element name="topic">farming</xsl:element></xsl:when>
				<xsl:when test="$topic = 'GEOSCIENCES'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'GEOSCIENCES Geochemistry'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'GEOSCIENCES Geology'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'GEOSCIENCES Geomorphology'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'GEOSCIENCES Geophysics'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'GEOSCIENCES Hydrogeology'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS Cyclones'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS Drought'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS Earthquake'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS Fire'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS Flood'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS Landslip'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS Manmade'"><xsl:element name="topic">society</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS Pests'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS Severe local storms'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HAZARDS Tsunamis'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HEALTH'"><xsl:element name="topic">health</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HERITAGE'"><xsl:element name="topic">society</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HERITAGE Aboriginal'"><xsl:element name="topic">society</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HERITAGE Architectural'"><xsl:element name="topic">society</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HERITAGE Natural'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HERITAGE World'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HUMAN ENVIRONMENT'"><xsl:element name="topic">structure</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HUMAN ENVIRONMENT Economics'"><xsl:element name="topic">structure</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HUMAN ENVIRONMENT Housing'"><xsl:element name="topic">structure</xsl:element><xsl:element name="topic">planningCadastre</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HUMAN ENVIRONMENT Livability'"><xsl:element name="topic">structure</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HUMAN ENVIRONMENT Planning'"><xsl:element name="topic">structure</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HUMAN ENVIRONMENT Structures and Facilities'"><xsl:element name="topic">structure</xsl:element></xsl:when>
				<xsl:when test="$topic = 'HUMAN ENVIRONMENT Urban Design'"><xsl:element name="topic">structure</xsl:element></xsl:when>
				<xsl:when test="$topic = 'INDUSTRY'"><xsl:element name="topic">economy</xsl:element></xsl:when>
				<xsl:when test="$topic = 'INDUSTRY Manufacturing'"><xsl:element name="topic">economy</xsl:element></xsl:when>
				<xsl:when test="$topic = 'INDUSTRY Mining'"><xsl:element name="topic">economy</xsl:element></xsl:when>
				<xsl:when test="$topic = 'INDUSTRY Other'"><xsl:element name="topic">economy</xsl:element></xsl:when>
				<xsl:when test="$topic = 'INDUSTRY Primary'"><xsl:element name="topic">economy</xsl:element></xsl:when>
				<xsl:when test="$topic = 'INDUSTRY Service'"><xsl:element name="topic">economy</xsl:element></xsl:when>
				<xsl:when test="$topic = 'LAND'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'LAND Cadastre'"><xsl:element name="topic">planningCadastre</xsl:element></xsl:when>
				<xsl:when test="$topic = 'LAND Cover'"><xsl:element name="topic">biota</xsl:element><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'LAND Geodesy'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'LAND Geography'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'LAND Ownership'"><xsl:element name="topic">planningCadastre</xsl:element></xsl:when>
				<xsl:when test="$topic = 'LAND Topography'"><xsl:element name="topic">elevation</xsl:element><xsl:element name="topic">environment</xsl:element><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'LAND Use'"><xsl:element name="topic">society</xsl:element></xsl:when>
				<xsl:when test="$topic = 'LAND Valuation'"><xsl:element name="topic">planningCadastre</xsl:element><xsl:element name="topic">economy</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MARINE'"><xsl:element name="topic">oceans</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MARINE Biology'"><xsl:element name="topic">oceans</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MARINE Coasts'"><xsl:element name="topic">oceans</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MARINE Estuaries'"><xsl:element name="topic">oceans</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MARINE Geology and Geophysics'"><xsl:element name="topic">oceans</xsl:element><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MARINE Human Impacts'"><xsl:element name="topic">oceans</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MARINE Meteorology'"><xsl:element name="topic">oceans</xsl:element><xsl:element name="topic">climatologyMeteorologyAtmosphere</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MARINE Reefs'"><xsl:element name="topic">oceans</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MINERALS'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MOLECULAR BIOLOGY'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'MOLECULAR BIOLOGY Genetics'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'OCEANOGRAPHY'"><xsl:element name="topic">geoscientificInformation</xsl:element><xsl:element name="topic">oceans</xsl:element></xsl:when>
				<xsl:when test="$topic = 'OCEANOGRAPHY Chemical'"><xsl:element name="topic">geoscientificInformation</xsl:element><xsl:element name="topic">oceans</xsl:element></xsl:when>
				<xsl:when test="$topic = 'OCEANOGRAPHY Physical'"><xsl:element name="topic">geoscientificInformation</xsl:element><xsl:element name="topic">oceans</xsl:element></xsl:when>
				<xsl:when test="$topic = 'PHOTOGRAPHY AND IMAGERY'"><xsl:element name="topic">imageryBaseMapsEarthCover</xsl:element></xsl:when>
				<xsl:when test="$topic = 'PHOTOGRAPHY AND IMAGERY Aerial'"><xsl:element name="topic">imageryBaseMapsEarthCover</xsl:element></xsl:when>
				<xsl:when test="$topic = 'PHOTOGRAPHY AND IMAGERY Remote Sensing'"><xsl:element name="topic">imageryBaseMapsEarthCover</xsl:element></xsl:when>
				<xsl:when test="$topic = 'PHOTOGRAPHY AND IMAGERY Satellite'"><xsl:element name="topic">imageryBaseMapsEarthCover</xsl:element></xsl:when>
				<xsl:when test="$topic = 'POLLUTION'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'POLLUTION Air'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'POLLUTION Noise'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'POLLUTION Soil'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'POLLUTION Water'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'SOIL'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'SOIL Biology'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'SOIL Chemistry'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'SOIL Erosion'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'SOIL Physics'"><xsl:element name="topic">geoscientificInformation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'TRANSPORTATION'"><xsl:element name="topic">transportation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'TRANSPORTATION Air'"><xsl:element name="topic">transportation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'TRANSPORTATION Land'"><xsl:element name="topic">transportation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'TRANSPORTATION Marine'"><xsl:element name="topic">transportation</xsl:element></xsl:when>
				<xsl:when test="$topic = 'UTILITIES'"><xsl:element name="topic">utilitiesCommunication</xsl:element></xsl:when>
				<xsl:when test="$topic = 'VEGETATION'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'VEGETATION Floristic'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'VEGETATION Structural'"><xsl:element name="topic">biota</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WASTE'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WASTE Greenhouse gas'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WASTE Heat'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WASTE Liquid'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WASTE Sewage'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WASTE Solid'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WASTE Toxic'"><xsl:element name="topic">environment</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER Groundwater'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER Hydrochemistry'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER Hydrology'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER Lakes'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER Quality'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER Rivers'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER Salinity'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER Supply'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER Surface'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:when test="$topic = 'WATER Wetlands'"><xsl:element name="topic">inlandWaters</xsl:element></xsl:when>
				<xsl:otherwise>UNKNOWN TOPIC '<xsl:value-of select="$topic"/>'</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:function>
	<!-- /name="topicMapping" -->
	<!--
	-->	<xsl:template name="GENMapping">
		<xsl:param name="GENkeyword" required="yes"/>
		<xsl:choose>
			<xsl:when test="$GENkeyword = 'Australia'">anzlic-australia</xsl:when>
			<xsl:when test="$GENkeyword = 'State or Territory'">anzlic-state_territory</xsl:when>
			<xsl:when test="$GENkeyword = 'External Territories'">anzlic-external_territories</xsl:when>
			<xsl:when test="$GENkeyword = '1:1 000 000 Map Series'">anzlic-1000k_map_series</xsl:when>
			<xsl:when test="$GENkeyword = '1:250 000 Map Series (Specials)'">anzlic-250k_map_series_sp</xsl:when>
			<xsl:when test="$GENkeyword = '1:250 000 Map Series'">anzlic-250k_map_series</xsl:when>
			<xsl:when test="$GENkeyword = '1:100 000 Map Series'">anzlic-100k_map_series</xsl:when>
			<xsl:when test="$GENkeyword = '1:100 000 Map Series (Specials)'">anzlic-100k_map_series_sp</xsl:when>
			<xsl:when test="$GENkeyword = '1:50 000 Map Series'">anzlic-50k_map_series</xsl:when>
			<xsl:when test="$GENkeyword = '1:25 000 Map Series'">anzlic-25k_map_series</xsl:when>
			<xsl:when test="$GENkeyword = 'Local Government Areas (LGA)'">anzlic-lga</xsl:when>
			<xsl:when test="$GENkeyword = 'Statistical Local Areas (SLA) 2000 Edition'">anzlic-sla_2000edition</xsl:when>
			<xsl:when test="$GENkeyword = 'Statistical Local Areas (SLA) 2001 Edition'">anzlic-sla_2001edition</xsl:when>
			<xsl:when test="$GENkeyword = 'Drainage Divisions and Major River Basins'">anzlic-drainage_division_river_basin</xsl:when>
			<xsl:when test="$GENkeyword = 'Interim Biogeographic Regionalisation of Australia (IBRA)'">anzlic-ibra</xsl:when>
			<xsl:when test="$GENkeyword = 'Interim Marine and Coastal Regionalisation of Australia (IMCRA)'">anzlic-imcra</xsl:when>
			<xsl:when test="$GENkeyword = 'Australian Navigational Charts'">anzlic-navigational_charts</xsl:when>
			<xsl:when test="$GENkeyword = 'Ocean and Sea Regions'">anzlic-ocean_sea</xsl:when>
			<xsl:when test="$GENkeyword = 'Marsden Grid Squares 5 degrees'">anzlic-marsden_5</xsl:when>
			<xsl:when test="$GENkeyword = 'Marsden Grid Squares 10 degrees'">anzlic-marsden_10</xsl:when>
			<xsl:otherwise>UNKNOWN GEN KEYWORD '<xsl:value-of select="$GENkeyword"/>'</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	<!-- /name="GENMapping" -->
	<!--
	-->
	<xsl:template name="GENMappingCode">
		<xsl:param name="GENkeyword" required="yes"/>
		<xsl:choose>
			<xsl:when test="$GENkeyword = 'Australia'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-australia</xsl:when>
			<xsl:when test="$GENkeyword = 'State or Territory'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-state_territory</xsl:when>
			<xsl:when test="$GENkeyword = 'External Territories'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-external_territories</xsl:when>
			<xsl:when test="$GENkeyword = '1:1 000 000 Map Series'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-1000k_map_series</xsl:when>
			<xsl:when test="$GENkeyword = '1:250 000 Map Series (Specials)'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-250k_map_series_sp</xsl:when>
			<xsl:when test="$GENkeyword = '1:250 000 Map Series'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-250k_map_series</xsl:when>
			<xsl:when test="$GENkeyword = '1:100 000 Map Series'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-100k_map_series</xsl:when>
			<xsl:when test="$GENkeyword = '1:100 000 Map Series (Specials)'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-100k_map_series_sp</xsl:when>
			<xsl:when test="$GENkeyword = '1:50 000 Map Series'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-50k_map_series</xsl:when>
			<xsl:when test="$GENkeyword = '1:25 000 Map Series'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-25k_map_series</xsl:when>
			<xsl:when test="$GENkeyword = 'Local Government Areas (LGA)'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-lga</xsl:when>
			<xsl:when test="$GENkeyword = 'Statistical Local Areas (SLA) 2000 Edition'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-sla_2000edition</xsl:when>
			<xsl:when test="$GENkeyword = 'Statistical Local Areas (SLA) 2001 Edition'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-sla_2001edition</xsl:when>
			<xsl:when test="$GENkeyword = 'Drainage Divisions and Major River Basins'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-drainage_division_river_basin</xsl:when>
			<xsl:when test="$GENkeyword = 'Interim Biogeographic Regionalisation of Australia (IBRA)'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-ibra</xsl:when>
			<xsl:when test="$GENkeyword = 'Interim Marine and Coastal Regionalisation of Australia (IMCRA)'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-imcra</xsl:when>
			<xsl:when test="$GENkeyword = 'Australian Navigational Charts'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-navigational_charts</xsl:when>
			<xsl:when test="$GENkeyword = 'Ocean and Sea Regions'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-ocean_sea</xsl:when>
			<xsl:when test="$GENkeyword = 'Marsden Grid Squares 5 degrees'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-marsden_5</xsl:when>
			<xsl:when test="$GENkeyword = 'Marsden Grid Squares 10 degrees'"><xsl:value-of select="$codesListLocation"/>anzlic-allgens.xml#anzlic-marsden_10</xsl:when>
			<xsl:otherwise>UNKNOWN GEN KEYWORD '<xsl:value-of select="$GENkeyword"/>'</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	<!-- /name="GENMappingCode" -->
	<!--

	-->
	<xsl:template match="processing-instruction()|comment()">
		<xsl:copy/>
	</xsl:template>
</xsl:stylesheet><!-- Stylus Studio meta-information - (c) 2004-2007. Progress Software Corporation. All rights reserved.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\anzlic-test-instances\ANZMETA2_App3.xml" htmlbaseurl="" outputurl="" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="yes" validator="internal" customvalidator="" ><parameterValue name="fileUUID" value="'xyzzy'"/><advancedProp name="sInitialMode" value=""/><advancedProp name="bXsltOneIsOkay" value="true"/><advancedProp name="bSchemaAware" value="true"/><advancedProp name="bXml11" value="false"/><advancedProp name="iValidation" value="0"/><advancedProp name="bExtensions" value="true"/><advancedProp name="iWhitespace" value="0"/><advancedProp name="sInitialTemplate" value=""/><advancedProp name="bTinyTree" value="true"/><advancedProp name="bWarnings" value="true"/><advancedProp name="bUseDTD" value="false"/><advancedProp name="iErrorHandling" value="fatal"/></scenario></scenarios><MapperMetaTag><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/><MapperBlockPosition></MapperBlockPosition><TemplateContext></TemplateContext><MapperFilter side="source"></MapperFilter></MapperMetaTag>
</metaInformation>
-->
