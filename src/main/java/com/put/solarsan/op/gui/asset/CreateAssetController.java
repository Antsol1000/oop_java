package com.put.solarsan.op.gui.asset;

import com.put.solarsan.op.asset.Asset;
import com.put.solarsan.op.gui.common.AbstractCreateController;
import com.put.solarsan.op.settings.Settings;

public abstract class CreateAssetController<A extends Asset<A>> extends AbstractCreateController<A> {

    protected Settings settings;

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
