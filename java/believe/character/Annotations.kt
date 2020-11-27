package believe.character

import javax.inject.Qualifier

/**
 * Annotates a long value indicating the length of time the player remains invulnerable after being
 * hit.
 */
@Qualifier
annotation class InvulnerabilityLength

/**
 * Annotates a long value indicating the length of time that an animation remains in the visible or
 * invisible states when flashing due to the player taking damage.
 */
@Qualifier
annotation class AnimationFlashLength

/**
 * Annotates a float value indicating the maximum amount of focus the player is allowed to store.
 */
@Qualifier
annotation class MaxFocus

/**
 * Annotates a long value indicating the amount of time, in milliseconds, it takes for the player's
 * focus to recharge from zero to max.
 */
@Qualifier
annotation class FocusRechargeTime
