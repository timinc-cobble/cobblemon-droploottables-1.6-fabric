package us.timinc.mc.cobblemon.droploottables.mixins.counter;

@org.spongepowered.asm.mixin.Mixin(us.timinc.mc.cobblemon.counter.CounterMod.class)
public class CounterPresent {
    @org.spongepowered.asm.mixin.injection.Inject(method = "onInitialize", at = @org.spongepowered.asm.mixin.injection.At("HEAD"), remap = false)
    void initializeMixin(org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        us.timinc.mc.cobblemon.droploottables.DropLootTables.INSTANCE.initializeCounter();
    }
}
