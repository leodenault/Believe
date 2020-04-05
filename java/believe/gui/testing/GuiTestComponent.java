package believe.gui.testing;

import believe.gui.GuiLayoutFactory;
import dagger.BindsInstance;
import dagger.Component;
import javax.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

@Component(modules = GuiTestDaggerModule.class)
public interface GuiTestComponent {
  GuiLayoutFactory getGuiLayoutFactory();

  @Component.Builder
  interface Builder {
    @BindsInstance
    Builder guiConfigurations(@Nullable @TestGuiConfigurations Set<?> configurations);

    default TestGuiConfigurationsBuilder addGuiConfiguration(Object guiConfiguration) {
      return new TestGuiConfigurationsBuilder(this).addGuiConfiguration(guiConfiguration);
    }

    GuiTestComponent build();
  }

  class TestGuiConfigurationsBuilder {
    private final GuiTestComponent.Builder guiTestComponentBuilder;
    private final Set<Object> configurations = new HashSet<>();

    TestGuiConfigurationsBuilder(GuiTestComponent.Builder guiTestComponentBuilder) {
      this.guiTestComponentBuilder = guiTestComponentBuilder;
    }

    public TestGuiConfigurationsBuilder addGuiConfiguration(Object guiConfiguration) {
      configurations.add(guiConfiguration);
      return this;
    }

    public GuiTestComponent build() {
      return guiTestComponentBuilder.guiConfigurations(configurations).build();
    }
  }
}
