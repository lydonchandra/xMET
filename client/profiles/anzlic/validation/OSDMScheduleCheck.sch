<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron"
  queryBinding="xslt2">
  
  <!-- 
    Created by John Hockaday of Geoscience Australia  for Office of Spatial Data Management and based 
    on Rick Jelliffe's original code.
    
    This product is released under the Creative Commons Attribution 2.5 Australia Licence 
    (http://creativecommons.org/licenses/by/2.5/au/)
    
    Copyright Commonwealth of Australia (Geoscience Australia) 2009.
    
    This file is available from the following URL:
    http://asdd.ga.gov.au/asdd/profileinfo/OSDMScheduleCheck.sch
    
    This Schematron implements the OSDM constraint:
    OSDM requires an Australian Government Dataset to have an OSDM Schedule rating:
    If (no hierarchyLevel or hierarchyLevel = 'dataset') and (jurisdiction keyword = Australia') then
    schedule keyword must exist.. 
    
    History:
    ======
    2008-12-01 version 1.0
    - Origianl version.
    
    2008-12-12 version 1.1 John Hockaday
    - Checks only the 'osdm-schedule' dictionary. This allows the OSDM schedule to be located at a url
    other than http://asdd.ga.gov.au/asdd/profileinfo/osdm-schedule.xml
    
    2009-05-13 version 1.2 John Hockaday
     - Checks to see if the content of the OSDM Schedule keyword matches the content list at
     http://asdd.ga.gov.au/asdd/profileinfo/osdm-schedule.xml
    
  -->
  
  <sch:title>Schematron to check that Australian jurisdiction datasets have an OSDM schedule.</sch:title>
  <sch:p>Version 1.1</sch:p>
  <sch:ns prefix="gmx" uri="http://www.isotc211.org/2005/gmx"/>
  <sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco"/>
  <sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd"/>
  <sch:ns prefix="gml" uri="http://www.opengis.net/gml"/>
  
  <sch:pattern>
    <!-- The following include statement doesn't work.
    <sch:include href="http://asdd.ga.gov.au/asdd/profileinfo/keywordValidation.sch"/>
    -->
    <!-- Get the keyword -->
    <sch:rule context="gmd:MD_Keywords/gmd:keyword/gco:CharacterString">
            <!-- Get the url reference -->
      <sch:let name="URI-reference"
        value="../../gmd:thesaurusName/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:code/gco:CharacterString"/>
        <!-- Get the url web page -->
      <sch:let name="URI" value="substring-before( $URI-reference, '#')"/>
        <!-- Get the uname from within the XML code list file. -->
      <sch:let name="fragment" value="substring-after( $URI-reference, '#')"/>
        <!-- Get the content of the XML codelist document -->
      <sch:let name="code-list-document" value="document( $URI )"/>
        <!-- Get the CodeListDictionary gml:id attribute from the content of the XML document. -->
      <sch:let name="dictionary"
        value=" $code-list-document//gmx:CodeListDictionary[@gml:id = $fragment ]"/>

  <!-- Test to see if the OSDM Schedule code list can be found. -->
      <sch:report test=" ($fragment='osdm-schedule') and not($code-list-document)" role="debug">
        Can't find the OSDM schedule code list at <sch:value-of select="$URI"/>
      </sch:report>
 
      <sch:let name="hierarchy" value="//*/gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue"/>
      <sch:let name="jurisdiction" value="current()"/>
      
      <sch:let name="osdmCodeList" value="'http://asdd.ga.gov.au/asdd/profileinfo/osdm-schedule.xml'"/>
      
      <sch:let name="schedulePath" 
      value="//*/gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:code/gco:CharacterString"/>
      <sch:let name="scheduleOK" value="//*
        [contains(gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:code/gco:CharacterString, '#osdm-schedule')]"/>
     
       <!-- Test to see if the OSDM schedule list is filled out when the hierarchyLevel is null or 'dataset' and
       the anzlic jurisdiction is 'Australia'. -->
      <sch:report test="(not($hierarchy) or $hierarchy='dataset') 
        and ($fragment = 'anzlic-jurisdic')
        and ($jurisdiction = 'Australia')
        and (not($scheduleOK))
        ">
        OSDM requires an Australian Government 'dataset' to have an OSDM Schedule rating where:
        <sch:emph>hierarchyLevel</sch:emph> = '<sch:value-of select="$hierarchy"/>' and 
        <sch:emph>jurisdiction keyword</sch:emph> = '<sch:value-of select="current()"/>'.  
        <sch:emph>Note:</sch:emph> that the 
        <sch:emph>dictionary list</sch:emph> '<sch:value-of select="$schedulePath"/>' 
        does not contain the 
        <sch:emph>OSDM schedule dictionary</sch:emph> 'osdm-schedule' 
      </sch:report>
            
            <!-- Report if the keyword is not in the OSDM Schedule keyword list. -->
    <sch:report 
      test="not($dictionary/gmx:codeEntry/gmx:CodeDefinition[gml:identifier = current()]) and 
      not($dictionary/gmx:codeEntry/gmx:CodeDefinition[gml:description = current()])
      and ($fragment='osdm-schedule')"> 
      Could not find the keyword code "<sch:value-of select="current()"/>" 
      in the keyword code list
      "<sch:value-of select="$URI"/>#<sch:value-of select="$fragment"/>"
    </sch:report>
    </sch:rule>
    
    </sch:pattern>
</sch:schema>