package com.pouffy.create_arcanus.registry;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pouffy.create_arcanus.CreateArcanus;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.core.Vec3i;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class AllSoundEvents {
    public static final Map<ResourceLocation, SoundEntry> entries = new HashMap();
    public static final AllSoundEvents.SoundEntry CONVERSION_COMPLETE;
    public static final AllSoundEvents.SoundEntry CONVERTING;
    public AllSoundEvents() {
    }

    private static SoundEntryBuilder create(String name) {
        return create(CreateArcanus.asResource(name));
    }

    public static SoundEntryBuilder create(ResourceLocation id) {
        return new SoundEntryBuilder(id);
    }

    public static void register(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        Iterator var2 = entries.values().iterator();

        while(var2.hasNext()) {
            SoundEntry entry = (SoundEntry)var2.next();
            entry.register(registry);
        }

    }

    public static void prepare() {
        Iterator var0 = entries.values().iterator();

        while(var0.hasNext()) {
            SoundEntry entry = (SoundEntry)var0.next();
            entry.prepare();
        }

    }

    public static JsonObject provideLangEntries() {
        JsonObject object = new JsonObject();
        Iterator var1 = entries.values().iterator();

        while(var1.hasNext()) {
            SoundEntry entry = (SoundEntry)var1.next();
            if (entry.hasSubtitle()) {
                object.addProperty(entry.getSubtitleKey(), entry.getSubtitle());
            }
        }

        return object;
    }

    public static SoundEntryProvider provider(DataGenerator generator) {
        return new SoundEntryProvider(generator);
    }

    public static void playItemPickup(Player player) {
        player.level.playSound((Player)null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F + Create.RANDOM.nextFloat());
    }
static{
    CONVERTING = create("converting").subtitle("Conversion noises").playExisting(SoundEvents.AMETHYST_BLOCK_HIT, 0.125F, 0.5F).playExisting(SoundEvents.AMETHYST_BLOCK_BREAK, 0.125F, 0.5F).category(SoundSource.BLOCKS).build();
    CONVERSION_COMPLETE = create("conversion_complete").subtitle("Conversion completes").playExisting(SoundEvents.BEACON_POWER_SELECT, 1.0F, 0.7F).category(SoundSource.BLOCKS).build();


}


    public static class SoundEntryBuilder {
        protected ResourceLocation id;
        protected String subtitle = "unregistered";
        protected SoundSource category;
        protected List<Pair<SoundEvent, Couple<Float>>> wrappedEvents;
        protected List<ResourceLocation> variants;
        protected int attenuationDistance;

        public SoundEntryBuilder(ResourceLocation id) {
            this.category = SoundSource.BLOCKS;
            this.wrappedEvents = new ArrayList();
            this.variants = new ArrayList();
            this.id = id;
        }

        public SoundEntryBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public SoundEntryBuilder attenuationDistance(int distance) {
            this.attenuationDistance = distance;
            return this;
        }

        public SoundEntryBuilder noSubtitle() {
            this.subtitle = null;
            return this;
        }

        public SoundEntryBuilder category(SoundSource category) {
            this.category = category;
            return this;
        }

        public SoundEntryBuilder addVariant(String name) {
            return this.addVariant(Create.asResource(name));
        }

        public SoundEntryBuilder addVariant(ResourceLocation id) {
            this.variants.add(id);
            return this;
        }

        public SoundEntryBuilder playExisting(SoundEvent event, float volume, float pitch) {
            this.wrappedEvents.add(Pair.of(event, Couple.create(volume, pitch)));
            return this;
        }

        public SoundEntryBuilder playExisting(SoundEvent event) {
            return this.playExisting(event, 1.0F, 1.0F);
        }

        public SoundEntry build() {
            SoundEntry entry = this.wrappedEvents.isEmpty() ? new AllSoundEvents.CustomSoundEntry(this.id, this.variants, this.subtitle, this.category, this.attenuationDistance) : new WrappedSoundEntry(this.id, this.subtitle, this.wrappedEvents, this.category, this.attenuationDistance);
            entries.put(((SoundEntry)entry).getId(), entry);
            return (SoundEntry)entry;
        }
    }

    public abstract static class SoundEntry {
        protected ResourceLocation id;
        protected String subtitle;
        protected SoundSource category;
        protected int attenuationDistance;

        public SoundEntry(ResourceLocation id, String subtitle, SoundSource category, int attenuationDistance) {
            this.id = id;
            this.subtitle = subtitle;
            this.category = category;
            this.attenuationDistance = attenuationDistance;
        }

        public abstract void prepare();

        public abstract void register(IForgeRegistry<SoundEvent> var1);

        public abstract void write(JsonObject var1);

        public abstract SoundEvent getMainEvent();

        public String getSubtitleKey() {
            String var10000 = this.id.getNamespace();
            return var10000 + ".subtitle." + this.id.getPath();
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public boolean hasSubtitle() {
            return this.subtitle != null;
        }

        public String getSubtitle() {
            return this.subtitle;
        }

        public void playOnServer(Level world, Vec3i pos) {
            this.playOnServer(world, pos, 1.0F, 1.0F);
        }

        public void playOnServer(Level world, Vec3i pos, float volume, float pitch) {
            this.play(world, (Player)null, (Vec3i)pos, volume, pitch);
        }

        public void play(Level world, Player entity, Vec3i pos) {
            this.play(world, entity, pos, 1.0F, 1.0F);
        }

        public void playFrom(Entity entity) {
            this.playFrom(entity, 1.0F, 1.0F);
        }

        public void playFrom(Entity entity, float volume, float pitch) {
            if (!entity.isSilent()) {
                this.play(entity.level, (Player)null, (Vec3i)entity.blockPosition(), volume, pitch);
            }

        }

        public void play(Level world, Player entity, Vec3i pos, float volume, float pitch) {
            this.play(world, entity, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, volume, pitch);
        }

        public void play(Level world, Player entity, Vec3 pos, float volume, float pitch) {
            this.play(world, entity, pos.x(), pos.y(), pos.z(), volume, pitch);
        }

        public abstract void play(Level var1, Player var2, double var3, double var5, double var7, float var9, float var10);

        public void playAt(Level world, Vec3i pos, float volume, float pitch, boolean fade) {
            this.playAt(world, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, volume, pitch, fade);
        }

        public void playAt(Level world, Vec3 pos, float volume, float pitch, boolean fade) {
            this.playAt(world, pos.x(), pos.y(), pos.z(), volume, pitch, fade);
        }

        public abstract void playAt(Level var1, double var2, double var4, double var6, float var8, float var9, boolean var10);
    }

    private static class SoundEntryProvider implements DataProvider {
        private DataGenerator generator;

        public SoundEntryProvider(DataGenerator generator) {
            this.generator = generator;
        }

        public void run(HashCache cache) throws IOException {
            this.generate(this.generator.getOutputFolder(), cache);
        }

        public String getName() {
            return "Create's Custom Sounds";
        }

        public void generate(Path path, HashCache cache) {
            Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
            path = path.resolve("assets/create");

            try {
                JsonObject json = new JsonObject();
                entries.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach((entry) -> {
                    ((SoundEntry)entry.getValue()).write(json);
                });
                DataProvider.save(GSON, cache, json, path.resolve("sounds.json"));
            } catch (IOException var5) {
                var5.printStackTrace();
            }

        }
    }

    private static class CustomSoundEntry extends SoundEntry {
        protected List<ResourceLocation> variants;
        protected SoundEvent event;

        public CustomSoundEntry(ResourceLocation id, List<ResourceLocation> variants, String subtitle, SoundSource category, int attenuationDistance) {
            super(id, subtitle, category, attenuationDistance);
            this.variants = variants;
        }

        public void prepare() {
            this.event = (SoundEvent)(new SoundEvent(this.id)).setRegistryName(this.id);
        }

        public void register(IForgeRegistry<SoundEvent> registry) {
            registry.register(this.event);
        }

        public SoundEvent getMainEvent() {
            return this.event;
        }

        public void write(JsonObject json) {
            JsonObject entry = new JsonObject();
            JsonArray list = new JsonArray();
            JsonObject s = new JsonObject();
            s.addProperty("name", this.id.toString());
            s.addProperty("type", "file");
            if (this.attenuationDistance != 0) {
                s.addProperty("attenuation_distance", this.attenuationDistance);
            }

            list.add(s);

            for(Iterator var5 = this.variants.iterator(); var5.hasNext(); list.add(s)) {
                ResourceLocation variant = (ResourceLocation)var5.next();
                s = new JsonObject();
                s.addProperty("name", variant.toString());
                s.addProperty("type", "file");
                if (this.attenuationDistance != 0) {
                    s.addProperty("attenuation_distance", this.attenuationDistance);
                }
            }

            entry.add("sounds", list);
            if (this.hasSubtitle()) {
                entry.addProperty("subtitle", this.getSubtitleKey());
            }

            json.add(this.id.getPath(), entry);
        }

        public void play(Level world, Player entity, double x, double y, double z, float volume, float pitch) {
            world.playSound(entity, x, y, z, this.event, this.category, volume, pitch);
        }

        public void playAt(Level world, double x, double y, double z, float volume, float pitch, boolean fade) {
            world.playLocalSound(x, y, z, this.event, this.category, volume, pitch, fade);
        }
    }

    private static class WrappedSoundEntry extends SoundEntry {
        private List<Pair<SoundEvent, Couple<Float>>> wrappedEvents;
        private List<Pair<SoundEvent, Couple<Float>>> compiledEvents;

        public WrappedSoundEntry(ResourceLocation id, String subtitle, List<Pair<SoundEvent, Couple<Float>>> wrappedEvents, SoundSource category, int attenuationDistance) {
            super(id, subtitle, category, attenuationDistance);
            this.wrappedEvents = wrappedEvents;
            this.compiledEvents = Lists.newArrayList();
        }

        public void prepare() {
            for(int i = 0; i < this.wrappedEvents.size(); ++i) {
                ResourceLocation location = CreateArcanus.asResource(this.getIdOf(i));
                SoundEvent sound = (SoundEvent)(new SoundEvent(location)).setRegistryName(location);
                this.compiledEvents.add(Pair.of(sound, (Couple)((Pair)this.wrappedEvents.get(i)).getSecond()));
            }

        }

        public void register(IForgeRegistry<SoundEvent> registry) {
            Iterator var2 = this.compiledEvents.iterator();

            while(var2.hasNext()) {
                Pair<SoundEvent, Couple<Float>> pair = (Pair)var2.next();
                registry.register((SoundEvent)pair.getFirst());
            }

        }

        public SoundEvent getMainEvent() {
            return (SoundEvent)((Pair)this.compiledEvents.get(0)).getFirst();
        }

        protected String getIdOf(int i) {
            return i == 0 ? this.id.getPath() : this.id.getPath() + "_compounded_" + i;
        }

        public void write(JsonObject json) {
            for(int i = 0; i < this.wrappedEvents.size(); ++i) {
                Pair<SoundEvent, Couple<Float>> pair = (Pair)this.wrappedEvents.get(i);
                JsonObject entry = new JsonObject();
                JsonArray list = new JsonArray();
                JsonObject s = new JsonObject();
                s.addProperty("name", ((SoundEvent)pair.getFirst()).getLocation().toString());
                s.addProperty("type", "event");
                if (this.attenuationDistance != 0) {
                    s.addProperty("attenuation_distance", this.attenuationDistance);
                }

                list.add(s);
                entry.add("sounds", list);
                if (i == 0 && this.hasSubtitle()) {
                    entry.addProperty("subtitle", this.getSubtitleKey());
                }

                json.add(this.getIdOf(i), entry);
            }

        }

        public void play(Level world, Player entity, double x, double y, double z, float volume, float pitch) {
            Iterator var11 = this.compiledEvents.iterator();

            while(var11.hasNext()) {
                Pair<SoundEvent, Couple<Float>> pair = (Pair)var11.next();
                Couple<Float> volPitch = (Couple)pair.getSecond();
                world.playSound(entity, x, y, z, (SoundEvent)pair.getFirst(), this.category, (Float)volPitch.getFirst() * volume, (Float)volPitch.getSecond() * pitch);
            }

        }

        public void playAt(Level world, double x, double y, double z, float volume, float pitch, boolean fade) {
            Iterator var11 = this.compiledEvents.iterator();

            while(var11.hasNext()) {
                Pair<SoundEvent, Couple<Float>> pair = (Pair)var11.next();
                Couple<Float> volPitch = (Couple)pair.getSecond();
                world.playLocalSound(x, y, z, (SoundEvent)pair.getFirst(), this.category, (Float)volPitch.getFirst() * volume, (Float)volPitch.getSecond() * pitch, fade);
            }

        }
    }
}
