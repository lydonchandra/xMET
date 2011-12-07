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
package xmet.profiles.codecs;

/**
 * An interface to be implemented by codecs if overriding the default codecs is
 * desired Applies to all profile specific type codecs who default to something
 * if unspecified.
 * @author Nahid Akbar
 */
public interface DefaultCodec
    extends
    Codec {

    /**
     * Gets the priority. Higher the priority value, the more chances of it
     * being used as default.
     * @return the priority
     */
    Integer getPriority();
}
