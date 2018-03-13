package com.ustadmobile.port.android.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.ustadmobile.core.db.UmLiveData;
import com.ustadmobile.core.db.dao.DownloadJobDao;
import com.ustadmobile.lib.database.annotation.UmQuery;
import com.ustadmobile.lib.db.entities.DownloadJob;
import com.ustadmobile.lib.db.entities.DownloadJobWithRelations;
import com.ustadmobile.lib.db.entities.DownloadJobWithTotals;

/**
 * Created by mike on 2/2/18.
 */
@Dao
public abstract class DownloadJobDaoAndroid extends DownloadJobDao {

    @Override
    @Insert
    public abstract long insert(DownloadJob job);

    @Override
    @Query("Update DownloadJob SET status = :status, timeRequested = :timeRequested WHERE id = :id")
    public abstract long queueDownload(int id, int status, long timeRequested);

    @Override
    @Query("SELECT * FROM DownloadJob WHERE status > 0 AND status <= 10 ORDER BY timeRequested LIMIT 1")
    protected abstract DownloadJobWithRelations findNextDownloadJob();

    @Override
    @Query("UPDATE DownloadJob SET status = :status WHERE id = :jobId")
    public abstract long updateJobStatus(int jobId, int status);

    @Override
    @Query("UPDATE DownloadJob SET status = :setTo WHERE status BETWEEN :rangeFrom AND :rangeTo")
    public abstract void updateJobStatusByRange(int rangeFrom, int rangeTo, int setTo);

    @Transaction
    public DownloadJobWithRelations findNextDownloadJobAndSetStartingStatus(){
        return super.findNextDownloadJobAndSetStartingStatus();
    }

    @Override
    public UmLiveData<DownloadJobWithRelations> getByIdLive(int id) {
        return new UmLiveDataAndroid<>(getByIdLiveR(id));
    }

    @Override
    @Update
    public abstract void update(DownloadJob job);

    @Query("SELECT * From DownloadJob WHERE id = :id")
    public abstract LiveData<DownloadJobWithRelations> getByIdLiveR(int id);

    @Query("SELECT * FROM DownloadJob WHERE id = :id")
    public abstract DownloadJobWithRelations findById(int id);

    @Override
    @Query("SELECT * FROM DownloadJob ORDER BY timeCreated DESC LIMIT 1")
    public abstract DownloadJobWithRelations findLastCreated();

    @Query("SELECT DownloadJob.*, " +
            " (SELECT COUNT(*) FROM DownloadJobItem WHERE DownloadJobItem.downloadJobId = DownloadJob.id) AS numJobItems, " +
            " (SELECT SUM(DownloadJobItem.downloadLength) FROM DownloadJobItem WHERE DownloadJobItem.downloadJobId = DownloadJob.id) AS totalDownloadSize " +
            " FROM DownloadJob Where DownloadJob.id= :id")
    public abstract LiveData<DownloadJobWithTotals> findByIdWithTotals_Room(int id);

    @Override
    public UmLiveData<DownloadJobWithTotals> findByIdWithTotals(int id) {
        return new UmLiveDataAndroid<>(findByIdWithTotals_Room(id));
    }
}
