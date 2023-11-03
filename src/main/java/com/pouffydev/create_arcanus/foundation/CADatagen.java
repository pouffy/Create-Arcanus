package com.pouffydev.create_arcanus.foundation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pouffydev.create_arcanus.CreateArcanus;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.simibubi.create.infrastructure.data.CreateRegistrateTags;
import com.simibubi.create.infrastructure.data.GeneratedEntriesProvider;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CADatagen {
    public static void gatherData(GatherDataEvent event) {
        addExtraRegistrateData();
        
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        
        if (event.includeClient()) {
            generator.addProvider(true, new CABlockModelGenerator(generator, existingFileHelper));
        }
        //if (event.includeServer()) {
            //GeneratedEntriesProvider generatedEntriesProvider = new GeneratedEntriesProvider(output, lookupProvider);
            //lookupProvider = generatedEntriesProvider.getRegistryProvider();
            //generator.addProvider(true, generatedEntriesProvider);
            
            //generator.addProvider(true, new CreateRecipeSerializerTagsProvider(output, lookupProvider, existingFileHelper));
            //generator.addProvider(true, new DamageTypeTagGen(output, lookupProvider, existingFileHelper));
            //generator.addProvider(true, new AllAdvancements(output));
            //generator.addProvider(true, new StandardRecipeGen(output));
            //generator.addProvider(true, new MechanicalCraftingRecipeGen(output));
            //generator.addProvider(true, new SequencedAssemblyRecipeGen(output));
            //ProcessingRecipeGen.registerAll(generator, output);
        //}
    }
    private static void addExtraRegistrateData() {
        CreateRegistrateTags.addGenerators();
        CreateArcanus.registrate.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;
            
            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
            //AllAdvancements.provideLang(langConsumer);
            //AllSoundEvents.provideLang(langConsumer);
            //providePonderLang(langConsumer);
        });
    }
    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/create_arcanus/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }
    private static void providePonderLang(BiConsumer<String, String> consumer) {
        // Register these since FMLClientSetupEvent does not run during datagen
        //AllPonderTags.register();
        //PonderIndex.register();
        //
        //SharedText.gatherText();
        //PonderLocalization.generateSceneLang();
        //
        //GeneralText.provideLang(consumer);
        //PonderLocalization.provideLang(Create.ID, consumer);
    }
}
