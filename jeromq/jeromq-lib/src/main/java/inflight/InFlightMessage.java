package inflight;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shun on 9/2/16.
 */

public class InFlightMessage {
    private static final String TAG = InFlightMessage.class.getSimpleName();

    private static final String PAYLOAD_TYPE = "type";
    private static final String PAYLOAD_VERSION = "version";
    private static final String PAYLOAD_METHOD_NAME = "methodname";
    private static final String PAYLOAD_SOURCE = "source";
    private static final String PAYLOAD_TARGET = "target";
    private static final String PAYLOAD_ARGS = "args";

    private String mType;
    private String mVersion;
    private String mMethodName;
    private String mSource;
    private String mTarget;
    private JSONObject mArgs;

    public InFlightMessage() {
        mType = mVersion = mMethodName = mSource = mTarget = "";
        mArgs = null;
    }

    public InFlightMessage(JSONObject json) {
        mType = json.optString(PAYLOAD_TYPE);
        mVersion = json.optString(PAYLOAD_VERSION);
        mMethodName = json.optString(PAYLOAD_METHOD_NAME);
        mSource = json.optString(PAYLOAD_SOURCE);
        mTarget = json.optString(PAYLOAD_TARGET);
        mArgs = json.optJSONObject(PAYLOAD_ARGS);
    }

    protected InFlightMessageType getType() {
        return InFlightMessageType.getMessageType(mType);
    }

    public String getVersion() {
        return mVersion;
    }

    public String getMethodName() {
        return mMethodName;
    }

    public String getSource() {
        return mSource;
    }

    public String getTarget() {
        return mTarget;
    }

    public JSONObject getArgs() {
        return mArgs;
    }

    protected void setType(InFlightMessageType inFlightMessageType) {
        this.mType = inFlightMessageType.toString();
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public void setMethodName(String methodName) {
        this.mMethodName = methodName;
    }

    public void setSource(String source) {
        this.mSource = source;
    }

    public void setTarget(String target) {
        this.mTarget = target;
    }

    public void setArgs(JSONObject args) {
        this.mArgs = args;
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put(PAYLOAD_TYPE, mType);
            json.put(PAYLOAD_VERSION, mVersion);
            json.put(PAYLOAD_METHOD_NAME, mMethodName);
            json.put(PAYLOAD_SOURCE, mSource);
            json.put(PAYLOAD_TARGET, mTarget);
            json.put(PAYLOAD_ARGS, mArgs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static InFlightMessage toInFlightMessage(JSONObject json) {
        Log.v(TAG, json.toString());
        InFlightMessage inFlightMessage = new InFlightMessage(json);
        switch (inFlightMessage.getType()) {
            case REQUEST:
                return new MessageRequest(json);

            case PROPERTY_SYNC:
                return new MessagePropertySync(json);

            case EVENT:
                return new MessageEvent(json);

            case UNKNOWN:
            default: return inFlightMessage;
        }
    }
}
