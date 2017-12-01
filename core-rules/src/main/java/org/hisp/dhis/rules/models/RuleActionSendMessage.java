package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionSendMessage  extends RuleAction {

    @Nonnull
    public static RuleActionSendMessage create(
            @Nullable String notification) {
        if (notification == null) {
            throw new IllegalArgumentException("notification must not be null");
        }

        return new AutoValue_RuleActionSendMessage(notification == null ? "" : notification);
    }
    public abstract String notification();
}
