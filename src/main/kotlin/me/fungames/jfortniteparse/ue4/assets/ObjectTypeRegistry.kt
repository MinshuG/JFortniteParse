package me.fungames.jfortniteparse.ue4.assets

import me.fungames.jfortniteparse.fort.exports.*
import me.fungames.jfortniteparse.fort.exports.variants.*
import me.fungames.jfortniteparse.fort.objects.rows.*
import me.fungames.jfortniteparse.ue4.UClass
import me.fungames.jfortniteparse.ue4.assets.exports.*
import me.fungames.jfortniteparse.ue4.assets.exports.mats.UMaterial
import me.fungames.jfortniteparse.ue4.assets.exports.mats.UMaterialInstance
import me.fungames.jfortniteparse.ue4.assets.exports.mats.UMaterialInstanceConstant
import me.fungames.jfortniteparse.ue4.assets.exports.mats.UMaterialInterface
import me.fungames.jfortniteparse.ue4.assets.exports.tex.UTexture
import me.fungames.jfortniteparse.ue4.assets.exports.tex.UTexture2D
import java.util.concurrent.ConcurrentHashMap

object ObjectTypeRegistry {
    val classes = ConcurrentHashMap<String, Class<out UObject>>()
    val structs = ConcurrentHashMap<String, Class<*>>()

    init {
        registerEngine()
        registerFortnite()
    }

    private inline fun registerEngine() {
        registerClass(UCurveTable::class.java)
        registerClass(UDataAsset::class.java)
        registerClass(UDataTable::class.java)
        registerClass(ULevel::class.java)
        registerClass(UMaterial::class.java)
        registerClass(UMaterialInstance::class.java)
        registerClass(UMaterialInstanceConstant::class.java)
        registerClass(UMaterialInterface::class.java)
        registerClass(UPaperSprite::class.java)
        registerClass(UPrimaryDataAsset::class.java)
        registerClass(UUserDefinedEnum::class.java)
        registerClass(USoundWave::class.java)
        registerClass(UStaticMesh::class.java)
        registerClass(UStreamableRenderAsset::class.java)
        registerClass(UStringTable::class.java)
        registerClass(UTexture2D::class.java)
        registerClass(UTexture::class.java)
    }

    private inline fun registerFortnite() {
        // -- Export classes --
        registerClass(AthenaBackpackItemDefinition::class.java)
        registerClass(AthenaChallengeDisplayData::class.java)
        registerClass(AthenaCharacterItemDefinition::class.java)
        registerClass(AthenaCharacterPartItemDefinition::class.java)
        registerClass(AthenaCosmeticItemDefinition::class.java)
        registerClass(AthenaDailyQuestDefinition::class.java)
        registerClass(AthenaDanceItemDefinition::class.java)
        registerClass(AthenaEmojiItemDefinition::class.java)
        registerClass(AthenaGadgetItemDefinition::class.java)
        registerClass(AthenaGliderItemDefinition::class.java)
        registerClass(AthenaItemWrapDefinition::class.java)
        registerClass(AthenaLoadingScreenItemDefinition::class.java)
        registerClass(AthenaMusicPackItemDefinition::class.java)
        registerClass(AthenaPetCarrierItemDefinition::class.java)
        registerClass(AthenaPickaxeItemDefinition::class.java)
        registerClass(AthenaRewardEventGraph::class.java)
        registerClass(AthenaSeasonItemDefinition::class.java)
        registerClass(AthenaSkyDiveContrailItemDefinition::class.java)
        registerClass(AthenaSprayItemDefinition::class.java)
        registerClass(AthenaToyItemDefinition::class.java)
        registerClass(FortAbilityKit::class.java)
        registerClass(FortAbilitySet::class.java)
        registerClass(FortAccoladeItemDefinition::class.java)
        registerClass(FortAccountItemDefinition::class.java)
        registerClass(FortAthenaRewardEventGraphPurchaseToken::class.java)
        registerClass(FortBannerTokenType::class.java)
        registerClass(FortBuildingItemDefinition::class.java)
        registerClass(FortCampaignHeroLoadoutItemDefinition::class.java)
        registerClass(FortCardPackItemDefinition::class.java)
        registerClass(FortCatalogMessaging::class.java)
        registerClass(FortChallengeBundleItemDefinition::class.java)
        registerClass(FortChallengeBundleProgressTrackerToken::class.java)
        registerClass(FortChallengeBundleScheduleDefinition::class.java)
        registerClass(FortCharacterType::class.java)
        registerClass(FortCollectionBookData::class.java)
        registerClass(FortConditionalResourceItemDefinition::class.java)
        registerClass(FortConsumableAccountItemDefinition::class.java)
        registerClass(FortCosmeticCharacterPartVariant::class.java)
        registerClass(FortCosmeticDynamicVariant::class.java)
        registerClass(FortCosmeticFloatSliderVariant::class.java)
        registerClass(FortCosmeticItemTexture::class.java)
        registerClass(FortCosmeticLockerItemDefinition::class.java)
        registerClass(FortCosmeticMaterialVariant::class.java)
        registerClass(FortCosmeticMeshVariant::class.java)
        registerClass(FortCosmeticNumericalVariant::class.java)
        registerClass(FortCosmeticParticleVariant::class.java)
        registerClass(FortCosmeticProfileBannerVariant::class.java)
        registerClass(FortCosmeticProfileLoadoutVariant::class.java)
        registerClass(FortCosmeticRichColorVariant::class.java)
        registerClass(FortCosmeticVariant::class.java)
        registerClass(FortCosmeticVariantBackedByArray::class.java)
        registerClass(FortCurrencyItemDefinition::class.java)
        registerClass(FortDailyRewardScheduleDefinitions::class.java)
        registerClass(FortDefenderItemDefinition::class.java)
        registerClass(FortEditToolItemDefinition::class.java)
        registerClass(FortEventCurrencyItemDefinitionRedir::class.java)
        registerClass(FortExpeditionItemDefinition::class.java)
        registerClass(FortFeatItemDefinition::class.java)
        registerClass(FortGadgetItemDefinition::class.java)
        registerClass(FortHeroType::class.java)
        registerClass(FortHomebaseBannerColorMap::class.java)
        registerClass(FortHomebaseBannerIconItemDefinition::class.java)
        registerClass(FortHomebaseNodeItemDefinition::class.java)
        registerClass(FortIngredientItemDefinition::class.java)
        registerClass(FortItemAccessTokenType::class.java)
        registerClass(FortItemCategory::class.java)
        registerClass(FortItemDefinition::class.java)
        registerClass(FortItemSeriesDefinition::class.java)
        registerClass(FortMontageItemDefinitionBase::class.java)
        registerClass(FortMtxOfferData::class.java)
        registerClass(FortPersistableItemDefinition::class.java)
        registerClass(FortPersistentResourceItemDefinition::class.java)
        registerClass(FortPersonalVehicleItemDefinition::class.java)
        registerClass(FortPlaysetGrenadeItemDefinition::class.java)
        registerClass(FortPrerollDataItemDefinition::class.java)
        registerClass(FortProfileItemDefinition::class.java)
        registerClass(FortQuestItemDefinition::class.java)
        registerClass(FortQuotaItemDefinition::class.java)
        registerClass(FortRepeatableDailiesCardItemDefinition::class.java)
        registerClass(FortSchematicItemDefinition::class.java)
        registerClass(FortStatItemDefinition::class.java)
        registerClass(FortTeamPerkItemDefinition::class.java)
        registerClass(FortTokenType::class.java)
        registerClass(FortVariantTokenType::class.java)
        registerClass(FortWeaponAdditionalData_AudioVisualizerData::class.java)
        registerClass(FortWeaponAdditionalData_SingleWieldState::class.java)
        registerClass(FortWeaponItemDefinition::class.java)
        registerClass(FortWeaponMeleeDualWieldItemDefinition::class.java)
        registerClass(FortWeaponMeleeItemDefinition::class.java)
        registerClass(FortWeaponRangedItemDefinition::class.java)
        registerClass(FortWorkerType::class.java)
        registerClass(FortWorldItemDefinition::class.java)
        registerClass(MarshalledVFX_AuthoredDataConfig::class.java)
        registerClass(McpItemDefinitionBase::class.java)
        registerClass(RewardGraphToken::class.java)
        registerClass(VariantTypeBase::class.java)
        registerClass(VariantTypeMaterials::class.java)
        registerClass(VariantTypeParticles::class.java)
        registerClass(VariantTypeSounds::class.java)

        // -- Data table row structs --
        registerStruct(AlterationGroup::class.java)
        registerStruct(AlterationIntrinsicMapping::class.java)
        registerStruct(AlterationMapping::class.java)
        registerStruct(AlterationNamedExclusions::class.java)
        registerStruct(AlterationSlotDefinition::class.java)
        registerStruct(AlterationSlotsLoadout::class.java)
        registerStruct(AthenaExtendedXPCurveEntry::class.java)
        registerStruct(AthenaSeasonalXPCurveEntry::class.java)
        registerStruct(CosmeticFilterTagDataRow::class.java)
        registerStruct(CosmeticMarkupTagDataRow::class.java)
        registerStruct(CosmeticSetDataRow::class.java)
        registerStruct(FortBaseWeaponStats::class.java)
        registerStruct(FortCategoryTableRow::class.java)
        registerStruct(FortCollectionBookPageCategoryTableRow::class.java)
        registerStruct(FortCollectionBookPageData::class.java)
        registerStruct(FortCollectionBookSectionData::class.java)
        registerStruct(FortCollectionBookSlotData::class.java)
        registerStruct(FortCollectionBookXPData::class.java)
        registerStruct(FortCriteriaRequirementData::class.java)
        registerStruct(FortLoginReward::class.java)
        registerStruct(FortPhoenixLevelRewardData::class.java)
        registerStruct(FortPostMaxPhoenixLevelRewardData::class.java)
        registerStruct(FortQuestObjectiveStatTableRow::class.java)
        registerStruct(FortQuestRewardTableRow::class.java)
        registerStruct(FortRangedWeaponStats::class.java)
        registerStruct(FortSquadIconData::class.java)
        registerStruct(FortTrapStats::class.java)
        registerStruct(FortWeaponAlterationRarityMappingData::class.java)
        registerStruct(FortWeaponDurabilityByRarityStats::class.java)
        registerStruct(GameDifficultyInfo::class.java)
        registerStruct(HomebaseBannerCategoryData::class.java)
        registerStruct(HomebaseBannerColorData::class.java)
        registerStruct(HomebaseBannerIconData::class.java)
        registerStruct(HomebaseSquad::class.java)
        registerStruct(Recipe::class.java)
    }

    fun registerClass(clazz: Class<out UObject>) {
        var name = clazz.simpleName
        if (name[0] == 'U' && name[1].isUpperCase()) {
            name = name.substring(1)
        }
        registerClass(name, clazz)
    }

    fun registerClass(serializedName: String, clazz: Class<out UObject>) {
        classes[serializedName] = clazz
    }

    fun registerStruct(clazz: Class<*>) {
        var name = clazz.simpleName
        if (name[0] == 'F' && name[1].isUpperCase()) {
            name = name.substring(1)
        }
        registerStruct(name, clazz)
    }

    fun registerStruct(serializedName: String, clazz: Class<*>) {
        structs[serializedName] = clazz
    }

    fun constructClass(serializedName: String): UObject {
        if (serializedName.startsWith("/Script/") || serializedName.startsWith("Default__")) {
            return UObject().apply { exportType = serializedName }
        }
        var clazz = classes[serializedName]
        if (clazz == null) {
            UClass.logger.warn("Didn't find class $serializedName in registry")
            clazz = UObject::class.java
        }
        return clazz.newInstance().apply {
            readGuid = true
            exportType = serializedName
        }
    }

    fun constructStruct(serializedName: String): Any {
        if (serializedName.startsWith("/Script/") || serializedName.startsWith("Default__")) {
            return Object()
        }
        val clazz = structs[serializedName]
        if (clazz == null) {
            UClass.logger.warn("Didn't find struct $serializedName in registry")
            return Object()
        }
        return clazz.newInstance()
    }
}

fun String.unprefix(): String {
    if ((get(0) == 'U' || get(0) == 'F' || get(0) == 'A') && get(1).isUpperCase()) {
        return substring(1)
    }
    return this
}