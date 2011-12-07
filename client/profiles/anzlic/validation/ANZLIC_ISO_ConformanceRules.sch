<?xml version="1.0" encoding="UTF-8" standalone="yes"?>

<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
        
  	<!--
This Schematron schema implements the ANZLIC ISO Profile rules
These rules apply in addition to
1. XML Schema Validation using the ISO 19139 schema
2. the additional conformance rules implemented as Schematron schema ISO19139TableA1ConformanceRules.sch
-->
	<!-- 
This script was developed for ANZLIC - the Spatial Information Council 
by CSIRO and later updated by GA as part of a project to develop an XML implementation of the ANZLIC ISO Metadata Profile. 

December 2006.

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
	<sch:ns prefix="gml" uri="http://www.opengis.net/gml"/>
	<sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd"/>
	<sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco"/>
	<sch:ns prefix="xlink" uri="http://www.w3.org/1999/xlink"/>

    <!-- Set the nilReasonList variable to the list (inappropriate, missing,
        template, unknow or withheld.  This list should be changed if the XSD
        values in gml/basicTypes.xsd are changed.-->
    <sch:let name="nilReasonList"
    value="distinct-values(('inapplicable','missing','template','unknown','withheld'))"/>
    
    <!-- Test that the fileIdentifier exists and that it is not empty unless
    there is an appropriate nilReason attribute. -->
    
	<sch:pattern fpi="ANZLIC Metadata Profile Version 1.1.1 Annex B Table 5 row 3"><sch:title>fileIdentifier required</sch:title>
		<sch:rule context="//gmd:MD_Metadata">
		    <sch:assert test="gmd:fileIdentifier and ((normalize-space(gmd:fileIdentifier) ne '')  or
		        (normalize-space(gmd:fileIdentifier/gco:CharacterString) ne '') or
		        (gmd:fileIdentifier/@gco:nilReason =
		        $nilReasonList))">fileIdentifier not present</sch:assert>
			<!-- the text "fileIdentifier not present" only gets emitted if the assertion fails -->
		</sch:rule>
	</sch:pattern>
<!-- Test that every CharacterString element has content or it's parent has a
    valid nilReason attribute value. -->	
    <sch:pattern fpi="CharacterString must have content"><sch:title>CharacterString must have content or it's parent must have a valid nilReason attribute. Doesn't test if CharacterString exists for all relevant parents.</sch:title>
        <sch:rule context="//*[../gco:CharacterString]">
            <sch:report test="(normalize-space(../gco:CharacterString) eq '') and not(../@gco:nilReason = $nilReasonList)">CharacterString must have content or parent's nilReason attrbute must be legitimate.</sch:report>
        </sch:rule>
    </sch:pattern>
    
</sch:schema>