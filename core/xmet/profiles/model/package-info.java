/**
 * Contains the object model of the profile the application works with.
 * Checklist for adding a new model element:
 * <ol>
 * <li>Add the object and appropriate methods
 * <li>Add appropriate methods to ModelVisitor and DefaultModelVisitor
 * <li>Add it to appropriate Profile Decoders. e.g. XSDSchemaDecoder
 * <li>Add it to appropriate Data Decoders.
 * <li>Add it to various methods in ModelUtils. e.g.
 * <ol>
 * <li>addConstraints, getConstraints
 * <li>cloneEntity
 * <li>accept - model visitor/default model visitor
 * <li>add a is[Name] and as[Name] methods in ModelUtils
 * <li>getSetable
 * <li>isPresent
 * <li>
 * <li>
 * </ol>
 * <li>Update ConstraintsVerification
 * <li>
 * </ol>
 * @author Nahid Akbar
 */
package xmet.profiles.model;

