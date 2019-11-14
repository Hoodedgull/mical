package sensorapi.micapi

import kotlin.random.Random

class MicUsedImpl : MicAlerterInterface {
    override fun isMicBeingUsed(): IsMicInUseResult {
        val chance = Random.nextInt(0,100);
        return if (chance > 20){
            IsMicInUseResult(false, "");
        } else if (chance > 15){
            IsMicInUseResult(true,"Skype")}
        else{
            IsMicInUseResult(true, "YouTube")
        }
    }
}