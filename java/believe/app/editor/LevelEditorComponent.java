package believe.app.editor;

import believe.app.ApplicationComponent;
import dagger.Component;
import javax.inject.Singleton;

/** Top-level component for the Believe level editor. */
@Singleton
@Component(modules = LevelEditorModule.class)
interface LevelEditorComponent extends ApplicationComponent {}
