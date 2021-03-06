/*
 * Copyright 2012-2016 JetBrains s.r.o
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrains.jetpad.projectional.svg;

import jetbrains.jetpad.model.collections.list.ObservableList;

public class SvgStyleElement extends SvgElement {
  public SvgStyleElement() {
    super();
  }

  public SvgStyleElement(String content) {
    this();

    setContent(content);
  }

  public SvgStyleElement(SvgCssResource resource) {
    this(resource.css());
  }

  @Override
  public String getElementName() {
    return "style";
  }

  public void setContent(String content) {
    ObservableList<SvgNode> children = children();
    while (!children.isEmpty()) {
      children.remove(0);
    }
    SvgTextNode textNode = new SvgTextNode(content);
    children.add(textNode);
    setAttribute("type", "text/css");
  }
}