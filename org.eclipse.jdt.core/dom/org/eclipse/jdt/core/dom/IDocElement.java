/*******************************************************************************
 * Copyright (c) 2004, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.core.dom;

/**
 * Common marker interface for AST nodes that represent fragments in doc elements. 
 * These are node types that can legitimately be included in {@link TagElement#fragments() TagElement.fragments()}.
 *
 * @since 3.0
 * @see TagElement#fragments()
 */
interface IDocElement {
	// marker-type interfaces have no members
}
