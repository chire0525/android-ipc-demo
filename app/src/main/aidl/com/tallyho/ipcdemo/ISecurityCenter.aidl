// ISecurityCenter.aidl
package com.tallyho.ipcdemo;

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}