<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:gco="http://www.isotc211.org/2005/gco">
  <xsl:output method="html" version="4.0" encoding="UTF-8"
		indent="yes" />

  <xsl:template match="/">
    <html>
      <head>
        <style type="text/css">
          table { font-family: "Arial",sans-serif ; font-size: 9px; }
          th {text-align: left; vertical-align: text-top; background-color: #dddddd;}
          td {margin-left: 5px;background-color: #ffffff;}
        </style>
      </head>
      <body>
        <div style="font-size: 15px; color: BLACK;">
          <b>
            ISO19115-2005 Metadata Record
          </b>
        </div>
        <xsl:apply-templates />
      </body>
    </html>
  </xsl:template>
  <xsl:template match="node()">
    <xsl:if test="name() != ''">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <th>
            <xsl:apply-templates select="current()" mode="printLabel" />
          </th>
        </tr>
        <xsl:apply-templates select="child::node()" />
        <xsl:value-of select="node()" />
      </table>
    </xsl:if>
    <xsl:if test="name() = ''">
      <xsl:value-of select="node()" />
    </xsl:if>
  </xsl:template>

  <xsl:template match="gco:*">
    <td>
      <xsl:value-of select="current()" />
    </td>
  </xsl:template>

  <xsl:template match="node()[@codeListValue]">
    <td>
      <xsl:value-of select="@codeListValue" />
    </td>
  </xsl:template>

  <xsl:template match="@*">
    <td>
      <xsl:value-of select="name()" /> = <xsl:value-of select="current()" />
    </td>
  </xsl:template>


  <xsl:template match="*" mode="printLabel">
    <xsl:choose>
      <xsl:when test="name() = 'gmd:CI_OnlineResource'">OnLine resource</xsl:when>
      <xsl:when test="name() = 'gmd:aggregateDataSetIdentifier'">Aggregate data set identifier</xsl:when>
      <xsl:when test="name() = 'gmd:aggregateDataSetName'">Aggregate data set name</xsl:when>
      <xsl:when test="name() = 'gmd:aggregationInfo'">Aggregation info</xsl:when>
      <xsl:when test="name() = 'gmd:associationType'">Association type</xsl:when>
      <xsl:when test="name() = 'gmd:axisDimensionProperties'">Axis dimension properties</xsl:when>
      <xsl:when test="name() = 'gmd:characterEncoding'">Character encoding</xsl:when>
      <xsl:when test="name() = 'gmd:codeSpace'">Code space</xsl:when>
      <xsl:when test="name() = 'gmd:composedOf'">Composed of</xsl:when>
      <xsl:when test="name() = 'gmd:Country'">Country</xsl:when>
      <xsl:when test="name() = 'gmd:dataSetURI'">Data Set URI</xsl:when>
      <xsl:when test="name() = 'gmd:describes'">Describes</xsl:when>
      <xsl:when test="name() = 'gmd:distributionOrderProcess'">Distribution order process</xsl:when>
      <xsl:when test="name() = 'gmd:DS_Association'">Association</xsl:when>
      <xsl:when test="name() = 'gmd:DS_DataSet'">Data set</xsl:when>
      <xsl:when test="name() = 'gmd:DS_Initiative'">Initiative</xsl:when>
      <xsl:when test="name() = 'gmd:DS_OtherAggregate'">Other aggregate</xsl:when>
      <xsl:when test="name() = 'gmd:DS_Platform'">Platform</xsl:when>
      <xsl:when test="name() = 'gmd:DS_ProductionSeries'">Production series</xsl:when>
      <xsl:when test="name() = 'gmd:DS_Sensor'">Sensor</xsl:when>
      <xsl:when test="name() = 'gmd:DS_Series'">Series</xsl:when>
      <xsl:when test="name() = 'gmd:DS_StereoMate'">Stereo mate</xsl:when>
      <xsl:when test="name() = 'gmd:EX_SpatialTemporalExtent'">Spatial temporal extent</xsl:when>
      <xsl:when test="name() = 'gmd:featureAttribute'">Feature attribute</xsl:when>
      <xsl:when test="name() = 'gmd:featureType'">Feature type</xsl:when>
      <xsl:when test="name() = 'gmd:georeferencedParameters'">Georeferenced parameters</xsl:when>
      <xsl:when test="name() = 'gmd:has'">Has</xsl:when>
      <xsl:when test="name() = 'gmd:initiativeType'">Initiative type</xsl:when>
      <xsl:when test="name() = 'gmd:LanguageCode'">Language code</xsl:when>
      <xsl:when test="name() = 'gmd:locale'">Locale</xsl:when>
      <xsl:when test="name() = 'gmd:LocalisedCharacterString'">Localised character string</xsl:when>
      <xsl:when test="name() = 'gmd:localisedString'">Localised string</xsl:when>
      <xsl:when test="name() = 'gmd:MD_AggregateInformation'">Aggregate information</xsl:when>
      <xsl:when test="name() = 'gmd:MD_DistributionUnits'">Distribution units</xsl:when>
      <xsl:when test="name() = 'gmd:onlineResource'">Online resource</xsl:when>
      <xsl:when test="name() = 'gmd:partOf'">Part of</xsl:when>
      <xsl:when test="name() = 'gmd:propertyType'">Property type</xsl:when>
      <xsl:when test="name() = 'gmd:PT_Locale'">Locale</xsl:when>
      <xsl:when test="name() = 'gmd:PT_LocaleContainer'">Locale container</xsl:when>
      <xsl:when test="name() = 'gmd:responsibleParty'">Responsible party</xsl:when>
      <xsl:when test="name() = 'gmd:seriesMetadata'">Series metadata</xsl:when>
      <xsl:when test="name() = 'gmd:subset'">Subset</xsl:when>
      <xsl:when test="name() = 'gmd:superset'">Superset</xsl:when>
      <xsl:when test="name() = 'gmd:textGroup'">Text group</xsl:when>
      <xsl:when test="name() = 'gmd:URL'">URL</xsl:when>
      <xsl:when test="name() = 'gmd:verticalCRS'">Vertical CRS</xsl:when>
      <xsl:when test="name() = 'gmd:abstract'">Abstract</xsl:when>
      <xsl:when test="name() = 'gmd:accessConstraints'">Access constraints</xsl:when>
      <xsl:when test="name() = 'gmd:address'">Address</xsl:when>
      <xsl:when test="name() = 'gmd:administrativeArea'">State</xsl:when>
      <xsl:when test="name() = 'gmd:alternateTitle'">Alternate title</xsl:when>
      <xsl:when test="name() = 'gmd:amendmentNumber'">Amendment number</xsl:when>
      <xsl:when test="name() = 'gmd:applicationProfile'">Application profile</xsl:when>
      <xsl:when test="name() = 'gmd:applicationSchemaInfo'">Application schema info</xsl:when>
      <xsl:when test="name() = 'gmd:attributeDescription'">Attribute description</xsl:when>
      <xsl:when test="name() = 'gmd:attributeInstances'">Attribute instances</xsl:when>
      <xsl:when test="name() = 'gmd:attributes'">Attributes</xsl:when>
      <xsl:when test="name() = 'gmd:authority'">Authority</xsl:when>
      <xsl:when test="name() = 'gmd:bitsPerValue'">Bits per value</xsl:when>
      <xsl:when test="name() = 'gmd:cameraCalibrationInformationAvailability'">Camera calibration information availability</xsl:when>
      <xsl:when test="name() = 'gmd:cellGeometry'">Cell geometry</xsl:when>
      <xsl:when test="name() = 'gmd:centerPoint'">Center point</xsl:when>
      <xsl:when test="name() = 'gmd:characterSet'">Character set</xsl:when>
      <xsl:when test="name() = 'gmd:checkPointAvailability'">Check point availability</xsl:when>
      <xsl:when test="name() = 'gmd:checkPointDescription'">Check point description</xsl:when>
      <xsl:when test="name() = 'gmd:CI_Address'">Address</xsl:when>
      <xsl:when test="name() = 'gmd:CI_Citation'">Citation</xsl:when>
      <xsl:when test="name() = 'gmd:CI_Contact'">Contact</xsl:when>
      <xsl:when test="name() = 'gmd:CI_Date'">Date</xsl:when>
      <xsl:when test="name() = 'gmd:CI_DateTypeCode'">Date type code</xsl:when>
      <xsl:when test="name() = 'gmd:CI_OnLineFunctionCode'">OnLine function code</xsl:when>
      <xsl:when test="name() = 'gmd:CI_PresentationFormCode'">Presentation form code</xsl:when>
      <xsl:when test="name() = 'gmd:CI_ResponsibleParty'">Responsible party</xsl:when>
      <xsl:when test="name() = 'gmd:CI_RoleCode'">Role code</xsl:when>
      <xsl:when test="name() = 'gmd:CI_Series'">Series</xsl:when>
      <xsl:when test="name() = 'gmd:CI_Telephone'">Telephone</xsl:when>
      <xsl:when test="name() = 'gmd:citation'">Citation</xsl:when>
      <xsl:when test="name() = 'gmd:citedResponsibleParty'">Cited responsible party</xsl:when>
      <xsl:when test="name() = 'gmd:city'">City</xsl:when>
      <xsl:when test="name() = 'gmd:classification'">Classification</xsl:when>
      <xsl:when test="name() = 'gmd:classificationSystem'">Classification system</xsl:when>
      <xsl:when test="name() = 'gmd:cloudCoverPercentage'">Cloud cover percentage</xsl:when>
      <xsl:when test="name() = 'gmd:code'">Code</xsl:when>
      <xsl:when test="name() = 'gmd:collectiveTitle'">Collective title</xsl:when>
      <xsl:when test="name() = 'gmd:complianceCode'">Compliance code</xsl:when>
      <xsl:when test="name() = 'gmd:compressionGenerationQuantity'">Compression generation quantity</xsl:when>
      <xsl:when test="name() = 'gmd:condition'">Condition</xsl:when>
      <xsl:when test="name() = 'gmd:constraintLanguage'">Constraint language</xsl:when>
      <xsl:when test="name() = 'gmd:contact'">Contact</xsl:when>
      <xsl:when test="name() = 'gmd:contactInfo'">Contact info</xsl:when>
      <xsl:when test="name() = 'gmd:contactInstructions'">Contact instructions</xsl:when>
      <xsl:when test="name() = 'gmd:contentInfo'">Content info</xsl:when>
      <xsl:when test="name() = 'gmd:contentType'">Content type</xsl:when>
      <xsl:when test="name() = 'gmd:controlPointAvailability'">Control point availability</xsl:when>
      <xsl:when test="name() = 'gmd:cornerPoints'">Corner points</xsl:when>
      <xsl:when test="name() = 'gmd:country'">Country</xsl:when>
      <xsl:when test="name() = 'gmd:credit'">Credit</xsl:when>
      <xsl:when test="name() = 'gmd:dataQualityInfo'">Data quality info</xsl:when>
      <xsl:when test="name() = 'gmd:dataset'">Dataset</xsl:when>
      <xsl:when test="name() = 'gmd:dataType'">Data type</xsl:when>
      <xsl:when test="name() = 'gmd:date'">Date</xsl:when>
      <xsl:when test="name() = 'gmd:dateOfNextUpdate'">Date of next update</xsl:when>
      <xsl:when test="name() = 'gmd:dateStamp'">Date stamp</xsl:when>
      <xsl:when test="name() = 'gmd:dateTime'">Date and Time</xsl:when>
      <xsl:when test="name() = 'gmd:dateType'">Date type</xsl:when>
      <xsl:when test="name() = 'gmd:definition'">Definition</xsl:when>
      <xsl:when test="name() = 'gmd:deliveryPoint'">Delivery point</xsl:when>
      <xsl:when test="name() = 'gmd:denominator'">Denominator</xsl:when>
      <xsl:when test="name() = 'gmd:density'">Density</xsl:when>
      <xsl:when test="name() = 'gmd:densityUnits'">Density units</xsl:when>
      <xsl:when test="name() = 'gmd:description'">Description</xsl:when>
      <xsl:when test="name() = 'gmd:descriptiveKeywords'">Descriptive keywords</xsl:when>
      <xsl:when test="name() = 'gmd:descriptor'">Descriptor</xsl:when>
      <xsl:when test="name() = 'gmd:dimension'">Dimension</xsl:when>
      <xsl:when test="name() = 'gmd:dimensionName'">Dimension name</xsl:when>
      <xsl:when test="name() = 'gmd:dimensionSize'">Dimension size</xsl:when>
      <xsl:when test="name() = 'gmd:distance'">Distance</xsl:when>
      <xsl:when test="name() = 'gmd:distributionFormat'">Distribution format</xsl:when>
      <xsl:when test="name() = 'gmd:distributionInfo'">Distribution and On-line Resource(s)</xsl:when>
      <xsl:when test="name() = 'gmd:distributor'">Distributor</xsl:when>
      <xsl:when test="name() = 'gmd:distributorContact'">Distributor contact</xsl:when>
      <xsl:when test="name() = 'gmd:distributorFormat'">Distributor format</xsl:when>
      <xsl:when test="name() = 'gmd:distributorTransferOptions'">Distributor transfer options</xsl:when>
      <xsl:when test="name() = 'gmd:domainCode'">Domain code</xsl:when>
      <xsl:when test="name() = 'gmd:domainOfValidity'">Domain of validity</xsl:when>
      <xsl:when test="name() = 'gmd:domainValue'">Domain value</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_AbsoluteExternalPositionalAccuracy'">Absolute external positional accuracy</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_AccuracyOfATimeMeasurement'">Accuracy of time measurement</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_CompletenessCommission'">Completeness commission</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_CompletenessOmission'">Completeness omission</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_ConceptualConsistency'">Conceptual consistency</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_ConformanceResult'">Conformance result</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_DataQuality'">Data quality</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_DomainConsistency'">Domain consistency</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_EvaluationMethodTypeCode'">Evaluation method type code</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_FormatConsistency'">Format consistency</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_GriddedDataPositionalAccuracy'">Gridded data positional accuracy</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_NonQuantitativeAttributeAccuracy'">Non quantitative attribute accuracy</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_QuantitativeAttributeAccuracy'">Quantitative attribute accuracy</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_QuantitativeResult'">Quantitative result</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_RelativeInternalPositionalAccuracy'">Relative internal positional accuracy</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_Scope'">Scope</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_TemporalConsistency'">Temporal consistency</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_TemporalValidity'">Temporal validity</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_ThematicClassificationCorrectness'">Thematic classification correctness</xsl:when>
      <xsl:when test="name() = 'gmd:DQ_TopologicalConsistency'">Topological consistency</xsl:when>
      <xsl:when test="name() = 'gmd:DS_AssociationTypeCode'">Association type code</xsl:when>
      <xsl:when test="name() = 'gmd:DS_InitiativeTypeCode'">Initiative type code</xsl:when>
      <xsl:when test="name() = 'gmd:eastBoundLongitude'">East bound longitude</xsl:when>
      <xsl:when test="name() = 'gmd:edition'">Edition</xsl:when>
      <xsl:when test="name() = 'gmd:editionDate'">Edition date</xsl:when>
      <xsl:when test="name() = 'gmd:electronicMailAddress'">Email</xsl:when>
      <xsl:when test="name() = 'gmd:environmentDescription'">Environment description</xsl:when>
      <xsl:when test="name() = 'gmd:equivalentScale'">Equivalent scale</xsl:when>
      <xsl:when test="name() = 'gmd:errorStatistic'">Error statistic</xsl:when>
      <xsl:when test="name() = 'gmd:evaluationMethodDescription'">Evaluation method description</xsl:when>
      <xsl:when test="name() = 'gmd:evaluationMethodType'">Evaluation method type code</xsl:when>
      <xsl:when test="name() = 'gmd:evaluationProcedure'">Evaluation procedure</xsl:when>
      <xsl:when test="name() = 'gmd:EX_BoundingPolygon'">EX_BoundingPolygon</xsl:when>
      <xsl:when test="name() = 'gmd:EX_Extent'">Extent</xsl:when>
      <xsl:when test="name() = 'gmd:EX_GeographicBoundingBox'">Geographic bounding box</xsl:when>
      <xsl:when test="name() = 'gmd:EX_GeographicDescription'">Geographic description</xsl:when>
      <xsl:when test="name() = 'gmd:EX_TemporalExtent'">Temporal Extent</xsl:when>
      <xsl:when test="name() = 'gmd:EX_VerticalExtent'">Vertical extent</xsl:when>
      <xsl:when test="name() = 'gmd:explanation'">Explanation</xsl:when>
      <xsl:when test="name() = 'gmd:extendedElementInformation'">Extended element information</xsl:when>
      <xsl:when test="name() = 'gmd:extensionOnLineResource'">Extension Online resource</xsl:when>
      <xsl:when test="name() = 'gmd:extent'">Extent</xsl:when>
      <xsl:when test="name() = 'gmd:extentTypeCode'">Extent type code</xsl:when>
      <xsl:when test="name() = 'gmd:facsimile'">Fax</xsl:when>
      <xsl:when test="name() = 'gmd:featureCatalogueCitation'">Feature catalogue citation</xsl:when>
      <xsl:when test="name() = 'gmd:featureInstances'">Feature instances</xsl:when>
      <xsl:when test="name() = 'gmd:features'">Features</xsl:when>
      <xsl:when test="name() = 'gmd:featureTypes'">Feature types</xsl:when>
      <xsl:when test="name() = 'gmd:fees'">Fees</xsl:when>
      <xsl:when test="name() = 'gmd:fileDecompressionTechnique'">File decompression technique</xsl:when>
      <xsl:when test="name() = 'gmd:fileDescription'">File description</xsl:when>
      <xsl:when test="name() = 'gmd:fileIdentifier'">File identifier</xsl:when>
      <xsl:when test="name() = 'gmd:fileName'">File name</xsl:when>
      <xsl:when test="name() = 'gmd:fileType'">File type</xsl:when>
      <xsl:when test="name() = 'gmd:filmDistortionInformationAvailability'">Film distortion information availability</xsl:when>
      <xsl:when test="name() = 'gmd:formatDistributor'">Format distributor</xsl:when>
      <xsl:when test="name() = 'gmd:function'">Function</xsl:when>
      <xsl:when test="name() = 'gmd:geographicElement'">Geographic element</xsl:when>
      <xsl:when test="name() = 'gmd:geographicIdentifier'">Geographic identifier</xsl:when>
      <xsl:when test="name() = 'gmd:geometricObjectCount'">Geometric object count</xsl:when>
      <xsl:when test="name() = 'gmd:geometricObjects'">Geometric objects</xsl:when>
      <xsl:when test="name() = 'gmd:geometricObjectType'">Geometric object type</xsl:when>
      <xsl:when test="name() = 'gmd:graphicOverview'">Graphic overview</xsl:when>
      <xsl:when test="name() = 'gmd:graphicsFile'">Graphics file</xsl:when>
      <xsl:when test="name() = 'gmd:handlingDescription'">Handling description</xsl:when>
      <xsl:when test="name() = 'gmd:hierarchyLevel'">Hierarchy level</xsl:when>
      <xsl:when test="name() = 'gmd:hierarchyLevelName'">Hierarchy level name</xsl:when>
      <xsl:when test="name() = 'gmd:hoursOfService'">Hours of service</xsl:when>
      <xsl:when test="name() = 'gmd:identificationInfo'">Identification info</xsl:when>
      <xsl:when test="name() = 'gmd:identifier'">Identifier</xsl:when>
      <xsl:when test="name() = 'gmd:illuminationAzimuthAngle'">Illumination azimuth angle</xsl:when>
      <xsl:when test="name() = 'gmd:illuminationElevationAngle'">Illumination elevation angle</xsl:when>
      <xsl:when test="name() = 'gmd:imageQualityCode'">Image quality code</xsl:when>
      <xsl:when test="name() = 'gmd:imagingCondition'">Imaging condition</xsl:when>
      <xsl:when test="name() = 'gmd:includedWithDataset'">Included with dataset</xsl:when>
      <xsl:when test="name() = 'gmd:individualName'">Individual name</xsl:when>
      <xsl:when test="name() = 'gmd:ISBN'">ISBN</xsl:when>
      <xsl:when test="name() = 'gmd:ISSN'">ISSN</xsl:when>
      <xsl:when test="name() = 'gmd:issueIdentification'">Issue identification</xsl:when>
      <xsl:when test="name() = 'gmd:keyword'">Subject</xsl:when>
      <xsl:when test="name() = 'gmd:language'">Language</xsl:when>
      <xsl:when test="name() = 'gmd:languageCode'">Language code</xsl:when>
      <xsl:when test="name() = 'gmd:lensDistortionInformationAvailability'">Lens distortion information availability</xsl:when>
      <xsl:when test="name() = 'gmd:level'">Hierarchy level</xsl:when>
      <xsl:when test="name() = 'gmd:levelDescription'">Level description</xsl:when>
      <xsl:when test="name() = 'gmd:LI_Lineage'">Lineage</xsl:when>
      <xsl:when test="name() = 'gmd:LI_ProcessStep'">Process step</xsl:when>
      <xsl:when test="name() = 'gmd:LI_Source'">Source</xsl:when>
      <xsl:when test="name() = 'gmd:lineage'">Lineage</xsl:when>
      <xsl:when test="name() = 'gmd:linkage'">Linkage</xsl:when>
      <xsl:when test="name() = 'gmd:maintenanceAndUpdateFrequency'">Maintenance and update frequency</xsl:when>
      <xsl:when test="name() = 'gmd:maintenanceNote'">Maintenance note</xsl:when>
      <xsl:when test="name() = 'gmd:maximumOccurrence'">Maximum occurrence</xsl:when>
      <xsl:when test="name() = 'gmd:maximumValue'">Maximum value</xsl:when>
      <xsl:when test="name() = 'gmd:maxValue'">Maximum value</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ApplicationSchemaInformation'">Application schema info</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Band'">Band</xsl:when>
      <xsl:when test="name() = 'gmd:MD_BrowseGraphic'">MD_BrowseGraphic</xsl:when>
      <xsl:when test="name() = 'gmd:MD_CellGeometryCode'">Cell geometry code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_CharacterSetCode'">Character set code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ClassificationCode'">Classification code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Constraints'">Constraints</xsl:when>
      <xsl:when test="name() = 'gmd:MD_CoverageContentTypeCode'">Content type code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_CoverageDescription'">Coverage description</xsl:when>
      <xsl:when test="name() = 'gmd:MD_DataIdentification'">Data identification</xsl:when>
      <xsl:when test="name() = 'gmd:MD_DatatypeCode'">Data type code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_DigitalTransferOptions'">Digital transfer options</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Dimension'">Dimension</xsl:when>
      <xsl:when test="name() = 'gmd:MD_DimensionNameTypeCode'">Dimension name type code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Distribution'">Distribution</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Distributor'">Distributor</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ExtendedElementInformation'">Extended elemen information</xsl:when>
      <xsl:when test="name() = 'gmd:MD_FeatureCatalogueDescription'">Feature catalogue description</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Format'">Format</xsl:when>
      <xsl:when test="name() = 'gmd:MD_GeometricObjects'">Geometric objects</xsl:when>
      <xsl:when test="name() = 'gmd:MD_GeometricObjectTypeCode'">Geometric object type code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Georectified'">Georectified</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Georeferenceable'">Georeferenceable</xsl:when>
      <xsl:when test="name() = 'gmd:MD_GridSpatialRepresentation'">Grid spatial representation</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Identifier'">Identifier</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ImageDescription'">Image description</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ImagingConditionCode'">Imaging condition code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Keywords'">Keywords</xsl:when>
      <xsl:when test="name() = 'gmd:MD_KeywordTypeCode'">Keyword type code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_LegalConstraints'">Legal constraints</xsl:when>
      <xsl:when test="name() = 'gmd:MD_MaintenanceFrequencyCode'">Maintenance frequency code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_MaintenanceInformation'">Maintenance information</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Medium'">Medium</xsl:when>
      <xsl:when test="name() = 'gmd:MD_MediumFormatCode'">Medium format</xsl:when>
      <xsl:when test="name() = 'gmd:MD_MediumNameCode'">Medium name code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Metadata'">Metadata</xsl:when>
      <xsl:when test="name() = 'gmd:MD_MetadataExtensionInformation'">Metadata extension info</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ObligationCode'">Obligation code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_PixelOrientationCode'">Pixel orientation code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_PortrayalCatalogueReference'">Portrayal catalogue reference</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ProgressCode'">Progress code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_RangeDimension'">Range dimension</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ReferenceSystem'">Reference system</xsl:when>
      <xsl:when test="name() = 'gmd:MD_RepresentativeFraction'">Representative fraction</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Resolution'">Resolution</xsl:when>
      <xsl:when test="name() = 'gmd:MD_RestrictionCode'">Restriction code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ScopeCode'">Scope code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ScopeDescription'">Scope description</xsl:when>
      <xsl:when test="name() = 'gmd:MD_SecurityConstraints'">Security constraints</xsl:when>
      <xsl:when test="name() = 'gmd:MD_ServiceIdentification'">Service identification</xsl:when>
      <xsl:when test="name() = 'gmd:MD_SpatialRepresentationTypeCode'">Spatial representation type code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_StandardOrderProcess'">Standard order process</xsl:when>
      <xsl:when test="name() = 'gmd:MD_TopicCategoryCode'">Topic category code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_TopologyLevelCode'">Topology level code</xsl:when>
      <xsl:when test="name() = 'gmd:MD_Usage'">Usage</xsl:when>
      <xsl:when test="name() = 'gmd:MD_VectorSpatialRepresentation'">Vector spatial representation</xsl:when>
      <xsl:when test="name() = 'gmd:measureDescription'">Measure description</xsl:when>
      <xsl:when test="name() = 'gmd:measureIdentification'">Measure identification</xsl:when>
      <xsl:when test="name() = 'gmd:mediumFormat'">Medium format</xsl:when>
      <xsl:when test="name() = 'gmd:mediumNote'">Medium note</xsl:when>
      <xsl:when test="name() = 'gmd:metadataConstraints'">Metadata constraints</xsl:when>
      <xsl:when test="name() = 'gmd:metadataExtensionInfo'">Metadata extension info</xsl:when>
      <xsl:when test="name() = 'gmd:metadataMaintenance'">Metadata maintenance</xsl:when>
      <xsl:when test="name() = 'gmd:metadataStandardName'">Metadata standard name</xsl:when>
      <xsl:when test="name() = 'gmd:metadataStandardVersion'">Metadata standard version</xsl:when>
      <xsl:when test="name() = 'gmd:minimumValue'">Minimum value</xsl:when>
      <xsl:when test="name() = 'gmd:minValue'">Minimum value</xsl:when>
      <xsl:when test="name() = 'gmd:name'">Name</xsl:when>
      <xsl:when test="name() = 'gmd:nameOfMeasure'">Name of measure</xsl:when>
      <xsl:when test="name() = 'gmd:northBoundLatitude'">North bound latitude</xsl:when>
      <xsl:when test="name() = 'gmd:numberOfDimensions'">Number of dimensions</xsl:when>
      <xsl:when test="name() = 'gmd:obligation'">Obligation</xsl:when>
      <xsl:when test="name() = 'gmd:offLine'">Offline</xsl:when>
      <xsl:when test="name() = 'gmd:offset'">Offset</xsl:when>
      <xsl:when test="name() = 'gmd:onLine'">OnLine resource</xsl:when>
      <xsl:when test="name() = 'gmd:orderingInstructions'">Ordering instructions</xsl:when>
      <xsl:when test="name() = 'gmd:organisationName'">Organisation name</xsl:when>
      <xsl:when test="name() = 'gmd:orientationParameterAvailability'">Orientation parameter availability</xsl:when>
      <xsl:when test="name() = 'gmd:orientationParameterDescription'">Orientation parameter description</xsl:when>
      <xsl:when test="name() = 'gmd:other'">Other</xsl:when>
      <xsl:when test="name() = 'gmd:otherCitationDetails'">Other citation details</xsl:when>
      <xsl:when test="name() = 'gmd:otherConstraints'">Other constraints</xsl:when>
      <xsl:when test="name() = 'gmd:page'">Page</xsl:when>
      <xsl:when test="name() = 'gmd:parameterCitation'">Parameter citation</xsl:when>
      <xsl:when test="name() = 'gmd:parentEntity'">Parent entity</xsl:when>
      <xsl:when test="name() = 'gmd:parentIdentifier'">Parent identifier</xsl:when>
      <xsl:when test="name() = 'gmd:pass'">Pass</xsl:when>
      <xsl:when test="name() = 'gmd:peakResponse'">Peak response</xsl:when>
      <xsl:when test="name() = 'gmd:phone'">Phone</xsl:when>
      <xsl:when test="name() = 'gmd:plannedAvailableDateTime'">Planned available datetime</xsl:when>
      <xsl:when test="name() = 'gmd:pointInPixel'">PointIn pixel</xsl:when>
      <xsl:when test="name() = 'gmd:pointOfContact'">Point of contact</xsl:when>
      <xsl:when test="name() = 'gmd:polygon'">Polygon</xsl:when>
      <xsl:when test="name() = 'gmd:portrayalCatalogueCitation'">Portrayal catalogue citation</xsl:when>
      <xsl:when test="name() = 'gmd:portrayalCatalogueInfo'">Portrayal catalogue info</xsl:when>
      <xsl:when test="name() = 'gmd:positionName'">Position name</xsl:when>
      <xsl:when test="name() = 'gmd:postalCode'">Postcode</xsl:when>
      <xsl:when test="name() = 'gmd:presentationForm'">Presentation form</xsl:when>
      <xsl:when test="name() = 'gmd:processingLevelCode'">Processing level code</xsl:when>
      <xsl:when test="name() = 'gmd:processor'">Processor</xsl:when>
      <xsl:when test="name() = 'gmd:processStep'">Process step</xsl:when>
      <xsl:when test="name() = 'gmd:protocol'">Protocol</xsl:when>
      <xsl:when test="name() = 'gmd:PT_FreeText'">Free text</xsl:when>
      <xsl:when test="name() = 'gmd:purpose'">Purpose</xsl:when>
      <xsl:when test="name() = 'gmd:radiometricCalibrationDataAvailability'">Radiometric calibration data availability</xsl:when>
      <xsl:when test="name() = 'gmd:rationale'">Rationale</xsl:when>
      <xsl:when test="name() = 'gmd:referenceSystemIdentifier'">Reference system identifier</xsl:when>
      <xsl:when test="name() = 'gmd:referenceSystemInfo'">Reference system info</xsl:when>
      <xsl:when test="name() = 'gmd:report'">Report</xsl:when>
      <xsl:when test="name() = 'gmd:resolution'">Resolution</xsl:when>
      <xsl:when test="name() = 'gmd:resourceConstraints'">Resource constraints</xsl:when>
      <xsl:when test="name() = 'gmd:resourceFormat'">Resource format</xsl:when>
      <xsl:when test="name() = 'gmd:resourceMaintenance'">Resource maintenance</xsl:when>
      <xsl:when test="name() = 'gmd:resourceSpecificUsage'">Resource specific usage</xsl:when>
      <xsl:when test="name() = 'gmd:result'">Result</xsl:when>
      <xsl:when test="name() = 'gmd:role'">Role</xsl:when>
      <xsl:when test="name() = 'gmd:RS_Identifier'">RS identifier</xsl:when>
      <xsl:when test="name() = 'gmd:rule'">Rule</xsl:when>
      <xsl:when test="name() = 'gmd:scaleDenominator'">Scale</xsl:when>
      <xsl:when test="name() = 'gmd:scaleFactor'">Scale factor</xsl:when>
      <xsl:when test="name() = 'gmd:schemaAscii'">Schema ASCII</xsl:when>
      <xsl:when test="name() = 'gmd:schemaLanguage'">Schema language</xsl:when>
      <xsl:when test="name() = 'gmd:scope'">Scope</xsl:when>
      <xsl:when test="name() = 'gmd:sequenceIdentifier'">Sequence identifier</xsl:when>
      <xsl:when test="name() = 'gmd:series'">Series</xsl:when>
      <xsl:when test="name() = 'gmd:shortName'">Short name</xsl:when>
      <xsl:when test="name() = 'gmd:softwareDevelopmentFile'">Software development file</xsl:when>
      <xsl:when test="name() = 'gmd:softwareDevelopmentFileFormat'">Software development file format</xsl:when>
      <xsl:when test="name() = 'gmd:source'">Source</xsl:when>
      <xsl:when test="name() = 'gmd:sourceCitation'">Source citation</xsl:when>
      <xsl:when test="name() = 'gmd:sourceExtent'">Source extent</xsl:when>
      <xsl:when test="name() = 'gmd:sourceReferenceSystem'">Source reference system</xsl:when>
      <xsl:when test="name() = 'gmd:sourceStep'">Source step</xsl:when>
      <xsl:when test="name() = 'gmd:southBoundLatitude'">South bound latitude</xsl:when>
      <xsl:when test="name() = 'gmd:spatialExtent'">Spatial extent</xsl:when>
      <xsl:when test="name() = 'gmd:spatialRepresentationInfo'">Spatial representation info</xsl:when>
      <xsl:when test="name() = 'gmd:spatialRepresentationType'">Spatial representation type</xsl:when>
      <xsl:when test="name() = 'gmd:spatialResolution'">Spatial resolution</xsl:when>
      <xsl:when test="name() = 'gmd:specification'">Specification</xsl:when>
      <xsl:when test="name() = 'gmd:specificUsage'">Specific usage</xsl:when>
      <xsl:when test="name() = 'gmd:statement'">Statement</xsl:when>
      <xsl:when test="name() = 'gmd:status'">Status</xsl:when>
      <xsl:when test="name() = 'gmd:supplementalInformation'">Supplemental Information</xsl:when>
      <xsl:when test="name() = 'gmd:temporalElement'">Temporal element</xsl:when>
      <xsl:when test="name() = 'gmd:thesaurusName'">Thesaurus name</xsl:when>
      <xsl:when test="name() = 'gmd:title'">Title</xsl:when>
      <xsl:when test="name() = 'gmd:toneGradation'">Tone gradation</xsl:when>
      <xsl:when test="name() = 'gmd:topicCategory'">Topic category</xsl:when>
      <xsl:when test="name() = 'gmd:topologyLevel'">Topology level</xsl:when>
      <xsl:when test="name() = 'gmd:transferOptions'">Transfer options</xsl:when>
      <xsl:when test="name() = 'gmd:transferSize'">Transfer size</xsl:when>
      <xsl:when test="name() = 'gmd:transformationDimensionDescription'">Transformation dimension description</xsl:when>
      <xsl:when test="name() = 'gmd:transformationDimensionMapping'">Transformation dimension mapping</xsl:when>
      <xsl:when test="name() = 'gmd:transformationParameterAvailability'">Transformation parameter availability</xsl:when>
      <xsl:when test="name() = 'gmd:triangulationIndicator'">Triangulation indicator</xsl:when>
      <xsl:when test="name() = 'gmd:turnaround'">Turnaround</xsl:when>
      <xsl:when test="name() = 'gmd:type'">Type</xsl:when>
      <xsl:when test="name() = 'gmd:units'">Value unit</xsl:when>
      <xsl:when test="name() = 'gmd:unitsOfDistribution'">Units of distribution</xsl:when>
      <xsl:when test="name() = 'gmd:updateScope'">Update scope</xsl:when>
      <xsl:when test="name() = 'gmd:updateScopeDescription'">Update scope description</xsl:when>
      <xsl:when test="name() = 'gmd:usageDateTime'">Usage datetime</xsl:when>
      <xsl:when test="name() = 'gmd:useConstraints'">Use constraints</xsl:when>
      <xsl:when test="name() = 'gmd:useLimitation'">Use limitation</xsl:when>
      <xsl:when test="name() = 'gmd:userContactInfo'">User contact info</xsl:when>
      <xsl:when test="name() = 'gmd:userDefinedMaintenanceFrequency'">User defined maintenance frequency</xsl:when>
      <xsl:when test="name() = 'gmd:userDeterminedLimitations'">User determined limitations</xsl:when>
      <xsl:when test="name() = 'gmd:userNote'">User note</xsl:when>
      <xsl:when test="name() = 'gmd:value'">Value</xsl:when>
      <xsl:when test="name() = 'gmd:valueType'">Value type</xsl:when>
      <xsl:when test="name() = 'gmd:valueUnit'">Value unit</xsl:when>
      <xsl:when test="name() = 'gmd:version'">Version</xsl:when>
      <xsl:when test="name() = 'gmd:verticalElement'">Vertical element</xsl:when>
      <xsl:when test="name() = 'gmd:voice'">Voice</xsl:when>
      <xsl:when test="name() = 'gmd:volumes'">Volumes</xsl:when>
      <xsl:when test="name() = 'gmd:westBoundLongitude'">West bound longitude</xsl:when>
      <xsl:when test="name() = 'gco:Date'">Date</xsl:when>
      <xsl:when test="name() = 'gco:DateTime'">Date and time</xsl:when>
      <xsl:when test="name() = 'gco:Measure'">Measure</xsl:when>
      <xsl:when test="name() = 'gts:TM_PeriodDuration'">Period duration</xsl:when>
      <xsl:when test="name() = 'gml:beginPosition'">Begin date</xsl:when>
      <xsl:when test="name() = 'gml:endPosition'">End date</xsl:when>
      <xsl:when test="name() = 'gml:relatedTime'">Related time</xsl:when>
      <xsl:when test="name() = 'gml:coordinates'">Coordinates</xsl:when>
      <xsl:when test="name() = 'gml:Polygon'">Polygon</xsl:when>
      <xsl:when test="name() = 'gml:Point'">Point</xsl:when>
      <xsl:when test="name() = 'gml:LineString'">Line</xsl:when>
      <xsl:when test="name() = 'gml:LinearRing'">Linear ring</xsl:when>
      <xsl:when test="name() = 'gml:exterior'">Outer boundary</xsl:when>
      <xsl:when test="name() = 'gml:interior'">Inner boundary</xsl:when>
      <xsl:when test="name() = 'gml:begin'">Begin</xsl:when>
      <xsl:when test="name() = 'gml:end'">End</xsl:when>
      <xsl:when test="name() = 'gml:duration'">Duration</xsl:when>
      <xsl:when test="name() = 'gml:timeInterval'">Time interval</xsl:when>
      <xsl:when test="name() = 'gml:TimeInstant'">Time instant</xsl:when>
      <xsl:when test="name() = 'gml:TimePosition'">Time position</xsl:when>
      <xsl:when test="name() = 'gml:TimePeriod'">Time period</xsl:when>
      <xsl:when test="name() = 'gml:TimeNode'">Time node</xsl:when>
      <xsl:when test="name() = 'gml:TimeEdge'">Time edge</xsl:when>
      <xsl:when test="name() = 'gml:ProjectedCRS'">Projected CRS</xsl:when>
      <xsl:when test="name() = 'gml:id'">Identifier</xsl:when>
      <xsl:when test="name() = 'srsName'">Spatial Reference System Name</xsl:when>
      <xsl:when test="name() = 'uuidref'">UUID</xsl:when>
      <xsl:when test="name() = 'srv:SV_ServiceIdentification'">Service Identification (19119)</xsl:when>
      <xsl:when test="name() = 'srv:serviceType'">Service Type</xsl:when>
      <xsl:when test="name() = 'srv:serviceTypeVersion'">Service Version</xsl:when>
      <xsl:when test="name() = 'srv:accessProperties'">Access Properties</xsl:when>
      <xsl:when test="name() = 'srv:restrictions'">Restrictions</xsl:when>
      <xsl:when test="name() = 'srv:containsOperations'">Contains Operations</xsl:when>
      <xsl:when test="name() = 'srv:operatesOn'">Operates On</xsl:when>
      <xsl:when test="name() = 'srv:operationName'">Operation Name</xsl:when>
      <xsl:when test="name() = 'srv:SV_OperationMetadata'">Operation</xsl:when>
      <xsl:when test="name() = 'srv:DCP'">Distributed Computing Platforms</xsl:when>
      <xsl:when test="name() = 'srv:DCPList'">Distributed Computing Platforms List</xsl:when>
      <xsl:when test="name() = 'srv:operationDescription'">Operation Description</xsl:when>
      <xsl:when test="name() = 'srv:invocationName'">Invocation Name</xsl:when>
      <xsl:when test="name() = 'srv:parameters'">Parameters</xsl:when>
      <xsl:when test="name() = 'srv:connectPoint'">Connect Point</xsl:when>
      <xsl:when test="name() = 'srv:dependsOn'">Depends On</xsl:when>
      <xsl:when test="name() = 'srv:providerName'">Provider Name</xsl:when>
      <xsl:when test="name() = 'srv:serviceContact'">Service Contact</xsl:when>
      <xsl:when test="name() = 'srv:name'">Name</xsl:when>
      <xsl:when test="name() = 'srv:description'">Description</xsl:when>
      <xsl:when test="name() = 'srv:optionality'">Optionality</xsl:when>
      <xsl:when test="name() = 'srv:repeatability'">Repeatability</xsl:when>
      <xsl:when test="name() = 'srv:coupledResource'">Coupled Resource</xsl:when>
      <xsl:when test="name() = 'srv:SV_CoupledResource'">Coupled Resource</xsl:when>
      <xsl:when test="name() = 'srv:identifier'">Identifier</xsl:when>
      <xsl:when test="name() = 'srv:couplingType'">Coupling Type</xsl:when>
      <xsl:when test="name() = 'srv:SV_CouplingType'">Coupling Type</xsl:when>
      <xsl:when test="name() = 'srv:extent'">Extent</xsl:when>
      <xsl:when test="name() = 'srv:keywords'">Keywords</xsl:when>
      <xsl:when test="name() = 'srv:SV_Parameter'">Parameter</xsl:when>
      <xsl:when test="name() = 'srv:direction'">Direction</xsl:when>
      <xsl:when test="name() = 'srv:valueType'">Value type</xsl:when>
      <xsl:when test="name() = 'srv:SV_ParameterDirection'">Parameter direction</xsl:when>
      <xsl:when test="name() = 'years'">Years</xsl:when>
      <xsl:when test="name() = 'zone'">Zone</xsl:when>
      <xsl:when test="name() = 'InDirectReferenceSystem'">Indirect projection system (for non geographical resources)</xsl:when>
      <xsl:when test="name() = 'DirectReferenceSystem'">Direct projection system (for geographical resources)</xsl:when>
      <xsl:when test="name() = 'code'">System code</xsl:when>
      <xsl:when test="name() = 'label'">Content</xsl:when>
      <xsl:when test="name() = 'codeSpace'">Reference Authority</xsl:when>
      <xsl:when test="name() = 'gco:aName'">Name</xsl:when>
      <xsl:when test="name() = 'gco:attributeType'">Attribute type</xsl:when>
      <xsl:when test="name() = 'gco:TypeName'">Type name</xsl:when>
      <xsl:when test="name() = 'uom'">Units of measure</xsl:when>
      <xsl:otherwise >
        <xsl:value-of select="name()" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
