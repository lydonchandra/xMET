/**
 * Contains the model the rest of SCV works on. Checklist for adding a new
 * element to the model:
 * <ol>
 * <li>Add the class
 * <li>Add relevant superclass and interfaces
 * <ol>
 * <li>Code
 * <li>GroupSubitem
 * <li>PageSubitem
 * <li>ParentItem
 * <li>RepeatedItem
 * </ol>
 * <li>Annotate with the @CS storage annotations like the other classes
 * <li>Add the class to SCVUtils.scvModelClasses array
 * <li>Make it serializable to allow clonning.
 * <li>Modify the ModelVisitor to add appropriate visitor methods
 * <li>Modify the DefaultModelVisitor to provide a super implementation of the
 * methods
 * <li>Add the class to SCVUtils.accept function
 * <li>Modify the SCV Designer ore CodeEditorPSE to be able to add it from
 * designer. Remember to modify getAllowedChildrenClasses, addChildren,
 * getMaximumChildrenCount, getTreeCellRendererComponent, and updateEditorPanels
 * methods as appropriate
 * <li>Update dependent classes
 * <ol>
 * <li>Add initialisation code in InitializerUtil
 * (xmet.client.tools.metadata.editor.views.semiCustom.utils)
 * <li>Update PanelBuilder
 * (xmet.client.tools.metadata.editor.views.semiCustom.view)
 * <li>Update ValidationUpdateUtil
 * (xmet.client.tools.metadata.editor.views.semiCustom.view)
 * <li>Update DefaultValuesLoaderUtil
 * (xmet.client.tools.metadata.editor.views.semiCustom.view)
 * <li>Update DataLoaderUtil
 * (xmet.client.tools.metadata.editor.views.semiCustom.view)
 * <li>Update CodeExecutorUtil (if code)
 * (xmet.client.tools.metadata.editor.views.semiCustom.utils)
 * <li>Update CodeExecusionTraverserUtil
 * (xmet.client.tools.metadata.editor.views.semiCustom.utils)
 * <li>Update XSLTTemplateBuilderUtil
 * (xmet.client.tools.metadata.editor.views.semiCustom.designer)
 * <li>Update RetraceUtil
 * (xmet.client.tools.metadata.editor.views.semiCustom.utils)
 * <li>Update DefaultModelSkeletonVisitor
 * (xmet.client.tools.metadata.editor.views.semiCustom.utils)
 * <li>Update XpathExtractionUtil
 * (xmet.client.tools.metadata.editor.views.semiCustom.utils)
 * <li>Update XpathFixUtil
 * (xmet.client.tools.metadata.editor.views.semiCustom.utils)
 * <li>Update UninitializeUtil
 * (xmet.client.tools.metadata.editor.views.semiCustom.utils)
 * </ol>
 * </ol>
 * @author Nahid Akbar
 */
package xmet.tools.metadata.editor.views.scv.model;

