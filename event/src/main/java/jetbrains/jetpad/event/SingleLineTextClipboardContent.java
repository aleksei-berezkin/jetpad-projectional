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
package jetbrains.jetpad.event;

class SingleLineTextClipboardContent implements ClipboardContent {
  private String myText;

  SingleLineTextClipboardContent(String text) {
    myText = text;
  }

  @Override
  public boolean isSupported(ContentKind<?> kind) {
    return kind == ContentKinds.SINGLE_LINE_TEXT;
  }

  @Override
  public <T> T get(ContentKind<T> kind) {
    if (kind == ContentKinds.SINGLE_LINE_TEXT) return (T) myText;

    throw new IllegalArgumentException();
  }
}