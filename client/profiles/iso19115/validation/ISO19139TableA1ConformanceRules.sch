<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron"
	queryBinding="xslt2">
<!--
This Schematron schema implements the rules listed in ISO TS 19139 Table A.1 
"Conformance Rules not enforceable with XML Schema"
These rules apply in addition to
1. XML Schema Validation using the ISO 19139 schema
-->
<!-- 
This script was developed for ANZLIC - the Spatial Information Council 
by CSIRO and later updated by GA, BRS and CSIRO 
as part of a project to develop an XML implementation of the ANZLIC ISO Metadata Profile. 

December 2006, June 2007.

This work is licensed under the Creative Commons Attribution 2.5 License. 
To view a copy of this license, visit 
    http://creativecommons.org/licenses/by/2.5/au/ 
or send a letter to 

Creative Commons, 
543 Howard Street, 5th Floor, 
San Francisco, California, 94105, 
USA.
-->

	<sch:title>Schematron validation</sch:title>
	<sch:ns prefix="gml" uri="http://www.opengis.net/gml" />
	<sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd"/>
	<sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco"/>
	<sch:ns prefix="xlink" uri="http://www.w3.org/1999/xlink"/>
	
	<!-- Set the nilReasonList variable to the list (inappropriate, missing,
		template, unknow or withheld.  This list should be changed if the XSD
		values in gml/basicTypes.xsd are changed.-->
	<sch:let name="nilReasonList"
		value="distinct-values(('inapplicable','missing','template','unknown','withheld'))"/>

	<!--anzlic/trunk/gml/3.2.0/gmd/citation.xsd-->
	<!-- TEST 21 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row24"><sch:title>name required</sch:title>
		<sch:rule context="//gmd:CI_ResponsibleParty">
			<sch:assert test="(count(gmd:individualName) + count(gmd:organisationName) + count(gmd:positionName)) > 0">count of (individualName + organisationName + positionName) > 0</sch:assert>
		</sch:rule>
	</sch:pattern>
	<!-- anzlic/trunk/gml/3.2.0/gmd/constraints.xsd-->
	<!-- TEST  4 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row07"><sch:title>otherConstraints required if otherRestrictions</sch:title>
		<sch:rule context="//gmd:MD_LegalConstraints">
			<sch:report test="gmd:accessConstraints/gmd:MD_RestrictionCode/@codeListValue='otherRestrictions' and not(gmd:otherConstraints)">otherConstraints: documented if accessConstraints or useConstraints = "otherRestrictions"</sch:report>
			<sch:report test="gmd:useConstraints/gmd:MD_RestrictionCode/@codeListValue='otherRestrictions' and not(gmd:otherConstraints)">otherConstraints: documented if accessConstraints or useConstraints = "otherRestrictions"</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- anzlic/trunk/gml/3.2.0/gmd/content.xsd-->
	<!-- TEST 13 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row16"><sch:title>units required for values</sch:title>
		<sch:rule context="//gmd:MD_Band">
			<sch:report test="(gmd:maxValue or gmd:minValue) and not(gmd:units)">"units" is mandatory if "maxValue" or "minValue" are provided</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- anzlic/trunk/gml/3.2.0/gmd/dataQuality.xsd -->
	<!-- TEST 10 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row13"><sch:title>description required if no sourceExtent</sch:title>
		<sch:rule context="//gmd:LI_Source">
			<sch:assert test="gmd:description or gmd:sourceExtent">"description" is mandatory if "sourceExtent" is not documented</sch:assert>
		</sch:rule>
	</sch:pattern>
	<!-- TEST 11 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row14"><sch:title>sourceExtent required if no description</sch:title>
		<sch:rule context="//gmd:LI_Source">
			<sch:assert test="gmd:description or gmd:sourceExtent">"description" is mandatory if "sourceExtent" is not documented</sch:assert>
		</sch:rule>
	</sch:pattern>
	<!-- TEST  7 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row10"><sch:title>content mandatory for dataset or series</sch:title>
		<sch:rule context="//gmd:DQ_DataQuality">
			<sch:report test="(((count(*/gmd:LI_Lineage/gmd:source) +
				count(*/gmd:LI_Lineage/gmd:processStep)) = 0) and
				(gmd:scope/gmd:DQ_Scope/gmd:level/gmd:MD_ScopeCode/@codeListValue='dataset'
				or
				gmd:scope/gmd:DQ_Scope/gmd:level/gmd:MD_ScopeCode/@codeListValue='series'))
				and not(gmd:lineage/gmd:LI_Lineage/gmd:statement) and (gmd:lineage)">If(count(source) + count(processStep) =0) and (DQ_DataQuality.scope.level = 'dataset' or 'series') then statement is mandatory
			</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- TEST  8 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row11"><sch:title>source required if no statement or processStep</sch:title>
		<sch:rule context="//gmd:LI_Lineage">
			<sch:report test="not(gmd:source) and not(gmd:statement) and not(gmd:processStep)">"source" role is mandatory if LI_Lineage.statement and "processStep" role are not documented</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- TEST  9 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row12"><sch:title>processStep required if no statement or source</sch:title>
		<sch:rule context="//gmd:LI_Lineage">
			<sch:report test="not(gmd:processStep) and not(gmd:statement) and not(gmd:source)">"processStep" role is mandatory if LI_Lineage.statement and "source" role are not documented</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- TEST 5 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row08"><sch:title>dataset must have report or lineage</sch:title>
		<sch:rule context="//gmd:DQ_DataQuality">
			<sch:report test="gmd:scope/gmd:DQ_Scope/gmd:level/gmd:MD_ScopeCode/@codeListValue='dataset' and 
				not(gmd:report) and 
				not(gmd:lineage)">"report" or "lineage" role is mandatory if scope.DQ_Scope.level = 'dataset'</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- TEST  6 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row09"><sch:title>levelDescription needed unless dataset or series</sch:title>
		<sch:rule context="//gmd:DQ_Scope">
			<sch:assert test="gmd:level/gmd:MD_ScopeCode/@codeListValue='dataset' or 
				gmd:level/gmd:MD_ScopeCode/@codeListValue='series' or (gmd:levelDescription and
				((normalize-space(gmd:levelDescription) ne '') or
				(gmd:levelDescription/gmd:MD_ScopeDescription) or
				(gmd:levelDescription/@gco:nilReason =
				$nilReasonList)))">"levelDescription" is mandatory if "level"
				notEqual 'dataset' or 'series'</sch:assert>
		</sch:rule>
	</sch:pattern>
	<!-- anzlic/trunk/gml/3.2.0/gmd/distribution.xsd-->
	<!-- TEST 14 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row17"><sch:title>units required for density values</sch:title>
		<sch:rule context="//gmd:MD_Medium">
			<sch:report test="gmd:density and not(gmd:densityUnits)">"densityUnits" is mandatory if "density" is provided</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- test 15 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row18"><sch:title>MD_Format required</sch:title>
		<sch:rule context="//gmd:MD_Distribution">
			<sch:assert test="count(gmd:distributionFormat)>0 or count(gmd:distributor/gmd:MD_Distributor/gmd:distributorFormat)>0">count (distributionFormat + distributor/MD_Distributor/distributorFormat) > 0</sch:assert>
		</sch:rule>
	</sch:pattern>
	<!-- anzlic/trunk/gml/3.2.0/gmd/extent.xsd-->
	<!-- TEST 20 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row23"><sch:title>element required</sch:title>
		<sch:rule context="//gmd:EX_Extent">
			<sch:assert test="count(gmd:description)>0 or count(gmd:geographicElement)>0 or count(gmd:temporalElement)>0 or count(gmd:verticalElement)>0">count(description + geographicElement + temporalElement + verticalElement) >0"</sch:assert>
		</sch:rule>
	</sch:pattern>
	<!-- anzlic/trunk/gml/3.2.0/gmd/identification.xsd-->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row03"><sch:title>character set indication</sch:title>
		<!-- UNVERIFIED -->
		<sch:rule context="//gmd:MD_DataIdentification">
			<!-- characterSet: documented if ISO/IEC 10646 not used and not defined by the encoding standard -->
		</sch:rule>
	</sch:pattern>
	<!-- TEST  1 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row04"><sch:title>dataset must have extent</sch:title>
		<sch:rule context="//*[gmd:identificationInfo/gmd:MD_DataIdentification]">
			<sch:report test="(not(gmd:hierarchyLevel) or gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue='dataset') and 
				(count(//gmd:MD_DataIdentification/gmd:extent/*/gmd:geographicElement/gmd:EX_GeographicBoundingBox) + count (//gmd:MD_DataIdentification/gmd:extent/*/gmd:geographicElement/gmd:EX_GeographicDescription)) =0 ">MD_Metadata.hierarchyLevel = "dataset" (i.e. the default value of this property on the parent) implies 
						count (extent.geographicElement.EX_GeographicBoundingBox) + count (extent.geographicElement.EX_GeographicDescription) &gt;=1 </sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- TEST  2 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row05"><sch:title>dataset or series must have topicCategory</sch:title>
		<sch:rule context="//gmd:MD_DataIdentification">
			<sch:report test="(not(../../gmd:hierarchyLevel) or
				(../../gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue='dataset') or
				(../../gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue='series')) and
				(not(gmd:topicCategory))">
				topicCategory is mandatory  if MD_Metadata.hierarchyLevel equal "dataset" or
				"series" or doesn't exist</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- TEST  3 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row06"><sch:title>Either "aggregateDataSetName" or "aggregateDataSetIdentifier" must be documented</sch:title>
		<sch:rule context="//gmd:MD_AggregateInformation">
			<sch:assert test="gmd:aggregateDataSetName or gmd:aggregateDataSetIdentifier">Either "aggregateDataSetName" or "aggregateDataSetIdentifier" must be documented</sch:assert>
		</sch:rule>
	</sch:pattern>
	<!-- anzlic/trunk/gml/3.2.0/gmd/metadataEntity.xsd: -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row01"><sch:title>language indication</sch:title>
		<!-- UNVERIFIED -->
		<sch:rule context="//gmd:MD_Metadata">
			<sch:assert test="gmd:language and ((normalize-space(gmd:language) ne '')  or
				(normalize-space(gmd:language/gco:CharacterString) ne '') or
				(gmd:language/gmd:LanguageCode) or
				(gmd:language/@gco:nilReason =  $nilReasonList))">language not present</sch:assert>
			<!-- language: documented if not defined by the encoding standard. It can't
				be documented by the encoding because GML doesn't include xml:language. -->
		</sch:rule>
	</sch:pattern>
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row02"><sch:title>character set indication</sch:title>
		<!-- UNVERIFIED -->
		<sch:rule context="//gmd:MD_Metadata">
			<!-- characterSet: documented if ISO/IEC 10646 not used and not defined by
				the encoding standard. Can't tell if XML declaration has an encoding
				attribute. -->
		</sch:rule>
	</sch:pattern>
	<!-- anzlic/trunk/gml/3.2.0/gmd/metadataExtension.xsd-->
	<!-- TEST 16 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row19"><sch:title>detail required unless simple term</sch:title>
		<sch:rule context="//gmd:MD_ExtendedElementInformation">
			<sch:assert test="(gmd:dataType/gmd:MD_DatatypeCode/@codeListValue='codelist'
				or gmd:dataType/gmd:MD_DatatypeCode/@codeListValue='enumeration' or
				gmd:dataType/gmd:MD_DatatypeCode/@codeListValue='codelistElement') or
				(gmd:obligation and ((normalize-space(gmd:obligation) ne '')  or
				(gmd:obligation/gmd:MD_ObligationCode) or
				(gmd:obligation/@gco:nilReason =
				$nilReasonList)))">if "dataType" notEqual 'codelist', 'enumeration' or 'codelistElement' then "obligation" is mandatory</sch:assert>
			<sch:assert test="(gmd:dataType/gmd:MD_DatatypeCode/@codeListValue='codelist'
				or gmd:dataType/gmd:MD_DatatypeCode/@codeListValue='enumeration' or
				gmd:dataType/gmd:MD_DatatypeCode/@codeListValue='codelistElement') or 
				(gmd:maximumOccurrence and ((normalize-space(gmd:maximumOccurrence) ne '')  or
				(normalize-space(gmd:maximumOccurrence/gco:CharacterString) ne '') or
				(gmd:maximumOccurrence/@gco:nilReason =
				$nilReasonList)))">if "dataType" notEqual 'codelist', 'enumeration' or 'codelistElement' then "maximumOccurence" is mandatory</sch:assert>
			<sch:assert test="(gmd:dataType/gmd:MD_DatatypeCode/@codeListValue='codelist'
				or gmd:dataType/gmd:MD_DatatypeCode/@codeListValue='enumeration' or
				gmd:dataType/gmd:MD_DatatypeCode/@codeListValue='codelistElement') or (gmd:domainValue and 
				((normalize-space(gmd:domainValue) ne '')  or 
				(normalize-space(gmd:domainValue/gco:CharacterString) ne '') or
				(gmd:domainValue/@gco:nilReason =
				$nilReasonList)))">if "dataType" notEqual 'codelist', 'enumeration' or 'codelistElement' then "domainValue" is mandatory</sch:assert>
		</sch:rule>
	</sch:pattern>
	<!-- TEST 17 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row20"><sch:title>condition</sch:title>
		<sch:rule context="//gmd:MD_ExtendedElementInformation">
			<sch:report test="gmd:obligation/gmd:MD_ObligationCode='conditional' and not(gmd:condition)">if "obligation" = 'conditional' then "condition" is mandatory</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- TEST 18 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row21"><sch:title>domainCode</sch:title>
		<sch:rule context="//gmd:MD_ExtendedElementInformation">
			<sch:report test="gmd:dataType/gmd:MD_DatatypeCode/@codeListValue='codelistElement' and not(gmd:domainCode)">if "dataType" = 'codelistElement' then "domainCode" is mandatory</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- TEST 19 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row22"><sch:title>shortName</sch:title>
		<sch:rule context="//gmd:MD_ExtendedElementInformation">
			<sch:report
				test="gmd:dataType/gmd:MD_DatatypeCode/@codeListValue!='codelistElement' and
				not(gmd:shortName)">if "dataType" not equal to  'codelistElement' then "shortName" is mandatory</sch:report>
		</sch:rule>
	</sch:pattern>
	<!-- anzlic/trunk/gml/3.2.0/gmd/spatialRepresentation.xsd-->
	<!-- TEST 12 -->
	<sch:pattern fpi="ISOFTDS19139:2005-TableA1-Row15"><sch:title>checkPointDescription required if available</sch:title>
		<sch:rule context="//gmd:MD_Georectified">
			<sch:report test="(gmd:checkPointAvailability/gco:Boolean='1' or
				gmd:checkPointAvailability/gco:Boolean='true') and not(gmd:checkPointDescription)">"checkPointDescription" is mandatory if "checkPointAvailability" = 1 or true</sch:report>
		</sch:rule>
	</sch:pattern>
</sch:schema>
