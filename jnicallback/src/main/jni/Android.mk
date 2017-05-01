LOCAL_PATH := $(call my-dir)

# Build shared library
include $(CLEAR_VARS)
LOCAL_MODULE := callback
LOCAL_SRC_FILES := main.cpp

# Include header files
#LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)