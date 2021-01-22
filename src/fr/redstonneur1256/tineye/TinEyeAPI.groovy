package fr.redstonneur1256.tineye

import fr.redstonneur1256.redutilities.async.Task
import org.jetbrains.annotations.NotNull

interface TinEyeAPI {

    void shutdown();

    @NotNull Task<SearchResult> searchImage(@NotNull String url);

    @NotNull Task<Integer> getRemainingSearches();

}