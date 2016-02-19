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
package jetbrains.jetpad.cell.completion;

import jetbrains.jetpad.base.Handler;
import jetbrains.jetpad.base.Registration;
import jetbrains.jetpad.cell.*;
import jetbrains.jetpad.cell.trait.CellTrait;
import jetbrains.jetpad.completion.CompletionItem;
import jetbrains.jetpad.completion.CompletionMenuModel;
import jetbrains.jetpad.event.MouseEvent;
import jetbrains.jetpad.geometry.Rectangle;
import jetbrains.jetpad.geometry.Vector;
import jetbrains.jetpad.mapper.Mapper;
import jetbrains.jetpad.mapper.MapperFactory;
import jetbrains.jetpad.mapper.Synchronizers;
import jetbrains.jetpad.model.event.CompositeRegistration;
import jetbrains.jetpad.model.property.DerivedProperty;
import jetbrains.jetpad.model.property.Properties;
import jetbrains.jetpad.model.property.ReadableProperty;
import jetbrains.jetpad.model.property.WritableProperty;
import jetbrains.jetpad.values.Color;

import java.util.Arrays;

class CompletionMenu {
  private static final Color BACKGROUND = new Color(226, 242, 254);
  private static final Color SELECTED_BACKGROUND = new Color(0, 62, 149);
  private static final Color MATCH_TEXT = new Color(247, 232, 193);

  static Cell createCell(CompletionMenuModel model, Handler<CompletionItem> completer, CompositeRegistration reg) {
    final CompletionMenuModelMapper mapper = new CompletionMenuModelMapper(model, completer);
    mapper.attachRoot();

    reg.add(new Registration() {
      @Override
      protected void doRemove() {
        mapper.detachRoot();
      }
    });

    return mapper.getTarget();
  }

  private static class CompletionMenuModelMapper extends Mapper<CompletionMenuModel, ScrollCell> {
    private VerticalCell myRootCell = new VerticalCell();
    private VerticalCell myVerticalCell = new VerticalCell();
    private TextCell myEmptyCell = new TextCell();
    private Handler<CompletionItem> myCompleter;

    private CompletionMenuModelMapper(CompletionMenuModel source, Handler<CompletionItem> completer) {
      super(source, new ScrollCell());

      myEmptyCell.textColor().set(Color.RED);

      myCompleter = completer;

      myRootCell.children().addAll(Arrays.asList(myVerticalCell, myEmptyCell));

      getTarget().children().add(myRootCell);
      getTarget().hasShadow().set(true);
      getTarget().borderColor().set(Color.LIGHT_GRAY);
      getTarget().background().set(BACKGROUND);
      getTarget().maxDimension().set(new Vector(600, 200));
      getTarget().scroll().set(true);
    }

    @Override
    protected void registerSynchronizers(SynchronizersConfiguration conf) {
      super.registerSynchronizers(conf);

      conf.add(Synchronizers.forObservableRole(
          this,
          getSource().visibleItems,
          myVerticalCell.children(),
          new MapperFactory<CompletionItem, Cell>() {
            @Override
            public Mapper<? extends CompletionItem, ? extends Cell> createMapper(CompletionItem source) {
              return new CompletionItemMapper(source);
            }
          }));

      conf.add(Synchronizers.forPropsOneWay(getSource().loading, Properties.ifProp(myEmptyCell.text(), "Loading...", "<no completion items>")));
      conf.add(Synchronizers.forPropsOneWay(getSource().loading, Properties.ifProp(myEmptyCell.textColor(), Color.GRAY, Color.RED)));
      conf.add(Synchronizers.forPropsOneWay(Properties.isEmpty(getSource().visibleItems), myEmptyCell.visible()));
    }
  }

  private static class CompletionItemMapper extends Mapper<CompletionItem, HorizontalCell> {
    private TextCell myText;

    private CompletionItemMapper(CompletionItem source) {
      super(source, new HorizontalCell());
      getTarget().children().add(myText = new TextCell());
      getTarget().addTrait(new CellTrait() {
        @Override
        public void onMousePressed(Cell cell, MouseEvent event) {
          CompletionMenuModelMapper parentMapper = (CompletionMenuModelMapper) getParent();
          if (parentMapper.getSource().selectedItem.get() == getSource()) {
            parentMapper.myCompleter.handle(getSource());
          } else {
            parentMapper.getSource().selectedItem.set(getSource());
          }
          event.consume();
        }
      });
    }

    @Override
    protected void registerSynchronizers(SynchronizersConfiguration conf) {
      super.registerSynchronizers(conf);
      final ReadableProperty<String> text = ((CompletionMenuModelMapper) getParent()).getSource().text;

      final ReadableProperty<Boolean> matches = new DerivedProperty<Boolean>(text) {
        @Override
        public Boolean doGet() {
          return getSource().isMatch(text.get());
        }
      };

      ReadableProperty<CompletionItem> selectedItem = ((CompletionMenuModelMapper) getParent()).getSource().selectedItem;
      final ReadableProperty<Boolean> selected = Properties.same(selectedItem, getSource());

      ReadableProperty<Color> textColor = new DerivedProperty<Color>(matches, selected) {
        @Override
        protected Color doGet() {
          if (matches.get()) {
            return selected.get() ? MATCH_TEXT : Color.MAGENTA;
          } else {
            return selected.get() ? Color.WHITE : Color.BLACK;
          }
        }
      };
      conf.add(Synchronizers.forPropsOneWay(textColor, myText.textColor()));

      conf.add(Synchronizers.forPropsOneWay(new DerivedProperty<String>() {
        @Override
        public String doGet() {
          return getSource().visibleText(text.get());
        }

        @Override
        public String getPropExpr() {
          return "visibleText(" + getSource() + ")";
        }
      }, myText.text()));

      conf.add(Synchronizers.forPropsOneWay(selected, new WritableProperty<Boolean>() {
            @Override
            public void set(Boolean value) {
              if (value == null) {
                value = Boolean.FALSE;
              }
              getTarget().background().set(value ? SELECTED_BACKGROUND : null);
              if (value && getTarget().isAttached()) {
                getTarget().scrollTo(new Rectangle(0, 0, 1, getTarget().dimension().y));
              }
            }
          }
      ));
    }
  }
}