package app.trian.rust.data

import app.trian.rust.data.EMOJIS.EMOJI_CLOUDS
import app.trian.rust.data.EMOJIS.EMOJI_FLAMINGO
import app.trian.rust.data.EMOJIS.EMOJI_MELTING
import app.trian.rust.data.EMOJIS.EMOJI_PINK_HEART
import app.trian.rust.data.EMOJIS.EMOJI_POINTS


object EMOJIS {
    // EMOJI 15
    const val EMOJI_PINK_HEART = "\uD83E\uDE77"

    // EMOJI 14 🫠
    const val EMOJI_MELTING = "\uD83E\uDEE0"

    // ANDROID 13.1 😶‍🌫️
    const val EMOJI_CLOUDS = "\uD83D\uDE36\u200D\uD83C\uDF2B️"

    // ANDROID 12.0 🦩
    const val EMOJI_FLAMINGO = "\uD83E\uDDA9"

    // ANDROID 12.0  👉
    const val EMOJI_POINTS = " \uD83D\uDC49"
}

val contentRandom = listOf<String>(
    "Check it out!",
    "Thank you!$EMOJI_PINK_HEART",
    "You can use all the same stuff",
    "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
    "Compose newbie: I’ve scourged the internet for tutorials about async data " +
            "loading but haven’t found any good ones $EMOJI_MELTING $EMOJI_CLOUDS. " +
            "What’s the recommended way to load async data and emit composable widgets?",
    "Compose newbie as well $EMOJI_FLAMINGO, have you looked at the JetNews sample? " +
            "Most blog posts end up out of date pretty fast but this sample is always up to " +
            "date and deals with async data loading (it's faked but the same idea " +
            "applies) $EMOJI_POINTS https://goo.gle/jetnews"
)


