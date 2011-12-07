/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br>
 * <br>
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br>
 * <br>
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br>
 * <br>
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br>
 * <br>
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.services;

/**
 * a Service Provider. Implementation of the lowest level of abstraction in
 * xMET's design. To Implement a service interface X and ServiceProvider<X>. It
 * must have a default constructor as service locators are instantiated without
 * parameters. It must also be present in a services setting file if not being
 * registered from another tool. Refer to admin documentation for more details
 * on the services setting file.
 * @param <E> the element type
 * @author Nahid Akbar
 */
public interface ServiceProvider<E> {

    /**
     * Gets the priority. Services are sorted in terms of their priorities so
     * that the service with the highest priority comes before the service with
     * the lowest priority. Note that this can determine which service gets used
     * in situations when only one service is used. This souldn't matter if
     * there is a selection.
     * @return the priority
     */
    Integer getPriority();

}
