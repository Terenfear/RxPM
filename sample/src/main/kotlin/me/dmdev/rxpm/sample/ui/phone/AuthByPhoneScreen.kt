package me.dmdev.rxpm.sample.ui.phone

import android.view.inputmethod.EditorInfo
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.enabled
import com.jakewharton.rxbinding2.widget.editorActions
import io.reactivex.Observable
import kotlinx.android.synthetic.main.screen_auth_by_phone.*
import me.dmdev.rxpm.sample.App
import me.dmdev.rxpm.sample.R
import me.dmdev.rxpm.sample.extensions.showKeyboard
import me.dmdev.rxpm.sample.ui.base.Screen
import me.dmdev.rxpm.sample.util.Country


class AuthByPhoneScreen : Screen<AuthByPhonePm>() {

    override val screenLayout = R.layout.screen_auth_by_phone

    override fun providePresentationModel()
            = AuthByPhonePm(App.component.phoneUtil,
                            App.component.resourceProvider,
                            App.component.authModel)

    override fun onBindPresentationModel(pm: AuthByPhonePm) {
        super.onBindPresentationModel(pm)
        pm.countryCode bindTo editCountryCodeLayout
        pm.phoneNumber bindTo editPhoneNumberLayout
        pm.chosenCountry.observable.bindTo {
            countryName.text = it.name
        }

        pm.inProgress.observable bindTo progressConsumer
        pm.doneButtonEnabled.observable bindTo doneButton.enabled()

        pm.phoneNumberFocus.observable.bindTo { phoneNumberEdit.requestFocus() }

        countryName.clicks().bindTo(pm.countryClicks.consumer)

        Observable
                .merge(
                        doneButton.clicks(),

                        phoneNumberEdit.editorActions()
                                .filter { it == EditorInfo.IME_ACTION_SEND }
                                .map { Unit }
                )
                .bindTo(pm.doneAction.consumer)

    }

    fun onCountryChosen(country: Country) {
        presentationModel.chooseCountryAction.consumer.accept(country)
    }

    override fun onResume() {
        super.onResume()
        phoneNumberEdit.showKeyboard()
    }

}