package ai.chiyo.compendium.core.element.data.time

data class Duration(var type: DurationType, var timeData: TimeData, var concentration: Boolean) {
    constructor(type: DurationType, duration: Int, timeUnit: TimeUnit, concentration: Boolean)
            : this(type, TimeData(duration, timeUnit), concentration)
}
