package com.taj.doorunlock.activity.loginfragments;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.taj.doorunlock.R;
import com.taj.doorunlock.helper.APIResponse;
import com.taj.doorunlock.helper.FingerprintHandler;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.pojo.GeneralPojo;
import com.taj.doorunlock.services.ApiListener;

import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;
import static com.taj.doorunlock.helper.GlobalClass.edit;
import static com.taj.doorunlock.helper.GlobalClass.sharedPreferences;

public class LoginMpinFragment extends Fragment implements ApiListener, GlobalClass.OnBiometricAuthSucess {


    //Fingerprint auth variables
    private static final String KEY_NAME = "RefinaryBioMetric";
    private Cipher mCipher;
    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintManager mFingerprintManager;
    private KeyguardManager mKeyguardManager;
    private CancellationSignal mCancellationSignal;


    private EditText et_1, et_2, et_3, et_4;
    private Button tv_forgot_mpin, mBtn_biometric;
    private Context mContext;
    private AlertDialog mBioAlertDialog;

    private ViewGroup mViewGroup;
    private LinearLayout m_lyt;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loginmpin, container, false);
        try {
            mContext = view.getContext();
            et_1 = view.findViewById(R.id.et_1);
            et_2 = view.findViewById(R.id.et_2);
            et_2 = view.findViewById(R.id.et_2);
            et_3 = view.findViewById(R.id.et_3);
            et_4 = view.findViewById(R.id.et_4);
            m_lyt = view.findViewById(R.id.lyt);
            mBtn_biometric = view.findViewById(R.id.btn_biometric);
            tv_forgot_mpin = view.findViewById(R.id.tv_forgot_mpin);

            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
            mViewGroup = view.findViewById(android.R.id.content);
            getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.GONE);
            init();



            mBtn_biometric.setOnClickListener(v -> {
                showBio();
                m_lyt.setAlpha(0.2f);
                initiateFingerprintlistner();
            });

            mBtn_biometric.setVisibility(View.GONE);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            mKeyguardManager = (KeyguardManager) mContext.getSystemService(KEYGUARD_SERVICE);
            mFingerprintManager = (FingerprintManager) mContext.getSystemService(FINGERPRINT_SERVICE);

            if (mFingerprintManager != null) {
                if (mFingerprintManager.isHardwareDetected()) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                        GlobalClass.ShowAlet(mContext, "Alert", "Please enable the fingerprint permission");
                        //enableBioWithoutBiometric();
                    } else if(mFingerprintManager.hasEnrolledFingerprints()) {
                        //  enableBIometric();
                        mBtn_biometric.setVisibility(View.VISIBLE);
                        showBio();
                        m_lyt.setAlpha(0.2f);
                        initiateFingerprintlistner();
                    }
                }
            }
        }

    }

    private void initiateFingerprintlistner() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    generateKey();

                } catch (FingerprintException e) {
                    e.printStackTrace();
                }
                if (initCipher()) {
                    try {
                        mCryptoObject = new FingerprintManager.CryptoObject(mCipher);
                        FingerprintHandler helper = new FingerprintHandler(mContext, this);
                        helper.startAuth(mFingerprintManager, mCryptoObject);
                        mCancellationSignal = new CancellationSignal();
                        mFingerprintManager.authenticate(mCryptoObject, mCancellationSignal, 0, helper, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void init() {

        et_1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_2.requestFocus();
                        }
                    }, GlobalClass.mDelay);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        et_2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (s.length() == 1) {
                            et_3.requestFocus();
                        }
                        if (s.toString().isEmpty()) {
                            et_1.requestFocus();
                        }
                    }
                }, GlobalClass.mDelay);

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        et_3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (s.length() == 1) {
                            et_4.requestFocus();
                        }
                        if (s.toString().isEmpty()) {
                            et_2.requestFocus();
                        }
                    }
                }, GlobalClass.mDelay);


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        et_4.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (s.length() == 1) {
                            et_4.onEditorAction(EditorInfo.IME_ACTION_DONE);
                            login();
                        }
                        if (s.toString().isEmpty()) {
                            et_3.requestFocus();
                        }
                    }
                }, GlobalClass.mDelay);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        tv_forgot_mpin.setOnClickListener(v -> {
            Fragment fragment = new UserAuthenticationFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        });
    }

    private void login() {
     String   mpin = et_1.getText().toString() +
                et_2.getText().toString() +
                et_3.getText().toString() +
                et_4.getText().toString();
        if (mpin.length() == 4) {
            LoginMpin(sharedPreferences.getString("user_token", ""),mpin);
        } else {
            GlobalClass.ShowAlet(mContext, "Alert", "Please enter valid Mpin");
        }
    }

    private void LoginMpin(String token, String mpin) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("mpin",mpin);
        map.put("device_id", GlobalClass.mPrefix+GlobalClass.android_id +GlobalClass.mSufix);
        Log.d("device_id",GlobalClass.mPrefix+GlobalClass.android_id +GlobalClass.mSufix);
        Call<GeneralPojo> loginMpincall = GlobalClass.mApi.loginMpin(map);
        APIResponse.postCallRetrofit(loginMpincall, "loginMpin", mContext, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            if (apiCallName.equalsIgnoreCase("loginMpin")) {
                GeneralPojo generalPojo = (GeneralPojo) response.body();
                if (generalPojo.getStatus()) {
                    if (mBioAlertDialog != null && mBioAlertDialog.isShowing()) {
                        mBioAlertDialog.dismiss();
                        stopFingerAuth();
                    }

                    if (sharedPreferences.getString("guestUUID","").equals("")){
                        edit.putString("guestUUID",generalPojo.getData().getProfile().getGuestUUID());
                        edit.apply();
                        edit.commit();
                    }

                    if (sharedPreferences.getString("guestUUID","").equalsIgnoreCase(generalPojo.getData().getProfile().getGuestUUID())){
                        GlobalClass.user_registered = true;
                    }else{
                        GlobalClass.user_registered = false;
                        GlobalClass.edit.putBoolean("isRegestrationComplete",false);
                        GlobalClass.edit.apply();
                        GlobalClass.edit.commit();
                        edit.clear();
                        edit.apply();
                    }

                    GlobalClass.mUser_token=generalPojo.getData().getProfile().getToken();
                    generalPojo.getData().getLocation_key();
                    Intent intent = new Intent();
                    intent.setClassName(mContext, "com.taj.doorunlock.activity.BookingDetailsListActivity");
                    intent.putExtra("fName",generalPojo.getData().getProfile().getFirstname());
                    intent.putExtra("locktype", generalPojo.getData().getLockType());
                    intent.putExtra("lockey", generalPojo.getData().getLocation_key());

                    startActivity(intent);
                    getActivity().finish();
                } else {
                    GlobalClass.ShowAlet(mContext, "Alert", generalPojo.getMessage());
                    try {
                        initiateFingerprintlistner();
                        et_1.setText("");
                        et_2.setText("");
                        et_3.setText("");
                        et_4.setText("");
                        new Handler().postDelayed(() -> et_1.requestFocus(), GlobalClass.mDelay);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {
        GlobalClass.ShowAlet(mContext, "Alert","Invalid login PIN, Please try again");

    }

    private void generateKey() throws FingerprintException {
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");

            mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            mKeyStore.load(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mKeyGenerator.init(new
                        KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(
                                KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
            }

            mKeyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }


    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean initCipher() {
        try {
            mCipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME,
                    null);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    public void onSucessfullBiometricAuth() {
        LoginMpin(GlobalClass.sharedPreferences.getString("user_token", ""), "");
    }

    private class FingerprintException extends Exception {

        public FingerprintException(Exception e) {
            super(e);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            stopFingerAuth();

            if (mBioAlertDialog != null && mBioAlertDialog.isShowing()) {
                mBioAlertDialog.dismiss();
            }
           if(GlobalClass.alert!=null && GlobalClass.alert.isShowing()){
               GlobalClass.alert.dismiss();
           }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopFingerAuth() {
        if (mCancellationSignal != null && !mCancellationSignal.isCanceled()) {
            mCancellationSignal.cancel();
        }
    }

    private void showBio() {
        try {
            TextView btn_cancel;

            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(mContext).inflate(R.layout.mpin_biometric_overlay, mViewGroup, false);

            //Now we need an AlertDialog.Builder object
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.NewDialog);

            //setting the view of the builder to our custom view that we already inflated
            builder.setView(dialogView);
            // mBioAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            //finally creating the alert dialog and displaying it
            mBioAlertDialog = builder.create();
            btn_cancel = dialogView.findViewById(R.id.btn_cancel);

            btn_cancel.setOnClickListener(v -> {
                mBioAlertDialog.dismiss();
                m_lyt.setAlpha(1);
                stopFingerAuth();
            });
            mBioAlertDialog.show();

            mBioAlertDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }
}
