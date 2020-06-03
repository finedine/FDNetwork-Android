package com.bambulabs.fdnetwork

class FDNetworkCompanions {
    companion object {

        var authenticationToken: String? = null

        var baseUrl = "https://api.finedinemenu.com/v1/"
        var panelUrl = "https://panel.finedinemenu.com/"

        fun setStagingActive(boolean: Boolean){
            if (boolean) {

                baseUrl = "https://finedine-api-staging.herokuapp.com/v1"
                panelUrl = "http://fdmetronic.herokuapp.com/"

            } else {

                baseUrl = "https://api.finedinemenu.com/v1/"
                panelUrl = "https://panel.finedinemenu.com/"

            }

        }

    }
}