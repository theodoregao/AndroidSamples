#include <string.h>
#include <jni.h>
#include <inttypes.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>
#include <dlfcn.h>
#include <pthread.h>
#include <unistd.h>
#include "test_shun_gao_jnicallback_Native.h"

#define LOGI(...) \
  ((void)__android_log_print(ANDROID_LOG_INFO, "Main::", __VA_ARGS__))

#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, "Main::", __VA_ARGS__))


static jclass callbacksClass;
static jobject callbacksInstance;
int value = 0;
JavaVM* jvm;

extern "C" JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM* vm, void* /*reserved*/)
{
    LOGI("onload");
    jvm = vm;
    return JNI_VERSION_1_6;
}

void *call_from_thread(void *) {
    LOGI("call_from_thread");
    JNIEnv *env;
    jint rs = jvm->AttachCurrentThread(&env, NULL);
    while (true) {
        sleep(1);
        value++;
        if (callbacksInstance != NULL) {
            jmethodID onValueReceived = env->GetMethodID(callbacksClass, "onValueReceived", "(I)V");
            env->CallVoidMethod(callbacksInstance, onValueReceived, (jint) value);
        }
    }
    return NULL;
}

JNIEXPORT void JNICALL Java_test_shun_gao_jnicallback_Native_registerCallback (JNIEnv *env, jclass, jobject callbacks) {
    LOGI("register");
    pthread_t t;
    pthread_create(&t, NULL, call_from_thread, NULL);

    callbacksInstance = env->NewGlobalRef(callbacks);
    jclass objClass = env->GetObjectClass(callbacks);
    if (objClass) {
        callbacksClass = reinterpret_cast<jclass>(env->NewGlobalRef(objClass));
        env->DeleteLocalRef(objClass);
    }
}