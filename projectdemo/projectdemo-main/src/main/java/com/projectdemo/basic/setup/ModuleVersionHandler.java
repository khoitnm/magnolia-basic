package com.projectdemo.basic.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.Delta;
import info.magnolia.module.delta.ModuleBootstrapTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 * ModuleVersionHandler is where you will bootstrap general system configuration, order filters,
 * do security configuration or something else regarding system and version of the project.
 *
 * @author dat.dang
 */
public class ModuleVersionHandler extends DefaultModuleVersionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleVersionHandler.class);
    private final Task _bootstrapModuleConfigTask = new ModuleBootstrapTask();

    @Override
    protected List<Delta> getUpdateDeltas(final InstallContext installContext, final Version from) {
        List<Delta> updateDeltas = super.getUpdateDeltas(installContext, from);
        final Version toVersion = installContext.getCurrentModuleDefinition().getVersion();

        if (hasModuleNewRevision(from, toVersion)) {
            updateDeltas.add(getDefaultUpdate(installContext));
        }

        return updateDeltas;
    }

    /**
     * Compares the versions and the revision classifier. If the version numbers
     * are equal, a different classifier should trigger a module update.
     */
    private static boolean hasModuleNewRevision(final Version fromVersion, final Version toVersion) {
        boolean triggerUpdate = false;
        if (toVersion.isEquivalent(fromVersion)) {
            String toClassifier = toVersion.getClassifier();
            String fromClassifier = fromVersion.getClassifier();
            if (toClassifier == null && fromClassifier != null) {
                LOGGER.debug("A released version was found. Trigger module update.");
                triggerUpdate = true;
            } else if (fromClassifier != null && !fromClassifier.equals(toClassifier)) {
                LOGGER.debug("A new classifier version was found. Trigger module update.");
                triggerUpdate = true;
            }
        }
        return triggerUpdate;
    }
}