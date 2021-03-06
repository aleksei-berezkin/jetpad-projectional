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
package jetbrains.jetpad.projectional.view.toGwt;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import jetbrains.jetpad.geometry.Rectangle;
import jetbrains.jetpad.mapper.Synchronizer;
import jetbrains.jetpad.mapper.Synchronizers;
import jetbrains.jetpad.model.property.WritableProperty;
import jetbrains.jetpad.projectional.view.View;

class GwtViewSynchronizers {
  static Synchronizer boundsSyncrhonizer(View view, final Element el) {
    return Synchronizers.forPropsOneWay(view.bounds(), new WritableProperty<Rectangle>() {
      @Override
      public void set(Rectangle value) {
        el.getStyle().setWidth(value.dimension.x + 1, Style.Unit.PX);
        el.getStyle().setHeight(value.dimension.y + 1, Style.Unit.PX);
      }
    });
  }

}