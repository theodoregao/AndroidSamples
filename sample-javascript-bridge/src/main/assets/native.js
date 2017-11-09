function log() {
    native.log("from js");
}

function fromNative(message) {
    native.log("from native() " + message);
    return "return value";
}