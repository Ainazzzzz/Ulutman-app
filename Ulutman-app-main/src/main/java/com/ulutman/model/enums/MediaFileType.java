package com.ulutman.model.enums;

public enum MediaFileType {
    /** Изображения объявления → publishes/{uuid}.ext */
    PUBLISH_IMAGE,
    /** Изображение рекламы → ads/images/{userId}/{uuid}.ext */
    AD_IMAGE,
    /** Чек оплаты рекламы → ads/receipts/{userId}/{uuid}.ext */
    AD_RECEIPT,
    /** Аватар пользователя → avatars/{userId}/{uuid}.ext */
    AVATAR
}
