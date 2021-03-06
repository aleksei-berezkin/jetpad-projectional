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
import com.google.gwt.user.client.DOM;
import jetbrains.jetpad.mapper.Synchronizers;
import jetbrains.jetpad.model.property.WritableProperty;
import jetbrains.jetpad.projectional.base.ImageData;
import jetbrains.jetpad.projectional.view.ImageView;

class ImageViewMapper extends BaseViewMapper<ImageView, Element> {
  ImageViewMapper(ViewToDomContext ctx, ImageView source) {
    super(ctx, source, DOM.createImg());
  }

  @Override
  protected void registerSynchronizers(SynchronizersConfiguration conf) {
    super.registerSynchronizers(conf);

    conf.add(Synchronizers.forPropsOneWay(getSource().image, new WritableProperty<ImageData>() {
      @Override
      public void set(ImageData value) {
        getTarget().setPropertyInt("width", value.getDimension().x);
        getTarget().setPropertyInt("height", value.getDimension().y);

        if (value instanceof ImageData.UrlImageData) {
          getTarget().setPropertyString("src", ((ImageData.UrlImageData) value).getUrl());
        } else if (value instanceof ImageData.EmptyImageData) {
          getTarget().setPropertyString("src", null);
        } else {
          throw new UnsupportedOperationException();
        }
      }
    }));
  }
}