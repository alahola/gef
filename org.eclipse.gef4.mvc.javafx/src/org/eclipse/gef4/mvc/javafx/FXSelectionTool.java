package org.eclipse.gef4.mvc.javafx;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import org.eclipse.gef4.mvc.aspects.selection.AbstractSelectionTool;
import org.eclipse.gef4.mvc.parts.IContentPart;
import org.eclipse.gef4.mvc.parts.IHandlePart;
import org.eclipse.gef4.mvc.parts.IRootVisualPart;
import org.eclipse.gef4.mvc.parts.IVisualPart;
import org.eclipse.gef4.mvc.tools.ITool;

public class FXSelectionTool extends AbstractSelectionTool<Node> {

	private EventHandler<MouseEvent> pressedFilter = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			IVisualPart<Node> targetPart = getTargetPart(event);
			if(targetPart == null || targetPart instanceof IRootVisualPart) {
				select(null, false);
			}
			else if (targetPart instanceof IContentPart) {
				if (select((IContentPart<Node>) targetPart, event.isControlDown())) {
//					event.consume();
				}
			}
			else if (targetPart instanceof IHandlePart){
				IHandlePart<Node> handlePart = (IHandlePart<Node>)targetPart;
				ITool<Node> handleTool = handlePart.getHandleTool();
				getDomain().pushTool(handleTool);
			}
			else {
				throw new IllegalArgumentException("Unsupported part type.");
			}
		}
	};

	protected IVisualPart<Node> getTargetPart(MouseEvent event) {
		IVisualPart<Node> newSelection = getDomain().getViewer()
				.getVisualPartMap().get(((Node) event.getTarget()));
		if (newSelection instanceof IVisualPart) {
			return (IVisualPart<Node>) newSelection;
		}
		return null;
	}

	@Override
	public void activate() {
		super.activate();
		((FXViewer) getDomain().getViewer()).getCanvas().getScene()
				.addEventFilter(MouseEvent.MOUSE_PRESSED, pressedFilter);
	}

	@Override
	public void deactivate() {
		((FXViewer) getDomain().getViewer()).getCanvas().getScene()
				.removeEventFilter(MouseEvent.MOUSE_PRESSED, pressedFilter);
		super.deactivate();
	}

}
