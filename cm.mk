$(call inherit-product, device/samsung/sltechn/sltetdzc.mk)

# Enhanced NFC
$(call inherit-product, vendor/cm/config/nfc_enhanced.mk)

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

PRODUCT_NAME := cm_sltechn
PRODUCT_DEVICE := sltechn

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_NAME=sltetdzc \
    PRODUCT_DEVICE=sltechn \
    TARGET_DEVICE=sltechn \
    PRODUCT_MODEL=SM-G8508S \
    BUILD_FINGERPRINT=samsung/sltetdzc/sltechn:4.4.4/KTU84P/G8508SZCU1BOI1:user/release-keys \
    PRIVATE_BUILD_DESC="sltetdzc-user 4.4.4 KTU84P G8508SZCU1BOI1 release-keys"
