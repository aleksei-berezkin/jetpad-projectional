/*
 * Copyright 2012-2013 JetBrains s.r.o
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
package jetbrains.jetpad.projectional.demo.nanoLang.mapper;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import jetbrains.jetpad.mapper.Mapper;
import jetbrains.jetpad.mapper.Synchronizers;
import jetbrains.jetpad.projectional.cell.TextCell;
import jetbrains.jetpad.projectional.cell.support.TextEditing;
import jetbrains.jetpad.projectional.demo.nanoLang.model.VarExpression;

class VarExpressionMapper extends Mapper<VarExpression, TextCell> {
  VarExpressionMapper(VarExpression source) {
    super(source, new TextCell());
  }

  @Override
  protected void registerSynchronizers(SynchronizersConfiguration conf) {
    super.registerSynchronizers(conf);

    getTarget().addTrait(TextEditing.validTextEditing(new Predicate<String>() {
      @Override
      public boolean apply(String s) {
        return s.length() == 1 && s.charAt(0) != ' ';
      }
    }));

    conf.add(Synchronizers.forProperties(getSource().name, getTarget().text()));
  }
}