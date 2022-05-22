package dev.mlukas.langreader.statistics;

import dev.mlukas.langreader.language.Language;

public record LanguageStatistics(
        Language language,
        int knownCount,
        int studiedCount,
        int ignoredCount
) {}
