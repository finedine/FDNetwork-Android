package com.bambulabs.fdnetwork

class FDNetworkCompanions {
    companion object {

        var fdNetworkApplication : FDNetworkApplication? = null
        var authenticationToken: String? = null
        var isStagingMode: Boolean = false
        var isTestModeEnabled: Boolean = false
        var baseUrl = "https://api.finedinemenu.com/"
        var panelUrl = "https://panel.finedinemenu.com/"


        fun setTestMode(boolean: Boolean){
            isTestModeEnabled = boolean
        }

        fun setStagingActive(boolean: Boolean){
            if (boolean) {

                baseUrl = "https://api-staging.finedinemenu.com/"
                panelUrl = "http://fdmetronic.herokuapp.com/"

                // baseUrl = "http://192.168.1.52:8080/"
                // panelUrl = "http://192.168.1.52:5000/"

                isStagingMode = true

            } else {

                baseUrl = "https://api.finedinemenu.com/"
                panelUrl = "https://panel.finedinemenu.com/"

                // baseUrl = "http://192.168.1.52:8080/"
                // panelUrl = "http://192.168.1.52:5000/"

                isStagingMode = false

            }

        }

    }
}