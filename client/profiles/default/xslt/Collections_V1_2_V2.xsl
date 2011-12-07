<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:gfc="http://www.isotc211.org/2005/gfc"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:srv="http://www.isotc211.org/2005/srv"
	xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:gsr="http://www.isotc211.org/2005/gsr"
	xmlns:gss="http://www.isotc211.org/2005/gss" xmlns:gts="http://www.isotc211.org/2005/gts"
	xmlns:gmi="http://www.isotc211.org/2005/gmi" xmlns:gmd="http://www.isotc211.org/2005/gmd"
	xmlns:gmx="http://www.isotc211.org/2005/gmx" xmlns:gco="http://www.isotc211.org/2005/gco"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output version="1.0" indent="no" method="xml" encoding="utf-8"
		omit-xml-declaration="no" />
	<xsl:template match="/gmd:MD_Metadata">
		<gmd:MD_Metadata
			xsi:schemaLocation="http://www.isotc211.org/2005/gco http://www.isotc211.org/2005/gco/gco.xsd http://www.isotc211.org/2005/gmd http://www.isotc211.org/2005/gmd/gmd.xsd">
			<xsl:apply-templates select="comment()" mode="copy-comments" />
			<xsl:copy-of select="gmd:fileIdentifier" />
			<gmd:language>
				<gco:CharacterString>eng</gco:CharacterString>
			</gmd:language>
			<gmd:hierarchyLevel>
				<xsl:element name="gmd:MD_ScopeCode">
					<xsl:attribute name="codeList">http://asdd.ga.gov.au/asdd/profileinfo/GAScopeCodeList.xml#MD_ScopeCode</xsl:attribute>
					<xsl:attribute name="codeListValue">collection</xsl:attribute>
				</xsl:element>
			</gmd:hierarchyLevel>
			<xsl:copy-of select="gmd:hierarchyLevelName" />
			<gmd:contact>
				<gmd:CI_ResponsibleParty>
					<gmd:organisationName>
						<gco:CharacterString>Geoscience Australia</gco:CharacterString>
					</gmd:organisationName>
					<gmd:role>
						<gmd:CI_RoleCode
							codeList="http://asdd.ga.gov.au/asdd/profileinfo/gmxCodelists.xml#CI_RoleCode"
							codeListValue="pointOfContact" />
					</gmd:role>
				</gmd:CI_ResponsibleParty>
			</gmd:contact>
			<gmd:dateStamp>
				<gco:Date>2011-06-28</gco:Date>
			</gmd:dateStamp>
			<gmd:metadataStandardName>
				<gco:CharacterString>ANZLIC Metadata Profile: An Australian/New
					Zealand Profile AS/NZS ISO 19115:2005, Geographic information -
					Metadata</gco:CharacterString>
			</gmd:metadataStandardName>
			<gmd:metadataStandardVersion>
				<gco:CharacterString>1.1</gco:CharacterString>
			</gmd:metadataStandardVersion>

			<xsl:apply-templates mode="gmd:identificationInfo"
				select="gmd:identificationInfo/gmd:MD_DataIdentification" />
			<xsl:apply-templates mode="gmd:distributionInfo"
				select="gmd:distributionInfo/gmd:MD_Distribution" />
			<xsl:apply-templates mode="gmd:metadataMaintenance"
				select="gmd:metadataMaintenance/gmd:MD_MaintenanceInformation" />
		</gmd:MD_Metadata>
	</xsl:template>
	<xsl:template match="*" mode="gmd:identificationInfo">
		<gmd:identificationInfo>
			<gmd:MD_DataIdentification>
				<gmd:citation>
					<gmd:CI_Citation>
						<gmd:title>
							<gco:CharacterString>
								<xsl:value-of
									select="gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString" />
							</gco:CharacterString>
						</gmd:title>
						<gmd:date>
							<gmd:CI_Date>
								<xsl:if test="gmd:date != ''">
									<gmd:date>
										<gco:Date>
											<xsl:value-of
												select="gmd:citation/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
										</gco:Date>
									</gmd:date>
								</xsl:if>
								<gmd:dateType>
									<xsl:element name="gmd:CI_DateTypeCode">
										<xsl:attribute name="codeList">http://asdd.ga.gov.au/asdd/profileinfo/gmxCodelists.xml#CI_DateTypeCode</xsl:attribute>
										<xsl:attribute name="codeListValue">revision</xsl:attribute>
									</xsl:element>
								</gmd:dateType>
							</gmd:CI_Date>
						</gmd:date>
						<gmd:identifier>
							<gmd:MD_Identifier>
								<gmd:authority>
									<gmd:CI_Citation>
										<gmd:title>
											<gco:CharacterString>
												<xsl:value-of
													select="gmd:citation/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:authority/gmd:CI_Citation/gmd:title/gco:CharacterString" />
											</gco:CharacterString>
										</gmd:title>
										<gmd:date gco:nilReason="unknown" />
										<gmd:otherCitationDetails>
											<gco:CharacterString>
												<xsl:value-of
													select="gmd:citation/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:authority/gmd:CI_Citation/gmd:otherCitationDetails/gco:CharacterString" />
											</gco:CharacterString>
										</gmd:otherCitationDetails>
									</gmd:CI_Citation>
								</gmd:authority>
								<gmd:code>
									<gco:CharacterString>
										<xsl:value-of
											select="gmd:citation/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:code/gco:CharacterString" />
									</gco:CharacterString>
								</gmd:code>
							</gmd:MD_Identifier>
						</gmd:identifier>
						<gmd:citedResponsibleParty>
							<gmd:CI_ResponsibleParty>
								<xsl:copy-of
									select="gmd:citation/gmd:CI_Citation/gmd:citedResponsibleParty/gmd:CI_ResponsibleParty/node()[name() != 'gmd:role']"></xsl:copy-of>
								<gmd:role>
									<gmd:CI_RoleCode
										codeList="http://asdd.ga.gov.au/asdd/profileinfo/gmxCodelists.xml#CI_RoleCode"
										codeListValue="custodian" />
								</gmd:role>
							</gmd:CI_ResponsibleParty>
						</gmd:citedResponsibleParty>
					</gmd:CI_Citation>
				</gmd:citation>
				<xsl:copy-of select="gmd:abstract" />
				<xsl:copy-of select="gmd:purpose" />
				<xsl:copy-of select="gmd:credit" />
				<xsl:copy-of select="gmd:resourceMaintenance" />
				<xsl:for-each select="gmd:resourceFormat">
					<gmd:resourceFormat>
						<gmd:MD_Format>
							<gmd:name>
								<xsl:copy-of select="gmd:MD_Format/gmd:name/gco:CharacterString" />
							</gmd:name>
							<gmd:version gco:nilReason="missing" />
							<gmd:specification>
								<xsl:copy-of
									select="gmd:MD_Format/gmd:specification/gco:CharacterString" />
							</gmd:specification>
							<gmd:formatDistributor>
								<gmd:MD_Distributor>
									<gmd:distributorContact gco:nilReason="missing" />
									<xsl:for-each
										select="gmd:MD_Format/gmd:formatDistributor/gmd:MD_Distributor/gmd:distributorTransferOptions">
										<gmd:distributorTransferOptions>
											<gmd:MD_DigitalTransferOptions>
												<gmd:onLine>
													<gmd:CI_OnlineResource>
														<gmd:linkage>
															<xsl:copy-of
																select="gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:linkage/gmd:URL" />
														</gmd:linkage>
														<gmd:description>
															<xsl:copy-of
																select="gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:description/gco:CharacterString" />
														</gmd:description>
													</gmd:CI_OnlineResource>
												</gmd:onLine>
												<gmd:offLine>
													<gmd:MD_Medium>
														<gmd:volumes>
															<xsl:copy-of
																select="gmd:MD_DigitalTransferOptions/gmd:offLine/gmd:MD_Medium/gmd:volumes/gco:Integer" />
														</gmd:volumes>
														<gmd:mediumFormat>
															<xsl:element name="gmd:MD_MediumFormatCode">
																<xsl:attribute name="codeList">http://xmet/profiles/ga-collections/codelists/collections.xmettextcodelist#MediumNameCode</xsl:attribute>
																<xsl:attribute name="codeListValue"><xsl:copy-of
																	select="gmd:MD_DigitalTransferOptions/gmd:offLine/gmd:MD_Medium/gmd:mediumFormat/gmd:MD_MediumFormatCode/@codeListValue" /></xsl:attribute>
															</xsl:element>
														</gmd:mediumFormat>
														<gmd:mediumNote>
															<xsl:copy-of
																select="gmd:MD_DigitalTransferOptions/gmd:offLine/gmd:MD_Medium/gmd:mediumNote/gco:CharacterString" />
														</gmd:mediumNote>
													</gmd:MD_Medium>
												</gmd:offLine>
											</gmd:MD_DigitalTransferOptions>
										</gmd:distributorTransferOptions>
									</xsl:for-each>
								</gmd:MD_Distributor>
							</gmd:formatDistributor>
						</gmd:MD_Format>
					</gmd:resourceFormat>
				</xsl:for-each>
				<gmd:resourceSpecificUsage>
					<gmd:MD_Usage>
						<gmd:specificUsage>
							<gco:CharacterString>
								<xsl:value-of
									select="gmd:resourceSpecificUsage[2]/gmd:MD_Usage/gmd:specificUsage/gco:CharacterString" />
							</gco:CharacterString>
						</gmd:specificUsage>
						<gmd:userContactInfo gco:nilReason="missing" />
					</gmd:MD_Usage>
				</gmd:resourceSpecificUsage>
				<gmd:resourceSpecificUsage>
					<gmd:MD_Usage>
						<gmd:specificUsage>
							<gco:CharacterString>
								<xsl:value-of
									select="gmd:resourceSpecificUsage[1]/gmd:MD_Usage/gmd:specificUsage/gco:CharacterString" />
							</gco:CharacterString>
						</gmd:specificUsage>
						<gmd:userContactInfo gco:nilReason="missing" />
					</gmd:MD_Usage>
				</gmd:resourceSpecificUsage>
				<xsl:for-each select="gmd:resourceConstraints[1]">
					<gmd:resourceConstraints>
						<xsl:choose>
							<xsl:when test="gmd:MD_Usage != ''">
								<xsl:copy-of select="gmd:MD_Usage" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="gco:nilReason">missing</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</gmd:resourceConstraints>
				</xsl:for-each>
				<xsl:for-each select="gmd:resourceConstraints[2]">
					<gmd:resourceConstraints>
						<xsl:choose>
							<xsl:when test="gmd:MD_Usage != ''">
								<xsl:copy-of select="gmd:MD_Usage" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="gco:nilReason">missing</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</gmd:resourceConstraints>
				</xsl:for-each>
				<xsl:for-each select="gmd:resourceConstraints[3]">
					<gmd:resourceConstraints>
						<xsl:choose>
							<xsl:when test="gmd:MD_Usage != ''">
								<xsl:copy-of select="gmd:MD_Usage" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="gco:nilReason">missing</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</gmd:resourceConstraints>
				</xsl:for-each>
				<xsl:for-each select="gmd:resourceConstraints/gmd:MD_SecurityConstraints">
					<gmd:resourceConstraints>
						<gmd:MD_SecurityConstraints>
							<gmd:classification>
								<xsl:choose>
									<xsl:when test="gmd:MD_Usage != ''">
										<xsl:element name="gmd:MD_ClassificationCode">
											<xsl:attribute name="codeList">http://asdd.ga.gov.au/asdd/profileinfo/australianClassificationCode.xml#MD_ClassificationCode</xsl:attribute>
											<xsl:attribute name="codeListValue"><xsl:copy-of
												select="gmd:classification/gmd:MD_ClassificationCode/@codeListValue" /></xsl:attribute>
										</xsl:element>
									</xsl:when>
								</xsl:choose>
							</gmd:classification>
						</gmd:MD_SecurityConstraints>
					</gmd:resourceConstraints>
				</xsl:for-each>
				<gmd:language>
					<gco:CharacterString>eng</gco:CharacterString>
				</gmd:language>
				<xsl:copy-of select="gmd:topicCategory" />
				<xsl:copy-of select="gmd:extent" />
				<xsl:copy-of select="gmd:supplementalInformation" />
			</gmd:MD_DataIdentification>
		</gmd:identificationInfo>
	</xsl:template>
	<xsl:template match="*" mode="gmd:metadataMaintenance">
		<gmd:metadataMaintenance>
			<gmd:MD_MaintenanceInformation>
				<gmd:maintenanceAndUpdateFrequency>
					<xsl:element name="gmd:MD_MaintenanceFrequencyCode">
						<xsl:attribute name="codeList">http://asdd.ga.gov.au/asdd/profileinfo/gmxCodelists.xml#MD_MaintenanceFrequencyCode</xsl:attribute>
						<xsl:attribute name="codeListValue"><xsl:value-of
							select="gmd:maintenanceAndUpdateFrequency/gmd:MD_MaintenanceFrequencyCode/@codeListValue" /></xsl:attribute>
					</xsl:element>
				</gmd:maintenanceAndUpdateFrequency>
				<xsl:copy-of select="node()[name() = 'gmd:maintenanceNote']" />
			</gmd:MD_MaintenanceInformation>
		</gmd:metadataMaintenance>
	</xsl:template>
	<xsl:template match="*" mode="gmd:distributionInfo">
		<gmd:distributionInfo>
			<gmd:MD_Distribution>
				<gmd:distributor>
					<gmd:MD_Distributor>
						<gmd:distributorContact>
							<gmd:CI_ResponsibleParty>
								<xsl:copy-of
									select="gmd:MD_Distribution/gmd:distributor/gmd:MD_Distributor/gmd:distributorContact/gmd:CI_ResponsibleParty/node()[name() != 'gmd:role']"></xsl:copy-of>
								<gmd:role>
									<gmd:CI_RoleCode
										codeList="http://asdd.ga.gov.au/asdd/profileinfo/gmxCodelists.xml#CI_RoleCode"
										codeListValue="distributor" />
								</gmd:role>
							</gmd:CI_ResponsibleParty>
						</gmd:distributorContact>
						<gmd:distributorFormat>
							<gmd:MD_Format>
								<gmd:name>
									<gco:CharacterString>
										<xsl:value-of
											select="gmd:distributor/gmd:MD_Distributor/gmd:distributorFormat/gmd:MD_Format/gmd:name/gco:CharacterString" />
									</gco:CharacterString>
								</gmd:name>
								<gmd:version gco:nilReason="missing" />
							</gmd:MD_Format>
						</gmd:distributorFormat>
						<gmd:distributorTransferOptions>
							<gmd:MD_DigitalTransferOptions>
								<gmd:unitsOfDistribution>
									<gco:CharacterString>entire collection</gco:CharacterString>
								</gmd:unitsOfDistribution>
								<gmd:transferSize gco:nilReason="withheld">
									<xsl:if test="gco:Real != ''">
										<gco:Real>
											<xsl:value-of
												select="gmd:distributionFormat/gmd:MD_Format/gmd:formatDistributor/gmd:MD_Distributor/gmd:distributorTransferOptions/gmd:MD_DigitalTransferOptions/gmd:transferSize/gco:Real" />
										</gco:Real>
									</xsl:if>
								</gmd:transferSize>
							</gmd:MD_DigitalTransferOptions>
						</gmd:distributorTransferOptions>
					</gmd:MD_Distributor>
				</gmd:distributor>
			</gmd:MD_Distribution>
		</gmd:distributionInfo>
	</xsl:template>
	<xsl:template match="comment()" mode="copy-comments">
		<xsl:comment>
			<xsl:value-of select="." />
		</xsl:comment>
	</xsl:template>
</xsl:stylesheet>


