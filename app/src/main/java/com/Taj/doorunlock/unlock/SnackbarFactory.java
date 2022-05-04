package com.taj.doorunlock.unlock;

import android.view.View;

import com.assaabloy.mobilekeys.api.MobileKeysApiErrorCode;
import com.assaabloy.mobilekeys.api.MobileKeysException;
import com.taj.doorunlock.R;
import com.google.android.material.snackbar.Snackbar;


/**
 * Snackbar factory
 */
public class SnackbarFactory
{

    private final View view;
    private Snackbar mSnackbar;

    public SnackbarFactory(View view)
    {
        this.view = view;
    }

    /**
     * Create snackbar from error code
     *
     * @param exception
     * @param retryActionListener
     * @return snackbar that describes the error code, null if no description is present
     */
    private Snackbar create(MobileKeysException exception, View.OnClickListener retryActionListener)
    {
        int resId = R.string.error_generic_error;
        if (exception != null && exception.getErrorCode() != null)
        {
            MobileKeysApiErrorCode mobileKeysErrorCode = exception.getErrorCode();
            switch (mobileKeysErrorCode)
            {
                case INVALID_INVITATION_CODE:
                    resId = R.string.error_setup_failed_retry;
                    break;
                case DEVICE_SETUP_FAILED:
                    resId = R.string.error_device_setup_failed;
                    break;
                case SERVER_UNREACHABLE:
                    resId = R.string.error_server_communication_failed;
                    break;
                case SDK_INCOMPATIBLE:
                    resId = R.string.error_device_api_incompatible;
                    break;
                case DEVICE_NOT_ELIGIBLE:
                    resId = R.string.error_device_not_eligible;
                    break;
                case SDK_BUSY:
                    resId = R.string.error_api_is_busy;
                    break;
                case ENDPOINT_NOT_SETUP:
                    resId = R.string.error_endpoint_not_personalize;
                    break;
                case INTERNAL_ERROR:
                    resId = R.string.error_internal_error;
                    break;
            }
        }
        Snackbar snackbar = Snackbar.make(view, resId, Snackbar.LENGTH_INDEFINITE);
        if (retryActionListener != null)
        {
            snackbar.setAction(R.string.action_retry, retryActionListener);
        }
        else
        {
            snackbar.setAction(R.string.action_dismiss, new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // empty action
                }
            });
        }
        return snackbar;
    }

    /**
     * Create and show snackbar with exception, dismiss latest
     *
     * @param exception           to present
     * @param retryActionListener action listener
     */
    public void createAndShow(MobileKeysException exception, View.OnClickListener retryActionListener)
    {
        Snackbar snackbar = create(exception, retryActionListener);
        showAndDismissSnackbar(snackbar);
    }

    public void createAndShow(int resId)
    {
        Snackbar snackbar = Snackbar.make(view, resId, Snackbar.LENGTH_SHORT);
        showAndDismissSnackbar(snackbar);
    }

    private void showAndDismissSnackbar(Snackbar newSnack)
    {
        Snackbar oldSnack = mSnackbar;
        if (oldSnack != null && oldSnack.isShownOrQueued())
        {
            oldSnack.dismiss();
        }
        mSnackbar = newSnack;
        newSnack.show();
    }
}
