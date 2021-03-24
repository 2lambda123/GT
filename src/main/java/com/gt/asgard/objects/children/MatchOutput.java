package com.gt.asgard.objects.children;

import com.gt.common.view.OrderView;

public class MatchOutput {
    public boolean fullyMatch;
    public OrderView modifiedOrder;

    public MatchOutput(boolean fullyMatch, OrderView modifiedOrder) {
        this.fullyMatch = fullyMatch;
        this.modifiedOrder = modifiedOrder;
    }

    public boolean isFullyMatch() {
        return fullyMatch;
    }

    public void setFullyMatch(boolean fullyMatch) {
        this.fullyMatch = fullyMatch;
    }

    public OrderView getModifiedOrder() {
        return modifiedOrder;
    }

    public void setModifiedOrder(OrderView modifiedOrder) {
        this.modifiedOrder = modifiedOrder;
    }
}
