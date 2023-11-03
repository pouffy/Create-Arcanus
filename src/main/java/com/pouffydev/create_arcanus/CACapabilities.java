package com.pouffydev.create_arcanus;

import com.stal111.forbidden_arcanus.common.aureal.capability.IAureal;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CACapabilities {
    public static final Capability<IAureal> aureal = CapabilityManager.get(new CapabilityToken<>(){});
}
