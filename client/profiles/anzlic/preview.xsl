<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gmx="http://www.isotc211.org/2005/gmx" xmlns:gmi="http://www.isotc211.org/2005/gmi" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:srv="http://www.isotc211.org/2005/srv" xmlns:gfc="http://www.isotc211.org/2005/gfc" xmlns:gss="http://www.isotc211.org/2005/gss" xmlns:gml="http://www.opengis.net/gml" xmlns:gsr="http://www.isotc211.org/2005/gsr" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.0">
  <xsl:output method="html" version="4.0" encoding="UTF-8" indent="yes" />
  <xsl:template match="/">
    <html>
      <head>
        <style type="text/css">body {color: #000000;background-color: #ffffff;}table {border-width: 1px;border-style: solid;width: 100%;}th {color: #000000;background-color: #eeeeee; padding-left: 5px;}</style>
      </head>
      <body>
        <table>
          <tr>
            <th>Resource Type</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue" />
            </td>
          </tr>
          <tr>
            <th>Resource Type Description</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:hierarchyLevelName/gco:CharacterString" />
            </td>
          </tr>
          <tr>
            <th>Parent metadata record</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:parentIdentifier/gco:CharacterString" />
            </td>
          </tr>
          <tr>
            <th>Title:</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString" />
            </td>
          </tr>
          <tr>
            <th>Other Citation Details</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:otherCitationDetails/gco:CharacterString" />
            </td>
          </tr>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date">
            <tr>
              <th colspan="2">Key date</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <xsl:if test="gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode/@codeListValue = 'creation'">
                    <tr>
                      <th colspan="2">Date when the resource was created</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <xsl:if test="gmd:CI_Date/gmd:date/gco:Date">
                            <tr>
                              <th colspan="2">Date</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>When was the resource created?</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_Date/gmd:date/gco:Date" />
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:CI_Date/gmd:date/gco:DateTime">
                            <tr>
                              <th colspan="2">Date Time</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>When was the resource created?</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_Date/gmd:date/gco:DateTime" />
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode/@codeListValue = 'publication'">
                    <tr>
                      <th colspan="2">Date when the resouce was published</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <xsl:if test="gmd:CI_Date/gmd:date/gco:Date">
                            <tr>
                              <th colspan="2">Date</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>When was the resource published?</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_Date/gmd:date/gco:Date" />
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:CI_Date/gmd:date/gco:DateTime">
                            <tr>
                              <th colspan="2">Date Time</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>When was the resource published?</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_Date/gmd:date/gco:DateTime" />
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode/@codeListValue = 'revision'">
                    <tr>
                      <th colspan="2">Date when the resource was last updated</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <xsl:if test="gmd:CI_Date/gmd:date/gco:Date">
                            <tr>
                              <th colspan="2">Date</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>When was the resource last updated?</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_Date/gmd:date/gco:Date" />
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:CI_Date/gmd:date/gco:DateTime">
                            <tr>
                              <th colspan="2">Date Time</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>When was the resource last updated?</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_Date/gmd:date/gco:DateTime" />
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                </table>
              </td>
            </tr>
          </xsl:for-each>
          <tr>
            <th>Metadata Language</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:language/gco:CharacterString" />
            </td>
          </tr>
          <tr>
            <th>Abstract</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract/gco:CharacterString" />
            </td>
          </tr>
          <tr>
            <th>Purpose</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:purpose/gco:CharacterString" />
            </td>
          </tr>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact">
            <tr>
              <th colspan="2">Resource Contact</th>
            </tr>
            <tr>
              <td colspan="2">
                <table />
              </td>
            </tr>
          </xsl:for-each>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceFormat">
            <tr>
              <th colspan="2">Resource Format</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <tr>
                    <th>What is the name of the native format of the Resource?</th>
                    <td>
                      <xsl:value-of select="gmd:MD_Format/gmd:name/gco:CharacterString" />
                    </td>
                  </tr>
                  <tr>
                    <th>What is the version of the specified format?</th>
                    <td>
                      <xsl:value-of select="gmd:MD_Format/gmd:version/gco:CharacterString" />
                    </td>
                  </tr>
                  <tr>
                    <th>Is there any known specifications of the format?</th>
                    <td>
                      <xsl:value-of select="gmd:MD_Format/gmd:specification/gco:CharacterString" />
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </xsl:for-each>
          <tr>
            <th>Enter any known URL, file location or physical location of the resource</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:dataSetURI/gco:CharacterString" />
            </td>
          </tr>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:dataQualityInfo">
            <tr>
              <th colspan="2">History &amp; Quality</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <tr>
                    <th>Scope</th>
                    <td>
                      <xsl:value-of select="gmd:DQ_DataQuality/gmd:scope/gmd:DQ_Scope/gmd:level/gmd:MD_ScopeCode/@codeListValue" />
                    </td>
                  </tr>
                  <tr>
                    <th>History</th>
                    <td>
                      <xsl:value-of select="gmd:DQ_DataQuality/gmd:lineage/gmd:LI_Lineage/gmd:statement/gco:CharacterString" />
                    </td>
                  </tr>
                  <xsl:for-each select="gmd:DQ_DataQuality/gmd:report">
                    <tr>
                      <th colspan="2">Quality Report</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <xsl:if test="gmd:DQ_AbsoluteExternalPositionalAccuracy">
                            <tr>
                              <th colspan="2">Absolute External Positional Accuracy</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_AbsoluteExternalPositionalAccuracy/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_AbsoluteExternalPositionalAccuracy/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_CompletenessOmission">
                            <tr>
                              <th colspan="2">Completeness Ommision</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_CompletenessOmission/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_CompletenessOmission/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_CompletenessCommission">
                            <tr>
                              <th colspan="2">Completeness Commission</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_CompletenessCommission/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_CompletenessCommission/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_QuantitativeAttributeAccuracy">
                            <tr>
                              <th colspan="2">Quantitative Attribute Accuracy</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_QuantitativeAttributeAccuracy/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_QuantitativeAttributeAccuracy/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_TopologicalConsistency">
                            <tr>
                              <th colspan="2">Topological Consistency</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_TopologicalConsistency/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_TopologicalConsistency/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_TemporalValidity">
                            <tr>
                              <th colspan="2">Temporal Validity</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_TemporalValidity/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_TemporalValidity/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_DomainConsistency">
                            <tr>
                              <th colspan="2">Domain Consistency</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_DomainConsistency/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_DomainConsistency/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_ThematicClassificationCorrectness">
                            <tr>
                              <th colspan="2">Thematic Classification Correctness</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_ThematicClassificationCorrectness/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_ThematicClassificationCorrectness/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_ConceptualConsistency">
                            <tr>
                              <th colspan="2">Conceptual Consistency</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_ConceptualConsistency/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_ConceptualConsistency/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_FormatConsistency">
                            <tr>
                              <th colspan="2">Format Consistency</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_FormatConsistency/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_FormatConsistency/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_GriddedDataPositionalAccuracy">
                            <tr>
                              <th colspan="2">Gridded Data Positional Accuracy</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_GriddedDataPositionalAccuracy/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_GriddedDataPositionalAccuracy/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_RelativeInternalPositionalAccuracy">
                            <tr>
                              <th colspan="2">Relative Internal Positional Accuracy</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_RelativeInternalPositionalAccuracy/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_RelativeInternalPositionalAccuracy/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_NonQuantitativeAttributeAccuracy">
                            <tr>
                              <th colspan="2">Non Quantitative Attribute Accuracy</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_NonQuantitativeAttributeAccuracy/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_NonQuantitativeAttributeAccuracy/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_AccuracyOfATimeMeasurement">
                            <tr>
                              <th colspan="2">Accuracy Of A Time Measurement</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_AccuracyOfATimeMeasurement/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_AccuracyOfATimeMeasurement/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:DQ_TemporalConsistency">
                            <tr>
                              <th colspan="2">Temporal Consistency</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="gmd:DQ_TemporalConsistency/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="gmd:DQ_TemporalConsistency/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="gmd:DQ_QuantitativeResult">
                                            <tr>
                                              <th colspan="2">Quantitative Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table />
                                              </td>
                                            </tr>
                                          </xsl:if>
                                        </table>
                                      </td>
                                    </tr>
                                  </xsl:for-each>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                        </table>
                      </td>
                    </tr>
                  </xsl:for-each>
                </table>
              </td>
            </tr>
          </xsl:for-each>
          <tr>
            <th>Status</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:status/gmd:MD_ProgressCode/@codeListValue" />
            </td>
          </tr>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceMaintenance">
            <tr>
              <th colspan="2">Maintenance</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <tr>
                    <th>How often is the Resource updated?</th>
                    <td>
                      <xsl:value-of select="gmd:MD_MaintenanceInformation/gmd:maintenanceAndUpdateFrequency/gmd:MD_MaintenanceFrequencyCode/@codeListValue" />
                    </td>
                  </tr>
                  <tr>
                    <th>When is the Resource next due to be updated?</th>
                    <td>
                      <xsl:value-of select="gmd:MD_MaintenanceInformation/gmd:dateOfNextUpdate/gco:Date" />
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </xsl:for-each>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:spatialResolution">
            <tr>
              <th colspan="2">Scale / Resolution</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <xsl:if test="gmd:MD_Resolution/gmd:equivalentScale">
                    <tr>
                      <th colspan="2">Scale</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Scale &amp;#009; &amp;#009; &amp;#032; 1:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_Resolution/gmd:equivalentScale/gmd:MD_RepresentativeFraction/gmd:denominator/gco:Integer" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="gmd:MD_Resolution/gmd:distance">
                    <tr>
                      <th colspan="2">Resolution</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Resolution</th>
                            <td>
                              <xsl:value-of select="gmd:MD_Resolution/gmd:distance/gco:Distance" />
                            </td>
                          </tr>
                          <tr>
                            <th>Units</th>
                            <td>
                              <xsl:value-of select="gmd:MD_Resolution/gmd:distance/gco:Distance/@uom" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                </table>
              </td>
            </tr>
          </xsl:for-each>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints">
            <tr>
              <th colspan="2">Resource Constraints</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <xsl:if test="gmd:MD_LegalConstraints">
                    <tr>
                      <th colspan="2">Legal Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_LegalConstraints/gmd:useLimitation" />
                            </td>
                          </tr>
                          <tr>
                            <th>Access:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_LegalConstraints/gmd:accessConstraints/gmd:MD_RestrictionCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Use:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_LegalConstraints/gmd:useConstraints/gmd:MD_RestrictionCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Other:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_LegalConstraints/gmd:otherConstraints/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="gmd:MD_SecurityConstraints">
                    <tr>
                      <th colspan="2">Security Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_SecurityConstraints/gmd:useLimitation/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>Classification:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_SecurityConstraints/gmd:classification/gmd:MD_ClassificationCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Authority:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_SecurityConstraints/gmd:classificationSystem/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="gmd:MD_Constraints">
                    <tr>
                      <th colspan="2">Generic Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_Constraints/gmd:useLimitation" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                </table>
              </td>
            </tr>
          </xsl:for-each>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:metadataConstraints">
            <tr>
              <th colspan="2">Metadata Constraints</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <xsl:if test="gmd:MD_LegalConstraints">
                    <tr>
                      <th colspan="2">Legal Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_LegalConstraints/gmd:useLimitation" />
                            </td>
                          </tr>
                          <tr>
                            <th>Access:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_LegalConstraints/gmd:accessConstraints/gmd:MD_RestrictionCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Use:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_LegalConstraints/gmd:useConstraints/gmd:MD_RestrictionCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Other:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_LegalConstraints/gmd:otherConstraints/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="gmd:MD_SecurityConstraints">
                    <tr>
                      <th colspan="2">Security Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_SecurityConstraints/gmd:useLimitation/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>Classification:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_SecurityConstraints/gmd:classification/gmd:MD_ClassificationCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Authority:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_SecurityConstraints/gmd:classificationSystem/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="gmd:MD_Constraints">
                    <tr>
                      <th colspan="2">Generic Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="gmd:MD_Constraints/gmd:useLimitation" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                </table>
              </td>
            </tr>
          </xsl:for-each>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent">
            <tr>
              <th colspan="2">Extent</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <tr>
                    <th>Extent Description</th>
                    <td>
                      <xsl:value-of select="gmd:EX_Extent/gmd:description/gco:CharacterString" />
                    </td>
                  </tr>
                  <xsl:for-each select="gmd:EX_Extent/gmd:temporalElement">
                    <tr>
                      <th colspan="2">Temporal Extent</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <xsl:if test="gmd:EX_TemporalExtent/gmd:extent/gml:TimeInstant">
                            <tr>
                              <th colspan="2">Single date/tiime</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Single date/time</th>
                                    <td>
                                      <xsl:value-of select="gmd:EX_TemporalExtent/gmd:extent/gml:TimeInstant/gml:timePosition" />
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod">
                            <tr>
                              <th colspan="2">A range of date/times</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Start date</th>
                                    <td>
                                      <xsl:value-of select="gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod/gml:beginPosition" />
                                    </td>
                                  </tr>
                                  <tr>
                                    <th>End date/time</th>
                                    <td>
                                      <xsl:value-of select="gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod/gml:endPosition" />
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:EX_TemporalExtent/gmd:extent/gml:TimeNode">
                            <tr>
                              <th colspan="2">Time Node</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table />
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="gmd:EX_TemporalExtent/gmd:extent/gml:TimeEdge">
                            <tr>
                              <th colspan="2">Time Edge</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table />
                              </td>
                            </tr>
                          </xsl:if>
                        </table>
                      </td>
                    </tr>
                  </xsl:for-each>
                  <xsl:for-each select="gmd:EX_Extent/gmd:verticalElement">
                    <tr>
                      <th colspan="2">Vertical Extent</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Vertical coodinate Reference System</th>
                            <td>
                              <xsl:value-of select="gmd:EX_VerticalExtent/gmd:verticalCRS/gml:VerticalCRS/gml:identifier" />
                            </td>
                          </tr>
                          <tr>
                            <th>Scope of the Coordinate Reference System</th>
                            <td>
                              <xsl:value-of select="gmd:EX_VerticalExtent/gmd:verticalCRS/gml:VerticalCRS/gml:scope" />
                            </td>
                          </tr>
                          <tr>
                            <th>Enter the minimum vertical extent value</th>
                            <td>
                              <xsl:value-of select="gmd:EX_VerticalExtent/gmd:minimumValue/gco:Real" />
                            </td>
                          </tr>
                          <tr>
                            <th>Enter the maximum vertical extent</th>
                            <td>
                              <xsl:value-of select="gmd:EX_VerticalExtent/gmd:maximumValue/gco:Real" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:for-each>
                </table>
              </td>
            </tr>
          </xsl:for-each>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:distributionInfo/gmd:MD_Distribution/gmd:distributor">
            <tr>
              <th colspan="2">Distributor</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <xsl:for-each select="gmd:MD_Distributor/gmd:distributorFormat">
                    <tr>
                      <th colspan="2">Distribution Format</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>What is the name of the format?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_Format/gmd:name/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>What is the version of the format?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_Format/gmd:version/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>Is there an amendment number of the format version?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_Format/gmd:amendmentNumber/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>Is there a known specification of the format?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_Format/gmd:specification/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>Are there any file decompression techniques to be used?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_Format/gmd:fileDecompressionTechnique/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:for-each>
                  <xsl:for-each select="gmd:MD_Distributor/gmd:distributorTransferOptions">
                    <tr>
                      <th colspan="2">Distribution Transfer Options</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>What is the unit of distribution?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_DigitalTransferOptions/gmd:unitsOfDistribution/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>How big is the transfer? (enter decimal numbers and use the picklist on right to denote size)</th>
                            <td>
                              <xsl:value-of select="gmd:MD_DigitalTransferOptions/gmd:transferSize/gco:Real" />
                            </td>
                          </tr>
                          <xsl:for-each select="gmd:MD_DigitalTransferOptions/gmd:onLine">
                            <tr>
                              <th colspan="2">Online Transfer Information</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>What is the online location of this option? (URL)</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_OnlineResource/gmd:linkage/gmd:URL" />
                                    </td>
                                  </tr>
                                  <tr>
                                    <th>Are there any specific connection protocols that needs to be used. e.g. scp or ftp.</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_OnlineResource/gmd:protocol/gco:CharacterString" />
                                    </td>
                                  </tr>
                                  <tr>
                                    <th>Is there any specific applications which need to be used for this?</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_OnlineResource/gmd:applicationProfile/gco:CharacterString" />
                                    </td>
                                  </tr>
                                  <tr>
                                    <th>What is the name of the online resource?</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_OnlineResource/gmd:name/gco:CharacterString" />
                                    </td>
                                  </tr>
                                  <tr>
                                    <th>Enter description of the online resource here</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_OnlineResource/gmd:description/gco:CharacterString" />
                                    </td>
                                  </tr>
                                  <tr>
                                    <th>What function does this online resource perform?</th>
                                    <td>
                                      <xsl:value-of select="gmd:CI_OnlineResource/gmd:function/gmd:CI_OnLineFunctionCode/@codeListValue" />
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </xsl:for-each>
                          <tr>
                            <th>What is the name of the medium the resource can be transferred in?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_DigitalTransferOptions/gmd:offLine/gmd:MD_Medium/gmd:name/gmd:MD_MediumNameCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>What is the medium density? (enter a decimal number only)</th>
                            <td>
                              <xsl:value-of select="gmd:MD_DigitalTransferOptions/gmd:offLine/gmd:MD_Medium/gmd:density/gco:Real" />
                            </td>
                          </tr>
                          <tr>
                            <th>What is the unit of the density? (if density is specified above)</th>
                            <td>
                              <xsl:value-of select="gmd:MD_DigitalTransferOptions/gmd:offLine/gmd:MD_Medium/gmd:densityUnits/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>How many items of this medium are there? (enter whole numbers only)</th>
                            <td>
                              <xsl:value-of select="gmd:MD_DigitalTransferOptions/gmd:offLine/gmd:MD_Medium/gmd:volumes/gco:Integer" />
                            </td>
                          </tr>
                          <tr>
                            <th>Are there any other constraints in relation to using this medium?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_DigitalTransferOptions/gmd:offLine/gmd:MD_Medium/gmd:mediumNote/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:for-each>
                  <xsl:for-each select="gmd:MD_Distributor/gmd:distributionOrderProcess">
                    <tr>
                      <th colspan="2">Standard Order Process</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>What are the feels and terms?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_StandardOrderProcess/gmd:fees/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>What is the planned available date/time?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_StandardOrderProcess/gmd:plannedAvailableDateTime/gco:DateTime" />
                            </td>
                          </tr>
                          <tr>
                            <th>Enter any specific ordering instructions here.</th>
                            <td>
                              <xsl:value-of select="gmd:MD_StandardOrderProcess/gmd:orderingInstructions/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>What is the typical turnaround time for the filling of an order?</th>
                            <td>
                              <xsl:value-of select="gmd:MD_StandardOrderProcess/gmd:turnaround/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:for-each>
                </table>
              </td>
            </tr>
          </xsl:for-each>
        </table>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>