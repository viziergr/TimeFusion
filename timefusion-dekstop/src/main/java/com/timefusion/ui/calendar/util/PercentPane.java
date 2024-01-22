package com.timefusion.ui.calendar.util;

import java.util.List;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class PercentPane extends AnchorPane {

  private static final String TOP_ANCHOR = "percpane-top-anchor";
  private static final String LEFT_ANCHOR = "percpane-left-anchor";
  private static final String BOTTOM_ANCHOR = "percpane-bottom-anchor";
  private static final String RIGHT_ANCHOR = "percpane-right-anchor";

  public PercentPane() {
    super();
  }

  public PercentPane(Node... children) {
    super();
    getChildren().addAll(children);
  }

  static void setConstraint(Node node, Object key, Object value) {
    if (value == null) {
      node.getProperties().remove(key);
    } else {
      node.getProperties().put(key, value);
    }
    if (node.getParent() != null) {
      node.getParent().requestLayout();
    }
  }

  static Object getConstraint(Node node, Object key) {
    if (node.hasProperties()) {
      Object value = node.getProperties().get(key);
      if (value != null) {
        return value;
      }
    }
    return null;
  }

  public static void setTopAnchor(Node child, Double value) {
    setConstraint(child, TOP_ANCHOR, value);
  }

  public static void setRightAnchor(Node child, Double value) {
    setConstraint(child, RIGHT_ANCHOR, value);
  }

  public static void setBottomAnchor(Node child, Double value) {
    setConstraint(child, BOTTOM_ANCHOR, value);
  }

  public static void setLeftAnchor(Node child, Double value) {
    setConstraint(child, LEFT_ANCHOR, value);
  }

  public static Double getTopAnchor(Node child) {
    return (Double) getConstraint(child, TOP_ANCHOR);
  }

  public static Double getRightAnchor(Node child) {
    return (Double) getConstraint(child, RIGHT_ANCHOR);
  }

  public static Double getBottomAnchor(Node child) {
    return (Double) getConstraint(child, BOTTOM_ANCHOR);
  }

  public static Double getLeftAnchor(Node child) {
    return (Double) getConstraint(child, LEFT_ANCHOR);
  }

  @Override
  protected void layoutChildren() {
    final List<Node> children = getManagedChildren();
    for (Node child : children) {
      if (PercentPane.getLeftAnchor(child) != null) {
        AnchorPane.setLeftAnchor(
          child,
          getWidth() *
          Math.min(
            PercentPane.getLeftAnchor(child),/* what we want */
            1.0 -
            Optional.ofNullable(PercentPane.getRightAnchor(child)).orElse(0.0)
          )
        );
      }
      if (PercentPane.getTopAnchor(child) != null) {
        AnchorPane.setTopAnchor(
          child,
          getHeight() *
          Math.min(
            PercentPane.getTopAnchor(child),
            1.0 -
            Optional.ofNullable(PercentPane.getBottomAnchor(child)).orElse(0.0)
          )
        );
      }
      if (PercentPane.getRightAnchor(child) != null) {
        AnchorPane.setRightAnchor(
          child,
          getWidth() *
          Math.min(
            PercentPane.getRightAnchor(child),
            1.0 -
            Optional.ofNullable(PercentPane.getLeftAnchor(child)).orElse(0.0)
          )
        );
      }
      if (PercentPane.getBottomAnchor(child) != null) {
        AnchorPane.setBottomAnchor(
          child,
          getHeight() *
          Math.min(
            PercentPane.getBottomAnchor(child),
            1.0 -
            Optional.ofNullable(PercentPane.getTopAnchor(child)).orElse(0.0)
          )
        );
      }
    }
    super.layoutChildren();
  }
}
