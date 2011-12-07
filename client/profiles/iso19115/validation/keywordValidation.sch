<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron">

  <!-- 
 Created by Rick Jelliffe to be used to validate ISO 19139 XML metadata record's keywords
 against ISO 19139 keyword code lists that are compliant to ISO 19139 gmx CT_CodelistCatalogue.
 See http://www.isotc211.org/2005/gmx/catalogues.xsd
 
 This product is released under the Creative Commons Attribution 2.5 Australia Licence 
 (http://creativecommons.org/licenses/by/2.5/au/)
  
 This file is available from the following URL:
 http://asdd.ga.gov.au/asdd/profileinfo/keywordValidation.sch
  
 History:
 ======
 2008-11-11 version 1.0
 - Origiinal version created by Rick Jelliffe and blatantly copied by John Hockaday, Geoscience Australia,
    with a few better error messages.
  
  -->

  <title>Example of a Schematron schema for checking that 
    codes are drawn from codelists specified in the document being validated</title>
  <ns prefix="gmx" uri="http://www.isotc211.org/2005/gmx"/>
  <ns prefix="gco" uri="http://www.isotc211.org/2005/gco"/>
  <ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd"/>
  <ns prefix="gml" uri="http://www.opengis.net/gml"/>

  <pattern>
    <rule context="gmd:MD_Keywords/gmd:keyword/gco:CharacterString">

      <let name="URI-reference"
        value="../../gmd:thesaurusName/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:code/gco:CharacterString"/>

      <let name="URI" value="substring-before( $URI-reference, '#')"/>

      <let name="fragment" value="substring-after( $URI-reference, '#')"/>

      <let name="code-list-document" value="document( $URI )"/>

      <let name="dictionary"
        value=" $code-list-document//gmx:CodeListDictionary[@gml:id = $fragment ]"/>

      <assert test=" string-length(normalize-space( $URI )) &gt; 0" role="debug">  
        The codelist file URI should not be empty.
        </assert>

      <assert test="$code-list-document" role="debug">
        Could not find the keyword code list at 
        "<value-of select="$URI"/>".
      </assert>

      <assert test=" string-length(normalize-space( $fragment )) &gt; 0" role="debug">
          The fragment identifier in a codelist URI should not be empty.
          </assert>

      <assert test="$dictionary" role="debug">
        Could not find the keyword dictionary 
        "<value-of select="$fragment"/>" in the keyword code list
        "<value-of select="$URI"/>".
      </assert>

      <assert
        test=" $dictionary/gmx:codeEntry/gmx:CodeDefinition[gml:identifier = current()] or 
              $dictionary/gmx:codeEntry/gmx:CodeDefinition[gml:description = current()]"> 
              Could not find the keyword code "<value-of select="current()"/>" 
              in the keyword code list
              "<value-of select="$URI"/>#<value-of select="$fragment"/>"
            </assert>

    </rule>
  </pattern>
</schema>
