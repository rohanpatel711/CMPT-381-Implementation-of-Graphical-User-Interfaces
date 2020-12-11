package CriticalPath;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;



public class GraphController {

    private GraphModel model;
    private InteractionModel iModel;


    private enum NodeState {READY, MOVING, PREPARE_CREATE, SELECTED}

    private enum EdgeState {STARTED,STOPPED}

    private boolean dragging = false;

    private NodeState nodeState;
    private EdgeState edgeState;

    private Node selectedNode;

    double normX, normY;
    double prevX, prevY;

    public GraphController() {
        nodeState = NodeState.READY;
    }

    public void setModel(GraphModel newModel) {
        model = newModel;
    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    public void handlePressed(MouseEvent event) {
        normX = (event.getX() / (iModel.viewWidth * iModel.zoomLevel));
        normY = (event.getY() / (iModel.viewHeight * iModel.zoomLevel));
        prevX = normX;
        prevY = normY;
        boolean nodeHit = model.checkNodeHit(normX + iModel.viewPortLeft, normY + iModel.viewPortTop);
        boolean edgehit = model.checkEdgeHit(normX + iModel.viewPortLeft, normY + iModel.viewPortTop);


        if (event.getButton() == MouseButton.SECONDARY) {
            if (nodeHit) {
                edgeState = EdgeState.STARTED;
                Node startNode = model.findNode(normX + iModel.viewPortLeft, normY + iModel.viewPortTop);

                Edge edge = new Edge(normX + iModel.viewPortLeft, normY + iModel.viewPortTop, normX + iModel.viewPortLeft, normY + iModel.viewPortTop, startNode);
                iModel.setDraggingEdge(edge);
                model.addEdge(edge);
            }
               else {
                    // on the background
                    // side effects:
                    // (none)
                    // move to new state
                    nodeState = NodeState.PREPARE_CREATE;
                }


        } else {
            switch (nodeState) {
                case READY:
                    // are we on background or on a box?
                    if (nodeHit) {
                        // context: on a box
                        // side effects:
                        // set the box as the selection
                        Node n = model.findNode(normX + iModel.viewPortLeft, normY + iModel.viewPortTop);
                        iModel.setSelection(n);
                        selectedNode = n;
                        nodeState = NodeState.SELECTED;
                    } else if (edgehit) {
                        Edge e = model.findEdge(normX + iModel.viewPortLeft, normY + iModel.viewPortTop);

                        //uncheck if already selected otherwise select edge
                        if (iModel.selected!=null){
                            if (iModel.selected.equals(e)) {
                                iModel.unsetSelection();
                            }else{
                                iModel.setSelection(e);
                            }
                        }else {
                            iModel.setSelection(e);
                        }


                    }else{
                        iModel.unsetSelection();
                    }
                    break;
                case SELECTED:
                    iModel.unsetSelection();
                    nodeState = NodeState.READY;
                    break;
            }
        }
    }

    public void handleReleased(MouseEvent event) {
        normX = (event.getX() / (iModel.viewWidth * iModel.zoomLevel)) + iModel.viewPortLeft;
        normY = (event.getY() / (iModel.viewHeight * iModel.zoomLevel)) + iModel.viewPortTop;

        if (dragging) {
            dragging = false;
            nodeState = NodeState.READY;
        } else {

            if (event.getButton() == MouseButton.SECONDARY) {
                Edge edge = iModel.getDraggingEdge();
                if (edgeState == EdgeState.STARTED) {
                    if (model.checkNodeHit(normX, normY)) {
                        model.finishEdge(edge, model.findNode(normX, normY));
                        edgeState=EdgeState.STOPPED;
                    } else {
                        model.removeEdge(edge);
                    }
                }
            }


            switch (nodeState) {
                case PREPARE_CREATE:
                    // context:
                    // none
                    // side effects:
                    // create new box
                    model.createNode(normX, normY, DefaultData.defaultNodeWidth, DefaultData.defaultNodeHeight);
                    // move to new state
                    nodeState = NodeState.READY;
                    break;
                case MOVING:
                    // context: none
                    // side effects:
                    iModel.unsetDraggingNode();
                    // move to new state
                    nodeState = NodeState.READY;
                    break;

                case SELECTED:
                    iModel.setSelection(selectedNode);


            }
        }
    }

    public void handleDrag(MouseEvent event) {
        normX = (event.getX() / (iModel.viewWidth * iModel.zoomLevel));
        normY = (event.getY() / (iModel.viewHeight * iModel.zoomLevel));
        double dX = normX - prevX;
        double dY = normY - prevY;
        prevX = normX;
        prevY = normY;

        if (dragging) {
            iModel.changeViewPort(dX, dY);
        } else {
            if (event.getButton() == MouseButton.SECONDARY) {
                if (edgeState == EdgeState.STARTED) {
                    model.moveEdge(iModel.getDraggingEdge(), normX + iModel.viewPortLeft, normY + iModel.viewPortTop);
                } else {
                    dragging = true;
                }
            } else {
                // context: none
                // side effects
                if (nodeState == NodeState.SELECTED) {
                    // move to new state
                    iModel.setDraggingNode(this.selectedNode);
                    nodeState = NodeState.MOVING;
                } else if (nodeState == NodeState.MOVING) {
                    model.moveNode(iModel.getDraggingNode(), dX, dY);
                } else {
                    dragging = true;
                }

            }
        }
    }

    public void deleteElement(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE) { //back space for mac users
            if (iModel.selected instanceof Edge) {
                Edge edge = (Edge) iModel.selected;
                model.removeEdge(edge);
            } else if (iModel.selected instanceof Node) {
                Node node = (Node) iModel.selected;
                model.removeNode(node);
            }
        }
    }

    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        iModel.changeZoomLevel((Double) newValue);
    }

    public void updateNodeName(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (iModel.selected instanceof Node) {
                if (!iModel.selected.equals(model.source)) {
                    iModel.updateNodeName(((TextField) keyEvent.getSource()).getText());
                }else{
                    ((TextField) keyEvent.getSource()).setText("Source");
                }
            }
        }
    }

    public void updateNodeCost(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {

            if (iModel.selected instanceof Node) {
                try {
                    double cost = Double.parseDouble(((TextField) keyEvent.getSource()).getText());
                    if (!iModel.selected.equals(model.source)) {
                        iModel.updateNodeCost(cost);
                    }else{
                        ((TextField) keyEvent.getSource()).setText("0.0");
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Wrong Cost");
                    alert.setHeaderText("Cost of activity can only be a number");
                    alert.setContentText("");
                    alert.showAndWait();

                }
            }
        }
    }
}
