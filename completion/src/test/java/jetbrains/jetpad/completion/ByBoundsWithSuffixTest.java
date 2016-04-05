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
package jetbrains.jetpad.completion;

import jetbrains.jetpad.base.Runnables;

public class ByBoundsWithSuffixTest extends BaseCompletionItemTestCase {
  @Override
  CompletionItem createCompletionItem() {
    return new ByBoundsCompletionItem("'", "'") {
      @Override
      public Runnable complete(String text) {
        return Runnables.EMPTY;
      }
    };
  }

  @Override
  String[] getStrictPrefixes() {
    return new String[] { "", "'", "'x" };
  }

  @Override
  String[] getMatches() {
    return new String[] { "''", "'x'" };
  }

  @Override
  String[] getBadItems() {
    return new String[] { " '", " ''", "'''", "a'b'", "'a'b" };
  }
}
