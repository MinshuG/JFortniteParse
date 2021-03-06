package me.fungames.jfortniteparse.fort.exports.variants;

import me.fungames.jfortniteparse.fort.objects.variants.BaseVariantDef;
import me.fungames.jfortniteparse.fort.objects.variants.LoadoutVariantDef;

import java.util.List;

public class FortCosmeticProfileLoadoutVariant extends FortCosmeticVariantBackedByArray {
    public List<LoadoutVariantDef> LoadoutAugmentations;

    @Override
    public List<? extends BaseVariantDef> getVariants() {
        return LoadoutAugmentations;
    }
}
