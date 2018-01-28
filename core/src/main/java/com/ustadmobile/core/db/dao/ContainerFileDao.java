package com.ustadmobile.core.db.dao;

import com.ustadmobile.lib.db.entities.ContainerFile;
import com.ustadmobile.lib.db.entities.ContainerFileWithRelations;

/**
 * Created by mike on 1/27/18.
 */

public abstract class ContainerFileDao {

    public abstract ContainerFileWithRelations findContainerFileByPath(String path);

    public abstract long insert(ContainerFile containerFile);

    public abstract void updateLastUpdatedById(int id, long lastUpdated);


}