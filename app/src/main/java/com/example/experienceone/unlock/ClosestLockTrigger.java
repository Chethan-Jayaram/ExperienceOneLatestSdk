package com.example.experienceone.unlock;

import android.util.Log;

import com.assaabloy.mobilekeys.api.ble.ManualOpeningTrigger;
import com.assaabloy.mobilekeys.api.ble.OpeningStatus;
import com.assaabloy.mobilekeys.api.ble.OpeningTriggerAction;
import com.assaabloy.mobilekeys.api.ble.OpeningType;
import com.assaabloy.mobilekeys.api.ble.Reader;

/**
 * Custom opening trigger used to open closest reader.
 * Only use custom opening trigger if you are to implement your own trigger, otherwise use the standard triggers
 * available in the SDK.
 */
public class ClosestLockTrigger extends ManualOpeningTrigger
{
    /**
     * Callback interface
     */
    public interface LockInRangeListener
    {
        /**
         * On lock in range callback
         *
         * @param lockInRange
         */
        void onLockInRange(boolean lockInRange);
    }


    private static final String TAG = ClosestLockTrigger.class.getName();
    private final LockInRangeListener listener;

    public ClosestLockTrigger(LockInRangeListener listener)
    {
        this.listener = listener;
    }

    @Override
    public OpeningTriggerAction onScanReceived(Reader reader)
    {
        if (reader.isInMotionRange())
        {
            updateLockInRangeStatus(true);
        }
        return OpeningTriggerAction.noOpening();
    }

    @Override
    public void onNoReadersInRange()
    {
        super.onNoReadersInRange();
        updateLockInRangeStatus(false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        updateLockInRangeStatus(getBleScanner().listReaders().isEmpty());
    }

    @Override
    public void onStop()
    {
        super.onStop();
        updateLockInRangeStatus(false);
    }

    /**
     * Open closest reader
     */
    public void openClosestReader()
    {
        Reader closestReader = getClosestReader();
        if (validReader(closestReader))
        {
            Log.d(TAG, "Open reader");
            openReader(closestReader, OpeningType.APPLICATION_SPECIFIC);
        }
    }

    /**
     * Verify that the reader is valid
     *
     * @param bleReader
     * @return
     */
    private boolean validReader(Reader bleReader)
    {

        if (bleReader == null || !bleReader.isInMotionRange())
        {
            Log.d(TAG, "Reader out of range");
            connectionListener().onReaderConnectionFailed(
                    null, OpeningType.APPLICATION_SPECIFIC, OpeningStatus.OUT_OF_RANGE);
            return false;
        }

        return true;
    }

    /**
     * Update lock in range status
     *
     * @param lockInRange
     */
    private void updateLockInRangeStatus(boolean lockInRange)
    {
        listener.onLockInRange(lockInRange);
    }
}
