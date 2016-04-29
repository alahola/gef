/*******************************************************************************
 * Copyright (c) 2016 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API and implementation
 *     Alexander Nyßen  (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.gef4.fx.nodes;

import org.eclipse.gef4.common.beans.property.ReadOnlyMapWrapperEx;
import org.eclipse.gef4.fx.anchors.AnchorKey;
import org.eclipse.gef4.fx.anchors.DynamicAnchor;
import org.eclipse.gef4.fx.anchors.DynamicAnchor.AnchoredReferencePoint;
import org.eclipse.gef4.geometry.planar.Point;

import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.collections.FXCollections;

/**
 * Abstract base class for {@link IConnectionRouter}s.
 */
public abstract class AbstractRouter implements IConnectionRouter {

	private ReadOnlyMapWrapper<AnchorKey, Point> positionHintsProperty = new ReadOnlyMapWrapperEx<>(
			FXCollections.<AnchorKey, Point> observableHashMap());

	/**
	 * Computes the reference point for the dynamic anchor at the given index.
	 *
	 * @param connection
	 *            The {@link Connection} that is currently routed.
	 * @param index
	 *            The index specifying the dynamic anchor for which to provide a
	 *            reference point.
	 * @return The reference point for the anchor at the given index.
	 */
	protected abstract Point getAnchoredReferencePoint(Connection connection,
			int index);

	@Override
	public ReadOnlyMapProperty<AnchorKey, Point> positionHintsProperty() {
		return positionHintsProperty.getReadOnlyProperty();
	}

	/**
	 * Update's the reference point of the anchor with the given index.
	 *
	 * @param connection
	 *            The connection whose anchor to update.
	 * @param index
	 *            The index of the connection anchor, whose reference point is
	 *            to be updated.
	 */
	protected void updateComputationParameters(Connection connection,
			int index) {
		// only update if necessary (when it changes)
		AnchorKey anchorKey = connection.getAnchorKey(index);
		AnchoredReferencePoint referencePointParameter = ((DynamicAnchor) connection
				.getAnchor(index)).getComputationParameter(anchorKey,
						AnchoredReferencePoint.class);
		Point oldRef = referencePointParameter.get();

		// if we have a position hint for the anchor, we need to use this as the
		// reference point
		Point newRef = getAnchoredReferencePoint(connection, index);
		if (oldRef == null || !newRef.equals(oldRef)) {
			referencePointParameter.set(newRef);
		}
	}
}
