package com.tripasfactory.thetripaslibrary.Interfaces;

public interface CertificateValidationInterface {
    void onAccept();
    void onTimeout();
    void onDeny();
}
