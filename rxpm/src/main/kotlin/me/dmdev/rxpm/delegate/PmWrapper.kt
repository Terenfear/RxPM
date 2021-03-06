package me.dmdev.rxpm.delegate

import me.dmdev.rxpm.PresentationModel
import me.dmdev.rxpm.PresentationModel.Lifecycle
import me.jeevuz.outlast.Outlasting


internal class PmWrapper<out PM : PresentationModel>(val presentationModel: PM) : Outlasting {

    override fun onCreate() {
        presentationModel.lifecycleConsumer.accept(Lifecycle.CREATED)
    }

    override fun onDestroy() {
        presentationModel.lifecycleConsumer.accept(Lifecycle.DESTROYED)
    }
}