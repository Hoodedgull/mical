package sensorapi.micapi

interface MicAlerterInterface {

    //Should return a micBeingUsed object
    fun isMicBeingUsed() = IsMicInUseResult(false, "noApp");
}