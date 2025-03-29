package us.timinc.mc.cobblemon.droploottables.droppers

object Droppers {
    fun load() {
        CaptureDropper.load()
        KoDropper.load()
        ReleaseDropper.load()
        FossilRevivedDropper.load()
        EvolutionDropper.load()
        PeriodicDropper.load()
        EggHatchingDropper.load()
        StarterChosenDropper.load()
        ScannedDropper.load()
        CombatVictoryDropper.load()
    }
}