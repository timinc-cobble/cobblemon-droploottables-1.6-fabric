package us.timinc.mc.cobblemon.droploottables.droppers

object Droppers {
    fun load() {
        CaptureDropper.load()
        KoDropper.load()
        ReleaseDropper.load()
    }
}