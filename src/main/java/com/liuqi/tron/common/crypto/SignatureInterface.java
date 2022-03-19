package com.liuqi.tron.common.crypto;

public interface SignatureInterface {
    boolean validateComponents();

    byte[] toByteArray();
}