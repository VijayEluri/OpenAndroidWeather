/*
	Copyright 2011 Torstein Ingebrigtsen Bø

    This file is part of OpenAndroidWeather.

    OpenAndroidWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenAndroidWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenAndroidWeather.  If not, see <http://www.gnu.org/licenses/>.
 */

package no.firestorm.wsklima.exception;

/**
 * No location is available
 */
public class NoLocationException extends Exception {
	private static final long serialVersionUID = 8928053123816594956L;

	public NoLocationException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 */
	public NoLocationException(Throwable cause) {
		super(cause);
	}

}
