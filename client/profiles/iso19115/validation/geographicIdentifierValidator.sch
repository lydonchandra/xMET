<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron"  >
  
  <!-- 
    Schematron that  looks up the geographic identifier code and checks that
    the code in the XML metadata document is valid
    according to that geogrpahic Identifier code in the ISO 19139 gmxCT_CodelistCatalogue XML files.
  -->
  <!--
 Similar to Rick Jelliffe's validation of ISO 19139 XML metadata record's keywords
 against ISO 19139 keyword code lists that are compliant to ISO 19139 gmx CT_CodelistCatalogue.
 See http://www.isotc211.org/2005/gmx/catalogues.xsd
 
 This product is released under the Creative Commons Attribution 2.5 Australia Licence 
 (http://creativecommons.org/licenses/by/2.5/au/)
  
 This file is available from the following URL:
 http://asdd.ga.gov.au/asdd/profileinfo/GENvalidation.sch
  
 History:
 ======
 2008-11-13 version 1.0
 - Origiinal version created by John Hockaday, Geoscience Australia, but based on Rick Jelliffe's code.
  
  -->
  
  <title>ISO 19139 Geographic Identifiers validator.</title>
  <ns prefix="gmx" uri="http://www.isotc211.org/2005/gmx"  /> 
  <ns prefix="gco" uri="http://www.isotc211.org/2005/gco"  />
  <ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd"  />
  <ns prefix="gml" uri="http://www.opengis.net/gml"  />
  
  <pattern>
    <rule context= "gmd:geographicIdentifier/gmd:MD_Identifier/gmd:code/gco:CharacterString"  >

      <let name= "URI-reference" 
        value="../../gmd:authority/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:code/gco:CharacterString" />    
      
      <let name="URI" value= "substring-before( $URI-reference, '#')" />
      
      <let name="fragment" value= "substring-after( $URI-reference, '#')" />
      
      <let name="code-list-document" value="document( $URI )" />
      
      <let name="dictionary" value=" $code-list-document//gmx:CodeListDictionary[@gml:id = $fragment ]" />
        
      <assert test=" string-length(normalize-space( $URI )) &gt; 0" role="debug">  
        The codelist file URI should not be empty.
        </assert>
   
      <assert test="$code-list-document" role="debug">
        Could not find the geographic identifier's code list at 
        "<value-of select="$URI"/>".
      </assert>
      
        <assert test=" string-length(normalize-space( $fragment )) &gt; 0" role="debug">
          The fragment identifier in a codelist URI should not be empty.
          </assert>
          
      <assert test="$dictionary" role="debug">
        Could not find the geographic identifier's dictionary 
        "<value-of select="$fragment"/>" in the code list
        "<value-of select="$URI"/>".
      </assert>
      
            <assert test=" $dictionary/gmx:codeEntry/gmx:CodeDefinition[gml:identifier = current()]" > 
              Could not find the geographic identifier code "<value-of select="current()"/>" 
              in the code list
              "<value-of select="$URI"/>#<value-of select="$fragment"/>"
            </assert>
            
    </rule>
  </pattern>
</schema>
