package believe.gui

import javax.inject.Qualifier

/** Annotates a set of configuration objects used in initializing GUI components. */
@Qualifier
annotation class GuiConfigurations

/** Annotates the sound played when a menu selection is selected. */
@Qualifier
annotation class SelectedSound

/** Annotates the sound played when a menu selection is focused. */
@Qualifier
annotation class FocusSound

/** Annotates the sound played when a number selection arrow is pressed. */
@Qualifier
annotation class ArrowPressedSound

/** Annotates the sound played when a number selection arrow is depressed. */
@Qualifier
annotation class ArrowDepressedSound
