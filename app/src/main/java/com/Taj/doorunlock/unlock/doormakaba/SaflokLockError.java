package com.taj.doorunlock.unlock.doormakaba;


import com.taj.doorunlock.R;

/**
 *
 */
public enum SaflokLockError {
    Error113(113, R.string.error_113),
    Error117(117, R.string.error_117),
    Error122(122, R.string.error_122),
    Error123(123, R.string.error_123),
    Error127(127, R.string.error_127),
    Error128(128, R.string.error_128),
    Error129(129, R.string.error_129),
    Error134(134, R.string.error_134),
    Error135(135, R.string.error_135),
    Error136(136, R.string.error_136),
    Error137(137, R.string.error_137),
    Error141(141, R.string.error_141),
    Error149(149, R.string.error_149),
    Error150(150, R.string.error_150),
    Error152(152, R.string.error_152),
    Error154(154, R.string.error_154),
    Error178(178, R.string.error_178),
    Any_UnknownError(255, R.string.error_255);

    private final int code;
    private final int description;

    SaflokLockError(int code, int description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public int getDescription() {
        return description;
    }

    public static SaflokLockError fromCode(int code) {
        for (SaflokLockError error : values()) {
            if (error.getCode() == code) {
                return error;
            }
        }
        return Any_UnknownError;
    }
}