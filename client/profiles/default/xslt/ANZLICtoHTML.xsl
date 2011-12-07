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
            <th>Resource Type Name</th>
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
          <tr>
            <th>Metadata role</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:contact/gmd:CI_ResponsibleParty/gmd:role/gmd:CI_RoleCode/@codeListValue" />
            </td>
          </tr>
          <tr>
            <th>Author</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:contact/gmd:CI_ResponsibleParty" />
            </td>
          </tr>
          <tr>
            <th>Withhold Name</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:contact/gmd:CI_ResponsibleParty/gmd:individualName/@gco:nilReason" />
            </td>
          </tr>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact">
            <tr>
              <th colspan="2">Resouce contact</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <tr>
                    <th>Withhold Name</th>
                    <td>
                      <xsl:value-of select="//gmd:CI_ResponsibleParty/gmd:individualName/@gco:nilReason" />
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </xsl:for-each>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:credit">
            <tr>
              <th colspan="2">Recognition of those who contributed to the compilation of the resource</th>
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
                <table />
              </td>
            </tr>
          </xsl:for-each>
          <tr>
            <th>Resource location</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:dataSetURI/gco:CharacterString" />
            </td>
          </tr>
          <tr>
            <th>History</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:dataQualityInfo/gmd:DQ_DataQuality/gmd:lineage/gmd:LI_Lineage/gmd:statement/gco:CharacterString" />
            </td>
          </tr>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:dataQualityInfo">
            <tr>
              <th colspan="2">Data quality</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <xsl:for-each select="//gmd:DQ_DataQuality/gmd:report">
                    <tr>
                      <th colspan="2">Reports</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <xsl:if test="//gmd:DQ_AbsoluteExternalPositionalAccuracy">
                            <tr>
                              <th colspan="2">Absolute External Positional Accuracy</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_AbsoluteExternalPositionalAccuracy/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_AbsoluteExternalPositionalAccuracy/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_CompletenessOmission">
                            <tr>
                              <th colspan="2">Completeness Ommision</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_CompletenessOmission/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_CompletenessOmission/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_CompletenessCommission">
                            <tr>
                              <th colspan="2">Completeness Commission</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_CompletenessCommission/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_CompletenessCommission/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_QuantitativeAttributeAccuracy">
                            <tr>
                              <th colspan="2">Quantitative Attribute Accuracy</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_QuantitativeAttributeAccuracy/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_QuantitativeAttributeAccuracy/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_TopologicalConsistency">
                            <tr>
                              <th colspan="2">Topological Consistency</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_TopologicalConsistency/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_TopologicalConsistency/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_TemporalValidity">
                            <tr>
                              <th colspan="2">Temporal Validity</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_TemporalValidity/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_TemporalValidity/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_DomainConsistency">
                            <tr>
                              <th colspan="2">Domain Consistency</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_DomainConsistency/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_DomainConsistency/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_ThematicClassificationCorrectness">
                            <tr>
                              <th colspan="2">Thematic Classification Correctness</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_ThematicClassificationCorrectness/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_ThematicClassificationCorrectness/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_ConceptualConsistency">
                            <tr>
                              <th colspan="2">Conceptual Consistency</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_ConceptualConsistency/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_ConceptualConsistency/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_FormatConsistency">
                            <tr>
                              <th colspan="2">Format Consistency</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_FormatConsistency/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_FormatConsistency/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_GriddedDataPositionalAccuracy">
                            <tr>
                              <th colspan="2">Gridded Data Positional Accuracy</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_GriddedDataPositionalAccuracy/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_GriddedDataPositionalAccuracy/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_RelativeInternalPositionalAccuracy">
                            <tr>
                              <th colspan="2">Relative Internal Positional Accuracy</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_RelativeInternalPositionalAccuracy/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_RelativeInternalPositionalAccuracy/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_NonQuantitativeAttributeAccuracy">
                            <tr>
                              <th colspan="2">Non Quantitative Attribute Accuracy</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_NonQuantitativeAttributeAccuracy/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_NonQuantitativeAttributeAccuracy/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_AccuracyOfATimeMeasurement">
                            <tr>
                              <th colspan="2">Accuracy Of A Time Measurement</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_AccuracyOfATimeMeasurement/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_AccuracyOfATimeMeasurement/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
                          <xsl:if test="//gmd:DQ_TemporalConsistency">
                            <tr>
                              <th colspan="2">Temporal Consistency</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Date of test</th>
                                    <td>
                                      <xsl:value-of select="//gmd:DQ_TemporalConsistency/gmd:dateTime/gco:DateTime" />
                                    </td>
                                  </tr>
                                  <xsl:for-each select="//gmd:DQ_TemporalConsistency/gmd:result">
                                    <tr>
                                      <th colspan="2">Requirements and results</th>
                                    </tr>
                                    <tr>
                                      <td colspan="2">
                                        <table>
                                          <xsl:if test="//gmd:DQ_ConformanceResult">
                                            <tr>
                                              <th colspan="2">Conformance Result</th>
                                            </tr>
                                            <tr>
                                              <td colspan="2">
                                                <table>
                                                  <tr>
                                                    <th>Title of Specification</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Publication Date</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Explanation</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString" />
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <th>Results of test:</th>
                                                    <td>
                                                      <xsl:value-of select="//gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean" />
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </xsl:if>
                                          <xsl:if test="//gmd:DQ_QuantitativeResult">
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
              <xsl:value-of select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:status/gmd:MD_ProgressCode" />
            </td>
          </tr>
          <tr>
            <th>Maintenance</th>
            <td>
              <xsl:value-of select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceMaintenance/gmd:MD_MaintenanceInformation/gmd:maintenanceAndUpdateFrequency/gmd:MD_MaintenanceFrequencyCode/@codeListValue" />
            </td>
          </tr>
          <xsl:for-each select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:spatialResolution">
            <tr>
              <th colspan="2">Scale &amp; Resolution</th>
            </tr>
            <tr>
              <td colspan="2">
                <table>
                  <xsl:if test="//gmd:MD_Resolution/gmd:equivalentScale">
                    <tr>
                      <th colspan="2">Scale</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Scale &amp;#009; &amp;#009; &amp;#032; 1:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_Resolution/gmd:equivalentScale/gmd:MD_RepresentativeFraction/gmd:denominator/gco:Integer" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="//gmd:MD_Resolution/gmd:distance">
                    <tr>
                      <th colspan="2">Resolution</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Resolution</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_Resolution/gmd:distance/gco:Distance" />
                            </td>
                          </tr>
                          <tr>
                            <th>Units</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_Resolution/gmd:distance/gco:Distance/@uom" />
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
                  <xsl:if test="//gmd:MD_LegalConstraints">
                    <tr>
                      <th colspan="2">Legal Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_LegalConstraints/gmd:useLimitation" />
                            </td>
                          </tr>
                          <tr>
                            <th>Access:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_LegalConstraints/gmd:accessConstraints/gmd:MD_RestrictionCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Use:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_LegalConstraints/gmd:useConstraints/gmd:MD_RestrictionCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Other:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_LegalConstraints/gmd:otherConstraints/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="//gmd:MD_SecurityConstraints">
                    <tr>
                      <th colspan="2">Security Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_SecurityConstraints/gmd:useLimitation/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>Classification:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_SecurityConstraints/gmd:classification/gmd:MD_ClassificationCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Authority:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_SecurityConstraints/gmd:classificationSystem/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="//gmd:MD_Constraints">
                    <tr>
                      <th colspan="2">Generic Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_Constraints/gmd:useLimitation" />
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
                  <xsl:if test="//gmd:MD_LegalConstraints">
                    <tr>
                      <th colspan="2">Legal Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_LegalConstraints/gmd:useLimitation" />
                            </td>
                          </tr>
                          <tr>
                            <th>Access:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_LegalConstraints/gmd:accessConstraints/gmd:MD_RestrictionCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Use:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_LegalConstraints/gmd:useConstraints/gmd:MD_RestrictionCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Other:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_LegalConstraints/gmd:otherConstraints/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="//gmd:MD_SecurityConstraints">
                    <tr>
                      <th colspan="2">Security Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_SecurityConstraints/gmd:useLimitation/gco:CharacterString" />
                            </td>
                          </tr>
                          <tr>
                            <th>Classification:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_SecurityConstraints/gmd:classification/gmd:MD_ClassificationCode/@codeListValue" />
                            </td>
                          </tr>
                          <tr>
                            <th>Authority:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_SecurityConstraints/gmd:classificationSystem/gco:CharacterString" />
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </xsl:if>
                  <xsl:if test="//gmd:MD_Constraints">
                    <tr>
                      <th colspan="2">Generic Constraint</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <th>Constraint:</th>
                            <td>
                              <xsl:value-of select="//gmd:MD_Constraints/gmd:useLimitation" />
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
                      <xsl:value-of select="//gmd:EX_Extent/gmd:description/gco:CharacterString" />
                    </td>
                  </tr>
                  <xsl:for-each select="//gmd:EX_Extent/gmd:temporalElement">
                    <tr>
                      <th colspan="2">Temporal extent</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <xsl:if test="//gmd:EX_TemporalExtent/gmd:extent/gml:TimeInstant">
                            <tr>
                              <th colspan="2">Single date/tiime</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Single date/time</th>
                                    <td>
                                      <xsl:value-of select="//gmd:EX_TemporalExtent/gmd:extent/gml:TimeInstant/gml:timePosition" />
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </xsl:if>
                          <xsl:if test="//gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod">
                            <tr>
                              <th colspan="2">A range of date/times</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table>
                                  <tr>
                                    <th>Start date</th>
                                    <td>
                                      <xsl:value-of select="//gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod/gml:beginPosition" />
                                    </td>
                                  </tr>
                                  <tr>
                                    <th>End date/time</th>
                                    <td>
                                      <xsl:value-of select="//gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod/gml:endPosition" />
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
                  <xsl:for-each select="//gmd:EX_Extent/gmd:verticalElement">
                    <tr>
                      <th colspan="2">Vertical extent</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table />
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
                  <tr>
                    <th>Distributor's details</th>
                    <td>
                      <xsl:value-of select="//gmd:MD_Distributor/gmd:distributorContact/gmd:CI_ResponsibleParty" />
                    </td>
                  </tr>
                  <xsl:for-each select="//gmd:MD_Distributor/gmd:distributorFormat">
                    <tr>
                      <th colspan="2">Distribution format</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table />
                      </td>
                    </tr>
                  </xsl:for-each>
                  <xsl:for-each select="//gmd:MD_Distributor/gmd:distributorTransferOptions">
                    <tr>
                      <th colspan="2">Distribution transfer options</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <xsl:for-each select="//gmd:MD_DigitalTransferOptions/gmd:onLine">
                            <tr>
                              <th colspan="2">Online Information</th>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <table />
                              </td>
                            </tr>
                          </xsl:for-each>
                        </table>
                      </td>
                    </tr>
                  </xsl:for-each>
                  <xsl:for-each select="//gmd:MD_Distributor/gmd:distributionOrderProcess">
                    <tr>
                      <th colspan="2">Standard order process</th>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table />
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