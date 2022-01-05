package com.antsol.op.gui.asset;

import com.antsol.op.asset.Asset;
import com.antsol.op.gui.common.AbstractCreateController;
import com.antsol.op.market.Market;
import lombok.Setter;

public abstract class CreateAssetController<A extends Asset> extends AbstractCreateController<A> {

    @Setter
    protected Market<A> market;

}
