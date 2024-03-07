#!/bin/sh
JNI_LIBS=../app/libs
LIB_NAME=libtrianapp

# shellcheck disable=SC2164
cd common
cargo build --target aarch64-linux-android --release
cargo build --target armv7-linux-androideabi --release
cargo build --target i686-linux-android --release

rm -rf $JNI_LIBS
mkdir $JNI_LIBS
mkdir $JNI_LIBS/arm64-v8a
mkdir $JNI_LIBS/armeabi-v7a
mkdir $JNI_LIBS/x86

cp target/aarch64-linux-android/release/$LIB_NAME.so $JNI_LIBS/arm64-v8a/$LIB_NAME.so
cp target/armv7-linux-androideabi/release/$LIB_NAME.so $JNI_LIBS/armeabi-v7a/$LIB_NAME.so
cp target/i686-linux-android/release/$LIB_NAME.so $JNI_LIBS/x86/$LIB_NAME.so

cargo run --bin uniffi-bindgen generate --library target/aarch64-linux-android/release/$LIB_NAME.so --language kotlin --out-dir ../app/src/main/java
