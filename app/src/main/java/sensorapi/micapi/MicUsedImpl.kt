package sensorapi.micapi

import kotlin.random.Random

class MicUsedImpl : MicAlerterInterface {
    override fun isMicBeingUsed(): IsMicInUseResult {
        val chance = Random.nextInt(0,100);
        if (chance > 20){
            return IsMicInUseResult(false, "");
        } else {
            return IsMicInUseResult(true, "Skype")
        }
    }
}