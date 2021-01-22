package fr.redstonneur1256.tineye

import fr.redstonneur1256.redutilities.Validate
import fr.redstonneur1256.tineye.impl.TinEyeAPIImpl
import org.jetbrains.annotations.NotNull

class TinEyeAPIFactory {

    @NotNull
    static TinEyeAPI create(@NotNull String privateKey, @NotNull String publicKey) {
        Validate.nonNull(privateKey, "privateKey")
        Validate.nonNull(publicKey, "publicKey")
        new TinEyeAPIImpl(privateKey, publicKey)
    }

    @NotNull
    static TinEyeAPI create(@NotNull String privateKey, @NotNull String publicKey, @NotNull String apiURL) {
        Validate.nonNull(privateKey, "privateKey")
        Validate.nonNull(publicKey, "publicKey")
        Validate.nonNull(apiURL, "apiURL")
        new TinEyeAPIImpl(privateKey, publicKey, apiURL)
    }

}
