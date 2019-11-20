package sensorapi.micapi

import kotlin.random.Random

class MicUsedImpl : MicAlerterInterface {
    override fun isMicBeingUsed(): IsMicInUseResult {
        val chance = Random.nextInt(0,100);
        var a = listOf("Facebook", "YouTube", "Skype", "Google", "Messenger", "RandomApp", "Hello my friends", "Don't Look hahahha", ":-)")
        a = a.shuffled()
        if (chance > 80){
            return IsMicInUseResult(false, "");
        } else {
            return IsMicInUseResult(true, a.first())
        }
    }
}